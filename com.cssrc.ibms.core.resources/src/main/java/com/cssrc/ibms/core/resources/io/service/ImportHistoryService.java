package com.cssrc.ibms.core.resources.io.service;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.io.bean.ImportHistory;
import com.cssrc.ibms.core.resources.io.dao.ImportHistoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
@Service
public class ImportHistoryService {
    @Resource
    private ImportHistoryDao dao;

    /**
     * 产品验收的导入历史
     * 每次产品验收导入都会在导入历史表里加一条记录
     *
     * @param acceptancePlan
     */
    public void addImportHistoryForCPYS(AcceptancePlan acceptancePlan) {
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        ImportHistory importHistory=new ImportHistory();
        Long id=UniqueIdUtil.genId();
        importHistory.setId(id.toString());
        importHistory.setChbh(acceptancePlan.getChbgbbh());
        importHistory.setRwmc(acceptancePlan.getCpmc());
        importHistory.setDrsj(dateFormat.format(calendar.getTime()));
        importHistory.setZz(acceptancePlan.getYszz());
        importHistory.setZzId(acceptancePlan.getYszzId());
        importHistory.setSsxhhpc(acceptancePlan.getSscppc());
        dao.insert(importHistory);
    }

    /**
     * 新增一条靶场和所检的导入历史
     *
     * @param rangeTestPlan
     */
    public void addImportHistoryForBCSY(RangeTestPlan rangeTestPlan) {
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        ImportHistory importHistory=new ImportHistory();
        Long id=UniqueIdUtil.genId();
        importHistory.setId(id.toString());
        importHistory.setId(id.toString());
        importHistory.setChbh(rangeTestPlan.getF_CHBGBBH());
        importHistory.setRwmc(rangeTestPlan.getF_SYRWMC());
        importHistory.setDrsj(dateFormat.format(calendar.getTime()));
        importHistory.setZz(rangeTestPlan.getF_SYZZ());
        importHistory.setZzId(rangeTestPlan.getF_SYZZID());
        importHistory.setSsxhhpc(rangeTestPlan.getF_XHID());
        dao.insert(importHistory);
    }
}
