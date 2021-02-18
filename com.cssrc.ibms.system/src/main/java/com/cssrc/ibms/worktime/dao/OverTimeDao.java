package com.cssrc.ibms.worktime.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.worktime.model.OverTime;

@Repository
public class OverTimeDao extends BaseDao<OverTime> {
	public Class getEntityClass() {
		return OverTime.class;
	}

	public List<OverTime> getListByUserId(long userId, int type,
			Date startTime, Date endTime) {
		Map params = new HashMap();
		params.put("userId", Long.valueOf(userId));
		params.put("workType", Integer.valueOf(type));
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return getBySqlKey("getListByUserId", params);
	}

	public List<OverTime> getListByStart(Date startTime, long userId, int type) {
		Map params = new HashMap();
		params.put("startTime", startTime);
		params.put("userId", Long.valueOf(userId));
		params.put("workType", Integer.valueOf(type));

		return getBySqlKey("getListByStart", params);
	}
}
