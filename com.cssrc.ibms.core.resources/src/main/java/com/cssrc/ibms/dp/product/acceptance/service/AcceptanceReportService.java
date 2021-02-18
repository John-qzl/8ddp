package com.cssrc.ibms.dp.product.acceptance.service;

import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AcceptanceReportService {
    @Resource
    private AcceptanceReportDao dao;

    /**
     * 生成当前型号的下一个总结报告的id
     * 只返回id就可以了
     * @param xhId
     * @return
     */
    public String generatorReportNumber(String xhId){
        //加1是下一个编号
        Integer countSummaryReport=dao.countSummaryBroupBySSXH(xhId)+1;
        String nextNumber=countSummaryReport.toString();
        if (nextNumber.length()==1){
            nextNumber="00"+nextNumber;
        }else if (nextNumber.length()==2){
            nextNumber="0"+nextNumber;
        }
        return nextNumber;
    }
}
