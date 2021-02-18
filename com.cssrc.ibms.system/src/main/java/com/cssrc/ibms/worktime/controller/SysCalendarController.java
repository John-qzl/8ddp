 package com.cssrc.ibms.worktime.controller;
 
 import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.worktime.model.CalendarSetting;
import com.cssrc.ibms.worktime.model.SysCalendar;
import com.cssrc.ibms.worktime.service.CalendarAssignService;
import com.cssrc.ibms.worktime.service.CalendarSettingService;
import com.cssrc.ibms.worktime.service.SysCalendarService;
import com.cssrc.ibms.worktime.service.VacationService;
import com.cssrc.ibms.worktime.service.WorkTimeSettingService;
 
 @Controller
 @RequestMapping({"/oa/worktime/sysCalendar/"})
 @Action(ownermodel=SysAuditModelType.WORKTIME_SYSCALENDAR)
 public class SysCalendarController extends BaseController
 {
 
   @Resource
   private SysCalendarService sysCalendarService;
 
   @Resource
   private WorkTimeSettingService workTimeSettingService;
 
   @Resource
   private CalendarSettingService calendarSettingService;
 
   @Resource
   private VacationService vacationService;
 
   @Resource
   private CalendarAssignService calendarAssignService;
 
   @RequestMapping({"list"})
   @Action(description="查看系统日历分页列表", detail="查看系统日历分页列表")
   public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     List list = this.sysCalendarService.getAll(new QueryFilter(request, "sysCalendarItem"));
     ModelAndView mv = getAutoView().addObject("sysCalendarList", list);
 
     return mv;
   }
 
   @RequestMapping({"del"})
   @Action(description="删除系统日历", execOrder=ActionExecOrder.BEFORE, detail="删除系统日历：<#list StringUtils.split(id,\",\") as item><#assign entity=sysCalendarService.getById(Long.valueOf(item))/>【${entity.name}】</#list>")
   public void del(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     String preUrl = RequestUtil.getPrePage(request);
     ResultMessage message = null;
     try {
       Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
       this.calendarAssignService.delByCalId(lAryId);
       this.calendarSettingService.delByCalId(lAryId);
       this.sysCalendarService.delByIds(lAryId);
       message = new ResultMessage(1, "删除系统日历成功!");
     }
     catch (Exception ex) {
       message = new ResultMessage(0, "删除失败:" + ex.getMessage());
     }
     addMessage(message, request);
     response.sendRedirect(preUrl);
   }
 
   @RequestMapping({"edit"})
   @Action(description="编辑系统日历", detail="<#if isAdd>添加系统日历 <#else>编辑系统日历:<#assign entity=sysCalendarService.getById(Long.valueOf(id))/>【${entity.name}】</#if>")
   public ModelAndView edit(HttpServletRequest request)
     throws Exception
   {
     Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
 
     Calendar cal = Calendar.getInstance();
     int defalutYear = cal.get(1);
     int defaultMon = cal.get(2) + 1;
 
     int year = RequestUtil.getInt(request, "wtYear", defalutYear);
     int mon = RequestUtil.getInt(request, "wtMon", defaultMon);
     String flag = RequestUtil.getString(request, "flag");
 
     if (StringUtils.isNotEmpty(flag)) {
       if (flag.equals("next")) {
         year++; year = mon == 12 ? year : year;
         mon = mon == 12 ? (mon = 1) : mon + 1;
       } else {
         year = mon == 1 ? year - 1 : year;
         mon = mon == 1 ? (mon = 12) : mon - 1;
       }
     }
 
     int mondDays = TimeUtil.getDaysOfMonth(year, mon);
 
     int firstDay = TimeUtil.getWeekDayOfMonth(year, mon);
 
     SysCalendar sysCalendar = new SysCalendar();
 
     Map vacationMap = this.vacationService.getByYearMon(year + "-" + mon + "-01", year + "-" + mon + "-" + mondDays);
 
     boolean monthFlag = false;
 
     Map settingMap = new HashMap();
     boolean isadd = true;
     if (id.longValue() != 0L) {
       sysCalendar = (SysCalendar)this.sysCalendarService.getById(id);
 
       List calSetlist = this.calendarSettingService.getCalByIdYearMon(id, year, mon);
       if ((calSetlist != null) && (calSetlist.size() > 0)) monthFlag = true;
       settingMap = convertList2Map(calSetlist);
       isadd = false;
     }
 
     List wtSetting = this.workTimeSettingService.getAll();
 
     List yearlist = DateUtil.getUpDownFiveYear(cal);
     LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
     return getAutoView()
       .addObject("sysCalendar", sysCalendar)
       .addObject("settingMap", settingMap)
       .addObject("vacationMap", vacationMap)
       .addObject("wtYear", Integer.valueOf(year))
       .addObject("wtMon", Integer.valueOf(mon))
       .addObject("wtSetting", wtSetting)
       .addObject("yearlist", yearlist)
       .addObject("monthFlag", Boolean.valueOf(monthFlag))
       .addObject("mondDays", Integer.valueOf(mondDays))
       .addObject("firstDay", Integer.valueOf(firstDay));
   }
 
   private Map<Integer, CalendarSetting> convertList2Map(List<CalendarSetting> calSetlist) {
     Map map = new HashMap();
     for (CalendarSetting setting : calSetlist) {
       map.put(Integer.valueOf(setting.getDays().shortValue()), setting);
     }
     return map;
   }
 
   @RequestMapping({"get"})
   @Action(description="查看系统日历明细", detail="查看系统日历明细")
   public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     long id = RequestUtil.getLong(request, "id");
     long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
     SysCalendar sysCalendar = (SysCalendar)this.sysCalendarService.getById(Long.valueOf(id));
     return getAutoView().addObject("sysCalendar", sysCalendar).addObject("canReturn", Long.valueOf(canReturn));
   }
 
   @RequestMapping({"setDefault"})
   @Action(description="设置默认日历")
   public void setDefault(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     String preUrl = RequestUtil.getPrePage(request);
     Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
     this.sysCalendarService.setDefaultCal(id);
     addMessage(new ResultMessage(1, "设置默认日历成功!"), request);
     response.sendRedirect(preUrl);
   }
 }