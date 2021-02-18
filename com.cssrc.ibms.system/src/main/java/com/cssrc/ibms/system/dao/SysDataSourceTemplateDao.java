package com.cssrc.ibms.system.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysDataSourceTemplate;

/**
 * SysDataSourceTemplateDao
 * @author liubo
 * @date 2017年4月14日
 */
@Repository
public class SysDataSourceTemplateDao extends BaseDao<SysDataSourceTemplate>{

	@Override
	public Class<?> getEntityClass() {
		// TODO Auto-generated method stub
		return SysDataSourceTemplate.class;
	}

}
