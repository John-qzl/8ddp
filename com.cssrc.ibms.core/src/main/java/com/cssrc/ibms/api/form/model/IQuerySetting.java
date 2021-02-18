package com.cssrc.ibms.api.form.model;

public interface IQuerySetting {
	public static final int RIGHT_TYPE_SHOW = 0;
	public static final int RIGHT_TYPE_PRINT = 1;
	public static final int RIGHT_TYPE_EXPORT = 2;
	public static final int RIGHT_TYPE_FILTER = 3;
	public static final int RIGHT_TYPE_MANAGE = 4;
	public static final String PARAMS_KEY_FILTERKEY = "__filterKey__";
	public static final String PARAMS_KEY_CTX = "__ctx";
	public static final String PARAMS_KEY_ISQUERYDATA = "__isQueryData";
	public static final String EXPORT_BUTTON = "exportButton";
	public static final String CONDITION_AND = " AND ";
	public static final String CONDITION_OR = " OR ";
	public static final String DATE_BEGIN = "begin";
	public static final String DATE_END = "end";
	public static final String PAGE = "p";
	public static final String PAGESIZE = "z";
	public static final String SORTFIELD = "s";
	public static final String ORDERSEQ = "o";
	public static final int STYLE_LIST = 0;
	public static final int STYLE_TREE = 1;
	public static final Short NO_NEED_PAGE = Short.valueOf((short) 0);
	public static final Short NEED_PAGE = Short.valueOf((short) 1);
	Long getId();
}