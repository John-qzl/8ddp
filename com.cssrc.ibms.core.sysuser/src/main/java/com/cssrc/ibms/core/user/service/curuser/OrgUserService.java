package com.cssrc.ibms.core.user.service.curuser;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
/**
 * 
 * <p>Title:OrgUserService</p>
 * @author Yangbo 
 * @date 2016-8-3上午11:04:21
 */
public class OrgUserService implements ICurUserService {

	@Resource
	private SysOrgService sysOrgService;

	public List<Long> getByCurUser(CurrentUser currentUser) {
		List<SysOrg> orgs = this.sysOrgService.getOrgsByUserId(currentUser.getUserId());
		List<Long> list = new ArrayList<Long>();
		for (SysOrg org : orgs) {
			list.add(org.getOrgId());
		}
		return list;
	}
	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
	public String getKey() {
		return "org";
	}

	public String getTitle() {
		return "组织授权（本层级）";
	}
}
