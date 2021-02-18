package com.cssrc.ibms.worktime.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.worktime.model.CalendarAssign;
import com.cssrc.ibms.worktime.model.SysCalendar;
import com.cssrc.ibms.worktime.service.CalendarAssignService;
import com.cssrc.ibms.worktime.service.SysCalendarService;

@Controller
@RequestMapping({ "/oa/worktime/calendarAssign/" })
@Action(ownermodel=SysAuditModelType.WORKTIME_CALENDARASSIGN)
public class CalendarAssignController extends BaseController {

	
	@Resource
	private CalendarAssignService calendarAssignService;

	@Resource
	private SysCalendarService sysCalendarService;

	@Resource
	private ISysUserService sysUserService;

	@Resource
	private ISysOrgService sysOrgService;

	@RequestMapping({ "list" })
	@Action(description = "查看日历分配分页列表", detail = "查看日历分配分页类表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.calendarAssignService.getAll(new QueryFilter(request,
				"calendarAssignItem"));
		ModelAndView mv = getAutoView().addObject("calendarAssignList", list);

		String flag = RequestUtil.getString(request, "flag");
		if (flag.equals("1")) {
			mv.addObject("flag", flag);
		}

		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除日历分配", execOrder = ActionExecOrder.BEFORE, detail = "删除日历分配：<#list StringUtils.split(id,\",\") as item><#assign calendarAssign=calendarAssignService.getById(Long.valueOf(item))/><#assign sysCalendar=sysCalendarService.getById(calendarAssign.canlendarId)/><#if calendarAssign.assignType==1><#assign assign=sysUserService.getById(calendarAssign.assignId)/><#elseif calendarAssign.assignType==2><#assign assign=sysOrgService.getById(calendarAssign.assignId)/></#if>【工作日历:${sysCalendar.name} , 被分配人类型：<#if calendarAssign.assignType==1>人员<#elseif calendarAssign.assignType==2>组织</#if> , 被分配人名称：<#if calendarAssign.assignType==1>${assign.fullname}<#elseif calendarAssign.assignType==2>${assign.orgName}</#if>】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.calendarAssignService.delByIds(lAryId);
			message = new ResultMessage(1, "删除日历分配成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑日历分配", detail = "编辑日历分配")
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		CalendarAssign calendarAssign = null;
		if (id.longValue() != 0L) {
			calendarAssign = (CalendarAssign) this.calendarAssignService
					.getById(id);
			if (calendarAssign.getAssignType().shortValue() == 1) {
				ISysUser sysUser = (ISysUser) this.sysUserService
						.getById(calendarAssign.getAssignId());
				calendarAssign.setAssignUserName(sysUser.getFullname());
			} else {
				ISysOrg sysOrg = (ISysOrg) this.sysOrgService
						.getById(calendarAssign.getAssignId());
				calendarAssign.setAssignUserName(sysOrg.getOrgName());
			}
		} else {
			calendarAssign = new CalendarAssign();
		}

		List typelist = this.calendarAssignService.getAssignUserType();

		List callist = this.sysCalendarService.getAll();

		if (callist.size() > 0) {
			return getAutoView().addObject("calendarAssign", calendarAssign)
					.addObject("returnUrl", returnUrl)
					.addObject("callist", callist)
					.addObject("typelist", typelist);
		}

		response.sendRedirect(request.getContextPath()
				+ "/oa/worktime/calendarAssign/list.do?flag=1");
		return null;
	}

	@RequestMapping({ "get" })
	@Action(description = "查看日历分配明细", detail = "查看日历分配明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		CalendarAssign calendarAssign = (CalendarAssign) this.calendarAssignService
				.getById(Long.valueOf(id));
		SysCalendar sysCal = (SysCalendar) this.sysCalendarService
				.getById(calendarAssign.getCanlendarId());
		calendarAssign.setCalendarName(sysCal.getName());
		if (calendarAssign.getAssignType().shortValue() == 1) {
			ISysUser sysUser = (ISysUser) this.sysUserService
					.getById(calendarAssign.getAssignId());
			calendarAssign.setAssignUserName(sysUser.getFullname());
		} else {
			ISysOrg sysOrg = (ISysOrg) this.sysOrgService
					.getById(calendarAssign.getAssignId());
			calendarAssign.setAssignUserName(sysOrg.getOrgName());
		}
		return getAutoView().addObject("calendarAssign", calendarAssign)
				.addObject("canReturn", Long.valueOf(canReturn));
	}
}