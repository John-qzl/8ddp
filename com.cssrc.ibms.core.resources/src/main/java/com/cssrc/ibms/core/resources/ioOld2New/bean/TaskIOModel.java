package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "task")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskIOModel {
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String status;
	@XmlAttribute
	private String testTeam;
	@XmlAttribute
	private String desc;
	
	@XmlElement(name ="task")
	private List<TaskIOModel> subTaskList = new ArrayList<TaskIOModel>();
	
	@XmlAttribute
	private String planStartTime;
	@XmlAttribute
	private String planEndTime;
	@XmlAttribute
	private String actualStartTime;
	@XmlAttribute
	private String actualEndTime;
	@XmlAttribute
	private String index;
	@XmlAttribute
	private String taskUid;
	
	@XmlAttribute
	private String schedulerSwitch;
	@XmlAttribute
	private String auditStatus;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTestTeam() {
		return testTeam;
	}
	public void setTestTeam(String testTeam) {
		this.testTeam = testTeam;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<TaskIOModel> getSubTaskList() {
		return subTaskList;
	}
	public void setSubTaskList(List<TaskIOModel> subTaskList) {
		this.subTaskList = subTaskList;
	}
	public String getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}
	public String getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}
	public String getActualStartTime() {
		return actualStartTime;
	}
	public void setActualStartTime(String actualStartTime) {
		this.actualStartTime = actualStartTime;
	}
	public String getActualEndTime() {
		return actualEndTime;
	}
	public void setActualEndTime(String actualEndTime) {
		this.actualEndTime = actualEndTime;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getSchedulerSwitch() {
		return schedulerSwitch;
	}
	public void setSchedulerSwitch(String schedulerSwitch) {
		this.schedulerSwitch = schedulerSwitch;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getTaskUid() {
		return taskUid;
	}
	public void setTaskUid(String taskUid) {
		this.taskUid = taskUid;
	}
	
}
