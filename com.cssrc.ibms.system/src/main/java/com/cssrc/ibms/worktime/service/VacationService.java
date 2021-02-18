package com.cssrc.ibms.worktime.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.worktime.IVacationService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.worktime.dao.VacationDao;
import com.cssrc.ibms.worktime.model.Vacation;

@Service
public class VacationService extends BaseService<Vacation> implements IVacationService{

	@Resource
	private VacationDao dao;

	protected IEntityDao<Vacation, Long> getEntityDao() {
		return this.dao;
	}

	public Map<Integer, String> getByYearMon(String statTime, String endTime) {
		Map map = new HashMap();

		Date startDate = TimeUtil.convertString(statTime, "yyyy-MM-dd");
		Date endDate = TimeUtil.convertString(endTime, "yyyy-MM-dd");
		List<Vacation> valist = this.dao.getByYearMon(startDate, endDate);
		int curMonth = Integer.parseInt(statTime.split("-")[1]);
		for (Vacation va : valist) {
			String[] days = DateUtil.getDaysBetweenDate(va.getStatTime()
					.toString(), va.getEndTime().toString());
			for (String day : days) {
				int tmpMonth = Integer.parseInt(day.split("-")[1]);
				if (curMonth == tmpMonth) {
					map.put(Integer.valueOf(Integer.parseInt(day.substring(8,
							10))), va.getName());
				}
			}
		}
		return map;
	}
}