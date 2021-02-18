package com.cssrc.ibms.core.web.tag;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.core.util.string.StringUtil;

public class ResourcesTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -2247520825166062410L;
	private String alias;
	private String name;
	
	@Override
	protected int doStartTagInternal() throws Exception {
		if("ROOT_PID".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IResources.ROOT_PID);
			}else{
				pageContext.getOut().print(IResources.ROOT_PID);
			}
		}else if("ROOT_ID".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IResources.ROOT_ID);
			}else{
				pageContext.getOut().print(IResources.ROOT_ID);
			}
		}
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
