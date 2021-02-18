package com.cssrc.ibms.core.login.model;

import com.cssrc.ibms.api.sysuser.model.IOnlineUser;

public class OnlineUser implements IOnlineUser{
	private String sessionId;
	private Long userId;
	private String username;
	private String fullname;
	private Short sex;
	private Long orgId;
	private String depPath;
	private String roleIds;
	protected String orgName;
	private Short title;

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Short getTitle() {
		return this.title;
	}

	public void setTitle(Short title) {
		this.title = title;
	}

	public Short getSex() {
		return sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getDepPath() {
		return depPath;
	}

	public void setDepPath(String depPath) {
		this.depPath = depPath;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
}
