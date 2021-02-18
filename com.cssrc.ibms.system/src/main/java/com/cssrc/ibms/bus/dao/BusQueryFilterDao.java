package com.cssrc.ibms.bus.dao;

import com.cssrc.ibms.bus.model.BusQueryFilter;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:BusQueryFilterDao</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:13:02
 */
@Repository
public class BusQueryFilterDao extends BaseDao<BusQueryFilter> {
	public Class<?> getEntityClass() {
		return BusQueryFilter.class;
	}

	public List<BusQueryFilter> getMyFilterList(String tableName, Long userId) {
		Map params = new HashMap();
		params.put("tableName", tableName);
		params.put("userId", userId);
		return getBySqlKey("getMyFilterList", params);
	}

	public List<BusQueryFilter> getShareFilterList(String tableName, Long userId) {
		Map params = new HashMap();
		params.put("tableName", tableName);
		params.put("userId", userId);
		return getBySqlKey("getShareFilterList", params);
	}
}
