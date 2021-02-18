package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "ckresList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CkIOModel {
	
	@XmlAttribute
	private String itemdefid;
	@XmlAttribute
	private String value;
	@XmlAttribute
	private String instanceid;
	@XmlAttribute
	private String ifnull;
	@XmlAttribute
	private String result;
	@XmlAttribute
	private String sketchmap;
	
	@XmlElement(name ="CKFile")
	private List<CKFileIOModel> CKFile;
	@XmlElement(name ="itemModel")
	private ItemIOModel itemModel;
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
	public ItemIOModel getItemModel() {
		return itemModel;
	}
	public void setItemModel(ItemIOModel itemModel) {
		this.itemModel = itemModel;
	}
	public String getItemdefid() {
		return itemdefid;
	}
	public void setItemdefid(String itemdefid) {
		this.itemdefid = itemdefid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	public String getIfnull() {
		return ifnull;
	}
	public void setIfnull(String ifnull) {
		this.ifnull = ifnull;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getSketchmap() {
		return sketchmap;
	}
	public void setSketchmap(String sketchmap) {
		this.sketchmap = sketchmap;
	}
	public List<CKFileIOModel> getCKFile() {
		return CKFile;
	}
	public void setCKFile(List<CKFileIOModel> cKFile) {
		CKFile = cKFile;
	}
	
}
