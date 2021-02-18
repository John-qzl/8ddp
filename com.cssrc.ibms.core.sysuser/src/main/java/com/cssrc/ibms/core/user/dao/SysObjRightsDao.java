package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysObjRights;

/**
 * 对象权限DAO层
 * 
 * @author Yangbo 2016-7-22
 *
 */
@Repository
public class SysObjRightsDao extends BaseDao<SysObjRights> {
	public Class<?> getEntityClass() {
		return SysObjRights.class;
	}
	
	/**
	 * 删除权限关系
	 * @param objType
	 * @param objectId
	 */
	public void deleteByObjTypeAndObjectId(String objType, String objectId)
	{	
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("objType", objType);
		params.put("objectId", objectId);
		delBySqlKey("deleteByObjTypeAndObjectId", params);
	}
	
	/**
	 * 获取某类权限记录
	 * @param objType
	 * @param objectId
	 * @return
	 */
	public List<SysObjRights> getObject(String objType, String objectId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("objType", objType);
		params.put("objectId", objectId);
		return getBySqlKey("getObject", params);
	}
	
}
