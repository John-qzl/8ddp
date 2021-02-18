package com.cssrc.ibms.core.flow.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.NodeUser;
/**  
 * 开发人员:zhulongchao 
 */
@Repository
public class NodeUserDao extends BaseDao<NodeUser>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return NodeUser.class;
	}
	
	
	
	/**
	 * 根据流程定义ID获得流程节点人员
	 * @param actDefId
	 * @return
	 */
	public List<NodeUser> getByActDefId(String actDefId) {
		return getBySqlKey("getByActDefId", actDefId);
	} 
	
	/**
	 * 
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}
	
	
	/**
	 * 
	 * @Methodname: delByConditionId
	 * @Discription: 
	 * @param conditionId
	 * @Author HH
	 * @Time 2012-12-19 下午7:34:42
	 */
	public void delByConditionId(Long conditionId)
	{
		getBySqlKey("delByConditionId",conditionId);
	}
	
	/**
	 * 修复数据
	 * @return
	 */
	public List<NodeUser> selectNull(){
		return (List<NodeUser>)getBySqlKey("selectNull");
	}
	
	/**
	 * 根据条件用户条件规则ID，获取节点用户
	 * @param conditionId
	 * @return
	 */
	public List<NodeUser> getByConditionId(Long conditionId) {
		
		return (List<NodeUser>)getBySqlKey("getByConditionId",conditionId);
	}

}