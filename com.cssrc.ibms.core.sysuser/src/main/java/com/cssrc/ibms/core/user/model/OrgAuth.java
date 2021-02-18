package com.cssrc.ibms.core.user.model;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.IOrgAuth;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 
 * <p>Title:OrgAuth</p>
 * @author YangBo 
 * @date 2016-7-30上午10:47:05
 */
public class OrgAuth extends BaseModel implements IOrgAuth{
	private static final long serialVersionUID = 8644964589580429585L;
	protected Long id;
	protected Long userId;
	protected String userName;
	protected Long orgId;
	protected String orgName;
	protected Long dimId;
	protected String dimName;
	protected String orgPerms;
	protected String userPerms;
	protected String posPerms;
	protected String orgauthPerms;
	List<SysRole> assignRoles;
	private String orgPath;
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setDimId(Long dimId) {
		this.dimId = dimId;
	}

	public Long getDimId() {
		return this.dimId;
	}

	public void setOrgPerms(String orgPerms) {
		this.orgPerms = orgPerms;
	}

	public String getOrgPerms() {
		return this.orgPerms;
	}

	public void setUserPerms(String userPerms) {
		this.userPerms = userPerms;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDimName() {
		return this.dimName;
	}

	public void setDimName(String dimName) {
		this.dimName = dimName;
	}

	public List<SysRole> getAssignRoles() {
		return this.assignRoles;
	}

	public void setAssignRoles(List<SysRole> assignRoles) {
		this.assignRoles = assignRoles;
	}

	public String getPosPerms() {
		return this.posPerms;
	}

	public void setPosPerms(String posPerms) {
		this.posPerms = posPerms;
	}

	public String getOrgauthPerms() {
		return this.orgauthPerms;
	}

	public void setOrgauthPerms(String orgauthPerms) {
		this.orgauthPerms = orgauthPerms;
	}

	public String getUserPerms() {
		return this.userPerms;
	}

	public boolean equals(Object object) {
		if (!(object instanceof OrgAuth)) {
			return false;
		}
		OrgAuth rhs = (OrgAuth) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.userId,
				rhs.userId).append(this.orgId, rhs.orgId).append(this.dimId,
				rhs.dimId).append(this.orgPerms, rhs.orgPerms).append(
				this.userPerms, rhs.userPerms).append(this.posPerms,
				rhs.posPerms).append(this.orgauthPerms, rhs.orgauthPerms)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.userId).append(this.orgId).append(this.dimId)
				.append(this.orgPerms).append(this.userPerms).append(
						this.posPerms).append(this.orgauthPerms).toHashCode();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("userId",
				this.userId).append("orgId", this.orgId).append("dimId",
				this.dimId).append("orgPerms", this.orgPerms).append(
				"userPerms", this.userPerms).append("posPerms", this.posPerms)
				.append("orgauthPerms", this.orgauthPerms).toString();
	}

	public String getOrgPath() {
		return this.orgPath;
	}

	public void setOrgPath(String orgPath) {
		this.orgPath = orgPath;
	}
}
