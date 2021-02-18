package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.AgentCondition;
/**
 *<pre>
 * 对象功能:条件代理的配置 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class AgentConditionDao extends BaseDao<AgentCondition>
{
	@Override
	public Class<?> getEntityClass()
	{
		return AgentCondition.class;
	}
	
	public List<AgentCondition> getByMainId(Long settingid) {
		return this.getBySqlKey("getAgentConditionList", settingid);
	}
	
	public void delByMainId(Long settingid) {
		this.delBySqlKey("delByMainId", settingid);
	}

}