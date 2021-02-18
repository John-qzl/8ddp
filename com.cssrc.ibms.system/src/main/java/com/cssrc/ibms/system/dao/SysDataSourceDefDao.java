package com.cssrc.ibms.system.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysDataSourceDef;

/**
 * SysDataSourceDefDao
 * @author liubo
 * @date 2017年4月14日
 */
@Repository
public class SysDataSourceDefDao extends BaseDao<SysDataSourceDef>{

	@Override
	public Class<?> getEntityClass() {
		// TODO Auto-generated method stub
		return SysDataSourceDef.class;
	}

}
