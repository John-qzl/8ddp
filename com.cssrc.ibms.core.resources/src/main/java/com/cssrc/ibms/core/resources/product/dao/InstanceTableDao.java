package com.cssrc.ibms.core.resources.product.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
/**
 * @description 实例
 * @author fuyong
 * @date 2020年06月03日 下午3:53:26
 * @version V1.0
 */
@Repository
public class InstanceTableDao {
	
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	public Map<String,Object> getInstanceHTML(String id) {
		String sql="SELECT * FROM W_TB_INSTANT WHERE ID = '" + id + "'";
		return jdbcDao.queryForMap(sql, null);
	}
	public List<Map<String, Object>> getInstanceByPlnanId(String planId){
		String sql="select * from W_TB_INSTANT WHERE F_PLANID ='" + planId + "'";
		return jdbcDao.queryForList(sql, null);
	}
	
	public void updateStatus(String id,String status) {
		String sql="update W_TB_INSTANT set F_STATUS='"+status+"' where ID='"+id+"'";
		jdbcDao.exesql(sql, null);
		
	}

}
