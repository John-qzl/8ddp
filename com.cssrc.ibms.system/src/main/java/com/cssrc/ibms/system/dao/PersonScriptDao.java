package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.PersonScript;
/**
 *<pre>
 * 对象功能:系统条件脚本 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class PersonScriptDao extends BaseDao<PersonScript>
{
	@Override
	public Class<?> getEntityClass()
	{
		return PersonScript.class;
	}

	public List<PersonScript> getPersonScript(){
		return this.getBySqlKey("getPersonScript");
	}
}