package com.cssrc.ibms.system.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.system.model.GlobalType;
/**
 * 系统分类
 * <p>Title:GlobalTypeDao</p>
 * @author Yangbo 
 * @date 2016-8-30上午09:46:15
 */
@Repository
public class GlobalTypeDao extends BaseDao<GlobalType> {

	@Override
	public Class<GlobalType> getEntityClass() {
		return GlobalType.class;
	}

	public List<GlobalType> getByNodePath(String nodePath)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodePath", nodePath + "%");
		return getBySqlKey("getByNodePath", params);
	}

	public List<GlobalType> getByParentId(long parentId)
	{
		return getBySqlKey("getByParentId", Long.valueOf(parentId));
	}

	public boolean isNodeKeyExists(String catKey, String nodeKey)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("catkey", catKey);
		params.put("nodeKey", nodeKey);
		int rtn = ((Integer)getOne("isNodeKeyExists", params)).intValue();
		return rtn > 0;
	}

	public boolean isNodeKeyExistsForUpdate(Long typeId, String catKey, String nodeKey)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("typeId", typeId);
		params.put("catkey", catKey);
		params.put("nodeKey", nodeKey);
		int rtn = ((Integer)getOne("isNodeKeyExistsForUpdate", params)).intValue();
		return rtn > 0;
	}

	public void updSn(Long typeId, Long sn)
	{
		GlobalType globalType = new GlobalType();
		globalType.setTypeId(typeId);
		globalType.setSn(sn);
		update("updSn", globalType);
	}

	public List<GlobalType> getByCatKey(String catKey)
	{
		return getBySqlKey("getByCatKey", catKey);
	}

	public GlobalType getByDictNodeKey(String nodeKey)
	{
		GlobalType globalType = (GlobalType)getUnique("getByDictNodeKey", nodeKey);
		return globalType;
	}

	/**
	 * @param catKey
	 * @param nodeKey
	 * @return
	 */
	public GlobalType getByCateKeyAndNodeKey(String catKey, String nodeKey)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("catKey", catKey);
		params.put("nodeKey", nodeKey);
		GlobalType globalType = (GlobalType)getUnique("getByCateKeyAndNodeKey", params);
		return globalType;
	}
	
	public List<GlobalType> getByCatKeyAndTypeName(String catKey, String typeName)
	{
		Map<String, String> params = new HashMap<String, String>();
		params.put("catKey", catKey);
		params.put("typeName", typeName);
		List<GlobalType> list = getBySqlKey("getByCatKeyAndTypeName", params);
		return list;
	}
	
	/**
	 * 控制用户分类
	 *@author YangBo @date 2016年10月11日下午4:05:16
	 *@param catKey
	 *@param userId
	 *@return
	 */
	public List<GlobalType> getPersonType(String catKey, Long userId)
	{
		Map<String, Object> params = new Hashtable<String, Object>();
		params.put("catkey", catKey);
		params.put("userId", userId);
		List<GlobalType> list = getBySqlKey("getPersonType", params);
		return list;
	}

	public List<GlobalType> getByFormRights(String catKey, Long userId, String roleIds, String orgIds)
	{
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ownerId", userId);
		params.put("catKey", catKey);
		if (StringUtils.isNotEmpty(roleIds)) {
			params.put("roleIds", roleIds);
		}
		if (StringUtils.isNotEmpty(orgIds)) {
			params.put("orgIds", orgIds);
		}
		return getBySqlKey("getByFormRights", params);
	}
	
	/**
	 * 级联更新父子分类是否删除状态
	 * @param path
	 */
	public void updateStatus(String path,Long currentUserId,short status) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", path);
		params.put("currentUserId", currentUserId);
		params.put("status", status);
		params.put("updateTime", new Date());
		update("updateStatus", params);
	}
}