package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodePrivilege;
 
@Repository
public class NodePrivilegeDao extends BaseDao<NodePrivilege> {
	@Override
	public Class<?> getEntityClass() {
		return NodePrivilege.class;
	}

	/**
	 * 根据发布id和流程节点id取得对象。
	 * 
	 * @param deployId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<NodePrivilege> getPrivilegesByDefIdAndNodeId(
			String actDefId, String nodeId) {
		Map map = new HashMap();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		List<NodePrivilege> list = (List<NodePrivilege>) this.getList(
				"getPrivilegesByDefIdAndNodeId", map);
		return list;
	}

	/**
	 * 根据发布id和流程节点id删除特权
	 * 
	 * @param actDefId
	 * @param nodeId
	 */
	public void delByDefIdAndNodeId(String actDefId, String nodeId) {
		Map map = new HashMap();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		delBySqlKey("delByDefIdAndNodeId", map);
	}

	/**
	 * 根据发布ID,流程节点ID和特权类型获取特权列表
	 * 
	 * @param actDefId
	 * @param nodeId
	 * @param privilegeMode
	 * @return
	 */
	public List<NodePrivilege> getPrivilegesByDefIdAndNodeIdAndMode(String actDefId, String nodeId, Long privilegeMode) {
		Map map = new HashMap();
		map.put("actDefId", actDefId);
		map.put("nodeId", nodeId);
		map.put("privilegeMode", privilegeMode);
		List<NodePrivilege> list = (List<NodePrivilege>) this.getList("getPrivilegesByDefIdAndNodeIdAndMode", map);
		return list;
	}
	
	/**
	 * 根据act流程定义Id删除特权
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}
}