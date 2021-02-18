package com.cssrc.ibms.dp.rangeTest.mission.controller;

import com.cssrc.ibms.dp.rangeTest.mission.service.RangeTestSummaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/rangeTest/mission/summary/")
public class RangeTestSummaryController {
@Resource
    private RangeTestSummaryService rangeTestSummaryService;

    @RequestMapping("getRangeTestReport")
    public void updateSummaryStatus(String summaryId,String status){
        rangeTestSummaryService.updateSummaryStatus(summaryId,status);
    }

}
