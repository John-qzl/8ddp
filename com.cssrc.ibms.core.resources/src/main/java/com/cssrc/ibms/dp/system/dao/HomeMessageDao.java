package com.cssrc.ibms.dp.system.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

@Repository
public class HomeMessageDao {
	@Resource
	JdbcDao jdbcDao;
	
	public List<Map<String, Object>> getSystemMessage(){
		String sql="select * from W_XTGGB order by ID desc";
		List<Map<String, Object>> list=new ArrayList<>();
		list=jdbcDao.queryForList(sql, null);
		return list;	
	}
}
