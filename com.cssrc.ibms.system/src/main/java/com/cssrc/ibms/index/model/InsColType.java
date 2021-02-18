package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class InsColType {
	private Long typeId;

	private String name;

	private String key;

	private String url;

	private String moreUrl;

	private String loadType;

	private String tempId;

	private String tempName;

	private String iconCls;

	private String memo;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private String orgId;

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	
	public Serializable getPkId()
	{
		return this.typeId;
	}

	public void setPkId(Serializable pkId)
	{
		this.typeId = ((Long)pkId);
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

	public String getMoreUrl() {
		return moreUrl;
	}

	public void setMoreUrl(String moreUrl) {
		this.moreUrl = moreUrl == null ? null : moreUrl.trim();
	}

	public String getLoadType() {
		return loadType;
	}

	public void setLoadType(String loadType) {
		this.loadType = loadType == null ? null : loadType.trim();
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId == null ? null : tempId.trim();
	}

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName == null ? null : tempName.trim();
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls == null ? null : iconCls.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy == null ? null : createBy.trim();
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId == null ? null : orgId.trim();
	}


	public boolean equals(Object object)
	{
		if (!(object instanceof InsColType)) {
			return false;
		}
		InsColType rhs = (InsColType)object;
		return new EqualsBuilder().append(this.typeId, rhs.typeId)
				.append(this.name, rhs.name).append(this.key, rhs.key)
				.append(this.url, rhs.url).append(this.memo, rhs.memo)
				.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973).append(this.typeId)
				.append(this.name).append(this.key).append(this.url)
				.append(this.memo).toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("typeId", this.typeId)
				.append("name", this.name).append("key", this.key)
				.append("url", this.url).append("memo", this.memo).toString();
	}
}