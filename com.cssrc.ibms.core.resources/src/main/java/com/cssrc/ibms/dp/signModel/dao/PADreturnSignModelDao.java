package com.cssrc.ibms.dp.signModel.dao;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.signModel.entity.WPadhcqzb;
import com.cssrc.ibms.system.dao.SysFileDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
/**
 * PAD回传签章表数据库操作类
 * by zmz
 * 20200821
 */
public class PADreturnSignModelDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private SysFileDao sysFileDao;
    /**
     * 根据id查找padhcqzb的信息
     * @param signModelId
     * @return
     */
    public WPadhcqzb selectById(String signModelId) {
        String sql = " select * from W_PADHCQZB where id="+signModelId;
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        WPadhcqzb wPadhcqzb = JSON.parseObject(JSON.toJSONString(map), WPadhcqzb.class);
        return wPadhcqzb;
    }

    /**
     * 查找当前用户在表中的记录条数
     * @param userid
     * @return
     */
    public Integer CountByUserid(String userid) {
        String sql="select * from W_PADHCQZB where F_YHID='"+userid+"'";
        Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        if (map==null){
            return 0;
        }
        return map.size();
    }

    /**
     * 删除当前用户在表中的所有记录
     * @param userid
     */
    public void deleteByUserid(String userid) {
        String sql="delete from W_PADHCQZB where F_YHID='"+userid+"'";
        jdbcDao.exesql(sql, null);
    }

    public void insert(WPadhcqzb wPadhcqzb) {
        String sql="INSERT INTO W_PADHCQZB (ID,F_QZID,F_YH,F_YHID,F_PADHCQM) VALUES ("
                +wPadhcqzb.getId()+",'"
                +wPadhcqzb.getF_Qzid()+ "','"
                +wPadhcqzb.getF_Yh()+"','"
                +wPadhcqzb.getF_Yhid()+"','"
                +wPadhcqzb.getF_Padhcqm()+"'"
                +")";
        jdbcDao.exesql(sql, null);
    }

    public void deleteByFileId(String fileId) {
        String sql="delete W_PADHCQZB where DBMS_LOB.SUBSTR(F_QZID, 200, 1)='"+fileId+"'";
        jdbcDao.exesql(sql, null);
    }

    /**
     * 当前签章无人认领,只保存签章本身信息,不保存人的信息
     * @param wPadhcqzb
     */
    public void insertWithoutUser(WPadhcqzb wPadhcqzb) {
        String sql="INSERT INTO W_PADHCQZB (ID,F_QZID,F_PADHCQM) VALUES ("
                +wPadhcqzb.getId()+",'"
                +wPadhcqzb.getF_Qzid()+ "','"
                +wPadhcqzb.getF_Padhcqm()+"'"
                +")";
        jdbcDao.exesql(sql, null);
    }
}

