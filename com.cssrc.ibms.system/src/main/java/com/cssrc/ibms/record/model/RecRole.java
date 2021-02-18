package com.cssrc.ibms.record.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import com.cssrc.ibms.api.rec.model.IRecRole;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
public class RecRole  extends BaseModel implements GrantedAuthority, Cloneable,IRecRole{
	@SysFieldDescription(detail="角色类别id")
	protected Long typeId;// 角色类别id
	@SysFieldDescription(detail="角色id")
	protected Long roleId;
	@SysFieldDescription(detail="别名")
	protected String alias;// 别名
	@SysFieldDescription(detail="角色名")
	protected String roleName;
	@SysFieldDescription(detail="备注描述")
	protected String roleDesc;// memo,备注描述
	@SysFieldDescription(detail="允许删除",maps="{\"1\":\"允许\",\"0\":\"不允许\"}")
	protected Short allowDel;// 允许删除
	@SysFieldDescription(detail="允许编辑",maps="{\"1\":\"允许\",\"0\":\"不允许\"}")
	protected Short allowEdit;// 允许编辑(0,不允许,1,允许)
	@SysFieldDescription(detail="是否启用",maps="{\"1\":\"启用\",\"0\":\"未启用\"}")
	protected Short status;// 是否启用(状态)	
	@SysFieldDescription(detail="记录权限设置")
	protected Short allowSet;//记录权限设置 0:不允许  ，1：允许	
	@SysFieldDescription(detail="是否为父",maps="{\"true\":\"是\",\"false\":\"否\"}")
	protected String isParent = "false";
	@SysFieldDescription(detail="filter")
	protected String filter = "";
	
	/*add by dwj 2017年5月31日15:34:55*/
	@SysFieldDescription(detail="记录角色是否隐藏")
	protected Short isHide;//对应的记录角色是否隐藏(0,不隐藏,1,隐藏) 
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	public Short getAllowDel() {
		return allowDel;
	}
	public void setAllowDel(Short allowDel) {
		this.allowDel = allowDel;
	}
	public Short getAllowEdit() {
		return allowEdit;
	}
	public void setAllowEdit(Short allowEdit) {
		this.allowEdit = allowEdit;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Short getAllowSet() {
		return allowSet;
	}
	public void setAllowSet(Short allowSet) {
		this.allowSet = allowSet;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public Short getIsHide() {
		return isHide;
	}
	public void setIsHide(Short isHide) {
		this.isHide = isHide;
	}
	public boolean equals(Object object) {
		if (!(object instanceof RecRole)) {
			return false;
		}
		RecRole rhs = (RecRole) object;
		return new EqualsBuilder().append(this.alias, rhs.alias).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.roleId)
				.append(this.alias).append(this.roleName)
				.append(this.roleDesc).append(this.allowDel).append(
						this.allowEdit).append(this.status).append(
						this.typeId).append(this.isHide).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("roleId", this.roleId).append(
				"alias", this.alias).append("roleName", this.roleName).append("roleDesc", this.roleDesc)
				.append("allowDel", this.allowDel).append("allowEdit",
						this.allowEdit).append("status", this.status).append(
						"typeId", this.typeId).append("isHide", this.isHide).toString();
	}

	public Object clone() {
		RecRole obj = null;
		try {
			obj = (RecRole) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public String getAuthority() {
		return this.alias;
	}
}
