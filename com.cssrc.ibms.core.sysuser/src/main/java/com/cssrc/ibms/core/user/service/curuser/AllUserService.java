package com.cssrc.ibms.core.user.service.curuser;

import java.util.List;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
/**
 * 
 * <p>Title:AllUserService</p>
 * @author Yangbo 
 * @date 2016-8-3上午11:05:22
 */
public class AllUserService implements ICurUserService {
	public List<Long> getByCurUser(CurrentUser currentUser) {
		return null;
	}

	public String getKey() {
		return "all";
	}

	public String getTitle() {
		return "所有人";
	}

	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return null;
	}

}
