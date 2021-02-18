package com.cssrc.ibms.api.form.model;

public interface IQuerySql {
	public static final short IS_NOT_CUSTOM_PARAM = 0;
	public static final short IS_CUSTOM_PARAM = 1;
	String getAlias();
	String getName();
	Long getId();
	String getSql();
}