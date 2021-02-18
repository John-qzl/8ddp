package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.UserRole;
/**
 * 用户角色映射DAO
 * <p>Title:UserRoleDao</p>
 * @author Yangbo 
 * @date 2016-8-6下午02:42:01
 */
@Repository
public class UserRoleDao extends BaseDao<UserRole> {
	public Class getEntityClass() {
		return UserRole.class;
	}

	public UserRole getUserRoleModel(Long userId, Long roleId) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("roleId", roleId);
		UserRole userRole = (UserRole) getUnique("getUserRoleModel", param);
		return userRole;
	}

	public int delUserRoleByIds(Long userId, Long roleId) {
		Map params = new HashMap();
		params.put("userId", userId);
		params.put("roleId", roleId);

		int affectCount = delBySqlKey("delUserRoleByIds", params);
		return affectCount;
	}

	public List<Long> getUserIdsByRoleId(Long roleId) {
		List list = getBySqlKey("getUserIdsByRoleId", roleId);
		return list;
	}

	public List<UserRole> getUserRoleByRoleId(Long roleId) {
		return getBySqlKey("getUserRoleByRoleId", roleId);
	}

	public void delByRoleId(Long roleId) {
		delBySqlKey("delByRoleId", roleId);
	}

	public void delByUserId(Long userId) {
		delBySqlKey("delByUserId", userId);
	}

	public List<UserRole> getByUserId(Long userId) {
		return getBySqlKey("getByUserId", userId);
	}

	public List<UserRole> getUserByRoleIds(String roleIds) {
		Map params = new HashMap();
		params.put("roleIds", roleIds);
		return getBySqlKey("getUserByRoleIds", params);
	}
}
