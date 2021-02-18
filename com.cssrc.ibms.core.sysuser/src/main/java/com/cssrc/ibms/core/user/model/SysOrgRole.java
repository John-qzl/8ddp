package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.ISysOrgRole;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 组织角色授权
 * <p>Title:SysOrgRole</p>
 * @author Yangbo 
 * @date 2016-8-5下午02:45:44
 */
public class SysOrgRole extends BaseModel implements ISysOrgRole{
	@SysFieldDescription(detail="ID")
	protected Long id;
	@SysFieldDescription(detail="组织ID")
	protected Long orgid;
	@SysFieldDescription(detail="角色 ID")
	protected Long roleid;
	@SysFieldDescription(detail="表单类型")
	protected Integer fromType = Integer.valueOf(0);
	@SysFieldDescription(detail="组织名")
	protected String orgName = "";
	@SysFieldDescription(detail="角色")
	private SysRole role;
	@SysFieldDescription(detail="是否可删除")
	protected Integer canDel = Integer.valueOf(0);

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}

	public Long getOrgid() {
		return this.orgid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public Long getRoleid() {
		return this.roleid;
	}

	public SysRole getRole() {
		return this.role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getCanDel() {
		return this.canDel;
	}

	public void setCanDel(Integer canDel) {
		this.canDel = canDel;
	}

	public Integer getFromType() {
		return this.fromType;
	}

	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysOrgRole)) {
			return false;
		}
		SysOrgRole rhs = (SysOrgRole) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.orgid,
				rhs.orgid).append(this.roleid, rhs.roleid).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.orgid).append(this.roleid).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("orgid",
				this.orgid).append("roleid", this.roleid).toString();
	}
}
