 package com.cssrc.ibms.worktime.controller;
 
 import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.worktime.model.WorkTime;
import com.cssrc.ibms.worktime.model.WorkTimeSetting;
import com.cssrc.ibms.worktime.service.WorkTimeService;
import com.cssrc.ibms.worktime.service.WorkTimeSettingService;
 
 @Controller
 @RequestMapping({"/oa/worktime/workTimeSetting/"})
 @Action(ownermodel=SysAuditModelType.WORKTIME_WORKTIMESETTING)
 public class WorkTimeSettingController extends BaseController
 {
 
   @Resource
   private WorkTimeSettingService workTimeSettingService;
 
   @Resource
   private WorkTimeService workTimeService;
 
   @RequestMapping({"list"})
   @Action(description="查看班次设置分页列表", detail="查看班次设置分页列表")
   public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     List list = this.workTimeSettingService.getAll(new QueryFilter(request, "workTimeSettingItem"));
     ModelAndView mv = getAutoView().addObject("workTimeSettingList", list);
 
     return mv;
   }
 
   @RequestMapping({"del"})
   @Action(description="删除班次设置", execOrder=ActionExecOrder.BEFORE, detail="删除班次设置：<#list StringUtils.split(id,\",\") as item><#assign entity=workTimeSettingService.getById(Long.valueOf(item))/>【${entity.name}】</#list>")
   public void del(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     String preUrl = RequestUtil.getPrePage(request);
     ResultMessage message = null;
     try {
       Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
 
       for (Long settingId : lAryId) {
         List<WorkTime> workTimelist = this.workTimeService.getBySettingId(settingId);
         for (WorkTime worktime : workTimelist) {
           this.workTimeService.delById(worktime.getId());
         }
       }
 
       this.workTimeSettingService.delByIds(lAryId);
       message = new ResultMessage(1, "删除班次设置成功!");
     }
     catch (Exception ex) {
       message = new ResultMessage(0, "删除失败:" + ex.getMessage());
     }
     addMessage(message, request);
     response.sendRedirect(preUrl);
   }
 
   @RequestMapping({"edit"})
   @Action(description="编辑班次设置", detail="<#if isAdd>添加编辑班次设置<#else>编辑班次设置:<#assign entity=workTimeSettingService.getById(Long.valueOf(id))/>【${entity.name}】</#if>")
   public ModelAndView edit(HttpServletRequest request)
     throws Exception
   {
     Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
 
     ModelAndView mv = getAutoView();
     String returnUrl = RequestUtil.getPrePage(request);
     mv.addObject("returnUrl", returnUrl);
 
     List workTimelist = new ArrayList();
     WorkTimeSetting workTimeSetting = null;
     boolean isadd = true;
     if (id.longValue() != 0L) {
       workTimeSetting = (WorkTimeSetting)this.workTimeSettingService.getById(id);
       workTimelist = this.workTimeService.getBySettingId(id);
       mv.addObject("workTimelist", workTimelist);
       isadd = false;
     } else {
       workTimeSetting = new WorkTimeSetting();
     }
 
     LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
     mv.addObject("workTimeSetting", workTimeSetting);
 
     return mv;
   }
 
   @RequestMapping({"get"})
   @Action(description="查看班次设置明细", detail="查看班次设置明细")
   public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     long id = RequestUtil.getLong(request, "id");
     long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
     WorkTimeSetting workTimeSetting = (WorkTimeSetting)this.workTimeSettingService.getById(Long.valueOf(id));
 
     List workTimelist = this.workTimeService.getBySettingId(Long.valueOf(id));
 
     return getAutoView().addObject("workTimeSetting", workTimeSetting)
       .addObject("workTimelist", workTimelist).addObject("canReturn", Long.valueOf(canReturn));
   }
 }