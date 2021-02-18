package com.cssrc.ibms.api.system.model;

public interface IGlobalType {
	public static final String CAT_PRODUCT_TYPE = "PT";
	public static final String CAT_CAL_UNIT = "CT";
	
	public static final String CAT_FILE_FORMAT = "FILEFORMAT";
	public static final String CAT_BANK_CASH = "BKCH";
	public static final String CAT_ARCH_FOND = "AR_FD";
	public static final String CAT_ARCH_ROLL = "AR_RL";
	public static final String CAT_ROLL_FILE = "AR_RLF";
	
	//分类标识 静态常量
	public static final String CAT_FORM="FORM";
	public static final String CAT_FLOW="FLOW";
	public static final String CAT_FILE="FILE";
	public static final String CAT_ATTACH = "ATTACH_TYPE";
	public static final String CAT_REPORT="REPORT";
	public static final String CAT_DIC = "DIC";
	public static final String CAT_INDEX_COLUMN = "INDEX_COLUMN_TYPE";
	
	public static final String NODE_KEY_DIC = "DIC";
	public static final String CAT_JLQJTZ="JLQJTZ";
	public static final String CAT_KNOWLEDGE = "KNOWLEDGE_TYPE";

	public static final String TYPE_NAME_BPM = "流程分类";
	public static final String TYPE_NAME_DIC = "数据字典";
	public static final Integer DATA_TYPE_TREE = Integer.valueOf(1);
	public static final Integer DATA_TYPE_FLAT = Integer.valueOf(0);
	public static final long ROOT_PID = -1L;
	public static final long ROOT_ID = 0L;
	public static final long ROOT_DEPTH = 0L;
	public static final String IS_PARENT_N = "false";
	public static final String IS_PARENT_Y = "true";
	public static final int IS_LEAF_N = 0;
	public static final int IS_LEAF_Y = 1;
	public static final String NODE_CODE_TYPE_AUTO_N = "0";
	public static final String NODE_CODE_TYPE_AUTO_Y = "1";
	String getNodePath();
	void setNodePath(String nodePath);
	String getTypeName();
	void setTypeName(String typeName);
	void setTypeId(Long proTypeId);
	Long getTypeId();
	String getNodeKey();
	void setNodeKey(String nodeKey);
	String getCatKey();
	void setCatKey(String catKey);
}