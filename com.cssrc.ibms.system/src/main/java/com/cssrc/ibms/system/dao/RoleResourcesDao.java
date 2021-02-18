package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.RoleResources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 功能用户映射管理
 * <p>Title:RoleResourcesDao</p>
 * @author Yangbo 
 * @date 2016-8-23下午04:38:47
 */
@Repository
public class RoleResourcesDao extends BaseDao<RoleResources> {
	public Class getEntityClass() {
		return RoleResources.class;
	}
	/**
	 * 根据角色获得映射关系
	 * @param roleId
	 * @return
	 */
	public List<RoleResources> getByRole(Long roleId) {
		Map params = new HashMap();
		params.put("roleId", roleId);
		return getBySqlKey("getByRoleId", params);
	}
	/**
	 * 删除某角色的功能配置
	 * @param roleId
	 */
	public void delByRole(Long roleId) {
		Map params = new HashMap();
		params.put("roleId", roleId);
		delBySqlKey("delByRole", params);
	}
	/**
	 * 删除该功能所有角色权限
	 * @param resId
	 */
	public void delByResId(Long resId) {
		delBySqlKey("delByResId", resId);
	}

	public List<RoleResources> getByResId(Long resId) {
		return getBySqlKey("getByResId", resId);
	}

	public void delByRoleAndRes(Long[] roleIds, Long[] resIds) {
		Map params = new HashMap();
		params.put("roleIds", roleIds);
		params.put("resIds", resIds);
		delBySqlKey("delByRoleAndRes", params);
	}
}
