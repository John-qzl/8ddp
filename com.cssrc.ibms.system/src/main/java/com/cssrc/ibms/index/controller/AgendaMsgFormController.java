package com.cssrc.ibms.index.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.Agenda;
import com.cssrc.ibms.index.model.AgendaExecut;
import com.cssrc.ibms.index.model.AgendaMsg;
import com.cssrc.ibms.index.service.AgendaMsgService;

/**
 * 日程交流信息Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/calendar/agendaMsg/" })
public class AgendaMsgFormController   extends BaseController{

	@Resource
	private AgendaMsgService service;
	
	@ModelAttribute("agendaMsg")
	public AgendaMsg processForm(HttpServletRequest request)
	{
		Long Id = Long.valueOf(RequestUtil.getLong(request, "Id"));
		AgendaMsg agendaMsg = null;
		if (Id != 0l)
			agendaMsg = (AgendaMsg)this.service.getById(Id);
		else {
			agendaMsg = new AgendaMsg();
		}

		return agendaMsg;
	}
	
	
	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("agendaMsg")  AgendaMsg agendaMsg, BindingResult result) throws Exception
	{

		if (result.hasFieldErrors()) {
			return new JsonResult(false, "false");
		}
		String msg = null;
		
		Long agendaId = RequestUtil.getLong(request, "agendaId");
		
		Long creatorId = UserContextUtil.getCurrentUserId();
		
		String creator = UserContextUtil.getCurrentUser().getFullname();
		//时间格式转换需要时分秒
		Date curdate =new Date();
		agendaMsg.setId(UniqueIdUtil.genId());
		agendaMsg.setAgendaId(agendaId);
		agendaMsg.setReplyId(creatorId);
		agendaMsg.setSendTime(curdate);
		agendaMsg.setReplyer(creator);
		service.add(agendaMsg);
		msg = getText("日程成功添加!", new Object[] { agendaMsg.getSubject() }, "日程成功添加!");

		return new JsonResult(true, msg);
	}
	

}
