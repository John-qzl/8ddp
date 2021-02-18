package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateIOModel {
	
	@XmlAttribute
	private String number;
	@XmlAttribute
	private String projectid;
//	private byte[] contents;
	@XmlAttribute
	private String contents;
	@XmlAttribute
	private String rownum;
	@XmlAttribute
	private String templateFolderId;
	
	@XmlElement(name ="conditionList")
	private List<ConditionIOModel> conditionList = new ArrayList<ConditionIOModel>();
	@XmlElement(name ="signList")
	private List<SignIOModel> signList = new ArrayList<SignIOModel>();
	@XmlElement(name ="itemList")
	private List<ItemIOModel> itemList = new ArrayList<ItemIOModel>();
//	@XmlElement(name ="cellList")
//	private List<CellIOModel> cellList = new ArrayList<CellIOModel>();
	@XmlElement(name ="headerList")
	private List<HeaderIOModel> headerList = new ArrayList<HeaderIOModel>();
	
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
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	public String getRownum() {
		return rownum;
	}
	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	public List<ConditionIOModel> getConditionList() {
		return conditionList;
	}
	public void setConditionList(List<ConditionIOModel> conditionList) {
		this.conditionList = conditionList;
	}
	public List<SignIOModel> getSignList() {
		return signList;
	}
	public void setSignList(List<SignIOModel> signList) {
		this.signList = signList;
	}
	public List<ItemIOModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<ItemIOModel> itemList) {
		this.itemList = itemList;
	}
//	public List<CellIOModel> getCellList() {
//		return cellList;
//	}
//	public void setCellList(List<CellIOModel> cellList) {
//		this.cellList = cellList;
//	}
	public List<HeaderIOModel> getHeaderList() {
		return headerList;
	}
	public void setHeaderList(List<HeaderIOModel> headerList) {
		this.headerList = headerList;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getTemplateFolderId() {
		return templateFolderId;
	}
	public void setTemplateFolderId(String templateFolderId) {
		this.templateFolderId = templateFolderId;
	}
	
}
