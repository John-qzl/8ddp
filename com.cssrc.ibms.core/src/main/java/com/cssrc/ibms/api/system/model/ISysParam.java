package com.cssrc.ibms.api.system.model;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public interface ISysParam {
	public static final int CONDITION_OR = 1;
	public static final int CONDITION_AND = 2;
	public static final int CONDITION_EXP = 3;
	public static final Map<String, String> DATA_COLUMN_MAP = new HashMap();
	public static final short EFFECT_USER = 1;
	public static final short EFFECT_ORG = 2;
	public static final Map<String, String> DATA_TYPE_MAP=new HashMap();
	public static final Map<String, String> CONDITION_US=new HashMap();
	public static SimpleDateFormat PARAM_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-DD");

	String getParamName();
	Long getParamId();
	String getDataType();
	String getSourceKey();
	String getSourceType();
	String getDescription();
	Short getStatus_();
	String getCategory();
	String getParamKey();
	
	
}