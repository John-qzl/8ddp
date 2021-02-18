package com.cssrc.ibms.api.form.model;

import java.util.List;
import java.util.Map;


public interface ISubTable {

	List<Map<String, Object>> getDataList();

	String getFkName();

	String getPkName();

	void setDataList(List<Map<String, Object>> newSubDatas);

	String getTableName();

    void setTableId(Long tableId);
	
    Long getTableId();
}