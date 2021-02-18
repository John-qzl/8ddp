package com.cssrc.ibms.api.system.model;

public interface IDictionary {
	@Deprecated
	public static final String ScriptType = "scriptType";

	Object getItemName();
	Short getDic_delFlag();
	Long getSn();
	String getItemValue();
	String getItemKey();
	String getDescp();
}