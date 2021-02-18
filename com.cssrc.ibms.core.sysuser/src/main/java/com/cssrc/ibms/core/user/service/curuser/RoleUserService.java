package com.cssrc.ibms.core.user.service.curuser;
import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.user.service.UserRoleService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
/**
 * 
 * <p>Title:RoleUserService</p>
 * @author Yangbo 
 * @date 2016-8-3上午11:06:19
 */
public class RoleUserService implements ICurUserService {

	@Resource
	UserRoleService userRoleService;

	public List<Long> getByCurUser(CurrentUser currentUser) {
		List<UserRole> list = this.userRoleService.getByUserId(currentUser.getUserId());
		List<Long> roleList = new ArrayList<Long>();
		for (UserRole userRole : list) {
			roleList.add(userRole.getRoleId());
		}
		return roleList;
	}

	public String getKey() {
		return "role";
	}
	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
	public String getTitle() {
		return "角色授权";
	}
}
