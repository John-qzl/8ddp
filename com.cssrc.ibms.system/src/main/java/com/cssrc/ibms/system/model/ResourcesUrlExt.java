package com.cssrc.ibms.system.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement(namespace = "bpm")
public class ResourcesUrlExt extends ResourcesUrl {
	private String func = "";
	private String role = "";

	public String getFunc() {
		return this.func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
