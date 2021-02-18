package com.cssrc.ibms.core.web.tag.ui;

import org.apache.commons.lang.StringUtils;

public class MiniComboxTag extends AbstractMiniDataControl{

	private static final long serialVersionUID = 1L;
	private boolean allowInput = false;

	private boolean showNullItem = false;

	private String nullItemText = "请选择...";

	public String getControlType()
	{
		return "mini-combobox";
	}

	public String getExtAttrs()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\" allowInput=\"");
		sb.append(this.allowInput);
		sb.append("\" showNullItem=\"");
		sb.append(this.showNullItem);
		sb.append("\"");
		if (StringUtils.isNotBlank(this.nullItemText)) {
			sb.append(" nullItemText=\"").append(this.nullItemText).append("\" ");
		}
		return sb.toString();
	}

	public boolean isAllowInput() {
		return this.allowInput;
	}

	public void setAllowInput(boolean allowInput) {
		this.allowInput = allowInput;
	}

	public boolean isShowNullItem() {
		return this.showNullItem;
	}

	public void setShowNullItem(boolean showNullItem) {
		this.showNullItem = showNullItem;
	}

	public String getNullItemText() {
		return this.nullItemText;
	}

	public void setNullItemText(String nullItemText) {
		this.nullItemText = nullItemText;
	}

}
