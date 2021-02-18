package com.cssrc.ibms.core.web.tag.ui;

public class RadioBooleanTag extends AbstractMiniDataControl{

	private static final long serialVersionUID = 1L;
	public final String booleanData = "[{id:'YES',text:'是'},{id:'NO',text:'否'}]";

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
		return "[{id:'YES',text:'是'},{id:'NO',text:'否'}]";
	}

}
