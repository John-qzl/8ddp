package com.cssrc.ibms.api.migration.model;

import java.util.List;

public interface IOutTable {
	public static final String USER_TABLE =  "cwm_sys_user";
	public static final String DEPT_TABLE =  "cwm_sys_department";
	String getTableName();
	String getTableDesc();
	Short getIsMain();
	List<IOutField> getField();
	boolean isExceptTable();//除外的表（不进行导入）
}