package com.cssrc.ibms.worktime.controller;

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
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.worktime.model.Vacation;
import com.cssrc.ibms.worktime.service.VacationService;

@Controller
@RequestMapping({ "/oa/worktime/vacation/" })
@Action(ownermodel = SysAuditModelType.WORKTIME_VACATION)
public class VacationFormController extends BaseFormController {

	@Resource
	private VacationService vacationService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新法定假期设置", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>法定假期设置 :【${SysAuditLinkService.getVacationLink(Long.valueOf(id))}】")
	public void save(HttpServletRequest request, HttpServletResponse response,
			Vacation vacation) throws Exception {
		ResultMessage resultMessage = null;
		boolean isadd = vacation.getId() == null;
		String resultMsg = null;
		if (vacation.getId() == null) {
			vacation.setId(Long.valueOf(UniqueIdUtil.genId()));
			this.vacationService.add(vacation);
			resultMsg = "法定假期设置添加成功";
		} else {
			this.vacationService.update(vacation);
			resultMsg = "法定假期设置更新成功";
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id", vacation.getId()
					.toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}

	@ModelAttribute
	protected Vacation getFormObject(@RequestParam("id") Long id, Model model)
			throws Exception {
		this.logger.debug("enter Vacation getFormObject here....");
		Vacation vacation = null;
		if (id != null)
			vacation = (Vacation) this.vacationService.getById(id);
		else {
			vacation = new Vacation();
		}
		return vacation;
	}
}