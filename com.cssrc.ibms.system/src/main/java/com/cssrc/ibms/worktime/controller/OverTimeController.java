package com.cssrc.ibms.worktime.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.worktime.model.OverTime;
import com.cssrc.ibms.worktime.service.OverTimeService;

@Controller
@RequestMapping({ "/oa/worktime/overTime/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_OVERTIME)
public class OverTimeController extends BaseController {

	@Resource
	private OverTimeService overTimeService;

	@Resource
	private ISysUserService sysUserService;

	@RequestMapping({ "list" })
	@Action(description = "查看加班情况分页列表", detail = "查看加班情况分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.overTimeService.getAll(new QueryFilter(request,
				"overTimeItem"));
		ModelAndView mv = getAutoView().addObject("overTimeList", list);

		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除加班请假管理", execOrder = ActionExecOrder.BEFORE, detail = "删除加班请假管理<#list StringUtils.split(id,\",\") as item><#assign entity=overTimeService.getById(Long.valueOf(item))/>【${entity.subject}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.overTimeService.delByIds(lAryId);
			message = new ResultMessage(1, "删除加班情况成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑加班情况", detail = "<#if isAdd>添加加班情况<#else>编辑加班情况:<#assign entity=overTimeService.getById(Long.valueOf(id))/>【${entity.subject}】</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		OverTime overTime = null;
		boolean isadd = true;
		if (id.longValue() != 0L) {
			overTime = (OverTime) this.overTimeService.getById(id);
			ISysUser sysUser = (ISysUser) this.sysUserService.getById(overTime
					.getUserId());
			overTime.setUserName(sysUser.getFullname());
			isadd = false;
		} else {
			overTime = new OverTime();
			Date curdate = new Date();
			overTime.setStartTime(curdate);
			overTime.setEndTime(curdate);
		}
		List typelist = this.overTimeService.getWorkType();

		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		return getAutoView().addObject("overTime", overTime)
				.addObject("returnUrl", returnUrl)
				.addObject("typelist", typelist);
	}

	@RequestMapping({ "get" })
	@Action(description = "查看加班情况明细", detail = "查看加班情况明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		OverTime overTime = (OverTime) this.overTimeService.getById(Long
				.valueOf(id));
		ISysUser sysUser = (ISysUser) this.sysUserService.getById(overTime
				.getUserId());
		overTime.setUserName(sysUser.getFullname());
		return getAutoView().addObject("overTime", overTime).addObject(
				"canReturn", Long.valueOf(canReturn));
	}
}