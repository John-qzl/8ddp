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
import com.cssrc.ibms.worktime.model.CalendarSetting;
import com.cssrc.ibms.worktime.service.CalendarSettingService;
 
 @Controller
 @RequestMapping({"/oa/worktime/calendarSetting/"})
 @Action(ownermodel=SysAuditModelType.WORKTIME_CALENDSETTING)
 public class CalendarSettingFormController extends BaseFormController
 {
 
   @Resource
   private CalendarSettingService calendarSettingService;
 
   @RequestMapping({"save"})
   @Action(description="添加或更新日历设置")
   public void save(HttpServletRequest request, HttpServletResponse response, CalendarSetting calendarSetting, BindingResult bindResult)
     throws Exception
   {
     ResultMessage resultMessage = validForm("calendarSetting", calendarSetting, bindResult, request);
 
     if (resultMessage.getResult() == 0)
     {
       writeResultMessage(response.getWriter(), resultMessage);
       return;
     }
     String resultMsg = null;
     if (calendarSetting.getId() == null) {
       calendarSetting.setId(Long.valueOf(UniqueIdUtil.genId()));
       this.calendarSettingService.add(calendarSetting);
       resultMsg = "添加日历设置成功";
     } else {
       this.calendarSettingService.update(calendarSetting);
       resultMsg = "更新日历设置成功";
     }
     writeResultMessage(response.getWriter(), resultMsg, 1);
   }
 
   @ModelAttribute
   protected CalendarSetting getFormObject(@RequestParam("id") Long id, Model model)
     throws Exception
   {
     this.logger.debug("enter CalendarSetting getFormObject here....");
     CalendarSetting calendarSetting = null;
     if (id != null)
       calendarSetting = (CalendarSetting)this.calendarSettingService.getById(id);
     else {
       calendarSetting = new CalendarSetting();
     }
     return calendarSetting;
   }
 }