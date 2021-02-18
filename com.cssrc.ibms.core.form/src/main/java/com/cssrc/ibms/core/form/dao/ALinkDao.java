package com.cssrc.ibms.core.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FormControlService需要用到的查询dao
 */
@Repository
public class ALinkDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private JdbcTemplate jdbcTemplate;


    /**
     * 根据策划id双表联查获取策划信息
     * @param planId
     * @return
     */
    public List<Map<String, Object>> getPlanInfoById(String planId) {
        String sql = "SELECT a.F_SSCPPC,a.F_CPPC,b.F_SSCPLB " +
                "from W_CPYSCHBGB a " +
                "INNER JOIN W_CPLBPCB b on a.F_CPPC=b.F_PCH " +
                "WHERE a.id='"+planId+"'";
        return jdbcDao.queryForList(sql, null);
    }

    public String getxhIdByPlanIdForRangeTest(String planId) {
        String sql ="SELECT * FROM W_BCRWCHBGB WHERE ID=:id";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", planId);
        Map<String, Object> map1=jdbcDao.queryForMap(sql,map);
        if (map1==null){
            return null;
        }else {
            return map1.get("F_XHID").toString();
        }
    }

    public Map<String,Object> getSysFileByFileId(String fileId){
        String sql="select * from CWM_SYS_FILE where FILEID=:fileId";
        Map<String,Object> sqlMap=new HashMap<>();
        sqlMap.put("fileId",fileId);
        Map<String,Object> resMap=jdbcDao.queryForMap(sql,sqlMap);
        return resMap;
    }


}
