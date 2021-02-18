package com.cssrc.ibms.worktime.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.worktime.model.OverTime;
import com.cssrc.ibms.worktime.model.WorkTime;
import com.cssrc.ibms.worktime.service.CalendarAssignService;
import com.cssrc.ibms.worktime.service.CalendarSettingService;
import com.cssrc.ibms.worktime.service.OverTimeService;

@Controller
@RequestMapping({ "/oa/worktime/overTime/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_OVERTIME)
public class OverTimeFormController extends BaseFormController {

	@Resource
	private OverTimeService overTimeService;

	@Resource
	private CalendarAssignService calendarAssignService;

	@Resource
	private CalendarSettingService calendarSettingService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新加班请假管理", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>加班请假管理【${SysAuditLinkService.getOverTimeLink(Long.valueOf(id))}】")
	public void save(HttpServletRequest request, HttpServletResponse response,
			OverTime overTime) throws Exception {
		ResultMessage resultMessage = null;
		String resultMsg = null;
		String stime = RequestUtil.getString(request, "startTime");
		String etime = RequestUtil.getString(request, "endTime");
		overTime.setStartTime(TimeUtil.getDateTimeByTimeString(stime));
		overTime.setEndTime(TimeUtil.getDateTimeByTimeString(etime));

		boolean isadd = overTime.getId() == null;

		Long calendarId = this.calendarAssignService
				.getCalendarIdByUserId(overTime.getUserId());
		List<WorkTime> workTimes = this.calendarSettingService.getByCalendarId(
				calendarId.longValue(), overTime.getStartTime(),
				overTime.getEndTime());

		long otStartTime = overTime.getStartTime().getTime();
		long otEndTime = overTime.getEndTime().getTime();
		long wkStartTime;
		if (overTime.getWorkType().shortValue() == 1) {
			for (WorkTime workTime : workTimes) {
				wkStartTime = workTime.getStartDateTime().getTime();
				long wkEndTime = workTime.getEndDateTime().getTime();
				if (((wkStartTime >= otStartTime) || (wkEndTime <= otStartTime))
						&& ((wkStartTime <= otStartTime) || (wkStartTime >= otEndTime)))
					continue;
				resultMsg = "加班时间与正常上班时间有冲突";
				writeResultMessage(response.getWriter(), resultMsg, 0);
				return;
			}
		} else if (overTime.getWorkType().shortValue() == 2) {
			boolean conflict = false;
			for (WorkTime workTime : workTimes) {
				wkStartTime = workTime.getStartDateTime().getTime();
				long wkEndTime = workTime.getEndDateTime().getTime();
				if (((wkStartTime >= otStartTime) || (wkEndTime <= otStartTime))
						&& ((wkStartTime <= otStartTime) || (wkStartTime >= otEndTime)))
					continue;
				conflict = true;
				break;
			}

			if (!conflict) {
				resultMsg = "所请假时间段无班次安排，不用请假";
				writeResultMessage(response.getWriter(), resultMsg, 0);
				return;
			}

		}

		if (overTime.getId() == null) {
			overTime.setId(Long.valueOf(UniqueIdUtil.genId()));
			this.overTimeService.add(overTime);
			if (overTime.getWorkType().shortValue() == 1)
				resultMsg = "加班情况添加成功";
			else
				resultMsg = "请假情况添加成功";
		} else {
			this.overTimeService.update(overTime);
			if (overTime.getWorkType().shortValue() == 1)
				resultMsg = "加班情况更新成功";
			else {
				resultMsg = "请假情况更新成功";
			}
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
		try {
			LogThreadLocalHolder.putParamerter("id", overTime.getId()
					.toString());
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}

	@ModelAttribute
	protected OverTime getFormObject(@RequestParam("id") Long id, Model model)
			throws Exception {
		this.logger.debug("enter OverTime getFormObject here....");
		OverTime overTime = null;
		if (id != null)
			overTime = (OverTime) this.overTimeService.getById(id);
		else {
			overTime = new OverTime();
		}
		return overTime;
	}
}