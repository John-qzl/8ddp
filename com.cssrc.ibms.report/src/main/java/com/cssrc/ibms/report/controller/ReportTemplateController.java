package com.cssrc.ibms.report.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.report.inf.ISignService;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.core.constant.system.GlobalTypeConstant;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.report.model.ReportParam;
import com.cssrc.ibms.report.model.ReportTemplate;
import com.cssrc.ibms.report.service.ReportParamService;
import com.cssrc.ibms.report.service.ReportTemplateService;

@RequestMapping("oa/system/reportTemplate")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
@Controller
public class ReportTemplateController extends BaseController
{
    @Resource
    private ReportTemplateService reportTemplateService;
    
    @Resource
    private ReportParamService reportParamService;
    
    @Resource
    private IGlobalTypeService globalTypeService;
    
    @Resource
    private ISignService signService;
    @Resource
    IFormDefService formDefService;
    
    @Resource
    IDataTemplateService dataTemplateService;
    
    
    private final String finerreport = "finerreport";
    
    /**
     * report manage jsp
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/manage"})
    public ModelAndView manage(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        return new ModelAndView("report/freport/reportTemplateManage.jsp");
    }
    
    /**
     * 查看office模板列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/list"})
    @Action(description = "查看报表模板分页列表", detail = "查看报表模板分页列表")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/freport/reportTemplateList.jsp");
        List<?> list = this.reportTemplateService.getAll(new QueryFilter(request, "reportTemplateItem"));
        mv.addObject("reportTemplateList", list).addObject("typeid", RequestUtil.getLong(request, "typeid"));
        return mv;
    }
    
    /**
     * 删除office模板
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping({"del"})
    @Action(description = "删除报表模板", execOrder = ActionExecOrder.BEFORE, detail = "删除报表模板：<#list StringUtils.split(reportid,\",\") as item><#assign entity=reportTemplateService.getById(Long.valueOf(item))/>【${entity.title}】</#list>")
    public void del(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String preUrl = RequestUtil.getPrePage(request);
        ResultMessage message = null;
        try
        {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "reportid");
            this.reportTemplateService.delReportTemplates(lAryId);
            message = new ResultMessage(1, "删除报表模板成功!");
        }
        catch (Exception ex)
        {
            message = new ResultMessage(0, "删除失败:" + ex.getMessage());
        }
        addMessage(message, request);
        response.sendRedirect(preUrl);
    }
    
    /**
     * get report 模板 by title
     * 
     * @param request
     * @param response
     * @param title
     * @throws Exception
     */
    @RequestMapping({"{title}/get"})
    @Action(description = "查看报表模板明细", detail = "查看报表模板明细")
    public void getByTitle(HttpServletRequest request, HttpServletResponse response, @PathVariable("title") String title)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        ReportTemplate report = (ReportTemplate)this.reportTemplateService.getByTitle(title);
        if (report != null)
        {
            List<ReportParam> params = reportParamService.getByReportid(report.getReportid());
            result.put("result", "1");
            result.put("report", report);
            result.put("params", params);
        }
        else
        {
            result.put("result", "0");
            result.put("message", "报表不存在，请修改报表title");
        }
        PrintWriter writer = response.getWriter();
        writer.print(JSONArray.fromObject(result).toString());
        
    }
    
    /**
     * 编辑office报表模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"edit"})
    public ModelAndView edit(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/freport/reportTemplateEdit.jsp");
        Long reportid = Long.valueOf(RequestUtil.getLong(request, "reportid"));
        String returnUrl = RequestUtil.getPrePage(request);
        ReportTemplate reportTemplate = null;
        if (reportid.longValue() != 0L)
        {
            reportTemplate = (ReportTemplate)this.reportTemplateService.getById(reportid);
        }
        else
        {
            reportTemplate = new ReportTemplate();
            reportTemplate.setTypeid(RequestUtil.getLong(request, "typeid"));
        }
        List<? extends IGlobalType> list = this.globalTypeService.getByCatKey(GlobalTypeConstant.CAT_REPORT, true);
        return mv.addObject("reportTemplate", reportTemplate)
            .addObject("returnUrl", returnUrl)
            .addObject("typelist", list)
            .addObject("setparam", RequestUtil.getLong(request, "setparam"))
            .addObject("reportParamList", reportParamService.getByReportid(reportid));
    }
    
    /**
     * 保存office报表模板
     * 
     * @param request
     * @param response
     * @param reportTemplate
     * @throws Exception
     */
    @RequestMapping({"save"})
    @Action(description = "添加或更新报表模板", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>报表模板：【${SysAuditLinkService.getReportTemplateLink(Long.valueOf(reportid))}】")
    public void save(MultipartHttpServletRequest request, HttpServletResponse response, ReportTemplate reportTemplate)
        throws Exception
    {
        ResultMessage result = new ResultMessage(ResultMessage.Fail);
        Boolean isAdd = reportTemplate.getReportid() == null;
        Map<String, MultipartFile> files = request.getFileMap();
        Iterator<MultipartFile> it = files.values().iterator();
        // 老版本需要上传附件
        if (it.hasNext())
        {
            MultipartFile f = (MultipartFile)it.next();
            String oriFileName = f.getOriginalFilename();
            
            String rootPath = "WEB-INF" + File.separator + "reportlets" + File.separator;
            String filePath = SysContextUtil.getAppName() + File.separator;
            File dir = new File(FileUtil.getRootPath() + rootPath + filePath);
            if (!dir.exists())
            {
                dir.mkdir();
            }
            String fullPath = FileUtil.getRootPath() + rootPath + filePath + oriFileName.replace("\\", File.separator);
            File file = new File(fullPath);
            if (file.exists())
            {
                result = new ResultMessage(ResultMessage.Fail, "报表模板已经存在！");
            }
            else
            {
                FileUtil.writeByte(fullPath, f.getBytes());
                reportTemplate.setReporttype(this.finerreport);
                filePath = filePath + oriFileName.replace("\\", File.separator);
                result = reportTemplateService.saveReportTemplate(reportTemplate, filePath, null);
            }
            
        }
        else if (reportTemplate.getReportid() == null)
        {
            result = this.reportTemplateService.saveReportTemplate(reportTemplate);
        }
        else if (reportTemplate.getReportid() != null)
        {
            this.reportTemplateService.update(reportTemplate);
            result = new ResultMessage(ResultMessage.Success);
        }
        
        PrintWriter writer = response.getWriter();
        writer.print(result.toString());
        try
        {
            LogThreadLocalHolder.setResult((short)result.getResult());
            LogThreadLocalHolder.putParamerter("reportid", reportTemplate.getReportid().toString());
            LogThreadLocalHolder.putParamerter("isAdd", isAdd);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * finre report 报表预览
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"{id}/preView"})
    public ModelAndView preView(@PathVariable("id") Long id, HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/freport/reportPreView.jsp");
        ReportTemplate reportTemplate = this.reportTemplateService.getById(id);
        List<ReportParam> params = reportParamService.getByReportid(id);
        return mv.addObject("reportTemplate", reportTemplate)
            .addObject("params", params)
            .addObject("requestUri", request.getRequestURI() + "?" + request.getQueryString());
    }
    
    /**
     * finre report 报表预览 根据报表的标题预览
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"{title}/titleView"})
    public ModelAndView preView(@PathVariable("title") String title, HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/freport/reportPreView.jsp");
        
        // 判断是否有rpc远程接口 rpcrefname="interfacesImplConsumerCommonService"
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        //数据主键
        String pk = RequestUtil.getString(request, "pk");
        //data template Id
        String displayId = RequestUtil.getString(request, "displayId");
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据rpc远程接口调用数据
            CommonService commonService = (CommonService)AppUtil.getContext().getBean(rpcrefname);
            // 获取业务数据列表显示
            mv = commonService.printReport(mv, title);
            
        }
        else
        {
            // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            ReportTemplate reportTemplate = this.reportTemplateService.getByTitle(title);
            List<ReportParam> params = reportParamService.getByReportid(reportTemplate.getReportid());
            //添加两个默认的参数
            ReportParam pkParam=new ReportParam("id");
            if(!params.contains(pkParam)){
                params.add(new ReportParam("id",pk));
            }
            ReportParam displayIdParam=new ReportParam("displayId");
            if(!params.contains(displayIdParam)){
                params.add(new ReportParam("displayId",displayId));
            }
            String actInstId=this.dataTemplateService.getActInstId(pk,displayId);
            ReportParam actInstIdPram=new ReportParam("actInstId");
            if(!params.contains(actInstIdPram)){
                params.add(new ReportParam("actInstId",actInstId));
            }
            
            
            mv.addObject("reportTemplate", reportTemplate);
            mv.addObject("params", params);
        }
        
        return mv.addObject("requestUri", request.getRequestURI() + "?" + request.getQueryString());
    }
    
    
    /**
     * preview report by FormDef 根据表单标题预览对应的报表模板
     * 
     * @param request
     * @param response
     * @param title
     * @throws Exception
     */
    @RequestMapping({"{formDefId}/formDefIdView"})
    public ModelAndView formDefIdView(HttpServletRequest request, HttpServletResponse response, @PathVariable("formDefId") Long formDefId)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/freport/reportPreView.jsp");
        //默认参数ID
        String id=RequestUtil.getString(request, "__ID__");
        //获取表单
        IFormDef bpmFormDef=formDefService.getById(formDefId);
        //根据表单获取报表模板
        ReportTemplate reportTemplate = this.reportTemplateService.getByTitle(bpmFormDef.getSubject());
        List<ReportParam> params=new ArrayList<ReportParam>();
        if(reportTemplate!=null){
            params = reportParamService.getByReportid(reportTemplate.getReportid());
            mv.addObject("params", params);
        }else{
            //通过表单构造一个报表模板，需要保证finerrport 服务器中存在 对应的报表模板
            reportTemplate=new ReportTemplate(bpmFormDef);
        }
        //添加一个默认参数ID
        params.add(new ReportParam("id",id));
        mv.addObject("params", params);
        mv.addObject("reportTemplate", reportTemplate);
        
        return mv.addObject("requestUri", request.getRequestURI() + "?" + request.getQueryString());

    }
    
    /**
     * finre report 报表签名后pdf下载
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"{title}/signdown"})
    public ModelAndView signDownload(@PathVariable("title") String title, HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        // 判断是否有rpc远程接口 rpcrefname="interfacesImplConsumerCommonService"
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        String refUserSign = RequestUtil.getString(request, "refUserSign");
        byte[] bfile=null;
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据rpc远程接口调用数据
            CommonService commonService = (CommonService)AppUtil.getContext().getBean(rpcrefname);
            // 获取业务数据列表显示
            bfile = commonService.signDownload(request, title,refUserSign);
        }
        else
        {
            // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            bfile = signService.signDownload(request, title,refUserSign);
        
        }
        FileOperator.downLoadFile(request, response, bfile, title+".pdf");
        return null;
    }
    
    
    /**
     * finre report 报表预览 根据报表的标题预览
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"{title}/titleList"})
    public ModelAndView preList(@PathVariable("title") String title, HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/freport/reportPreList.jsp");
        
        // 判断是否有rpc远程接口 rpcrefname="interfacesImplConsumerCommonService"
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        String filterkey = RequestUtil.getString(request, "filterkey");

        //数据主键
        String pk = RequestUtil.getString(request, "pk");
        //data template Id
        String displayId = RequestUtil.getString(request, "displayId");
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            
        }
        else
        {
            //当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            ReportTemplate reportTemplate = this.reportTemplateService.getByTitle(title);
            //通过模板解析查询头
            String queryHtml=reportTemplateService.explainQueryHtml(displayId,filterkey);
            mv.addObject("queryHtml", queryHtml);
            mv.addObject("reportTemplate", reportTemplate);
            mv.addObject("pk", pk);
        }
        
        return mv.addObject("requestUri", request.getRequestURI() + "?" + request.getQueryString());
    }
    

    
}
