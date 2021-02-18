package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class InstanceIOModel{
	@XmlAttribute
	private String number;
	@XmlAttribute
	private String content;
	@XmlAttribute
	private String tempid;
	@XmlAttribute
	private String status;
	@XmlAttribute
	private String version;
	private String bb;
	@XmlAttribute
	private String tableInstanceRelatedName;			//表格关联DATAOBJECT表字段
	
	@XmlElement(name ="template")
	private TemplateIOModel template;
	
	@XmlElement(name ="conditionresList")
	private List<ConditionResIOModel> conditionresList = new ArrayList<ConditionResIOModel>();
	@XmlElement(name ="signresList")
	private List<SignResIOModel> signresList = new ArrayList<SignResIOModel>();
	@XmlElement(name ="ckresList")
	private List<CkIOModel> ckresList = new ArrayList<CkIOModel>();
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTempid() {
		return tempid;
	}
	public void setTempid(String tempid) {
		this.tempid = tempid;
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
	public String getBb() {
		return bb;
	}
	public void setBb(String bb) {
		this.bb = bb;
	}
	public List<ConditionResIOModel> getConditionresList() {
		return conditionresList;
	}
	public void setConditionresList(List<ConditionResIOModel> conditionresList) {
		this.conditionresList = conditionresList;
	}
	public List<SignResIOModel> getSignresList() {
		return signresList;
	}
	public void setSignresList(List<SignResIOModel> signresList) {
		this.signresList = signresList;
	}
	public List<CkIOModel> getCkresList() {
		return ckresList;
	}
	public void setCkresList(List<CkIOModel> ckresList) {
		this.ckresList = ckresList;
	}
	public TemplateIOModel getTemplate() {
		return template;
	}
	public void setTemplate(TemplateIOModel template) {
		this.template = template;
	}
	public String getTableInstanceRelatedName() {
		return tableInstanceRelatedName;
	}
	public void setTableInstanceRelatedName(String tableInstanceRelatedName) {
		this.tableInstanceRelatedName = tableInstanceRelatedName;
	}
	
}
