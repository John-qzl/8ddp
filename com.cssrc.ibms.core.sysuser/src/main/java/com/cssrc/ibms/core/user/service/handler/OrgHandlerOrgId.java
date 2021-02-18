package com.cssrc.ibms.core.user.service.handler;

import javax.annotation.Resource;

import com.cssrc.ibms.core.user.intf.IOrgHandler;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;
/**
 * 
 * @date 2016-12-15
 */
public class OrgHandlerOrgId implements IOrgHandler {

	@Resource
	SysOrgService sysOrgService;

	public SysOrg getByType(String organizationId) {
		SysOrg sysOrg = new SysOrg();
		Long orgId = Long.parseLong(organizationId);
		if (orgId.longValue() != 1L) {
			sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
		}
		return sysOrg;
	}
}
