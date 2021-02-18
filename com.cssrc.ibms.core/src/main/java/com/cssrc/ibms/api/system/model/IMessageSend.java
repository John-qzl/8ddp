package com.cssrc.ibms.api.system.model;

public interface IMessageSend {
	public final static String MESSAGETYPE_PERSON="1";//个人信息
	public final static String MESSAGETYPE_SCHEDULE="2";//日程安排 
	public final static String MESSAGETYPE_PLAN="3";//计划任务
	public final static String MESSAGETYPE_SYSTEM="4";//系统信息
	public final static String MESSAGETYPE_AGENCY ="5";//代办提醒
	public final static String MESSAGETYPE_FLOWTASK ="6";//流程提醒
	
	public final static String SPLIT_FLAG = "[userorg]";

	String getSubject();

	String getReceiverName();

	String getContent();

	Long getCreateBy();

	Long getId();

	Long getRid();
	
}