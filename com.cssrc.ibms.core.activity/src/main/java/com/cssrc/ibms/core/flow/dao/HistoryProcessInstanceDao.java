package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;

@Repository
public class HistoryProcessInstanceDao extends  BaseDao<HistoricProcessInstanceEntity>{

	@Override
	public Class getEntityClass() {
		return HistoricProcessInstanceEntity.class;
	}
	
	/**
	 * 根据流程实例ID和开始节点ID获取历史流程实例对象
	 * @param actInstanceId 流程实例ID
	 * @param nodeId 开始节点ID
	 * @return
	 */
	public HistoricProcessInstanceEntity getByInstanceIdAndNodeId(String actInstanceId,String nodeId){
		Map<String,String> map = new HashMap<String,String>();
		map.put("actInstanceId",actInstanceId);
		map.put("nodeId",nodeId);
		return this.getUnique("getByInstanceIdAndNodeId", map);
	}
}
