package com.cssrc.ibms.worktime.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.worktime.model.CalendarSetting;
import com.cssrc.ibms.worktime.service.CalendarSettingService;

@Controller
@RequestMapping({ "/oa/worktime/calendarSetting/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_CALENDSETTING)
public class CalendarSettingController extends BaseController {

	@Resource
	private CalendarSettingService calendarSettingService;

	@RequestMapping({ "list" })
	@Action(description = "查看日历设置分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.calendarSettingService.getAll(new QueryFilter(request,
				"calendarSettingItem"));
		ModelAndView mv = getAutoView().addObject("calendarSettingList", list);

		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除日历设置")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.calendarSettingService.delByIds(lAryId);
			message = new ResultMessage(1, "删除日历设置成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑日历设置")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		CalendarSetting calendarSetting = null;
		if (id.longValue() != 0L)
			calendarSetting = (CalendarSetting) this.calendarSettingService
					.getById(id);
		else {
			calendarSetting = new CalendarSetting();
		}
		return getAutoView().addObject("calendarSetting", calendarSetting)
				.addObject("returnUrl", returnUrl);
	}

	@RequestMapping({ "get" })
	@Action(description = "查看日历设置明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		CalendarSetting calendarSetting = (CalendarSetting) this.calendarSettingService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("calendarSetting", calendarSetting);
	}
}