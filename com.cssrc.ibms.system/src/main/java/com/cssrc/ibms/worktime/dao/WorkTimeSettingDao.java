package com.cssrc.ibms.worktime.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.worktime.model.WorkTimeSetting;

@Repository
public class WorkTimeSettingDao extends BaseDao<WorkTimeSetting> {
	public Class getEntityClass() {
		return WorkTimeSetting.class;
	}
}