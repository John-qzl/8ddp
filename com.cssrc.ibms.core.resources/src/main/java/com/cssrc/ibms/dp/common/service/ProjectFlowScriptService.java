package com.cssrc.ibms.dp.common.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestSummaryDao;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.engine.IScript;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.dao.IOSignDefDao;
import com.cssrc.ibms.core.resources.io.dao.IOSignResultDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableTempDao;
import com.cssrc.ibms.core.resources.product.dao.InstanceTableDao;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.product.acceptance.service.AcceptancePlanService;
import com.cssrc.ibms.dp.product.infor.service.ProductInforService;
import com.cssrc.ibms.report.service.SignModelService;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;

/**
 * @description 流程脚本引擎实现类-项目级定制
 * @author xie chen
 * @date 2019年12月12日 上午9:17:54
 * @version V1.0
 */
@Service
public class ProjectFlowScriptService implements IScript {

	@Resource
	private AcceptancePlanService acceptancePlanService;
	@Resource
	private ProductInforService productInforService;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private InstanceTableDao instanceTableDao;
	@Resource
	private WorkBoardDao workBoardDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private IOTableInstanceDao ioTableInstanceDao;
	@Resource
	private IOTableTempDao ioTableTempDao;
	@Resource
	private IOSignDefDao ioSignDefDao;
	@Resource
	private IOSignResultDao iOSignResultDao;
	@Resource
	private SignModelService signModelService;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private ModuleManageService moduleManageService;
	@Resource
	private RangeTestPlanDao rangeTestPlanDao;
	@Resource
	private TestPlanDao testPlanDao;
	@Resource
	private RangeTestSummaryDao rangeTestSummaryDao;
	/**
	 * @Desc 提交审批自动添加验收组长和外单位人员
	 * @param acceptancePlanId
	 * @param userId
	 * @param userName
	 */
	public void applySubmit(String acceptancePlanId, String userId, String userName,String otherUserId) {

/*		SysOrg sysOrg = sysOrgService.getDefaultOrgByUserId(Long.valueOf(userId));
		managerMap.put("sysOrgId", sysOrg.getOrgId());
		managerMap.put("sysOrgName", sysOrg.getOrgName());*/
		if(userId==null) {
			return ;
		}
		List<SysOrg> list = sysOrgService.getOrgsByUserId(Long.valueOf(userId));
		List<String> groupList=acceptanceGroupDao.getListByAcceptancePanId(acceptancePlanId);
		
		if(groupList.contains(userId)) {
			return;
		}
		Map<String, Object> managerMap = new HashMap<>();
		managerMap.put("position", "组长");
		managerMap.put("userName", userName);
		managerMap.put("userId", userId);
		managerMap.put("acceptancePlanId", acceptancePlanId);
		if (list.size() != 0) {
			managerMap.put("sysOrgId", list.get(0).getOrgId());
			managerMap.put("sysOrgName", list.get(0).getOrgName());
		}
		acceptanceGroupDao.addGroupManager(managerMap);
		if(otherUserId!=null&&!otherUserId.equals("")) {  //添加外单位人员到组
			String[] str=otherUserId.split(",");
			for (String strUserId : str) {
				managerMap = new HashMap<>();
				managerMap.put("position", "组员");
				SysOrg org = sysOrgService.getDefaultOrgByUserId(Long.valueOf(strUserId));
				if(org!=null) {
					managerMap.put("sysOrgId", org.getOrgId());
					managerMap.put("sysOrgName", org.getOrgName());
				}
				managerMap.put("acceptancePlanId", acceptancePlanId);
				managerMap.put("userName",sysUserService.getById(Long.valueOf(strUserId)).getFullname());
				managerMap.put("userId", otherUserId);
				if(!groupList.contains(otherUserId)&&!otherUserId.equals(userId)) {
					acceptanceGroupDao.addGroupManager(managerMap);
				}
				
			}
		}
	}
	/**
	 * @Desc 靶场组长自动回填

	 * @param userId
	 * @param userName
	 */
	public void rangeTestSubmit(String PlanId, String userId, String userName) {
		if(userId==null) {
			return ;
		}
		List<SysOrg> list = sysOrgService.getOrgsByUserId(Long.valueOf(userId));
		List<AcceptanceGroup> groupList=acceptanceGroupDao.getByMissionId(PlanId);
		
		for (AcceptanceGroup acceptanceGroup : groupList) {
			if(acceptanceGroup.getXmId().equals(userId)) {
				return;
			}
		}
		String orgName="";
		String orgId="";
		if (list.size() != 0) {
			orgId=CommonTools.Obj2String(list.get(0).getOrgId());
			orgName=CommonTools.Obj2String(list.get(0).getOrgName());
		}
		acceptanceGroupDao.addRangeTestPlanGroupManager(userName,userId,PlanId,orgName,orgId);
	}

	/**
	 * @Desc 验收策划审批通过自动创建产品
	 * @param acceptancePlanId

	 */
	public void planApplyPass(String acceptancePlanId) {
		// 1.更新产品验收策划审批状态
		acceptancePlanService.updateApproveStatus(acceptancePlanId, "审批通过");
		Map<String, Object> data = acceptancePlanService.getPlansById(acceptancePlanId);
		acceptancePlanService.insertfiledata(data);
		this.SetfileDataStatus(acceptancePlanId, "0");

		// 2.流程结束后通知组员
		List<Map<String, Object>> groupList = acceptanceGroupDao.getDataByAcceptancePanId(acceptancePlanId);
		String createPlanUserId = data.get("F_JSFZRID").toString(); // 获取策划发起人
		String createPlanUserName = data.get("F_JSFZR").toString();
		Boolean check = false;
		for (Map<String, Object> map : groupList) { // 判断策划流程发起人是否是队伍中的
			if (map.get("F_XMID").toString().equals(createPlanUserId)) {
				check = true;
			}
		}
		if (!check) {
			Map<String, Object> map = new HashMap<>();
			map.put("F_XM", createPlanUserName);
			map.put("F_XMID", createPlanUserId);
			map.put("F_zw", "发起人");
			groupList.add(map);
		}
		for (int i = 0; i < groupList.size(); i++) {
			acceptanceMessageDao.insertMessageByPlan(groupList.get(i), data);
		}
		workBoardDao.insertwork(data);
		//策划通过后自动把当前组员添加到团队成员中
		List<String> userIdList=moduleManageService.getByModuleId(data.get("F_SSXHID").toString());//获得当前团队成员
		for (Map<String, Object> group : groupList) {  //判断当前队员是否在团队成员中
			if(group.get("F_XMID")!=null&&!group.get("F_XMID").toString().equals("")) {
				if(!userIdList.contains(group.get("F_XMID").toString())) {
					moduleManageService.insert(data.get("F_SSXHID").toString(), group.get("F_XMID").toString(), group.get("F_XM").toString(), "team");
				}
			}
		}
		
		
		// 3.流程结束后添加到工作项看板中
		// 2.批量创建产品
		/*
		 * productInforService.autoBatchAddProduct(acceptancePlanId, productBatchId,
		 * productKeys.split(","));
		 */
	}

	public void fileDataInsert(String businessKey) {
		Map<String, Object> data = acceptancePlanService.getReportInfo(businessKey);
		acceptancePlanService.insertReportfiledata(data);
	}

	public void SetfileDataStatus(String businessKey, String status) {
		if (businessKey != null) {
			if ("1".equals(status)) {
				acceptancePlanService.updateFileDataStatus(businessKey, "归档中");
			} else if ("2".equals(status)) {
				acceptancePlanService.updateFileDataStatus(businessKey, "已归档");
				// 流程结束后通知组员
				Map<String, Object> data = acceptancePlanService.getPlansById(businessKey);
				List<Map<String, Object>> groupList = acceptanceGroupDao.getDataByAcceptancePanId(businessKey);
				for (int i = 0; i < groupList.size(); i++) {
					acceptanceMessageDao.insertMessageByPlan(groupList.get(i), data);
				}
			} else {
				acceptancePlanService.updateFileDataStatus(businessKey, "未生成PDF");
			}
		}
	}

	public void setReportStatus(String businessKey, String status) {
		if (businessKey != null) {
			if ("1".equals(status)) {
				acceptanceGroupDao.setReport(businessKey, "未审批");
			} else if ("2".equals(status)) {
				acceptanceGroupDao.setReport(businessKey, "审批中");
			} else if ("0".equals(status)) {
				acceptanceGroupDao.setReport(businessKey, "驳回修改");
			} else {
				acceptanceGroupDao.setReport(businessKey, "审批通过");
			}
		}
	}

	/**
	 * 为当前总结添加总结通过时间
	 * 第一个参数说明模块  可以填 CPYS  BCSY  WQSJ
	 * 		其中 BCSY和 WQSJ 可以随便填
	 * @param ofPart
	 * @param bussinessKey
	 */
	public void updatePassTime(String ofPart, String bussinessKey){
		if (bussinessKey!=null){
			if ("CPYS".equals(ofPart)){
				//如果是产品验收
				acceptanceReportDao.updatePassTimeForCPYS(bussinessKey);
			}
			if ("BCSY".equals(ofPart)||"WQSJ".equals(ofPart)){
				//如果是靶场或者所检
				rangeTestSummaryDao.updatePassTimeForBCSYAndWQSJ(bussinessKey);
			}
		}
	}

	/**
	 * @Description 生成pdf时为策划添加结束时间
	 * @Author ZMZ
	 * @Date 2020/12/4 8:46
	 * @param ofPart
	 * @param bussinessKey
	 * @Return void
	 */
	public void updateEndTimeForPlan(String ofPart, String bussinessKey){
		if (bussinessKey!=null){
			if ("CPYS".equals(ofPart)){
				//如果是产品验收
				acceptancePlanService.updateEndTime(bussinessKey);
			}
			if ("BCSY".equals(ofPart)||"WQSJ".equals(ofPart)){
				//如果是靶场或者所检
				rangeTestSummaryDao.updatePassTimeForBCSYAndWQSJ(bussinessKey);
			}
		}
	}

	/**
	 * 总结通过时修改实例表单状态
	 * 	 * 第一个参数说明模块  可以填 CPYS  BCSY  WQSJ
	 * 	 * 		其中 BCSY和 WQSJ 可以随便填
	 * @param ofPart
	 * @param bussinessKey
	 */
	public void updateInstancesStatusBySummaryId(String ofPart, String bussinessKey){
		if (bussinessKey!=null){
			String planId="";
			if ("CPYS".equals(ofPart)){
				//如果是产品验收
				planId=acceptanceReportDao.getById(bussinessKey).get("F_PLANID").toString();
			}
			if ("BCSY".equals(ofPart)||"WQSJ".equals(ofPart)){
				//如果是靶场或者所检
				planId=rangeTestSummaryDao.getConfirmDataById(bussinessKey).get("F_SSCH").toString();
			}
			List<Map<String, Object>> instanceList=instanceTableDao.getInstanceByPlnanId(planId);
			if (instanceList!=null){
				//遍历每一个实例并改变状态为  已完成
				for (Map<String,Object> instance:instanceList){
					if ("正在使用".equals(instance.get("F_STATUS"))){
						instanceTableDao.updateStatus(instance.get("ID").toString(),"已完成");
					}
				}
			}
		}
	}

	public void signResult(String businessKey, ProcessCmd processCmd) throws ParseException {
		if (processCmd.getVoteAgree() == 2) {
			acceptanceGroupDao.setReport(businessKey, "驳回修改");
		}

		else if (processCmd.getVoteAgree() == 1) {
			Map<String, Object> map = acceptanceReportDao.getById(businessKey);
			SysUser sysUser = (SysUser) UserContextUtil.getCurrentUser();
			List<TableInstance> tableInstanceList = ioTableInstanceDao.getByPlanId(map.get("F_PLANID").toString());
			TableTemp tableReportTemp = null;
			TableInstance tableReportIns = null;
			for (TableInstance tableInstance : tableInstanceList) {
				if (tableInstance.getType() != null) {
					return;
				}
				TableTemp tableTemp = ioTableTempDao.getById(tableInstance.getTableTempID());
				if ("6".equals(tableTemp.getModelType())) {
					tableReportTemp = tableTemp;
					tableReportIns = tableInstance;
				}
			}
			if (tableReportTemp != null) {
				List<SignDef> signDefList = ioSignDefDao.getByTempId(tableReportTemp.getId());
				for (SignDef signDef : signDefList) {
					if (signDef.getName().indexOf("承制方") >= 0) {
						continue;
					} else {
						String fullName = signDef.getName();
						fullName = fullName.substring(fullName.indexOf("(") + 1, fullName.indexOf(")"));
						if (fullName.equals(sysUser.getFullname())) {
							int count = iOSignResultDao.getSignByDef(signDef.getId(), tableReportIns.getId());
							if (count == 0) {
								Long signModelPathFileID = signModelService.getSignModelPathFileID(sysUser.getUserId());
								// List<SignModel> signModelList=(List<SignModel>)
								// signModelService.getByUserId(sysUser.getUserId());
								// if(signModelList!=null&&signModelList.size()!=0) {
								// SignModel signModel=signModelList.get(0);
								SysFile sysFile = sysFileDao.getById(signModelPathFileID);
								SignResult signResult = new SignResult();
								Long signResultId = UniqueIdUtil.genId();
								signResult.setId(String.valueOf(signResultId));
								signResult.setTb_instan_id(tableReportIns.getId());
								signResult.setSigndef_id(signDef.getId());
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								signResult.setSignTime(df.format(new Date()));
								iOSignResultDao.insert(signResult);
								sysFile.setDataId(String.valueOf(signResult.getId()));
								sysFile.setFileId(UniqueIdUtil.genId());
								sysFileDao.add(sysFile);
								List<AcceptanceGroup> groupMap = acceptanceGroupDao
										.getByAcceptancePanId(map.get("F_PLANID").toString());
								for (AcceptanceGroup acceptanceGroup : groupMap) {
									if (acceptanceGroup.getXmId().equals(String.valueOf(sysUser.getUserId()))) {
										acceptanceGroupDao.updateSign(acceptanceGroup.getId(), signResult.getId());
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public HashSet getSignPersonnel(String planId) {
		HashSet hashSet = new HashSet();
		List<AcceptanceGroup> list = acceptanceGroupDao.getByAcceptancePanId(planId);
		int i = 0; // 如果当前组员只有组长
		int index = 0;
		for (AcceptanceGroup acceptanceGroup : list) {
			if (!acceptanceGroup.getZw().equals("组长") && acceptanceGroup.getXmId() != null
					&& !acceptanceGroup.getXmId().equals("")) {
				SysOrg sysOrg = sysOrgService.getById(Long.valueOf(acceptanceGroup.getDwId()));
				if (sysOrg.getPath().contains("10000025340019")) { // 如果当前成员不是八部内的不加入数据确认会签
					hashSet.add(acceptanceGroup.getXmId());
				}
			} else if (acceptanceGroup.getZw().equals("组长")) {
				i = index;
			}
			index++;
		}
		if (hashSet.size() == 0 && list.size() != 0) {
			hashSet.add(list.get(i).getXmId());
		}
		return hashSet;

	}

	public HashSet setDepartment(String userIds) {
		HashSet hashSet = new HashSet();
		if(userIds==null) {
			return hashSet;
		}
		String[] userGroup = userIds.split(",");
		for (String string : userGroup) {
			hashSet.add(string);
		}
		return hashSet;

	}

	/**
	 * 将靶场策划的指定任务改为指定状态
	 * @param missionId
	 * @param status
	 */
	public void missionStatusChange(String missionId,String status){
		rangeTestPlanDao.updateStatus(missionId,status);
	}
	public void updateDataConfirmStatus(String id,String status){
		testPlanDao.updateDataConfirmStatus(id, status);
	}
	
}
