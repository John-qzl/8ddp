package com.cssrc.ibms.core.user.service.curuser;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.user.service.UserPositionService;
/**
 * 
 * <p>Title:ChargeUserService</p>
 * @author Yangbo 
 * @date 2016-8-3下午02:24:37
 */
public class ChargeUserService implements ICurUserService {

	@Resource
	private UserPositionService userPositionService;

	public List<Long> getByCurUser(CurrentUser currentUser) {
		List<Long> orgIds = new ArrayList<Long>();
		List<UserPosition> userOrgs = this.userPositionService.getChargeOrgByUserId(currentUser.getUserId());
		for (UserPosition userOrg : userOrgs) {
			orgIds.add(userOrg.getOrgId());
		}
		return orgIds;
	}

	public String getKey() {
		return "orgMgr";
	}

	public String getTitle() {
		return "组织负责人";
	}

	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
}
