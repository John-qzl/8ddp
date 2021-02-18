package com.cssrc.ibms.api.form.model;

public interface IFormTemplate {
	// 模版类型
	// 主表模版
	public static final String MAIN_TABLE = "main";
	// 子表模版
	public static final String SUB_TABLE = "subTable";
	// 关系表模版
	public static final String REL_TABLE = "relTable";
	// 宏模版
	public static final String MACRO = "macro";
	// 列表模版
	public static final String LIST = "list";
	// 明细模版
	public static final String DETAIL = "detail";
	// 审批意见模板
    public static final String GROUPOPINION = "groupopinion";
    // 代办已办模板
    public static final String PENDINGSETTING = "pendingSetting";
    
	/**
	 * 表管理模板
	 */
	public static final String TABLE_MANAGE = "tableManage";
	/**
	 * 表管理模板
	 */
	public static final String DATA_TEMPLATE = "dataTemplate";
	
	public String getTemplateName();

    public String getHtml();
}