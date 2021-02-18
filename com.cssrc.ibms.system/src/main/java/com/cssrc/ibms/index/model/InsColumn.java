package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class InsColumn {
	private String colId;

	private Long typeId;

	private String name;

	private String key;

	private String url;

	private String enabled;

	private Integer numsOfPage;

	private String allowClose;

	private String colType;

	private String orgId;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;
	//栏目高
	private Integer height = Integer.valueOf(300);

	public String getColId() {
		return colId;
	}
	
	public void setColId(Long colId) {
	    if(colId!=null){
	        this.colId = colId.toString();
	    }
	}
	
	public Serializable getPkId()
	{
		return this.colId;
	}

	public void setPkId(Serializable pkId)
	{

        if(pkId!=null){
            this.colId = pkId.toString();
        }
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key == null ? null : key.trim();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url == null ? null : url.trim();
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled == null ? null : enabled.trim();
	}

	public Integer getNumsOfPage() {
		return numsOfPage;
	}

	public void setNumsOfPage(Integer numsOfPage) {
		this.numsOfPage = numsOfPage;
	}

	public String getAllowClose() {
		return allowClose;
	}

	public void setAllowClose(String allowClose) {
		this.allowClose = allowClose == null ? null : allowClose.trim();
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType == null ? null : colType.trim();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}


	public boolean equals(Object object)
	{
		if (!(object instanceof InsColumn)) {
			return false;
		}
		InsColumn rhs = (InsColumn)object;
		return new EqualsBuilder().append(this.colId, rhs.colId).append(this.name, rhs.name).append(this.key, rhs.key).append(this.enabled, rhs.enabled)
				.append(this.numsOfPage, rhs.numsOfPage).append(this.allowClose, rhs.allowClose).append(this.colType, rhs.colType).append(this.orgId, rhs.orgId)
				.append(this.createBy, rhs.createBy).append(this.createTime, rhs.createTime).append(this.updateBy, rhs.updateBy).append(this.updateTime, rhs.updateTime)
				.append(this.height, rhs.height).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973).append(this.colId).append(this.name).append(this.key).append(this.enabled).append(this.numsOfPage)
				.append(this.allowClose).append(this.colType).append(this.orgId)
				.append(this.createBy).append(this.createTime).append(this.updateBy)
				.append(this.updateTime).append(this.height).toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("colId", this.colId).append("name", this.name).append("key", this.key).append("enabled", this.enabled)
				.append("numsOfPage", this.numsOfPage).append("allowClose", this.allowClose).append("colType", this.colType).append("orgId", this.orgId)
				.append("createBy", this.createBy).append("createTime", this.createTime).append("updateBy", this.updateBy).append("updateTime", this.updateTime)
				.append("height", this.height).toString();
	}
}
