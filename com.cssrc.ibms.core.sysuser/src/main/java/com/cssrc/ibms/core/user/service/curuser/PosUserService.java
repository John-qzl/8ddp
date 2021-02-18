package com.cssrc.ibms.core.user.service.curuser;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.model.ICurrentUser;
import com.cssrc.ibms.core.login.model.CurrentUser;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.service.PositionService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

public class PosUserService implements ICurUserService {

	@Resource
	PositionService positionService;

	public List<Long> getByCurUser(CurrentUser currentUser) {
		List<Position> positions = this.positionService.getByUserId(currentUser.getUserId());
		List<Long> list = new ArrayList<Long>();
		for (Position pos : positions) {
			list.add(pos.getPosId());
		}
		return list;
	}

	public String getKey() {
		return "pos";
	}
	@Override
	public List<Long> getByCurUser(ICurrentUser currentUser) {
		return getByCurUser((CurrentUser)currentUser);
	}
	public String getTitle() {
		return "岗位授权";
	}
}
