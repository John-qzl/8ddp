package com.cssrc.ibms.core.web.tag;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

public class GlobalTypeTag extends RequestContextAwareTag{
	private static final long serialVersionUID = -2247520825166062410L;
	private String name;
	private String alias;

	@Override
	protected int doStartTagInternal() throws Exception {
		if("CAT_FORM".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_FORM);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_REPORT".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_REPORT);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_FILE".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_FILE);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_ATTACH".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_ATTACH);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_REPORT".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_REPORT);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_DIC".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_DIC);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_INDEX_COLUMN".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_INDEX_COLUMN);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}else if("CAT_FLOW".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, IGlobalType.CAT_FLOW);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(name));
			}
		}
		return SKIP_BODY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;

	}	
}
