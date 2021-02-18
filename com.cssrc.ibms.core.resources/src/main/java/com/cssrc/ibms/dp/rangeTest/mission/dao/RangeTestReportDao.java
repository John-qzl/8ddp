package com.cssrc.ibms.dp.rangeTest.mission.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Repository
public class RangeTestReportDao {
    @Resource
    private JdbcDao jdbcDao;
    /**
     * 根据任务id获取总结信息
     * @param missionId
     * @return
     */
    public List<Map<String, Object>> getRangeTestReport(String missionId) {
        String sql = "select * from W_BCSYBGB where F_SSCH='"+missionId+"'";
        return jdbcDao.queryForList(sql,null);
    }
}
