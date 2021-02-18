package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.InsColType;

/**
 * 栏目布局模板Dao层
 * @author YangBo
 *
 */
@Repository
public class InsColTypeDao extends BaseDao<InsColType> {

	public Class<InsColType> getEntityClass() {
		return InsColType.class;
	}
	
	/**
	 * 根据key获取唯一模板
	 * @param key
	 * @param orgId
	 * @return
	 */
	public InsColType getByKey(String key, String orgId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("key", key);
		params.put("orgId", orgId);
		return (InsColType) getUnique("getByKey", params);

	}
}
