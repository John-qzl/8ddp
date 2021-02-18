package com.cssrc.ibms.core.flow.listener;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.model.IFlowNode;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.core.activity.event.ProcessEndEvent;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 结束事件监听器。
 * @author zhulongchao
 *
 */
@Service
public class EndEventListener extends BaseNodeEventListener
{
    
    @Resource
    ProcessRunService processRunService;
    
    @Resource
    private INodeSetService nodeSetService;
    
    @Resource
    private IFormHandlerService formHandlerService;
    
    @Override
    protected void execute(DelegateExecution execution, String actDefId, String nodeId)
    {
        ExecutionEntity ent = (ExecutionEntity)execution;
        if (!ent.isEnded())
            return;
        
        // 当前的excutionId和主线程相同时s。
        if (ent.getId().equals(ent.getProcessInstanceId()))
        {
            handEnd(ent);
            ProcessRun processRun = this.processRunService.getByActInstanceId(new Long(ent.getProcessInstanceId()));
            // 流程结束，修改流程控件字段信息
            handProcessEndStateValue(execution, actDefId, nodeId);
            ProcessEndEvent ev = new ProcessEndEvent(processRun);
            ev.setExecutionEntity(ent);
            AppUtil.publishEvent(ev);
        }
    }
    
    private void handEnd(ExecutionEntity ent)
    {
        if (ent.getParentId() == null)
        {
            // 更新流程实例状态。
            updProcessRunStatus(ent);
            // 删除知会任务。
            delNotifyTask(ent);
            
            // 删除流程变量
            // ExecutionDao executionDao=(ExecutionDao)AppUtil.getBean("executionDao");
            // executionDao.delVariableByProcInstId(new Long( ent.getId()));
            // executionDao.delSubExecutionByProcInstId(new Long( ent.getId()));
            
            // PROC_INST_ID_
        }
        
    }
    
    /**
    * 流程结束，修改流程控件字段信息
    * @param excution
    * @param actDefId
    * @param nodeId
    */
    private void handProcessEndStateValue(DelegateExecution excution, String actDefId, String nodeId)
    {
        Object businessKey = excution.getVariables().get(BpmConst.FLOW_BUSINESSKEY);
        ExecutionEntity executionEnt = (ExecutionEntity)excution;
        if (executionEnt.getSuperExecution() != null)
        {
            Object executor = executionEnt.getSuperExecution().getVariable("assignee");
            if (executor != null)
            {
                TaskExecutor taskExecutor = (TaskExecutor)executor;
                if (StringUtils.isNotEmpty(taskExecutor.getBusinessKey()))
                {
                    businessKey = taskExecutor.getBusinessKey();
                }
            }
        }
        FlowNode flowNode = NodeCache.getNodeByActNodeId(actDefId, nodeId);
        String preNodeId = getPreUserTaskNode(flowNode).getNodeId();
        INodeSet nodeSet = (INodeSet)nodeSetService.getByActDefIdNodeId(actDefId, preNodeId);
        if (businessKey != null)
        {
            formHandlerService.updateProcessData(IFieldPool.FLOW_END_KEY, businessKey.toString(), nodeSet.getFormKey());
        }
    }
    
    /**
     * 获取最近的上一个用户节点
     * @param node
     * @return
     */
    private FlowNode getPreUserTaskNode(FlowNode node)
    {
        FlowNode preFlowNode = node.getPreFlowNodes().get(0);
        if (!preFlowNode.getNodeType().equals(IFlowNode.TYPE_USERTASK))
        {
            preFlowNode = getPreUserTaskNode(preFlowNode);
        }
        return preFlowNode;
    }
    
    /**
     * 流程终止时删除流程任务。
     * <pre>
     * 	1.删除流程实例任务。
     *  2.删除任务的参与者。
     *  3.删除流程表单运行情况
     * </pre>
     * @param ent
     */
    private void delNotifyTask(ExecutionEntity ent)
    {
        Long instanceId = new Long(ent.getProcessInstanceId());
        TaskDao taskDao = (TaskDao)AppUtil.getBean("taskDao");
        // 删除任务参与人
        // taskDao.delCandidateByInstanceId(instanceId);
        // 删除知会任务
        taskDao.delCustTaskByInstId(instanceId);
        // 删除流程表单运行情况
        IFormRunService formRunService = (IFormRunService)AppUtil.getBean(IFormRunService.class);
        formRunService.delByInstanceId(String.valueOf(instanceId));
    }
    
    /**
     * 更新流程运行状态。
     * <pre>
     * 1.更新流程运行状态为完成。
     * 2.计算流程过程的时间。
     * </pre>
     * @param ent
     */
    private void updProcessRunStatus(ExecutionEntity ent)
    {
        // 设置线程变量。这个和nextProcess 中的updateStatus关联。
        TaskThreadService.setObject(ProcessRun.STATUS_FINISH);
        // 流程结束后，需要更新流程实例的状态
        ProcessRunService processRunService = (ProcessRunService)AppUtil.getBean("processRunService");
        ProcessRun processRun = processRunService.getByActInstanceId(new Long(ent.getProcessInstanceId()));
        if (BeanUtils.isEmpty(processRun))
            return;
        // 设置流程状态为完成。
        processRun.setStatus(ProcessRun.STATUS_FINISH);
        processRunService.update(processRun);
    }
    
    @Override
    protected Integer getScriptType()
    {
        
        return BpmConst.EndScript;
    }
    
}
