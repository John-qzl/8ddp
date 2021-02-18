package com.cssrc.ibms.dp.rangeTest.mission.controller;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.dao.RangeTestInstaceDao;
import com.cssrc.ibms.core.resources.io.service.RangeTestInstanceService;
import com.cssrc.ibms.dp.rangeTest.mission.service.RangeTestPlanService;
import com.cssrc.ibms.dp.rangeTest.mission.service.RangeTestReportServie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller()
@RequestMapping("/RangeTest/mission/report/")
public class RangeTestReportController {
    @Resource
    RangeTestReportServie rangeTestReportService;
    @Resource
    RangeTestPlanService rangeTestPlanService;
    @Resource
    RangeTestInstanceService rangeTestInstanceService;

    /**
     * 如果当前策划下一个实例都没有,就返回0
     * 如果当前策划下挂了实例,就返回1
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("countRangeTestInstances")
    @ResponseBody
    public String countRangeTestInstances(HttpServletRequest request, HttpServletResponse response){
        String missionId = request.getParameter("missionId");
        String res=rangeTestInstanceService.CountHowManyInstanceForRangeTest(missionId);
        return res;
    }

    /**
     * @Desc 查看当前策划下面是否有策划报告
     * @param request
     * @param response
     */
    @RequestMapping("getRangeTestReport")
    @ResponseBody
    public String getRangeTestReport(HttpServletRequest request, HttpServletResponse response) {
        String missionId = request.getParameter("missionId");
        List<Map<String, Object>> list=rangeTestReportService.getRangeTestReport(missionId);
        if(list!=null&&list.size()>0) {
            return "0";
        }
        RangeTestPlan rangeTestPlan =rangeTestPlanService.getById(missionId);
        if(rangeTestPlan.getF_SYBGHCSJ()==null||rangeTestPlan.getF_SYBGHCSJ().equals("")) {
            return "2";
        }
        return "1";
    }
}
