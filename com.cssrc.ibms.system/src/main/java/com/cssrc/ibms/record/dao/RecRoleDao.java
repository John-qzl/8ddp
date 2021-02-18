package com.cssrc.ibms.record.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.record.model.RecRole;

import org.springframework.stereotype.Repository;

/**
 * Description:
 * <p>RecRoleDao.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
@Repository
public class RecRoleDao  extends BaseDao<RecRole> {
	public Class getEntityClass() {
		return RecRole.class;
	}
	/**
	 * 根据表单角色类型返回角色
	 * @param recType
	 * @return
	 */
	public List<RecRole> getByTypeId(Long recType) {
		return getBySqlKey("getByTypeId", Long.valueOf(recType));
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
	public List<RecRole> getRole(QueryFilter queryFilter) {
		return getBySqlKey("getRole", queryFilter);
	}
	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public RecRole getRoleByAlias(String alias) {
		return (RecRole)getUnique("getRoleByAlias",alias);
	}
	/**
	 * 根据用户id查询role列表
	 * @param queryFilter
	 * @return
	 */
	public List<RecRole> getRolesByMeta(Long userId,String sysRoleIds,String sysOrgIds,String typeAlias){
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("sysRoleIds", sysRoleIds);
		map.put("sysOrgIds", sysOrgIds);
		map.put("typeAlias", typeAlias);
		return getBySqlKey("getRolesByMeta", map);
	}
}
