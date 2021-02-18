package com.cssrc.ibms.core.user.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.AuthRole;
/**
 * 
 * 角色授权
 * <p>Title:AuthRoleDao</p>
 * @author Yangbo 
 * @date 2016-7-30下午02:45:32
 */
@Repository
public class AuthRoleDao extends BaseDao<AuthRole> {
	public Class<?> getEntityClass() {
		return AuthRole.class;
	}
	/**
	 * 删除授权id相关角色关系
	 * @param authId
	 */
	public void delByAuthId(Long authId) {
		delBySqlKey("delByAuthId", authId);
	}
	/**
	 * roleId有关信息
	 * @param roleId
	 */
	public void delByRoleId(Long roleId) {
		delBySqlKey("delByRoleId", roleId);
	}
}
