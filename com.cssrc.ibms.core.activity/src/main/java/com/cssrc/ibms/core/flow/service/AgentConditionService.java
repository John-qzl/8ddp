package com.cssrc.ibms.core.flow.service;

import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.AgentConditionDao;
import com.cssrc.ibms.core.flow.model.AgentCondition;
import com.cssrc.ibms.core.flow.model.UserCondition;


/**
 *<pre>
 * 对象功能:条件代理的配置 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class AgentConditionService extends BaseService<AgentCondition>
{
	@Resource
	private AgentConditionDao dao;
	
	@Resource
	private NodeUserService nodeUserService;
	
	public AgentConditionService()
	{
	}
	
	@Override
	protected IEntityDao<AgentCondition, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 计算规则是否成立
	 * @param agentCondition 规则实体
	 * @param formId 表单ID
	 * @param vars 流程变量
	 * @return
	 */
	public Boolean conditionCheck(AgentCondition agentCondition,Long formId,Map<String,Object> formVars) {
		Boolean isPassCondition=true;
		
		JSONObject jsonObject = JSONObject.fromObject(agentCondition.getCondition());
		if(jsonObject==null)return isPassCondition;
		
		//如果表单ID为空  或者 与现用表单不匹配，则认为规则 成立		
		Long currentTableId = jsonObject.getLong("tableId");
		if(currentTableId == null || !currentTableId.equals(formId))return isPassCondition;
		
		UserCondition bpmUserCondition = new UserCondition();
		bpmUserCondition.setFormIdentity(currentTableId.toString());
		bpmUserCondition.setCondition(jsonObject.getString("condition"));
		isPassCondition = nodeUserService.conditionCheck(bpmUserCondition, formId.toString(), formVars);
		return isPassCondition;
	}
}
