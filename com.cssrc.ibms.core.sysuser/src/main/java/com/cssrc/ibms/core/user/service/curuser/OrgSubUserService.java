package com.cssrc.ibms.core.user.service.curuser;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;
/**
 * 
 * <p>Title:OrgSubUserService</p>
 * @author Yangbo 
 * @date 2016-8-3上午11:04:09
 */
public class OrgSubUserService implements ICurUserService {

	@Resource
	SysOrgService sysOrgService;

	public List<Long> getByCurUser(CurrentUser currentUser) {
		Long orgId = currentUser.getOrgId();
		SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
		List<Long> list = new ArrayList<Long>();
		if(orgId <= 0L )
			return list;
		
		list.add(orgId);
		
		if (sysOrg == null)
			return list;
		do {
			sysOrg = (SysOrg) this.sysOrgService.getById(sysOrg.getOrgSupId());
			if (sysOrg == null)
				break;
			list.add(sysOrg.getOrgId());
		} while (!sysOrg.getOrgSupId().equals(SysOrg.BEGIN_ORGID));

		return list;
	}
	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
	public String getKey() {
		return "orgSub";
	}

	public String getTitle() {
		return "组织授权（包含子组织）";
	}
}
