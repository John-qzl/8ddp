package com.cssrc.ibms.bus.dao;

import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:BusQueryRuleDao</p>
 * @author Yangbo 
 * @date 2016-8-8下午03:21:37
 */
@Repository
public class BusQueryRuleDao extends BaseDao<BusQueryRule> {
	public Class<?> getEntityClass() {
		return BusQueryRule.class;
	}

	public BusQueryRule getByTableName(String tableName) {
		return (BusQueryRule) getUnique("getByTableName", tableName);
	}

	public Integer getCountByTableName(String tableName) {
		return (Integer) getOne("getCountByTableName", tableName);
	}
}
