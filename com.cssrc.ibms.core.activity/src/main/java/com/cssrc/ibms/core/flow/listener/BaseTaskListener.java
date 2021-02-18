package com.cssrc.ibms.core.flow.listener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.model.NodeScript;
import com.cssrc.ibms.core.flow.service.NodeScriptService;
import com.cssrc.ibms.core.flow.service.impl.NodeUserUtil;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 任务事件基类，采用了模版模式。
 * @author zhulongchao
 *
 */
public abstract class BaseTaskListener implements org.activiti.engine.delegate.TaskListener
{
    
    protected Logger logger = LoggerFactory.getLogger(BaseTaskListener.class);
    @Override
    public void notify(DelegateTask delegateTask)
    {
        
        logger.debug("enter the baseTaskListener notify method...");
        
        TaskEntity taskEnt = (TaskEntity)delegateTask;
        String nodeId = taskEnt.getExecution().getActivityId();
        String actDefId = taskEnt.getProcessDefinitionId();
        
        // 执行子类业务逻辑
        execute(delegateTask, actDefId, nodeId);
        // 获取脚本类型
        int scriptType = getScriptType();
        // 执行事件脚本
        exeEventScript(delegateTask, scriptType, actDefId, nodeId);
        
        this.handlerExtTaskExeCallBack(taskEnt);
        
    }
    
    /**
     * 执行子类业务逻辑
     * @param delegateTask
     * @param actDefId
     * @param nodeId
     */
    protected abstract void execute(DelegateTask delegateTask, String actDefId, String nodeId);
    
    /**
     * 获取脚本类型
     * @return
     */
    protected abstract int getScriptType();
    
    /**
     * 回调扩展函数
    * @return
    */
    protected abstract void handlerExtTaskExeCallBack(TaskEntity taskEnt);
    
    /**
     * 执行事件脚本
     * @param delegateTask
     * @param scriptType
     * @param actDefId
     * @param nodeId
     */
    private void exeEventScript(DelegateTask delegateTask, int scriptType, String actDefId, String nodeId)
    {
        logger.debug("enter the baseTaskListener exeEventScript method...");
        NodeScriptService bpmNodeScriptService = (NodeScriptService)AppUtil.getBean("nodeScriptService");
        
        NodeScript model = bpmNodeScriptService.getScriptByType(nodeId, actDefId, scriptType);
        if (model == null)
            return;
        
        String script = model.getScript();
        if (StringUtil.isEmpty(script))
            return;
        
        String instId = delegateTask.getProcessInstanceId();
        // 设置临时变量
        TaskThreadService.setTempLocal(instId);
        
        GroovyScriptEngine scriptEngine = (GroovyScriptEngine)AppUtil.getBean("scriptEngine");
        Map<String, Object> vars = delegateTask.getVariables();
        
        vars.put("task", delegateTask);
        ProcessCmd processCmd = TaskThreadService.getProcessCmd();
        vars.put("processCmd", processCmd);
        scriptEngine.execute(script, vars);
        // 恢复线程变量
        TaskThreadService.resetTempLocal(instId);
        
    }
    
    // protected List<String> getByTaskExecutor(List<TaskExecutor> executors){
    // Set<String> set=new LinkedHashSet<String>();
    // for(TaskExecutor executor:executors){
    // if(TaskExecutor.USER_TYPE_USER.equals(executor.getType())){
    // set.add(executor.getExecuteId());
    // }
    // else if(TaskExecutor.EXACT_NOEXACT==executor.getExactType()){
    //
    // }
    // else{
    // List<String> userList=getByTaskExecutor(executor);
    // set.addAll(userList);
    // }
    // }
    //
    // List<String> userList=new ArrayList<String>();
    // if(BeanUtils.isEmpty(set)) return userList;
    // userList.addAll(set);
    // return userList;
    // }
    
    /**
     * 获取执行人。
     * @param list
     * @return
     */
    protected Set<TaskExecutor> getByTaskExecutors(List<TaskExecutor> list)
    {
        Set<TaskExecutor> exSet = new LinkedHashSet<TaskExecutor>();
        for (TaskExecutor ex : list)
        {
            List<TaskExecutor> tmp = getByTaskExecutor(ex);
            
            exSet.addAll(tmp);
        }
        return exSet;
    }
    
    /**
     * 根据组执行人取得任务执行人。
     * @param taskExecutor
     * @return
     */
    protected List<TaskExecutor> getByTaskExecutor(TaskExecutor taskExecutor)
    {
        List<TaskExecutor> list = new ArrayList<TaskExecutor>();
        if (taskExecutor.getExactType() == TaskExecutor.EXACT_NOEXACT)
        {
            list.add(taskExecutor);
        }
        else
        {
            List<? extends ISysUser> userList = NodeUserUtil.getUserListByExecutor(taskExecutor);
            for (ISysUser sysUser : userList)
            {
                list.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getUsername()));
            }
        }
        return list;
    }
    
    protected String getNotAssignMessage(TaskExecutor taskExecutor)
    {
        String message = "{0}:【{1}】没有配置人员!";
        String type = "";
        if (TaskExecutor.USER_TYPE_ORG.equals(taskExecutor.getType()))
        {
            type = "部门";
        }
        else if (TaskExecutor.USER_TYPE_POS.equals(taskExecutor.getType()))
        {
            type = "岗位";
        }
        else if (TaskExecutor.USER_TYPE_ROLE.equals(taskExecutor.getType()))
        {
            type = "角色";
        }
        return StringUtil.formatParamMsg(message, type, taskExecutor.getExecutor()).toString();
    }
    
    /**
     * 是否允许代理。
     * <pre>
     * 1.如果跳过第一个节点，那么忽略第一个节点的代理设置。
     * 2.往下执行才允许代理，如果是驳回不允许代理。
     * </pre>
     * @return
     */
    protected boolean isAllowAgent()
    {
        // 驳回、追回
        ProcessCmd processCmd = TaskThreadService.getProcessCmd();
        
        if (BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack()) || BpmConst.TASK_BACK.equals(processCmd.isBack()))
        {
            return false;
        }
        
        // 流程启动,并跳过第一个节点
        Short toFirstNode = (Short)TaskThreadService.getToFirstNode();
        TaskThreadService.removeToFirstNode();
        if (toFirstNode != null && toFirstNode == 1)
        {
            return false;
        }
        
        return true;
    }
    
}
