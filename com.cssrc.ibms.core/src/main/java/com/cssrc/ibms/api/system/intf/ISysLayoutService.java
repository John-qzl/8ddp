package com.cssrc.ibms.api.system.intf;

import com.cssrc.ibms.api.system.model.ISysLayout;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;

public interface ISysLayoutService{

	/**
	 * Description: 保存布局信息
	 */
	public abstract void save(String layoutName, String appType, String appId);
	
	/**
	 * Description: 获取当前用户布局
	 */
	public abstract String getLayoutType(Long userId, ISysOrg curSysOrg);
	
	/**
	 * 获取当前用户布局
	 */
	public abstract ISysLayout getSysLayout(Long userId, ISysOrg curSysOrg);
}
