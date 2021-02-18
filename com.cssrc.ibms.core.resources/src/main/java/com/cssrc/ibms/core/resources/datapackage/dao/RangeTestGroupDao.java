package com.cssrc.ibms.core.resources.datapackage.dao;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

import com.cssrc.ibms.core.resources.datapackage.model.RangeTestGroup;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 靶场实验任务表单下发数据库操作类
 * W_BCSYZB
 * by zmz
 * 20200826
 */
@Repository
public class RangeTestGroupDao {
    @Resource
    private JdbcDao jdbcDao;

    /**
     * 根据所属任务(策划)查询任务组
     * @param planId
     * @return
     */
    public List<RangeTestGroup> selectBySSRW(String planId) {
        String sql = " select * from W_BCSYZB where F_SSRW="+planId;
        List<Map<String,Object>> list=jdbcDao.queryForList(sql,null);
        List<RangeTestGroup> rangeTestGroupList=JSON.parseArray(JSON.toJSONString(list),RangeTestGroup.class );
        return rangeTestGroupList;
    }

    /**
     * 根据id查任务组信息
     * @param id
     * @return
     */
    public RangeTestGroup getById(String id) {
        String sql = " select * from W_BCSYZB where id="+id;
        Map<String,Object> map=jdbcDao.queryForMap(sql,null);
        RangeTestGroup rangeTestGroup=JSON.parseObject(JSON.toJSONString(map),RangeTestGroup.class );
        return rangeTestGroup;
    }

    /**
     * 新建任务组
     * @param rangeTestGroup
     */
    public void insert(RangeTestGroup rangeTestGroup) {
        String sql="INSERT INTO W_BCSYZB " +
                "(ID,F_ZW,F_XM,F_XMID,F_DW,F_DWID,F_FZXM,F_SSRW,F_SSSYZJ,F_QSID) values ("
                +rangeTestGroup.getId()+",'"
                +rangeTestGroup.getF_ZW() +"','"
                +rangeTestGroup.getF_XM() +"','"
                +rangeTestGroup.getF_XMID() +"','"
                +rangeTestGroup.getF_DW() +"','"
                +rangeTestGroup.getF_DWID() +"','"
                +rangeTestGroup.getF_FZXM() +"','"
                +rangeTestGroup.getF_SSRW() +"','"
                +rangeTestGroup.getF_SSSYZJ() +"','"
                +rangeTestGroup.getF_QSID() +"'"
                +")";
        jdbcDao.exesql(sql,null);
    }

    /**
     * 根据所属任务取组员信息
     * @param missionId
     * @return
     */
    public List<Map<String, Object>> getBymissionId(String missionId) {
        String sql ="select * from W_BCSYZB where F_SSRW="+missionId;
        List<Map<String,Object>> mapList=jdbcDao.queryForList(sql,null);
        return mapList;
    }
}
