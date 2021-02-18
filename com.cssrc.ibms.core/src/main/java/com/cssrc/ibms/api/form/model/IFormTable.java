package com.cssrc.ibms.api.form.model;

import java.util.List;
import java.util.Map;


public interface IFormTable {
	// ======常量======
	public static String ParElmName = "table";
	public static String SubElmName = "subTable";
	/** 未发布 =0 */
	public final static Short NOT_PUBLISH = 0;
	/** 发布 =1 */
	public final static Short IS_PUBLISH = 1;
	/** 主表=0 */
	public final static Short IS_MAIN = 1;
	/** 从表=1 */
	public final static Short NOT_MAIN = 0;
	/** 主键类型 数字=0 */
	public final static Short PKTYPE_NUMBER = 0;
	/** 主键类型 字符串=1 */
	public final static Short PKTYPE_STRING = 1;
	/** 外部表=1 */
	public final static int EXTERNAL = 1;
	/** 不是外部表=1 */
	public final static int NOT_EXTERNAL = 0;

	/** 正常字段=0 */
	public final static int NEED_NORMAL = 0;
	/** 正常字段加上隐藏字段=1 */
	public final static int NEED_HIDE = 1;
	/** 正常字段加上逻辑删除字段 */
	public final static int NEED_DELETE = 2;
	
    
	boolean isExtTable();
	Object getKeyDataType();
	String getTableName();
	String getDsAlias();
	int getIsExternal();
	Long getTableId();
	String getTableDesc();
	String getPkField();
	Short getIsMain();
	Object getMainTableId();
	List<? extends IFormTable> getSubTableList();
	List<? extends IFormTable> getRelTableList();
	Map<String, String> getVariable();
	void setVariable(Map<String, String> vars);
	void setPkField(String lowerCase);
	String getRelation();
	void setRelation(String lowerCase);
	List<? extends IFormField> getFieldList();
	String getFactTableName();
	String getDbTableName();
}