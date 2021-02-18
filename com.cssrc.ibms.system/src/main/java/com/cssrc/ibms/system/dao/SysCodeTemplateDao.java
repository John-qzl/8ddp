package com.cssrc.ibms.system.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysCodeTemplate;

/**
 * 对象功能:自定义表代码模版 Dao类
 * @author zhulongchao
 *
 */
@Repository
public class SysCodeTemplateDao extends BaseDao<SysCodeTemplate>
{
	@Override
	public Class<?> getEntityClass()
	{
		return SysCodeTemplate.class;
	}

	public void delByTemplateType(Short templateType) {
		this.delBySqlKey("delByTemplateType", templateType);
	}

	public SysCodeTemplate getByTemplateAlias(String alias) {
		return this.getUnique("getByTemplateAlias", alias);
	}


}