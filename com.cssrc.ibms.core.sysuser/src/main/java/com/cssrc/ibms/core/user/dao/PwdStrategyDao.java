package com.cssrc.ibms.core.user.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.PwdStrategy;

import org.springframework.stereotype.Repository;

@Repository
public class PwdStrategyDao extends BaseDao<PwdStrategy> {
	public Class<?> getEntityClass() {
		return PwdStrategy.class;
	}
}
