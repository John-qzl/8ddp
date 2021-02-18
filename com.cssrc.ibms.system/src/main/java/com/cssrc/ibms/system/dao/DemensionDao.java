package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.system.model.Demension;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * <p>Title:DemensionDaoNew</p>
 * @author Yangbo 
 * @date 2016-8-1下午04:09:39
 */
@Repository
public class DemensionDao extends BaseDao<Demension> {
	public Class getEntityClass() {
		return Demension.class;
	}

	public boolean getNotExists(Map params) {
		int cnt = ((Integer) getOne("getExists", params)).intValue();
		return cnt == 0;
	}

	public List<Demension> getDemenByQuery(QueryFilter queryFilter) {
		return getBySqlKey("getDemenByQuery", queryFilter);
	}
	
	public Demension getByName(String name) {
		return getUnique("getByName", name);
	}
}
