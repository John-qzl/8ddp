package com.cssrc.ibms.core.resources.io.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.template.Cell;
@Repository
public class IOCellDao {
	@Resource
	private JdbcDao jdbcDao;

	public Cell getById(String id) {
		String sql = " select * from W_CELL where id='"+id+"'";	
		Map<String,Object> map = jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}else {
			return new Cell(map);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void update(Cell cell) {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_CELL set ");
		sql.append(" F_CONTENT=:content,F_RNUMBER=:rnumber,F_IFRESULT=:ifresult,");
		sql.append(" F_RESULTTYPE=:resulttype,F_ITEMDEF_ID=:itemdeid,F_HEADER_ID=:header_id,");
		sql.append(" F_TABLE_TEMP_ID=:table_temp_id ");		
		sql.append(" where id=:id");
		Map map = new HashMap();
		map.put("id", cell.getId());
		map.put("content", cell.getContent());
		map.put("rnumber", cell.getRnumber());
		map.put("ifresult", cell.getIfresult());
		map.put("resulttype", cell.getResulttype());
		map.put("itemdeid", cell.getItemdeid());
		map.put("header_id", cell.getHeader_id());
		map.put("table_temp_id", cell.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(Cell cell) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_CELL (ID,F_CONTENT,F_RNUMBER,F_IFRESULT,F_RESULTTYPE,F_ITEMDEF_ID,F_HEADER_ID,F_TABLE_TEMP_ID)");
		sql.append(" values (:ID,:F_CONTENT,:F_RNUMBER,:F_IFRESULT,:F_RESULTTYPE,:F_ITEMDEF_ID,:F_HEADER_ID,:F_TABLE_TEMP_ID)");
		Map map = new HashMap();
		map.put("ID", UniqueIdUtil.genId());
		map.put("F_CONTENT", cell.getContent());
		map.put("F_RNUMBER", cell.getRnumber());
		map.put("F_IFRESULT", cell.getIfresult());
		map.put("F_RESULTTYPE", cell.getResulttype());
		map.put("F_ITEMDEF_ID", cell.getItemdeid());
		map.put("F_HEADER_ID", cell.getHeader_id());
		map.put("F_TABLE_TEMP_ID", cell.getTable_temp_id());
		jdbcDao.exesql(sql.toString(), map);
		return (Long)map.get("ID");
	}
}
