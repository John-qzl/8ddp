package com.cssrc.ibms.api.activity.model;

public interface ITaskExe {
	/**	0=初始状态*/
	public final static Short STATUS_INIT =0;
	/** 1=完成  --由代理人来完成*/
	public final static Short STATUS_COMPLETE =1;
	/**2=取消  --代理人撤消*/
	public final static Short STATUS_CANCEL =2;
	/**	3=任务完成  --由其他人来完成*/
	public final static Short STATUS_OTHER_COMPLETE =3;
	/**	4=驳回*/
	public final static Short STATUS_BACK =4;
	
	/**	1=代理*/
	public final static Short TYPE_ASSIGNEE=1;
	/**	2=转办*/
	public final static Short TYPE_TRANSMIT =2;
	/**	3=流转*/
	public final static Short TYPE_TRANSTO =3;
	Long getTypeId();
}