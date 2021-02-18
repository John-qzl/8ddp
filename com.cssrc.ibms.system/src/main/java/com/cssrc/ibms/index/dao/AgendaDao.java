package com.cssrc.ibms.index.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.Agenda;


/**
 * 日程信息DAO层
 * @author YangBo
 *
 */
@Repository
public class AgendaDao extends BaseDao<Agenda>{

	public Class<Agenda> getEntityClass() {
		return Agenda.class;
	}
	
	
	/**
	 * 时间区间内日程数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Agenda> getCalendarList(Date startTime, Date endTime,Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("userId", userId);
		return getBySqlKey("getCalendarList", params);

	}

}
