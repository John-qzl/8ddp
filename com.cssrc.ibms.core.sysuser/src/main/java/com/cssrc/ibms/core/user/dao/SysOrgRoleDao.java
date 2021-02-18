package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysOrgRole;
/**
 * 组织角色授权（与角色表关联）
 * <p>Title:SysOrgRoleDao</p>
 * @author Yangbo 
 * @date 2016-8-5下午02:56:31
 */
@Repository
public class SysOrgRoleDao extends BaseDao<SysOrgRole> {
	public Class getEntityClass() {
		return SysOrgRole.class;
	}
	/**
	 * 获得某组织授权某角色的个数
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public boolean getCountByOrgidRoleid(Long orgId, Long roleId) {
		Map map = new HashMap();
		map.put("orgId", orgId);
		map.put("roleId", roleId);
		List roles = getBySqlKey("getCountByOrgidRoleid", map);
		return roles.size() > 0;
	}
	/**
	 * 获得orgid相关角色和组织信息
	 * @param orgId
	 * @return
	 */
	public List<SysOrgRole> getRolesByOrgId(Long orgId) {
		return getBySqlKey("getRolesByOrgId", orgId);
	}
	/**
	 * 删除roleId的相关数据
	 * @param roleId
	 */
	public void delByRoleId(Long roleId) {
		delBySqlKey("delByRoleId", roleId);
	}
	/**
	 * 删除该组织所有角色授权信息
	 * @param orgId
	 */
	public void delByOrgId(Long orgId) {
		delBySqlKey("delByOrgId", orgId);
	}
	/**
	 * 通过path级联删除组织角色授权信息
	 * @param path
	 */
	public void delByOrgPath(String path) {
		delBySqlKey("delByOrgPath", path);
	}
	/**
	 * 删除同时满足orgid和roleid的关联授权信息
	 * @param orgId
	 * @param roleId
	 */
	public void delByOrgIdAndRoleId(Long orgId, Long roleId) {
		Map param = new HashMap();
		param.put("orgId", orgId);
		param.put("roleId", roleId);
		delBySqlKey("delByOrgIdAndRoleId", param);
	}
}
