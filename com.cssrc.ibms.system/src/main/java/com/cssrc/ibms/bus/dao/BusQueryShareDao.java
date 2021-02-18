package com.cssrc.ibms.bus.dao;

import com.cssrc.ibms.bus.model.BusQueryShare;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:BusQueryShareDao</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:12:53
 */
@Repository
public class BusQueryShareDao extends BaseDao<BusQueryShare> {
	public Class<?> getEntityClass() {
		return BusQueryShare.class;
	}

	public BusQueryShare getByFilterId(Long filterId) {
		return (BusQueryShare) getOne("getByFilterId", filterId);
	}
}
