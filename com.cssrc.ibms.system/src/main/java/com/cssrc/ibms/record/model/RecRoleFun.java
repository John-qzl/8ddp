package com.cssrc.ibms.record.model;

import com.cssrc.ibms.api.rec.model.IRecRoleFun;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Description:功能点授权角色（表单）
 * <p>RecRoleFun.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
public class RecRoleFun  extends BaseModel
implements Cloneable,IRecRoleFun{
	protected Long roleFunId;
	protected Long roleId;//表单角色id
	protected Long funId;//表单功能点id
	protected String buttons;//已分配的按钮信息
	

	public String getButtons() {
		return buttons;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	public Long getRoleFunId() {
		return roleFunId;
	}

	public void setRoleFunId(Long roleFunId) {
		this.roleFunId = roleFunId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getFunId() {
		return funId;
	}

	public void setFunId(Long funId) {
		this.funId = funId;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof RecRoleFun))
		{
			return false;
		}
		RecRoleFun rhs = (RecRoleFun)object;
		return new EqualsBuilder()
		.append(this.roleFunId, rhs.roleFunId)
		.append(this.roleId, rhs.roleId)
		.append(this.funId, rhs.funId)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.roleFunId)
		.append(this.roleId)
		.append(this.funId)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("roleFunId", this.roleFunId)
		.append("roleId", this.roleId)
		.append("funId", this.funId)
		.toString();
	}

	public Object clone()
	{
		RecRoleFun obj = null;
		try {
			obj = (RecRoleFun)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}

