package com.cssrc.ibms.dp.product.acceptance.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.ActualData;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;
/**
 * @description 验收数据表数据库操作类
 * @author fu yong
 * @date 2020年7月10日 下午2:00:54
 * @version V1.0
 */
@Repository
public class ActualDataDao {
	
	@Resource
	private JdbcDao jdbcDao;
	
	public ActualData getById(String id) {
		String sql="select * from W_YSSJB where ID='"+id+"'";
		Map<String,Object> map=jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}
		return new ActualData(map);
	}
	
	
	public List<ActualData> getByPlanId(String planId) {
		String sql="select * from W_YSSJB where F_SSCP='"+planId+"'";
		List<Map<String,Object>> list=jdbcDao.queryForList(sql, null);
		if(list==null) {
			return null;
		}
		List<ActualData> actualDataList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			actualDataList.add(new ActualData(map));
		}
		return actualDataList;
	}
	
	public List<Map<String, Object>> getByProductId(String productId,String project){
		String sql="select * from W_CPB LEFT JOIN W_YSSJB on W_CPB.ID=W_YSSJB.F_SSCP where  F_YSXM=:project and W_CPB.F_CPMC!='/' and W_CPB.ID in("+productId+")";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("project", project);
		map.put("productId", productId);
		return jdbcDao.queryForList(sql,map);
	}
	public List<Map<String, Object>> getProjectTitle(String productId){
		String sql="SELECT DISTINCT F_YSXM from W_YSSJB where F_YSXM!='/' and F_YSXM is not null and F_SSCP in("+productId+")";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("productId", productId);
		return jdbcDao.queryForList(sql,map);
	}
	
	
	
	
	
	public void insert(ActualData actualData) {
		String  sql = SqlHelp.getInsertSql(ActualData.class, "W_YSSJB");
		Map<String, Object> map = MapUtil.transBean2Map(actualData);
		jdbcDao.exesql(sql, map);
	}
	public void update(ActualData actualData) {
		String  sql = SqlHelp.getUpdateSql(ActualData.class, "W_YSSJB");
		Map<String, Object> map = MapUtil.transBean2Map(actualData);
		jdbcDao.exesql(sql, map);
	}
	
}
