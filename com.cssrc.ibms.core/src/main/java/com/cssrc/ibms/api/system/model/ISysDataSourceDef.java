package com.cssrc.ibms.api.system.model;

/**
 * ISysDataSourceDef
 * @author liubo
 * @date 2017年4月14日
 */
public interface ISysDataSourceDef {

	public abstract String getCloseMethod();
	
	public abstract String getClassPath();
	
	public abstract String getSettingJson();
	
	public abstract String getInitMethod();
	
	public abstract String getDbType();
	
	public abstract String getAlias();
}