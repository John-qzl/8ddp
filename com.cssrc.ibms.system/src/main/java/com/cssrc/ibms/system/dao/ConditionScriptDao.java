package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.ConditionScript;
/**
 *<pre>
 * 对象功能:系统条件脚本 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class ConditionScriptDao extends BaseDao<ConditionScript>
{
	@Override
	public Class<?> getEntityClass()
	{
		return ConditionScript.class;
	}

	public List<ConditionScript> getConditionScript(){
		return this.getBySqlKey("getConditionScript");
	}
}