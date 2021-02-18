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

public class DetailToolbarTag extends BodyTagSupport{

	private static Map<String, String> allButtonMap = new HashMap<String, String>();
	private static final long serialVersionUID = 1L;
	private String toolbarId;
	private String hideRecordNav = "false";

	private String excludeButtons = "";

	static
	{
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

	public void setToolbarId(String toolbarId)
	{
		this.toolbarId = toolbarId;
	}

	public String getHideRecordNav() {
		return this.hideRecordNav;
	}

	public void setHideRecordNav(String hideRecordNav)
	{
		this.hideRecordNav = hideRecordNav;
	}

	public void setBodyContent(BodyContent b)
	{
		this.bodyContent = b;
	}

	public int doStartTag() throws JspException
	{
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
			model.put("toolbarId", this.toolbarId);
			model.put("hideRecordNav", this.hideRecordNav);
			model.put("toolbarId", this.toolbarId);
			model.putAll(allButtonMap);
			if (StringUtils.isNotEmpty(this.excludeButtons)) {
				String[] excludes = this.excludeButtons.split("[,]");
				for (String ex : excludes) {
					model.put(ex, "false");
				}
			}
			BodyContent content = getBodyContent();

			if (content != null) {
				String bodyContent = content.getString();
				if (StringUtils.isNotEmpty(bodyContent)) {
					Document doc = Jsoup.parse(bodyContent);

					Elements selfToobar = doc.select(".self-toolbar");
					model.put("extToolbars", selfToobar.html());
				}
			}

			String html = freemarkEngine.mergeTemplateIntoString("portal/detailToolbar.ftl", model);
			out.println(html);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 6;
	}

}
