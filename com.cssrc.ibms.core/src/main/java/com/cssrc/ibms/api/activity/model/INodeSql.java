package com.cssrc.ibms.api.activity.model;

/**
 * INodeSql
 * @author liubo
 * @date 2017年2月17日
 */
public interface INodeSql {
	
	public static String ACTION_SUBMIT = "submit";
	
	public static String ACTION_END = "end";
	
	String getSql();
	
	String getDsAlias();
}