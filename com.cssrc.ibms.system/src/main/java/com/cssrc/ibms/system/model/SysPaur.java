package com.cssrc.ibms.system.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * SysPaur实体
 * @author yangbo
 * 2016-7-19
 *
 */
public class SysPaur extends BaseModel{
	protected Long paurid;
	protected String paurname;
	protected String aliasname;
	protected String paurvalue;
	protected Long userid;

	public void setPaurid(Long paurid) {
		this.paurid = paurid;
	}

	public Long getPaurid() {
		return this.paurid;
	}

	public void setPaurname(String paurname) {
		this.paurname = paurname;
	}

	public String getPaurname() {
		return this.paurname;
	}

	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}

	public String getAliasname() {
		return this.aliasname;
	}

	public void setPaurvalue(String paurvalue) {
		this.paurvalue = paurvalue;
	}

	public String getPaurvalue() {
		return this.paurvalue;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getUserid() {
		return this.userid;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysPaur)) {
			return false;
		}
		SysPaur rhs = (SysPaur) object;
		return new EqualsBuilder().append(this.paurid, rhs.paurid).append(
				this.paurname, rhs.paurname).append(this.aliasname,
				rhs.aliasname).append(this.paurvalue, rhs.paurvalue).append(
				this.userid, rhs.userid).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.paurid)
				.append(this.paurname).append(this.aliasname).append(
						this.paurvalue).append(this.userid).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("paurid", this.paurid).append(
				"paurname", this.paurname).append("aliasname", this.aliasname)
				.append("paurvalue", this.paurvalue).append("userid",
						this.userid).toString();
	}
}
