package com.cssrc.ibms.dp.rangeTest.mission.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.crypto.Data;


import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.mission.service.TestPlanService;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.annotion.Action;


import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.controller.AcceptancePlanController;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;


import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceMessageDao;
import com.cssrc.ibms.dp.product.acceptance.service.WorkBordService;
import com.cssrc.ibms.dp.rangeTest.mission.dao.RangeTestSummaryDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 靶场试验策划controller
 * @author zmz
 *
 */
@Controller
@RequestMapping("/rangeTest/mission/plan/")
public class TestPlanController {
	@Resource
	private TestPlanService service;
	@Autowired
	private AcceptancePlanController acceptancePlanController;
	@Resource
	private AcceptanceGroupDao acceptanceGroupDao;
	@Resource
	private WorkBordService workBordService;



	/**
	 * @Desc 更新试验策划审批状态
	 * 如果通过则发消息给组长
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
	 * 获取下一个策划编号除代号外的剩余部分
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getNextRangeTestPlanNumber")
	public void getNextRangeTestPlanNumber(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String xhId=request.getParameter("xhId");
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");
		String time=dateFormat.format(calendar.getTime());
		String nextNumber="[-BCSY-"+time+"-"+service.generatorReportNumber(xhId)+"]";
		nextNumber=JSONArray.fromObject(nextNumber).toString();
		response.getWriter().print(nextNumber);
	}

	/**
	 * @Desc 保存组长信息
	 */
	@RequestMapping("saveTeamLeader")
	public void saveTeamLeader(HttpServletRequest request,HttpServletResponse response){
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		service.saveTeamLeader(acceptancePlanId);
	}

	/**
	 * @Desc 更新靶场数据确认的状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateDataConfirmStatus")
	public void updateDataConfirmStatus(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		String status = request.getParameter("status");
		service.updateDataConfirmStatus(acceptancePlanId, status);

	}



	/**
	 * @Desc 更新靶场和所检的数据确认子流程的状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("updateDataConfirmSubprocessStatus")
	public void updateDataConfirmSubprocessStatus(HttpServletRequest request, HttpServletResponse response) {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		String status = request.getParameter("status");
		service.updateDataConfirmSubprocessStatus(acceptancePlanId, status);
	}

	/**
	 * 获取当前策划所属的xhid
	 * 用于在下发表单时筛选模板
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("getModuleidByPlanid")
	public void getModuleidByPlanid(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String acceptancePlanId = request.getParameter("acceptancePlanId");
		String xhid=service.getModuleidByPlanid(acceptancePlanId);
		String json="[{\"xhid\":"+xhid+"}]";
		response.getWriter().print(JSONArray.fromObject(json).toString());
	}


	/**
	 * 这是总结表单的数据请求
	 * 		主要是从策划表单里拉取一些需要的数据用于填充总结表单
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping({ "getRangeTestPlanGroup" })
	@ResponseBody
	@Action(description = "获取策划id下的组员")
	public List<Map<String, Object>> getRangeTestPlanGroup(HttpServletRequest request, HttpServletResponse response) {
		//获取组id
		String missionId = request.getParameter("missionId");
		List<Map<String, Object>> list = new ArrayList<>();
		//根据组id获取组员信息
		//组员信息存储 在list 里,是list<map<string,object>>的rangeTestGroup
		list = acceptanceGroupDao.getByMissionIdToMap(missionId);

		//F_YSBGHCSJ==验收报告回传数据(取组员签署的

/*		//靶场不需要组员签署
		List<Map<String, Object>> params = service.getPlanById(missionId);
		String data = (String) params.get(0).get("F_SYBGHCSJ");
		JSONObject jsonObject = JSONObject.fromObject(data);

		if (!jsonObject.isEmpty()) {
//			  {"instructions":"wu","opinion":"听音乐有一个官员","problem":"验收通过","sellerOpinion":"wu","serialNumber":"10001","signreses":[
//			  {"signDefId":"10000029470470","signid":"25985226510470","time":"2020-08-27 17:20:03"},
//			  {"signDefId":"10000029470471","signid":"25985226510471","time":"2020-08-27 17:20:06"}
//			  ]}
			JSONArray jsonArray = jsonObject.getJSONArray("signreses");
			if (jsonArray.size() != 0) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(0);
				String signId = jsonObject2.getString("signid");
				SignResult signResult = signResultDao.getSignResultById(signId);
				List<Map<String, Object>> signList = formService
						.getSignResultById(String.valueOf(signResult.getInstantID()));

				//咕咕咕
				for (int i = 0; i < signList.size(); i++) {
					for (int y = 0; y < list.size(); y++) {
						String str = signList.get(i).get("F_NAME").toString();
						String str1 = list.get(y).get("F_XM").toString();
						if (str.indexOf("承制方") >= 0) {
							continue;
						}
						str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
						if (str1.equals(str)) {
							SysFile sysFile = sysFileService.getFileByDataId(signList.get(i).get("ID").toString());
							list.get(y).put("signPath", sysFile.getFileId());
						}
					}
				}

			}
		}*/
		return list;
	}


	@RequestMapping({ "getRangeTestPlanById" })
	@ResponseBody
	@Action(description = "获取id对应的策划信息")
	public Map<String, Object> getRangeTestPlanById(HttpServletRequest request, HttpServletResponse response) {
		String missionId = request.getParameter("missionId");
		//根据任务id取任务信息
		Map<String, Object> params = service.getPlanById(missionId).get(0);
		List<AcceptanceGroup> acceptanceGroup=acceptanceGroupDao.getByMissionId(missionId);
		params.put("acceptanceGroup", acceptanceGroup);
		//params.put("signList", null);
		//获取任务信息里的试验报告回传数据
		//String data = (String) params.get("F_SYBGHCSJ");
		//JSONObject jsonObject = JSONObject.fromObject(data);
		//如果回传数据为空,直接返回空,不考虑其他字段
		//if (jsonObject.isEmpty()) {
		//	return null;
		//}
		//从回传数据中获取签章信息
		/*JSONArray jsonArray = jsonObject.getJSONArray("signreses");
		if (jsonArray.size() != 0) {
			JSONObject jsonObject2 = jsonArray.getJSONObject(0);
			String signId = jsonObject2.getString("signid");
			SignResult signResult = signResultDao.getSignResultById(signId);
			List<Map<String, Object>> signList = formService
					.getSignResultById(String.valueOf(signResult.getInstantID()));
			params.put("signList", signList);
		}*/
		//下面这两个字段在前端没有对应的控件来填
/*		params.put("F_CPBH", rangeMissionDao.getCpMc(missionId));
		params.put("F_CPSL", rangeMissionDao.getCpNumber(missionId));*/
		return params;
	}


	/**
	 * 靶场试验归档页面生成pdf
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({ "printPDF" })
	@ResponseBody
	public JSONObject printPDF(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取策划id
		String missionId = request.getParameter("missionId");
		//修改策划的结束时间为当前时间
		service.updateEndTime(missionId);
		//判断转pdf的前置条件
		JSONObject msgJson=service.checkIfCouldConvertToPDF(missionId);
		//是否可以转PDF  2代表不可以
		if ("2".equals(msgJson.get("status").toString())){
			//前置条件不通过,直接返回
			return msgJson;
		}
		String id = String.valueOf(UniqueIdUtil.genId());
		String exportDirName="靶场数据确认PDF";
		String temppath = AppUtil.getAttachPath() + File.separator + "temp" + File.separator + exportDirName
				+ File.separator;
		//把这个策划下的所有实例的html取出来转成pdf
		List<InputStream> pdfs=service.transferAllInstanceToPDF(missionId);
		Calendar calendar=Calendar.getInstance();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		String time=dateFormat.format(calendar.getTime());
		String exportFileName=time+"_"+id;
		//把多个pdf合成为一个pdf的最终pdf的文件路径
		String exportfile = temppath + exportFileName + ".pdf";
		//如果当前路径不存在,则创建路径
		File fileDir=new File(temppath);
		if (!fileDir.exists()){
			fileDir.mkdir();
		}
		File file=new File(exportfile);
		file.createNewFile();
		OutputStream outputStream = new FileOutputStream(exportfile);
		//合并PDF
		acceptancePlanController.concatPDFs(pdfs, outputStream, true);
		//删除零散的pdf  因为文件流没有关闭,无法删除
/*		String allPDFPath=AppUtil.getAttachPath()+"\\temp\\htmlToPDF";
		File allPDFDir=new File(allPDFPath);
		allPDFDir.delete();*/

		//持久化最终的文件地址 String exportfile
		String relativePDFAddress="temp" + File.separator + exportDirName+ File.separator+ exportFileName + ".pdf";
		service.savePDFfileInfo(missionId,relativePDFAddress);
		//更新看板
		workBordService.updateWorkBoard(missionId,"已生成PDF","任务结束");
		msgJson.put("msg","归档成功!");
		return msgJson;
	}

	/**
	 * 为靶场试验和武器所检生成归档压缩包文件
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping("saveAsArchiveForBCSY")
	@ResponseBody
	public com.alibaba.fastjson.JSONObject saveAsArchiveForCPYS(HttpServletResponse response,HttpServletRequest request) throws JAXBException, ParseException {
		String planId=request.getParameter("acceptancePlanId");
		com.alibaba.fastjson.JSONObject msgJson=service.saveAsArchiveForBCSY(planId);
		return msgJson;
	}

	/**
	 * excel导入组员信息
	 */
	@RequestMapping({"importTeamInfoFromExcel"})
	public void importTeamInfo(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException, InvalidFormatException {
		MultipartFile multipartFile=request.getFile("file");
		String missionId=request.getParameter("missionId");
		String msg=service.importTeamInfo(multipartFile.getInputStream(),missionId);
		PrintWriter writer = response.getWriter();
		writer.println(msg);
		//	writer.println("{\"success\":\"true\"}");
	}

	/**
	 * @Desc 删除数据时同时删除草稿
	 * 我觉得这个地方大概率只会传来单个id,而不是ids
	 * @param request
	 * @param response
	 */
	@RequestMapping("topDelete")
	@ResponseBody
	public Map<String, Object> topDelete(HttpServletRequest request, HttpServletResponse response) {
		String ids = request.getParameter("Ids");
		String ofPart = request.getParameter("ofPart");
		Map<String, Object> map=new HashMap<String, Object>();
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		map.put("message", "0");
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			service.topDelete(ids,ofPart);
			map.put("message", "1");
			return map;
		}
		List<Map<String, Object>> planMap=service.getPlanById(ids);
		if(CommonTools.Obj2String(planMap.get(0).get("F_FQRID")).equals(CommonTools.Obj2String(curUser.getUserId()))) {
			service.topDelete(ids,ofPart);
			map.put("message", "1");
			return map;
		}
		return map;
	}


}

