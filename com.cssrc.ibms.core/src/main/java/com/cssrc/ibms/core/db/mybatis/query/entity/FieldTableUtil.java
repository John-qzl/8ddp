package com.cssrc.ibms.core.db.mybatis.query.entity;

import org.apache.commons.lang.StringUtils;

public class FieldTableUtil {
	public static String fixFieldName(String fieldName, String tableName,
			boolean isAs) {
		return fixFieldName(fieldName, fieldName, tableName, isAs);
	}

	public static String fixFieldName(String fieldName, String tableName) {
		return fixFieldName(fieldName, tableName, false);
	}

	public static String fixFieldName(String fieldName, String entityName,
			String tableName, boolean isAs) {
		if (StringUtils.isEmpty(tableName))
			return fieldName;
		StringBuilder sb = new StringBuilder();
		sb.append(tableName.toLowerCase()).append(".").append(fieldName);
		if (isAs)
			sb.append(" AS ").append(entityName);
		return sb.toString();
	}
}
