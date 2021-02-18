package com.cssrc.ibms.core.web.tag.ui;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * ue编辑框Tag
 * @author YangBo
 *
 */
public class UEditorTag extends BodyTagSupport{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String width;
	private Integer height;
	private String isMini = "false";

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
		String templateSource = "<script id=\"${id}\" name=\"${name}\" rich-type=\"ueditor\" type=\"text/plain\" style=\"width:${width};height:${height}px;\">${value}</script>";
		try
		{
			if (request.getAttribute("_writeUEditorJs") == null) {
				String contextPath = request.getContextPath();
				String script1 = "<script src=\"" + contextPath + "/scripts/jquery/plugins/jquery.getscripts.min.js\" type=\"text/javascript\"></script>";
				out.println(script1);
				if ("true".equals(this.isMini)) {
					String script2 = "<script type=\"text/javascript\" charset=\"utf-8\" src=\"" + contextPath + "/scripts/ueditor/use-ueditor-mini.js\"></script>";
					out.println(script2);
				} else {
					String script2 = "<script type=\"text/javascript\" charset=\"utf-8\" src=\"" + contextPath + "/scripts/ueditor/use-ueditor.js\"></script>";
					out.println(script2);
				}

				request.setAttribute("_writeUEditorJs", Boolean.valueOf(true));
			}

			FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("ctxPath", request.getContextPath());
			model.put("id", this.id);
			model.put("name", this.name);
			model.put("height", this.height);
			if (this.width == null)
				model.put("width", "100%");
			else {
				model.put("width", this.width);
			}
			BodyContent content = getBodyContent();

			if (content != null) {
				String bodyContent = content.getString();
				model.put("value", bodyContent);
			}

			String html = freemarkEngine.parseByStringTemplate(model, templateSource);
			out.println(html);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 6;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWidth() {
		return this.width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getIsMini() {
		return this.isMini;
	}

	public void setIsMini(String isMini) {
		this.isMini = isMini;
	}

}
