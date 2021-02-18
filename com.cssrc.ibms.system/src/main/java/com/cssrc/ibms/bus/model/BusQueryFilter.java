package com.cssrc.ibms.bus.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 
 * <p>Title:BusQueryFilter</p>
 * @author Yangbo 
 * @date 2016-8-9下午04:41:46
 */
public class BusQueryFilter extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final short SHARE = 1;
	public static final short NO_SHARE = 0;
	protected Long id;
	protected Long ruleId;
	protected String tableName;
	protected String filterName;
	protected String filterDesc;
	protected String filterKey;
	protected String queryParameter;
	protected String sortParameter;
	protected Short isShare = Short.valueOf((short) 0);
	protected Date createtime;
	protected Long createBy;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getRuleId() {
		return this.ruleId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return this.filterName;
	}

	public void setFilterDesc(String filterDesc) {
		this.filterDesc = filterDesc;
	}

	public String getFilterDesc() {
		return this.filterDesc;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public String getFilterKey() {
		return this.filterKey;
	}

	public void setQueryParameter(String queryParameter) {
		this.queryParameter = queryParameter;
	}

	public String getQueryParameter() {
		return this.queryParameter;
	}

	public void setSortParameter(String sortParameter) {
		this.sortParameter = sortParameter;
	}

	public String getSortParameter() {
		return this.sortParameter;
	}

	public void setIsShare(Short isShare) {
		this.isShare = isShare;
	}

	public Short getIsShare() {
		return this.isShare;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Long getCreateBy() {
		return this.createBy;
	}

	public boolean equals(Object object) {
		if (!(object instanceof BusQueryFilter)) {
			return false;
		}
		BusQueryFilter rhs = (BusQueryFilter) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.ruleId,
				rhs.ruleId).append(this.tableName, rhs.tableName).append(
				this.filterName, rhs.filterName).append(this.filterDesc,
				rhs.filterDesc).append(this.filterKey, rhs.filterKey).append(
				this.queryParameter, rhs.queryParameter).append(
				this.sortParameter, rhs.sortParameter).append(this.isShare,
				rhs.isShare).append(this.createtime, rhs.createtime).append(
				this.createBy, rhs.createBy).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.ruleId).append(this.tableName).append(
						this.filterName).append(this.filterDesc).append(
						this.filterKey).append(this.queryParameter).append(
						this.sortParameter).append(this.isShare).append(
						this.createtime).append(this.createBy).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("ruleId",
				this.ruleId).append("tableName", this.tableName).append(
				"filterName", this.filterName).append("filterDesc",
				this.filterDesc).append("filterKey", this.filterKey).append(
				"queryParameter", this.queryParameter).append("sortParameter",
				this.sortParameter).append("isShare", this.isShare).append(
				"createtime", this.createtime)
				.append("createBy", this.createBy).toString();
	}
}
