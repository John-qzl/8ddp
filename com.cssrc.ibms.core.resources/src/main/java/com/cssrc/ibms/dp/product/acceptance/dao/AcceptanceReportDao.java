package com.cssrc.ibms.dp.product.acceptance.dao;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
/**
 * @description 产品验收报告数据库操作类
 * @author fu yong
 * @date 2020年7月11日 下午4:04:54
 * @version V1.0
 */
@Repository
public class AcceptanceReportDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	
	public List<acceptanceReport> getByPlanId(String planId){
		String sql = "select * from w_cpysbgb  WHERE F_PLANID="+planId+"";
		List<Map<String,Object>> mapList=new ArrayList<>();
		mapList=jdbcTemplate.queryForList(sql);
		List<acceptanceReport> acceptanceReportList=new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			if(map!=null) {
				acceptanceReportList.add(new acceptanceReport(map));
			}
		}
		return acceptanceReportList;
	}
	public Map<String, Object> getById(String acceptancecReportId){
		String sql="select * from w_cpysbgb where ID='"+acceptancecReportId+"'";
		return jdbcDao.queryForMap(sql, null);
		
	}

	public void updataStatus(String id,String status) {
		String sql="update w_cpysbgb set F_SPZT='"+status+"' where ID='"+id+"'";
		jdbcDao.exesql(sql, null);
	}
	public List<Map<String, Object>> getAcceptanceReport(String acceptancePlanId){
		String sql="select * from W_CPYSBGB where F_PLANID='"+acceptancePlanId+"'";
		return jdbcDao.queryForList(sql, null);
	}
	
	public void updateReportProduct(String acceptancecReportId,String productName,String productNumber) {
		String sql="update W_CPYSBGB set F_YSCPSL='"+productNumber+"',F_YSCPBH='"+productName+"' where ID='"+acceptancecReportId+"'";
		jdbcDao.exesql(sql, null);
	}

	/**
	 * 统计一个型号下有多少个报告
	 * @param xhId
	 * @return
	 */
	public Integer countSummaryBroupBySSXH(String xhId){
		String sql="SELECT * FROM W_CPYSBGB WHERE F_SSXHID=:xhId";
		Map<String,Object> map=new HashMap<>();
		map.put("xhId",xhId);
		Integer countSummary=jdbcDao.queryForList(sql,map).size();
		return countSummary;
	}

	/**
	 * 把当前的总结id的通过时间改为现在
	 * @param recorderId
	 */
	public void updatePassTimeForCPYS(String recorderId){
		String sql="update W_CPYSBGB set F_ZJTGSJ=:timeForNow where ID=:recorderId";
		Map<String, Object> map = new HashMap<>();
		map.put("recorderId", recorderId);
		Calendar calendar= Calendar.getInstance();
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		map.put("timeForNow",dateFormat.format(calendar.getTime()));
		jdbcDao.exesql(sql,map);
	}
	
}
