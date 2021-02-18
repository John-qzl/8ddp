package com.cssrc.ibms.worktime.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.worktime.model.WorkTime;

@Repository
public class WorkTimeDao extends BaseDao<WorkTime> {
	public Class getEntityClass() {
		return WorkTime.class;
	}

	public List<WorkTime> getBySettingId(String settingId) {
		Map p = new HashMap();
		p.put("settingId", settingId);
		return getSqlSessionTemplate().selectList(
				getIbatisMapperNamespace() + ".getBySettingId", p);
	}
}
