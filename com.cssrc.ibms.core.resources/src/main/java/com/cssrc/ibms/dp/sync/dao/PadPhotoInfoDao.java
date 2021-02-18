package com.cssrc.ibms.dp.sync.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.sync.bean.PadPhotoInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PadPhotoInfoDao {
    @Resource
    private JdbcDao jdbcDao;

    /**
     * 插入本条记录
     * @param padPhotoInfo
     */
    public void insert(PadPhotoInfo padPhotoInfo){
        String sql= SqlHelp.getInsertSql(PadPhotoInfo.class,"W_PADPZXX");
        Map<String, Object> map = MapUtil.transBean2Map(padPhotoInfo);
        jdbcDao.exesql(sql,map);
    }

    /**
     * 根据实例查找签署和照片
     * @param instanceId
     * @return
     */
    public List<PadPhotoInfo> getByInstanceId(String instanceId) {
        String sql="select * from W_PADPZXX where F_INSTANCEID=:instanceId";
        Map<String ,Object> sqlMap=new HashMap<>();
        sqlMap.put("instanceId",instanceId);
        List<Map<String, Object>> resMap=jdbcDao.queryForList(sql,sqlMap);
        List<PadPhotoInfo> padPhotoInfoList=new ArrayList<>();
        if (resMap==null||resMap.size()==0){
            return null;
        }else {
            for (Map<String,Object> map:resMap){
                PadPhotoInfo padPhotoInfo=new PadPhotoInfo(map);
                padPhotoInfoList.add(padPhotoInfo);
            }
            return padPhotoInfoList;
        }
    }

    /**
     * 根据id查找整条记录
     * @param id
     * @return
     */
    public PadPhotoInfo getById(String id) {
        String sql="select * from W_PADPZXX where ID=:id";
        Map<String ,Object> sqlMap=new HashMap<>();
        sqlMap.put("id",id);
        Map<String, Object> resMap=jdbcDao.queryForMap(sql,sqlMap);
        if (resMap==null||resMap.size()==0){
            return null;
        }else {
                PadPhotoInfo padPhotoInfo=new PadPhotoInfo(resMap);
            return padPhotoInfo;
        }
    }
}
