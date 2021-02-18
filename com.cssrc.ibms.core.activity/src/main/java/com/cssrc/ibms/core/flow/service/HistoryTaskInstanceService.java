package com.cssrc.ibms.core.flow.service;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IHistoryTaskInstanceService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.HistoryTaskInstanceDao;
@Service
public class HistoryTaskInstanceService   extends BaseService<HistoricTaskInstanceEntity> implements IHistoryTaskInstanceService{
	
	@Resource
	private HistoryTaskInstanceDao dao;
	
	@Override
	protected IEntityDao<HistoricTaskInstanceEntity, Long> getEntityDao() 
	{
		return dao;
	}
	public HistoricTaskInstanceEntity getByInstanceIdAndNodeId(String actInstanceId, String nodeId){
		return dao.getByInstanceIdAndNodeId(actInstanceId, nodeId);
	}
	public HistoricTaskInstanceEntity getById(Long taskId){
		return dao.getById(taskId);
	}
	
}
