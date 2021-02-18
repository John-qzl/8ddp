package com.cssrc.ibms.api.form.model;

import java.util.List;
import java.util.Map;

public interface IRelTable
{

    List<Map<String, Object>> getRelTableDataList();

    IFormTable getRelFormTable();

    String getRelTableName();
    
}
