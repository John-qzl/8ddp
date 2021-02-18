package com.cssrc.ibms.api.form.intf;

import java.util.Map;

import net.sf.json.JSONObject;

public interface IQueryRightParseService {

	public abstract Map<String, Object> getRightMap(Long userId, Long curOrgId);

	public abstract boolean hasRight(JSONObject permission,
			Map<String, Object> rightMap);

	public abstract String getDefaultRight(Integer rightType);

	public abstract Map<String, Boolean> getPermission(int type, String field,
			Map<String, Object> rightMap);

}