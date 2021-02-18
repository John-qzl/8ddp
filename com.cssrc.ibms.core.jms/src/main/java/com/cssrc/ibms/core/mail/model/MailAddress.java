package com.cssrc.ibms.core.mail.model;

import java.io.Serializable;

public class MailAddress implements Serializable {
	private static final long serialVersionUID = -2851027420438874175L;
	protected String address = "";

	protected String name = "";

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MailAddress() {
	}

	public MailAddress(String address, String name) {
		this.address = address;
		this.name = name;
	}
}
