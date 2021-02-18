 
package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormDialog;

@Repository
public class FormDialogDao extends BaseDao<FormDialog>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormDialog.class;
	}
	
	/**
	 * 根据别名获取对话框对象。
	 * @param alias		对话框别名。
	 * @return
	 */
	public FormDialog getByAlias(String alias){
		return this.getUnique("getByAlias", alias);
	}
	
	/**
	 * 根据别名获取是否存在。
	 * @param alias
	 * @return
	 */
	public Integer  isExistAlias(String alias){
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