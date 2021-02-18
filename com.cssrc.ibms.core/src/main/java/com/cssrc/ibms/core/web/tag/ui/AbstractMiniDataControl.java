package com.cssrc.ibms.core.web.tag.ui;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractMiniDataControl extends TagSupport{

	protected Log logger = LogFactory.getLog(AbstractMiniDataControl.class);
	private static final long serialVersionUID = 1L;
	protected String name;
	protected String id;
	protected String value;
	protected String text = "";

	protected boolean required = false;

	protected String style = "";

	protected String emptyText = "请选择...";
	protected String onValueChanged;
	protected String data = "";

	public abstract String getControlType();

	public abstract String getExtAttrs();

	public int doStartTag()
			throws JspException
	{
		JspWriter writer = this.pageContext.getOut();
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<input ").append(" class=\"").append(getControlType()).append("\"");
			if (StringUtils.isNotEmpty(this.id)) {
				sb.append("id=\"").append(this.id).append("\"");
			}
			if (StringUtils.isNotEmpty(this.name)) {
				sb.append(" name=\"").append(this.name).append("\"");
			}
			sb.append(" emptyText=\"").append(this.emptyText).append("\"");
			if (StringUtils.isNotEmpty(this.onValueChanged)) {
				sb.append(" onvaluechanged=\"").append(this.onValueChanged).append("\"");
			}
			sb.append(" textField=\"text\" valueField=\"id\"");
			sb.append(" data=\"").append(getData()).append("\"");
			if (StringUtils.isNotEmpty(this.value)) {
				sb.append(" value=\"").append(this.value).append("\"");
			}
			sb.append(" required=\"");
			sb.append(this.required);
			if (StringUtils.isNotEmpty(this.style)) {
				sb.append(" style=\"").append("\"");
			}

			String extAtts = getExtAttrs();
			if (StringUtils.isNotEmpty(extAtts)) {
				sb.append(" ").append(extAtts);
			}
			sb.append("\"/>");

			writer.write(sb.toString());
			this.logger.debug("control html:" + sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getEmptyText() {
		return this.emptyText;
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}

	public String getOnValueChanged() {
		return this.onValueChanged;
	}

	public void setOnValueChanged(String onValueChanged) {
		this.onValueChanged = onValueChanged;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
