package com.cssrc.ibms.dp.form.model;

import java.util.Date;

public class SignResult {
	/*主键*/
	protected Long ID;
	/*签署用户*/
	protected String signuser;
	/*签署时间*/
	protected Date signtime;
	/*备注*/
	protected String remark;
	/*所属签署定义*/
	protected Long signID;
	/*所属表格实例*/
	protected Long instantID;
	
	public Long getID(){
		return ID;
	}
	public void setID(Long ID){
		this.ID=ID;
	}
	
	public String getSignUser(){
		return signuser;
	}
	public void setSignUser(String signuser){
		this.signuser=signuser;
	}
	
	public Date getSignTime(){
		return signtime;
	}
	public void setSignTime(Date signtime){
		this.signtime=signtime;
	}
	
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	
	public Long getSignID(){
		return signID;
	}
	public void setSignID(Long signID){
		this.signID=signID;
	}
	
	public Long getInstantID(){
		return instantID;
	}
	public void setInstantID(Long instantID){
		this.instantID=instantID;
	}
}
