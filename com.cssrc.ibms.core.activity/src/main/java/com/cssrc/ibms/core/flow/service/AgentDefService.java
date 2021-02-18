package com.cssrc.ibms.core.flow.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.AgentDefDao;
import com.cssrc.ibms.core.flow.model.AgentDef;

/**
 *<pre>
 * 对象功能:代理的流程列表 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class AgentDefService extends BaseService<AgentDef>
{
	@Resource
	private AgentDefDao dao;
	
	
	
	public AgentDefService()
	{
	}
	
	@Override
	protected IEntityDao<AgentDef, Long> getEntityDao() 
	{
		return dao;
	}
	
	
}
