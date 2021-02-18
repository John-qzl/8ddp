package com.cssrc.ibms.core.user.service.curuser;

import java.util.ArrayList;
import java.util.List;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
/**
 * 
 * @author Yangbo 2016-7-23
 *
 */
public class CurUserService implements ICurUserService {
	public List<Long> getByCurUser(CurrentUser currentUser) {
		List<Long> list = new ArrayList<Long>();
		list.add(currentUser.getUserId());
		return list;
	}

	public String getKey() {
		return "user";
	}

	public String getTitle() {
		return "用户授权";
	}
	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
}
