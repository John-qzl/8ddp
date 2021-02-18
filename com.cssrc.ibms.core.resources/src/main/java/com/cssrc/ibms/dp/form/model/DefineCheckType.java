package com.cssrc.ibms.dp.form.model;

public class DefineCheckType {
	/*主键*/
	Long ID;
	/*名称*/
	String name;
	/*简称*/
	String shortname;
	/*检查类型*/
	int type;
	/*检查项描述*/
	String checkdescribe;
	/*一类单点*/
	String ILdd;
	/*二类单点*/
	String IILdd;
	/*易错难*/
	String ycn;
	/*拧紧力矩要求*/
	String njljyq;
	/*最后一次动作*/
	String zhycdz;
	/*是否多媒体项目*/
	String ifmedia;
	/*所属检查表模板*/
	Long moduleID;
	
	public Long getID(){
		return ID;
	}
	public void setID(Long ID){
		this.ID=ID;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public String getShortname(){
		return shortname;
	}
	public void setShortname(String shortname){
		this.shortname=shortname;
	}
	
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type=type;
	}
	
	public String getDescribe(){
		return checkdescribe;
	}
	public void setDescribe(String describe){
		this.checkdescribe=describe;
	}
	
	public String getILdd(){
		return ILdd;
	}
	public void setILdd(String ILdd){
		this.ILdd=ILdd;
	}
	
	public String getIILdd(){
		return IILdd;
	}
	public void setIILdd(String IILdd){
		this.IILdd=IILdd;
	}
	
	public String getYcn(){
		return ycn;
	}
	public void setYcn(String ycn){
		this.ycn=ycn;
	}
	
	public String getNjljyq(){
		return njljyq;
	}
	public void setNjljyq(String njljyq){
		this.njljyq=njljyq;
	}
	
	public String getZhycdz(){
		return zhycdz;
	}
	public void setZhycdz(String zhycdz){
		this.zhycdz=zhycdz;
	}
	
	public String getIfmedia(){
		return ifmedia;
	}
	public void setIfmedia(String ifmedia){
		this.ifmedia=ifmedia;
	}
	
	public Long getModuleID(){
		return moduleID;
	}
	public void setModuleID(Long moduleID){
		this.moduleID=moduleID;
	}
	
	public void setCheckItem(int cellProperty)
	{
		boolean flag = false; 
		flag = isContainType(2,cellProperty);
		setILdd(flag?"TRUE":"FALSE");
		flag = isContainType(4,cellProperty);
		//setYcx(flag?"TRUE":"FALSE");
	//	setYcn("TRUE");
		flag = isContainType(8,cellProperty);
		setZhycdz(flag?"TRUE":"FALSE");
		flag = isContainType(16,cellProperty);
		setYcn(flag?"TRUE":"FALSE");
		flag = isContainType(32,cellProperty);
		setNjljyq(flag?"TRUE":"FALSE");
		flag = isContainType(64,cellProperty);
		setIILdd(flag?"TRUE":"FALSE");
		flag = isContainType(128,cellProperty);
		setIfmedia(flag?"TRUE":"FALSE");
	}
	
	private boolean isContainType(int type,int cellProperty)
	{
		int retType = type&cellProperty;
		if(retType==type)
			return true;
		else
			return false;
	}
}
