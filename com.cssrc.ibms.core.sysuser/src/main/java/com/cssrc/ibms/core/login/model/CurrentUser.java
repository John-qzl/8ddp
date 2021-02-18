package com.cssrc.ibms.core.login.model;

import com.cssrc.ibms.api.sysuser.model.ICurrentUser;

public class CurrentUser implements ICurrentUser{
	private Long userId = Long.valueOf(0L);

	private String username = "";//account

	private String fullname = "";//name
	private Long orgId = Long.valueOf(0L);

	private Long posId = Long.valueOf(0L);

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
		return fullname;
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

	public Long getPosId() {
		return this.posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}
}
