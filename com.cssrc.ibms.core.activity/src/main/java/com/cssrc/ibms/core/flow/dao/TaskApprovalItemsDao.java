package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.TaskApprovalItems;

@Repository
public class TaskApprovalItemsDao extends BaseDao<TaskApprovalItems> {
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		return TaskApprovalItems.class;
	}

	/**
	 * 根据流程定义ID取常用语
	 * 
	 * @param nodeId
	 * @return
	 */
	public TaskApprovalItems getFlowApproval(String actDefId, int isGlobal) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actDefId", actDefId);
		map.put("isGlobal", isGlobal);
		return this.getUnique("getFlowApproval", map);
	}

	/**
	 * 根据节点ID取常用语
	 * 
	 * @param nodeId
	 * @return
	 */
	public TaskApprovalItems getTaskApproval(String actDefId, String nodeId,
			int isGlobal) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		map.put("isGlobal", isGlobal);
		return this.getUnique("getTaskApproval", map);
	}

	/**
	 * actDefId,isGlobal删除流程常用语
	 * 
	 * @return
	 */
	public void delFlowApproval(String actDefId, int isGlobal) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actDefId", actDefId);
		map.put("isGlobal", isGlobal);
		this.delBySqlKey("delFlowApproval", map);
	}

	/**
	 * 根据节点ID删除常用语
	 * 
	 * @param nodeId
	 * @return
	 */
	public void delTaskApproval(String actDefId, String nodeId, int isGlobal) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		map.put("isGlobal", isGlobal);
		this.delBySqlKey("delTaskApproval", map);
	}

	/**
	 * 取流程常用语
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<TaskApprovalItems> getApprovalByActDefId(String actDefId,
			String nodeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		map.put("isGlobal", TaskApprovalItems.global);
		map.put("notGlobal", TaskApprovalItems.notGlobal);
		return this.getBySqlKey("getApprovalByActDefId", map);
	}

	/**
	 * 根据流程定义ID取常用语
	 * 
	 * @param actDefId
	 * @return
	 */
	public List<TaskApprovalItems> getByActDefId(String actDefId) {
		return this.getBySqlKey("getByActDefId", actDefId);
	}
	/**
	 * 根据act流程定义Id删除常用语
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}
	
	public List<TaskApprovalItems> getByDefKeyAndUserAndSys(String defKey, Long curUserId){
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("defKey", defKey);
	    map.put("curUserId", curUserId);
	    return getBySqlKey("getByDefKeyAndUserAndSys", map);
	}
	
	public List<TaskApprovalItems> getByType(Short type){
		Map<String, Object> map = new HashMap<String, Object>();
	    map.put("type", type);
	    return getBySqlKey("getByType", map);
	 }

    /**
     * 根据流程节点查找所有审批模板
     * @param string
     * @return
     */
    public List<TaskApprovalItems> getByDefNodeKey(String defNodeKey)
    {
        return getBySqlKey("getByDefNodeKey", defNodeKey);
    }
}