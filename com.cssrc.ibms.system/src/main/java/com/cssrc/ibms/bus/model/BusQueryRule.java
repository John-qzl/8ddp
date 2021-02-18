package com.cssrc.ibms.bus.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.db.mybatis.query.Filter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 高级查询规则
 * <p>Title:BusQueryRule</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:12:07
 */
public class BusQueryRule extends BaseModel {
	private static final long serialVersionUID = 1L;
	protected Long id;
	protected String tableName;
	protected Short needPage = Short.valueOf((short) 1);

	protected Integer pageSize = Integer.valueOf(20);

	protected Short isQuery = Short.valueOf((short) 0);

	protected Short isFilter = Short.valueOf((short) 0);
	protected String displayField;
	protected String filterField;
	protected String sortField;
	protected String exportField;
	protected String printField;
	protected Date createtime;
	protected Long createBy;
	protected Date updatetime;
	protected Long updateBy;
	protected String filterKey;
	protected List<Filter> filterList;
	protected Map<String, Boolean> permission;
	protected String url;
	protected Long filterFlag;

	public String getFilterKey() {
		return this.filterKey;
	}

	public void setFilterKey(String filterKey) {
		this.filterKey = filterKey;
	}

	public List<Filter> getFilterList() {
		return this.filterList;
	}

	public void setFilterList(List<Filter> filterList) {
		this.filterList = filterList;
	}

	public Map<String, Boolean> getPermission() {
		return this.permission;
	}

	public void setPermission(Map<String, Boolean> permission) {
		this.permission = permission;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getFilterFlag() {
		return this.filterFlag;
	}

	public void setFilterFlag(Long filterFlag) {
		this.filterFlag = filterFlag;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public Short getNeedPage() {
		return this.needPage;
	}

	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Short getIsQuery() {
		return this.isQuery;
	}

	public void setIsQuery(Short isQuery) {
		this.isQuery = isQuery;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getDisplayField() {
		return this.displayField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	public String getFilterField() {
		return this.filterField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortField() {
		return this.sortField;
	}

	public void setExportField(String exportField) {
		this.exportField = exportField;
	}

	public String getExportField() {
		return this.exportField;
	}

	public void setPrintField(String printField) {
		this.printField = printField;
	}

	public String getPrintField() {
		return this.printField;
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

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Long getUpdateBy() {
		return this.updateBy;
	}

	public Short getIsFilter() {
		return this.isFilter;
	}

	public void setIsFilter(Short isFilter) {
		this.isFilter = isFilter;
	}

	public boolean equals(Object object) {
		if (!(object instanceof BusQueryRule)) {
			return false;
		}
		BusQueryRule rhs = (BusQueryRule) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(
				this.tableName, rhs.tableName).append(this.needPage,
				rhs.needPage).append(this.pageSize, rhs.pageSize).append(
				this.isQuery, rhs.isQuery).append(this.isFilter, rhs.isFilter)
				.append(this.displayField, rhs.displayField).append(
						this.filterField, rhs.filterField).append(
						this.sortField, rhs.sortField).append(this.exportField,
						rhs.exportField)
				.append(this.printField, rhs.printField).append(
						this.createtime, rhs.createtime).append(this.createBy,
						rhs.createBy).append(this.updatetime, rhs.updatetime)
				.append(this.updateBy, rhs.updateBy).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.tableName).append(this.needPage).append(
						this.pageSize).append(this.isQuery).append(
						this.isFilter).append(this.displayField).append(
						this.filterField).append(this.sortField).append(
						this.exportField).append(this.printField).append(
						this.createtime).append(this.createBy).append(
						this.updatetime).append(this.updateBy).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"tableName", this.tableName).append("needPage", this.needPage)
				.append("pageSize", this.pageSize).append("isQuery",
						this.isQuery).append("isFilter", this.isFilter).append(
						"displayField", this.displayField).append(
						"filterField", this.filterField).append("sortField",
						this.sortField).append("exportField", this.exportField)
				.append("printField", this.printField).append("createtime",
						this.createtime).append("createBy", this.createBy)
				.append("updatetime", this.updatetime).append("updateBy",
						this.updateBy).toString();
	}
}
