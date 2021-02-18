package com.cssrc.ibms.core.web.tag.ui;

public class RadioStatusTag extends AbstractMiniDataControl{

	private static final long serialVersionUID = 1L;
	public final String statusData = "[{id:'ENABLED',text:'启用'},{id:'DISABLED',text:'禁用'}]";

	public String getControlType()
	{
		return "mini-radiobuttonlist";
	}

	public String getExtAttrs()
	{
		return null;
	}

	public String getData()
	{
		return "[{id:'ENABLED',text:'启用'},{id:'DISABLED',text:'禁用'}]";
	}

}
