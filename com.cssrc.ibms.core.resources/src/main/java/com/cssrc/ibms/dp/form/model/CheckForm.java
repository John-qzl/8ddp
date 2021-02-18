package com.cssrc.ibms.dp.form.model;

public class CheckForm {
	/*主键*/
	protected Long ID;
	/*模板名称*/
	protected String name;
	/*密级*/
	protected String secrecy;
	/*编号*/
	protected String snum;
	/*所属发次*/
	protected Long pid;
	/*检查表内容*/
	protected String contents;
	/*行数*/
	protected int rownum;
	/*备注*/
	protected String remark;
	/*所属文件夹*/
	protected Long fid;
	/**
	 * 模板类别
	 */
	private String type;
	/**
	 * 模板状态
	 */
	private String status;
	
	/**
	 * 型号id
	 */
	private String moduleId;
	
	public Long getCheckformId(){
		return ID;
	}
	public void setCheckformId(Long ID){
	this.ID=ID;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public String getSecrecy(){
		return secrecy;
	}
	public void setSecrecy(String secrecy){
		this.secrecy=secrecy;
	}
	
	public String getSnum(){
		return snum;
	}
	public void setSnum(String snum){
		this.snum=snum;
	}
	
	public Long getPid(){
		return pid;
	}
	public void setPid(Long pid){
		this.pid=pid;
	}
	
	public String getContents(){
		return contents;
	}
	public void setContents(String contents){
		this.contents=contents;
	}
	
	public int getRownum(){
		return rownum;
	}
	public void setRownum(int rownum){
		this.rownum=rownum;
	}
	
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	
	public Long getFid(){
		return fid;
	}
	public void setFid(Long fid){
		this.fid=fid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

}
