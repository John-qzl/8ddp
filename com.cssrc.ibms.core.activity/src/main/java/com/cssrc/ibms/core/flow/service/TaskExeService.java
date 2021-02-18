package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.ITaskExeService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.ProcessRunDao;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.dao.TaskExeDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.ProTransTo;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.TaskExe;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.msg.MessageUtil;


/**
 * <pre>
 * 对象功能:任务转办代理 Service类 
 * 开发人员:zhulongchao 
 * </pre>
 */
@Service
public class TaskExeService extends BaseService<TaskExe> implements ITaskExeService{
	@Resource
	private TaskExeDao dao;
	@Resource
	private ProcessRunDao processRunDao;
	@Resource
	private IBpmService bpmService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private TaskDao taskDao;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private ISysTemplateService sysTemplateService;
	@Resource
	private TaskService taskService;
	/*@Resource
	private CalendarAssignService calendarAssignService;*/
	@Resource
	private TaskMessageService taskMessageService;
	@Resource
	private ProcessRunService processRunService;
	
	@Resource
	private ProTransToService bpmProTransToService;
	public TaskExeService() {
	}

	@Override
	protected IEntityDao<TaskExe, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 添加任务转办或代理。
	 * 
	 * <pre>
	 * 	1.添加转办消息。
	 *  2.修改任务类型为转办。
	 *  3.删除产生该人的通知任务。
	 *  4.将原来的任务意见修改成当前执行人，并添加备注。
	 *  5.添加一条新的意见，状态为待审批，执行人为转办的人。
	 *  4.发送信息通知沟通发起人。
	 * </pre>
	 * 
	 * @param bpmTaskExe
	 * @throws Exception
	 */
	public void assignSave(TaskExe bpmTaskExe) throws Exception {
		// 保存代理信息
		dao.add(bpmTaskExe);

		Short opinionStatus = TaskOpinion.STATUS_DELEGATE;
		Integer useType=0;
		if (TaskExe.TYPE_ASSIGNEE.equals(bpmTaskExe.getAssignType())) {
			opinionStatus = TaskOpinion.STATUS_AGENT;
			useType=ISysTemplate.USE_TYPE_AGENT;
		} 
		else if (TaskExe.TYPE_TRANSMIT.equals(bpmTaskExe.getAssignType())) {
			opinionStatus = TaskOpinion.STATUS_DELEGATE;
			taskDao.updateTask(bpmTaskExe.getTaskId().toString(), bpmTaskExe
					.getAssigneeId().toString(), opinionStatus.toString());
			useType=ISysTemplate.USE_TYPE_DELEGATE;
		}

		// 取消通知任务。
		taskDao.delCommuTaskByInstNodeUser(bpmTaskExe.getActInstId(),bpmTaskExe.getTaskDefKey(), bpmTaskExe.getAssigneeId());
		// 将原来的任务审批意见修改成代办状态，并填写任务意见。
		TaskOpinion taskOpinion = taskOpinionService.getByTaskId(bpmTaskExe.getTaskId());
		taskOpinion.setCheckStatus(opinionStatus);

		// 如果是转办，更新执行人
		if (TaskExe.TYPE_TRANSMIT.equals(bpmTaskExe.getAssignType())) {
			ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
			taskOpinion.setExeUserId(sysUser.getUserId());
			taskOpinion.setExeFullname(sysUser.getFullname());
		}
		taskOpinion.setOpinion(bpmTaskExe.getMemo());
		taskOpinion.setEndTime(new Date());
		Long duration = taskOpinion.getEndTime().getTime() - taskOpinion.getStartTime().getTime(); //calendarAssignService.getRealWorkTime(taskOpinion.getStartTime(), taskOpinion.getEndTime(), taskOpinion.getExeUserId());
		taskOpinion.setDurTime(duration);
		taskOpinionService.update(taskOpinion);
		
		// 新添加一个任务意见，状态为初始值。
		ProcessRun processRun = processRunDao.getById(bpmTaskExe.getRunId());
		TaskOpinion newOpinion = new TaskOpinion();
		newOpinion.setOpinionId(UniqueIdUtil.genId());
		taskOpinion.setSuperExecution(processRunService.getSuperActInstId(processRun.getActInstId().toString()));
		newOpinion.setActInstId(processRun.getActInstId());
		newOpinion.setActDefId(processRun.getActDefId());
		newOpinion.setCheckStatus(TaskOpinion.STATUS_CHECKING);
		newOpinion.setStartTime(new Date());
		newOpinion.setTaskKey(bpmTaskExe.getTaskDefKey());
		newOpinion.setTaskName(bpmTaskExe.getTaskName());
		newOpinion.setTaskId(bpmTaskExe.getTaskId());
		newOpinion.setExeUserId(bpmTaskExe.getAssigneeId());
		newOpinion.setExeFullname(bpmTaskExe.getAssigneeName());
		
		taskOpinionService.add(newOpinion);
		
		//发送转办/代理消息
		this.sendMessage(bpmTaskExe, bpmTaskExe.getInformType(), useType, bpmTaskExe.getMemo(), false);
	}
	/**
	 * 发送消息
	 * @param bpmTaskExe
	 * @param userType
	 * @param moduleType
	 * @throws Exception
	 */
	private void sendMessage(TaskExe bpmTaskExe,String informType, Integer userType,
			 String opinion,boolean cancel) throws Exception {
		
		//取消时设置任务id为空。
		Long taskId=cancel?null:bpmTaskExe.getTaskId();
		
		Map<String, String> msgTempMap = sysTemplateService.getTempByFun(userType);
		
		Long receiverId = bpmTaskExe.getAssigneeId();
		ISysUser assignUser = sysUserService.getById(receiverId);
		List<ISysUser> receiverUserList=new ArrayList<ISysUser>();
		receiverUserList.add(assignUser);
		ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();
		taskMessageService.sendMessage(curUser, receiverUserList, informType, msgTempMap, bpmTaskExe.getSubject(), opinion, taskId, null);

	}

	/**
	 * 取消转办/代理/流转。
	 * 
	 * @param bpmTaskExe
	 * @param sysUser
	 * @param opinion
	 * @param informType
	 * @return
	 * @throws Exception 
	 */
	public ProcessRun cancel(TaskExe bpmTaskExe, ISysUser sysUser,
			String opinion, String informType) throws Exception {
		// 在转办任务中该记录标记为取消
		Short opininStatus = TaskOpinion.STATUS_CHECKING;
		
		ISysUser curUser = (ISysUser)UserContextUtil.getCurrentUser();

		String memo = bpmTaskExe.getMemo() + getText("service.bpmTaskExe.cancel.opinion") + opinion;
		bpmTaskExe.setMemo(memo);
		bpmTaskExe.setExeUserId(bpmTaskExe.getOwnerId());
		bpmTaskExe.setExeUserName(bpmTaskExe.getOwnerName());
		bpmTaskExe.setStatus(TaskExe.STATUS_CANCEL);
		bpmTaskExe.setExeTime(new Date());
		dao.update(bpmTaskExe);

		/****
		 * // 设置任务执行人为本人 taskDao.updateTask(bpmTaskExe.getTaskId().toString(),
		 * sysUser.getUserId().toString(), opininStatus.toString() );
		 ***/
		if (TaskExe.TYPE_TRANSTO.equals(bpmTaskExe.getAssignType())) {
			String taskId = bpmTaskExe.getTaskId().toString();
			ProcessTask task = taskDao.getByTaskId(taskId);
			taskService.deleteTask(taskId);
			//删除被流转任务产生的沟通任务
			taskDao.delCommuTaskByParentTaskId(taskId);
			List<TaskEntity> list = taskDao.getByParentTaskIdAndDesc(task.getParentTaskId(), TaskOpinion.STATUS_TRANSTO.toString());
			if(list.size()==0){//所有流转任务已结束
				//更改初始执行人状态为正常流转
				taskDao.updateTaskDescription(TaskOpinion.STATUS_CHECKING.toString(), task.getParentTaskId());
				ProTransTo proTransTo = bpmProTransToService.getByTaskId(Long.valueOf(task.getParentTaskId()));
				bpmProTransToService.delById(proTransTo.getId());//删除流转状态
			}
		}
		else{
			taskDao.updateTask(bpmTaskExe.getTaskId().toString(),sysUser.getUserId().toString(), opininStatus.toString() );
			
			// 获取任务意见并进行修改。
			TaskOpinion taskOpinion = taskOpinionService.getByTaskId(bpmTaskExe.getTaskId());
			// 状态修改为取消。
			taskOpinion.setCheckStatus(TaskOpinion.STATUS_DELEGATE_CANCEL);
			taskOpinion.setExeUserId(curUser.getUserId());
			taskOpinion.setExeFullname(curUser.getFullname());
			taskOpinion.setOpinion(opinion);
			taskOpinion.setEndTime(BeanUtils.isEmpty(taskOpinion.getEndTime())?new Date():taskOpinion.getEndTime());
			Long duration = taskOpinion.getEndTime().getTime() - taskOpinion.getStartTime().getTime(); //calendarAssignService.getRealWorkTime(taskOpinion.getStartTime(), taskOpinion.getEndTime(), sysUser.getUserId());
			taskOpinion.setDurTime(duration);
			taskOpinion.setEndTime(new Date());
			taskOpinionService.update(taskOpinion);
		}

		Object[] args={curUser.getFullname(),bpmTaskExe.getAssigneeName()};
		// 新添加一个任务意见，状态为初始值。
		ProcessRun processRun = processRunDao.getById(bpmTaskExe.getRunId());
		TaskOpinion newOpinion = new TaskOpinion();
		newOpinion.setOpinionId(UniqueIdUtil.genId());
		newOpinion.setSuperExecution(processRunService.getSuperActInstId(processRun.getActInstId().toString()));
		newOpinion.setActInstId(processRun.getActInstId());
		newOpinion.setActDefId(processRun.getActDefId());
		if (TaskExe.TYPE_TRANSTO.equals(bpmTaskExe.getAssignType())){//流转
			newOpinion.setCheckStatus(TaskOpinion.STATUS_CANCLE_TRANSTO);
			newOpinion.setOpinion(getText("service.bpmTaskExe.cancel.message",args));
			newOpinion.setStartTime(bpmTaskExe.getCratetime());
			newOpinion.setEndTime(new Date());
			Long durationTime = newOpinion.getEndTime().getTime() - newOpinion.getStartTime().getTime(); //calendarAssignService.getRealWorkTime(newOpinion.getStartTime(), newOpinion.getEndTime(), sysUser.getUserId());
			newOpinion.setDurTime(durationTime);
		}
		else{
			newOpinion.setCheckStatus(TaskOpinion.STATUS_CHECKING);
			newOpinion.setStartTime(new Date());
		}
		newOpinion.setTaskKey(bpmTaskExe.getTaskDefKey());
		newOpinion.setTaskName(bpmTaskExe.getTaskName());
		newOpinion.setTaskId(bpmTaskExe.getTaskId());
		newOpinion.setExeUserId(bpmTaskExe.getOwnerId());
		newOpinion.setExeFullname(bpmTaskExe.getOwnerName());
		taskOpinionService.add(newOpinion);
		
		Integer userType = ISysTemplate.USE_TYPE_CANCLE_DELEGATE;
		if (TaskExe.TYPE_ASSIGNEE.equals(bpmTaskExe.getAssignType())) {
			userType = ISysTemplate.USE_TYPE_CANCLE_AGENT;
		} else if (TaskExe.TYPE_TRANSMIT.equals(bpmTaskExe.getAssignType())) {
			userType = ISysTemplate.USE_TYPE_CANCLE_DELEGATE;
		} else if (TaskExe.TYPE_TRANSTO.equals(bpmTaskExe.getAssignType())) {
			userType = ISysTemplate.USE_TYPE_CANCLE_TRANSTO;
		}
//		// 转办人通知被转办人。
		this.sendMessage(bpmTaskExe, informType,userType, opinion,true);

		return processRun;

	}


	/**
	 * 批量取消转办。
	 * 
	 * @param ids
	 * @param opinion
	 * @param informType
	 * @throws Exception 
	 */
	public List<TaskExe> cancelBat(String ids, String opinion,
			String informType, ISysUser sysUser) throws Exception {
		String[] aryId = ids.split(",");
		List<TaskExe> list = new ArrayList<TaskExe>();
		for (int i = 0; i < aryId.length; i++) {
			Long lId = Long.parseLong(aryId[i]);
			TaskExe bpmTaskExe = dao.getById(lId);
			Long taskId = bpmTaskExe.getTaskId();
			TaskEntity taskEntity = bpmService.getTask(taskId.toString());
			Object[] args={bpmTaskExe.getSubject()};
			if (taskEntity == null) {
				MessageUtil.addMsg(getText("service.bpmTaskExe.cancelBat.ended",args));
				continue;
			}
			cancel(bpmTaskExe, sysUser, opinion, informType);
			list.add(bpmTaskExe);
			MessageUtil.addMsg(getText("service.bpmTaskExe.cancelBat.success",args));
		}
		return list;

	}


	/**
	 * 通过任务ID取消记录
	 * 
	 * @param taskId
	 */
	public void cancel(Long taskId) {
		TaskExe bpmTaskExe = getByTaskIdStatusInit(taskId);
		if (BeanUtils.isNotEmpty(bpmTaskExe)) {
			bpmTaskExe.setStatus(TaskExe.STATUS_CANCEL);
			dao.update(bpmTaskExe);
		}
	}

	/**
	 * 完成任务时更新转办任务的状态。
	 * 
	 * @param taskId
	 */
	public void complete(Long taskId) {
		List<TaskExe> list = getByTaskId(taskId);
		ISysUser sysuer = (ISysUser)UserContextUtil.getCurrentUser();
		for (TaskExe bpmTaskExe : list) {
			if (bpmTaskExe.getStatus().shortValue() == TaskExe.STATUS_INIT.shortValue()) {
				bpmTaskExe.setExeTime(new Date());
				bpmTaskExe.setExeUserId(sysuer.getUserId());
				bpmTaskExe.setExeUserName(sysuer.getFullname());
				if (bpmTaskExe.getAssigneeId().equals(sysuer.getUserId())) {
					bpmTaskExe.setStatus(TaskExe.STATUS_COMPLETE);
				} else {
					bpmTaskExe.setStatus(TaskExe.STATUS_OTHER_COMPLETE);
				}
			} 
			dao.update(bpmTaskExe);
		}
	}

	public TaskExe getByTaskIdStatusInit(Long taskId) {
		TaskExe bpmTaskExe = dao.getByTaskIdStatus(taskId,
				TaskExe.STATUS_INIT);
		return bpmTaskExe;
	}

	/**
	 * 根据任务ID获得任务转办代理
	 * 
	 * @param taskId
	 *            任务ID
	 * @return
	 */
	public List<TaskExe> getByTaskId(Long taskId) {
		return dao.getByTaskId(taskId);
	}

	/**
	 * 判断是否允许转办 。
	 * 
	 * @param taskId
	 * @return 允许为false，不允许为true
	 */
	public boolean isAssigneeTask(Long taskId) {
		return BeanUtils.isNotEmpty(getByTaskIdStatusInit(taskId)) ? true
				: false;
	}

	/**
	 * 返回流程的状态
	 * 
	 * @param list
	 * @return
	 */
	public Map<String, Short> getTaskMap(List<?> list) {
		Map<String, Short> map = new HashMap<String, Short>();

		if (BeanUtils.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				ProcessTask task = (ProcessTask) list.get(i);
				TaskExe bpmTaskExe = getByTaskIdStatusInit(new Long(
						task.getId()));
				if (BeanUtils.isEmpty(bpmTaskExe)) {
					map.put(task.getId(), (short) 0);
				} else {
					map.put(task.getId(), bpmTaskExe.getAssignType());
				}
			}
		}
		return map;
	}

	public List<TaskExe> accordingMattersList(QueryFilter filter) {
		return dao.accordingMattersList(filter);
	}
	
	/**
	 * 判断是否可以转办。
	 * @param taskId
	 * @param definition
	 * @return
	 */
	public boolean isAssigneeTask(TaskEntity taskEnt,Definition definition){
		//在第一个节点上不能转办。
		boolean isFirstNode= NodeCache.isFirstNode(taskEnt.getProcessDefinitionId(), taskEnt.getTaskDefinitionKey());
		if(isFirstNode){
			return false;
		}
		boolean rtn= definition.getAllowDivert()==1;
		if(!rtn) return false;
		boolean isCanAssignee =!isAssigneeTask(new Long(taskEnt.getId()));
		TaskOpinion taskOpinion=taskOpinionService.getByTaskId(new Long(taskEnt.getId()));
		//驳回到发起人 、 驳回 、已经转办的任务不能在转办      《？？？？？？    以下还木有成功实现 驳回不能转办的判定。？？？？？》
	    if ((TaskOpinion.STATUS_RECOVER_TOSTART.toString().equals(taskEnt.getDescription())) || 
	        (TaskOpinion.STATUS_REJECT_TOSTART.toString().equals(taskEnt.getDescription())) || 
	        (TaskOpinion.STATUS_REJECT.toString().equals(taskEnt.getDescription())) || 
	       (TaskOpinion.STATUS_DELEGATE.toString().equals(taskEnt.getDescription()))) {
	       isCanAssignee = false;
      }
		return isCanAssignee;
	}
	
	/**
	 * 根据任务ID取得代办。
	 * @param taskId
	 * @return
	 */
	public boolean getByIsAssign(Long taskId){
		int rtn= dao.getByIsAssign(taskId);
		return (rtn>0);
			
	}
	
	/**
	 * 根据流程实例ID删除转办代理事宜
	 * @param runId
	 */
	public void delByRunId(Long runId) {
		dao.delByRunId(runId);
	}

	public List<TaskExe> getByRunId(Long runId) {
		return dao.getByRunId(runId);
	}

	@Override
	public List accordingMattersList(Long ownerId, PagingBean pb) {
		return this.dao.accordingMattersList(ownerId, pb);
	}
	
}