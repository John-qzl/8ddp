package com.cssrc.ibms.core.resources.datapackage.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.resources.datapackage.service.WBSService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 甘特图相关
 * 
 * @author vector
 */
@Controller
@RequestMapping({ "/project/wbs/" })
@Action(ownermodel = SysAuditModelType.GANTT_STATISTICS)
public class WBSController extends BaseController {

	@Resource
	private WBSService wbsService;

	/**
	 * 初始化甘特图界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "init" })
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//数据包名称
		String sjbName = RequestUtil.getString(request, "sjbName");
		//数据包ID
		String sjbId = RequestUtil.getString(request, "sjbId");
		//发次名称
		String fcName=RequestUtil.getString(request, "projectName");//fcName
		//发次Id
		String fcId=RequestUtil.getString(request, "projectId");//fcId

		String ganttSiteId = RequestUtil.getString(request, "__ganttSiteId__");
		String showRealTimeLine = RequestUtil.getString(request, "showRealTimeLine");
		if (showRealTimeLine.equals("")) {
			showRealTimeLine = "false";
		}
		
		if ("".equals(ganttSiteId)) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			ganttSiteId = dateFormat.format(new Date());
		}
		
		ModelAndView mv = new ModelAndView("dataPackage/ganttView.jsp");
	
		
		mv.addObject("sjbName", sjbName);
		mv.addObject("sjbId", sjbId);
		mv.addObject("fcName", fcName);
		mv.addObject("fcId", fcId);
		mv.addObject("showRealTimeLine", showRealTimeLine);
		mv.addObject("ganttSiteId", ganttSiteId);
		
		return mv;
	}
	/**
	 * 获取甘特图的数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "getWBSData" })
	@ResponseBody
	public Map<String, Object> getWBSData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> data = wbsService.getWBSData(request);
		return data;
	}
	/**
	 * 获取当前节点的下一级 顺序编号
	 * eg: 1
	 * 		1.1  在此点击添加
	 * 		需要编号为1.2
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getSubTaskBh"})
	@ResponseBody
	public String getSubTaskBh(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sjbId = RequestUtil.getString(request, "sjbId");
		String curNodeId = RequestUtil.getString(request, "curNodeId");
		String curNodeOrder = RequestUtil.getString(request, "curNodeOrder");
		/*if(curNodeOrder.equals("0")){
			return "1";
		}*/
		String bh = wbsService.getSubTaskBh(sjbId, curNodeId,curNodeOrder);
		return bh;
	}
	/**
	 * 甘特图中计划任务的上下左右移动：调整计划的层级关系
	 * 前后端分离：后端获取前端传来的需要改变的型号值即可
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exchangeTaskOrder")
	@ResponseBody
	public Map<String,Object> exchangeTaskOrder(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Map<String,Object> res=new HashMap<String,Object>();
		try {
			String jsonData = RequestUtil.getString(request, "mydata");
			wbsService.exchangeTaskOrder(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("success", false);
			res.put("msg", e.getMessage());
		}
		return res;
	}
	
}
