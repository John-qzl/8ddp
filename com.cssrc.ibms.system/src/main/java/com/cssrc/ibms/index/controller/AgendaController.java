package com.cssrc.ibms.index.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.Agenda;
import com.cssrc.ibms.index.model.AgendaExecut;
import com.cssrc.ibms.index.service.AgendaExecutService;
import com.cssrc.ibms.index.service.AgendaService;

/**
 * 日程信息Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/calendar/agenda/" })
public class AgendaController   extends BaseController{

	@Resource
	private AgendaService service;
	
	@Resource
	private AgendaExecutService agendaExecutService;
	
	/**
	 * fullCalendar获取数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "calendar" })
	@ResponseBody
	public Object calendar(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取时间区间
		Date startTime = DateFormatUtil.parseDateTime(request.getParameter("startTime")); 
		Date endTime = DateFormatUtil.parseDateTime(request.getParameter("endTime")); 
		JSONArray jsonAry = new JSONArray();//保存日程数据
		Long curUserId = UserContextUtil.getCurrentUserId();//当前用户参与的日程
		List<Agenda> dataList = this.service.getCalendarList(startTime, endTime, curUserId);
		
		if (dataList.size() > 0)
		{
			for(Agenda  agenda : dataList){
				//完成的去除
				if(agenda.getStatus()==1L){
					continue;
				}
				JSONObject obj = new JSONObject();
				obj.put("id", agenda.getAgendaId().toString());
				String tile = agenda.getSubject()+" 创建人:"+agenda.getCreator();
				obj.put("title", tile);
				obj.put("start", DateFormatUtil.formaDatetTime(agenda.getStartTime()).toString());
				obj.put("end", DateFormatUtil.formaDatetTime(agenda.getEndTime()).toString());
				jsonAry.add(obj);
			}
		}
		return jsonAry;
	}
	
	
	@RequestMapping( { "show" })
	@ResponseBody
	public Object show(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//获取时间区间
		Date startTime = DateFormatUtil.parseDateTime(request.getParameter("startTime")); 
		Date endTime = DateFormatUtil.parseDateTime(request.getParameter("endTime"));
		List<Date> dateList = this.service.getDates(startTime, endTime);
		JSONObject dataObject = new JSONObject();//返回显示数据
		JSONObject eventObject = new JSONObject();//保存日程详细数据
		JSONObject dateObejct = new JSONObject();//保存日程日期数据
		
		Long curUserId = UserContextUtil.getCurrentUserId();//当前用户参与的日程
		List<Agenda> dataList = this.service.getCalendarList(startTime, endTime, curUserId);
		//添加日程备注信息
		for(Agenda  agenda : dataList){
			//完成的去除
			if(agenda.getStatus()==1L){
				continue;
			}
			Date start = agenda.getStartTime();
			if(DateUtil.isBefore(start,endTime)){
				JSONArray eventAry = new JSONArray();
				List<String> list = new ArrayList<String>();
				list.add(agenda.getAgendaId().toString());
				list.add(agenda.getSubject());
				list.add(DateFormatUtil.formatDate(start));
				list.add(start.getHours()+":"+start.getMinutes());
				if(start.getHours()>12){
					list.add("PM");
				}else{
					list.add("AM");
				}
				eventAry = JSONArray.fromObject(list);
				eventObject.put(agenda.getAgendaId().toString(),eventAry);
			}
	
		}
		
		//添加日程时间戳
		for(int i=0;i<dateList.size();i++){
			Date date = dateList.get(i);
			String dateStr = DateFormatUtil.formatDate(date);
			for(Agenda  agenda : dataList){
				//完成的去除
				if(agenda.getStatus()==1L){
					continue;
				}
				Date start = agenda.getStartTime();
				Date end = agenda.getEndTime();
				if(DateUtil.isBefore(start,date)&&DateUtil.isBefore(date,end)){
					dateObejct.accumulate(dateStr, agenda.getAgendaId().toString());
				}
			}
		}
		
		
		dataObject.put("dateevents", dateObejct);
		dataObject.put("end", DateFormatUtil.formatDate(endTime));
		dataObject.put("start", DateFormatUtil.formatDate(startTime));
		dataObject.put("events", eventObject);
		return dataObject;
	}
	
	
	
	/**
	 * 日程明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "get" })
	public ModelAndView get(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
		Agenda agenda = this.service.getById(agendaId);
		if (agenda != null) {
			List<AgendaExecut> executList =  agendaExecutService.getByAgendaId(agendaId);
			agendaExecutService.setAllExecutor(agenda, executList);//塞入人员信息
		}
		
		ModelAndView mv = getAutoView().addObject("agenda", agenda);
		
		return mv;
	}
	
	/**
	 * 日程详细窗口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "detail" })
	public ModelAndView detail(HttpServletRequest request,HttpServletResponse response) throws Exception {
		Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
		Agenda agenda = this.service.getById(agendaId);
		
		Long curUserId = UserContextUtil.getCurrentUserId();//当前用户id作为判断操作权限
		
		int isEditOrDel = 0 ;
		if(curUserId.equals(agenda.getCreatorId())){
			isEditOrDel = 1;
		}
		
		ModelAndView mv = getAutoView().addObject("agendaId", agendaId).addObject("isEditOrDel", isEditOrDel);
		
		return mv;
	}
	
	
	
	/**
	 * 编辑日程
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "edit" })
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
		String returnUrl = RequestUtil.getPrePage(request);
		
		Agenda agenda = null;
		if (agendaId != 0L) {
			agenda = this.service.getById(agendaId);
			List<AgendaExecut> executList =  agendaExecutService.getByAgendaId(agendaId);
			agendaExecutService.setAllExecutor(agenda, executList);//塞入人员信息
		} else {
			agenda = new Agenda();
		}
		
		return getAutoView().addObject("agenda", agenda).addObject("returnUrl", returnUrl);
	}
	
	
	@RequestMapping( { "del" })
	public void del(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String uId = request.getParameter("agendaId");
		Long curUserId = UserContextUtil.getCurrentUserId();//当前用户id作为判断删除权限
		ResultMessage message = null;
		try{
			if (StringUtils.isNotEmpty(uId)) {
				
				this.service.delAgendas(uId, curUserId);
				
				message = new ResultMessage(1, "成功删除！");
			}else {
				message  = new ResultMessage(0, "未找到删除ID！");
			}
		}catch(Exception e){
			e.printStackTrace();
			message  = new ResultMessage(0, "删除失败！");
		}
		
		addMessage(message, request);
	}
	
	/**
	 * 结束日程
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "finish" })
	public JsonResult finish(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String msg = null;
		Long agendaId = Long.valueOf(RequestUtil.getLong(request, "agendaId"));
		if(agendaId != null){
			Agenda agenda = this.service.getById(agendaId);
			agenda.setStatus(1L);//设置完成
			Date currdate = new Date();
			agenda.setEndTime(currdate);
			service.update(agenda);
			msg = "日程完成！";
		}
		
		return new JsonResult(true, msg);
	}
	
	
	@RequestMapping( { "getAll" })
	public ModelAndView getAll(HttpServletRequest request,HttpServletResponse response) throws Exception {
		List<Agenda> list = this.service.getAll(new QueryFilter(request,"agendaItem"));
		list = this.service.setAllExecutors(list);
		ModelAndView mv = getAutoView().addObject("agendaList", list);
		return mv;
	}
	
	
	@RequestMapping({"getType"})
	@ResponseBody
	public JSONArray getType(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONArray jsonAry = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("typeId", "工作类型");
		obj.put("typeName", "工作类型");
		jsonAry.add(obj);
		return jsonAry;
	}
	
	
	@RequestMapping("list")
	@Action(description = "日程信息")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception{
		ModelAndView modelView = null;
		try {
			modelView = getAutoView();
			
			Long curUserId = UserContextUtil.getCurrentUserId();//当前用户判断查看权限
			//管理员账户查看所有，普通用户查看涉及到的
			List<Agenda> agendaList = new ArrayList<Agenda>();
			if(curUserId <0L){
						agendaList = service.getAll(new QueryFilter(request,"agendaItem"));
			}else{
						agendaList = this.service.getCalendarList(null, null, curUserId);	//toTo 个人用户查询参数
			}
			//封装人员信息
			List<Agenda> data = this.service.setAllExecutors(agendaList);
			
			modelView.addObject("agendaList", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelView;
	}

	
}
