package com.cssrc.ibms.core.resources.datapackage.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;
import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.resources.datapackage.service.DataPackageService;
import com.cssrc.ibms.core.resources.datapackage.service.DpComAnalysisService;
import com.cssrc.ibms.core.resources.project.service.ProjectService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping("/dataPackage/dp/dpCom/")
public class DpComAnalysisController extends BaseController {
	
	@Resource
	private DpComAnalysisService dpComAnalysisService;
	@Resource
	private DataPackageService dpService;
	@Resource 
	private ProjectService projectService;
	@Resource
	private ModuleService moduleService;
	@Resource
	private ISysOrgService sysOrgService;


	/**
	 * 工作统计页面，用于选择型号
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/analysis")
	@Action(description = "发次数据统计")
	public ModelAndView getDpComAnalysisData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return getAutoView();
	}

	/**
	 * 根据型号进行搜索的结果页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/searchFormByModules")
	public ModelAndView searchFormByModules(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String moduleIds=request.getParameter("modules");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String ofPart=request.getParameter("ofPart");
		List<Map<String,Object>> modelMapList=new ArrayList<>();

		ModelAndView modelAndView=new ModelAndView();
		//产品验收的统计
		if ("YSCH".equals(ofPart)){
			modelMapList=dpComAnalysisService.analysisByModules(moduleIds,startTime,endTime);
			modelAndView.setViewName("/dataPackage/dp/searchFormByModules.jsp");
		}else if ("BCSY".equals(ofPart)){
			modelMapList=dpComAnalysisService.analysisByModulesForBCSYAndWQSJ(moduleIds,startTime,endTime,ofPart);
			modelAndView.setViewName("/dataPackage/dp/searchFormByModulesForBCSY.jsp");
		}else {
			modelMapList=dpComAnalysisService.analysisByModulesForBCSYAndWQSJ(moduleIds,startTime,endTime,ofPart);
			modelAndView.setViewName("/dataPackage/dp/searchFormByModulesForWQSJ.jsp");
		}

		modelAndView.addObject("modelMapList",modelMapList);
		return modelAndView;
	}

	/**
	 * 根据部门进行搜索的结果页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/searchFormByDepartments")
	public ModelAndView searchFormByDepartment(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String departments=request.getParameter("departments");
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		String ofPart=request.getParameter("ofPart");
		List<Map<String,Object>> modelMapList=new ArrayList<>();

		ModelAndView modelAndView=new ModelAndView();

		//产品验收的统计
		if ("YSCH".equals(ofPart)){
			modelMapList=dpComAnalysisService.analysisBydepartments(departments,startTime,endTime);
			modelAndView.setViewName("/dataPackage/dp/searchFormBydepartments.jsp");
		}else if ("BCSY".equals(ofPart)){
			modelMapList=dpComAnalysisService.analysisBydepartmentsForBCSYAndWQSJ(departments,startTime,endTime,ofPart);
			modelAndView.setViewName("/dataPackage/dp/searchFormBydepartmentsForBCSY.jsp");
		}else {
			modelMapList=dpComAnalysisService.analysisBydepartmentsForBCSYAndWQSJ(departments,startTime,endTime,ofPart);
			modelAndView.setViewName("/dataPackage/dp/searchFormBydepartmentsForWQSJ.jsp");
		}

		modelAndView.addObject("modelMapList",modelMapList);
		return modelAndView;
	}

	
	@RequestMapping("/getCountData")
	@Action(description = "执行状态数据统计")
	@ResponseBody
	public List<Map<String, Object>> getCountData(HttpServletRequest request, HttpServletResponse response) {
		// 获取型号id
		String productId = request.getParameter("productId");
		//统计项
		String[] countType = {"未开始", "待下载", "进行中", "已完成待下载", "已完成", "已终止"};
		//统计结果
		List<Map<String, Object>> countInfo = new ArrayList<Map<String, Object>>();	
		
		// 获取某型号下所有发次统计信息
		List<Map<String, Object>> projectInfo = projectService.queryProjectNodeById(productId);
		if (projectInfo.size() > 0) {
			for (int i = 0; i < projectInfo.size(); i++) {
				Map<String, Object> newMap = new HashMap<String, Object>();
				String projectId = CommonTools.Obj2String(projectInfo.get(i).get("ID"));
				String projectName = CommonTools.Obj2String(projectInfo.get(i).get("F_FCMC"));
				newMap.put("FCNAME", projectName);
				
				int total = 0;
				String countString = "";
				for (int j = 0; j < countType.length; j++) {
					int count = dpComAnalysisService.getCountBystatus(projectId, countType[j]);
					total += count;
					countString += count + ",";
				}
				countString += total;
				newMap.put("countStr", countString);
				countInfo.add(newMap);
			}
		}
		
		return countInfo;
	}
}
