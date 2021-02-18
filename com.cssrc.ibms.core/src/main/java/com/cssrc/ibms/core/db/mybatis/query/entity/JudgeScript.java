package com.cssrc.ibms.core.db.mybatis.query.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

public class JudgeScript {
	protected String value;

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString() {
		return new ToStringBuilder(this).append("value", this.value).toString();
	}
}
