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

import com.cssrc.ibms.api.activity.intf.IProTransToService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.ProTransToDao;
import com.cssrc.ibms.core.flow.dao.TaskDao;
import com.cssrc.ibms.core.flow.dao.TaskOpinionDao;
import com.cssrc.ibms.core.flow.model.CommuReceiver;
import com.cssrc.ibms.core.flow.model.ProTransTo;
import com.cssrc.ibms.core.flow.model.ProTransToAssignee;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 对象功能:流程流转状态 Service类 开发人员:zhulongchao
 */
@Service
public class ProTransToService extends BaseService<ProTransTo> implements IProTransToService{
	@Resource
	private ProTransToDao dao;

	@Resource
	private TaskDao taskDao;

	@Resource
	private ISysUserService sysUserService;

	@Resource
	private TaskOpinionDao taskOpinionDao;

	@Resource
	private ProcessRunService processRunService;

	@Resource
	private CommuReceiverService commuReceiverService;

	@Resource
	private TaskService taskService;

	public ProTransToService() {
	}

	@Override
	protected IEntityDao<ProTransTo, Long> getEntityDao() {
		return dao;
	}

	public void add(ProTransTo bpmProTransTo) {
		if (BeanUtils.isNotEmpty(bpmProTransTo)) {
			dao.add(bpmProTransTo);
		}
	}

	public ProTransTo getByTaskId(Long taskId) {
		return dao.getByTaskId(taskId);
	}

	public void delById(Long id) {
		dao.delById(id);
	}

	public void delByActInstId(Long actInstId) {
		dao.delByActInstId(actInstId);
	}

	public void delByTaskId(Long taskId) {
		dao.delByTaskId(taskId);
	}

	public void cancelTransToTaskCascade(String taskId) {
		List list = this.taskDao.getTransToTaskByParentTaskId(taskId);
		for (int i = 0; i < list.size(); i++) {
			ProcessTask task = (ProcessTask) list.get(i);
			this.taskService.deleteTask(task.getId());

			this.taskDao.delCommuTaskByParentTaskId(task.getId());
			if (task.getDescription().equals(
					TaskOpinion.STATUS_TRANSTO_ING.toString())) {
				delByTaskId(new Long(task.getId()));
				cancelTransToTaskCascade(task.getId());
			}
		}
	}

	public List<ProTransTo> mattersList(QueryFilter filter) {
		return this.dao.mattersList(filter);
	}

	public List<ProTransToAssignee> getAssignee(String parentTaskId,
			String assignee) throws Exception {
		List<TaskEntity> taskList = taskDao.getTransToTaskByParentTaskId(parentTaskId);
		List<ProTransToAssignee> list = handleAssignee(taskList, assignee);
		return list;
	}

	private List<ProTransToAssignee> handleAssignee(List<?> taskList,
			String assignee) {
		Map<String, String> taskMap = new HashMap<String, String>();
		if (taskList != null) {
			for (int i = 0; i < taskList.size(); i++) {
				ProcessTask task = (ProcessTask) taskList.get(i);
				taskMap.put(task.getAssignee(), task.getParentTaskId());
			}
		}
		String[] assignees = assignee.split(",");
		List<ProTransToAssignee> list = new ArrayList<ProTransToAssignee>();
		for (String transTo : assignees) {
			ISysUser sysUser = sysUserService.getById(Long.valueOf(transTo));
			ProTransToAssignee transToAssignee = new ProTransToAssignee();
			transToAssignee.setUserId(sysUser.getUserId());
			transToAssignee.setUserName(sysUser.getFullname());
			if (taskMap.containsKey(transTo)) {
				transToAssignee.setStatus(ProTransToAssignee.STATUS_CHECKING);
				transToAssignee.setParentTaskId(taskMap.get(transTo));
			} else {
				transToAssignee.setStatus(ProTransToAssignee.STATUS_CHECKED);
			}
			list.add(transToAssignee);
		}
		return list;
	}

	public void addTransTo(ProTransTo bpmProTransTo, String taskId,
			String opinion, String userIds, String informType) throws Exception {
		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
		ProcessTask processTask = taskDao.getByTaskId(taskId);
		String[] aryUsers = userIds.split(",");

		Map<Long, Long> usrIdTaskIds = addTransToTask(processTask, aryUsers);

		Long opinionId = addOpinion(processTask, opinion, sysUser, true);

		commuReceiverService.saveReceiver(opinionId, usrIdTaskIds, sysUser);

		String assignee = bpmProTransTo.getAssignee();
		if (StringUtil.isEmpty(assignee)) {
			bpmProTransTo.setAssignee(userIds);
		} else {
			bpmProTransTo.setAssignee(assignee + "," + userIds);
		}
		dao.update(bpmProTransTo);

		ProcessRun processRun = this.processRunService
				.getByActInstanceId(new Long(processTask.getProcessInstanceId()));
		processRunService.notifyCommu(processRun.getSubject(), usrIdTaskIds,
				informType, sysUser, opinion, ISysTemplate.USE_TYPE_TRANSTO);
	}

	private Map<Long, Long> addTransToTask(ProcessTask ent, String[] users)
			throws Exception {
		Map<Long, Long> map = new HashMap<Long, Long>();
		String parentId = ent.getId();
		for (String userId : users) {
			String taskId = String.valueOf(UniqueIdUtil.genId());
			TaskEntity task = (TaskEntity) taskService.newTask(taskId);
			task.setAssignee(userId);
			task.setOwner(userId);
			task.setCreateTime(new Date());
			task.setName(ent.getName());
			task.setParentTaskId(parentId);
			task.setTaskDefinitionKey(ent.getTaskDefinitionKey());
			task.setProcessInstanceId(ent.getProcessInstanceId());
			task.setDescription(TaskOpinion.STATUS_TRANSTO.toString());
			task.setProcessDefinitionId(ent.getProcessDefinitionId());
			taskService.saveTask(task);
			map.put(Long.valueOf(userId), Long.valueOf(taskId));
		}
		return map;
	}

	public void cancel(ProcessTask processTask, String opinion,
			String informType) throws Exception {
		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
		String taskId = processTask.getId();
		String parentTaskId = processTask.getParentTaskId();
		taskService.deleteTask(taskId);

		if (processTask.getDescription().equals(
				TaskOpinion.STATUS_TRANSTO_ING.toString())) {
			delByTaskId(new Long(taskId));
			cancelTransToTaskCascade(taskId);
		}

		this.taskDao.delCommuTaskByParentTaskId(taskId);
		List list = this.taskDao.getTransToTaskByParentTaskId(parentTaskId);
		ProTransTo bpmProTransTo = getByTaskId(Long.valueOf(parentTaskId));
		if (list.size() == 0) {
			ProcessTask parentTask = this.taskDao.getByTaskId(parentTaskId);
			if (parentTask.getParentTaskId() == null) {
				this.taskDao.updateTaskDescription(TaskOpinion.STATUS_CHECKING
						.toString(), parentTaskId);
			} else {
				this.taskDao.updateTaskDescription(TaskOpinion.STATUS_CHECKING
						.toString(), parentTaskId);
			}
			delById(bpmProTransTo.getId());
		} else {
			String assignee = bpmProTransTo.getAssignee();
			if (assignee.startsWith(processTask.getAssignee()))
				assignee = assignee
						.replace(processTask.getAssignee() + ",", "");
			else {
				assignee = assignee
						.replace("," + processTask.getAssignee(), "");
			}
			bpmProTransTo.setAssignee(assignee);
			update(bpmProTransTo);
		}

		commuReceiverService.setCommuReceiverStatus(new Long(taskId),
				CommuReceiver.CANCEL, new Long(processTask.getAssignee()));

		addOpinion(processTask, opinion, sysUser, false);

		ProcessRun processRun = this.processRunService
				.getByActInstanceId(new Long(processTask.getProcessInstanceId()));
		Map<Long, Long> usrIdTaskIds = new HashMap<Long, Long>();
		usrIdTaskIds.put(Long.valueOf(processTask.getAssignee()), null);
		processRunService.notifyCommu(processRun.getSubject(), usrIdTaskIds,
				informType, sysUser, opinion,
				ISysTemplate.USE_TYPE_CANCLE_TRANSTO);
	}

	private Long addOpinion(ProcessTask processTask, String opinion,
			ISysUser sysUser, boolean isAdd) {
		Long opinionId = Long.valueOf(UniqueIdUtil.genId());
		TaskOpinion taskOpinion = new TaskOpinion();
		taskOpinion.setOpinionId(opinionId);
		taskOpinion.setActDefId(processTask.getProcessDefinitionId());
		taskOpinion.setActInstId(processTask.getProcessInstanceId());
		if (isAdd) {
			taskOpinion.setCheckStatus(TaskOpinion.STATUS_ADD_TRANSTO);
			taskOpinion.setStartTime(new Date());
			taskOpinion.setEndTime(new Date());
			taskOpinion.setOpinion(opinion);
		} else {
			taskOpinion.setCheckStatus(TaskOpinion.STATUS_CANCLE_TRANSTO);
			ISysUser assignee = sysUserService.getById(Long.valueOf(processTask
					.getAssignee()));
			String cancelOpinion = "取消【" + assignee.getFullname()
					+ "】的流转任务。原因：" + opinion;
			taskOpinion.setOpinion(cancelOpinion);
			taskOpinion.setStartTime(processTask.getCreateTime());
			taskOpinion.setEndTime(new Date());
			Long durationTime = taskOpinion.getEndTime().getTime()
					- taskOpinion.getStartTime().getTime(); // this.calendarAssignService.getRealWorkTime(taskOpinion.getStartTime(),
			// taskOpinion.getEndTime(), sysUser.getUserId());
			taskOpinion.setDurTime(durationTime);
		}
		taskOpinion.setExeUserId(sysUser.getUserId());
		taskOpinion.setExeFullname(sysUser.getFullname());
		taskOpinion.setTaskKey(processTask.getTaskDefinitionKey());
		taskOpinion.setTaskName(processTask.getName());

		this.taskOpinionDao.add(taskOpinion);
		return opinionId;
	}

}
