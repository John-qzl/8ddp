package com.cssrc.ibms.core.user.service;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.AuthRoleDao;
import com.cssrc.ibms.core.user.model.AuthRole;

/**
 * 授权角色表(role---auth关联)
 * 
 * <p>Title:AuthRoleService</p>
 * @author Yangbo 
 * @date 2016-7-30下午02:53:40
 */
@Service
public class AuthRoleService extends BaseService<AuthRole> {

	@Resource
	private AuthRoleDao dao;

	protected IEntityDao<AuthRole, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 删除该授权角色关系
	 * @param authId
	 */
	public void delByAuthId(Long authId) {
		this.dao.delByAuthId(authId);
	}
	/**
	 * 删除该角色的授权关系
	 * @param roleId
	 */
	public void delByRoleId(Long roleId) {
		this.dao.delByRoleId(roleId);
	}

}
