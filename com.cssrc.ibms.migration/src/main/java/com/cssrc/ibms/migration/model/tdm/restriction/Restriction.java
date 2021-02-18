package com.cssrc.ibms.migration.model.tdm.restriction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "数据约束")
@XmlAccessorType(XmlAccessType.NONE)
public class Restriction {	
	
    @XmlElement(name="枚举约束")
    private List<SimpleRestriciton> simpleRestriction = new ArrayList<SimpleRestriciton>();
    
    @XmlElement(name="数据表枚举约束 ")
    private List<TableRestriction> tableRestriction = new ArrayList<TableRestriction>();

	public List<SimpleRestriciton> getSimpleRestriction() {
		return simpleRestriction;
	}

	public void setSimpleRestriction(List<SimpleRestriciton> simpleRestriction) {
		this.simpleRestriction = simpleRestriction;
	}

	public List<TableRestriction> getTableRestriction() {
		return tableRestriction;
	}

	public void setTableRestriction(List<TableRestriction> tableRestriction) {
		this.tableRestriction = tableRestriction;
	}
}
