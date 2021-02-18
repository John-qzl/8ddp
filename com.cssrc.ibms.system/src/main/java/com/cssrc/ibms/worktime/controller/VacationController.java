package com.cssrc.ibms.worktime.controller;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.worktime.model.Vacation;
import com.cssrc.ibms.worktime.service.VacationService;

@Controller
@RequestMapping({ "/oa/worktime/vacation/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_VACATION)
public class VacationController extends BaseController {

	@Resource
	private VacationService vacationService;

	@RequestMapping({ "list" })
	@Action(description = "查看法定假期设置分页列表", detail = "查看法定假期设置分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.vacationService.getAll(new QueryFilter(request,
				"vacationItem"));
		ModelAndView mv = getAutoView().addObject("vacationList", list);

		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除法定假期设置", execOrder = ActionExecOrder.BEFORE, detail = "删除法定假期设置：<#list StringUtils.split(id,\",\") as item><#assign entity=vacationService.getById(Long.valueOf(item))/>【${entity.name}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.vacationService.delByIds(lAryId);
			message = new ResultMessage(1, "删除法定假期设置成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑法定假期设置", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加法定假期设置<#else>编辑法定假期设置<#assign entity=vacationService.getById(Long.valueOf(id))/>【${entity.name}】</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		Vacation vacation = null;
		boolean isadd = true;
		if (id.longValue() != 0L) {
			vacation = (Vacation) this.vacationService.getById(id);
			isadd = false;
		} else {
			vacation = new Vacation();
		}

		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		return getAutoView().addObject("vacation", vacation)
				.addObject("returnUrl", returnUrl)
				.addObject("year", Integer.valueOf(year));
	}

	@RequestMapping({ "get" })
	@Action(description = "查看法定假期设置明细", detail = "查看法定假期设置明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		Vacation vacation = (Vacation) this.vacationService.getById(Long
				.valueOf(id));
		vacation.setsTime(TimeUtil.getDateTimeString(vacation.getStatTime(),
				"yyyy-MM-dd"));
		vacation.seteTime(TimeUtil.getDateTimeString(vacation.getEndTime(),
				"yyyy-MM-dd"));
		return getAutoView().addObject("vacation", vacation).addObject(
				"canReturn", Long.valueOf(canReturn));
	}
}