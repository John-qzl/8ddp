package com.cssrc.ibms.api.activity.intf;

import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;

public interface IHistoryTaskInstanceService {
	public HistoricTaskInstanceEntity getByInstanceIdAndNodeId(String actInstanceId, String nodeId);
	public HistoricTaskInstanceEntity getById(Long taskId);
}
