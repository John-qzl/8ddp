package com.cssrc.ibms.core.flow.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.AgentDef;
/**
 *<pre>
 * 对象功能:代理的流程列表 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class AgentDefDao extends BaseDao<AgentDef>
{
	@Override
	public Class<?> getEntityClass()
	{
		return AgentDef.class;
	}
	public List<AgentDef> getByMainId(Long settingid) {
		return this.getBySqlKey("getAgentDefList", settingid);
	}
	
	public void delByMainId(Long settingid) {
		this.delBySqlKey("delByMainId", settingid);
	}
}