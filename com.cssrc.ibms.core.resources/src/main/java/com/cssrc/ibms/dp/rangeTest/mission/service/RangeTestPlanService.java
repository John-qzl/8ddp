package com.cssrc.ibms.dp.rangeTest.mission.service;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RangeTestPlanService {
    @Resource
    RangeTestPlanDao rangeTestPlanDao;

    public RangeTestPlan getById(String missionId) {
        return rangeTestPlanDao.selectById(missionId);

    }
}
