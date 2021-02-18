
package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeRule;

/**
 * 对象功能:流程节点规则 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class NodeRuleDao extends BaseDao<NodeRule>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return NodeRule.class;
	}
	/**
	 * 获取所有的任务定义
	 * @param actDefId
	 * @param ruleId
	 * @return
	 */
	public  List<NodeRule>  getByDefIdNodeId(String actDefId,String nodeId)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);

		return getBySqlKey("getByDefIdNodeId", params); 
	}
	
	/**
	 * 更新规则排序。
	 * @param ruleId
	 * @param priority
	 */
	public void reSort(Long ruleId,Long priority){
		Map map=new HashMap();
		map.put("ruleId", ruleId);
		map.put("priority", priority);
		this.update("reSort", map);
	}
	/**
	 * 根据act流程定义Id删除流程节点规则
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		 delBySqlKey("delByActDefId", actDefId);
	}
	/**
	 * 通过act流程定义ID获得节点规则
	 * @param actDefId
	 * @return
	 */
	public List<NodeRule> getByActDefId(String actDefId) {
		return getBySqlKey("getByActDefId", actDefId); 
	}
}