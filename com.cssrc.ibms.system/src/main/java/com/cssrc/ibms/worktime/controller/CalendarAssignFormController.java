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
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.worktime.model.CalendarAssign;
import com.cssrc.ibms.worktime.service.CalendarAssignService;
 
 @Controller
 @RequestMapping({"/oa/worktime/calendarAssign/"})
 @Action(ownermodel=SysAuditModelType.WORKTIME_CALENDARASSIGN)
 public class CalendarAssignFormController extends BaseFormController
 {
 
   @Resource
   private CalendarAssignService calendarAssignService;
 
   @RequestMapping({"save"})
   @Action(description="添加或更新日历分配", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>日历分配<#assign sysCalendar=sysCalendarService.getById(calendarId)/>【工作日历:${SysAuditLinkService.getSysCalendarLink(Long.valueOf(calendarId))}, 被分配人类型：<#if calendarAssign.assignType==1>人员,被分配人名称：${calendarAssign.assignUserName}<#elseif calendarAssign.assignType==2>组织,被分配组织名称：${calendarAssign.assignUserName}</#if>】")
   public void save(HttpServletRequest request, HttpServletResponse response, CalendarAssign calendarAssign, BindingResult bindResult)
     throws Exception
   {
     try
     {
       LogThreadLocalHolder.putParamerter("calendarAssign", calendarAssign);
       LogThreadLocalHolder.putParamerter("calendarId", calendarAssign.getCanlendarId().toString());
       LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(calendarAssign.getId() == null));
     } catch (Exception e) {
       e.printStackTrace();
       this.logger.error(e.getMessage());
     }
     ResultMessage resultMessage = validForm("calendarAssign", calendarAssign, bindResult, request);
 
     if (resultMessage.getResult() == 0)
     {
       writeResultMessage(response.getWriter(), resultMessage);
       return;
     }
     String resultMsg = "";
     if (calendarAssign.getId() == null) {
       String userid = RequestUtil.getString(request, "assignUid");
       String assignUserName = RequestUtil.getString(request, "assignUserName");
       String[] userids = userid.split(",");
       String[] userNames = assignUserName.split(",");
       for (int idx = 0; idx < userids.length; idx++) {
         CalendarAssign calAss = this.calendarAssignService.getbyAssignId(new Long(userids[idx]));
         if (calAss == null) {
           CalendarAssign ca = new CalendarAssign();
           ca.setId(Long.valueOf(UniqueIdUtil.genId()));
           ca.setCanlendarId(calendarAssign.getCanlendarId());
           ca.setAssignType(calendarAssign.getAssignType());
           ca.setAssignId(new Long(userids[idx]));
           this.calendarAssignService.add(ca);
         }
         else {
           resultMsg = resultMsg + userNames[idx] + ",";
         }
       }
     }
 
     if (resultMsg.length() == 0)
     {
       resultMsg = "日历分配成功";
       writeResultMessage(response.getWriter(), resultMsg, 1);
     }
     else {
       resultMsg = resultMsg + "已分配工作日历";
       writeResultMessage(response.getWriter(), resultMsg, 0);
     }
   }
 
   @ModelAttribute
   protected CalendarAssign getFormObject(@RequestParam("id") Long id, Model model)
     throws Exception
   {
     this.logger.debug("enter CalendarAssign getFormObject here....");
     CalendarAssign calendarAssign = null;
     if (id != null)
       calendarAssign = (CalendarAssign)this.calendarAssignService.getById(id);
     else {
       calendarAssign = new CalendarAssign();
     }
     return calendarAssign;
   }
 }