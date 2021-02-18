package com.cssrc.ibms.core.flow.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.springframework.stereotype.Repository;

@Repository
public class HistoryTaskInstanceDao extends BaseDao<HistoricTaskInstanceEntity> {
	public Class getEntityClass() {
		return HistoricTaskInstanceEntity.class;
	}

	public HistoricTaskInstanceEntity getByInstanceIdAndNodeId(
			String actInstanceId, String nodeId) {
		Map map = new HashMap();
		map.put("actInstanceId", actInstanceId);
		map.put("nodeId", nodeId);
		List list = getBySqlKey("getByInstanceIdAndNodeId", map);
		if (list.size() > 0)
			return (HistoricTaskInstanceEntity) list.get(0);
		return null;
	}

	public HistoricTaskInstanceEntity getById(Long taskId) {
		HistoricTaskInstanceEntity ent = (HistoricTaskInstanceEntity) getUnique(
				"getById", taskId);
		return ent;
	}
}
