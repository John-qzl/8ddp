package com.cssrc.ibms.core.form.dao;


import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormDefHi;
/**
 * 自定义表单历史Dao层
 * @author YangBo
 *
 */
@Repository
public class FormDefHiDao extends BaseDao<FormDefHi>
{
	public Class<FormDefHi> getEntityClass()
	{
		return FormDefHi.class;
	}
}

