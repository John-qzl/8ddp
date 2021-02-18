package com.cssrc.ibms.core.resources.datapackage.dao;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestDataPackage;
import com.cssrc.ibms.dp.signModel.entity.WPadhcqzb;
import com.cssrc.ibms.system.dao.SysFileDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Repository
/**
 * 靶场实验任务表单下发数据库操作类
 * W_BCSYSJBXXXX
 * by zmz
 * 20200821
 */
public class RangeTestDataPackageDao {
    @Resource
    private JdbcDao jdbcDao;


    public RangeTestDataPackage getById(String dataPackageId) {
        String sql = " select * from W_BCSYSJBXXXX where id="+dataPackageId;
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        RangeTestDataPackage rangeTestDataPackage = JSON.parseObject(JSON.toJSONString(map), RangeTestDataPackage.class);
        return rangeTestDataPackage;
    }

    public List<RangeTestDataPackage> getByPlanId(String missionId) {
        String sql = " select * from W_BCSYSJBXXXX where F_SSCH="+missionId;
        List<Map<String,Object>> mapList = jdbcDao.queryForList(sql, null);
        List<RangeTestDataPackage> rangeTestDataPackageList = JSON.parseArray(JSON.toJSONString(mapList), RangeTestDataPackage.class);
        return rangeTestDataPackageList;
    }


    /**
     * 保存全部信息
     * @param rangeTestDataPackage
     */
    public void insert(RangeTestDataPackage rangeTestDataPackage) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO W_BCSYSJBXXXX " +
                "(ID,F_SJMC,F_MJ,F_BMQX,F_SCR,F_SCRID,F_SCSJ,F_WCSJ,F_BB,F_SM,F_ZXZT,F_SXMBMC,F_SXMBID,F_SSCH)" +
                "VALUES ("
                +rangeTestDataPackage.getId()+",'"
                +rangeTestDataPackage.getF_SJMC() +"','"
                +rangeTestDataPackage.getF_MJ() +"','"
                +rangeTestDataPackage.getF_BMQX() +"','"
                +rangeTestDataPackage.getF_SCR() +"','"
                +rangeTestDataPackage.getF_SCRID() +"',"
                //上传时间
                +"to_date('"+sdf.format(rangeTestDataPackage.getF_SCSJ()).substring(0,10)+"','yyyy-mm-dd'),"
                //完成时间
                +"to_date('"+sdf.format(rangeTestDataPackage.getF_WCSJ()).substring(0,10)+"','yyyy-mm-dd'),'"
                +rangeTestDataPackage.getF_BB() +"','"
                +rangeTestDataPackage.getF_SM() +"','"
                +rangeTestDataPackage.getF_ZXZT() +"','"
                +rangeTestDataPackage.getF_SXMBMC() +"','"
                +rangeTestDataPackage.getF_SXMBID() +"','"
                +rangeTestDataPackage.getF_SSCH() +"'"
                +")";
        jdbcDao.exesql(sql,null);
    }

    /**
     * 更新条目
     * @param id
     */
    public void deleteById(String id) {
        String sql="delete from W_BCSYSJBXXXX where id="+id;
        jdbcDao.exesql(sql,null);
    }
}
