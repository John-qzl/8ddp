package com.cssrc.ibms.core.resources.io.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.Header;
@Repository
public class IOHeaderDao {
	@Resource
	private JdbcDao jdbcDao;

	public Header getById(String id) {
		String sql = " select * from W_HEADER where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new Header(map);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(Header header) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_HEADER set ");
		sql.append(" F_NAME=:F_NAME,F_ORDER=:F_ORDER,F_TABLE_TEMP_ID=:F_TABLE_TEMP_ID");
		sql.append(" where id=:ID");
		Map map = new HashMap();
		map.put("ID", header.getId());
		map.put("F_NAME", header.getName());
		map.put("F_ORDER", header.getOrder());
		map.put("F_TABLE_TEMP_ID", header.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(Header header) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_HEADER (ID,F_NAME,F_ORDER,F_TABLE_TEMP_ID)");
		sql.append(" values (:ID,:F_NAME,:F_ORDER,:F_TABLE_TEMP_ID)");
		Map map = new HashMap();
		map.put("ID", UniqueIdUtil.genId());
		map.put("F_NAME", header.getName());
		map.put("F_ORDER", header.getOrder());
		map.put("F_TABLE_TEMP_ID", header.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);
		return (Long)map.get("ID");
	}
}
