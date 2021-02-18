package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.ProTransTo;

/**
 * 对象功能:流程流转状态 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class ProTransToDao extends BaseDao<ProTransTo>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return ProTransTo.class;
	}
	
	public ProTransTo getByTaskId(Long taskId){
		List<ProTransTo> list=this.getBySqlKey("getByTaskId", taskId);
		if(list.size()==0) return null;
		return list.get(0);
	}
	
	public void delByActInstId(Long actInstId){
		delBySqlKey("delByActInstId", actInstId);
	}
	
	public List<ProTransTo> mattersList(QueryFilter filter) {
		return getBySqlKey("mattersList", filter);
	}  

	public void delByTaskId(Long taskId) {
		delBySqlKey("delByTaskId", taskId);
	}
	
}