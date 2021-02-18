package com.cssrc.ibms.worktime.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.worktime.IOverTimeService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.worktime.dao.OverTimeDao;
import com.cssrc.ibms.worktime.model.OverTime;

@Service
public class OverTimeService extends BaseService<OverTime> implements IOverTimeService{

	@Resource
	private OverTimeDao dao;

	protected IEntityDao<OverTime, Long> getEntityDao() {
		return this.dao;
	}

	public List getWorkType() {
		List typelist = new ArrayList();
		Map map = new HashMap();
		map.put("typeId", "1");
		map.put("typeName", "加班");
		typelist.add(map);
		map = new HashMap();
		map.put("typeId", "2");
		map.put("typeName", "请假");
		typelist.add(map);
		return typelist;
	}

	public List<OverTime> getListByUserId(long userId, int type,
			Date startTime, Date endTime) {
		return this.dao.getListByUserId(userId, type, startTime, endTime);
	}

	public List<OverTime> getListByStart(Date startTime, long userId, int type) {
		return this.dao.getListByStart(startTime, userId, type);
	}
}
