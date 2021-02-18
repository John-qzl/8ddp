package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 
 * <p>Title:MonOrgRole</p>
 * @author Yangbo 
 * @date 2016-8-11下午02:22:19
 */
public class MonOrgRole extends BaseModel {
	protected Long id;
	protected Long groupid;
	protected Long roleid;
	protected Long orgid;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public Long getGroupid() {
		return this.groupid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public Long getRoleid() {
		return this.roleid;
	}

	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}

	public Long getOrgid() {
		return this.orgid;
	}

	public boolean equals(Object object) {
		if (!(object instanceof MonOrgRole)) {
			return false;
		}
		MonOrgRole rhs = (MonOrgRole) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.groupid,
				rhs.groupid).append(this.roleid, rhs.roleid).append(this.orgid,
				rhs.orgid).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.groupid).append(this.roleid).append(this.orgid)
				.toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"groupid", this.groupid).append("roleid", this.roleid).append(
				"orgid", this.orgid).toString();
	}
}
