package com.cssrc.ibms.api.form.model;

public interface IFormDef {
	//在同一个页面中编辑
	public static final String EditInline="edit";
	//在窗口中编辑
	public static final String EditForm="form";
	/**
	 * 设计类型：通过表生成
	 */
	public static final int DesignType_FromTable = 0;
	/**
	 * 设计类型：自定义
	 */
	public static final int DesignType_CustomDesign = 1;	
	//在窗口中编辑
	public static final String EditWindow="window";
	// 分页
	public static String PageSplitor = "#page#";
	/** 不是默认版本 */
	public static final Short IS_NOT_DEFAULT = 0;
	/** 是默认版本 */
	public static final Short IS_DEFAULT = 1;
	/** 未发布 */
	public static final Short IS_NOT_PUBLISHED = 0;
	/** 发布*/
	public static final Short IS_PUBLISHED = 1;
	Long getTableId();
	Long getFormKey();
	Long getFormDefId();
	String getFormDesc();
	void setTableName(String tableName);
	String getTableName();
	String getSubject();
	String getCategoryName();
	String getHtml();
	int getDesignType();
	String getTabTitle();
	
}