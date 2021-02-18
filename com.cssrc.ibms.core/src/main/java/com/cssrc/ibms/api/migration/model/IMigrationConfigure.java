package com.cssrc.ibms.api.migration.model;

public interface IMigrationConfigure {
	public static final String LOG_FILENAME= "数据迁移-表结构导入日志";
	public static final String MIGRATION_TYPE_TDM = "tdm";
	public static final String TDM_VERSION_QMS = "qms";
	public static final String XML_FORM_TEMPLATE = "form_template";//表单模板
	public static final String XML_FORM_DEF = "form_def";//表单设计
	
	String getXmlName();
}
