package com.cssrc.ibms.dp.product.acceptance.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.dao.IOConventionalDao;
import com.cssrc.ibms.core.resources.io.dao.IOTableInstanceDao;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;
import com.cssrc.ibms.core.resources.product.service.ProductCategoryBatchService;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.form.model.SignResult;
import com.cssrc.ibms.dp.form.service.FormService;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.product.acceptance.service.AcceptancePlanService;
import com.cssrc.ibms.dp.product.infor.dao.ProductInforDao;
import com.cssrc.ibms.dp.product.infor.service.ProductInforService;
import com.cssrc.ibms.dp.sync.bean.Conventional;
import com.cssrc.ibms.system.dao.SysParameterDao;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SerialNumberService;
import com.cssrc.ibms.system.service.SysFileService;
import com.fr.base.FRContext;
import com.fr.base.Parameter;
import com.fr.dav.LocalEnv;
import com.fr.general.ModuleContext;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.PDFExporter;
import com.fr.main.impl.WorkBook;
import com.fr.main.workbook.ResultWorkBook;
import com.fr.report.module.EngineModule;
import com.fr.stable.WriteActor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description 产品验收策划控制器
 * @author xie chen
 * @date 2019年12月11日 下午6:20:08
 * @version V1.0
 */
@Controller
@RequestMapping("/product/acceptance/plan/")
public class AcceptancePlanController extends BaseController {

	@Resource
	private AcceptancePlanService service;
	@Resource
	private ProductCategoryBatchService productCategoryBatchService;
	@Resource
	private ProductInforService productInforService;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private FormService formService;
	@Resource
	private SignResultDao signResultDao;
	@Resource
	private SysFileService sysFileService;
	@Resource
	private ProductDao productDao;
	@Resource
	private AcceptanceReportDao acceptanceReportDao;
	@Resource
	private SysParameterDao parameterDao;
	@Resource
	private ProductInforDao productInforDao;
	@Resource
	private IOTableInstanceDao iOTableInstanceDao;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private IOConventionalDao iOConventionalDao;
	@Resource
	private SerialNumberService serialNumberService;
	@Resource
	private WorkBoardDao workBoardDao;
	@Resource
	private AcceptanceMessageDao acceptanceMessageDao;
	@Resource
	private SysOrgService sysOrgService;
	@Resource
	private SysUserDao sysUserDao;
	/**
	 * @Desc 更新验收策划审批状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateApproveStatus")
	public void updateApproveStatus(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		String status = request.getParameter("status");
		service.updateApproveStatus(acceptancePlanId, status);
	}

	/**
	 * @Desc 更新验收策划归档状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateFileStatus")
	public void updateFileStatus(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		String status = request.getParameter("status");
		service.updateFileStatus(acceptancePlanId, status);
	}

	/**
	 * @Desc 校验策划编号是否重复
	 * @param request
	 * @param response
	 */
	@RequestMapping("checkPlanNumber")
	@ResponseBody
	public String checkPlanNumber(HttpServletRequest request, HttpServletResponse response) {
		String acceptanceNumber = request.getParameter("acceptanceNumber");
		return service.checkPlanNumber(acceptanceNumber);
	}
	/**
	 * @Desc 删除数据时同时删除草稿
	 * @param request
	 * @param response
	 */
	@RequestMapping("topDelete")
	@ResponseBody
	public Map<String, Object> topDelete(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map=new HashMap<String, Object>();
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		String ids = request.getParameter("Ids");
		map.put("message", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			service.topDelete(ids);
			map.put("message", "1");
			return map;
		}
		Map<String, Object> acceptanceMap=service.getPlansById(ids);
		if(CommonTools.Obj2String(acceptanceMap.get("F_JSFZRID")).equals(CommonTools.Obj2String(curUser.getUserId()))) {
			service.topDelete(ids);
			map.put("message", "1");
			return map;
		}
		return map;
	}
	/**
	 * @Desc 获取策划编号
	 * @param request
	 * @param response
	 */
	@RequestMapping("getPlanNumber")
	@ResponseBody
	public String getPlanNumber(HttpServletRequest request, HttpServletResponse response) {
		return serialNumberService.getCurIdByAlias("yschbh");
	}

	/**
	 * @Desc 已废弃（原新增策划，提交审批按钮后置事件，现移至节点后置事件）
	 * @param request
	 * @param response
	 */
	@RequestMapping("applySubmit")
	public void applySubmit(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		String status = request.getParameter("status");
		// 1.更新验收策划审批状态
		service.updateApproveStatus(acceptancePlanId, status);
		// 2.自动添加验收策划组组长信息
		service.autoAddGroupManager(acceptancePlanId);
	}

	@RequestMapping({ "getTreeData" })
	@ResponseBody
	@Action(description = "获取产品批次-验收策划结构树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String productBatchId = request.getParameter("batchId");
		String productBatchKey = request.getParameter("batchKey");
		Map<String, Object> productBatch = productCategoryBatchService.getById(productBatchId);
		List<String> acceptancePlanTree = new ArrayList<String>();
		// 1）产品批次基本信息
		String productBatchUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200168\"";
		// 2）产品-验收策划
		String acceptancePlanUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021790753\"";

		// 根节点-当前产品批次节点
		String rootNode = "{id:" + productBatchId + ", parentId:-1" + ", name:\"" + productBatchKey
				+ "\", type:\"productBatch\" , clickUrl:" + productBatchUrl + ", target : \"listFrame\",open:true}";
		/*
		 * 去除根节点 acceptancePlanTree.add(rootNode);
		 */
		List<Map<String, Object>> acceptancePlans = service.getPlansByProductBatchId(productBatchId);
		for (Map<String, Object> planMap : acceptancePlans) {
			if ("审批通过".equals(planMap.get("F_SPZT"))) {
				// 策划审批通过
				String planKey = CommonTools.Obj2String(planMap.get("F_CHBGBBH"));
				String node = "{id:" + planMap.get("ID") + ",parentId:" + productBatchId + ",productBatchId:"
						+ productBatchId + ", productCategoryId:" + productBatch.get("F_SSCPLB") + ", name:\"" + planKey
						+ "\", type:\"acceptancePlan\" , clickUrl:" + acceptancePlanUrl
						+ ", target : \"listFrame\",open:true}";
				acceptancePlanTree.add(node);
				/*
				 * // 构造假数据--用于描述pad效果 String batchNode = "{id:1" + ",parentId:" +
				 * planMap.get("ID") + ", name:\"" + productBatchKey +"批次"+ "\", type:\"batch\""
				 * + ", target : \"listFrame\",open:true}"; acceptancePlanTree.add(batchNode);
				 * List<Map<String, Object>> products =
				 * productInforService.getByAcceptancePlanId(CommonTools.Obj2String(planMap.get(
				 * "ID"))); for (Map<String, Object> product : products) { String productNode =
				 * "{id:" + product.get("ID") + ",parentId:" + planMap.get("ID") + ", name:\"" +
				 * product.get("F_CPBH") + "\", type:\"acceptancePlan\"" +
				 * ", target : \"listFrame\",open:true}"; acceptancePlanTree.add(productNode); }
				 */
			}
		}
		response.getWriter().print(JSONArray.fromObject(acceptancePlanTree).toString());
	}

	/**
	 * @Desc 下发依据文件
	 * @param request
	 * @param response
	 */
	@RequestMapping("filesAssign")
	public void filesAssign(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		service.updateFilesAssignStatus(acceptancePlanId, "已下发");
	}

	@RequestMapping({ "getAcceptancePlanById" })
	@ResponseBody
	@Action(description = "获取id对应的策划信息")
	public Map<String, Object> getAcceptancePlanById(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		Map<String, Object> params = service.getPlansById(acceptancePlanId);
		params.put("F_CPBH", productDao.getCpMc(acceptancePlanId));
		params.put("F_CPSL", productDao.getCpNumber(acceptancePlanId));
		params.put("signList", null);
		String data = (String) params.get("F_YSBGHCSJ");
		JSONObject jsonObject = JSONObject.fromObject(data);
		if (jsonObject.isEmpty()) {
			return null;
		}
		JSONArray jsonArray = jsonObject.getJSONArray("signreses");
		if (jsonArray.size() != 0) {
			JSONObject jsonObject2 = jsonArray.getJSONObject(0);
			String signId = jsonObject2.getString("signid");
			SignResult signResult = signResultDao.getSignResultById(signId);
			List<Map<String, Object>> signList = formService
					.getSignResultById(String.valueOf(signResult.getInstantID()));
			params.put("signList", signList);
		}

		return params;
	}

	@RequestMapping({ "getAcceptancePlanInfoById" })
	@ResponseBody
	@Action(description = "获取id对应的策划信息")
	public Map<String, Object> getAcceptancePlanInfoById(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		Map<String, Object> params = service.getPlansById(acceptancePlanId);
		params.put("F_CPBH", productDao.getCpMc(acceptancePlanId));
		params.put("F_CPSL", productDao.getCpNumber(acceptancePlanId));
		params.put("signList", null);
		return params;
	}
	@RequestMapping({ "checkDepartment" })
	@ResponseBody
	@Action(description = "判断是否是外来人员")
	public Map<String, Object> checkDepartment(HttpServletRequest request, HttpServletResponse response) {
		String  userIds= request.getParameter("userIds");
		String  userNames= request.getParameter("userNames");
		
		String[] userIdArr=userIds.split(",");
		String[] userNameArr=userNames.split(",");
		userIds="";
		userNames="";
		for (int i = 0; i < userIdArr.length; i++) {
			SysOrg sysOrg = sysOrgService.getDefaultOrgByUserId(Long.valueOf(userIdArr[i]));
			if (sysOrg.getPath().contains("10000025340019")) { // 如果当前成员不是八部内的不加入数据确认会签
				userIds+=userIdArr[i]+",";
				userNames+=userNameArr[i]+",";
			}
		}
		Map<String, Object> info=new HashedMap();
		info.put("userIds", userIds.substring(0, userIds.length()-1));
		info.put("userNames", userNames.substring(0, userNames.length()-1));
		return info;
	}
	
	
	@RequestMapping({ "getAcceptancePlanGroup" })
	@ResponseBody
	@Action(description = "获取策划id下的组员")
	public List<Map<String, Object>> getAcceptancePlanGroup(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> params = service.getPlansById(acceptancePlanId);
		String data = (String) params.get("F_YSBGHCSJ");
		JSONObject jsonObject = JSONObject.fromObject(data);
		list = acceptanceGroupDao.getDataByAcceptancePanId(acceptancePlanId);
		if (!jsonObject.isEmpty()) {
			JSONArray jsonArray = jsonObject.getJSONArray("signreses");
			if (jsonArray.size() != 0) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(0);
				String signId = jsonObject2.getString("signid");
				SignResult signResult = signResultDao.getSignResultById(signId);
				List<Map<String, Object>> signList = formService
						.getSignResultById(String.valueOf(signResult.getInstantID()));
				for (Map<String, Object> signMap : signList) {
					if(CommonTools.Obj2String(signMap.get("F_NAME")).indexOf("签署人员")>=0) {
						String signName=CommonTools.Obj2String(signMap.get("F_NAME"));
						Map<String, Object> map=new HashMap<>();
						map.put("ID", UniqueIdUtil.genId());
						map.put("F_ZW","组员");
						map.put("F_XM", signName.substring(signName.indexOf("(") + 1, signName.indexOf(")")));
						map.put("F_XMID", "");
						map.put("F_DW", "");
						map.put("F_DWID", "");
						map.put("F_FZXM", "");
						map.put("F_SSCP", acceptancePlanId);
						list.add(map);
					}
				}
				for (int i = 0; i < signList.size(); i++) {
					for (int y = 0; y < list.size(); y++) {
						String str = signList.get(i).get("F_NAME").toString();
						String str1 =" " ;
						if(list.get(y).get("F_XM")!=null) {
							str1=list.get(y).get("F_XM").toString();
						}
						if (str.indexOf("承制方") >= 0) {
							continue;
						}
						str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
						if (str1.equals(str)) {
							SysFile sysFile = sysFileService.getFileByDataId(signList.get(i).get("ID").toString());
							list.get(y).put("signPath", sysFile.getFileId());
							continue;
						}
					}
				}

			}
		}
		return list;
	}

	/**
	 * 为产品验收生成归档压缩包文件
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("saveAsArchiveForCPYS")
	@ResponseBody
	public JSONObject saveAsArchiveForCPYS(HttpServletResponse response,HttpServletRequest request) throws JAXBException {
		String planId=request.getParameter("acceptancePlanId");
		JSONObject msgJson=service.saveAsArchiveForCPYS(planId);
		return msgJson;
	}

	// 打印生成pdf文件
	@RequestMapping({ "printPDF" })
	@ResponseBody
	public JSONObject printPDF(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject msgJson = new JSONObject();
		msgJson.put("status", "1");
		String msg = "";
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		//为策划添加归档时间
		service.updatePdfFileTime(acceptancePlanId);
		List<String> templateNameList = new ArrayList<>();

		templateNameList.add("PlanningReport.cpt");

		List<Map<String, Object>> productList = productInforDao.getProductByPlanId(acceptancePlanId);
		if (productList == null || productList.size() == 0) {
			msg = "当前未上传产品信息无法归档！";
			msgJson.put("msg", msg);
			msgJson.put("status", "2");
			return msgJson;
		}
		for (Map<String, Object> map : productList) {
			templateNameList.add("AcceptancePlan.cpt");
		}
		List<Conventional> conventionalList = iOConventionalDao.getConByPlanId(acceptancePlanId);
		if (conventionalList.size() != 0) {
			templateNameList.add("Conventional.cpt");
		}

		int exportType = 0;
		String exportFileName = "验收文件";
		List<InputStream> pdfs = new ArrayList<>();
		String id = String.valueOf(UniqueIdUtil.genId());
		String temppath = AppUtil.getAttachPath() + File.separator + "temp" + File.separator + id + exportFileName
				+ File.separator;
		List<acceptanceReport> list = acceptanceReportDao.getByPlanId(acceptancePlanId);
		if ("1".equals(list.get(0).getLx())) {
			templateNameList.add("ImportConclusionReport.cpt");
		} else {
			templateNameList.add("ConclusionReport.cpt");
		}

		if (list == null || list.size() == 0) {
			msg = "当前未生成验收报告无法归档!";
			msgJson.put("msg", msg);
			msgJson.put("status", "2");
			return msgJson;
		} else {
			if (!list.get(0).getSpzt().equals("审批通过")) {
				msg = "当前审批通过未通过无法归档!";
				msgJson.put("msg", msg);
				msgJson.put("status", "2");
				return msgJson;
			}
		}
		Map<String, Object> acceptancePlan = acceptancePlanDao.getMapById(acceptancePlanId);
		String string = acceptancePlan.get("F_YSYJWJ").toString();
		JSONArray filejsonArray = JSONArray.fromObject(string);
		JSONObject filejsonObject = filejsonArray.getJSONObject(0);
		String fileName = filejsonObject.getString("name").toString();
		String acceptanceReportId = "";
		if (list != null && list.size() != 0) {
			acceptanceReportId = list.get(0).getId();
		}
		int c = 0;
		for (int i = 0; i < templateNameList.size(); i++) {
			String envpath = parameterDao.getById(Long.valueOf("6005")).getParamvalue();
			System.out.print(envpath);
			FRContext.setCurrentEnv(new LocalEnv(envpath));
			ModuleContext.startModule(EngineModule.class.getName());
			ResultWorkBook rWorkBook = null;
			String tempname = FileOperator.generateFilenameNoSemicolon(UniqueIdUtil.genId() + ".pdf");
			String tempfile = temppath + tempname;
			FileOperator.createFolderFile(tempfile);
			try {
				WorkBook workBook = (WorkBook) TemplateWorkBookIO.readTemplateWorkBook(FRContext.getCurrentEnv(),
						"\\8dp\\" + templateNameList.get(i));
				Parameter[] parameters = workBook.getParameters();
				if (templateNameList.get(i).equals("PlanningReport.cpt")) {
					for (int j = 0; j < parameters.length; j++) {
						if (parameters[j].toString().equals("$acceptancePlanId")) {
							parameters[j].setValue(acceptancePlanId);
						} else if (parameters[j].toString().equals("$fileName")) {
							parameters[j].setValue(fileName);
						}
					}
				} else if (templateNameList.get(i).equals("Conventional.cpt")) {
					for (int j = 0; j < parameters.length; j++) {
						if (parameters[j].toString().equals("$acceptancePlanId")) {
							parameters[j].setValue(acceptancePlanId);
						}
					}
				} else if (templateNameList.get(i).equals("AcceptancePlan.cpt")) {
					for (int j = 0; j < parameters.length; j++) {
						if (parameters[j].toString().equals("$productId")) {
							parameters[j].setValue(productList.get(c).get("ID").toString());
							c++;
						} else if (parameters[j].toString().equals("$instanceId")) {
							parameters[j].setValue(productList.get(c).get("F_SSSLID").toString());
						} else if (parameters[j].toString().equals("$acceptanceDate")) {
							TableInstance tableInstance = iOTableInstanceDao
									.getById(productList.get(c).get("F_SSSLID").toString());
							parameters[j].setValue(tableInstance.getStartTime());
						}
					}
				} else if (templateNameList.get(i).equals("ConclusionReport.cpt")) {
					for (int j = 0; j < parameters.length; j++) {
						if (parameters[j].toString().equals("$acceptancePlanId")) {
							parameters[j].setValue(acceptancePlanId);
						} else if (parameters[j].toString().equals("$acceptanceReportId")) {
							parameters[j].setValue(acceptanceReportId);
						} else if (parameters[j].toString().equals("$fileName")) {
							parameters[j].setValue(fileName);
						}
					}
				} else if (templateNameList.get(i).equals("ImportConclusionReport.cpt")) {
					for (int j = 0; j < parameters.length; j++) {
						if (parameters[j].toString().equals("$acceptancePlanId")) {
							parameters[j].setValue(acceptancePlanId);
						} else if (parameters[j].toString().equals("$acceptanceReportId")) {
							parameters[j].setValue(acceptanceReportId);
						} else if (parameters[j].toString().equals("$fileName")) {
							parameters[j].setValue(fileName);
						}
					}
				}

				Map parameterMap = new HashedMap();
				for (int j = 0; j < parameters.length; j++) {
					parameterMap.put(parameters[j].getName(), parameters[j].getValue());
				}
				FileOutputStream outputStream;
				outputStream = new FileOutputStream(new File(tempfile));
				PDFExporter pdfExport = new PDFExporter();
				pdfExport.export(outputStream, workBook.execute(parameterMap, new WriteActor()));
				outputStream.close();
				ModuleContext.stopModules();
			} catch (Exception e) {
				msg = e.getMessage();
				e.printStackTrace();
				msgJson.put("msg", msg);
				msgJson.put("status", "2");
				return msgJson;
			}
			pdfs.add(new FileInputStream(tempfile));
		}
		String exportfile = temppath + exportFileName + ".pdf";
		OutputStream outputStream = new FileOutputStream(exportfile);
		//合并PDF
		concatPDFs(pdfs, outputStream, true);
		/*
		 * FileOperator.downLoadFile(request, response, exportfile,
		 * exportFileName+".pdf"); response.getWriter().write(exportfile);
		 */
		SysFile sysFile = new SysFile();
		sysFile.setFileId(UniqueIdUtil.genId());
		sysFile.setFilename("验收文件");
		sysFile.setFilepath("temp" + File.separator + id + exportFileName + File.separator + exportFileName + ".pdf");
		sysFile.setCreateBy(UserContextUtil.getCurrentUserId());
		File file = new File(exportfile);
		sysFile.setDataId(acceptancePlanId);
		sysFile.setExt("pdf");
		sysFile.setFileType("FILE");
		sysFile.setCreator(UserContextUtil.getCurrentUser().getFullname());
		sysFile.setDelFlag(Short.valueOf("0"));
		sysFile.setProtypeId(Long.valueOf(8));
		sysFile.setFiling(Long.valueOf(0));
		sysFile.setIsnew(Long.valueOf(1));
		sysFile.setVersion("0.1");
		sysFile.setStoreWay(Long.valueOf(0));
		sysFile.setIsEncrypt(Long.valueOf(0));
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", String.valueOf(sysFile.getFileId()));
		jsonObject.put("name", "验收文件pdf");
		jsonArray.add(jsonObject);
		sysFileService.add(sysFile);
		service.updateFile(acceptancePlanId, jsonArray.toString());
		acceptancePlanDao.UpdateStatus(acceptancePlanId);
		msgJson.put("msg", "生成pdf成功!");
		//2.流程结束后通知组员
		List<Map<String,Object>> groupList=acceptanceGroupDao.getDataByAcceptancePanId(acceptancePlanId);
		String createPlanUserId=acceptancePlan.get("F_JSFZRID").toString();  //获取策划发起人
		String createPlanUserName=acceptancePlan.get("F_JSFZR").toString();
		Boolean check=false;
		for (Map<String, Object> teamMate : groupList) {  //判断策划流程发起人是否是队伍中的
			if(teamMate.get("F_XMID").toString().equals(createPlanUserId)) {
				check=true;
			}
		}
		if(!check) {
			Map<String, Object> teamMateMap=new HashMap<>();
			teamMateMap.put("F_XM", createPlanUserName);
			teamMateMap.put("F_XMID", createPlanUserId);
			teamMateMap.put("F_zw", "发起人");
			groupList.add(teamMateMap);
		}
		for(int i=0;i<groupList.size();i++) {
			acceptanceMessageDao.insertMessageByArchive(groupList.get(i), acceptancePlan);
		}
		workBoardDao.updatework(acceptancePlanId, "已生成PDF", "任务结束");
		return msgJson;

	}

	/**
	 * @Description 服务器生成靶场试验压缩包的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:22
	 * @param missionId
	 * @Return void
	 */
	public void sendMassageWhenPrintPDFForYSCH(String missionId){
		//定位策划
		Map<String,Object> rangeTestPlan=acceptancePlanDao.getMapById(missionId);
		//定位组员表
		List<AcceptanceGroup> acceptanceGroupList=acceptanceGroupDao.getByAcceptancePanId(missionId);
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			SysUser teamMember=sysUserDao.getById(Long.valueOf(acceptanceGroup.getXmId()));
			if ("组长".equals(acceptanceGroup.getZw())){
				//是组长   直接发消息
				acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"已生成PDF","组长");
			}else {
				//不是组长
				//先看下这个人在组里是不是有担任组长的同时担任组员
				if (curUserIsTeamLeader(acceptanceGroupList,acceptanceGroup.getXmId())){
					//当前这个人既是组员也是组长
					//组长的消息是上面发的,这里直接跳过
				}else {
					//当前这个人不是组长,直接发消息
					acceptanceMessageDao.insertMessageForCommon(teamMember,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"已生成PDF","组员");
				}
			}
		}
		//确定发起人
		String sponsorId=rangeTestPlan.get("F_FQRID").toString();
		SysUser sponsor=sysUserDao.getById(Long.valueOf(sponsorId));
		if (curUserInTeam(acceptanceGroupList,sponsorId)){
			//发起人属于组员/组长
			//该消息已由上面发送
		}else {
			//发起人不属于组员/组长
			acceptanceMessageDao.insertMessageForCommon(sponsor,missionId,rangeTestPlan.get("F_CHBGBBH")+"-"+rangeTestPlan.get("F_CPMC"),"已生成PDF","发起人");
		}
	}

	/**
	 * @Description 看看传来的人是不是组员或者组长 换句话说,是不是非发起人
	 * @Author ZMZ
	 * @Date 2020/12/7 9:39
	 * @param acceptanceGroupList
	 * @param sponsorId
	 * @Return boolean
	 */
	private boolean curUserInTeam(List<AcceptanceGroup> acceptanceGroupList, String sponsorId) {
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			if (acceptanceGroup.getXmId().equals(sponsorId)){
				//传来的这个人是组长/组员
				return true;
			}
		}
		//找完队列也没有找到
		//说明这个人不在组员表里
		return false;
	}

	/**
	 * 看看当前这个人是不是组长
	 * 如果是的话返回true
	 * @param acceptanceGroupList
	 * @return
	 */
	public boolean curUserIsTeamLeader(List<AcceptanceGroup> acceptanceGroupList,String xmId) {
		for (AcceptanceGroup acceptanceGroup:acceptanceGroupList){
			if ("组长".equals(acceptanceGroup.getZw())){
				//先确定当前组的组长的id
				if (acceptanceGroup.getXmId().equals(xmId)){
					//传来的这个人是组长没跑了
					return true;
				}
			}
		}
		//找完队列也没有找到
		//说明这个人不担任组长
		return false;
	}


	/***
	 * pdf合并
	 * 
	 * @param streamOfPDFFiles
	 * @param outputStream
	 * @param paginate
	 */
	public static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

		Document document = new Document();
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages,
								520, 5, 0);
						cb.endText();
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	// 打印生成pdf文件
		@RequestMapping({ "HtmlPrintPDF" })
		@ResponseBody
		public JSONObject HtmlPrintPDF(HttpServletRequest request, HttpServletResponse response) throws Exception {

			JSONObject msgJson = new JSONObject();
			msgJson.put("status", "1");
			String msg = "生成PDF成功";
			msgJson.put("msg", msg);
			String acceptancePlanId = request.getParameter("acceptancePlanId");
			List<TableInstance> tableInstanceList=iOTableInstanceDao.getByPlanId(acceptancePlanId);
			List<InputStream> pdfs=new ArrayList<>();
			String path=AppUtil.getAttachPath();
			for (TableInstance tableInstance : tableInstanceList) {
				String html=tableInstance.getContent();
		        int index1 = html.indexOf("<table");
		        int index2 = html.lastIndexOf("</table>");
		        html = html.substring(index1, index2 + 8); 
			}

			return msgJson;
		}
	
}
