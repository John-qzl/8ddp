package com.cssrc.ibms.core.resources.datapackage.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 靶场实验任务表单下发数据库操作类
 * W_BCRWCHBGB
 * by zmz
 * 20200821
 */
@Repository
public class RangeTestPlanDao {
    @Resource
    private JdbcDao jdbcDao;

    public RangeTestPlan selectById(String id) {
        String sql = " select * from W_BCRWCHBGB where id="+id;
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        RangeTestPlan rangeTestAcceptancePlan = JSON.parseObject(JSON.toJSONString(map), RangeTestPlan.class);
        return rangeTestAcceptancePlan;
    }

    /**
     * 根据型号id获取策划信息
     * @param moduleId
     * @return
     */
    public List<Map<String,Object>> getPlanListByModuleIdForBCSY(String moduleId){
        Map<String,Object> sqlMap=new HashMap<>();
        sqlMap.put("moduleId",moduleId);
        String sql=" select * from W_BCRWCHBGB where F_XHID=:moduleId and F_CHBGBBH like '%BCSY%'";
        List<Map<String,Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
        return resMapList;
    }

    public List<Map<String, Object>> getPlanListByModuleIdForWQSJ(String moduleId) {
        Map<String,Object> sqlMap=new HashMap<>();
        sqlMap.put("moduleId",moduleId);
        String sql=" select * from W_BCRWCHBGB where F_XHID=:moduleId and F_CHBGBBH like '%WQSJ%'";
        List<Map<String,Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
        return resMapList;
    }

    public void insert(RangeTestPlan rangeTestPlan) {
        /*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql="insert into W_BCRWCHBGB "
                +"(ID,F_XHDH,F_SYRWMC,F_WQXTBH,F_DDBH,F_SJDD,F_KSSJ,F_JSSJ,F_SYFZR,F_SYFZRID,F_XHID,F_SPZT,F_SYYJWJ) values("
                +rangeTestPlan.getID()+",'"
                +rangeTestPlan.getF_XHDH() +"','"
                +rangeTestPlan.getF_SYRWMC() +"','"
                +rangeTestPlan.getF_WQXTBH() +"','"
                +rangeTestPlan.getF_DDBH() +"','"
                +rangeTestPlan.getF_SJDD() +"','"
                //开始时间
                +"to_date('"+rangeTestPlan.getF_KSSJ()+"','YYYY-MM-DD HH:MM:SS'),"
                //结束时间
                +"to_date('"+rangeTestPlan.getF_JSSJ()+"','YYYY-MM-DD HH:MM:SS'),'"
                +rangeTestPlan.getF_SYFZR() +"','"
                +rangeTestPlan.getF_SYFZRID() +"','"
                +rangeTestPlan.getF_XHID() +"','"
                +rangeTestPlan.getF_SPZT() +"','"
                +rangeTestPlan.getF_SYYJWJ() +"'"
                +")";*/
        StringBuffer newSql=new StringBuffer();
        Map<String, Object> map = MapUtil.transBean2Map(rangeTestPlan);
        newSql.append("INSERT INTO W_BCRWCHBGB ( ID, F_XHDH, F_SYRWMC, F_WQXTBH, F_DDBH, F_SJDD, F_KSSJ, F_JSSJ, F_XHID, F_SPZT, F_SYYJWJ,");
        newSql.append("F_FQR,F_FQRID,F_CHBGBBH,F_SYZZ,F_SYZZID,F_SYDW,F_SYDWID,F_GDLCZT )");
        newSql.append(" VALUES(");
        newSql.append(" :ID, :f_XHDH, :f_SYRWMC, :f_WQXTBH, :f_DDBH, :f_SJDD, ");
        newSql.append(":f_KSSJ,:f_JSSJ,");
        //newSql.append("to_date(:").append("f_KSSJ").append(", 'YYYY-MM-DD')").append(",");
        //newSql.append("to_date(:").append("f_JSSJ").append(", 'YYYY-MM-DD')").append(",");
        newSql.append(" :f_XHID, :f_SPZT, :f_SYYJWJ,:f_FQR,:f_FQRID,:f_CHBGBBH,:f_SYZZ,:f_SYZZID,:f_SYDW,:f_SYDWID,:f_GDLCZT)");
        jdbcDao.exesql(newSql.toString(),map);
            /*String  sql = SqlHelp.getInsertSql(RangeTestPlan.class, "w_bcrwchbgb");

            jdbcDao.exesql(sql, map);*/
    }

    /**
     * 更新记录
     * @param rangeTestPlan
     */
    public void update(RangeTestPlan rangeTestPlan) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql="update W_BCRWCHBGB set " +
                "ID=:ID,F_XHDH=:F_XHDH,F_SYRWMC=:F_SYRWMC, F_WQXTBH=:F_WQXTBH, F_DDBH=:F_DDBH, F_SJDD=:F_SJDD, " +
                "F_KSSJ=:F_KSSJ, F_JSSJ=:F_JSSJ, F_XHID=:F_XHID, F_SPZT=:F_SPZT, F_SYYJWJ=:F_SYYJWJ," +
                "F_FQR=:F_FQR,F_FQRID=:F_FQRID,F_CHBGBBH=:F_CHBGBBH,F_SYZZ=:F_SYZZ,F_SYZZID=:F_SYZZID,F_SYDW=:F_SYDW,F_SYDWID=:F_SYDWID" +
                ",F_GDLCZT=:F_GDLCZT"
                +" where id=:ID";
        Map<String,Object> map =new HashMap<>();
        map.put("ID",rangeTestPlan.getID());
        map.put("F_XHDH",rangeTestPlan.getF_XHDH() );
        map.put("F_SYRWMC",rangeTestPlan.getF_SYRWMC());
        map.put("F_WQXTBH",rangeTestPlan.getF_WQXTBH());
        map.put("F_DDBH",rangeTestPlan.getF_DDBH());
        map.put("F_SJDD",rangeTestPlan.getF_SJDD());
        map.put("F_KSSJ",rangeTestPlan.getF_KSSJ());
        map.put("F_JSSJ",rangeTestPlan.getF_JSSJ());
/*        map.put("F_SYFZR",rangeTestPlan.getF_SYFZR());
        map.put("F_SYFZRID",rangeTestPlan.getF_SYFZRID());*/
        map.put("F_XHID",rangeTestPlan.getF_XHID());
        map.put("F_SPZT",rangeTestPlan.getF_SPZT());
        map.put("F_SYYJWJ",rangeTestPlan.getF_SYYJWJ());
        map.put("F_FQR",rangeTestPlan.getF_FQR());
        map.put("F_FQRID",rangeTestPlan.getF_FQRID());
        /*map.put("F_BCBH",rangeTestPlan.getF_BCBH());
        map.put("F_BCSYBH",rangeTestPlan.getF_BCSYBH());*/
        map.put("F_CHBGBBH",rangeTestPlan.getF_CHBGBBH());
        map.put("F_SYZZ",rangeTestPlan.getF_SYZZ());
        map.put("F_SYZZID",rangeTestPlan.getF_SYZZID());
        map.put("F_SYDW",rangeTestPlan.getF_SYDW());
        map.put("F_SYDWID",rangeTestPlan.getF_SYDWID());
        map.put("F_GDLCZT",rangeTestPlan.getF_GDLCZT());

        jdbcDao.exesql(sql,map);
    }

    /**
     * pad回传实例表单时,填写试验报告回传数据列
     * @param missionId
     * @param backData
     */
    public void updateBackData(String missionId, String backData) {

        Map<String,Object>  map=new HashMap<>();
        map.put("backData",backData);
        map.put("missionId",missionId);
        String sql="update W_BCRWCHBGB set F_sybghcsj=:backData where ID=:missionId";
        jdbcDao.exesql(sql, map);
    }

    /**
     * 为指定的策划增加pad文件字段
     * @param pdfJson
     * @param missionId
     */
    public void updatePDF(String pdfJson,String missionId){
        String sql="update W_BCRWCHBGB set F_GDWJ=:pdfJson,F_GDLCZT='已生成PDF' where ID=:missionId";
        Map<String,Object> map =new HashMap<>();
        map.put("pdfJson",pdfJson);
        map.put("missionId",missionId);
        jdbcDao.exesql(sql,map);
    }

    /**
     * 把归档流程状态从未归档改成未生成pdf
     * @param missionId
     */
    public void updatePDFStatus(String missionId){
        String sql="update W_BCRWCHBGB set F_GDLCZT='未生成PDF' where ID=:missionId";
        Map<String,Object> map =new HashMap<>();
        map.put("missionId",missionId);
        jdbcDao.exesql(sql,map);
    }

    /**
     * 更新指定策划为指定状态
     * @param missionId
     * @param status
     */
    public void updateStatus(String missionId, String status) {
        String sql="update W_BCRWCHBGB set F_spzt=:status where ID=:missionId";
        Map<String,Object> map =new HashMap<>();
        map.put("status",status);
        map.put("missionId",missionId);
        jdbcDao.exesql(sql, map);
    }
    
    /**
     * 查找型号对应的已完成的策划
     * @param moduleId
     */
    public List<Map<String, Object>> getByMoudleId(String moduleId,String type) {
    	List<Map<String, Object>> testRangeList=new ArrayList<>();
        String sql="select * from W_BCRWCHBGB where F_spzt='审批通过' and F_XHID=:moduleId and F_CHBGBBH like '%"+type+"%'";
        Map<String,Object> map =new HashMap<>();
        map.put("moduleId",moduleId);
        map.put("type", type);
        testRangeList=jdbcDao.queryForList(sql, map);
        return testRangeList;
    }

}
