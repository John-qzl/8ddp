package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.ISysOrgTactic;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 组织策略
 * <p>Title:SysOrgTactic</p>
 * @author Yangbo 
 * @date 2016-8-4下午08:36:44
 */
public class SysOrgTactic extends BaseModel implements ISysOrgTactic{

	public static Long DEFAULT_ID = Long.valueOf(1L);
	protected Long id;
	protected Short orgTactic; //策略（0、无策略1、按照级别 2、手工选择，3、按照级别+手工选择）
	protected Long demId;
	protected Long orgType;
	protected String orgSelectId;//组织选择ID

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setOrgTactic(Short orgTactic) {
		this.orgTactic = orgTactic;
	}

	public Short getOrgTactic() {
		return this.orgTactic;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public Long getDemId() {
		return this.demId;
	}

	public void setOrgType(Long orgType) {
		this.orgType = orgType;
	}

	public Long getOrgType() {
		return this.orgType;
	}

	public void setOrgSelectId(String orgSelectId) {
		this.orgSelectId = orgSelectId;
	}

	public String getOrgSelectId() {
		return this.orgSelectId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysOrgTactic)) {
			return false;
		}
		SysOrgTactic rhs = (SysOrgTactic) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(
				this.orgTactic, rhs.orgTactic).append(this.demId, rhs.demId)
				.append(this.orgType, rhs.orgType).append(this.orgSelectId,
						rhs.orgSelectId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.orgTactic).append(this.demId).append(this.orgType)
				.append(this.orgSelectId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"orgTactic", this.orgTactic).append("demId", this.demId)
				.append("orgType", this.orgType).append("orgSelectId",
						this.orgSelectId).toString();
	}
}
