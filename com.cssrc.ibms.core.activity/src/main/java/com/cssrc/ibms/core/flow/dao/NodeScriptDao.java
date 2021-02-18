package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeScript;

@Repository
public class NodeScriptDao extends BaseDao<NodeScript>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return NodeScript.class;
	}
	
	/**
	 * 根据节点获取事件列表代码。
	 * @param nodeId
	 * @param actDefId
	 * @return
	 */
	public List<NodeScript> getByNodeScriptId(String nodeId,String actDefId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		return getBySqlKey("getNodeScript",params);
	}
	
	/**
	 * 根据节点获取事件列表代码。
	 * @param nodeId
	 * @param actDefId
	 * @return
	 */
	public List<NodeScript> getByActDefId(String actDefId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);

		return getBySqlKey("getNodeScript",params);
	}
	
	
	/**
	 * 根据流程定义ID和节点ID删除脚本数据。
	 * @param nodeId
	 * @param actDefId
	 */
	public void delByDefAndNodeId(String actDefId,String nodeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		this.update("delByDefAndNodeId",params);
	}
	
	/**
	 * 根据节点和脚本类型脚本。
	 * @param nodeId
	 * @param actDefId
	 * @param scriptType
	 * @return
	 */
	public NodeScript getScriptByType(String nodeId,String actDefId,Integer scriptType){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		params.put("scriptType", scriptType);
		return this.getUnique("getScriptByType", params);
	}
	/**
	 * 根据act流程定义Id删除流程节点事件脚本
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}

}