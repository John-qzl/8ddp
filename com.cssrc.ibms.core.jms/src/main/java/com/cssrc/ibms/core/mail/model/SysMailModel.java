package com.cssrc.ibms.core.mail.model;

import java.io.Serializable;
import java.util.Map;
/**
 * 
 * <p>Title:SysMailModel</p>
 * @author Yangbo 
 * @date 2016年9月29日下午3:34:30
 */
public class SysMailModel implements Serializable {
	private Mail mail;
	private Long outMailUserSetingId;
	private Map vars;

	public Mail getMail() {
		return this.mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public Long getOutMailUserSetingId() {
		return this.outMailUserSetingId;
	}

	public void setOutMailUserSetingId(Long outMailUserSetingId) {
		this.outMailUserSetingId = outMailUserSetingId;
	}

	public Map getVars() {
		return this.vars;
	}

	public void setVars(Map vars) {
		this.vars = vars;
	}
}
