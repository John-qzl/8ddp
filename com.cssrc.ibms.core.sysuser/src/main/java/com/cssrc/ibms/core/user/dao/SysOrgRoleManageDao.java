package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysOrgRole;
import com.cssrc.ibms.core.user.model.SysOrgRoleManage;
/**
 * 
 * <p>Title:SysOrgRoleManageDao</p>
 * @author Yangbo 
 * @date 2016-8-5下午03:13:14
 */
@Repository
public class SysOrgRoleManageDao extends BaseDao<SysOrgRoleManage> {
	public Class getEntityClass() {
		return SysOrgRoleManage.class;
	}
	/**
	 * 该组织可分配的角色
	 * @param orgId
	 * @return
	 */
	public List<SysOrgRole> getAssignRoleByOrgId(Long orgId) {
		String statment = getIbatisMapperNamespace() + ".getAssignRoleByOrgId";
		return getSqlSessionTemplate().selectList(statment, orgId);
	}
	/**
	 * 是否分配了该角色
	 * @param orgId
	 * @param roleId
	 * @return
	 */
	public boolean isOrgRoleExists(Long orgId, Long roleId) {
		Map map = new HashMap();
		map.put("orgId", orgId);
		map.put("roleId", roleId);
		Integer rtn = (Integer) getOne("isOrgRoleExists", map);
		return rtn.intValue() > 0;
	}

	public void delByOrgId(Long orgId) {
		getBySqlKey("delByOrgId", orgId);
	}
}
