package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.DefVar;
/**
 * 对象功能:流程变量定义 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class DefVarDao extends BaseDao<DefVar>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return DefVar.class;
	}
	
	
	public boolean isVarNameExist(String varName,String varKey,Long defId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("defId", defId);
		params.put("varName", varName);
		params.put("varKey", varKey);
	    Integer obj=(Integer) this.getOne("isVarNameExist", params);
		return obj>0;
	}
	
	/**
	 * 根据流程发布和节点id取得流程变量列表。
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	public List<DefVar> getByDeployAndNode(String deployId,String nodeId){
		Map map=new HashMap();
		map.put("actDeployId", deployId);
		map.put("nodeId", nodeId);
		return getBySqlKey("getByDeployAndNode", map);
	}
	/**
	 * 根据流程定义id获取流程变量列表。
	 * @param defId
	 * @return
	 */
	public List<DefVar> getVarsByFlowDefId(Long defId){
		return getBySqlKey("getVars", defId);
	}
	/**
	 * 根据defId删除该流程下的流程变量
	 */
	public void delByDefId(Long defId){
		delBySqlKey("delByDefId", defId);
		
	}
	
	
	/**
	 * 根据流程定义id获取流程变量列表。
	 * @param defId
	 * @return
	 */	
	public List<DefVar> getByDefId(Long defId){
		return getBySqlKey("getByDefId", defId);
	}
	
}