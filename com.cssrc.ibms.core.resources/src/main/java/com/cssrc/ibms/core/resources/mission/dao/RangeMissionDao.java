package com.cssrc.ibms.core.resources.mission.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

@Repository
public class RangeMissionDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	/**
	 * 根据型号id获取任务信息
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getRangeMissionByModuleId(String moduleId) {
		String sql = "SELECT ID,F_RWMC,F_RWDH,F_SSXH,F_ZRBM,F_ZRBMID,F_SYDW,F_SYDWID FROM W_BCSYRWB WHERE F_SSXH="+moduleId+"ORDER BY ID"; 
		return jdbcDao.queryForList(sql, null);
	}
	/**
	 * 插入单条excel来的信息
	 * @param missionInfo
	 */
	public void singleMissionInsert(Map<String, Object> missionInfo) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO w_bcrwchbgb (ID,F_XHDH,F_SYRWMC,F_WQXTBH,F_DDBH,F_SJDD,F_KSSJ,F_JSSJ,F_SYFZR,F_SYFZRID,F_XHID,F_SPZT)");
		sql.append(" VALUES ("+UniqueIdUtil.genId()+",'"+missionInfo.get("所属型号代号")+"','"+missionInfo.get("任务名称")+"',");
		sql.append("'"+missionInfo.get("武器系统编号")+"','"+missionInfo.get("导弹编号")+"','"+missionInfo.get("所检地点")+"',");
		sql.append("'"+missionInfo.get("开始时间")+"','"+missionInfo.get("结束时间")+"','"+missionInfo.get("试验负责人")+"',");
		sql.append(missionInfo.get("试验负责人ID")+","+missionInfo.get("所属型号ID")+","+"'未审批'");
		sql.append(")");
		jdbcTemplate.execute(sql.toString());
		
	}

	/**
	 * 这个是总结表单要的信息,不知道具体想要什么
	 * @param missionId
	 * @return
	 */
	public String getCpMc(String missionId) {

		/*String sql = "SELECT F_CPMC FROM W_CPB WHERE F_PLANID = "+planId;*/
		/*String sql="select A.*,B.* from W_CPB A ";
		sql=sql+"INNER  JOIN W_TB_INSTANT B ON A.F_SSSLID = B.ID where ";
		sql=sql+"A.F_PLANID = '" + planId + "' and  B.F_STATUS!='废弃'";
		Map params = new HashMap();
		params.put("planId", planId);
		List<Map<String, Object>> dataList = jdbcDao.queryForList(sql.toString(), null);
		String cpmc="";
		for (Map<String, Object> map : dataList) {
			cpmc+=map.get("F_CPMC")+",";
		}
		if(!cpmc.equals("")) {
			cpmc=cpmc.substring(0,cpmc.length()-1);
		}
		return cpmc ;*/
		return null;
	}
}
