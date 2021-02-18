package com.cssrc.ibms.api.custom.model;

import net.sf.json.JSONArray;


public interface IAdvancedQuery {
	public String getDisplayId();
	public String getQueryKey();
	public JSONArray getConditionJSON();
}
