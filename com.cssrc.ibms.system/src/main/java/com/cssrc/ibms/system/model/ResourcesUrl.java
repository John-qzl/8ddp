package com.cssrc.ibms.system.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 功能点URL管理(可添加外部网址)
 * <p>Title:ResourcesUrl</p>
 * @author Yangbo 
 * @date 2016-8-22下午09:46:48
 */
public class ResourcesUrl extends BaseModel {
	protected Long resUrlId;
	protected Long resId;
	protected String name;
	protected String url;
	
	public Long getResUrlId() {
		return resUrlId;
	}

	public void setResUrlId(Long resUrlId) {
		this.resUrlId = resUrlId;
	}

	public Long getResId() {
		return resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public boolean equals(Object object) {
		if (!(object instanceof ResourcesUrl)) {
			return false;
		}
		ResourcesUrl rhs = (ResourcesUrl) object;
		return new EqualsBuilder().append(this.resUrlId, rhs.resUrlId).append(
				this.resId, rhs.resId).append(this.name, rhs.name).append(
				this.url, rhs.url).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.resUrlId)
				.append(this.resId).append(this.name).append(this.url)
				.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("urlId", this.resUrlId)
				.append("resId", this.resId).append("name", this.name).append(
						"url", this.url).toString();
	}
}
