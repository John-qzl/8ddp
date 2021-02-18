package com.cssrc.ibms.api.form.intf;

import java.util.Map;

import com.cssrc.ibms.api.form.model.IQuerySetting;

import net.sf.json.JSONObject;

public interface IQueryFilterFieldParseService {

	public abstract String getRightFilterField(IQuerySetting sysQuerySetting,
			Map<String, Object> rightMap, String baseURL);

	public abstract String getFilterKey(IQuerySetting sysQuerySetting,
			Map<String, Object> params);

	public abstract JSONObject getFilterFieldJson(IQuerySetting sysQuerySetting,
			Map<String, Object> rightMap, Map<String, Object> params);

	public abstract String getFilterSQL(JSONObject filterJson);

}