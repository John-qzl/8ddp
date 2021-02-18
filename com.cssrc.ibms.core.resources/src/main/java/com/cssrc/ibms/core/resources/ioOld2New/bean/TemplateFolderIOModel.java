package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "folder")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateFolderIOModel {
	
	@XmlAttribute
	private String desc;
	@XmlAttribute
	private String projectId;
	@XmlElement(name ="folder")
	private List<TemplateFolderIOModel> subTFolderList = new ArrayList<TemplateFolderIOModel>();
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
	public List<TemplateFolderIOModel> getSubTFolderList() {
		return subTFolderList;
	}
	public void setSubTFolderList(List<TemplateFolderIOModel> subTFolderList) {
		this.subTFolderList = subTFolderList;
	}
	
}
