package com.cssrc.ibms.api.form.model;

public interface ITableModel {
	/**
	 * 自定义字段的字段前缀(F_)
	 */
	public static String CUSTOMER_COLUMN_PREFIX = "F_";
	/**
	 * 自定义表的表前缀(W_)
	 */
	public static String CUSTOMER_TABLE_PREFIX = "W_";
	/**
	 * 自定义表的主键(ID)
	 */
	public static final String PK_COLUMN_NAME = "ID";
	/**
	 * 自定义表的外键(REFID)
	 */
	public static final String FK_COLUMN_NAME = "REFID";
	/**
	 * 自定义表的索引前缀(IDX_)
	 */
	public static final String CUSTOMER_INDEX_PREFIX = "IDX_";
	
	/**
	 * 历史业务数据表名的后缀(_HISTORY)
	 */
	public static final String CUSTOMER_TABLE_HIS_SUFFIX="_HISTORY";

	/**
	 * 新添加的表通用前缀 (TT_)
	 */
	public static final String CUSTOMER_TABLE_COMM_PREFIX="TT_";
	
	/**
	 * 在主表表中默认添加用户字段。(curentUserId_)
	 */
	public static final String CUSTOMER_COLUMN_CURRENTUSERID="curentUserId_";
	/**
	 * 在主表和从表 表中默认添加组织字段。
	 */
	public static final String CUSTOMER_COLUMN_CURRENTORGID="curentOrgId_";
	/**
	 * 流程运行ID
	 */
	public static final String CUSTOMER_COLUMN_FLOWRUNID="flowRunId_";
	/**
	 * 流程定义ID
	 */
	public static final String CUSTOMER_COLUMN_DEFID="defId_";
	
}