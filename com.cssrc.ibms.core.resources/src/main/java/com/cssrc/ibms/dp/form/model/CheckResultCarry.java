package com.cssrc.ibms.dp.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "checkResultCarry")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckResultCarry {
	/*主键*/
	@XmlAttribute
	protected Long ID;
	/*检查结果*/
	@XmlAttribute
	protected String result;
	/*检查值*/
	@XmlAttribute
	protected String value;
	/*是否为空*/
	@XmlAttribute
	protected String ifnumm;
	/*示意图*/
	@XmlAttribute
	protected String sketchmap;
	/*所属检查项*/
	@XmlAttribute
	protected Long itemID;
	/*所属表格实例*/
	@XmlAttribute
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
