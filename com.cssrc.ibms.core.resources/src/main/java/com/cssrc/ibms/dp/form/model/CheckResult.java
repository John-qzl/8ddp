package com.cssrc.ibms.dp.form.model;

public class CheckResult {
	/*主键*/
	protected Long ID;
	/*检查结果*/
	protected String result;
	/*检查值*/
	protected String value;
	/*是否为空*/
	protected String ifnumm;
	/*示意图*/
	protected String sketchmap;
	/*所属检查项*/
	protected Long itemID;
	/*所属表格实例*/
	protected Long instantID;
	
	public Long getID(){
		return ID;
	}
	public void setID(Long ID){
		this.ID=ID;
	}
	
	public String getResult(){
		return result;
	}
	public void setResult(String result){
		this.result=result;
	}
	
	public String getValue(){
		return value;
	}
	public void setValue(String value){
		this.value=value;
	}
	
	public String getIfnumm(){
		return ifnumm;
	}
	public void setIfnumm(String ifnumm){
		this.ifnumm=ifnumm;
	}
	
	public String getSketchmap(){
		return sketchmap;
	}
	public void setSketchmap(String Sketchmap){
		this.sketchmap=Sketchmap;
	}
	
	public Long getItemID(){
		return itemID;
	}
	public void setItemID(Long itemID){
		this.itemID=itemID;
	}
	
	public Long getInstantID(){
		return instantID;
	}
	public void setInstantID(Long instantID){
		this.instantID=instantID;
	}
}
