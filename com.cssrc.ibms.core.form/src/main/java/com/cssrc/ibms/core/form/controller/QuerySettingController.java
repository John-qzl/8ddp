package com.cssrc.ibms.core.form.controller;

 
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.form.service.FormTemplateService;
import com.cssrc.ibms.core.form.service.QueryFieldService;
import com.cssrc.ibms.core.form.service.QuerySettingService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping({"/oa/form/querySetting/"})
public class QuerySettingController extends BaseController
{

  @Resource
  private FormTemplateService bpmFormTemplateService;

  @Resource
  private QuerySettingService sysQuerySettingService;

  @Resource
  private QueryFieldService sysQueryFieldService;
  @Resource
  private FormTemplateService formTemplateService;

  @RequestMapping({"edit"})
  @Action(description="编辑SYS_QUERY_SETTING")
  public ModelAndView edit(HttpServletRequest request)
    throws Exception
  {
    Long sqlId = Long.valueOf(RequestUtil.getLong(request, "sqlId", 0L));
    List<FormTemplate> templates = this.bpmFormTemplateService.getQueryDataTemplate();
    QuerySetting sysQuerySetting = this.sysQuerySettingService.getBySqlId(sqlId);
    List<QueryField> sysQueryFields = this.sysQueryFieldService.getDisplayFieldListBySqlId(sqlId);
    return getAutoView().addObject("templates", templates).addObject("sysQuerySetting", sysQuerySetting).addObject("sysQueryFields", sysQueryFields);
  }

  @RequestMapping({"save"})
  @Action(description="添加或更新业务查询数据模板", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>")
  public void save(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String resultMsg = null;
    String sysQuerySettingJson = request.getParameter("sysQuerySettingJson");
    QuerySetting sysQuerySetting = this.sysQuerySettingService.getSysQuerySetting(sysQuerySettingJson);
    try {
      boolean flag = false;
      if ((sysQuerySetting.getId() == null) || (sysQuerySetting.getId().longValue() == 0L)) {
        flag = true;
      }
      this.sysQuerySettingService.save(sysQuerySetting, flag);
      resultMsg = flag ? "添加业务查询模板成功" : "更新业务查询模板成功";
      writeResultMessage(response.getWriter(), resultMsg, 1);
    }
    catch (Exception e) {
      writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
    }
  }

  @RequestMapping({"filterDialog"})
  public ModelAndView filterDialog(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Long sqlId = Long.valueOf(RequestUtil.getLong(request, "sqlId", 0L));

    List<QueryField> sysQueryFields = this.sysQueryFieldService.getListBySqlId(sqlId);
    List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
    return getAutoView().addObject("commonVars", commonVars).addObject("sysQueryFields", sysQueryFields).addObject("source", "1");
  }

  @RequestMapping({"script"})
  public ModelAndView script(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Long sqlId = Long.valueOf(RequestUtil.getLong(request, "sqlId", 0L));
    List<QueryField> sysQueryFields = this.sysQueryFieldService.getListBySqlId(sqlId);

    List<CommonVar> commonVars = CommonVar.getCurrentVars(false);

    return getAutoView().addObject("commonVars", commonVars).addObject("sysQueryFields", sysQueryFields).addObject("source", "1");
  }

  @RequestMapping({"preview"})
  public ModelAndView preview(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String __baseURL = request.getRequestURI().replace("/preview.do", "/getDisplay.do");
    Map<String, Object> params = RequestUtil.getQueryMap(request);
    Map<String, Object> queryParams = RequestUtil.getQueryParams(request);

    Long id = Long.valueOf(RequestUtil.getLong(request, "__displayId__"));
    params.put("__baseURL", __baseURL);
    params.put("__ctx", request.getContextPath());
    params.put("ctx", request.getContextPath());
    params.put("__tic", "sysQuerySetting");

    String html = this.sysQuerySettingService.getDisplay(id, params, queryParams);
    //headHtml
    QuerySetting querySetting = sysQuerySettingService.getById(id);
    String headHtml = formTemplateService.getHeadHtmlDealed(querySetting.getTemplateAlias(), params);
    return getAutoView().addObject("html", html)
    		.addObject("headHtml", headHtml);
  }

  @ResponseBody
  @RequestMapping({"getDisplay"})
  public Map<String, Object> getDisplay(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Map<String,Object> map = new HashMap<String,Object>();
    map.put("success", Boolean.valueOf(true));
    try {
      Map<String,Object> params = RequestUtil.getQueryMap(request);

      Map<String,Object> queryParams = RequestUtil.getQueryParams(request);
      Long id = Long.valueOf(RequestUtil.getLong(request, "__displayId"));
      String __baseURL = request.getRequestURI();
      params.put("__baseURL", __baseURL);
      params.put("__ctx", request.getContextPath());
      params.put("__displayId__", id.toString());
      params.put("__filterKey__", RequestUtil.getString(request, "__filterKey__"));
      params.put("__isQueryData", Boolean.valueOf(true));
      params.put("__tic", "sysQuerySetting");
      String html = this.sysQuerySettingService.getDisplay(id, params, queryParams);
      map.put("html", html);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("success", Boolean.valueOf(false));
      map.put("msg", e.getMessage());
    }
    return map;
  }

  @RequestMapping({"exportData"})
  @Action(description="导出业务数据模板数据", detail="导出业务数据模板数据")
  public void exportData(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
	  try {
		Map<String,Object> params = RequestUtil.getQueryMap(request);
		Long id = Long.valueOf(RequestUtil.getLong(request, "__displayId__"));
		
		int exportType = RequestUtil.getInt(request, "__exportType__");   
		HSSFWorkbook wb = this.sysQuerySettingService.export(id, exportType, params);
		String fileName = "SysQuery_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd")+".xls";
		String fullPath = SysConfConstant.UploadFileFolder+"\\temp"+File.separator+fileName;
		ExcelUtil.writeExcel(fullPath, wb);
		ResultMessage message = new ResultMessage(ResultMessage.Success,"");
		message.addData("path", fullPath);
		writeResultMessage(response.getWriter(), message);
	}catch(Exception e) {
		ResultMessage message = new ResultMessage(ResultMessage.Fail,e.getMessage());
        writeResultMessage(response.getWriter(), message);
	}
  }

  @RequestMapping({"editTemplate"})
  public ModelAndView editTemplate(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
    QuerySetting sysQuerySetting = (QuerySetting)this.sysQuerySettingService.getById(id);
    return getAutoView().addObject("sysQuerySetting", sysQuerySetting);
  }

  @RequestMapping({"saveTemplate"})
  public void saveTemplate(HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String resultMsg = "";
    Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
    String templateHtml = RequestUtil.getString(request, "templateHtml");

    templateHtml = templateHtml.replace("''", "'");
    QuerySetting sysQuerySetting = (QuerySetting)this.sysQuerySettingService.getById(id);
    sysQuerySetting.setTemplateHtml(templateHtml);
    this.sysQuerySettingService.update(sysQuerySetting);
    resultMsg = "更新自定义表管理显示模板成功";
    writeResultMessage(response.getWriter(), resultMsg, 1);
  }
}