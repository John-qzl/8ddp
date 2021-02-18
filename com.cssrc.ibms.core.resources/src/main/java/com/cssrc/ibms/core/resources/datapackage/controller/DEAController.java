package com.cssrc.ibms.core.resources.datapackage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.product.acceptance.bean.ActualData;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductActual;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.product.acceptance.dao.ActualDataDao;
import com.cssrc.ibms.dp.product.infor.dao.ProductInforDao;
import com.fr.report.core.A.a;
import com.fr.report.core.A.n;
import com.fr.report.core.A.p;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.resources.datapackage.service.DEAService;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.core.resources.product.service.ProductService;
import com.cssrc.ibms.core.resources.project.service.ProjectService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping("/dataPackage/dea/")
public class DEAController extends BaseController {
	
	@Resource 
	private ProjectService projectService;
	@Resource 
	private DEAService deaService;
	@Resource
	private ProductService productService;
	@Resource
	private ProductTypeDao productTypeDao ;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private IOTableInstanceDao iOTableInstanceDao;
	@Resource
	private ModuleService moduleService;
	@Resource
	private ProductDao productDao;
	@Resource
	private ProductCategoryBatchDao productCategoryBatchDao;
	@Resource
	private ActualDataDao actualDataDao;
	@Resource
	private ProductInforDao productInforDao;
	@RequestMapping("/getQueryInfo")
	@Action(description = "获取查询条件")
	public ModelAndView getQueryInfo(HttpServletRequest request, HttpServletResponse response) {
	/*	// 获取型号id
		String productId = request.getParameter("xhId");
		String flagType = CommonTools.null2String((request.getParameter("type"))) ;
		if("carry".equals(flagType)){
			flagType = "运载" ;
		}else if("space".equals(flagType)){
			flagType = "空间" ;
		}else{
			flagType = "结构机构" ;
		}*/
		List<Map<String, Object>> productInfo = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> projectInfo = new ArrayList<Map<String,Object>>();;
		// 所有型号
		productInfo = moduleService.getAllModules();
		
		
		ModelAndView modelAndView = new ModelAndView("/dataPackage/dp/dataPackageDEA.jsp");
		return 	modelAndView.addObject("projectInfo", projectInfo)
				/*.addObject("productId", productId)*/
				.addObject("productInfo", productInfo);
	}
	
	@RequestMapping("/getQueryInfoByFrom")
	@Action(description = "获取查询条件")
	public ModelAndView getQueryInfoByFrom(HttpServletRequest request, HttpServletResponse response) {
		String flagType = "" ;

		// 获取检查结果id
		String ckResultId = request.getParameter("ckResultId");
		// 获取表单实例id
		String slid = request.getParameter("slid");
		Map typeMap = productTypeDao.getProductType(Long.valueOf(slid)) ;

		List<Map<String, Object>> productInfo = productService.queryTreeAllByType(String.valueOf(typeMap.get("TYPE")));

				
		ModelAndView modelAndView = new ModelAndView("oa/editor/dataPackageDEA.jsp");
		return 	modelAndView.addObject("productInfo", productInfo)
				.addObject("ckResultId", ckResultId)
				.addObject("slid", slid);
	}
	@RequestMapping("/getProductByBath")
	@Action(description = "根据批次获取产品")
	@ResponseBody
	public List<ProductInfo> getProductByBath(HttpServletRequest request, HttpServletResponse response) {
		String batchId = request.getParameter("batchId");
		Map<String, Object> bathMap=productCategoryBatchDao.getById(batchId);
		List<ProductInfo> productInfoList=new ArrayList<>();
		List<Map<String, Object>> acceptancePlanList=acceptancePlanDao.getApprovedPlansByProductBatchId(batchId);
		if(acceptancePlanList!=null||acceptancePlanList.size()!=0) {
			for (Map<String, Object>  acceptancePlan : acceptancePlanList) {
				List<Map<String, Object>> acceptanceReportList=new ArrayList<>();
				acceptanceReportList=acceptanceReportDao.getAcceptanceReport( CommonTools.Obj2String(acceptancePlan.get("ID")));
				for (Map<String, Object> acceptanceReport : acceptanceReportList) {
					if(CommonTools.Obj2String(acceptanceReport.get("F_SPZT")).equals("审批通过")) {
						List<TableInstance> tableInstanceList=iOTableInstanceDao.getByPlanId(CommonTools.Obj2String(acceptancePlan.get("ID")));
						for (TableInstance tableInstance : tableInstanceList) {
							if(!tableInstance.getStatus().equals("废弃")) {
								productInfoList.addAll(productInforDao.getByInstanceId(tableInstance.getId()));
							}
						}
					}
				}
			}
		}
		for (ProductInfo productInfo : productInfoList) {
			productInfo.setPcchbh(CommonTools.Obj2String(bathMap.get("F_PCH")));
		}
		return productInfoList;
	}
		
		
		@RequestMapping("/getProductByBaths")
		@Action(description = "根据多个批次获取产品")
		@ResponseBody
		public List<ProductInfo> getProductByBaths(HttpServletRequest request, HttpServletResponse response) {
			String batchIdArray = request.getParameter("batchIdArray");
			List<ProductInfo> productInfoList=new ArrayList<>();
			String[] batchIds=batchIdArray.split(",");
			for (String batchId : batchIds) {
				Map<String, Object> bathMap=productCategoryBatchDao.getById(batchId);
				List<Map<String, Object>> acceptancePlanList=acceptancePlanDao.getApprovedPlansByProductBatchId(batchId);
				if(acceptancePlanList!=null||acceptancePlanList.size()!=0) {
					for (Map<String, Object>  acceptancePlan : acceptancePlanList) {
						List<Map<String, Object>> acceptanceReportList=new ArrayList<>();
						acceptanceReportList=acceptanceReportDao.getAcceptanceReport( CommonTools.Obj2String(acceptancePlan.get("ID")));
						for (Map<String, Object> acceptanceReport : acceptanceReportList) {
							if(CommonTools.Obj2String(acceptanceReport.get("F_SPZT")).equals("审批通过")) {
								List<TableInstance> tableInstanceList=iOTableInstanceDao.getByPlanId(CommonTools.Obj2String(acceptancePlan.get("ID")));
								for (TableInstance tableInstance : tableInstanceList) {
									if(!tableInstance.getStatus().equals("废弃")) {
										List<ProductInfo> list=productInforDao.getByInstanceId(tableInstance.getId());
										for (ProductInfo productInfo : list) {
											productInfo.setPcchbh(CommonTools.Obj2String(bathMap.get("F_PCH")));
											productInfo.setSsxhfc(bathMap.get("ID").toString());
										}
										productInfoList.addAll(list);
									}
								}
							}
						}
					}
				}
			}
		return productInfoList;
	}

		@RequestMapping("/getActualData")
		@Action(description = "获取验收数据")
		@ResponseBody
		public Map<String, Object> getActualData(HttpServletRequest request, HttpServletResponse response) {
			
			Map<String, Object> map=new HashMap<String, Object>();
			String productIds = request.getParameter("productIds");
			int index=Integer.valueOf(request.getParameter("index"));
			List<ProductActual> actualDataList=new ArrayList<>();
			List<String> titleList=new ArrayList<>();
			List<Map<String, Object>> data=new ArrayList<>();
			String[] productArray=productIds.split(",");
			List<String> productName=new ArrayList<String>();
			List<Map<String, Object>> projectList=actualDataDao.getProjectTitle(productIds.substring(0,productIds.length()-1));
			int j=0;
			for (Map<String, Object> project : projectList) {
				List<Map<String, Object>> productList=actualDataDao.getByProductId(productIds.substring(0,productIds.length()-1),CommonTools.Obj2String(project.get("F_YSXM")));
				Map<String, Object> colum=new LinkedHashMap<>();
				if(productList.size()!=0) {
					colum=productList.get(0);
				}
				for(int i=0;i<productList.size();i++) {
					colum.put(CommonTools.Obj2String(productList.get(i).get("F_CPMC")), productList.get(i).get("F_SCZ"));
					if(j==0) {
						productName.add(CommonTools.Obj2String(productList.get(i).get("F_CPMC")));
					}
					
				}
				data.add(colum);
				j++;
			}
			boolean check=true;
			if(data.size()!=0) {
				int c=data.size()%10;
				int b=data.size()/10;
				if(c==0&&index>c) {
					check=false;
				}
				else if(c!=0&&index>b+1) {
					check=false;
				}
				else if(index>b) {
					int number=(b)*10;
					data=data.subList(number, number+c);
				}
				else {
					int number=(index-1)*10;
					data=data.subList(number, number+10);
				}
			}
			map.put("data", data);
			map.put("productNameList", productName);
			map.put("check", check);
			return map;
		}
	@RequestMapping("/query")
	@Action(description = "查询")
	@ResponseBody
	public List<Map<String, Object>> query(HttpServletRequest request, HttpServletResponse response) {
		// 获取型号id
		String productId = request.getParameter("productId");
		// 获取发次id
		String projectId = request.getParameter("projectId");
		// 获取关键参数
		String args = request.getParameter("args");
		//获取型号的类型
		String resultTableName = productTypeDao.getProductTypeByProductId(Long.valueOf(productId.split(",")[0])) ;
		List<Map<String, Object>> dataList = deaService.query(productId, projectId, args, "", "", resultTableName);

		return dataList;
	}
	
	@RequestMapping("/queryByFrom")
	@Action(description = "查询")
	@ResponseBody
	public List<Map<String, Object>> queryByFrom(HttpServletRequest request, HttpServletResponse response) {
		// 获取型号id
		String productId = request.getParameter("productId");
		// 获取发次id
		String projectId = request.getParameter("projectId");
		// 获取检查结果id
		String ckResultId = request.getParameter("ckResultId");
		// 获取实例id
		String slid = request.getParameter("slid");
		Map typeMap = productTypeDao.getProductType(Long.valueOf(slid)) ;
		String ck_resultName = "" ;
		if("空间".equals(typeMap.get("TYPE"))){
			ck_resultName = "W_CK_RESULT" ;
		}else if("运载".equals(typeMap.get("TYPE"))){
			ck_resultName = "W_CK_RESULT_CARRY" ;
		}else{
			ck_resultName = "W_CK_RESULT_JGJG" ;
		}
		List<Map<String, Object>> dataList = deaService.query(productId, projectId, "", ckResultId, slid,ck_resultName);

		return dataList;
	}
	
	@RequestMapping("/getIControlChart")
	@Action(description = "单值控制图")
	@ResponseBody
	public Map<String, Object> getIControlChart(HttpServletRequest request, HttpServletResponse response) {
		// 获取成功数据
		String succData = request.getParameter("succData");
		//正态性校验
		//boolean flag = deaService.isNormalDistribution(succData);
		//判断是否有离群值
		
		//包络分析
		Map<String, Object> dataMap = deaService.getIControlChart(succData);

		return dataMap;
	}
	
	@RequestMapping("/getXControlChart")
	@Action(description = "均值控制图")
	@ResponseBody
	public Map<String, Object> getXControlChart(HttpServletRequest request, HttpServletResponse response) {
		// 获取成功数据
		String sucData = request.getParameter("sucData");
		// 获取待分析数据
		String analyData = request.getParameter("analyData");
		//包络分析
		Map<String, Object> dataMap = deaService.getXControlChart(sucData, analyData);

		return dataMap;
	}
}
