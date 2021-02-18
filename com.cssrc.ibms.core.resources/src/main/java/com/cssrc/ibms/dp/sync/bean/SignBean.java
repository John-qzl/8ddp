package com.cssrc.ibms.dp.sync.bean;

public class SignBean{
	private String name;
	private String value;
	private String signId;
	private String time;
	private String postid;
	private String remark;
	private String signDef;
	private String signorder;
	
	public String getSignorder() {
		return signorder;
	}
	public void setSignorder(String signorder) {
		this.signorder = signorder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPostid() {
		return postid;
	}
	public void setPostid(String postid) {
		this.postid = postid;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getSignDef() {
		return signDef;
	}
	public void setSigndef(String signDef) {
		this.signDef = signDef;
	}
	
}
