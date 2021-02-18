package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysRole;

@Repository
public class SysRoleDao extends BaseDao<SysRole> {
	public Class getEntityClass() {
		return SysRole.class;
	}
	/**
	 * 根据角色别名查询
	 * @param roleAlias
	 * @return
	 */
	public SysRole getByRoleAlias(String roleAlias) {
		return (SysRole) getUnique("getByRoleAlias", roleAlias);
	}

	/**
	 * 判断角色别名是否存在
	 * @param alias
	 * @return
	 */
	public boolean isExistRoleAlias(String alias) {
		Integer count = (Integer) getOne("isExistRoleAlias", alias);
		return count.intValue() > 0;
	}
	/**
	 * 该角色别名是否更新过（多个相同别名角色）
	 * @param alias
	 * @param roleId
	 * @return
	 */
	public boolean isExistRoleAliasForUpd(String alias, Long roleId) {
		Map map = new HashMap();
		map.put("alias", alias);
		map.put("roleId", roleId);
		Integer count = (Integer) getOne("isExistRoleAliasForUpd", map);
		return count.intValue() > 0;
	}
	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public List<SysRole> getRole(QueryFilter queryFilter) {
		return getBySqlKey("getRole", queryFilter);
	}
	/**
	 * 获取用户担任的角色
	 * @param userId
	 * @return
	 */
	public List<SysRole> getByUserId(Long userId) {
		return getBySqlKey("getByUserId", userId);
	}
	/**
	 * 获得组织角色
	 * @param orgId
	 * @return
	 */
	public List<SysRole> getByOrgId(Long orgId) {
		return getBySqlKey("getByOrgId", orgId);
	}	
	
	/**
	 * 上级组织给下级组织分配的可授权角色
	 * @param orgId
	 * @return
	 */
	public List<SysRole> getManageRolesByOrgId(Long orgId) {
		List list = getBySqlKey("getManageRolesByOrgId", orgId);

		return list;
	}

	/**
	 * 根据角色名查询角色列表
	 * @param roleName
	 * @return
	 */
	public List<SysRole> getByRoleName(String roleName) {
		return getBySqlKey("getByRoleName", roleName);
	}

	/**
	 * 查找授权的角色
	 * @param authId
	 * @return
	 */
	public List<SysRole> getByAuthId(Long authId) {
		return getBySqlKey("getByAuthId", authId);
	}
	/**
	 * 组织分级管理授权的角色
	 * @param params
	 * @return
	 */
	public List<SysRole> getUserAssignRole(Map params) {
		return getBySqlKey("getUserAssignRole", params);
	}

}
