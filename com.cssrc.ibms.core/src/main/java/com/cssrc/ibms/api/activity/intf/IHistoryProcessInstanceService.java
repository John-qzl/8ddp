package com.cssrc.ibms.api.activity.intf;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;

public interface IHistoryProcessInstanceService {
	public HistoricProcessInstanceEntity getByInstanceIdAndNodeId(String actInstanceId,String nodeId);
}
