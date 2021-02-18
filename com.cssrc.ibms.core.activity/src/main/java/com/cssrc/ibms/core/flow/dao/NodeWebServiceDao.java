package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeWebService;

 
@Repository
public class NodeWebServiceDao extends BaseDao<NodeWebService> {
	@Override
	public Class<?> getEntityClass() {
		return NodeWebService.class;
	}

	public List<NodeWebService> getAllByNodeIdActDefId(String nodeId, String actDefId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		return this.getBySqlKey("getAllNodeWebService", params);
	}
	
	public NodeWebService getByNodeIdActDefId(String nodeId, String actDefId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		return getUnique("getNodeWebService", params);
	}
	
	public List<NodeWebService> getByActDefId(String actDefId) {
		return this.getBySqlKey("getByActDefId", actDefId);
	}
	
	public void delByActDefId(String actDefId){
		this.delBySqlKey("delByActDefId", actDefId);
	}
}