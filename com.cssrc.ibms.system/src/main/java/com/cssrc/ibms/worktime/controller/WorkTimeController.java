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
import com.cssrc.ibms.worktime.model.WorkTime;
import com.cssrc.ibms.worktime.service.WorkTimeService;

@Controller
@RequestMapping({ "/oa/worktime/workTime/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_WORKTIME)
public class WorkTimeController extends BaseController {

	@Resource
	private WorkTimeService workTimeService;

	@RequestMapping({ "list" })
	@Action(description = "查看班次时间分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String setId = request.getParameter("settingId");
		Long settingId = new Long(request.getParameter("settingId"));
		QueryFilter query = new QueryFilter(request, "workTimeItem");
		query.addFilterForH("settingId", String.valueOf(settingId));
		List list = this.workTimeService.getAll(query);
		ModelAndView mv = getAutoView().addObject("workTimeList", list);

		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除班次时间")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.workTimeService.delByIds(lAryId);
			message = new ResultMessage(1, "删除班次时间成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑班次时间")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		WorkTime workTime = null;
		if (id.longValue() != 0L)
			workTime = (WorkTime) this.workTimeService.getById(id);
		else {
			workTime = new WorkTime();
		}
		return getAutoView().addObject("workTime", workTime).addObject(
				"returnUrl", returnUrl);
	}

	@RequestMapping({ "get" })
	@Action(description = "查看班次时间明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		WorkTime workTime = (WorkTime) this.workTimeService.getById(Long
				.valueOf(id));
		return getAutoView().addObject("workTime", workTime);
	}
}