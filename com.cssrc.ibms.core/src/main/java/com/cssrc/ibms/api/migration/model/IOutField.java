package com.cssrc.ibms.api.migration.model;


public interface IOutField {
	public static final String COLUMN_TYPE_STRING = "String";
	public static final String COLUMN_TYPE_DATETIME = "DateTime";
	public static final String COLUMN_TYPE_BOOLEAN = "Boolean";
	public static final String COLUMN_TYPE_DATE = "Date";
	public static final String COLUMN_TYPE_TEXT = "Text";
	public static final String COLUMN_TYPE_INTEGER = "Integer";
	
	public static final String FILEUPLOAD_FIELD_KEY = "[{\"key\":\"name\",\"value\":\"ATTACHMENT\"}]";	
	public static final String PROCESS_FIELD_KEY = "[{\"key\":\"displayName\",\"value\":\"流程状态\"}]";
	public static final String LSH_NAME = "表单编号";
	public static final String LSH_ALIAS = "bdbh";
	/** 字符串类型：最大长度*/
	Integer getCharLen();
	/** 控件类型*/
	Short getControlType();
	/** 字段特有属性设置*/
	String getCtlProperty();
	/** 小数点后精确位数*/
	Integer getDecimalLen();
	/** 数据字典*/
	String getDictType();
	/** 加密算法*/
	String getEncrypt();
	/** 字段显示名*/
	String getFieldDesc();
	/** 字段id*/
	Long getFieldId();
	/** 字段名称*/
	String getFieldName();
	/** 字段类型*/
	String getFieldType();
	/** number型长度*/
	Integer getIntLen();
	/** 0-未删除 1-已删除*/
	Short getIsDeleted();
	/**流程变量 0-否 1-是*/
	Short getIsFlowVar();
	/**是否隐藏 0-否 1-是*/
	Short getIsHidden();
	/**是否主数据  0-否 1-是*/
	Short getIsMainData();
	/**是否主键显示值  0-否 1-是*/
	Short getIsPkShow();
	/**是否流程引用  0-否 1-是*/
	Short getIsReference();
	/**是否必填  0-否 1-是*/
	Short getIsRequired();
	/**是否千分位显示  0-否 1-是*/
	Short getIsShowComdify();
	/**是否唯一  0-否 1-是*/
	Short getIsUnique();
	/**是否签章  0-否 1-是*/
	Short getIsWebSign();
	/**下拉选项值*/
	String getOptions();
	/**原名*/
	String getOriginalName();
	/**记录删除类型*/
	Short getRelDelLMType();
	/**rel关联表记录删除类型*/
	Short getRelDelType();
	/**自定义对话框名称*/
	String getRelFormDialog();
	/**弹出框配置信息*/
	String getRelFormDialogStripCData();
	/**关联表ID*/
	Long getRelTableId();
	/**关联表名称*/
	String getRelTableName();
	/**关联类型*/
	Short getRelTableType();
	/**脚本*/
	String getScript();
	/**脚本id*/
	String getScriptID();
	/**流水号别名*/
	String getSerialNumber();
	/**排序*/
	Integer getSn();
	/** 控件样式*/
	String getStyle();
	/** 表id*/
	Long getTableId();
	/** 验证规则*/
	String getValidRule();
	/** 值来源*/
	Short getValueFrom();
}
