package com.cssrc.ibms.dp.rangeTest.mission.service;

import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestReportDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RangeTestReportServie {
    @Resource
    RangeTestReportDao rangeTestReportDao;

    /**
     * 根据任务id获取总结
     * @param missionId
     * @return
     */
    public List<Map<String, Object>> getRangeTestReport(String missionId) {
        return rangeTestReportDao.getRangeTestReport(missionId);
    }
}
