package com.cssrc.ibms.core.form.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormRule;

@Repository
public class FormRuleDao extends BaseDao<FormRule>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormRule.class;
	}
}