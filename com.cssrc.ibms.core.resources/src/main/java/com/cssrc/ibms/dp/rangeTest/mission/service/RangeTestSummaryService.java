package com.cssrc.ibms.dp.rangeTest.mission.service;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.mission.service.TestPlanService;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestSummaryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RangeTestSummaryService {
    @Resource
    private RangeTestSummaryDao rangeTestSummaryDao;
    @Resource
    private AcceptanceMessageDao acceptanceMessageDao;
    @Resource
    private AcceptanceGroupDao acceptanceGroupDao;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private TestPlanService testPlanService;
    @Resource
    private WorkBoardDao workBoardDao;
    @Resource
    private RangeTestPlanDao rangeTestPlanDao;

    /**
     * 根据数据确认id更新数据确认状态
     * @param summaryId
     * @param status
     */
    public void updateSummaryStatus(String summaryId, String status) {
        rangeTestSummaryDao.updateSPZT(summaryId,status);
        if("审批通过".equals(status)){
            //审批通过,给相关人员发消息
            insertMessageByRangeTestSummary(summaryId);
            //写看板
            workBoardDao.updatework(summaryId,"数据确认完毕","生成PDF归档");
            //改策划的pdf状态
            rangeTestPlanDao.updatePDFStatus(summaryId);
        }
    }

    public void insertMessageByRangeTestSummary(String missionId) {
        //定位策划
        Map<String, Object> rangeTestPlan = rangeTestSummaryDao.getConfirmDataByPlanId(missionId);
        //定位组员表
        List<AcceptanceGroup> acceptanceGroupList = acceptanceGroupDao.getByMissionId(missionId);
        for (AcceptanceGroup acceptanceGroup : acceptanceGroupList) {
            SysUser teamMember = sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
            if ("组长".equals(acceptanceGroup.getZw())) {
                //是组长   直接发消息
                acceptanceMessageDao.insertMessageByRangeTestPlan(teamMember, rangeTestPlan, "组长");
            } else {
                //不是组长
                //先看下这个人在组里是不是有担任组长的同时担任组员
                if (testPlanService.curUserIsTeamLeader(acceptanceGroupList, acceptanceGroup.getXmId())) {
                    //当前这个人既是组员也是组长
                    //组长的消息是上面发的,这里直接跳过
                } else {
                    //当前这个人不是组长,直接发消息
                    acceptanceMessageDao.insertMessageByRangeTestPlan(teamMember, rangeTestPlan, "组员");
                }
            }
        }
    }

    /**
     * 获取当前策划下的数据确认的状态(不含驳回的
     * @param missionId
     * @return
     */
    public String getConfirmStatusByPlanId(String missionId){
        return rangeTestSummaryDao.getConfirmStatusByPlanId(missionId);
    }
}
