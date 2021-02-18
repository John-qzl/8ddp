package com.cssrc.ibms.core.web.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class FormToolbarTag extends BodyTagSupport{

	private static Map<String, String> allButtonMap = new HashMap<String, String>();
	private static final long serialVersionUID = 1L;
	private String pkId;
	private String toolbarId;
	private String hideRecordNav = "false";

	private String hideRemove = "false";

	private String excludeButtons = "";

	static
	{
		allButtonMap.put(ToolbarButtonType.save.name(), "true");
		allButtonMap.put(ToolbarButtonType.remove.name(), "true");
		allButtonMap.put(ToolbarButtonType.prevRecord.name(), "true");
		allButtonMap.put(ToolbarButtonType.nextRecord.name(), "true");
	}

	public String getExcludeButtons()
	{
		return this.excludeButtons;
	}

	public void setExcludeButtons(String excludeButtons) {
		this.excludeButtons = excludeButtons;
	}

	public String getToolbarId() {
		return this.toolbarId;
	}

	public void setToolbarId(String toolbarId) {
		this.toolbarId = toolbarId;
	}

	public void setBodyContent(BodyContent b)
	{
		this.bodyContent = b;
	}

	public String getHideRemove() {
		return this.hideRemove;
	}
	public void setHideRemove(String hideRemove) {
		this.hideRemove = hideRemove;
	}

	public int doStartTag() throws JspException {
		return 2;
	}

	public int doAfterBody() throws JspException
	{
		return 0;
	}

	public int doEndTag()
			throws JspException
	{
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		try {
			FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("rootPath", request.getContextPath());
			if (StringUtils.isNotEmpty(this.pkId)) {
				model.put("pkId", this.pkId);
			}
			model.putAll(allButtonMap);

			if (StringUtils.isNotEmpty(this.excludeButtons)) {
				String[] excludes = this.excludeButtons.split("[,]");
				for (String ex : excludes) {
					model.put(ex, "false");
				}
			}

			model.put("hideRecordNav", this.hideRecordNav);
			if ("true".equals(this.hideRemove)) {
				model.put(ToolbarButtonType.remove.name(), "false");
			}

			model.put("toolbarId", this.toolbarId);
			BodyContent content = getBodyContent();

			if (content != null) {
				String bodyContent = content.getString();
				if (StringUtils.isNotEmpty(bodyContent)) {
					Document doc = Jsoup.parse(bodyContent);

					Elements selfToobar = doc.select(".self-toolbar");
					model.put("extToolbars", selfToobar.html());
				}
			}

			String html = freemarkEngine.mergeTemplateIntoString("portal/formToolbar.ftl", model);
			out.println(html);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 6;
	}

	public String getPkId() {
		return this.pkId;
	}

	public void setPkId(String pkId) {
		this.pkId = pkId;
	}

	public String getHideRecordNav() {
		return this.hideRecordNav;
	}

	public void setHideRecordNav(String hideRecordNav) {
		this.hideRecordNav = hideRecordNav;
	}

}
