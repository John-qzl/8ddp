package com.cssrc.ibms.core.web.tag;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

public class SysFileTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -2247520825166062410L;
	private String alias;
	private String name;
	
	@Override
	protected int doStartTagInternal() throws Exception {
		return SKIP_BODY;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
