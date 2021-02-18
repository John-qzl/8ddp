package com.cssrc.ibms.core.form.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.intf.ITaskOpinionService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.ITaskOpinion;
import com.cssrc.ibms.api.custom.intf.ICustomService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IPkValue;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.api.system.intf.ISysBusEventService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.ISysBusEvent;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.db.mybatis.dialect.DialectFactoryBean;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.util.JdbcHelperUtil;
import com.cssrc.ibms.core.encrypt.FiledEncryptFactory;
import com.cssrc.ibms.core.encrypt.IFiledEncrypt;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.form.dao.FormFieldDao;
import com.cssrc.ibms.core.form.dao.FormHandlerDao;
import com.cssrc.ibms.core.form.dao.FormTableDao;
import com.cssrc.ibms.core.form.model.CustomRecord;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormModel;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.PkValue;
import com.cssrc.ibms.core.form.model.RelTable;
import com.cssrc.ibms.core.form.util.FormDataUtil;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.form.util.ParseReult;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.string.StringUtil;

import freemarker.template.TemplateException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 对象功能:自定义表单数据处理Service类 开发人员:zhulongchao
 */
@Service
public class FormHandlerService implements IFormHandlerService {
	protected Logger logger = LoggerFactory.getLogger(FormHandlerService.class);
	//
	@Resource
	private FormHandlerDao dao;
	@Resource
	private DataTemplateDao dataTemplateDao;
	@Resource
	private ISerialNumberService serialNumberService;
	@Resource
	private FormRightsService formRightsService;
	@Resource
	private FormControlService formControlService;
	@Resource
	private ITaskOpinionService taskOpinionService;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	private FormFieldDao formFieldDao;
	@Resource
	private FormTableDao formTableDao;
	@Resource
	private FormDefService formDefService;
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private IProcessRunService processRunService;
	@Resource
	private ISysDataSourceService sysDataSourceService;
	@Resource
	private IPositionService positionService;
	@Resource
	private ISysFileService sysFileService;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private FormTableService formTableService;
	@Resource
	private ISysBusEventService sysBusEventService;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;
	@Resource
    private IMessageProducer messageProducer;
	@Resource
	private IDefinitionService definitionService;
	@Resource
	private ICustomService iCustomService;

	// 流程实例分隔符
	private String INSTANCEID_SPLITOR = "#instanceId#";
	// 流程示意图替换符
	private String FLOW_CHART_SPLITOR = "(?s)<div[^>]*?\\s+name=\"editable-input\"\\s+class=\"flowchart\">(.*?)</div>";
	private String FLOW_CHART_SPLITOR_URL = "<div name=\"editable-input\" class=\"flowchart\" url=\"flowchartURL\"></div>";
	private String FLOW_CHART_SPLITOR_IE = "(?s)<div[^>]*?\\s+class=\"flowchart\"\\s+name=\"editable-input\">(.*?)</div>";
	private String FLOW_CHART_SPLITOR_IE_URL = "<div class=\"flowchart\" name=\"editable-input\" url=\"flowchartURL\"></div>";

	/**
	 * 根据表单定义，用户ID，主键获取表单的html代码。
	 * 
	 * <pre>
	 * 实现流程：
	 * 1.获取表单的模版。
	 * 2.判断主键是否为空。
	 * 		1.主键为空。
	 * 			实例化BpmFormData数据。
	 * 			1.判断字段值来源是流水号。
	 * 				如果是流水号,则生成流水号。
	 * 			2.判断字段值是来自脚本。
	 * 				通过脚本计算得出字段的值。
	 * 		2.主键不为空。
	 * 			根据主键获取表单的数据。
	 * 
	 * 3.将数据和字段权限给模版引擎解析，生成html。
	 * </pre>
	 * 
	 * @param bpmFormDef
	 *            表单定义对象。
	 * @param processRun
	 *            流程运行实例对象。
	 * @param userId
	 *            用户ID。
	 * @param pkValue
	 *            主键值。
	 * @return
	 * @throws Exception
	 */
	public String obtainHtml(FormDef bpmFormDef, IProcessRun processRun,
			Long userId, String nodeId, String ctxPath, String parentActDefId)
			throws Exception {
		String pkValue = processRun.getBusinessKey();
		String instanceId = processRun.getActInstId();
		String actDefId = processRun.getActDefId();
		return obtainHtml(bpmFormDef, userId, pkValue, instanceId, actDefId,
				nodeId, ctxPath, parentActDefId, false);

	}

	public String obtainHtml(FormDef bpmFormDef, IProcessRun processRun,
			Long userId, String nodeId, String ctxPath, String parentActDefId,
			boolean isCopyFlow) throws Exception {
		String pkValue = processRun.getBusinessKey();
		String instanceId = processRun.getActInstId();
		String actDefId = processRun.getActDefId();
		return obtainHtml(bpmFormDef, userId, pkValue, instanceId, actDefId,
				nodeId, ctxPath, parentActDefId, isCopyFlow);
	}

	/**
	 * 根据表单定义，用户ID，主键获取表单的html代码。
	 * 
	 * <pre>
	 * 实现流程：
	 * 1.获取表单的模版。
	 * 2.判断主键是否为空。
	 * 		1.主键为空。
	 * 			实例化BpmFormData数据。
	 * 			1.判断字段值来源是流水号。
	 * 				如果是流水号,则生成流水号。
	 * 			2.判断字段值是来自脚本。
	 * 				通过脚本计算得出字段的值。
	 * 			3.如果是日期控件，或者日期。
	 * 				默认显示时间的话，根据日期格式获取当前日期。
	 * 		2.主键不为空。
	 * 			根据主键获取表单的数据。
	 * 
	 * 3.将数据和字段权限给模版引擎解析，生成html。
	 * </pre>
	 * 
	 * @param bpmFormDef
	 *            自定义表单对象
	 * @param userId
	 *            用户Id
	 * @param pkValue
	 *            主键值
	 * @param instanceId
	 *            ACT流程实例ID
	 * @param actDefId
	 *            ACT流程定义ID
	 * @param nodeId
	 * @return
	 * @throws Exception
	 */
    public String getFormDetail(Long formDefId, String businessKey, Long userId, IProcessRun processRun, String ctxPath,
        boolean isCopyFlow)
        throws Exception
    {
        FormDef bpmFormDef = formDefService.getById(formDefId);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(_bpmFormDef_, bpmFormDef);
        param.put(_businessKey_, businessKey);
        param.put(_userId_, userId);
        param.put(_processRun_, processRun);
        param.put(_contextPath_, ctxPath);
        param.put(_isCopyFlow_, isCopyFlow);
        return getFormDetail(param);
    }

	public String getFormDetail(Map<String, Object> param) throws Exception {
        
	    FormDef bpmFormDef=MapUtil.get(param, _bpmFormDef_,FormDef.class);
	    String businessKey=MapUtil.get(param, _businessKey_,String.class);
	    Long userId=MapUtil.get(param, _userId_,Long.class);
	    IProcessRun processRun=MapUtil.get(param, _processRun_,IProcessRun.class);
	    String ctxPath=MapUtil.get(param,_contextPath_,String.class);
	    Boolean isCopyFlow=MapUtil.get(param, _isCopyFlow_,Boolean.class);
        Boolean highLight=MapUtil.get(param, _highLight_,Boolean.class);

		Long tableId = bpmFormDef.getTableId();
		String template = bpmFormDef.getTemplate();
		String instanceId = "";
		String actDefId = "";
		if (BeanUtils.isNotEmpty(processRun)) {
			instanceId = processRun.getActInstId();
			actDefId = processRun.getActDefId();
		}else{
		    DataTemplate dataTemplate= this.dataTemplateDao.getByFormKey(bpmFormDef.getFormKey());
		    if(dataTemplate!=null&&dataTemplate.getDefId()!=null){
		        IDefinition definition=definitionService.getById(dataTemplate.getDefId()) ;
		        if(definition!=null){
		            actDefId=definition!=null?definition.getActDefId():null;
		        }
		        
		    }

		}
		DataTemplate dataTemplate = dataTemplateDao.getByFormKey(bpmFormDef
				.getFormKey());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataTemplate", dataTemplate);
		FormData data = getBpmFormData(params, tableId, businessKey,
				instanceId, actDefId, "", isCopyFlow);
		Map<String, Map> model = new HashMap<String, Map>();
		model.put("main", data.getMainFields());
		model.put("opinion", data.getOptions());
		// sub子表数据
		model.put("sub", data.getSubTableMap());
		// 关系表数据
		model.put("relTab", data.getRelTableMap());
		//父亲表数据
	    model.putAll(this.getParentFormData(data.getMainFields(),template));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("model", model);
		// 兼容之前生成的模版。
		map.put("table", new HashMap<Object, Object>());
		// 传入控制器的service，用于在模版中解析字段。
		map.put("service", formControlService);
		map.put("permission", formRightsService.getByFormKey(param));
		map.put("instanceId", instanceId);
		// 根据权限控制字段显示情况
		//logger.info(template);
		String html = freemarkEngine.parseByStringTemplate(map, template);
		//logger.info(html);
		// 解析子表的默认内容
		html = changeSubShow(html);
		// 解析关系表的默认内容
		html = changeRelShow(html);
		// 有分页的情况。
		String tabTitle = bpmFormDef.getTabTitle();
		if (tabTitle.indexOf(FormDef.PageSplitor) > -1) {
			html = getTabHtml(tabTitle, html);
		}
		
		// 替换流程实例分隔符。
		if(instanceId == null) {
			instanceId = "";
		}
        html = html.replace(INSTANCEID_SPLITOR, instanceId);
		String priUrl = "";
        // 流程图解析
        if (StringUtil.isNotEmpty(instanceId)) {
            priUrl = ctxPath + "/oa/flow/processRun/processImageDiv.do?actInstId=" + instanceId + "&notShowTopBar=0";
        } else if (StringUtil.isNotEmpty(actDefId)) {
            priUrl = ctxPath + "/oa/flow/definition/flowImgDiv.do?actDefId=" + actDefId;
        }
        String urlHtml = FLOW_CHART_SPLITOR_URL.replaceAll("flowchartURL", priUrl);
        String ie_urlHtml = FLOW_CHART_SPLITOR_IE_URL.replaceAll("flowchartURL", priUrl);
        html = html.replaceAll(FLOW_CHART_SPLITOR, urlHtml).replaceAll(FLOW_CHART_SPLITOR_IE, ie_urlHtml);
		
        // 流程图解析
//        if (StringUtil.isNotEmpty(instanceId)) {
//            // 替换流程实例分隔符。
//            html = html.replace(INSTANCEID_SPLITOR, instanceId);
//            String repStr = "<iframe src=\""
//                    + ctxPath
//                    + "/oa/flow/processRun/processImage.do?actInstId="
//                    + instanceId
//                    + "&notShowTopBar=0\" name=\"flowchart\" id=\"flowchart\" marginwidth=\"0\" marginheight=\"0\" frameborder=\"0\"  style=\"height:97%;width:100%\"></iframe>";
//            html = html.replaceAll(FLOW_CHART_SPLITOR, repStr).replaceAll(
//                    FLOW_CHART_SPLITOR_IE, repStr);
//        } else if (StringUtil.isNotEmpty(actDefId)) {
//            String repStr = "<iframe src=\""
//                    + ctxPath
//                    + "/oa/flow/definition/flowImg.do?actDefId="
//                    + actDefId
//                    + "\"  name=\"flowchart\" id=\"flowchart\" marginwidth=\"0\" marginheight=\"0\" frameborder=\"0\"  style=\"height:97%;width:100%\"></iframe>";
//            html = html.replaceAll(FLOW_CHART_SPLITOR, repStr).replaceAll(
//                    FLOW_CHART_SPLITOR_IE, repStr);
//        }
    

		// 子表权限放到页面 （在页面解析的）并虚构子表权限 避免页面解析出错
		FormTable bpmFormTable = formTableService.getTableById(tableId);
		html += getSubPermission(bpmFormTable, true);
		//虚构关系表权限
		html += getRelPermission(bpmFormTable, true);
		//logger.info(html);
		return html;
	}

    /** 
    * @Title: getParentFormData 
    * @Description: TODO(获取父亲表数据) 
    * @param @param mdata
    * @param @param template
    * @param @return     
    * @return Map<? extends String,? extends Map>    返回类型 
    * @throws 
    */
    private Map<String, Map<String,Object>> getParentFormData(Map<String, Object> mdata, String template)
    {
        Document doc= Jsoup.parseBodyFragment(template);
        Elements list = doc.select("div[type=parenttable]");
        Map<String, Map<String,Object>> result = new HashMap<String, Map<String,Object>>();

        Map<String, Object> parentTableData = new HashMap<String, Object>();

        for (Iterator<Element> it = list.iterator(); it.hasNext();)
        {
            Element parentTable = it.next();
            String tableName = parentTable.attr("tableName").toLowerCase();
            String tableId = parentTable.attr("tableId");
            String relField = parentTable.attr("p:relField").toLowerCase();
            if(StringUtil.isEmpty(relField)){
                continue;
            }
            if(!mdata.containsKey(relField)){
                continue;
            }
            Object pkVal=mdata.get(relField);
            FormTable parentTab=formTableService.getByTableId(Long.valueOf(tableId),1);
            JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getFormTableJdbcTemplate(parentTab);
            PkValue pk = new PkValue(parentTab.getPkField(), pkVal);
            Map<String, Object> parentData = dao.getByKey(jdbcTemplate, pk, parentTab, true);
            parentTableData.put(tableName, parentData);
            
        }
        result.put("parentTableData", parentTableData);
        return result;
    }

    /**
	 * 根据表单获取生成html。
	 * 
	 * @param bpmFormDef
	 *            表单定义
	 * @param userId
	 *            用户ID
	 * @param pkValue
	 *            主键
	 * @param instanceId
	 *            流程实例ID
	 * @param actDefId
	 *            流程定义ID
	 * @param nodeId
	 *            节点ID
	 * @param ctxPath
	 *            上下文
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String obtainHtml(FormDef bpmFormDef, Long userId, String pkValue,
			String instanceId, String actDefId, String nodeId, String ctxPath,
			String parentActDefId, boolean isCopyFlow) throws Exception {
		Long tableId = bpmFormDef.getTableId();
		String template = bpmFormDef.getTemplate();
		// String template =
		// "<input id='tableId' name='tableId' type='hidden' value='" + tableId
		// +"'/>" + tempStr ;
		Long formKey = bpmFormDef.getFormKey();
		// 实例化BpmFormData数据
		DataTemplate dataTemplate = dataTemplateDao.getByFormKey(bpmFormDef
				.getFormKey());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataTemplate", dataTemplate);
		FormData data = getBpmFormData(params, tableId, pkValue, instanceId,
				actDefId, nodeId, isCopyFlow);
		if (("#dataTem".equals(actDefId)) || ("#formPrev".equals(actDefId))) {
			actDefId = "";
		}
		/* 获取本任务保存但未提交意见 */

		Map<String, Map> model = new HashMap<String, Map>();
		model.put("main", data.getMainFields());
		model.put("opinion", data.getOptions());
		// sub子表数据
		model.put("sub", data.getSubTableMap());
		// 关系表数据
		model.put("relTab", data.getRelTableMap());
	      //父亲表数据
        model.putAll(this.getParentFormData(data.getMainFields(),template));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("model", model);
		// 传入控制器的service，用于在模版中解析字段。
		map.put("service", formControlService);
		map.put("nodeId", nodeId);
		map.put("instanceId", instanceId);
		
		// 传入字段的权限。
		if (userId > 0) {
			if (StringUtil.isEmpty(parentActDefId)) {
				map.put("permission", this.formRightsService
						.getByFormKeyAndUserId(formKey, userId, actDefId,
								nodeId));
			} else {
				map.put("permission", this.formRightsService
						.getByFormKeyAndUserId(formKey, userId, actDefId,
								nodeId, parentActDefId));
			}
		} else {
			// map.put("permission",
			// this.formRightsService.getByFormKeyAndUserId(formKey, userId,
			// actDefId, nodeId));
			map.put("permission", formRightsService.getByFormKey(bpmFormDef, userId));
		}
		// 兼容之前生成的模版。
		map.put("table", new HashMap<Object, Object>());
		//logger.info(template);
		String output = freemarkEngine.parseByStringTemplate(map, template);
		//logger.info(output);
		//解析子表的默认内容
		output = changeSubShow(output);
		// 解析关系表的默认内容
		output = changeRelShow(output);

		String tabTitle = bpmFormDef.getTabTitle();
		// 有分页的情况。
		if (tabTitle.indexOf(FormDef.PageSplitor) > -1) {
			output = getTabHtml(tabTitle, output);
		}
		// 替换流程实例分隔符。
		output = output.replace(INSTANCEID_SPLITOR, instanceId);
		// 流程图解析
//		if (StringUtil.isNotEmpty(instanceId)) {
//			String repStr = "<iframe src=\""
//					+ ctxPath
//					+ "/oa/flow/processRun/processImage.do?actInstId="
//					+ instanceId
//					+ "&notShowTopBar=0\" name=\"flowchart\" id=\"flowchart\" marginwidth=\"0\" marginheight=\"0\" frameborder=\"0\" scrolling=\"no\" style=\"height:97%;width:100%\"></iframe>";
//			output = output.replaceAll(FLOW_CHART_SPLITOR, repStr).replaceAll(
//					FLOW_CHART_SPLITOR_IE, repStr);
//		} else if (StringUtil.isNotEmpty(actDefId)) {
//			String repStr = "<iframe src=\""
//					+ ctxPath
//					+ "/oa/flow/definition/flowImg.do?actDefId="
//					+ actDefId
//					+ "\"  name=\"flowchart\" id=\"flowchart\" marginwidth=\"0\" marginheight=\"0\" frameborder=\"0\" scrolling=\"no\" style=\"height:97%;width:100%\" ></iframe>";
//			output = output.replaceAll(FLOW_CHART_SPLITOR, repStr).replaceAll(
//					FLOW_CHART_SPLITOR_IE, repStr);
//		}
		// 流程图解析
		String priUrl = "";
		if (StringUtil.isNotEmpty(instanceId)) {
            priUrl = ctxPath + "/oa/flow/processRun/processImageDiv.do?actInstId=" + instanceId + "&notShowTopBar=0";
		} else if (StringUtil.isNotEmpty(actDefId)) {
            priUrl = ctxPath + "/oa/flow/definition/flowImgDiv.do?actDefId=" + actDefId;
		}
        String urlHtml = FLOW_CHART_SPLITOR_URL.replaceAll("flowchartURL", priUrl);
        String ie_urlHtml = FLOW_CHART_SPLITOR_IE_URL.replaceAll("flowchartURL", priUrl);
		output = output.replaceAll(FLOW_CHART_SPLITOR, urlHtml).replaceAll(FLOW_CHART_SPLITOR_IE, ie_urlHtml);


		// 子表权限放到页面 （在页面解析的）
		// 子表字段相关权限
		Map<String, List<JSONObject>> subFileJsonMap = formRightsService
				.getSubTablePermission(formKey, userId, actDefId, nodeId);
		String subFilePermissionMap = "{}";
		if (BeanUtils.isNotEmpty(subFileJsonMap)) {
			subFilePermissionMap = JSONObject.fromObject(subFileJsonMap)
					.toString();
		}
		output += "<script type=\"text/javascript\" > var subFilePermissionMap = "
				+ subFilePermissionMap + " </script>";
		// 关系表权限放到页面 （在页面解析的）
		// 关系表字段相关权限
		Map<String, List<JSONObject>> relFileJsonMap = formRightsService
				.getRelTablePermission(formKey, userId, actDefId, nodeId);
		String relFilePermissionMap = "{}";
		if (BeanUtils.isNotEmpty(relFileJsonMap)) {
			relFilePermissionMap = JSONObject.fromObject(relFileJsonMap)
					.toString();
		}
		output += "<script type=\"text/javascript\" > var relFilePermissionMap = "
				+ relFilePermissionMap + " </script>";
		//logger.info(output);
		//是否继续处理，针对一些空的table处理 去掉空的td tr 空的div等
		//output=trimHtmlTag(output);

		return output;
	}

	public String trimHtmlTag(String html)
    {
        Document doc= Jsoup.parseBodyFragment(html);
        return doc.body().html();
    }

    /**
	 * 获取表字段的国际化资源
	 * 
	 * @param tableId
	 * @return
	 */
	public Map<String, String> getI18nMap(Long tableId) {
		Map<String, String> i18nMap = new HashMap<String, String>();
		FormTable mainFormTableDef = formTableDao.getById(tableId);
		String prefix = "m_" + mainFormTableDef.getTableName() + "_";
		handlerFormLanguage(i18nMap, tableId, prefix);
		List<FormTable> formTableList = formTableDao
				.getSubTableByMainTableId(tableId);

		for (FormTable bpmFormTable : formTableList) {
			prefix = "s_" + bpmFormTable.getTableName() + "_";
			Long subTableId = bpmFormTable.getTableId();
			handlerFormLanguage(i18nMap, subTableId, prefix);
		}
		return i18nMap;
	}

	private void handlerFormLanguage(Map<String, String> i18nMap, Long tableId,
			String prefix) {
		/*
		 * String lanType = UserContextUtil.getLocale().toString();
		 * List<BpmFormLanguage> mainFields =
		 * bpmFormLanguageDao.getByFormIdAndLanType(tableId.toString(), lanType,
		 * BpmFormLanguage.BPM_FORM_TYPE); for(BpmFormLanguage bpmFormLanguage :
		 * mainFields){ String key = prefix + bpmFormLanguage.getReskey();
		 * String value = bpmFormLanguage.getResvalue(); i18nMap.put(key,
		 * value); }
		 */
	}

	/**
	 * 根据表ID，主键值，流程实例Id 获取bpmFormData 实例
	 * 
	 * @param tableId
	 * @param pkValue
	 * @param instanceId
	 * @return
	 * @throws Exception
	 */
	public FormData getBpmFormData(Map<String, Object> parmas, Long tableId,
			String pkValue, String instanceId, String actDefId, String nodeId,
			boolean isCopyFlow) throws Exception {
		FormData data = null;
		if (StringUtil.isNotEmpty(pkValue)) {
			// 根据主键和表取出数据填充BpmFormData。
			if (("#dataTem".equals(actDefId)) || ("#formPrev".equals(actDefId))) {
				actDefId = "";
			}
			data = dao.getByKey(parmas, tableId, pkValue, actDefId, nodeId,true);
			// 获取表单的意见。
			if (StringUtil.isNotEmpty(instanceId)) {

				Map<String, String> formOptions = getFormOptionsByInstance(
						instanceId, nodeId);
				if (BeanUtils.isNotEmpty(formOptions)) {
					data.setOptions(formOptions);
				}
			}
			if (isCopyFlow) {
				List<FormField> list = formFieldDao.getByTableIdContainHidden(tableId);
				Map<String, Object> resultMap = getCalculateDataMap(list, data, actDefId);
				data.setMainFields(resultMap);
			}
		} else {
			data = new FormData();
			// 获取流水号和脚本计算结果
			List<FormField> list = formFieldDao
					.getByTableIdContainHidden(tableId);
			Map<String, Object> resultMap = getCalculateDataMap(list, data,
					actDefId);
			data.setMainFields(resultMap);

		}

		return data;
	}

	private Map<String, Object> getCalculateDataMap(List<FormField> list,
			FormData data, String actDefId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentOrg();
		IPosition pos = (IPosition) UserContextUtil.getCurrentPos();
		for (FormField field : list) {
			String fieldName = field.getFieldName().toLowerCase();
			// 值来源为流水号。
			if (field.getValueFrom() == FormField.VALUE_FROM_IDENTITY) {
				if ("#dataTem".equals(actDefId)) {
					String id = serialNumberService.nextId(field
							.getSerialNumber());
					resultMap.put(fieldName, id);
				} else if (!"#formPrev".equals(actDefId)) {
					String prop = field.getCtlProperty();
					if (StringUtil.isNotEmpty(prop)) {
						JSONObject jsonObject = JSONObject.fromObject(prop);
						if (jsonObject.containsKey("isShowidentity")) {
							String isShowidentity = jsonObject
									.getString("isShowidentity");
							if ("1".equals(isShowidentity)) {
								String id = serialNumberService.nextId(field
										.getSerialNumber());
								resultMap.put(fieldName, id);
							}
						}
					}

				}
			}
			// 值来源为脚本。
			else if (field.getValueFrom() == FormField.VALUE_FROM_SCRIPT_SHOW) {
				Object result = FormUtil
						.calcuteField(field.getScript(), data.getMainFields(),
								TableModel.CUSTOMER_COLUMN_PREFIX);
				resultMap.put(fieldName, result);
			}
			// 计算默认日期数据
			else if (field.getControlType() == 15
					|| "date".equals(field.getFieldType())) {
				String prop = field.getCtlProperty();
				// {"format":"yyyy-MM-dd","displayDate":1,"condition":"like"}
				if (StringUtil.isNotEmpty(prop)) {
					try {
						JSONObject jsonObject = JSONObject.fromObject(prop);
						if (jsonObject.containsKey("displayDate")) {
							String format = jsonObject.getString("format");
							String displayDate = jsonObject
									.getString("displayDate");
							if (displayDate.equals("1")) {
								resultMap.put(fieldName,
										TimeUtil.getDateString(format));
							}
						}
					} catch (Exception ex) {
						logger.debug(ex.getMessage());
					}
				}
			}
			// 用户选择器默认当前用户
			else if (field.getControlType().shortValue() == IFieldPool.SELECTOR_USER_SINGLE) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					JSONObject jsonObject = JSONObject.fromObject(prop);
					if (jsonObject.containsKey("showCurUser")) {
						String showCurUser = JSONObject.fromObject(prop)
								.getString("showCurUser");
						if (showCurUser.equals("1")) {
							if (field.getIsHidden() == 1) {
								resultMap.put(fieldName, user.getUserId());
							} else {
								resultMap.put(fieldName, user.getFullname());
							}
						}
					}

				}
			} else if (IFieldPool.SELECTOR_ORG_SINGLE == field.getControlType()
					.shortValue() && BeanUtils.isNotEmpty(org)) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					JSONObject jsonObject = JSONObject.fromObject(prop);
					if (jsonObject.containsKey("showCurOrg")) {
						String showCurUser = JSONObject.fromObject(prop)
								.getString("showCurOrg");
						if (showCurUser.equals("1")) {
							if (field.getIsHidden() == 1) {
								resultMap.put(fieldName, org.getOrgId());
							} else {
								resultMap.put(fieldName, org.getOrgName());
							}
						}
					}

				}
			} else if (IFieldPool.SELECTOR_POSITION_SINGLE == field
					.getControlType() && BeanUtils.isNotEmpty(pos)) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					JSONObject jsonObject = JSONObject.fromObject(prop);
					if (jsonObject.containsKey("showCurPos")) {
						String showCurUser = JSONObject.fromObject(prop)
								.getString("showCurPos");
						if (showCurUser.equals("1")) {
							if (field.getIsHidden() == 1) {
								resultMap.put(fieldName, pos.getPosId());
							} else {
								resultMap.put(fieldName, pos.getPosName());
							}
						}
					}

				}
			}

		}
		return resultMap;
		// 将流水号和脚本计算结果加入data
		// data.setMainFields(resultMap);
	}

	/**
	 * 对设计的表单进行解析。
	 * 
	 * <pre>
	 * 生成实际的表单html。
	 * </pre>
	 * 
	 * @param title
	 * @param parseResult
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String obtainHtml(String title, ParseReult parseResult,
			Map<String, Map<String, String>> permission, Boolean isPreview)
			throws Exception {
		String template = parseResult.getTemplate();

		FormTable bpmFormTable = parseResult.getFormTable();

		if (BeanUtils.isEmpty(permission)) {
			permission = new HashMap<String, Map<String, String>>();

			Map<String, String> fieldPermission = new HashMap<String, String>();
			Map<String, String> tablePermission = new HashMap<String, String>();
			Map<String, String> opinionPermission = new HashMap<String, String>();

			permission.put("field", fieldPermission);
			permission.put("table", tablePermission);
			permission.put("opinion", opinionPermission);
		}

		FormData data = new FormData();
		// 获取流水号和脚本计算结果
		Map<String, Object> resultMap = new HashMap<String, Object>();

		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentOrg();
		IPosition pos = (IPosition) UserContextUtil.getCurrentPos();
		List<FormField> list = bpmFormTable.getFieldList();
		for (FormField field : list) {
			String fieldName = field.getFieldName().toLowerCase();
			// 值来源为流水号。
			if (field.getValueFrom() == FormField.VALUE_FROM_IDENTITY) {
				// 真正启动流程时，流水号才+1，其他情况，只取得当前的流水号
				if (!isPreview.booleanValue()) {
					String id = serialNumberService.getCurIdByAlias(field
							.getSerialNumber());
					resultMap.put(fieldName, id);
				}
			}
			// 值来源为脚本。
			else if (field.getValueFrom() == FormField.VALUE_FROM_SCRIPT_SHOW) {
				Object result = FormUtil
						.calcuteField(field.getScript(), data.getMainFields(),
								TableModel.CUSTOMER_COLUMN_PREFIX);
				resultMap.put(fieldName, result);
			}
			// 计算默认日期数据
			else if (field.getControlType() == 15
					|| field.getFieldType().equals("date")) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					try {
						JSONObject jsonObject = JSONObject.fromObject(prop);
						String format = jsonObject.getString("format");
						String displayDate = jsonObject
								.getString("displayDate");
						if (displayDate.equals("1")) {
							resultMap.put(fieldName,
									TimeUtil.getDateString(format));
						}
					} catch (Exception ex) {
					}
				}
			}

			// 用户选择器默认当前用户
			else if (field.getControlType() == IFieldPool.SELECTOR_USER_SINGLE) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					JSONObject jsonObj = JSONObject.fromObject(prop);
					if (jsonObj.containsKey("showCurUser")) {
						String showCurUser = jsonObj.getString("showCurUser");
						if (showCurUser.equals("1")) {
							resultMap.put(fieldName + "id", user.getUserId());
							resultMap.put(fieldName, user.getFullname());
						}
					}
				}
			}

			// 组织选择器默认当前组织
			else if (field.getControlType() == IFieldPool.SELECTOR_ORG_SINGLE
					&& BeanUtils.isNotEmpty(org)) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					JSONObject jsonObj = JSONObject.fromObject(prop);
					if (jsonObj.containsKey("showCurOrg")) {
						String showCurOrg = jsonObj.getString("showCurOrg");
						if (showCurOrg.equals("1")) {
							resultMap.put(fieldName + "id", org.getOrgId());
							resultMap.put(fieldName, org.getOrgName());
						}
					}
				}
			}
			// 岗位选择器默认当前岗位
			else if (IFieldPool.SELECTOR_POSITION_SINGLE == field
					.getControlType() && BeanUtils.isNotEmpty(pos)) {
				String prop = field.getCtlProperty();
				if (StringUtil.isNotEmpty(prop)) {
					JSONObject jsonObject = JSONObject.fromObject(prop);
					if (jsonObject.containsKey("showCurPos")) {
						String showCurUser = JSONObject.fromObject(prop)
								.getString("showCurPos");
						if (showCurUser.equals("1")) {
							resultMap.put(fieldName + "id", pos.getPosId());
							resultMap.put(fieldName, pos.getPosName());
						}
					}
				}
			}
		}
		// 将流水号和脚本计算结果加入data
		data.setMainFields(resultMap);

		Map<String, Map> model = new HashMap<String, Map>();
		model.put("main", data.getMainFields());
		model.put("opinion", data.getOptions());
		// sub子表数据
		model.put("sub", data.getSubTableMap());
		// 关系表数据
		model.put("relTab", data.getRelTableMap());
        //父亲表数据
        model.putAll(this.getParentFormData(data.getMainFields(),template));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("model", model);
		// 传入控制器的service，用于在模版中解析字段。
		map.put("service", formControlService);
		// 传入字段的权限。
		map.put("permission", permission);
		// 兼容之前生成的模版。
		map.put("table", new HashMap());
		// 表单国际化
		Long tableId = bpmFormTable.getTableId();
		if (tableId > 0) {
			map.put("optionmap", getOptionI18nMap(tableId));
			map.put("i18nmap", getI18nMap(tableId));
		} else {
			map.put("optionmap", new HashMap<String, FormField>());
			map.put("i18nmap", new HashMap<String, String>());
		}
		//System.out.print(template);
		String output = freemarkEngine.parseByStringTemplate(map, template);

		// 解析子表的默认内容
		output = changeSubShow(output);
		// 解析关系表的默认内容
		output = changeRelShow(output);
		// 虚构子表权限
		output += getSubPermission(bpmFormTable, false);
		// 虚构关系表权限
		output += getRelPermission(bpmFormTable, false);
		// 有分页的情况。
		if (title.indexOf(FormDef.PageSplitor) > -1) {
			output = getTabHtml(title, output);
		}
		return output;
	}

	/**
	 * 根据流程实例取得流程的意见。
	 * 
	 * @param instanceId
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public Map<String, String> getFormOptionsByInstance(String instanceId,
			String nodeId) throws IOException, TemplateException {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, List<ITaskOpinion>> taskMap = new HashMap<String, List<ITaskOpinion>>();
		// 未提交的意见
		List<? extends ITaskOpinion> formlist = taskOpinionService
				.getFormOpinionByActInstId(instanceId);
		for (ITaskOpinion option : formlist) {
			if (StringUtil.isNotEmpty(option.getFieldName())) {
				map.put(option.getFieldName().toLowerCase(),
						option.getOpinion());
			}
		}
		// 已经提交的意见
		List<? extends ITaskOpinion> list = taskOpinionService.getByActInstId(instanceId);
		for (ITaskOpinion option : list) {
			if (StringUtil.isNotEmpty(option.getFieldName())) {
				String fieldName = option.getFieldName().toLowerCase();
				if (taskMap.containsKey(fieldName)) {
					List<ITaskOpinion> opinionList = taskMap.get(fieldName);
					opinionList.add(option);
				} else {
					List<ITaskOpinion> opinionList = new ArrayList<ITaskOpinion>();
					opinionList.add(option);
					taskMap.put(fieldName, opinionList);
				}
			}
		}
		Set<Map.Entry<String, List<ITaskOpinion>>> set = taskMap.entrySet();
		for (Iterator<Map.Entry<String, List<ITaskOpinion>>> it = set
				.iterator(); it.hasNext();) {
			Map.Entry<String, List<ITaskOpinion>> entry = (Map.Entry<String, List<ITaskOpinion>>) it
					.next();
			List<ITaskOpinion> optionList = entry.getValue();
			String options = "";
			for (ITaskOpinion opinion : optionList) {
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("opinion", opinion);
				if (opinion.getTaskKey().equalsIgnoreCase(nodeId)) {
					options += opinion.getOpinion();
				} else {
					options += freemarkEngine.mergeTemplateIntoString(
							"opinion.ftl", model);
				}

			}
			map.put(entry.getKey(), options);
		}

		return map;
	}

	/**
	 * 获取tab的html。
	 * 
	 * @param tabTitle
	 * @param html
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	private String getTabHtml(String tabTitle, String html)
			throws TemplateException, IOException {
		String[] aryTitle = tabTitle.split(FormDef.PageSplitor);
		String[] aryHtml = html.split(FormDef.PageSplitor);

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < aryTitle.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", aryTitle[i]);
			map.put("html", aryHtml[i]);
			list.add(map);
		}
		String formPath = FormTemplateService.getFormTemplatePath() + "tab.ftl";
		String tabTemplate = FileOperator.readFile(formPath);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tabList", list);
		String output = freemarkEngine.parseByStringTemplate(map, tabTemplate);
		//提出html里的script脚本
		Pattern pattern = Pattern.compile("<script([\\s\\S]*?)script>");
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			String str = matcher.group();
			output = output.replace(str, "");
			output = str + "\n" + output;
		}
		return output;
	}

	/**
	 * 保存表单数据.
	 * <p>
	 * detailed comment
	 * </p>
	 * 
	 * @param tableId
	 * @param need
	 * @param pkData
	 * @param formData
	 * @param displayId
	 * @throws Exception
	 * @see
	 */
	public String saveFormData(FormTable bpmFormTable, Long tableId,
			int isNeed, String pkData, String formData, String formKey,Map<String,Object> params)
			throws Exception {
		if (bpmFormTable == null) {
			bpmFormTable = formTableService.getByTableId(tableId, isNeed);// 1为正常字段加上隐藏字段
		}
		if (StringUtil.isEmpty(pkData)) {
			try {
				FormData bpmFormData = FormDataUtil.parseJson(formData, bpmFormTable);
				this.handFormData(bpmFormData, formKey, formData,params);
				pkData = String.valueOf(bpmFormData.getPkValue().getValue());
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			PkValue pkValue = new PkValue(bpmFormTable.getPkField(), pkData);
			pkValue.setIsAdd(false);
			FormData bpmFormData = FormDataUtil.parseJson(formData, pkValue,
					bpmFormTable);
			this.handFormData(bpmFormData, formKey, formData,params);
		}
		return pkData;
	}
	
    /**
     * rpc 远程 保存表单数据
     * @param bpmFormTable
     * @param tableId
     * @param isNeed
     * @param pkData
     * @param formData
     * @param formKey
     * @throws Exception 
     */
	public void saveFormData(IFormTable formTable, Long tableId,
           int isNeed, String pkData, String formData, String formKey)
           throws Exception {
	    this.saveFormData((FormTable)formTable, tableId, isNeed, pkData, formData, formKey);
	}

	/**
	 * 处理动态表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	public void handFormData(FormData bpmFormData) throws Exception {
		dao.handFormData(null, bpmFormData);
	}

	/**
	 * 处理动态表单数0据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	public void handFormData(FormData bpmFormData, String formKey,
			String jsonData,Map<String,Object> params) throws Exception {
		ISysBusEvent busEvent = this.sysBusEventService.getByFormKey(formKey);
		handEvent(busEvent, true, bpmFormData, jsonData);
        DataTemplate dataTemplate = dataTemplateDao.getByFormKey(Long.valueOf(formKey));
        if(params==null){
            params=new HashMap<String,Object>();
        }
		params.put("dataTemplate", dataTemplate);
		dao.handFormData(params, bpmFormData);
		handEvent(busEvent, false, bpmFormData, jsonData);
	}

	private void handEvent(ISysBusEvent busEvent, boolean isBefore,
			FormData bpmFormData, String jsonData) {
		if (busEvent == null)
			return;
		String script = "";
		if (isBefore)
			script = busEvent.getPreScript();
		else {
			script = busEvent.getAfterScript();
		}
		if (StringUtil.isEmpty(script))
			return;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("data", bpmFormData);
		params.put("jsonData", jsonData);
		this.groovyScriptEngine.execute(script, params);
	}

	/**
	 * 处理动态表单数据
	 * 
	 * @param bpmFormData
	 * @param processRun
	 * @throws Exception
	 */
	public void handFormData(FormData bpmFormData, IProcessRun processRun)
			throws Exception {
		dao.handFormData(null, bpmFormData, processRun);
	}

	/**
	 * 处理动态表单数据
	 * 
	 * @param bpmFormData
	 * @param processRun
	 * @param nodeId
	 * @throws Exception
	 */
	public void handFormData(FormData bpmFormData, IProcessRun processRun,
			String nodeId) throws Exception {
		dao.handFormData(null, bpmFormData, processRun, nodeId);
	}

	/**
	 * 根据主键查询列表数据。
	 * 
	 * @param tableId
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	public FormData getByKey(Map<String, Object> params, long tableId,
			String pkValue) throws Exception {
		return dao.getByKey(params, tableId, pkValue, true);
	}

	/**
	 * 
	 * @param dsAlias
	 * @return
	 * @throws Exception
	 */
	public JdbcDao getNewJdbcDao(String dsAlias) throws Exception {
		ISysDataSource sysDataSource = sysDataSourceService.getByAlias(dsAlias);
		JdbcDao jdao = new JdbcDao();
		jdao.setDialect(DialectFactoryBean.getDialect(sysDataSource.getDbType()));
		JdbcTemplate jdbcTemplate = new JdbcTemplate(
				sysDataSourceService
						.getDriverMangerDataSourceByAlias(sysDataSource
								.getAlias()));
		jdao.setJdbcTemplate(jdbcTemplate);
		return jdao;
	}

	/**
	 * 删除业务数据。
	 * 
	 * @param id
	 * @param tableName
	 * @throws Exception
	 */
	public void delById(String pkValue, FormTable bpmFormTable)
			throws Exception {
	    Map<String,Object> param=new HashMap<>();
		String sql = "delete from w_" + bpmFormTable.getTableName()+ " where id=:pk";
		param.put("pk", pkValue);
		if (bpmFormTable.isExtTable()) {
			sql = "delete from " + bpmFormTable.getTableName() + " where "
					+ bpmFormTable.getPkField() + "=:pk";
			this.getNewJdbcDao(bpmFormTable.getDsAlias()).exesql(sql,param);
		} else {
			jdbcDao.exesql(sql,param);
		}
	}

	public boolean isExistsData(String tableName, String pkValue) {
		if (tableName.indexOf(TableModel.CUSTOMER_TABLE_PREFIX.toLowerCase()) < 0) {
			tableName = TableModel.CUSTOMER_TABLE_PREFIX + tableName;
		}
		Map<String, Object> data = dao.getByKey(tableName, pkValue);
		if (BeanUtils.isEmpty(data)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除业务数据。
	 * 
	 * @param id
	 * @param tableName
	 */
	public void delByIdTableName(String id, String tableName) {
	    Map<String, Object> param=new HashMap<>();
		String sql = "delete from " + tableName + " where id=:pk";
		param.put("pk", id);
		jdbcDao.exesql(sql, param);
	}

	/**
	 * 根据表名和主键是否有数据。
	 * 
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public boolean isHasDataByPk(String tableName, Long pk) {
		return dao.isHasDataByPk(tableName, pk);
	}

	public void delByDsAliasAndTableName(String dsAlias, String tableName,
			String pk) throws Exception {
		FormTable bpmFormTable = formTableService.getByAliasTableName(dsAlias,
				tableName);
		JdbcHelper<?, ?> jdbcHelper = JdbcHelperUtil.getJdbcHelper(dsAlias);
		String sql = "delete from " + tableName + " where "
				+ bpmFormTable.getPkField() + "=" + pk;
		jdbcHelper.getJdbcTemplate().execute(sql);
	}

	/**
	 * 计算默认值 流水号,脚本计算结果,当前日期，当前人,当前组织
	 * 
	 * @param list
	 *            当前表单字段List
	 * @param data
	 *            表单数据
	 * @return
	 */
	public Map<String, Object> getDataMap(List<FormField> list, FormData data) {
		// 获取流水号,脚本计算结果,当前日期
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentOrg();
		IPosition position = (IPosition) UserContextUtil.getCurrentPos();

		for (FormField field : list) {
			String fieldName = field.getFieldName().toLowerCase();
			String prop = field.getCtlProperty();
			// 值来源为流水号。
			if (field.getValueFrom() == FormField.VALUE_FROM_IDENTITY) {
				String id = serialNumberService.nextId(field.getSerialNumber());
				resultMap.put(fieldName, id);
			}
			// 值来源为脚本。
			else if (field.getValueFrom() == FormField.VALUE_FROM_SCRIPT_SHOW) {
				Object result = FormUtil
						.calcuteField(field.getScript(), data.getMainFields(),
								TableModel.CUSTOMER_COLUMN_PREFIX);
				resultMap.put(fieldName, result);
			}
			// 计算默认日期数据
			else if (field.getControlType() == IFieldPool.DATEPICKER
					|| field.getFieldType().equals("date")) {
				// {"format":"yyyy-MM-dd","displayDate":1,"condition":"like"}
				if (StringUtil.isEmpty(prop))
					continue;
				try {
					JSONObject jsonObject = JSONObject.fromObject(prop);
					String format = jsonObject.getString("format");
					String displayDate = jsonObject.getString("displayDate");
					if (displayDate.equals("1")) {
						resultMap
								.put(fieldName, TimeUtil.getDateString(format));
					}
				} catch (Exception ex) {
				}
			}

			// 用户选择器默认当前用户
			else if (field.getControlType() == IFieldPool.SELECTOR_USER_SINGLE) {
				if (StringUtil.isEmpty(prop))
					continue;
				try {
					JSONObject jsonObj = JSONObject.fromObject(prop);
					if (!jsonObj.containsKey("showCurUser"))
						continue;
					String showCurUser = jsonObj.getString("showCurUser");
					// if (!showCurUser.equals("1")) continue;

					if (showCurUser.equals("1")) {
						if (field.getIsHidden() == 1) {
							resultMap.put(fieldName, user.getUserId());
						} else {
							resultMap.put(fieldName, user.getFullname());
						}
					} else if (showCurUser.equals("2")) {
						ISysOrg sysOrg = sysOrgService
								.getPrimaryOrgByUserId(user.getUserId());
						if (sysOrg == null) {
							continue;
						}
						Long orgId = sysOrg.getOrgId();

						List<ISysUser> leaders = null;// sysUserDao.getSuperiorByUserId(user.getUserId(),
														// orgId);
						if (BeanUtils.isNotEmpty(leaders)) {
							if (field.getIsHidden() == 1) {
								resultMap.put(fieldName, leaders.get(0)
										.getUserId());
							} else {
								resultMap.put(fieldName, leaders.get(0)
										.getFullname());
							}
						}
					}
				} catch (Exception ex) {
					String msg = ExceptionUtil.getExceptionMessage(ex);
					logger.debug(msg);
				}
			}

			// 组织选择器默认当前组织
			else if (field.getControlType() == IFieldPool.SELECTOR_ORG_SINGLE) {
				if (StringUtil.isEmpty(prop))
					continue;
				try {
					JSONObject jsonObj = JSONObject.fromObject(prop);
					if (!jsonObj.containsKey("showCurOrg"))
						continue;
					String showCurOrg = jsonObj.getString("showCurOrg");
					if (!showCurOrg.equals("1"))
						continue;
					if (field.getIsHidden() == 1) {
						resultMap.put(fieldName, org.getOrgId());
					} else {
						resultMap.put(fieldName, org.getOrgName());
					}
				} catch (Exception ex) {
				}
			}
			// 组织单选。
			else if (field.getControlType() == IFieldPool.SELECTOR_POSITION_SINGLE) {
				if (StringUtil.isEmpty(prop))
					continue;
				try {
					JSONObject jsonObj = JSONObject.fromObject(prop);
					if (!jsonObj.containsKey("showCurPos"))
						continue;
					String showCurPos = jsonObj.getString("showCurPos");
					if (!showCurPos.equals("1"))
						continue;
					if (field.getIsHidden() == 1) {
						resultMap.put(fieldName, position.getPosId());
					} else {
						resultMap.put(fieldName, position.getPosName());
					}
				} catch (Exception ex) {
				}
			}

		}
		return resultMap;
	}

	/**
	 * 获取选项的国际化资源
	 * 
	 * @return
	 */
	public Map<String, FormField> getOptionI18nMap(Long tableId) {
		Map<String, FormField> map = new HashMap<String, FormField>();
		FormTable mainFormTableDef = formTableDao.getById(tableId);

		List<FormField> list = formFieldDao.getByTableIdContainHidden(tableId);

		String prefix = "m_" + mainFormTableDef.getTableName() + "_";
		handlerOptionMap(list, map, prefix);
		List<FormTable> formTableList = formTableDao
				.getSubTableByMainTableId(tableId);
		for (FormTable bpmFormTable : formTableList) {
			prefix = "s_" + bpmFormTable.getTableName() + "_";
			Long subTableId = bpmFormTable.getTableId();
			List<FormField> subFieldList = formFieldDao
					.getByTableIdContainHidden(subTableId);
			handlerOptionMap(subFieldList, map, prefix);
		}
		return map;
	}

	/**
	 * 构建选项的国际化资源map
	 * 
	 * @param list
	 * @param map
	 * @param prefix
	 */
	private void handlerOptionMap(List<FormField> list,
			Map<String, FormField> map, String prefix) {
		for (FormField bpmFormField : list) {
			String options = bpmFormField.getOptions();
			if (StringUtil.isNotEmpty(options)) {
				String key = prefix + bpmFormField.getFieldName();
				map.put(key, bpmFormField);
			}
		}
	}

	/**
	 * 虚构子表的字符串权限并变成 JS变量 的字符串
	 * 
	 * @param bpmFormTable
	 * @param onlyRead
	 */
	public String getSubPermission(FormTable bpmFormTable, boolean onlyRead) {
		String html = "";
		// 子表字段权限
		List<FormTable> tableList = bpmFormTable.getSubTableList();
		Map<String, List<JSONObject>> subFileJsonMap = new HashMap<String, List<JSONObject>>();
		List<JSONObject> subJsonList = new ArrayList<JSONObject>();
		List<JSONObject> subTableShowList = new ArrayList<JSONObject>();
		for (FormTable table : tableList) {
			// 子表事个表的权限
			JSONObject permission = null;
			if (onlyRead) {
				permission = formRightsService.getReadPermissionJson(
						table.getTableName(), table.getTableDesc(), 4);
			} else {
				permission = formRightsService.getPermissionJson(
						table.getTableName(), table.getTableDesc(), 4);
			}
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", bpmFormTable.getTableId());
			permission.put("mainTableName", bpmFormTable.getTableName());
			subTableShowList.add(permission);

			// 每个子表中的每个字段
			List<FormField> subFieldList = table.getFieldList();
			for (FormField field : subFieldList) {
				// 子表事个表的权限
				JSONObject subPermission = null;
				if (onlyRead) {
					subPermission = formRightsService.getReadPermissionJson(
							field.getFieldName(), field.getFieldDesc(), 1);
					subPermission.put("power", "r");
				} else {
					subPermission = formRightsService.getPermissionJson(
							field.getFieldName(), field.getFieldDesc(), 1);
					subPermission.put("power", "w");
				}
				subPermission.put("tableId", table.getTableId());
				subPermission.put("tableName", table.getTableName());
				subPermission.put("mainTableId", bpmFormTable.getTableId());
				subPermission.put("mainTableName", bpmFormTable.getTableId());
				subJsonList.add(subPermission);
			}
		}
		subFileJsonMap.put("subFileJsonList", subJsonList);
		subFileJsonMap.put("subTableShowList", subTableShowList);

		String subFilePermissionMap = "{}";
		if (BeanUtils.isNotEmpty(subFileJsonMap)) {
			subFilePermissionMap = JSONObject.fromObject(subFileJsonMap)
					.toString();
		}
		html += "<script type=\"text/javascript\" > var subFilePermissionMap = "
				+ subFilePermissionMap + " </script>";
		return html;
	}

	/**
	 * rel关联表的字符串权限并变成 JS变量 的字符串
	 * 
	 * @param bpmFormTable
	 * @param onlyRead
	 */
	public String getRelPermission(FormTable bpmFormTable, boolean onlyRead) {
		String html = "";
		// rel关联表字段权限
		List<FormTable> tableList = bpmFormTable.getRelTableList();
		Map<String, List<JSONObject>> relFileJsonMap = new HashMap<String, List<JSONObject>>();
		List<JSONObject> relJsonList = new ArrayList<JSONObject>();
		List<JSONObject> relTableShowList = new ArrayList<JSONObject>();
		for (FormTable table : tableList) {
			// 子表事个表的权限
			JSONObject permission = null;
			if (onlyRead) {
				permission = formRightsService.getReadPermissionJson(
						table.getTableName(), table.getTableDesc(), 4);
			} else {
				permission = formRightsService.getPermissionJson(
						table.getTableName(), table.getTableDesc(), 4);
			}
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", bpmFormTable.getTableId());
			permission.put("mainTableName", bpmFormTable.getTableName());
			relTableShowList.add(permission);

			// 每个子表中的每个字段
			List<FormField> relFieldList = table.getFieldList();
			for (FormField field : relFieldList) {
				// 子表事个表的权限
				JSONObject relPermission = null;
				if (onlyRead) {
					relPermission = formRightsService.getReadPermissionJson(
							field.getFieldName(), field.getFieldDesc(), 1);
					relPermission.put("power", "r");
				} else {
					relPermission = formRightsService.getPermissionJson(
							field.getFieldName(), field.getFieldDesc(), 1);
					relPermission.put("power", "w");
				}
				relPermission.put("tableId", table.getTableId());
				relPermission.put("tableName", table.getTableName());
				relPermission.put("mainTableId", bpmFormTable.getTableId());
				relPermission.put("mainTableName", bpmFormTable.getTableId());
				relJsonList.add(relPermission);
			}
		}
		relFileJsonMap.put("relFileJsonList", relJsonList);
		relFileJsonMap.put("relTableShowList", relTableShowList);

		String relFilePermissionMap = "{}";
		if (BeanUtils.isNotEmpty(relFileJsonMap)) {
			relFilePermissionMap = JSONObject.fromObject(relFileJsonMap)
					.toString();
		}
		html += "<script type=\"text/javascript\" > var relFilePermissionMap = "
				+ relFilePermissionMap + " </script>";
		return html;
	}

	/**
	 * 查询子表编辑行默认的东西时，要默认出来
	 * 
	 * @param list
	 * @param map
	 * @param prefix
	 */
	private String changeSubShow(String html) {
		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentOrg();
		IPosition pos = (IPosition) UserContextUtil.getCurrentPos();

		Document doc = Jsoup.parseBodyFragment(html);
		// 查询子表编辑行默认的东西时，要默认出来
		Elements formtypes = doc
				.select("[formtype=edit],[formtype=form],[formtype=window]");
		for (Element ft : formtypes) {
			// Elements
			// rows=ft.select("input[name^=s:],textarea[name^=s:],select[name^=s:]");
			Elements rows = ft.select("input[name^=s:]");
			for (Element el : rows) {
				String name = el.attr("name");
				String displayDate = el.attr("displayDate"); // 默认当前时间
				if ("1".equals(displayDate)) {

				} else {
					String showCurUser = el.attr("showCurUser"); // 默认当前人
					String showCurOrg = el.attr("showCurOrg"); // 默认当前部门
					String showCurPos = el.attr("showCurPos"); // 默认当前岗位

					Elements inputs = ft.select("input[name=" + name + "ID]");
					Element input = null;
					String value = "";
					Long id_value = 0l;
					if (inputs.size() == 1) { // 有对应的
						input = ft.select("input[name=" + name + "ID]").get(0);
					}

					if ("1".equals(showCurUser)) {
						value = user.getFullname();
						id_value = user.getUserId();
					} else if ("1".equals(showCurOrg)) {
						value = org.getOrgName();
						id_value = user.getUserId();
					} else if ("1".equals(showCurPos)) {
						value = pos.getPosName();
						id_value = pos.getPosId();
					}

					if (StringUtil.isNotEmpty(value)) {
						el.attr("value", value);
						if (BeanUtils.isNotEmpty(input) && id_value > 0) {
							input.attr("value", id_value.toString());
						}
					}
				}
			}
		}
		return doc.body().html();
	}

	/**
	 * 查询rel表编辑行默认的东西时，要默认出来
	 * 
	 * @param list
	 * @param map
	 * @param prefix
	 */
	private String changeRelShow(String html) {
		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		ISysOrg org = (ISysOrg) UserContextUtil.getCurrentOrg();
		IPosition pos = (IPosition) UserContextUtil.getCurrentPos();

		Document doc = Jsoup.parseBodyFragment(html);
		// 查询子表编辑行默认的东西时，要默认出来
		Elements formtypes = doc
				.select("[formtype=edit],[formtype=form],[formtype=window]");
		for (Element ft : formtypes) {
			// Elements
			// rows=ft.select("input[name^=s:],textarea[name^=s:],select[name^=s:]");
			Elements rows = ft.select("input[name^=r:]");
			for (Element el : rows) {
				String name = el.attr("name");
				String displayDate = el.attr("displayDate"); // 默认当前时间
				if ("1".equals(displayDate)) {

				} else {
					String showCurUser = el.attr("showCurUser"); // 默认当前人
					String showCurOrg = el.attr("showCurOrg"); // 默认当前部门
					String showCurPos = el.attr("showCurPos"); // 默认当前岗位

					Elements inputs = ft.select("input[name=" + name + "ID]");
					Element input = null;
					String value = "";
					Long id_value = 0l;
					if (inputs.size() == 1) { // 有对应的
						input = ft.select("input[name=" + name + "ID]").get(0);
					}

					if ("1".equals(showCurUser)) {
						value = user.getFullname();
						id_value = user.getUserId();
					} else if ("1".equals(showCurOrg)) {
						if(org!=null) {
							value = org.getOrgName();
						}
						id_value = user.getUserId();
					} else if ("1".equals(showCurPos)) {
						value = pos.getPosName();
						id_value = pos.getPosId();
					}

					if (StringUtil.isNotEmpty(value)) {
						el.attr("value", value);
						if (BeanUtils.isNotEmpty(input) && id_value > 0) {
							input.attr("value", id_value.toString());
						}
					}
				}
			}
		}
		return doc.body().html();
	}

	/**
	 * 根据主键查找记录
	 * */
	public Map<String, Object> getByKey(String tableName, String pk) {
		return dao.getByKey(tableName, pk);
	}

	/**
	 * 根据外键查询列表数据。
	 * 
	 * @param tableName
	 * @param fkValue
	 * @param tableId
	 * @return
	 */
	public List<Map<String, Object>> getByFk(Map<String, Object> params,
			FormTable table, String fkValue, boolean isHandleData) {

		return dao.getByFk(params, table, fkValue, isHandleData);

	}

	@Override
	public IFormData getFormData(String businessKey,IFormTable bpmFormTable,String json) throws Exception {
		// 有主键的情况,表示数据已经存在。
		if (StringUtil.isNotEmpty(businessKey)) {
			String pkName = bpmFormTable.isExtTable() ? bpmFormTable
					.getPkField() : TableModel.PK_COLUMN_NAME;
			PkValue pkValue = new PkValue(pkName, businessKey);
			pkValue.setIsAdd(false);
			return FormDataUtil.parseJson(json, pkValue, (FormTable)bpmFormTable);

		} else {
			return FormDataUtil.parseJson(json, (FormTable)bpmFormTable);
		}
	}
	
	/**
	 * 根据主键获取子表的数据。
	 * @param tableName
	 * @param pk
	 * @return
	 */
	@Override
	public List getByFk(String tableName, Long fk) {
		return this.getByFk(tableName, fk);
	}
	/**
	 * 删除业务数据。
	 * 
	 * @param id
	 * @param tableName
	 * @throws Exception
	 */
	@Override
	public void delById(String businessKey, String tableName) throws Exception {
		FormTable bpmFormTable=formTableService.getByTableName(tableName);
		this.delById(businessKey, bpmFormTable);
	}
	/**
	 * 保存表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	@Override
	public void handFormData(Map<String, Object> params, IFormData bpmFormData,
			IProcessRun processRun) throws Exception {
		this.dao.handFormData(params,(FormData)bpmFormData,processRun);
		
	}
	/**
	 * 处理动态表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	@Override
	public void handFormData(Map<String, Object> params, IFormData bpmFormData,
			IProcessRun processRun, String nodeId) throws Exception {
		dao.handFormData(params,(FormData)bpmFormData,processRun,nodeId);
	}

	/**
	 * 
	 * @param bpmFormTable
	 * @param pk
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getByKey(IFormTable bpmFormTable, String pk,
			String[] fields) throws Exception {
		return this.dao.getByKey((FormTable)bpmFormTable, pk, fields);
	}

	/**
	 * 根据主键查询列表数据。
	 * 
	 * @param tableId
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	@Override
	public IFormData getByKey(Map<String, Object> parmas, long tableId,
			String pkValue, boolean isHandleData) throws Exception {
		return dao.getByKey(parmas,tableId,pkValue,isHandleData);
	}

	/**
	 * 获取新的主键
	 * @param pkname
	 * @param pkvalue
	 * @param isAdd
	 * @return
	 */
	@Override
	public IPkValue getNewPkValue(String pkname, String pkvalue, boolean isAdd) {
		PkValue pkValue=new PkValue();
		pkValue.setIsAdd(true);
		pkValue.setValue(pkvalue);
		pkValue.setName(pkname);
		return pkValue;
	}

	/**
	 * 保存表单数据
	 * @param bpmFormData
	 * @throws Exception
	 */
	@Override
	public void handFormData(Map<String, Object> params, IFormData bpmFormData) throws Exception {
		dao.handFormData(params,(FormData)bpmFormData);
	}

	@Override
	public FormModel getStartForm(INodeSet bpmNodeSet, String businessKey, String actDefId,
			String ctxPath, Long userId) throws Exception {
		FormModel formModel = new FormModel();
		if (bpmNodeSet == null||bpmNodeSet.getFormType()==-1)
			return formModel;
		if (bpmNodeSet.getFormType() == BpmConst.OnLineForm.shortValue()) {
			Long formKey = bpmNodeSet.getFormKey();
			if (formKey != null && formKey > 0) {
				FormDef bpmFormDef = formDefService
						.getDefaultPublishedByFormKey(formKey);
				if (bpmFormDef != null) {
					FormTable bpmFormTable = formTableService.getById(bpmFormDef.getTableId());
					bpmFormDef.setTableName(bpmFormTable.getTableName());

					String formHtml = this.obtainHtml(bpmFormDef, userId,businessKey, "", actDefId, bpmNodeSet.getNodeId(), ctxPath, "", false);
					formHtml += CommonTools.null2String(bpmNodeSet.getInitScriptHandler());
					formModel.setFormHtml(formHtml);
				}
			}
		} else {
			String formUrl = bpmNodeSet.getFormUrl();
			// 替换主键。
			formUrl = formUrl.replaceFirst(BpmConst.FORM_PK_REGEX, businessKey);
			if (!formUrl.startsWith("http")) {
				formUrl = ctxPath + formUrl;
			}
			formModel.setFormType(BpmConst.UrlForm);
			formModel.setFormUrl(formUrl);
		}
		return formModel;
	}

	@Override
	public String obtainHtml(Long formId, Long userId, String pkValue,
			String instanceId, String actDefId, String nodeId, String ctxPath,
			String parentActDefId, boolean isCopyFlow) throws Exception {
		FormDef bpmFormDef=formDefService.getById(formId);
		return this.obtainHtml(bpmFormDef, userId, pkValue, "", actDefId, nodeId, ctxPath, "", false);
	}

	@Override
	public IFormData getFormFromJson(String json, IPkValue pkValue,
			IFormTable bpmFormTable) throws Exception {
		if(pkValue==null){
			return FormDataUtil.parseJson(json,(FormTable)bpmFormTable);
		}else{
			return FormDataUtil.parseJson(json, (PkValue)pkValue,(FormTable)bpmFormTable);
		}
	}

	/**
	 * @param bpmFormData
	 * @param processRun
	 * @throws Exception 
	 */
	@Override
	public void handFormData(IFormData bpmFormData, IProcessRun processRun) throws Exception {
		dao.handFormData(null,(FormData)bpmFormData, processRun);
	}

	/**
	 * @param bpmFormData
	 * @param processRun
	 * @param nodeId
	 * @throws Exception 
	 */
	@Override
	public void handFormData(IFormData bpmFormData, IProcessRun processRun,
			String nodeId) throws Exception {
		dao.handFormData(null,(FormData)bpmFormData, processRun, nodeId);
	}

	/**
	 * 根据主键获取关联表的数据。
	 * 
	 * @param tableName
	 * @param fkFieldName
	 *            外键字段
	 * @param fkFieldValue外键值
	 * @param orderFieldName
	 *            排序字段
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getRelDataByFk(String relTableName,
			String fkFieldName, Object fkFieldValue, String orderFieldName) {
		List<Map<String, Object>>  list = this.dao.getRelDataByFk(relTableName,
				fkFieldName, fkFieldValue, orderFieldName);
		return list;
	}
	
	
	/**
	 * 根据业务表id查找记录
	 *@author YangBo @date 2016年11月24日上午11:19:15
	 *@param jdbcTemplate
	 *@param pk
	 *@param bpmFormTable
	 *@param isHandleData
	 *@return
	 */
	@Override
	public Map<String, Object> getByKey(JdbcTemplate jdbcTemplate,  String  pkField,String pkValue, IFormTable bpmFormTable ,boolean isHandleData){
		PkValue pk=new PkValue(pkField,pkValue);
		return dao.getByKey(jdbcTemplate, pk,  (FormTable)bpmFormTable, true);
	}
	
	/**
	 * 根据主键和节点id查找列表数据
	 * @author liubo
	 * @param pkValue
	 * @param nodeId
	 * @return
	 */
	public FormData getBpmFormData(String pkValue, String nodeId) {
		if (StringUtil.isEmpty(pkValue)) throw new RuntimeException("获取业务json PK 不能为空");
		IProcessRun run = this.processRunService.getByBusinessKey(null,pkValue);
 
		FormDef bpmFormDef = formDefService.getById(run.getFormDefId());
 
		FormTable bpmFormTable = formTableService.getTableById(bpmFormDef.getTableId());
 
		FormData data = null;
		try {
			data = this.dao.getByKey(null,bpmFormTable.getTableId(), pkValue, run.getActDefId(), nodeId, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		return data;
	}

    /**
     * 流程控件字段信息更新
     * value：值
     * businessKey：主键
     * formkey：表单设计key
     */
    public void updateProcessData(String value,String businessKey,Long formkey) {
    	try{
    		FormDef formDef = formDefService.getDefaultVersionByFormKey(formkey);
        	Long tableId = formDef.getTableId();
        	FormTable ftable = formTableService.getById(tableId);
        	//获取流程控件字段的key
        	List<FormField> list = formTableService.getControlField(tableId,IFieldPool.FLOW_STATE);
        	if(BeanUtils.isEmpty(list)){
        		return;
        	}
        	String collName = list.get(0).getFieldName();    	
        	Map updateMap = new HashMap();
        	updateMap.put(TableModel.CUSTOMER_COLUMN_PREFIX+collName, value);
        	
        	this.dao.updateDataByPk(updateMap,TableModel.CUSTOMER_TABLE_PREFIX+ftable.getTableName(),ftable.getPkField(),businessKey);
    	}catch(Exception e){
    		logger.error(e.getMessage());
    	}
    	
    }
    public void updateCustomRecord(List<CustomRecord> list) {
    	dao.updateCustomRecord(list);
    }
    public void addCustomRecord(List<CustomRecord> list) {
    	dao.addCustomRecord(list);
    }

}
