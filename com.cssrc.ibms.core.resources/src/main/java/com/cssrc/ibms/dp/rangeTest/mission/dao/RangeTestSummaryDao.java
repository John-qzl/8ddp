package com.cssrc.ibms.dp.rangeTest.mission.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.rangeTest.mission.model.RangeTestSummaryMapToBean;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RangeTestSummaryDao {
    @Resource
    private JdbcDao jdbcDao;

    /**
     * 更改审批状态
     * @param summaryId
     * @param status
     */
    public void updateSPZT(String summaryId, String status) {
        //UPDATE W_BCSYBGB SET F_SPZT='123' WHERE id=10000029850502
        String sql="update W_BCSYBGB SET F_SPZT='"+status+"' WHERE id="+summaryId;
        jdbcDao.exesql(sql,null);
    }

    /**
     * 根据策划id查总结, 返回model型
     * @param planId
     * @return
     * @throws ParseException
     */
    public RangeTestSummaryMapToBean getById(String planId) throws ParseException {
		String sql="SELECT * FROM W_BCSYBGB where F_SSCH=:planId";
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("planId",planId);
		Map<String, Object> resMap=jdbcDao.queryForMap(sql,sqlMap);
		if (resMap==null){
			return new RangeTestSummaryMapToBean();
		}else {
			return new RangeTestSummaryMapToBean(resMap);
		}
	}


    /**
     * 根据策划id获取当前数据确认的记录
     * @param missionId
     * @return
     */
    public  Map<String,Object> getConfirmDataByPlanId(String missionId){
        String sql="select * from W_BCSYBGB WHERE F_SSCH=:missionId and F_SPZT!='审批终止'";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("missionId",missionId);
        Map<String,Object> res=jdbcDao.queryForMap(sql,map);
        if (map.size()==0){
            return null;
        }
        return res;
    }
    
    /**
     * 根据策划id获取已经结束的报告表
     * @param missionId
     * @return
     */
    public  Map<String,Object> getReportByPlanId(String missionId){
        String sql="select * from W_BCSYBGB WHERE F_SSCH=:missionId and F_SPZT='审批通过'";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("missionId",missionId);
        Map<String,Object> res=jdbcDao.queryForMap(sql,map);
        if (map.size()==0){
            return null;
        }
        return res;
    }
    
    

    /**
     * 根据id获取当前数据确认的记录
     * @param missionId
     * @return
     */
    public  Map<String,Object> getConfirmDataById(String missionId){
        String sql="select * from W_BCSYBGB WHERE ID=:missionId and F_SPZT!='审批终止'";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("missionId",missionId);
        Map<String,Object> res=jdbcDao.queryForMap(sql,map);
        if (map.size()==0){
            return null;
        }
        return res;
    }

    /**
     * 根据策划id获取当前数据确认的状态
     * @param missionId
     * @return
     */
    public String getConfirmStatusByPlanId(String missionId){
        String sql="select * from W_BCSYBGB WHERE F_SSCH=:missionId and F_SPZT!='审批终止'";
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("missionId",missionId);
        Map<String,Object> res=jdbcDao.queryForMap(sql,map);
        if (res==null){
            return "未发起";
        }else {
            return res.get("F_SPZT").toString();
        }
    }

    /**
     * 把当前的总结id的通过时间改为现在
     * @param recorderId
     */
    public void updatePassTimeForBCSYAndWQSJ(String recorderId){
        String sql="update W_BCSYBGB set F_ZJTGSJ=:timeForNow where ID=:recorderId";
        Map<String, Object> map = new HashMap<>();
        map.put("recorderId", recorderId);
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        map.put("timeForNow",dateFormat.format(calendar.getTime()));
        jdbcDao.exesql(sql,map);
    }
}
