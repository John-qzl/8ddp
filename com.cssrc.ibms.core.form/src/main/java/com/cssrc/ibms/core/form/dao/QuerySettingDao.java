package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.QuerySetting;

@Repository
public class QuerySettingDao extends BaseDao<QuerySetting> {
	public Class<?> getEntityClass() {
		return QuerySetting.class;
	}

	public QuerySetting getBySqlId(Long sqlId) {
		QuerySetting sysQuerySetting = (QuerySetting) getUnique("getBySqlId",
				sqlId);
		return sysQuerySetting == null ? new QuerySetting() : sysQuerySetting;
	}

	public void delBySqlId(Long sqlId) {
		Map params = new HashMap();
		params.put("sqlId", sqlId);
		delBySqlKey("delBySqlId", params);
	}
}
