package com.cssrc.ibms.worktime.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.worktime.model.Vacation;

@Repository
public class VacationDao extends BaseDao<Vacation> {
	public Class getEntityClass() {
		return Vacation.class;
	}

	public List<Vacation> getByYearMon(Date statTime, Date endTime) {
		Map map = new HashMap();
		map.put("statTime", statTime);
		map.put("endTime", endTime);
		String sqlKey = "getByYearMon_" + getDbType();
		return getBySqlKey(sqlKey, map);
	}
}