package com.cssrc.ibms.core.resources.mission.dao;
import java.text.ParseException;
import java.util.*;

import javax.annotation.Resource;

import com.cssrc.ibms.core.flow.dao.ProcessRunDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.mission.model.RangeTestPlanMapToBean;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
/**
 * 靶场试验策划报告表dao
 * @author zmz
 *
 */
@Repository
public class TestPlanDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ProcessRunDao processRunDao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;

	/**
	 * 根据任务id(ssrw)获取model
	 * @param moduleName
	 * @return
	 */
	public List<Map<String, Object>> getApprovedPlansByModuleName(
			String moduleName) {
		String sql = "SELECT * FROM W_BCRWCHBGB WHERE F_XHDH='"+moduleName+"' AND F_SPZT='审批通过'  ORDER BY ID ";

		return jdbcDao.queryForList(sql,null);
	}

	public RangeTestPlanMapToBean getById(String planId) throws ParseException {
		String sql="SELECT * FROM W_BCRWCHBGB where id=:planId";
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("planId",planId);
		Map<String, Object> resMap=jdbcDao.queryForMap(sql,sqlMap);
		if (resMap==null){
			return new RangeTestPlanMapToBean();
		}else {
			return new RangeTestPlanMapToBean(resMap);
		}
	}
	public List<RangeTestPlanMapToBean> getByMissileId(String missileId) throws ParseException {
		String sql="SELECT * FROM W_BCRWCHBGB where f_ddbh=:missileId and f_spzt='审批通过'";
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("missileId", missileId);
		List<Map<String, Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
		List<RangeTestPlanMapToBean> list=new ArrayList<>();
		for (Map<String, Object> map : resMapList) {
			list.add(new RangeTestPlanMapToBean(map));
		}
		return list;
	}

	/**
	 * 获取所有通过审批的靶场试验
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getApprovedPlansByModuleId(String moduleId) {

		String sql = "SELECT ID,F_XHDH,F_SYRWMC,F_WQXTBH,F_DDBH,F_SJDD,F_KSSJ,F_JSSJ,F_SYFZR,F_SYFZRID,F_CHBGBBH FROM W_BCRWCHBGB WHERE F_XHID="+moduleId+" AND F_SPZT='审批通过' AND F_CHBGBBH LIKE '%BCSY%' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}

	/**
	 * 获取所有通过审批的武器所检
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getApprovedWeaponCheckPlansByModuleId(String moduleId) {

		String sql = "SELECT * FROM W_BCRWCHBGB WHERE F_XHID="+moduleId+" AND F_SPZT='审批通过' and F_CHBGBBH LIKE '%WQSJ%'  ORDER BY ID ";


		return jdbcDao.queryForList(sql,null);
	}



	/**
	 * 修改流程状态为指定值
	 * @param acceptancePlanId
	 * @param status
	 */
    public void updateApproveStatus(String acceptancePlanId, String status) {
		String sql = "UPDATE w_bcrwchbgb SET F_SPZT='"+status+"' WHERE ID="+acceptancePlanId+"";
		jdbcTemplate.execute(sql);
    }

    public String getModuleidByPlanid(String acceptancePlanId) {
    	String sql="select F_XHID from W_BCRWCHBGB where ID="+acceptancePlanId;
    	String xhid=jdbcDao.queryForMap(sql,null).get("F_XHID").toString();
    	return xhid;
    }

	/**
	 * 根据id获取记录信息
	 * @param missionId
	 * @return
	 */
	public List<Map<String, Object>> getPlanById(String missionId) {
		String sql = "SELECT * FROM W_BCRWCHBGB WHERE ID="+missionId;
		List<Map<String,Object>> mapList=jdbcDao.queryForList(sql,null);

		if (mapList.size()==0){
			return null;
		}
		return mapList;
    }
	/**
	 * 根据id获取记录信息
	 * @param missionId
	 * @return
	 */
	public Map<String,Object> getPlanByIdToMap(String missionId) {
		String sql = "SELECT * FROM W_BCRWCHBGB WHERE ID="+missionId;
		List<Map<String,Object>> mapList=jdbcDao.queryForList(sql,null);
		if (mapList.size()==0){
			return null;
		}
		Map<String,Object> map=mapList.get(0);
		return map;
	}

	/**
	 * 更新靶场数据确认的流程状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateDataConfirmStatus(String acceptancePlanId, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("acceptancePlanId", acceptancePlanId);
		map.put("status",status);
		//"UPDATE w_bcrwchbgb SET F_SPZT='"+status+"' WHERE ID="+acceptancePlanId+"";
		String sql = "UPDATE w_bcsybgb SET F_SPZT=:status WHERE ID=:acceptancePlanId";
		jdbcDao.exesql(sql,map);
	}

	/**
	 * 更新靶场数据确认的子流程状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateDataConfirmSubprocessStatus(String acceptancePlanId, String status) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("acceptancePlanId", acceptancePlanId);
		map.put("status",status);
		String sql = "UPDATE W_BCSJQRDBB SET F_BLZT=:status WHERE ID=:acceptancePlanId";
		jdbcDao.exesql(sql,map);
	}

	/**
	 * 获取当前型号下靶场策划的条数
	 * @param xhId
	 * @return
	 */
    public Integer countBySSXH(String xhId) {
		String sql="SELECT * FROM W_BCRWCHBGB WHERE W_BCRWCHBGB.F_XHID=:xhId and W_BCRWCHBGB.F_CHBGBBH like '%BCSY%'";
		Map<String,Object> map=new HashMap<>();
		map.put("xhId",xhId);
		Integer countSummary=jdbcDao.queryForList(sql,map).size();
		return countSummary;
    }

	/**
	 * 获取当前型号下武器所检的条数
	 * @param xhId
	 * @return
	 */
	public Integer countBySSXHForWeaponCheck(String xhId) {
		String sql="SELECT * FROM W_BCRWCHBGB WHERE W_BCRWCHBGB.F_XHID=:xhId and W_BCRWCHBGB.F_CHBGBBH like '%WQSJ%'";
		Map<String,Object> map=new HashMap<>();
		map.put("xhId",xhId);
		Integer countSummary=jdbcDao.queryForList(sql,map).size();
		return countSummary;
	}

	/**
	 * 获取武器所检的最新的一个策划编号的id  返回的是已经+1的编号
	 * @param moduleId
	 * @return
	 */
	public String getNextPlanNumber(String moduleId){
		String sql="SELECT * FROM ( SELECT F_CHBGBBH FROM W_BCRWCHBGB WHERE F_XHID="+moduleId
				+"and F_CHBGBBH like '%WQSJ%' ORDER BY ID DESC ) WHERE ROWNUM<=1";
		Map<String, Object> sqlMap=new HashMap<>();
		sqlMap.put("moduleId",moduleId);
		Map<String, Object> resMap=jdbcDao.queryForMap(sql, sqlMap);
		String fullNumber="001";
		//如果检索的结果为空, 直接返回001
		if (resMap==null){
			return fullNumber;
		}
		//如果检索的结果不为空,  返回编号的最后三位+1
		if (resMap!=null){
			fullNumber=resMap.get("F_CHBGBBH").toString();
		}
		String lastThreeNumber=fullNumber.substring(fullNumber.length()-3);
		Integer lastNumber=Integer.valueOf(lastThreeNumber)+1;
		String number=(lastNumber).toString();
		switch (number.length()) {
			case 1:
				number="00"+number;
				break;
			case 2:
				number="0"+number;
				break;
			case 3:
				number=number;
				break;
		}
		return number;
	}

	/**
	 * 获取靶场策划的最新的一个策划编号的id  返回的是已经+1的编号
	 * @param moduleId
	 * @return
	 */
	public String getNextRangeTestPlanNumber(String moduleId){
		String sql="SELECT * FROM ( SELECT F_CHBGBBH FROM W_BCRWCHBGB WHERE F_XHID="+moduleId
				+"and F_CHBGBBH like '%BCSY%' ORDER BY ID DESC ) WHERE ROWNUM<=1";
		Map<String, Object> sqlMap=new HashMap<>();
		sqlMap.put("moduleId",moduleId);
		Map<String, Object> resMap=jdbcDao.queryForMap(sql, sqlMap);
		String fullNumber="001";
		//如果检索的结果为空, 直接返回001
		if (resMap==null){
			return fullNumber;
		}
		//如果检索的结果不为空,  返回编号的最后三位+1
		if (resMap!=null){
			fullNumber=resMap.get("F_CHBGBBH").toString();
		}
		String lastThreeNumber=fullNumber.substring(fullNumber.length()-3);
		Integer lastNumber=Integer.valueOf(lastThreeNumber)+1;
		String number=(lastNumber).toString();
		switch (number.length()) {
			case 1:
				number="00"+number;
				break;
			case 2:
				number="0"+number;
				break;
			case 3:
				number=number;
				break;
		}
		return number;
	}

	/**
	 * @Desc 删除数据时同时删除草稿
	 * @param Ids
	 */
	public void topDelete(String Ids,String ofPart) {
		String[] idArry=Ids.split(",");
		String formKey="";
		if ("BCSY".equals(ofPart)){
			//如果是靶场试验的的
			formKey="10000031890861";
		}else {
			//如果是武器所检的的
			formKey="10000032000037";
		}
		for (String string : idArry) {
			Map<String,Object> map=this.getPlanByIdToMap(string);
			String status=map.get("F_SPZT").toString();
			if(status.equals("未审批")) {
				processRunDao.deleteFormData(Long.valueOf(formKey), string);
				delete(string);
			}
			else {
				delete(string);
			}

		}
	}
	public void delete(String id) {
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("ID", id);
		String sql="delete from W_BCRWCHBGB where ID=:ID";
		jdbcDao.exesql(sql, param);
		acceptanceGroupDao.deleteByPlanId(id);
	}

	/**
	 * @Description 把策划的通过时间改为传来的时间
	 * @Author ZMZ
	 * @Date 2020/12/4 9:43
	 * @param missionId
	 * @param endDate
	 * @Return void
	 */
    public void updateEndTime(String missionId, String endDate) {
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("id",missionId);
		sqlMap.put("endDate",endDate);
		String sql=" update  W_BCRWCHBGB set F_JSSJ=:endDate where id=:id";
		jdbcDao.exesql(sql,sqlMap);
    }
}
