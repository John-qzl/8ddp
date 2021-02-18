package com.cssrc.ibms.core.resources.io.bean.template;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author user
 * 检查表模板  W_TABLE_TEMP
 */
@XmlRootElement(name = "tableTemp")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableTemp {
	public TableTemp() {}
	public TableTemp(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.name = CommonTools.Obj2String(map.get("F_NAME"));
		this.secrecy = CommonTools.Obj2String(map.get("F_SECRECY"));
		this.number = CommonTools.Obj2String(map.get("F_NUMBER"));
		this.project_id = CommonTools.Obj2String(map.get("F_PROJECT_ID"));
		this.contents = CommonTools.Obj2String(map.get("F_CONTENTS"));
		this.rownum = CommonTools.Obj2String(map.get("F_ROWNUM"));
		this.remark = CommonTools.Obj2String(map.get("F_REMARK"));
		this.temp_file_id = CommonTools.Obj2String(map.get("F_TEMP_FILE_ID"));
		this.status = CommonTools.Obj2String(map.get("F_STATUS"));
		this.type = CommonTools.Obj2String(map.get("F_TYPE"));
		this.moduleId=CommonTools.Obj2String(map.get("F_MODULE_ID"));
		this.modelType=CommonTools.Obj2String(map.get("F_ZL"));
	}
	@XmlAttribute
	private String id; //主键
	@XmlAttribute
	private String name; //模板名称		
	@XmlAttribute
	private String secrecy; //密级
	@XmlAttribute
	private String number; //编号
	@XmlAttribute(name="projectId")
	private String project_id; //所属发次
	@XmlAttribute
	private String contents; //检查表内容
	@XmlAttribute
	private String rownum; //行数
	@XmlAttribute
	private String remark; //备注
	@XmlAttribute(name="tempFileId")
	private String temp_file_id; //所属文件夹
	@XmlAttribute
	private String status; //模板状态
	@XmlAttribute
	private String type; //模板类型：极简、标准、组合表等等
	@XmlAttribute
	private String moduleId; //所属策划
	@XmlAttribute
	private String modelType; //模板种类
	
	
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 签署定义集合
	 */
	@XmlElement(name ="signDef")
	private List<SignDef> signDefList;
	
/*	*//**
	 * 表头集合  二期没有用
	 *//*
	@XmlElement(name ="header")
	private List<Header> headerList;*/
	
	/**
	 * 检查项定义集合
	 */
	@XmlElement(name ="checkItemDef")
	private List<CheckItemDef> checkItemDefList;
	
	/**
	 * 检查条件集合
	 */
	@XmlElement(name ="checkCondition")
	private List<CheckCondition> checkConditionList;
	
	
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
	public String getSecrecy() {
		return secrecy;
	}
	public void setSecrecy(String secrecy) {
		this.secrecy = secrecy;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getRownum() {
		return rownum;
	}
	public void setRownum(String rownum) {
		this.rownum = rownum;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTemp_file_id() {
		return temp_file_id;
	}
	public void setTemp_file_id(String temp_file_id) {
		this.temp_file_id = temp_file_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<SignDef> getSignDefList() {
		return signDefList;
	}
	public void setSignDefList(List<SignDef> signDefList) {
		this.signDefList = signDefList;
	}
	public List<CheckItemDef> getCheckItemDefList() {
		return checkItemDefList;
	}
	public void setCheckItemDefList(List<CheckItemDef> checkItemDefList) {
		this.checkItemDefList = checkItemDefList;
	}
	public List<CheckCondition> getCheckConditionList() {
		return checkConditionList;
	}
	public void setCheckConditionList(List<CheckCondition> checkConditionList) {
		this.checkConditionList = checkConditionList;
	}
}
