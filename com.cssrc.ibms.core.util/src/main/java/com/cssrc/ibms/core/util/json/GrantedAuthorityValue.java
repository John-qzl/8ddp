package com.cssrc.ibms.core.util.json;

public class GrantedAuthorityValue {
	public String authority = "";

	public GrantedAuthorityValue() {
	}

	public GrantedAuthorityValue(Object obj) {
		this.authority = obj.toString();
	}
}
