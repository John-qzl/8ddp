package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.SubtableRights;

/**
 * <pre>
 * 对象功能:子表权限 Dao类 
 * 开发人员: zhulongchao
 * </pre>
 */
@Repository
public class SubtableRightsDao extends BaseDao<SubtableRights> {
	@Override
	public Class<?> getEntityClass() {
		return SubtableRights.class;
	}

	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings("all")
	public SubtableRights getByDefIdAndNodeId(String actDefId, String nodeId,Long tableId, String parentActDefId) {
		Map map = new HashMap();
		map.put("actdefid", actDefId);
		map.put("nodeid", nodeId);
		map.put("tableid", tableId);
		map.put("parentActDefId", parentActDefId);
		SubtableRights model = (SubtableRights) this.getUnique("getByDefIdAndNodeIdAndParentId", map);
		return model;
	}
	
	/**
	 * 根据流程ID和节点ID获取子表权限配置
	 * 
	 * @param defId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings("all")
	public SubtableRights getByDefIdAndNodeId(String actDefId, String nodeId,Long tableId) {
		Map map = new HashMap();
		map.put("actdefid", actDefId);
		map.put("nodeid", nodeId);
		map.put("tableid", tableId); 
		SubtableRights model = (SubtableRights) this.getUnique("getByDefIdAndNodeId", map);
		return model;
	}

	public void delByActDefId(String actDefId) {
		this.delBySqlKey("delByActDefId", actDefId);
	}

}