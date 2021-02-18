package com.cssrc.ibms.core.resources.product.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

@Repository
public class HelpFileDao {
	
	@Resource
	private JdbcDao jdbcDao;
	
	
	public List<Map<String, Object>> getAllHelpFile(){
		String sql="select * from w_syscb";
		List<Map<String, Object>> list=new ArrayList<>();
		Map<String, Object> map=new HashMap<String, Object>();
		return jdbcDao.queryForList(sql, map);
	}
}
