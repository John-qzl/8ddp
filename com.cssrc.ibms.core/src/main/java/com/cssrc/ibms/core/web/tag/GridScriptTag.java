package com.cssrc.ibms.core.web.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class GridScriptTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	private String baseUrl;
	private String entityName;
	private String entityTitle;
	private Integer winWidth = Integer.valueOf(750);

	private Integer winHeight = Integer.valueOf(500);
	private String gridId;
	private String orgId = "";

	public String getorgId()
	{
		return this.orgId;
	}

	public void setorgId(String orgId) {
		this.orgId = orgId;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getGridId()
	{
		return this.gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getEntityTitle() {
		return this.entityTitle;
	}

	public void setEntityTitle(String entityTitle) {
		this.entityTitle = entityTitle;
	}

	public Integer getWinWidth() {
		return this.winWidth;
	}

	public void setWinWidth(Integer winWidth) {
		this.winWidth = winWidth;
	}

	public Integer getWinHeight() {
		return this.winHeight;
	}

	public void setWinHeight(Integer winHeight) {
		this.winHeight = winHeight;
	}

	public int doStartTag() throws JspException
	{
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		try {
			FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("rootPath", request.getContextPath());
			model.put("entityName", this.entityName);
			model.put("entityTitle", this.entityTitle);
			model.put("baseUrl", this.baseUrl);
			model.put("gridId", this.gridId);
			model.put("winHeight", this.winHeight);
			model.put("winWidth", this.winWidth);
			model.put("orgId", this.orgId);
			String html = freemarkEngine.mergeTemplateIntoString("portal/gridScript.ftl", model);
			out.println(html);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

}
