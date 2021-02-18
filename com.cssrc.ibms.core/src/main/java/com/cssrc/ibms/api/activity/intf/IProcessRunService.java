package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.model.IFormDef;

public interface IProcessRunService {

	/**
	 * 取得流程第一个节点。
	 * 
	 * @param actDefId
	 * @return
	 */
	public abstract String getFirstNodetByDefId(String actDefId);

	public abstract List<? extends IProcessRun> getByActDefId(String actDefId);

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
	 * @param taskEntity
	 *            任务实例
	 * @param opinion
	 *            意见
	 * @param informType
	 *            通知类型
	 * @param userIds
	 *            用户ID
	 * @param subject
	 *            主题信息
	 * @throws Exception
	 */
	public abstract void saveCommuniCation(TaskEntity taskEntity,
			String opinion, String informType, String userIds, String subject)
			throws Exception;

	/**
	 * 添加已办历史。
	 * 
	 * <pre>
	 * 	添加沟通或流转反馈任务到已办。
	 * </pre>
	 * 
	 * @param taskEnt
	 */
	public abstract void addActivityHistory(TaskEntity taskEnt);

	/**
	 * 根据流程运行Id,删除流程运行实体。<br/>
	 * 些方法不会级联删除相关信息。
	 * 
	 * @see com.ibms.core.service.GenericService#delById(java.io.Serializable)
	 */
	public abstract void delById(Long id);

	/**
	 * 根据Act流程定义ID，获取流程实例
	 * 
	 * @param actDefId
	 * @param pb
	 * @return
	 */
	public abstract List<? extends IProcessRun> getByActDefId(String actDefId,
			PagingBean pb);

	/**
	 * 按流程实例ID获取IProcessRun实体
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public abstract IProcessRun getByActInstanceId(Long processInstanceId);

	public abstract void delByActDefId(String actDefId);

	public abstract List<? extends IProcessRun> getMyDraft(
			QueryFilter queryFilter);
	
	public abstract List<? extends IProcessRun> getMyDraft(
			Long userId ,PagingBean pg);

	/**
	 * 我的请求
	 * 
	 * @return
	 * @author liguang 2012.11.30
	 */
	public abstract List<? extends IProcessRun> getMyRequestList(
			QueryFilter filter);

	public abstract List<? extends IProcessRun> getMyRequestCompletedList(
			QueryFilter filter);

	/**
	 * 我的办结
	 * 
	 * @return
	 */
	public abstract List<? extends IProcessRun> getMyCompletedList(
			QueryFilter filter);

	/**
	 * 已办事宜
	 * 
	 * @return
	 * @author liguang 2012.11.30
	 */
	public abstract List<? extends IProcessRun> getAlreadyMattersList(
			QueryFilter filter);

	/**
	 * 办结事宜
	 * 
	 * @return
	 * @author liguang 2012.11.30
	 */
	public abstract List<? extends IProcessRun> getCompletedMattersList(
			QueryFilter filter);

	/**
	 * 查询我发起的和我参与流程
	 * 
	 * @param sqlKey
	 * @param queryFilter
	 * @return
	 * @author liguang 2012.11.6
	 */
	public abstract List<? extends IProcessRun> selectPro(
			QueryFilter queryFilter);

	/**
	 * 批量审批。
	 * 
	 * @param taskIds
	 *            任务id使用逗号进行分割。
	 * @param opinion
	 *            意见。
	 * @throws Exception
	 */
	public abstract void nextProcessBat(String taskIds, String opinion,
			boolean isSaveForm) throws Exception;

	/**
	 * 根据runid找到copyid
	 * 
	 * @return
	 */
	public abstract IProcessRun getCopyIdByRunid(Long runId);

	/**
	 * 保存任务处理意见。
	 * 
	 * @param taskEntity
	 * @param opinion
	 */
	public abstract void saveOpinion(TaskEntity taskEntity, String opinion,
			String fieldName);

	/**
	 * 根据流程任务ID，取得流程扩展实例
	 * 
	 * @param taskId
	 *            流程任务ID
	 * @return
	 */
	public abstract IProcessRun getByTaskId(String taskId);



	/**
	 * 获取某人的流程实例列表。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @param creatorId
	 *            创建人
	 * @param instanceAmount
	 *            流程数量
	 * @return
	 */
	public abstract List<? extends IProcessRun> getRefList(Long defId,
			Long creatorId, Integer instanceAmount, int type);

	/**
	 * 获取监控的流程实例列表。
	 * 
	 * @param filter
	 * @return
	 */
	public abstract List<? extends IProcessRun> getMonitor(QueryFilter filter);

	/**
	 * 判断是否允许追回
	 * 
	 * @param runId
	 * @param backToStart
	 * @return
	 * @throws Exception
	 */
	public abstract ResultMessage checkRedo(Long runId) throws Exception;

	/**
	 * 判断是否允许撤销，这个允许多实例撤销
	 * 
	 * @param runId
	 * @param backToStart
	 * @return
	 * @throws Exception
	 */
	public abstract ResultMessage checkRecover(Long runId)
			throws Exception;

	/**
	 * 追回任务
	 * 
	 * @param runId
	 * @param informType
	 * @param memo
	 * @return
	 * @throws Exception
	 */
	public abstract ResultMessage recover(Long runId, String informType,
			String memo) throws Exception;

	/**
	 * 根据流程实例对流程进行撤销
	 * 
	 * @param runId
	 * @param informType
	 * @param memo
	 * @return
	 * @throws Exception
	 */
	public abstract ResultMessage redo(Long runId, String informType,
			String memo, boolean isSaveForm,boolean toStart) throws Exception;

	/**
	 * 根据流程实例对流程进行撤销
	 * 
	 * @param runId
	 * @param informType
	 * @param memo
	 * @return
	 * @throws Exception
	 */
	// public ResultMessage recover(Long runId, String informType, String memo)
	// throws Exception {
	// IProcessRun processRun = processRunService.getById(runId);
	//
	// Short status = processRun.getStatus();
	// if (status.shortValue() == IProcessRun.STATUS_FINISH) {
	// return new ResultMessage(ResultMessage.Fail, "对不起,此流程实例已经结束!");
	// }
	//
	// if (status.shortValue() == IProcessRun.STATUS_MANUAL_FINISH) {
	// return new ResultMessage(ResultMessage.Fail, "对不起,此流程实例已经被删除!");
	// }
	// // 判断当前人是否为发起人。
	// // 如果当前人为发起人那么可以撤回所有的流程。
	// boolean isCreator = processRun.getCreatorId().longValue() == ContextUtil
	// .getCurrentUserId().longValue();
	// if (!isCreator) {
	// return new ResultMessage(ResultMessage.Fail, "对不起,非流程发起人不能撤销到开始节点!");
	// }
	// // 获取开始节点。
	// FlowNode flowNode = NodeCache.getFirstNodeId(processRun.getActDefId());
	// String nodeId = flowNode.getNodeId();
	//
	// List<ProcessTask> taskList = bpmService.getTasks(processRun
	// .getActInstId());
	// List<String> taskNodeIdList = getNodeIdByTaskList(taskList);
	//
	// if (taskNodeIdList.contains(nodeId)) {
	// return new ResultMessage(ResultMessage.Fail, "当前已经是开始节点!");
	// }
	//
	// boolean hasRead = getTaskHasRead(taskList);
	// if (hasRead && "".equals(memo)) {
	// return new ResultMessage(ResultMessage.Fail, "对不起,请填写撤销的原因!");
	// }
	// // 判断是否允许返回。
	// boolean allowBack = getTaskAllowBack(taskList);
	// if (!allowBack) {
	// return new ResultMessage(ResultMessage.Fail, "对不起,当前流程实例不允许撤销!");
	// }
	// backToStart(memo, taskList, informType);
	//
	// return new ResultMessage(ResultMessage.Success,
	// getText("service.processrun.recoversuccess"));
	// }

	public abstract IProcessRun getByBusinessKey(Long formKey,String businessKey);

	public abstract IProcessRun getByBusinessKeyAndFormDef(String businessKey,
			String formdefid);

	public abstract IProcessRun getByBusinessKeyAndDefId(String businessKey,
			Long defId);
	
	public abstract void copyDraft(Long runId) throws Exception;

	/**
	 * 流程任务 管理员更改执行人
	 * 
	 * @param taskId
	 * @param userId
	 * @param voteContent
	 * @param informType
	 * @throws Exception
	 */
	public abstract ResultMessage updateTaskAssignee(String taskId,
			String userId, String voteContent, String informType)
			throws Exception;

	public abstract void saveAddSignOpinion(TaskEntity taskEntity,
			String opinion, String informType, Map<Long, Long> userTaskID,
			String subject) throws Exception;

	public abstract void updateTaskDescription(String description, String taskId);

	public abstract void delTransToTaskByParentTaskId(String parentTaskId);

	public abstract List<TaskEntity> getByParentTaskIdAndDesc(
			String parentTaskId, String description);

	public abstract boolean getHasRightsByTask(Long taskId, Long userId);

	public abstract List<? extends IProcessRun> getTestRunsByActDefId(
			String actDefId);

	/**
	 * 获取父流程的实例ID
	 * 
	 * @param actInstId
	 *            流程的实例ID
	 * @return
	 */
	public abstract String getSuperActInstId(String actInstId);


	public abstract List<? extends IProcessRun> getMyFlowsAndCptoList(
			QueryFilter filter);

	public abstract List<? extends IProcessRun> getMyCompletedAndCptoList(
			QueryFilter filter);
	
	/**
	 * 工作台显示我参与审批 流程
	 * @param assignee
	 * @param status
	 * @param pb
	 * @return
	 */
	public abstract List<?extends IProcessRun> getMyAttend(Long assignee, Short status, PagingBean pb);
	/**
	 * 工作台显示我发起的流程
	 * @param creatorId
	 * @param pb
	 * @return
	 */
	public abstract List<?extends IProcessRun> myStart(Long creatorId,PagingBean pb);

	/**
	 * 工作台显示我的办结
	 * @param creatorId
	 * @param pb
	 * @return
	 */
	public abstract List<?extends IProcessRun> myCompleted(long curUserId, PagingBean pb);
	/**
	 * 工作台显示已办事宜
	 * @param assignee
	 * @param pb
	 * @return
	 */
	public abstract List<?extends IProcessRun> myAlready(long assignee, PagingBean pb);
	/**
	 * 工作台显示办结事宜
	 * @param assignee
	 * @param pb
	 * @return
	 */
	public abstract List<?extends IProcessRun> completedMatters(Long assignee, PagingBean pb);
	
	/**
	 * 获取流程实例
	 * @param valueOf
	 * @return
	 */
	public abstract IProcessRun getById(Long valueOf);
	
	/**
	 * 根据formDefId来查找流程运行实例
	 * @param formDefId
	 * @return
	 */
	public abstract List<?extends IProcessRun> getByFlowKey(String flowKey,String businessKey);
	
	/**
	 * rpc 启动流程
	 * @param processCmd
	 * @return
	 */
	public abstract String startProcessRpc(IProcessCmd processCmd);
	
	/**
	 * 根据流程Key启动流程
	 * @param taskId
	 * @param flowKey
	 * @param pk
	 * @param userId
	 * @param variables
	 * @return
	 */
	public abstract String startProcessRpc(String taskId, String flowKey, String pk, String userId, Map<String, Object> variables);

	/**
	 * 流程启动页面
	 * @param request
	 * @param mv
	 * @param flowKey
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public abstract ModelAndView startFlowView(HttpServletRequest request,ModelAndView mv, String flowKey, Long userId)throws Exception;

    /**
     * 获取用户的代办任务
     * @param userId
     * @return
     */
    public abstract int getPendingMattersList(Long userId);

    /**
     * 获取用户的请求
     * @param userId
     * @return
     */
    public abstract int getMyRequestList(Long userId);

    /**
     * 通过业务数据主键，业务数据模板获取流程实例
     * @param actDefId
     * @param pk
     * @return
     */
    public abstract List<? extends IProcessRun> getByActDefIdAndPk(String actDefId, String pk);

    /** 
    * @Title: gerFormDefByActDefId 
    * @Description: TODO(根据流程定义Id获取流程关联表单) 
    * @param @param actDefId
    * @param @return    设定文件 
    * @return IFormDef    返回流程业务表单
    * @throws 
    */
    public abstract IFormDef gerFormDefByActDefId(String actDefId);

	
    /** 
    * @Title: getByBusinessKey 
    * @Description: TODO(根据业务数据主键获取流程run实例) 
    * @param @param businessKey
    * @param @return     
    * @return ProcessRun    返回类型 
    * @throws 
    */
    public abstract IProcessRun getByBusinessKey(String businessKey);

    /** 
    * @Title: deleteFormData 
    * @Description: TODO(删除草稿数据) 
    * @param @param formKey 流程表单key
    * @param @param pk     业务主键
    * @return void    返回类型 
    * @throws 
    */
    public abstract void deleteFormData(Long formKey, String pk);
	

}