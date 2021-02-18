package com.cssrc.ibms.core.resources.ioOld2New.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "conditionresList")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConditionResIOModel {

	@XmlAttribute
	private String conditionid;
	@XmlAttribute
	private String instanceid;
	@XmlAttribute
	private String value;
	
	@XmlElement(name ="condition")
	private ConditionIOModel condition;
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
	public String getConditionid() {
		return conditionid;
	}
	public void setConditionid(String conditionid) {
		this.conditionid = conditionid;
	}
	public String getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ConditionIOModel getCondition() {
		return condition;
	}
	public void setCondition(ConditionIOModel condition) {
		this.condition = condition;
	}
	
	
}
