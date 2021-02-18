package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.InsPortal;
@Repository
/**
 * 首页布局Dao层
 * @author YangBo
 *
 */
public class InsPortalDao extends BaseDao<InsPortal>{

	public Class<InsPortal> getEntityClass() {
		return InsPortal.class;
	}
	
	/**
	 * 根据key获取唯一布局
	 * @param key
	 * @param orgId
	 * @return
	 */
	public InsPortal getByKey(String key, String orgId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key", key);
		params.put("orgId", orgId);
		return (InsPortal) getUnique("getByKey", params);

	}
	
	/**
	 * 根据用户和key确定一个布局
	 * @param key
	 * @param orgId
	 * @param userId
	 * @return
	 */
	public InsPortal getByIdKey(String key, String orgId, String userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key", key);
		params.put("orgId", orgId);
		params.put("userId", userId);
		return (InsPortal) getUnique("getByIdKey", params);

	}
	
	/**
	 * 根据权限查找布局
	 * @param objType insPortal
	 * @param ownerId 角色，岗位，用户等id
	 * @return
	 */
	public InsPortal getPortalByRights(String objType, Long ownerId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("objType", objType);
		params.put("ownerId", ownerId);
		return (InsPortal) getUnique("getPortal", params);
	}

}
