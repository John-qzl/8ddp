package com.cssrc.ibms.core.db.mybatis.query.entity;

public abstract class FieldTable {
	protected String tableName;
	protected String fieldName;
	protected boolean isMain = true;
	protected String fixFieldName;

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isMain() {
		return this.isMain;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}

	public String getFixFieldName() {
		return FieldTableUtil.fixFieldName(this.fieldName, this.tableName);
	}
}
