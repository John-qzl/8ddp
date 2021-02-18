package com.cssrc.ibms.core.resources.io.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;
@Repository
public class IOSignDefDao {
	@Resource
	private JdbcDao jdbcDao;

	public SignDef getById(String id) {
		String sql = " select * from W_SIGNDEF where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new SignDef(map);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(SignDef signDef) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_SIGNDEF set ");
		sql.append(" F_NAME=:F_NAME,F_ORDER=:F_ORDER,F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", signDef.getId());
		map.put("F_NAME", signDef.getName());
		map.put("F_ORDER", signDef.getOrder());
		map.put("F_TABLE_TEMP_ID", signDef.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(SignDef signDef) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_SIGNDEF (ID,F_NAME,F_ORDER,F_TABLE_TEMP_ID)");
		sql.append(" values (:ID,:F_NAME,:F_ORDER,:F_TABLE_TEMP_ID)");
		Map map = new HashMap();
		map.put("ID", signDef.getId());
		map.put("F_NAME", signDef.getName());
		map.put("F_ORDER", signDef.getOrder());
		map.put("F_TABLE_TEMP_ID", signDef.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	/**
	 * 查询签署定义表
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_SIGNDEF where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(F_ORDER) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	@SuppressWarnings({"rawtypes","unchecked"})
	public List<SignDef> getByTempId(String tempId){
		List<SignDef> signDefList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TABLE_TEMP_ID", tempId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			SignDef ck = new SignDef(map);
			signDefList.add(ck);
		}
		return signDefList;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteByTempId(String id) {
		String sql = "delete from W_SIGNDEF where F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID";
		Map map = new HashMap();
		map.put("F_TABLE_TEMP_ID", id);
		this.jdbcDao.exesql(sql, map);
	}
}
