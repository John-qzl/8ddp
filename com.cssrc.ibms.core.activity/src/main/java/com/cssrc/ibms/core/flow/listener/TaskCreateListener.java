package com.cssrc.ibms.core.flow.listener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;

import com.cssrc.ibms.api.activity.intf.IExtTaskExecuteService;
import com.cssrc.ibms.api.activity.intf.IExtTaskUserAssignService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.dao.NodeSetDao;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.TaskFork;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.AgentSettingService;
import com.cssrc.ibms.core.flow.service.BpmService;
import com.cssrc.ibms.core.flow.service.NodeUserService;
import com.cssrc.ibms.core.flow.service.ProStatusService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.TaskForkService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;
import com.cssrc.ibms.core.flow.service.thread.TaskUserAssignService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 一般任务开始事件监听器。
 * 
 * <pre>
 * 1.携带上一任务的分发令牌。
 * 2.记录新产生的任务，为后续的回退作准备。
 * 3.添加流程意见。
 * 4.启动流程时添加或保存状态数据。
 * 5.如果当前节点是分发节点则进行任务分发，并直接返回，否则往下执行。
 * 6.从上下文中获取分配人员的数据，进行分配，如果有人员则进行分配，如果没有则往下执行。
 * 7.从数据库中加载人员进行人员分配。
 * 
 * </pre>
 * 
 * @author zhulongchao
 *
 */
public class TaskCreateListener extends BaseTaskListener
{
    
    private TaskOpinionService taskOpinionService = (TaskOpinionService)AppUtil.getBean(TaskOpinionService.class);
    
    private TaskUserAssignService taskUserAssignService =
        (TaskUserAssignService)AppUtil.getBean(TaskUserAssignService.class);
    
    private TaskForkService taskForkService = (TaskForkService)AppUtil.getBean(TaskForkService.class);
    
    private NodeSetDao bpmNodeSetDao = (NodeSetDao)AppUtil.getBean(NodeSetDao.class);
    
    private BpmService bpmService = (BpmService)AppUtil.getBean(BpmService.class);
    
    private ProStatusService bpmProStatusService = (ProStatusService)AppUtil.getBean(ProStatusService.class);
    
    private AgentSettingService agentSettingService = (AgentSettingService)AppUtil.getBean(AgentSettingService.class);
    
    private ISysUserService sysUserService = (ISysUserService)AppUtil.getBean(ISysUserService.class);
    
    private ProcessRunService processRunService = (ProcessRunService)AppUtil.getBean(ProcessRunService.class);
    
    private INodeSetService nodeSetService = (INodeSetService)AppUtil.getBean(INodeSetService.class);
    
    private IFormHandlerService formHandlerService = (IFormHandlerService)AppUtil.getBean(IFormHandlerService.class);
    
    @Override
    protected void execute(DelegateTask delegateTask, String actDefId, String nodeId)
    {
        // 设置任务状态。
        delegateTask.setDescription(TaskOpinion.STATUS_CHECKING.toString());
        // setTaskStatus(TaskThreadService.getProcessCmd(), delegateTask);
        // 携带上一任务的分发令牌
        String token = TaskThreadService.getToken();
        if (token != null)
        {
            delegateTask.setVariableLocal(TaskFork.TAKEN_VAR_NAME, token);
        }
        // 记录新产生的任务，为后续的回退作准备
        TaskThreadService.addTask((TaskEntity)delegateTask);
        // 生成任务签批意见
        addOpinion(token, delegateTask);
        
        // 流程控件字段-显示值修改
        handProcessStateValue(delegateTask, actDefId, nodeId);
        
        Long actInstanceId = new Long(delegateTask.getProcessInstanceId());
        
        // 启动流程时添加或保存状态数据。
        bpmProStatusService.addOrUpd(actDefId, actInstanceId, nodeId);
        // 获取当前和下一任务节点执行人数组（注意这个数组的空值情况）
        Map<String, List<TaskExecutor>> nodeUserMap = taskUserAssignService.getNodeUserMap();
        
        boolean isHandlerExtTaskUser = handlerExtTaskUser(delegateTask, actDefId, nodeId);
        // 如果扩展接口中已经处理了人员，则跳过
        if (isHandlerExtTaskUser)
        {
            return;
        }
        // 处理任务分发。
        boolean isHandForkTask = handlerForkTask(actDefId, nodeId, nodeUserMap, delegateTask);
        if (isHandForkTask)
        {
            return;
        }
        
        boolean isSubProcess = handSubProcessUser(delegateTask);
        if (isSubProcess)
        {
            return;
        }
        
        // 处理外部子流程用户。
        boolean isHandExtUser = handExtSubProcessUser(delegateTask);
        if (isHandExtUser)
        {
            return;
        }
        
        // 在上一步中指定了该任务的执行人员
        if (nodeUserMap != null && nodeUserMap.get(nodeId) != null)
        {
            List<TaskExecutor> executorIds = nodeUserMap.get(nodeId);
            assignUser(delegateTask, executorIds);
            return;
        }
        
        List<TaskExecutor> executorUsers = taskUserAssignService.getExecutors();
        // 当前执行人。
        if (BeanUtils.isNotEmpty(executorUsers))
        {
            assignUser(delegateTask, executorUsers);
            return;
        }
        // 处理从数据库加载用户，并进行分配。 --edit by zxg 2017-03-11
        handAssignUserFromDb(actDefId, nodeId, delegateTask);
    }
    
    @Override
    protected void handlerExtTaskExeCallBack(TaskEntity taskEnt)
    {
        ExecutionEntity excution = taskEnt.getExecution();
        String processInstanceId = excution.getProcessInstanceId();
        try
        {
            IExtTaskExecuteService exeTaskService = AppUtil.getBean(IExtTaskExecuteService.class);
            if (exeTaskService != null)
            {
                ProcessRun processRun = this.processRunService.getByActInstanceId(Long.valueOf(processInstanceId));
                String businessKey = processRun.getBusinessKey();
                exeTaskService.createTaskCallBack(excution, taskEnt, businessKey);
            }
        }
        catch (Exception e)
        {
        }
        
    }
    
    private boolean handlerExtTaskUser(DelegateTask delegateTask, String actDefId, String nodeId)
    {
        ExecutionEntity excution = (ExecutionEntity)delegateTask.getExecution();
        String defKey = excution.getProcessDefinition().getKey();
        String processInstanceId = excution.getProcessInstanceId();
        try
        {
            IExtTaskUserAssignService assignService = AppUtil.getBean(IExtTaskUserAssignService.class);
            if (assignService != null)
            {
                ProcessRun processRun = this.processRunService.getByActInstanceId(Long.valueOf(processInstanceId));
                String businessKey = processRun.getBusinessKey();
                List<TaskExecutor> taskExecutors =
                    (List<TaskExecutor>)assignService.getTaskExecutor(actDefId, defKey, nodeId, businessKey);
                if (taskExecutors != null && taskExecutors.size() > 0)
                {
                    assignUser(delegateTask, taskExecutors);
                    return true;
                }
            }
            
        }
        catch (Exception e)
        {
        }
        return false;
        
    }
    
    private void handProcessStateValue(DelegateTask delegateTask, String actDefId, String nodeId)
    {
        ExecutionEntity excution = (ExecutionEntity)delegateTask.getExecution();
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
        if (businessKey != null)
        {
            INodeSet nodeSet = (INodeSet)nodeSetService.getByActDefIdNodeId(actDefId, nodeId);
            formHandlerService.updateProcessData(actDefId + IFieldPool.FLOW_SPLIT + nodeId,
                businessKey.toString(),
                nodeSet.getFormKey());
        }
        
    }
    
    /**
     * 处理内部子流程的人员分配。
     * 
     * <pre>
     * 	将任务的执行变量人取出，指定给任务执行人。
     * </pre>
     * 
     * @param delegateTask
     * @return
     */
    private boolean handSubProcessUser(DelegateTask delegateTask)
    {
        FlowNode flowNode =
            NodeCache.getByActDefId(delegateTask.getProcessDefinitionId()).get(delegateTask.getTaskDefinitionKey());
        boolean isMultipleNode = flowNode.getIsMultiInstance();
        if (!isMultipleNode)
            return false;
        // 若为多实例子流程中的任务，则从线程中的人员取出，并且把该人员从线程中删除
        TaskExecutor taskExecutor = (TaskExecutor)delegateTask.getVariable("assignee");
        if (taskExecutor != null)
        {
            // 分配任务执行人。
            assignUser(delegateTask, taskExecutor);
            
            int completeInstance = (Integer)delegateTask.getVariable("nrOfCompletedInstances");
            int nrOfInstances = (Integer)delegateTask.getVariable("nrOfInstances");
            // 清空该人员集合
            if (completeInstance == nrOfInstances)
            {
                delegateTask.removeVariable(BpmConst.SUBPRO_MULTI_USERIDS);
            }
        }
        return true;
    }
    
    /**
     * 外部子流程流程多实例任务人员分配。
     * 
     * @param delegateTask
     * @return
     * @throws Exception
     */
    private boolean handExtSubProcessUser(DelegateTask delegateTask)
    {
        ExecutionEntity executionEnt = (ExecutionEntity)delegateTask.getExecution();
        
        // executionEnt.setId("220000000110955");
        // 没有父流程
        if (executionEnt.getSuperExecution() == null)
        {
            return false;
        }
        
        if (!BpmUtil.isMuiltiExcetion(executionEnt.getSuperExecution()))
        {
            return false;
        }
        String actDefId = executionEnt.getSuperExecution().getProcessDefinitionId();
        Map<String, FlowNode> mapParent = NodeCache.getByActDefId(actDefId);
        
        String parentNodeId = executionEnt.getSuperExecution().getActivityId();
        String curentNodeId = executionEnt.getActivityId();
        
        FlowNode parentFlowNode = mapParent.get(parentNodeId);
        Map<String, FlowNode> subNodeMap = parentFlowNode.getSubProcessNodes();
        FlowNode startNode = NodeCache.getStartNode(subNodeMap);
        
        if (startNode.getNextFlowNodes().size() == 1)
        {
            FlowNode nextNode = startNode.getNextFlowNodes().get(0);
            // 驳回子流程需要根据task id 继续往下流转
            TaskThreadService.addExtSubTask(delegateTask.getId());
            
            if (nextNode.getNodeId().equals(curentNodeId))
            {
                TaskExecutor taskExecutor = (TaskExecutor)executionEnt.getSuperExecution().getVariable("assignee");
                // 如果 taskExecutor 中已经指定了task执行人，则直接分配执行人
                if (taskExecutor != null && StringUtil.isNotEmpty(taskExecutor.getExecuteId()))
                {
                    assignUser(delegateTask, taskExecutor);
                }
                else
                {
                    // 如果没有指定，则通过nodeset人员配置重新获取
                    Map<String, Object> parentVariables = executionEnt.getSuperExecution().getVariables();
                    Map<String, Object> curentVariables = executionEnt.getVariables();
                    
                    String startUserId = curentVariables.get(BpmConst.StartUser).toString();
                    String preTaskUser = UserContextUtil.getCurrentUserId().toString();
                    NodeUserService nodeUserService = AppUtil.getBean(NodeUserService.class);
                    
                    // 如果 外部子流程前节点 有分支节点，流程变量必须从父task中获取
                    Map<String, Object> variables = marginVariables(parentVariables, curentVariables);
                    variables.put(BpmConst.SUBFLOW_BUSINESSKEY, taskExecutor.getBusinessKey());
                    
                    List<TaskExecutor> userList = nodeUserService.getExeUserIds(executionEnt.getProcessDefinitionId(),
                        executionEnt.getProcessInstanceId(),
                        curentNodeId,
                        startUserId,
                        preTaskUser,
                        variables);
                    assignUser(delegateTask, userList);
                }
                return true;
            }
            return false;
        }
        logger.debug("多实例外部调用子流程起始节点后只能跟一个任务节点");
        return false;
        
    }
    
    private Map<String, Object> marginVariables(Map<String, Object> parentVariables,
        Map<String, Object> curentVariables)
    {
        Map<String, Object> variables = new HashMap<String, Object>();
        for (String key : curentVariables.keySet())
        {
            if (!variables.containsKey(key))
            {
                variables.put(key, curentVariables.get(key));
            }
            
        }
        for (String key : parentVariables.keySet())
        {
            if (!variables.containsKey(key))
            {
                variables.put(key, parentVariables.get(key));
            }
        }
        
        return variables;
    }
    
    /**
     * 添加流程任务意见。
     * 
     * @param token
     * @param delegateTask
     */
    private void addOpinion(String token, DelegateTask delegateTask)
    {
        TaskOpinion taskOpinion = new TaskOpinion(delegateTask);
        taskOpinion.setOpinionId(UniqueIdUtil.genId());
        taskOpinion.setTaskToken(token);
        taskOpinionService.add(taskOpinion);
    }
    
    /**
     * 从数据库加载人员并分配用户。
     * 
     * @param actDefId
     * @param nodeId
     * @param delegateTask
     */
    private void handAssignUserFromDb(String actDefId, String nodeId, DelegateTask delegateTask)
    {
        NodeUserService userService = (NodeUserService)AppUtil.getBean(NodeUserService.class);
        
        String actInstId = delegateTask.getProcessInstanceId();
        
        ProcessInstance processInstance = bpmService.getProcessInstance(actInstId);
        List<TaskExecutor> users = null;
        // 获取流程变量。
        Map<String, Object> vars = delegateTask.getVariables();
        // 执行任务的情况
        if (processInstance != null)
        {
            // 获取上个任务的执行人，这个执行人在上一个流程任务的完成事件中进行设置。
            // 代码请参考TaskCompleteListener。
            String startUserId = (String)vars.get(BpmConst.StartUser);
            
            String preStepUserId = UserContextUtil.getCurrentUserId().toString();
            Long preStepOrgId = UserContextUtil.getCurrentOrgId();
            vars.put(BpmConst.PRE_ORG_ID, preStepOrgId);
            
            if (StringUtil.isEmpty(startUserId) && vars.containsKey(BpmConst.PROCESS_INNER_VARNAME))
            {
                Map<String, Object> localVars = (Map<String, Object>)vars.get(BpmConst.PROCESS_INNER_VARNAME);
                startUserId = (String)localVars.get(BpmConst.StartUser);
            }
            
            users = userService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, preStepUserId, vars);
        }
        // 启动流程
        else
        {
            // startUser
            // 上个节点的任务执行人
            String startUserId = (String)vars.get(BpmConst.StartUser);
            // 内部子流程启动
            if (StringUtil.isEmpty(startUserId) && vars.containsKey(BpmConst.PROCESS_INNER_VARNAME))
            {
                Map<String, Object> localVars = (Map<String, Object>)vars.get(BpmConst.PROCESS_INNER_VARNAME);
                startUserId = (String)localVars.get(BpmConst.StartUser);
            }
            users = userService.getExeUserIds(actDefId, null, nodeId, startUserId, startUserId, vars);
        }
        assignUser(delegateTask, users);
    }
    
    /**
     * 处理任务分发。
     * 
     * <pre>
     * 	1.根据指定的用户产生新的任务，并指定了相应的excution，任务历史数据。
     * 		支持用户独立的往下执行，不像会签的方式需要等待其他的任务完成才往下执行。
     *  2.产生分发记录。
     * 
     * </pre>
     * 
     * @param actDefId 流程定义ID
     * @param nodeId 流程节点ID
     * @param nodeUserMap 上下文指定的分发用户。
     * @param delegateTask 任务对象。
     * @return
     */
    private boolean handlerForkTask(String actDefId, String nodeId, Map<String, List<TaskExecutor>> nodeUserMap,
        DelegateTask delegateTask)
    {
        // 若任务进行回退至分发任务节点上，则不再进行任务分发
        ProcessCmd processCmd = TaskThreadService.getProcessCmd();
        if (processCmd != null && BpmConst.TASK_BACK.equals(processCmd.isBack()))
            return false;
        NodeSet bpmNodeSet = bpmNodeSetDao.getByActDefIdNodeId(actDefId, nodeId);
        // 当前任务为分发任务,即根据当前分发要求进行生成分发任务
        if (bpmNodeSet != null && NodeSet.NODE_TYPE_FORK.equals(bpmNodeSet.getNodeType()))
        {
            List<TaskExecutor> taskExecutors = taskUserAssignService.getExecutors();
            // 若当前的线程里包含了该任务对应的执行人员列表，则任务的分发用户来自于此
            if (BeanUtils.isEmpty(taskExecutors))
            {
                // 若当前的线程里包含了该任务对应的执行人员列表，则任务的分发用户来自于此
                if (nodeUserMap != null && nodeUserMap.get(nodeId) != null)
                {
                    taskExecutors = nodeUserMap.get(nodeId);
                }
                // 否则，从数据库获取人员设置
                else
                {
                    NodeUserService userService = (NodeUserService)AppUtil.getBean(NodeUserService.class);
                    ProcessInstance processInstance =
                        bpmService.getProcessInstance(delegateTask.getProcessInstanceId());
                    if (processInstance != null)
                    {
                        Map<String, Object> vars = delegateTask.getVariables();
                        vars.put("executionId", delegateTask.getExecutionId());
                        String preTaskUser = UserContextUtil.getCurrentUserId().toString();
                        String actInstId = delegateTask.getProcessInstanceId();
                        String startUserId = (String)delegateTask.getVariable(BpmConst.StartUser);
                        taskExecutors =
                            userService.getExeUserIds(actDefId, actInstId, nodeId, startUserId, preTaskUser, vars);
                    }
                }
            }
            if (BeanUtils.isNotEmpty(taskExecutors))
            {
                bpmService.newForkTasks((TaskEntity)delegateTask, taskExecutors);
                taskForkService.newTaskFork(delegateTask,
                    bpmNodeSet.getJoinTaskName(),
                    bpmNodeSet.getJoinTaskKey(),
                    taskExecutors.size());
            }
            else
            {
                ProcessRun processRun =
                    processRunService.getByActInstanceId(new Long(delegateTask.getProcessInstanceId()));
                String msg = processRun.getSubject() + "请设置分发人员";
                MessageUtil.addMsg(msg);
                throw new RuntimeException(msg);
                
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * 分配用户执行人或候选人组。
     * 
     * @param delegateTask
     * @param taskExecutor
     */
    private void assignUser(DelegateTask delegateTask, TaskExecutor taskExecutor)
    {
        if (TaskExecutor.USER_TYPE_USER.equals(taskExecutor.getType()))
        {
            delegateTask.setOwner(taskExecutor.getExecuteId());
            
            Long sysUserId = Long.valueOf(taskExecutor.getExecuteId());
            ISysUser sysUser = null;
            
            // 取代理用户
            if (isAllowAgent())
            {
                sysUser = agentSettingService.getAgent(delegateTask, sysUserId);
            }
            
            if (sysUser != null)
            {
                delegateTask.setAssignee(sysUser.getUserId().toString());
                delegateTask.setDescription(TaskOpinion.STATUS_AGENT.toString());
                delegateTask.setOwner(taskExecutor.getExecuteId());
            }
            else
            {
                delegateTask.setAssignee(taskExecutor.getExecuteId());
            }
            TaskOpinion taskOpinion = taskOpinionService.getByTaskId(new Long(delegateTask.getId()));
            ISysUser exeUser = sysUserService.getById(sysUserId);
            taskOpinion.setExeUserId(exeUser.getUserId());
            taskOpinion.setExeFullname(exeUser.getFullname());
            taskOpinionService.update(taskOpinion);
        }
        else
        {
            delegateTask.setAssignee(BpmConst.EMPTY_USER);
            delegateTask.setOwner(BpmConst.EMPTY_USER);
            List<TaskExecutor> userList = getByTaskExecutor(taskExecutor);
            for (TaskExecutor ex : userList)
            {
                if (ex.getType().equals(TaskExecutor.USER_TYPE_USER))
                {
                    delegateTask.addCandidateUser(ex.getExecuteId());
                }
                else
                {
                    delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
                }
            }
        }
    }
    
    /**
     * 分配任务执行人。
     * 
     * @param delegateTask
     * @param users
     */
    private void assignUser(DelegateTask delegateTask, List<TaskExecutor> executors)
    {
        if (BeanUtils.isEmpty(executors))
        {
            String msg = "节点:" + delegateTask.getName() + ",没有设置执行人";
            MessageUtil.addMsg(msg);
            throw new RuntimeException(msg);
        }
        // 是否执行过分配执行人
        boolean isExecPerson = false;
        // 只有一个人的情况。
        if (executors.size() == 1)
        {
            TaskExecutor taskExecutor = executors.get(0);
            
            if (TaskExecutor.USER_TYPE_USER.equals(taskExecutor.getType()))
            {
                
                // 是否是流程启动，并跳过第一个节点
                Long sysUserId = Long.valueOf(taskExecutor.getExecuteId());
                ISysUser sysUser = null;
                // 取代理用户
                if (isAllowAgent())
                {
                    sysUser = agentSettingService.getAgent(delegateTask, sysUserId);
                }
                // 有代理人员的情况
                if (sysUser != null)
                {
                    delegateTask.setAssignee(sysUser.getUserId().toString());
                    delegateTask.setDescription(TaskOpinion.STATUS_AGENT.toString());
                    delegateTask.setOwner(taskExecutor.getExecuteId());
                }
                else
                {
                    delegateTask.setAssignee(taskExecutor.getExecuteId());
                }
                
                TaskOpinion taskOpinion = taskOpinionService.getByTaskId(new Long(delegateTask.getId()));
                sysUser = sysUserService.getById(sysUserId);
                taskOpinion.setExeUserId(sysUser.getUserId());
                taskOpinion.setExeFullname(sysUser.getFullname());
                
                taskOpinionService.update(taskOpinion);
                // 设置执行过标志
                isExecPerson = true;
            }
            else if (TaskExecutor.USER_TYPE_ROLE.equals(taskExecutor.getType()))
            {// 角色,只有一个人时，才能进行分配给代理用户
             // 当前用户角色
                Long sysRoleId = Long.valueOf(taskExecutor.getExecuteId());
                // 根据角色取得人员
                Set<TaskExecutor> set = getByTaskExecutors(executors);
                if (BeanUtils.isEmpty(set))
                {
                    String msg = "没有设置人员,请检查人员配置!";
                    MessageUtil.addMsg(msg);
                    throw new RuntimeException(msg);
                }
                else
                {
                    List<? extends ISysUser> sysUsers = sysUserService.getByRoleId(sysRoleId);
                    // 判断是否为一个人
                    if (sysUsers != null && sysUsers.size() == 1)
                    {
                        Long sysUserId = sysUsers.get(0).getUserId();
                        ISysUser sysUser = null;
                        // 取代理用户
                        if (isAllowAgent())
                        {
                            sysUser = agentSettingService.getAgent(delegateTask, sysUserId);
                        }
                        // 有代理人员的情况
                        if (sysUser != null)
                        {
                            delegateTask.setAssignee(sysUser.getUserId().toString());
                            delegateTask.setDescription(TaskOpinion.STATUS_AGENT.toString());
                            delegateTask.setOwner(String.valueOf(sysUserId));
                        }
                        else
                        {
                            delegateTask.setAssignee(String.valueOf(sysUserId));
                        }
                        
                        TaskOpinion taskOpinion = taskOpinionService.getByTaskId(new Long(delegateTask.getId()));
                        sysUser = sysUserService.getById(sysUserId);
                        taskOpinion.setExeUserId(sysUser.getUserId());
                        taskOpinion.setExeFullname(sysUser.getFullname());
                        
                        taskOpinionService.update(taskOpinion);
                        // 设置执行过标志
                        isExecPerson = true;
                    }
                }
            }
        }
        if (!isExecPerson)
        {
            delegateTask.setAssignee(BpmConst.EMPTY_USER);
            delegateTask.setOwner(BpmConst.EMPTY_USER);
            Set<TaskExecutor> set = getByTaskExecutors(executors);
            if (BeanUtils.isEmpty(set))
            {
                String msg = "没有设置人员,请检查人员配置!";
                MessageUtil.addMsg(msg);
                throw new RuntimeException(msg);
            }
            for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();)
            {
                TaskExecutor ex = it.next();
                if (ex.getType().equals(TaskExecutor.USER_TYPE_USER))
                {
                    delegateTask.addCandidateUser(ex.getExecuteId());
                }
                else
                {
                    delegateTask.addGroupIdentityLink(ex.getExecuteId(), ex.getType());
                }
            }
        }
    }
    
    @Override
    protected int getScriptType()
    {
        return BpmConst.StartScript;
    }
    
}
