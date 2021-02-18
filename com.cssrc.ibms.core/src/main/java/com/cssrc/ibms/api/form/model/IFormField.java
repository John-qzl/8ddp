package com.cssrc.ibms.api.form.model;

import java.util.Map;


public interface IFormField {

	public static short VALUE_FROM_FORM = 0;// 表单输入
	public static short VALUE_FROM_SCRIPT_SHOW = 1;// 值来源为脚本(显示)。
	public static short VALUE_FROM_SCRIPT_HIDDEN = 2;// 值来源为脚本(不显示)。
	public static short VALUE_FROM_IDENTITY = 3;// 值来源为流水号。

	/**
	 * 标记删除：是
	 */
	public final static int IS_DELETE_Y = 1;
	/**
	 * 标记删除：否
	 */
	public final static int IS_DELETE_N = 0;
	/**
	 * 标记是否为关联表数据
	 */
	public final static String ISRELTABLE = "isRelTable";	
	/** 条件值来源类型，来自表单输入 */
	public static short COND_TYPE_FORM = 0;
	/** 条件值来源类型，来自script */
	public static short COND_TYPE_SCRIPT = 1;
	/** 条件值来源类型，来自固定值 */
	public static short COND_TYPE_FIX = 2;

	/** 隐藏字段 +ID。 */
	public static final String FIELD_HIDDEN = "ID";

	/** 隐藏 */
	public static final short HIDDEN = 1;
	/** 非隐藏 */
	public static final short NO_HIDDEN = 0;

	public static String ElmName = "field";

	public static final String DATATYPE_VARCHAR = "varchar";
	public static final String DATATYPE_CLOB = "clob";
	public static final String DATATYPE_DATE = "date";
	public static final String DATATYPE_NUMBER = "number";
	Short getRelTableType();
	String getFieldName();
	String getFieldDesc();
	String getCtlProperty();
	Short getControlType();
	Long getFieldId();
	String getFieldType();
	Short getIsHidden();
	Short getIsFlowVar();
	Long getTableId();
	String getScript();
	void setScript(String script);
	void setFieldName(String lowerCase);
	void setOptions(String options);
	Map<String, String> getPropertyMap();
    String getFactFiledName();
    String getDatefmt();
    String getRelFormDialogStripCData();
    Short getRelDelType();
    String getEncrypt();
    String getJsonOptions();
    String getDictType();
    String getOptions();
    Short getIsMainData();
    Short getIsList();
}