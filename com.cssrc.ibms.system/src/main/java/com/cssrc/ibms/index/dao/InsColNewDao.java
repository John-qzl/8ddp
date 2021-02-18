package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.InsColNew;
import com.cssrc.ibms.index.model.InsPortCol;
/**
 * 新闻栏目关联信息DAO层
 * @author YangBo
 *
 */
@Repository
public class InsColNewDao extends BaseDao<InsColNew> {

	public Class<InsColNew> getEntityClass() {
		return InsColNew.class;
	}
	
	/**
	 * 删除
	 * @param colId
	 */
	public void delByColId(Long colId)
	{	
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("colId", colId);
		params.put("orgId", orgId);
		delBySqlKey("delByColId", params);
	}
	
	/**
	 * 删除指定新闻公告信息
	 * @param newId
	 */
	public void delByNewId(Long newId)
	{	
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("newId", newId);
		params.put("orgId", orgId);
		delBySqlKey("delByNewId", params);
	}
	
	/**
	 * 根据栏目id和新闻id确定一条记录
	 * @param colId
	 * @param newId
	 * @return
	 */
	public InsColNew getByColIdNewId(Long colId, Long newId)
	{	

		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("newId", newId);
		params.put("colId", colId);
		params.put("orgId", orgId);
		return (InsColNew)getUnique("getByColIdNewId", params);
	}
	
	/**
	 * 判断是否确定一条新闻栏目信息
	 * @param colId
	 * @param newId
	 * @return
	 */
	public Boolean booleanByColIdNewId(Long colId, Long newId) 
	{
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("newId", newId);
		params.put("colId", colId);
		params.put("orgId", orgId);
		List<InsColNew> list = getBySqlKey("getByColIdNewId", params);
		return list.size()>0;
	}
	
	/**
	 * 删除该新闻栏目信息
	 * @param colId
	 * @param newId
	 */
	public void delByColIdNewId(Long colId, Long newId)
	{	
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("newId", newId);
		params.put("colId", colId);
		delBySqlKey("delByColIdNewId", params);
	}
}
