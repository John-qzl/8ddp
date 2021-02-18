package com.cssrc.ibms.layout.model;

import com.cssrc.ibms.api.system.model.ISysLayout;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class SysLayout extends BaseModel implements ISysLayout{

	private static final long serialVersionUID = 1L;

	// 主键
	private Long id;
	// 布局名称
	private String layoutName;
	// 布局应用：用户ID，组织ID
	private Long appId;
	// 布局应用：用户ID，组织ID, 全局（默认0）
	private Long appType = Long.valueOf(0);
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLayoutName() {
		return layoutName;
	}
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}
	
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Long getAppType() {
		return appType;
	}
	public void setAppType(Long appType) {
		this.appType = appType;
	}
	
}
