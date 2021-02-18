package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormQuery;
/**
 * 对象功能:通用表单查询 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class FormQueryDao extends BaseDao<FormQuery>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormQuery.class;
	}
	
	/**
	 * 根据别名获取查询对象。
	 * @param alias		查询别名。
	 * @return
	 */
	public FormQuery getByAlias(String alias){
		return this.getUnique("getByAlias", alias);
	}

	/**
	 * 根据别名获取是否存在。
	 * @param alias
	 * @return
	 */
	public Integer isExistAlias(String alias){
		return (Integer)this.getOne("isExistAlias", alias);
	}
	
	/**
	 * 根据别名判断是否存在，用于更新判断。
	 * @param id
	 * @param alias
	 * @return
	 */
	public Integer isExistAliasForUpd(Long id,String alias){
		Map map=new HashMap();
		map.put("id",id);
		map.put("alias", alias);
		return (Integer)this.getOne("isExistAliasForUpd", map);
	}
}