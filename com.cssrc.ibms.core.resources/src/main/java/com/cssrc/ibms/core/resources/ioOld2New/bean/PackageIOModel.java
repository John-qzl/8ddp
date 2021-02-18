package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "package")
@XmlAccessorType(XmlAccessType.FIELD)
public class PackageIOModel {
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String desc;
	@XmlAttribute
	private String projectId;
	@XmlAttribute
	private String user;
	
	@XmlElement(name ="package")
	private List<PackageIOModel> subPackageList = new ArrayList<PackageIOModel>();
	
	@XmlElement(name ="testTeam")
	private List<TestTeamIOModel> testTeamList = new ArrayList<TestTeamIOModel>();
	
	@XmlElement(name ="task")
	private List<TaskIOModel> taskList = new ArrayList<TaskIOModel>();
	
	@XmlElement(name ="dataObject")
	private List<DataObjectIOModel> dataObjectList = new ArrayList<DataObjectIOModel>();
	
	//非普通分类节点的附加属性
	@XmlAttribute
	private String planStartTime; //计划开始时间：  设计、                       试验
	@XmlAttribute
	private String planEndTime; //计划结束时间：        设计、                       试验
	@XmlAttribute
	private String partId; //产品（软件）代号：                           单机、软件
	@XmlAttribute
	private String partName; //产品（软件）名称：                     单机、软件
	@XmlAttribute
	private String partstatus; //状态：                                       单机、软件
	@XmlAttribute
	private String unit; //单位：                                                         单机、软件、试验
	@XmlAttribute
	private String version; //版本号：                                                        软件
	@XmlAttribute
	private String site;//试验地点：                                                                            试验
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public List<PackageIOModel> getSubPackageList() {
		return subPackageList;
	}
	public void setSubPackageList(List<PackageIOModel> subPackageList) {
		this.subPackageList = subPackageList;
	}
	public List<TestTeamIOModel> getTestTeamList() {
		return testTeamList;
	}
	public void setTestTeamList(List<TestTeamIOModel> testTeamList) {
		this.testTeamList = testTeamList;
	}
	public List<TaskIOModel> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<TaskIOModel> taskList) {
		this.taskList = taskList;
	}
	public List<DataObjectIOModel> getDataObjectList() {
		return dataObjectList;
	}
	public void setDataObjectList(List<DataObjectIOModel> dataObjectList) {
		this.dataObjectList = dataObjectList;
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
	public String getPartId() {
		return partId;
	}
	public void setPartId(String partId) {
		this.partId = partId;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartstatus() {
		return partstatus;
	}
	public void setPartstatus(String partstatus) {
		this.partstatus = partstatus;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
}
