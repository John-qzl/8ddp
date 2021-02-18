package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.api.sysuser.model.ICurrentUser;

/**
 * 
 * @author Yangbo 2016-7-20
 *
 */
public abstract interface ICurUserService {

	public abstract String getKey();

	public abstract String getTitle();

	public abstract List<Long> getByCurUser(ICurrentUser currentUser);

}
