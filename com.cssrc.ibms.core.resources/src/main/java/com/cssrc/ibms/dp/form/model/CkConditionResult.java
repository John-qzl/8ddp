package com.cssrc.ibms.dp.form.model;

public class CkConditionResult {
	/*主键*/
	protected Long ID;
	/*值*/
	protected String value;
	/*所属检查条件*/
	protected Long conditionID;
	/*所属表格实例*/
	protected Long instantID;
	
	public Long getID(){
		return ID;
	}
	public void setID(Long ID){
		this.ID=ID;
	}
	
	public String getValue(){
		return value;
	}
	public void setValue(String value){
		this.value=value;
	}
	
	public Long getConditionID(){
		return conditionID;
	}
	public void setConditionID(Long conditionID){
		this.conditionID=conditionID;
	}
	
	public Long getInstantID(){
		return instantID;
	}
	public void setInstantID(Long instantID){
		this.instantID=instantID;
	}
}
