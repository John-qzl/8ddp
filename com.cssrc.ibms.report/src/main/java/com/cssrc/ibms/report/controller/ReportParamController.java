package com.cssrc.ibms.report.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.report.model.ReportParam;
import com.cssrc.ibms.report.service.ReportParamService;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;

@RequestMapping("oa/system/reportParam")
@Action(ownermodel=SysAuditModelType.REPORT_MANAGEMENT)
@Controller
public class ReportParamController extends BaseController {
	@Resource
	private ReportParamService reportParamService;
	
	@RequestMapping({"/list"})
	@Action(description = "查看报表模板参数列表", detail = "查看报表模板参数列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv=new ModelAndView("report/freport/reportParamList.jsp");
		List<ReportParam> list = this.reportParamService.getAll();
		mv.addObject("reportParamList", list);
		return mv;
	}
	
	@RequestMapping({"/{reportid}/get"})
	@Action(description = "获取参数列表", detail = "获取参数列表")
	public void getParamByReportId(HttpServletRequest request,
			HttpServletResponse response,@PathVariable("reportid")Long reportid) throws Exception {
		List<ReportParam> list = this.reportParamService.getByReportid(reportid);
		String json=JSONArray.fromObject(list).toString();
		PrintWriter writer = response.getWriter();
		writer.print(json);
	}

	@RequestMapping({ "del" })
	@Action(description = "删除报表模板", execOrder = ActionExecOrder.BEFORE, detail = "删除报表模板参数：<#list StringUtils.split(paramid,\",\") as item><#assign entity=reportParamService.getById(Long.valueOf(item))/>【${entity.name}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		PrintWriter writer = response.getWriter();
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "paramid");
			this.reportParamService.delByIds(lAryId);
			message = new ResultMessage(1, "删除报表模板参数成功!");
			writer.print(message.toString());
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
			writer.print(message.toString());

		}
		
	}

	@RequestMapping({ "edit" })
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv=new ModelAndView("report/freport/reportParamEdit.jsp");
		Long reportid = Long.valueOf(RequestUtil.getLong(request, "reportid"));
		Long paramid = Long.valueOf(RequestUtil.getLong(request, "paramid"));
		boolean edit = RequestUtil.getBoolean(request, "edit");

		String returnUrl = RequestUtil.getPrePage(request);
		ReportParam reportParam = null;
		if (paramid.longValue() != 0L) {
			reportParam = (ReportParam) this.reportParamService
					.getById(paramid);
		} else {
			reportParam = new ReportParam();
		}
		reportParam.setReportid(reportid);
		return mv.addObject("reportParam", reportParam)
				.addObject("returnUrl", returnUrl)
				.addObject("edit", edit);
	}

	@RequestMapping({ "save" })
	@Action(description = "添加或更新报表模板", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>报表模板参数：【${SysAuditLinkService.getReportParamLink(Long.valueOf(paramid))}】")
	public void save(HttpServletRequest request,
			HttpServletResponse response, ReportParam reportParam)
			throws Exception {
		String createTime = RequestUtil.getString(request, "tmpCreateTime");
		ResultMessage result=new ResultMessage(ResultMessage.Fail);
		String st = "";
		if (StringUtil.isNotEmpty(createTime)) {
			st = DateUtil.timeStrToDateStr(createTime);
		}
		Boolean isAdd = reportParam.getReportid() == null;
		result=this.reportParamService.saveReportParam(reportParam,st.length() > 0 ? DateUtil.parseDate(st) : new Date());
		PrintWriter writer = response.getWriter();
		writer.print(result.toString());
		try {
			LogThreadLocalHolder.setResult((short)result.getResult());
			LogThreadLocalHolder.putParamerter("reportid", reportParam.getParamid().toString());
			LogThreadLocalHolder.putParamerter("isAdd", isAdd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
