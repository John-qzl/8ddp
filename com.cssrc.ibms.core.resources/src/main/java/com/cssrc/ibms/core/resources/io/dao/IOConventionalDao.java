package com.cssrc.ibms.core.resources.io.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.sync.bean.Conventional;

@Repository
public class IOConventionalDao {
	@Resource
	private JdbcDao jdbcDao;
	
	public void insert(Conventional conventional) {
		String  sql = SqlHelp.getInsertSql(Conventional.class, "w_cgxyssj");
		Map<String, Object> map = MapUtil.transBean2Map(conventional);
		jdbcDao.exesql(sql, map);
	}
	public List<Conventional> getByPlanId(String planId){
		String sql="select * from w_cgxyssj where F_PLANID='"+planId+"'";
		List<Conventional> list=new ArrayList<>();
		List<Map<String, Object>> MapList=jdbcDao.queryForList(sql,null);
		for (Map<String, Object> map : MapList) {
			Conventional conventional=new Conventional(map);
			list.add(conventional);
		}
		return list;
	}
	
	public List<Conventional> getConByPlanId(String planId){
		String sql="select A.* from w_cgxyssj A ";
		sql=sql+"INNER  JOIN W_TB_INSTANT B ON A.F_SLID = B.ID where ";
		sql=sql+"A.F_PLANID = '" + planId + "' and  B.F_STATUS!='废弃' ORDER BY A.ID ";
		List<Conventional> list=new ArrayList<>();
		List<Map<String, Object>> MapList=jdbcDao.queryForList(sql,null);
		for (Map<String, Object> map : MapList) {
			Conventional conventional=new Conventional(map);
			list.add(conventional);
		}
		return list;
	}
	
	public Conventional getById(String id) {
		String sql="select * from w_cgxyssj where ID='"+id+"'";
		Map<String, Object> map=jdbcDao.queryForMap(sql,null);
		if(map!=null) {
			Conventional conventional=new Conventional(map);
			return conventional;
		}
		return null;
	}
	
	public void update(Conventional conventional) {
		String  sql = SqlHelp.getUpdateSql(Conventional.class, "w_cgxyssj");
		Map<String, Object> map = MapUtil.transBean2Map(conventional);
		jdbcDao.exesql(sql, map);
	}
}
