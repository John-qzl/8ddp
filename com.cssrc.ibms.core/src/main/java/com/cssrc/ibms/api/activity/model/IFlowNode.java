package com.cssrc.ibms.api.activity.model;


public interface IFlowNode {
	public static final String TYPE_USERTASK="userTask";
	public static final String TYPE_SUBPROCESS="subProcess";
	public static final String TYPE_START_EVENT="startEvent";
	String getNodeId();
	public String getNodeName();

}