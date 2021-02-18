package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeMessage;

@Repository
public class NodeMessageDao extends BaseDao<NodeMessage>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return NodeMessage.class;
	}
	/**
	 * 通过流程发布ID及节点id获取流程设置节点实体
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<NodeMessage> getMessageByActDefIdNodeId(String actDefId,String nodeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);	
		return this.getBySqlKey("getMessageByActDefIdNodeId", params);
	}
	/**
	 * 根据act流程定义Id删除流程消息节点
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}
	
	public  void delByActDefIdAndNodeId(String actDefId,String nodeId){
		Map<String,Object> param=new HashMap<String, Object>();
		param.put("actDefId", actDefId);
		param.put("nodeId", nodeId);
		delBySqlKey("delByActDefIdAndNodeId", param);
	}
	
	/**
	 * 根据流程定义ID取得消息列表。
	 * @param actDefId
	 * @return
	 */
	public List<NodeMessage> getByActDefId(String actDefId) {
		return this.getBySqlKey("getByActDefId", actDefId);
	}
	public List<NodeMessage> getByActDefIdAndNodeId(String actDefId,
			String nodeId) {
		Map<String,Object> params=new HashMap<String, Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		return this.getBySqlKey("getByActDefIdAndNodeId", params);
	}
	
}