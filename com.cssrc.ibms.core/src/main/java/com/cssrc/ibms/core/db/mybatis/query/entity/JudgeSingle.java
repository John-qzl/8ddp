package com.cssrc.ibms.core.db.mybatis.query.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

public class JudgeSingle extends FieldTable {
	protected String compare;
	protected String value;

	public String getCompare() {
		return this.compare;
	}

	public void setCompare(String compare) {
		this.compare = compare;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return new ToStringBuilder(this).append("fieldName", this.fieldName)
				.append("compare", this.compare).append("value", this.value)
				.toString();
	}
}
