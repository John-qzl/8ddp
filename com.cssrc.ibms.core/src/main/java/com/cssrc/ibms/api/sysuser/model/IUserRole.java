package com.cssrc.ibms.api.sysuser.model;

public interface IUserRole {

	public abstract void setRoleId(Long roleId);

	public abstract Long getRoleId();

	public abstract void setUserId(Long userId);

	public abstract Long getUserId();

	public abstract void setUserRoleId(Long userRoleId);

	public abstract Long getUserRoleId();

	public abstract String getFullname();

	public abstract void setFullname(String fullname);

	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getRoleName();

	public abstract void setRoleName(String roleName);

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

	public abstract Object clone();

}