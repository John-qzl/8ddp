package com.cssrc.ibms.api.statistics.model;

public interface ITool {
	public static final Long ROOT_TOOL_ID = 0L;
	public static final String ROOT_TOOL_NAME = "统计工具";
	public static final String ROOT_TOOL_ALIAS = "father";
	public static final String NAME = "tool";
	
	Long getToolId();
	void setToolId(Long toolId);
	String getName();
	void setName(String name);
	String getAlias();
	void setAlias(String alias);
	String getToolDesc();
	void setToolDesc(String toolDesc);	
	public boolean equals(Object object);	
}
