package com.cssrc.ibms.core.web.tag;

import java.util.HashMap;
import java.util.List;
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

import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class GridToolbarTag extends BodyTagSupport{

	private static Map<String, String> allButtonMap = new HashMap<String, String>();

	private String excludeButtons = "";

	private String includeButtons = "";

	private String isGranted = "false";

	private String style = "border-bottom:0;margin:0;padding:0;";
	private static final long serialVersionUID = 1L;
	private String entityName;

	static
	{
		for (ToolbarButtonType type : ToolbarButtonType.values())
			allButtonMap.put(type.name(), "true");
	}

	public String getEntityName()
	{
		return this.entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
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

	public int doEndTag() throws JspException
	{
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
		try {
			FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
			IResourcesService resourcesService = (IResourcesService)AppUtil.getBean(IResourcesService.class);

			Map<String,Object> model = new HashMap<String,Object>();
			model.put("rootPath", request.getContextPath());
			model.put("entityName", this.entityName);
			model.put("style", this.style);
			String str1;
			String btn;
/*			if ("true".equals(this.isGranted)) {
				List listButtons = resourcesService.getModuleButtons(this.entityName);

				if (listButtons.size() == 0) {
					if (StringUtils.isNotEmpty(this.includeButtons)) {
						String[] ibuttons = this.includeButtons.split(",");
						String[] arrayOfString2;
						int i = (arrayOfString2 = ibuttons).length; 
						for (str1 = 0; str1 < i; str1++) { 
							btn = arrayOfString2[str1];
							model.put(btn, "true"); 
							}
					}
				}
				else
					for (SysMenu sysMenu : listButtons)
						model.put(sysMenu.getKey(), "true");
			}
			else
			{
				model.putAll(allButtonMap);
			}*/
			model.putAll(allButtonMap);
			if (StringUtils.isNotEmpty(this.excludeButtons)) {
				String[] ebuttons = this.excludeButtons.split(",");
				String[] arrayOfString1;
				int str = (arrayOfString1 = ebuttons).length; 
				for (int n = 0; n < str; n++) { 
					btn = arrayOfString1[n];
					model.put(btn, "false");
					}
			}
			
			BodyContent content = getBodyContent();
			if (content != null) {
				String bodyContent = content.getString();
				Document doc = Jsoup.parse(bodyContent);

				Elements selfToobar = doc.select(".self-toolbar");
				model.put("extToolbars", selfToobar.html());
				Elements selfSearch = doc.select(".self-search");
				String selfSearchHtml = selfSearch.html();

				if (StringUtils.isNotEmpty(selfSearchHtml)) {
					model.put("selfSearch", selfSearchHtml);

					model.put(ToolbarButtonType.fieldSearch.name(), "false");
				}
			}

			String html = freemarkEngine.mergeTemplateIntoString("portal/gridToolbar.ftl", model);
			out.println(html);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 6;
	}

	public String getExcludeButtons() {
		return this.excludeButtons;
	}

	public void setExcludeButtons(String excludeButtons) {
		this.excludeButtons = excludeButtons;
	}

	public String getIncludeButtons() {
		return this.includeButtons;
	}

	public void setIncludeButtons(String includeButtons) {
		this.includeButtons = includeButtons;
	}

	public String getIsGranted() {
		return this.isGranted;
	}

	public void setIsGranted(String isGranted) {
		this.isGranted = isGranted;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
