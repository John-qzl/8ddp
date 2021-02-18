package com.cssrc.ibms.dp.product.acceptance.dao;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.flow.dao.ProcessRunDao;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;

import com.cssrc.ibms.core.util.common.MapUtil;

/**
 * @description 产品验收策划数据库操作类
 * @author xie chen
 * @date 2019年12月11日 下午6:21:29
 * @version V1.0
 */
@Repository
public class AcceptancePlanDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private ProcessRunDao processRunDao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	
	/**
	 * @Desc 更新验收策划审批状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateApproveStatus(String acceptancePlanId, String status) {
		String sql = "UPDATE w_cpyschbgb SET F_SPZT='"+status+"' WHERE ID="+acceptancePlanId+"";
		jdbcTemplate.execute(sql);
	}
	/**
	 * @Desc 更新验收策划归档状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateFileStatus(String acceptancePlanId, String status) {
		String sql = "UPDATE w_cpyschbgb SET F_GDLCZT='"+status+"' WHERE ID="+acceptancePlanId+"";
		jdbcTemplate.execute(sql);
	}
	/**
	 * @Desc 校验策划编号是否重复
	 * @param acceptancePlanId
	 * @param acceptancePlanId
	 */
	public String checkPlanNumber(String acceptancePlanId){
		String sql = "SELECT * FROM w_cpyschbgb WHERE F_chbgbbh='"+acceptancePlanId+"'";
		List<Map<String, Object>> list=jdbcDao.queryForList(sql, null);
		if(list!=null&&list.size()>0) {
			return String.valueOf(list.size());
		}
		return "0";
	}
	/**
	 * @Desc 更新验收策划归档状态
	 * @param businessKey
	 * @param status
	 */
	public void updateFileDataStatus(String businessKey, String status) {
		String sql = "UPDATE w_cpyschbgb SET F_GDLCZT='"+status+"' WHERE ID="+businessKey+"";
		jdbcTemplate.execute(sql);
	}
	/**
	 * @Desc 获取产品验收策划信息map
	 * @param acceptancePlanId
	 * @return
	 */
	public Map<String, Object> getMapById(String acceptancePlanId) {
		String sql = "SELECT * FROM w_cpyschbgb WHERE ID='"+acceptancePlanId+"'";
		return jdbcDao.queryForMap(sql, null);

	}

	/**
	 * @Desc 获取产品验收策划信息map
	 * @param acceptancePlanId
	 * @return
	 */
	public AcceptancePlan getBeanById(String acceptancePlanId) {
		String sql = "SELECT * FROM w_cpyschbgb WHERE ID='"+acceptancePlanId+"'";
		Map<String,Object> map=jdbcDao.queryForMap(sql, null);
		AcceptancePlan acceptancePlan=null;
		try {
			acceptancePlan=new AcceptancePlan(map);
		} catch (ParseException e) {
			System.out.println("查询产品验收策划后将策划从hashmap转为bean时出错");
			e.printStackTrace();
		}
		return acceptancePlan;

	}

	/**
	 * @Desc 获取策划下面的报告
	 * @param acceptancePlanId
	 * @return
	 */
	public Map<String, Object> getReportById(String acceptancePlanId) {
		String sql = "SELECT * FROM w_cpysbgb WHERE ID='"+acceptancePlanId+"'";
		return jdbcDao.queryForMap(sql, null);

	}
	
	/**
	 * @Desc 获取产品验收策划信息map
	 * @param id
	 * @return
	 */
	public List<Map<String,Object>> getMapByPc(String id) {
		String sql = "SELECT * FROM w_cpyschbgb WHERE F_SSCPPC="+id+"";
		return jdbcDao.queryForList(sql,null);
	}

	/**
	 * @Desc 获取产品批次下所有的策划
	 * @return
	 */
	public List<Map<String, Object>> getPlansByProductBatchId(String productBatchId){
		String sql="SELECT * FROM W_CPYSCHBGB WHERE F_SSCPPC='"+productBatchId+"' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	/**
	 * @Desc 绑定生成的pdf
	 * @param id
	 * @return
	 */
	public void updateFile(String id,String context) {
		String sql="update w_cpyschbgb set F_gdwj="+"'"+context+"'"+" where ID="+"'"+id+"'";
		jdbcDao.exesql(sql, null);
	}
	
	
	/**
	 * @Desc 获取产品批次下所有审批通过的策划
	 * @return
	 */
	public List<Map<String, Object>> getApprovedPlansByProductBatchId(String productBatchId){
		String sql="SELECT * FROM W_CPYSCHBGB WHERE F_SSCPPC='"+productBatchId+"' AND F_SPZT='审批通过' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	/**
	 * @Desc 获取所有策划数据
	 * @return
	 */
	public List<Map<String, Object>> getAllAcceptancePlan(){
		String sql="SELECT * FROM W_CPYSCHBGB where F_SPZT='审批通过'";
		return jdbcDao.queryForList(sql,null);
	}
	/**
	 * @Desc 更新验收策划-依据文件下发状态
	 * @param acceptancePlanId
	 * @param status
	 */
	public void updateFilesAssignStatus(String acceptancePlanId, String status) {
		String sql = "UPDATE w_cpyschbgb SET F_SFYXFYJWJ='"+status+"' WHERE ID="+acceptancePlanId+"";
		jdbcTemplate.execute(sql);
	}
	/**
	 * @Desc 验收策划审批通过向归档表中插入数据
	 * @param data

	 */
	public void insertfiledata(Map<String,Object> data) {
		Long id=UniqueIdUtil.genId();
		String planId=data.get("ID").toString();
		String name=(String) data.get("F_CHBGBBH");
		String filePath=(String) data.get("F_YSYJWJ");
		String sql="insert into W_FILE_DATA values( "+id+", "+"'"+name+"', "+planId+", "+planId+",'1',"+"'"+filePath+"'"+")";
		jdbcTemplate.execute(sql);
	}
	/**
	 * @Desc 验验收报告向归档表中插入数据
	 * @param data

	 */
	public void insertReportfiledata(Map<String,Object> data) {
		Long id=UniqueIdUtil.genId();
		String planId=(String)data.get("F_PLANID");
		String dataId=data.get("ID").toString();
		String name="验收报告";
		String sql="insert into W_FILE_DATA values( "+id+", "+"'"+name+"', "+planId+", "+dataId+",'4',"+"'"+""+"'"+")";
		jdbcTemplate.execute(sql);
	}
	public Map<String,Object> getReportInfo(String id){
		String sql="SELECT * FROM W_CPYSBGB WHERE ID='"+id+"'";
		return jdbcTemplate.queryForMap(sql);
	}
	
	public void UpdateStatus(String id) {
		String sql="update w_cpyschbgb set F_GDLCZT='已生成PDF' where ID='"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	public void deleteById(String id){
		String sql="delete from W_CPYSBGB where F_ID='"+id+"'";
		jdbcTemplate.execute(sql);
	}
	
	public void insert(AcceptancePlan acceptancePlan) {
		String  sql = SqlHelp.getInsertSql(AcceptancePlan.class, "w_cpyschbgb");
		Map<String, Object> map = MapUtil.transBean2Map(acceptancePlan);
		jdbcDao.exesql(sql, map);
	}
	
	public void updateReportData(AcceptancePlan acceptancePlan) {
		Map<String, Object> param=new HashMap<>();
		param.put("F_YSBGHCSJ", acceptancePlan.getYsbghcsj());
		param.put("ID",acceptancePlan.getId());
		String  sql="update W_CPYSCHBGB set  F_YSBGHCSJ=:F_YSBGHCSJ where ID=:ID";
		jdbcDao.exesql(sql, param);
	}
	
	public void updateBackData(String planId) {
		String sql="update W_CPYSCHBGB set F_ysbghcsj='{}' where ID='"+planId+"'";
		jdbcDao.exesql(sql, null);
	}
	public List<Map<String, Object>> getAllBCAcceptancePlan(){
		String sql="SELECT * FROM W_BCRWCHBGB where F_SPZT='审批通过'";
		return jdbcDao.queryForList(sql,null);
	}
	public void updateBackData(String planId,String backData) {
		String sql="update W_CPYSCHBGB set F_ysbghcsj='"+backData+"' "+"where ID='"+planId+"'";
		jdbcDao.exesql(sql, null);
	}
	/**
	 * @Desc 删除数据时同时删除草稿
	 * @param Ids
	 */
	public void topDelete(String Ids) {
		String[] idArry=Ids.split(",");
		for (String string : idArry) {
			Map<String,Object> map=this.getMapById(string);
			String status=map.get("F_SPZT").toString();
			if(status.equals("待审批")) {
				processRunDao.deleteFormData(Long.valueOf("10000030540034"), string);
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
		String sql="delete from w_cpyschbgb where ID=:ID";
		jdbcDao.exesql(sql, param);
		acceptanceGroupDao.deleteByPlanId(id);
	}

	/**
	 * 根据型号查找所有属于这个型号的策划
	 * @param moduleId
	 * @return
	 */
	public List<Map<String,Object>> getPlanListByModuleId(String moduleId){
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("moduleId",moduleId);
		String sql="select *  FROM W_CPYSCHBGB where F_SSXHID=:moduleId";
		List<Map<String,Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
		return resMapList;
	}
	
	
	/**
	 * 根据型号查找所有属于这个型号的策划并已完成
	 * @param moduleId
	 * @return
	 */
	public List<Map<String,Object>> getByModuleId(String moduleId){
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("moduleId",moduleId);
		String sql="select *  FROM W_CPYSCHBGB where F_SSXHID=:moduleId and F_SPZT='审批通过'";
		List<Map<String,Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
		return resMapList;
	}
	/**
	 * @Description
	 * @Author ZMZ
	 * @Date 2020/12/4 9:00
	 * @param bussinessKey
	 * @Return void
	 */
    public void updateEndTime(String bussinessKey, Date endTime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	Map<String,Object> sqlMap=new HashMap<>();
    	sqlMap.put("id",bussinessKey);
    	String sql="update W_CPYSCHBGB set F_YSJSSJ=to_date('"+sdf.format(endTime)+"','yyyy-mm-dd')"
				+"where id=:id";
    	jdbcDao.exesql(sql,sqlMap);
    }
	/**
	 * @Description 为当前策划添加归档时间
	 * @Author ZMZ
	 * @Date 2020/12/25 10:22
	 * @param acceptancePlanId
     * @param pdfFileTime
	 * @Return void
	 */
	public void updatePdfFileTime(String acceptancePlanId, Date pdfFileTime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("id",acceptancePlanId);
		String sql="update W_CPYSCHBGB set F_GDSJ=to_date('"+sdf.format(pdfFileTime)+"','yyyy-mm-dd')"
				+"where id=:id";
		jdbcDao.exesql(sql,sqlMap);
	}
}


