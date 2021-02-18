package com.cssrc.ibms.index.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.InsNewsCm;

/**
 * 新闻评论内容DAO层
 * @author YangBo
 *
 */
@Repository
public class InsNewsCmDao extends BaseDao<InsNewsCm>{

	public Class<InsNewsCm> getEntityClass() {
		return InsNewsCm.class;
	}
	
	/**
	 * 根据新闻id获取新闻评论信息
	 * @param newId
	 * @return
	 */
	public List<InsNewsCm> getByNewId(Long newId){
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("newId", newId);
		params.put("orgId", orgId);
		List<InsNewsCm> list = getBySqlKey("getByNewId", params);
		return list;
	}
	
	/**
	 * 根据回复评论Id获取内容
	 * @param repId
	 * @return
	 */
	 public List<InsNewsCm> getByReplyId(Long repId){
			String orgId = UserContextUtil.getCurrentOrgId().toString();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("repId", repId);
			params.put("orgId", orgId);
			List<InsNewsCm> list = getBySqlKey("getByReplyId", params);
			return list;
	 }
}
