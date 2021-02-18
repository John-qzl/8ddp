package com.cssrc.ibms.core.resources.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.xalan.xsltc.compiler.sym;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.io.bean.ProductCategoryBath;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.user.service.UserRoleService;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;

@Controller
@RequestMapping("/model/user/role/")
public class ProjectUserRoleController extends BaseController{
	@Resource
	private ModuleService moduleService;
	@Resource
	private ProductCategoryBatchDao productCategoryBatchDao;
	@Resource
	private ModuleManageService moduleManageService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private UserRoleService userRoleService;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private RangeTestPlanDao rangeTestPlanDao;

	/**
	 * 根据当前req中传来的moduleId来确定当前人是不是型号管理员
	 * 是的话返回 1
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUseRole")
	@ResponseBody
	public Map<String, Object> getUseRole(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		String moduleId=request.getParameter("moduleId");
		Map<String, Object> moduleMap=moduleService.getByModelId(moduleId);
		/*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
		List<String> userIdList=moduleManageService.getByModuleId(moduleId,"manage");
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		else if(userIdList.contains(curUser.getUserId().toString())){
			map.put("useRole", "1");
			return map;
		}
		return map;
	}
	
	/**
	 * 武器系统缩所检和靶场实验
	 * 是的话返回 1
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("getUseRoleByTest")
	@ResponseBody
	public Map<String, Object> getUseRoleByTest(HttpServletRequest request, HttpServletResponse response) {
 		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		String moduleId=request.getParameter("moduleId");
		String type=request.getParameter("type");
		
		Map<String, Object> moduleMap=moduleService.getByModelId(moduleId);
		/*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
		List<String> userIdList=moduleManageService.getByModuleId(moduleId,"manage");
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		else if(userIdList.contains(curUser.getUserId().toString())){
			map.put("useRole", "1");
			return map;
		}
		List<Map<String, Object>> testRangeList=rangeTestPlanDao.getByMoudleId(moduleId, type);
		if(testRangeList.size()!=0) {
			for (Map<String, Object> testRange : testRangeList) {
				if(CommonTools.Obj2String(testRange.get("F_SYZZID")).equals(curUser.getUserId().toString())) {
					map.put("useRole", "1");
					return map;
				}
			}
		}
		return map;
	}
	
	
	
	@RequestMapping("getUseFcRole")
	@ResponseBody
	public Map<String, Object> getUseFcRole(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		String fcId=request.getParameter("fcId");
		Map<String, Object> categoryBatchMap=productCategoryBatchDao.getById(fcId);
		String moduleId=categoryBatchMap.get("F_SSXH").toString();
		
		Map<String, Object> moduleMap=moduleService.getByModelId(moduleId);
		/*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
		List<String> userIdList=moduleManageService.getByModuleId(moduleId,"manage");
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		else if(userIdList.contains(curUser.getUserId().toString())){
			map.put("useRole", "1");
			return map;
		}
		List<Map<String,Object>> acceptancePlanList=acceptancePlanDao.getByModuleId(moduleId);
		for (Map<String, Object> acceptancePlan : acceptancePlanList) {
			String fcBathId=CommonTools.Obj2String(acceptancePlan.get("F_SSCPPC"));
			Map<String, Object> productCategoryBathMap=productCategoryBatchDao.getById(fcBathId);
			if(CommonTools.Obj2String(productCategoryBathMap.get("F_SSCPLB")).equals(fcId)) {
				if(CommonTools.Obj2String(acceptancePlan.get("F_YSZZID")).equals(curUser.getUserId().toString())) {
					map.put("useRole", "1");
					return map;
				}
			}
		}
		return map;
	}
	
	@RequestMapping("isManager")
	@ResponseBody
	public Map<String, Object> isManager(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		return map;
	}
	@RequestMapping("isExist")
	@ResponseBody
	public Map<String, Object> isExist(HttpServletRequest request, HttpServletResponse response) {
		String dataType=request.getParameter("dataType");
		String dataId=request.getParameter("dataId");
		String fullName=request.getParameter("fullName");
		Map<String, Object> map=new HashMap<String, Object>();
		List<SysUser> sysUser=(List<SysUser>) sysUserService.getByFullname(fullName);
		map.put("exist", "0");
		if(sysUser.size()!=0) {
			if(dataType.equals("roleResFilter")) {
				String roleId=dataId;
				List<Long> userRoleList =userRoleService.getUserIdsByRoleId(Long.valueOf(roleId));
				List<String> userList=new ArrayList<>();
				for (SysUser user : sysUser) {
					if(userRoleList.contains(user.getUserId())){
						map.put("exist", "1");
					}
				}
			}
			else if(dataType.equals("modelManageFilter")) {
				String modelId=dataId;
				List<String> userList = moduleManageService.getByModuleIdDis(modelId);
				for (SysUser user : sysUser) {
					if(userList.contains(user.getUserId().toString())){
						map.put("exist", "1");
					}
				}
			}
		}
		return map;
	}
	@RequestMapping("getAcceptanceRole")
	@ResponseBody
	public Map<String, Object> getAcceptanceRole(HttpServletRequest request, HttpServletResponse response) {
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		String batchId=request.getParameter("batchId");
		Map<String, Object> bathMap=productCategoryBatchDao.getById(batchId);
		String xh=bathMap.get("F_SSXH").toString();
		List<String> userIdList=moduleManageService.getByModuleId(xh,"manage");
		 if(userIdList.size()==0) {
			map.put("useRole", "0");
			return map;
		}
		for (String string : userIdList) {
			String curUserId=curUser.getUserId().toString();
			if(string.equals(curUserId)) {
				map.put("useRole", "1");
				return map;
			}
		}
		return map;
	}
	@RequestMapping("getRoleByPlanId")
	@ResponseBody
	public Map<String, Object> getRoleByPlanId(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		String acceptancePlanId=request.getParameter("acceptancePlanId");
		Map<String, Object> acceptancePlan=acceptancePlanDao.getMapById(acceptancePlanId);
		/*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
		List<String> userIdList=moduleManageService.getByModuleId(CommonTools.Obj2String(acceptancePlan.get("F_SSXHID")),"manage");
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		else if(userIdList.contains(curUser.getUserId().toString())){
			map.put("useRole", "1");
			return map;
		}
		else if(curUser.getUserId().toString().equals(CommonTools.Obj2String(acceptancePlan.get("F_YSZZID").toString()))){
			map.put("useRole", "1");
			return map;
		}
		return map;
	}
	@RequestMapping("getRoleByRangeId")
	@ResponseBody
	public Map<String, Object> getRoleByRangeId(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		String missionId=request.getParameter("missionId");
		RangeTestPlan rangeTestPlan=rangeTestPlanDao.selectById(missionId);
		/*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
		List<String> userIdList=moduleManageService.getByModuleId(rangeTestPlan.getF_XHID(),"manage");
		map.put("useRole", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			map.put("useRole", "1");
			return map;
		}
		else if(userIdList.contains(curUser.getUserId().toString())){
			map.put("useRole", "1");
			return map;
		}
		return map;
	}
	

	//获取策划下发权限
		@RequestMapping("getRangeDownRole")
		@ResponseBody
		public Map<String, Object> getRangeDownRole(HttpServletRequest request, HttpServletResponse response) {
			// 获取当前用户
			ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
			Map<String, Object> map=new HashMap<String, Object>();
			String moduleId=request.getParameter("moduleId");
			String acceptancePlanId=request.getParameter("acceptancePlanId");
			Map<String, Object> moduleMap=moduleService.getByModelId(moduleId);
			RangeTestPlan rangePlanMap=rangeTestPlanDao.selectById(acceptancePlanId);
			List<String> userIdList=moduleManageService.getByModuleId(moduleId,"manage");
			map.put("useRole", "0");
			if(curUser.getRoles().indexOf("sjgly")>=0) {
				map.put("useRole", "1");
				return map;
			}
			else if(userIdList.contains(curUser.getUserId().toString())){
				map.put("useRole", "1");
				return map;
			}
			else if(curUser.getUserId().toString().equals(CommonTools.Obj2String(rangePlanMap.getF_SYZZID()))){
				map.put("useRole", "1");
				return map;
			}
			return map;
		}
}
