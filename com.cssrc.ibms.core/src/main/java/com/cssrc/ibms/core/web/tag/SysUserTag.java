package com.cssrc.ibms.core.web.tag;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.util.string.StringUtil;

public class SysUserTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -2247520825166062410L;
	private String alias;
	private String name;
	
	@Override
	protected int doStartTagInternal() throws Exception {
		if("SEARCH_BY_POS".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SEARCH_BY_POS);
			}else{
				pageContext.getOut().print(ISysUser.SEARCH_BY_POS);
			}
		}else if("SEARCH_BY_ORG".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias,ISysUser.SEARCH_BY_ORG );
			}else{
				pageContext.getOut().print(ISysUser.SEARCH_BY_ORG );
			}
		}else if("SEARCH_BY_ROL".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SEARCH_BY_ROL);
			}else{
				pageContext.getOut().print(ISysUser.SEARCH_BY_ROL);
			}
		}else if("SEARCH_BY_ONL".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SEARCH_BY_ONL);
			}else{
				pageContext.getOut().print(ISysUser.SEARCH_BY_ONL);
			}
		}else if("UN_LOCKED".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.UN_LOCKED);
			}else{
				pageContext.getOut().print(ISysUser.UN_LOCKED);
			}
		}else if("LOCKED".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.LOCKED);
			}else{
				pageContext.getOut().print(ISysUser.LOCKED);
			}
		}else if("DYNPWD_STATUS_BIND".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.DYNPWD_STATUS_BIND);
			}else{
				pageContext.getOut().print(ISysUser.DYNPWD_STATUS_BIND);
			}
		}else if("DYNPWD_STATUS_UNBIND".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.DYNPWD_STATUS_UNBIND);
			}else{
				pageContext.getOut().print(ISysUser.DYNPWD_STATUS_UNBIND);
			}
		}else if("DYNPWD_STATUS_OUT".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.DYNPWD_STATUS_OUT);
			}else{
				pageContext.getOut().print(ISysUser.DYNPWD_STATUS_OUT);
			}
		}else if("SECURITY_HEXIN".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SECURITY_HEXIN);
			}else{
				pageContext.getOut().print(ISysUser.SECURITY_HEXIN);
			}
		}else if("SECURITY_ZHONGYAO".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SECURITY_ZHONGYAO);
			}else{
				pageContext.getOut().print(ISysUser.SECURITY_ZHONGYAO);
			}
		}else if("SECURITY_YIBAN".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SECURITY_YIBAN);
			}else{
				pageContext.getOut().print(ISysUser.SECURITY_YIBAN);
			}
		}else if("SECURITY_FEIMI".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SECURITY_FEIMI);
			}else{
				pageContext.getOut().print(ISysUser.SECURITY_FEIMI);
			}
		}else if("FEIMI".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.FEIMI);
			}else{
				pageContext.getOut().print(ISysUser.FEIMI);
			}
		}else if("SKILL_TITLE_1".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SKILL_TITLE_1);
			}else{
				pageContext.getOut().print(ISysUser.SKILL_TITLE_1);
			}
		}else if("SKILL_TITLE_2".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SKILL_TITLE_2);
			}else{
				pageContext.getOut().print(ISysUser.SKILL_TITLE_2);
			}
		}else if("SKILL_TITLE_3".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SKILL_TITLE_3);
			}else{
				pageContext.getOut().print(ISysUser.SKILL_TITLE_3);
			}
		}else if("SKILL_TITLE_4".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SKILL_TITLE_4);
			}else{
				pageContext.getOut().print(ISysUser.SKILL_TITLE_4);
			}
		}else if("SKILL_TITLE_5".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SKILL_TITLE_5);
			}else{
				pageContext.getOut().print(ISysUser.SKILL_TITLE_5);
			}
		}else if("SKILL_TITLE_6".equals(name)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, ISysUser.SKILL_TITLE_6);
			}else{
				pageContext.getOut().print(ISysUser.SKILL_TITLE_6);
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
