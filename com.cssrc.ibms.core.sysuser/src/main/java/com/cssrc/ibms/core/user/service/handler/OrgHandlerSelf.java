package com.cssrc.ibms.core.user.service.handler;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.intf.IOrgHandler;
import com.cssrc.ibms.core.user.model.SysOrg;

/**
 * 
 * <p>Title:OrgHandlerSelf</p>
 * @author Yangbo 
 * @date 2016-8-8下午01:49:34
 */
public class OrgHandlerSelf implements IOrgHandler {
	public SysOrg getByType(String type) {
		SysOrg sysOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		return sysOrg;
	}
}
