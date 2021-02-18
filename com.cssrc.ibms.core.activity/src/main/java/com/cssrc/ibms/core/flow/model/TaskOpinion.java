package com.cssrc.ibms.core.flow.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.cssrc.ibms.api.activity.model.ITaskOpinion;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;

/**
 * 对象功能:流程任务审批意见Model对象  
 * 开发人员:zhulongchao  
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
public class TaskOpinion extends BaseModel implements ITaskOpinion{
    
    // 流程标题
    protected String flowSubject;
	// opinionId
	protected Long opinionId;
	// actInstId
	protected String actInstId;
	// 任务名称
	protected String taskName;
	// 任务Key
	protected String taskKey;
	// 任务令牌
	protected String taskToken;
	// taskId
	protected Long taskId;
	// 执行开始时间
	protected java.util.Date startTime;
	// 结束时间
	protected java.util.Date endTime;
	// 持续时间
	protected Long durTime;
	// 执行人ID
	protected Long exeUserId;
	// 执行人名
	protected String exeFullname;
	// 审批意见
	protected String opinion;
	// 审批状态
	protected Short checkStatus = STATUS_CHECKING;
	// 表单定义Id
	protected Long formDefId = 0L;
	// 意见表单名称
	protected String fieldName;
	// 流程定义ID
	protected String actDefId;
	// 父流程实例ID
	protected String superExecution;
	//印章ID
	protected String signId;
	
	//审批意见模板code;
	protected String voteCode;
	//备注
	protected String memo;
	
	protected String candidateUser="";
	
	private List<ISysUser> candidateUsers=new ArrayList<ISysUser>();
	
	public List<TaskExeStatus> candidateUserStatusList=new ArrayList<TaskExeStatus>();
	
	public TaskExeStatus taskExeStatus;
	

	/**
	 * 抄送表的中状态，0为未读，1为已读

	protected Short status=0;
		 */
	protected String status;
	
	protected String startTimeStr;
	protected String endTimeStr;
	protected String durTimeStr;
	
	protected String signImage;

	public TaskOpinion() {

	}

	public TaskOpinion(ProcessTask task) {
		this.actDefId = task.getProcessDefinitionId();
		this.actInstId = task.getProcessInstanceId();
		this.taskId = new Long(task.getId());
		this.taskName = task.getName();
		this.taskKey = task.getTaskDefinitionKey();
		this.startTime = new Date();
	}

	public TaskOpinion(DelegateTask task) {
		this.actDefId = task.getProcessDefinitionId();
		this.actInstId = task.getProcessInstanceId();
		this.taskId = new Long(task.getId());
		this.taskKey = task.getTaskDefinitionKey();
		this.taskName = task.getName();
		this.checkStatus=STATUS_CHECKING;
		this.startTime = new Date();
		ExecutionEntity superExecution = ((ExecutionEntity) task.getExecution()).getProcessInstance()
				.getSuperExecution();
		if (superExecution != null)
			this.superExecution = superExecution.getProcessInstanceId();
	}

	public void setOpinionId(Long opinionId) {
		this.opinionId = opinionId;
	}

	/**
	 * 返回 opinionId
	 * 
	 * @return
	 */
	public Long getOpinionId() {
		return opinionId;
	}

	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}

	/**
	 * 返回 actInstId
	 * 
	 * @return
	 */
	public String getActInstId() {
		return actInstId;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * 返回 任务名称
	 * 
	 * @return
	 */
	public String getTaskName() {
		return taskName;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	/**
	 * 返回 taskId
	 * 
	 * @return
	 */
	public Long getTaskId() {
		return taskId;
	}

	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 返回 执行开始时间
	 * 
	 * @return
	 */
	public java.util.Date getStartTime() {
		return startTime;
	}

	public String getStartTimeStr() {
		if (startTime == null)
			return "";
		return TimeUtil.getDateTimeString(this.startTime);
	}

	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 返回 结束时间
	 * 
	 * @return
	 */
	public java.util.Date getEndTime() {

		return endTime;
	}

	public String getEndTimeStr() {

		if (endTime == null)
			return "";
		endTimeStr = TimeUtil.getDateString(this.endTime);
		return endTimeStr;
	}

	public void setDurTime(Long durTime) {
		this.durTime = durTime;
	}

	public String getDurTimeStr() {
		if (this.durTime == null)
			return "";
		return TimeUtil.getTime(this.durTime);
	}

	/**
	 * 返回 持续时间
	 * 
	 * @return
	 */
	public Long getDurTime() {
		return durTime;
	}

	public void setExeUserId(Long exeUserId) {
		this.exeUserId = exeUserId;
	}

	/**
	 * 返回 执行人ID
	 * 
	 * @return
	 */
	public Long getExeUserId() {
		return exeUserId;
	}

	public void setExeFullname(String exeFullname) {
		this.exeFullname = exeFullname;
	}

	/**
	 * 返回 执行人名
	 * 
	 * @return
	 */
	public String getExeFullname() {
		if (StringUtil.isEmpty(exeFullname))
			return "";
		return exeFullname;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	/**
	 * 返回 审批意见
	 * 
	 * @return
	 */
	public String getOpinion() {
		return opinion;
	}

	public void setCheckStatus(Short checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getStatus() {
		//获取流程状态
		return FlowUtil.getTaskStatus(checkStatus, 1);
	}

	
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 返回 审批状态(来自数据字典)
	 * 
	 * @return
	 */
	public Short getCheckStatus() {
		return checkStatus;
	}

	/**
	 * 获取表单定义ID
	 * 
	 * @return
	 */
	public Long getFormDefId() {
		return formDefId;
	}

	public void setFormDefId(Long formDefId) {
		this.formDefId = formDefId;
	}

	/**
	 * 获取表单定义名称。
	 * 
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 获取流程定义ID。
	 * 
	 * @return
	 */
	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getTaskToken() {
		return taskToken;
	}

	public void setTaskToken(String taskToken) {
		this.taskToken = taskToken;
	}

	public String getSuperExecution() {
		return superExecution;
	}

	public void setSuperExecution(String superExecution) {
		this.superExecution = superExecution;
	}

	public String getCandidateUser() {
		return candidateUser;
	}

	public void setCandidateUser(String candidateUser) {
		this.candidateUser = candidateUser;
	}

	public void setCandidateUsers(List<ISysUser> candidateUsers) {
		this.candidateUsers = candidateUsers;
	}

	public List<ISysUser> getCandidateUsers() {
		return candidateUsers;
	}
	
	public List<TaskExeStatus> getCandidateUserStatusList() {
		return candidateUserStatusList;
	}

	public void setCandidateUserStatusList(List<TaskExeStatus> candidateUserStatusList) {
		this.candidateUserStatusList = candidateUserStatusList;
	}

	public TaskExeStatus getTaskExeStatus() {
		return taskExeStatus;
	}

	public void setTaskExeStatus(TaskExeStatus taskExeStatus) {
		this.taskExeStatus = taskExeStatus;
	}
	
    public String getSignId()
    {
        return signId;
    }

    public void setSignId(String signId)
    {
        this.signId = signId;
    }
    
    
	public String getSignImage()
    {
        return signImage;
    }

    public void setSignImage(String signImage)
    {
        this.signImage = signImage;
    }

    public String getVoteCode()
    {
        return voteCode;
    }

    public void setVoteCode(String voteCode)
    {
        this.voteCode = voteCode;
    }
    
    public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getFlowSubject()
    {
        return flowSubject;
    }

    public void setFlowSubject(String flowSubject)
    {
        this.flowSubject = flowSubject;
    }

    /**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof TaskOpinion)) {
			return false;
		}
		TaskOpinion rhs = (TaskOpinion) object;
		return new EqualsBuilder().append(this.opinionId, rhs.opinionId).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.opinionId).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("opinionId", this.opinionId).append("actInstId", this.actInstId)
				.append("taskName", this.taskName).append("taskId", this.taskId).append("startTime", this.startTime)
				.append("endTime", this.endTime).append("durTime", this.durTime).append("exeUserId", this.exeUserId)
				.append("exeFullname", this.exeFullname).append("opinion", this.opinion)
				.append("checkStatus", this.checkStatus).append("actDefId", this.actDefId)
				.append("superExecution", this.superExecution)
				.append("signId", this.signId)
				.append("memo", this.memo).toString();
	}


	
	

}