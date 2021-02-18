package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.io.bean.template.CheckCondition;
import com.cssrc.ibms.core.resources.io.bean.template.CheckItemDef;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;

@XmlRootElement(name = "tableTempBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableTempBean {
	TableTemp tableTemp;  
	
	@XmlElement(name="checkItemDef")
	@XmlElementWrapper(name="checkItemDefList")
	List<CheckItemDef> checkItemDefList;
	
	@XmlElement(name="signDef")
	@XmlElementWrapper(name="signDefList")
	List<SignDef> signDefList;
	
	@XmlElement(name="checkCondition")
	@XmlElementWrapper(name="checkConditionList")
	List<CheckCondition> checkConditionList;

	public TableTemp getTableTemp() {
		return tableTemp;
	}

	public void setTableTemp(TableTemp tableTemp) {
		this.tableTemp = tableTemp;
	}

	public List<CheckItemDef> getCheckItemDefList() {
		return checkItemDefList;
	}

	public void setCheckItemDefList(List<CheckItemDef> checkItemDefList) {
		this.checkItemDefList = checkItemDefList;
	}

	public List<SignDef> getSignDefList() {
		return signDefList;
	}

	public void setSignDefList(List<SignDef> signDefList) {
		this.signDefList = signDefList;
	}

	public List<CheckCondition> getCheckConditionList() {
		return checkConditionList;
	}

	public void setCheckConditionList(List<CheckCondition> checkConditionList) {
		this.checkConditionList = checkConditionList;
	}
	
	
}
