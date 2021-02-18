package com.cssrc.ibms.bus.dao;

import com.cssrc.ibms.bus.model.BusQuerySetting;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:BusQuerySettingDao</p>
 * @author Yangbo 
 * @date 2016-8-9下午03:13:07
 */
@Repository
public class BusQuerySettingDao extends BaseDao<BusQuerySetting> {
	public Class<?> getEntityClass() {
		return BusQuerySetting.class;
	}

	public BusQuerySetting getByTableNameUserId(String tableName, Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("tableName", tableName);
		params.put("userId", userId);
		return (BusQuerySetting) getOne("getByTableNameUserId", params);
	}
}
