package com.cssrc.ibms.dp.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * User :  sgl.
 * Date : 2019/1/18.
 * Time : 14:21.
 */
@Repository
public class DataPackageInfoDao {
    private static final String TAG = "DataPackageInfoDao";
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    /**
     * @Description: 根据主键获取数据包详细信息
     * @Author  shenguoliang
     * @Params [id]
     * @Date 2018/8/1 16:20
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     * @Line 32
     */
    public Map<String,Object>getDataMapById(Long id) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select * from W_DATAPACKAGEINFO ");
        sql.append("  where ID=:ID ");

        Map params = new HashMap();
        params.put("ID", id);
        return jdbcDao.queryForMap(sql.toString(), params);
    }

    /**
     * @Description: 查询
     * @Author  shenguoliang
     * @Params [keyValueMap]
     * @Date 2018/8/1 16:21
     * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @Line 57
     */
    @SuppressWarnings("rawtypes")
    public List<Map<String, Object>> query(Map<String, Object> keyValueMap) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from W_DATAPACKAGEINFO where 1=1 ");

        Set keys = keyValueMap.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value = keyValueMap.get(key);
            sql.append(" and ").append(key).append(" = '").append(value).append("'");
        }
        return jdbcDao.queryForList(sql.toString(), null);
    }

    public Map<String, Object> getDataPackageByInsId(Map<String, Object> param) {
        String sql = "SELECT * FROM W_DATAPACKAGEINFO  WHERE F_SSMB=:slId";
        return jdbcDao.queryForMap(sql, param);
    }
    public void updateDataPackageById(Map<String, Object> param) {
        String sql = "UPDATE W_DATAPACKAGEINFO SET F_ZXZT=:zxzt, F_WCSJ=:endtime WHERE F_SSMB=:slId";
        int k = jdbcDao.exesql(sql, param);
    }
    public void updateDataPackageById(Map<String, Object> param ,String time) {
        String sql = "UPDATE W_DATAPACKAGEINFO SET F_ZXZT=:zxzt, F_WCSJ=to_date('"+time+"','yyyy-mm-dd hh24:mi:ss') " +
                "WHERE F_SSMB=:slId";
        jdbcDao.exesql(sql, param);
    }



}
