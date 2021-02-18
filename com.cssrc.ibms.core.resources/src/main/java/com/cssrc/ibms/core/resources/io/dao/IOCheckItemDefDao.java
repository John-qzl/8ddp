package com.cssrc.ibms.core.resources.io.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;


import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.CheckItemDef;
@Repository
public class IOCheckItemDefDao {
	@Resource
	private JdbcDao jdbcDao;

	public CheckItemDef getById(String id) {
		String sql = " select * from W_ITEMDEF where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new CheckItemDef(map);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(CheckItemDef checkItemDef) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_ITEMDEF set ");
		sql.append(" F_NAME=:F_NAME,F_SHORTNAME=:F_SHORTNAME,F_TYPE=:F_TYPE,");
		sql.append(" F_DESCRIPTION=:F_DESCRIPTION,F_ILDD=:F_ILDD,F_IILDD=:F_IILDD,");
		sql.append(" F_YCN=:F_YCN,F_NJLJYQ=:F_NJLJYQ,F_ZHYCDZ=:F_ZHYCDZ,");
		sql.append(" F_IFMEDIA=:F_IFMEDIA,F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID ");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", checkItemDef.getId());
		map.put("F_NAME", checkItemDef.getName());
		map.put("F_SHORTNAME", checkItemDef.getShortname());
		map.put("F_TYPE", checkItemDef.getType());
		map.put("F_DESCRIPTION", checkItemDef.getDescription());
		map.put("F_ILDD", checkItemDef.getIldd());
		map.put("F_IILDD", checkItemDef.getIildd());
		map.put("F_YCN", checkItemDef.getYcn());
		map.put("F_NJLJYQ", checkItemDef.getNjljyq());
		map.put("F_ZHYCDZ", checkItemDef.getZhycdz());
		map.put("F_IFMEDIA", checkItemDef.getIfmedia());
		map.put("F_TABLE_TEMP_ID", checkItemDef.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(CheckItemDef checkItemDef) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_ITEMDEF (ID,F_NAME,F_SHORTNAME,F_TYPE,F_DESCRIPTION,F_ILDD,F_IILDD,F_YCN,F_NJLJYQ,F_ZHYCDZ,F_IFMEDIA,F_TABLE_TEMP_ID)");
		sql.append(" values (:ID,:F_NAME,:F_SHORTNAME,:F_TYPE,:F_DESCRIPTION,:F_ILDD,:F_IILDD,:F_YCN,:F_NJLJYQ,:F_ZHYCDZ,:F_IFMEDIA,:F_TABLE_TEMP_ID)");
		Map map = new HashMap();
		map.put("ID",checkItemDef.getId());
		map.put("F_NAME", checkItemDef.getName());
		map.put("F_SHORTNAME", checkItemDef.getShortname());
		map.put("F_TYPE", checkItemDef.getType());
		map.put("F_DESCRIPTION", checkItemDef.getDescription());
		map.put("F_ILDD", checkItemDef.getIldd());
		map.put("F_IILDD", checkItemDef.getIildd());
		map.put("F_YCN", checkItemDef.getYcn());
		map.put("F_NJLJYQ", checkItemDef.getNjljyq());
		map.put("F_ZHYCDZ", checkItemDef.getZhycdz());
		map.put("F_IFMEDIA", checkItemDef.getIfmedia());
		map.put("F_TABLE_TEMP_ID", checkItemDef.getTable_temp_id());
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
		sql.append(" select * from W_ITEMDEF where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(ID) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	@SuppressWarnings({"rawtypes","unchecked"} )
	public List<CheckItemDef> getByTempId(String tempId){
		List<CheckItemDef> checkItemDefList = new ArrayList();
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_TABLE_TEMP_ID", tempId);
		List<Map<String,Object>> list = this.query(keyValueMap);
		for(Map<String,Object> map : list) {
			CheckItemDef cf = new CheckItemDef(map);
			checkItemDefList.add(cf);
		}
		return checkItemDefList;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteByTempId(String id) {
		String sql = "delete from W_ITEMDEF where F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID";
		Map map = new HashMap();
		map.put("F_TABLE_TEMP_ID", id);
		this.jdbcDao.exesql(sql, map);
	}
}
