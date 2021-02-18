package com.cssrc.ibms.worktime.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.worktime.IWorkTimeSettingService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.worktime.dao.WorkTimeSettingDao;
import com.cssrc.ibms.worktime.model.WorkTimeSetting;

@Service
public class WorkTimeSettingService extends BaseService<WorkTimeSetting> implements IWorkTimeSettingService{

	@Resource
	private WorkTimeSettingDao dao;

	protected IEntityDao<WorkTimeSetting, Long> getEntityDao() {
		return this.dao;
	}
}
