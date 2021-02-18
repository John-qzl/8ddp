package com.cssrc.ibms.dp.product.acceptance.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;

/**
 * @description 产品验收组数据库操作类
 * @author xie chen
 * @date 2019年12月11日 下午7:04:54
 * @version V1.0
 */
@Repository
public class AcceptanceGroupDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource 
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private WorkBoardDao workBoardDao;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	/**
	 * @Desc 添加组长
	 * @param map
	 */
	public void addGroupManager(Map<String, Object> map) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO w_cpyszb (ID,F_ZW,F_XM,F_XMID,F_SSCP,F_DW,F_DWID)");
		sql.append(" VALUES ("+UniqueIdUtil.genId()+",'"+map.get("position")+"','"+map.get("userName")+"',");
		sql.append("'"+map.get("userId")+"','"+map.get("acceptancePlanId")+"','"+map.get("sysOrgName")+"','"+map.get("sysOrgId")+"')");
		jdbcTemplate.execute(sql.toString());
	}

	/**
	 * @Desc 靶场策划添加组长
	 * @param userName	组长姓名
	 * @param userId	组长id
	 * @param ofPlanId	靶场策划id
	 * @param dep	部门名称
	 * @param depId	部门id
	 */
	public void addRangeTestPlanGroupManager(String userName,String userId,String ofPlanId,String dep,String depId) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO w_cpyszb (ID,F_ZW,F_XM,F_XMID,F_SSBCCH,F_DW,F_DWID,F_FZXM)");
		sql.append(" VALUES ("+UniqueIdUtil.genId()+",'组长','"+userName+"',");
		sql.append("'"+userId+"','"+ofPlanId+"','"+dep+"','"+depId+"','组长')");
		jdbcTemplate.execute(sql.toString());
	}
	
	public void setReport(String id,String status) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE w_cpysbgb SET F_SPZT='"+status+"' WHERE ID='"+id+"'");
		jdbcDao.exesql(sql.toString(), null); 
		if(status.equals("审批通过")) {
			String get="select * from w_cpysbgb where ID="+id;
			Map<String,Object> map=new HashMap<>();
			map=jdbcTemplate.queryForMap(get);
			Long Id=UniqueIdUtil.genId();
			String planId=map.get("F_PLANID").toString();
			String insert="insert into W_FILE_DATA(ID,F_MZ,F_PLANID,F_SJID,F_SJLB) values("+"'"+Id+"'"+","+"'验收报告'"+","+"'"+planId+"'"+","+"'"+id+"'"+","+"'4')";
			jdbcTemplate.execute(insert); 
			if("1".equals(map.get("F_LX"))) {
				acceptancePlanDao.updateBackData(planId);
			}
			workBoardDao.updatework(planId, "报告总结审批通过", "生成PDF归档");
			List<Map<String,Object>> groupList=acceptanceGroupDao.getDataByAcceptancePanId(planId);
			Map<String, Object> acceptancePlan=acceptancePlanDao.getMapById(planId);
			String createPlanUserId=acceptancePlan.get("F_JSFZRID").toString();  //获取策划发起人
			String createPlanUserName=acceptancePlan.get("F_JSFZR").toString();
			Boolean check=false;
			for (Map<String, Object> teamMate : groupList) {  //判断策划流程发起人是否是队伍中的
				
				if(teamMate.get("F_XMID")!=null&&teamMate.get("F_XMID").toString().equals(createPlanUserId)) {
					check=true;
				}
			}
			if(!check) {
				Map<String, Object> teamMateMap=new HashMap<>();
				teamMateMap.put("F_XM", createPlanUserName);
				teamMateMap.put("F_XMID", createPlanUserId);
				teamMateMap.put("F_zw", "发起人");
				groupList.add(teamMateMap);
			}
			for(int i=0;i<groupList.size();i++) {
				acceptanceMessageDao.insertMessageByReport(groupList.get(i), map);
			}
		}
	}

	public List<Map<String,Object>> getDataByAcceptancePanId(String acceptancePlanId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from W_CPYSZB where F_SSCP="+acceptancePlanId);
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql.toString());
		return list;
	}

	public List<String> getListByAcceptancePanId(String acceptancePlanId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from W_CPYSZB where F_SSCP="+acceptancePlanId);
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql.toString());
		List<String> list=new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			if(map.get("F_XMID")!=null&&!map.get("F_XMID").equals("")) {
				list.add(map.get("F_XMID").toString());
			}
		}
		return list;
	}

	public List<AcceptanceGroup> getByAcceptancePanId(String acceptancePlanId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from W_CPYSZB where F_SSCP="+acceptancePlanId);
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql.toString());
		List<AcceptanceGroup> acceptanceGroupList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			if(map!=null) {
				acceptanceGroupList.add(new AcceptanceGroup(map));
			}
		}	
		return acceptanceGroupList;
	}
	
	public void insert(AcceptanceGroup acceptanceGroup) {
		String  sql = SqlHelp.getInsertSql(AcceptanceGroup.class, "w_cpyszb");
		Map<String, Object> map = MapUtil.transBean2Map(acceptanceGroup);
		jdbcDao.exesql(sql, map);
	}
	
	public void updateSign(String acceptanceGroupId,String signId) {
		String sql="update W_CPYSZB set F_QSID='"+signId+"' where ID='"+acceptanceGroupId+"'";
		jdbcDao.exesql(sql, null);
		
	}

	/**
	 * 根据靶场策划id查组员信息  返回组员类
	 * @param missionId
	 * @return
	 */
    public List<AcceptanceGroup> getByMissionId(String missionId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from W_CPYSZB where F_SSBCCH="+missionId);
		List<Map<String,Object>> list=jdbcTemplate.queryForList(sql.toString());
		List<AcceptanceGroup> acceptanceGroupList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			if(map!=null) {
				acceptanceGroupList.add(new AcceptanceGroup(map));
			}
		}
		return acceptanceGroupList;
    }

	/**
	 * 根据靶场策划id查组员信息  返回mapList
	 * @param missionId
	 * @return
	 */
	public List<Map<String,Object>> getByMissionIdToMap(String missionId) {
		Map<String, Object> sqlMap = new HashMap<>();
		sqlMap.put("missionId", missionId);
		String sql = "select * from W_CPYSZB where F_SSBCCH=:missionId";
		List<Map<String, Object>> sqlResult = jdbcDao.queryForList(sql, sqlMap);
		if (sqlResult.size() == 0) {
			return sqlResult;
		}
		return sqlResult;
	}

	public AcceptanceGroup getById(String Id) {
		String sql="select * from W_CPYSZB where ID="+Id;
		Map<String,Object> map=jdbcDao.queryForMap(sql,null);
		if (map==null){
			return null;
		}else {
			AcceptanceGroup acceptanceGroup=new AcceptanceGroup(map);
			return acceptanceGroup;
		}
	}

	public void deleteByPlanId(String planId) {
		Map<String, Object> param=new HashMap<>();
		param.put("sscp", planId);
		String sql="delete from W_CPYSZB where F_SSCP=:sscp";
		jdbcDao.exesql(sql, param);
	}

	/**
	 * 更新数据信息
	 * @param acceptanceGroup
	 */
	public void update(AcceptanceGroup acceptanceGroup) {
		String  sql = SqlHelp.getUpdateSql(AcceptanceGroup.class, "w_cpyszb");
		Map<String, Object> map = MapUtil.transBean2Map(acceptanceGroup);
		jdbcDao.exesql(sql, map);
	}
}


