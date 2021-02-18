package com.cssrc.ibms.record.dao;
import com.cssrc.ibms.api.rec.model.IRecRoleSonFun;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.record.model.RecRoleSonFun;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * <p>RecRoleSonFunDao.java</p>
 * @author dengwenjie 
 * @date 2017年4月7日
 */
@Repository
public class RecRoleSonFunDao  extends BaseDao<RecRoleSonFun> {
	public Class getEntityClass() {
		return RecRoleSonFun.class;
	}
	/**
	 * 根据角色id获取RecRoleSonFun集合
	 * @param roleSonId
	 * @return
	 */
	public List<RecRoleSonFun> getByRoleSonId(long roleSonId) {
		return getBySqlKey("getByRoleSonId", Long.valueOf(roleSonId));
	}

	/**
	 * 根据功能点id获取RecRoleSonFun集合
	 * @param funId
	 * @return
	 */
	public List<RecRoleSonFun> getByfunId(long funId) {
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
	public void delByRoleSonId(Long roleSonId) {
		delBySqlKey("delByRoleSonId", roleSonId);
	}
	
	public void delByRoleAndFun(Long[] roleSonIds, Long[] funIds) {
		Map params = new HashMap();
		params.put("roleSonIds", roleSonIds);
		params.put("funIds", funIds);
		delBySqlKey("delByRoleAndFun", params);
	}
	public RecRoleSonFun getByFunAndRole(Long roleSonId, Long funId) {
		Map params = new HashMap();
		params.put("roleSonId", roleSonId);
		params.put("funId", funId);
		return (RecRoleSonFun)getOne("getByFunAndRole", params);
	}
	public List<RecRoleSonFun> getByRoleAliasFun(String roleAlias, Long funId,Long dataId){
		Map params = new HashMap();
		params.put("roleAlias", roleAlias);
		params.put("funId", funId);
		params.put("dataId", dataId);
		return getBySqlKey("getByRoleAliasFun", params);
	}
}
