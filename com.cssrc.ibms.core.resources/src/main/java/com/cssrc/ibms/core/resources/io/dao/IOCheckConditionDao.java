package com.cssrc.ibms.core.resources.io.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.CheckCondition;
@Repository
public class IOCheckConditionDao {
	@Resource
	private JdbcDao jdbcDao;

	public CheckCondition getById(String id) {
		String sql = " select * from W_CK_CONDITION where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new CheckCondition(map);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(CheckCondition checkCondition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_CK_CONDITION set ");
		sql.append(" F_NAME=:F_NAME,F_ORDER=:F_ORDER,F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", checkCondition.getId());
		map.put("F_NAME", checkCondition.getName());
		map.put("F_ORDER", checkCondition.getOrder());
		map.put("F_TABLE_TEMP_ID", checkCondition.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(CheckCondition checkCondition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_CK_CONDITION (ID,F_NAME,F_ORDER,F_TABLE_TEMP_ID)");
		sql.append(" values (:ID,:F_NAME,:F_ORDER,:F_TABLE_TEMP_ID)");
		Map map = new HashMap();
		map.put("ID", checkCondition.getId());
		map.put("F_NAME", checkCondition.getName());
		map.put("F_ORDER", checkCondition.getOrder());
		map.put("F_TABLE_TEMP_ID", checkCondition.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_CK_CONDITION where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(F_ORDER) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	public List<CheckCondition> getByTempId(String tempId){
		List<CheckCondition> checkConditionList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TABLE_TEMP_ID", tempId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			CheckCondition ck = new CheckCondition(map);
			checkConditionList.add(ck);
		}
		return checkConditionList;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteByTempId(String id) {
		String sql = "delete from W_CK_CONDITION where F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID";
		Map map = new HashMap();
		map.put("F_TABLE_TEMP_ID", id);
		this.jdbcDao.exesql(sql, map);
	}
}
