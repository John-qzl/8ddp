package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 授权用户
 * 
 * <p>Title:AuthRole</p>
 * @author Yangbo 
 * @date 2016-7-30下午02:41:45
 */
public class AuthRole extends BaseModel {
	protected Long id;
	protected Long authId;
	protected Long roleId;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public AuthRole(long id, Long authId, Long roleId) {
		this.id = Long.valueOf(id);
		this.authId = authId;
		this.roleId = roleId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

	public Long getAuthId() {
		return this.authId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof AuthRole)) {
			return false;
		}
		AuthRole rhs = (AuthRole) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.authId,
				rhs.authId).append(this.roleId, rhs.roleId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.authId).append(this.roleId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("authId",
				this.authId).append("roleId", this.roleId).toString();
	}
}
