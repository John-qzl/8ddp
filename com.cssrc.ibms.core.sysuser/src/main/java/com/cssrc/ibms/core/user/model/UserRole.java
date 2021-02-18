package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.IUserRole;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 用户角色映射
 * <p>Title:UserRole</p>
 * @author Yangbo 
 * @date 2016-8-6下午02:38:06
 */
public class UserRole extends BaseModel implements Cloneable,IUserRole {
	@SysFieldDescription(detail="用户角色映射ID")
	protected Long userRoleId;
	@SysFieldDescription(detail="角色ID")
	protected Long roleId;
	@SysFieldDescription(detail="用户ID")
	protected Long userId;
	@SysFieldDescription(detail="中文名")
	protected String fullname;
	@SysFieldDescription(detail="用户名称")
	protected String username; //账号，登陆用户名就是user表的username
	@SysFieldDescription(detail="角色名称")
	protected String roleName = "";

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Long getUserRoleId() {
		return this.userRoleId;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public boolean equals(Object object) {
		if (!(object instanceof UserRole)) {
			return false;
		}
		UserRole rhs = (UserRole) object;
		return new EqualsBuilder().append(this.roleId, rhs.roleId).append(
				this.userId, rhs.userId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.roleId)
				.append(this.userId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("roleId", this.roleId).append(
				"userId", this.userId).toString();
	}

	public Object clone() {
		UserRole obj = null;
		try {
			obj = (UserRole) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
