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

public class FormScriptTag  extends TagSupport{

	private static final long serialVersionUID = 1L;
	private String formId;
	private String orgId = "";
	private String entityName;
	private String baseUrl;

	public String getFormId()
	{
		return this.formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getorgId() {
		return this.orgId;
	}

	public void setorgId(String orgId) {
		this.orgId = orgId;
	}

	public int doStartTag() throws JspException
	{
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		try {
			FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("rootPath", request.getContextPath());
			if (StringUtils.isNotEmpty(this.entityName))
				model.put("entityName", this.entityName);
			else {
				model.put("entityName", "main");
			}
			model.put("baseUrl", this.baseUrl);
			model.put("formId", this.formId);
			model.put("orgId", this.orgId);
			String html = freemarkEngine.mergeTemplateIntoString("portal/formScript.ftl", model);
			out.println(html);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

}
