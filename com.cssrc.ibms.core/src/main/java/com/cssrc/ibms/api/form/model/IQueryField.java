package com.cssrc.ibms.api.form.model;

public interface IQueryField {
	public static final Short IS_NOT_SHOW = Short.valueOf((short) 0);

	public static final Short IS_SHOW = Short.valueOf((short) 1);

	public static final Short IS_NOT_SEARCH = Short.valueOf((short) 0);

	public static final Short IS_SEARCH = Short.valueOf((short) 1);
	public static final String DATATYPE_VARCHAR = "varchar";
	public static final String DATATYPE_CLOB = "clob";
	public static final String DATATYPE_DATE = "date";
	public static final String DATATYPE_NUMBER = "number";
	String getName();
	String getFieldDesc();
}