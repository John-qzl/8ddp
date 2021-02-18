package com.cssrc.ibms.core.resources.datapackage.service;

import javax.annotation.Resource;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestReportDao;
import com.fr.web.core.A.A;
import com.fr.web.core.A.S;
import org.olap4j.impl.ArrayMap;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.resources.datapackage.dao.DpComAnalysisDao;
import com.cssrc.ibms.core.web.controller.BaseController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class DpComAnalysisService {
	
	@Resource
	private DpComAnalysisDao dpComAnalysisDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private RangeTestPlanDao rangeTestPlanDao;
	@Resource
	private RangeTestReportDao rangeTestReportDao;

	
	/**
	 * 根据发次Id,父节点（普通节点）统计

	 * @return
	 */
	public int getCount(String projectId, String parentId) {
		return dpComAnalysisDao.getCount(projectId, parentId);
	}
	
	/**
	 * 根据发次Id,执行状态统计
	 * @param projectId
	 * @param status
	 * @return
	 */
	public int getCountBystatus(String projectId, String status) {
		return dpComAnalysisDao.getCountBystatus(projectId, status);
	}
	
	/**
	 * 无普通节点-统计

	 * @return
	 */
	public int getSpecNodeCount(String projectId) {
		return dpComAnalysisDao.getSpecNodeCount(projectId);
	}

	/**
	 * 根据开始及 型号id(可能多个)来进行统计工作
	 * @param moduleIds
	 */
    public List<Map<String,Object>> analysisByModules(String moduleIds,String startTime,String endTime) {
    	//最后要返回的数据集
    	List<Map<String,Object>> modelMapList=new ArrayList<>();
    	//获取符合条件的型号的 mapList
		List<Map<String,Object>> moduleMapList=getmoduleMapListMeetTheTime(moduleIds,startTime,endTime);
		//以型号为主线,存储该型号下的信息
		for (Map<String,Object> moduleMap:moduleMapList){
			Map<String,Object> modelMap=new HashMap<>();
			modelMap.put("moduleCode",moduleMap.get("F_XHDH").toString());
			//获取验收了几次(有几个策划
			List<Map<String,Object>> planMapList=acceptancePlanDao.getPlanListByModuleId(moduleMap.get("ID").toString());
			//剔除不在时间范围内的策划
			List<Map<String,Object>> planMapListMeetTheTime=getPlanMapListMeetTheTime(planMapList,startTime,endTime);
			int countNumberOfPlan=planMapListMeetTheTime.size();
			modelMap.put("countNumberOfPlan",countNumberOfPlan);

			//获取本型号去了多少人
				//获取策划下的团队
			List<AcceptanceGroup> teamList=new ArrayList<>();
			for (Map<String,Object> planMap:planMapListMeetTheTime){
				teamList.addAll(acceptanceGroupDao.getByAcceptancePanId(planMap.get("ID").toString()));
			}
				//获取去验收的人(set
			Set<String> teamMemberSet=new HashSet<>();
			for (AcceptanceGroup acceptanceGroup:teamList){
				teamMemberSet.add(acceptanceGroup.getXm());
			}
			int countNumberOfTeamMember=teamMemberSet.size();
			modelMap.put("countNumberOfTeamMember",countNumberOfTeamMember);

			//获取产品数量
			List<Map<String,Object>> productMapList=new ArrayList<>();
			for (Map<String,Object> planMap:planMapListMeetTheTime){
				productMapList.addAll(productDao.getProductsByPlanId(planMap.get("ID").toString()));
			}
			//将获取到的产品map转为set
			Set<String> productSet=new HashSet<>();
			//剔除掉map中的id防止生成set的时候id不同造成相同的产品被计算多次
			for (Map<String,Object> productMap:productMapList){
				productSet.add(productMap.get("F_CPMC").toString());
			}
			int countNumberOfProduct=productSet.size();
			modelMap.put("countNumberOfProduct",countNumberOfProduct);

			//获取本型号验收用了几天
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//最早开始的策划日期
			Date earlyDate = null;
			//最晚结束的报告日期
			Date laterDate=null;
			for (Map<String,Object> planMap:planMapListMeetTheTime){
				//获取最早的策划的通过时间
				String planTime=planMap.get("F_YSRQ").toString();
				Date planDate=null;
				try {
					planDate=sdf.parse(planTime);
				} catch (ParseException e) {
					System.out.println("时间转换出错,错误码135135");
					e.printStackTrace();
				}
				if(earlyDate==null){
					earlyDate=planDate;
				}else {
					if (earlyDate.getTime()>planDate.getTime()){
						//如果策划的时间比预置时间还早
						earlyDate=planDate;
					}
				}

				//获取最晚的报告通过时间
				String ReportPassedTime=acceptanceReportDao.getByPlanId(planMap.get("ID").toString()).get(0).getZjtgsj();
				Date reportDate=null;
				try {
					reportDate=sdf.parse(ReportPassedTime);
				} catch (ParseException e) {
					System.out.println("时间转换出错,错误码158158");
					e.printStackTrace();
				}
				if (laterDate==null){
					laterDate=reportDate;
				}else {
					if (laterDate.getTime()<reportDate.getTime()){
						//如果总结的时间比预置时间还晚
						laterDate=reportDate;
					}
				}
			}
			//算时间差的条件
			if (earlyDate!=null&&laterDate!=null){
				long diff = laterDate.getTime() - earlyDate.getTime();
				long days = diff / (1000 * 60 * 60 * 24);
				modelMap.put("countNumberOfDays",days);
			}else {
				//开始时间和结束时间有一个为null,  直接给日期为 0
				modelMap.put("countNumberOfDays",0);
			}


			modelMapList.add(modelMap);
		}
		return modelMapList;
    }

	/**
	 * 根据传过来的时间剔除掉不在时间范围内的验收策划
	 * @param planMapList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String, Object>> getPlanMapListMeetTheTime(List<Map<String, Object>> planMapList, String startTime, String endTime) {
		//符合要求时间的验收策划
		List<Map<String, Object>> planMapListMeetTheTime=new ArrayList<>();

		//格式化时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			if (startTime!=""){
				//如果没有选择开始时间
				startDate=sdf.parse(startTime);
			}
			if (endTime!=""){
				//如果没有选择结束时间
				endDate=sdf.parse(endTime);
			}
		} catch (ParseException e) {
			System.out.println("时间转换失败 错误码6565");
			e.printStackTrace();
		}

		//含有总结通过时间的策划
		List<Map<String, Object>> planMapListHasPassedTime=new ArrayList<>();

		//过滤掉没有总结通过时间的策划
		for (Map<String,Object> planMap:planMapList){
			//先确定当前策划是不是有总结
			List<acceptanceReport> acceptanceReportList=acceptanceReportDao.getByPlanId(planMap.get("ID").toString());
			if (acceptanceReportList.size()!=0){
				//确地当前验收是否已通过
				if (acceptanceReportList.get(0).getSpzt().equals("审批通过")){
					//有总结通过时间,可以加入到map里
					planMapListHasPassedTime.add(planMap);
				}
			}
		}

		//以时间为依据过滤策划
		for (Map<String,Object> planMap:planMapListHasPassedTime){
			//获取策划时间
			String planStartTime=planMap.get("F_YSRQ").toString();
			String planEndTime=planMap.get("F_YSJSSJ").toString();
			if ("".equals(planEndTime)){
				//如果没有策划结束时间，则认为该策划未结束，不予分析
				continue;
			}
			//格式化策划时间
			Date planStartDate=null;
			Date planEndDate=null;
			try {
				planStartDate=sdf.parse(planStartTime);
				planEndDate=sdf.parse(planEndTime);
			} catch (ParseException e) {
				System.out.println("时间转换失败 错误码8484");
				e.printStackTrace();
			}
			//如果没有传来需要过滤的时间,直接返回原来的数据
			if (startDate==null&&endDate==null){
				return planMapListHasPassedTime;
			}else if (startDate!=null&&endDate==null){
				//只有开始时间,没有结束时间
				if (planStartDate.getTime()>startDate.getTime()){
					//比要求时间晚的策划,  保留
					planMapListMeetTheTime.add(planMap);
				}
			}else if (startDate==null&&endDate!=null){
				//只有结束时间,没有开始时间
				if (planEndDate.getTime()<endDate.getTime()){
					planMapListMeetTheTime.add(planMap);
				}
			}else {
				//有开始时间和结束时间
				if (planStartDate.getTime()>startDate.getTime()
				&&planEndDate.getTime()<endDate.getTime()){
					planMapListMeetTheTime.add(planMap);
				}
			}
		}
		return planMapListMeetTheTime;
	}

	/**
	 * 获取符合要求的型号的mapList
	 * @param moduleIds
	 * @param startTime
	 * @param endTime
	 * @return
	 */
    public List<Map<String,Object>> getmoduleMapListMeetTheTime(String moduleIds,String startTime,String endTime){
		//格式化时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			if (startTime!=""){
				//如果没有选择开始时间
				startDate=sdf.parse(startTime);
			}
			if (endTime!=""){
				//如果没有选择结束时间
				endDate=sdf.parse(endTime);
			}
		} catch (ParseException e) {
			System.out.println("时间转换失败 错误码6565");
			e.printStackTrace();
		}
		//获取型号id的list
		String[] muduleIdList=moduleIds.split(",");
		List<Map<String,Object>> moduleMapList=new ArrayList<>();
		//获取每个型号的map
		for (String moduleId:muduleIdList){
			moduleMapList.add(moduleDao.getById(moduleId));
		}
		//符合时间要求的型号
		List<Map<String,Object>> moduleMapListMeetTheTime=new ArrayList<>();
		//获取在时间范围内的型号
		for (Map<String,Object> module:moduleMapList){
			String moduleStartTime=module.get("F_KSSJ").toString();
			Date moduleStartDate = null;
			try {
				moduleStartDate=sdf.parse(moduleStartTime);
			} catch (ParseException e) {
				System.out.println("时间转换失败 错误码8484");
				e.printStackTrace();
			}
			if (startDate==null&&endDate==null){
				//没有传来时间, 不进行时间过滤
				moduleMapListMeetTheTime.add(module);
			}else if (startDate==null&&endDate!=null){
				//如果开始时间为空, 就只判断结束时间
				if (moduleStartDate.getTime()<endDate.getTime()){
					//在时间范围内的,留下
					moduleMapListMeetTheTime.add(module);
				}
			}else if (endDate==null&&startDate!=null){
				//如果结束时间为空, 就只判断开始时间
				if (moduleStartDate.getTime()>startDate.getTime()){
					//在时间范围内的,留下
					moduleMapListMeetTheTime.add(module);
				}
			}else {
				//开始时间和结束时间都传来了
				if (moduleStartDate.getTime()>startDate.getTime()
						&&moduleStartDate.getTime()<endDate.getTime()){
					//在时间范围内的,留下
					moduleMapListMeetTheTime.add(module);
				}
			}
		}
		return moduleMapListMeetTheTime;
	}

	/**
	 * 根据部门进行统计分析
	 * @param departments
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> analysisBydepartments(String departments, String startTime, String endTime) {
		//最后要返回的数据集
		List<Map<String,Object>> modelMapList=new ArrayList<>();

		//部门信息集合
		List<SysOrg> sysOrgList=sysOrgDao.getOrgByIds(departments);

		//获取每一个部门的工作统计信息
		for (SysOrg sysOrg:sysOrgList){
			Map<String,Object> modelMap=new HashMap<>();
			//保存当前单位名称
			modelMap.put("departmentName",sysOrg.getOrgName());

				//获取这个部门下的型号
			List<Map<String,Object>> moduleMapListOfThisDepartment=moduleDao.getModuleByOrgId(sysOrg.getOrgId().toString());

				//过滤不在时间区间内的型号
			List<Map<String,Object>> moduleMapListMeetTheTime=getmoduleMapListMeetTheTime(moduleMapListOfThisDepartment,startTime,endTime);

			//保存型号数量
			modelMap.put("countNumberOfModule",moduleMapListMeetTheTime.size());

			//以型号为主线查找策划人员等信息
			for (Map<String,Object> moduleMap:moduleMapListMeetTheTime){

				//获取验收了几次(有几个策划
				List<Map<String,Object>> planMapList=acceptancePlanDao.getPlanListByModuleId(moduleMap.get("ID").toString());
				//剔除不在时间范围内的策划
				List<Map<String,Object>> planMapListMeetTheTime=getPlanMapListMeetTheTime(planMapList,startTime,endTime);
				int countNumberOfPlan=planMapListMeetTheTime.size();
				modelMap.put("countNumberOfPlan",countNumberOfPlan);

				//获取本型号去了多少人
				//获取策划下的团队
				List<AcceptanceGroup> teamList=new ArrayList<>();
				for (Map<String,Object> planMap:planMapListMeetTheTime){
					teamList.addAll(acceptanceGroupDao.getByAcceptancePanId(planMap.get("ID").toString()));
				}
				//剔除不是本单位的人
				teamList=get8DDPTeamList(teamList);
				//获取去验收的人(set
				Set<String> teamMemberSet=new HashSet<>();
				for (AcceptanceGroup acceptanceGroup:teamList){
					teamMemberSet.add(acceptanceGroup.getXm());
				}
				int countNumberOfTeamMember=teamMemberSet.size();
				modelMap.put("countNumberOfTeamMember",countNumberOfTeamMember);

				//获取产品数量
				List<Map<String,Object>> productMapList=new ArrayList<>();
				for (Map<String,Object> planMap:planMapListMeetTheTime){
					productMapList.addAll(productDao.getProductsByPlanId(planMap.get("ID").toString()));
				}
				//将获取到的产品map转为set
				Set<String> productSet=new HashSet<>();
				//剔除掉map中的id防止生成set的时候id不同造成相同的产品被计算多次
				for (Map<String,Object> productMap:productMapList){
					productSet.add(productMap.get("F_CPMC").toString());
				}
				int countNumberOfProduct=productSet.size();
				modelMap.put("countNumberOfProduct",countNumberOfProduct);
				//获取本型号验收用了几天
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//最早开始的策划日期
				Date earlyDate = null;
				//最晚结束的报告日期
				Date laterDate=null;
				for (Map<String,Object> planMap:planMapListMeetTheTime){
					//获取最早的策划的通过时间
					String planTime=planMap.get("F_YSRQ").toString();
					Date planDate=null;
					try {
						planDate=sdf.parse(planTime);
					} catch (ParseException e) {
						System.out.println("时间转换出错,错误码135135");
						e.printStackTrace();
					}
					if(earlyDate==null){
						earlyDate=planDate;
					}else {
						if (earlyDate.getTime()>planDate.getTime()){
							//如果策划的时间比预置时间还早
							earlyDate=planDate;
						}
					}

					//获取最晚的报告通过时间
					String ReportPassedTime=acceptanceReportDao.getByPlanId(planMap.get("ID").toString()).get(0).getZjtgsj();
					Date reportDate=null;
					try {
						reportDate=sdf.parse(ReportPassedTime);
					} catch (ParseException e) {
						System.out.println("时间转换出错,错误码158158");
						e.printStackTrace();
					}
					if (laterDate==null){
						laterDate=reportDate;
					}else {
						if (laterDate.getTime()<reportDate.getTime()){
							//如果总结的时间比预置时间还晚
							laterDate=reportDate;
						}
					}
				}
				//算时间差的条件
				if (earlyDate!=null&&laterDate!=null){
					long diff = laterDate.getTime() - earlyDate.getTime();
					long days = diff / (1000 * 60 * 60 * 24);
					modelMap.put("countNumberOfDays",days);
				}else {
					//开始时间和结束时间有一个为null,  直接给日期为 0
					modelMap.put("countNumberOfDays",0);
				}

			}
			if (moduleMapListMeetTheTime.size()==0){
				//如果这个单位没有型号,对计数器赋 0
				modelMap.put("countNumberOfPlan",0);
				modelMap.put("countNumberOfTeamMember",0);
				modelMap.put("countNumberOfProduct",0);
				modelMap.put("countNumberOfDays",0);
			}
			modelMapList.add(modelMap);
		}
		return modelMapList;
	}
	/**
	 * @Description 剔除掉不是八部的人
	 * @Author ZMZ
	 * @Date 2020/12/4 11:21
	 * @param teamList
	 * @Return java.util.List<com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup>
	 */
	private List<AcceptanceGroup> get8DDPTeamList(List<AcceptanceGroup> teamList) {
		//返回的集合
		List<AcceptanceGroup> teamListOnly8DDP=new ArrayList<>();
		//先确定八部的id
		SysOrg sysOrg=sysOrgDao.getOrgByOrgName("八部");
		String depIdOf8DDP=sysOrg.getOrgId().toString();
		for (AcceptanceGroup teamMember:teamList){
			String depName=teamMember.getDw();
			SysOrg curOrg=sysOrgDao.getOrgByOrgName(depName);
			if (curOrg.getPath().indexOf(depIdOf8DDP)!=-1){
				//如果当前组织的路径里包含8部的id
				teamListOnly8DDP.add(teamMember);
			}
		}
		return teamListOnly8DDP;
	}

	/**
	 * 获取符合要求的型号的mapList
	 * @param moduleMapList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Object>> getmoduleMapListMeetTheTime(List<Map<String,Object>> moduleMapList,String startTime,String endTime){
		//格式化时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			if (startTime!=""){
				//如果没有选择开始时间
				startDate=sdf.parse(startTime);
			}
			if (endTime!=""){
				//如果没有选择结束时间
				endDate=sdf.parse(endTime);
			}
		} catch (ParseException e) {
			System.out.println("时间转换失败 错误码6565");
			e.printStackTrace();
		}
		//符合时间要求的型号
		List<Map<String,Object>> moduleMapListMeetTheTime=new ArrayList<>();
		//获取在时间范围内的型号
		for (Map<String,Object> module:moduleMapList){
			String moduleStartTime=module.get("F_KSSJ").toString();
			Date moduleStartDate = null;
			try {
				moduleStartDate=sdf.parse(moduleStartTime);
			} catch (ParseException e) {
				System.out.println("时间转换失败 错误码8484");
				e.printStackTrace();
			}
			if (startDate==null&&endDate==null){
				//没有传来时间, 不进行时间过滤
				moduleMapListMeetTheTime.add(module);
			}else if (startDate==null&&endDate!=null){
				//如果开始时间为空, 就只判断结束时间
				if (moduleStartDate.getTime()<endDate.getTime()){
					//在时间范围内的,留下
					moduleMapListMeetTheTime.add(module);
				}
			}else if (endDate==null&&startDate!=null){
				//如果结束时间为空, 就只判断开始时间
				if (moduleStartDate.getTime()>startDate.getTime()){
					//在时间范围内的,留下
					moduleMapListMeetTheTime.add(module);
				}
			}else {
				// 开始时间和结束时间都传来了
				if (moduleStartDate.getTime()>startDate.getTime()
				&&moduleStartDate.getTime()<endDate.getTime()){
					//在时间范围内的,留下
					moduleMapListMeetTheTime.add(module);
				}
			}
		}
		return moduleMapListMeetTheTime;
	}

	/**
	 * 靶场(和武器所检)  根据型号进行统计工作情况
	 * @param moduleIds
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> analysisByModulesForBCSYAndWQSJ(String moduleIds, String startTime, String endTime,String ofPart) {
		//最后要返回的数据集
		List<Map<String,Object>> modelMapList=new ArrayList<>();
		//获取符合条件的型号的 mapList
		List<Map<String,Object>> moduleMapList=getmoduleMapListMeetTheTime(moduleIds,startTime,endTime);
		//以型号为主线,存储该型号下的信息
		for (Map<String,Object> moduleMap:moduleMapList){
			Map<String,Object> modelMap=new HashMap<>();
			modelMap.put("moduleCode",moduleMap.get("F_XHDH").toString());
			//获取有几个策划
			List<Map<String,Object>> planMapList=new ArrayList<>();
			if ("BCSY".equals(ofPart)){
				planMapList=rangeTestPlanDao.getPlanListByModuleIdForBCSY(moduleMap.get("ID").toString());
			}else {
				planMapList=rangeTestPlanDao.getPlanListByModuleIdForWQSJ(moduleMap.get("ID").toString());
			}

			//剔除不在时间范围内的策划
			List<Map<String,Object>> planMapListMeetTheTime=getPlanMapListMeetTheTimeForBCSYAndWQSJ(planMapList,startTime,endTime);
			int countNumberOfPlan=planMapListMeetTheTime.size();
			modelMap.put("countNumberOfPlan",countNumberOfPlan);

			//获取本型号去了多少人
			//获取策划下的团队
			List<AcceptanceGroup> teamList=new ArrayList<>();
			for (Map<String,Object> planMap:planMapListMeetTheTime){
				teamList.addAll(acceptanceGroupDao.getByAcceptancePanId(planMap.get("ID").toString()));
			}
			//获取去验收的人(set
			Set<String> teamMemberSet=new HashSet<>();
			for (AcceptanceGroup acceptanceGroup:teamList){
				teamMemberSet.add(acceptanceGroup.getXm());
			}
			int countNumberOfTeamMember=teamMemberSet.size();
			modelMap.put("countNumberOfTeamMember",countNumberOfTeamMember);

			//获取产品数量(  靶场不需要
/*			List<Map<String,Object>> productMapList=new ArrayList<>();
			for (Map<String,Object> planMap:planMapListMeetTheTime){
				productMapList.addAll(productDao.getProductsByPlanId(planMap.get("ID").toString()));
			}
			//将获取到的产品map转为set
			Set<String> productSet=new HashSet<>();
			//剔除掉map中的id防止生成set的时候id不同造成相同的产品被计算多次
			for (Map<String,Object> productMap:productMapList){
				productSet.add(productMap.get("F_CPMC").toString());
			}
			int countNumberOfProduct=productSet.size();
			modelMap.put("countNumberOfProduct",countNumberOfProduct);
*/
			//获取本型号验收用了几天
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			//最早开始的策划日期
			Date earlyDate = null;
			//最晚结束的报告日期
			Date laterDate=null;
			for (Map<String,Object> planMap:planMapListMeetTheTime){
				//获取最早的策划的通过时间
				String planTime=planMap.get("F_KSSJ").toString();
				Date planDate=null;
				try {
					planDate=sdf.parse(planTime);
				} catch (ParseException e) {
					System.out.println("时间转换出错,错误码135135");
					e.printStackTrace();
				}
				if(earlyDate==null){
					earlyDate=planDate;
				}else {
					if (earlyDate.getTime()>planDate.getTime()){
						//如果策划的时间比预置时间还早
						earlyDate=planDate;
					}
				}

				//获取最晚的报告通过时间
				String ReportPassedTime=rangeTestReportDao.getRangeTestReport(planMap.get("ID").toString()).get(0).get("F_ZJTGSJ").toString();
				Date reportDate=null;
				try {
					reportDate=sdf.parse(ReportPassedTime);
				} catch (ParseException e) {
					System.out.println("时间转换出错,错误码158158");
					e.printStackTrace();
				}
				if (laterDate==null){
					laterDate=reportDate;
				}else {
					if (laterDate.getTime()<reportDate.getTime()){
						//如果总结的时间比预置时间还晚
						laterDate=reportDate;
					}
				}
			}
			//算时间差的条件
			if (earlyDate!=null&&laterDate!=null){
				long diff = laterDate.getTime() - earlyDate.getTime();
				long days = diff / (1000 * 60 * 60 * 24);
				modelMap.put("countNumberOfDays",days);
			}else {
				//开始时间和结束时间有一个为null,  直接给日期为 0
				modelMap.put("countNumberOfDays",0);
			}


			modelMapList.add(modelMap);
		}
		return modelMapList;
	}

	/**
	 * 通过时间过滤掉不应该统计的靶场和所检的策划
	 * @param planMapList
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String, Object>> getPlanMapListMeetTheTimeForBCSYAndWQSJ(List<Map<String, Object>> planMapList, String startTime, String endTime) {
		//符合要求时间的验收策划
		List<Map<String, Object>> planMapListMeetTheTime=new ArrayList<>();

		//格式化时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		Date endDate = null;
		try {
			if (startTime!=""){
				//如果没有选择开始时间
				startDate=sdf.parse(startTime);
			}
			if (endTime!=""){
				//如果没有选择结束时间
				endDate=sdf.parse(endTime);
			}
		} catch (ParseException e) {
			System.out.println("时间转换失败 错误码6565");
			e.printStackTrace();
		}

		//含有总结通过时间的策划
		List<Map<String, Object>> planMapListHasPassedTime=new ArrayList<>();

		//过滤掉没有总结通过时间的策划
		for (Map<String,Object> planMap:planMapList){
			//先确定当前策划是不是有总结
			List<Map<String, Object>> reportList=rangeTestReportDao.getRangeTestReport(planMap.get("ID").toString());
			if (reportList.size()!=0){
				//确地当前验收是否已通过
				if (reportList.get(0).get("F_SPZT").equals("审批通过")){
					//有总结通过时间,可以加入到map里
					planMapListHasPassedTime.add(planMap);
				}
			}
		}

		//以时间为依据过滤策划
		for (Map<String,Object> planMap:planMapListHasPassedTime){
			//获取策划时间
			String planStartTime=planMap.get("F_KSSJ").toString();
			String planEndTime=planMap.get("F_JSSJ").toString();
			if ("".equals(planEndTime)){
				//20201204 结束时间可能为空，如果结束时间为空，则认为该任务未结束，不予统计
				continue;
			}
			//格式化策划时间
			Date planStartDate=null;
			Date planEndDate=null;
			try {
				planStartDate=sdf.parse(planStartTime);
				planEndDate=sdf.parse(planEndTime);
			} catch (ParseException e) {
				System.out.println("时间转换失败 错误码8484");
				e.printStackTrace();
			}
			//如果没有传来需要过滤的时间,直接返回原来的数据
			if (startDate==null&&endDate==null){
				return planMapListHasPassedTime;
			}else if (startDate!=null&&endDate==null){
				//只有开始时间,没有结束时间
				if (planStartDate.getTime()>startDate.getTime()){
					//比要求时间晚的策划,  保留
					planMapListMeetTheTime.add(planMap);
				}
			}else if (startDate==null&&endDate!=null){
				//只有结束时间,没有开始时间
				if (planEndDate.getTime()<endDate.getTime()){
					planMapListMeetTheTime.add(planMap);
				}
			}else {
				//有开始时间和结束时间
				if (planStartDate.getTime()>startDate.getTime()
						&&planEndDate.getTime()<endDate.getTime()){
					planMapListMeetTheTime.add(planMap);
				}
			}
		}
		return planMapListMeetTheTime;
	}

	/**
	 * 靶场和武器所检依据部门查询工作信息
	 * @param departments
	 * @param startTime
	 * @param endTime
	 * @param ofPart
	 * @return
	 */
	public List<Map<String, Object>> analysisBydepartmentsForBCSYAndWQSJ(String departments, String startTime, String endTime, String ofPart) {
		//最后要返回的数据集
		List<Map<String,Object>> modelMapList=new ArrayList<>();

		//部门信息集合
		List<SysOrg> sysOrgList=sysOrgDao.getOrgByIds(departments);

		//获取每一个部门的工作统计信息
		for (SysOrg sysOrg:sysOrgList){
			Map<String,Object> modelMap=new HashMap<>();
			//保存当前单位名称
			modelMap.put("departmentName",sysOrg.getOrgName());

			//获取这个部门下的型号
			List<Map<String,Object>> moduleMapListOfThisDepartment=moduleDao.getModuleByOrgId(sysOrg.getOrgId().toString());

			//过滤不在时间区间内的型号
			List<Map<String,Object>> moduleMapListMeetTheTime=getmoduleMapListMeetTheTime(moduleMapListOfThisDepartment,startTime,endTime);

			//保存型号数量
			modelMap.put("countNumberOfModule",moduleMapListMeetTheTime.size());

			//以型号为主线查找策划人员等信息
			for (Map<String,Object> moduleMap:moduleMapListMeetTheTime){

				//获取验收了几次(有几个策划
				List<Map<String,Object>> planMapList=new ArrayList<>();
				if ("WQSJ".equals(ofPart)){
					planMapList=rangeTestPlanDao.getPlanListByModuleIdForWQSJ(moduleMap.get("ID").toString());
				}else if ("BCSY".equals(ofPart)){
					planMapList=rangeTestPlanDao.getPlanListByModuleIdForBCSY(moduleMap.get("ID").toString());
				}

				//剔除不在时间范围内的策划
				List<Map<String,Object>> planMapListMeetTheTime=getPlanMapListMeetTheTimeForBCSYAndWQSJ(planMapList,startTime,endTime);
				int countNumberOfPlan=planMapListMeetTheTime.size();
				modelMap.put("countNumberOfPlan",countNumberOfPlan);

				//获取本型号去了多少人
				//获取策划下的团队
				List<AcceptanceGroup> teamList=new ArrayList<>();
				for (Map<String,Object> planMap:planMapListMeetTheTime){
					teamList.addAll(acceptanceGroupDao.getByAcceptancePanId(planMap.get("ID").toString()));
				}
				//剔除不是本单位的人
				teamList=get8DDPTeamList(teamList);
				//获取去验收的人(set
				Set<String> teamMemberSet=new HashSet<>();
				for (AcceptanceGroup acceptanceGroup:teamList){
					teamMemberSet.add(acceptanceGroup.getXm());
				}
				int countNumberOfTeamMember=teamMemberSet.size();
				modelMap.put("countNumberOfTeamMember",countNumberOfTeamMember);

				//获取本型号验收用了几天
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//最早开始的策划日期
				Date earlyDate = null;
				//最晚结束的报告日期
				Date laterDate=null;
				for (Map<String,Object> planMap:planMapListMeetTheTime){
					//获取最早的策划的时间
					String planTime=planMap.get("F_KSSJ").toString();
					Date planDate=null;
					try {
						planDate=sdf.parse(planTime);
					} catch (ParseException e) {
						System.out.println("时间转换出错,错误码135135");
						e.printStackTrace();
					}
					if(earlyDate==null){
						earlyDate=planDate;
					}else {
						if (earlyDate.getTime()>planDate.getTime()){
							//如果策划的时间比预置时间还早
							earlyDate=planDate;
						}
					}

					//获取最晚的报告通过时间
					String ReportPassedTime=rangeTestReportDao.getRangeTestReport(planMap.get("ID").toString()).get(0).get("F_ZJTGSJ").toString();
					Date reportDate=null;
					try {
						reportDate=sdf.parse(ReportPassedTime);
					} catch (ParseException e) {
						System.out.println("时间转换出错,错误码158158");
						e.printStackTrace();
					}
					if (laterDate==null){
						laterDate=reportDate;
					}else {
						if (laterDate.getTime()<reportDate.getTime()){
							//如果总结的时间比预置时间还晚
							laterDate=reportDate;
						}
					}
				}
				//算时间差的条件
				if (earlyDate!=null&&laterDate!=null){
					long diff = laterDate.getTime() - earlyDate.getTime();
					long days = diff / (1000 * 60 * 60 * 24);
					modelMap.put("countNumberOfDays",days);
				}else {
					//开始时间和结束时间有一个为null,  直接给日期为 0
					modelMap.put("countNumberOfDays",0);
				}

			}
			if (moduleMapListMeetTheTime.size()==0){
				//如果这个单位没有型号,对计数器赋 0
				modelMap.put("countNumberOfPlan",0);
				modelMap.put("countNumberOfTeamMember",0);
				modelMap.put("countNumberOfDays",0);
			}
			modelMapList.add(modelMap);
		}
		return modelMapList;
	}
}
