package com.cssrc.ibms.core.flow.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.activity.model.ProcessTaskHistory;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.TaskHistoryDao;

@Service
public class TaskHistoryService extends BaseService<ProcessTaskHistory>{
	@Resource
	private TaskHistoryDao dao;

	public TaskHistoryService() {
	}

	@Override
	protected IEntityDao<ProcessTaskHistory, Long> getEntityDao() {
		return dao;
	}

	
}
