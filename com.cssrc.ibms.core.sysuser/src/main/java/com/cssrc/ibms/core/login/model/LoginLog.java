package com.cssrc.ibms.core.login.model;

import java.util.Date;

import com.cssrc.ibms.api.login.model.ILoginLog;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * LoginLog实体
 * 主要是登陆时候记录各种登录情况(不局限成功)
 * 意义在于后续可以根据此实体，实现验证码，登陆错误次数过多锁死等登陆安全机制
 * @author Yangbo
 *2016.7.14
 */
public class LoginLog extends BaseModel implements ILoginLog{
	protected Long id;
	protected String account;
	protected Date loginTime = new Date();
	protected String ip;
	protected Short status;
	protected String desc;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return this.ip;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}

	public boolean equals(Object object) {
		if (!(object instanceof LoginLog)) {
			return false;
		}
		LoginLog rhs = (LoginLog) object;
		return this.id.equals(rhs.getId());
	}

}
