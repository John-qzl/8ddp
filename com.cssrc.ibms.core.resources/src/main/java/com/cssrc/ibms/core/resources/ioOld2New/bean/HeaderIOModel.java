package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "headerList")
@XmlAccessorType(XmlAccessType.FIELD)
public class HeaderIOModel{
	
	@XmlAttribute
	private String order;
	@XmlAttribute
	private String tempid;
	
	@XmlElement(name ="cellList")
	private List<CellIOModel> cellList = new ArrayList<CellIOModel>();
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getTempid() {
		return tempid;
	}
	public void setTempid(String tempid) {
		this.tempid = tempid;
	}
	public List<CellIOModel> getCellList() {
		return cellList;
	}
	public void setCellList(List<CellIOModel> cellList) {
		this.cellList = cellList;
	}
	
}
