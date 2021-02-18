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
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.worktime.model.WorkTimeSetting;
import com.cssrc.ibms.worktime.service.WorkTimeService;
import com.cssrc.ibms.worktime.service.WorkTimeSettingService;
 
 @Controller
 @RequestMapping({"/oa/worktime/workTimeSetting/"})
 @Action(ownermodel=SysAuditModelType.WORKTIME_WORKTIMESETTING)
 public class WorkTimeSettingFormController extends BaseFormController
 {
 
   @Resource
   private WorkTimeSettingService workTimeSettingService;
 
   @Resource
   private WorkTimeService workTimeService;
 
   @RequestMapping({"save"})
   @Action(description="添加或更新班次设置", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>新班次设置：【${SysAuditLinkService.getWorkTimeSettingLink(Long.valueOf(id))}】")
   public void save(HttpServletRequest request, HttpServletResponse response, WorkTimeSetting workTimeSetting, BindingResult bindResult)
     throws Exception
   {
     ResultMessage resultMessage = validForm("workTimeSetting", workTimeSetting, bindResult, request);
 
     if (resultMessage.getResult() == 0)
     {
       writeResultMessage(response.getWriter(), resultMessage);
       return;
     }
     String resultMsg = null;
     boolean isadd = workTimeSetting.getId() == null;
     Long settingId;
     if (workTimeSetting.getId() == null) {
       settingId = Long.valueOf(UniqueIdUtil.genId());
       workTimeSetting.setId(settingId);
       this.workTimeSettingService.add(workTimeSetting);
       resultMsg = "班次设置添加成功";
     } else {
       settingId = workTimeSetting.getId();
       this.workTimeSettingService.update(workTimeSetting);
       resultMsg = "班次设置更新成功";
     }
 
     String[] startTime = request.getParameterValues("startTime");
     String[] endTime = request.getParameterValues("endTime");
     String[] memo = request.getParameterValues("desc");
 
     this.workTimeService.workTimeAdd(settingId, startTime, endTime, memo);
 
     writeResultMessage(response.getWriter(), resultMsg, 1);
     try
     {
       LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
       LogThreadLocalHolder.putParamerter("id", workTimeSetting.getId().toString());
     } catch (Exception e) {
       e.printStackTrace();
       this.logger.error(e.getMessage());
     }
   }
 
   @ModelAttribute
   protected WorkTimeSetting getFormObject(@RequestParam("id") Long id, Model model)
     throws Exception
   {
     this.logger.debug("enter WorkTimeSetting getFormObject here....");
     WorkTimeSetting workTimeSetting = null;
     if (id != null)
       workTimeSetting = (WorkTimeSetting)this.workTimeSettingService.getById(id);
     else {
       workTimeSetting = new WorkTimeSetting();
     }
     return workTimeSetting;
   }
 }