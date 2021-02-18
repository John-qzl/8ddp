package com.cssrc.ibms.system.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 资源授权角色
 * <p>Title:RoleResources</p>
 * @author Yangbo 
 * @date 2016-8-23下午03:52:21
 */
public class RoleResources extends BaseModel
implements Cloneable
{
	protected Long roleResId;
	protected Long roleId;//角色id
	protected Long resId;//功能点id
	

	
	public Long getRoleResId() {
		return roleResId;
	}

	public void setRoleResId(Long roleResId) {
		this.roleResId = roleResId;
	}

	public Long getResId() {
		return resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	

	public boolean equals(Object object)
	{
		if (!(object instanceof RoleResources))
		{
			return false;
		}
		RoleResources rhs = (RoleResources)object;
		return new EqualsBuilder()
		.append(this.roleResId, rhs.roleResId)
		.append(this.roleId, rhs.roleId)
		.append(this.resId, rhs.resId)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.roleResId)
		.append(this.roleId)
		.append(this.resId)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("roleResId", this.roleResId)
		.append("roleId", this.roleId)
		.append("resId", this.resId)
		.toString();
	}

	public Object clone()
	{
		RoleResources obj = null;
		try {
			obj = (RoleResources)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}

