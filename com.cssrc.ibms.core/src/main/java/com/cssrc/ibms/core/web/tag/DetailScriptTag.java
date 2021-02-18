package com.cssrc.ibms.core.web.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class DetailScriptTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	private String formId;
	private String baseUrl;
	private String entityName;

	public String getEntityName()
	{
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFormId() {
		return this.formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
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
			model.put("baseUrl", this.baseUrl);
			model.put("formId", this.formId);
			if (StringUtils.isNotEmpty(this.entityName))
				model.put("entityName", this.entityName);
			else {
				model.put("entityName", "main");
			}
			String html = freemarkEngine.mergeTemplateIntoString("portal/detailScript.ftl", model);
			out.println(html);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

}
