package com.cssrc.ibms.dp.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "checkCondition")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckCondition {
	
	/*主键*/
	protected Long ID;
	/*条件名称*/
	protected String name;
	/*顺序*/
	protected int sequence;
	/*所属模板*/
	protected Long module;
	
	public Long getCheckconditionId(){
		return ID;
	}
	public  void setCheckconditionId(Long ID){
		this.ID=ID;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public int getSequence(){
		return sequence;
	}
	public void setSequence(int sequence){
		this.sequence=sequence;
	}
	
	public Long getModule(){
		return module;
	}
	public void setModule(Long module){
		this.module=module;
	}
}
