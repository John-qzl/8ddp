package com.cssrc.ibms.dbom.sqlmodel;

import java.util.List;
import com.cssrc.ibms.core.util.string.ArrayUtil;

public class SelectSql {
	private String[] fileds;

	public String getSql() {

		if (fileds == null || fileds.length == 0) {
			return "select *";
		} else {
			StringBuffer sql = new StringBuffer("select");
			for (String f : fileds) {
				sql.append(f + ",");
			}
			return sql.substring(0, sql.length() - 1);
		}
	}

	public SelectSql(String field, String spilit) {
		this.fileds = field.split(spilit);
	}

	public SelectSql(String[] fileds) {
		this.fileds = fileds;
	}

	public SelectSql(List<String> fieldList) {
		this.fileds = ArrayUtil.convertArray(fieldList);
	}

}
