package com.cssrc.ibms.worktime.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.worktime.model.WorkTime;
import com.cssrc.ibms.worktime.service.WorkTimeService;

@Controller
@RequestMapping({ "/oa/worktime/workTime/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_WORKTIME)
public class WorkTimeFormController extends BaseFormController {

	@Resource
	private WorkTimeService workTimeService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新班次时间")
	public void save(HttpServletRequest request, HttpServletResponse response,
			WorkTime workTime, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("workTime", workTime,
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		if (workTime.getId() == null) {
			workTime.setId(Long.valueOf(UniqueIdUtil.genId()));
			this.workTimeService.add(workTime);
			resultMsg = "添加班次时间成功";
		} else {
			this.workTimeService.update(workTime);
			resultMsg = "更新班次时间成功";
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
	}

	@ModelAttribute
	protected WorkTime getFormObject(@RequestParam("id") Long id, Model model)
			throws Exception {
		this.logger.debug("enter WorkTime getFormObject here....");
		WorkTime workTime = null;
		if (id != null)
			workTime = (WorkTime) this.workTimeService.getById(id);
		else {
			workTime = new WorkTime();
		}
		return workTime;
	}
}