package com.cssrc.ibms.api.form.model;

import java.util.List;
import java.util.Map;



public interface IFormData {

	String getTableName();

	Map<String, Object> getVariables();

	IPkValue getPkValue();

	IFormTable getFormTable();

	boolean isExternal();

	Map<String, Object> getMainFields();

	Map<String, String> getOptions();

	String getDsAlias();

	void setPkValue(IPkValue newPkValue);

	void setMainFields(Map<String, Object> mainData);

	List<? extends ISubTable> getSubTableList();

	void setSubTableList(List<?extends ISubTable> newSubTables);

	boolean isAdd();

	Object getMainField(String fieldName);

	String getFullTableName();

	Map<String, Object> getMainCommonFields();

    List<?extends IRelTable> getRelTableList();

    long getTableId();
	
	
}