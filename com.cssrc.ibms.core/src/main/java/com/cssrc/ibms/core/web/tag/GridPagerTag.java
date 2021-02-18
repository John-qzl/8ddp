package com.cssrc.ibms.core.web.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class GridPagerTag  extends TagSupport{

	private static final long serialVersionUID = 1L;
	private String pagerId;
	private String entityName;

	public String getPagerId()
	{
		return this.pagerId;
	}

	public void setPagerId(String pagerId) {
		this.pagerId = pagerId;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public int doStartTag() throws JspException
	{
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		try
		{
			FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("rootPath", request.getContextPath());
			model.put("entityName", this.entityName);
			model.put("pagerId", this.pagerId);
			String html = freemarkEngine.mergeTemplateIntoString("portal/gridPager.ftl", model);
			out.println(html);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

}
