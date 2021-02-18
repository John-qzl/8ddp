package com.cssrc.ibms.api.system.model;

import java.util.Map;

public interface BaseInsPortalParams {

	String getUserId();
	void setUserId(String userId);
	
	String getOrgId();
	void setOrgId(String orgId);
	
	Integer getPageSize();
	void setPageSize(Integer pageSize);
	
	Map<String, Object> getParams();
	void setParams(Map<String, Object> params);
	
	Integer getPage();
	void setPage(Integer page);
}
