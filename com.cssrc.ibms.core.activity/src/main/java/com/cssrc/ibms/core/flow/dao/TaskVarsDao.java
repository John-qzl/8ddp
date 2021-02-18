package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.TaskVars;
@Repository
public class TaskVarsDao extends BaseDao<TaskVars>
{
	@Override
	public Class<TaskVars> getEntityClass()
	{
		return TaskVars.class;
	}
	
	/**
	 * 获取本任务所有的流程变量
	 * @param queryFilter
	 * @return
	 */
	public List<TaskVars> getTaskVars(QueryFilter queryFilter)
	{
		
		return getBySqlKey("getTaskVars", queryFilter);
	}

	public void delVarsByActInstId(String actInstId) {
		this.delBySqlKey("delVarsByActInstId", actInstId);
		
	}
}
