package com.cssrc.ibms.core.log.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 操作错误日志记录
 * @author Yangbo 2016-7-26
 *
 */
public class SysErrorLog extends BaseModel {
	protected Long id;
	protected String hashcode;
	protected String name;//执行人姓名
	protected String account;//执行人账户
	protected String ip;
	protected String errorurl;
	protected String error;
	protected Date errordate;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}

	public String getHashcode() {
		return this.hashcode;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setErrorurl(String errorurl) {
		this.errorurl = errorurl;
	}

	public String getErrorurl() {
		return this.errorurl;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return this.error;
	}

	public void setErrordate(Date errordate) {
		this.errordate = errordate;
	}

	public Date getErrordate() {
		return this.errordate;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysErrorLog)) {
			return false;
		}
		SysErrorLog rhs = (SysErrorLog) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(
				this.hashcode, rhs.hashcode).append(this.account, rhs.account)
				.append(this.ip, rhs.ip).append(this.errorurl, rhs.errorurl)
				.append(this.error, rhs.error).append(this.errordate,
						rhs.errordate).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.hashcode).append(this.account).append(this.ip)
				.append(this.errorurl).append(this.error)
				.append(this.errordate).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"hashcode", this.hashcode).append("account", this.account)
				.append("ip", this.ip).append("errorurl", this.errorurl)
				.append("error", this.error)
				.append("errordate", this.errordate).toString();
	}
}
