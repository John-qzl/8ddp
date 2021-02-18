package com.cssrc.ibms.core.mail.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class OaLinkman extends BaseModel {
	protected Long id;
	protected String name;
	protected String sex;
	protected String phone;
	protected String email;
	protected String company;
	protected String job;
	protected String address;
	protected Date createtime;
	protected int status;
	protected Long userid;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return this.sex;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return this.company;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getJob() {
		return this.job;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public boolean equals(Object object) {
		if (!(object instanceof OaLinkman)) {
			return false;
		}
		OaLinkman rhs = (OaLinkman) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.name, rhs.name).append(this.sex, rhs.sex)
				.append(this.phone, rhs.phone).append(this.email, rhs.email)
				.append(this.company, rhs.company).append(this.job, rhs.job)
				.append(this.address, rhs.address)
				.append(this.createtime, rhs.createtime)
				.append(this.status, rhs.status)
				.append(this.userid, rhs.userid).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.sex).append(this.phone)
				.append(this.email).append(this.company).append(this.job)
				.append(this.address).append(this.createtime)
				.append(this.status).append(this.userid).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("name", this.name).append("sex", this.sex)
				.append("phone", this.phone).append("email", this.email)
				.append("company", this.company).append("job", this.job)
				.append("address", this.address)
				.append("createtime", this.createtime)
				.append("status", this.status).append("userid", this.userid)
				.toString();
	}
}
