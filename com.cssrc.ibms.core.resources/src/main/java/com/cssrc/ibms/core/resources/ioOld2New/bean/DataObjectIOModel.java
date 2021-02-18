package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dataObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataObjectIOModel{
	@XmlAttribute
	private String type;
	@XmlAttribute
	private String task;
	@XmlAttribute
	private String dataSecrecy;
	@XmlAttribute
	private String station;
	@XmlAttribute
	private String desc;
	@XmlAttribute
	private String templateId;
	
	@XmlAttribute
	private String creatorName;
	@XmlAttribute
	private String createTime;
	@XmlAttribute
	private String secretTime;
	@XmlAttribute
	private String status;
	@XmlAttribute
	private String version;
	@XmlAttribute
	private String fileId;
	@XmlAttribute
	private String instanceId;
	
	@XmlAttribute
	private String dataUid;
	@XmlAttribute
	private String tableInstanceRelatedName;
	
	@XmlElement(name ="rdmFile")
	private List<RdmFileIOModel> rdmFileList = new ArrayList<RdmFileIOModel>();
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
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getDataSecrecy() {
		return dataSecrecy;
	}
	public void setDataSecrecy(String dataSecrecy) {
		this.dataSecrecy = dataSecrecy;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getSecretTime() {
		return secretTime;
	}
	public void setSecretTime(String secretTime) {
		this.secretTime = secretTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public List<RdmFileIOModel> getRdmFileList() {
		return rdmFileList;
	}
	public void setRdmFileList(List<RdmFileIOModel> rdmFileList) {
		this.rdmFileList = rdmFileList;
	}
	public String getDataUid() {
		return dataUid;
	}
	public void setDataUid(String dataUid) {
		this.dataUid = dataUid;
	}
	public String getTableInstanceRelatedName() {
		return tableInstanceRelatedName;
	}
	public void setTableInstanceRelatedName(String tableInstanceRelatedName) {
		this.tableInstanceRelatedName = tableInstanceRelatedName;
	}
	
}
