package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.activity.model.ProcessTaskHistory;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;

@Repository
public class TaskHistoryDao extends BaseDao<ProcessTaskHistory>
{
	
	
	@Override
	public Class getEntityClass()
	{
		return ProcessTaskHistory.class;
	}
	
	public ProcessTaskHistory getLastFinshTaskByProcId(String processInstanceId) {
		List<ProcessTaskHistory> list = getBySqlKey("getFinshTaskByProcId",
				processInstanceId);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
}
