package com.cssrc.ibms.index.controller;



import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.Agenda;
import com.cssrc.ibms.index.service.AgendaService;

/**
 * 日程信息Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/calendar/agenda/" })
public class AgendaFormController  extends BaseController{

	@Resource
	private AgendaService service;
	
	@ModelAttribute("agenda")
	public Agenda processForm(HttpServletRequest request)
	{
		Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
		Agenda agenda = null;
		if (agendaId != 0l)
			agenda = (Agenda)this.service.getById(agendaId);
		else {
			agenda = new Agenda();
		}

		return agenda;
	}
	
	
	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("agenda")  Agenda agenda, BindingResult result) throws Exception
	{

		if (result.hasFieldErrors()) {
			return new JsonResult(false, "false");
		}
		String msg = null;
		
		Long agendaId = RequestUtil.getLong(request, "agendaId");
		Date startTime =DateFormatUtil.parseDateTime( request.getParameter("startTime"));
		Date endTime =DateFormatUtil.parseDateTime( request.getParameter("endTime"));
		
		Long creatorId = UserContextUtil.getCurrentUserId();
		
		String creator = UserContextUtil.getCurrentUser().getFullname();
		//时间格式转换需要时分秒
		agenda.setStartTime(startTime);
		agenda.setEndTime(endTime);
		if (agendaId == 0L) {
			agenda.setAgendaId(UniqueIdUtil.genId());
			agenda.setCreator(creator);
			agenda.setCreatorId(creatorId);
			service.add(agenda);
			service.addExecutData(agenda);
			msg = getText("日程成功添加!", new Object[] { agenda.getSubject() }, "日程成功添加!");
		}else{
			service.update(agenda);
			service.addExecutData(agenda);
			msg = getText("日程成功更新!", new Object[] { agenda.getSubject() }, "日程成功更新!");
		}
		
		

		return new JsonResult(true, msg);
	}
	

}
