package com.cssrc.ibms.record.dao;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.record.model.RecRoleFun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
/**
 * Description:
 * <p>RecRoleFunDao.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
@Repository
public class RecRoleFunDao  extends BaseDao<RecRoleFun> {
	public Class getEntityClass() {
		return RecRoleFun.class;
	}
	/**
	 * 根据角色id获取RecRoleFun集合
	 * @param roleId
	 * @return
	 */
	public List<RecRoleFun> getByRoleId(long roleId) {
		return getBySqlKey("getByRoleId", Long.valueOf(roleId));
	}

	/**
	 * 根据功能点id获取RecRoleFun集合
	 * @param funId
	 * @return
	 */
	public List<RecRoleFun> getByfunId(long funId) {
		return getBySqlKey("getByfunId", Long.valueOf(funId));
	}
	/**
	 * 删除该功能所有角色权限
	 * @param funId
	 */
	public void delByFunId(Long funId) {
		delBySqlKey("delByFunId", funId);
	}
	
	/**
	 * 删除该功能所有角色权限
	 * @param funId
	 */
	public void delByRoleId(Long roleId) {
		delBySqlKey("delByRoleId", roleId);
	}
	
	public void delByRoleAndFun(Long[] roleIds, Long[] funIds) {
		Map params = new HashMap();
		params.put("roleIds", roleIds);
		params.put("funIds", funIds);
		delBySqlKey("delByRoleAndFun", params);
	}
	public RecRoleFun getByFunAndRole(Long roleId, Long funId) {
		Map params = new HashMap();
		params.put("roleId", roleId);
		params.put("funId", funId);
		return (RecRoleFun)getOne("getByFunAndRole", params);
	}
	public List<RecRoleFun> getByRoleAliasFun(String roleAlias, Long funId){
		Map params = new HashMap();
		params.put("roleAlias", roleAlias);
		params.put("funId", funId);
		return getBySqlKey("getByRoleAliasFun", params);
	}
}
