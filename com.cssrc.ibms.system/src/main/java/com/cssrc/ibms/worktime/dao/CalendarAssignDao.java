package com.cssrc.ibms.worktime.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.worktime.model.CalendarAssign;

@Repository
public class CalendarAssignDao extends BaseDao<CalendarAssign> {
	public Class getEntityClass() {
		return CalendarAssign.class;
	}

	public CalendarAssign getByAssignId(int assignType, long assignId) {
		Map map = new HashMap();
		map.put("assignType", Integer.valueOf(assignType));
		map.put("assignId", Long.valueOf(assignId));
		CalendarAssign obj = (CalendarAssign) getUnique("getByAssignId", map);
		return obj;
	}

	public void delByCalId(Long[] calIds) {
		for (Long calId : calIds)
			getBySqlKey("delByCalId", calId);
	}

	public CalendarAssign getbyAssignId(Long assignId) {
		return (CalendarAssign) getUnique("getbyAssign", assignId);
	}
}
