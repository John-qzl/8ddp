package com.cssrc.ibms.core.util.bean;

import java.util.Map;
import java.util.Map.Entry;

public class MapSqlUtils {
	
	public static String mapSql(Map<String, Object> map,String tableName) {
		String sql="";
		String columnName="";
		String value="";
		for(Entry<String, Object> entry:map.entrySet()){
			  columnName+=entry.getKey()+",";
			  value+=":"+entry.getKey()+",";
	        }
		columnName=columnName.substring(0,columnName.length()-1);
		value=value.substring(0,value.length()-1);
		sql="insert into "+ tableName +"( "+columnName+" ) "+"values( "+value+" )";
		return sql;
		
	}
}
