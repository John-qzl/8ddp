package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeSign;

@Repository
public class NodeSignDao extends BaseDao<NodeSign>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return NodeSign.class;
	}
	
	
	/**
	 * 根据发布id和流程节点id取得对象。
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NodeSign getByDefIdAndNodeId(String actDefId,String nodeId)
	{
		Map map=new HashMap();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		NodeSign model=(NodeSign) this.getUnique("getByDefIdAndNodeId", map);
		return model;
	}
	
	/**
	 * 根据发布id和流程节点id取得对象。
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<NodeSign> getByActDefId(String actDefId)
	{
		return getBySqlKey("getByActDefId",actDefId);
	}
	
	/**
	 * 根据act流程定义Id删除会签任务投票规则
	 * @param actDefId
	 */
	public void delActDefId(String actDefId){
		 delBySqlKey("delByActDefId", actDefId);
		
	}
}