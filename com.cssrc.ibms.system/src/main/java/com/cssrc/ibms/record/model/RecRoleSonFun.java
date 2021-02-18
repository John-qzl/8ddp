package com.cssrc.ibms.record.model;

import com.cssrc.ibms.api.rec.model.IRecRoleSonFun;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Description:
 * <p>RecRoleSonFun.java</p>
 * @author dengwenjie 
 * @date 2017年4月7日
 */
public class RecRoleSonFun  extends BaseModel
implements Cloneable,IRecRoleSonFun{
	protected Long roleSonFunId;
	protected Long roleSonId;//记录角色id
	protected Long funId;//记录功能点id
	protected String buttons;//已分配的按钮信息
	

	public String getButtons() {
		return buttons;
	}

	public void setButtons(String buttons) {
		this.buttons = buttons;
	}

	public Long getRoleSonFunId() {
		return roleSonFunId;
	}

	public void setRoleSonFunId(Long roleSonFunId) {
		this.roleSonFunId = roleSonFunId;
	}

	public Long getRoleSonId() {
		return roleSonId;
	}

	public void setRoleSonId(Long roleSonId) {
		this.roleSonId = roleSonId;
	}

	public Long getFunId() {
		return funId;
	}

	public void setFunId(Long funId) {
		this.funId = funId;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof RecRoleSonFun))
		{
			return false;
		}
		RecRoleSonFun rhs = (RecRoleSonFun)object;
		return new EqualsBuilder()
		.append(this.roleSonFunId, rhs.roleSonFunId)
		.append(this.roleSonId, rhs.roleSonId)
		.append(this.funId, rhs.funId)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.roleSonFunId)
		.append(this.roleSonId)
		.append(this.funId)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("roleSonFunId", this.roleSonFunId)
		.append("roleSonId", this.roleSonId)
		.append("funId", this.funId)
		.toString();
	}

	public Object clone()
	{
		RecRoleSonFun obj = null;
		try {
			obj = (RecRoleSonFun)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}

