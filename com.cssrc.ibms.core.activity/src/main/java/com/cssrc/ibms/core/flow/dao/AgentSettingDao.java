package com.cssrc.ibms.core.flow.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.AgentSetting;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 *<pre>
 * 对象功能:代理设定 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class AgentSettingDao extends BaseDao<AgentSetting>
{
	@Override
	public Class<?> getEntityClass()
	{
		return AgentSetting.class;
	}
	public List<AgentSetting> getGeneralAgentByFilter(Map<String,Object> params){
		return this.getBySqlKey("getAgentByFilter", params);
	}
	public List<AgentSetting> getByFlowKey(String flowKey,Map<String,Object> params) {
		return this.getBySqlKey("getByFlowKey", params);
	}
	public AgentSetting getValidByFlowAndAuthidAndDate(String flowKey,Long authid, Date date) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("flowkey", flowKey);
		params.put("authid", authid);
		params.put("date", date);
		List<AgentSetting> list = this.getBySqlKey("getValidByFlowAndAuthidAndDate", params);
		if(BeanUtils.isNotEmpty(list)){
			return list.get(0);
		}else{
			return null;
		}
	}

}