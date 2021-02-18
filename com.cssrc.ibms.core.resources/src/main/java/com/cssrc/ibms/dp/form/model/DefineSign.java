package com.cssrc.ibms.dp.form.model;

public class DefineSign {
	/*主键*/
	protected Long ID;
	/*签署项名称*/
	protected String name;
	/*顺序*/
	protected int sequence;
	/*所属模板*/
	protected Long module;
	
	public Long getDefinesignId(){
		return ID;
	}
	public void setDefinesignId(Long ID){
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
