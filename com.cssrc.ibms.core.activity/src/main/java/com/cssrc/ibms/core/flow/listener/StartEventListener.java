package com.cssrc.ibms.core.flow.listener;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssrc.ibms.api.activity.intf.ITaskThreadService;
import com.cssrc.ibms.api.activity.model.INodeSql;
import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.sysuser.intf.IEventUtilService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 开始事件监听器。
 * 
 * @author zhulongchao
 *
 */
public class StartEventListener extends BaseNodeEventListener
{
    
    /** 
    * @Fields serialVersionUID : TODO(序列化的ID标识) 
    */ 
    private static final long serialVersionUID = -375806263871450235L;
    

    private final static Log logger = LogFactory.getLog(StartEventListener.class);
    
    @Resource
    DefinitionDao bpmDefinitionDao;
    
    @Resource
    ProcessRunService processRunService;
    
    @Resource
    private NodeSetService bpmNodeSetService;
    
    @Resource
    IFormRunService bpmFormRunService;
    
    @Resource
    IFormDefService bpmFormDefService;
    
    @Resource
    IFormTableService formTableService;
    @Override
    protected void execute(DelegateExecution execution, String actDefId, String nodeId)
    {
        logger.debug("nodeId" + nodeId);
        handExtSubProcess(execution);
        
        //添加开始节点的sql监听
        ExecutionEntity ent = (ExecutionEntity)execution;
        //当前的excutionId和主线程相同时。
		if(ent.getId().equals(ent.getProcessInstanceId()) ){
			ITaskThreadService taskThreadService= AppUtil.getBean(ITaskThreadService.class);
			IProcessCmd processCmd = taskThreadService.getProcessCmdP();
			IEventUtilService eventUtilService= AppUtil.getBean(IEventUtilService.class);
			eventUtilService.publishNodeSqlEvent(ent.getProcessDefinitionId(), nodeId, INodeSql.ACTION_SUBMIT, (Map)processCmd.getTransientVar("mainData"));
		}
    }
    
    /**
     * 处理子流程。
     * 
     * <pre>
     * 1.如果流程变量当中有“innerPassVars”变量，那么这个是一个外部子流程的调用。
     * 2.创建processrun记录。
     * 3.准备流程实例id为初始化执行堆栈做准备。
     * 4.处理运行表单。
     * 5.通知任务执行人。
     * </pre>
     * 
     * @param execution
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private void handExtSubProcess(DelegateExecution execution)
    {
        
        ExecutionEntity ent = (ExecutionEntity)execution;
       
        // 非子流程调用直接返回。
        if (execution.getVariable(BpmConst.PROCESS_INNER_VARNAME) == null||ent.getSuperExecution()==null){
            return;
        }
        
        Map<String, Object> parentVar=ent.getSuperExecution().getVariables();
        
        Map<String, Object> variables = (Map<String, Object>)ent.getVariable(BpmConst.PROCESS_INNER_VARNAME);
        // 是否外部调用（这里指的是通过 webservice接口调用)
        Boolean isExtCall = (Boolean)variables.get(BpmConst.IS_EXTERNAL_CALL);
        // 获取流程实例
        String instanceId = ent.getProcessInstanceId();
        /*if(ent.getSuperExecution()!=null&&ent.getSuperExecution().getVariable(BpmConst.BACK_SUBFLOWNODE)!=null){
            //驳回子节点后续处理--目前是重新开启一个新的子流程实例，需要根据procesrun 和业务数据key修改 activiti流程相关数据
            instanceId=ent.getSuperExecution().getVariable(BpmConst.BACK_SUBFLOWINSTID).toString();
        }else{
            instanceId = ent.getProcessInstanceId();
        }*/
        //准备流程实例id为初始化执行堆栈做准备。
        TaskThreadService.addExtSubProcess(instanceId);
        
        String actDefId = ent.getProcessDefinitionId();
        // add by hhj
        Long parentRunId = (Long)parentVar.get(BpmConst.FLOW_RUNID);
        ProcessRun parentProcessRun = (ProcessRun)this.processRunService.getById(parentRunId);
        
        // 添加processRun。
        TaskExecutor taskExecutor=(TaskExecutor) ent.getSuperExecution().getVariable("assignee");
        long runId = initProcessRun(actDefId, instanceId, variables, parentProcessRun,taskExecutor);
        // 设置流程运行变量
        execution.setVariable(BpmConst.FLOW_RUNID, runId);
        
        variables.put(BpmConst.FLOW_RUNID, runId);
        execution.setVariables(variables);
        
        // 处理运行时表单。
        if (isExtCall != null && !isExtCall)
        {
            bpmFormRunService.addFormRun(actDefId, runId, instanceId);
        }
        //如果是驳回到子流程，需要重新处理数据
        //backSubFlow(ent.getProcessInstanceId(),instanceId);
    }
    
    /**
     * 插入流程运行记录。
     * 
     * @param actDefId
     * @param instanceId
     * @param variables
     */
    private Long initProcessRun(String actDefId, String instanceId, Map<String, Object> variables,
        ProcessRun parentProcessRun,TaskExecutor taskExecutor)
    {
        // add by hhj
        String parentActDefId = (String)variables.get(BpmConst.FLOW_PARENT_ACTDEFID);
        Definition bpmDefinition = bpmDefinitionDao.getByActDefId(actDefId);
        ProcessRun processRun = new ProcessRun();
        NodeSet bpmNodeSet =
            this.bpmNodeSetService.getStartBpmNodeSet(bpmDefinition.getDefId(), actDefId, parentActDefId);
        if (bpmNodeSet == null)
        {
            String msg = "外部子流程【" + bpmDefinition.getSubject() + "】未设置表单!";
            MessageUtil.addMsg(msg);
            throw new RuntimeException(msg);
        }
        
        String businessKey = (String)variables.get(BpmConst.FLOW_BUSINESSKEY);
        //Long parentRunId = (Long)variables.get(BpmConst.FLOW_RUNID);
        // 获取外部流程的form表单id
        if(bpmNodeSet.getFormKey()!=null){
            IFormDef bpmFormDef = bpmFormDefService.getDefaultPublishedByFormKey(bpmNodeSet.getFormKey());
            IFormTable formTable=formTableService.getById(bpmFormDef.getTableId());
            String pkName = formTable.isExtTable() ? formTable
                .getPkField() : ITableModel.PK_COLUMN_NAME;
                processRun.setFormDefId(bpmFormDef.getFormDefId());
                processRun.setPkName(pkName);
                processRun.setDsAlias(formTable.getDsAlias());
                processRun.setTableName(formTable.getTableName());
                processRun.setBusDescp(bpmFormDef.getFormDesc());

        }
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        if (curUser != null)
        {
            processRun.setCreator(curUser.getFullname());
            processRun.setCreatorId(curUser.getUserId());
        }
        else
        {
            processRun.setCreator("系统");
            processRun.setCreatorId(SystemConst.SYSTEMUSERID);
        }
        
        if (Definition.STATUS_TEST.equals(bpmDefinition.getStatus()))
        {
            processRun.setIsFormal(ProcessRun.TEST_RUNNING);
        }
        else
        {
            processRun.setIsFormal(ProcessRun.FORMAL_RUNNING);
        }
        
        processRun.setActDefId(bpmDefinition.getActDefId());
        processRun.setDefId(bpmDefinition.getDefId());
        processRun.setProcessName(bpmDefinition.getSubject());
        
        processRun.setActInstId(instanceId);
        
        String subject = (String)variables.get(BpmConst.FLOW_RUN_SUBJECT);
        processRun.setSubject(subject);
        processRun.setFlowKey(bpmDefinition.getActDefKey());
        //这里的buskey 应该是子流程对应的buskey--需要修改
        if(StringUtil.isNotEmpty(taskExecutor.getBusinessKey())){
            processRun.setBusinessKey(taskExecutor.getBusinessKey());
            processRun.setRootBusinessKey(businessKey);
        }else{
            processRun.setRootBusinessKey(businessKey);
            processRun.setBusinessKey(businessKey);
        }
        //processRun.setBusinessKey(businessKey);
        processRun.setCreatetime(new Date());
        processRun.setStatus(ProcessRun.STATUS_RUNNING);
        if (Definition.STATUS_TEST.equals(bpmDefinition.getStatus())) {
            processRun.setIsFormal(ProcessRun.TEST_RUNNING);
        } else {
            processRun.setIsFormal(ProcessRun.FORMAL_RUNNING);
        }
        processRun.setParentId(parentProcessRun.getRunId());
        processRun.setTypeId(bpmDefinition.getTypeId());
        
        if(taskExecutor.getBackSub()) {
            ProcessRun run=processRunService.getByBusinessKey(taskExecutor.getBusinessKey());
            run.setEndTime(null);
            run.setStatus(ProcessRun.STATUS_RUNNING);
            processRun.setRunId(run.getRunId());
            processRun.setCreatetime(run.getCreatetime());
            processRunService.getDao().add(processRun);
            processRunService.getDao().updateHistory(run);

        }else {
            processRun.setRunId(UniqueIdUtil.genId());
            processRunService.add(processRun);

        }
        

        
        
        return processRun.getRunId();
    }
    
    /** 
     * 以前 获取外部子流程表单时需要判断和主流程是否同一个表单，不是同一个则跑出异常，
     * 目前修改成子流程可以拥有自己的表单
    * @Title: getExtSubProcessFormKey 
    * @Description: TODO(获取外部流程的form表单id) 
    * @param @param formKey
    * @param @param parentFormDefId
    * @param @param subject
    * @param @return    设定文件 
    * @return Long    返回类型 
    * @throws 
    */
    private Long getExtSubProcessFormKey(Long formKey, Long parentFormDefId, String subject)
    {
        IFormDef bpmFormDef = bpmFormDefService.getDefaultPublishedByFormKey(formKey);
        Long formDefId = bpmFormDef.getFormDefId();
        if (formDefId.equals(parentFormDefId))
        {
            return formDefId;
        }
        IFormDef parentBpmFormDef = bpmFormDefService.getById(parentFormDefId);
        if (bpmFormDef.getTableId().equals(parentBpmFormDef.getTableId()))
        {
            return formDefId;
        }
        String msg = "外部子流程【" + subject + "】设置的表单的主表与父流程的必须相同!";
        MessageUtil.addMsg(msg);
        throw new RuntimeException(msg);
    }
    
    @Override
    protected Integer getScriptType()
    {
        
        return BpmConst.StartScript;
    }
    
}
