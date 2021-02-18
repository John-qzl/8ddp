package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 角色Model(有关组织和用户的权限问题)
 * <p>Title:SysRole</p>
 * @author Yangbo 
 * @date 2016-8-18下午05:15:06
 */
public class SysRole extends BaseModel implements GrantedAuthority, Cloneable,ISysRole {

	@SysFieldDescription(detail="角色ID")
	protected Long roleId;
	@SysFieldDescription(detail="别名")
	protected String alias;// 别名
	@SysFieldDescription(detail="角色名")
	protected String roleName;
	@SysFieldDescription(detail="角色描述")
	protected String roleDesc;// memo,备注描述
	@SysFieldDescription(detail="是否允许删除",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short allowDel;// 允许删除
	@SysFieldDescription(detail="是否允许编辑",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short allowEdit;// 允许编辑(0,不允许,1,允许)
	@SysFieldDescription(detail="是否启用",maps="{\"1\":\"启用\",\"0\":\"禁用\"}")
	protected Short status;// 是否启用(状态)
	@SysFieldDescription(detail="父id")
	protected Long pId = Long.valueOf(0L);
	@SysFieldDescription(detail="分类")
	protected String category;// 分类

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setAllowDel(Short allowDel) {
		this.allowDel = allowDel;
	}

	public Short getAllowDel() {
		return this.allowDel;
	}

	public void setAllowEdit(Short allowEdit) {
		this.allowEdit = allowEdit;
	}

	public Short getAllowEdit() {
		return this.allowEdit;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysRole)) {
			return false;
		}
		SysRole rhs = (SysRole) object;
		return new EqualsBuilder().append(this.alias, rhs.alias).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.roleId)
				.append(this.alias).append(this.roleName)
				.append(this.roleDesc).append(this.allowDel).append(
						this.allowEdit).append(this.status).append(
						this.category).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("roleId", this.roleId).append(
				"alias", this.alias).append("roleName", this.roleName).append("roleDesc", this.roleDesc)
				.append("allowDel", this.allowDel).append("allowEdit",
						this.allowEdit).append("status", this.status).append(
						"category", this.category).toString();
	}

	public Object clone() {
		SysRole obj = null;
		try {
			obj = (SysRole) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public String getAuthority() {
		return this.alias;
	}
}
