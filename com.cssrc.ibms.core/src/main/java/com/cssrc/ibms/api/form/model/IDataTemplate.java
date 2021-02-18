package com.cssrc.ibms.api.form.model;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.page.PagingBean;

public interface IDataTemplate {
	/** 权限类型（显示）=0 */
	public static final int RIGHT_TYPE_SHOW = 0;
	/** 权限类型（打印）=1 */
	public static final int RIGHT_TYPE_PRINT = 1;
	/** 权限类型（导出）=2 */
	public static final int RIGHT_TYPE_EXPORT = 2;
	/** 权限类型（过滤条件）=3 */
	public static final int RIGHT_TYPE_FILTER = 3;
	/** 权限类型（管理）=4 */
	public static final int RIGHT_TYPE_MANAGE = 4;

	/** 参数标识(过滤条件KEY) */
	public static final String PARAMS_KEY_FILTERKEY = "__filterKey__";
	/** 参数标识(流程定义ID) */
	public static final String PARAMS_KEY_DEFID = "__defId__";
	/** 参数标识(当前路径) */
	public static final String PARAMS_KEY_CTX = "__ctx";
	/** 参数标识(是否初始化查询) */
	public static final String PARAMS_KEY_ISQUERYDATA = "__isQueryData";

	/** SQL 条件 and */
	public static final String CONDITION_AND = " AND ";
	/** SQL 条件 or */
	public static final String CONDITION_OR = " OR ";

	/** 数据来源于自定义表=1 */
	public static final String SOURCE_CUSTOM_TABLE = "1";
	/** 数据来源于其它表=2 */
	public static final String SOURCE_OTHER_TABLE = "2";

	// 功能按钮
	/** 新增 */
	public static final String MANAGE_TYPE_ADD = "add";
	/** 编辑 */
	public static final String MANAGE_TYPE_EDIT = "edit";
	/** 删除 */
	public static final String MANAGE_TYPE_DEL = "del";
	/** 明细 */
	public static final String MANAGE_TYPE_DETAIL = "detail";
	/** 附件 */
	public static final String MANAGE_TYPE_ATTACH = "attach";
	/** 流程监控 */
	public static final String MANAGE_TYPE_PROCESS = "process";
	/** 导入 */
	public static final String MANAGE_TYPE_IMPORT = "import";
	/** 导出 */
	public static final String MANAGE_TYPE_EXPORT = "export";
	/** 打印 */
	public static final String MANAGE_TYPE_PRINT = "print";
	/** 启动流程 */
	public static final String MANAGE_TYPE_START = "start";
	/** 启动1 */
	public static final String MANAGE_TYPE_START1 = "start1";
	/** 启动2 */
	public static final String MANAGE_TYPE_START2 = "start2";
	/** 启动3 */
	public static final String MANAGE_TYPE_START3 = "start3";
	/** 上报数据 */
	public static final String MANAGE_TYPE_REPORTDATA = "reportData";
	/** 接受数据 */
	public static final String MANAGE_TYPE_ACCEPT = "accept";
	/** 拒绝数据 */
	public static final String MANAGE_TYPE_DECLINE = "decline";
	/** 反馈数据 */
	public static final String MANAGE_TYPE_FEEDBACK = "feedBack";
	/** 审批通过 */
	public static final String MANAGE_TYPE_APPROVALS = "approvals";
	/**复杂表单详细 */
	public static final String MANAGE_TYPE_COMPLEXDETAIL = "complexDetail";
	/**
	 * 业务数据表
	 */
	public static final String BUS_TABLE = "IBMS_BUS_LINK";
	public static final String BUS_TABLE_PK = "BUS_PK";
	public static final String BUS_TABLE_PK_STR = "BUS_PK_STR";

	/**
	 * 日期开始结束标记
	 */
	public static final String DATE_BEGIN = "begin";
	public static final String DATE_END = "end";
	
	/**
	 * 数字开始结束标记
	 */
	public static final String NUMBER_BEGIN = "begin";
	public static final String NUMBER_END = "end";

	/** 分页常量 */
	public static final String PAGE = "p";
	/** 分页大小常量 */
	public static final String PAGESIZE = "z";
	/** 排序常量 */
	public static final String SORTFIELD = "s";
	/** 排序方向常量 */
	public static final String ORDERSEQ = "o";

	/** 显示样式 列表 */
	public static final int STYLE_LIST = 0;
	/** 显示样式 树型 */
	public static final int STYLE_TREE = 1;
	
	/** 数据模板与表单权限 -表关联 */
	public static final int RECTYPE_TABLE = 1;
	/** 数据模板与表单权限 -字段关联 */
	public static final int RECTYPE_COLUMN = 2;
	
	public static final long serialVersionUID = 1L;

	Long getDefId();
	String getTemplateAlias();
	String getDisplayField();
	String getExportField();
	String getFilterField();
	String getManageField();
	String getPrintField();
	String  getSortField();
	void setTemplateAlias(String templateAlias);
	void setDisplayField(String displayField);
	void setExportField(String exportField);
	void setFilterField(String filterField);
	void setManageField(String manageField);
	void setPrintField(String printField);
	void setSortField(String sortField);
	Long getId();
	Long getTableId();
	String getFileTempHtml();
	String getProcessTempHtml();
	String getAttacTempHtml();
	String getProcessCondition(); 
	String getMultiTabTempHtml();
	String getRecRightField();
	void setFileTempHtml(String fileTempHtml);
	void setProcessTempHtml(String processTempHtml);
	void setProcessCondition(String processCondition); 
	void setAttacTempHtml(String attacTempHtml);
	void setMultiTabTempHtml(String multiTabTempHtml);
	void setRecRightField(String recRightField);
	public Long getFormKey();
	
	public String getName();
    String getConditionField();
    public String getTemplateHtml();

	public void setTemplateHtml(String templateHtml);
	public void setPageSize(Integer pageSize);
}