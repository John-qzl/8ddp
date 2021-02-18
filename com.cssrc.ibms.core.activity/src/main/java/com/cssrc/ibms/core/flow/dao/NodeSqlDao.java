package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeSql;

/**
 * NodeSqlDao
 * @author liubo
 * @date 2017年2月16日
 */
@Repository
public class NodeSqlDao extends BaseDao<NodeSql>{
	
	public Class<?> getEntityClass(){
		return NodeSql.class;
	}
 
	public List getByDef(String actdefId, String nodeId, String action){
		Map map = new HashMap();
		map.put("actdefId", actdefId);
		map.put("nodeId", nodeId);
		map.put("action", action);
 
		return getBySqlKey("getByDef", map);
	}
}
