package com.cssrc.ibms.core.form.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.DataTemplate;
/**
 *<pre>
 * 对象功能:业务数据模板 Dao类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Repository
public class DataTemplateDao extends BaseDao<DataTemplate>
{
	@Override
	public Class<?> getEntityClass()
	{
		return DataTemplate.class;
	}

	public DataTemplate getByFormKey(Long formKey) {
		return  this.getUnique("getByFormKey",formKey);
	}
	
	/**
	 * 根据表单的formKey删除表单模版。
	 * @param formKey
	 */
	public void delByFormKey(Long formKey){
		this.delBySqlKey("delByFormKey", formKey);
	}
	
	/**
	 * 根据formkey获取业务表单数量。
	 * @param formKey
	 * @return
	 */
	public Integer getCountByFormKey(Long formKey){
		return (Integer)this.getOne("getCountByFormKey", formKey);
	}
	/**
	 * 根据tableid获取业务表单数量。
	 * @param tableId
	 * @return
	 */
	public List<DataTemplate> getByTableId(Long tableId){
		return this.getBySqlKey("getByTableId", tableId);
	}
	
	/**
     * 根据alias 别名获取业务数据模板。
     * @param alias
     * @return
     */
    public DataTemplate getByAlias(String alias){
        return this.getUnique("getByAlias", alias);
    }

	/**
	 * 根据alias查id,也就是获取displayId
	 * alias可能重复,建议使用getIdByFormSubject
	 * @deprecated
	 * @param alias
	 * @return
	 */
	public String getIdByAlias(String alias){

		String displayId= (String)this.getOne("getIdByAlias",alias);
		return displayId;
	}

	/**

	 * 根据IBMS_FORM_DEF.subject查IBMS_DATA_TEMPLATE.Id
	 * 为避免中文乱码问题,请考虑使用getIdByFormAlias
	 * @deprecated
	 * @param subject
	 * @return
	 *
	 */
	public String getIdByFormSubject(String subject){
		String displayId= (String)this.getOne("getIdByFormSubject",subject);
		return displayId;
	}

	/**
	 * 根据IBMS_FORM_DEF.formAlisa查IBMS_DATA_TEMPLATE.Id
	 * @param formAlias
	 * @return
	 */
	public String getIdByFormAlias(String formAlias){
		String displayId= (String)this.getOne("getIdByFormAlias",formAlias);
		return displayId;
	}
}