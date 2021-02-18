package com.cssrc.ibms.core.web.tag;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.string.StringUtil;

public class UserTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -2247520825166062410L;
	private String alias;
	private String paramname;
	
	@Override
	protected int doStartTagInternal() throws Exception {
		if (StringUtil.isNotEmpty(alias)) {
			this.pageContext.setAttribute(alias,
					SysConfConstant.SYS_PARAM_MAP.get(paramname));
		} else {
			this.pageContext.setAttribute(paramname,
					SysConfConstant.SYS_PARAM_MAP.get(paramname));
		}
		return SKIP_BODY;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	
}
