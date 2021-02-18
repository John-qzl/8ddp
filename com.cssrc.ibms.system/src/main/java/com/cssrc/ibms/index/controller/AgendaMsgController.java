package com.cssrc.ibms.index.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.Agenda;
import com.cssrc.ibms.index.model.AgendaExecut;
import com.cssrc.ibms.index.model.AgendaMsg;
import com.cssrc.ibms.index.service.AgendaMsgService;
import com.cssrc.ibms.index.service.AgendaService;

/**
 * 日程交流信息Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/calendar/agendaMsg/" })
public class AgendaMsgController   extends BaseController{

	@Resource
	private AgendaMsgService service;
	@Resource
	private AgendaService agendaService;
	
	
	@RequestMapping( { "edit" })
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
		AgendaMsg agendaMsg =new AgendaMsg();
		agendaMsg.setAgendaId(agendaId);
		ModelAndView mv = getAutoView().addObject("agendaMsg", agendaMsg);

		return mv;
	}
	
	@RequestMapping( { "list" })
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView modelView = null;
		try {
			modelView = getAutoView();

			Long curUserId = UserContextUtil.getCurrentUserId();//当前用户判断查看权限
			//管理员账户查看所有，普通用户查看涉及到的
			QueryFilter filter = new QueryFilter(request,"agendaMsgItem");
			filter.addFilterForIB("userId", curUserId);
			List<AgendaMsg> list = service.getAll(filter);
			//封装人员信息
			if(list.size()>0){
				for(AgendaMsg agendaMsg:list){
					Agenda agenda = agendaService.getById(agendaMsg.getAgendaId());
					agendaMsg.setSubject(agenda.getSubject());
				}
			}
			
			modelView.addObject("agendaMsgList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}
	
	
	@RequestMapping( { "get" })
	public ModelAndView get(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView modelView = null;
		try {
			modelView = getAutoView();
			Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
			
			Long curUserId = UserContextUtil.getCurrentUserId();//当前用户的操作权限
			//管理员账户查看所有，普通用户查看涉及到的
			QueryFilter filter = new QueryFilter(request,"agendaMsgItem");
			filter.addFilterForIB("userId", curUserId);
			filter.addFilterForIB("agendaId", agendaId);//获得当前日程
			List<AgendaMsg> list = service.getAll(filter);
			
			modelView.addObject("agendaMsgList", list).addObject("curUserId", curUserId).addObject("agendaId", agendaId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}
	
	
	@RequestMapping({"del"})
	@ResponseBody
	public JsonResult del(HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
		String uId = request.getParameter("ids");
		if (StringUtils.isNotEmpty(uId)) {
			String[] ids = uId.split(",");
			for (String id : ids) {
				this.service.delById(Long.valueOf(id));
			}
		}
		return new JsonResult(true, "成功删除！");
	}
}
