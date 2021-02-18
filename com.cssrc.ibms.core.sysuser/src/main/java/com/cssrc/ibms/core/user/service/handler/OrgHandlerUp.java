package com.cssrc.ibms.core.user.service.handler;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.intf.IOrgHandler;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;
/**
 * 
 * <p>Title:OrgHandlerUp</p>
 * @author Yangbo 
 * @date 2016-8-8下午01:50:51
 */
public class OrgHandlerUp implements IOrgHandler {

	@Resource
	SysOrgService sysOrgService;

	public SysOrg getByType(String type) {
		SysOrg sysOrg = (SysOrg) UserContextUtil.getCurrentOrg();
		if (sysOrg.getOrgSupId().longValue() != 1L) {
			sysOrg = (SysOrg) this.sysOrgService.getById(sysOrg.getOrgSupId());
		}
		return sysOrg;
	}
}
