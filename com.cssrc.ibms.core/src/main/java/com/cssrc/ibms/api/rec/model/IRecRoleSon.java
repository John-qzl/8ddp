package com.cssrc.ibms.api.rec.model;

import java.util.List;

public interface IRecRoleSon {
	public Long getRoleSonId();

	public void setRoleSonId(Long roleSonId);

	public Long getTypeId();

	public void setTypeId(Long typeId);

	public String getTypeName();

	public void setTypeName(String typeName);

	public Long getDataTemplateId();

	public void setDataTemplateId(Long dataTemplateId);

	public Long getDataId();

	public void setDataId(Long dataId);

	public String getRoleSonName();

	public void setRoleSonName(String roleSonName);

	public String getAlias();

	public void setAlias(String alias);

	public String getRoleSonDesc();

	public void setRoleSonDesc(String roleSonDesc);

	public String getFilter();

	public void setFilter(String filter);

	public String getUserAdd();

	public void setUserAdd(String userAdd);

	public String getUserDel();

	public void setUserDel(String userDel);

	public Long getRoleId();

	public void setRoleId(Long roleId);
}
