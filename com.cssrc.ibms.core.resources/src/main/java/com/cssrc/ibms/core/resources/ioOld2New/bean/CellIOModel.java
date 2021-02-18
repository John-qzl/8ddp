package com.cssrc.ibms.core.resources.ioOld2New.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cellList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CellIOModel{
	
	@XmlAttribute
	private String tempId;
	@XmlAttribute
	private String rnumber;
	@XmlAttribute
	private String itemdefid;
	@XmlAttribute
	private String content;
	@XmlAttribute
	private String resulttype;
	@XmlAttribute
	private String header;
	@XmlAttribute
	private String ifresult;
	
	@XmlElement(name ="item")
	private ItemIOModel item;
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
	public String getTempId() {
		return tempId;
	}
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	public String getRnumber() {
		return rnumber;
	}
	public void setRnumber(String rnumber) {
		this.rnumber = rnumber;
	}
	public String getItemdefid() {
		return itemdefid;
	}
	public void setItemdefid(String itemdefid) {
		this.itemdefid = itemdefid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getResulttype() {
		return resulttype;
	}
	public void setResulttype(String resulttype) {
		this.resulttype = resulttype;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getIfresult() {
		return ifresult;
	}
	public void setIfresult(String ifresult) {
		this.ifresult = ifresult;
	}
	public ItemIOModel getItem() {
		return item;
	}
	public void setItem(ItemIOModel item) {
		this.item = item;
	}
	
}
