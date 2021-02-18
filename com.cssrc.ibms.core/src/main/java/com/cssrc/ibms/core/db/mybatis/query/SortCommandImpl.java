package com.cssrc.ibms.core.db.mybatis.query;

import org.apache.commons.lang.builder.HashCodeBuilder;

public class SortCommandImpl implements CriteriaCommand {
	private String sortName;
	private String ascDesc;
	private QueryFilter filter;

	public SortCommandImpl(String sortName, String ascDesc, QueryFilter filter) {
		this.sortName = sortName;
		this.ascDesc = ascDesc;
		this.filter = filter;
	}

	public String getSortName() {
		return this.sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getAscDesc() {
		return this.ascDesc;
	}

	public void setAscDesc(String ascDesc) {
		this.ascDesc = ascDesc;
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.sortName)
				.append(this.ascDesc).toHashCode();
	}

	public String getPartHql() {
		return this.sortName + " " + this.ascDesc;
	}
	public String getPartSql() {
		return this.sortName.replace("##", "_") + " " + this.ascDesc;
	}

}

