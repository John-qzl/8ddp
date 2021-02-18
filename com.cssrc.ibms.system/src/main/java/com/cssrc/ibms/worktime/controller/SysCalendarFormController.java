 package com.cssrc.ibms.worktime.controller;
 
 import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.worktime.service.SysCalendarService;
 
 @Controller
 @RequestMapping({"/oa/worktime/sysCalendar/"})
 @Action(ownermodel=SysAuditModelType.WORKTIME_SYSCALENDAR)
 public class SysCalendarFormController extends BaseFormController
 {
 
   @Resource
   private SysCalendarService sysCalendarService;
 
   @RequestMapping({"save"})
   @Action(description="添加或更新系统日历", execOrder=ActionExecOrder.AFTER, detail="<#assign entity=calJson?eval /><#if entity.id==0>添加<#else>更新</#if>系统日历【${entity.name}】")
   public void save(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     String json = RequestUtil.getString(request, "calJson");
     ResultMessage resultMessage = null;
     try
     {
       this.sysCalendarService.saveCalendar(json);
       resultMessage = new ResultMessage(1, "编辑日历成功!");
     }
     catch (Exception ex) {
       String str = MessageUtil.getMessage();
       if (StringUtil.isNotEmpty(str)) {
         resultMessage = new ResultMessage(0, "编辑日历失败:" + str);
         response.getWriter().print(resultMessage);
       } else {
         String message = ExceptionUtil.getExceptionMessage(ex);
         resultMessage = new ResultMessage(0, message);
         response.getWriter().print(resultMessage);
       }
     }
     writeResultMessage(response.getWriter(), resultMessage);
   }
 }