package com.cssrc.ibms.core.user.intf;

import com.cssrc.ibms.core.user.model.SysOrg;

/**
 * 
 * <p>Title:IOrgService</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:40:29
 */
public abstract interface IOrgService {
	public abstract SysOrg getSysOrgByScope(String paramString1,
			String paramString2);
}
