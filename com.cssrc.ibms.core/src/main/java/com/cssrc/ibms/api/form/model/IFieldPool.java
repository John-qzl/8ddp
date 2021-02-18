package com.cssrc.ibms.api.form.model;

/**
 * 字段model
 * 
 * @author zhulongchao
 * 
 */
public interface IFieldPool {

	/** 单行文本框 =1 */
	short TEXT_INPUT = 1;
	/** 多行文本框 =2 */
	short TEXTAREA = 2;
	/** 数据字典 =3 */
	short DICTIONARY = 3;
	/** 人员选择器(单选)=4 */
	short SELECTOR_USER_SINGLE = 4;
	/** 人员选择器(多选)=8 */
	short SELECTOR_USER_MULTI = 8;
	/** 角色选择器(单选)=17 */
	short SELECTOR_ROLE_SINGLE = 17;
	/** 角色选择器（多选）=5 */
	short SELECTOR_ROLE_MULTI = 5;
	/** 组织选择器(单选)=18 */
	short SELECTOR_ORG_SINGLE = 18;
	/** 组织选择器(多选）=6 */
	short SELECTOR_ORG_MULTI = 6;
	/** 岗位选择器(单选=19) */
	short SELECTOR_POSITION_SINGLE = 19;
	/** 岗位选择器(多选）=7 */
	short SELECTOR_POSITION_MULTI = 7;
	/** 附件或文件上传=9 */
	short ATTACHEMENT = 9;
	/** 富文本框ckeditor =10 */
	short CKEDITOR = 10;
	/** 下拉单选项=11 */
	short SELECT_INPUT = 11;
	/** 下拉多选项=28 */
	short SELECTS_INPUT = 28;
	/** 密级管理=26 */
	short SECURITY_CONTROL = 26;
	/** Office控件 =12 */
	short OFFICE_CONTROL = 12;
	/** 复选框 =13 */
	short CHECKBOX = 13;
	/** 单选按钮=14 */
	short RADIO_INPUT = 14;
	/** 日期控件=15 */
	short DATEPICKER = 15;
	/** 隐藏域=16 */
	short HIDEDOMAIN = 16;
	/** 流程引用选择器=20 */
	short SELECTOR_PROCESS_INSTANCE = 20;
	/** WebSign控件=21 */
	short WEBSIGN_CONTROL = 21;
	/** 图片显示控件=21 */
	short PICTURE_SHOW_CONTROL = 22;
	/** 关系列=23 */
	short RELATION_COLUMN_CONTROL = 23;
	
	/** 流程状态字段=24 */
	short FLOW_STATE = 24;

	String FLOW_SPLIT = "@";
	String FLOW_START_KEY = "start";
	String FLOW_END_KEY = "end";
	String FLOW_START_VAL = "流程未发起";
	String FLOW_END_VAL = "流程已结束";
	
	/** 数据库类型-字符串=varchar */
	String DATATYPE_VARCHAR = "varchar";
	/** 数据库类型-大文本=clob */
	String DATATYPE_CLOB = "clob";
	/** 数据库类型-日期=date */
	String DATATYPE_DATE = "date";
	/** 数据库类型-数字=number */
	String DATATYPE_NUMBER = "number";
	/****外键显示列构造。**/
	String FK_TABLEFIELD="target";
    String FK_LISTSHOW="listShow";
    String FK_DIALOGFIELD="src";

	String FK_SHOWAppCode = "XXXXXFKColumnShow";
	String FK_SHOWAppNAME = "外键显示列";
	String rpcrefname = "rpcrefname";//rpc远程接口
	String fkColumnShow = "fkColumnShow";//获取外键显示列。如F_MC,F_MT,可以是多个，用“，”分开
	String fkIdName = "fkIdName";//外键对应表的主键名称,如ID
	String fkColumn = "fkColumn";//外键显示列。如kf_test_id
}
