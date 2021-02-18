package com.cssrc.ibms.report.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.report.model.SignItem;
@Repository
public class SignItemDao extends BaseDao<SignItem>{

	@Override
	public Class getEntityClass() {
		return SignItem.class;
	}
	
	
}