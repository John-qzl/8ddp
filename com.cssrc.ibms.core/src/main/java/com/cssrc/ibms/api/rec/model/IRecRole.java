package com.cssrc.ibms.api.rec.model;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;


public interface IRecRole {
	public static final String ROLE_SUPER = "ROLE_SUPER"; // 超级角色
	public static final String ROLE_PUBLIC = "ROLE_PUBLIC";// 普通角色
	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";// 匿名角色
	public static final Long SYSTEM_ROLEID = Long.valueOf(-1L);
	public static final Long RIGHT_ROLEID = Long.valueOf(-2L);
	public static final Long CHECK_ROLEID = Long.valueOf(-3L);
	public static final GrantedAuthority ROLE_GRANT_SUPER = new GrantedAuthorityImpl(
			"ROLE_SUPER");
	public static final ConfigAttribute ROLE_CONFIG_PUBLIC = new SecurityConfig(
			"ROLE_PUBLIC");
	public static final ConfigAttribute ROLE_CONFIG_ANONYMOUS = new SecurityConfig(
			"ROLE_ANONYMOUS");
	
	public abstract void setTypeId(Long typeId);

	public abstract Long getTypeId();
	
	public abstract void setRoleId(Long roleId);

	public abstract Long getRoleId();

	public abstract void setAlias(String alias);

	public abstract String getAlias();

	public abstract void setRoleName(String roleName);

	public abstract String getRoleName();

	public abstract void setAllowDel(Short allowDel);

	public abstract Short getAllowDel();

	public abstract void setAllowEdit(Short allowEdit);

	public abstract Short getAllowEdit();

	public abstract String getRoleDesc();

	public abstract void setRoleDesc(String roleDesc);

	public abstract Short getStatus();

	public abstract void setStatus(Short status);

	public abstract String getIsParent();

	public abstract void setIsParent(String isParent);

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

	public abstract Object clone();

	public abstract String getAuthority();

}