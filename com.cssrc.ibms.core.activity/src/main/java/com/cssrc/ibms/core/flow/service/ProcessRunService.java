package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.activity.ActivityRequiredException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.cfg.IdGenerator;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormModel;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IPkValue;
import com.cssrc.ibms.api.form.model.ISubTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.IEventUtilService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.ProcessExecution;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.activity.model.ProcessTaskHistory;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.service.IActivitiRunService;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.dao.CommuReceiverDao;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.dao.ExecutionDao;
import com.cssrc.ibms.core.flow.dao.HistoryActivityDao;
import com.cssrc.ibms.core.flow.dao.ProCopytoDao;
import com.cssrc.ibms.core.flow.dao.ProStatusDao;
import com.cssrc.ibms.core.flow.dao.ProcessRunDao;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.dao.TaskExeDao;
import com.cssrc.ibms.core.flow.dao.TaskForkDao;
import com.cssrc.ibms.core.flow.dao.TaskHistoryDao;
import com.cssrc.ibms.core.flow.dao.TaskOpinionDao;
import com.cssrc.ibms.core.flow.dao.TaskReadDao;
import com.cssrc.ibms.core.flow.intf.ISkipCondition;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.ExecutionStack;
import com.cssrc.ibms.core.flow.model.GangedSet;
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.NodeSql;
import com.cssrc.ibms.core.flow.model.ProCopyto;
import com.cssrc.ibms.core.flow.model.ProStatus;
import com.cssrc.ibms.core.flow.model.ProTransTo;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.model.TaskExe;
import com.cssrc.ibms.core.flow.model.TaskFork;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.model.TaskRead;
import com.cssrc.ibms.core.flow.model.TaskSignData;
import com.cssrc.ibms.core.flow.service.impl.ActService;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;
import com.cssrc.ibms.core.flow.service.thread.TaskUserAssignService;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

import net.sf.json.JSONArray;

/**
 * 对象功能:流程实例扩展Service类 开发人员:zhulongchao
 */
@Service
public class ProcessRunService extends BaseService<ProcessRun> implements IProcessRunService
{
    
    protected Logger log = LoggerFactory.getLogger(ProcessRunService.class);
    
    @Resource
    private ProcessRunDao dao;
    
    @Resource
    private DefinitionDao definitionDao;
    
    @Resource
    private BpmService bpmService;
    
    @Resource
    private IEventUtilService eventUtilService;
    
    @Resource
    private TaskSignDataService taskSignDataService;
    
    @Resource
    private IFormHandlerService formHandlerService;
    
    @Resource
    private NodeSetService bpmNodeSetService;
    
    @Resource
    private TaskService taskService;
    
    @Resource
    private RuntimeService runtimeService;
    
    @Resource
    private TaskUserAssignService taskUserAssignService;
    
    @Resource
    private TaskUserService taskUserService;
    
    @Resource
    private TaskOpinionDao taskOpinionDao;
    
    @Resource
    private IDataTemplateService dataTemplateService;
    
    @Resource
    private IFormRunService formRunService;
    
    @Resource
    private TaskDao taskDao;
    
    @Resource
    private RunLogService runLogService;
    
    @Resource
    private ISysTemplateService sysTemplateService;
    
    @Resource
    private ExecutionDao executionDao;
    
    @Resource
    private IFormDefService formDefService;
    
    @Resource
    private IFormTableService formTableService;
    
    @Resource
    private CommuReceiverService commuReceiverService;
    
    @Resource
    private TaskHistoryDao taskHistoryDao;
    
    @Resource
    private TaskExeService taskExeService;
    
    @Resource
    private HistoryActivityDao historyActivityDao;
    
    @Resource
    private TaskOpinionService taskOpinionService;
    
    @Resource
    private ProCopytoService proCopytoService;
    
    @Resource
    private TaskExeDao taskExeDao;
    
    @Resource
    private CommuReceiverDao commuReceiverDao;
    
    @Resource
    private TaskReadDao taskReadDao;
    
    @Resource
    private ProCopytoDao proCopytoDao;
    
    @Resource
    private ProTransToService proTransToService;
    
    /** 流程跳转规则 */
    @Resource
    private JumpRule jumpRule;
    
    @Resource
    private ActService actService;
    
    @Resource
    private ExecutionStackService executionStackService;
    
    @Resource
    private Properties configproperties;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private TaskMessageService taskMessageService;
    
    @Resource
    IdGenerator idGenerator;
    
    @Resource
    ProStatusDao proStatusDao;
    
    @Resource
    private IFormFieldService formFieldService;
    
    @Resource
    private ISerialNumberService serialNumberService;
    
    @Resource
    private TaskForkDao taskForkDao;
    
    @Resource
    private DefinitionService definitionService;
    
    @Resource
    private ProcessRunService processRunService;
    
    @Resource
    private NodeButtonService nodeButtonService;
    
    @Resource
    private NodeSetService nodeSetService;
    
    @Resource
    private ISysFileService sysFileService;
    
    @Resource
    private GangedSetService gangedSetService;
    
    @Resource
    IActivitiRunService activitiRunService;
    
    @Resource
    JdbcDao jdbcDao;
    
    public ProcessRunService()
    {
    }
    
    @Override
    protected IEntityDao<ProcessRun, Long> getEntityDao()
    {
        return dao;
    }
    
    /**
     * 若下一任务分发任务节点，则指定下一任务的执行人员
     * 
     * @param processCmd
     */
    private void setThreadTaskUser(ProcessCmd processCmd)
    {
        // 回退不设置用户。
        if (processCmd.isBack().intValue() == 0)
        {
            String[] nodeIds = processCmd.getLastDestTaskIds();
            String[] nodeUserIds = processCmd.getLastDestTaskUids();
            // 设置下一步产生任务的执行人员
            if (nodeIds != null && nodeUserIds != null)
            {
                taskUserAssignService.addNodeUser(nodeIds, nodeUserIds);
            }
        }
        // 设置了任务执行人。
        if (processCmd.getTaskExecutors().size() > 0)
        {
            taskUserAssignService.setExecutors(processCmd.getTaskExecutors());
        }
        TaskThreadService.setProcessCmd(processCmd);
    }
    
    /**
     * 设置流程变量。
     * 
     * @param taskId
     * @param processCmd
     */
    private void setVariables(String taskId, ProcessCmd processCmd)
    {
        if (StringUtil.isEmpty(taskId))
        {
            return;
        }
        Map<String, Object> vars = processCmd.getVariables();
        if (BeanUtils.isNotEmpty(vars))
        {
            for (Iterator<Entry<String, Object>> it = vars.entrySet().iterator(); it.hasNext();)
            {
                Entry<String, Object> obj = it.next();
                taskService.setVariable(taskId, obj.getKey(), obj.getValue());
            }
        }
        Map<String, Object> formVars = taskService.getVariables(taskId);
        formVars.put(BpmConst.IS_EXTERNAL_CALL, processCmd.isInvokeExternal());
        formVars.put(BpmConst.FLOW_RUN_SUBJECT, processCmd.getSubject());
        // 设置线程变量。
        TaskThreadService.setVariables(formVars);
    }
    
    /**
     * 获取任务实例
     * 
     * @param processCmd
     * @return
     */
    private TaskEntity getTaskEntByCmd(ProcessCmd processCmd)
    {
        TaskEntity taskEntity = null;
        String taskId = processCmd.getTaskId();
        Long runId = processCmd.getRunId();
        // 根据任务id获取任务
        if (StringUtil.isNotEmpty(taskId))
        {
            taskEntity = bpmService.getTask(taskId);
        }
        // 如果任务实例为空，则根据runid再次获取。
        if (taskEntity == null && runId != null && runId > 0)
        {
            ProcessRun processRun = this.getById(runId);
            if (processRun == null)
                return null;
            String instanceId = processRun.getActInstId();
            ProcessTask processTask = bpmService.getFirstNodeTask(instanceId);
            if (processTask == null)
                return null;
            taskEntity = bpmService.getTask(processTask.getId());
            
        }
        return taskEntity;
    }
    
    /**
     * 流程启动下一步
     * 
     * @param processCmd 流程执行命令实体。
     * @param isSaveForm 是否要保存表单
     * @return ProcessRun 流程实例
     * @throws Exception
     */
    public ProcessRun nextProcess(ProcessCmd processCmd, boolean isSaveForm)
        throws Exception
    {
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        ProcessRun processRun = null;
        String taskId = "";
        TaskEntity taskEntity = getTaskEntByCmd(processCmd);
        if (taskEntity == null)
        {
            return null;
        }
        String nodeId = taskEntity.getTaskDefinitionKey();
        taskId = taskEntity.getId();
        
        String taskStatus = taskEntity.getDescription();
        // 通知任务直接返回
        if (taskEntity.getExecutionId() == null && TaskOpinion.STATUS_COMMUNICATION.toString().equals(taskStatus))
        {
            return null;
        }
        if (TaskOpinion.STATUS_TRANSTO_ING.toString().equals(taskStatus))
        {
            handleInterveneTransTo(taskId);
        }
        
        // 当下一节点为条件同步节点时，可以指定执行路径
        Object nextPathObj = processCmd.getFormDataMap().get("nextPathId");
        if (nextPathObj != null)
        {
            bpmService.setTaskVariable(taskId, "NextPathId", nextPathObj.toString());
        }
        String parentNodeId = taskEntity.getTaskDefinitionKey();
        String actDefId = taskEntity.getProcessDefinitionId();
        String instanceId = taskEntity.getProcessInstanceId();
        String executionId = taskEntity.getExecutionId();
        Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
        processRun = dao.getByActInstanceId(new Long(instanceId));
        
        processCmd.addTransientVar("bpm_definition", bpmDefinition);
        processCmd.setProcessRun(processRun);
        
        // 该任务是否为别人交办任务
        try
        {
            // 取到当前任务是否带有分发令牌
            String taskToken = (String)taskService.getVariableLocal(taskId, TaskFork.TAKEN_VAR_NAME);
            // 注意getVariableLocal和getVariable区别
            String parentActDefId = (String)this.taskService.getVariable(taskId, "parentActDefId");
            NodeSet bpmNodeSet = null;
            if (StringUtil.isEmpty(parentActDefId))
                bpmNodeSet = this.bpmNodeSetService.getByActDefIdNodeId(actDefId, parentNodeId);
            else
            {
                bpmNodeSet = this.bpmNodeSetService.getByActDefIdNodeId(actDefId, parentNodeId, parentActDefId);
            }
            
            // 回填字段加入流程审批意见实体中
            getOpinionField(processCmd, bpmDefinition.getDefId(), parentActDefId);
            // 设置下一步包括分发任务的用户
            setThreadTaskUser(processCmd);
            
            /** 处理在线表单的业务数据 */
            // 判断是否要保存表单
            if (isSaveForm)
            {
                handFormData(processCmd, bpmNodeSet, processRun, parentNodeId);
            }
            
            // 调用前置处理器
            if (!processCmd.isSkipPreHandler())
            {
                invokeHandler(processCmd, bpmNodeSet, true);
            }
            
            /*
             * 流程回退处理的前置处理，查找该任务节点其回退的堆栈树父节点，以便其跳转时，能跳回该节点上 此处应该注意下 如果流程设置了驳回节点，则不需要根据流程任务执行堆栈来查找退回/驳回的目标节点，
             * processCmd.getDestTask()为设置的跳转目标节点 所以 ExecutionStack对象会是空的 processCmd.setDestTask("UserTask1");
             */
            ExecutionStack parentStack = backPrepare(processCmd, taskEntity, taskToken);
            
            if (parentStack != null)
            {
                parentNodeId = parentStack.getNodeId();
            }
            // 设置会签用户或处理会签意见
            signUsersOrSignVoted(processCmd, taskEntity);
            // 设置流程标题。
            processCmd.setSubject(processRun.getSubject());
            // 设置流程变量。
            setVariables(taskId, processCmd);
            
            // 若是自跳转方式
            if (NodeSet.JUMP_TYPE_SELF.equals(processCmd.getJumpType()))
            {
                // 让他跳回本节点
                processCmd.setDestTask(taskEntity.getTaskDefinitionKey());
            }
            // 如果是干预添加干预审批数据
            addInterVene(processCmd, taskEntity);
            // 如果是追回，需要覆盖一下审批意见
            if (processRun.getStatus().shortValue() == ProcessRun.STATUS_RECOVER)
            {
                addRetrOrRecoverOpinion(processCmd, taskEntity, true);
            }
            // 是否仅完成当前任务(完成当前任务，创建下个任务)
            completeTask(processCmd, taskEntity, bpmNodeSet.getIsJumpForDef());
            
            // 调用后置处理器这里
            if (!processCmd.isSkipAfterHandler())
            {
                invokeHandler(processCmd, bpmNodeSet, false);
            }
            // 如果在流程运行主键为空，并且在processCmd不为空的情况下，我们更新流程流程运行的主键。
            if (StringUtil.isEmpty(processRun.getBusinessKey()) && StringUtil.isNotEmpty(processCmd.getBusinessKey()))
            {
                processRun.setBusinessKey(processCmd.getBusinessKey());
                // 设置流程主键变量。
                runtimeService.setVariable(executionId, BpmConst.FLOW_BUSINESSKEY, processCmd.getBusinessKey());
            }
            
            if (processCmd.isBack() > 0 && parentStack != null)
            {// 任务回退时，弹出历史执行记录的堆栈树节点
                executionStackService.pop(parentStack, processCmd.isRecover(), processCmd.isBack());
            }
            else
            {
                // 记录执行执行的堆栈
                List<String> map = TaskThreadService.getExtSubProcess();
                if (BeanUtils.isEmpty(map))
                {
                    executionStackService.addStack(instanceId, parentNodeId, taskToken);
                }
                else
                {
                    // 初始化外部子流程。
                    initExtSubProcessStack();
                }
            }
            
            /*
             * 为了解决在任务自由跳转或回退时，若流程实例有多个相同Key的任务，会把相同的任务删除。 与TaskCompleteListner里的以下代码对应使用 if(processCmd!=null &&
             * (processCmd.isBack()>0 || StringUtils.isNotEmpty(processCmd.getDestTask()))){
             * taskDao.updateNewTaskDefKeyByInstIdNodeId (delegateTask.getTaskDefinitionKey() +
             * "_1",delegateTask.getTaskDefinitionKey (),delegateTask.getProcessInstanceId()); }
             */
            if (processCmd.isBack() > 0 || StringUtils.isNotEmpty(processCmd.getDestTask()))
            {
                // 更新其相对对应的key
                taskDao.updateOldTaskDefKeyByInstIdNodeId(taskEntity.getTaskDefinitionKey() + "_1",
                    taskEntity.getTaskDefinitionKey(),
                    taskEntity.getProcessInstanceId());
            }
            
            List<Task> taskList = TaskThreadService.getNewTasks();
            
            // 处理信息
            String informType = processCmd.getInformType();
            // 发送 站内消息 短信 邮件 通知
            taskMessageService
                .notify(TaskThreadService.getNewTasks(), informType, processRun.getSubject(), null, "", parentActDefId);
            // 处理代理
            handleAgentTaskExe(processCmd);
            // 记录流程执行日志。
            recordLog(processCmd, taskEntity.getName(), processRun);
            
            // 任务完成
            taskExeService.complete(new Long(taskId));
            // 修改流程任务的状态。
            if (BeanUtils.isNotEmpty(taskList))
            {
                if (((Task)taskList.get(0)).getProcessDefinitionId().equals(processRun.getActDefId()))
                {
                    updateStatus(processCmd, processRun);
                }
            }
            
            // 修改流程任务的状态。这个状态在EndEventListener.updProcessRunStatus中进行设置，防止在结束的时候，再次更新状态。
            if (TaskThreadService.getObject() == null)
            {
                updateStatus(processCmd, processRun);
            }
            else
            {
                // 获取流程变量
                Map<String, Object> vars = TaskThreadService.getVariables();
                // 添加抄送任务以及发送提醒
                proCopytoService
                    .handlerCopyTask(processRun, vars, processCmd.getCurrentUserId().toString(), bpmDefinition);
                // 处理发送提醒消息给发起人
                if (StringUtil.isNotEmpty(bpmDefinition.getInformStart()))
                {
                    handSendMsgToStartUser(processRun, bpmDefinition);
                }
            }
            
            // 获取当前节点的动作
            String action = getActionByCmdVoteAgree(processCmd.getVoteAgree());
            if (StringUtil.isNotEmpty(action))
            {
                // 处理节点sql事件
                eventUtilService
                    .publishNodeSqlEvent(actDefId, nodeId, action, (Map)processCmd.getTransientVar("mainData"));
                
                eventUtilService.publishTriggerNewFlowEvent(action, nodeId, processCmd);
            }
            // 节点跳过设定--yangbo
            handAutoJump(bpmDefinition, processCmd, isSaveForm);
            // 正常往下执行,如果下一步的执行人和当前人相同那么直接往下执行。
            handSameExecutorJump(taskList, bpmDefinition, processCmd, false);
            
            // 驳回子流程回调函数
            callBack(processCmd);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
        finally
        {
            // 清空在线程中绑定的用户等信息.
            clearThreadLocal();
        }
        
        return processRun;
    }
    
    public void callBack(ProcessCmd processCmd)
    {
        if (processCmd.isBackToExtSub())
        {
            List<String> extSubProcess = TaskThreadService.getExtSubProcess();
            String newInstId = extSubProcess.get(0);
            String oldInstId = processCmd.getVariables().get(BpmConst.BACK_SUBFLOWINSTID).toString();
            if (!oldInstId.equals(newInstId))
            {
                proBackToSubFlow(oldInstId, newInstId);
            }
            List<String> extSubTask = TaskThreadService.getExtSubTask();
            String subTaskNode = this.bpmService.getTask(extSubTask.get(extSubTask.size()-1)).getTaskDefinitionKey();
            String toNode = processCmd.getVariables().get(BpmConst.BACK_SUBFLOWNODE).toString();
            toNode = toNode.split("\\.")[1];
            if (!subTaskNode.equals(toNode))
            {
                try
                {
                    Map<String, Object> vars = processCmd.getVariables();
                    taskService.setVariables(extSubTask.get(0), vars);
                    this.bpmService.transTo(extSubTask.get(0), toNode);
                }
                catch (ActivityRequiredException e)
                {
                    e.printStackTrace();
                }
            }
        }
        TaskThreadService.cleanExtSubTask();
    }
    
    public void proBackToSubFlow(String oldInstId, String newInstId)
    {
        List<Map<String,Object>> params=new ArrayList();
        Map<String, Object> map=new HashMap<>();
        List<String> batchSql = new ArrayList<>();
        StringBuffer sql = new StringBuffer("INSERT INTO act_ru_execution(ID_,");
        sql.append("REV_,");
        sql.append("PROC_INST_ID_,");
        sql.append("BUSINESS_KEY_,");
        sql.append("PARENT_ID_,");
        sql.append("PROC_DEF_ID_,");
        sql.append("SUPER_EXEC_,");
        sql.append("ACT_ID_,");
        sql.append("IS_ACTIVE_,");
        sql.append("IS_CONCURRENT_,");
        sql.append("IS_SCOPE_,");
        sql.append("IS_EVENT_SCOPE_,");
        sql.append("SUSPENSION_STATE_,");
        sql.append("CACHED_ENT_STATE_,");
        sql.append("TENANT_ID_");
        sql.append(") SELECT " + oldInstId + ",REV_,");
        sql.append(oldInstId + ",");
        sql.append("BUSINESS_KEY_,");
        sql.append("PARENT_ID_,");
        sql.append("PROC_DEF_ID_,");
        sql.append("SUPER_EXEC_,");
        sql.append("ACT_ID_,");
        sql.append("IS_ACTIVE_,");
        sql.append("IS_CONCURRENT_,");
        sql.append("IS_SCOPE_,");
        sql.append("IS_EVENT_SCOPE_,");
        sql.append("SUSPENSION_STATE_,");
        sql.append("CACHED_ENT_STATE_,");
        sql.append("TENANT_ID_ FROM ");
        sql.append("act_ru_execution WHERE ID_=:newInstId");
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // 修改 task 流程实例ID 为 oldinstId
        sql = new StringBuffer("UPDATE act_ru_task SET EXECUTION_ID_=:oldInstId ,PROC_INST_ID_=:oldInstId WHERE PROC_INST_ID_=:newInstId");
        map=new HashMap<>();
        map.put("oldInstId", oldInstId);
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 修改 原有的流程 运行变量
        sql = new StringBuffer("UPDATE act_ru_variable SET EXECUTION_ID_=:oldInstId,PROC_INST_ID_=:oldInstId WHERE PROC_INST_ID_=:newInstId");
        map=new HashMap<>();
        map.put("oldInstId", oldInstId);
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 删除 act_hi_procinst
        sql = new StringBuffer("DELETE FROM act_hi_procinst WHERE ID_=:newInstId");
        map=new HashMap<>();
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 删除 act_hi_varinst
        sql = new StringBuffer("DELETE FROM act_hi_varinst WHERE PROC_INST_ID_=:newInstId");
        map=new HashMap<>();
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 修改 act_ru_identitylink
        sql = new StringBuffer("UPDATE act_ru_identitylink SET PROC_INST_ID_=:oldInstId WHERE PROC_INST_ID_=:newInstId");
        map=new HashMap<>();
        map.put("oldInstId", oldInstId);
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 删除 act_hi_taskinst
        sql = new StringBuffer("DELETE FROM act_hi_taskinst WHERE PROC_INST_ID_=:newInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 删除 act_hi_identitylink
        sql = new StringBuffer("DELETE FROM act_hi_identitylink WHERE PROC_INST_ID_=:newInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 修改流程实例ID为原来已有的实例ID
        sql = new StringBuffer(" DELETE FROM act_ru_execution  WHERE ID_=:newInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        // -- 删除 ibms_pro_run
        sql = new StringBuffer(" DELETE  FROM ibms_pro_run_his WHERE ACTINSTID=:newInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        sql = new StringBuffer("UPDATE ibms_pro_run_his SET status=1 WHERE ACTINSTID=:oldInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("oldInstId", oldInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        sql = new StringBuffer("UPDATE ibms_pro_run SET ACTINSTID=:oldInstId,status=1 WHERE ACTINSTID=:newInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("oldInstId", oldInstId);
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);

        sql =new StringBuffer("UPDATE  ibms_task_opinion SET ACTINSTID=:oldInstId WHERE ACTINSTID=:newInstId");
        batchSql.add(sql.toString());
        map=new HashMap<>();
        map.put("oldInstId", oldInstId);
        map.put("newInstId", newInstId);
        batchSql.add(sql.toString());
        params.add(map);
        jdbcDao.exesql(sql.toString(), map);
        
    }
    
    /**
     * 获取当前节点人员的操作动作
     * 
     * @author liubo
     * @param sot
     * @return
     */
    private String getActionByCmdVoteAgree(Short sot)
    {
        if ((sot.shortValue() == 1) || (sot.shortValue() == 5))
            return NodeSql.ACTION_AGREE;
        if ((sot.shortValue() == 2) || (sot.shortValue() == 6))
            return NodeSql.ACTION_OPPOSITE;
        if (sot.shortValue() == 3)
            return NodeSql.ACTION_REJECT;
        return null;
    }
    
    // nextProcess中的 处理在线表单的业务数据
    private void handFormData(ProcessCmd processCmd, NodeSet bpmNodeSet, ProcessRun processRun, String parentNodeId)
        throws Exception
    {
        if (processCmd.isInvokeExternal())
        {
            return;
        }
        IFormData bpmFormData = handlerFormData(processCmd, processRun, parentNodeId);
        // 将记录意见放到ProcessCmd中。
        if (bpmFormData != null)
        {
            Map<String, String> optionsMap = bpmFormData.getOptions();
            setOpinionByForm(optionsMap, processCmd);
        }
        
    }
    
    private void addRetrOrRecoverOpinion(ProcessCmd processCmd, TaskEntity taskEntity, boolean isRecover)
    {
        String opinion = processCmd.getVoteContent();
        Long opinionId = Long.valueOf(UniqueIdUtil.genId());
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setOpinionId(opinionId);
        taskOpinion.setOpinion(opinion);
        taskOpinion.setTaskId(Long.valueOf(taskEntity.getId()));
        taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
        taskOpinion.setActInstId(taskEntity.getProcessInstanceId());
        taskOpinion.setStartTime(new Date());
        taskOpinion.setEndTime(new Date());
        taskOpinion.setExeUserId(sysUser.getUserId());
        taskOpinion.setExeFullname(sysUser.getFullname());
        taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
        taskOpinion.setTaskName(taskEntity.getName());
        if (isRecover)
            taskOpinion.setCheckStatus(TaskOpinion.STATUS_RESUBMIT);
        else
        {
            taskOpinion.setCheckStatus(TaskOpinion.STATUS_AGREE);
        }
        
        dealTaskOpinSupId(taskEntity, taskOpinion);
        
        this.taskOpinionDao.add(taskOpinion);
    }
    
    /**
     * 流程实体添加回填意见字段
     * 
     * @param processCmd
     * @param defId
     * @param parentActDefId当前的流程节点id
     */
    private void getOpinionField(ProcessCmd processCmd, Long defId, String parentActDefId)
    {
        ArrayList<String> list = new ArrayList<String>();
        List<NodeSet> bpmNodeSets = this.bpmNodeSetService.getByDefIdOpinion(defId, parentActDefId);
        // 添加当前任务节点回填字段
        for (NodeSet bpmNodeSet : bpmNodeSets)
        {
            list.add(bpmNodeSet.getOpinionField());
        }
        processCmd.addTransientVar("opinionFields", list);
    }
    
    /**
     * 完成任务跳转。
     * 
     * @param processCmd ProcessCmd对象
     * @param taskEntity 流程任务实例
     * @param isJumpForDef 规则不作用时，是否正常跳转(1,正常跳转,0,不跳转)
     * @throws ActivityRequiredException
     */
    private void completeTask(ProcessCmd processCmd, TaskEntity taskEntity, Short isJumpForDef)
        throws ActivityRequiredException
    {
        String taskId = taskEntity.getId();
        // processCmd.setDestTask("UserTask3")
        if (processCmd.isOnlyCompleteTask())
        {
            bpmService.onlyCompleteTask(taskId);
        }
        else if (StringUtils.isNotEmpty(processCmd.getDestTask()))
        {
            // 自由跳转或回退
            bpmService.transTo(taskId, processCmd.getDestTask());
        }
        else
        { // 正常流程跳转
            ExecutionEntity execution = actService.getExecution(taskEntity.getExecutionId());
            // 从规则中获取跳转
            String jumpTo = jumpRule.evaluate(execution, isJumpForDef);
            bpmService.transTo(taskId, jumpTo);
        }
    }
    
    /**
     * 节点跳转(之前流程同意的自动审批)处理 2016-6-28
     * 
     * @param definition
     * @param processCmd
     * @throws Exception
     */
    private void handAutoJump(Definition definition, ProcessCmd processCmd, boolean isSaveForm)
        throws Exception
    {
        List<Task> taskList = TaskThreadService.getNewTasks();
        if ((BeanUtils.isEmpty(taskList)) || (processCmd.getIsManage().shortValue() != 0))
        {
            return;
        }
        if (processCmd.isBack().intValue() != 0)
        {
            return;
        }
        // 清除执行人
        this.taskUserAssignService.clearExecutors();
        while (taskList.size() > 0)
        {
            TaskEntity task = (TaskEntity)taskList.get(0);
            taskList.remove(0);
            // 获得流程定义中节点跳过设置
            if (!processCmd.getProcessRun().getActInstId().equals(task.getProcessInstanceId()))
            {
                continue;
            }
            Definition d = definitionService.getByActDefId(task.getProcessDefinitionId());
            String skipSetting = "";
            if (d != null)
            {
                skipSetting = d.getSkipSetting();
            }
            if (StringUtil.isEmpty(skipSetting))
            {
                continue;
            }
            
            boolean rtn = canSkip(skipSetting, task, processCmd);
            if (!rtn)
            {
                continue;
            }
            skipTask(task, processCmd, isSaveForm);
        }
    }
    
    /**
     * 流程执行命令实体节点跳过参数设置 2016-6-28
     * 
     * @param task
     * @param processCmd
     * @param isSaveForm 是否要保存表单
     * @throws Exception
     */
    private void skipTask(Task task, ProcessCmd processCmd, boolean isSaveForm)
        throws Exception
    {
        processCmd.setSkip(true);
        processCmd.setTaskId(task.getId());
        processCmd.setVoteAgree(TaskOpinion.STATUS_AGREE);
        processCmd.setDestTask("");
        nextProcess(processCmd, isSaveForm);
        processCmd.setSkip(false);
    }
    
    /**
     * 判断节点是否跳过设置 2016-6-28
     * 
     * @param skipSetting
     * @param task
     * @param processCmd
     * @return
     */
    private boolean canSkip(String skipSetting, Task task, ProcessCmd processCmd)
    {
        Map<String, ISkipCondition> map = (Map)AppUtil.getBean("skipConditionMap");
        // 流程定义中有跳过设定的加入流程命令实体processCmd中
        if (skipSetting.indexOf("global") != -1)
        {
            ISkipCondition condition = (ISkipCondition)map.get("global");
            processCmd.addTransientVar("skipCondition", condition);
            return true;
        }
        
        String[] arySkip = skipSetting.split(",");
        for (String skip : arySkip)
        {
            ISkipCondition condition = (ISkipCondition)map.get(skip);
            boolean rtn = condition.canSkip(task);
            if (rtn)
            {
                processCmd.addTransientVar("skipCondition", condition);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 遇到相同用户跳转
     * 
     * @param taskList
     * @param bpmDefinition
     * @param processCmd
     * @param isSaveForm 是否要保存表单
     * @throws Exception
     */
    private void handSameExecutorJump(List<Task> taskList, Definition bpmDefinition, ProcessCmd processCmd,
        boolean isSaveForm)
        throws Exception
    {
        if (processCmd.isBack().intValue() == 0 && bpmDefinition.getSameExecutorJump() == 1)
        {
            // 管理员不能连续执行。
            if (BeanUtils.isNotEmpty(taskList) && processCmd.getIsManage() == 0)
            {
                Task task = taskList.get(0);
                TaskThreadService.clearNewTasks();
                // 清除执行用户
                taskUserAssignService.clearExecutors();
                if (UserContextUtil.getCurrentUserId().toString().equals(task.getAssignee()))
                {
                    processCmd.setTaskId(task.getId());
                    processCmd.setVoteAgree(TaskOpinion.STATUS_AGREE);
                    nextProcess(processCmd, isSaveForm);
                }
            }
        }
    }
    
    /**
     * 处理发送提醒消息给发起人
     * 
     * @throws Exception
     */
    private void handSendMsgToStartUser(ProcessRun processRun, Definition bpmDefinition)
        throws Exception
    {
        String informStart = bpmDefinition.getInformStart();
        if (StringUtil.isEmpty(informStart))
            return;
        
        String subject = processRun.getSubject();
        if (BeanUtils.isEmpty(processRun))
            return;
        Long startUserId = processRun.getCreatorId();
        ISysUser user = sysUserService.getById(startUserId);
        List<ISysUser> receiverUserList = new ArrayList<ISysUser>();
        receiverUserList.add(user);
        
        Map<String, String> msgTempMap = sysTemplateService.getTempByFun(ISysTemplate.USE_TYPE_NOTIFY_STARTUSER);
        taskMessageService
            .sendMessage(null, receiverUserList, informStart, msgTempMap, subject, "", null, processRun.getRunId());
    }
    
    /**
     * 更新状态
     * 
     * @param cmd
     * @param processRun
     */
    private void updateStatus(ProcessCmd cmd, ProcessRun processRun)
    {
        boolean isRecover = cmd.isRecover();
        boolean isRedo = cmd.isRedo();
        
        int isBack = cmd.isBack();
        Short status = ProcessRun.STATUS_RUNNING;
        switch (isBack)
        {
            // 正常
            case 0:
                status = ProcessRun.STATUS_RUNNING;
                break;
            // 驳回（撤销)
            case 1:
                if (isRecover)
                {
                    // 已撤销
                    status = ProcessRun.STATUS_RECOVER;
                }
                else if (isRedo)
                {
                    // 已追回
                    status = ProcessRun.STATUS_REDO;
                }
                else
                    status = ProcessRun.STATUS_REJECT;
                break;
            // 驳回到发起人（撤销)
            case 2:
                if (isRecover)
                {
                    status = ProcessRun.STATUS_RECOVER;
                }
                else
                {
                    status = ProcessRun.STATUS_REJECT;
                }
                break;
        }
        processRun.setStatus(status);
        this.update(processRun);
        
    }
    
    /**
     * 处理转办或代理
     * 
     * @param taskList
     * @param cmd
     * @throws Exception
     */
    private void handleAgentTaskExe(ProcessCmd cmd)
        throws Exception
    {
        List<Task> taskList = TaskThreadService.getNewTasks();
        if (BeanUtils.isEmpty(taskList))
            return;
        for (Task taskEntity : taskList)
        {
            String actDefId = taskEntity.getProcessDefinitionId();
            String nodeId = taskEntity.getTaskDefinitionKey();
            Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
            NodeSet bpmNodeSet = bpmNodeSetService.getByActDefIdNodeId(actDefId, nodeId);
            String informType = cmd.getInformType();
            if (StringUtil.isEmpty(informType))
            {
                if (!Definition.STATUS_TEST.equals(bpmDefinition.getStatus())
                    && StringUtil.isNotEmpty(bpmNodeSet.getInformType()))
                {
                    informType = bpmNodeSet.getInformType();
                }
                else
                {
                    informType = bpmDefinition.getInformType();
                }
                cmd.setInformType(informType);
            }
            if (!TaskOpinion.STATUS_AGENT.toString().equals(taskEntity.getDescription()))
                continue;
            
            String assigeeId = taskEntity.getAssignee();
            ISysUser auth = sysUserService.getById(Long.valueOf(taskEntity.getOwner()));
            ISysUser agent = sysUserService.getById(Long.valueOf(assigeeId));
            addAgentTaskExe(taskEntity, cmd, auth, agent);
        }
    }
    
    /**
     * 添加代理数据。
     * 
     * @param task 任务实例
     * @param cmd ProcessCmd对象
     * @param auth 授权人
     * @param agent 代理人
     * @throws Exception
     */
    private void addAgentTaskExe(Task task, ProcessCmd cmd, ISysUser auth, ISysUser agent)
        throws Exception
    {
        ProcessRun processRun = cmd.getProcessRun();
        if (processRun == null)
        {
            processRun = this.getByActInstanceId(new Long(task.getProcessInstanceId()));
        }
        String informType = cmd.getInformType();
        
        String memo =
            getText("service.processrun.addAgentTaskExe.memo", new Object[] {auth.getFullname(), agent.getFullname()});
        String processSubject = processRun.getSubject();
        
        TaskExe bpmTaskExe = new TaskExe();
        bpmTaskExe.setId(UniqueIdUtil.genId());
        bpmTaskExe.setTaskId(new Long(task.getId()));
        bpmTaskExe.setAssigneeId(agent.getUserId());
        bpmTaskExe.setAssigneeName(agent.getFullname());
        bpmTaskExe.setOwnerId(auth.getUserId());
        bpmTaskExe.setOwnerName(auth.getFullname());
        bpmTaskExe.setSubject(processSubject);
        bpmTaskExe.setStatus(TaskExe.STATUS_INIT);
        bpmTaskExe.setMemo(memo);
        bpmTaskExe.setCratetime(new Date());
        bpmTaskExe.setActInstId(new Long(task.getProcessInstanceId()));
        bpmTaskExe.setTaskDefKey(task.getTaskDefinitionKey());
        bpmTaskExe.setTaskName(task.getName());
        bpmTaskExe.setAssignType(TaskExe.TYPE_ASSIGNEE);
        bpmTaskExe.setRunId(processRun.getRunId());
        bpmTaskExe.setTypeId(processRun.getTypeId());
        bpmTaskExe.setInformType(informType);
        taskExeService.assignSave(bpmTaskExe);
    }
    
    /**
     * 初始化子流程任务堆栈。
     * 
     * <pre>
     * 子流程处理当作新的流程进行处理，进行初始化处理。
     * </pre>
     */
    private void initExtSubProcessStack()
    {
        List<String> list = TaskThreadService.getExtSubProcess();
        if (BeanUtils.isEmpty(list))
            return;
        List<Task> taskList = TaskThreadService.getNewTasks();
        Map<String, List<Task>> map = getMapByTaskList(taskList);
        for (String instanceId : list)
        {
            List<Task> tmpList = map.get(instanceId);
            executionStackService.initStack(instanceId, tmpList);
        }
    }
    
    private Map<String, List<Task>> getMapByTaskList(List<Task> taskList)
    {
        Map<String, List<Task>> map = new HashMap<String, List<Task>>();
        for (Task task : taskList)
        {
            String instanceId = task.getProcessInstanceId();
            if (map.containsKey(instanceId))
            {
                map.get(instanceId).add(task);
            }
            else
            {
                List<Task> list = new ArrayList<Task>();
                list.add(task);
                map.put(instanceId, list);
            }
        }
        return map;
    }
    
    private void clearThreadLocal()
    {
        // 清空线程中空任务
        TaskThreadService.clearAll();
        TaskUserAssignService.clearAll();
    }
    
    /**
     * 保存沟通或流转意见，并删除任务。 设置流转的转办代理事宜为完成 删除被流转任务产生的沟通任务
     * 
     * @param taskEntity
     * @param taskOpinion
     */
    public void saveOpinion(TaskEntity taskEntity, TaskOpinion taskOpinion)
    {
        String taskId = taskEntity.getId();
        String description = taskEntity.getDescription();
        dealTaskOpinSupId(taskEntity, taskOpinion);
        taskOpinionDao.add(taskOpinion);
        taskService.deleteTask(taskId);
        if (description.equals(TaskOpinion.STATUS_TRANSTO_ING.toString()))
            handleInterveneTransTo(taskId);
        else if (description.equals(TaskOpinion.STATUS_TRANSTO.toString()))
        {// 删除被流转任务产生的沟通任务
            this.taskDao.delCommuTaskByParentTaskId(taskId);
        }
        
    }
    
    private void handleInterveneTransTo(String taskId)
    {
        proTransToService.delByTaskId(new Long(taskId));
        
        taskDao.delCommuTaskByParentTaskId(taskId);
        proTransToService.cancelTransToTaskCascade(taskId);
    }
    
    /**
     * 流程操作日志记录
     * 
     * @param processCmd
     * @throws Exception
     */
    private void recordLog(ProcessCmd processCmd, String taskName, ProcessRun processRun)
        throws Exception
    {
        String memo = "";
        Integer type = -1;
        Object[] args = {taskName};
        // 追回
        if (processCmd.isRecover())
        {
            type = RunLog.OPERATOR_TYPE_RETRIEVE;
            memo = getText("service.processrun.recordLog.retrieve", args);
        }
        // 驳回
        else if (BpmConst.TASK_BACK.equals(processCmd.isBack()))
        {
            type = RunLog.OPERATOR_TYPE_REJECT;
            memo = getText("service.processrun.recordLog.reject", args);
        }
        // 驳回到发起人
        else if (BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack()))
        {
            type = RunLog.OPERATOR_TYPE_REJECT2SPONSOR;
            memo = getText("service.processrun.recordLog.reject2sponsor", args);
        }
        else
        {
            // 同意
            if (TaskOpinion.STATUS_AGREE.equals(processCmd.getVoteAgree()))
            {
                type = RunLog.OPERATOR_TYPE_AGREE;
                memo = getText("service.processrun.recordLog.agree", args);
            }
            // 反对
            else if (TaskOpinion.STATUS_REFUSE.equals(processCmd.getVoteAgree()))
            {
                type = RunLog.OPERATOR_TYPE_OBJECTION;
                memo = getText("service.processrun.recordLog.objection", args);
            }
            // 弃权
            else if (TaskOpinion.STATUS_ABANDON.equals(processCmd.getVoteAgree()))
            {
                type = RunLog.OPERATOR_TYPE_ABSTENTION;
                memo = getText("service.processrun.recordLog.abstention", args);
            }
            // 更改执行路径
            if (TaskOpinion.STATUS_CHANGEPATH.equals(processCmd.getVoteAgree()))
            {
                type = RunLog.OPERATOR_TYPE_CHANGEPATH;
                memo = getText("service.processrun.recordLog.changepath", args);
            }
        }
        if (type == -1)
            return;
        
        runLogService.addRunLog(processRun, type, memo);
    }
    
    /**
     * 回退准备。
     * 
     * @param processCmd
     * @param taskEntity
     * @param taskToken
     * @param backToNodeId
     * @return
     * @throws Exception
     */
    private ExecutionStack backPrepare(ProcessCmd processCmd, TaskEntity taskEntity, String taskToken)
        throws Exception
    {
        List<TaskOpinion> taskOpinions = null;
        String instanceId = taskEntity.getProcessInstanceId();
        if (StringUtil.isNotEmpty(processCmd.getDestTask()))
        {
            String[] toNode = processCmd.getDestTask().split("\\.", -1);
            if (toNode.length == 3 && !processCmd.isBackToExtSub())
            {
                processCmd.setDestTask(toNode[1]);
            }
            taskOpinions =
                taskOpinionService.getByActInstIdTaskKey(Long.parseLong(instanceId), processCmd.getDestTask());
        }
        if (processCmd.isBack() == 0 && BeanUtils.isEmpty(taskOpinions) && !processCmd.isBackToExtSub())
        {
            return null;
        }
        String aceDefId = taskEntity.getProcessDefinitionId();
        
        String backToNodeId = processCmd.getStartNode();//
        if (StringUtil.isEmpty(backToNodeId))
        {
            backToNodeId = NodeCache.getFirstNodeId(aceDefId).getNodeId();
        }
        ExecutionStack parentStack = null;
        // 驳回
        if (processCmd.isBack().equals(BpmConst.TASK_BACK) || BeanUtils.isNotEmpty(taskOpinions))
        {
            processCmd.setBack(BpmConst.TASK_BACK);
            processCmd.setVoteAgree((short)-1);
            // 根据流程定义Id以及流程节点id 获取 流程节点 配置属性 当前活动 nodeSet对象
            NodeSet curNodeSet =
                this.bpmNodeSetService.getNodeSetByActDefIdNodeId(aceDefId, taskEntity.getTaskDefinitionKey());
            if (StringUtil.isEmpty(processCmd.getDestTask()) && StringUtil.isNotEmpty(curNodeSet.getBackNode()))
            {
                // 如果流程节点表单中配置了驳回指定路径，那么驳回时只能跳转到该路径
                processCmd.setDestTask(curNodeSet.getBackNode());
                return null;
            }
            else
            {
                parentStack = executionStackService.backPrepared(processCmd, taskEntity, taskToken);
            }
        }
        // 驳回到发起人
        else if (processCmd.isBack() == BpmConst.TASK_BACK_TOSTART.intValue())
        {
            // 获取发起节点。
            parentStack = executionStackService.getLastestStack(instanceId, backToNodeId, null);
            if (parentStack != null)
            {
                processCmd.setDestTask(parentStack.getNodeId());
                taskUserAssignService.addNodeUser(parentStack.getNodeId(), parentStack.getAssignees());
            }
        }
        else if (processCmd.isBackToExtSub())
        {
            try
            {
                // 驳回到子流程。
                String subbusinessKey = processCmd.getSubbusinessKey();
                // 需要重新启动子流程实例后然后运行到子流程步骤节点
                String destTask = processCmd.getDestTask();
                String[] destTasks = destTask.split("\\.");
                String flowKey = destTasks[0];
                Definition definition = this.definitionDao.getByDefKey(flowKey);
                NodeSet globalForm = nodeSetService.getBySetType(definition.getDefId(), NodeSet.SetType_GloabalForm);
                ProcessRun run = this.processRunService.getByBusinessKey(globalForm.getFormKey(), subbusinessKey);
                
                processCmd.setDestTask(destTasks[destTasks.length - 1]);
                processCmd.addVariable(BpmConst.BACK_SUBFLOWNODE, destTask);
                processCmd.addVariable(BpmConst.SUBFLOW_BUSINESSKEY, subbusinessKey);
                processCmd.addVariable(BpmConst.BACK_SUBFLOWINSTID, run.getActInstId());
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
            
        }
        return parentStack;
    }
    
    /**
     * 更新意见。
     * 
     * <pre>
     * 1.首先根据任务id查询意见，应为意见在task创建时这个意见就被产生出来，所以能直接获取到该意见。
     * 2.获取意见，注意一个节点只允许一个意见填写，不能在同一个任务节点上同时允许两个意见框的填写，比如同时科员意见，局长意见等。
     * 3.更新意见。
     * </pre>
     * 
     * @param optionsMap
     * @param taskId
     */
    private void setOpinionByForm(Map<String, String> optionsMap, ProcessCmd cmd)
    {
        if (BeanUtils.isEmpty(optionsMap))
            return;
        
        Set set = optionsMap.keySet();
        for (Iterator it = set.iterator(); it.hasNext();)
        {
            String key = (String)it.next();
            String value = (String)optionsMap.get(key);
            if (StringUtil.isNotEmpty(value))
            {
                cmd.setVoteFieldName(key);
                cmd.setVoteContent(value);
                break;
            }
        }
    }
    
    /**
     * 会签用户的设置及会签投票的处理。
     * 
     * <pre>
     *      1.从上下文中获取会签人员数据，如果会签人员数据不为空，则把人员绑定到线程，供会签任务节点产生会签用户使用。
     *      2.如果从上下文中获取了投票的数据。
     *          1.进行投票操作。
     *          2.设置流程状态，设置会签的意见，状态。
     * </pre>
     * 
     * @param processCmd
     * @param taskId
     * @param taskDefKey
     */
    private void signUsersOrSignVoted(ProcessCmd processCmd, TaskEntity taskEntity)
    {
        // 处理后续的节点若有为多实例时，把页面中提交过来的多实例的人员放置至线程中，在后续的任务创建中进行获取
        String nodeId = taskEntity.getTaskDefinitionKey();
        String taskId = taskEntity.getId();
        // 判断当前任务是否会多实例会签任务
        boolean isSignTask = bpmService.isSignTask(taskEntity);
        // 是会签任务将人员取出并设置会签人员。
        if (isSignTask)
        {
            TaskOpinion taskOpinion = this.taskOpinionService.getByTaskId(new Long(taskEntity.getId()));
            
            if (BeanUtils.isEmpty(taskOpinion))
            {
                addRetrOrRecoverOpinion(processCmd, taskEntity, false);
            }
            Map<String, List<TaskExecutor>> executorMap = processCmd.getTaskExecutor();
            if (executorMap != null && executorMap.containsKey(nodeId))
            {
                List<TaskExecutor> executorList = executorMap.get(nodeId);
                taskUserAssignService.setExecutors(executorList);
            }
        }
        
        if (processCmd.getVoteAgree() != null)
        {
            // 加入任务的处理结果及意见
            if (isSignTask)
            {// 加上会签投票
                taskSignDataService.signVoteTask(taskId, processCmd);
            }
            processCmd.getVariables().put(BpmConst.NODE_APPROVAL_STATUS + "_" + nodeId, processCmd.getVoteAgree());
            // processCmd.getVariables().put(BpmConst.NODE_APPROVAL_CONTENT +
            // "_" + nodeId,processCmd.getVoteContent());
        }
        
    }
    
    /**
     * 获取流程定义
     * 
     * @param processCmd
     * @return
     */
    public Definition getDefinitionProcessCmd(ProcessCmd processCmd)
    {
        Definition bpmDefinition = null;
        if (processCmd.getActDefId() != null)
        {
            bpmDefinition = definitionDao.getByActDefId(processCmd.getActDefId());
        }
        else
        {
            bpmDefinition = definitionDao.getByActDefKeyIsMain(processCmd.getFlowKey());
        }
        return bpmDefinition;
    }
    
    /**
     * 启动流程。
     * 
     * <pre>
     * 如果业务主键为空
     * </pre>
     * 
     * @param processCmd
     * @param userId
     * @return
     */
    private ProcessInstance startWorkFlow(ProcessCmd processCmd)
    {
        String businessKey = processCmd.getBusinessKey();
        String userId = UserContextUtil.getCurrentUserId().toString();
        ProcessInstance processInstance = null;
        if (StringUtil.isNotEmpty(businessKey))
        {
            processCmd.getVariables().put(BpmConst.FLOW_BUSINESSKEY, businessKey);
        }
        // 如果主键为空，那么生成一个业务主键。
        else
        {
            businessKey = String.valueOf(UniqueIdUtil.genId());
        }
        
        // 设置流程变量[startUser]。
        Authentication.setAuthenticatedUserId(userId);
        if (processCmd.getActDefId() != null)
        {
            processInstance =
                bpmService.startFlowById(processCmd.getActDefId(), businessKey, processCmd.getVariables());
        }
        else
        {
            processInstance =
                bpmService.startFlowByKey(processCmd.getFlowKey(), businessKey, processCmd.getVariables());
        }
        Authentication.setAuthenticatedUserId(null);
        // 清除流程定义线程变量
        // DeploymentCache.clearProcessDefinitionEntity();
        return processInstance;
    }
    
    /**
     * 取得流程第一个节点。
     * 
     * @param actDefId
     * @return
     */
    public String getFirstNodetByDefId(String actDefId)
    {
        String bpmnXml = bpmService.getDefXmlByProcessDefinitionId(actDefId);
        // 取得第一个任务节点
        String firstTaskNode = BpmUtil.getFirstTaskNode(bpmnXml);
        return firstTaskNode;
    }
    
    /**
     * 执行前后处理器。 0 代表失败 1代表成功，-1代表不需要执行
     * 
     * @param processCmd
     * @param bpmNodeSet
     * @param isBefore
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    private void invokeHandler(ProcessCmd processCmd, NodeSet bpmNodeSet, boolean isBefore)
        throws Exception
    {
        if (bpmNodeSet == null)
            return;
        String handler = "";
        if (isBefore)
        {
            handler = bpmNodeSet.getBeforeHandler();
        }
        else
        {
            handler = bpmNodeSet.getAfterHandler();
        }
        if (StringUtil.isEmpty(handler))
            return;
        
        // 改成groovy 脚本
        GroovyScriptEngine scriptEngine = (GroovyScriptEngine)AppUtil.getBean("scriptEngine");
        Map<String, Object> grooVars = new HashMap<String, Object>();
        grooVars.put("processCmd", processCmd);
        scriptEngine.execute(handler, grooVars);
        /*
         * String[] aryHandler = handler.split("[.]"); if (aryHandler != null) { String beanId = aryHandler[0]; String
         * method = aryHandler[1]; // 触发该Bean下的业务方法 Object serviceBean = AppUtil.getBean(beanId); if (serviceBean !=
         * null) { Method invokeMethod = serviceBean.getClass().getDeclaredMethod( method, new Class[] {
         * ProcessCmd.class }); invokeMethod.invoke(serviceBean, processCmd); } }
         */
    }
    
    /**
     * 获取起始节点的NodeSet。
     * 
     * @param defId
     * @param toFirstNode
     * @param firstNodeSet
     * @return
     */
    public NodeSet getStartNodeSet(Long defId, String nodeId)
    {
        NodeSet firstNodeSet = bpmNodeSetService.getByDefIdNodeId(defId, nodeId);
        NodeSet bpmNodeSetGlobal = bpmNodeSetService.getBySetType(defId, NodeSet.SetType_GloabalForm);
        if (firstNodeSet != null && firstNodeSet.getFormType() != -1)
        {
            return firstNodeSet;
        }
        return bpmNodeSetGlobal;
    }
    
    /**
     * 启动流程。<br>
     * 
     * <pre>
     * 步骤： 
     * 1.表单数据保存。 
     * 2.启动流程。 
     * 3.记录流程运行情况。 
     * 4.记录流程执行堆栈。 
     * 5.根据流程的实例ID，查询任务ID。
     * 6.取得任务Id，并完成该任务。 
     * 7.记录流程堆栈。
     * </pre>
     * 
     * @param processCmd
     * @return
     * @throws Exception
     */
    public ProcessRun startProcess(ProcessCmd processCmd)
        throws Exception
    {
        
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        Definition bpmDefinition = getDefinitionProcessCmd(processCmd);
        // 通过webservice启动流程时，传入的actDefId或者flowKey不正确时抛出这个异常
        if (bpmDefinition == null)
            throw new Exception(getText("service.processrun.notDef"));
        // 流程状态不为启用并且不为测试状态。
        if (!Definition.STATUS_ENABLED.equals(bpmDefinition.getStatus())
            && !Definition.STATUS_TEST.equals(bpmDefinition.getStatus()))
            throw new Exception(getText("service.processrun.disable"));
        // 是否跳转到第一个流程节点
        Long defId = bpmDefinition.getDefId();
        // 是否跳过第一个任务节点，当为1 时，启动流程后完成第一个任务。
        Short toFirstNode = bpmDefinition.getToFirstNode();
        if (StringUtil.isNotEmpty(processCmd.getStartNode()))
        {
            toFirstNode = Short.valueOf(processCmd.getStartNode());
        }
        
        String actDefId = bpmDefinition.getActDefId();
        
        String nodeId = getFirstNodetByDefId(actDefId);
        // 开始节点
        NodeSet bpmNodeSet = getStartNodeSet(defId, nodeId);
        
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        if(sysUser==null&&StringUtil.isNotEmpty(processCmd.getCurrentUserId())) {
            sysUser = this.sysUserService.getById(Long.valueOf(processCmd.getCurrentUserId()));
            UserContextUtil.setCurrentUser(sysUser);
        }
        ProcessRun processRun = processCmd.getProcessRun();
        try
        {
            // 设置跳过第一个节点配置，用户判断代理
            // BaseTaskListener.isAllowAgent
            TaskThreadService.setToFirstNode(toFirstNode);
            // 如果第一步跳转，那么设置发起人为任务执行人。
            if (toFirstNode == 1)
            {
                List<TaskExecutor> excutorList = new ArrayList<TaskExecutor>();
                excutorList.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getFullname()));
                taskUserAssignService.addNodeUser(nodeId, excutorList);
            }
            // 设置下一步包括分发任务的用户
            setThreadTaskUser(processCmd);
            
            // 在线表单数据处理
            String businessKey = "";
            // 初始流程运行记录。
            if (BeanUtils.isEmpty(processRun))
            {
                processRun = initProcessRun(bpmDefinition);
            }
            else
            {
                // 草稿页启动流程
                processRun.setCreatetime(new Date());
                businessKey = processRun.getBusinessKey();
            }
            /** 1.表单数据保存(处理业务表单，外部调用不触发表单处理) */
            handForm(processCmd, processRun);
            // 已经在上行代码中保存了表单，下面的程序将不再保存表单。
            boolean isSaveForm = false;
            // 生成流程任务标题
            if (BeanUtils.isEmpty(processCmd.getProcessRun()))
            {
                String subject = getSubject(bpmDefinition, processCmd);
                processRun.setSubject(subject);
                processCmd.addVariable("subject_", subject);
            }
            
            // 调用前置处理器
            if (!processCmd.isSkipPreHandler())
            {
                invokeHandler(processCmd, bpmNodeSet, true);
            }
            if (StringUtil.isEmpty(businessKey))
            {
                businessKey = processCmd.getBusinessKey();
            }
            // 生成流程任务标题
            String subject = getSubject(bpmDefinition, processCmd);
            
            ISysOrg sysOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
            // 增加发起人组织流程变量。
            if (sysOrg != null)
            {
                processCmd.addVariable(BpmConst.START_ORG_ID, sysOrg.getOrgId());
            }
            processCmd.addVariable(BpmConst.FLOW_MAIN_ACTDEFID, processRun.getActDefId());
            // 添加流程runId。
            processCmd.addVariable(BpmConst.FLOW_RUNID, processRun.getRunId());
            // 添加流程主题。
            processCmd.addVariable(BpmConst.FLOW_RUN_SUBJECT, subject);
            /** 2.启动流程 */
            ProcessInstance processInstance = startWorkFlow(processCmd);
            
            String processInstanceId = processInstance.getProcessInstanceId();
            processRun.setBusinessKey(businessKey);
            processRun.setRootBusinessKey(businessKey);
            processRun.setActInstId(processInstanceId);
            processRun.setSubject(subject);
            
            if (sysOrg != null)
            {
                processRun.setStartOrgId(sysOrg.getOrgId());
                processRun.setStartOrgName(sysOrg.getOrgName());
            }
            processRun.setStatus(ProcessRun.STATUS_RUNNING);
            // 更新或添加流程实例
            /** 3.记录流程运行情况。 */
            if (BeanUtils.isEmpty(processCmd.getProcessRun()))
            {
                this.add(processRun);
            }
            else
            {
                this.update(processRun);
            }
            
            List<Task> taskList = TaskThreadService.getNewTasks();
            // 初始化执行的堆栈树
            /** 4.记录流程执行堆栈。 */
            executionStackService.initStack(processInstanceId, taskList, toFirstNode);
            
            processCmd.setProcessRun(processRun);
            // 后置处理器
            if (!processCmd.isSkipAfterHandler())
            {
                invokeHandler(processCmd, bpmNodeSet, false);
            }
            // 获取下一步的任务并完成跳转。
            if (toFirstNode == 1)
            {
                handJumpOverFirstNode(processInstanceId, processCmd, isSaveForm);
            }
            else
            {
                // 处理信息
                String informType = processCmd.getInformType();
                // 处理信息
                taskMessageService
                    .notify(TaskThreadService.getNewTasks(), informType, processRun.getSubject(), null, "", "");
            }
            // 处理同用户节点
            handSameExecutorJump(TaskThreadService.getNewTasks(), bpmDefinition, processCmd, isSaveForm);
            
            // 添加运行期表单,往表FormRun中插记录，外部调用时不对表单做处理。
            if (!processCmd.isInvokeExternal())
            {
                formRunService.addFormRun(actDefId, processRun.getRunId(), processInstanceId);
            }
            
            // 添加到流程运行日志
            String memo = getText("service.processrun.startFlow") + ":" + subject;
            runLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_START, memo);
            
            // 更新流程历史实例为 开始标记为1。
            if (toFirstNode == 1)
            {
                historyActivityDao.updateIsStart(Long.parseLong(processInstanceId), nodeId);
                
            }
            // 增加流程提交的审批历史
            else
            {
                addSubmitOpinion(processRun);
                
            }
            // 处理代理
            if (toFirstNode.shortValue() != 1)
            {
                handleAgentTaskExe(processCmd);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            throw ex;
        }
        
        return processRun;
    }
    
    // “启动流程时候startProcess()”， 处理业务表单，外部调用不触发表单处理
    private void handForm(ProcessCmd processCmd, ProcessRun processRun)
        throws Exception
    {
        if (processCmd.isInvokeExternal())
        {
            return;
        }
        IFormData bpmFormData = handlerFormData(processCmd, processRun, "");
        if (bpmFormData == null)
        {
            return;
        }
        
        processRun.setTableName(bpmFormData.getTableName());
        if (bpmFormData.getPkValue() != null)
        {
            processRun.setPkName(bpmFormData.getPkValue().getName());
            processRun.setDsAlias(bpmFormData.getDsAlias());
        }
        // 将记录意见放到ProcessCmd中。
        if (bpmFormData != null)
        {
            Map optionsMap = bpmFormData.getOptions();
            setOpinionByForm(optionsMap, processCmd);
        }
    }
    
    private void addSubmitOpinion(ProcessRun processRun)
    {
        TaskOpinion opinion = new TaskOpinion();
        Long startUserId = processRun.getCreatorId();
        ISysUser startUser = sysUserService.getById(startUserId);
        opinion.setOpinionId(UniqueIdUtil.genId());
        opinion.setSuperExecution(this.getSuperActInstId(processRun.getActInstId().toString()));
        opinion.setCheckStatus(TaskOpinion.STATUS_SUBMIT);
        opinion.setActInstId(processRun.getActInstId());
        opinion.setExeFullname(startUser.getFullname());
        opinion.setExeUserId(startUserId);
        opinion.setOpinion(getText("service.processrun.submit"));
        opinion.setStartTime(processRun.getCreatetime());
        opinion.setTaskName(getText("service.processrun.submitFlow"));
        opinion.setEndTime(new Date());
        opinion.setDurTime(0L);
        taskOpinionService.add(opinion);
    }
    
    /**
     * 处理任务跳转。
     * 
     * @param processInstanceId
     * @param processCmd
     * @param isSaveForm 是否要保存表单
     * @throws Exception
     */
    private void handJumpOverFirstNode(String processInstanceId, ProcessCmd processCmd, boolean isSaveForm)
        throws Exception
    {
        // 流程启动时跳过第一个节点，清除节点和人员的映射
        taskUserAssignService.clearNodeUserMap();
        TaskThreadService.clearNewTasks();
        List<ProcessTask> taskList = bpmService.getTasks(processInstanceId);
        ProcessTask taskEntity = taskList.get(0);
        String taskId = taskEntity.getId();
        String parentNodeId = taskEntity.getTaskDefinitionKey();
        // 填写第一步意见。
        processCmd.getVariables().put(BpmConst.NODE_APPROVAL_STATUS + "_" + parentNodeId, TaskOpinion.STATUS_SUBMIT);
        processCmd.getVariables().put(BpmConst.NODE_APPROVAL_CONTENT + "_" + parentNodeId, "填写表单");
        processCmd.setVoteAgree(TaskOpinion.STATUS_SUBMIT);
        processCmd.setTaskId(taskId);
        // 设置流程变量。
        setVariables(taskId, processCmd);
        // 进行流程跳转。
        // 流程启动已经保存过一次，不需要对表单进行保存。
        nextProcess(processCmd, isSaveForm);
        
        // bpmService.transTo(taskId, "");
        /*
         * nextProcess 已经对执行堆栈数据做过添加处理了，这里为什么还要在添加一次， 如果不注释，会多一条数据
         * executionStackService.addStack(taskEntity.getProcessInstanceId(), parentNodeId, "");
         */
    }
    
    /**
     * 转发办结流程
     * 
     * @param processRun
     * @param targetUserIds
     * @param currUser
     * @param informType 通知类型
     * @throws Exception
     */
    public void divertProcess(ProcessRun processRun, List<String> targetUserIds, ISysUser currUser, String informType,
        String suggestion)
        throws Exception
    {
        String instanceId = processRun.getActInstId();
        String actDefId = processRun.getActDefId();
        Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
        Long defTypeId = bpmDefinition.getTypeId();
        Long currUid = currUser.getUserId();
        String currName = currUser.getFullname();
        if (BeanUtils.isEmpty(bpmDefinition))
            return;
        
        if (BeanUtils.isEmpty(targetUserIds))
            return;
        
        List<ISysUser> userList = new ArrayList<ISysUser>();
        
        for (String user : targetUserIds)
        {
            long uid = Long.parseLong(user);
            if (uid == currUid)
            {
                // 当前用户不能转发给自己
                continue;
            }
            ISysUser destUser = sysUserService.getById(uid);
            ProCopyto bpmProCopyto = new ProCopyto();
            bpmProCopyto.setActInstId(Long.parseLong(instanceId));
            bpmProCopyto.setCcTime(new Date());
            bpmProCopyto.setCcUid(uid);
            bpmProCopyto.setCcUname(destUser.getFullname());
            bpmProCopyto.setCopyId(UniqueIdUtil.genId());
            bpmProCopyto.setCpType(ProCopyto.CPTYPE_SEND);
            bpmProCopyto.setIsReaded(0L);
            bpmProCopyto.setRunId(processRun.getRunId());
            bpmProCopyto.setSubject(processRun.getSubject());
            bpmProCopyto.setDefTypeId(defTypeId);
            bpmProCopyto.setCreateId(currUid);
            bpmProCopyto.setCreator(currName);
            proCopytoService.add(bpmProCopyto);
            
            userList.add(destUser);
            //
        }
        Map<String, String> msgTempMap = sysTemplateService.getTempByFun(ISysTemplate.USE_TYPE_FORWARD);
        String subject = processRun.getSubject();
        Long runId = processRun.getRunId();
        taskMessageService.sendMessage(currUser, userList, informType, msgTempMap, subject, suggestion, null, runId);
    }
    
    /*  *//**
          * 处理在线表单数据。
          * 
          * @param processRun
          * @param processCmd
          * @param nodeId
          * @return
          * @throws Exception
          */
    /*
     * private IFormData handlerFormData(ProcessCmd processCmd, ProcessRun processRun, String nodeId) throws Exception {
     * return handlerFormData(processCmd, processRun, nodeId, false); }
     */
    
    /**
     * 处理在线表单数据。
     * 
     * @param processRun
     * @param processCmd
     * @param nodeId
     * @param isToStart
     * @return
     * @throws Exception
     */
    public IFormData handlerFormData(ProcessCmd processCmd, ProcessRun processRun, String nodeId)
        throws Exception
    {
        String json = processCmd.getFormData();
        IFormData bpmFormData = null;
        IFormTable bpmFormTable = null;
        String businessKey = "";
        // 判断节点Id是否为空，为空表示开始节点。
        boolean isStartFlow = false;
        if (StringUtil.isEmpty(nodeId))
        {
            businessKey = processCmd.getBusinessKey();
            isStartFlow = true;
        }
        else
        {
            businessKey = processRun.getBusinessKey();
        }
        if (StringUtils.isEmpty(json))
        {
            // 表单数据为空时也对节点sql进行处理
            String opinionField = (String)processCmd.getTransientVar("opinionField");
            Boolean batComplte = (Boolean)processCmd.getTransientVar("batComplte");
            
            if ((StringUtil.isNotEmpty(opinionField)) && (batComplte != null) && (batComplte.booleanValue()))
            {
                IFormDef bpmFormDef = (IFormDef)this.formDefService.getById(processRun.getFormDefId());
                bpmFormTable = this.formTableService.getTableById(bpmFormDef.getTableId(), 1);
                IFormData data = this.formHandlerService.getFormData(businessKey, bpmFormTable, json);
                
                NodeSqlService.handleData(processCmd, data);
                
                handFieldOpinion(processCmd, data);
                this.formHandlerService.handFormData(data, processRun, nodeId);
            }
            return null;
        }
        
        // 获取提交的数据，以便解析自定义关联的表，这里需要放在FormTable对象主表初始化之前
        // TODELETE formTableService.setDataJson(json);
        
        if (isStartFlow && ProcessRun.STATUS_FORM.equals(processRun.getStatus()))
        {
            Long formDefId = processRun.getFormDefId();
            IFormDef bpmFormDef = formDefService.getById(formDefId);
            bpmFormTable = formTableService.getByTableId(bpmFormDef.getTableId(), 1);
        }
        else
        {
            bpmFormTable = formTableService.getByDefId(processRun.getDefId());
        }
        // 这里有设置流程表单变量
        bpmFormData = this.formHandlerService.getFormData(businessKey, bpmFormTable, json);
        // 意见回填字段处理
        handFieldOpinion(processCmd, bpmFormData);
        processCmd.putVariables(bpmFormData.getVariables());
        // 生成的主键
        IPkValue pkValue = bpmFormData.getPkValue();
        businessKey = pkValue.getValue().toString();
        
        String pk = processRun.getBusinessKey();
        
        processRun.setBusinessKey(businessKey);
        processCmd.setBusinessKey(businessKey);
        
        // 处理节点sql信息
        NodeSqlService.handleData(processCmd, bpmFormData);
        
        // 从dataTemplate中取设置sub表的排序 add by zxg
        IDataTemplate dataTemplate =
            dataTemplateService.getByFormKey(formDefService.getById(processRun.getFormDefId()).getFormKey());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dataTemplate", dataTemplate);
        // 启动流程。
        if (isStartFlow)
        {
            // 保存表单数据,存取表单数据
            formHandlerService.handFormData(params, bpmFormData, processRun);
        }
        else
        {
            // 保存表单数据,存取表单数据
            formHandlerService.handFormData(params, bpmFormData, processRun, nodeId);
            if (!processCmd.isSkip())
            {
                this.update(processRun);
            }
            // 业务主键为空的情况，设置流程主键。设置流程变量。
            if (StringUtil.isEmpty(pk))
            {
                runtimeService.setVariable(processRun.getActInstId(), BpmConst.FLOW_BUSINESSKEY, businessKey);
            }
        }
        return bpmFormData;
    }
    
    /**
     * 表单回填字段处理(目前功能未开发) 2016-6-30 --yangbo
     * 
     * @param processCmd
     * @param bpmFormData
     * @throws Exception
     */
    private void handFieldOpinion(ProcessCmd processCmd, IFormData bpmFormData)
        throws Exception
    {
        String opinionField = (String)processCmd.getTransientVar("opinionField");
        Short optionHtml = (Short)processCmd.getTransientVar("optionHtml");
        String opinion = processCmd.getVoteContent();
        int vote = processCmd.getVoteAgree().shortValue();
        if (StringUtil.isEmpty(opinionField))
            return;
        IFormTable bpmFormTable = bpmFormData.getFormTable();
        if (!bpmFormData.isExternal())
        {
            opinionField = ITableModel.CUSTOMER_COLUMN_PREFIX + opinionField;
        }
        opinionField = opinionField.toLowerCase();
        
        Map<String, Object> mainMap = bpmFormData.getMainFields();
        
        Map<String, Object> map = this.formHandlerService
            .getByKey(bpmFormTable, bpmFormData.getPkValue().getValue().toString(), new String[] {opinionField});
        if (BeanUtils.isNotEmpty(map))
        {
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                String val = (String)entry.getValue();
                if (StringUtil.isNotEmpty(val))
                {
                    mainMap.put(((String)entry.getKey()).toLowerCase(), entry.getValue());
                }
                
            }
            
        }
        
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        String userName = sysUser == null ? "系统用户" : sysUser.getFullname();
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setExeFullname(userName);
        taskOpinion.setCheckStatus(Short.valueOf((short)vote));
        taskOpinion.setCreatetime(new Date());
        taskOpinion.setOpinion(opinion);
        if (processCmd.isSkip())
        {
            taskOpinion.setOpinion("同意(自动审批)");
        }
        
        boolean supportHtml = optionHtml.shortValue() == 1;
        String val = TaskOpinionService.getOpinion(taskOpinion, supportHtml);
        boolean batComplte = processCmd.getTransientVar("batComplte") != null
            ? ((Boolean)processCmd.getTransientVar("batComplte")).booleanValue()
            : false;
        if (batComplte)
        {
            val = "(批量审批)" + val;
        }
        if (mainMap.containsKey(opinionField))
        {
            String str = (String)mainMap.get(opinionField);
            if (!StringUtil.isEmpty(str))
            {
                val = str + "\n" + val;
            }
        }
        mainMap.put(opinionField, val);
    }
    
    /**
     * 获取流程标题。
     * 
     * @param bpmDefinition
     * @param processCmd
     * @return
     */
    private String getSubject(Definition bpmDefinition, ProcessCmd processCmd)
    {
        // 若设置了标题，则直接返回该标题，否则按后台的标题规则返回
        if (StringUtils.isNotEmpty(processCmd.getSubject()))
        {
            return processCmd.getSubject();
        }
        
        String rule = bpmDefinition.getTaskNameRule();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", bpmDefinition.getSubject());
        ISysUser user = (ISysUser)UserContextUtil.getCurrentUser();
        map.put(BpmConst.StartUser, user.getUsername());
        map.put("startDate", TimeUtil.getCurrentDate());
        map.put("startTime", TimeUtil.getCurrentTime());
        map.put("businessKey", processCmd.getBusinessKey());
        map.putAll(processCmd.getVariables());
        rule = BpmUtil.getTitleByRule(rule, map);
        if (Definition.STATUS_TEST.equals(bpmDefinition.getStatus()))
        {
            if (StringUtil.isEmpty(bpmDefinition.getTestStatusTag()))
            {
                return Definition.TEST_TAG + "-" + rule;
            }
            else
            {
                return bpmDefinition.getTestStatusTag() + "-" + rule;
            }
        }
        return rule;
    }
    
    /**
     * 初始化ProcessRun.
     * 
     * @return
     */
    private ProcessRun initProcessRun(Definition bpmDefinition)
    {
        ProcessRun processRun = new ProcessRun();
        NodeSet bpmNodeSet = bpmNodeSetService.getBySetType(bpmDefinition.getDefId(), NodeSet.SetType_BpmForm);
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        if (BeanUtils.isEmpty(bpmNodeSet))
        {
            bpmNodeSet = bpmNodeSetService.getBySetType(bpmDefinition.getDefId(), NodeSet.SetType_GloabalForm);
        }
        if (BeanUtils.isNotEmpty(bpmNodeSet))
        {
            if (NodeSet.FORM_TYPE_ONLINE.equals(bpmNodeSet.getFormType()))
            {
                IFormDef bpmFormDef = formDefService.getDefaultPublishedByFormKey(bpmNodeSet.getFormKey());
                if (bpmFormDef != null)
                {
                    processRun.setFormDefId(bpmFormDef.getFormDefId());
                    processRun.setBusDescp(bpmFormDef.getFormDesc());
                }
            }
            else
            {
                processRun.setBusinessUrl(bpmNodeSet.getFormUrl());
            }
        }
        
        processRun.setCreator(curUser.getFullname());
        processRun.setCreatorId(curUser.getUserId());
        processRun.setActDefId(bpmDefinition.getActDefId());
        processRun.setDefId(bpmDefinition.getDefId());
        processRun.setProcessName(bpmDefinition.getSubject());
        processRun.setFlowKey(bpmDefinition.getDefKey());
        // 流程实例归档后是否允许打印表单
        processRun.setIsPrintForm(bpmDefinition.getIsPrintForm());
        processRun.setCreatetime(new Date());
        processRun.setStatus(ProcessRun.STATUS_RUNNING);
        if (Definition.STATUS_TEST.equals(bpmDefinition.getStatus()))
        {
            processRun.setIsFormal(ProcessRun.TEST_RUNNING);
        }
        else
        {
            processRun.setIsFormal(ProcessRun.FORMAL_RUNNING);
        }
        if (BeanUtils.isNotEmpty(bpmDefinition.getTypeId()))
            processRun.setTypeId(bpmDefinition.getTypeId());
        
        processRun.setRunId(UniqueIdUtil.genId());
        
        return processRun;
    }
    
    /**
     * 获取历史实例
     * 
     * @param queryFilter
     * @return
     */
    public List<ProcessRun> getAllHistory(QueryFilter queryFilter)
    {
        return dao.getAllHistory(queryFilter);
    }
    
    /**
     * 查看我参与审批流程列表
     * 
     * @param filter
     * @return
     */
    public List<ProcessRun> getMyAttend(QueryFilter filter)
    {
        // return dao.getMyAttend(filter);
        return dao.getMyAttend(filter);
    }
    
    @Override
    public void delByIds(Long[] ids)
    {
        if (ids == null || ids.length == 0)
            return;
        for (Long uId : ids)
        {
            ProcessRun processRun = getById(uId);
            Short procStatus = processRun.getStatus();
            Long instanceId = Long.parseLong(processRun.getActInstId());
            // 删除流程流转状态
            proTransToService.delByActInstId(instanceId);
            // 删除BPM_PRO_CPTO抄送转发
            proCopytoDao.delByRunId(uId);
            if (ProcessRun.STATUS_FINISH.shortValue() != procStatus
                && ProcessRun.STATUS_FORM.shortValue() != procStatus)
            {
                
                // executionDao.delVariableByProcInstId(instanceId);
                // taskDao.delCandidateByInstanceId(instanceId);
                // taskDao.delByInstanceId(instanceId);
                // executionDao.delExecutionByProcInstId(instanceId);
                deleteProcessInstance(processRun);
            }
            else
            {
                // 流程操作日志
                Object[] args = {processRun.getProcessName()};
                String memo = getText("service.processrun.delProInstance", args);
                if (ProcessRun.STATUS_FORM == procStatus)
                {
                    memo = getText("service.processrun.delProDraft", args);
                    runLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_DELETEFORM, memo);
                }
                else
                {
                    runLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_DELETEINSTANCE, memo);
                }
                
                delById(uId);
            }
        }
    }
    
    public List<ProcessRun> getMyProcessRun(Long creatorId, String subject, Short status, PagingBean pb)
    {
        return dao.getMyProcessRun(creatorId, subject, status, pb);
    }
    
    @Override
    public void add(ProcessRun entity)
    {
        super.add(entity);
        ProcessRun history = (ProcessRun)entity.clone();
        dao.addHistory(history);
    }
    
    @Override
    public void update(ProcessRun entity)
    {
        ProcessRun history = dao.getByIdHistory(entity.getRunId());
        if (ProcessRun.STATUS_MANUAL_FINISH.intValue() == entity.getStatus()
            || ProcessRun.STATUS_FINISH.intValue() == entity.getStatus())
        {
            Date endDate = new Date();
            Date startDate = history.getCreatetime();
            long duration = endDate.getTime() - startDate.getTime();// calendarAssignService.getTaskMillsTime(startDate,
                                                                    // endDate,
                                                                    // userId);
            history.setEndTime(endDate);
            history.setDuration(duration);
            dao.updateHistory(history);
            dao.delById(entity.getRunId());
        }
        else
        {
            dao.updateHistory(history);
            super.update(entity);
        }
    }
    
    public List<ProcessRun> getByActDefId(String actDefId)
    {
        return dao.getbyActDefId(actDefId);
    }
    
    /**
     * 保存沟通信息。
     * 
     * <pre>
     *  1.如果任务这个任务执行人为空的情况，先将当前设置成任务执行人。
     *  2.产生沟通任务。
     *  3.添加意见。
     *  4.保存任务接收人。
     *  5.产生通知消息。
     * </pre>
     * 
     * @param taskEntity 任务实例
     * @param opinion 意见
     * @param informType 通知类型
     * @param userIds 用户ID
     * @param subject 主题信息
     * @throws Exception
     */
    public void saveCommuniCation(TaskEntity taskEntity, String opinion, String informType, String userIds,
        String subject)
        throws Exception
    {
        String taskId = taskEntity.getId();
        String[] aryUsers = userIds.split(",");
        
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        
        // 任务执行人为空设定当前人为任务执行人。
        String assignee = taskEntity.getAssignee();
        if (FlowUtil.isAssigneeEmpty(assignee))
        {
            taskDao.updateTaskAssignee(taskId, sysUser.getUserId().toString());
            // 根据任务ID获取意见。
            TaskOpinion oldOpinion = taskOpinionDao.getByTaskId(Long.parseLong(taskId));
            oldOpinion.setExeUserId(sysUser.getUserId());
            oldOpinion.setExeFullname(sysUser.getFullname());
            taskOpinionDao.update(oldOpinion);
        }
        
        // 产生沟通任务
        Map<Long, Long> usrIdTaskIds = bpmService.genCommunicationTask(taskEntity, aryUsers, sysUser);
        
        Long opinionId = UniqueIdUtil.genId();
        
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setOpinionId(opinionId);
        taskOpinion.setOpinion(opinion);
        // taskOpinion.setSuperExecution(this.getSuperActInstId(taskEntity.getProcessDefinitionId()));
        taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
        taskOpinion.setActInstId(taskEntity.getProcessInstanceId());
        taskOpinion.setStartTime(new Date());
        taskOpinion.setEndTime(new Date());
        taskOpinion.setExeUserId(sysUser.getUserId());
        taskOpinion.setExeFullname(sysUser.getFullname());
        taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
        taskOpinion.setTaskName(taskEntity.getName());
        taskOpinion.setCheckStatus(TaskOpinion.STATUS_COMMUNICATION);
        dealTaskOpinSupId(taskEntity, taskOpinion);
        // 增加流程意见
        taskOpinionDao.add(taskOpinion);
        // 保存接收人
        commuReceiverService.saveReceiver(opinionId, usrIdTaskIds, sysUser);
        // 发送通知。
        notifyCommu(subject, usrIdTaskIds, informType, sysUser, opinion, ISysTemplate.USE_TYPE_COMMUNICATION);
    }
    
    /**
     * 保存流转信息。
     * 
     * <pre>
     *  1.如果这个任务执行人为空的情况，先将当前设置成任务执行人。
     *  2.保存流转状态
     *  3.产生流转任务。
     *  4.添加意见。
     *  5.保存任务接收人。
     *  6.产生通知消息。
     * </pre>
     * 
     * @param taskEntity 任务实例
     * @param opinion 意见
     * @param informType 通知类型
     * @param userIds 用户ID
     * @param transType 流转类型
     * @param action 执行后动作
     * @param subject 主题信息
     * @throws Exception
     */
    public void saveTransTo(TaskEntity taskEntity, String opinion, String informType, String userIds, String transType,
        String action, ProcessRun processRun)
        throws Exception
    {
        String taskId = taskEntity.getId();
        String actInstId = taskEntity.getProcessInstanceId();
        String[] aryUsers = userIds.split(",");
        
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        
        // 任务执行人为空设定当前人为任务执行人。
        String assignee = taskEntity.getAssignee();
        if (FlowUtil.isAssigneeEmpty(assignee))
        {
            taskDao.updateTaskAssignee(taskId, sysUser.getUserId().toString());
            // 根据任务ID获取意见。
            TaskOpinion oldOpinion = taskOpinionDao.getByTaskId(Long.parseLong(taskId));
            oldOpinion.setExeUserId(sysUser.getUserId());
            oldOpinion.setExeFullname(sysUser.getFullname());
            taskOpinionDao.update(oldOpinion);
        }
        // 修改初始任务状态
        taskDao.updateTaskDescription(TaskOpinion.STATUS_TRANSTO_ING.toString(), taskId);
        // 删除沟通任务
        taskDao.delCommuTaskByParentTaskId(taskId);
        // 保存流转状态
        Long id = UniqueIdUtil.genId();
        ProTransTo bpmProTransTo = new ProTransTo();
        bpmProTransTo.setId(id);
        bpmProTransTo.setTaskId(Long.valueOf(taskId));
        bpmProTransTo.setTransType(Integer.valueOf(transType));
        bpmProTransTo.setAction(Integer.valueOf(action));
        bpmProTransTo.setCreatetime(new Date());
        bpmProTransTo.setActInstId(Long.valueOf(actInstId));
        bpmProTransTo.setTransResult(1);
        bpmProTransTo.setAssignee(userIds);
        if (FlowUtil.isAssigneeEmpty(assignee))
        {
            bpmProTransTo.setCreateUserId(sysUser.getUserId());
        }
        else
        {
            bpmProTransTo.setCreateUserId(Long.valueOf(assignee));
        }
        proTransToService.add(bpmProTransTo);
        
        // 产生流转任务
        Map<Long, Long> usrIdTaskIds = bpmService.genTransToTask(taskEntity, aryUsers, sysUser, processRun, informType);
        
        Long opinionId = UniqueIdUtil.genId();
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setOpinionId(opinionId);
        taskOpinion.setOpinion(opinion);
        /*
         * taskOpinion.setSuperExecution(this.getSuperActInstId(taskEntity. getProcessDefinitionId()));
         */
        taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
        taskOpinion.setActInstId(actInstId);
        taskOpinion.setStartTime(new Date());
        taskOpinion.setEndTime(new Date());
        taskOpinion.setExeUserId(sysUser.getUserId());
        taskOpinion.setExeFullname(sysUser.getFullname());
        taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
        taskOpinion.setTaskName(taskEntity.getName());
        taskOpinion.setCheckStatus(TaskOpinion.STATUS_TRANSTO);
        dealTaskOpinSupId(taskEntity, taskOpinion);
        // 增加流程意见
        taskOpinionDao.add(taskOpinion);
        // 保存接收人
        commuReceiverService.saveReceiver(opinionId, usrIdTaskIds, sysUser);
        // 发送通知。
        notifyCommu(processRun.getSubject(), usrIdTaskIds, informType, sysUser, opinion, ISysTemplate.USE_TYPE_TRANSTO);
    }
    
    /**
     * 添加已办历史。
     * 
     * <pre>
     *  添加沟通或流转反馈任务到已办。
     * </pre>
     * 
     * @param taskEnt
     */
    public void addActivityHistory(TaskEntity taskEnt)
    {
        HistoricActivityInstanceEntity ent = new HistoricActivityInstanceEntity();
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        ent.setId(String.valueOf(UniqueIdUtil.genId()));
        ent.setActivityId(taskEnt.getTaskDefinitionKey());
        
        ent.setActivityName(taskEnt.getName());
        ent.setProcessInstanceId(taskEnt.getProcessInstanceId());
        ent.setAssignee(sysUser.getUserId().toString());
        ent.setProcessDefinitionId(taskEnt.getProcessDefinitionId());
        ent.setStartTime(taskEnt.getCreateTime());
        ent.setEndTime(new Date());
        ent.setDurationInMillis(System.currentTimeMillis() - taskEnt.getCreateTime().getTime());
        ent.setExecutionId("0");
        ent.setActivityType("userTask");
        historyActivityDao.add(ent);
    }
    
    /**
     * 添加干预数据。
     * 
     * <pre>
     *  添加干预数据到到审批历史。
     * </pre>
     * 
     * @param processCmd
     * @param taskEnt
     */
    private void addInterVene(ProcessCmd processCmd, DelegateTask taskEnt)
    {
        if (processCmd.getIsManage() == 0)
            return;
        String assignee = taskEnt.getAssignee();
        String tmp = "";
        
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        Long userId = SystemConst.SYSTEMUSERID;
        String userName = SystemConst.SYSTEMUSERNAME;
        if (curUser != null)
        {
            userId = curUser.getUserId();
            userName = curUser.getFullname();
        }
        
        if (FlowUtil.isAssigneeNotEmpty(assignee))
        {
            ISysUser sysUser = sysUserService.getById(new Long(assignee));
            if (sysUser != null)
            {
                tmp = "原执行人:" + UserContextUtil.getUserLink(sysUser.getUserId().toString(), sysUser.getFullname());
            }
        }
        else
        {
            tmp = "原候选执行人:";
            Set<? extends ISysUser> userList = taskUserService.getCandidateUsers(taskEnt.getId());
            for (ISysUser user : userList)
            {
                if (user != null)
                {
                    tmp += UserContextUtil.getUserLink(user.getUserId().toString(), user.getFullname()) + ",";
                }
            }
        }
        Date endDate = new Date();
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setOpinionId(UniqueIdUtil.genId());
        taskOpinion.setSuperExecution(this.getSuperActInstId(taskEnt.getProcessInstanceId()));
        taskOpinion.setActDefId(taskEnt.getProcessDefinitionId());
        taskOpinion.setActInstId(taskEnt.getProcessInstanceId());
        taskOpinion.setTaskKey(taskEnt.getTaskDefinitionKey());
        taskOpinion.setTaskName(taskEnt.getName());
        taskOpinion.setExeUserId(userId);
        taskOpinion.setExeFullname(userName);
        taskOpinion.setCheckStatus(TaskOpinion.STATUS_INTERVENE);
        taskOpinion.setStartTime(taskEnt.getCreateTime());
        taskOpinion.setTaskId(new Long(taskEnt.getId()));
        taskOpinion.setCreatetime(taskEnt.getCreateTime());
        taskOpinion.setEndTime(endDate);
        taskOpinion.setOpinion(tmp);
        Long duration = endDate.getTime() - taskEnt.getCreateTime().getTime();// calendarAssignService.getRealWorkTime(taskEnt.getCreateTime(),
                                                                              // endDate,
                                                                              // userId);
        taskOpinion.setDurTime(duration);
        taskOpinionService.add(taskOpinion);
        
    }
    
    /**
     * 根据流程运行Id,删除流程运行实体。<br/>
     * 些方法不会级联删除相关信息。
     * 
     * @see com.ibms.core.service.GenericService#delById(java.io.Serializable)
     */
    @Override
    public void delById(Long id)
    {
        dao.delById(id);
        dao.delByIdHistory(id);
    }
    
    /**
     * 发送短信或者邮件通知。
     * 
     * @param subject 标题
     * @param usrIdTaskIds 目标任务用户与ID对应关系列表
     * @param informTypes 通知类型。
     * @param sysUser 发送用户
     * @param opinion 意见(或原因)
     * @param sysTemplate 消息模板类型
     * @throws Exception
     */
    public void notifyCommu(String subject, Map<Long, Long> usrIdTaskIds, String informTypes, ISysUser sysUser,
        String opinion, Integer sysTemplate)
        throws Exception
    {
        Map<String, String> msgTempMap = sysTemplateService.getTempByFun(sysTemplate);
        // 如果目标节点是空，获取目标节点
        if (usrIdTaskIds == null || usrIdTaskIds.size() == 0)
            return;
        
        Iterator<Entry<Long, Long>> iter = usrIdTaskIds.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<Long, Long> entry = (Map.Entry<Long, Long>)iter.next();
            Long userId = entry.getKey();
            if (userId.equals(sysUser.getUserId()))
                continue;
            Long taskId = entry.getValue();
            ISysUser receiverUser = (ISysUser)sysUserService.getById(userId);
            List<ISysUser> receiverUserList = new ArrayList<ISysUser>();
            receiverUserList.add(receiverUser);
            taskMessageService
                .sendMessage(sysUser, receiverUserList, informTypes, msgTempMap, subject, opinion, taskId, 0L);
            // // 内部消息
            // if (informTypes.contains(BpmConst.MESSAGE_TYPE_INNER))
            // this.sendInnerMessage(receiverUser, title, subject,
            // innerTemplate, sysUser, opinion, taskId, true);
            // // 邮件
            // if (informTypes.contains(BpmConst.MESSAGE_TYPE_MAIL))
            // this.sendMailMessage(receiverUser, title, subject, mailTemplate,
            // sendUser, opinion, taskId, true);
            // // 手机短信
            // if (informTypes.contains(BpmConst.MESSAGE_TYPE_SMS))
            // this.sendShortMessage(receiverUser, subject, smsTemplate,
            // sendUser, opinion);
        }
    }
    
    /**
     * 根据Act流程定义ID，获取流程实例
     * 
     * @param actDefId
     * @param pb
     * @return
     */
    public List<ProcessRun> getByActDefId(String actDefId, PagingBean pb)
    {
        return dao.getByActDefId(actDefId, pb);
    }
    
    /**
     * 按流程实例ID获取ProcessRun实体
     * 
     * @param processInstanceId
     * @return
     */
    public ProcessRun getByActInstanceId(Long processInstanceId)
    {
        return dao.getByActInstanceId(processInstanceId);
    }
    
    /**
     * 级联删除流程实例扩展
     * 
     * @param processRun
     */
    private void deleteProcessRunCasade(ProcessRun processRun)
    {
        List<ProcessInstance> childrenProcessInstance =
            runtimeService.createProcessInstanceQuery().superProcessInstanceId(processRun.getActInstId()).list();
        for (ProcessInstance instance : childrenProcessInstance)
        {
            ProcessRun pr = getByActInstanceId(new Long(instance.getProcessInstanceId()));
            if (pr != null)
            {
                deleteProcessRunCasade(pr);
            }
        }
        long procInstId = Long.parseLong(processRun.getActInstId());
        Short procStatus = processRun.getStatus();
        if (ProcessRun.STATUS_FINISH.shortValue() != procStatus)
        {
            try
            {
                executionDao.delVariableByProcInstId(procInstId);
                taskDao.delCandidateByInstanceId(procInstId);
                taskDao.delByInstanceId(procInstId);
                // 关联ACT_RU_EXECUTION删除act_ru_identitylink
                executionDao.delExecutionByProcInstId(procInstId);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        String actDefId = processRun.getActDefId();
        Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
        if (!Definition.STATUS_TEST.equals(bpmDefinition.getStatus()))
        {
            String memo = getText("service.processrun.delProInstance", new Object[] {processRun.getProcessName()});
            runLogService.addRunLog(processRun, RunLog.OPERATOR_TYPE_DELETEINSTANCE, memo);
        }
        delById(processRun.getRunId());
    }
    
    /**
     * 获取流程扩展的根流程扩展（最顶层的父流程扩展）
     * 
     * @param processRun
     * @return
     */
    private ProcessRun getRootProcessRun(ProcessRun processRun)
    {
        ProcessInstance parentProcessInstance =
            runtimeService.createProcessInstanceQuery().subProcessInstanceId(processRun.getActInstId()).singleResult();
        if (parentProcessInstance != null)
        {
            // Get parent ProcessRun
            ProcessRun parentProcessRun = getByActInstanceId(new Long(parentProcessInstance.getProcessInstanceId()));
            // Get Parent ProcessInstance sub ProcessInstance
            return getRootProcessRun(parentProcessRun);
        }
        return processRun;
    }
    
    /**
     * 删除流程运行实体（ProcessRun），级联删除Act流程实例以及父流程
     * 
     * @param processRun
     */
    private void deleteProcessInstance(ProcessRun processRun)
    {
        ProcessRun rootProcessRun = getRootProcessRun(processRun);
        deleteProcessRunCasade(rootProcessRun);
        // runtimeService.deleteProcessInstance(rootProcessRun.getActInstId(),"Manual Delete");
    }
    
    public void delByActDefId(String actDefId)
    {
        List<ProcessRun> list = dao.getbyActDefId(actDefId);
        List<TaskExe> bpmTaskExeList;
        for (ProcessRun processRun : list)
        {
            if (ProcessRun.STATUS_FORM.equals(processRun.getStatus()))
                continue;
            // 删除BPM_PRO_CPTO抄送转发,BPM_TASK_EXE代理转办数据
            proCopytoDao.delByRunId(processRun.getRunId());
            // 代理转办数据BPM_TASK_EXE删除
            // BPM_COMMU_RECEIVER:通知接收人，BPM_TASK_READ：任务是否已读
            bpmTaskExeList = taskExeDao.getByRunId(processRun.getRunId());
            for (TaskExe bpmTaskExe : bpmTaskExeList)
            {
                commuReceiverDao.delByTaskId(bpmTaskExe.getTaskId());
                taskReadDao.delByActInstId(bpmTaskExe.getActInstId());
            }
            taskExeDao.delByRunId(processRun.getRunId());
            deleteProcessInstance(processRun);
        }
        dao.delHistroryByActDefId(actDefId);
    }
    
    public List<ProcessRun> getMyDraft(Long userId, PagingBean pb)
    {
        return dao.getMyDraft(userId, pb);
    }
    
    public List<ProcessRun> getMyDraft(QueryFilter queryFilter)
    {
        return dao.getMyDraft(queryFilter);
    }
    
    /**
     * 保存草稿
     * 
     * @param processCmd
     * @param formKey
     * @param formUrl
     * @throws Exception
     */
    public Map<String, Object> saveForm(ProcessCmd processCmd)
        throws Exception
    {
        // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        Map<String, Object> result = new HashMap<String, Object>();
        String actDefId = processCmd.getActDefId();
        Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
        // 开始节点
        String nodeId = getFirstNodetByDefId(actDefId);
        NodeSet bpmNodeSet = getStartNodeSet(bpmDefinition.getDefId(), nodeId);
        
        ProcessRun processRun = initProcessRun(bpmDefinition);
        String businessKey = "";
        // 保存草稿后，处理业务表单，外部调用不触发表单处理
        if (!processCmd.isInvokeExternal())
        {
            IFormData bpmFormData = handlerFormData(processCmd, processRun, "");
            if (bpmFormData != null)
            {
                businessKey = processCmd.getBusinessKey();
                processRun.setTableName(bpmFormData.getTableName());
                if (bpmFormData.getPkValue() != null)
                {
                    processRun.setPkName(bpmFormData.getPkValue().getName());
                    processRun.setDsAlias(bpmFormData.getDsAlias());
                }
                IFormDef defaultForm = formDefService.getDefaultPublishedByFormKey(bpmNodeSet.getFormKey());
                processRun.setFormDefId(defaultForm.getFormDefId());
                
            }
        }
        // 调用前置处理器
        if (!processCmd.isSkipPreHandler())
        {
            invokeHandler(processCmd, bpmNodeSet, true);
        }
        if (StringUtil.isEmpty(businessKey))
        {
            businessKey = processCmd.getBusinessKey();
        }
        String subject = getSubject(bpmDefinition, processCmd);
        processRun.setBusinessKey(businessKey);
        processRun.setSubject(subject);
        processRun.setStatus(ProcessRun.STATUS_FORM);
        if (processCmd.getRunId() != null && processCmd.getRunId() != 0)
        {
            processRun.setRunId(processCmd.getRunId());
            this.update(processRun);
        }
        else
        {
            processRun.setCreatetime(new Date());
            this.add(processRun);
        }
        // 调用前置处理器
        if (!processCmd.isSkipPreHandler())
        {
            invokeHandler(processCmd, bpmNodeSet, true);
        }
        // 添加到流程运行日志
        String memo = getText("service.processrun.saveForm") + ":" + subject;
        
        runLogService.addRunLog(processRun.getRunId(), RunLog.OPERATOR_TYPE_SAVEFORM, memo);
        result.put("runId", processRun.getRunId());
        result.put("businessKey", processRun.getBusinessKey());
        
        return result;
    }
    
    /**
     * 我的请求
     * 
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getMyRequestList(QueryFilter filter)
    {
        return dao.getMyRequestList(filter);
    }
    
    public List<ProcessRun> getMyRequestCompletedList(QueryFilter filter)
    {
        return this.dao.getMyRequestCompletedList(filter);
    }
    
    /**
     * 我的办结
     * 
     * @return
     */
    public List<ProcessRun> getMyCompletedList(QueryFilter filter)
    {
        return dao.getMyCompletedList(filter);
    }
    
    /**
     * 已办事宜
     * 
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getAlreadyMattersList(QueryFilter filter)
    {
        return dao.getAlreadyMattersList(filter);
    }
    
    /**
     * 办结事宜
     * 
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getCompletedMattersList(QueryFilter filter)
    {
        return dao.getCompletedMattersList(filter);
    }
    
    /**
     * 查询我发起的和我参与流程
     * 
     * @param sqlKey
     * @param queryFilter
     * @return
     * @author liguang 2012.11.6
     */
    public List<ProcessRun> selectPro(QueryFilter queryFilter)
    {
        return dao.getBySqlKey("selectPro", queryFilter);
    }
    
    /**
     * 批量审批。
     * 
     * @param taskIds 任务id使用逗号进行分割。
     * @param opinion 意见。
     * @param isSaveForm 是否要保存表单
     * @throws Exception
     */
    public void nextProcessBat(String taskIds, String opinion, boolean isSaveForm)
        throws Exception
    {
        String[] aryTaskId = taskIds.split(",");
        for (String taskId : aryTaskId)
        {
            TaskEntity taskEntity = bpmService.getTask(taskId);
            ProcessRun processRun = this.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
            String subject = processRun.getSubject();
            if (taskEntity.getExecutionId() == null
                && TaskOpinion.STATUS_COMMUNICATION.toString().equals(taskEntity.getDescription()))
            {
                MessageUtil.addMsg(
                    "<span class='red'>" + subject + "," + getText("service.processrun.commStatu") + "</span><br/>");
                continue;
            }
            if (taskEntity.getExecutionId() == null
                && TaskOpinion.STATUS_TRANSTO.toString().equals(taskEntity.getDescription()))
            {
                MessageUtil.addMsg(
                    "<span class='red'>" + subject + "," + getText("service.processrun.transtoStatu") + "</span><br/>");
                continue;
            }
            // 是否允许批量审批
            // if (bpmDefinition.getAllowBatchApprove() == 0) {
            // MessageUtil.addMsg("<span class='red'>" + subject
            // + ",流程定义不能批量审批!</span><br/>");
            // continue;
            // }
            
            ProcessCmd processCmd = new ProcessCmd();
            processCmd.setVoteAgree(TaskOpinion.STATUS_AGREE);
            processCmd.setVoteContent(opinion);
            processCmd.setTaskId(taskId);
            nextProcess(processCmd, isSaveForm);
            
            MessageUtil.addMsg(
                "<span class='green'>" + subject + "," + getText("service.processrun.agreeSuccess") + "</span><br>");
        }
    }
    
    /**
     * 根据runid找到copyid
     * 
     * @return
     */
    public ProcessRun getCopyIdByRunid(Long runId)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("runId", runId);
        return (ProcessRun)dao.getOne("getCopyIdByRunid", map);
    }
    
    /**
     * 保存表单数据。
     * 
     * @param json json数据
     * @param formKey
     * @param userId
     * @param taskId
     * @param defId
     * @param bizKey
     * @param businessDocumentKey
     * @param opinion
     * @return
     * @throws Exception
     */
    public void saveData(ProcessCmd processCmd)
        throws Exception
    {
        String taskId = processCmd.getTaskId();
        ProcessRun processRun = null;
        NodeSet bpmNodeSet = null;
        TaskEntity task = null;
        if (StringUtil.isEmpty(taskId))
        {
            processRun = processCmd.getProcessRun();
            bpmNodeSet = getStartNodeSet(processRun.getDefId(), "");
        }
        else
        {
            task = bpmService.getTask(taskId);
            String actInstId = task.getProcessInstanceId();
            String actDefId = task.getProcessDefinitionId();
            String nodeId = task.getTaskDefinitionKey();
            processRun = getByActInstanceId(Long.parseLong(actInstId));
            String opinion = processCmd.getVoteContent();
            saveOpinion(task, opinion, "");// 通用意见
            bpmNodeSet = bpmNodeSetService.getByActDefIdNodeId(actDefId, nodeId);
        }
        // 处理业务表单，外部调用不触发表单处理
        if (!processCmd.isInvokeExternal())
        {
            IFormData formData = handlerFormData(processCmd, processRun, bpmNodeSet.getNodeId());
            // 存储自定义的意见数据
            if (task != null)
            {
                Map<String, String> optionsMap = formData.getOptions();
                if (!BeanUtils.isEmpty(optionsMap))
                {
                    Set<String> set = optionsMap.keySet();
                    String key = set.iterator().next();
                    String value = optionsMap.get(key);
                    
                    saveOpinion(task, value, key);// 通用意见
                }
                
            }
            // 更新流程变量
            setVariables(taskId, processCmd);
        }
        // 调用前置处理器
        if (!processCmd.isSkipPreHandler())
        {
            invokeHandler(processCmd, bpmNodeSet, true);
        }
    }
    
    /**
     * 保存任务处理意见。
     * 
     * @param taskEntity
     * @param opinion
     */
    public void saveOpinion(TaskEntity taskEntity, String opinion, String fieldName)
    {
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        TaskOpinion taskOpinion = taskOpinionDao.getOpinionByTaskId(new Long(taskEntity.getId()), curUser.getUserId());
        if (taskOpinion == null)
        {
            Long taskId = Long.parseLong(taskEntity.getId());
            Long opinionId = UniqueIdUtil.genId();
            taskOpinion = new TaskOpinion();
            taskOpinion.setOpinionId(opinionId);
            taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
            taskOpinion.setActInstId(taskEntity.getProcessInstanceId());
            taskOpinion.setTaskId(taskId);
            taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
            taskOpinion.setTaskName(taskEntity.getName());
            taskOpinion.setStartTime(new Date());
            taskOpinion.setCheckStatus(TaskOpinion.STATUS_OPINION);
            taskOpinion.setOpinion(opinion);
            taskOpinion.setFieldName(fieldName);
            
            taskOpinion.setExeUserId(curUser.getUserId());
            taskOpinion.setExeFullname(curUser.getFullname());
            
            dealTaskOpinSupId(taskEntity, taskOpinion);
            
            taskOpinionDao.add(taskOpinion);
        }
        else
        {
            taskOpinion.setExeUserId(curUser.getUserId());
            taskOpinion.setExeFullname(curUser.getFullname());
            taskOpinion.setOpinion(opinion);
            taskOpinion.setFieldName(fieldName);
            taskOpinionDao.update(taskOpinion);
        }
        
    }
    
    private void dealTaskOpinSupId(TaskEntity taskEntity, TaskOpinion taskOpinion)
    {
        while (taskEntity.getExecutionId() == null)
        {
            String parentTaskId = taskEntity.getParentTaskId();
            taskEntity = this.bpmService.getTask(parentTaskId);
        }
        
        Object isExtCall = this.runtimeService.getVariable(taskEntity.getExecutionId(), "isExtCall");
        
        if (isExtCall != null)
        {
            ProcessExecution processExecution =
                (ProcessExecution)this.executionDao.getById(taskEntity.getExecutionId());
            
            if (StringUtil.isEmpty(processExecution.getSuperExecutionId()))
            {
                taskOpinion.setSuperExecution(processExecution.getParentId());
            }
            else
            {
                ProcessExecution processExecution1 =
                    (ProcessExecution)this.executionDao.getById(processExecution.getSuperExecutionId());
                taskOpinion.setSuperExecution(processExecution1.getParentId());
            }
        }
    }
    
    /**
     * 根据流程任务ID，取得流程扩展实例
     * 
     * @param taskId 流程任务ID
     * @return
     */
    public ProcessRun getByTaskId(String taskId)
    {
        ProcessTaskHistory taskHistory = taskHistoryDao.getById(Long.valueOf(taskId));
        if (taskHistory == null)
        {
            return null;
        }
        String actInstId = taskHistory.getProcessInstanceId();
        if (StringUtils.isEmpty(actInstId))
            return null;
        return getByActInstanceId(Long.valueOf(actInstId));
    }
    
    public Long getProcessDuration(ProcessRun entity)
    {
        Long duration = 0L;
        String actInstId = entity.getActInstId();
        Map<String, Map<Long, List<TaskOpinion>>> signTask = new HashMap<String, Map<Long, List<TaskOpinion>>>();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actInstId", entity.getActInstId());
        
        List<TaskOpinion> taskOpinions = taskOpinionService.getByActInstId(actInstId);
        
        for (TaskOpinion opn : taskOpinions)
        {
            boolean isSignTask = bpmService.isSignTask(entity.getActDefId(), opn.getTaskKey());
            if (!isSignTask)
            {// 不是会签
                if (BeanUtils.isNotEmpty(opn.getDurTime()))
                {
                    duration += opn.getDurTime();
                }
                
            }
            else
            {// 是会签，按批次，取最大值
                Map<Long, List<TaskOpinion>> taskMap = signTask.get(opn.getTaskKey());
                if (taskMap == null)
                {
                    taskMap = new HashMap<Long, List<TaskOpinion>>();
                    signTask.put(opn.getTaskKey(), taskMap);
                }
                
                if (opn.getTaskId() == null)
                {
                    continue;
                }
                TaskSignData signData = taskSignDataService.getByTaskId(opn.getTaskId().toString());
                if (signData == null)
                {
                    continue;
                }
                List<TaskOpinion> taskList = taskMap.get(signData.getGroupNo());
                if (taskList == null)
                {
                    taskList = new ArrayList<TaskOpinion>();
                    taskMap.put(signData.getGroupNo(), taskList);
                }
                taskList.add(opn);
            }
        }
        
        // 添加会签的节点时长
        for (Map.Entry<String, Map<Long, List<TaskOpinion>>> entry : signTask.entrySet())
        {
            Map<Long, List<TaskOpinion>> map = entry.getValue();
            for (Map.Entry<Long, List<TaskOpinion>> ent : map.entrySet())
            {
                Long maxDuration = 0L;
                List<TaskOpinion> list = ent.getValue();
                for (TaskOpinion o : list)
                {
                    long durtime = o.getDurTime() == null ? 0 : o.getDurTime();
                    if (maxDuration.longValue() > durtime)
                    {
                        maxDuration = durtime;
                    }
                }
                duration += maxDuration;
            }
        }
        
        return duration;
    }
    
    public Long getProcessLastSubmitDuration(ProcessRun entity)
    {
        Long duration = 0L;
        String actInstId = entity.getActInstId();
        
        Map<String, Map<Long, List<TaskOpinion>>> signTask = new HashMap<String, Map<Long, List<TaskOpinion>>>();
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actInstId", entity.getActInstId());
        
        List<TaskOpinion> taskOpinions = taskOpinionService.getByActInstId(actInstId);
        List<TaskOpinion> lastTaskOpinions = new ArrayList<TaskOpinion>();
        // 以结束时间排序
        Collections.sort(taskOpinions, new Comparator<TaskOpinion>()
        {
            @Override
            public int compare(TaskOpinion o1, TaskOpinion o2)
            {
                Date startTime1 = o1.getStartTime();
                Date endTime1 = o1.getEndTime();
                Date startTime2 = o2.getStartTime();
                Date endTime2 = o2.getEndTime();
                
                if (endTime1 != null && endTime2 != null)
                {
                    return endTime1.compareTo(endTime2);
                }
                else if (endTime1 != null && endTime2 == null)
                {
                    return 1;
                }
                else if (endTime1 == null && endTime2 != null)
                {
                    return -1;
                }
                else
                {
                    return startTime1.compareTo(startTime2);
                }
            }
        });
        
        boolean hasResubmit = false;
        
        for (int i = taskOpinions.size() - 1; i >= 0; i--)
        {
            if (TaskOpinion.STATUS_RESUBMIT.equals(taskOpinions.get(i).getCheckStatus()))
            {
                hasResubmit = true;
                break;
            }
            else
            {
                lastTaskOpinions.add(taskOpinions.get(i));
            }
        }
        
        if (!hasResubmit)
        {
            if (entity.getDuration() != null)
            {
                return entity.getDuration();
            }
            else
            {
                return getProcessDuration(entity);
            }
        }
        
        for (TaskOpinion opn : lastTaskOpinions)
        {
            boolean isSignTask = bpmService.isSignTask(entity.getActDefId(), opn.getTaskKey());
            if (!isSignTask)
            {// 不是会签
                if (opn.getDurTime() != null)
                {
                    duration += opn.getDurTime();
                }
            }
            else
            {// 是会签，按批次，取最大值
                Map<Long, List<TaskOpinion>> taskMap = signTask.get(opn.getTaskKey());
                if (taskMap == null)
                {
                    taskMap = new HashMap<Long, List<TaskOpinion>>();
                    signTask.put(opn.getTaskKey(), taskMap);
                }
                TaskSignData signData = taskSignDataService.getByTaskId(opn.getTaskId().toString());
                List<TaskOpinion> taskList = taskMap.get(signData.getGroupNo());
                if (taskList == null)
                {
                    taskList = new ArrayList<TaskOpinion>();
                    taskMap.put(signData.getGroupNo(), taskList);
                }
                taskList.add(opn);
            }
        }
        
        // 添加会签的节点时长
        for (Map.Entry<String, Map<Long, List<TaskOpinion>>> entry : signTask.entrySet())
        {
            Map<Long, List<TaskOpinion>> map = entry.getValue();
            for (Map.Entry<Long, List<TaskOpinion>> ent : map.entrySet())
            {
                Long maxDuration = 0L;
                List<TaskOpinion> list = ent.getValue();
                for (TaskOpinion o : list)
                {
                    long durtime = o.getDurTime() == null ? 0 : o.getDurTime();
                    if (maxDuration.longValue() > durtime)
                    {
                        maxDuration = durtime;
                    }
                }
                duration += maxDuration;
            }
        }
        
        return duration;
    }
    
    /**
     * 获取某人的流程实例列表。
     * 
     * @param defId 流程定义ID
     * @param creatorId 创建人
     * @param instanceAmount 流程数量
     * @return
     */
    public List<ProcessRun> getRefList(Long defId, Long creatorId, Integer instanceAmount, int type)
    {
        List<ProcessRun> list = null;
        // 我提交的流程
        if (type == 0)
        {
            list = dao.getRefList(defId, creatorId, instanceAmount);
        }
        // 我审批的流程数据
        else
        {
            list = dao.getRefListApprove(defId, creatorId, instanceAmount);
        }
        
        return list;
    }
    
    /**
     * 获取监控的流程实例列表。
     * 
     * @param filter
     * @return
     */
    public List<ProcessRun> getMonitor(QueryFilter filter)
    {
        return dao.getMonitor(filter);
    }
    
    /**
     * 判断是否允许追回
     * 
     * @param runId
     * @return
     * @throws Exception
     */
    public ResultMessage checkRedo(Long runId)
        throws Exception
    {
        ProcessRun processRun = this.getById(runId);
        Definition bpmDefinition = definitionDao.getByActDefId(processRun.getActDefId());
        // 判断当前人是否为发起人。
        // 如果当前人为发起人那么可以撤回所有的流程。
        /*
         * 追回的话不需要 判断是否是创建人 boolean isCreator = processRun.getCreatorId().longValue() == UserContextUtil
         * .getCurrentUserId().longValue(); if (!isCreator) { return new ResultMessage(ResultMessage.Fail,
         * getText("service.processrun.recoverStart")); }
         */
        Short status = processRun.getStatus();
        if (status.shortValue() == ProcessRun.STATUS_FINISH.shortValue())
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.finish"));
        }
        
        if (status.shortValue() == ProcessRun.STATUS_MANUAL_FINISH.shortValue())
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.del"));
        }
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.disable"));
        }
        
        FlowNode flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
        if (flowNode == null)
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.notFindStartNode"));
        }
        String nodeId = flowNode.getNodeId();
        List<ProcessTask> taskList = bpmService.getTasks(processRun.getActInstId());
        
        List<String> taskNodeIdList = getNodeIdByTaskList(taskList);
        if (taskNodeIdList.contains(nodeId))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.cancelled"));
        }
        
        // 判断是否允许返回。
        boolean allowBack = this.getTaskAllowBack(taskList);
        if (!allowBack)
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.notAllowedRevoked"));
        }
        return new ResultMessage(ResultMessage.Success, getText("service.processrun.canUndo"));
    }
    
    /**
     * 判断是否允许撤销，这个允许多实例撤销
     * 
     * @param runId
     * @return
     * @throws Exception
     */
    public ResultMessage checkRecover(Long runId)
        throws Exception
    {
        ProcessRun curProcessRun = this.getById(runId);
        ProcessRun processRun = this.getRootProcessRun(curProcessRun);
        Definition bpmDefinition = definitionDao.getByActDefId(processRun.getActDefId());
        // 判断当前人是否为发起人。
        // 如果当前人为发起人那么可以撤回所有的流程。
        boolean isCreator = processRun.getCreatorId().longValue() == UserContextUtil.getCurrentUserId().longValue();
        if (!isCreator)
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.retrieveStart"));
        }
        Short status = processRun.getStatus();
        if (status.shortValue() == ProcessRun.STATUS_FINISH.shortValue())
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.finish"));
        }
        
        if (status.shortValue() == ProcessRun.STATUS_MANUAL_FINISH.shortValue())
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.del"));
        }
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.disable"));
        }
        String nodeId = "";
        if (NodeCache.isMultipleFirstNode(processRun.getActDefId()))
        {
            nodeId = processRun.getStartNode();
        }
        else
        {
            FlowNode flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
            if (flowNode == null)
            {
                return new ResultMessage(0, "找不到流程起始节点!");
            }
            nodeId = flowNode.getNodeId();
        }
        List<ProcessTask> taskList = bpmService.getTasks(processRun.getActInstId());
        
        List<String> taskNodeIdList = getNodeIdByTaskList(taskList);
        if (taskNodeIdList.contains(nodeId))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.canretrieved"));
        }
        
        return new ResultMessage(ResultMessage.Success, getText("service.processrun.canUndo"));
    }
    
    /**
     * 撤回到开始节点任务
     * 
     * @param runId
     * @param informType
     * @param memo
     * @return
     * @throws Exception
     */
    public ResultMessage recover(Long runId, String informType, String memo)
        throws Exception
    {
        // TODO 删除原实例
        ProcessRun processRun = getById(runId);
        String actInstId = processRun.getActInstId();
        ProcessRun rootProcessRun = getRootProcessRun(processRun);
        
        // 获取开始节点。
        FlowNode flowNode = null;
        
        if (NodeCache.isMultipleFirstNode(rootProcessRun.getActDefId()))
            flowNode = NodeCache.getNodeByActNodeId(rootProcessRun.getActDefId(), rootProcessRun.getStartNode());
        else
        {
            flowNode = NodeCache.getFirstNodeId(rootProcessRun.getActDefId());
        }
        
        List<ProcessTask> tasks = bpmService.getTasks(rootProcessRun.getActInstId());
        List<Task> taskList = new ArrayList<Task>();
        for (ProcessTask task : tasks)
        {
            taskList.add(bpmService.getTask(task.getId()));
        }
        // 任务追回 通知任务所属人
        ISysTemplate sysTemplate = sysTemplateService.getDefaultByUseType(ISysTemplate.USE_TYPE_REVOKED);
        Map<String, String> map = this.getTempByUseType(ISysTemplate.USE_TYPE_REVOKED);
        taskMessageService.notify(taskList, informType, rootProcessRun.getSubject(), map, memo, "");
        // 删除流程变量信息
        delRelateVars(actInstId);
        // 删除流程实例信息
        executionDao.delVarByInstIdAndVarName(Long.valueOf(actInstId), "_token_");
        // 删除流程任务节点信息
        taskForkDao.delByActInstId(actInstId);
        ProcessInstance parentProcessInstance =
            runtimeService.createProcessInstanceQuery().processInstanceId(rootProcessRun.getActInstId()).singleResult();
        // 修改 processrun 状态信息
        if (parentProcessInstance != null)
        {
            addTaskInRecover(parentProcessInstance, flowNode);
            addRecoverOpinion(rootProcessRun, memo);
            ProcessRun historyProcessRun = dao.getByIdHistory(rootProcessRun.getRunId());
            rootProcessRun.setStatus(ProcessRun.STATUS_RECOVER);
            historyProcessRun.setStatus(ProcessRun.STATUS_RECOVER);
            dao.update(rootProcessRun);
            dao.updateHistory(historyProcessRun);
        }
        return new ResultMessage(ResultMessage.Success, getText("service.processrun.retrieveSuc"));
    }
    
    private void delRelateVars(String actInstId)
    {
        Map varsMap = this.runtimeService.getVariables(actInstId);
        Iterator iterator = varsMap.keySet().iterator();
        while (iterator.hasNext())
        {
            String name = (String)iterator.next();
            if (containVars(name))
            {
                executionDao.delVarByInstIdAndVarName(Long.valueOf(Long.parseLong(actInstId)), name);
            }
        }
    }
    
    private boolean containVars(String varName)
    {
        String[] aryNames =
            {"nrOfInstances", "nrOfCompletedInstances", "nrOfActiveInstances", "loopCounter", "_signUsers"};
        for (String tmp : aryNames)
        {
            if (varName.contains(tmp))
            {
                return true;
            }
        }
        return false;
    }
    
    // 删除子实例及相关信息
    private void delSubInfoByProcInstId(String procInstId)
    {
        // 获取子流程列表
        List<ProcessExecution> processExecutionList = executionDao.getSubExecutionByProcInstId(procInstId);
        for (int i = 0; i < processExecutionList.size(); i++)
        {
            ProcessExecution p = processExecutionList.get(i);
            String subProcInstId = p.getId();
            Long subProcInstIdL = Long.parseLong(subProcInstId);
            // 如果不是活动，可能包含多实例的子流程
            if (p.getIsActive() == 0)
            {
                delSubInfoByProcInstId(subProcInstId);
            }
            else
            {
                List<ProcessExecution> subProcessExecutionList = executionDao.getSubExecutionBySuperId(subProcInstId);
                if (subProcessExecutionList != null && subProcessExecutionList.size() == 1)
                {
                    ProcessExecution subProcessExecution = subProcessExecutionList.get(0);
                    
                    String subActinstid = subProcessExecution.getProcessInstanceId();
                    delSubInfoByProcInstId(subActinstid);
                    // 删除子流程根实例
                    proStatusDao.delByProcInstId(Long.parseLong(subActinstid));
                    dao.delHistoryByActinstid(subActinstid);
                    dao.delProByActinstid(subActinstid);
                    taskDao.delByInstanceId(Long.parseLong(subActinstid));
                    executionDao.delVariableByProcInstId(Long.parseLong(subActinstid));
                    executionDao.delExecutionById(subActinstid);
                }
            }
            dao.delHistoryByActinstid(subProcInstId);
            dao.delProByActinstid(subProcInstId);
            executionDao.delVariableByProcInstId(subProcInstIdL);
            // 删除task表数据
            taskDao.delByInstanceId(Long.parseLong(subProcInstId));
            executionDao.delExecutionByProcInstId(subProcInstIdL);
            
        }
        // 删除审批相关信息
        proStatusDao.delByProcInstId(Long.parseLong(procInstId));
        // 根据实例Id删除子流程历史
        dao.delSubHistoryByProcInstId(procInstId);
        // 根据流程实例Id删除非主线程的流程
        dao.delSubProByProcInstId(procInstId);
        
        // 删除流程 act_ru_identitylink 信息
        this.delRuIdentitylink(procInstId);
        // 根据流程实例删除任务。
        taskDao.delByInstanceId(Long.parseLong(procInstId));
        executionDao.delSubByProcInstId(procInstId);
        executionDao.delSubExecutionByProcInstId(Long.parseLong(procInstId));
    }
    
    private void delRuIdentitylink(String procInstId)
    {
        executionDao.delCandidateByProcInstId(procInstId);
        List<Task> list = this.taskService.createTaskQuery().processInstanceId(procInstId).list();
        for (Task t : list)
        {
            executionDao.delCandidateByTaskId(t.getId());
        }
    }
    
    /**
     * 描述: 在追回中重新分配任务
     * 
     * @param parentProcessInstance 流程实例
     * @param flowNode 开始节点
     * @author wzh
     */
    private void addTaskInRecover(ProcessInstance parentProcessInstance, FlowNode flowNode)
    {
        String procInstId = parentProcessInstance.getProcessInstanceId();
        // 删除非主线程相关信息
        delSubInfoByProcInstId(procInstId);
        
        String newTaskId = idGenerator.getNextId();
        Date curr = new Date();
        String currentId = UserContextUtil.getCurrentUserId().toString();
        String defId = parentProcessInstance.getProcessDefinitionId();
        // 创建新的任务给发起人
        ProcessTask newTask = new ProcessTask();
        newTask.setId(newTaskId);
        newTask.setProcessInstanceId(procInstId);
        newTask.setProcessDefinitionId(defId);
        newTask.setExecutionId(procInstId);
        newTask.setName(flowNode.getNodeName());
        newTask.setTaskDefinitionKey(flowNode.getNodeId());
        newTask.setPriority("50");
        newTask.setCreateTime(curr);
        newTask.setAssignee(currentId);
        newTask.setOwner(currentId);
        newTask.setDescription("-1");
        // 创建新的历史
        ProcessTaskHistory newTaskHistory = new ProcessTaskHistory();
        newTaskHistory.setId(newTaskId);
        newTaskHistory.setProcessDefinitionId(defId);
        newTaskHistory.setExecutionId(procInstId);
        newTaskHistory.setName(flowNode.getNodeName());
        newTaskHistory.setTaskDefinitionKey(flowNode.getNodeId());
        newTaskHistory.setStartTime(curr);
        newTaskHistory.setAssignee(currentId);
        newTaskHistory.setOwner(currentId);
        newTaskHistory.setDescription("-1");
        // BeanUtils.copyProperties(newTaskHistory, newTask);
        newTaskHistory.setStartTime(curr);
        
        ProStatus proStatus = new ProStatus();
        proStatus.setId(Long.parseLong(newTaskId));
        proStatus.setActinstid(Long.parseLong(procInstId));
        proStatus.setCreatetime(curr);
        proStatus.setNodeid(flowNode.getNodeId());
        proStatus.setNodename(flowNode.getNodeName());
        proStatus.setActdefid(defId);
        proStatus.setStatus(TaskOpinion.STATUS_RESUBMIT);// STATUS_RECOVER_TOSTART
        
        taskDao.insertTask(newTask);
        taskHistoryDao.add(newTaskHistory);
        executionDao.updateMainThread(procInstId, flowNode.getNodeId());
        proStatusDao.add(proStatus);
        
    }
    
    /**
     * 描述: 添加追回原因
     * 
     * @param processRun
     * @param memo
     * @author wzh
     */
    private void addRecoverOpinion(ProcessRun processRun, String memo)
    {
        TaskOpinion opinion = new TaskOpinion();
        Long startUserId = processRun.getCreatorId();
        ISysUser startUser = sysUserService.getById(startUserId);
        opinion.setOpinionId(UniqueIdUtil.genId());
        opinion.setSuperExecution(this.getSuperActInstId(processRun.getActInstId().toString()));
        opinion.setCheckStatus(TaskOpinion.STATUS_RESUBMIT);
        opinion.setActInstId(processRun.getActInstId());
        opinion.setExeFullname(startUser.getFullname());
        opinion.setExeUserId(startUserId);
        opinion.setOpinion(memo);
        opinion.setStartTime(processRun.getCreatetime());
        opinion.setTaskName(getText("service.processrun.retrieveFlow"));
        opinion.setEndTime(new Date());
        opinion.setDurTime(0L);
        taskOpinionService.add(opinion);
    }
    
    /**
     * 
     * 追认当前已完成任务
     * 
     * @param runId
     * @param informType
     * @param memo
     * @param isSaveForm 是否要保存表单
     * @return
     * @throws Exception
     */
    public ResultMessage redo(Long runId, String informType, String memo, boolean isSaveForm, boolean toStart)
        throws Exception
    {
        ProcessRun processRun = this.getById(runId);
        
        Short status = processRun.getStatus();
        if (status.shortValue() == ProcessRun.STATUS_FINISH.shortValue())
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.finish"));
        }
        if (status.shortValue() == ProcessRun.STATUS_MANUAL_FINISH.shortValue())
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.del"));
        }
        // 判断当前人是否为发起人。
        // 如果当前人为发起人那么可以撤回所有的流程。
        /*
         * 
         * boolean isCreator = processRun.getCreatorId().longValue() == UserContextUtil .getCurrentUserId().longValue();
         * if (!isCreator) { return new ResultMessage(ResultMessage.Fail, getText("service.processrun.recoverStart")); }
         */
        // 获取开始节点。
        FlowNode flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
        String nodeId = flowNode.getNodeId();
        
        List<ProcessTask> taskList = bpmService.getTasks(processRun.getActInstId());
        List<String> taskNodeIdList = getNodeIdByTaskList(taskList);
        
        if (taskNodeIdList.contains(nodeId))
        {
            // 追回的节点包含了开始节点
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.isStartNode"));
        }
        
        boolean hasRead = getTaskHasRead(taskList);
        if (hasRead && "".equals(memo))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.recoverOption"));
        }
        // 判断是否允许返回。
        boolean allowBack = getTaskAllowBack(taskList);
        if (!allowBack)
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.notAllowedRevoked"));
        }
        backToStart(memo, taskList, informType, isSaveForm, toStart);
        return new ResultMessage(ResultMessage.Success, getText("service.processrun.recoversuccess"));
    }
    
    private List<String> getNodeIdByTaskList(List<ProcessTask> taskList)
    {
        List<String> list = new ArrayList<String>();
        for (ProcessTask task : taskList)
        {
            list.add(task.getTaskDefinitionKey());
        }
        return list;
    }
    
    /**
     * 撤销到开始节点。
     * 
     * @param memo
     * @param taskList
     * @param isSaveForm 是否要保存表单
     * @throws Exception
     */
    private void backToStart(String memo, List<ProcessTask> taskList, String informType, boolean isSaveForm,
        boolean toStart)
        throws Exception
    {
        List<Task> taskEntityList = new ArrayList<Task>();
        ProcessRun processRun = this.getByActInstanceId(Long.parseLong(taskList.get(0).getProcessInstanceId()));
        for (ProcessTask task : taskList)
        {
            Task taskEntity = bpmService.getTask(task.getId());
            taskEntityList.add(taskEntity);
        }
        // 发送撤销提醒
        ISysTemplate sysTemplate = sysTemplateService.getDefaultByUseType(ISysTemplate.USE_TYPE_REVOKED);
        Map<String, String> msgTempMap = this.getTempByUseType(ISysTemplate.USE_TYPE_REVOKED);
        taskMessageService.notify(taskEntityList, informType, processRun.getSubject(), msgTempMap, memo, "");
        // 4.在当前运行任务中，查找其父节点为以上树的，则允许撤销
        for (int i = 0; i < taskList.size(); i++)
        {
            ProcessTask taskEntity = taskList.get(i);
            // 加上回退状态，使其通过任务完成事件中记录其是撤销的状态
            ProcessCmd processCmd = new ProcessCmd();
            processCmd.setTaskId(taskEntity.getId());
            // 设置为撤销
            /*
             * 这里不应该是撤销，如果是撤销的，我的请求中数据状态就会是已撤销，撤销流程应该是发起人才能操作，而且是直接回到发起节点 processCmd.setRecover(true);
             */
            // 状态有点问题
            processCmd.setRedo(true);
            
            if (toStart)
            {
                processCmd.setBack(BpmConst.TASK_BACK_TOSTART);
                processCmd.setVoteAgree(TaskOpinion.STATUS_RECOVER_TOSTART);
            }
            else
            {
                // 这里应该从ibms_exe_stack 堆栈中 获取 回退到上一步任务节点
                ExecutionStack executionStack = this.executionStackService.getLastestStack(processRun.getActInstId(),
                    taskEntity.getTaskDefinitionKey());
                processCmd.setBack(BpmConst.TASK_BACK);
                processCmd.setVoteAgree(TaskOpinion.STATUS_RECOVER);
                // 通过 ibms_exe_stack 执行堆栈，原路退回到任务节点
                ExecutionStack parentStack = executionStackService.getById(executionStack.getParentId());
                processCmd.setDestTask(parentStack.getNodeId());
                
            }
            
            processCmd.setVoteContent(memo);
            processCmd.setInformType(informType);
            // 第一个任务跳转回来，其他直接完成任务。
            if (i > 0)
            {
                processCmd.setOnlyCompleteTask(true);
            }
            // 进行回退处理
            this.nextProcess(processCmd, isSaveForm);
            // 判断是否为别人转办的任务 如果是从交办记录表中标记为取消记录
            taskExeService.cancel(new Long(taskEntity.getId()));
        }
    }
    
    private Map<String, String> getTempByUseType(Integer useType)
        throws Exception
    {
        ISysTemplate temp = sysTemplateService.getDefaultByUseType(useType);
        if (temp == null)
            throw new Exception(getText("service.processrun.nifindTemplate"));
        
        Map<String, String> map = new HashMap<String, String>();
        map.put(ISysTemplate.TEMPLATE_TITLE, temp.getTitle());
        map.put(ISysTemplate.TEMPLATE_TYPE_HTML, temp.getHtmlContent());
        map.put(ISysTemplate.TEMPLATE_TYPE_PLAIN, temp.getPlainContent());
        
        return map;
    }
    
    /**
     * 根据任务列表是否已读。
     * 
     * @param list
     * @return
     */
    private boolean getTaskHasRead(List<ProcessTask> list)
    {
        boolean rtn = false;
        for (ProcessTask task : list)
        {
            List<TaskRead> readList =
                taskReadDao.getTaskRead(new Long(task.getProcessInstanceId()), new Long(task.getId()));
            if (BeanUtils.isNotEmpty(readList))
            {
                rtn = true;
                break;
            }
        }
        return rtn;
    }
    
    /**
     * 取得任务列表是否允许驳回。
     * 
     * @param list
     * @return
     */
    private boolean getTaskAllowBack(List<ProcessTask> list)
    {
        for (ProcessTask task : list)
        {
            boolean allBack = bpmService.getIsAllowBackByTask(task);
            if (allBack)
            {
                return true;
            }
        }
        return false;
        
    }
    
    public ProcessRun getByBusinessKey(Long formKey, String businessKey)
    {
        if (StringUtil.isNotEmpty(businessKey))
        {
            ProcessRun processRun = dao.getByBusinessKey(formKey, businessKey);
            return processRun;
        }
        else
        {
            return null;
        }
        
    }
    
    public ProcessRun getByBusinessKeyAndFormDef(String businessKey, String formdefid)
    {
        if (StringUtil.isNotEmpty(businessKey) && StringUtil.isNotEmpty(formdefid))
        {
            List<ProcessRun> processRunList = dao.getByBusinessKeyAndFormDef(businessKey, formdefid);
            if (processRunList != null && processRunList.size() > 0)
            {
                return (ProcessRun)processRunList.get(0);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public ProcessRun getByBusinessKeyAndDefId(String businessKey, Long defId)
    {
        if (StringUtil.isNotEmpty(businessKey))
        {
            List<ProcessRun> processRunList = dao.getByBusinessKeyAndDefId(businessKey, defId);
            if (processRunList != null && processRunList.size() > 0)
            {
                return (ProcessRun)processRunList.get(processRunList.size() - 1);
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
    }
    
    public void copyDraft(Long runId)
        throws Exception
    {
        ProcessRun processRun = getById(runId);
        Long newRunId = UniqueIdUtil.genId();
        processRun.setRunId(newRunId);
        String businessKey = processRun.getBusinessKey();
        String newBusKey = Long.toString(UniqueIdUtil.genId());
        IFormTable bpmFormTable =
            formTableService.getByAliasTableName(processRun.getDsAlias(), processRun.getTableName());
        if (BeanUtils.isNotEmpty(bpmFormTable))
        {
            // 查找源数据(未经处理的源数据 便于后面的插入数据)
            IDataTemplate dataTemplate =
                dataTemplateService.getByFormKey(formDefService.getById(processRun.getFormDefId()).getFormKey());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("dataTemplate", dataTemplate);
            IFormData bpmFormData = formHandlerService.getByKey(params, bpmFormTable.getTableId(), businessKey, false);
            // 设置新的主键
            bpmFormData
                .setPkValue(formHandlerService.getNewPkValue(bpmFormData.getPkValue().getName(), newBusKey, true));
            
            Map<String, Object> mainData = bpmFormData.getMainFields();
            mainData.put(bpmFormData.getPkValue().getName(), newBusKey);
            bpmFormData.setMainFields(mainData);
            // 子表数据绑定 主表新主键
            List<? extends ISubTable> subTableList = bpmFormData.getSubTableList();
            List<ISubTable> newSubTables = new ArrayList<ISubTable>();
            for (ISubTable subTable : subTableList)
            {
                List<Map<String, Object>> subDatas = subTable.getDataList();
                List<Map<String, Object>> newSubDatas = new ArrayList<Map<String, Object>>();
                for (Map<String, Object> subData : subDatas)
                {
                    subData.put(subTable.getFkName(), newBusKey);
                    subData.put(subTable.getPkName(), Long.toString(UniqueIdUtil.genId()));
                    newSubDatas.add(subData);
                }
                subTable.setDataList(newSubDatas);
                newSubTables.add(subTable);
            }
            bpmFormData.setSubTableList(newSubTables);
            formHandlerService.handFormData(params, bpmFormData);
            processRun.setBusinessKey(newBusKey);
            processRun.setCreatetime(new Date());
            this.add(processRun);
        }
    }
    
    /**
     * 流程任务 管理员更改执行人
     * 
     * @param taskId
     * @param userId
     * @param voteContent
     * @param informType
     * @throws Exception
     */
    public ResultMessage updateTaskAssignee(String taskId, String userId, String voteContent, String informType)
        throws Exception
    {
        TaskEntity taskEntity = bpmService.getTask(taskId);
        String originUserId = taskEntity.getAssignee();
        if (BeanUtils.isEmpty(taskEntity))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.taskNotExist"));
        }
        if (userId.equals(originUserId))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.notSame"));
        }
        String actDefId = taskEntity.getProcessDefinitionId();
        Definition bpmDefinition = definitionDao.getByActDefId(actDefId);
        if (Definition.STATUS_INST_DISABLED.equals(bpmDefinition.getStatus()))
        {
            return new ResultMessage(ResultMessage.Fail, getText("service.processrun.notChange"));
        }
        bpmService.updateTaskAssignee(taskId, userId);
        // 记录日志。
        ProcessRun processRun = this.getByActInstanceId(new Long(taskEntity.getProcessInstanceId()));
        Long runId = processRun.getRunId();
        
        String originUserName = "";
        if (FlowUtil.isAssigneeNotEmpty(originUserId))
        {
            
            ISysUser originUser = sysUserService.getById(Long.parseLong(originUserId));
            originUserName = originUser.getFullname();
        }
        ISysUser user = sysUserService.getById(Long.parseLong(userId));
        ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
        
        String memo = getText("service.processrun.updateTaskAssignee.mome1",
            new Object[] {processRun.getSubject(), curUser.getFullname(), taskEntity.getName()});
        if (StringUtil.isNotEmpty(originUserName))
        {
            memo += "," + getText("service.processrun.updateTaskAssignee.mome2", new Object[] {originUserName});
        }
        memo += getText("service.processrun.updateTaskAssignee.mome3", new Object[] {user.getFullname()});
        runLogService.addRunLog(runId, RunLog.OPERATOR_TYPE_ASSIGN, memo);
        
        memo = getText("service.processrun.updateTaskAssignee.mome1",
            new Object[] {processRun.getSubject(), curUser.getFullname(), taskEntity.getName()});
        if (StringUtil.isNotEmpty(originUserName))
        {
            memo += "," + getText("service.processrun.updateTaskAssignee.mome2", new Object[] {originUserName});
        }
        memo += getText("service.processrun.updateTaskAssignee.mome3", new Object[] {user.getFullname()});
        runLogService.addRunLog(user, runId, RunLog.OPERATOR_TYPE_ASSIGN, memo);
        String content = memo + getText("service.processrun.updateTaskAssignee.mome4", new Object[] {voteContent});
        // 添加审批历史
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setOpinion(content);
        taskOpinion.setTaskId(Long.parseLong(taskId));
        taskOpinion.setActDefId(processRun.getActDefId());
        taskOpinion.setActInstId(processRun.getActInstId());
        taskOpinion.setStartTime(new Date());
        taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
        taskOpinion.setOpinionId(UniqueIdUtil.genId());
        taskOpinion.setTaskName(taskEntity.getName());
        taskOpinion.setExeUserId(curUser.getUserId());
        taskOpinion.setExeFullname(curUser.getFullname());
        taskOpinion.setCheckStatus(TaskOpinion.STATUS_CHANGE_ASIGNEE);
        dealTaskOpinSupId(taskEntity, taskOpinion);
        taskOpinionDao.add(taskOpinion);
        
        List<ISysUser> receiverUser = new ArrayList<ISysUser>();
        receiverUser.add(user);
        String title = getText("service.processrun.updateTaskAssignee.mome5", new Object[] {processRun.getSubject()});
        taskMessageService.sendMessage(curUser, receiverUser, informType, title, content);
        return new ResultMessage(ResultMessage.Success, getText("service.processrun.updateTaskAssignee.changeSuc"));
    }
    
    public void saveAddSignOpinion(TaskEntity taskEntity, String opinion, String informType, Map<Long, Long> userTaskID,
        String subject)
        throws Exception
    {
        String taskId = taskEntity.getId();
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        Long opinionId = Long.valueOf(UniqueIdUtil.genId());
        
        TaskOpinion taskOpinion = new TaskOpinion();
        taskOpinion.setOpinionId(opinionId);
        taskOpinion.setOpinion(opinion);
        taskOpinion.setActDefId(taskEntity.getProcessDefinitionId());
        taskOpinion.setActInstId(taskEntity.getProcessInstanceId());
        taskOpinion.setStartTime(new Date());
        taskOpinion.setEndTime(new Date());
        taskOpinion.setExeUserId(sysUser.getUserId());
        taskOpinion.setExeFullname(sysUser.getFullname());
        taskOpinion.setTaskKey(taskEntity.getTaskDefinitionKey());
        taskOpinion.setTaskName(taskEntity.getName());
        taskOpinion.setCheckStatus(TaskOpinion.STATUS_RETROACTIVE);
        
        dealTaskOpinSupId(taskEntity, taskOpinion);
        
        this.taskOpinionDao.add(taskOpinion);
        
        this.commuReceiverService.saveReceiver(opinionId, userTaskID, sysUser);
        
        notifyCommu(subject, userTaskID, informType, sysUser, opinion, ISysTemplate.USE_TYPE_RETROACTIVE);
    }
    
    public void updateTaskDescription(String description, String taskId)
    {
        taskDao.updateTaskDescription(description, taskId);
    }
    
    public void delTransToTaskByParentTaskId(String parentTaskId)
    {
        taskDao.delTransToTaskByParentTaskId(parentTaskId);
    }
    
    public List<TaskEntity> getByParentTaskIdAndDesc(String parentTaskId, String description)
    {
        return taskDao.getByParentTaskIdAndDesc(parentTaskId, description);
    }
    
    public List<ProcessTask> getTasksByRunId(Long runId)
    {
        return taskDao.getTasksByRunId(runId);
    }
    
    public ProcessTask getByTaskId(Long taskId)
    {
        return taskDao.getByTaskId(String.valueOf(taskId));
    }
    
    public boolean getHasRightsByTask(Long taskId, Long userId)
    {
        return taskDao.getHasRightsByTask(taskId, userId);
    }
    
    public List<ProcessRun> getTestRunsByActDefId(String actDefId)
    {
        return dao.getTestRunsByActDefId(actDefId);
    }
    
    public ProcessTask getTaskByParentIdAndUser(String parentTaskId, String userId)
    {
        return taskDao.getTaskByParentIdAndUser(parentTaskId, userId);
    }
    
    /**
     * 获取父流程的实例ID
     * 
     * @param actInstId 流程的实例ID
     * @return
     */
    public String getSuperActInstId(String actInstId)
    {
        if (actInstId == null)
        {
            return null;
        }
        ProcessInstance processInstance =
            runtimeService.createProcessInstanceQuery().subProcessInstanceId(actInstId).singleResult();
        if (processInstance == null)
        {
            return null;
        }
        String supInstId = processInstance.getProcessInstanceId();
        return supInstId;
    }
    
    public String getParentProcessRunActDefId(Long runId)
    {
        ProcessRun processRun = dao.getById(runId);
        return getParentProcessRunActDefId(processRun);
    }
    
    public String getParentProcessRunActDefId(ProcessRun processRun)
    {
        return dao.getParentProcessRunActDefId(processRun);
    }
    
    public List<ProcessRun> getMyFlowsAndCptoList(QueryFilter filter)
    {
        return this.dao.getMyFlowsAndCptoList(filter);
    }
    
    public List<ProcessRun> getMyCompletedAndCptoList(QueryFilter filter)
    {
        return this.dao.getMyCompletedAndCptoList(filter);
    }
    
    @Override
    public List<ProcessRun> getMyAttend(Long assignee, Short status, PagingBean pb)
    {
        return this.dao.getMyAttend(assignee, status, pb);
    }
    
    @Override
    public List<ProcessRun> myStart(Long creatorId, PagingBean pb)
    {
        return this.dao.myStart(creatorId, pb);
    }
    
    @Override
    public List<ProcessRun> myCompleted(long curUserId, PagingBean pb)
    {
        return this.dao.myCompleted(curUserId, pb);
    }
    
    @Override
    public List<ProcessRun> myAlready(long assignee, PagingBean pb)
    {
        return this.dao.Myalready(assignee, pb);
    }
    
    /**
     * 工作台显示办结事宜
     * 
     * @param assignee
     * @param pb
     * @return
     */
    @Override
    public List<ProcessRun> completedMatters(Long assignee, PagingBean pb)
    {
        return this.dao.completedMatters(assignee, pb);
    }
    
    @Override
    public List<? extends IProcessRun> getByFlowKey(String flowKey, String businessKey)
    {
        // TODO Auto-generated method stub
        if (StringUtil.isNotEmpty(businessKey) && StringUtil.isNotEmpty(flowKey))
        {
            List<ProcessRun> processRunList = dao.getByFlowKey(flowKey, businessKey);
            if (processRunList != null && processRunList.size() > 0)
            {
                return processRunList;
            }
            else
            {
                return null;
            }
        }
        else
        {
            return null;
        }
        // return this.dao.getByFlowKey(flowKey);
    }
    
    /**
     * RPC服务调用启动流程方法
     */
    public String startProcessRpc(String taskId, String flowKey, String pk, String userId,
        Map<String, Object> variables)
    {
        
        ProcessRun processRun = null;
        try
        {
            ProcessCmd processCmd = new ProcessCmd();
            if (StringUtil.isNotEmpty(taskId))
            {
                processCmd.setTaskId(taskId);
            }
            if (StringUtil.isNotEmpty(flowKey))
            {
                processCmd.setFlowKey(flowKey);
            }
            if (StringUtil.isNotEmpty(pk))
            {
                processCmd.setBusinessKey(pk);
            }
            if (variables != null)
            {
                processCmd.setVariables(variables);
            }
            processCmd.setCurrentUserId(userId);
            Definition bpmDefinition = definitionService.getMainDefByActDefKey(flowKey);
            // 通知类型
            if (BeanUtils.isNotEmpty(bpmDefinition))
            {
                String informType = bpmDefinition.getInformType();
                processCmd.setInformType(informType);
            }
            processRun = this.startProcess((ProcessCmd)processCmd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return processRun.getActInstId();
    }
    
    /**
     * RPC服务调用启动流程方法
     */
    public String startProcessRpc(IProcessCmd processCmd)
    {
        
        ProcessRun processRun = null;
        try
        {
            processRun = this.startProcess((ProcessCmd)processCmd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return processRun.getActInstId();
    }
    
    /**
     * 启动流程页面
     * 
     * @param request TODO
     * @param mv
     * @param userId TODO
     * @param title
     * @return
     * @throws Exception
     */
    public ModelAndView startFlowView(HttpServletRequest request, ModelAndView mv, String flowKey, Long userId)
        throws Exception
    {
        Long defId = RequestUtil.getLong(request, "defId");
        String headHtml = "";
        String ctxPath = request.getContextPath();
        ProcessRun processRun = null;
        Definition bpmDefinition = null;
        Long runId = RequestUtil.getLong(request, "runId", 0L);
        Long displayId = RequestUtil.getLong(request, "__displayId__");
        
        if (BeanUtils.isNotEmpty(defId) && defId != 0l)
        {
            bpmDefinition = definitionService.getById(defId);
        }
        else if (BeanUtils.isNotEmpty(flowKey))
        {
            bpmDefinition = definitionService.getByDefKey(flowKey);
        }
        else if (BeanUtils.isNotEmpty(displayId) && defId != 0l)
        {
            IDataTemplate bpmDataTemplate = dataTemplateService.getById(displayId);
            defId = bpmDataTemplate.getDefId();
            bpmDefinition = definitionService.getById(defId);
        }
        else if (BeanUtils.isNotEmpty(runId))
        {
            
            processRun = processRunService.getById(runId);
            defId = processRun.getDefId();
            bpmDefinition = definitionService.getById(processRun.getDefId());
        }
        if (bpmDefinition == null)
        {
            throw new IllegalStateException("未找到流程定义，请检查！");
        }
        // 流程草稿传入
        String businessKey = RequestUtil.getString(request, "businessKey", "");
        
        // 复制表单 启动流程
        String copyKey = RequestUtil.getString(request, "copyKey", "");
        ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
        Map<String, Object> paraMap = RequestUtil.getParameterValueMap(request, false, false);
        paraMap.remove("businessKey");
        paraMap.remove("defId");
        
        if (BeanUtils.isEmpty(bpmDefinition))
        {
            return RequestUtil.getTipInfo(getText("controller.task.startFlowForm.del"));
        }
        if (bpmDefinition.getStatus().shortValue() == Definition.STATUS_DISABLED.shortValue()
            || bpmDefinition.getStatus().shortValue() == Definition.STATUS_INST_DISABLED.shortValue())
        {
            return RequestUtil.getTipInfo(getText("controller.task.startFlowForm.disable"));
        }
        Boolean isFormEmpty = false;
        Boolean isExtForm = false;
        String form = "";
        Long formKey = 0L;
        // 通过草稿启动流程
        if (BeanUtils.isNotEmpty(processRun)
            && processRun.getStatus().shortValue() == ProcessRun.STATUS_FORM.shortValue())
        {
            mv.addObject("isDraft", false);
            businessKey = processRun.getBusinessKey();
            Long formDefId = processRun.getFormDefId();
            String actDefId = processRun.getActDefId();
            int isNewVersion = RequestUtil.getInt(request, "isNewVersion", 0);
            if (formDefId != 0L)
            {
                boolean isExistsData =
                    formHandlerService.isExistsData(processRun.getTableName(), processRun.getBusinessKey());
                if (!isExistsData)
                    return new ModelAndView("redirect:noData.do");
            }
            
            if (StringUtil.isNotEmpty(processRun.getBusinessUrl()))
            {
                isExtForm = true;
                form = processRun.getBusinessUrl();
                // 替换主键。
                form = processRun.getBusinessUrl().replaceFirst(BpmConst.FORM_PK_REGEX, businessKey);
                if (!form.startsWith("http"))
                {
                    form = ctxPath + form;
                }
            }
            else
            {
                if (isNewVersion == 1)
                {
                    IFormDef defaultFormDef = formDefService.getById(formDefId);
                    formDefId = formDefService.getDefaultPublishedByFormKey(defaultFormDef.getFormKey()).getFormDefId();
                }
                IDataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formDefId);
                if (bpmDataTemplate != null)
                {
                    headHtml = dataTemplateService.getFormHeadHtml(bpmDataTemplate.getId(), ctxPath);
                }
                
                FlowNode flowNode = NodeCache.getFirstNodeId(actDefId); // 流程第一个节点
                String nodeId = flowNode.getNodeId(); // 流程第一个节点名称
                form = formHandlerService
                    .obtainHtml(formDefId, userId, businessKey, "", actDefId, nodeId, ctxPath, "", false);
            }
            // 流程定义里面的启动
        }
        else
        {
            if (StringUtil.isNotEmpty(copyKey))
                businessKey = copyKey;
            mv.addObject("isDraft", true);
            String actDefId = bpmDefinition.getActDefId();
            
            // 获取表单节点
            NodeSet bpmNodeSet = nodeSetService.getStartNodeSet(bpmDefinition.getDefId(), actDefId);
            
            IFormModel formModel = formHandlerService.getStartForm(bpmNodeSet, businessKey, actDefId, ctxPath, userId);
            
            IDataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(bpmNodeSet.getFormKey());
            
            if (bpmDataTemplate != null)
            {
                headHtml = dataTemplateService.getFormHeadHtml(bpmDataTemplate.getId(), ctxPath);
              //定制 去除游览器缓存
                String[] headjsArray=headHtml.split("\n");
                String headjs="";
                if(headjsArray!=null&&headjsArray.length!=0) {
                	for (String string : headjsArray) {
                		int num=string.indexOf(".js"); 
                		if(num!=-1) {
                        	Random random=new Random();
                        	headjs+=string.substring(0,num+3)+"?num="+random.nextLong()+string.substring(num+3,string.length());
                        }
    				} 	
                }
                headHtml=headjs;
                
            }
            // 是外部表单
            isFormEmpty = formModel.isFormEmpty();
            isExtForm = formModel.getFormType() > 0;
            
            if (isExtForm)
            {
                form = formModel.getFormUrl();
            }
            else if (formModel.getFormType() == 0)
            {
                form = formModel.getFormHtml();
            }
            if (BeanUtils.isNotEmpty(bpmNodeSet))
            {
                mv.addObject("formKey", bpmNodeSet.getFormKey());
                mv.addObject("tableId", bpmNodeSet.getFormDef().getTableId());
            }
        }
        // 获取按钮
        Map<String, List<NodeButton>> mapButton = nodeButtonService.getMapByStartForm(defId);
        // 帮助文档
        ISysFile sysFile = null;
        if (BeanUtils.isNotEmpty(bpmDefinition.getAttachment()))
        {
            sysFile = sysFileService.getById(bpmDefinition.getAttachment());
        }
        // 通过defid和nodeId获取联动设置
        List<GangedSet> bpmGangedSets = gangedSetService.getByDefIdAndNodeId(defId, GangedSet.START_NODEID);
        JSONArray gangedSetJarray = (JSONArray)JSONArray.fromObject(bpmGangedSets);
        mv.addObject("bpmDefinition", bpmDefinition)
            .addObject("isExtForm", isExtForm)
            .addObject("isFormEmpty", isFormEmpty)
            .addObject("mapButton", mapButton)
            .addObject("defId", defId)
            .addObject("paraMap", paraMap)
            .addObject("form", form)
            .addObject("runId", runId)
            .addObject("businessKey", StringUtil.isEmpty(copyKey) ? businessKey : "")
            .addObject("sysFile", sysFile)
            .addObject("bpmGangedSets", gangedSetJarray)
            .addObject("curUserId", sysUser.getUserId().toString())
            .addObject("curUserName", sysUser.getFullname())
            .addObject("curUserLoginName", sysUser.getUsername())
            .addObject("headHtml", headHtml)
            .addObject("curDate", DateUtil.getDateString(new Date(), "yyyy-MM-dd"))
            .addObject("curDateTime", DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return mv;
    }
    
    /**
     * 获取用户的代办任务
     * 
     * @param userId
     * @return
     */
    @Override
    public int getPendingMattersList(Long userId)
    {
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("userId", userId);
        List<?> list = this.taskDao.getBySqlKey("getAllMyTask", pram);
        if (list != null)
        {
            return list.size();
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * 获取用户的请求
     * 
     * @param userId
     * @return
     */
    @Override
    public int getMyRequestList(Long userId)
    {
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("creatorId", userId);
        List<?> list = this.dao.getBySqlKey("getMyRequestList", pram);
        if (list != null)
        {
            return list.size();
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * 通过业务数据主键，业务数据模板获取流程实例
     * 
     * @param actDefId
     * @param pk
     * @return
     */
    @Override
    public List<? extends IProcessRun> getByActDefIdAndPk(String actDefId, String pk)
    {
        Map<String, Object> pram = new HashMap<String, Object>();
        pram.put("actDefId", actDefId);
        pram.put("pk", pk);
        List<ProcessRun> list = this.dao.getBySqlKey("getByActDefIdAndPk", pram);
        return list;
    }
    
    public IFormDef gerFormDefByActDefId(String actDefId)
    {
        INodeSet nodeset = nodeSetService.getBySetTypeAndActDefId(actDefId, NodeSet.SetType_GloabalForm);
        IFormDef formDef = formDefService.getById(nodeset.getFormKey());
        return formDef;
        
    }
    
    public ProcessRun getByBusinessKey(String businessKey)
    {
        return dao.getByBusinessKey(businessKey);
    }
    
    @Override
    public void deleteFormData(Long formKey, String pk)
    {
        this.dao.deleteFormData(formKey, pk);
    }

    public ProcessRunDao getDao()
    {
        return dao;
    }

    
    
}
