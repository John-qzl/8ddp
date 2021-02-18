package com.cssrc.ibms.core.util.json;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class GrantedAuthorityAdapter extends
		XmlAdapter<GrantedAuthorityValue, GrantedAuthority> {
	public GrantedAuthorityValue marshal(GrantedAuthority v) throws Exception {
		return new GrantedAuthorityValue(v.getAuthority());
	}

	public GrantedAuthority unmarshal(GrantedAuthorityValue v) throws Exception {
		return new GrantedAuthorityImpl(v.authority);
	}
}
