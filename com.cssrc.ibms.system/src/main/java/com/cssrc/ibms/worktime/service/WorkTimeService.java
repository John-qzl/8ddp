package com.cssrc.ibms.worktime.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.worktime.IWorkTimeService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.worktime.dao.WorkTimeDao;
import com.cssrc.ibms.worktime.model.WorkTime;

@Service
public class WorkTimeService extends BaseService<WorkTime> implements IWorkTimeService{

	@Resource
	private WorkTimeDao dao;

	protected IEntityDao<WorkTime, Long> getEntityDao() {
		return this.dao;
	}

	public List<WorkTime> getBySettingId(Long settingId) {
		return this.dao.getBySettingId(String.valueOf(settingId));
	}

	public void workTimeAdd(Long settingId, String[] startTime,
			String[] endTime, String[] memo) {
		if ((startTime != null) && (endTime != null)) {
			this.dao.delBySqlKey("delBySettingId", settingId);

			for (int idx = 0; idx < startTime.length; idx++) {
				WorkTime worktime = new WorkTime();
				try {
					worktime.setId(Long.valueOf(UniqueIdUtil.genId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				worktime.setSettingId(settingId);
				worktime.setStartTime(startTime[idx]);
				worktime.setEndTime(endTime[idx]);
				worktime.setMemo(memo[idx]);
				this.dao.add(worktime);
			}
		}
	}
}
