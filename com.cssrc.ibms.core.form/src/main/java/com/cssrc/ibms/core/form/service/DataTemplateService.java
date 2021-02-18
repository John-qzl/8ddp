package com.cssrc.ibms.core.form.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.custom.intf.IAdvancedQueryService;
import com.cssrc.ibms.api.custom.intf.ICustomService;
import com.cssrc.ibms.api.custom.model.IAdvancedQuery;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IDataTemplatefilterService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRightsService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.rec.intf.IRecRoleFunService;
import com.cssrc.ibms.api.rec.intf.IRecRoleService;
import com.cssrc.ibms.api.rec.intf.IRecRoleSonFunService;
import com.cssrc.ibms.api.rec.intf.IRecRoleSonService;
import com.cssrc.ibms.api.rec.model.IRecRoleFun;
import com.cssrc.ibms.api.rec.model.IRecRoleSonFun;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.ISysBusEventService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.ISysBusEvent;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.dialect.DialectFactoryBean;
import com.cssrc.ibms.core.db.mybatis.entity.SQLClause;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.page.PageUtils;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.encrypt.FiledEncryptFactory;
import com.cssrc.ibms.core.encrypt.IFiledEncrypt;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.excel.Excel;
import com.cssrc.ibms.core.excel.editor.IFontEditor;
import com.cssrc.ibms.core.excel.style.Color;
import com.cssrc.ibms.core.excel.style.font.BoldWeight;
import com.cssrc.ibms.core.excel.style.font.Font;
import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.form.dao.FormDefDao;
import com.cssrc.ibms.core.form.dao.FormFieldDao;
import com.cssrc.ibms.core.form.dao.FormTableDao;
import com.cssrc.ibms.core.form.dao.FormTemplateDao;
import com.cssrc.ibms.core.form.dao.RelTableDao;
import com.cssrc.ibms.core.form.excel.DataEntity;
import com.cssrc.ibms.core.form.excel.ExcelCheck;
import com.cssrc.ibms.core.form.excel.ExcelReader;
import com.cssrc.ibms.core.form.excel.FieldEntity;
import com.cssrc.ibms.core.form.excel.TableEntity;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FilterJsonStruct;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.model.PkValue;
import com.cssrc.ibms.core.form.model.SubTable;
import com.cssrc.ibms.core.form.util.FileGridUtil;
import com.cssrc.ibms.core.form.util.FormDataUtil;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.ArrayUtil;
import com.cssrc.ibms.core.util.string.StringPool;
import com.cssrc.ibms.core.util.string.StringUtil;

import freemarker.template.TemplateException;

/**
 * <pre>
 * 对象功能:业务数据模板 Service类 
 * 开发人员:zhulongchao
 * </pre>
 */
@Service
public class DataTemplateService extends BaseService<DataTemplate> implements
		IDataTemplateService {
	@Resource
    private DataTemplateService dataTemplateService;
	@Resource
	private DataTemplateDao dao;
	@Resource
    private RelTableService relTableService;
	@Resource
    private RelTableDao relTableDao;
	@Resource
	private FormTemplateDao formTemplateDao;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private IPositionService positionService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	private FormHandlerService formHandlerService;
	@Resource
	private ISysDataSourceService sysDataSourceService;
	@Resource
	private GroovyScriptEngine groovyScriptEngine;
	@Resource
	private FormTableService formTableService;
	@Resource
	IFormFieldService formFieldService;
	@Resource
	private FormDefDao formDefDao;
	@Resource
	private FormTableDao formTableDao;
	@Resource
	private FormFieldDao formFieldDao;
	@Resource
	private IProcessRunService processRunService;
	@Resource
	private FormDialogService formDialogService;
	@Resource
	private FormDefService formDefService;
	@Resource
	private ISysBusEventService sysBusEventService;
	@Resource
	IFormRightsService formRightsService;
	@Resource
	ISysFileService sysFileService;
	@Resource
	IRecRoleFunService recRoleFunService;
	@Resource
	IRecRoleService recRoleService;
	@Resource
	IRecRoleSonService recRoleSonService;
	@Resource
	IRecRoleSonFunService recRoleSonFunService;
	@Resource
	IDefinitionService definitionService;
	@Resource
	IDataTemplatefilterService dataTemplatefilterService;
	@Resource
	ISysParameterService sysParameterService;
	@Resource
	FormControlService  formControlService;
	@Resource
	INodeSetService nodeSetService;
	@Resource
	IAdvancedQueryService advancedQueryService;
	public DataTemplateService() {
	}

	@Override
	protected IEntityDao<DataTemplate, Long> getEntityDao() {
		return dao;
	}

	public DataTemplate getByFormKey(Long formKey) {
		return dao.getByFormKey(formKey);
	}

	/**
	 * 保存信息
	 * 
	 * @param bpmDataTemplate
	 * @param flag
	 *            是否是新增
	 * @throws Exception
	 */
	public void save(DataTemplate bpmDataTemplate, boolean flag)
			throws Exception {

		String templateHtml = generateTemplate(bpmDataTemplate);
		//将会用绑定的表单模板覆盖编辑模板的内容
		bpmDataTemplate.setTemplateHtml(templateHtml);
		if (flag) {
			Long dtId=UniqueIdUtil.genId();
			bpmDataTemplate.setId(dtId);
			this.add(bpmDataTemplate);
			//添加默认文件附件模板(初始化添加)
			sysFileService.saveFileAndAttachHtml(dtId);
			//添加默认流程监控模板(初始化添加)
			sysFileService.saveProcessTempHtml(dtId);
			//生成默认文件模板
			sysFileService.createFileAttachJSP(dtId);
			//生成流程监控默认文件模板
			sysFileService.createProcessJSP(dtId);

		} else {
			DataTemplate oldDataTemplate=dataTemplateService.getById(bpmDataTemplate.getId());
			//保存明细多tab相关字段
			bpmDataTemplate.setRecRightField(oldDataTemplate.getRecRightField());
			bpmDataTemplate.setMultiTabTempHtml(oldDataTemplate.getMultiTabTempHtml());
			bpmDataTemplate.setFileTempHtml(oldDataTemplate.getFileTempHtml());
			bpmDataTemplate.setAttacTempHtml(oldDataTemplate.getAttacTempHtml());
			//保存流程监控的条件字段
			bpmDataTemplate.setProcessCondition(oldDataTemplate.getProcessCondition());
			//保存流程监控模板
			Boolean isGet = false;
			if(BeanUtils.isEmpty(oldDataTemplate.getProcessTempHtml())){
				isGet = true;
			}else{
				bpmDataTemplate.setProcessTempHtml(oldDataTemplate.getProcessTempHtml());
			}
			
			this.update(bpmDataTemplate);
			if(BeanUtils.isEmpty(oldDataTemplate.getFileTempHtml())||BeanUtils.isEmpty(oldDataTemplate.getAttacTempHtml())){
				//添加默认文件附件模板(初始化添加)
				sysFileService.saveFileAndAttachHtml(bpmDataTemplate.getId());
				//生成默认文件模板
				sysFileService.createFileAttachJSP(bpmDataTemplate.getId());
				//添加默认流程监控模板(初始化添加)
				if(isGet){
					sysFileService.saveProcessTempHtml(bpmDataTemplate.getId());
				}
				//生成默认流程监控模板
				sysFileService.createProcessJSP(bpmDataTemplate.getId());
			}
		}
	}
	public String generateTemplate(IDataTemplate dataTemplate,String html)throws Exception {
		DataTemplate bpmDataTemplate = (DataTemplate )dataTemplate;
		String oldDisplayFiled = bpmDataTemplate.getDisplayField();
		FormTable bpmFormTable = formTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
		// 是否有条件查询
		boolean hasCondition = hasCondition(bpmDataTemplate.getConditionField());
		// 是否有功能按钮
		boolean hasManage = hasManage(bpmDataTemplate.getManageField());
		// 显示字段特殊处理
		displayFieldManage(bpmDataTemplate,bpmFormTable.getPkField());
		//流程字段处理
		handlerProcessCol(bpmFormTable,bpmDataTemplate);
		// 第一次解析模板
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bpmDataTemplate", bpmDataTemplate);
		map.put("hasCondition", hasCondition);
		map.put("hasManage", hasManage);
		map.put("pkField", bpmFormTable.getPkField());
		map.put("formatData", getFormatDataMap(bpmFormTable));
		String templateHtml = freemarkEngine.parseByStringTemplate(map,html);
		//解析完成后还原
		bpmDataTemplate.setDisplayField(oldDisplayFiled);
		return templateHtml;
	}
	/**
	 * 解析生成第一次的模板
	 * 
	 * @param bpmDataTemplate
	 * @return
	 * @throws Exception
	 */
	public String generateTemplate(DataTemplate dataTemplate)
			throws Exception {
		FormTemplate bpmFormTemplate = formTemplateDao.getByTemplateAlias(dataTemplate.getTemplateAlias());
		String html = bpmFormTemplate.getHtml();
		return generateTemplate(dataTemplate,html);
	}
	private void handlerProcessCol(FormTable bpmFormTable,DataTemplate bpmDataTemplate){
		List<FormField> fieldList = bpmFormTable.getFieldList();
		JSONObject satrtObj = new JSONObject();
		JSONObject endObj = new JSONObject();
		satrtObj.put("key", IFieldPool.FLOW_START_KEY);satrtObj.put("value",IFieldPool.FLOW_START_VAL);
		endObj.put("key", IFieldPool.FLOW_END_KEY);endObj.put("value", IFieldPool.FLOW_END_VAL);
		for (FormField bpmFormField : fieldList) {
			JSONArray jarry = new JSONArray();
			Short controlType = bpmFormField.getControlType();
			String fieldType = bpmFormField.getFieldType();
			if(fieldType.equals(IFieldPool.DATATYPE_VARCHAR)){
				switch(controlType){
				case IFieldPool.FLOW_STATE:
					Long defId = bpmDataTemplate.getDefId();
					if(BeanUtils.isNotEmpty(defId)){
						List<? extends INodeSet> list = nodeSetService.getAllByDefId(defId);
						jarry.add(satrtObj);
						for(INodeSet set : list){
							if(BeanUtils.isNotEmpty(set.getNodeId())){
								JSONObject nodeobj = new JSONObject();
								nodeobj.put("key", set.getActDefId()+IFieldPool.FLOW_SPLIT+set.getNodeId());
								nodeobj.put("value",set.getNodeName());
								jarry.add(nodeobj);
							}
						}
						jarry.add(endObj);
					}
				}
			}
			if(BeanUtils.isNotEmpty(jarry)){
				bpmFormField.setOptions(jarry.toString());
				formFieldDao.update(bpmFormField);
			}
		
		}
	}
	private void displayFieldManage(DataTemplate bpmDataTemplate,String pkField){
		Long displayId = bpmDataTemplate.getId();
		Long defId = bpmDataTemplate.getDefId();
		JSONObject paramObj = new JSONObject();
		paramObj.put("displayId", displayId);
		paramObj.put("defId", defId);
		paramObj.put("pk", "@"+pkField+"#");
		String displayfield = bpmDataTemplate.getDisplayField();
		JSONArray arr = JSONArray.fromObject(displayfield);
		JSONArray newArr = new JSONArray();
		if(BeanUtils.isNotEmpty(arr)){
			for(int i= 0; i<arr.size();i++){
				JSONObject obj = JSONObject.fromObject(arr.get(i));
				if(BeanUtils.isNotEmpty(obj)){
					if(obj.containsKey("displayType")&&obj.containsKey("action")
                			&&obj.containsKey("onclick")&&obj.containsKey("urlParams")){
						String action = obj.getString("action");						
						action = replaceUrl(action,paramObj);
						action = mergeUrlAndParams(action,obj.getString("urlParams"));
						obj.put("action", action);						
					}
                		
				}
				newArr.add(obj);
			}
		}
		bpmDataTemplate.setDisplayField(newArr.toString());
	}
	private String replaceUrl(String url,JSONObject obj){
		String regEx ="\\[{1}.+?\\]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(url);
		if(obj.isEmpty()){
			return url;
		}
		String result = url;
		while(matcher.find()){
			String groups = matcher.group();
			String gg = groups.replaceAll("\\[", "").replaceAll("\\]", "");
			if(obj.containsKey(gg)){
				result = matcher.replaceFirst(CommonTools.Obj2String(obj.get(gg)));
			}else{
				result = matcher.replaceFirst("noKey-"+gg);
			}
			matcher = pattern.matcher(result);
		}
		result = result.replace("@", "[").replace("#", "]");
		return result;
	}
	private String mergeUrlAndParams(String urlPath, String urlParams) {
		try{
			String otherParam = "";
			if (StringUtil.isEmpty(urlParams))
				return urlPath;
			StringBuffer sb = new StringBuffer();
			int idx1 = urlPath.indexOf("?");
			if (idx1 > 0){
				sb.append(urlPath.substring(0, idx1));
				otherParam = urlPath.substring(idx1+1,urlPath.length());
			}else{
				sb.append(urlPath);
			}
			sb.append("?");
			JSONArray paramsJsonArray = JSONArray.fromObject(urlParams);
			for (Iterator localIterator = paramsJsonArray.iterator(); localIterator.hasNext();) {
				Object obj = localIterator.next();
				JSONObject jsonObject = JSONObject.fromObject(obj);
				String isCustomParam = jsonObject.getString("isCustomParam");
				String key = jsonObject.getString("key");
				String value = jsonObject.getString("value");
				if (1 == Short.valueOf(isCustomParam).shortValue()) {
					sb.append(key);
					sb.append("=");
					if (StringUtil.isNotEmpty(value)) {
						sb.append(value);
					}
					sb.append("&");
				} else {
					sb.append(key);
					sb.append("=");
					sb.append("[" + value + "]");
					sb.append("&");
				}
			}
			sb.append(otherParam);
			return sb.toString();
		}catch(Exception e){
			return urlPath;
		}
		
	}
	/**
	 * 是否有管理
	 * 
	 * @param manageField
	 * @return
	 */
	private boolean hasManage(String manageField) {
		if (StringUtils.isEmpty(manageField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		return jsonAry.size() > 0 ? true : false;
	}

	/**
	 * 是否有条件
	 * 
	 * @param conditionField
	 * @return
	 */
	private boolean hasCondition(String conditionField) {
		if (StringUtils.isEmpty(conditionField))
			return false;
		JSONArray jsonAry = JSONArray.fromObject(conditionField);
		return jsonAry.size() > 0 ? true : false;
	}

	/**
	 * 获得显示的字段的头
	 * 
	 * @param name
	 * @param desc
	 * @param permission
	 * @return
	 */
	public String getShowField(String name, String desc, String action,
			Map<String, String> sort, Map<String, Object> permission) {

		String html = "";
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name))
			return html;
		String sortSeq = "";
		boolean isRight = (Boolean) permission.get(name);
		if (isRight) {
			if (BeanUtils.isNotEmpty(sort)) {
				String sortField = sort.get("sortField");
				String orderSeq = sort.get("orderSeq");
				if (name.equalsIgnoreCase(sortField))
					sortSeq = "ASC".equalsIgnoreCase(orderSeq) ? "↑" : "↓";
			}
			return "<th><a href='####' onclick='linkAjax(this)' action='" + action
					+ "'>" + desc + sortSeq + "</a></th>";
		}
		return "";
	}

	/**
	 * 获得显示的字段的头
	 * 
	 * @param name
	 * @param desc
	 * @param permission
	 * @return
	 */
	public String getShowField(String name, String html,
			Map<String, Object> permission) {
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name))
			return html;
		boolean isRight = (Boolean) permission.get(name);
		if (isRight)
			return html;
		return "";
	}

	/**
	 * 显示列表的数据
	 * 
	 * @param name
	 * @param data
	 *            数据
	 * @param parse
	 *            处理的值
	 * @param permission
	 * @return
	 */
	public String getShowFieldList(String name, Map<String, Object> data,
			Map<String, Object> formatData, Map<String, Object> permission) {
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name))
			return "";
		boolean isRight = (Boolean) permission.get(name);
		if (isRight) {
			Object o = data.get(name);
			return "<td>" + o + "</td>";

		}
		return "";
	}

	/**
	 * 显示功能按钮
	 * 
	 * @param name
	 *            权限类型名字
	 * @param desc
	 *            按钮名字
	 * @param pkField
	 *            主键字段
	 * @param actionUrl
	 *            按钮的url
	 * @param permission
	 *            权限
	 * @return
	 */
	public String getManage(String name, String desc, String pkField,
			Map<String, Object> data, Map<String, String> actionUrl,
			Map<String, Object> permission) {
		String html = "";
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name)
				|| !permission.containsKey(name))
			return html;
		boolean isRight = (Boolean) permission.get(name);
		if (!isRight)
			return html;

		if (DataTemplate.MANAGE_TYPE_ADD.equals(name)
				|| DataTemplate.MANAGE_TYPE_EXPORT.equals(name)
				|| DataTemplate.MANAGE_TYPE_EXPORT.equals(name))
			return html;
		Object pk = data.get(pkField);
		String action = actionUrl.get(name);
		// action
		if (DataTemplate.MANAGE_TYPE_START.equals(name))
			action = action + "&businessKey=" + pk;
		else
			action = action + "&__pk__=" + pk;

		if (DataTemplate.MANAGE_TYPE_EDIT.equals(name)) {// 编辑
			html = "<a href='####' onclick='openLinkDialog({scope:this,isFull:true})'  action='"
					+ action + "' class='link edit'>" + desc + "</a>";
		} else if (DataTemplate.MANAGE_TYPE_DEL.equals(name)) {// 删除
			html = "<a href='" + action + "' class='link del'>" + desc + "</a>";
		} else if (DataTemplate.MANAGE_TYPE_DETAIL.equals(name)) {// 明细
			html = "<a href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "' class='link detail'>" + desc + "</a>";
		} else if (DataTemplate.MANAGE_TYPE_START.equals(name)) {// 启动
			html = "<a class='link run' href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "'>" + desc + "</a>";
		}

		return html;
	}

	/**
	 * 获取toolbar的按钮
	 * 
	 * @param name
	 *            权限类型名字
	 * @param desc
	 *            按钮名字
	 * @param actionUrl
	 *            按钮的url
	 * @param permission
	 *            权限
	 * @return
	 */
	public String getToolBar(String name, String desc,
			Map<String, String> actionUrl, Map<String, Object> permission) {
		String html = "";
		if (BeanUtils.isEmpty(permission) || StringUtils.isEmpty(name)
				|| !permission.containsKey(name))
			return html;
		boolean isRight = (Boolean) permission.get(name);
		if (!isRight)
			return html;

		if (DataTemplate.MANAGE_TYPE_EDIT.equals(name)
				|| DataTemplate.MANAGE_TYPE_DEL.equals(name)
				|| DataTemplate.MANAGE_TYPE_DETAIL.equals(name))
			return html;
		String action = actionUrl.get(name);
		if (DataTemplate.MANAGE_TYPE_ADD.equals(name)) {// 新增
			html = "<div class='group'>"
					+ "<a class='link add' href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		} else if (DataTemplate.MANAGE_TYPE_EXPORT.equals(name)) {// 导出
			html = "<div class='group'>"
					+ "<a class='link export' href='#' onclick='exportExcel(this)' action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		} else if (DataTemplate.MANAGE_TYPE_PRINT.equals(name)) {// 打印
			html = "<div class='group'>"
					+ "<a class='link print' href='#' onclick='' action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		} else if (DataTemplate.MANAGE_TYPE_START.equals(name)) {// 启动
			html = "<div class='group'>"
					+ "<a class='link run' href='#' onclick='openLinkDialog({scope:this,isFull:true}) action='"
					+ action + "'><span></span>" + desc + "</a></div>"
					+ "<div class='l-bar-separator'></div>";
		}
		return html;

	}
	public String getDisplay(IDataTemplate dataTemplate,Long curUserId,Map<String,Object> params) throws Exception{
		DataTemplate bpmDataTemplate =  (DataTemplate)dataTemplate;
		
		Long curOrgId = UserContextUtil.getCurrentOrg()==null?0L:UserContextUtil.getCurrentOrg().getOrgId();
		CommonVar.setCurrentVars(params);		
		Map<String, Object> rightMap = this.getRightMap(curUserId, curOrgId);
		FormTable bpmFormTable = formTableService.getByTableId(bpmDataTemplate.getTableId(), 1);
		Map<String, Object> formatData = this.getFormatDataMap(bpmFormTable);
		// 取得数据 (分页后的数据)包含构建查询sql和查询数据
		bpmDataTemplate = this.getData(bpmDataTemplate, bpmFormTable,rightMap, params, new HashMap(), formatData,null);
		String templateHtml = bpmDataTemplate.getTemplateHtml();
		Map<String, Object> map = new HashMap<String, Object>();						
		map.clear();
		map.put("bpmDataTemplate", bpmDataTemplate);
		map.put("service", this);
		map.put("formControlService", formControlService);
		map.put("permission",getPermission(DataTemplate.RIGHT_TYPE_SHOW,bpmDataTemplate.getDisplayField(), rightMap));
		
		String html = freemarkEngine.parseByStringTemplate(map, templateHtml);
		return html;
	}
	/**
	 * 预览数据
	 * 
	 * @param id
	 * @param params
	 * @param queryParams
	 * @return
	 * @throws Exception
	 */
	public String getDisplay(Long id, Map<String, Object> params, Map<String, Object> queryParams, Long curUserId) throws Exception {

		Long curOrgId = null;
		String rpcName = CommonTools.Obj2String(params.get(IFieldPool.rpcrefname));
		if(StringUtil.isEmpty(rpcName)){
			if (null != UserContextUtil.getCurrentOrg()) {
				curOrgId = UserContextUtil.getCurrentOrg().getOrgId();
			}
			// 设置常用变量
			CommonVar.setCurrentVars(params);
		}else{
			if (null != UserContextUtil.getCurrentOrg()) {
				curOrgId = UserContextUtil.getCurrentOrg().getOrgId();
			}
			ISysUser curUser = sysUserService.getById(curUserId);
			ISysOrg curOrg = null;
			IPosition curPos = null;
			if(curUser != null){ //对于一个人拥有多个岗位，暂不考虑
				Long[] orgIds = curUser.getOrgId();
				if(orgIds != null && orgIds.length > 0){
					curOrgId = orgIds[0];
					curOrg = sysOrgService.getById(curOrgId);
				}
				curPos = positionService.getDefaultPosByUserId(curUserId);
			}
			// 设置常用变量
			List<CommonVar> commonVars = CommonVar.getCurrentVars(true, curUser, curOrg, curPos);
			for (CommonVar var : commonVars){
				params.put(var.getAlias(), var.getValue());
			}
		}

		// 获取权限map
		Map<String, Object> rightMap = this.getRightMap(curUserId, curOrgId);
		// 获取业务数据模板IBMS_DATA_TEMPLATE的id
		DataTemplate bpmDataTemplate = dao.getById(id);
		// 是否初始化查询
		if (!params.containsKey(DataTemplate.PARAMS_KEY_ISQUERYDATA)) {
			params.put(DataTemplate.PARAMS_KEY_ISQUERYDATA,
					bpmDataTemplate.getIsQuery() == 0);
		}
		// 根据业务数据模板对应的主表id, 获取(主表, rel关系表, sub子表)信息
		FormTable bpmFormTable = formTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
		// 获取需要格式化的列map
		Map<String, Object> formatData = this.getFormatDataMap(bpmFormTable);

		String baseURL = (String) params.get("__baseURL");
		// 获取过滤的权限
		bpmDataTemplate = this.getRightFilterField(bpmDataTemplate, rightMap,
				baseURL);
		// 过滤条件KEY
		String filterKey = this.getFilterKey(bpmDataTemplate, params);
		// 构建URL
		String tableIdCode = (String) params.get("__tic");
		if (tableIdCode == null) {
			tableIdCode = "";
			params.put("__tic", "");
		}
		// 排序
		Map<String, String> sortMap = getSortMap(params, tableIdCode);

		// 自定义参数//TODO 空实现
		//parseCustomParameterAsMap(bpmDataTemplate, request);

		// 取得当前过滤的条件
		JSONObject filterJson = this.getFilterFieldJson(bpmDataTemplate,
				rightMap, params);
		// 判断是否要进行查询
		boolean isQueryData = (Boolean) params
				.get(DataTemplate.PARAMS_KEY_ISQUERYDATA);
		if (isQueryData) {
			// 取得数据 (分页后的数据)包含构建查询sql和查询数据
			bpmDataTemplate = this.getData(bpmDataTemplate, bpmFormTable,
					rightMap, params, sortMap, formatData, filterJson);
		}
		// 需要排除的url参数
		List<String> excludes = this.getExcludes(params, queryParams);

		String pageURL = this.addParametersToUrl(baseURL, params, excludes);
		String searchFormURL = baseURL;
		// 获取业务数据模板html
		String templateHtml = bpmDataTemplate.getTemplateHtml();
		// 如果取不到默认模板，就重新解析
		if (StringUtil.isEmpty(templateHtml)) {
			templateHtml = this.generateTemplate(bpmDataTemplate);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取分页的HTML
		String pageHtml = this.getPageHtml(bpmDataTemplate, map, tableIdCode,
				pageURL);
		//明细多TAB
    	/** 1.如果fromMultiTabView=true，需要进行表单权限按钮控制 */
    	String manageField = bpmDataTemplate.getManageField();
		Boolean fromMultiTabView = (Boolean)params.get("fromMultiTabView");
		if(fromMultiTabView){
			manageField = this.changeManageField(params,manageField);
		}				
		// 权限类型（管理）
		Map<String, Boolean> managePermission = getManagePermission(
				DataTemplate.RIGHT_TYPE_MANAGE,
				manageField, rightMap);
		params.put("__defId__", bpmDataTemplate.getDefId());
		// 第二次解析模板
		map.clear();
		map.put("bpmDataTemplate", bpmDataTemplate);
		map.put("pageHtml", pageHtml);
		map.put("pageURL", pageURL);
		map.put("sort", sortMap);
		map.put("sortField", sortMap.get("sortField"));
		map.put("orderSeq", sortMap.get("orderSeq"));
		map.put("tableIdCode", tableIdCode);
		map.put("searchFormURL", searchFormURL);
		// map.put("VarMap", map.getParameterMap());// custom parameter
		map.put("service", this);
		map.put("formControlService", formControlService);
		// 当前字段的权限
		map.put("permission",
				getPermission(DataTemplate.RIGHT_TYPE_SHOW,
						bpmDataTemplate.getDisplayField(), rightMap));
		// 功能按钮的权限
		map.put("managePermission", managePermission);
		// actionUrl
		map.put("actionUrl", getActionUrl(params));
		map.put("filterKey", filterKey);
		// 判断列表中是否出现checkbox
		map.put("checkbox", isCheckbox(managePermission));
		map.put("formatData", formatData);

		// 把自定义查询显示值，放到queryParams传到页面显示
		for (String key : params.keySet()) {
			if (key.lastIndexOf(IFieldPool.FK_SHOWAppCode) > -1) {
				Object obj = params.get(key);
				queryParams.put(key, obj);
			}
		}
		map.put("defParams", params);//用于用户写定制js时调用
		map.put("param", queryParams);
		// DBom管理-dbom使用：树节点拼接SQL语句
		map.put("dbomSql", CommonTools.Obj2String(params.get("__dbomSql__")));
		// DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
		map.put("dbomFKName", CommonTools.Obj2String(params.get("__dbomFKName__")));
		// DBom管理-dbom使用：子表新增表单时，存储外键字段值
		map.put("dbomFKValue", CommonTools.Obj2String(params.get("__dbomFKValue__")));
		// 封装当前用户，在FreeMarker中直接使用即可，使用方法${CurSysUser} by liqing 2016-02-19
		map.put("CurSysUser", UserContextUtil.getCurrentUser());
		
		//调用项目中的实现，没有则跳过
		ICustomService customService=(ICustomService)AppUtil.getProjectBean(ICustomService.class);
		if(BeanUtils.isNotEmpty(customService)){
			Map<String, Object> customMap= customService.getCustomDataTemplateMap(bpmDataTemplate, bpmFormTable, params,map);
			if(BeanUtils.isNotEmpty(customMap)){
				map = customMap;
			}
		}
		
		String html = freemarkEngine.parseByStringTemplate(map, templateHtml);
		//System.out.print(templateHtml);
		//System.out.print(html);
		TransactionAspectSupport.currentTransactionStatus();
		return html;
	}		
	
	/**
	 * 获取格式化的数据
	 * @param bpmFormTable
	 * @return
	 */
	public Map<String, Object> getFormatDataMap(FormTable bpmFormTable){
		return getFormatDataMap(bpmFormTable.getFieldList());
	}
	
    /**
     * 获取格式化的数据
     * @param bpmFormTable
     * @return
     */
	public Map<String, Object> getFormatDataMap(List<?extends IFormField> fieldList)
    {
        Map<String, Object> map = new HashMap<String, Object>();
		for (IFormField bpmFormField : fieldList) {
			String name = bpmFormField.getFieldName();
			Short controlType = bpmFormField.getControlType();
			String fieldType = bpmFormField.getFieldType();

			if (FormField.DATATYPE_DATE.equals(fieldType)) {
				map.put(name, bpmFormField.getDatefmt());
			} else {
				if (controlType.shortValue() == IFieldPool.SELECT_INPUT
						|| controlType.shortValue() == IFieldPool.RADIO_INPUT
						|| controlType.shortValue() == IFieldPool.CHECKBOX
						|| controlType.shortValue() == IFieldPool.SECURITY_CONTROL
						|| controlType.shortValue() == IFieldPool.SELECTS_INPUT
						|| controlType.shortValue() == IFieldPool.FLOW_STATE) { //流程状态控件
					//密级管理控件类型和下拉框等一并处理
					String options = bpmFormField.getJsonOptions();

					if (StringUtils.isEmpty(options))
						continue;

					Map<String, String> optionMap = new LinkedHashMap<String, String>();
					JSONArray jarray = JSONArray.fromObject(options);
					for (Object obj : jarray) {
						JSONObject json = (JSONObject) obj;
						String key = (String) json.get("key");
						String value = (String) json.get("value");
						optionMap.put(key, value);
					}
					map.put(name, optionMap);
				}else if(controlType.shortValue() == IFieldPool.DICTIONARY){
					//在格式化数据中将数据字典的type放进去  by liubo
					map.put(name, bpmFormField.getDictType().toString());
				}

			}
		}
		return map;
    }

	/**
	 * 是否有选择框
	 * 
	 * <pre>
	 * 	导出、打印
	 * </pre>
	 * 
	 * @param managePermission
	 * @return
	 */
	private boolean isCheckbox(Map<String, Boolean> managePermission) {
		if (BeanUtils.isEmpty(managePermission))
			return false;
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_DEL)) {// 如果包含"删除"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_DEL))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_EXPORT)) {// 如果包含"导出"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_EXPORT))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_PRINT)) {// 如果包含"打印"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_PRINT))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_REPORTDATA)) {// 如果包含"上报数据"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_REPORTDATA))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_ACCEPT)) {// //如果包含"接受数据"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_ACCEPT))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_DECLINE)) {// 如果包含"拒绝数据"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_DECLINE))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_FEEDBACK)) {// 如果包含"反馈数据"权限，则显示列表左侧的checkbox
			if (managePermission.get(DataTemplate.MANAGE_TYPE_FEEDBACK))
				return true;
		}
		if (managePermission.containsKey(DataTemplate.MANAGE_TYPE_ATTACH)) {//如果包含"附件"权限，则显示列表左侧的checkbox by YangBo
			if (managePermission.get(DataTemplate.MANAGE_TYPE_ATTACH))
				return true;
		}
		return false;
	}

	/**
	 * 获取排序的字段和标识
	 * 
	 * @param params
	 * @param tableIdCode
	 * @return
	 */
	private Map<String, String> getSortMap(Map<String, Object> params,
			String tableIdCode) {
		Map<String, String> sortMap = new HashMap<String, String>();
		// 排序
		String sortField = null;
		String orderSeq = "DESC";
		String newSortField = null;
		Boolean flag = false;//true 表示排序不反转
		if (params.get(tableIdCode + DataTemplate.SORTFIELD) != null)
			sortField = (String) params.get(tableIdCode
					+ DataTemplate.SORTFIELD);

		if (params.get(tableIdCode + DataTemplate.ORDERSEQ) != null)
			orderSeq = (String) params.get(tableIdCode + DataTemplate.ORDERSEQ);

		if (params.get(tableIdCode + "__ns__") != null)
			newSortField = (String) params.get(tableIdCode + "__ns__");
		
		if (params.get(tableIdCode + "__flag__") != null)
			flag = Boolean.valueOf(params.get(tableIdCode + "__flag__").toString());
		if (StringUtil.isNotEmpty(newSortField)&&!flag) {
			if (newSortField.equals(sortField)) {
				if (orderSeq.equals("ASC")) {
					orderSeq = "DESC";
				} else {
					orderSeq = "ASC";
				}
			}
			sortField = newSortField;
			params.put(tableIdCode + DataTemplate.SORTFIELD, sortField);
			params.put(tableIdCode + DataTemplate.ORDERSEQ, orderSeq);

			sortMap.put("sortField", sortField);
			sortMap.put("orderSeq", orderSeq);
		}else if (flag){
			params.put(tableIdCode + DataTemplate.SORTFIELD, sortField);
			params.put(tableIdCode + DataTemplate.ORDERSEQ, orderSeq);

			sortMap.put("sortField", sortField);
			sortMap.put("orderSeq", orderSeq);
		}

		return sortMap;
	}

	/**
	 * 排除的参数
	 * 
	 * @param params
	 * @param queryParams
	 * @return
	 */
	private List<String> getExcludes(Map<String, Object> params,
			Map<String, Object> queryParams) {

		List<String> excludes = new ArrayList<String>();
		for (String key : params.keySet()) {
			if (key.endsWith("__ns__")) {
				excludes.add(key);
			}
			if (key.endsWith("__pk__")) {
				excludes.add(key);
			}
		}
		excludes.add("rand");
		excludes.add("__baseURL");
		excludes.add("__tic");
		excludes.add("__displayId");
		// 排除查询的
		for (Entry<String, Object> entry : queryParams.entrySet()) {
			String key = entry.getKey();
			if (!key.startsWith("Q_"))
				continue;
			String[] aryParaKey = key.split("_");
			if (aryParaKey.length < 3)
				continue;
			String paraName = key.substring(2, key.lastIndexOf("_"));
			excludes.add(paraName);
		}

		return excludes;
	}

	/**
	 * 过滤条件KEY
	 * 
	 * @param bpmDataTemplate
	 * @param params
	 * @return
	 */
	public String getFilterKey(DataTemplate bpmDataTemplate,
			Map<String, Object> params) {
		Object key = params.get(DataTemplate.PARAMS_KEY_FILTERKEY);
		if (BeanUtils.isNotEmpty(key))
			return (String) key;
		String filterField = bpmDataTemplate.getFilterField();
		if (StringUtils.isEmpty(filterField))
			return "";
		JSONArray jsonAry = JSONArray.fromObject(filterField);
		if (JSONUtils.isNull(jsonAry) || jsonAry.size() == 0)
			return "";
		JSONObject jsonObj = jsonAry.getJSONObject(0);
		String filterKey = jsonObj.getString("key");
		params.put(DataTemplate.PARAMS_KEY_FILTERKEY, filterKey);
		return filterKey;
	}

	/**
	 * 获取有权限的过滤字段
	 * 
	 * @param bpmDataTemplate
	 * @param rightMap
	 * @param baseURL
	 * @return
	 */
	public DataTemplate getRightFilterField(DataTemplate bpmDataTemplate,
			Map<String, Object> rightMap, String baseURL) {
		String filterField = bpmDataTemplate.getFilterField();
		JSONArray jsonArray = JSONArray.fromObject(filterField);
		String destFilterField = new JSONArray().toString();
		if (JSONUtil.isEmpty(jsonArray)) {
			bpmDataTemplate.setFilterField(destFilterField);
			return bpmDataTemplate;
		}
		baseURL = baseURL.replace("/getDisplay.do", "/preview.do");
		String url = baseURL + "?__displayId__=" + bpmDataTemplate.getId();
		// 有权限过滤条件
		JSONArray jsonAry = new JSONArray();
		for (Object obj : jsonArray) {
			JSONObject jObj = (JSONObject) obj;
			JSONArray rightAry = JSONArray.fromObject(jObj.get("right"));
			JSONObject permission = (JSONObject) rightAry.getJSONObject(0);
			if (hasRight(permission, rightMap))
				jsonAry.add(obj);
		}
		if (JSONUtil.isEmpty(jsonAry)) {
			bpmDataTemplate.setFilterField(destFilterField);
			return bpmDataTemplate;
		}
		JSONArray destJsonAry = new JSONArray();
		for (Object obj : jsonAry) {
			JSONObject json = (JSONObject) obj;
			String name = json.getString("name");
			String key = json.getString("key");
			json.accumulate("desc", StringUtil.subString(name, 5, "..."));// 展示字段
			json.accumulate("url", url + "&"
					+ DataTemplate.PARAMS_KEY_FILTERKEY + "=" + key);
			destJsonAry.add(json);
		}
		bpmDataTemplate.setFilterField(destJsonAry.toString());
		return bpmDataTemplate;
	}
	/**
	 * 
	 * 获取Action的URl
	 * 
	 * @param params
	 * @return
	 */
	private Map<String, String> getActionUrl(Map<String, Object> params) {
		Map<String, String> map = new HashMap<String, String>();
		String __baseURL = (String) params.get("__baseURL");
		String __ctx = (String) params.get(DataTemplate.PARAMS_KEY_CTX);
		Long defId = (Long) params.get(DataTemplate.PARAMS_KEY_DEFID);

		String __displayId__ = "?__displayId__="
				+ (String) params.get("__displayId__");

		String addBaseURL = __baseURL.replace("/getDisplay.do", "/editData.do")
				+ __displayId__;
		String editBaseURL = __baseURL
				.replace("/getDisplay.do", "/editData.do") + __displayId__;
		String deleteBaseURL = __baseURL.replace("/getDisplay.do",
				"/deleteData.do") + __displayId__;
		String detailBaseURL = __baseURL.replace("/getDisplay.do",
				"/detailData.do") + __displayId__;
		String startBaseURL = BeanUtils.isEmpty(defId) ? "" : (__ctx
				+ "/oa/flow/task/startFlowForm.do"+__displayId__+"&defId=" + defId);
		String startBaseURL1 = BeanUtils.isEmpty(defId) ? "" : (__ctx
				+ "/oa/flow/task/startFlowForm.do"+__displayId__+"&defId=" + defId);
		String startBaseURL2 = BeanUtils.isEmpty(defId) ? "" : (__ctx
				+ "/oa/flow/task/startFlowForm.do"+__displayId__+"&defId=" + defId);
		String startBaseURL3 = BeanUtils.isEmpty(defId) ? "" : (__ctx
				+ "/oa/flow/task/startFlowForm.do"+__displayId__+"&defId=" + defId);
		// added by liqing 2015-11-24 start
		String printBaseURL = __baseURL.replace("/getDisplay.do",
				"/printData.do") + __displayId__;
		// added by liqing 2015-11-24 end
		// 导出
		String exportBaseURL = __baseURL.replace("/getDisplay.do",
				"/exportData.do")
				+ __displayId__
				+ '&'
				+ DataTemplate.PARAMS_KEY_FILTERKEY
				+ "="
				+ params.get(DataTemplate.PARAMS_KEY_FILTERKEY);

		// 导入
		String importBaseURL = __baseURL.replace("/getDisplay.do",
				"/importData.do") + __displayId__;
		// 同步
		// String syncUrl = __baseURL.replace("/getDisplay.do",
		// "/syncData.do") + __displayId__;
		//附件
		String attachBaseURL = __baseURL.replace("/getDisplay.do",
				"/fileView.do") + __displayId__;
		//流程监控
		String processTempURL = __baseURL.replace("/getDisplay.do",
				"/processView.do") + __displayId__ + "&defId=" + defId;		
		//复杂表单详细url 
		String complexDetailURL = __baseURL.replace("/getDisplay.do",
				"/multiTabView.do") + __displayId__;		
		
		map.put(DataTemplate.MANAGE_TYPE_ADD, addBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_EDIT, editBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_DEL, deleteBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_DETAIL, detailBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_START,  startBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_START1, startBaseURL1);
		map.put(DataTemplate.MANAGE_TYPE_START2, startBaseURL2);
		map.put(DataTemplate.MANAGE_TYPE_START3, startBaseURL3);
		map.put(DataTemplate.MANAGE_TYPE_EXPORT, exportBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_IMPORT, importBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_ATTACH, attachBaseURL);
		map.put(DataTemplate.MANAGE_TYPE_PROCESS, processTempURL);
		// 打印 by liqing
		map.put(DataTemplate.MANAGE_TYPE_PRINT, printBaseURL);
		// by wenjie
		map.put(DataTemplate.MANAGE_TYPE_COMPLEXDETAIL, complexDetailURL);
		// map.put(DataTemplate.MANAGE_TYPE_SYNC, syncUrl);
		return map;
	}

	/**
	 * 获取当前用户的权限Map
	 * 
	 * @param userId
	 * @param curOrgIdo
	 * @return
	 */
	public Map<String, Object> getRightMap(Long userId, Long curOrgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		ISysUser user = sysUserService.getById(userId);
		List<?extends ISysRole> roles = sysRoleService.getByUserId(userId);
		List<?extends IPosition> positions = positionService.getByUserId(userId);
		List<?extends ISysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
		// 获取可以管理的组织列表。
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);

		map.put("userId", userId);
		map.put("curOrgId", curOrgId);
		map.put("roles", roles);
		map.put("positions", positions);
		map.put("orgs", orgs);
		map.put("ownOrgs", ownOrgs);
		return map;
	}

	/**
	 * 获取管理的权限
	 * 
	 * @param type
	 * @param bpmDataTemplate
	 * @param roles
	 * @param positions
	 * @param orgs
	 * @param ownOrgs
	 * @param userId
	 * @return
	 */
	public Map<String, Boolean> getManagePermission(int type,
			String manageField, Map<String, Object> rightMap) {
		if (StringUtils.isEmpty(manageField))
			return null;
		JSONArray jsonAry = JSONArray.fromObject(manageField);
		Map<String, Boolean> map = getPermissionMap(type, jsonAry, rightMap);
		return map;
	}

	/**
	 * 获取字段的权限
	 * 
	 * @param userId
	 * @param type
	 * @param bpmDataTemplate
	 * @return
	 */
	public Map<String, Boolean> getPermission(int type, String displayField,
			Map<String, Object> rightMap) {
		JSONArray jsonAry = JSONArray.fromObject(displayField);
		return getPermissionMap(type, jsonAry, rightMap);
	}

	/**
	 * 获取权限map
	 * 
	 * @param jsonAry
	 * @param type
	 * @param rightMap
	 * @return
	 */
	private Map<String, Boolean> getPermissionMap(int type, JSONArray jsonAry,
			Map<String, Object> rightMap) {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		if (JSONUtil.isEmpty(jsonAry))
			return map;
		int i = 0;
		for (Object obj : jsonAry) {
			JSONObject json = JSONObject.fromObject(obj);
			String name = (String) json.get("name");
			// 取得控制类型
			String controltype = (String) json.get("controltype");
			JSONArray rights = (JSONArray) json.get("right");
			for (Object right : rights) {
				JSONObject rightJson = JSONObject.fromObject(right);
				Integer s = (Integer) rightJson.get("s");
				if (s.intValue() == type){
					//判断控制类型'4': 人员选择器(单选)、'17': 角色选择器(单选)、'18': 组织选择器(单选)、'19': 岗位选择器(单选)
					if(controltype!=null&&("4".equals(controltype)||"17".equals(controltype)||"18".equals(controltype)||"19".equals(controltype))){
						map.put(name+"ID", this.hasRight(rightJson, rightMap));
					}
					//业务表字段的权限 和 表单按钮的权限，controltype为null的时候表示表单按钮的权限
                    if(controltype==null){
                        //页面按钮类型可以多个连续添加，权限不能按照按钮类型来控制，目前只能加index 游标来控制每一个按钮的权限
                        map.put(i+"_"+name, this.hasRight(rightJson, rightMap));
                    }
					map.put(name, this.hasRight(rightJson, rightMap));
				}
			}
			i++;
		}
		return map;
	}

	/**
	 * 判断是否有权限。
	 * 
	 * @param permission
	 * @param rightMap
	 *            权限map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean hasRight(JSONObject permission, Map<String, Object> rightMap) {
		String type = permission.get("type").toString();
		String id = permission.get("id").toString();
		Object script = permission.get("script");
		return hasRight(rightMap, type, id, script);
	}
	
    /**
     * 判断是否有权限。
     * 
     * @param permission
     * @param rightMap
     *            权限map
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean hasRight(com.alibaba.fastjson.JSONObject permission, Map<String, Object> rightMap) {
        String type = permission.get("type").toString();
        String id = permission.get("id").toString();
        Object script = permission.get("script");
        return hasRight(rightMap, type, id, script);
    }
    
    private boolean hasRight(Map<String, Object> rightMap, String type, String id, Object script)
    {
        boolean hasRight=false;
        if ("none".equals(type)) // 无
			return false;

		if ("everyone".equals(type))// 所有人
			return true;

		Long userId = (Long) rightMap.get("userId");
		// Long curOrgId = (Long) rightMap.get("curOrgId");
		List<ISysRole> roles = (List<ISysRole>) rightMap.get("roles");
		List<?extends IPosition> positions = (List<?extends IPosition>) rightMap.get("positions");
		List<ISysOrg> orgs = (List<ISysOrg>) rightMap.get("orgs");
		List<ISysOrg> ownOrgs = (List<ISysOrg>) rightMap.get("ownOrgs");
		// 指定用户
		if ("user".equals(type)) {
			hasRight = StringUtil.contain(id, userId.toString());
			return hasRight;
		}
		// 指定角色
		else if ("role".equals(type)) {
			for (ISysRole role : roles) {
				if (StringUtil.contain(id, role.getRoleId().toString())) {
					return true;
				}
			}
		}
		// 指定组织
		else if ("org".equals(type)) {
			for (ISysOrg org : orgs) {
				if (StringUtil.contain(id, org.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 组织负责人
		else if ("orgMgr".equals(type)) {
			for (ISysOrg ownOrg : ownOrgs) {
				if (StringUtil.contain(id, ownOrg.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 岗位
		else if ("pos".equals(type)) {
			for (IPosition position : positions) {
				if (StringUtil.contain(id, position.getPosId().toString())) {
					return true;
				}
			}
		} else if ("script".equals(type)) {
			if (BeanUtils.isEmpty(script))
				return false;
			Map<String, Object> map = new HashMap<String, Object>();
			CommonVar.setCurrentVars(map);
			return groovyScriptEngine.executeBoolean(script.toString(), map);
		}
		return false;
    }

	private String addParametersToUrl(String url, Map<String, Object> params,
			List<String> excludes) {
		StringBuffer sb = new StringBuffer();
		int idx1 = url.indexOf("?");
		if (idx1 > 0) {
			sb.append(url.substring(0, idx1));
		} else {
			sb.append(url);
		}
		sb.append("?");

		Map<String, Object> map = getQueryStringMap(url);
		if (BeanUtils.isNotEmpty(params)) {
			map.putAll(params);
		}
		// 排除
		if (excludes != null) {
			for (String ex : excludes) {
				map.remove(ex);
			}
		}

		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object val = entry.getValue();
			if (BeanUtils.isEmpty(val))
				continue;
			sb.append(key);
			sb.append("=");
			sb.append(val);
			sb.append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 获得查询的Map
	 * 
	 * @param url
	 * @return
	 */
	private static Map<String, Object> getQueryStringMap(String url) {
		Map<String, Object> map = new HashMap<String, Object>();
		int idx1 = url.indexOf("?");
		if (idx1 > 0) {
			String queryStr = url.substring(idx1 + 1);
			String[] queryNodeAry = queryStr.split("&");
			for (String queryNode : queryNodeAry) {
				String[] strAry = queryNode.split("=");
				if (strAry.length == 1) {
					map.put(strAry[0], null);
				} else {
					map.put(strAry[0].trim(), strAry[1]);
				}
			}
		}
		return map;
	}

	/**
	 * 取得数据 (分页后的数据)包含构建查询sql和查询数据
	 * 
	 * @param bpmDataTemplate
	 * @param bpmFormTable
	 * @param rightMap
	 *            权限数据
	 * @param params
	 *            页面传过来的参数
	 * @param sortMap
	 *            排序map
	 * @param formatData
	 *            格式化的数据
	 * @param filterJson
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DataTemplate getData(DataTemplate bpmDataTemplate,
			FormTable bpmFormTable, Map<String, Object> rightMap,
			Map<String, Object> params, Map<String, String> sortMap,
			Map<String, Object> formatData, JSONObject filterJson)
			throws Exception {

		JdbcHelper jdbcHelper = this.getJdbcHelper(bpmFormTable);

		String tableIdCode = "";
		if (params.get("__tic") != null) {
			tableIdCode = (String) params.get("__tic");
		}
		// 查询出来的数据列表(列表中为map对象)
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String sql = "";
		// 是否需要分页。
		if (bpmDataTemplate.getNeedPage().shortValue() == 1) {
			int currentPage = 1;
			Object pageObj = params.get(tableIdCode + DataTemplate.PAGE);
			if (pageObj != null) {
				currentPage = Integer.parseInt(pageObj.toString());
			}
			int pageSize = bpmDataTemplate.getPageSize();
			Object pageSizeObj = params
					.get(tableIdCode + DataTemplate.PAGESIZE);
			if (pageSizeObj != null) {
				pageSize = Integer.parseInt(params.get(
						tableIdCode + DataTemplate.PAGESIZE).toString());
			}

			// 获取拼接的SQL
			sql = this.getSQL(filterJson, bpmDataTemplate, bpmFormTable, rightMap, params, sortMap);
			//查看当前使用了哪张表(此代码不应上传) by zmz
			
//			String dbTableName=sql.substring(sql.lastIndexOf("from")+5,sql.lastIndexOf("from")+12);
//			System.out.println("当前访问了表:"+dbTableName);
			//调用项目中的实现，没有则跳过
			ICustomService customService=(ICustomService)AppUtil.getProjectBean(ICustomService.class);
			if(BeanUtils.isNotEmpty(customService)){
				String customSql = customService.getCustomDataTemplateSql(jdbcHelper,bpmDataTemplate, bpmFormTable, params, sql);
				if(BeanUtils.isNotEmpty(customSql)){
					sql = customSql;
				}
			}
			Object oldPageSizeObj = params.get(tableIdCode + "oz");
			int oldPageSize = bpmDataTemplate.getPageSize();
			if (oldPageSizeObj != null) {
				oldPageSize = Integer.parseInt(params.get(tableIdCode + "oz")
						.toString());
			}
			if (pageSize != oldPageSize) {
				int first = PageUtils.getFirstNumber(currentPage, oldPageSize);
				currentPage = first / pageSize + 1;
			}
			PagingBean pageBean = new PagingBean(currentPage, pageSize);
			// 参数
			// params.putAll();
			// 查询出来的数据列表(列表中为map对象)
			list = jdbcHelper.getPage(currentPage, pageSize, sql, params,
					pageBean);
			bpmDataTemplate.setPageBean(pageBean);
		} else {
			// 获取数据
			sql = this.getSQL(filterJson, bpmDataTemplate, bpmFormTable,
					rightMap, params, sortMap);
			// 查询出来的数据列表(列表中为map对象)
			list = jdbcHelper.queryForList(sql, params);
		}
		// 获取整理过的数据(包含数据格式转换)
		if (BeanUtils.isNotEmpty(list)) {
			list = getDataList(list, bpmFormTable, formatData, true);

		}
		bpmDataTemplate.setList(list);

		logger.info("查询的SQL：" + sql);
		return bpmDataTemplate;
	}

	public JSONObject getFilterFieldJson(DataTemplate bpmDataTemplate,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject filterJson = getFilterFieldJson(
				bpmDataTemplate.getFilterField(), rightMap, params);
		if (JSONUtil.isEmpty(filterJson)
				&& bpmDataTemplate.getIsFilter().shortValue() == 0) {
			JSONArray jsonAry = new JSONArray();
			jsonAry.add(getDefaultFilterJson());
			bpmDataTemplate.setFilterField(jsonAry.toString());
		}
		return filterJson;
	}

	/**
	 * 获取过滤字段的JSON 如果没有值取默认
	 * 
	 * @param filterField
	 * @param isFilter
	 * @param rightMap
	 * @param params
	 * @return
	 */
	private JSONObject getFilterFieldJson(String filterField,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject filterJson = this.getFilterJson(filterField, rightMap,
				params);

		return filterJson;
	}

	/**
	 * 默认的条件
	 * 
	 * @return
	 */
	private JSONObject getDefaultFilterJson() {
		return JSONObject
				.fromObject("{\"name\":\""
						+ getText("service.bpmDataTemplate.getDefaultFilterJson.name")
						+ "\",\"key\":\"Default\",\"type\":\"1\",\"condition\":\"[]\",\"right\":[{\"s\":3,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}");
	}

	/**
	 * 获取整理过的数据(包含数据格式转换)
	 * 
	 * @param list
	 *            //查询出来的数据列表(列表中为map对象)
	 * @param bpmFormTable
	 * @param formatData
	 * @param isMain
	 * @return
	 */
	public List<Map<String, Object>> getDataList(
			List<Map<String, Object>> list, FormTable bpmFormTable,
			Map<String, Object> formatData, boolean isMain) {
		// 判断数据源
		String source = bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL ? DataTemplate.SOURCE_CUSTOM_TABLE
				: DataTemplate.SOURCE_OTHER_TABLE;
		// 获取主键Field
		String pkField = bpmFormTable.getPkField();
		// 获取外键列
		String relation = StringUtils.isEmpty(bpmFormTable.getRelation()) ? TableModel.FK_COLUMN_NAME
				: bpmFormTable.getRelation();
		// 获取主表的列信息list
		List<FormField> fieldList = bpmFormTable.getFieldList();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		/*
		 * 获取当前表的所有外键列，并构建Map 此代码不能放在循环里面，否则影响执行效率
		 * 不需要构建map，直接通过配置的json数据作为参数传递
		 * Map<String, Map<String, Object>> fkColumnsMap = bpmFormTable
				.genFKColumnShowRelFormDialogMap();*/
		try {
			
			// for循环,遍历数据列表(列表中为map对象(column_name,column_value))
			for (Map<String, Object> map : list) {
				Map<String, Object> mapData = new HashMap<String, Object>();
				for (FormField bpmFormField : fieldList) {
					// 列名
					String name = bpmFormField.getFieldName();
					// 大写的列名
					String nameUp = bpmFormField.getFieldName().toUpperCase();
					// 控件类型
					Short controlType = bpmFormField.getControlType();
					// 外键关联表id
					//Long relTableId = bpmFormField.getRelTableId();
					// 数字型字段属性（千分位、货币类型、小数点后位数）
					Map<String,String> paraMap = bpmFormField.getPropertyMap();
					// 修正字段名 加F_
					String fixFieldName = this.fixFieldName(nameUp, source);
					Object o = null;
					if (map.containsKey(fixFieldName)) {
						// 判断是否为外键列
						if (controlType.shortValue() == IFieldPool.RELATION_COLUMN_CONTROL) {
						    //logger.info(bpmFormField.getRelFormDialog());
							// 获取外键列的显示值
						    o = getFormatFkData(map,fixFieldName, bpmFormField);
						} else {
							// 获取数据格式化的值
							o = getFormatData(map.get(fixFieldName), name,
									formatData, controlType,paraMap,bpmFormField);
							
						}
						mapData.put("show_"+name, o);
						//字符串截取
						o = StringUtil.contentCut(o, 30);
						mapData.put(name, o);

					}
					
				}
				// 加入主键
				mapData.put(pkField, map.get(pkField));
				// 加入外键
				if (!isMain)
					mapData.put(relation, map.get(relation));

				dataList.add(mapData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}


    /**
     * @param dataMap
     * @param bpmFormField
     * @return
     */
    public Object getFormatFkData(Map<String, Object> dataMap, String fkColumn, FormField formField)
    {
        
        try
        {
            String relJsonStr = CommonTools.stripCData(formField.getRelFormDialog());
            if (StringUtil.isNotEmpty(relJsonStr))
            {
                // 获取对话框数据需要的参数
                Map<String, Object> param = new HashMap<String, Object>();
                // 对话框返回的数据结果
                Map<String, Object> result = new HashMap<String, Object>();
                // 方法返回的数据
                StringBuffer resultStr = new StringBuffer();
                // 关联关系配置json格式数据
                JSONObject relJson = JSONObject.fromObject(relJsonStr);
                // 远程rpc配置
                String rpcrefname = (String)relJson.get(IFieldPool.rpcrefname);
                // 对话框配置
                String dialogAlias = (String)relJson.get("name");
                // 字段映射配置
                JSONArray fields = relJson.getJSONArray("fields");
                // 当前表关联关系外键值
                if(dataMap.get(fkColumn)==null){
                    return null;
                }
                String fkVal = dataMap.get(fkColumn).toString();
                
                // 获取对话框和当前表关联字段对应的字段。
                String dialogKey = "";
                boolean isGetDialog = false;
                for (Object field : fields)
                {
                    JSONObject f = (JSONObject)field;
                    String target = f.getString(IFieldPool.FK_TABLEFIELD);
                    if (target.indexOf(IFieldPool.FK_SHOWAppCode) > -1)
                    {
                        isGetDialog = true;
                    }
                    if (fkColumn.equalsIgnoreCase(ITableModel.CUSTOMER_COLUMN_PREFIX + target.toUpperCase()))
                    {
                        dialogKey = (String)f.get(IFieldPool.FK_DIALOGFIELD);
                    }
                    
                }
                // 放置参数数据
                param.put(dialogKey, fkVal);
                if (isGetDialog)
                {
                    result = getDialogData(param, rpcrefname, dialogAlias);
                }
                for (Object field : fields)
                {
                    JSONObject f = (JSONObject)field;
                    String target = f.getString(IFieldPool.FK_TABLEFIELD);
                    //外键字段值永远不需要从 对话框中远程获取数据
                    if (!target.equals(formField.getFieldName()))
                    {
                        //如果是虚拟字段需要从对话框中远程获取数据
                        if (target.indexOf(IFieldPool.FK_SHOWAppCode) > -1&&BeanUtils.isNotEmpty(result))
                        {
                            //从对话框中获取数据
                            dialogKey = f.getString(IFieldPool.FK_DIALOGFIELD);
                            resultStr.append(result.get(dialogKey)).append("-");
                            //dataMap.put(formField.getFieldName(), result.get(dialogKey));
                        }
                       
                    }
                }
                /*Map<String,Object> result_=new HashMap<String,Object>();
                for(String key:dataMap.keySet()){
                    result_.put(key.replace(ITableModel.CUSTOMER_COLUMN_PREFIX, ""), dataMap.get(key));
                }
                return result_;*/
                if(resultStr.length()>0){
                    return resultStr.substring(0, resultStr.length()-1);
                }else{
                    return "";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> getDialogData(Map<String, Object> param, String rpcrefname, String dialogAlias)
        throws Exception
    {
        Map<String, Object> result;
        // 判断是否有rpc远程接口
        if (StringUtil.isNotEmpty(rpcrefname))
        {
            // 采用IOC方式，根据rpc远程接口调用数据
            CommonService commonService = (CommonService)AppUtil.getContext().getBean(rpcrefname);
            // 必须从paramsMap,移除非数据库字段参数，否则查询数据库会报错。
            result=commonService.getFKColumnShowData(dialogAlias, param);
        }
        else
        {
            // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
            result=formDialogService.getFKColumnShowData(dialogAlias,param);

        }
        return result;
    }

	public List<Map<String, Object>> getDataListForDialog(
			List<Map<String, Object>> list, FormTable bpmFormTable,
			Map<String, Object> formatData, boolean isMain) {
		String source = bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL ? DataTemplate.SOURCE_CUSTOM_TABLE
				: DataTemplate.SOURCE_OTHER_TABLE;
		String pkField = bpmFormTable.getPkField();
		String relation = StringUtils.isEmpty(bpmFormTable.getRelation()) ? TableModel.FK_COLUMN_NAME
				: bpmFormTable.getRelation();
		List<FormField> fieldList = bpmFormTable.getFieldList();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> mapData = new HashMap<String, Object>();
			for (FormField bpmFormField : fieldList) {
				String name = bpmFormField.getFieldName();
				//字段显示为默认状态，不转换为大写  by liubo
				//String nameUp = bpmFormField.getFieldName().toUpperCase();
				String fixFieldName = this.fixFieldName(name, source);
				// 控件类型
				Short controlType = bpmFormField.getControlType();
				Object o = null;
				// 数字型字段属性（千分位、货币类型、小数点后位数）
				Map<String,String> paraMap = bpmFormField.getPropertyMap();
				
				if (map.containsKey(fixFieldName))
					o = getFormatData(map.get(fixFieldName), name, formatData,
							controlType,paraMap,bpmFormField);
				mapData.put(fixFieldName, o);
			}
			// 加入主键
			mapData.put(pkField, map.get(pkField));
			// 加入外键
			if (!isMain)
				mapData.put(relation, map.get(relation));

			dataList.add(mapData);
		}
		return dataList;
	}

	/**
	 * 获取格式化自定义对话框数据（按输入的map顺序）
	 * @author liubo
	 * @date 2017年9月14日上午10:56:03
	 * @param list
	 * @param bpmFormTable
	 * @param formatData
	 * @param isMain
	 * @return
	 */
	public List<Map<String, Object>> getOrderDataListForDialog(
			List<Map<String, Object>> list, FormTable bpmFormTable,
			Map<String, Object> formatData, boolean isMain) {
		String source = bpmFormTable.getIsExternal() == FormTable.NOT_EXTERNAL ? DataTemplate.SOURCE_CUSTOM_TABLE
				: DataTemplate.SOURCE_OTHER_TABLE;
		List<FormField> fieldList = bpmFormTable.getFieldList();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> mapData = map;
			for (FormField bpmFormField : fieldList) {
				String name = bpmFormField.getFieldName();
				//字段显示为默认状态，不转换为大写  by liubo
				//String nameUp = bpmFormField.getFieldName().toUpperCase();
				String fixFieldName = this.fixFieldName(name, source);
				// 控件类型
				Short controlType = bpmFormField.getControlType();
				Object o = null;
				// 数字型字段属性（千分位、货币类型、小数点后位数）
				Map<String,String> paraMap = bpmFormField.getPropertyMap();
				
				if (map.containsKey(fixFieldName))
					o = getFormatData(map.get(fixFieldName), name, formatData,
							controlType,paraMap,bpmFormField);
				mapData.put(fixFieldName, o);
			}
			dataList.add(mapData);
		}
		return dataList;
	}
	
	/**
	 * 获取数据格式化的值
	 * 
	 * @param o
	 * @param name
	 * @param parse
	 * @return
	 */
	private Object getFormatData(Object o, String name,
			Map<String, Object> formatData, Short controlType,Map<String,String> paraMap,FormField formField) {
		if (BeanUtils.isEmpty(o)){
				return "";
		}
		// 如果是日期的
		if (o instanceof Date) {
			String style = StringPool.DATE_FORMAT_DATE;
			if (formatData.containsKey(name)) {
				Object format = formatData.get(name);
				if (BeanUtils.isNotEmpty(format))
					style = (String) format;
			}
			o = DateFormatUtil.format((Date) o, style);
		}
		// 如果是 下拉框，单选框，复选框，数据字典的
		else if (formatData.containsKey(name)) {
			Object obj = formatData.get(name);
			if (BeanUtils.isNotEmpty(obj)) {
				if (obj instanceof Map) {
					Map<?, ?> map = (Map<?, ?>) obj;
					//复选框的情况
					if(controlType!=null && (controlType.shortValue() == IFieldPool.CHECKBOX)){
						String[] oArrays = o.toString().split(",");
						o = "";
						for(String oArray:oArrays){
							o = o+","+map.get(oArray);
						}
						if(o.toString().length()>0){
							o = o.toString().substring(1);
						}
					}else{
						//下拉框，单选框，密级管理
						//需要对o进行转化为string，否则如果key为数字的情况，将会出现无法取到value的情况。
						 o = map.get(String.valueOf(o));
					}
					if (BeanUtils.isEmpty(o)){
						o = "";
					}
				}
				//数据字典
				if(controlType.equals(IFieldPool.DICTIONARY)){
					o = FormDataUtil.getDicNameByValue(o,formField.getDictType());
				}
			}

		}
		// 数字型字段属性（千分位、货币类型、小数点后位数）
		else if(o instanceof Number){								
				Object isShowComdify= paraMap.get("isShowComdify");
				Object decimalValue= paraMap.get("decimalValue");
				Object coinValue= paraMap.get("coinValue");
				
				o =StringUtil.getNumber(o,isShowComdify,decimalValue,coinValue);
		}else{
			if(controlType.equals(IFieldPool.ATTACHEMENT)){
				JSONArray json=JSONArray.fromObject(o);
				List<Map<String,String>> list1=(List)json;
				String divStr="";
				int size = list1.size();
				int n = 0;
				for(Map<String,String> map:list1){
					n++;
					for(String key:map.keySet()){
						if(key.equals("id")){
							//divStr+="<a name= \"fjzd"+"\"href="+'"'+"${ctx}/oa/system/sysFile/download.do?fileId="+map.get(key)+'"';	
							//divStr+="<a href="+'"'+"####"+'"'+" onclick="+'"'+"javascript:downloadFile("+map.get(key)+")"+'"'; //下载附件
							divStr+="<a href='####' onclick='javascript:downloadFile("+map.get(key)+")'"; //下载附件
						}
						if(key.equals("name")){
							if(size == n){
								divStr+=">"+map.get(key)+"</a>";	
							}else {
								divStr+=">"+map.get(key)+",\t</a>";	
							}
						}
					}
				}
				o=divStr;
				
			}
		}
		
		if(StringUtil.isNotEmpty(formField.getEncrypt())){
            FiledEncryptFactory filedEncryptFactory=AppUtil.getBean(FiledEncryptFactory.class);
            IFiledEncrypt filedEncrypt=filedEncryptFactory.creatEncrypt(formField.getEncrypt());
            return filedEncrypt.decrypt(o.toString());
		}else{
		    return o.toString();
		}
		
	}

	/**
	 * 通过自定义表获取数据源
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public JdbcHelper getJdbcHelper(IFormTable bpmFormTable) throws Exception {
		JdbcHelper jdbcHelper = null;
		// 外部数据源
		if (bpmFormTable.isExtTable()) {
			jdbcHelper = this.getJdbcHelper(bpmFormTable.getDsAlias());
		} else {
			jdbcHelper = this.getConfigJdbcHelper();
		}
		return jdbcHelper;
	}

	/**
	 * 从配置文件中读取配置属性数据的信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private JdbcHelper getConfigJdbcHelper() throws Exception {
		String dbType = AppConfigUtil.get("jdbc.dbType");
		String className = AppConfigUtil.get("jdbc.driverClassName");
		String url = AppConfigUtil.get("jdbc.url");
		String user = AppConfigUtil.get("jdbc.username");
		String pwd = AppConfigUtil.get("jdbc.password");
		return JdbcHelper
				.getJdbcHelper(user, className, url, user, pwd, dbType);
	}

	/**
	 * 根据数据源获取JdbcHelper。
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private JdbcHelper getJdbcHelper(String dsAlias) throws Exception {
		if ("LOCAL".equalsIgnoreCase(dsAlias))// 如果别名是LOCAL则是本地数据源
			return getConfigJdbcHelper();

		DriverManagerDataSource dataSource = sysDataSourceService
				.getDriverMangerDataSourceByAlias(dsAlias);
		JdbcHelper<?, ?> jdbcHelper = JdbcHelper.getInstance();
		String className = dataSource.getConnectionProperties().getProperty(
				"driverClassName");

		jdbcHelper.init(dsAlias, className, dataSource.getUrl(),
				dataSource.getUsername(), dataSource.getPassword());
		jdbcHelper.setCurrentDb(dsAlias);
		jdbcHelper.setDialect(DialectFactoryBean
				.getDialectByDriverClassName(className));
		return jdbcHelper;
	}

	/**
	 * TODO 获取拼接的SQL
	 * 
	 * @param filterJson
	 * 
	 * @param bpmDataTemplate
	 * @param bpmFormTable
	 * @param rightMap
	 * @param params
	 * @param sortMap
	 * @return
	 */
	private String getSQL(JSONObject filterJson, DataTemplate bpmDataTemplate,
			FormTable bpmFormTable, Map<String, Object> rightMap,
			Map<String, Object> params, Map<String, String> sortMap) {
		if (JSONUtil.isNotEmpty(filterJson)) {
			String type = (String) filterJson.get("type");
			if ("2".equals(type)) {
				String condition = (String) filterJson.get("condition");
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("map", params);

				// 获取表单上的查询
				Map<String, FormField> bpmFormFieldMap = this
						.getFormFieldMap(bpmFormTable);
				String tableName = bpmFormTable.getTableName();
				// 根据查询条件列表，计算Where SQL 语句
				String where = this.getQuerySQL(bpmDataTemplate, "", tableName,
						params, bpmFormFieldMap);
				Map<String, Object> whereMap = new HashMap();
				whereMap.put("where", where);
				paramsMap.put("whereMap", whereMap);

				StringBuffer sql = new StringBuffer(
						groovyScriptEngine.executeString(condition, paramsMap));
				String pkField = bpmFormTable.getPkField();
				String source = bpmDataTemplate.getSource();

				return this.getOrderBySql(sql, pkField,
						bpmDataTemplate.getSortField(), sortMap, source);
			}
		}

		// 以下是拼接SQL的
		String source = bpmDataTemplate.getSource();
		String tableName = bpmFormTable.getTableName();
		String table = tableName.toLowerCase();
		Map<String, String> tableMap = new HashMap<String, String>();
		tableMap.put(table, fixTableName(tableName, source));
		StringBuffer sql = new StringBuffer("SELECT ");
		// 主从表关联关系
		Map<String, Map<String, String>> relationMap = new HashMap<String, Map<String, String>>();

		Map<String, FormField> bpmFormFieldMap = this
				.getFormFieldMap(bpmFormTable);

		String pkField = bpmFormTable.getPkField();
		// where sql
		String where = "";

		// 过滤条件 sql
		where = this.getFilterSQL(filterJson, tableMap, relationMap);

		// 查询条件 sql
		where = this.getQuerySQL(bpmDataTemplate, where, tableName, params,
				bpmFormFieldMap);
		
		// 列值过滤条件 sql
		where = this.getLineValueFilterSQL(bpmDataTemplate, where, tableName, params,
						bpmFormFieldMap);
		// 高级查询sql
		where = this.getAdvancedQuerySQL(where, tableMap, relationMap,params);
		// 表名的SQL
		sql.append(this.getFromTableSQL(pkField, bpmDataTemplate, tableName,
				tableMap, rightMap));

		if (StringUtils.isNotEmpty(where)) {
			// 多表之间的关联SQL
			sql.append(" WHERE ")
					.append(getTableWhere(table, pkField, where, tableMap,
							relationMap));
		} else {
			Set<Entry<String, String>> set = tableMap.entrySet();
			if (set.size() > 1) {
				sql.append(" WHERE ").append(
						getTableWhere(table, pkField, "1=1", tableMap,
								relationMap));
			}
		}

		// By weilei:添加自定义查询SQL
		sql.append(addConditionSQL(params, sql.toString()));

		// By weilei：针对DBom数据查询添加过滤条件
		String dbomSql = CommonTools.Obj2String(params.get("__dbomSql__"));
		dbomSql = "".equals(dbomSql) ? "" : dbomSql.replace("<DBom>", "'");
		if (!"".equals(dbomSql)) {
			if (!sql.toString().toUpperCase().contains("WHERE")) {
				sql.append(" WHERE " + dbomSql);
			} else {
				sql.append(" AND " + dbomSql);
			}
		}

		// order sql 排序
		return this.getOrderBySql(sql, pkField, bpmDataTemplate.getSortField(),
				sortMap, source);

	}
	public String getAdvancedQuerySQL(String where,Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap,Map<String, Object> params){
		if(params.containsKey("queryKey")&&params.containsKey("__displayId__")){
			String queryKey = (String)params.get("queryKey");;
			String displayId = String.valueOf(params.get("__displayId__"));
			IAdvancedQuery aq  = advancedQueryService.getAdvancedQuery(displayId, queryKey);
			JSONArray condition = aq.getConditionJSON();
			if(condition==null){
				return where;
			}
			List<Map<String, Object>> operatorList = new ArrayList<Map<String, Object>>();
			// 转换成SQL
			List<FilterJsonStruct> filters = com.alibaba.fastjson.JSONArray
					.parseArray(condition.toString(),FilterJsonStruct.class);
			this.getFilterResult(filters, tableMap, relationMap, operatorList);
			String sql = this.executeOperator(operatorList);
			sql = sql.equals("")?"":" and "+sql;
			String and = where.equals("")?"":" and ";
			where += and + "(1=1"+ sql + ")";
			return where;
		}else{	
			return where;
		}		
	}
	/**
	 * 1、multi： 可多选的情况（ 下拉选项等）；
	 * 2、lr：左右模糊查询（文本类型、大文本等）；
	 * 3、numberRange：数字范围查询；
	 * 4、dateRange：日期范围查询；
	 * @return ： 列值过滤sql
	 */
	public String getLineValueFilterSQL(DataTemplate bpmDataTemplate, String where,
			String tableName, Map<String, Object> params,
			Map<String, FormField> bpmFormFieldMap){
		String source  = bpmDataTemplate.getSource();
		StringBuffer sb = new StringBuffer();
		String and = StringUtils.isEmpty(where) ? "" : " AND ";
		if(BeanUtils.isEmpty(params)){
			return where;
		}
		Iterator  it = params.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			if(key.contains("LVF_")){
				String type = key.substring(key.lastIndexOf("_")+1);
				String fieldName= key.substring(key.indexOf("LVF_")+4,key.lastIndexOf("_"));
				fieldName = this.fixFieldName(fieldName, source);
				String value = (String)params.get(key);
				switch(type){
				case "multi":
					if(value.startsWith(",")){
						value = value.substring(1);
					}
					if(value.endsWith(",")){
						value = value.substring(0,value.length()-1);
					}
					value = "'"+value.replaceAll(",", "','")+"'";
					sb.append(" and ").append(" (").append(fieldName)
						.append(" in (").append(value).append(") ");
					if(value.contains("start")){
						sb.append(" or ").append(fieldName).append(" is null )");
					}else{
						sb.append(" ) ");
					}
					break;
				case "lr":
					sb.append(" and ").append(" ").append(fieldName)
						.append(" like '%").append(value).append("%' ");
					break;
				case "numberRange":
					if(value.contains(",")){
						String  startNum = value.split(",")[0];
						String  endNum = value.split(",")[1];
						Long sNum =-99999999999999L,eNum =99999999999999L;
						sNum = StringUtil.isNumeric(startNum)?Long.valueOf(startNum):sNum;
						eNum = StringUtil.isNumeric(endNum)?Long.valueOf(endNum):eNum;
						sb.append(" and ").append(" ").append(fieldName);
						sb.append(" between ").append(sNum).append(" and ").append(eNum);
					}
					break;
				case "dateRange":
					if(value.contains(",")&&value.split(",").length==3){
						String  startTime = value.split(",")[0];
						String  endTime = value.split(",")[1];
						String  format = value.split(",")[2];
						
						startTime = StringUtil.isFormatDate(startTime, format)?startTime:"";
						endTime = StringUtil.isFormatDate(endTime, format)?endTime:"";						
						if(!startTime.equals("")){
							sb.append(" and ").append(" ")
							.append(fieldName).append(">=").append("'"+startTime+"'");
						}
						if(!endTime.equals("")){
							sb.append(" and ")
							.append(fieldName).append("<=").append("'"+endTime+"'");
						}
					}
					break;
				}
			}
		}
		if (sb.length() > 0)
			where += and + " (1=1 " + sb.toString() + ") ";
		return where;
	}
	/**
	 * 添加自定义查询SQL.
	 * <p>
	 * 使用方法如下:
	 * </p>
	 * <p>
	 * url:
	 * "/oa/form/dataTemplate/preview.do?__displayId__=10000005750009&__key__=F_FK_PLAN_ID&__value__=10000005750009"
	 * </p>
	 * 
	 * @author [创建人] WeiLei <br/>
	 *         [创建时间] 2016-7-23 上午09:29:17 <br/>
	 *         [修改人] WeiLei <br/>
	 *         [修改时间] 2016-7-23 上午09:29:17
	 * @param params
	 * @param querySQL
	 * @return
	 * @see
	 */
	public String addConditionSQL(Map<String, Object> params, String querySQL) {

		String whereSql = "";
		String key = CommonTools.Obj2String(params.get("__key__"));
		String value = CommonTools.Obj2String(params.get("__value__"));

		if (!"".equals(key)) {
			String[] values = value.split(",");
			String strwhere = "(";
			for (String v : values) {
				strwhere += " " + key + "='" + v + "' OR";
			}
			strwhere = strwhere.length() > 1 ? strwhere.substring(0,
					strwhere.length() - 2)
					+ ")" : "";
			if (!querySQL.toUpperCase().contains("WHERE")
					&& !"".equals(strwhere)) {
				whereSql += " WHERE " + strwhere;
			} else {
				whereSql += strwhere;
			}
		}
		return whereSql;
	}

	private Map<String, FormField> getFormFieldMap(FormTable bpmFormTable) {
		Map<String, FormField> map = new HashMap<String, FormField>();
		List<FormField> fieldList = bpmFormTable.getFieldList();
		if (BeanUtils.isEmpty(fieldList))
			return map;
		for (FormField bpmFormField : fieldList) {
			String name = bpmFormField.getFieldName();
			map.put(name, bpmFormField);
		}
		return map;
	}

	/**
	 * 获取表之间的关系
	 * 
	 * @param where
	 * @param table
	 * @param tableMap
	 * @param relationMap
	 * @return
	 */
	private String getTableWhere(String table, String pk, String where,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap) {
		Set<Entry<String, String>> set = tableMap.entrySet();
		// 只有一个表
		if (set.size() == 1) {
			return where;
		} else {
			StringBuffer sb = new StringBuffer();
			// 从表与主表的关系
			for (Iterator<Entry<String, Map<String, String>>> it = relationMap
					.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Map<String, String>> e = (Map.Entry<String, Map<String, String>>) it
						.next();
				String key = e.getKey();
				String fk = e.getValue().get(table);
				if (StringUtils.isNotEmpty(fk)) {
					String mainKey = table + "." + pk;
					String subKey = key + "." + fk;
					sb.append(" AND ").append(mainKey).append("=")
							.append(subKey);
				}
			}
			where = where + sb.toString();
		}
		return where;
	}

	/**
	 * 获取SQL的表
	 * 
	 * @param pkField
	 * 
	 * @param bpmDataTemplate
	 * @param tableName
	 * @param map
	 * @param rightMap
	 * @return
	 */
	public String getFromTableSQL(String pkField,
			DataTemplate bpmDataTemplate, String tableName,
			Map<String, String> tableMap, Map<String, Object> rightMap) {
		// 拼接Select的SQL
		StringBuffer sb = new StringBuffer();
		String c = ",";
		String displayField = bpmDataTemplate.getDisplayField();// 获取业务数据模板中配置的显示列
		if (StringUtils.isNotEmpty(displayField)) {
			sb.append(fixFieldName(pkField, DataTemplate.SOURCE_OTHER_TABLE,
					tableName)).append(c);// 修正字段名 加 F_
			Map<String, Boolean> map = this.getPermission(
					DataTemplate.RIGHT_TYPE_SHOW, displayField, rightMap);
			for (Iterator<Map.Entry<String, Boolean>> it = map.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, Boolean> e = (Map.Entry<String, Boolean>) it
						.next();
				String key = e.getKey();
				if (key.equalsIgnoreCase(pkField))
					continue;
				sb.append(
						this.fixFieldName(key, bpmDataTemplate.getSource(),
								tableName)).append(c);
			}
		} else {
			sb.append(tableName).append(".*").append(c);
		}

		// 拼接from后的SQL
		StringBuffer from = new StringBuffer();
		//拼接关联表leftSql语句
		String leftSql = "";
		for (Iterator<Entry<String, String>> it = tableMap.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> e = (Map.Entry<String, String>) it.next();
			String key = e.getKey();
			String val = e.getValue();
			if(IFormField.ISRELTABLE.equals(key)){
				//暂存 拼接关联表left join语句，多个用“ ”隔开
				leftSql = leftSql + " " + val;
			}else{
				//拼接主表，子表，ibms_bus_link表语句
				from.append(val).append(" ").append(key).append(" ").append(c);
			}
		}
		//若存在关联表left join语句就拼接在主表后面
		if(leftSql != ""){
			int n = from.indexOf(tableName)+tableName.length();
			from.replace(n,n+1,leftSql);
		}
		String s = sb.substring(0, sb.length() - 1);
		String s1 = from.substring(0, from.length() - 1);
		return s + " from " + s1;
	}

	/**
	 * 获取过滤的SQL
	 * 
	 * @param jsonObj
	 * 
	 * @param bpmDataTemplate
	 * @param tableMap
	 *            表对应的表Map<表，真实表名>
	 * @param rightMap
	 * @param params
	 * @param relationMap
	 * @return
	 */
	public String getFilterSQL(JSONObject filterJson,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap) {
		if (JSONUtil.isEmpty(filterJson)){
		    return "";
		}
		List<Map<String, Object>> operatorList = new ArrayList<Map<String, Object>>();
		// 转换成SQL
		List<FilterJsonStruct> filters = com.alibaba.fastjson.JSONArray
				.parseArray(filterJson.get("condition").toString(),
						FilterJsonStruct.class);
		this.getFilterResult(filters, tableMap, relationMap, operatorList);

		return this.executeOperator(operatorList);
	}

	/**
	 * 取出满足过滤条件的JSON对象
	 * 
	 * @param filterField
	 * @param rightMap
	 *            权限
	 * @param params
	 *            参数
	 * @return
	 */
	public JSONObject getFilterJson(String filterField,
			Map<String, Object> rightMap, Map<String, Object> params) {
		JSONObject jsonObj = null;
		if (StringUtils.isEmpty(filterField))
			return jsonObj;
		JSONArray jsonAry = JSONArray.fromObject(filterField);
		if (JSONUtil.isEmpty(jsonAry))
			return jsonObj;
		// 取得满足key的条件
		String filterKey = (String) params
				.get(DataTemplate.PARAMS_KEY_FILTERKEY);
		if (StringUtils.isEmpty(filterKey)) {
			jsonObj = jsonAry.getJSONObject(0);
		} else {
			for (Object obj : jsonAry) {
				JSONObject jObj = (JSONObject) obj;
				String key = (String) jObj.get("key");
				if (key.equals(filterKey)) {
					jsonObj = jObj;// 取出满足的Key
					break;
				}
			}
		}
		return jsonObj;
	}

	/**
	 * 过滤所有的条件
	 * 
	 * @param filters
	 * @param tableMap
	 * @param relationMap
	 * @param operatorList
	 */
	private void getFilterResult(List<FilterJsonStruct> filters,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap,
			List<Map<String, Object>> operatorList) {
		for (FilterJsonStruct filter : filters) {
			// 组合条件
			if (filter.getBranch()) {
				List<Map<String, Object>> branchResultList = new ArrayList<Map<String, Object>>();
				this.getFilterResult(filter.getSub(), tableMap, relationMap,
						branchResultList);
				String branchResult = this.executeOperator(branchResultList);
				Map<String, Object> resultMap = this.getResultMap(
						filter.getCompType(), branchResult);
				operatorList.add(resultMap);
			} else {
				if (filter.getRuleType().intValue() == 2) {// 脚本直接返回结果
					String script = filter.getScript();
					if (StringUtil.isNotEmpty(script)) {
						String tables = filter.getTables();
						//在脚本中存在业务数据关联表时的特殊处理
						if(script.indexOf("IBMS_BUS_LINK")!=-1){
							String tableName = "IBMS_BUS_LINK";
							String tableLow = tableName.toLowerCase();
							if (!tableMap.containsKey(tableLow)) {
								tableMap.put(tableLow,tableName);
							}
						}
						
						List<FilterJsonStruct> filterList = com.alibaba.fastjson.JSONArray
								.parseArray(tables, FilterJsonStruct.class);
						for (FilterJsonStruct filterJsonStruct : filterList) {
							this.setTableMap(filterJsonStruct, tableMap,
									relationMap);
						}

						Map<String, Object> resultMap = this.getResultMap(
								filter.getCompType(), script);
						operatorList.add(resultMap);
					}
				} else {
					this.getNormalFilterResult(filter, tableMap, relationMap,
							operatorList);
				}
			}
		}
	}

	private Map<String, Object> getResultMap(String operator, String result) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("operator", operator);
		resultMap.put("result", result);
		return resultMap;
	}

	/**
	 * 获取普通条件的结果
	 * 
	 * @param filter
	 * @param tableMap
	 * @param relationMap
	 * @param operatorList
	 */
	private void getNormalFilterResult(FilterJsonStruct filter,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap,
			List<Map<String, Object>> operatorList) {
		String flowvarKey = filter.getFlowvarKey();
		String table = filter.getTable();
		String source = filter.getSource();

		// 获取表的数据
		this.setTableMap(filter, tableMap, relationMap);

		String script = "";
		switch (filter.getOptType()) {
		case 1:// 数字
		case 2:// 字符串
			// 条件一
			if (StringUtils.isNotEmpty(filter.getJudgeVal1())) {
				script = this.getCompareScript(filter.getJudgeCon1(),
						flowvarKey, filter.getJudgeVal1(), filter.getOptType(),
						table, source, filter.getIsHidden());
			}
			// 条件二
			if (StringUtils.isNotEmpty(filter.getJudgeVal2())) {
				String moreScript = getCompareScript(filter.getJudgeCon2(),
						flowvarKey, filter.getJudgeVal2(), filter.getOptType(),
						table, source, filter.getIsHidden());
				if (StringUtils.isNotEmpty(script))
					script = script + DataTemplate.CONDITION_AND;
				script = script + moreScript;
			}
			break;
		case 3:// 日期
			// 条件一
			if (StringUtils.isNotEmpty(filter.getJudgeVal1())) {
				String val1 = this.sqlToDate(filter.getJudgeVal1(),
						filter.getDatefmt());
				script = this.getCompareScript(filter.getJudgeCon1(),
						flowvarKey, val1, filter.getOptType(), table, source,
						filter.getIsHidden());
			}
			// 条件二
			if (StringUtils.isNotEmpty(filter.getJudgeVal2())) {
				String val2 = this.sqlToDate(filter.getJudgeVal2(),
						filter.getDatefmt());
				String moreScript = getCompareScript(filter.getJudgeCon2(),
						flowvarKey, val2, filter.getOptType(), table, source,
						filter.getIsHidden());
				if (StringUtils.isNotEmpty(script))
					script = script + DataTemplate.CONDITION_AND;
				script = script + moreScript;
			}
			break;
		case 4:// 字典
			String[] vals = filter.getJudgeVal1().split("&&");
			for (String val : vals) {
				if (StringUtils.isNotEmpty(script))
					script += DataTemplate.CONDITION_AND;
				script += getCompareScript(filter.getJudgeCon1(), flowvarKey,
						val, filter.getOptType(), table, source,
						filter.getIsHidden());
			}
			break;
		case 26:// 密级管理	Liubo	2017-03-01
			String fieldNameStandard = this.fixFieldName(flowvarKey, source, table);
			if (StringUtils.isNotEmpty(script))
				script = script + DataTemplate.CONDITION_AND;
			script = script + sysFileService.getFileConditions(fieldNameStandard);
			break;
		case 5:// 角色、组织、岗位选择器
			String judgeCon = filter.getJudgeCon1();
			String[] ids = filter.getJudgeVal1().split("&&");
			if (ids.length == 2) {
				script = getCompareScript(judgeCon, filter.getFlowvarKey(),
						ids[0], filter.getOptType(), table, source,
						filter.getIsHidden());
			} else {// 特殊类型的
				if ("3".equalsIgnoreCase(judgeCon)
						|| "4".equalsIgnoreCase(judgeCon))
					script = getCompareScript(judgeCon, filter.getFlowvarKey(),
							filter.getJudgeVal1(), filter.getOptType(), table,
							source, filter.getIsHidden());

			}
			break;
            case 6:// 人员扩展后单独作为一个选择器不和角色、组织、岗位选择器混合使用
                String judgeCon1 = filter.getJudgeCon1();
                String[] ids1 = filter.getJudgeVal1().split("&&");
                if (ids1.length == 2)
                {
                    script =
                        getCompareScript(judgeCon1,
                            filter.getFlowvarKey(),
                            ids1[0],
                            filter.getOptType(),
                            table,
                            source,
                            filter.getIsHidden());
                }
                else
                {// 特殊类型的
                    switch (judgeCon1){
                        case "3":
                        case "4":
                        case "8":
                        case "11":
                        case "12":
                        case "13":
                            script =
                            getCompareScript(judgeCon1,
                                filter.getFlowvarKey(),
                                filter.getJudgeVal1(),
                                filter.getOptType(),
                                table,
                                source,
                                filter.getIsHidden()); 
                            break;
                    }
                    
                }
                break;
        }

		if (StringUtil.isEmpty(script))
			return;
		// 执行结果记录到operatorList中
		Map<String, Object> resultMap = this.getResultMap(filter.getCompType(),
				script);
		operatorList.add(resultMap);
	}

	/**
	 * 设置表的字段
	 * 
	 * @param table
	 * @param source
	 * @param mainTable
	 * @param relation
	 * @param tableMap
	 * @param relationMap
	 */
	private void setTableMap(FilterJsonStruct filter,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap) {
		String table = filter.getTable();
		String tableLow = table.toLowerCase();
		FormTable formTable = formTableDao.getByTableName(table);
		String source = filter.getSource();
		String mainTable = filter.getMainTable();
		String mainTableLow = mainTable.toLowerCase();
		FormTable formMainTable = formTableDao.getByTableName(mainTable);
		String relation = filter.getRelation();
		
		//isRelTable 表示为关联表 by liubo
		if(StringUtils.isNotEmpty(relation)&&relation.equals(IFormField.ISRELTABLE)){
			List<FormField> formFieldList = formFieldDao.getRelFieldsByTableId(formTable.getTableId(), formMainTable.getTableId());
			//拼接left join语句
			String leftSql = "left join "+this.fixTableName(table, source)+" "+tableLow+" on "+
					tableLow+"."+this.fixFieldName(formFieldList.get(0).getFieldName(),source)+"="+mainTableLow+"."+formMainTable.getPkField();
			if (!tableMap.containsKey(tableLow)) {
				tableMap.put(relation, leftSql);
			}
		}else{
			if (StringUtils.isNotEmpty(table)) {
				if (!tableMap.containsKey(tableLow)) {
					tableMap.put(tableLow, this.fixTableName(table, source));
				}
				if (StringUtils.isNotEmpty(mainTable)) {
					if (!relationMap.containsKey(tableLow)
							&& !tableLow.equals(mainTableLow)) {
						Map<String, String> map = new HashMap<String, String>();
						map.put(mainTableLow, relation);
						relationMap.put(tableLow, map);
					}
				}
			}
		}
	}

	/**
	 * 格式日期
	 * 
	 * @param val
	 * @param datefmt
	 * @return
	 */
	private String sqlToDate(String val, String datefmt) {
		StringBuffer sb = new StringBuffer();
		// TODO 需要修复，如果数据库不是ORACL的有问题
		sb.append("TO_DATE('")
				.append(val)
				.append("','")
				.append(StringUtils.isEmpty(datefmt) ? StringPool.DATE_FORMAT_DATE
						: datefmt).append("')");
		return sb.toString();
	}

	/**
	 * 修正后的表名
	 * 
	 * @param tableName
	 * @param source
	 * @return
	 */
	public String fixTableName(String tableName, String source) {
		if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(source))
			return tableName;
		if (DataTemplate.SOURCE_CUSTOM_TABLE.equals(source))
			tableName = TableModel.CUSTOMER_TABLE_PREFIX
					+ tableName.toUpperCase();
		return tableName;

	}

	/**
	 * 修正字段名
	 * 
	 * @param fieldName
	 *            字段名
	 * @param source
	 *            数据来源 1.表示自定义表（需要加F_修正）
	 * @param prefix
	 *            前缀修正
	 * @return
	 */
	private String fixFieldName(String fieldName, String source) {
		return fixFieldName(fieldName, source, "");
	}

	/**
	 * 修正字段名
	 * 
	 * @param fieldName
	 *            字段名
	 * @param source
	 *            数据来源 1.表示自定义表（需要加F_修正）
	 * @param prefix
	 *            前缀修正
	 * @return
	 */
	public String fixFieldName(String fieldName, String source, String prefix) {
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(source))
			return fieldName;
		if (DataTemplate.SOURCE_CUSTOM_TABLE.equals(source))
			fieldName = TableModel.CUSTOMER_COLUMN_PREFIX + fieldName;
		if (StringUtils.isNotEmpty(prefix))
			fieldName = prefix.toLowerCase() + "." + fieldName;
		return fieldName;
	}

	/**
	 * 获取根据条件组合的脚本
	 * 
	 * @param judgeCon
	 *            判断条件
	 * @param flowvarKey
	 *            字段
	 * @param judgeVal
	 *            字段的值
	 * @param type
	 *            类型
	 * @param table
	 *            表名
	 * @param source
	 *            数据来源
	 * @param isHidden
	 * @return
	 */
	private String getCompareScript(String judgeCon, String fieldName,
			String judgeVal, int type, String table, String source, int isHidden) {
		StringBuffer sb = new StringBuffer();
		fieldName = this.fixFieldName(fieldName, source, table);
		switch (type) {
		case 1:// 数值
		case 3:// 日期
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append("=").append(judgeVal);
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append("!=").append(judgeVal);
			} else if ("3".equals(judgeCon)) {
				sb.append(fieldName).append(">").append(judgeVal);
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(">=").append(judgeVal);
			} else if ("5".equals(judgeCon)) {
				sb.append(fieldName).append("<").append(judgeVal);
			} else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append("<=").append(judgeVal);
			}
			break;
		case 2:// 字符串
		case 4:// 字典
			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append("=").append("'").append(judgeVal)
						.append("'");
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append("!=").append("'").append(judgeVal)
						.append("'");
			} else if ("3".equals(judgeCon)) {
				sb.append("UPPER(").append(fieldName).append(")=")
						.append(" UPPER('").append(judgeVal).append("')");
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%")
						.append(judgeVal).append("%'");
			} else if ("5".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '")
						.append(judgeVal).append("%'");
			} else if ("6".equals(judgeCon)) {
				sb.append(fieldName).append(" LIKE").append(" '%")
						.append(judgeVal).append("'");
			}
			break;
		case 5:// 人员选择器
			if (isHidden == FormField.NO_HIDDEN)
				fieldName = fieldName + TableModel.PK_COLUMN_NAME;

			if ("1".equals(judgeCon)) {
				sb.append(fieldName).append(" in (").append("")
						.append(judgeVal).append(")");
			} else if ("2".equals(judgeCon)) {
				sb.append(fieldName).append(" not in (").append("")
						.append(judgeVal).append(")");
			} else if ("3".equals(judgeCon)) {
				sb.append(fieldName).append(" = :").append("").append(judgeVal)
						.append("");
			} else if ("4".equals(judgeCon)) {
				sb.append(fieldName).append(" != :").append("")
						.append(judgeVal).append("");
			}

			break;
		case 6:// 人员选择器
            if (isHidden == FormField.NO_HIDDEN){
                fieldName = fieldName + TableModel.PK_COLUMN_NAME;
            }
            switch(judgeCon){
                case "1":
                    sb.append(fieldName).append(" in (").append("")
                    .append(judgeVal).append(")");
                    break;
                case "2":
                    sb.append(fieldName).append(" not in (").append("")
                    .append(judgeVal).append(")");
                    break;
                case "3":
                    sb.append(fieldName).append(" = :").append("").append(judgeVal)
                    .append("");
                    break;
                case "4":
                    sb.append(fieldName).append(" != :").append("")
                    .append(judgeVal).append("");
                    break;
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "10":
                case "11":
                case "12":
                case "13":
                case "14":
                case "15":
                case "16":
                case "17":
                    sb.append(fieldName).append(" in (").append("")
                    .append(getUserByjudgeVal(judgeCon,judgeVal)).append(")");
                    break;
            }
            break;
		default:
			break;
		}
		return sb.toString();
	}

    private String getUserByjudgeVal(String judgeCon, String judgeVal)
    {
        StringBuffer userIds=new StringBuffer();
        Long userId=UserContextUtil.getCurrentUserId();
        List<? extends ISysUser> users=dataTemplatefilterService.invoke(judgeCon,judgeVal,userId);
        for(ISysUser u:users){
            userIds.append(u.getUserId()+",");
        }
        if(userIds.length()>0){
            return userIds.substring(0, userIds.length()-1);
        }else{
            return null;
        }
    }


    /**
	 * 获取SQL运算结果
	 * 
	 * @param operatorList
	 * @return
	 */
	private String executeOperator(List<Map<String, Object>> operatorList) {
		if (operatorList.size() == 0)
			return "";
		String returnVal = (String) operatorList.get(0).get("result");
		if (operatorList.size() == 1)
			return returnVal;
		int size = operatorList.size();
		for (int k = 1; k < size; k++) {
			Map<String, Object> resultMap = operatorList.get(k);
			String operator = resultMap.get("operator").toString();
			if ("or".equals(operator)) { // 或运算
				returnVal = "(" + returnVal + ") OR ("
						+ resultMap.get("result") + ")";
			} else if ("and".equals(operator)) { // 与运算
				returnVal = "(" + returnVal + ") AND ("
						+ resultMap.get("result") + ")";
			}
		}
		if (StringUtils.isNotEmpty(returnVal))
			returnVal = "(" + returnVal + ")";
		return returnVal;
	}

	/**
	 * 根据表单以及表单模板获取排序sql查询语句
	 * 
	 * @param sql
	 * @param bpmDataTemplate
	 *            表单模板
	 * @param formTable
	 *            表单
	 * @return
	 */
	public StringBuffer getOrderBySql(StringBuffer sql,
			DataTemplate bpmDataTemplate) {
		FormTable formTable = this.formTableService.getById(bpmDataTemplate
				.getTableId());
		String strsql = this.getOrderBySql(sql, formTable.getPkField(),
				bpmDataTemplate.getSortField(), null,
				bpmDataTemplate.getSource());
		return new StringBuffer(strsql);
	}

	/**
	 * 获得order by的SQL
	 * 
	 * @param sql
	 * 
	 * @param table
	 * @param sortField
	 * @param params
	 * @param sortMap
	 * @param source
	 * @return
	 */
	private String getOrderBySql(StringBuffer sql, String pkField,
			String sortField, Map<String, String> sortMap, String source) {
		StringBuffer orderBy = new StringBuffer();
		if (BeanUtils.isNotEmpty(sortMap)) {
			orderBy.append(" ORDER BY ")
					.append(fixFieldName(sortMap.get("sortField"), source, ""))
					.append(" ").append(sortMap.get("orderSeq"));
		} else {
			// 取设置的排序
			if (StringUtils.isNotEmpty(sortField)) {
				String sortSql = this.getSortSQL(sortField, source);
				if (StringUtils.isNotEmpty(sortSql))
					orderBy.append(" ORDER BY ").append(sortSql);
			}
		}
		// 如果没有排序 则用主键排序，避免分页问题
		if (StringUtils.isEmpty(orderBy.toString())) {
			orderBy.append(" ORDER BY ").append(pkField).append(" ASC");
		}
		StringBuffer select = new StringBuffer();
		select.append("select *  from  (").append(sql).append(") t ")
				.append(orderBy);
		return select.toString();
	}

	/**
	 * 获取排序的SQL
	 * 
	 * @param sortField
	 * @param source
	 * @return
	 */
	private String getSortSQL(String sortField, String source) {
		StringBuffer sb = new StringBuffer();
		JSONArray jsonArray = JSONArray.fromObject(sortField);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = (JSONObject) jsonArray.get(i);
			String name = (String) jsonObj.get("name");
			String sort = (String) jsonObj.get("sort");
			sb.append(this.fixFieldName(name, source, "")).append(" " + sort)
					.append(",");
		}
		if (sb.length() > 0)
			return sb.substring(0, sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 根据查询条件列表，计算Where SQL 语句
	 * 
	 * @param where
	 * @param tableName
	 * 
	 * @param conditions
	 * @param params
	 * @param bpmFormFieldMap
	 * @param fieldTypeMap
	 * @return
	 */
	private String getQuerySQL(DataTemplate bpmDataTemplate, String where,
			String tableName, Map<String, Object> params,
			Map<String, FormField> bpmFormFieldMap) {
		StringBuffer sb = new StringBuffer();
		String and = StringUtils.isEmpty(where) ? "" : " AND ";

		List<SQLClause> conditionFields = this.getConditionList(bpmDataTemplate
				.getConditionField());
		if (BeanUtils.isEmpty(conditionFields))
			return where;
		for (SQLClause condition : conditionFields) {
			this.getCluaseSQL(bpmDataTemplate, tableName, condition, params,
					bpmFormFieldMap, sb);
		}
		if (sb.length() > 0)
			where += and + " (1=1 " + sb.toString() + ") ";

		return where;
	}

	/**
	 * 计算出WHERE SQL
	 * 
	 * @param bpmDataTemplate
	 * @param tableName
	 * @param condition
	 * @param params
	 * @param bpmFormFieldMap
	 * @param fieldTypeMap
	 * @param sb
	 */

	private void getCluaseSQL(DataTemplate bpmDataTemplate, String tableName,
			SQLClause condition, Map<String, Object> params,
			Map<String, FormField> bpmFormFieldMap, StringBuffer sb) {
		String field = condition.getName();
		String f_field = this.fixFieldName(field, bpmDataTemplate.getSource(),
				tableName);
		String operate = condition.getOperate();
		int valueFrom = condition.getValueFrom();
		String joinType = condition.getJoinType();
		String type = condition.getType();	
		joinType = " " + joinType + " ";
		String controlType = condition.getControlType();

		Boolean isSelector = this.isSelector(controlType);
		Map<String, Object> dateMap = getQueryValue(condition, params, field,
				type, operate);

		Object value = this.getQueryValue(condition, params, field, isSelector);
		if (BeanUtils.isEmpty(value) && BeanUtils.isEmpty(dateMap))
			return;
		if(controlType.equals("28")){//下拉多选框条件处理
			value = value.toString();
			if (operate.equalsIgnoreCase("7")) {// "7","in" "8","not in"
				value = "'"+value.toString().replace(",", "','")+"'";
				sb.append(joinType).append(f_field).append(" in ( ")
						.append(value).append(")");
			} else if (operate.equalsIgnoreCase("8")) {
				value = "'"+value.toString().replace(",", "','")+"'";
				sb.append(joinType).append(f_field).append(" not in ( ")
						.append(value).append(")");
			}
			params.put(f_field, value);
		} else if(controlType.equals("3")){//数据字典
			value = value.toString();
			if(!value.equals("全部")){//值全部的时候查寻所有的
				FormField formField = bpmFormFieldMap.get(field);
				if(formField!=null)
					value = FormDataUtil.getDicValueByName(value,formField.getDictType());
				if (operate.equalsIgnoreCase("1")) {// 1","=" "2","!=" "3","like" "6","in"
					sb.append(joinType).append(f_field).append("=").append(":")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("2")) {
					sb.append(joinType).append(f_field).append(" != ")
							.append(":").append(f_field);
				} else if (operate.equalsIgnoreCase("3")) {
					value = "%" + value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				} else {
					value = "'"+value.toString().replace(",", "','")+"'";
					sb.append(joinType).append(f_field).append(" in ( ")
							.append(value).append(")");
				}
				params.put(f_field, value);
			}
			
		} else if (type.equals(ColumnModel.COLUMNTYPE_VARCHAR)) {// 字符串
			if (isSelector) {
				f_field = f_field + TableModel.PK_COLUMN_NAME;
				String f_fieldID = field + TableModel.PK_COLUMN_NAME;
				FormField bpmFormField = bpmFormFieldMap.get(f_fieldID);
				if (BeanUtils.isNotEmpty(bpmFormField)) {
					if (isrMultiSelector(bpmFormField.getControlType())) {
						// 这个有bug 暂时这样处理吧
						sb.append(joinType + f_field + " like '%" + value
								+ "%'");
					} else {
						if (operate.equalsIgnoreCase("2")) {// 1","=" "2","!=" "3","like""4","左like" "5","右like"
							sb.append(joinType + f_field + "!=:" + f_field);
							params.put(f_field, String.valueOf(value));
						} else {
							sb.append(joinType + f_field + "=:" + f_field);
							params.put(f_field, String.valueOf(value));
						}
					}
				}
			} else {
				value = value.toString();
				if (operate.equalsIgnoreCase("1")) {// 1","=" "2","!=" "3","like""4","左like" "5","右like"
					sb.append(joinType).append(f_field).append("=").append(":")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("2")) {
					sb.append(joinType).append(f_field).append(" != ")
							.append(":").append(f_field);
				} else if (operate.equalsIgnoreCase("3")) {
					value = "%" + value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("4")) {
					value = "%" + value.toString();
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				} else if (operate.equalsIgnoreCase("5")) {
					value = value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				}else if(operate.equalsIgnoreCase("6")) {
					value = "'"+value.toString().replace(",", "','")+"'";
					sb.append(joinType).append(f_field).append(" in ( ")
							.append(value).append(")");
				}else {
					value = "%" + value.toString() + "%";
					sb.append(joinType).append(f_field).append(" LIKE :")
							.append(f_field);
				}
				params.put(f_field, value);
			}

		} else if (type.equals(ColumnModel.COLUMNTYPE_DATE)) {// 日期
			if ("6".equalsIgnoreCase(operate)) {// 日期范围特殊处理
				if (BeanUtils.isNotEmpty(dateMap.get(DataTemplate.DATE_BEGIN))) {
					String begingField = DataTemplate.DATE_BEGIN + field;
					sb.append(joinType + f_field + ">=:" + begingField + " ");
					params.put(begingField,
							dateMap.get(DataTemplate.DATE_BEGIN));
				}

				if (BeanUtils.isNotEmpty(dateMap.get(DataTemplate.DATE_END))) {
					String endField = DataTemplate.DATE_END + field;
					sb.append(joinType + f_field + "<=:" + endField + " ");
					params.put(endField, dateMap.get(DataTemplate.DATE_END));
				}
			} else {
				String op = this.getOperate(operate);
				if (valueFrom == 1) {
					if (params.containsKey(field)) {
						sb.append(joinType + f_field + op + ":" + field + " ");
					}
				} else {
					sb.append(joinType + f_field + op + ":" + field + " ");
					params.put(field, value);
				}
			}
		} else if(type.equals(ColumnModel.COLUMNTYPE_NUMBER)){//数字
			if("7".equalsIgnoreCase(operate)){
				//数字范围特殊处理
				if (BeanUtils.isNotEmpty(dateMap.get(DataTemplate.NUMBER_BEGIN))) {
					String begingField = DataTemplate.NUMBER_BEGIN + field;
					sb.append(joinType + f_field + ">=:" + begingField + " ");
					params.put(begingField,
							dateMap.get(DataTemplate.NUMBER_BEGIN));
				}
				if (BeanUtils.isNotEmpty(dateMap.get(DataTemplate.NUMBER_END))) {
					String endField = DataTemplate.NUMBER_END + field;
					sb.append(joinType + f_field + "<=:" + endField + " ");
					params.put(endField, dateMap.get(DataTemplate.NUMBER_END));
				}
			}else{
				//一般的>、<、>=、<=、=处理
				String op = this.getOperate(operate);
				if (valueFrom == 1) {
					if (params.containsKey(field)) {

						sb.append(joinType + f_field + op + ":" + field + " ");
					}
				} else {
					sb.append(joinType + f_field + op + ":" + field + " ");
					params.put(field, value);
				}
			}
		}
	}

	/**
	 * 是否是选择器
	 * 
	 * @param controlType
	 * @return
	 */
	private Boolean isSelector(String controlType) {
		if (BeanUtils.isEmpty(controlType))
			return false;
		if (controlType.equals(String.valueOf(IFieldPool.SELECTOR_USER_SINGLE))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_USER_MULTI))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_ORG_SINGLE))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_ORG_MULTI))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_POSITION_SINGLE))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_POSITION_MULTI))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_ROLE_SINGLE))
				|| controlType.equals(String
						.valueOf(IFieldPool.SELECTOR_ROLE_MULTI)))
			return true;
		return false;
	}

	/**
	 * 是否是多选选择器
	 * 
	 * @param controlType
	 * @return
	 */
	private Boolean isrMultiSelector(Short controlType) {
		if (BeanUtils.isEmpty(controlType))
			return false;
		if (controlType.shortValue() == IFieldPool.SELECTOR_USER_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_ORG_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_POSITION_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_ROLE_MULTI)
			return true;
		return false;
	}

	private Map<String, Object> getQueryValue(SQLClause condition,
			Map<String, Object> params, String field, String type,
			String operate) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (type.equals(ColumnModel.COLUMNTYPE_DATE)
				&& "6".equalsIgnoreCase(operate)) {
			String beginKey = DataTemplate.DATE_BEGIN + field;
			Object beginVal = null;
			String endKey = DataTemplate.DATE_END + field;
			Object endVal = null;
			if (params.containsKey(beginKey))
				beginVal = params.get(beginKey);
			if (params.containsKey(endKey))
				endVal = params.get(endKey);
			if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal)) {
				map.put(DataTemplate.DATE_BEGIN, beginVal);
				map.put(DataTemplate.DATE_END, endVal);
			}
		}
		if (type.equals(ColumnModel.COLUMNTYPE_NUMBER)
				&& "7".equalsIgnoreCase(operate)) {
			String beginKey = DataTemplate.DATE_BEGIN + field;
			Object beginVal = null;
			String endKey = DataTemplate.DATE_END + field;
			Object endVal = null;
			if (params.containsKey(beginKey))
				beginVal = params.get(beginKey);
			if (params.containsKey(endKey))
				endVal = params.get(endKey);
			if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal)) {
				map.put(DataTemplate.DATE_BEGIN, beginVal);
				map.put(DataTemplate.DATE_END, endVal);
			}
		}
		return map;
	}

	/**
	 * 获得查询的值
	 * 
	 * @param condition
	 * @param params
	 * @param field
	 * @param isSelector
	 * @return
	 */
	private Object getQueryValue(SQLClause condition,
			Map<String, Object> params, String field, Boolean isSelector) {
		int valueFrom = condition.getValueFrom();
		Object value = null;
		switch (valueFrom) {
		case 1:// 输入
				// 是日期类型，又是日期范围
			if (isSelector)
				field = field + TableModel.PK_COLUMN_NAME;
			if (params.containsKey(field)) {
				value = params.get(field);
			}
			break;
		case 2:// 固定值
			value = condition.getValue();
			break;
		case 3:// 脚本
			String script = (String) condition.getValue();
			if (StringUtil.isNotEmpty(script)) {
				value = groovyScriptEngine.executeObject(script, null);
			}
			break;
		case 4:// 自定义变量
				// value =
				// sysTableManage.getParameterMap().get(condition.getValue().toString());
			break;
		}
		return value;
	}

	/**
	 * 获得操作类型
	 * 
	 * @param operate
	 * @return
	 */
	private String getOperate(String operate) {
		String op = "=";
		if ("1".equalsIgnoreCase(operate)) {// =
			op = "=";
		} else if ("2".equalsIgnoreCase(operate)) {// >
			op = ">";
		} else if ("3".equalsIgnoreCase(operate)) {// <
			op = "<";
		} else if ("4".equalsIgnoreCase(operate)) {// >=
			op = ">=";
		} else if ("5".equalsIgnoreCase(operate)) {// <=
			op = "<=";
		}
		return op;
	}

	/**
	 * 获得条件脚本的SQL
	 * 
	 * @param conditionField
	 * @return
	 */
	private List<SQLClause> getConditionList(String conditionField) {
		List<SQLClause> conditionFields = new ArrayList<SQLClause>();
		if (StringUtil.isEmpty(conditionField))
			return conditionFields;

		JSONArray jsonArray = JSONArray.fromObject(conditionField);

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			SQLClause field = new SQLClause();
			// field.setJoinType(jsonObject.getString("jt"));
			field.setJoinType("AND");
			field.setName(jsonObject.getString("na"));
			field.setComment(jsonObject.getString("cm"));
			field.setType(jsonObject.getString("ty"));
			field.setValue(jsonObject.get("va"));
			field.setValueFrom(jsonObject.getInt("vf"));
			field.setOperate(jsonObject.getString("op"));
			field.setControlType(jsonObject.getString("ct"));
			field.setQueryType(jsonObject.getString("qt"));
			conditionFields.add(field);
		}
		return conditionFields;
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
	private String getPageHtml(DataTemplate bpmDataTemplate,
			Map<String, Object> map, String tableIdCode, String pageURL)
			throws IOException, TemplateException {
		String pageHtml = "";
		String pageHtmlCopy = "" ;
		// if(BeanUtils.isEmpty(bpmDataTemplate.getList()))
		// return pageHtml;
		// 需要分页
		if (bpmDataTemplate.getNeedPage() == 1) {
			PagingBean pageBean = bpmDataTemplate.getPageBean();
			map.put("tableIdCode", tableIdCode);
			map.put("pageBean", pageBean);
			map.put("showExplain", true);
			map.put("showPageSize", true);
			map.put("baseHref", pageURL);
			map.put("pageURL", pageURL);
			// map.put("VarMap", map.getParameterMap());// custom parameter
			String pageTempl = "pageAjax.ftl";
			int type = SysConfConstant.SHOW_TYPE;
			if(type == 0){
				pageTempl = "oldpageAjax.ftl";//旧版本分页模板 by YangBo
			}
			pageHtml = freemarkEngine.mergeTemplateIntoString(pageTempl,map);

		}
		return pageHtml;
	}

	/**
	 * 获取字段列表
	 * 
	 * @param tableId
	 * @return
	 */
	public FormTable getFieldListByTableId(Long tableId) {
		FormTable bpmFormTable = formTableService
				.getTableByIdContainHidden(tableId);

		List<FormTable> otherTableList = new ArrayList<FormTable>();
		// 加入BPM_BUS_LINK的表和字段
		List<FormField> fieldList = new ArrayList<FormField>();
		FormField field0 = this.newFormField("BUS_CREATOR_ID",
				getText("service.bpmDataTemplate.getField.startUser"),
				FormField.DATATYPE_NUMBER, IFieldPool.SELECTOR_USER_SINGLE,
				FormField.HIDDEN);
		FormField field1 = this.newFormField("BUS_ORG_ID",
				getText("service.bpmDataTemplate.getField.startOrg"),
				FormField.DATATYPE_NUMBER, IFieldPool.SELECTOR_ORG_SINGLE,
				FormField.HIDDEN);
		FormField field2 = this.newFormField("BUS_CREATETIME",
				getText("service.bpmDataTemplate.getField.createTime"),
				FormField.DATATYPE_DATE, IFieldPool.DATEPICKER,
				FormField.NO_HIDDEN);
		FormField field3 = this.newFormField("BUS_UPDID",
				getText("service.bpmDataTemplate.getField.updateUser"),
				FormField.DATATYPE_NUMBER, IFieldPool.SELECTOR_USER_SINGLE,
				FormField.HIDDEN);
		FormField field4 = this.newFormField("BUS_UPDTIME",
				getText("service.bpmDataTemplate.getField.updateTime"),
				FormField.DATATYPE_DATE, IFieldPool.DATEPICKER,
				FormField.NO_HIDDEN);
		FormField field5 = this.newFormField("BUS_STATUS", "状态", "number",
				IFieldPool.TEXT_INPUT, FormField.VALUE_FROM_FORM);

		fieldList.add(field0);
		fieldList.add(field1);
		fieldList.add(field2);
		fieldList.add(field3);
		fieldList.add(field4);
		fieldList.add(field5);
		FormTable otherTable = new FormTable();
		otherTable.setTableName(DataTemplate.BUS_TABLE);
		otherTable
				.setTableDesc(getText("service.bpmDataTemplate.getField.tableDesc"));
		otherTable.setIsMain((short) 2);// 标记为特殊表
		otherTable.setIsExternal(1);
		otherTable
				.setRelation(bpmFormTable.getKeyDataType().shortValue() == FormTable.PKTYPE_NUMBER
						.shortValue() ? DataTemplate.BUS_TABLE_PK
						: DataTemplate.BUS_TABLE_PK_STR);

		otherTable.setFieldList(fieldList);
		otherTableList.add(otherTable);
		// end 加入BPM_BUS_LINK的表和字段
		if (BeanUtils.isNotEmpty(otherTableList))
			bpmFormTable.setOtherTableList(otherTableList);
		return bpmFormTable;
	}

	private FormField newFormField(String fieldName, String fieldDesc,
			String fieldType, short controlType, short isHidden) {
		FormField bpmFormField = new FormField();
		bpmFormField.setFieldName(fieldName);
		bpmFormField.setFieldDesc(fieldDesc);
		bpmFormField.setFieldType(fieldType);
		bpmFormField.setControlType(controlType);
		bpmFormField.setIsHidden(isHidden);
		return bpmFormField;
	}
	/**
     * 获取表单数据
     * 
     * @param id
     * @param pk
     * @return
     * @throws Exception
     */
    public String getForm(Long id, String pk, Long curUserId, String contextPath) throws Exception {
        Map<String, Object> param=new HashMap<String,Object>();
        param.put(IFormHandlerService._displayId_, id);
        param.put(IFormHandlerService._userId_, curUserId);
        param.put(IFormHandlerService._businessKey_, pk);
        param.put(IFormHandlerService._contextPath_, contextPath);
        param.put(IFormHandlerService._isCopyFlow_, false);
        return formHandlerService.getFormDetail(param);
    }
    /**
     * 获取表单数据Map
     * @return
     * @throws Exception
     */
    public Map<String,Object> getForm(Map<String, Object> map) throws Exception {
        Map<String,Object> rtnMap = new HashMap();
        Long id=MapUtil.get(map, IFormHandlerService._displayId_, Long.class);
        String pk=MapUtil.get(map,IFormHandlerService._businessKey_, String.class);
        String contextPath=MapUtil.get(map,IFormHandlerService._contextPath_, String.class);
        DataTemplate bpmDataTemplate = dao.getById(id);
        List<FormDef> bpmFormDefList = formDefDao.getByFormKeyIsDefault(bpmDataTemplate.getFormKey(), FormDef.IS_DEFAULT);
        if (BeanUtils.isEmpty(bpmFormDefList)) {
            return rtnMap;
        }
        FormDef bpmFormDef = bpmFormDefList.get(0);
        IProcessRun run = processRunService.getByBusinessKeyAndFormDef(pk,bpmFormDef.getFormDefId().toString());
        if(run==null){
        	run=processRunService.getByBusinessKey(pk);
        }
        map.put(IFormHandlerService._processRun_, run);
        map.put(IFormHandlerService._bpmFormDef_, bpmFormDef);
        rtnMap.put("form", formHandlerService.getFormDetail(map));
        rtnMap.put("headhtml", this.getFormHeadHtml(id, contextPath));
        rtnMap.put("formKey", bpmFormDef.getFormKey());
        return rtnMap;
    }

	/**
	 * 获取表单headhtml
	 * @param id
	 * @param pk
	 * @param curUserId
	 * @param contextPath
	 * @return
	 */
	public String getFormHeadHtml(Long id,String contextPath){
		DataTemplate bpmDataTemplate = dao.getById(id);
		List<FormDef> bpmFormDefList = formDefDao.getByFormKeyIsDefault(bpmDataTemplate.getFormKey(), FormDef.IS_DEFAULT);
		if (BeanUtils.isEmpty(bpmFormDefList))
			return "";
		FormDef bpmFormDef = bpmFormDefList.get(0);
		Map<String,Object> map = new HashMap();
		map.put("ctx", contextPath);
		return StringUtil.elReplace(bpmFormDef.getHeadHtml(), map);
	}



	/**
	 * 删除数据 ，包括批量删除数据
	 * 
	 * @param id
	 * @param _pk_
	 *            用逗号撇开。
	 * @throws Exception
	 */
	public void deleteData(Long bpmDataTemplateId, String _pk_)
			throws Exception {
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	    
		DataTemplate bpmDataTemplate = dao.getById(bpmDataTemplateId);
		Long tableId = bpmDataTemplate.getTableId();
		FormTable bpmFormTable = formTableService.getById(tableId);
		Map<String, Object> map = new HashMap<String, Object>();
		if (BeanUtils.isEmpty(bpmFormTable))
			return;

		// By:weilei 循环删除数据
		String[] pks = _pk_.split(",");
		for (String pk : pks) {
			// 删除业务表 BUS_TABLE 数据
			map.clear();
			map.put(bpmFormTable.getKeyDataType().shortValue() == FormTable.PKTYPE_NUMBER
					.shortValue() ? DataTemplate.BUS_TABLE_PK
					: DataTemplate.BUS_TABLE_PK_STR, pk);
			this.deleteTable(DataTemplate.BUS_TABLE, bpmFormTable, map);
			// 删除sub子表数据
			List<FormTable> subTableList = formTableService
					.getSubTableByMainTableId(tableId);
			map.clear();
			map.put("refId", pk);
			for (FormTable subTable : subTableList) {
				this.deleteTable(subTable, map);
			}
			// 删除rel关联表数据
            // 获取引用主表的关联表信息,应该通过关联表ID,表名称来获取关联字段，通过字段是否是远程对话框配置来判断是否进行同步操作。
            syncRelData(bpmFormTable,pk);
			// 删除主表数据
			map.clear();
			map.put("ID", pk);
			this.deleteTable(bpmFormTable, map);
			//流程草稿数据一并删除
			this.processRunService.deleteFormData(bpmDataTemplate.getFormKey(),pk);
		}
	}

    private void syncRelData(FormTable formTable,String pk)
        throws Exception
    {
        Map<String,Object> param=new HashMap<String,Object>();
        List<?extends IFormField> handelFiled=formFieldService.getRelFiledByTableIdAndName(formTable.getTableId().toString(),formTable.getTableName());
        String sql = "select * from " + formTable.getFactTableName()
            + " where id=:id";
        param.put("id", pk);
        JdbcHelper<?, ?> jdbcHelper = getJdbcHelper(formTable);
        List<?> data=null;
        try{
            data=jdbcHelper.queryForList(sql, param);
        }catch(Exception e){
            e.printStackTrace();
        }
        Map<String,Object> mData=new HashMap<>();
		if(data.size()>0){
			mData=(Map<String,Object>)data.get(0);
		}
        for(IFormField f:handelFiled){
            String relDialog=f.getRelFormDialogStripCData();
            FormTable relTable=this.formTableService.getById(f.getTableId());
            if(StringUtil.isNotEmpty(relDialog)){
                //配置了自定义对话框
                JSONObject relDialogJson=JSONObject.fromObject(relDialog);
                JSONArray fields=relDialogJson.getJSONArray("fields");
                String rpc=relDialogJson.getString(IFieldPool.rpcrefname);
                //删除的话只删除同一个应用系统的关联数据，不能删除rpc远程关联的数据。
                if(StringUtil.isEmpty(rpc)){
                    if (f.getRelDelType()== 1) {
                        // 1-直接删除
                        param.clear();
                        @SuppressWarnings("unchecked")
						Iterator<Object> it=fields.iterator();
                        while(it.hasNext()){
                        	JSONObject ob=(JSONObject) it.next();
                        	if(ob.get("target").equals(f.getFieldName())){
                        		 param.put(f.getFactFiledName(), mData.get(ob.get("src")));
                        		 break;
                        	}
                        }
                        if(param.isEmpty()){
                        	logger.debug("关联关系直接删除失败：未找到相关参数 ",param);
                        	return;
                        }
                        logger.debug("关联关系直接删除成功 ：",param);
                        this.deleteTable(relTable, param);
                    } else {
                        // 1-关联更新
                        String srcPk="";
                        Object pkValue="";
                        StringBuffer updateSQL=new StringBuffer("update "+relTable.getFactTableName()+" set "+ f.getFactFiledName()+"=null ");
                        for(Object _f:fields){
                            JSONObject fjson=(JSONObject)_f;
                            String target=fjson.getString(IFieldPool.FK_TABLEFIELD);
                            if(target.indexOf(IFieldPool.FK_SHOWAppCode)<0){
                            	if(target.toUpperCase().equals(ITableModel.PK_COLUMN_NAME)){
                            		continue;
                            	}
                            	if(!f.getFactFiledName().equals(ITableModel.CUSTOMER_COLUMN_PREFIX+target)) {
                            		updateSQL.append(","+ITableModel.CUSTOMER_COLUMN_PREFIX+target+"=null");
                            	}
                            }
                            if(target.equals(f.getFieldName())){
                                srcPk=fjson.getString(IFieldPool.FK_DIALOGFIELD);
                                pkValue=mData.get(srcPk);
                            }
                            
                            
                        }
                        updateSQL.append(" where "+f.getFactFiledName()+"=:"+f.getFactFiledName());
                        //更新取消关联
                        param.clear();
                        param.put(f.getFactFiledName(), StringUtil.isEmpty(pkValue.toString())?pk:pkValue);
                        jdbcHelper = getJdbcHelper(relTable);
                        jdbcHelper.execute(updateSQL.toString(), param);
                    }
                }
            }else{
                // 判断是否要级联删除rel记录。
                if (f.getRelDelType()== 1) {
                    // 1-直接删除
                    param.clear();
                    param.put(f.getFactFiledName(), pk);
                    this.deleteTable(relTable, param);
                } else {
                    //更新取消关联
                    param.clear();
                    param.put(f.getFactFiledName(), pk);
                    this.updateTable(relTable, param);
                }
            }
        }
    }

	/**
     * 删除表的数据
     * 
     * @param tableName
     * @param bpmFormTable
     * @param map
     * @throws Exception
     */
    public void deleteTable(String tablename ,FormTable bpmFormTable,
            Map<String, Object> map) throws Exception {
        String delSQL = "DELETE FROM " + tablename + " WHERE  ";
        for (String key : map.keySet()) {
            delSQL += key + "=:" + key;
        }
        JdbcHelper<?, ?> jdbcHelper = getJdbcHelper(bpmFormTable);
        jdbcHelper.execute(delSQL, map);
    }
    
	/**
	 * 删除表的数据
	 * 
	 * @param tableName
	 * @param bpmFormTable
	 * @param map
	 * @throws Exception
	 */
	public void deleteTable(FormTable bpmFormTable,
			Map<String, Object> map) throws Exception {
		String delSQL = "DELETE FROM " + bpmFormTable.getFactTableName() + " WHERE  ";
		for (String key : map.keySet()) {
			delSQL += key + "=:" + key;
		}
		JdbcHelper<?, ?> jdbcHelper = getJdbcHelper(bpmFormTable);
		jdbcHelper.execute(delSQL, map);
	}

	/**
	 * 更新表的数据
	 * 
	 * @param tableName
	 * @param bpmFormTable
	 * @param map
	 * @throws Exception
	 */
	public void updateTable(FormTable bpmFormTable,
			Map<String, Object> map) throws Exception {
		String updatePartSql = "";
		String updateWhereSql = "";
		for (String key : map.keySet()) {
			updatePartSql += key + "= null ,";
			updateWhereSql += " and " + key + "=:" + key;
		}
		if (StringUtils.isNotEmpty(updatePartSql)) {
			updatePartSql = updatePartSql.substring(0,
					updatePartSql.length() - 1);
			String updateSQL = "update " + bpmFormTable.getFactTableName() + " set " + updatePartSql
					+ " where 1=1 " + updateWhereSql;
			JdbcHelper<?, ?> jdbcHelper = getJdbcHelper(bpmFormTable);
			jdbcHelper.execute(updateSQL, map);
		}

	}

	// TODO =================导出===================================
	// 取得导出条件语句
	public String exportWhereSql(Map<String, Object> params,
			DataTemplate bpmDataTemplate) {
		Long curUserId = UserContextUtil.getCurrentUserId();
		//Long curOrgId = UserContextUtil.getCurrentOrg().getOrgId();

		FormTable bpmFormTable = formTableService.getTableById(bpmDataTemplate
				.getTableId());
		// 获取权限map
		Map<String, Object> rightMap;
		if(UserContextUtil.getCurrentUser().getUsername().equals("system")){
			rightMap = this.getRightMap(curUserId, null);
		}else{
			Long orgId = UserContextUtil.getCurrentOrg()==null?0L:UserContextUtil.getCurrentOrg().getOrgId();
			rightMap = this.getRightMap(curUserId, orgId);
		}
		// 取得当前过滤的条件
		JSONObject filterJson = this.getFilterJson(
				bpmDataTemplate.getFilterField(), rightMap, params);
		// 过滤条件 sql
		// 以下是拼接SQL的
		String source = bpmDataTemplate.getSource();
		String tableName = bpmFormTable.getTableName();
		String table = tableName.toLowerCase();
		// 获取表单上的查询
		Map<String, FormField> bpmFormFieldMap = this
				.getFormFieldMap(bpmFormTable);
		Map<String, String> tableMap = new HashMap<String, String>();
		tableMap.put(table, fixTableName(tableName, source));
		if (JSONUtil.isNotEmpty(filterJson)) {
			String type = (String) filterJson.get("type");
			if ("2".equals(type)) {
				String condition = (String) filterJson.get("condition");
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("map", params);

				// 获取表单上的查询
				// 查询条件 sql
				String where = this.getQuerySQL(bpmDataTemplate, "", tableName,
						params, bpmFormFieldMap);
				Map<String, Object> whereMap = new HashMap();
				whereMap.put("where", where);
				paramsMap.put("whereMap", whereMap);

				return groovyScriptEngine.executeString(condition, paramsMap);
			}
		}

		// = this.getFilterSQL(filterJson, tableMap, new HashMap<String,
		// Map<String, String>>());

		// 查询条件 sql TODO 待完善界面上文本框查询条件语句
		// String where = this.getQuerySQL(bpmDataTemplate, "", tableName,
		// params,bpmFormFieldMap);
		return "";
	}

	public Map<String, String> getTableName(FormTable bpmFormTable) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(bpmFormTable.getTableName(), bpmFormTable.getTableDesc());
		for (FormTable table : bpmFormTable.getSubTableList()) {
			map.put(table.getTableName(), table.getTableDesc());
		}
		return map;
	}

	public Map<String, Boolean> getFieldPermission(int type, String fieldJson,
			Map<String, Object> rightMap) {
		JSONArray jsonAry = JSONArray.fromObject(fieldJson);
		return getFieldPermissionMap(type, jsonAry, rightMap);

	}

	@SuppressWarnings("unchecked")
	private Map<String, Boolean> getFieldPermissionMap(int type,
			JSONArray jsonAry, Map<String, Object> rightMap) {
		Map<String, Boolean> map = new ListOrderedMap();
		if (JSONUtil.isEmpty(jsonAry))
			return map;

		for (Object obj1 : jsonAry) {
			JSONObject json1 = JSONObject.fromObject(obj1);
			JSONArray fields = (JSONArray) json1.get("fields");
			String tableName = (String) json1.get("tableName");
			String isMain = (String) json1.get("isMain");
			// 添加主键和外键字段
			String key = tableName
					+ ("1".equals(isMain) ? TableModel.PK_COLUMN_NAME
							: TableModel.FK_COLUMN_NAME);
			map.put(key, true);
			for (Object obj : fields) {
				JSONObject json = JSONObject.fromObject(obj);
				String name = (String) json.get("name");
				JSONArray rights = (JSONArray) json.get("right");
				for (Object right : rights) {
					JSONObject rightJson = JSONObject.fromObject(right);
					Integer s = (Integer) rightJson.get("s");
					if (s.intValue() == type)
						map.put(tableName + name,
								this.hasRight(rightJson, rightMap));
				}
			}

		}
		return map;
	}

	/**
	 * 获得数据列表
	 * 
	 * @param bpmDataTemplate
	 * @param displayFieldList
	 * @param displayFieldMap
	 * @param bpmFormTable
	 * @param exportIds
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List<List<Object>>> getDataList(
			DataTemplate bpmDataTemplate,
			Map<String, List<String>> displayFieldName,
			Map<String, Boolean> displayFieldMap,
			Map<String, Object> formatData, FormTable bpmFormTable,
			String[] exportIds) throws Exception {
		Map<String, List<List<Object>>> dataObjectList = new HashMap<String, List<List<Object>>>();
		// 主表的数据
		List<Map<String, Object>> list = bpmDataTemplate.getList();
		if (BeanUtils.isEmpty(displayFieldName) || BeanUtils.isEmpty(list))
			return dataObjectList;
		String mainTableName = bpmFormTable.getTableName();

		List<List<Object>> dataMainList = new ArrayList<List<Object>>();
		String pkField = bpmFormTable.getPkField();
		Short keyDataType = bpmFormTable.getKeyDataType();
		
		List<Object> keyValList = new ArrayList<Object>();
		
		List<List<Object>> idDataList = new ArrayList<List<Object>>();
		//按顺序保存主键ids
		List<Object> idList = new ArrayList<Object>();
		for (Map<String, Object> map : list) {
			List<Object> dataList = new ArrayList<Object>();
			List<String> displayFieldList = displayFieldName.get(mainTableName);
			for (String name : displayFieldList) {
				// 按字段的顺序
				Boolean f = displayFieldMap.get(mainTableName + name);
				if (f) {
					dataList.add(map.get(name));
				}
			}
			
			idList.add(map.get(pkField));
			// 导出指定的ID
			if (ArrayUtils.isNotEmpty(exportIds))
				dataList = getExportDataList(dataList, map, exportIds, pkField, keyDataType);

			if (BeanUtils.isNotEmpty(dataList)) {
				dataMainList.add(dataList);
				keyValList.add(map.get(pkField));
			}
		}
		idDataList.add(idList);
		//放入导出数据ids
		dataObjectList.put("ids", idDataList);
		// 主表的数据
		dataObjectList.put(mainTableName, dataMainList);
		if (bpmFormTable.getSubTableList().size() == 0)
			return dataObjectList;

		JdbcHelper jdbcHelper = this.getJdbcHelper(bpmFormTable);
		// 子表数据
		for (FormTable subTable : bpmFormTable.getSubTableList()) {
			List<List<Object>> dataSubList = new ArrayList<List<Object>>();
			String subTableName = subTable.getTableName();
			List<String> displayFieldList = displayFieldName.get(subTableName);
			Map<String, Object> subFormatData = getFormatDataMap(subTable);
			for (Object o : keyValList) {
				// 子表数据
				dataSubList = this.getSubTableDataList(dataSubList,
						displayFieldList, displayFieldMap, o, pkField,
						keyDataType, subTable, subTableName, jdbcHelper,
						subFormatData);
			}
			dataObjectList.put(subTable.getTableName(), dataSubList);
		}

		return dataObjectList;
	}

	/**
	 * 获取子表的数据
	 * 
	 * @param dataSubList
	 * @param displayFieldList
	 * @param displayFieldMap
	 * @param o
	 * @param pkField
	 * @param keyDataType
	 * @param subTable
	 * @param subTableName
	 * @param jdbcHelper
	 * @param formatData
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<List<Object>> getSubTableDataList(
			List<List<Object>> dataSubList, List<String> displayFieldList,
			Map<String, Boolean> displayFieldMap, Object o, String pkField,
			Short keyDataType, FormTable subTable, String subTableName,
			JdbcHelper jdbcHelper, Map<String, Object> formatData)
			throws Exception {
		String idStr = String.valueOf(o);
		String relation = StringUtils.isEmpty(subTable.getRelation()) ? TableModel.FK_COLUMN_NAME
				: subTable.getRelation();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("SELECT * FROM ");
		sql.append(subTable.getFactTableName()).append(" WHERE ")
				.append(relation).append(" = ");
		if (keyDataType.shortValue() == FormTable.PKTYPE_NUMBER.shortValue())
			sql.append(Long.parseLong(idStr));
		else
			sql.append(" = ").append(idStr);
		List<Map<String, Object>> list = jdbcHelper.queryForList(
				sql.toString(), params);
		// 转换的数据
		if (BeanUtils.isEmpty(list))
			return dataSubList;
		// 整理数据
		list = this.getDataList(list, subTable, formatData, false);
		for (Map<String, Object> map : list) {
			List<Object> dataList = new ArrayList<Object>();
			for (String name : displayFieldList) {
				// 按字段的顺序取出数据
				Boolean f = displayFieldMap.get(subTableName + name);
				if (!f)
					continue;
				if (relation.equals(name))
					dataList.add(map.get(name));
				else
					dataList.add(map.get(name));
			}
			dataSubList.add(dataList);
		}
		return dataSubList;
	}

	/**
	 * 获得导出的数据list
	 * 
	 * @param dataList
	 * @param map
	 * @param exportIds
	 * @param pkField
	 * @param keyDataType
	 * @return
	 */
	private List<Object> getExportDataList(List<Object> dataList,
			Map<String, Object> map, String[] ids, String pkField,
			Short keyDataType) {
		Object idVal = map.get(pkField);
		if (idVal == null)
			return null;
		String idStr = String.valueOf(idVal);
		Boolean b = true;
		if (keyDataType.shortValue() == FormTable.PKTYPE_NUMBER.shortValue()) {
			Long id = Long.parseLong(idStr);
			Long[] idLs = ArrayUtil.convertArray(ids);
			b = ArrayUtils.contains(idLs, id);
		} else {
			b = ArrayUtils.contains(ids, idStr);
		}

		if (!b)
			dataList = null;
		return dataList;
	}

	/**
	 * 获得显示的字段
	 * 
	 * @param displayField
	 * @param displayFieldMap
	 * @param b
	 *            0代表显示desc 其它代表显示name
	 * @param isSubTable
	 * @return map<表名,字段列表>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<String>> getDisplayFieldList(String displayField,
			Map<String, Boolean> displayFieldMap, int b, Boolean isSubTable) {
		Map<String, List<String>> displayFieldList = new ListOrderedMap();
		JSONArray jsonAry = JSONArray.fromObject(displayField);
		if (JSONUtil.isEmpty(jsonAry) || BeanUtils.isEmpty(displayFieldMap))
			return displayFieldList;

		for (Object obj1 : jsonAry) {
			List<String> list = new ArrayList<String>();
			JSONObject json1 = JSONObject.fromObject(obj1);
			String table = (String) json1.get("tableName");
			String isMain = (String) json1.get("isMain");
			JSONArray fields = (JSONArray) json1.get("fields");

			// 加入主键或者外键 如果有子表加入外键
			if (isSubTable) {
				String val = getFk(isMain, b);
				list.add(val);
			}

			for (Object obj : fields) {
				JSONObject json = JSONObject.fromObject(obj);
				String name = (String) json.get("name");
				String desc = (String) json.get("desc");
				String tableName = (String) json.get("tableName");
				Boolean f = displayFieldMap.get(tableName + name);
				if (!f)
					continue;
				if (b == 0)
					list.add(desc);
				else
					list.add(name);
			}
			displayFieldList.put(table, list);
		}
		return displayFieldList;
	}

	private String getFk(String isMain, int b) {
		String val = "";
		if ("1".equals(isMain)) {
			if (b == 0)
				val = getText("service.bpmDataTemplate.getFk.pk");
			else
				val = TableModel.PK_COLUMN_NAME;
		} else {
			if (b == 0)
				val = getText("service.bpmDataTemplate.getFk.fk");
			else
				val = TableModel.FK_COLUMN_NAME;
		}
		return val;
	}

	// TODO =================导入===================================
	/**
	 * 导入文件
	 * 
	 * @param inputStream
	 * @param id
	 * @throws Exception
	 */
	public StringBuffer importFile(InputStream inputStream, Long id) throws Exception {
		//用于记录导出校验信息
		StringBuffer importInfo = new StringBuffer();
		
		ExcelReader excel = new ExcelReader();
		// 读出execl
		TableEntity tableEntity = excel.readFile(inputStream);
		// 获取DataTemplate
		DataTemplate bpmDataTemplate = dao.getById(id);
		// 获取FormTable
		FormTable bpmFormTable = formTableService.getByTableId(
				bpmDataTemplate.getTableId(), FormTable.NEED_HIDE);

		// 获取表中唯一约束的列
		List<FormField> formFieldList = bpmFormTable.getFieldList();
		String pkField = bpmFormTable.getPkField();
		String tableName = bpmFormTable.getTableName();
		String table = tableName.toLowerCase();
		Map<String, String> tableMap = new HashMap<String, String>();
		tableMap.put(table,
				fixTableName(tableName, DataTemplate.SOURCE_CUSTOM_TABLE));
		// 必填校验
		ExcelCheck eCheck = new ExcelCheck(importInfo,formFieldList,tableEntity);
		eCheck.check(ExcelCheck.REQUIRE_CHECK);
		//字段有无校验
		eCheck.check(ExcelCheck.NAMEID_CHECK);
		//日期校验
		eCheck.check(ExcelCheck.TIME_CHECK);
		if(!importInfo.toString().equals("")){       //dengwenjie
			return importInfo;
		}
		
		// 将唯一约束的字段和约束的wheresql放置在Map中。
		Map checkUniqueConditMap = new HashMap();
		StringBuffer checkUniqueSql = new StringBuffer(" SELECT ");
		checkUniqueSql.append(getFromTableSQL(pkField, bpmDataTemplate,
				tableName, tableMap, new HashMap()));
		checkUniqueSql.append(" where 1=1 ");
		if(formFieldList != null && formFieldList.size() > 0) {
			for (FormField formField : formFieldList) {
				if (formField.getIsUnique() == 1) {
					String fieldName = formField.getFieldName();
					String f_field = this.fixFieldName(fieldName,
							bpmDataTemplate.getSource(), tableName);
					StringBuffer checkUniqueCondit = new StringBuffer();
					checkUniqueCondit.append(" AND ").append(f_field)
							.append("=").append(":").append(f_field);
					checkUniqueConditMap.put(fieldName, checkUniqueCondit);
					checkUniqueConditMap.put("PARAM_" + fieldName, f_field);
				}
			}
		}
		// 如果没有唯一性约束条件，可将其置空。
		if (BeanUtils.isEmpty(checkUniqueConditMap)) {
			checkUniqueConditMap = null;
		} else {
			checkUniqueConditMap.put("checkUniqueSql", checkUniqueSql);
		}
		
		// 执行导入数据
		int rowNum = 0;
		for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
			String pkValue = dataEntity.getPkVal();
			Map<String, List<DataEntity>> subDataEntityMap = getSubDataEntityMap(
					tableEntity, pkValue);
			FormData bpmFormData = parseFormData(dataEntity, subDataEntityMap,
					bpmFormTable, checkUniqueConditMap);
				// 执行插入更新操作
				formHandlerService.handFormData(bpmFormData, id.toString(), null,null);
				eCheck.getImportInfo(bpmFormData,dataEntity,rowNum,importInfo);
				rowNum++;
		}
		return importInfo;

	}
	@SuppressWarnings("unchecked")
	private Map<String, List<DataEntity>> getSubDataEntityMap(
			TableEntity tableEntity, String pkValue) {
		Map<String, List<DataEntity>> subDataEntityMap = new ListOrderedMap();
		if (null != tableEntity.getSubTableEntityList()) {
			// 子表
			for (TableEntity subTableEntity : tableEntity
					.getSubTableEntityList()) {
				if (subTableEntity == null)
					continue;
				String key = subTableEntity.getName();
				List<DataEntity> subDataEntityList = new ArrayList<DataEntity>();
				for (DataEntity subDataEntity : subTableEntity
						.getDataEntityList()) {
					if (subDataEntity.getPkVal().equals(pkValue)) {
						subDataEntityList.add(subDataEntity);
					}
				}
				subDataEntityMap.put(key, subDataEntityList);
			}
		}
		return subDataEntityMap;
	}

	/**
	 * 转换表的数据
	 * 
	 * @param dataEntity
	 * @param subDataEntityMap
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	private FormData parseFormData(DataEntity dataEntity,
			Map<String, List<DataEntity>> subDataEntityMap,
			FormTable bpmFormTable, Map checkUniqueConditMap) throws Exception {
		FormData bpmFormData = new FormData(bpmFormTable);
		PkValue pkValue = FormDataUtil.generatePk(bpmFormTable);
		// 判断id与导入表是否重复
		if (checkUniqueConditMap != null) {
			// 查找出数据库中的唯一记录，并获取其ID
			pkValue = checkUnique(dataEntity, bpmFormTable,
					checkUniqueConditMap, pkValue);
		}

		bpmFormData.setPkValue(pkValue);
		// 处理主表
		handleMain(dataEntity, bpmFormData);
		// 处理子表
		handSub(subDataEntityMap, bpmFormData);
		return bpmFormData;
	}

	/**
	 * 查找出数据库中的唯一记录，并获取其ID
	 * 
	 * @param dataEntity
	 * @param subDataEntityMap
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	private PkValue checkUnique(DataEntity dataEntity, FormTable bpmFormTable,
			Map checkUniqueConditMap, PkValue pkValue) throws Exception {
		// 获取jdbc
		JdbcHelper jdbcHelper = this.getJdbcHelper(bpmFormTable);
		// 获取检查是否唯一的sql语句。
		StringBuffer checkUniqueSql = (StringBuffer) checkUniqueConditMap
				.get("checkUniqueSql");
		// excel中的数据列。
		List<FieldEntity> fieldEntityList = dataEntity.getFieldEntityList();
		// FormField
		List<FormField> mainFields = bpmFormTable.getFieldList();
		// 主表字段
		Map<String, FormField> fieldDescMap = this
				.convertFieldToMap(mainFields);

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 对字段名称进行遍历
		for (FieldEntity fieldEntity : fieldEntityList) {
			String name = fieldEntity.getName();
			String value = fieldEntity.getValue();
			FormField bpmFormField = fieldDescMap.get(name);
			if (BeanUtils.isEmpty(bpmFormField))
				continue;
			String fieldName = bpmFormField.getFieldName();
			// 判断列名和唯一性约束的列名是否一致
			if (checkUniqueConditMap.containsKey(fieldName)) {
				StringBuffer whereSql = (StringBuffer) checkUniqueConditMap
						.get(fieldName);
				String keyName = (String) checkUniqueConditMap.get("PARAM_"
						+ fieldName);
				Object convertValue = FormDataUtil.convertType(value,
						bpmFormField);
				checkUniqueSql.append(whereSql);
				paramsMap.put(keyName, convertValue);
			} else {
				continue;
			}
		}
		// 查询数据库中是否有该记录。
		List list = jdbcHelper.queryForList(checkUniqueSql.toString(),
				paramsMap);
		if (list != null && list.size() > 0) {
			Map map = (Map) list.get(0);
			pkValue.setIsAdd(false);
			// 这里给已有id
			pkValue.setValue(map.get(bpmFormTable.getPkField()));
		}
		return pkValue;
	}

	/**
	 * 处理子表
	 * 
	 * @param dataEntity
	 * @param subDataEntityMap
	 * @param bpmFormData
	 * @throws Exception
	 */
	private void handSub(Map<String, List<DataEntity>> subDataEntityMap,
			FormData bpmFormData) throws Exception {
		FormTable mainTable = bpmFormData.getFormTable();
		List<FormTable> listTable = mainTable.getSubTableList();
		// 将表名消息并作为键和表对象进行关联。
		Map<String, FormTable> formTableMap = convertTableMap(listTable);

		boolean isExternal = mainTable.isExtTable();

		for (Iterator<Entry<String, List<DataEntity>>> it = subDataEntityMap
				.entrySet().iterator(); it.hasNext();) {
			SubTable subTable = new SubTable();
			Map.Entry<String, List<DataEntity>> e = (Map.Entry<String, List<DataEntity>>) it
					.next();
			String key = e.getKey();
			List<DataEntity> dataEntityList = e.getValue();
			FormTable subFormTable = formTableMap.get(key);

			// 获取子表的列元数据。
			List<FormField> subTableFields = subFormTable.getFieldList();
			// 将子表的字段名称作为键，字段对象作为值放到map对象当中。
			Map<String, FormField> subFieldDescMap = convertFieldToMap(subTableFields);
			// 设置子表名称
			subTable.setTableName(subFormTable.getTableName());

			// 设置子表的主键和外键名称。
			if (isExternal) {
				String pk = subFormTable.getPkField();
				subTable.setPkName(pk);
				subTable.setFkName(subFormTable.getRelation());
			} else {
				subTable.setPkName(TableModel.PK_COLUMN_NAME);
				subTable.setFkName(TableModel.FK_COLUMN_NAME);
			}

			for (DataEntity dataEntity : dataEntityList) {
				Map<String, Object> subRow = handleRow(subFormTable,
						subFieldDescMap, dataEntity.getFieldEntityList());
				// 处理主键数据
				handFkRow(subFormTable, subRow, bpmFormData.getPkValue());
				// 处理需要计算的数据。
				subTable.addRow(subRow);
			}

			bpmFormData.addSubTable(subTable);
		}
	}

	/**
	 * 处理子表行数据的主键和外键。
	 * 
	 * <pre>
	 * 添加子表的主键和外键。
	 * </pre>
	 * 
	 * @param mainTabDef
	 *            主表定义。
	 * @param bpmFormTable
	 *            子表定义。
	 * @param rowData
	 *            子表一行数据。
	 * @param pkValue
	 *            主键数据。
	 * @throws Exception
	 */
	public static void handFkRow(FormTable bpmFormTable,
			Map<String, Object> rowData, PkValue pkValue) throws Exception {
		boolean isExternal = bpmFormTable.isExtTable();
		// 外部表数据
		if (isExternal) {
			String pkField = bpmFormTable.getPkField().toLowerCase();
			if (!rowData.containsKey(pkField)) {
				PkValue pk = FormDataUtil.generatePk(bpmFormTable);
				rowData.put(pk.getName(), pk.getValue());
			} else {
				Object obj = rowData.get(pkField);
				if (obj == null || "".equals(obj.toString().trim())) {
					PkValue pk = FormDataUtil.generatePk(bpmFormTable);
					rowData.put(pk.getName(), pk.getValue());
				}
			}
			String fk = bpmFormTable.getRelation();
			rowData.put(fk, pkValue.getValue());
		}
		// 本地表数据
		else {
			String pkField = bpmFormTable.getPkField().toLowerCase();
			// 没有包含主键则添加一个。
			if (!rowData.containsKey(pkField)) {
				Long pk = UniqueIdUtil.genId();
				rowData.put(TableModel.PK_COLUMN_NAME.toLowerCase(), pk);
			}

			rowData.put(TableModel.FK_COLUMN_NAME.toLowerCase(),
					pkValue.getValue());
		}

	}

	/**
	 * 处理主表
	 * 
	 * @param dataEntity
	 * @param bpmFormData
	 */
	private void handleMain(DataEntity dataEntity, FormData bpmFormData) {
		// 主表 main
		FormTable mainTableDef = bpmFormData.getFormTable();
		List<FormField> mainFields = mainTableDef.getFieldList();
		// 主表字段
		Map<String, FormField> mainFieldDescMap = this
				.convertFieldToMap(mainFields);

		// 将主表JSON转换成map数据。
		Map<String, Object> mainFiledsData = handleRow(mainTableDef,
				mainFieldDescMap, dataEntity.getFieldEntityList());
		// 添加主表数据
		bpmFormData.addMainFields(mainFiledsData);

		PkValue pkValue = bpmFormData.getPkValue();

		bpmFormData.addMainFields(pkValue.getName().toLowerCase(),
				pkValue.getValue());
	}

	private Map<String, FormTable> convertTableMap(List<FormTable> list) {
		Map<String, FormTable> map = new HashMap<String, FormTable>();
		for (FormTable tb : list) {
			map.put(tb.getTableDesc(), tb);
		}
		return map;
	}

	/**
	 * 处理一行数据
	 * 
	 * @param bpmFormTable
	 * @param fieldDescMap
	 * @param list
	 * @return
	 */
	private Map<String, Object> handleRow(FormTable bpmFormTable,
			Map<String, FormField> fieldDescMap, List<FieldEntity> list) {
		boolean isExternal = bpmFormTable.isExtTable();
		// int keyType= bpmFormTable.getKeyDataType();
		// String pkField=bpmFormTable.getPkField();
		String colPrefix = (isExternal ? "" : TableModel.CUSTOMER_COLUMN_PREFIX)
				.toLowerCase();
		Map<String, Object> row = new HashMap<String, Object>();
		// 对字段名称进行遍历
		for (FieldEntity fieldEntity : list) {
			String name = fieldEntity.getName();
			String value = fieldEntity.getValue();
			FormField bpmFormField = fieldDescMap.get(name);
			if (BeanUtils.isEmpty(bpmFormField))
				continue;
			String key = bpmFormField.getFieldName();
			Object convertValue = FormDataUtil.convertType(value, bpmFormField);
			String fieldName = key.toLowerCase();
			if (!isExternal
					&& !fieldName.equalsIgnoreCase(TableModel.PK_COLUMN_NAME)) {
				fieldName = (colPrefix + key).toLowerCase();
			}
			row.put(fieldName, convertValue);
		}
		return row;
	}

	/**
	 * 转换Map<字段注释,自定义表>
	 * 
	 * @param list
	 * @return
	 */
	private Map<String, FormField> convertFieldToMap(List<FormField> list) {
		Map<String, FormField> map = new HashMap<String, FormField>();
		for (Iterator<FormField> it = list.iterator(); it.hasNext();) {
			FormField field = it.next();
			map.put(field.getFieldDesc(), field);
		}
		return map;
	}

	/**
	 * 根据formKey获取业务表单数量。
	 * 
	 * @param formKey
	 * @return
	 */
	public Integer getCountByFormKey(Long formKey) {
		return dao.getCountByFormKey(formKey);
	}

	/**
	 * 处理外键列,在查询条件字段json中加入元素relFormDialog
	 * 
	 * @param conditionField
	 * @param bpmDataTemplate
	 * @return
	 */
	public String genConditionFieldRelFormDialog(String conditionField,
			DataTemplate bpmDataTemplate) {
		JSONArray conditionFieldJsonArrayNew = new JSONArray();
		boolean needResetConditionField = false;
		if (StringUtils.isNotEmpty(conditionField)) {
			JSONArray conditionFieldJsonArray = JSONArray
					.fromObject(conditionField);
			if (conditionFieldJsonArray != null) {
				for (int i = 0; i < conditionFieldJsonArray.size(); i++) {
					Object jsonObj = conditionFieldJsonArray.get(i);
					JSONObject conditionFieldJson = JSONObject
							.fromObject(jsonObj);
					String ct = conditionFieldJson.getString("ct");
					// 查找fk关联外键的dialog脚本
					String relFormDialog = "";
					if (StringUtil.isNotEmpty(ct)
							&& ct.equals(String
									.valueOf(IFieldPool.RELATION_COLUMN_CONTROL))) {
						String fieldName = conditionFieldJson.getString("na");
						Long tableId = bpmDataTemplate.getTableId();
						List<FormField> formFieldList = formTableService
								.getFormFieldList(tableId, FormTable.NEED_HIDE);
						for (FormField formField : formFieldList) {
							String name = formField.getFieldName();
							Short controlType = formField.getControlType();
							if (fieldName.toLowerCase().equals(
									name.toLowerCase())
									&& controlType.shortValue() == IFieldPool.RELATION_COLUMN_CONTROL) {
								// 找到dialog脚本
								relFormDialog = formField.getRelFormDialog();
								// json加一个属性relFormDialog
								// relFormDialog中包含<![CDATA[]]>。在页面上通过util.js的stripCData方法去掉<![CDATA[]]>
								conditionFieldJson.put("relFormDialog",
										relFormDialog);
								needResetConditionField = true;
								break;
							}
						}
					}
					conditionFieldJsonArrayNew.add(conditionFieldJson);

				}
			}
		}
		if (needResetConditionField == true) {
			conditionField = conditionFieldJsonArrayNew.toString();
		}
		return conditionField;
	}

	/**
	 * 编辑业务数据模板数据.
	 */
	public Map<String, Object> editData(Long displayId, String pk, Long userId,
			String ctxPath) throws Exception {
		Long tableId = 0L;
		Long formKey = 0L;
		String tableName = "";
		String pkField = "";
		String headhtml = "";
		FormDef bpmFormDef = null;
		ISysBusEvent sysBusEvent = null;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		DataTemplate bpmDataTemplate = this.getById(displayId);
		formKey = bpmDataTemplate.getFormKey();
		if (formKey != 0) {
			bpmFormDef = formDefService.getDefaultVersionByFormKey(formKey);
			tableId = bpmFormDef.getTableId();
			FormTable bpmFormTable = formTableService.getById(tableId);
			tableName = bpmFormTable.getTableName();
			/* 查询该记录对应的流程ID */
			String instanceId = "";
			String actDefId = "";
			IProcessRun run = processRunService.getByBusinessKeyAndFormDef(pk,
					bpmFormDef.getFormDefId().toString());
			if (!BeanUtils.isEmpty(run)) {
				instanceId = run.getActInstId();
				// actDefId = run.getActDefId();
			}
			String html = formHandlerService.obtainHtml(bpmFormDef,
					userId, pk, instanceId,
					actDefId, "", ctxPath, "", false);
			// System.out.println(html);
			pkField = bpmFormTable.getPkField();
			bpmFormDef.setHtml(html);
			sysBusEvent = this.sysBusEventService.getByFormKey(formKey
					.toString());
			headhtml = dataTemplateService.getFormHeadHtml(displayId, ctxPath);
		}
		dataMap.put("formKey", formKey);
		dataMap.put("isBackData", bpmDataTemplate.getIsBakData());
		dataMap.put("pkField", pkField);
		dataMap.put("tableId", tableId);
		dataMap.put("tableName", tableName);
		dataMap.put("bpmFormDef", bpmFormDef);
		dataMap.put("sysBusEvent", sysBusEvent);
		dataMap.put("headhtml", headhtml);
		return dataMap;
	}

	
	/**
	 * 对主表、关联表的查询结果进行数据处理
	 * @param allRecords
	 * @param formatData
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAllDataList(
			List<Map<String, Object>> allRecords2,
			Map<String, Object> formatData) throws Exception {

		// 1.日期格式化
		List<Map<String, Object>> dateRecords = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : allRecords2) {
			for (String key : map.keySet()) {
				if (map.get(key) instanceof Date) {
					String date = map.get(key).toString();
					Date rq = DateFormatUtil.parse(date);
					// 获得String类型的日期
					String newValue = DateFormatUtil.formaDatetTime(rq);
					map.put(key, newValue);
				}
			}
			dateRecords.add(map);
		}
		// 日期处理过得到的新的数据集合
		List<Map<String, Object>> dateHandleRecords = dateRecords;
		// 用来存放对单选框、下拉框数据处理后的新的数据集
		List<Map<String, Object>> mainHandleDataList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> m : dateHandleRecords) // 一条主表、关联表记录
		{
			for (Map.Entry<String, Object> entry : m.entrySet()) {
				String key = entry.getKey();
				String newKey = entry.getKey().toLowerCase()
						.replaceAll("m_", "");
				Object oldObj = entry.getValue();
				// 匹配formatdata中字段属性 (去除日期的情况， 测试下拉框、单选框 Map的情形)
				if (formatData.containsKey(newKey)) {
					Object replObj = formatData.get(newKey);// 获取循环的当前数据的key在formatData中对应的value值(如果有)
					if (BeanUtils.isNotEmpty(replObj)) {
						if (replObj instanceof Map) {
							// 下拉框，单选框，密级管理
							Map<?, ?> map = (Map<?, ?>) replObj;
							Object newObj = map.get(String.valueOf(oldObj));
							m.put(key, newObj);
						}
					}
				}
			}
			mainHandleDataList.add(m);
		}
		return mainHandleDataList;
	}
	
	/**
	 * 获取主表对应的所有关联表
	 * @param id(displayId)
	 * @return
	 * @throws Exception
	 */
	public String getRelTables(Long id) throws Exception {
		DataTemplate bpmDataTemplate = dao.getById(id);
		FormTable bpmFormTable = formTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
		
		Long mainTableId = bpmFormTable.getTableId();
		String mainTableName = bpmFormTable.getTableDesc();
		JSONObject mainSon = new JSONObject();
		mainSon.put("ID", mainTableId);
		mainSon.put("name", mainTableName);
		//获取关联表信息
		List<FormTable> relTableList = bpmFormTable.getRelTableList();
		JSONArray relArray = new JSONArray();
		for (int i = 0; i < relTableList.size(); i++) {
			JSONObject relSon = new JSONObject();
			String relTableName = relTableList.get(i).getTableName();
			String relTableDesc = relTableList.get(i).getTableDesc();
			Long relTableId = relTableList.get(i).getTableId();
			relSon.put("ID", relTableId);
			relSon.put("desc", relTableDesc);
			relSon.put("name", relTableName);
			// relObj.put("rel"+(i+1)+"", relSon);
			relArray.add(relSon);
		}
		JSONObject mainRelObj = new JSONObject();
		mainRelObj.put("main", mainSon);
		mainRelObj.put("rel", relArray);
		return mainRelObj.toString();
	}

	/**
	 * 导出含关联表记录的Excel
	 * @param id
	 * @param relTableName
	 * @param relTableId
	 * @return
	 * @throws Exception
	 */
	public String exportMainRel(Long id, String relTableName, Long relTableId)
			throws Exception {
		DataTemplate bpmDataTemplate = dao.getById(id);
		FormTable bpmFormTable = formTableService.getByTableId(
				bpmDataTemplate.getTableId(), 1);
	
		Long mainTableId = bpmFormTable.getTableId();
		String mainTableName = bpmFormTable.getTableName();
		String mainTableDesc = bpmFormTable.getTableDesc();
		//获取关联表中存放主表ID的字段
		String relSaveColumn = relTableDao.getRelSaveColumn(mainTableId,
				relTableId);
		//获取主表、关联表数据(精确到每一行记录的关联)
//		List<Map<String, Object>> allRecords = relTableDao.getAllRelRecords(
//				mainTableName, relTableName, relSaveColumn);
		//获取所得主表、关联表数据的表头(列名/注释)
		ArrayList<String> mainRelTableFieldsDesc = relTableService
				.getMainRelTableFields(mainTableId, relTableId);
		//分别获取 主表、关联表 的列属性(英文 fieldName)
		ArrayList<String> mainTableFieldsName = relTableService
				.getMainTableFieldsName(mainTableId);
		ArrayList<String> relTableFieldsName = relTableService
				.getRelTableFieldsName(relTableId);
		//获取主表、关联表数据
		List<Map<String, Object>> allRecords2 = relTableDao.getAllRecords(
				mainTableName, mainTableFieldsName, relTableName,
				relTableFieldsName, relSaveColumn);
		Map<String, Object> formatData = this.getFormatDataMap(bpmFormTable);
		List<Map<String, Object>> formatRecords = this.getAllDataList(
				allRecords2, formatData);
		//生成Excel
		String fullPath = relTableService.exportExcel(mainTableDesc,
				mainRelTableFieldsDesc, formatRecords);
		return fullPath;
	}
	
	/**
	 * 导出excel
	 * 
	 * @param id
	 * @param exportIds
	 * @param exportType
	 * @param filterKey
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook export(Long id, String[] exportIds, int exportType,
			Map<String, Object> params, Long curUserId) throws Exception {
		
		CommonVar.setCurrentVars(params);

		DataTemplate bpmDataTemplate = dao.getById(id);
		if (BeanUtils.isEmpty(bpmDataTemplate))
			return null;
		FormTable bpmFormTable = formTableService.getByTableId( bpmDataTemplate.getTableId(), 1);
		// 是否有子表
		Boolean isSubTable = bpmFormTable.getSubTableList().size() > 0 ? true : false;

		// 获取权限map
		Map<String, Object> rightMap;
		if(UserContextUtil.getCurrentUser().getUsername().equals("system")){
			rightMap = this.getRightMap(curUserId, null);
		}else{
			Long orgId = UserContextUtil.getCurrentOrg()==null?0L:UserContextUtil.getCurrentOrg().getOrgId();
			rightMap = this.getRightMap(curUserId, orgId);
		}
		//Map<String, Object> rightMap = this.getRightMap(curUserId, curOrgId);

//		Map<String, Boolean> exportFieldMap = this.getFieldPermission(
//				DataTemplate.RIGHT_TYPE_EXPORT,
//				bpmDataTemplate.getExportField(), rightMap);
		Map<String, Boolean> exportFieldMap = this.getFieldPermission(params,bpmDataTemplate.getExportField());
		// 显示的列注释
		Map<String, List<String>> displayFieldDesc = this
				.getDisplayFieldList(bpmDataTemplate.getExportField(),
						exportFieldMap, 0, isSubTable);

		// 显示的字段的名
		Map<String, List<String>> displayFieldName = this
				.getDisplayFieldList(bpmDataTemplate.getExportField(),
						exportFieldMap, 1, isSubTable);
		
		// 获取表
		Map<String, String> tableNameMap = this.getTableName(bpmFormTable);
		// 格式化的数据
		Map<String, Object> formatData = this.getFormatDataMap(bpmFormTable);

		// 如果有选择数据, 设置为不需要分页,导出所有的
		if (exportType == 0 ||exportType == 3)
			bpmDataTemplate.setNeedPage((short) 0);
		// 只有
		if (exportType != 1)
			exportIds = null;

		// 取得当前过滤的条件
		JSONObject filterJson = this.getFilterJson( bpmDataTemplate.getFilterField(), rightMap, params);
		// 主表显示的数据
		bpmDataTemplate = this.getData(bpmDataTemplate, bpmFormTable, rightMap, params, null, formatData, filterJson);

		// 主表数据和从表的数据
		Map<String, List<List<Object>>> dataMapList = this.getDataList( bpmDataTemplate, displayFieldName, exportFieldMap, formatData, bpmFormTable, exportIds);
		//获取表id TODO
		List<Object> idList = dataMapList.get("ids").get(0);
		// 以下开始写excel
		// 创建新的Excel 工作簿
		Excel excel = new Excel();

		int j = 0;
		for (Iterator<Entry<String, List<String>>> it = displayFieldDesc
				.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, List<String>> e = (Map.Entry<String, List<String>>) it
					.next();
			String key = e.getKey();
			String tableDesc = tableNameMap.get(key);
			List<String> list = e.getValue();
			if (j == 0)
				excel.sheet().sheetName(tableDesc);// 重命名当前处于工作状态的表的名称
			else {
				excel.setWorkingSheet(j).sheetName(tableDesc);// 把子表激活
			}
			// 第一行标题 加粗
			excel.row(0, 0).value(list.toArray()).font(new IFontEditor() {
				// 设置字体
				@Override
				public void updateFont(Font font) {
					font.boldweight(BoldWeight.BOLD);// 粗体
				}
			}).bgColor(Color.GREY_25_PERCENT);
			// 取得表的数据
			List<List<Object>> dataList = dataMapList.get(key);

			if (BeanUtils.isNotEmpty(dataList)) {
				// 从第2行写入数据
				for (int i = 0; i < dataList.size(); i++) {
					List<Object> objectList = dataList.get(i);
					excel.row(i + 1).value(objectList.toArray())
							.dataFormat("@");
				}
			}
			j++;
		}

		return excel.getWorkBook();
	}

	@Override
	public void update(IDataTemplate dataTemplate) {
		this.dao.update((DataTemplate)dataTemplate);
	}

	/**
	 * 获取操作权限数据
	 *@author YangBo @date 2016年10月29日下午9:44:22
	 *@param dataTempId
	 *@param userId
	 *@param flag 类别树权限：1,附件列表顶部按钮、管理列按钮权限：2
	 *	3:明细权限
	 *@return
	 */
	public Map<String, Long>  getAttachpermission(Long dataTempId,Long userId,int flag){
		DataTemplate dataTemplate=this.getById(dataTempId);
		Long formKey=dataTemplate.getFormKey();
		return getAttachpermissionByFormKey(formKey,userId,flag);
	}
	public Map<String, Long>  getAttachpermissionByFormKey(Long formKey,Long userId,int flag){
		Map<String, Long> map = new HashMap<String, Long>();
		Map<String, Map<String, String>> permission=formRightsService.getByFormKeyAndUserId(formKey, userId, null, null);
		Map<String, String> attachPermission=permission.get("attach");
		for(String right:attachPermission.keySet()){
			boolean isTreeRight = flag==1&&right.contains("file");  //是否为文件夹权限\类别树
			boolean isAttachRight = flag==2&&(right.contains("attach")||right.contains("manage"));  //是否为附件列表顶部按钮、管理列按钮权限
			if(isTreeRight){
				if(attachPermission.get(right).equals("w")){
					map.put(right, Long.valueOf(1));
				}else{
					map.put(right, Long.valueOf(0));
				}
			}else if(isAttachRight){
				if(attachPermission.get(right).equals("w")){
					map.put(right, Long.valueOf(1));
				}else{
					map.put(right, Long.valueOf(0));
				}
			}
			
		}
		return map;
	}
	/**
	 * 表单数据导出附件
	 * @param request TODO
	 * @param response TODO
	 * @param id
	 * @param exportType
	 * @param params
	 * @param curUserId
	 * @throws Exception
	 */
	public void exportDataAttach(HttpServletRequest request, HttpServletResponse response, Long id, int exportType, Map<String, Object> params, Long curUserId) throws IOException,Exception {
		CommonVar.setCurrentVars(params);
		//获取业务数据信息
    	DataTemplate dataTemplate = dao.getById(id);
    	Long tableId = dataTemplate.getTableId();
		
		//获取表单信息（包括主表，关联表，子表字段list）
    	FormTable mainFormTableDef=formTableService.getByTableId(tableId,1);

		// 获取权限map
		Map<String, Object> rightMap;
		if(UserContextUtil.getCurrentUser().getUsername().equals("system")){
			rightMap = this.getRightMap(curUserId, null);
		}else{
			Long orgId = UserContextUtil.getCurrentOrg()==null?0L:UserContextUtil.getCurrentOrg().getOrgId();
			rightMap = this.getRightMap(curUserId, orgId);
		}

		// 格式化的数据
		Map<String, Object> formatData = this.getFormatDataMap(mainFormTableDef);

		// 取得当前过滤的条件
		JSONObject filterJson = this.getFilterJson( dataTemplate.getFilterField(), rightMap, params);
		// 主表显示的数据
		dataTemplate = this.getData(dataTemplate, mainFormTableDef, rightMap, params, null, formatData, filterJson);
		String pkField = mainFormTableDef.getPkField();
    	//获取表数据
    	List<Map<String, Object>> list = dataTemplate.getList();
    	List<FormTable> relTableList = mainFormTableDef.getRelTableList();
    	List<FormField> fieldList = relTableList.get(0).getFieldList();
    	List<FormField> mainfieldList = mainFormTableDef.getFieldList();
    	//附件字段
    	String fileField = "";
    	for(FormField formField:mainfieldList){
    		short fieldType = formField.getControlType();
    		if(fieldType == 9){
    			//主表附件字段
    			fileField += formField.getFieldName()+",";
    		}
    	}
    	/**关联表附件字段定制区域**/
    	
    	
    	fileField = fileField.substring(0,fileField.length()-1);
    	String[] fileFields = fileField.split(",");
    	//记录列表主键
    	String ids = "";
    	//添加所有附件
    	try {
    		List<ISysFile> fileList = new ArrayList<ISysFile>();
        	for (Map<String, Object> map : list) {
        		ids += map.get(pkField).toString() + ",";
        		for(int i=0;i<fileFields.length;i++){
        			//获取该附件字段的附件id
        			String mainFieldDataId = sysFileService.getMainFieldDataIds(fileFields[i], mainFormTableDef.getTableName(), Long.valueOf(map.get(pkField).toString()), mainFormTableDef, Long.valueOf(1),null);
        			//获取附件列表
        			if(!StringUtil.isEmpty(mainFieldDataId)){
        				List<ISysFile> filedFileList = (List<ISysFile>) sysFileService.getFileByIds(mainFieldDataId);
            			if(filedFileList.size()!=0){
            				this.insertDataToSysFile(map.get(pkField).toString(),filedFileList,fileFields[i]);
                			fileList.addAll(filedFileList);
            			}
        			}
        		}
    		}
    		
        	ids = ids.substring(0,ids.length()-1);
        	System.out.println(ids);
        	//表id
        	String tableIds = String.valueOf(tableId);
        	//附件管理
        	List<ISysFile> sysFileList = (List<ISysFile>) sysFileService.getFileListByDataId(ids, tableIds);
        	if(sysFileList.size()!= 0){
        		fileList.addAll(sysFileList);
        		if(fileList.size() != 0){
        			String fileName = new SimpleDateFormat("yyyyMMddHHmmsssss").format(new Date()) + dataTemplate.getAlias()+ ".zip";
        			//下载压缩包方法
        			this.downByZip(request, response, fileName, fileList);
        		}
        	}
    	} catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	/**
	 *导出数据列表的对应附件包
	 * @param request
	 * @param response
	 * @param fileName
	 * @param sysFileList
	 * @throws IOException
	 */
	public void downByZip (HttpServletRequest request, HttpServletResponse response,String fileName, List<ISysFile> sysFileList) throws IOException {
		
		//压缩包路径
		String fullPath = AppUtil.getAttachPath() + File.separator + fileName;
		File file = new File(fullPath);
		if (!file.exists())
			file.createNewFile();
		// 服务器上创建文件压缩包
		ZipOutputStream zos = new ZipOutputStream(file);
		zos.setEncoding("GBK");
		try{
			for (ISysFile obj : sysFileList) {
				String filename = obj.getDataId() + "_"+ obj.getFilename() + "." + obj.getExt();
				String attachFieldName = obj.getAttachFieldName();
				if(!StringUtil.isEmpty(attachFieldName)){ //附件字段命名
					filename = obj.getDataId() + "_" + attachFieldName + "_" + obj.getFilename() + "." + obj.getExt();
				}
				String filepath = AppUtil.getAttachPath() + File.separator + String.valueOf(obj.getFilepath());
				Boolean isNoGroup = obj.getFilepath().startsWith("group");//判断是否分布式文件
				String interview_server = FastDFSFileOperator.getInterviewServer(); //分布式请求url端口
				if(isNoGroup){
					filepath=interview_server+"/"+obj.getFilepath();
				} else {//分布式暂时不处理解密
					// by weilei:对加密文件进行解密处理
					String destFilePath = sysFileService.getDecodeFilePath(filepath, filename, obj.getIsEncrypt(),false);
					if (!"".equals(destFilePath)) {
						filepath = destFilePath;
					}
					// 删除解密文件
					File destFile = new File(destFilePath);
					if (destFile.exists()) {
						destFile.delete();
					}
				}
				// 将附件压缩进zos压缩包中
				sysFileService.compressFile(zos, filepath, filename);
			}
			zos.flush();
			zos.close();
			// 下载文件到本地，文件源地址为fullPath，下载后名称为fileName
			FileOperator.downLoadFile(request, response, fullPath, fileName);
		} catch(Exception e){
			e.printStackTrace();
		}
		// 删除zip文件
		File zipFile = new File(fullPath);
		if (zipFile.exists())
			zipFile.delete();
	}
	
	/**
	 * 删除数据列表记录下附件文件
	 * by YangBo
	 * @param _pk_
	 */
	@SuppressWarnings("unchecked")
	public void delFileOfData (String _pk_){
		String attachPath = AppUtil.getAttachPath();
		//删除该数据下附件
		List<ISysFile> sysFileList;
		try {
			//获取附件
			sysFileList = (List<ISysFile>) sysFileService.getFileListByDataId(_pk_, null);
			if( sysFileList.size()!=0){
				for (ISysFile sysFile : sysFileList) {
					sysFileService.delFiles(sysFile, attachPath);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 给附件字段附件列表添加数据列表记录和字段名
	 * @param dataId
	 * @param sysFileList
	 * @param attachFieldName
	 * @return
	 */
	public void insertDataToSysFile (String dataId,  List<ISysFile> sysFileList, String attachFieldName){
		for (ISysFile obj : sysFileList) {
			obj.setDataId(dataId);
			obj.setAttachFieldName(attachFieldName);
		}
	}

    @Override
    public Map<String, Object> getFormatDataMap(Long tableId, int tag)
    {
        FormTable bpmFormTable = formTableService.getByTableId(tableId, tag);
        return this.getFormatDataMap(bpmFormTable);
    }
    
    

    @Override
    public String getFilterSql(Long id, String filterKey)
    {
        Long curOrgId = UserContextUtil.getCurrentOrgId();
        Long curUserId = UserContextUtil.getCurrentUserId();

        Map<String,Object> param=new HashMap<String,Object>();
        param.put(DataTemplate.PARAMS_KEY_FILTERKEY, filterKey);
        //权限map
        Map<String, Object> rightMap = this.getRightMap(curUserId, curOrgId);

        // 获取业务数据模板
        DataTemplate dataTemplate = dao.getById(id);
        FormTable formTable = formTableService.getTableById(dataTemplate
            .getTableId());
        
        String source = dataTemplate.getSource();
        String tableName = formTable.getTableName();
        String table = tableName.toLowerCase();
        Map<String, String> tableMap = new HashMap<String, String>();
        tableMap.put(table, fixTableName(tableName, source));
        
        //根据 filterKey 获取过滤条件
        JSONObject filterJson = this.getFilterFieldJson(dataTemplate,
            rightMap, param);
        //父子表关联关联系
        Map<String, Map<String, String>> relationMap = new HashMap<String, Map<String, String>>();
        String filterSql = this.getFilterSQL(filterJson, tableMap, relationMap);
        if(filterSql.indexOf(":[CUR_USER]")>0){
            filterSql=filterSql.replace(":[CUR_USER]", curUserId.toString());
        }
        for(String t:tableMap.keySet()){
            filterSql=filterSql.replace(t.toUpperCase()+".", tableMap.get(t).toString()+".");
        }
        return filterSql;      
    }
    /**
     * 循环manageField中的按钮，根据表单权限进行筛选
     * @param params
     * @param manageField
     * @return
     */
    private String changeManageField(Map<String, Object> params,String manageField){
    	Long funId = Long.valueOf(CommonTools.Obj2String(params.get("funId")));
    	Long pk = Long.valueOf(CommonTools.Obj2String(params.get("__pk__")));
    	String typeAlias = CommonTools.Obj2String(params.get("typeAlias"));
    	Long userId = Long.valueOf(CommonTools.Obj2String(params.get("[CUR_USER]")));
    	List<? extends IRecRoleFun> recRoleFuns = null;
    	List<? extends IRecRoleSonFun> recRoleSonFuns = null;
    	boolean flag = recRoleSonService.isExistRoleSon(pk);
    	if(flag){
    		String roleAlias = recRoleSonService.getAllRoleSonAliasByType(userId, pk);
    		recRoleSonFuns = recRoleSonFunService.getByRoleAliasFun(roleAlias,funId,pk);
    	}else{
    		String roleAlias = recRoleService.getAllRoleAliasByType(userId, typeAlias);
    		recRoleFuns = recRoleFunService.getByRoleAliasFun(roleAlias,funId);
    	}
    	//循环manageField中的按钮，根据表单权限进行筛选
    	JSONArray manageFieldAry = JSONArray.fromObject(manageField);
    	JSONArray removeAry = new JSONArray();
    	if(BeanUtils.isNotEmpty(manageFieldAry)){
    		for(Object obj : manageFieldAry){
    			JSONObject manageF = (JSONObject) obj;
    			String unique = (String)manageF.getString("unique");
    			boolean buttonFlag;
    			if(flag){
    				buttonFlag = isNotContainButton2(unique,recRoleSonFuns);
    			}else{
    				buttonFlag = isNotContainButton(unique,recRoleFuns);
    			}  			
    			if(buttonFlag){
    				removeAry.add(obj);
    			}
    		}
    		manageFieldAry.removeAll(removeAry);
    	}  	
    	return manageFieldAry.toString();
    }    
    /**
     * 默认按钮权限判断
     * @param unique ： 按钮标识
     * @param recRoleFuns ：表单权限
     * @return
     */
    private boolean  isNotContainButton(String unique,List<? extends IRecRoleFun> recRoleFuns){
    	boolean flag =true;
    	if(BeanUtils.isNotEmpty(recRoleFuns)){
    		for(IRecRoleFun rrf : recRoleFuns){
    			String buttons = rrf.getButtons();
    			if(BeanUtils.isNotEmpty(buttons)){
    				JSONArray bArr = JSONArray.fromObject(buttons);
        			for(Object obj : bArr){
        				JSONObject b = (JSONObject) obj;
        				String id = (String)b.get("id");
        				boolean checked = (boolean)b.get("checked");
        				if(id.equals(unique)){
        					if(checked){
        						flag = false;
        					}
        					break;
        				}
        			}
        			if(!flag){
        				break;
        			}
    			}
    		}
    	}
    	return flag;
    }
    /**
     * 记录按钮权限判断
     * @param unique ： 按钮标识
     * @param recRoleSonFuns ：表单权限
     * @return
     */
    private boolean  isNotContainButton2(String unique,List<? extends IRecRoleSonFun> recRoleSonFuns){
    	boolean flag =true;
    	if(BeanUtils.isNotEmpty(recRoleSonFuns)){
    		for(IRecRoleSonFun rrsf : recRoleSonFuns){
    			String buttons = rrsf.getButtons();
    			if(BeanUtils.isNotEmpty(buttons)){
    				JSONArray bArr = JSONArray.fromObject(buttons);
        			for(Object obj : bArr){
        				JSONObject b = (JSONObject) obj;
        				String id = (String)b.get("id");
        				boolean checked = (boolean)b.get("checked");
        				if(id.equals(unique)){
        					if(checked){
        						flag = false;
        					}
        					break;
        				}
        			}
        			if(!flag){
        				break;
        			}
    			}
    		}
    	}
    	return flag;
    }
    
    
    
    /**
     * 根据alias 别名获取业务数据模板。
     * @param alias
     * @return
     */
    public DataTemplate getByAlias(String alias){
        return this.dao.getByAlias(alias);
    }

    @Override
    public String getActInstId(String pk, String displayId)
    {
        if(StringUtil.isEmpty(displayId)||StringUtil.isEmpty(pk)){
            return null;
        }
        DataTemplate template=this.getById(Long.valueOf(displayId));
        if(template==null){
            return null;
        }
        //流程定义ID
        Long defId=template.getDefId();
        if(defId==null){
            return null;
        }
        IDefinition definition=definitionService.getById(defId);
        if(definition==null){
            return null;
        }
        String actDefId=definition.getActDefId();
        List<?extends IProcessRun> processRuns=this.processRunService.getByActDefIdAndPk(actDefId,pk);
        if(processRuns!=null&&processRuns.size()>0){
            return processRuns.get(0).getActInstId();
        }else{
            return null;
        }
        
    }
    /**
     * 获取用户自定义字段
     * @param params
     * @param fieldJson
     * @return
     */
    private  Map<String, Boolean> getFieldPermission(Map<String, Object> params,String fieldJson){
    	JSONArray jsonAry = JSONArray.fromObject(fieldJson);
    	Map<String, Boolean> map = new ListOrderedMap();
    	if(!params.containsKey("colSetting")){
    		return map;
    	}
		if (JSONUtil.isEmpty(jsonAry))
			return map;
		JSONObject userDef_cols = JSONObject.fromObject(params.get("colSetting"));
		for (Object obj1 : jsonAry) {
			JSONObject json1 = JSONObject.fromObject(obj1);
			JSONArray fields = (JSONArray) json1.get("fields");
			String tableName = (String) json1.get("tableName");
			String isMain = (String) json1.get("isMain");
			// 添加主键和外键字段
			String key = tableName
					+ ("1".equals(isMain) ? TableModel.PK_COLUMN_NAME
							: TableModel.FK_COLUMN_NAME);
			map.put(key, true);
			for (Object obj : fields) {
				JSONObject json = JSONObject.fromObject(obj);
				String name = (String) json.get("name");
				map.put(tableName + name,false);
				if(userDef_cols.containsKey(name)){
					JSONObject colObj = JSONObject.fromObject(userDef_cols.get(name));
					if(colObj.getBoolean("checked")){
						map.put(tableName + name,true);		
					}
				}
			}
		}
		return map;
    }
    /**
     * @param fileContainer : fileContainer = { operaterright:{操作权限信息},fileIds:文件fileids}
     * @return ligerGrid所需数据
     * @throws Exception
     */
    public Map<String,Object> getFileGrid(JSONObject fileContainer) throws Exception{
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	//权限信息：不进行处理
    	JSONObject operaterRight = JSONObject.fromObject(fileContainer.getString("operaterRight"));
    	String  fileIds = fileContainer.getString("fileIds");
    	String  fieldName = fileContainer.getString("fieldName");
    	List<? extends ISysFile> list;
    	if(fileIds.equals("")){
    		list = new ArrayList();
    	}else{
    		list = sysFileService.getFileByIds(fileIds);
    	}
    	JSONObject gridData = FileGridUtil.getData(list);
    	JSONArray gridColumns =  FileGridUtil.getColumns();
    	map.put("operaterRight", operaterRight);
    	map.put("data", gridData);
    	map.put("columns", gridColumns);
    	map.put("fieldName", fieldName);
    	return map;
    }

	/**
	 * 拆分subject,返回每一个subject的id
	 *   * 前端传值:subject_1,subject_2,subject_3,subject_4,...
	 * 	 * 期望返回值:[{subject_1:xxxxxxxxxxx},{subject_2:xxxxxxxxxx},{subject_3:xxxxxxxxxx}...]
	 * 	 由于中文可能导致的乱码问题,建议使用getDisplayByFormAlias
	 * @deprecated
	 * @param allSubjects
	 * @return
	 */
	@Override
	public String getDisplayBySubjects(String allSubjects) {
		//拆分alisa为数组
		String subjects[] = allSubjects.split(",");
		StringBuffer displayIds=new StringBuffer("[]");
		for (String subject:subjects){
			//查询id并压入json
			displayIds=pushIdToDisplayId(subject,dao.getIdByFormSubject(subject),displayIds);
		}
		return displayIds.toString();
	}

	/**
	 * 根据IBMS_FORM_DEF.formAlisa查IBMS_DATA_TEMPLATE.Id
	 *
	 * 拆分formAlisa,返回每一个formAlisa的id
	 *   * 前端传值:formAlisa_1,formAlisa_2,formAlisa_3,formAlisa_4,...
	 * 	 * 期望返回值:[{formAlisa_1:xxxxxxxxxxx},{formAlisa_2:xxxxxxxxxx},{formAlisa_3:xxxxxxxxxx}...]
	 * @param allFormAliases
	 * @return
	 */
	@Override
	public String getDisplayIdByFormAliases(String allFormAliases) {
		//拆分alisa为数组
		String formAliases[] = allFormAliases.split(",");
		StringBuffer displayIds=new StringBuffer("[]");
		for (String formAlias:formAliases){
			//查询id并压入json
			displayIds=pushIdToDisplayId(formAlias,dao.getIdByFormAlias(formAlias),displayIds);
		}
		return displayIds.toString();
	}

	//把当前id添加到队列里
	public StringBuffer pushIdToDisplayId(String alias,String id,StringBuffer displayIds){
		Integer strLength=displayIds.length();
		String idJson="{\""+alias+"\":"+id+"}";
		if (strLength==2){
			//检查json是否为空
			displayIds.insert(strLength-1,idJson);
		}else {
			//如果json不为空,就在最末尾加上数字并额外添加前缀","
			displayIds.insert(strLength-1,","+idJson);
		}
		return displayIds;
	}

}
