 package com.cssrc.ibms.core.form.controller;
 
 
 import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.core.constant.system.GlobalTypeConstant;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.model.QuerySql;
import com.cssrc.ibms.core.form.service.QueryFieldService;
import com.cssrc.ibms.core.form.service.QuerySettingService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

 @Controller
 @RequestMapping({"/oa/form/queryField/"})
 public class QueryFieldController extends BaseController
 {
 
   @Resource
   private QueryFieldService sysQueryFieldService;
 
   @Resource
   private QuerySettingService sysQuerySettingService;
   
   @Resource
   private IGlobalTypeService globalTypeService;
 
   @RequestMapping({"save"})
   @Action(description="添加或更新SYS_QUERY_FIELD")
   public void save(HttpServletRequest request, HttpServletResponse response, QueryField sysQueryField)
     throws Exception
   {
     String resultMsg = null;
 
     Long sqlId = Long.valueOf(RequestUtil.getLong(request, "sqlId", 0L));
     try
     {
       String jsonArrStr = request.getParameter("jsonArrStr");
       List sysQueryFie = this.sysQueryFieldService.getSysQueryFieldArr(jsonArrStr);
       this.sysQueryFieldService.saveOrUpdate(sysQueryFie);
 
       if ((sysQueryFie != null) && (!sysQueryFie.isEmpty()) && (sqlId.longValue() != 0L)) {
         QuerySetting sysQuerySetting = this.sysQuerySettingService.getBySqlId(sqlId);
         if (sysQuerySetting != null)
         {
           this.sysQuerySettingService.syncSettingAndField(sysQuerySetting, sysQueryFie,null);
         }
       }
 
       resultMsg = getText("更新成功", new Object[] { "SYS_QUERY_SQL" });
       writeResultMessage(response.getWriter(), resultMsg, 1);
     } catch (Exception e) {
       e.printStackTrace();
       writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
     }
   }
 
   @RequestMapping({"list"})
   @Action(description="查看SYS_QUERY_FIELD分页列表")
   public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     Long sqlId = Long.valueOf(RequestUtil.getLong(request, "sqlId", 0L));
 
     List list = this.sysQueryFieldService.getListBySqlId(sqlId);
 
     return getAutoView().addObject("sysQueryFields", list);
   }
 
   @SuppressWarnings("unchecked")
   @RequestMapping({"editDetail"})
   @Action(description="查看SYS_QUERY_FIELD分页列表")
   public ModelAndView editDetail(HttpServletRequest request, HttpServletResponse response)
     throws Exception
   {
     Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
 
     List<IGlobalType> globalTypes = (List<IGlobalType>) this.globalTypeService.getByCatKey(GlobalTypeConstant.CAT_DIC,true);
 
     QueryField sysQueryField = (QueryField)this.sysQueryFieldService.getById(id);
 
     return getAutoView().addObject("sysQueryField", sysQueryField).addObject("globalTypes", globalTypes);
   }
   @RequestMapping({"saveDetail"})
   @Action(description="更新SYS_QUERY_FIELD的控件和格式化")
   public void saveDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
     try {
       String jsonStr = RequestUtil.getString(request, "jsonStr");
       JSONObject json = JSONObject.fromObject(jsonStr);
 
       Long id = Long.valueOf(json.getLong("id"));
       QueryField sysQueryField = (QueryField)this.sysQueryFieldService.getById(id);
 
       sysQueryField.setFormat(json.getString("format"));
       sysQueryField.setControlType((short)json.getInt("controlType"));
       sysQueryField.setControlContent(json.getString("controlContent"));
 
       this.sysQueryFieldService.update(sysQueryField);
 
       QuerySetting sysQuerySetting = this.sysQuerySettingService.getBySqlId(sysQueryField.getSqlId());
       this.sysQueryFieldService.syncConditionControlAndField(sysQueryField, sysQuerySetting);
 
       writeResultMessage(response.getWriter(), "更新成功", 1);
     } catch (Exception e) {
       e.printStackTrace();
       writeResultMessage(response.getWriter(), "更新失败", 0);
     }
   }
 }
 