package com.cssrc.ibms.core.form.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.com.cssrc.ibms.solrclient.data.FileQueryResult;
import org.com.cssrc.ibms.solrclient.intf.ISolrQueryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.intf.IDataTemplateRequestService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.job.intf.IJobService;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.rec.intf.IRecTypeService;
import com.cssrc.ibms.api.rec.model.IRecType;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.IDemensionService;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISolrService;
import com.cssrc.ibms.api.system.intf.ISysBusEventService;
import com.cssrc.ibms.api.system.intf.ISysFileFolderService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.IDemension;
import com.cssrc.ibms.api.system.model.ISysBusEvent;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.model.ISysFileFolder;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTypeService;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.form.dao.FormHandlerDao;
import com.cssrc.ibms.core.form.excel.ExcelCheck;
import com.cssrc.ibms.core.form.model.BusBak;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FileAttachRights;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.service.BusBakService;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.form.service.FormDefService;
import com.cssrc.ibms.core.form.service.FormFieldService;
import com.cssrc.ibms.core.form.service.FormHandlerService;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.form.service.FormTemplateService;
import com.cssrc.ibms.core.form.util.DataTemplateUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * <pre>
 * 对象功能:业务数据模板 控制器类 
 * 开发人员:zhulongchao
 * </pre>
 */
@Controller
@RequestMapping("/oa/form/dataTemplate/")
@Action(ownermodel = SysAuditModelType.FORM_MANAGEMENT)
public class DataTemplateController extends BaseController
{
    @Resource
    private FormFieldService formFieldService;
    
    @Resource
    private DataTemplateService dataTemplateService;
    
    @Resource
    private ISysParameterService sysParameterService;
    
    @Resource
    private FormTableService formTableService;
    
    @Resource
    private FormTemplateService formTemplateService;
    
    @Resource
    private FormDefService formDefService;
    
    @Resource
    private FormHandlerService formHandlerService;
    
    @Resource
    private IDefinitionService definitionService;
    
    @Resource
    private IProcessRunService processRunService;
    
    @Resource
    private FormHandlerDao formHandlerDao;
    
    @Resource
    private ISysBusEventService sysBusEventService;
    
    @Resource
    ISysFileFolderService sysFileFolderService;
    
    @Resource
    ISysFileService sysFileService;
    
    @Resource
    private IGlobalTypeService globalTypeService;
    
    @Resource
    private BusBakService busBakService;
    
    @Resource
    private IRecTypeService recTypeService;
    
    @Resource
    IDemensionService demensionService;
    @Resource
    IJobService jobService;
    @Resource
    ISysOrgTypeService sysOrgTypeService;
    @Resource
	ISolrService solrService;

    /**
     * 根据alisa获取当前displayId,用于前端确定displayId的值
     * 前端传值:alisa_1,alias_2,alias_3,alias_4,...
     * 期望返回值:[{alias_1:xxxxxxxxxxx},{alias_2:xxxxxxxxxx},{alias_3:xxxxxxxxxx}...]
     * 为了防止可能出现的中文乱码问题,请使用getDisplayByFormAlias
     * @deprecated
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("getDisplayBySubjects")
    public void getDisplayBySubjects(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String allSubjects=request.getParameter("allSubjects");
        //遍历数据,返回json
        String displayIds=dataTemplateService.getDisplayBySubjects(allSubjects);
        response.getWriter().print(JSONArray.fromObject(displayIds).toString());
    }

    /**
     * 根据formAlisa获取当前displayId,用于前端确定displayId的值
     * 前端传值:formAlisa_1,formAlisa_2,formAlisa_3,formAlisa_4,...
     * 期望返回值:[{formAlisa_1:xxxxxxxxxxx},{formAlisa_2:xxxxxxxxxx},{formAlisa_3:xxxxxxxxxx}...]
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("getDisplayIdByFormAliases")
    public void getDisplayByFormAliases(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String allFormAliases=request.getParameter("allFormAliases");
        //遍历数据,返回json
        String displayIds=dataTemplateService.getDisplayIdByFormAliases(allFormAliases);
        response.getWriter().print(JSONArray.fromObject(displayIds).toString());
    }


    /**
     * 添加或更新业务数据模板。
     * 
     * @param request
     * @param response
     
     * @return
     * @throws Exception
     */
    @RequestMapping("save")
    @Action(description = "添加或更新业务数据模板",exectype = SysAuditExecType.UPDATE_TYPE, execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>业务数据模板:【${SysAuditLinkService.getDataTemplateLink(Long.valueOf(tempId))}】")
    @DataNote(beanName = { DataTemplate.class }, pkName = "tempId")
    public void save(HttpServletRequest request, HttpServletResponse response)throws Exception {

        String resultMsg = null;
        DataTemplate bpmDataTemplate = getFormObject(request);
        try
        {
            boolean flag = false;
            if (bpmDataTemplate.getId() == null || bpmDataTemplate.getId() == 0)
                flag = true;
            else
                flag = false;
            dataTemplateService.save(bpmDataTemplate, flag);
            resultMsg =
                flag ? getText("record.added", getText("controller.bpmTableTemplate")) : getText("record.updated",
                    getText("controller.bpmTableTemplate"));
            LogThreadLocalHolder.putParamerter("isAdd", flag);
            LogThreadLocalHolder.putParamerter("tempId", bpmDataTemplate.getId().toString());
            writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Success);
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), ResultMessage.Fail);
        }
    }
    /**
     * 从前台传来用户操作的节点，存入session中，用于获取当前用户操作的具体节点名
     */
    @RequestMapping("setTabId")
    public void setTabId(HttpServletRequest request, HttpServletResponse response){
    	String tabid=RequestUtil.getString(request,"tabid");
    	request.getSession().setAttribute("tabid", tabid);
    	
    }
    /**
     * 取得 DataTemplate 实体
     * 
     * @param request
     * @return
     * @throws Exception
     */
    protected DataTemplate getFormObject(HttpServletRequest request)
        throws Exception
    {
        
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] {"yyyy-MM-dd"})));
        
        String json = RequestUtil.getString(request, "json", false);
        if (StringUtil.isEmpty(json))
            return null;
        JSONObject obj = JSONObject.fromObject(json);
        
        String displayField = obj.getString("displayField");
        String conditionField = obj.getString("conditionField");
        
        String sortField = obj.getString("sortField");
        String subSortField = obj.getString("subSortField");
        String relSortField = obj.getString("relSortField");
        String filterField = obj.getString("filterField");
        String manageField = obj.getString("manageField");
        String exportField = obj.getString("exportField");
        
        obj.remove("displayField");
        obj.remove("conditionField");
        obj.remove("sortField");
        obj.remove("subSortField");
        obj.remove("filterField");
        obj.remove("manageField");
        
        DataTemplate bpmDataTemplate = (DataTemplate)JSONObject.toBean(obj, DataTemplate.class);
        // 处理外键列,在查询条件字段json中加入元素relFormDialog
        conditionField = dataTemplateService.genConditionFieldRelFormDialog(conditionField, bpmDataTemplate);
        
        bpmDataTemplate.setDisplayField(displayField);
        bpmDataTemplate.setConditionField(conditionField);
        bpmDataTemplate.setSortField(sortField);
        bpmDataTemplate.setSubSortField(subSortField);
        bpmDataTemplate.setRelSortField(relSortField);
        bpmDataTemplate.setFilterField(filterField);
        bpmDataTemplate.setManageField(manageField);
        bpmDataTemplate.setExportField(exportField);
        
        return bpmDataTemplate;
    }
    
    /**
     * 取得业务数据模板分页列表
     * 
     * @param request
     * @param response
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @Action(description = "查看业务数据模板分页列表",detail="查看业务数据模板分页列表",exectype=SysAuditExecType.SELECT_TYPE)
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        List<DataTemplate> list = dataTemplateService.getAll(new QueryFilter(request, "bpmDataTemplateItem"));
        ModelAndView mv = this.getAutoView().addObject("bpmDataTemplateList", list);
        
        return mv;
    }
    
    /**
     * 删除业务数据模板
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("del")
    @Action(description = "删除业务数据模板", execOrder = ActionExecOrder.BEFORE, 
    		exectype=SysAuditExecType.DELETE_TYPE,detail = "删除业务数据模板："
        + "<#list StringUtil.split(id,\",\") as item>"
        + "<#assign entity=dataTemplateService.getById(Long.valueOf(item))/>" + "【${entity.name}】" + "</#list>")
    public void del(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String preUrl = RequestUtil.getPrePage(request);
        ResultMessage message = null;
        try
        {
            Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
            dataTemplateService.delByIds(lAryId);
            message =
                new ResultMessage(ResultMessage.Success, getText("record.deleted",
                    getText("controller.bpmTableTemplate")));
        }
        catch (Exception ex)
        {
            message =
                new ResultMessage(ResultMessage.Fail, getText("record.delete.fail",
                    getText("controller.bpmTableTemplate")) + ex.getMessage());
        }
        addMessage(message, request);
        response.sendRedirect(preUrl);
    }
    
    /**
     * 编辑业务数据模板
     * 
     * @param request
     *
     * @throws Exception
     */
    @RequestMapping("edit")
    @Action(description = "编辑业务数据模板")
    public ModelAndView edit(HttpServletRequest request)
        throws Exception
    {
        Long tableId = RequestUtil.getLong(request, "tableId");
        Long formKey = RequestUtil.getLong(request, "formKey");
        
        FormTable bpmFormTable = formTableService.getByTableId(tableId, 0);
        List<FormTemplate> templates = formTemplateService.getDataTemplate();
        DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
        List<? extends IRecType> recTypes = this.recTypeService.getAll();
        if (BeanUtils.isEmpty(bpmDataTemplate))
        {
            bpmDataTemplate = new DataTemplate();
            bpmDataTemplate.setFormKey(formKey);
            bpmDataTemplate.setTableId(tableId);
            bpmDataTemplate.setDisplayField(DataTemplateUtil.getDisplayField(bpmFormTable, ""));
            bpmDataTemplate.setExportField(DataTemplateUtil.getExportField(bpmFormTable, ""));
        }
        else
        {
            Long defId = bpmDataTemplate.getDefId();
            if (BeanUtils.isNotEmpty(defId))
            {
                IDefinition bpmDefinition = definitionService.getById(defId);
                if (BeanUtils.isNotEmpty(bpmDefinition))
                    bpmDataTemplate.setSubject(bpmDefinition.getSubject());
            }
            bpmDataTemplate.setDisplayField(DataTemplateUtil.getDisplayField(bpmFormTable, bpmDataTemplate.getDisplayField()));
            
            bpmDataTemplate.setExportField(DataTemplateUtil.getExportField(bpmFormTable, bpmDataTemplate.getExportField()));
        }
        bpmDataTemplate.setSource((bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL) ? DataTemplate.SOURCE_CUSTOM_TABLE
            : DataTemplate.SOURCE_OTHER_TABLE);
        return this.getAutoView()
            .addObject("bpmFormTable", bpmFormTable)
            .addObject("bpmDataTemplate", bpmDataTemplate)
            .addObject("templates", templates)
            .addObject("recTypes", JSON.toJSONString(recTypes))
            .addObject("fieldList", JSON.toJSONString(bpmFormTable.getFieldList()));
    }   
    // ==== end 导出字段
    /**
     * 取得业务数据模板明细
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("get")
    @Action(description = "查看业务数据模板明细")
    public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        long id = RequestUtil.getLong(request, "id");
        DataTemplate bpmDataTemplate = dataTemplateService.getById(id);
        return getAutoView().addObject("bpmDataTemplate", bpmDataTemplate);
    }
    
    /**
     * 预览
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("preview")
    public ModelAndView preview(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
    	
        // 取得当前页URL,如有参数则带参数
        String __baseURL = request.getRequestURI().replace("/preview.do", "/getDisplay.do");
        Map<String, Object> params = RequestUtil.getQueryMap(request);
        Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
        Object obj=params.get("batchName");
        // 取得传入参数ID __displayId__ 为IBMS_DATA_TEMPLATE表的id
        Long id = RequestUtil.getLong(request, "__displayId__");
        params.put("__baseURL", __baseURL);
        params.put("ctx", request.getContextPath());
        params.put(DataTemplate.PARAMS_KEY_CTX, request.getContextPath());
        params.put("__tic", "bpmDataTemplate");
        params.put("fromMultiTabView", RequestUtil.getBoolean(request, "fromMultiTabView"));
        // 获取用户
        Long curUserId = UserContextUtil.getCurrentUserId();
        // 判断是否有rpc远程接口 &rpcrefname="interfacesImplConsumerCommonService"
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        String html = "";
        String headHtml = "";
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据rpc远程接口调用数据
            CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
            // 获取业务数据列表显示
            html = commonService.getDisplay(id, params, queryParams, curUserId);
        }
        else
        {
            // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            html = dataTemplateService.getDisplay(id, params, queryParams, curUserId);
            DataTemplate bpmDataTemplate = dataTemplateService.getById(id);
            //定制 去除游览器缓存
            headHtml = formTemplateService.getHeadHtmlDealed(bpmDataTemplate.getTemplateAlias(), params);
            String[] headjsArray=headHtml.split("\n");
            String headjs="";
            if(headjsArray!=null&&headjsArray.length!=0) {
            	for (String string : headjsArray) {
            		int num=string.indexOf(".js"); 
            		if(num!=-1) {
                    	Random random=new Random();
                    	headjs+=string.substring(0,num+3)+"?num="+random.nextLong()+string.substring(num+3,string.length());
                    }
				} 	
            }
            headHtml=headjs;
        }
        
        return getAutoView().addObject("html", html)
        		.addObject("headHtml", headHtml);
    }
    
    /**
     * 展示数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getDisplay")
    @Action(description = "查询业务数据",execOrder = ActionExecOrder.AFTER, 
    detail = "查询业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,__pk__)}", 
    exectype = SysAuditExecType.SELECT_TYPE,
    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public Map<String, Object> getDisplay(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", true);
        try
        {
            Map<String, Object> params = RequestUtil.getQueryMap(request);
            Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
            Long id = RequestUtil.getLong(request, "__displayId");
            String __baseURL = request.getRequestURI();
            request.getParameterMap();
            params.put("__baseURL", __baseURL);
            params.put("__ctx", request.getContextPath());
            params.put("__displayId__", id.toString());
            request.getParameterMap();
            params.put(DataTemplate.PARAMS_KEY_FILTERKEY,
                RequestUtil.getString(request, DataTemplate.PARAMS_KEY_FILTERKEY));
            // DBom管理-dbom使用：树节点拼接SQL语句
            params.put("__dbomSql__", CommonTools.null2String(request.getParameter("__dbomSql__")));
            // DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
            params.put("__dbomFKName__", CommonTools.null2String(request.getParameter("__dbomFKName__")));
            // DBom管理-dbom使用：子表新增表单时，存储外键字段值
            params.put("__dbomFKValue__", CommonTools.null2String(request.getParameter("__dbomFKValue__")));
            
            params.put(DataTemplate.PARAMS_KEY_ISQUERYDATA, true);
            params.put("__tic", "bpmDataTemplate");
            params.put("fromMultiTabView", RequestUtil.getBoolean(request, "fromMultiTabView"));
            // 对外键显示值参数进行转换，即对含XXXXXFKColumnShow的key进行转换。
            // 存在参数kf_test_idXXXXXFKColumnShow=[Ljava.lang.String;@55f59a60, 无法识别，将[Ljava.lang.String;@55f59a60转换为具体的值。
            if (params != null)
            {
                Set<String> set = params.keySet();
                Iterator<String> it = set.iterator();
                while (it.hasNext())
                {
                    Object obj = it.next();
                    if (obj.toString().endsWith(IFieldPool.FK_SHOWAppCode))
                    {
                        params.put(obj.toString(), RequestUtil.getString(request, obj.toString()));
                    }
                }
            }
            String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
            String html = "";
            Long curUserId = UserContextUtil.getCurrentUserId();
            if (StringUtil.isNotEmpty(rpcrefname))
            {
                // 采用IOC方式，根据rpc远程接口调用数据
                CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
                // 获取业务数据列表显示
                html = commonService.getDisplay(id, params, queryParams, curUserId);
            }
            else
            {
                // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
                html = dataTemplateService.getDisplay(id, params, queryParams, curUserId);
            }
            map.put("html", html);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            map.put("success", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }
    
    /**
     * 编辑模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("editTemplate")
    public ModelAndView editTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long id = RequestUtil.getLong(request, "id");
        DataTemplate bpmDataTemplate = dataTemplateService.getById(id);
        return getAutoView().addObject("bpmDataTemplate", bpmDataTemplate);
    }
    
    /**
     * 保存模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("saveTemplate")
    public void saveTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String resultMsg = "";
        Long id = RequestUtil.getLong(request, "id");
        String templateHtml = RequestUtil.getString(request, "templateHtml");
        
        templateHtml = templateHtml.replace("''", "'");
        DataTemplate bpmDataTemplate = dataTemplateService.getById(id);
        bpmDataTemplate.setTemplateHtml(templateHtml);
        dataTemplateService.update(bpmDataTemplate);
        resultMsg = getText("controller.bpmDataTemplate.saveTemplate");
        writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Success);
    }
    
    /**
     * 过滤条件窗口
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("filterDialog")
    public ModelAndView filterDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long tableId = RequestUtil.getLong(request, "tableId");
        FormTable bpmFormTable = dataTemplateService.getFieldListByTableId(tableId);
        String source =
            (bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL) ? DataTemplate.SOURCE_CUSTOM_TABLE
                : DataTemplate.SOURCE_OTHER_TABLE;
        // 获取mainTable被其他表引用的所有外键列。
        List<FormTable> relTables = formTableService.getRelTableByMainTableId(tableId);
        for (FormTable relTable : relTables)
        {
            Long relTableId = relTable.getTableId();
            List<FormField> relFields = formFieldService.getByTableId(relTableId);
            relTable.setFieldList(relFields);
        }
        List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
        return this.getAutoView()
            .addObject("commonVars", commonVars)
            .addObject("bpmFormTable", bpmFormTable)
            .addObject("tableId", tableId)
            .addObject("relTables", relTables)
            .addObject("isRelTable", IFormField.ISRELTABLE)
            .addObject("source", source);
    }
    
    /**
     * 脚本
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("script")
    public ModelAndView script(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long tableId = RequestUtil.getLong(request, "tableId");
        FormTable bpmFormTable = dataTemplateService.getFieldListByTableId(tableId);
        List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
        String source =
            (bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL) ? DataTemplate.SOURCE_CUSTOM_TABLE
                : DataTemplate.SOURCE_OTHER_TABLE;
        // 获取mainTable被其他表引用的所有外键列。
        List<FormTable> relTables = formTableService.getRelTableByMainTableId(tableId);
        for (FormTable relTable : relTables)
        {
            Long relTableId = relTable.getTableId();
            List<FormField> relFields = formFieldService.getByTableId(relTableId);
            relTable.setFieldList(relFields);
        }
        return this.getAutoView()
            .addObject("commonVars", commonVars)
            .addObject("bpmFormTable", bpmFormTable)
            .addObject("tableId", tableId)
            .addObject("relTables", relTables)
            .addObject("isRelTable", IFormField.ISRELTABLE)
            .addObject("source", source);
    }
    
    /**
     * 编辑业务数据模板数据.
     *
     * <p>
     * detailed comment
     * </p>
     * 
     * @author [创建人] WeiLei <br/>
     *         [创建时间] 2016-9-12 上午09:41:28 <br/>
     *         [修改人] WeiLei <br/>
     *         [修改时间] 2016-9-12 上午09:41:28
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @see
     */
    @RequestMapping("editData")
    @Action(description = "编辑业务数据模板数据", detail = "编辑业务数据模板数据")
    public ModelAndView editData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        
        Long displayId = RequestUtil.getLong(request, "__displayId__");        
        String pk = RequestUtil.getString(request, "__pk__");
        // DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
        String dbomFKName = RequestUtil.getString(request, "__dbomFKName__");
        // DBom管理-dbom使用：子表新增表单时，存储外键字段值
        String dbomFKValue = RequestUtil.getString(request, "__dbomFKValue__");
        String flag = RequestUtil.getString(request, "flag");
     // 用于同层排序自动赋值
    // 	String tcpx = RequestUtil.getString(request, "tcpx");
        // 返回参数
        Long tableId = 0L;
        Long formKey = 0L;
        String tableName = "";
        String pkField = "";
        FormDef bpmFormDef = null;
        ISysBusEvent sysBusEvent = null;
        String ctxPath = request.getContextPath();
        String returnUrl = RequestUtil.getPrePage(request);
        boolean hasPk = StringUtil.isNotEmpty(pk);
        String headHtml = "";
        
        
        // 判断是否有RPC远程过程调用服务
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        
        Long userId = UserContextUtil.getCurrentUserId();
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据RPC远程过程调用服务调用数据
            CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
            try
            {
                dataMap = commonService.editData(displayId, pk, UserContextUtil.getCurrentUserId(), request.getContextPath());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            //当不是RPC远程过程调用服务 或者 远程过程调用超时失败，从本地调用
            dataMap = dataTemplateService.editData(displayId, pk, userId, ctxPath);
        }
        
        tableId = (Long)dataMap.get("tableId");
        pkField = (String)dataMap.get("pkField");
        formKey = (Long)dataMap.get("formKey");
        tableName = (String)dataMap.get("tableName");
        bpmFormDef = (FormDef)dataMap.get("bpmFormDef");
        sysBusEvent = (ISysBusEvent)dataMap.get("sysBusEvent");
        headHtml = (String)dataMap.get("headhtml");
        ModelAndView mv = IDataTemplateRequestService.getCustomEditData(request, getAutoView());
        return mv.addObject("bpmFormDef", bpmFormDef)
            .addObject("id", pk)
            .addObject("pkField", pkField)
            .addObject("tableId", tableId)
            .addObject("tableName", tableName)
            .addObject("returnUrl", returnUrl)
            .addObject("hasPk", hasPk)
            .addObject("alias", formKey)
            .addObject("sysBusEvent", sysBusEvent)
            .addObject("dbomFKName", dbomFKName)
            .addObject("dbomFKValue", dbomFKValue)
            .addObject("displayId", displayId)
            .addObject("isBackData", dataMap.get("isBackData"))
            .addObject("headHtml", headHtml) 
        	.addObject("formKey", formKey)
            .addObject("flag",flag);
       //     .addObject("tcpx",tcpx);
    }
    
    /**
     * 明细数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("detailData")
    @Action(description = "查看业务数据模板明细数据", detail = "查看业务数据模板明细数据")
    public ModelAndView detailData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long id = RequestUtil.getLong(request, "__displayId__");
        String pk = RequestUtil.getString(request, "__pk__");
        // DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
        String dbomFKName = RequestUtil.getString(request, "__dbomFKName__");
        // DBom管理-dbom使用：子表新增表单时，存储外键字段值
        String dbomFKValue = RequestUtil.getString(request, "__dbomFKValue__");
        
        // 判断是否有rpc远程接口 &rpcrefname="interfacesImplConsumerCommonService"
        String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
        
        String contextPath = AppUtil.getContextPath();
        String form = "";String headhtml="";
        Long curUserId = UserContextUtil.getCurrentUserId();
        Long formKey = null;
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据rpc远程接口调用数据
            CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
            // 获取业务数据表单详细
            form = commonService.getFormData(id, pk, curUserId, contextPath);
        }
        else
        {
            // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            Map<String, Object> param=new HashMap<String,Object>();
            param.put(IFormHandlerService._displayId_, id);
            param.put(IFormHandlerService._userId_, curUserId);
            param.put(IFormHandlerService._businessKey_, pk);
            param.put(IFormHandlerService._contextPath_, contextPath);
            param.put(IFormHandlerService._highLight_, false);
            param.put(IFormHandlerService._isCopyFlow_, false);

            Map<String,Object> map = dataTemplateService.getForm(param);
            form = CommonTools.Obj2String(map.get("form"));
            headhtml = CommonTools.Obj2String(map.get("headhtml"));
            String[] headjsArray=headhtml.split("\n");
            String headjs="";
            if(headjsArray!=null&&headjsArray.length!=0) {
            	for (String string : headjsArray) {
            		int num=string.indexOf(".js"); 
            		if(num!=-1) {
                    	Random random=new Random();
                    	headjs+=string.substring(0,num+3)+"?num="+random.nextLong()+string.substring(num+3,string.length());
                    }
				} 	
            }
            headhtml=headjs;
            formKey = map.get("formKey")==null?0L:(Long)map.get("formKey");
        }
        
        return getAutoView().addObject("form", form)
            .addObject("pk", pk)
            .addObject("headHtml", headhtml)
            .addObject("dbomFKName", dbomFKName)
            .addObject("dbomFKValue", dbomFKValue)
            .addObject("ctx", contextPath)
        	.addObject("formKey", formKey);
    }
    
    /**
     * 明细数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("printData")
    @Action(description = "根据模板打印数据申请表单", detail = "根据模板打印数据申请表单")
    public ModelAndView printData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long id = RequestUtil.getLong(request, "__displayId__");
        Long formDefId = RequestUtil.getLong(request, "__formDefId__");
        String pk = RequestUtil.getString(request, "__pk__");
        FormDef bpmFormDef = new FormDef();
        // 判断id是否为空
        if (id != null && id != 0)
        {
            DataTemplate bpmDataTemplate = dataTemplateService.getById(id);
            Long formKey = bpmDataTemplate.getFormKey();
            bpmFormDef = formDefService.getDefaultVersionByFormKey(formKey);
        }
        else if (formDefId != null)
        {
            bpmFormDef = formDefService.getById(formDefId);
        }
        
        //
        // 根据tableId和formKey取得报表信息
        // ReportTemplate reportTamplate = reportTemplateService.doReportTemplate(id,pk);
        // 返回
        ModelAndView ret = getAutoView().addObject("reportName_REC", bpmFormDef.getSubject()).addObject("ID", pk);
        /*
         * //判断是否存在报表信息 if(reportTamplate!=null){ FormTable mainTable =
         * formTableService.getByTableId(reportTamplate.getTableId(), 0); //取得主表数据 //Map mainData =
         * reportTemplateService.doDataById(pk,mainTable.getTableName()); Map mainData =
         * formHandlerDao.getByKey(mainTable.getFactTableName(), pk);
         * 
         * //取得报表模板类型 String reportType = CommonTools.Obj2String(reportTamplate.getType()); //保存到request中
         * ret.addObject("reportType",reportType); //判断是否为ireport
         * if(reportType.equals(ReportTemplateConstants.IREPORT_TYPE_IREPORT_STR)){ //根据Ireport模板生成报表 String
         * resultReportFile = reportTemplateService.doCreateReport(request,response,mainTable,pk,mainData);
         * ret.addObject("modelFileName",resultReportFile); request.getSession().setAttribute("wordDocument", null);
         * }else{ //书签 ret.addObject("reportTemplate",reportTamplate.getContent());
         * ret.addObject("modelFileName",reportTamplate.getFilepath());
         * 
         * //生成到temp文件下，输出到客户端IE WordDocument wordDoc = new WordDocument(); //所有书签 String marks =
         * reportTamplate.getContent(); //判断是否有需要替换书签 if(marks!=null && !marks.equals("")){
         * 
         * 
         * //分隔所有书签 String[] mark = marks.split(",");// 该模板中所有标签 for (int i = 0; i < mark.length; i++) {// 数据入口数据插入标签
         * String markValue = mark[i]; if(markValue.startsWith("PO_B_")){ //插入书签
         * reportTemplateService.doInsertCBM(wordDoc, markValue,mainTable, mainData); }else
         * if(markValue.startsWith("PO_T_")){ //插入表格 //doInsertTBM(wordDoc, key, value, bm, datas, dataEntry); }else
         * if(markValue.startsWith("PO_R_")){ //循环书签 //doInsertRBM(wordDoc, key, value, bm, datas); } } }
         * request.getSession().setAttribute("wordDocument", wordDoc); } //报告名称
         * ret.addObject("reportName",reportTamplate.getName()); //ID ret.addObject("ID",reportTamplate.getId());
         * ret.addObject("formKey", reportTamplate.getFormKey()).addObject("tableId", reportTamplate.getTableId()); }
         */
        return ret;
    }
    
    /**
     * 删除数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("deleteData")
    @Action(description = "删除业务数据",execOrder = ActionExecOrder.BEFORE, 
    detail = "删除业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId__))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,__pk__)}", 
    exectype = SysAuditExecType.DELETE_TYPE,
    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public void deleteData(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String preUrl = RequestUtil.getPrePage(request);
        ResultMessage message = null;
        Long id = RequestUtil.getLong(request, "__displayId__");
        String pk = RequestUtil.getString(request, "__pk__");
        boolean noDirect = RequestUtil.getBoolean(request, "noDirect", false);
        
        try
        {
            // 判断是否有RPC远程过程调用服务
            String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
            if (StringUtil.isNotEmpty(rpcrefname))
            {
                // 采用IOC方式，根据RPC远程过程调用服务调用数据
                CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
                commonService.deleteData(id, pk);
            }
            else
            {
                // 删除列表记录
                dataTemplateService.deleteData(id, pk);
                // 删除该记录绑定的附件文件
                dataTemplateService.delFileOfData(pk);
            }
            //by songchen 根据id 删除索引
            solrService.deleteSqlDataIndex(pk);
            message = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
        }
        catch (Exception ex)
        {
            message = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail") + ":" + ex.getMessage());
        }
        addMessage(message, request);
        if(!noDirect) {
        	response.sendRedirect(preUrl);
        }
       
    }
    /**
     * 导出数据
     * 
     * @param request
     * @param response
     * @throws Exception
     * @throws Exception
     */
    @RequestMapping("exportData")
    @Action(description = "导出业务数据", execOrder = ActionExecOrder.AFTER,
    detail = "${SysAuditLinkService.getExportDesc(isFR,id,exportPK,exportType)}", 
    	    exectype = SysAuditExecType.EXPORT_TYPE,
    	    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public ModelAndView exportData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {// 取得导出类型
        String isFR = RequestUtil.getString(request, "isFR");
        Long id = null;
        String exportPK="";
        int exportType=-1;//1 选中   0全部  2当前页  3列表关联附件
        if (isFR != null && "1".equals(isFR))
        {
            // 判断是否为增加后缀名称
            String isAddFilter = RequestUtil.getString(request, "isAddFilter");
            id = RequestUtil.getLong(request, "__displayId__");
            String filterKey = RequestUtil.getString(request, "__filterKey__");
            
            // 取得模板名称
            String templateName = RequestUtil.getString(request, "templateName");
            // 判断是否为添加后缀
            if (isAddFilter != null && !"".equals(isAddFilter) && "true".equals(isAddFilter) && filterKey != null
                && !"".equals(filterKey))
            {
                filterKey = "_" + filterKey;
            }
            else
            {
                filterKey = "";
            }
            ModelAndView ret = getAutoView();
            DataTemplate bpmDataTemplate = dataTemplateService.getById(id);
            // 判断是否为空
            if (!"".equals(templateName))
            {
                ret.addObject("reportName_LIST", templateName + "_LIST");
            }
            else
            {
                
                Long formKey = bpmDataTemplate.getFormKey();
                FormDef bpmFormDef = formDefService.getDefaultVersionByFormKey(formKey);
                ret.addObject("reportName_LIST", bpmFormDef.getSubject() + filterKey + "_LIST");
            }
            
            // 取得筛选条件
            Map<String, Object> params = RequestUtil.getQueryMap(request);
            // CON_SQL
            String con_sql = dataTemplateService.exportWhereSql(params, bpmDataTemplate);
            // 判断是空
            if (con_sql != null && StringUtil.isNotEmpty(con_sql))
            {
                // 只取得ID
                con_sql = " ID in( select ID from (" + con_sql + "))";
            }
            else
            {
                con_sql = " 1=1 ";
            }
            try {
    			LogThreadLocalHolder.putParamerter("isFR", isFR);
    			LogThreadLocalHolder.putParamerter("id", id);
    			LogThreadLocalHolder.putParamerter("exportPK", exportPK);
    			LogThreadLocalHolder.putParamerter("exportType", exportType);
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error(e.getMessage());
    		}
            // 根据tableId和formKey取得报表信息
            // ReportTemplate reportTamplate = reportTemplateService.doReportTemplate(id,pk);
            // 返回
            return ret.addObject("CON_SQL", con_sql);
        }
        else
        {
            Map<String, Object> params = RequestUtil.getQueryMap(request);
            id = RequestUtil.getLong(request, "__displayId__");
            String[] exportIds = RequestUtil.getStringAryByStr(request, "__exportIds__");
            exportPK=RequestUtil.getString(request,"__exportIds__");
            exportType = RequestUtil.getInt(request, "__exportType__");//1 选中   0全部  2当前页  3列表关联附件
            try {
    			LogThreadLocalHolder.putParamerter("isFR", isFR);
    			LogThreadLocalHolder.putParamerter("id", id);
    			LogThreadLocalHolder.putParamerter("exportPK", exportPK);
    			LogThreadLocalHolder.putParamerter("exportType", exportType);
    		} catch (Exception e) {
    			e.printStackTrace();
    			logger.error(e.getMessage());
    		}
            /*
             * //判断导出类型是否为导出全部数据 if(exportType==3||exportType==0){ String filterKey = RequestUtil.getString(request,
             * "__filterKey__"); //判断是否为全部 if(exportType==0){ filterKey = ""; }else{ filterKey="_"+filterKey; } String
             * fileName = reportTemplateService.export(request, response, id,filterKey); return
             * getAutoView().addObject("exportFileName",fileName); }else{
             */
            // 判断是否有RPC远程过程调用服务
            String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
            Long curUserId = UserContextUtil.getCurrentUserId();
            // 导出关联附件
            if (exportType == 3)
            { // 导出列表附件
                dataTemplateService.exportDataAttach(request, response, id, exportType, params, curUserId);
            }
            else
            {
                if (StringUtil.isNotEmpty(rpcrefname))
                {
                    // 采用IOC方式，根据RPC远程过程调用服务调用数据
                    CommonService commonService = (CommonService)AppUtil.getBean(rpcrefname);
                    commonService.exportExcel(id, exportIds, exportType, params, response, curUserId);
                }
                else
                {
                	try {
                		 HSSFWorkbook wb = dataTemplateService.export(id, exportIds, exportType, params, curUserId);
                         String fileName = "DataTemplate_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd")+".xls";
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
            }
            return null;
        }
        
    }
    
    /**
     * 导入业务数据模板数据
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("importData")
    @Action(description = "开始导入业务数据", execOrder = ActionExecOrder.AFTER,
    detail = "开始导入业务数据<#assign entity=dataTemplateService.getById(Long.valueOf(__displayId__))/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,__pk__)}", 
    	    exectype = SysAuditExecType.IMPORT_TYPE,
    	    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public ModelAndView importData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long __displayId__ = RequestUtil.getLong(request, "__displayId__");
        return getAutoView().addObject("__displayId__", __displayId__);
        
    }
    
    /**
     * 导入数据保存
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("importSave")
    @Action(description = "导入业务数据并保存", execOrder = ActionExecOrder.AFTER,
    detail = "导入业务数据并保存<#assign entity=dataTemplateService.getById(id)/>" + "${sysAuditLinkService.getFormTableDesc(entity.tableId,'')}", 
    	    exectype = SysAuditExecType.IMPORT_TYPE,
    	    ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT)
    public void importSave(MultipartHttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long id = RequestUtil.getLong(request, "__displayId__");
        MultipartFile file = request.getFile("file");
        StringBuffer sb;
        try
        {
            sb = dataTemplateService.importFile(file.getInputStream(), id);
        }
        catch (Exception e)
        {
            sb = new StringBuffer();
            sb.append("导入失败：\r\n");
            sb.append(e.getMessage());
        }
        try {
			LogThreadLocalHolder.putParamerter("id", id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
        ExcelCheck.exportCheckInfo(sb, "导入日志", response);
    }
    
    /**
     * 附件窗口
     *
     * @author YangBo @date 2016年10月17日下午2:29:22
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("fileView")
    @Action(ownermodel = SysAuditModelType.FILE_MANAGEMENT,description = "文件树结构查询", detail = "文件树结构查询",exectype = SysAuditExecType.SELECT_TYPE)
    public void fileView(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        // 后台生成相应的文库文件(针对临时文库)
        Long userId = UserContextUtil.getCurrentUserId();
        String jsonStr = RequestUtil.getStringAry(request, "jsonStr");
        List<? extends ISysFileFolder> list = sysFileFolderService.getFolderByUserId(userId);
        if (null == list || list.size() <= 0)
        {
            list = sysFileFolderService.saveFolder(userId);// 每个用户默认添加的
        }
        Long dataTempId = RequestUtil.getLong(request, "__displayId__");
        // pk为该条记录id
        Long dataId = RequestUtil.getLong(request, "__pk__");
        //是否为明细
        boolean isDetail = RequestUtil.getBoolean(request, "isDetail",false);
        
        // 取出权限数据
        Map<String, Long> permission = null;
        if(isDetail){
        	permission = FileAttachRights.getTreeDetail();
        }else{
        	permission = dataTemplateService.getAttachpermission(dataTempId, userId, 1);
        }
 
        
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        jsonObject.put("dataTempId", dataTempId);
        jsonObject.put("dataId", dataId);
        jsonObject.put("isDetail", isDetail);
        String paramJson = jsonObject.toString();
        
        try
        {
            if (BeanUtils.isNotEmpty(dataTempId))
            {
                // 获取文件
                String root = FileUtil.getRootPath();
                String filePathName =
                    root + File.separator + "attachFile" + File.separator + "fileView" + dataTempId + ".jsp";
                boolean existFile = FileOperator.isFileExist(filePathName);
                if (!existFile)
                {// 本地文件丢失或不存在临时生成一个
                    sysFileService.createFileAttachJSP(dataTempId);
                }
                String url = "/attachFile" + File.separator + "fileView" + dataTempId + ".jsp";
                request.setAttribute("dataTempId", dataTempId);
                request.setAttribute("permission", JSON.toJSONString(permission));
                request.setAttribute("paramJson", paramJson);
                request.setAttribute("userId", userId);
                request.getRequestDispatcher(url).forward(request, response);
            }
            else
            {
                message = new ResultMessage(ResultMessage.Fail, getText("未进行数据模板绑定，无附件模板"));
                writeResultMessage(response.getWriter(), message);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail, getText("页面找不到"));
            writeResultMessage(response.getWriter(), message);
        }
    }
    @RequestMapping("tableFile")
    @Action(ownermodel = SysAuditModelType.FILE_MANAGEMENT,description = "附件列表查询", detail = "业务表单-附件列表查询",exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView tableFile(HttpServletRequest request, HttpServletResponse response)
         throws Exception
    {
        String paramJson = RequestUtil.getString(request, "paramJson");
        JSONObject jsonObject = JSONObject.fromObject(paramJson);
        // 过滤字段
        Long dataId = JSONUtil.getLong(jsonObject, "dataId", 0L);
        Long formKey = JSONUtil.getLong(jsonObject, "formKey",0L);
        boolean isDetail = JSONUtil.getBoolean(jsonObject, "isDetail");//是否为明细
        Long userId = UserContextUtil.getCurrentUserId();
        // 获取该表单的业务表表id和表单定义id
        FormDef formdef = formDefService.getDefaultPublishedByFormKey(formKey);
        Long tableId = formdef.getTableId();
        
        // 取出权限数据
        Map<String, Long> permission = null;
        if(isDetail){
        	permission = FileAttachRights.getListDetail();
        }else{
        	permission = dataTemplateService.getAttachpermissionByFormKey(formKey, userId, 2);
        }
        // 获取附件列表
        List<? extends ISysFile> list = sysFileService.getAttachList(request, jsonObject, 0L, dataId, tableId);
        // 是否显示密级列的参数
        Boolean isShowSecurity = false;
        // 获取密级参数数据
        List<? extends ISysParameter> spDatas = sysParameterService.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
        if (spDatas.size() > 0 && "1".equals(spDatas.get(0).getParamvalue()))
        {
            isShowSecurity = true;
        }
        return this.getAutoView().addObject("permission", JSON.toJSONString(permission))
        		.addObject("securityChineseMap",ISysFile.SECURITY_CHINESE_MAP)
        		.addObject("isShowSecurity",isShowSecurity)
        		.addObject("tableId",tableId)
        		.addObject("dataId",dataId)
        		.addObject("sysFileList",list)
        		.addObject("paramJson",paramJson);      
    }
    /**
     * 附件列表显示 typeId为节点id(globalType表的id)
     *
     * @author YangBo @date 2016年10月17日下午2:28:05
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @RequestMapping("fileList")
    @Action(ownermodel = SysAuditModelType.FILE_MANAGEMENT,description = "附件列表查询", detail = "附件列表查询",exectype = SysAuditExecType.SELECT_TYPE)
    public void fileList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        // String returnUrl = RequestUtil.getUrl(request);
        Map<String, Object> params = RequestUtil.getParameterValueMap(request);
        String paramJson = RequestUtil.getString(request, "paramJson");
        JSONObject jsonObject = JSONObject.fromObject(paramJson);
        // 过滤字段
        Long isRoot = RequestUtil.getLong(request, "isRoot", 0);
        Long dataTempId = JSONUtil.getLong(jsonObject, "dataTempId", 0L);
        Long dataId = JSONUtil.getLong(jsonObject, "dataId", 0L);
        Long userId = UserContextUtil.getCurrentUserId();
        Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
        //是否为明细
        boolean isDetail = JSONUtil.getBoolean(jsonObject, "isDetail");
        // 前置obj的参数获取
        Long fileFolder = JSONUtil.getLong(jsonObject, "fileFolder", 0L);// 用于判断是否存在文件列表：1.存在,0不存在
        Long dimension = JSONUtil.getLong(jsonObject, "dimension", 0L);// 用于判断是否启用维度：1.启用
        // 获取该表单的业务表表id和表单定义id
        DataTemplate dataTemplate = dataTemplateService.getById(dataTempId);
        Long tableId = dataTemplate.getTableId();
        Long formKey = dataTemplate.getFormKey();
        
        // 取出权限数据
        Map<String, Long> permission = null;
        if(isDetail){
        	permission = FileAttachRights.getListDetail();
        }else{
        	permission = dataTemplateService.getAttachpermission(dataTempId, userId, 2);
        }
        try
        {
            // 获取附件列表
            List<? extends ISysFile> list = sysFileService.getAttachList(request, jsonObject, typeId, dataId, tableId);
            // 是否显示密级列的参数
            Boolean isShowSecurity = false;
            // 获取密级参数数据
            List<? extends ISysParameter> spDatas = sysParameterService.getByParamName(ISysFile.IS_DISPLAY_SECURITY);
            if (spDatas.size() > 0 && "1".equals(spDatas.get(0).getParamvalue()))
            {
                isShowSecurity = true;
            }
            if (BeanUtils.isNotEmpty(dataTempId))
            {
                // 获取附件模板
                String root = FileUtil.getRootPath();
                String attachPathName =
                    root + File.separator + "attachFile" + File.separator + "fileList" + dataTempId + ".jsp";
                boolean existAttach = FileOperator.isFileExist(attachPathName);
                if (!existAttach)
                {// 本地文件丢失或不存在临时生成一个
                    sysFileService.createFileAttachJSP(dataTempId);
                }
                String url = "/attachFile" + File.separator + "fileList" + dataTempId + ".jsp";
                request.setAttribute("securityChineseMap", ISysFile.SECURITY_CHINESE_MAP);
                request.setAttribute("tableId", tableId);
                request.setAttribute("isShowSecurity", isShowSecurity);
                request.setAttribute("formKey", formKey);
                request.setAttribute("dataId", dataId);
                request.setAttribute("isRoot", isRoot);
                request.setAttribute("typeId", typeId);
                request.setAttribute("sysFileList", list);
                request.setAttribute("permission", JSON.toJSONString(permission));
                request.setAttribute("fileFolder", fileFolder);
                request.setAttribute("paramJson", paramJson);
                request.setAttribute("dimension", dimension);
                request.getRequestDispatcher(url).forward(request, response);
            }
            else
            {
                ResultMessage message = new ResultMessage(ResultMessage.Fail, getText("未进行数据模板绑定，无附件模板"));
                writeResultMessage(response.getWriter(), message);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ResultMessage message = new ResultMessage(ResultMessage.Fail, getText("附件列表查找错误"));
            writeResultMessage(response.getWriter(), message);
        }
        
    }
    
    /**
     * 表单管理---附件模板---用于设置表单的附件页面定制
     *
     * @author YangBo @date 2016年10月20日上午8:39:17
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("attachTemplate")
    @Action(description = "表单文件附件模板", detail = "表单文件附件模板")
    public ModelAndView attachTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = getAutoView();
        Long formKey = RequestUtil.getLong(request, "formKey");
        Long tableId = RequestUtil.getLong(request, "tableId");
        Long reload = RequestUtil.getLong(request, "reload", 0);// 是否初始化附件模板
        
        Map<String, String> htmlMap = new HashMap<String, String>();
        DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
        Long dataTempId = Long.valueOf(-1);
        if (BeanUtils.isEmpty(bpmDataTemplate))
        {
            htmlMap.put("fileHtml", "未生成业务模板，无法编写此模板！");
            htmlMap.put("attachHtml", "未生成业务模板，无法编写此模板！");
        }
        else
        {
            dataTempId = bpmDataTemplate.getId();
            String fileHtml = bpmDataTemplate.getFileTempHtml();
            String attachHtml = bpmDataTemplate.getAttacTempHtml();
            // 有的记录字段为空或初始化操作，此时初始该字段，给该字段赋值
            if (BeanUtils.isEmpty(fileHtml) || BeanUtils.isEmpty(attachHtml) || reload > 0)
            {
                sysFileService.saveFileAndAttachHtml(dataTempId);
                fileHtml = dataTemplateService.getById(dataTempId).getFileTempHtml();
                attachHtml = dataTemplateService.getById(dataTempId).getAttacTempHtml();
            }
            htmlMap.put("fileHtml", fileHtml);
            htmlMap.put("attachHtml", attachHtml);
        }
        
        return mv.addObject("htmlMap", htmlMap)
            .addObject("formKey", formKey)
            .addObject("tableId", tableId)
            .addObject("dataTempId", dataTempId);
    }
    
    /**
     * 文件附件html模板编辑(分为两步：1.替换业务模板表filehtml字段;2.重置临时文件)
     *
     * @author YangBo @date 2016年10月20日上午11:08:12
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("htmlSave")
    @Action(description = "文件附件html模板编辑", detail = "文件附件html模板编辑")
    public void htmlSave(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long templateId = RequestUtil.getLong(request, "dataTempId");
        // 获取编辑内容
        String fileTempHtml = request.getParameter("fileTempHtml");
        String attacTempHtml = request.getParameter("attacTempHtml");
        DataTemplate dataTemplate = dataTemplateService.getById(templateId);
        dataTemplate.setFileTempHtml(fileTempHtml);
        dataTemplate.setAttacTempHtml(attacTempHtml);
        try
        {
            // 更新附件文件模板字段
            dataTemplateService.update(dataTemplate);
            // 重置临时文件
            sysFileService.createFileAttachJSP(templateId);
            writeResultMessage(response.getWriter(), getText("controller.save.success"), ResultMessage.Success);
        }
        catch (Exception e)
        {
            String message = ExceptionUtil.getExceptionMessage(e);
            ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
            response.getWriter().print(resultMessage);
        }
    }
    
    /**
     * 附件版本历史
     *
     * @author YangBo @date 2016年11月1日下午10:25:08
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("versionHistory")
    @Action(ownermodel = SysAuditModelType.FILE_MANAGEMENT,description = "查看文件历史版本",detail = "查看文件${SysAuditLinkService.getSysFileLink(String.valueOf(fileId))}的历史版本", exectype = SysAuditExecType.SELECT_TYPE)
    public ModelAndView versionHistory(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("/oa/system/sysFileVersionHistory.jsp");
        QueryFilter filter = new QueryFilter(request, "sysFileItem");
        Long fileId = RequestUtil.getLong(request, "fileId");
        ISysFile sysFile = sysFileService.getById(fileId);
        filter.addFilterForIB("parentId", sysFile.getParentId());
        filter.addFilterForIB("isnew", ISysFile.ISOLD_VERSION);// 旧版本
        List<? extends ISysFile> list = this.sysFileService.getFileAttch(filter);
        return mv.addObject("sysFileList", list);
    }
    
    /**
     * 表单管理---流程监控模板
     *
     * @author Liubo
     * @date 2017-01-16 10:52:17
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("processTemplate")
    @Action(description = "流程监控模板", detail = "流程监控模板")
    public ModelAndView processTemplate(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = getAutoView();
        Long formKey = RequestUtil.getLong(request, "formKey");
        Long tableId = RequestUtil.getLong(request, "tableId");
        // 是否初始化流程监控模板
        Long reload = RequestUtil.getLong(request, "reload", 0);
        
        Map<String, String> htmlMap = new HashMap<String, String>();
        DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
        Long dataTempId = Long.valueOf(-1);
        if (BeanUtils.isEmpty(bpmDataTemplate))
        {
            htmlMap.put("processHtml", "未生成业务模板，无法编写此模板！");
        }
        else
        {
            dataTempId = bpmDataTemplate.getId();
            String processHtml = bpmDataTemplate.getProcessTempHtml();
            String processCondition = bpmDataTemplate.getProcessCondition();
            // 有的记录字段为空，此时初始该字段，给该字段赋值
            if (BeanUtils.isEmpty(processHtml) || reload > 0)
            {
                sysFileService.saveProcessTempHtml(dataTempId);
                processHtml = dataTemplateService.getById(dataTempId).getProcessTempHtml();
                
            }
            htmlMap.put("processHtml", processHtml);
            htmlMap.put("processCondition", processCondition);
        }
        
        return mv.addObject("htmlMap", htmlMap)
            .addObject("formKey", formKey)
            .addObject("tableId", tableId)
            .addObject("dataTempId", dataTempId);
    }
    @RequestMapping("processIsStart")
    @Action(description = "是否启动流程", detail = "流程监控业务数据模板数据")
    public void processIsStart(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	 boolean isStart = true;
         Long defId = RequestUtil.getLong(request, "defId");
         Long businessKey = RequestUtil.getLong(request, "__pk__");
         try{
        	 IProcessRun processRun = processRunService.getByBusinessKeyAndDefId(businessKey.toString(),defId);
	         if(BeanUtils.isEmpty(processRun)){
	        	 isStart = false;
	         }
	         response.getWriter().write(Boolean.toString(isStart));
         }catch(Exception e){
        	 //e.printStackTrace();
        	 response.getWriter().write(Boolean.toString(false));
         }
         
    }
    /**
     * 流程监控窗口
     *
     * @author Liubo
     * @date 2017-01-17
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("processView")
    @Action(description = "流程监控", detail = "流程监控业务数据模板数据")
    public void processView(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        // 后台生成相应的文库文件(针对临时文库)
        Long dataTempId = RequestUtil.getLong(request, "__displayId__");
        Long defId = RequestUtil.getLong(request, "defId");
        // pk为该条记录id
        Long businessKey = RequestUtil.getLong(request, "__pk__");
        DataTemplate dataTemplate = dataTemplateService.getById(dataTempId);
        // 获取流程监控条件字段
        String processCondition = dataTemplate.getProcessCondition();
        // 流程别名
        String fk = "";
        // 是否显示最新记录
        String onlyShowLatest = "";
        JSONObject jObject;
        // 监控流程
        List<IProcessRun> processConditionList = new ArrayList<IProcessRun>();
        if (processCondition != null && !processCondition.equals("\n"))
        {
            JSONArray jArray = JSONArray.fromObject(processCondition);
            for (Object ob : jArray)
            {
                jObject = JSONObject.fromObject(ob);
                fk = (String)jObject.get("flowkey");
                onlyShowLatest = (String)jObject.get("onlyShowLatest");
                List<? extends IProcessRun> processConditionList01 =
                    processRunService.getByFlowKey(fk, businessKey.toString());
                if (processConditionList01 != null)
                {
                    if (onlyShowLatest.equals("true") && processConditionList01.size() > 1)
                    {
                        Date processDate = null;
                        IProcessRun iProcessRun = null;
                        for (IProcessRun process : processConditionList01)
                        {
                            if (processDate == null)
                            {
                                processDate = process.getCreatetime();
                                iProcessRun = process;
                            }
                            else
                            {
                                if (processDate.after(process.getCreatetime()))
                                {
                                    processDate = process.getCreatetime();
                                    iProcessRun = process;
                                }
                            }
                        }
                        processConditionList.add(iProcessRun);
                        
                    }
                    else if (processConditionList01.size() > 0)
                    {
                        for (IProcessRun process : processConditionList01)
                        {
                            processConditionList.add(process);
                        }
                    }
                }
            }
        }
        // 当前记录对应的主流程（默认）
        IProcessRun processRun = processRunService.getByBusinessKeyAndDefId(businessKey.toString(), defId);
        try
        {
            if (BeanUtils.isNotEmpty(dataTempId))
            {
                // 获取文件
                String root = FileUtil.getRootPath();
                String filePathName =
                    root + File.separator + "attachFile" + File.separator + "processView" + dataTempId + ".jsp";
                boolean existFile = FileOperator.isFileExist(filePathName);
                if (!existFile)
                {
                    // 本地文件丢失或不存在临时生成一个
                    sysFileService.createProcessJSP(dataTempId);
                }
                String url = "/attachFile" + File.separator + "processView" + dataTempId + ".jsp";
                request.setAttribute("dataTempId", dataTempId);
                request.setAttribute("processRun", processRun);
                request.setAttribute("businessKey", businessKey);
                request.setAttribute("processConditionList", processConditionList);
                request.getRequestDispatcher(url).forward(request, response);
            }
            else
            {
                message = new ResultMessage(ResultMessage.Fail, getText("未进行数据模板绑定，无附件模板"));
                writeResultMessage(response.getWriter(), message);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail, getText("页面找不到"));
            writeResultMessage(response.getWriter(), message);
        }
    }
    
    /**
     * 流程监控html模板编辑(分为两步：1.替换业务模板表processTempHtml字段;2.重置临时文件)
     *
     * @author Liubo
     * @date 2017-01-18
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("processSave")
    @Action(description = "流程监控html模板编辑", detail = "流程监控html模板编辑")
    public void processSave(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long templateId = RequestUtil.getLong(request, "dataTempId");
        Long formKey = RequestUtil.getLong(request, "formKey");
        Long tableId = RequestUtil.getLong(request, "tableId");
        // 获取编辑内容
        String processTempHtml = request.getParameter("processTempHtml");
        String processConditionTemp = request.getParameter("processConditionTemp");
        DataTemplate dataTemplate = dataTemplateService.getById(templateId);
        dataTemplate.setProcessTempHtml(processTempHtml);
        dataTemplate.setProcessCondition(processConditionTemp);
        try
        {
            // 更新附件文件模板字段
            dataTemplateService.update(dataTemplate);
            // 重置临时文件
            sysFileService.createProcessJSP(templateId);
            writeResultMessage(response.getWriter(), getText("controller.save.success"), ResultMessage.Success);
        }
        catch (Exception e)
        {
            String message = ExceptionUtil.getExceptionMessage(e);
            ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
            response.getWriter().print(resultMessage);
        }
    }
    
    /**
     * 获取字段备份数据
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("getBak")
    public ModelAndView getBak(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = getAutoView();
        String pkvalue = request.getParameter("pkvalue");
        String tablename = request.getParameter("tablename");
        String filedname = request.getParameter("filedname");
        List<BusBak> baks = busBakService.getBakData(pkvalue, tablename, filedname);
        if (baks != null && baks.size() > 0)
        {
            mv.addObject("bakList", baks);
        }
        return mv;
    }
    
    @RequestMapping("multiTabCheck")
    @Action(description = "明细多TAB校验")
    public void multiTabCheck(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String resultMsg = "";
        Long formKey = RequestUtil.getLong(request, "formKey");
        boolean multiSet = RequestUtil.getBoolean(request, "multiSet");
        DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
        if (BeanUtils.isEmpty(bpmDataTemplate))
        {
            resultMsg = "未生成业务模板，无法进行明细多TAB设置！";
            writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Fail);
        }
        else
        {
            String recRightField = bpmDataTemplate.getRecRightField();
            if (BeanUtils.isEmpty(recRightField) && !multiSet)
            {
                resultMsg = "未进行明细多TAB设置，无法进行展示！";
                writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Fail);
            }
            else
            {
                writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Success);
            }
        }
    }
    
    @RequestMapping("multiTabEdit")
    @Action(description = "明细多TAB编辑", detail = "明细多TAB编辑")
    public ModelAndView multiTabEdit(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String multiTabHtml = "";
        Long dataTempId = Long.valueOf(-1);
        
        Long formKey = RequestUtil.getLong(request, "formKey");
        Long tableId = RequestUtil.getLong(request, "tableId");
        Long reload = RequestUtil.getLong(request, "reload", 0);// 是否初始化
        
        DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
        FormTable bpmFormTable = formTableService.getByTableId(tableId, 0);
        List<? extends IRecType> recTypes = this.recTypeService.getAll();
        
        dataTempId = bpmDataTemplate.getId();
        multiTabHtml = bpmDataTemplate.getMultiTabTempHtml();
        // 有的记录字段为空或初始化操作，此时初始该字段，给该字段赋值
        if (BeanUtils.isEmpty(multiTabHtml) || reload > 0)
        {
            sysFileService.saveMultiTabHtml(dataTempId);
            multiTabHtml = dataTemplateService.getById(dataTempId).getMultiTabTempHtml();
        }
        ModelAndView mv =
            getAutoView().addObject("multiTabHtml", multiTabHtml)
                .addObject("bpmDataTemplate", bpmDataTemplate)
                .addObject("recTypes", JSON.toJSONString(recTypes))
                .addObject("bpmFormTable", bpmFormTable)
                .addObject("fieldList", JSON.toJSONString(bpmFormTable.getFieldList()));
        return mv;
    }
    
    @RequestMapping("multiTabSettingSave")
    @Action(description = "明细多TAB设置", detail = "明细多TAB设置")
    public void multiTabSettingSave(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String resultMsg = "";
        String json = RequestUtil.getString(request, "json", false);
        if (StringUtil.isEmpty(json))
        {
            return;
        }
        JSONObject obj = JSONObject.fromObject(json);
        
        Long formKey = obj.getLong("formKey");
        String recRightField = obj.getString("recRightField");
        
        try
        {
            DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
            bpmDataTemplate.setRecRightField(recRightField);
            dataTemplateService.update(bpmDataTemplate);
            writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Success);
        }
        catch (Exception e)
        {
            writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), ResultMessage.Fail);
        }
    }
    
    @RequestMapping("multiTabHtmlSave")
    @Action(description = "明细多TAB的HTML编辑", detail = "明细多TAB的HTML编辑")
    public void multiTabHtmlSave(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String resultMsg = null;
        String multiTabTempHtml = RequestUtil.getString(request, "multiTabTempHtml");
        Long formKey = RequestUtil.getLong(request, "formKey");
        ;
        
        try
        {
            DataTemplate bpmDataTemplate = dataTemplateService.getByFormKey(formKey);
            bpmDataTemplate.setMultiTabTempHtml(multiTabTempHtml);
            dataTemplateService.update(bpmDataTemplate);
            
            // 生成或置换JSP临时文件
            sysFileService.createMultiTabJSP(bpmDataTemplate.getId());
            resultMsg = "明细多TAB保存成功！";
            writeResultMessage(response.getWriter(), resultMsg, ResultMessage.Success);
        }
        catch (Exception e)
        {
            writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), ResultMessage.Fail);
        }
    }
    
    @RequestMapping("multiTabView")
    @Action(description = "明细多TAB展示")
    public void multiTabView(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage message = null;
        
        Long __pk__ = RequestUtil.getLong(request, "__pk__");
        Long id = RequestUtil.getLong(request, "__displayId__");
        String typeAlias = RequestUtil.getString(request, "typeAlias");
        try
        {
            if (BeanUtils.isNotEmpty(id))
            {
                String filePathName =
                    FileUtil.getRootPath() + File.separator + "attachFile" + File.separator + ISysFile.MLUTITAB_VIEW
                        + id + ".jsp";
                boolean existFile = FileOperator.isFileExist(filePathName);
                if (!existFile)
                {// 本地文件丢失或不存在临时生成一个
                    sysFileService.createMultiTabJSP(id);
                }
                String jspPath = "/attachFile" + File.separator + ISysFile.MLUTITAB_VIEW + id + ".jsp";
                request.setAttribute("typeAlias", typeAlias);
                request.setAttribute("__pk__", __pk__);
                request.setAttribute("__displayId__", id);
                request.getRequestDispatcher(jspPath).forward(request, response);
            }
            else
            {
                message = new ResultMessage(ResultMessage.Fail, getText("未进行明细多Tab设置，无法进行展示！"));
                writeResultMessage(response.getWriter(), message);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = new ResultMessage(ResultMessage.Fail, getText("未进行明细多Tab设置，无法进行展示！"));
            writeResultMessage(response.getWriter(), message);
        }
    }
    
    /**
     * 表单明细(多tab格式)
     * 
     * @author yangBo
     */
    @RequestMapping("detail")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String returnUrl = RequestUtil.getPrePage(request);
        if (returnUrl != null)
        {
            returnUrl = returnUrl.replace("@", "&");
        }
        // 获取记录主键
        String pkId = RequestUtil.getString(request, "pkId");
        String alias = RequestUtil.getString(request, "alias");
        return getAutoView().addObject("pkId", pkId).addObject("alias", alias).addObject("returnUrl", returnUrl);
    }
    

    /**
     * 表单业务数据模板 过滤条件过滤设置
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("filterDialogSet")
    public ModelAndView filterDialogSet(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = getAutoView();
        String judgeConditionVal = request.getParameter("judgeConditionVal");
        
        List<?extends IDemension> demensionList = this.demensionService.getAll();
        
        String jugeVal = request.getParameter("jugeVal");
        if (StringUtil.isNotEmpty(jugeVal))
        {
            JSONObject obj = JSONObject.fromObject(jugeVal);
            Iterator<?> key = obj.keys();
            while (key.hasNext())
            {
                String k = key.next().toString();
                mv.addObject(k, obj.get(k));
            }
        }
        //根据不同的维度获取组织类型
        List sysOrgTypelist = new ArrayList();
        for(IDemension idemension:demensionList){
            Long demId = idemension.getDemId();
            IDemension demension = (IDemension) this.demensionService.getById(demId);
            List temporaryList = this.sysOrgTypeService.getByDemId(demId.longValue());
            if(!temporaryList.isEmpty()){
                sysOrgTypelist.addAll(temporaryList);
            }
            mv.addObject("sysOrgTypelist",sysOrgTypelist);
        }
        
        return mv.addObject("judgeConditionVal", judgeConditionVal)
            .addObject("demensionList", demensionList)
            .addObject("jobs", jobService.getAll());
    }
    @RequestMapping("exportFieldSet")
    public ModelAndView exportFieldSet(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
	    String colInfo = RequestUtil.getString(request, "colSetting");
	    colInfo = URLDecoder.decode(colInfo,"UTF-8");
	    Set colList = null;
	    if(!BeanUtils.isEmpty(colInfo)){
	    	JSONObject obj = JSONObject.fromObject(colInfo);
	    	colList = obj.keySet();
	    }
        ModelAndView mv = getAutoView();
        return mv.addObject("colSetting",JSONObject.fromObject(colInfo).toString())
        		.addObject("colList",JSON.toJSONString(colList));
    }
	@RequestMapping("urlParamsDialog")
	@Action(description = "urlParams对话框")
	public ModelAndView urlParamsDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Long tableId = Long.valueOf(RequestUtil.getLong(request, "tableId", 0L));
		FormTable bpmFormTable = null;
		if (tableId.longValue() != 0L) {
			bpmFormTable = formTableService.getByTableId(tableId, 1);
		}
		return getAutoView().addObject("dataTemplateFields", bpmFormTable.getFieldList());
	}
	@RequestMapping("getManagePermission")
	@Action(description = "获取按钮信息的权限")
	public void getManagePermission(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Long displayId = RequestUtil.getLong(request, "displayId");	
		Long curUserId = UserContextUtil.getCurrentUserId();
		Long curOrgId = 0L;
		if (null != UserContextUtil.getCurrentOrg()) {
			curOrgId = UserContextUtil.getCurrentOrg().getOrgId();
		}
		JSONObject resObj = new JSONObject();
		if(displayId.equals(0L)){
			resObj.put("success", false);
		}else{
			Map<String, Object> rightMap = dataTemplateService.getRightMap(curUserId, curOrgId);
			DataTemplate bpmDataTemplate = dataTemplateService.getById(displayId);
			String manageField = bpmDataTemplate.getManageField();
			// 权限类型（管理）
			Map<String, Boolean> managePermission = dataTemplateService.getManagePermission(
					DataTemplate.RIGHT_TYPE_MANAGE,manageField, rightMap);
			resObj.put("success", true);
			resObj.put("data", JSON.toJSON(managePermission));
		}
		response.getWriter().write(resObj.toString());						
	}
	@RequestMapping("getFixFields")
	@Action(description = "获取显示列-固定列")
	public void getFixFields(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		Long displayId = RequestUtil.getLong(request, "displayId");	
		Long curUserId = UserContextUtil.getCurrentUserId();
		Long curOrgId = 0L;
		if (null != UserContextUtil.getCurrentOrg()) {
			curOrgId = UserContextUtil.getCurrentOrg().getOrgId();
		}
		JSONObject resObj = new JSONObject();
		if(displayId.equals(0L)){
			resObj.put("success", false);
		}else{
			Map<String, Object> rightMap = dataTemplateService.getRightMap(curUserId, curOrgId);
			DataTemplate bpmDataTemplate = dataTemplateService.getById(displayId);
			Map<String, Boolean>  permission = 
					dataTemplateService.getPermission(DataTemplate.RIGHT_TYPE_SHOW,bpmDataTemplate.getDisplayField(), rightMap);
			JSONArray fixFields = DataTemplateUtil.getFixFields(bpmDataTemplate,permission);
			resObj.put("success", true);
			resObj.put("data", fixFields.toString());
		}
		response.getWriter().write(resObj.toString());						
	}
	@RequestMapping("fileGrid")
    @Action(ownermodel = SysAuditModelType.FILE_MANAGEMENT,description = "附件列表查询", detail = "附件列表查询",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView getFileGrid(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			String fileContainer = RequestUtil.getString(request, "fileContainer");	
			Map map = dataTemplateService.getFileGrid(JSONObject.fromObject(fileContainer));
			return this.getAutoView().addObject("fileGrid",map);
		}catch(Exception e){
			logger.error("getFileGrid执行发生错误！具体异常如下："+e.getMessage());
			return null;
		}
	}
	/**
     * 高级查询窗口
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("advancedQueryDialog")
    public ModelAndView advancedQueryDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
    	Long displayId = RequestUtil.getLong(request, "displayId");
    	Long queryKey = RequestUtil.getLong(request, "queryKey");
    	Long queryName = RequestUtil.getLong(request, "queryName");
    	
    	DataTemplate template = this.dataTemplateService.getById(displayId);  	
    	Long tableId = template.getTableId();   	
        FormTable bpmFormTable = dataTemplateService.getFieldListByTableId(tableId);
        String source =
            (bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL) ? DataTemplate.SOURCE_CUSTOM_TABLE
                : DataTemplate.SOURCE_OTHER_TABLE;
        List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
        String condition = "";//TODO 获取过滤条件（根据displayId、userId、queryKey）;
        return this.getAutoView()
            .addObject("commonVars", commonVars)
            .addObject("bpmFormTable", bpmFormTable)
            .addObject("tableId", tableId)
            .addObject("source", source)
        	.addObject("condition", condition)
        	.addObject("queryKey", queryKey)
        	.addObject("queryName", queryName);
    }
	/**
	 * 导出关联表选择视图
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exportRelTableSelect")
	public ModelAndView exportRelTableSelect(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		Long displayId = RequestUtil.getLong(request, "displayId");
		String mainRel = dataTemplateService.getRelTables(displayId);
		return mv.addObject("mainRelTables", mainRel).addObject("displayId",
				displayId);
	}

	/**
	 * 导出主表、关联表Excel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportRelTable")
	public void exportRelTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long displayId = RequestUtil.getLong(request, "displayId");
		Long relTableId = RequestUtil.getLong(request, "id");
		String relTableName = RequestUtil.getString(request, "name");
		String relTableDesc = RequestUtil.getString(request, "relTableDesc");
		String fullPath = dataTemplateService.exportMainRel(displayId,
				relTableName, relTableId);
		// 下载文件到本地，文件源地址为fullPath，下载后名称为fileName
		FileOperator.downLoadFile(request, response, fullPath, relTableDesc);
	}
}
