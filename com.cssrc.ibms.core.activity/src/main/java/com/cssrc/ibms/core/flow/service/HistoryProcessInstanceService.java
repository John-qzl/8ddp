package com.cssrc.ibms.core.flow.service;

import javax.annotation.Resource;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IHistoryProcessInstanceService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.HistoryProcessInstanceDao;

@Service
public class HistoryProcessInstanceService  extends BaseService<HistoricProcessInstanceEntity> implements IHistoryProcessInstanceService{
	@Resource
	private HistoryProcessInstanceDao dao;
	
	@Override
	protected IEntityDao<HistoricProcessInstanceEntity, Long> getEntityDao() 
	{
		return dao;
	}
	public HistoricProcessInstanceEntity getByInstanceIdAndNodeId(String actInstanceId,String nodeId){
		return dao.getByInstanceIdAndNodeId(actInstanceId, nodeId);
	}
}
