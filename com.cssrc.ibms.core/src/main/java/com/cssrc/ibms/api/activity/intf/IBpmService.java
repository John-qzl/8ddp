package com.cssrc.ibms.api.activity.intf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activity.ActivityRequiredException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskAmount;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IBpmService {

	/**
	 * 根据流程定义ID启动流程
	 * 
	 * @param proessDefId
	 *            流程定义ID
	 * @param variables
	 *            流程变量
	 * @param action
	 *            外部启动流程保存动作
	 */
	public abstract ProcessInstance startFlowById(String proessDefId,
			Map<String, Object> variables);

	/**
	 * 根据流程定义ID启动流程。
	 * 
	 * @param porcessDefId
	 *            流程定义ID。
	 * @param businessKey
	 *            主键。
	 * @param variables
	 *            流程变量。
	 * @return
	 */
	public abstract ProcessInstance startFlowById(String porcessDefId,
			String businessKey, Map<String, Object> variables);

	/**
	 * 按流程定义Key进行启动流程。
	 * 
	 * @param processDefKey
	 *            流程定义KEY
	 * @param businessKey
	 *            主键
	 * @param variables
	 *            流程变量。
	 * @return
	 */
	public abstract ProcessInstance startFlowByKey(String processDefKey,
			String businessKey, Map<String, Object> variables);

	/**
	 * 发布流程定义
	 * 
	 * @param name
	 *            流程名称
	 * @param xml
	 *            流程的xml
	 * @param action
	 *            外部保存流程的接口
	 * @throws UnsupportedEncodingException
	 */
	public abstract Deployment deploy(String name, String xml)
			throws UnsupportedEncodingException;

	/**
	 * 获取流程定义
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public abstract ProcessDefinitionEntity getProcessDefinitionEntity(
			String processDefinitionId);

	/**
	 * 转移到指定的节点。<br>
	 * 指定任务ID,转移到的节点，转移成功保存的相关情况。
	 * 
	 * @param taskId
	 *            任务ID
	 * @param toNode
	 *            转移到的节点
	 * @param variables
	 *            转移到的任务变量,产生的任务为全局变量。
	 * @throws ActivityRequiredException
	 */
	public abstract void transTo(String taskId, String toNode)
			throws ActivityRequiredException;

	/**
	 * 仅完成任务，不作跳转处理。
	 * 
	 * <pre>
	 * 1.获取流程活动实例。
	 * 2.备份原来的跳转情况。
	 * 3.清空流程跳转。
	 * 4.完成当前任务。
	 * 5.将备份的跳转再添加回来。
	 * </pre>
	 * 
	 * @param taskId
	 * @param variables
	 */
	public abstract void onlyCompleteTask(String taskId);

	/**
	 * 通过Deployment获取流程定义
	 * 
	 * @param deployId
	 * @return
	 */
	public abstract ProcessDefinitionEntity getProcessDefinitionByDeployId(
			String deployId);

	/**
	 * 通过流程定义Id获取流程定义实体
	 * 
	 * @param defId
	 *            act流程定义ID
	 * @return
	 */
	public abstract ProcessDefinitionEntity getProcessDefinitionByDefId(
			String actDefId);

	/**
	 * 通过任务ID获取该流程定义
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract ProcessDefinitionEntity getProcessDefinitionByTaskId(
			String taskId);

	/**
	 * 根据流程key查询流程定义列表。
	 * 
	 * @param key
	 * @return
	 */
	public abstract List<ProcessDefinition> getProcessDefinitionByKey(String key);

	/**
	 * 按流程实例ID取得流程定义
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public abstract ProcessDefinitionEntity getProcessDefinitionByProcessInanceId(
			String processInstanceId);

	/**
	 * 取得所有的活动任务列表
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract List<String> getActiveTasks(String taskId);

	/**
	 * 取得当前任务节点上的所有跳转任务
	 * 
	 * @param taskId
	 * @return 返回的Map格式为 <taskDefinitionKey,taskName>
	 */
	public abstract Map<String, String> getOutNodesByTaskId(String taskId);

	/**
	 * 按任务获取其所有所有的运行节点ID
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract List<String> getActiveActIdsByTaskId(String taskId);

	/**
	 * 按流程实例获取其所有的运行节点ID
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public abstract List<String> getActiveActIdsByProcessInstanceId(
			String processInstanceId);

	/**
	 * 通过deployId获取定义XML
	 * 
	 * @param deployId
	 * @return
	 */
	public abstract String getDefXmlByDeployId(String deployId);

	/**
	 * 在线流程设计获取国际化资源
	 * 
	 * @param actDefId
	 * @param defXml
	 * @return
	 */
	public abstract String getLocaleDefXml(String actDefId, String defXml);

	/**
	 * 按流程义获取流程定义XML
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public abstract String getDefXmlByProcessDefinitionId(
			String processDefinitionId);

	/**
	 * 通过任务Id获取流程定义XML
	 * 
	 * @param processDefinitionId
	 * @return
	 */
	public abstract String getDefXmlByProcessTaskId(String taskId);

	public abstract String getDefXmlByProcessProcessInanceId(
			String processInstanceId);

	/**
	 * 通过deployId及xml更新至数据库
	 * 
	 * @param deployId
	 * @param defXml
	 */
	public abstract void wirteDefXml(String deployId, String defXml);

	/**
	 * 获取所有的活动执行节点。
	 * 
	 * @param actDefId
	 *            activiti的流程定义Id
	 * @return
	 */
	public abstract List<ActivityImpl> getActivityNodes(String actDefId);

	/**
	 * 根据流程定义ID和节点Id判断当前节点是否是会签节点。
	 * 
	 * @param actDefId
	 *            流程定义ID。
	 * @param nodeId
	 *            节点id。
	 * @return
	 */
	public abstract boolean isSignTask(String actDefId, String nodeId);

	/**
	 * 返回任务实体
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract TaskEntity getTask(String taskId);

	/**
	 * 根据任务id获取可以跳转的任务节点。
	 * 
	 * <pre>
	 * 	数据结构解释。
	 * 	该方法返回的数据结构为：
	 * 	Map&lt;String, Map&lt;String, String>>
	 * 	键可能的值为4个：任务节点,网关节点，结束节点,自动任务
	 *  对应的值为一个MAP:
	 *  键为任务id，值为任务名称。
	 * </pre>
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract Map<String, Map<String, String>> getJumpNodes(String taskId);

	/**
	 * 获取某个流程节义中除去nodeId之外的所有节点
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public abstract Map<String, String> getTaskNodes(String actDefId,
			String nodeId);

	/**
	 * 获取某个定义所有的任务节点，包括子流程下的
	 * 
	 * @param actDefId
	 * @return
	 */
	public abstract List<String> getExecuteNodes(String actDefId);

	/**
	 * 返回流程任务节点的映射<activitiId,activityName>
	 * 
	 * @param actDefId
	 *            ACT流程定义ID
	 * @return
	 */
	public abstract Map<String, String> getExecuteNodesMap(String actDefId,
			boolean includeSubProcess);

	/**
	 * 获取所有的任务列表
	 * 
	 * @param queryFilter
	 * @return
	 */
	public abstract List<TaskEntity> getTasks(QueryFilter queryFilter);

	/**
	 * 获取我的任务列表
	 * 
	 * @param queryFilter
	 * @return
	 */
	public abstract List<TaskEntity> getMyTasks(QueryFilter queryFilter);

	/**
	 * 获取我的手机任务列表
	 * 
	 * @param filter
	 * @return
	 */
	public abstract List<TaskEntity> getMyMobileTasks(QueryFilter filter);

	/**
	 * 判断流程是否结束
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public abstract boolean isEndProcess(String processInstanceId);

	/**
	 * 判断某个任务是否为会签任务(multiInstance task)
	 * 
	 * @param taskEntity
	 * @return
	 */
	public abstract boolean isSignTask(TaskEntity taskEntity);

	/**
	 * 查看某个流程实例的所有历史
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public abstract List<HistoricTaskInstance> getHistoryTasks(
			String processInstanceId);

	/**
	 * 返回单独一条的流程历史
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract HistoricTaskInstanceEntity getHistoricTaskInstanceEntity(
			String taskId);

	/**
	 * 把某个任务授权给某个用户
	 * 
	 * @param taskId
	 * @param userId
	 */
	public abstract void assignTask(String taskId, String userId);

	/**
	 * 设置某个任务的到期时间
	 * 
	 * @param taskId
	 * @param dueDate
	 */
	public abstract void setDueDate(String taskId, Date dueDate);

	/**
	 * 根据executionId获取ExecutionEntity，并赋予流程变量。
	 * 
	 * @param executionId
	 * @return
	 */
	public abstract ExecutionEntity getExecution(String executionId);

	/**
	 * 按任务ID获取执行实体
	 * 
	 * @param taskId
	 *            任务Id
	 * @return
	 */
	public abstract ExecutionEntity getExecutionByTaskId(String taskId);

	public abstract Map<String, Object> getVarsByTaskId(String taskId);

	public abstract void setExecutionVariable(String executionId,
			String variableName, Object varVal);

	public abstract void setTaskVariable(String taskId, String variableName,
			Object varVal);

	/**
	 * 设置分支节点条件，同时保存流程定义的xml和流程设计的xml文件。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @param forkNode
	 *            网关节点
	 * @param map
	 *            条件映射。
	 * @param canChoicePathNodeId
	 *            可以选择路径的节点，一般为之前网关节点之前的任务节点。
	 * @throws IOException
	 */
	public abstract void saveCondition(long defId, String forkNode,
			Map<String, String> map, String canChoicePathNodeId)
			throws Exception;

	/**
	 * 根据userId获取任务列表,流程代理里用到
	 * 
	 * @param touserid
	 * @return
	 */
	public abstract List<TaskEntity> getTaskByUserId(Long agentuserid,
			QueryFilter filter);

	/**
	 * 获取我的任务列表
	 * 
	 * @param params
	 * @return
	 */
	public abstract String getMyEvents(Map map);

	/**
	 * 判断是否允许选择路径。
	 * 
	 * @param actDefId
	 * @param taskId
	 * @return
	 */
	public abstract boolean getCanChoicePath(String actDefId, String taskId);

	/**
	 * 删除任务
	 * 
	 * @param taskId
	 */
	public abstract void deleteTask(String taskId);

	/**
	 * 删除多个任务
	 * 
	 * @param taskIds
	 */
	public abstract void deleteTasks(String[] taskIds);

	/**
	 * 更新任务的执行人
	 * 
	 * @param taskId
	 * @param userId
	 */
	public abstract void updateTaskAssignee(String taskId, String userId);

	/**
	 * 修改指定任务的执行人为空。
	 * 
	 * @param taskId
	 *            任务ID。
	 */
	public abstract void updateTaskAssigneeNull(String taskId);

	/**
	 * 更新任务的所属人
	 * 
	 * @param taskId
	 * @param userId
	 */
	public abstract void updateTaskOwner(String taskId, String userId);

	public abstract ProcessInstance getProcessInstance(String actInstId);

	/**
	 * 根据流程实例ID终结流程。 1.更新bpm_pro_run状态为结束。 2.删除act_ru_identitylink数据。 3.删除任务表
	 * ACT_RU_TASK。 4.更新节点实例。 update ACT_HI_ACTINST set EXECUTION_ID_ = ?,
	 * ASSIGNEE_ = ?, END_TIME_ = ?, DURATION_ = ? where ID_ = ? 5.更新历史实例。
	 * update ACT_HI_PROCINST set PROC_DEF_ID_ = ?, START_TIME_ = ?, END_TIME_ =
	 * ?, DURATION_ = ?, END_ACT_ID_ = ?, DELETE_REASON_ = ? where ID_ = ? 6.
	 * 删除流程变量。 ACT_RU_VARIABLE 7. 删除execution 表。 ACT_RU_EXECUTION。 删除流程表单运行情况
	 * BPM_FORM_RUN
	 * 
	 * @param instanceId
	 * @throws Exception
	 */
	public abstract IProcessRun endProcessByInstanceId(Long instanceId,
			String nodeId, String memo) throws Exception;

	/**
	 * 根据任务ID获取是否有候选人。
	 * 
	 * @param taskIds
	 * @return
	 */
	public abstract List<Map> getHasCandidateExecutor(String taskIds);

	/**
	 * 设置流程内部变量。
	 * 
	 * @param task
	 *            任务
	 * @param varName
	 *            变量名称
	 * @param obj
	 *            设置流程变量值。
	 */
	public abstract void setInnerVariable(DelegateTask task, String varName,
			Object obj);

	/**
	 * 获取输出变量。
	 * 
	 * @param task
	 *            任务
	 * @param varName
	 *            变量名称
	 * @return
	 */
	public abstract Object getOutVariable(DelegateTask task, String varName);

	/**
	 * 获取流程变量。
	 * 
	 * @param excution
	 * @param varName
	 * @return
	 */
	public abstract Object getOutVariable(DelegateExecution excution,
			String varName);

	/**
	 * 通过线程变量传递值。
	 * 
	 * @param obj
	 */
	public abstract void setObject(Object obj);

	/**
	 * 通过线程变量获取值。
	 * 
	 * @param obj
	 */
	public abstract Object getObject();

	/**
	 * 判断任务实例是否允许被驳回。
	 * 
	 * @param task
	 * @return
	 */
	public abstract boolean getIsAllowBackByTask(TaskEntity task);

	/**
	 * 取得任务是否可以回退。
	 * 
	 * <pre>
	 * 	1.当前节点为多实例不允许驳回。
	 *  2.前面的节点如果是用户任务节点并且这个节点不是多实例节点允许驳回否则不能驳回。
	 * </pre>
	 * 
	 * @param actDefId
	 *            流程定义id
	 * @param actInstId
	 *            流程实例key。
	 * @param nodeId
	 *            节点Id。
	 * @return
	 */
	public abstract boolean getIsAllowBackByTask(String actDefId, String nodeId);

	/**
	 * 更新流程实例列表。 1、更新流程的执行时间 2、执行人
	 * 
	 * @param list
	 */
	public abstract void updHistoryActInst(Long actInstId, String nodeId,
			String assignee);

	/**
	 * 根据流程实例ID和任务定义Key获取任务。
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract List getByInstanceIdTaskDefKey(String instanceId,
			String taskDefKey);
	/**
	 * 查找某用户的待办任务
	 * @param userId 用户ID 
	 * @param taskName 任务名称
	 * @param subject  任务标题 
	 * @param processName 流程定义名称
	 * @param orderField 排序字段
	 * @param orderSeq  升序或降序 值有 asc 或 desc
	 * @return
	 */
	public abstract List<?extends IProcessTask> getTasks(Long userId, String taskName,
			String subject, String processName, String orderField,
			String orderSeq, PagingBean pb);

	public abstract List<? extends ITaskAmount> getMyTasksCount(
			Long currentUserId);
	
	/**
     * 根据流程实例ID,挂起流程实例.
     */
	public void suspendProcessInstanceById(String instanceId);
	
	/**
	 * 根据流程实例ID,激活流程实例.
	 */
	public void activateProcessInstanceById(String instanceId);

    /** 
    * @Title: getFirstNodeTask 
    * @Description: TODO(获取第一个任务。) 
    * @param @param instanceId
    * @param @return    设定文件 
    * @return IProcessTask    返回类型 
    * @throws 
    */
    public abstract IProcessTask getFirstNodeTask(String instanceId);

}