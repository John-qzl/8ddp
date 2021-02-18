package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.cssrc.ibms.api.system.model.BaseInsPortalParams;

/**
 * 首页布局常用参数类
 * @author YangBo
 *
 */
public class InsPortalParams implements BaseInsPortalParams,Serializable{
	private String userId;
	private String orgId;
	private Integer pageSize = Integer.valueOf(5);
	private Integer page = Integer.valueOf(1);

	private Map<String, Object> params = new HashMap<String, Object>();

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getParams() {
		return this.params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}


}
