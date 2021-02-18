package com.cssrc.ibms.dbom.sqlmodel;

import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.system.model.SysDataSource;

public class FunctionSql
{
    
    public static String toChart(String filed){
        String dbType=SysContextUtil.getJdbcType();
        if(dbType.equals(SysDataSource.DBTYPE_MYSQL)){
            return " CAST(" + filed + " as CHAR)";
        }else if(dbType.equals(SysDataSource.DBTYPE_ORACLE)){
           return " TO_CHAR(" + filed + ")";
        }else{
            return null;
        }
    }

}
