package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.api.sysuser.intf.ICurrentUserService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.login.model.CurrentUser;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 
 * @author Yangbo 2016-7-20
 *
 */
@Service
public class CurrentUserService implements ICurrentUserService{
	
	public Map<String, List<Long>> getUserRelation() {
		CurrentUser currentUser=getCurrentUser();
		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		try {
			List<ICurUserService> objectList = getUserRelationList();
			for (ICurUserService curObj : objectList) {
				List<Long> list = curObj.getByCurUser(currentUser);
				map.put(curObj.getKey(), list);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return map;
	}

	public List<String> getUserListKey() {
		List list = new ArrayList();
		try {
			List<ICurUserService> objectList = getUserRelationList();
			for (ICurUserService curObj : objectList) {
				String key = curObj.getKey();
				list.add(key);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<ICurUserService> getUserRelationList()
			throws ClassNotFoundException {
		Map instMap = AppUtil.getImplInstance(ICurUserService.class);
		Collection instCollect = instMap.values();
		List list = new ArrayList();
		for (Iterator localIterator = instCollect.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			list.add((ICurUserService) obj);
		}

		return list;
	}

	public List<ICurUserService> getUserRelationByKey(String relationKey) {
		List list = (List) AppUtil.getBean(relationKey);
		return list;
	}
	/**
	 * 当前用户pos，org，上下级等等关系,relationKey决定哪一个
	 *@author YangBo @date 2016年11月19日下午2:06:54
	 *@param currentUser
	 *@param relationKey
	 *@return
	 */
	public Map<String, List<Long>> getUserRelation(CurrentUser currentUser,
			String relationKey) {
		Map map = new HashMap();
		List<ICurUserService> objectList = getUserRelationByKey(relationKey);
		for (ICurUserService curObj : objectList) {
			List list = curObj.getByCurUser(currentUser);
			map.put(curObj.getKey(), list);
		}
		return map;
	}
	
	/**
	 * 获取当前用户
	 *@author YangBo @date 2016年11月19日下午2:06:21
	 *@return
	 */
	public CurrentUser getCurrentUser() {
		SysUser curUser = (SysUser) UserContextUtil.getCurrentUser();
		Position pos = (Position) UserContextUtil.getCurrentPos();
		CurrentUser currentUser = new CurrentUser();
		currentUser.setUserId(curUser.getUserId());
		currentUser.setUsername(curUser.getUsername());// account
		currentUser.setFullname(curUser.getFullname());// name
		if (pos != null) {
			currentUser.setOrgId(pos.getOrgId());
			currentUser.setPosId(pos.getPosId());
		}
		return currentUser;
	}
}
