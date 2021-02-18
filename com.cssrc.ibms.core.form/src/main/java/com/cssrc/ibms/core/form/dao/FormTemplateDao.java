package com.cssrc.ibms.core.form.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormTemplate;

/**
 * 对象功能:表单模板 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class FormTemplateDao extends BaseDao<FormTemplate>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormTemplate.class;
	}
	
	/**
	 * 获取所有的数据。
	 * @param params
	 * @return
	 */
	public List<FormTemplate> getAll(Map params){
		return this.getBySqlKey("getAll",params);
	}
	
	/**
	 * 插入一条数据
	 */
	public void add(FormTemplate bpmFormTemplate){
		this.getBySqlKey("add",bpmFormTemplate);
	}
	
	/**
	 * 删除所有的数据
	 */
	public void delSystem(){
		this.delBySqlKey("delSystem", null);
	}
	
	/**
	 * 根据别名获取模版。
	 * @param alias
	 * @return
	 */
	public FormTemplate getByTemplateAlias(String alias){
		return this.getUnique("getByTemplateAlias", alias);
	}
	
	/**
	 * 获取模版是否有数据。
	 * @return
	 */
	public Integer getHasData(){
		return (Integer)this.getOne("getHasData", null);
	}
}