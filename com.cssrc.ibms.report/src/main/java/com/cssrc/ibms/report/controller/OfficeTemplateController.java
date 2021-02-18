package com.cssrc.ibms.report.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.cssrc.ibms.report.model.OfficeItem;
import com.cssrc.ibms.report.model.OfficeTemplate;
import com.cssrc.ibms.report.service.OfficeTemplateService;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.GlobalTypeConstant;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.zhuozhengsoft.pageoffice.FileSaver;

@RequestMapping("oa/system/officeTemplate")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
@Controller
public class OfficeTemplateController
{
    @Resource
    private OfficeTemplateService officeTemplateService;
    
    @Resource
    private IGlobalTypeService globalTypeService;
    
    @Resource
    ISysFileService sysFileService;
    
    /**
     * 查看office 文件预览
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/preview"})
    public ModelAndView preview(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String opath = request.getParameter("opath");
        ModelAndView mv = new ModelAndView("report/office/previewOffice.jsp");
        return mv.addObject("userId", UserContextUtil.getCurrentUserId().toString())
            .addObject("sysdate", CommonTools.time2String2(new Date()))
            .addObject("opath", opath);
        
    }
    
    /**
     * 查看office 文件预览
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/preview/{fileId}"})
    public ModelAndView readFileById(@PathVariable("fileId") Long fileId, HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        ISysFile sysFile = sysFileService.getById(fileId);
        ModelAndView mv = new ModelAndView("report/office/previewOffice.jsp");
        return mv.addObject("userId", UserContextUtil.getCurrentUserId().toString())
            .addObject("sysdate", CommonTools.time2String2(new Date()))
            .addObject("opath", sysFile.getFilepath());
    }
    
    /**
     * 查看office模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/manage"})
    @Action(description = "查看office报表模板分页列表", detail = "查看office报表模板分页列表")
    public ModelAndView manage(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/office/officeTemplateManage.jsp");
        return mv;
        
    }
    
    /**
     * 查看office模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/uploadTemp"})
    public ModelAndView uploadTemp(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/office/officeTemplateUpload.jsp");
        OfficeTemplate officeTemplate = new OfficeTemplate();
        officeTemplate.setTypeId(RequestUtil.getLong(request, "typeid"));
        // 默认的doc模板
        mv.addObject("officeTemplate", officeTemplate);
        return mv;
        
    }
    
    @RequestMapping({"/saveUpload"})
    public ModelAndView saveUpload(OfficeTemplate officeTemplate, HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        
        ResultMessage result = new ResultMessage(ResultMessage.Fail);
        if (request instanceof MultipartHttpServletRequest)
        {
            MultipartHttpServletRequest mrequest = (MultipartHttpServletRequest)request;
            Map<String, MultipartFile> files = mrequest.getFileMap();
            Iterator<MultipartFile> it = files.values().iterator();
            // 附件保存本地根目录
            String attachPath = request.getSession().getServletContext().getRealPath("/");
            // String attachPath=AppUtil.getAttachPath();
            String filePath = null;
            while (it.hasNext())
            {
                MultipartFile f = it.next();
                // 文件存储路径
                filePath = "/office/" + SysContextUtil.getAppName() + File.separator + new Date().getTime() + ".doc";
                FileOperator.writeByte(attachPath + filePath, f.getBytes());
            }
            officeTemplate.setFilepath(filePath);
            result = officeTemplateService.saveOfficeTemplate(officeTemplate);
            
        }
        PrintWriter writer = response.getWriter();
        writer.print(result.toString());
        return null;
        
    }
    
    /**
     * 查看office模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"/list"})
    @Action(description = "查看office报表模板分页列表", detail = "查看office报表模板分页列表")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/office/officeTemplateList.jsp");
        List<?> list = this.officeTemplateService.getAll(new QueryFilter(request, "officeTemplateItem"));
        mv.addObject("officeTemplateList", list).addObject("typeid", RequestUtil.getLong(request, "Q_typeId_S"));
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
    @Action(description = "删除office报表模板", execOrder = ActionExecOrder.BEFORE, detail = "删除office报表模板：<#list StringUtils.split(reportid,\",\") as item><#assign entity=officeTemplateService.getById(Long.valueOf(item))/>【${entity.title}】</#list>")
    public ModelAndView del(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long[] lAryId = RequestUtil.getLongAryByStr(request, "officeid");
        officeTemplateService.delOfficeById(lAryId);
        return new ModelAndView("redirect:/oa/system/officeTemplate/list.do");
    }
    
    /**
     * 获取office模板标题
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping({"{title}/get"})
    @Action(description = "查看office报表模板明细", detail = "查看office报表模板明细")
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("title") String title)
        throws Exception
    {
        Map<String, Object> result = new HashMap<String, Object>();
        OfficeTemplate officeTemplate = this.officeTemplateService.getByTitle(title);
        if (officeTemplate != null)
        {
            result.put("result", "1");
            result.put("office", officeTemplate);
        }
        else
        {
            result.put("result", "0");
            result.put("message", "报表不存在，请修改报表title");
        }
        PrintWriter writer = response.getWriter();
        writer.print(JSONArray.toJSON(result));
        return null;
    }
    
    /**
     * 修改office模板
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping({"edit"})
    public ModelAndView edit(HttpServletRequest request)
        throws Exception
    {
        Long officeid = Long.valueOf(RequestUtil.getLong(request, "officeid"));
        String returnUrl = RequestUtil.getPrePage(request);
        ModelAndView mv = new ModelAndView("report/office/officeTemplateEdit.jsp");
        OfficeTemplate officeTemplate = null;
        if (officeid.longValue() != 0L)
        {
            officeTemplate = this.officeTemplateService.getById(officeid);
            request.setAttribute("fileName", officeTemplate.getFilepath());
            List<OfficeItem> items = officeTemplateService.getItemByOfficeId(officeid);
            mv.addObject("officeItemList", JSONArray.toJSON(items));
            mv.addObject("tables", officeTemplateService.getTablesByNames(officeTemplate.getTableName().split(",")));
            // 已存在的doc模板
            String appRoot=AppUtil.getRealPath("/");
            File file=new File(appRoot+officeTemplate.getFilepath());
            if(file.exists()){
                request.setAttribute("fileName", officeTemplate.getFilepath());
            }else{
                mv.addObject("fileName", "/office/temp.doc");
            }
        }
        else
        {
            officeTemplate = new OfficeTemplate();
            officeTemplate.setFilepath("/office/" + SysContextUtil.getAppName() + "/" + new Date().getTime() + ".doc");
            officeTemplate.setTypeId(RequestUtil.getLong(request, "typeid"));
            // 默认的doc模板
            mv.addObject("fileName", "/office/temp.doc");
        }
        List<?> list = this.globalTypeService.getByCatKey(GlobalTypeConstant.CAT_REPORT, true);
        return mv.addObject("officeTemplate", officeTemplate)
            .addObject("returnUrl", returnUrl)
            .addObject("typelist", list)
            .addObject("password", AppConfigUtil.get("pluginproperties", "pageOffice.password"))
            .addObject("setparam", RequestUtil.getLong(request, "setparam"))
            .addObject("userId", UserContextUtil.getCurrentUserId().toString())
            .addObject("sysdate", CommonTools.time2String2(new Date()));
    }
    
    /**
     * 保存office模板
     * 
     * @param request
     * @param response
     * @param officeTemplate
     * @throws Exception
     */
    @RequestMapping({"save"})
    @Action(description = "添加或更新office报表模板", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>office报表模板：【${SysAuditLinkService.getOfficeTemplateLink(Long.valueOf(reportid))}】")
    public void save(HttpServletRequest request, HttpServletResponse response, OfficeTemplate officeTemplate)
        throws Exception
    {
        String[] bookmarks = request.getParameterValues("bookmark");
        String allmarks = request.getParameter("allmarks");
        ResultMessage result = officeTemplateService.saveOfficeTemplate(officeTemplate, bookmarks, allmarks);
        PrintWriter writer = response.getWriter();
        writer.print(result.toString());
    }
    
    /**
     * 选择数据源对话框
     * 
     * @param request
     * @return
     */
    @RequestMapping({"dataSource.do"})
    public ModelAndView dataSource(HttpServletRequest request)
    {
        String tableName = request.getParameter("tableName");
        ModelAndView mv = new ModelAndView("report/office/dataSource.jsp");
        if (!StringUtil.isEmpty(tableName))
        {
            mv.addObject("tableName", tableName);
        }
        return mv;
    }
    
    /**
     * 数据源生成树结构
     * 
     * @param request
     * @return
     */
    @RequestMapping({"getDataSource.do"})
    @ResponseBody
    public List<?> getDataSource(HttpServletRequest request)
    {
        Map<String, String> params = new HashMap<String, String>();
        List<Map<String, String>> data = (List<Map<String, String>>)officeTemplateService.getTabs(params);
        return data;
    }
    
    /**
     * 定位书签对话框
     * 
     * @param request
     * @return
     */
    @RequestMapping({"dataColumns.do"})
    public ModelAndView dataColumns(HttpServletRequest request)
    {
        ModelAndView mv = new ModelAndView("report/office/dataColumn.jsp");
        String tableName = request.getParameter("tableName");
        mv.addObject("tableName", tableName);
        return mv;
    }
    
    /**
     * 书签生成树结构
     * 
     * @param request
     * @return
     */
    @RequestMapping({"getDataColumns.do"})
    @ResponseBody
    public List<?> getDataColumns(HttpServletRequest request)
    {
        String tableName = request.getParameter("tableName");
        String[] tabs = null;
        if (!StringUtil.isEmpty(tableName))
        {
            tabs = tableName.split(",");
        }
        if (tabs != null && tabs.length > 0)
        {
            List<Map<String, String>> data = officeTemplateService.getColumnsByTabNames(tabs);
            return data;
        }
        return null;
    }
    
    /**
     * 生成office template 模本文件
     * 
     * @param request
     * @param response
     */
    @RequestMapping({"saveOfficeTemplateFile.do"})
    public void saveOfficeTemplateFile(HttpServletRequest request, HttpServletResponse response)
    {
        String folder = request.getSession().getServletContext().getRealPath("/");
        // String folder=AppUtil.getAttachPath();
        FileSaver fs = new FileSaver(request, response);
        String filePath = fs.getFormField("filePath");
        try
        {
            if (filePath != null && !filePath.equals(""))
            {
                fs.saveToFile(folder + filePath);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            fs.close();
        }
        
    }
    
    /**
     * 根据office模板生成office doc 文档
     * 
     * @param request
     * @param response
     * @param officeId
     * @param dataId
     */
    @RequestMapping({"/{officeId}/{dataId}/extOffice"})
    public ModelAndView extOffice(HttpServletRequest request, HttpServletResponse response,
        @PathVariable("officeId") Long officeId, @PathVariable("dataId") String dataId)
    {
        ModelAndView mv = new ModelAndView("report/office/extOffice.jsp");
        // 本地word 保存路径
        String filepath = AppUtil.getAttachPath() + File.separator + "office" + File.separator;
        File file = new File(filepath);
        if (!file.exists())
        {
            FileOperator.createFolder(filepath);
        }
        OfficeTemplate officeTemplate = officeTemplateService.getById(officeId);
        String filename = officeTemplate.getDataEntry() + "." + officeTemplate.getTitle() + dataId + ".doc";
        File officeFile = new File(filepath + filename);
        if (officeFile.exists())
        {
            mv.addObject("officeFile", filename);
        }
        // 新建一个写类型的WordDocument 往里面写数据
        com.zhuozhengsoft.pageoffice.wordwriter.WordDocument wordWriter =
            new com.zhuozhengsoft.pageoffice.wordwriter.WordDocument();
        // 判断是生成文本还是更新文本内容
        String saveType = request.getParameter("saveType");
        List<Map<String, Object>> dataResult = new ArrayList<>();
        ResultMessage result =
            officeTemplateService.extOfficeReport(officeTemplate, wordWriter, dataId, saveType, dataResult);
        return mv.addObject("result", result.toString())
            .addObject("officeTemplate", officeTemplate)
            .addObject("wordWriter", wordWriter)
            .addObject("dataid", dataId)
            .addObject("ext", request.getParameter("ext"))
            .addObject("userId", UserContextUtil.getCurrentUserId().toString())
            .addObject("sysdate", CommonTools.time2String2(new Date()))
            .addObject("password", AppConfigUtil.get("pluginproperties", "pageOffice.password"))
            .addObject("filepath", officeTemplate.getFilepath())
            .addObject("cellData", JSON.toJSON(dataResult));
    }
    
    /**
     * 生成office word 文件
     * 
     * @param request
     * @param response
     */
    @RequestMapping({"saveOfficeFile"})
    public void saveOfficeFile(HttpServletRequest request, HttpServletResponse response)
    {
        String filepath = AppUtil.getAttachPath() + File.separator + "office" + File.separator;
        FileSaver fs = new FileSaver(request, response);
        
        String tableName = fs.getFormField("tableName");
        String title = fs.getFormField("title");
        String dataid = fs.getFormField("dataid");
        
        try
        {
            if (filepath != null && !filepath.equals(""))
            {
                fs.saveToFile(filepath + tableName + "." + title + dataid + ".doc");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            fs.close();
        }
        
    }
}
