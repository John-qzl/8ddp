package com.cssrc.ibms.core.flow.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.intf.IPendingTaskExtService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.api.form.model.IColumnModel;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.page.PageBean1;
import com.cssrc.ibms.core.db.mybatis.page.PageUtils;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.ArrayUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import freemarker.template.TemplateException;

/**
 * @ClassName: IPendingTaskTemplateService
 * @Description: TODO(代办已办任务 模板解析类)
 * @author zxg
 * @date 2017年6月20日 上午10:45:08
 * 
 */
public abstract class IPendingTaskTemplateService
{
    protected Logger logger = LoggerFactory.getLogger(IPendingTaskTemplateService.class);
    
    public static String settype_bus = "bus";// 代办/已办相关的业务字段
    
    public static String tableIdCode = "pendingsetting";// 代办/已办相关的业务字段
    
    public static String settype_all = "all";// 代办/已办都有的字段
    
    public static String settype_already = "already";// 已办特有的字段
    
    public static String settype_pending = "pending";// 待办特有的字段
    
    public static String f_subject = "f_subject";// 事件名称
    
    public static String f_processName = "f_processName";// 流程名称
    
    public static String f_creator = "f_creator";// 创建人
    
    public static String f_createtime = "f_createtime";// 创建时间
    
    public static String f_status = "f_status";// 状态
    
    public static String f_runId = "f_runId";// 状态
    
    public static String f_hasRead = "f_hasRead";// 阅读状态
    
    public static String f_pendingtype = "f_pendingtype";//待办类型
    
    public static String searchFormURLAlready = "/oa/flow/userTask/alreadyMattersList.do";// 状态
    
    public static String searchFormURLPending = "/oa/flow/userTask/pendingMattersList.do";// 状态
    
    public static String[] pending_fields = {"ID_,taskId", "REV_,revision", "EXECUTION_ID_,executionId",
        "PROC_INST_ID_,processInstanceId", "PROC_DEF_ID_,processDefinitionId", "NAME_,name",
        "PARENT_TASK_ID_,parentTaskId", "DESCRIPTION_,pendingtype", "TASK_DEF_KEY_,taskDefinitionKey", "OWNER_,owner",
        "ASSIGNEE_,assignee", "DELEGATION_,delegationState", "PRIORITY_,priority", "CREATE_TIME_,createtime",
        "DUE_DATE_,dueDate"};
    
    public static String[] already_fields = {"run.runId", "run.defId", "run.subject", "run.creatorId", "run.creator",
        "run.createtime", "run.busDescp", "run.status", "run.actInstId", "run.actDefId", "run.businessKey","run.root_businessKey",
        "run.businessUrl", "run.processName", "run.endTime", "run.duration", "run.lastSubmitDuration", "run.pkName",
        "run.tableName", "run.parentId", "run.startOrgId", "run.startOrgName", "run.typeId", "run.formKeyUrl",
        "run.formType", "run.formDefId", "run.flowKey", "run.dsAlias", "run.isFormal", "run.startNode", "run.relRunId",
        "run.globalFlowNo"};
    
    @Resource
    protected IDataTemplateService dataTemplateService;
    
    @Resource
    protected INodeSetService nodeSetService;
    
    @Resource
    protected IFormDefService formDefService;
    
    @Resource
    protected IFormFieldService formFieldService;
    
    @Resource
    protected IFormTableService formTableService;
    
    @Resource
    protected IFormTemplateService formTemplateService;
    
    @Resource
    protected FreemarkEngine freemarkEngine;
    
    @Resource
    protected IDefinitionService definitionService;
    
    @Resource
    GroovyScriptEngine groovyScriptEngine;
    @Resource
    BpmService bpmService;
    @Resource
    IProcessRunService processRunService;
    
    /**
     * @Title: pendingMatters
     * @Description: TODO(解析待办已办任务模板生成html)
     * @param @param params 参数map
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String pareTemplateMattersView(Map<String, Object> params)
    {
        IDefinition definition=this.getDefinition(params);
        if (definition != null)
        {
            String pendingSetting = definition.getPendingSetting();
            if (StringUtil.isNotEmpty(pendingSetting))
            {
                JSONObject json = JSON.parseObject(pendingSetting);
                String templateAlias = json.get("templateAlias").toString();
                templateAlias+="View";
                // 第一次解析
                String html = this.pareTemplateView(templateAlias, json);
                //logger.info(html);
                //第二次解析
                html = pareTemplateView(html, params, json);
                //logger.info(html);
                return html;
            }
        }
        return null;
        
    }
    

    /**
     * @Title: pareTemplate
     * @Description: TODO(第一次解析待办已办任务详细页面模板)
     * @param @param templateAlias
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String pareTemplateView(String templateAlias, JSONObject pendingSetting)
    {
        JSONArray _displayFields = JSON.parseArray(pendingSetting.getString("displayField"));
        
        String html = this.formTemplateService.getByTemplateAlias(templateAlias).getHtml();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkField", f_runId);
        map.put("defId", pendingSetting.get("defId"));
        // 显示的字段
        map.put("displayField", _displayFields);
        // 表字段 下拉数据
        Map<String, Object> formatData =
            this.dataTemplateService.getFormatDataMap(pendingSetting.getLong("tableId"), 0);
        formatData.put(f_status, this.getStatus());
        formatData.put(f_hasRead, this.getHasRead());
        formatData.put(f_pendingtype, this.getPendingType());
        map.put("formatData", formatData);

        try
        {
            // logger.info(html);
            html = freemarkEngine.parseByStringTemplate(map, html);
            return html;
        }
        catch (TemplateException | IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @Title: pareTemplate
     * @Description: TODO(第二次解析待办已办任务模板)
     * @param @param templateAlias
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public String pareTemplateView(String template, Map<String, Object> param, JSONObject pendingSetting)
    {
        try
        {
            JSONArray _displayFields = JSON.parseArray(pendingSetting.getString("displayField"));
            Map<String, Object> map = new HashMap<String, Object>();
            // 现实的字段
            map.put("displayField", _displayFields);
            // 查询条件
            map.put("param", param);
            //
            map.put("service", AppUtil.getBean("formControlService"));
            map.put("tableId", pendingSetting.getString("tableId"));

            // 权限，查询条件的权限
            Long curUserId = UserContextUtil.getCurrentUserId();
            Long curOrgId = UserContextUtil.getCurrentOrgId();
            Map<String, Object> rightMap = dataTemplateService.getRightMap(curUserId, curOrgId);
            Map<String, Boolean> permission = this.getPermissionMap(_displayFields, rightMap);
            // 权限
            map.put("permission", permission);
            // 查询的数据
            String taskSql = this.getTaskSql(param, pendingSetting).toString();
            taskSql+=" AND "+this.getPKField()+"=:"+this.getPKField();
            param.put(this.getPKField(), this.getPKValue(param));
            //logger.info(taskSql);
            String busSql = this.getBusSql(param, pendingSetting).toString();
            //logger.info(busSql);
            StringBuffer sql = new StringBuffer("select * from (").append(taskSql).append(") t_task");
            sql.append(" INNER JOIN (").append(busSql).append(")t_bus ON t_bus.id=t_task.f_root_businessKey");
            // 查询出来的数据列表(列表中为map对象)
            StringBuffer _sql_ = new StringBuffer();
            // 添加排序sql
            _sql_.append("SELECT ").append(this.getFieldSql(pendingSetting));
            _sql_.append(" FROM (").append(sql).append(")t").append(getSortSQL(map));
            // 分页查询
            PagingBean pageBean = this.getPageBean(param);
            JdbcHelper jdbcHelper = this.getConfigJdbcHelper();
            List<Map<String,Object>> list = jdbcHelper.getPage(_sql_.toString(), param, pageBean);
            Map<String,Object>  data=(Map<String, Object>) list.get(0);
            try {
            	 IPendingTaskExtService ipts=AppUtil.getBean(IPendingTaskExtService.class);
            	 if(ipts!=null){
            		 data=ipts.customData(pendingSetting.getLong("tableId"), data);
            	 }
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
            map.put("data", data);
            // 分页htmL
            String pageHtml = getPageHtml(pageBean, map, "");
            //logger.info(pageHtml);
            map.put("pageHtml", pageHtml);
            String html = freemarkEngine.parseByStringTemplate(map, template);
            return html;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @Title: pendingMatters
     * @Description: TODO(解析待办已办任务模板生成html)
     * @param @param params 参数map
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String pareTemplateMatters(Map<String, Object> params)
    {
        Object defId = params.get("defId");
        if (defId != null)
        {
            Long _defId = Long.valueOf(defId.toString());
            IDefinition definition = definitionService.getById(_defId);
            //找到所有子流程的defId
            List<String> defIds=new ArrayList<String>();
            defIds=getSubDefinition(definition,defIds);
            defIds.add(_defId.toString());
            params.put("defId", StringUtil.getArrayAsString(defIds));
            //definition=getRootDefinition(definition);
            //Set<String> keySet = NodeCache.getRootActDefByDefKey(definition.getActDefKey());

            String pendingSetting = definition.getPendingSetting();
            if (StringUtil.isNotEmpty(pendingSetting))
            {
                JSONObject json = JSON.parseObject(pendingSetting);
                String templateAlias = json.get("templateAlias").toString();
                // 第一次解析
                String html = this.pareTemplate(templateAlias, json);
                //logger.info(html);
                //第二次解析
                html = pareTemplate(html, params, json);
                //logger.info(html);
                return html;
            }
        }
        return null;
        
    }
    
    public IDefinition getRootDefinition(IDefinition definition)
    {
        if(StringUtil.isNotEmpty(definition.getKeyPath())){
            definition=this.definitionService.getByDefKey(definition.getKeyPath());
            getRootDefinition(definition);
        }
        return definition;
    }
    
    public List<String> getSubDefinition(IDefinition definition,List<String> defIds)
    {
        List<?extends IDefinition> subDefs=definitionService.getbyKeyPath(definition.getDefKey());
        for(IDefinition def:subDefs){
            defIds.add(def.getDefId().toString());
            getSubDefinition(def,defIds);
        }
        return defIds;
    }
    /**
     * @Title: pareTemplate
     * @Description: TODO(第一次解析待办已办任务模板)
     * @param @param templateAlias
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String pareTemplate(String templateAlias, JSONObject pendingSetting)
    {
        JSONArray _displayFields = JSON.parseArray(pendingSetting.getString("displayField"));
        JSONArray _conditionField = JSON.parseArray(pendingSetting.getString("conditionField"));
        
        String html = this.formTemplateService.getByTemplateAlias(templateAlias).getHtml();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pkField", this.getPKField());
        map.put("defId", pendingSetting.get("defId"));
        // 显示的字段
        map.put("displayField", _displayFields);
        // 查询条件字段
        map.put("conditionField", this.getConditionField(_conditionField));
        // 是否有查询条件
        map.put("hasCondition", _conditionField != null && _conditionField.size() > 0);
        // 表字段 下拉数据
        Map<String, Object> formatData =
            this.dataTemplateService.getFormatDataMap(pendingSetting.getLong("tableId"), 0);
        formatData.put(f_status, this.getStatus());
        formatData.put(f_hasRead, this.getHasRead());
        formatData.put(f_pendingtype, this.getPendingType());

        map.put("formatData", formatData);
        
        try
        {
            // logger.info(html);
            html = freemarkEngine.parseByStringTemplate(map, html);
            return html;
        }
        catch (TemplateException | IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /** 
    * @Title: getConditionField 
    * @Description: TODO(获取查询条件) 
    * @param @param _conditionField
    * @param @return    设定文件 
    * @return Object    返回类型 
    * @throws 
    */
    public JSONArray getConditionField(JSONArray _conditionField)
    {
        JSONArray result=new JSONArray();
        for(Object obj:_conditionField){
            JSONObject json=(JSONObject)obj;
            String type=json.getString("settype");
            if(settype_bus.equals(type)||equalsSetType(type)){
                result.add(json);
            }
        }
        return result;
    }


    /**
     * @Title: pareTemplate
     * @Description: TODO(第二次解析待办已办任务模板)
     * @param @param templateAlias
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String pareTemplate(String template, Map<String, Object> param, JSONObject pendingSetting)
    {
        try
        {
            JSONArray _displayFields = JSON.parseArray(pendingSetting.getString("displayField"));
            Map<String, Object> map = new HashMap<String, Object>();
            // 现实的字段
            map.put("displayField", _displayFields);
            // 查询条件
            map.put("param", param);
            // form url
            map.put("searchFormURL", AppUtil.getContextPath()+this.getUrl());
            map.put("service", AppUtil.getBean("formControlService"));
            map.put("tableId", pendingSetting.getString("tableId"));
            // 权限，查询条件的权限
            Long curUserId = UserContextUtil.getCurrentUserId();
            Long curOrgId = UserContextUtil.getCurrentOrgId();
            Map<String, Object> rightMap = dataTemplateService.getRightMap(curUserId, curOrgId);
            Map<String, Boolean> permission = this.getPermissionMap(_displayFields, rightMap);
            // 权限
            map.put("permission", permission);
            // 查询的数据
            String taskSql = this.getTaskSql(param, pendingSetting).toString();
            //logger.info(taskSql);
            String busSql = this.getBusSql(param, pendingSetting).toString();
            //logger.info(busSql);
            StringBuffer sql = new StringBuffer("select * from (").append(taskSql).append(") t_task");
            sql.append(" INNER JOIN (").append(busSql).append(")t_bus ON t_bus.id=t_task.f_root_businessKey");
            // 查询出来的数据列表(列表中为map对象)
            StringBuffer _sql_ = new StringBuffer();
            // 添加排序sql
            _sql_.append("SELECT ").append(this.getFieldSql(pendingSetting));
            _sql_.append(" FROM (").append(sql).append(")t").append(getSortSQL(map));
            PagingBean pageBean;
            if(param.containsKey("download_No_Page")){
            	pageBean =new PagingBean(1, Integer.MAX_VALUE);
            }else{
            	// 分页查询
                pageBean = this.getPageBean(param);
            }
            
            JdbcHelper jdbcHelper = this.getConfigJdbcHelper();
            logger.info(_sql_.toString());
            List list = jdbcHelper.getPage(_sql_.toString(), param, pageBean);
            map.put("list", list);
            // 分页htmL
            String pageHtml = getPageHtml(pageBean, map, "");
            //logger.info(pageHtml);
            map.put("pageHtml", pageHtml);
            String html = freemarkEngine.parseByStringTemplate(map, template);
            return html;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * @Title: getSortMap
     * @Description: TODO(获取排序的map)
     * @param @param params
     * @param @param tableIdCode
     * @param @return 
     * @return Map<String,String> 返回类型
     * @throws
     */
    public String getSortSQL(Map<String, Object> map)
    {
        
        Map<String, Object> params = (Map<String, Object>)map.get("param");
        String baseUrl = AppUtil.getContextPath() + this.getUrl() + "?";

        // 排序
        String sortField = null;
        String orderSeq = "DESC";
        String newSortField = null;
        if (params.get(tableIdCode + IDataTemplate.SORTFIELD) != null)
        {
            sortField = params.get(tableIdCode + IDataTemplate.SORTFIELD).toString();
        }
        
        if (params.get(tableIdCode + IDataTemplate.ORDERSEQ) != null)
        {
            orderSeq = params.get(tableIdCode + IDataTemplate.ORDERSEQ).toString();
        }
        
        if (params.get(tableIdCode + "__ns__") != null)
        {
            newSortField = (String)params.get(tableIdCode + "__ns__");
        }
        if (StringUtil.isNotEmpty(newSortField))
        {
            if (newSortField.equals(sortField))
            {
                if (orderSeq.equals("ASC"))
                {
                    orderSeq = "DESC";
                }
                else
                {
                    orderSeq = "ASC";
                }
            }
            sortField = newSortField;
            map.put("sortField", sortField);
            map.put("orderSeq", orderSeq);
            map.put(tableIdCode + IDataTemplate.SORTFIELD, sortField);
            map.put(tableIdCode + IDataTemplate.ORDERSEQ, orderSeq);
            baseUrl += tableIdCode + IDataTemplate.SORTFIELD + "=" + sortField;
            baseUrl += "&" + tableIdCode + IDataTemplate.ORDERSEQ + "=" + orderSeq;
            map.put("pageURL", baseUrl);
            return " ORDER BY " + sortField + " " + orderSeq;
        }
        baseUrl += tableIdCode + IDataTemplate.SORTFIELD + "=f_runId";
        map.put("pageURL", baseUrl);
        return " ORDER BY "+this.getPKField();
    }
    
    /**
     * 获取分页的HTML
     * 
     * @param bpmDataTemplate
     * @param map
     * @param tableIdCode
     * @param pageURL
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String getPageHtml(PagingBean pageBean, Map<String, Object> map, String pageURL)
        throws IOException, TemplateException
    {
        String pageHtml = "";
        map.put("tableIdCode", tableIdCode);
        map.put("pageBean", pageBean);
        map.put("showExplain", true);
        map.put("showPageSize", true);
        map.put("baseHref", pageURL);
        String pageTempl = "pageAjax.ftl";
        int type = SysConfConstant.SHOW_TYPE;
        if (type == 0)
        {
            pageTempl = "oldpageAjax.ftl";// 旧版本分页模板 by YangBo
        }
        pageHtml = freemarkEngine.mergeTemplateIntoString(pageTempl, map);
        
        return pageHtml;
    }
    
    /**
     * 从配置文件中读取配置属性数据的信息
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private JdbcHelper getConfigJdbcHelper()
        throws Exception
    {
        String dbType = AppConfigUtil.get("jdbc.dbType");
        String className = AppConfigUtil.get("jdbc.driverClassName");
        String url = AppConfigUtil.get("jdbc.url");
        String user = AppConfigUtil.get("jdbc.username");
        String pwd = AppConfigUtil.get("jdbc.password");
        return JdbcHelper.getJdbcHelper(user, className, url, user, pwd, dbType);
    }
    
    /**
     * @Title: getPageBean
     * @Description: TODO(获取分页的bean)
     * @param @param param
     * @param @return
     * @return PagingBean 返回类型
     * @throws
     */
    public PagingBean getPageBean(Map<String, Object> param)
    {
        int currentPage = 1;
        Object pageObj = param.get(tableIdCode + IDataTemplate.PAGE);
        if (pageObj != null)
        {
            currentPage = Integer.parseInt(pageObj.toString());
        }
        int pageSize = 20;
        Object pageSizeObj = param.get(tableIdCode + IDataTemplate.PAGESIZE);
        if (pageSizeObj != null)
        {
            pageSize = Integer.parseInt(param.get(tableIdCode + IDataTemplate.PAGESIZE).toString());
        }
        
        Object oldPageSizeObj = param.get(tableIdCode + "oz");
        int oldPageSize = 20;
        if (oldPageSizeObj != null)
        {
            oldPageSize = Integer.parseInt(param.get(tableIdCode + "oz").toString());
        }
        if (pageSize != oldPageSize)
        {
            int first = PageUtils.getFirstNumber(currentPage, oldPageSize);
            currentPage = first / pageSize + 1;
        }
        PagingBean pageBean = new PagingBean(currentPage, pageSize);
        return pageBean;
    }
    
    /**
     * @Title: getDefaultDisplayFieldRight
     * @Description: TODO(获取默认的现实类权限json)
     * @param @return
     * @return Object 返回类型
     * @throws
     */
    public Object getDefaultDisplayFieldRight()
    {
        JSONObject json = new JSONObject();
        json.put("type", "none");
        json.put("id", "");
        json.put("name", "");
        json.put("script", "");
        return json;
    }
    
    /**
     * @Title: getPermissionMap
     * @Description: TODO(获取显示字段权限map)
     * @param @param jsonArray
     * @param @param rightMap
     * @param @return
     * @return Map<String,Boolean> 返回类型
     * @throws
     */
    public Map<String, Boolean> getPermissionMap(JSONArray jsonArray, Map<String, Object> rightMap)
    {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        if (JSONUtil.isEmpty(jsonArray))
        {
            return map;
        }
        for (Object obj : jsonArray)
        {
            JSONObject json = (JSONObject)obj;
            String settype = json.getString("settype");
            String name = json.getString("name");
            
            if (!settype_bus.equals(settype) && !this.equalsSetType(settype))
            {
                map.put(name, false);
                continue;
            }
            // 取得控制类型
            String controltype = (String)json.get("controltype");
            JSONObject rights = json.getJSONObject("right");
            // 判断控制类型'4': 人员选择器(单选)、'17': 角色选择器(单选)、'18': 组织选择器(单选)、'19': 岗位选择器(单选)
            if (controltype != null
                && ("4".equals(controltype) || "17".equals(controltype) || "18".equals(controltype) || "19".equals(controltype)))
            {
                map.put(name + "ID", dataTemplateService.hasRight(rights, rightMap));
            }
            map.put(name, dataTemplateService.hasRight(rights, rightMap));
        }
        return map;
    }
    
    /**
     * @Title: getTableFields
     * @Description: TODO(设置查询条件字段JSONArray )
     * @param @param tableId
     * @param @return
     * @return Object 返回类型
     * @throws
     */
    public Object getTableFields(Long tableId)
    {
        List<? extends IFormField> fieldList = formFieldService.getByTableId(tableId);
        JSONArray jsonAry = new JSONArray();
        for (IFormField formField : fieldList)
        {
            jsonAry.add(new SettingObj(formField));
        }
        jsonAry.add(new SettingObj(f_status, "", "varchar", "事件名称", "1", settype_all));
        jsonAry.add(new SettingObj(f_processName, "", "varchar", "流程名称", "1", settype_all));
        jsonAry.add(new SettingObj(f_creator, "", "varchar", "创建人", "1", settype_all));
        JSONObject ftm = new JSONObject();
        ftm.put("format", "yyyy-MM-dd");
        jsonAry.add(new SettingObj(f_createtime, "", "date", ftm.toString(), "创建时间", "1", settype_all));
        jsonAry.add(new SettingObj(f_status, "", "varchar", "状态", "11", settype_all));
        
        // 待办任务特有查询条件
        jsonAry.add(new SettingObj(f_hasRead, "", "varchar", "阅读状态", "11", settype_pending));
        jsonAry.add(new SettingObj(f_pendingtype, "", "varchar", "待办类型", "11", settype_pending));
        return jsonAry;
    }
    
    // 显示字段
    /**
     * @Title: getDisplayField
     * @Description: TODO(获取显示字段json string)
     * @param @param tableId
     * @param @param displayField
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String getDisplayField(Long tableId, String displayField)
    {
        JSONArray fields = null;
        if (StringUtil.isNotEmpty(displayField))
        {
            fields = JSONArray.parseArray(displayField);
        }
        List<? extends IFormField> fieldList = formFieldService.getByTableId(tableId);
        JSONArray jsonAry = new JSONArray();
        for (IFormField formField : fieldList)
        {
            jsonAry.add(new SettingObj(formField, fields));
        }
        jsonAry.add(new SettingObj(fields, f_subject, "事件名称", "varchar", "", "PK", settype_all));
        jsonAry.add(new SettingObj(fields, f_processName, "流程名称", "varchar", "", "1", settype_all));
        jsonAry.add(new SettingObj(fields, f_creator, "创建人", "varchar", "", "1", settype_all));
        jsonAry.add(new SettingObj(fields, f_createtime, "创建时间", "date", "", "1", settype_all));
        jsonAry.add(new SettingObj(fields, f_status, "状态", "varchar", "", "11", settype_all));
        // 待办任务特有字段
        jsonAry.add(new SettingObj(fields, f_pendingtype, "待办类型", "varchar", "", "11", settype_pending));
        displayField = jsonAry.toString();
        
        return displayField;
    }
    
    /**
     * @Title: getCluaseSQL
     * @Description: TODO(获取查询 and sql语句块)
     * @param @param tableName 表明
     * @param @param condition 查询条件
     * @param @param params 查询参数
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String getCluaseSQL(JSONObject condition, Map<String, Object> params)
    {
        StringBuffer sb = new StringBuffer();
        String field = condition.getString("na");
        String f_field=field;
        if(settype_bus.equals(condition.getString("settype"))){
            f_field=ITableModel.CUSTOMER_COLUMN_PREFIX + field;
        }
        String operate = condition.getString("op");
        int valueFrom = condition.getIntValue("vf");
        String joinType = "AND";
        String type = condition.getString("ty");
        joinType = " " + joinType + " ";
        String controlType = condition.getString("ct");
        
        Boolean isSelector = this.isSelector(controlType);
        Map<String, Object> dateMap = getQueryValue(params, field, type, operate);
        Object value = this.getQueryValue(condition, params, field, isSelector);
        if(BeanUtils.isEmpty(value)&&BeanUtils.isEmpty(dateMap)){
            return "";
        }
        if (controlType.equals("28"))
        {
            // 下拉多选框条件处理
            params.put(f_field, Arrays.asList(value.toString().split(",")));
            if (operate.equalsIgnoreCase("7"))
            {
                // "7","in" "8","not in"
                sb.append(joinType).append(f_field).append(" in (:"+f_field+")");
            }
            else if (operate.equalsIgnoreCase("8"))
            {
                sb.append(joinType).append(f_field).append(" not in (:"+f_field+")");
            }
        }
        else if (type.equals(IColumnModel.COLUMNTYPE_VARCHAR))
        {
            // 字符串
            if (isSelector)
            {
                f_field = f_field + ITableModel.PK_COLUMN_NAME;
                
                if (isrMultiSelector(Short.valueOf(controlType)))
                {
                    // 这个有bug 暂时这样处理吧
                    value = "%" + value.toString() + "%";
                    sb.append(joinType + f_field + " like :"+f_field);
                }
                else
                {
                    if (operate.equalsIgnoreCase("2"))
                    {// 1","=" "2","!=" "3","like""4","左like" "5","右like"
                        sb.append(joinType + f_field + "!=:" + f_field);
                    }
                    else
                    {
                        sb.append(joinType + f_field + "=:" + f_field);
                    }
                }
                params.put(f_field, String.valueOf(value));
            }
            else
            {
                if (value == null)
                {
                    return "";
                }
                value = value.toString();
                if (operate.equalsIgnoreCase("1"))
                {// 1","=" "2","!=" "3","like""4","左like" "5","右like"
                    sb.append(joinType).append(f_field).append("=").append(":").append(f_field);
                }
                else if (operate.equalsIgnoreCase("2"))
                {
                    sb.append(joinType).append(f_field).append(" != ").append(":").append(f_field);
                }
                else if (operate.equalsIgnoreCase("3"))
                {
                    value = "%" + value.toString() + "%";
                    sb.append(joinType).append(f_field).append(" LIKE :").append(f_field);
                }
                else if (operate.equalsIgnoreCase("4"))
                {
                    value = "%" + value.toString();
                    sb.append(joinType).append(f_field).append(" LIKE :").append(f_field);
                }
                else if (operate.equalsIgnoreCase("5"))
                {
                    value = value.toString() + "%";
                    sb.append(joinType).append(f_field).append(" LIKE :").append(f_field);
                }
                else if (operate.equalsIgnoreCase("6"))
                {
                    value = "'" + value.toString().replace(",", "','") + "'";
                    sb.append(joinType).append(f_field).append(" in ( ").append(value).append(")");
                }
                else
                {
                    value = "%" + value.toString() + "%";
                    sb.append(joinType).append(f_field).append(" LIKE :").append(f_field);
                }
                params.put(f_field, value);
            }
            
        }
        else if (type.equals(IColumnModel.COLUMNTYPE_DATE))
        {
            // 日期
            if ("6".equalsIgnoreCase(operate))
            {
                // 日期范围特殊处理
                if (BeanUtils.isNotEmpty(dateMap.get(IDataTemplate.DATE_BEGIN)))
                {
                    String begingField = IDataTemplate.DATE_BEGIN + f_field;
                    sb.append(joinType + f_field + ">=:" + begingField + " ");
                    params.put(begingField, dateMap.get(IDataTemplate.DATE_BEGIN));
                }
                
                if (BeanUtils.isNotEmpty(dateMap.get(IDataTemplate.DATE_END)))
                {
                    String endField = IDataTemplate.DATE_END + f_field;
                    sb.append(joinType + f_field + "<=:" + endField + " ");
                    params.put(endField, dateMap.get(IDataTemplate.DATE_END));
                }
            }
            else
            {
                String op = this.getOperate(operate);
                if (valueFrom == 1)
                {
                    if (params.containsKey(f_field))
                    {
                        sb.append(joinType + f_field + op + ":" + f_field + " ");
                    }
                }
                else
                {
                    sb.append(joinType + f_field + op + ":" + f_field + " ");
                    params.put(f_field, value);
                }
            }
        }
        else if (type.equals(IColumnModel.COLUMNTYPE_NUMBER))
        {// 数字
            if ("7".equalsIgnoreCase(operate))
            {
                // 数字范围特殊处理
                if (BeanUtils.isNotEmpty(dateMap.get(IDataTemplate.NUMBER_BEGIN)))
                {
                    String begingField = IDataTemplate.NUMBER_BEGIN + f_field;
                    sb.append(joinType + f_field + ">=:" + begingField + " ");
                    params.put(begingField, dateMap.get(IDataTemplate.NUMBER_BEGIN));
                }
                if (BeanUtils.isNotEmpty(dateMap.get(IDataTemplate.NUMBER_END)))
                {
                    String endField = IDataTemplate.NUMBER_END + f_field;
                    sb.append(joinType + f_field + "<=:" + endField + " ");
                    params.put(endField, dateMap.get(IDataTemplate.NUMBER_END));
                }
            }
            else
            {
                // 一般的>、<、>=、<=、=处理
                String op = this.getOperate(operate);
                if (valueFrom == 1)
                {
                    if (params.containsKey(f_field))
                    {
                        
                        sb.append(joinType + f_field + op + ":" + f_field + " ");
                    }
                }
                else
                {
                    sb.append(joinType + f_field + op + ":" + f_field + " ");
                    params.put(f_field, value);
                }
            }
        }
        return sb.toString();
    }
    
    /**
     * @Title: getOperate
     * @Description: TODO(将int 类的操作类型 转换成 sql 中的比较运算符)
     * @param @param operate
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String getOperate(String operate)
    {
        String op = "=";
        if ("1".equalsIgnoreCase(operate))
        {// =
            op = "=";
        }
        else if ("2".equalsIgnoreCase(operate))
        {// >
            op = ">";
        }
        else if ("3".equalsIgnoreCase(operate))
        {// <
            op = "<";
        }
        else if ("4".equalsIgnoreCase(operate))
        {// >=
            op = ">=";
        }
        else if ("5".equalsIgnoreCase(operate))
        {// <=
            op = "<=";
        }
        return op;
    }
    
    /**
     * @Title: getQueryValue
     * @Description: TODO(获取查询条件对应的参数值)
     * @param @param condition
     * @param @param params
     * @param @param field
     * @param @param isSelector
     * @param @return
     * @return Object 返回类型
     * @throws
     */
    public Object getQueryValue(JSONObject condition, Map<String, Object> params, String field, Boolean isSelector)
    {
        int valueFrom = condition.getIntValue("vf");
        Object value = null;
        switch (valueFrom)
        {
            case 1:// 输入
                   // 是日期类型，又是日期范围
                if (isSelector)
                    field = field + ITableModel.PK_COLUMN_NAME;
                if (params.containsKey(field))
                {
                    value = params.get(field);
                }
                break;
            case 2:// 固定值
                value = condition.get("va");
                break;
            case 3:// 脚本
                String script = condition.getString("va");
                if (StringUtil.isNotEmpty(script))
                {
                    value = groovyScriptEngine.executeObject(script, null);
                }
                break;
            case 4:// 自定义变量
                break;
        }
        return value;
    }
    
    /**
     * @Title: getQueryValue
     * @Description: TODO(获取范围类型的date参数map)
     * @param @param params
     * @param @param field
     * @param @param type
     * @param @param operate
     * @param @return
     * @return Map<String,Object> 返回类型
     * @throws
     */
    public Map<String, Object> getQueryValue(Map<String, Object> params, String field, String type, String operate)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        if (type.equals(IColumnModel.COLUMNTYPE_DATE) && "6".equalsIgnoreCase(operate))
        {
            String beginKey = IDataTemplate.DATE_BEGIN + field;
            Object beginVal = null;
            String endKey = IDataTemplate.DATE_END + field;
            Object endVal = null;
            if (params.containsKey(beginKey))
                beginVal = params.get(beginKey);
            if (params.containsKey(endKey))
                endVal = params.get(endKey);
            if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal))
            {
                map.put(IDataTemplate.DATE_BEGIN, beginVal);
                map.put(IDataTemplate.DATE_END, endVal);
            }
        }
        if (type.equals(IColumnModel.COLUMNTYPE_NUMBER) && "7".equalsIgnoreCase(operate))
        {
            String beginKey = IDataTemplate.DATE_BEGIN + field;
            Object beginVal = null;
            String endKey = IDataTemplate.DATE_END + field;
            Object endVal = null;
            if (params.containsKey(beginKey))
                beginVal = params.get(beginKey);
            if (params.containsKey(endKey))
                endVal = params.get(endKey);
            if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal))
            {
                map.put(IDataTemplate.DATE_BEGIN, beginVal);
                map.put(IDataTemplate.DATE_END, endVal);
            }
        }
        return map;
    }
    
    /**
     * @Title: isrMultiSelector
     * @Description: TODO(判读是否是多条件下拉框选择器)
     * @param @param controlType
     * @param @return
     * @return Boolean 返回类型
     * @throws
     */
    public Boolean isrMultiSelector(Short controlType)
    {
        if (BeanUtils.isEmpty(controlType))
            return false;
        if (controlType.shortValue() == IFieldPool.SELECTOR_USER_MULTI
            || controlType.shortValue() == IFieldPool.SELECTOR_ORG_MULTI
            || controlType.shortValue() == IFieldPool.SELECTOR_POSITION_MULTI
            || controlType.shortValue() == IFieldPool.SELECTOR_ROLE_MULTI)
            return true;
        return false;
    }
    
    /**
     * @Title: isSelector
     * @Description: TODO(判读是否是下拉框选择器)
     * @param @param controlType
     * @param @return
     * @return Boolean 返回类型
     * @throws
     */
    public Boolean isSelector(String controlType)
    {
        if (BeanUtils.isEmpty(controlType))
        {
            return false;
        }
        int ct = Integer.parseInt(controlType);
        switch (ct)
        {
            case IFieldPool.SELECTOR_USER_SINGLE:
            case IFieldPool.SELECTOR_USER_MULTI:
            case IFieldPool.SELECTOR_ORG_SINGLE:
            case IFieldPool.SELECTOR_ORG_MULTI:
            case IFieldPool.SELECTOR_POSITION_SINGLE:
            case IFieldPool.SELECTOR_POSITION_MULTI:
            case IFieldPool.SELECTOR_ROLE_SINGLE:
            case IFieldPool.SELECTOR_ROLE_MULTI:
                return true;
        }
        return false;
    }
    
    /**
     * @ClassName: SettingObj
     * @Description: TODO(JSON object 操作内部类)
     * @author zxg
     * @date 2017年6月20日 上午10:51:14
     * 
     */
    public class SettingObj extends JSONObject
    {
        public static final long serialVersionUID = -6085363045343847256L;
        
        public SettingObj(JSONArray fields, String filedName, String filedDesc, String type, String style,
            String controltype, String settype)
        {
            this.put("name", filedName);
            JSONObject set = null;
            if (fields != null)
            {
                for (Object obj : fields)
                {
                    JSONObject _obj = (JSONObject)obj;
                    if (_obj.getString("name").equals(filedName))
                    {
                        set = _obj;
                        filedDesc = set.getString("desc");
                        break;
                    }
                }
            }
            this.put("desc", filedDesc);
            this.put("type", type);
            this.put("style", style);
            this.put("settype", settype);
            this.put("controltype", controltype);
            if (BeanUtils.isNotEmpty(set))
            {
                this.put("right", set.get("right"));
            }
            else
            {
                this.put("right", getDefaultDisplayFieldRight());
            }
        }
        
        public SettingObj(IFormField formField, JSONArray fields)
        {
            this.put("name", formField.getFieldName());
            String desc = formField.getFieldDesc();
            JSONObject set = null;
            if (fields != null)
            {
                for (Object obj : fields)
                {
                    JSONObject _obj = (JSONObject)obj;
                    if (_obj.getString("name").equals(formField.getFieldName()))
                    {
                        set = _obj;
                        desc = set.getString("desc");
                        break;
                    }
                }
            }
            this.put("desc", desc);
            this.put("settype", settype_bus);
            this.put("type", formField.getFieldType());
            this.put("style", formField.getDatefmt());
            this.put("controltype", formField.getControlType());
            if (BeanUtils.isNotEmpty(set))
            {
                this.put("right", set.get("right"));
            }
            else
            {
                this.put("right", getDefaultDisplayFieldRight());
            }
        }
        
        public SettingObj(IFormField formField)
        {
            this.put("fieldName", formField.getFieldName());
            this.put("ctlProperty", formField.getCtlProperty());
            this.put("fieldType", formField.getFieldType());
            this.put("fieldDesc", formField.getFieldDesc());
            this.put("controlType", formField.getControlType());
            this.put("settype", settype_bus);
            
        }
        
        public SettingObj(String fieldName, String ctlProperty, String fieldType, String fieldDesc, String controlType,
            String settype)
        {
            this.put("fieldName", fieldName);
            this.put("ctlProperty", ctlProperty);
            this.put("fieldType", fieldType);
            this.put("fieldDesc", fieldDesc);
            this.put("controlType", controlType);
            this.put("settype", settype);
            
        }
        
        public SettingObj(String fieldName, String ctlProperty, String fieldType, String fieldfmt, String fieldDesc,
            String controlType, String settype)
        {
            this.put("fieldName", fieldName);
            this.put("ctlProperty", ctlProperty);
            this.put("fieldType", fieldType);
            this.put("fieldfmt", fieldfmt);
            this.put("fieldDesc", fieldDesc);
            this.put("controlType", controlType);
            this.put("settype", settype);
            
        }
    }
    
    /**
     * @Title: getTaskSql
     * @Description: TODO(获取流程任务表查询语句)
     * @param @param param
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public abstract String getTaskSql(Map<String, Object> param, JSONObject pendingSetting);
    
    /**
     * @Title: getBusSql
     * @Description: TODO(获取业务表查询语句)
     * @param @param param
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public abstract String getBusSql(Map<String, Object> param, JSONObject pendingSetting);
    
    /**
     * @Title: equalsSetType
     * @Description: TODO(比较type 是否加入sql)
     * @param @param settype
     * @param @return
     * @return boolean 返回类型
     * @throws
     */
    public abstract boolean equalsSetType(String settype);
    
    /** 
    * @Title: getStatus 
    * @Description: TODO(获取任务状态集合) 
    * @param @return     
    * @return Object    返回类型 
    * @throws 
    */
    public abstract Object getStatus();
    
    
    /** 
    * @Title: getStatus 
    * @Description: TODO(获取阅读状态集合) 
    * @param @return     
    * @return Object    返回类型 
    * @throws 
    */
    public abstract Object getHasRead();
    
    /** 
    * @Title: getHasRead 
    * @Description: TODO(获取待办状态集合) 
    * @param @return     
    * @return Object    返回类型 
    * @throws 
    */
    public abstract Object getPendingType();
    
    
    public abstract String getUrl();
    
    
    public abstract String getFieldSql(JSONObject pendingSetting);
    
    public abstract String getPKField();
    public abstract String getPKValue(Map<String, Object> params);

    public abstract IDefinition getDefinition(Map<String, Object> params);
    
}
