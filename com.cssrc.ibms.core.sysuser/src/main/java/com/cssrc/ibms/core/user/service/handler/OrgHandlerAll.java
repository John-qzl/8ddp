package com.cssrc.ibms.core.user.service.handler;

import com.cssrc.ibms.core.user.intf.IOrgHandler;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.OrgHelper;

/**
 * 
 * <p>Title:OrgHandlerAll</p>
 * @author Yangbo 
 * @date 2016-8-8下午01:48:35
 */
public class OrgHandlerAll implements IOrgHandler {
	public SysOrg getByType(String type) {
		return OrgHelper.getTopOrg();
	}
}
