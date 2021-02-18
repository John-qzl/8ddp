package com.cssrc.ibms.core.flow.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.NodeScriptDao;
import com.cssrc.ibms.core.flow.model.NodeScript;


/**
 * 对象功能:节点运行脚本 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class NodeScriptService extends BaseService<NodeScript>
{
	@Resource
	private NodeScriptDao dao;
	
	public NodeScriptService()
	{
	}
	
	@Override
	protected IEntityDao<NodeScript, Long> getEntityDao() 
	{
		return dao;
	}
	/**
	 * 根据节点id获取脚本。
	 * @param nodeId
	 * @param actDefId
	 * @return
	 */
	public List<NodeScript>  getByNodeScriptId(String nodeId,String actDefId){

		return dao.getByNodeScriptId(nodeId, actDefId);
	}
	
	/**
	 * 根据流程定义id和流程节点id获取脚本放到一个map中。
	 * @param nodeId
	 * @param actDefId
	 * @return
	 */
	public Map<String,NodeScript> getMapByNodeScriptId(String nodeId,String actDefId){
		 List<NodeScript> list=this.getByNodeScriptId(nodeId, actDefId);
		 Map<String, NodeScript> map=new HashMap<String, NodeScript>();
		 for(NodeScript script:list){
			 map.put("type" + script.getScriptType(), script);
		 }
		 
		 return map;
	}
	
	/**
	 * 根据节点和事件类型获取脚本。
	 * @param nodeId
	 * @param actDefId
	 * @param scriptType
	 * @return
	 */
	public NodeScript getScriptByType(String nodeId,String actDefId,Integer scriptType){
		
		NodeScript script= dao.getScriptByType(nodeId, actDefId, scriptType);
		return script;
	}
	/**
	 * 保存流程节点定义
	 * @param defId
	 * @param nodeId
	 * @param list
	 * @throws Exception 
	 */
	public void saveScriptDef(String defId,String nodeId,List<NodeScript> list) throws Exception{
		//先删除
		this.dao.delByDefAndNodeId( defId,nodeId);
		//再重新添加
		for(NodeScript script:list){
			long id=UniqueIdUtil.genId();
			script.setId(id);
			script.setActDefId(defId);
			script.setNodeId(nodeId);
			dao.add(script);
		}
		
	}

}
