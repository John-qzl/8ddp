package com.cssrc.ibms.core.resources.io.dao;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.RangeTestInstance;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class RangeTestInstaceDao {
    @Resource
    JdbcDao jdbcDao;

    /**
     * 根据策划id获取策划下挂的实例
     * @param missionId
     * @return
     */
    public List<RangeTestInstance> getByPlanId(String missionId) {
        String sql="select * from W_TB_INSTANT where F_planid='"+missionId+"' and F_STATUS!='废弃'";
        List<Map<String,Object>> mapList = jdbcDao.queryForList(sql, null);
        List<RangeTestInstance> rangeTestInstanceList= JSON.parseArray(JSON.toJSONString(mapList),RangeTestInstance.class);
        return rangeTestInstanceList;
        /*
        * Map<String,Object> map = jdbcDao.queryForMap(sql, null);
        RangeTestPlan rangeTestAcceptancePlan = JSON.parseObject(JSON.toJSONString(map), RangeTestPlan.class);
        return rangeTestAcceptancePlan;
        * */

    }
}
