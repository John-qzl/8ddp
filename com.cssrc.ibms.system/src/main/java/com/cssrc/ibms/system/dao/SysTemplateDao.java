package com.cssrc.ibms.system.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysTemplate;

/**
 * 对象功能:模版管理 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class SysTemplateDao extends BaseDao<SysTemplate>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return SysTemplate.class;
	}
	
	/**
	 * 设置默认模板
	 * @param id
	 */
	public int setDefault(long id){
		return this.update("updateDefault", id);
	}
	
	/**
	 * 设置用途类型的所有模板为非默认状态
	 * @param tempType
	 */
	public int setNotDefaultByUseType(Integer useType){
		return this.update("updateNotDefaultByUseType", useType);
	}
	
	/**
	 * 通过用途类型获取模板
	 * @param useType
	 * @return
	 */
	public SysTemplate getDefaultByUseType(Integer useType){
		return this.getUnique("getDefaultByUseType", useType);
	}

	public void delByUseType(Integer useType) {
		this.delBySqlKey("delByUseType", useType);
	}
}