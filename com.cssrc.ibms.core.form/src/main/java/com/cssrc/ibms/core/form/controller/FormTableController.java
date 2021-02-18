package com.cssrc.ibms.core.form.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.activity.intf.ISubtableRightsService;
import com.cssrc.ibms.api.activity.model.ISubtableRights;
import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.rpc.intf.IDubboInterfaceService;
import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.db.util.JdbcHelperUtil;
import com.cssrc.ibms.core.encrypt.FiledEncryptFactory;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.form.intf.ITableOperator;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormRule;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.FormTableIndex;
import com.cssrc.ibms.core.form.service.FormDefService;
import com.cssrc.ibms.core.form.service.FormFieldService;
import com.cssrc.ibms.core.form.service.FormRuleService;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.form.util.FormDataUtil;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.IDbView;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.impl.TableMetaFactory;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 对象功能:自定义表 控制器类 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/form/formTable/")
@Action(ownermodel = SysAuditModelType.FORM_MANAGEMENT)
public class FormTableController extends BaseController {
	@Resource
	private FormTableService service;// TEST

	@Resource
	private ISubtableRightsService subtableRightsService;

	@Resource
	private FormFieldService bpmFormFieldService;

	@Resource
	private ISysDataSourceService sysDataSourceService;

	@Resource
	private FormRuleService bpmFormRuleService;

	@Resource
	private ISerialNumberService identityService;

	@Resource
	private FreemarkEngine freemarkEngine;

	@Resource
	private Properties configproperties;

	@Resource
	private FormDefService bpmFormDefService;

	@Resource
	private ITableOperator tableOperator;
	
	@Resource
	private IDubboInterfaceService dubboInterfaceService;
	
	/*
	 * @Resource private SysLanguageService sysLanguageService;
	 */

	/**
	 * 导入外部表。
	 * 
	 * @param request
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("defExtTable{viewName}")
	public ModelAndView defExtTable(HttpServletRequest request,
			@PathVariable String viewName) throws Exception {
		ModelAndView mv = this.getAutoView();
		// 选择表
		if (viewName.equals("1")) {
			List<? extends ISysDataSource> dsList = sysDataSourceService
					.getAll();
			mv.addObject("dsList", dsList);
		} else if (viewName.equals("2")) {

			Long tableId = RequestUtil.getLong(request, "tableId");
			String dataSource = RequestUtil.getString(request, "dataSource");
			String tableName = RequestUtil.getString(request, "tableName");

			boolean hasForm = false;

			if (tableId > 0) {
				FormTable formTable = service.getById(tableId);
				// 获取业务表单的主键
				if (formTable != null) {
					mv.addObject("bpmFormTable", formTable);
				}
				dataSource = formTable.getDsAlias();
				tableName = formTable.getTableName();

				Long tmpTableId = tableId;

				// 如果是子表的情况，先取得主表的ID,根据主表id判断是否可以编辑。
				if (formTable.getIsMain() == 0) {
					tmpTableId = formTable.getMainTableId();
				}
				if (tmpTableId != null && tmpTableId > 0)
					hasForm = bpmFormDefService.isTableHasFormDef(tmpTableId);
			}
			List<FormTable> mainTableList = service
					.getAllUnpublishedMainTable();
			mv.addObject("canEditTbColName", false)
					.addObject("tableId", tableId)
					.addObject("hasForm", hasForm)
					.addObject("mainTableList", mainTableList)
					.addObject("dataSource", dataSource)
					.addObject("tableName", tableName);
		}
		return mv;
	}

	/**
	 * 编辑自定义表。
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getAutoView();
		Long tableId = RequestUtil.getLong(request, "tableId");
		String returnUrl = request.getParameter("returnUrl");
		if (returnUrl != null) {
			returnUrl = returnUrl.replace("@", "&");
		}
		boolean canEditTbColName = true;
		// 无表单定义可以做任何的修改。
		if (tableId > 0) {
			//
			Long tmpTableId = tableId;

			FormTable bpmFormTable = service.getById(tmpTableId);
			// 如果是子表的情况，先取得主表的ID,根据主表id判断是否可以编辑。
			if (bpmFormTable.getIsMain().shortValue() == FormTable.NOT_MAIN
					.shortValue()) {
				tmpTableId = bpmFormTable.getMainTableId();
			}
			if (tmpTableId != null && tmpTableId > 0)
				canEditTbColName = !bpmFormDefService
						.isTableHasFormDef(tmpTableId);

		}
		List<FormTable> mainTableList = service.getAllUnpublishedMainTable();
		mv.addObject("canEditTbColName", canEditTbColName)
				.addObject("tableId", tableId)
				.addObject("returnUrl", returnUrl)
				.addObject("mainTableList", mainTableList);
		return mv;
	}

	/**
	 * 将隐藏字段的脚本放到对应字段的scriptID属性中。
	 * 
	 * @param list
	 */
	private void converFieldScript(List<FormField> list) {
		Map<String, FormField> fieldMap = new HashMap<String, FormField>();
		for (FormField field : list) {
			fieldMap.put(field.getFieldName(), field);
		}

		for (Iterator<FormField> it = list.iterator(); it.hasNext();) {
			FormField field = it.next();
			Short controleType = field.getControlType();
			// 是人员选择器
			boolean isSelector = service.isExecutorSelector(controleType);
			short valueFrom = field.getValueFrom().shortValue();
			// 选择器 脚本 隐藏字段。
			if (isSelector
					&& field.getIsHidden().shortValue() == FormField.HIDDEN
					&& field.getIsDeleted().shortValue() == FormField.IS_DELETE_N) {
				if (valueFrom == FormField.VALUE_FROM_SCRIPT_SHOW
						|| valueFrom == FormField.VALUE_FROM_SCRIPT_HIDDEN) {
					String script = field.getScript();
					String fieldId = field.getFieldName();
					fieldId = StringUtil.trimSufffix(fieldId,
							FormField.FIELD_HIDDEN);

					FormField bpmFormField = fieldMap.get(fieldId);
					bpmFormField.setScriptID(script);
				}
				// 删除隐藏字段。
				it.remove();
			}
		}
	}

	/**
	 * 根据表的Id返回表和字段对象。
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getByTableId")
	public Map<String, Object> getByTableId(HttpServletRequest request) {
		Long tableId = RequestUtil.getLong(request, "tableId");
		int mainTableIsPublished = RequestUtil.getInt(request,
				"mainTableIsPublished");
		Map<String, Object> map = new HashMap<String, Object>();
		FormTable bpmFormTable = service.getById(tableId);

		// 子表展示
		if (BeanUtils.isNotEmpty(bpmFormTable)
				&& bpmFormTable.getIsMain().shortValue() == 0) {
			Long mainTableId = bpmFormTable.getMainTableId();
			if (mainTableId > 0) {
				FormTable table = service.getById(mainTableId);
				// 为已发布的主表 添加一个子表时
				if (mainTableIsPublished == 1) {
					bpmFormTable.setMainTableDesc(table.getTableName());
				} else {
					bpmFormTable.setMainTableDesc(StringUtils.isEmpty(table
							.getTableDesc()) ? table.getTableName() : table
							.getTableDesc());
				}
				bpmFormTable.setMainTableName(table.getTableName());
			}

		}
		List<FormField> fieldList = bpmFormFieldService
				.getByTableIdContainHidden(tableId);
		map.put("table", bpmFormTable);
		// 将隐藏字段的脚本放到对应字段的scriptID属性中。
		converFieldScript(fieldList);
		map.put("fieldList", fieldList);

		return map;
	}

	/**
	 * 根据表id获取表相关信息
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getJsonByTableId")
	public String getJsonByTableId(HttpServletRequest request) {
		long tableId = RequestUtil.getLong(request, "tableId", 0L);
		FormTable formTable = this.service.getTableById(Long.valueOf(tableId),
				1);
		JSONObject jsonTable = getByTable(formTable);

		JSONArray children = jsonTable.getJSONArray("children");

		List<FormTable> formTables = formTable.getSubTableList();
		if (BeanUtils.isNotEmpty(formTables)) {
			for (FormTable table : formTables) {
				JSONObject subTable = getByTable(table);
				children.add(subTable);
			}
		}
		return jsonTable.toString();
	}

	private JSONObject getByTable(FormTable bpmFormTable) {
		int isMain = bpmFormTable.getIsMain().intValue();
		JSONObject jsonTable = getJsonObj(bpmFormTable.getTableName(),
				bpmFormTable.getTableName(), bpmFormTable.getTableDesc(), 1,
				isMain, "", bpmFormTable.isExtTable());
		JSONArray aryField = new JSONArray();

		List<FormField> mainList = bpmFormTable.getFieldList();
		for (FormField field : mainList) {
			JSONObject json = getJsonObj(bpmFormTable.getTableName(),
					field.getFieldName(), field.getFieldDesc(), 0, isMain,
					field.getFieldType(), bpmFormTable.isExtTable());
			aryField.add(json);
		}
		jsonTable.accumulate("children", aryField);

		return jsonTable;
	}

	private JSONObject getJsonObj(String tableName, String name,
			String comment, int isTable, int isMain, String dataType,
			boolean isExternal) {
		JSONObject json = new JSONObject();
		json.accumulate("name", name).accumulate("comment", comment)
				.accumulate("tableName", tableName)
				.accumulate("isTable", isTable).accumulate("isMain", isMain)
				.accumulate("isExternal", isExternal ? 1 : 0)
				.accumulate("dataType", dataType);
		return json;
	}

	/**
	 * 表选择对话框。
	 * 
	 * <pre>
	 *  只选择自定义表。
	 * </pre>
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("dialog")
	public ModelAndView dialog(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getAutoView();

		QueryFilter queryFilter = new QueryFilter(request, "bpmFormTableItem");
		// 只查询自定义表。
		queryFilter.addFilterForIB("isPublished", 1);
		List<FormTable> bpmFormTableList = service.getAllMainTable(queryFilter);
		mv.addObject("bpmFormTableList", bpmFormTableList);
		return mv;
	}

	/**
	 * 编辑列对话框。
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("columnDialog")
	public ModelAndView columnDialog(HttpServletRequest request)
			throws Exception {
		ModelAndView mv = this.getAutoView();
		int isAdd = RequestUtil.getInt(request, "isAdd", 0);
		int isMain = RequestUtil.getInt(request, "isMain", 0);
		// 验证规则
		List<FormRule> validRuleList = bpmFormRuleService.getAll();
        FiledEncryptFactory filedEncryptFactory=AppUtil.getBean(FiledEncryptFactory.class);
        List<String> encryptList =filedEncryptFactory.getEncryptCalculationList();
		// 取系统语言
		// List<SysLanguage> languages = sysLanguageService.getAll();
		// 获取流水号
		// List<ISerialNumber> identityList = identityService.getAll();
		String vers = request.getHeader("USER-AGENT");
		if (vers.indexOf("MSIE 6") != -1) {
			mv = new ModelAndView("/oa/form/formTableColumnDialog.jsp");
		}
		mv.addObject("validRuleList", validRuleList);
		QueryFilter queryFilter = new QueryFilter(request, "bpmFormTableItem");
		// 只查询自定义表。
		queryFilter.setPagingBean(null);// 没有翻页，全部列出来。
		List<FormTable> bpmFormTableList = service.getAllMainTable(queryFilter);
		// mv.addObject("identityList", identityList);
		mv.addObject("isAdd", isAdd).addObject("isMain", isMain)
				.addObject("bpmFormTableList", bpmFormTableList)
				// .addObject("languages",languages)
				.addObject("locale", ContextUtil.getLocale().toString())
				.addObject("encryptList",encryptList);
		return mv;

	}

	/**
	 * 编辑索引。
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("editIndex")
	public ModelAndView editIndex(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getAutoView();
		Long tableId = RequestUtil.getLong(request, "tableId");
		String returnUrl = RequestUtil.getPrePage(request);
		String tableName = RequestUtil.getString(request, "tableName");
		List<FormTableIndex> tableIndexes = tableOperator
				.getIndexesByTable(TableModel.CUSTOMER_TABLE_PREFIX + tableName);
		for (FormTableIndex index : tableIndexes) {
			index.setIndexTable(index.getIndexTable().toUpperCase()
					.substring(TableModel.CUSTOMER_TABLE_PREFIX.length()));
			if (!index.isPkIndex()) {
				index.setIndexName(index.getIndexName().toUpperCase()
						.substring(TableModel.CUSTOMER_INDEX_PREFIX.length()));
			}
			List<String> fields = new ArrayList<String>();
			for (String field : index.getIndexFields()) {
				if (field.toUpperCase().startsWith(
						TableModel.CUSTOMER_COLUMN_PREFIX)) {
					fields.add(field.toUpperCase().substring(
							TableModel.CUSTOMER_COLUMN_PREFIX.length()));
				} else {
					fields.add(field);
				}
			}
			index.setIndexFields(fields);
		}
		FormTable table = service.getById(tableId);
		mv.addObject("table", table).addObject("tableId", tableId)
				.addObject("returnUrl", returnUrl)
				.addObject("tableIndexes", tableIndexes);
		return mv;
	}

	/**
	 * 编辑索引对话框。
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("indexDialog")
	public ModelAndView indexDialog(HttpServletRequest request)
			throws Exception {
		int isAdd = RequestUtil.getInt(request, "isAdd", 0);
		Long tableId = RequestUtil.getLong(request, "tableId");
		String tableName = RequestUtil.getString(request, "tableName");
		String indexName = RequestUtil.getString(request, "indexName");
		boolean pkIndex = RequestUtil.getString(request, "pkIndex")
				.equalsIgnoreCase("true") ? true : false;

		FormTableIndex index;
		if (isAdd == 0) {
			String preTableName = TableModel.CUSTOMER_TABLE_PREFIX + tableName;
			String preIndexName = indexName;
			if (!pkIndex) {
				preIndexName = TableModel.CUSTOMER_INDEX_PREFIX + indexName;
			}
			index = tableOperator.getIndex(preTableName, preIndexName);
			index.setIndexTable(index.getIndexTable().toUpperCase()
					.replaceFirst(TableModel.CUSTOMER_TABLE_PREFIX, ""));
			index.setIndexName(index.getIndexName().toUpperCase()
					.replaceFirst(TableModel.CUSTOMER_INDEX_PREFIX, ""));
			List<String> fields = new ArrayList<String>();
			for (String field : index.getIndexFields()) {
				fields.add(field.toUpperCase().replaceFirst(
						TableModel.CUSTOMER_COLUMN_PREFIX, ""));
			}
			index.setIndexFields(fields);
		} else {
			index = new FormTableIndex();
			index.setIndexTable(tableName);
			index.setIndexFields(new ArrayList<String>());
		}
		List<FormField> tableFileds = bpmFormFieldService.getByTableId(tableId);

		String selectedFields = JSONArray.fromObject(index.getIndexFields())
				.toString();

		ModelAndView mv = this.getAutoView();
		mv.addObject("dbType", getDbType());
		mv.addObject("index", index);
		mv.addObject("tableName", index.getIndexTable());
		mv.addObject("tableFileds", tableFileds);
		mv.addObject("selectedFields", selectedFields);
		mv.addObject("isAdd", isAdd);
		return mv;
	}

	/**
	 * 保存/新建索引
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveIndex")
	@Action(description = "保存/新建索引", detail = "表 【${tableName}】 "
			+ "<#if '1'=isAdd>添加索引【${indexName}(${StringUtils.join(indexFields, \",\")})】"
			+ "<#else>修改索引【${oldIndexName}】为【${indexName}(${StringUtils.join(indexFields, \",\")})】"
			+ "</#if>")
	public void saveIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isAdd = RequestUtil.getInt(request, "isAdd", 0);
		// Long tableId = RequestUtil.getLong(request, "tableId");
		String tableName = RequestUtil.getString(request, "tableName");
		String indexType = RequestUtil.getString(request, "indexType");
		String indexName = RequestUtil.getString(request, "indexName");
		String oldIndexName = RequestUtil.getString(request, "oldIndexName");
		String[] indexFieldsAry = request.getParameterValues("indexFields");

		for (int i = 0; i < indexFieldsAry.length; i++) {
			indexFieldsAry[i] = TableModel.CUSTOMER_COLUMN_PREFIX
					+ indexFieldsAry[i];
		}
		List<String> indexFields = Arrays.asList(indexFieldsAry);

		ResultMessage resultMessage;
		FormTableIndex index = new FormTableIndex();
		index.setIndexName(indexName);
		index.setIndexType(indexType);
		index.setIndexFields(indexFields);
		index.setIndexTable(tableName);
		index.setIndexTable(TableModel.CUSTOMER_TABLE_PREFIX
				+ index.getIndexTable());
		index.setIndexName(TableModel.CUSTOMER_INDEX_PREFIX
				+ index.getIndexName());
		try {
			if (isAdd == 1) {
				tableOperator.createIndex(index);
				resultMessage = new ResultMessage(ResultMessage.Success,
						getText("controller.bpmFormTable.saveIndex.success"));
			} else {

				tableOperator.dropIndex(index.getIndexTable(),
						TableModel.CUSTOMER_INDEX_PREFIX + oldIndexName);
				tableOperator.createIndex(index);
				resultMessage = new ResultMessage(ResultMessage.Success,
						getText("controller.bpmFormTable.saveIndex.success"));
			}

		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						getText("controller.save.fail") + ":" + str);
				//response.getWriter().print(resultMessage.toString());
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				//response.getWriter().print(resultMessage.toString());
			}
		}
		writeResultMessage(response.getWriter(), resultMessage);
	}

	@RequestMapping("delIndex")
	@Action(description = "删除自定义表索引", detail = "删除自定义表 【${tableName}】 索引【 ${indexName}】")
	public void delIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String tableName = RequestUtil.getString(request, "tableName");
		String indexName = RequestUtil.getString(request, "indexName");

		ResultMessage resultMessage;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			tableName = TableModel.CUSTOMER_TABLE_PREFIX + tableName;
			indexName = TableModel.CUSTOMER_INDEX_PREFIX + indexName;
			tableOperator.dropIndex(tableName, indexName);
			resultMessage = new ResultMessage(ResultMessage.Success,
					getText("controller.del.success"));
		} catch (Exception ex) {
			ex.printStackTrace();
			resultMessage = new ResultMessage(ResultMessage.Fail,
					getText("controller.del.fail"));
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}

	private String getDbType() {

		return tableOperator.getDbType();
	}

	/**
	 * 选择视图
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selectView")
	public ModelAndView selectView(HttpServletRequest request) throws Exception {
		ModelAndView mv = this.getAutoView();
		List<? extends ISysDataSource> dsList = sysDataSourceService.getAll();
		mv.addObject("dsList", dsList);
		return mv;
	}

	/**
	 * 根据视图名称显示视图字段。
	 * 
	 * @param dsName
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getModelByView")
	public ModelAndView getModelByView(HttpServletRequest request)
			throws Exception {
		String ds = RequestUtil.getString(request, "dataSource");
		String viewName = RequestUtil.getString(request, "selList");

		IDbView idbView = TableMetaFactory.getDbView(ds);
		TableModel tableModel = idbView.getModelByViewName(viewName);

		ModelAndView mv = getAutoView();
		mv.addObject("table", tableModel);

		return mv;
	}

	/**
	 * 编辑视图
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editView")
	public ModelAndView editView(HttpServletRequest request) throws Exception {
		String viewName = RequestUtil.getString(request, "viewName");
		String viewComment = RequestUtil.getString(request, "viewComment");
		String pkName = RequestUtil.getString(request, "pkName");
		String[] aryCol = request.getParameterValues("column");
		String[] aryDbType = request.getParameterValues("dbtype");
		String[] aryComment = request.getParameterValues("comment");
		TableModel tableModel = new TableModel();
		tableModel.setName(viewName);
		tableModel.setComment(viewComment);

		for (int i = 0; i < aryCol.length; i++) {
			ColumnModel colModel = new ColumnModel();
			colModel.setName(aryCol[i]);
			colModel.setColumnType(aryDbType[i]);
			colModel.setComment(aryComment[i]);
			tableModel.addColumnModel(colModel);
		}
		Map map = new HashMap();
		map.put("table", tableModel);
		map.put("pkName", pkName);
		String html = freemarkEngine.mergeTemplateIntoString("view.ftl", map);

		ModelAndView mv = getAutoView();
		mv.addObject("html", html);
		mv.addObject("viewName", viewName);
		return mv;

	}

	/**
	 * 保存视图。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("saveView")
	@Action(description = "保存视图", detail = "保存自定义表视图")
	public void saveView(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String viewpath = configproperties.getProperty("viewpath");
		String viewCharset = configproperties.getProperty("viewCharset");
		String viewName = RequestUtil.getString(request, "viewName");

		String content = request.getParameter("txtViewHtml");
		if (!viewpath.endsWith(File.separator))
			viewpath += File.separator;
		viewpath = viewpath + viewName + ".aspx";

		Pattern regex = Pattern.compile(
				"<div\\s*style=\"display:none\"\\s*>(.*?)</div>",
				Pattern.DOTALL | Pattern.CASE_INSENSITIVE
						| Pattern.UNICODE_CASE);
		Matcher regexMatcher = regex.matcher(content);
		while (regexMatcher.find()) {
			String tag = regexMatcher.group(0);
			String innerContent = regexMatcher.group(1);

			content = content.replace(tag, innerContent);
		}
		content = content.replace("&lt;", "<");
		content = content.replace("&gt;", ">");

		FileOperator.writeFile(viewpath, content, viewCharset);
		PrintWriter writer = response.getWriter();
		ResultMessage resultMessage = new ResultMessage(ResultMessage.Success,
				getText("controller.save.success"));
		writeResultMessage(writer, resultMessage);
	}

	/**
	 * 根据数据源和表的名称获取外部表数据。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getTableModel")
	public Map<String, Object> getTableModel(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String dataSource = RequestUtil.getString(request, "dataSource");
		String tableName = RequestUtil.getString(request, "tableName");
		BaseTableMeta meta = TableMetaFactory.getMetaData(dataSource);
		TableModel tableModel = meta.getTableByName(tableName);

		FormTable table = new FormTable();
		table.setTableName(tableName);
		table.setTableDesc(tableModel.getComment());
		// 取得字段列表
		List<FormField> fieldList = convertFieldList(tableModel);

		List<? extends ISerialNumber> identityList = identityService.getAll();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("identityList", identityList);
		map.put("table", table);
		map.put("fieldList", fieldList);
		map.put("pkList", tableModel.getPrimayKey());
		return map;
	}

	/**
	 * 将表的列表转换为FieldLst。
	 * 
	 * @param tableModel
	 * @return
	 */
	private List<FormField> convertFieldList(TableModel tableModel) {
		List<FormField> fieldList = new ArrayList<FormField>();
		List<ColumnModel> colList = tableModel.getColumnList();
		for (ColumnModel model : colList) {
			// if(model.getIsPk()) continue;
			FormField field = new FormField();
			// field.setIsPk(0);

			field.setFieldName(model.getName());
			field.setFieldDesc(model.getComment());
			field.setCharLen(model.getCharLen());
			field.setIntLen(model.getIntLen());
			field.setDecimalLen(model.getDecimalLen());
			field.setFieldType(model.getColumnType());
			short isRequired = (short) (model.getIsNull() ? 0 : 1);
			field.setIsRequired(isRequired);
			setControlType(field);
			fieldList.add(field);
		}
		return fieldList;
	}

	/**
	 * 设置字段的默认控件类型。
	 * 
	 * @param field
	 */
	private void setControlType(FormField field) {
		String fieldType = field.getFieldType();
		if (fieldType.equalsIgnoreCase(ColumnModel.COLUMNTYPE_VARCHAR)
				|| fieldType.equalsIgnoreCase(ColumnModel.COLUMNTYPE_NUMBER)) {
			field.setControlType(IFieldPool.TEXT_INPUT);
		} else if (fieldType.equalsIgnoreCase(ColumnModel.COLUMNTYPE_DATE)) {
			field.setControlType(IFieldPool.DATEPICKER);
		} else if (fieldType.equalsIgnoreCase(ColumnModel.COLUMNTYPE_CLOB)) {
			field.setControlType(IFieldPool.TEXTAREA);
		}

	}

	/**
	 * 根据表名获取数据库列表。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	/*
	 * @SuppressWarnings("unchecked")
	 * 
	 * @RequestMapping("getTableList")
	 * 
	 * @ResponseBody public Map getTableList(HttpServletRequest request,
	 * HttpServletResponse response) throws IOException {
	 * 
	 * Map resultMap = new HashMap(); try { String ds =
	 * RequestUtil.getString(request, "ds"); String table =
	 * RequestUtil.getString(request, "table"); BaseTableMeta meta =
	 * TableMetaFactory.getMetaData(ds); Map<String, String> map =
	 * meta.getTablesByName(table); List<Schema> schemas =
	 * schemaService.findAll(); Map<String, String> resmap = new
	 * HashMap<String,String>(); for(Map.Entry<String,String>
	 * entry:map.entrySet()){ for(Schema s:schemas){
	 * if(entry.getValue().indexOf("_"+s.getId())>0){ resmap.put(entry.getKey(),
	 * entry.getValue()); } } } resultMap.put("success", "true");
	 * resultMap.put("tables", resmap); } catch (Exception ex) {
	 * resultMap.put("success", "false"); } return resultMap; }
	 */

	@RequestMapping("getTableById")
	@ResponseBody
	public Map<String, Object> getTableById(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("tableId") Long tableId)
			throws Exception {
		Map<String, Object> fields = new HashMap<String, Object>();
		Boolean incHide = RequestUtil.getBoolean(request, "incHide", false);
		int need = 0;
		if (incHide) {
			need = 1;
		}
		FormTable mainTable = service.getByTableId(tableId, need); // need
																	// 0为正常字段
																	// 1为正常字段加上隐藏字段
																	// 2
																	// 为正常字段加上逻辑删除字段
		// 外键显示列构造。
		mainTable = FormDataUtil.appendFKShowColumn(mainTable);

		fields.put("table", mainTable);
		return fields;
	}

	/**
	 * 取得视图列表。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getViewList")
	@ResponseBody
	public Map getViewList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Map resultMap = new HashMap();
		try {
			String ds = RequestUtil.getString(request, "ds");
			String table = RequestUtil.getString(request, "table");
			IDbView idbView = TableMetaFactory.getDbView(ds);
			List list = idbView.getViews(table);
			resultMap.put("success", "true");
			resultMap.put("views", list);
		} catch (Exception ex) {
			resultMap.put("success", "false");
		}
		return resultMap;
	}

	// /**
	// * 设置表关系
	// *
	// * @param request
	// * @param response
	// * @throws Exception
	// */
	// @RequestMapping("setRelation")
	// public ModelAndView setRelation(HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// ModelAndView mv = this.getAutoView();
	// long tableId = RequestUtil.getLong(request, "tableId");
	//
	// String dsName = RequestUtil.getString(request, "dsName");
	// /**
	// * 获取字段
	// */
	// List<FormField> fieldList = bpmFormFieldService
	// .getByTableId(tableId);
	// /**
	// * 获取可以关联的表
	// */
	// List<FormTable> tables = service.getByDsSubTable(dsName);
	//
	// FormTable bpmFormTable = service.getById(tableId);
	//
	// String tableName = bpmFormTable.getTableName();
	//
	// tables.remove(tableName);
	// String pkField = bpmFormTable.getPkField();
	// if (StringUtil.isNotEmpty(pkField))
	// pkField = "";
	// mv.addObject("pkField", bpmFormTable.getPkField());
	// mv.addObject("tables", tables);
	// mv.addObject("fieldList", fieldList);
	//
	// mv.addObject("tableName", tableName);
	// mv.addObject("dataSource", dsName);
	// mv.addObject("relation", bpmFormTable.getTableRelation());
	//
	// return mv;
	// }

	// /**
	// * 保存表之间的关系
	// *
	// * @param request
	// * @param response
	// * @throws Exception
	// */
	// @RequestMapping("saveRelation")
	// public void saveRelation(HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// PrintWriter writer = response.getWriter();
	// try {
	// String relation = request.getParameter("relation");
	// String tablename = RequestUtil.getString(request, "tablename");
	// String dataSource = RequestUtil.getString(request, "dataSource");
	//
	// service.saveRelation(dataSource, tablename, relation);
	//
	// this.writeResultMessage(writer, "设置成功", ResultMessage.Success);
	// } catch (Exception e) {
	// e.printStackTrace();
	// String str = MessageUtil.getMessage();
	// if (StringUtil.isNotEmpty(str)) {
	// ResultMessage resultMessage = new ResultMessage(
	// ResultMessage.Fail, "设置失败:" + str);
	// response.getWriter().print(resultMessage);
	// } else {
	// String message = ExceptionUtil.getExceptionMessage(e);
	// ResultMessage resultMessage = new ResultMessage(
	// ResultMessage.Fail, message);
	// response.getWriter().print(resultMessage);
	// }
	// }
	// }

	/**
	 * 判断表名是否存在。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("isTableNameExternalExisted")
	public void isTableNameExternalExisted(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		String tablename = RequestUtil.getString(request, "tablename");
		String dataSource = RequestUtil.getString(request, "dataSource");
		boolean rtn = service.isTableNameExternalExisted(tablename, dataSource);
		writer.print(rtn);
	}

	/**
	 * 是否有主表
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("isMain")
	public Map<String, Object> isMain(HttpServletRequest request) {
		Long tableId = RequestUtil.getLong(request, "tableId", 0);
		Map<String, Object> map = new HashMap<String, Object>();
		if (tableId != 0) {
			List<FormTable> list = service.getSubTableByMainTableId(tableId);
			map.put("isMain", list.size() > 0 ? true : false);
		} else {
			map.put("isMain", false);
		}
		return map;
	}

	/**
	 * 取得自定义表分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
    @RequestMapping("list")
	@Action(description = "查看自定义表分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", defaultValue = "1") int page)
			throws Exception {
	    String returnUrl = RequestUtil.getUrl(request);
		returnUrl = returnUrl.replace("&", "@");
		List<FormTable> list = service.getAll(new QueryFilter(request,
				"bpmFormTableItem"));
		ModelAndView mv = this.getAutoView()
				.addObject("bpmFormTableList", list)
				.addObject("returnUrl", returnUrl);

		return mv;
	}

	/**
	 * 编辑外部表。
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editExt")
	@Action(description = "编辑外部表")
	public ModelAndView editExt(HttpServletRequest request) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		String returnUrl = RequestUtil.getPrePage(request);
		return getAutoView().addObject("tableId", tableId).addObject(
				"returnUrl", returnUrl);
	}

	/**
	 * 删除外部表定义。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("delTableById")
	@Action(description = "删除外部表定义表", execOrder = ActionExecOrder.BEFORE, detail = "<#assign entity=formTableService.getById(Long.valueOf(tableId))/>"
			+ "删除外部表定义表: ${entity.tableDesc}【${entity.tableName}】")
	public void delTableById(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResultMessage resultMessage;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long tableId = RequestUtil.getLong(request, "tableId");
			service.delExtTableById(tableId);
			resultMessage = new ResultMessage(ResultMessage.Success,
					getText("controller.del.success"));
		} catch (Exception ex) {
			resultMessage = new ResultMessage(ResultMessage.Fail,
					getText("controller.del.fail"));
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 取得自定义表明细
	 * 
	 * @param request
	 * @param response
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看自定义表明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		Long canClose = RequestUtil.getLong(request, "canClose");
		ModelAndView mv = getAutoView();
		String returnUrl = RequestUtil.getPrePage(request);
		FormTable table = service.getById(tableId);
		List<FormField> fields = bpmFormFieldService.getByTableId(tableId);
		mv.addObject("table", table).addObject("fields", fields);
		String mainTable = "未分配";
		if (table.getIsMain() == 1) {
			List<FormTable> subList = service.getSubTableByMainTableId(tableId);
			mv.addObject("subList", subList);
		} else {
			Long mainTableId = table.getMainTableId();
			if (mainTableId > 0) {
				FormTable tb = service.getById(mainTableId);
				if (tb.getIsMain() == 1)
					mainTable = tb.getTableName();
				mv.addObject("mainTable", mainTable);
			}
		}

		return mv.addObject("canClose", canClose).addObject("canReturn", 0)
				.addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得自定义表明细
	 * 
	 * @param request
	 * @param response
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getOfLink")
	@Action(description = "查看自定义表明细")
	public ModelAndView getOfLink(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		Long canReturn = RequestUtil.getLong(request, "canReturn", 1);
		ModelAndView mv = new ModelAndView("/oa/form/formTableGet.jsp");
		FormTable table = service.getById(tableId);
		if (table != null) {
			List<FormField> fields = bpmFormFieldService.getByTableId(tableId);
			mv.addObject("table", table).addObject("fields", fields);
			String mainTable = "未分配";
			if (table.getIsMain() == 1) {
				List<FormTable> subList = service
						.getSubTableByMainTableId(tableId);
				mv.addObject("subList", subList);
			} else {
				Long mainTableId = table.getMainTableId();
				if (mainTableId > 0) {
					FormTable tb = service.getById(mainTableId);
					if (tb.getIsMain() == 1)
						mainTable = tb.getTableName();
					mv.addObject("mainTable", mainTable);
				}
			}
		}
		return mv.addObject("canReturn", canReturn);
	}

	/**
	 * 生成表。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("generateTable")
	@Action(description = "生成表", detail = "生成表【${SysAuditLinkService.getFormTableLink(Long.valueOf(tableId))}】")
	public void publish(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		PrintWriter out = response.getWriter();
		Long tableId = RequestUtil.getLong(request, "tableId");
		try {
			service.generateTable(tableId, UserContextUtil.getCurrentUser()
					.getFullname());
			// 处理关联关系表字段信息 TODO
			// service.dealRelationTable(tableId);

			resultMessage = new ResultMessage(ResultMessage.Success, null);
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						getText("controller.bpmFormTable.publish.fail") + ":"
								+ str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}

	/**
	 * 批量生成表。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("produceTable")
	@Action(description = "批量生成表", detail = "生成表【${SysAuditLinkService.getFormTableLink(Long.valueOf(tableId))}】")
	public void produce(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		PrintWriter out = response.getWriter();

		Long tableId;
		String ws = RequestUtil.getString(request, "tableId");
		String[] pml = ws.split(",");
		Long[] xc = new Long[pml.length];
		for (int i = 0; i < pml.length; i++) {
			xc[i] = Long.valueOf(pml[i]);
			tableId = xc[i];
			try {
				service.generateTable(tableId, UserContextUtil.getCurrentUser()
						.getFullname());
				resultMessage = new ResultMessage(ResultMessage.Success, null);
			} catch (Exception e) {
				e.printStackTrace();
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(ResultMessage.Fail,
							getText("controller.bpmFormTable.publish.fail")
									+ ":" + str);
				} else {
					String message = ExceptionUtil.getExceptionMessage(e);
					resultMessage = new ResultMessage(ResultMessage.Fail,
							message);
				}
			}
		}
		out.print(resultMessage);
	}
	
	/**
	 * 查看子表
	 * 
	 * @param request
	 * @param response
	 * @param tableId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("subTable")
	@Action(description = "查看子表")
	public ModelAndView subTable(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("tableId") Long tableId)
			throws Exception {

		FormTable table = service.getById(tableId);
		List<FormTable> subTables = service.getSubTableByMainTableId(tableId);

		return getAutoView().addObject("table", table).addObject("subTables",
				subTables);
	}

	/**
	 * 关联子表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("linkSubtable")
	public void linkSubtable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResultMessage resultMessage = null;
		PrintWriter out = response.getWriter();
		try {
			Long mainTableId = RequestUtil.getLong(request, "mainTableId");
			Long subTableId = RequestUtil.getLong(request, "subTableId");
			String fkField = request.getParameter("fkField");
			service.linkSubtable(mainTableId, subTableId, fkField);
			resultMessage = new ResultMessage(ResultMessage.Success,
					getText("controller.bpmFormTable.linkSubtable.success"));
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						getText("controller.bpmFormTable.linkSubtable.fail")
								+ ":" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}

	/**
	 * 移除子表关系
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("unlinkSubTable")
	@Action(description = "移除子表关系")
	public void unlinkSubTable(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("subTableId") Long subTableId) throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		service.unlinkSubTable(subTableId);
		response.sendRedirect(preUrl);
	}

	@ResponseBody
	@RequestMapping("getExtTableByTableId")
	public Map<String, Object> getExtTableByTableId(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("tableId") Long tableId)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		FormTable table = service.getById(tableId);
		// 取得字段列表
		List<FormField> fieldList = bpmFormFieldService
				.getAllByTableId(tableId);

		List<? extends ISerialNumber> identityList = identityService.getAll();
		// 验证规则
		List<FormRule> validRuleList = bpmFormRuleService.getAll();
		// 通过子表获得可关联的主表
		map.put("table", table);
		map.put("fieldList", fieldList);

		map.put("identityList", identityList);

		map.put("validRuleList", validRuleList);

		return map;
	}

	/**
	 * 获得所有未分配的子表
	 * 
	 * @param request
	 * @param response
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getAllUnassignedSubTable")
	public List<FormTable> getAllUnassignedSubTable(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("tableName") String tableName) throws Exception {
		List<FormTable> allUnassignedSubTable = service
				.getAllUnassignedSubTable();
		return allUnassignedSubTable;
	}

	/**
	 * 删除自定义表。 如果表已经定义表单，那么表不可以删除。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("delByTableId")
	@Action(description = "删除自定义表", execOrder = ActionExecOrder.BEFORE, detail = "<#assign entity=formTableService.getById(Long.valueOf(tableId))/>"
			+ "自定义表: ${entity.tableDesc}【${entity.tableName}】,"
			+ "<#if !isTableHasFormDef>" + "已删除" + "<#else>不能删除</#if>")
	public void delByTableId(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long[] aryTableId = RequestUtil.getLongAryByStr(request, "tableId");
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		boolean rtn = false;
		for (Long tableId : aryTableId) {
			Long fatherTableId = service.getById(tableId).getMainTableId();
			// 删除的时候 判断是否是子表 ，并且判断子表的父表是否绑定表单
			if (BeanUtils.isNotEmpty(fatherTableId) && fatherTableId != 0) {
				rtn = bpmFormDefService.isTableHasFormDef(fatherTableId);
				if (!rtn) {
					// 表未定义表单，可删除
					service.delByTableId(tableId);
					// 删除表相关字段
					service.delFieldsByTableId(tableId);
				} else if (BeanUtils.isEmpty(message)) {
					message = new ResultMessage(
							ResultMessage.Fail,
							getText("controller.bpmFormTable.delByTableId.pubSupForm"));
				}
			} else {
				rtn = bpmFormDefService.isTableHasFormDef(tableId);
				LogThreadLocalHolder.putParamerter("isTableHasFormDef", rtn);
				if (!rtn) {
					// 表未定义表单，可删除
					service.delByTableId(tableId);
					// 删除表相关字段
					service.delFieldsByTableId(tableId);
				} else if (BeanUtils.isEmpty(message)) {
					message = new ResultMessage(
							ResultMessage.Fail,
							getText("controller.bpmFormTable.delByTableId.definedForm"));
				}
			}
		}
		if (BeanUtils.isEmpty(message)) {
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.bpmFormTable.delByTableId.success"));
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 删除扩展表。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("delExtTableById")
	@Action(description = "删除扩展表", execOrder = ActionExecOrder.BEFORE, detail = "<#assign entity=formTableService.getById(Long.valueOf(tableId))/>"
			+ "扩展表: ${entity.tableDesc}【${entity.tableName}】,"
			+ "<#if isTableHasFormDef>" + "已删除" + "<#else>不能删除</#if>")
	public void delExtTableById(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long tableId = RequestUtil.getLong(request, "tableId");
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		boolean rtn = bpmFormDefService.isTableHasFormDef(tableId);
		LogThreadLocalHolder.putParamerter("isTableHasFormDef", rtn);
		if (rtn) {
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.bpmFormTable.delByTableId.definedForm"));
			addMessage(message, request);
		} else {
			service.delExtTableById(tableId);
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.bpmFormTable.delByTableId.success"));
			addMessage(message, request);
		}
		response.sendRedirect(preUrl);
	}

	/**
	 * 分组设置
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("team")
	@Action(description = "分组设置")
	public ModelAndView team(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		FormTable bpmFormTable = service.getById(tableId);

		String returnUrl = RequestUtil.getPrePage(request);
		return this.getAutoView().addObject("bpmFormTable", bpmFormTable)
				.addObject("returnUrl", returnUrl);

	}

	/**
	 * 分组设置保存
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("teamSave")
	@Action(description = "分组设置保存", detail = "保存自定义表【${SysAuditLinkService.getFormTableLink(Long.valueOf(tableId))}】分组")
	public String teamSave(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String json = RequestUtil.getString(request, "json");
		Long tableId = RequestUtil.getLong(request, "tableId");

		FormTable bpmFormTable = service.getById(tableId);
		JSONObject jsonObject = new JSONObject();
		try {
			bpmFormTable.setTeam(json);
			service.update(bpmFormTable);
			jsonObject.accumulate("success", true).accumulate("msg",
					getText("controller.save.success"));
		} catch (Exception e) {
			jsonObject.accumulate("success", false).accumulate("msg",
					getText("controller.save.fail") + ":" + e.getMessage());
		}
		return jsonObject.toString();
	}

	/**
	 * 取得表fields, 如果是主表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getFieldsByTableId")
	public String getFieldsByTableId(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("tableId") Long tableId)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		List<FormField> bpmFormFieldList = bpmFormFieldService
				.getByTableId(tableId);
		sb.append("{fields:");
		JSONArray jArray = (JSONArray) JSONArray.fromObject(bpmFormFieldList);
		sb.append(jArray.toString()).append("}");

		return sb.toString();
	}

	/**
	 * 复制表。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("copyTable")
	@Action(description = "复制自定义表")
	public ModelAndView copyTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		String addSync = RequestUtil.getString(request, "addSync");
		FormTable bpmFormTable = service.getByTableId(tableId, 0);
		Boolean isSubTable = false;
		if (BeanUtils.isNotEmpty(bpmFormTable)
				&& BeanUtils.isNotEmpty(bpmFormTable.getSubTableList()))
			isSubTable = true;
		return this.getAutoView().addObject("bpmFormTable", bpmFormTable)
				.addObject("isSubTable", isSubTable)
				.addObject("addSync", addSync);
	}

	/**
	 * 复制表。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveCopy")
	@Action(description = "保存复制自定义表", detail = "<#list jsonArray as item>"
			+ "<#if item_index==0>"
			+ "复制自定义表【${SysAuditLinkService.getFormTableLink(Long.valueOf(item.tableId))}】,"
			+ "复制的自定义表名为【${item.tableName}】,表注释为【${item.tableDesc}】   "
			+ "<#else>"
			+ "复制子表【${SysAuditLinkService.getFormTableLink(Long.valueOf(item.tableId))}】,"
			+ "复制的子表名【${item.tableName}】,子表注释【${item.tableDesc}】         "
			+ "</#if>" + "</#list>")
	public void saveCopy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String json = RequestUtil.getString(request, "json");
		boolean addSync = RequestUtil.getBoolean(request, "addSync", false);
		if (StringUtil.isEmpty(json))
			return;
		PrintWriter writer = response.getWriter();
		try {
			Boolean flag = false;
			JSONArray jsonArray = JSONArray.fromObject(json);
			LogThreadLocalHolder.putParamerter("jsonArray", jsonArray);
			String[] nameArray = new String[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				Object object = jsonArray.get(i);
				JSONObject jsonObject = JSONObject.fromObject(object);
				String tableName = (String) jsonObject.get("tableName");
				if (i > 0) {
					Boolean b = ArrayUtils.contains(nameArray, tableName);
					if (b)
						flag = true;
				}
				nameArray[i] = tableName;
			}
			if (!flag) {
				service.saveCopy(json, addSync);
				writeResultMessage(writer,
						getText("controller.bpmFormTable.saveCopy.success"),
						ResultMessage.Success);
			} else {
				writeResultMessage(writer,
						getText("controller.bpmFormTable.saveCopy.repeat"),
						ResultMessage.Fail);
			}

		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, "复制定义表失败" + ":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(
						ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	/**
	 * 分配主表。 取得未分配的主表。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("assignMainTable")
	public ModelAndView assignMainTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long subTableId = RequestUtil.getLong(request, "subTableId");

		FormTable bpmFormTable = service.getById(subTableId);

		String dataSource = bpmFormTable.getDsAlias();
		String tableName = bpmFormTable.getTableName();

		BaseTableMeta meta = TableMetaFactory.getMetaData(dataSource);
		TableModel tableModel = meta.getTableByName(tableName);
		// 取得字段列表
		List<FormField> fieldList = convertFieldList(tableModel);

		List<FormTable> mainTableList = service
				.getMainTableSubTableId(subTableId);// service.getAssignableMainTable();
		ModelAndView mv = getAutoView();
		mv.addObject("mainTableList", mainTableList)
				.addObject("subTableId", subTableId)
				.addObject("fieldList", fieldList);
		return mv;

	}

	/**
	 * 导出选择导出xml
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("export")
	@Action(description = "导出选择导出xml")
	public ModelAndView export(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableIds = RequestUtil.getString(request, "tableIds");

		ModelAndView mv = this.getAutoView();
		mv.addObject("tableIds", tableIds);
		return mv;
	}

	/**
	 * 导出表定义xml。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description = "导出自定义表", detail = "导出自定义表:"
			+ "<#list StringUtils.split(tableIds,\",\") as item>"
			+ "<#assign entity=formTableService.getById(Long.valueOf(item))/>"
			+ "【${entity.tableDesc}(${entity.tableName})】" + "</#list>")
	public void exportXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] tableIds = RequestUtil.getLongAryByStr(request, "tableIds");
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("formTable", true);
		map.put("formField", true);
		map.put("subTable", RequestUtil.getBoolean(request, "subTable"));
		map.put("identity", RequestUtil.getBoolean(request, "identity"));

		if (BeanUtils.isEmpty(tableIds))
			return;
		try {
			String strXml = service.exportXml(tableIds, map);
			String fileName = DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
			if (tableIds.length == 1) {
				FormTable formtable = service.getById(tableIds[0]);
				fileName = formtable.getTableDesc() + "_" + fileName;
			} else
				fileName = "多条表记录_" + fileName;
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.encodingString(fileName, "GBK", "ISO-8859-1")
					+ ".xml");
			response.getWriter().write(strXml);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导入表的XML。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description = "导入自定义表")
	public void importXml(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			service.importXml(fileLoad.getInputStream());
			message = new ResultMessage(ResultMessage.Success, MsgUtil.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.importXml.fail") + "<br/>"
							+ e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 获取流程变量树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getSubTree")
	@ResponseBody
	public String getSubTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String ctx = request.getContextPath();
		Long tableId = RequestUtil.getLong(request, "tableId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		String actDefId = RequestUtil.getString(request, "actDefId");
		StringBuffer sb = new StringBuffer("[");

		if (tableId.longValue() == 0L) {
			sb.append("]");
			return sb.toString();
		}
		List<FormTable> list = service.getSubTableByMainTableId(tableId);

		sb.append("{id:").append("-1").append(",isTable:").append("1")
				.append("").append(",name:\"").append("中间业务表").append("\"")
				.append(",icon:\"").append("").append("\"")
				.append(",showName: \"").append("中间业务表")
				.append("\",children:[");

		sb.append("{id:").append("\"b.BUS_FLOW_RUNID\"").append(",isTable:")
				.append("0").append("").append(",fieldType:\"")
				.append("number").append("\"").append(",name:\"")
				.append("b.BUS_FLOW_RUNID").append("\"").append(",icon:\"")
				.append("").append("\"").append(",showName: \"")
				.append("流程运行ID").append("\"},");

		sb.append("{id:").append("\"b.BUS_CREATOR_ID\"").append(",isTable:")
				.append("0").append("").append(",fieldType:\"")
				.append("number").append("\"").append(",name:\"")
				.append("b.BUS_CREATOR_ID").append("\"").append(",icon:\"")
				.append("").append("\"").append(",showName: \"")
				.append("创建人ID").append("\"},");

		sb.append("{id:").append("\"b.BUS_ORG_ID\"").append(",isTable:")
				.append("0").append("").append(",fieldType:\"")
				.append("number").append("\"").append(",name:\"")
				.append("b.BUS_ORG_ID").append("\"").append(",icon:\"")
				.append("").append("\"").append(",showName: \"").append("组织ID")
				.append("\"}");

		sb.append("]},");

		for (FormTable bpmFormTable : list) {
			List<FormField> bpmFormFieldList = bpmFormFieldService
					.getByTableId(bpmFormTable.getTableId());

			String sign = "<img src='" + ctx + "/styles/tree/no.png'>";
			ISubtableRights bpmSubtableRights = subtableRightsService
					.getByDefIdAndNodeId(actDefId, nodeId,
							bpmFormTable.getTableId());

			if (bpmSubtableRights != null) {
				sign = "<img src='" + ctx + "/styles/tree/yes.png'>";
			}

			sb.append("{id:").append(bpmFormTable.getTableId())
					.append(",isTable:").append("1").append("")
					.append(",name:\"").append(bpmFormTable.getTableName())
					.append("\"").append(",icon:\"").append("").append("\"")
					.append(",img:\"").append(sign).append("\"")
					.append(",showName: \"")
					.append(bpmFormTable.getTableName())
					.append("(" + bpmFormTable.getTableDesc() + ")")
					.append(sign).append("\",children:[");

			for (FormField bpmFormField : bpmFormFieldList) {
				sb.append("{id:").append(bpmFormField.getFieldId())
						.append(",isTable:").append("0").append("")
						.append(",fieldType:\"")
						.append(bpmFormField.getFieldType()).append("\"")
						.append(",name:\"a.f_")
						.append(bpmFormField.getFieldName()).append("\"")
						.append(",icon:\"").append("").append("\"")
						.append(",showName: \"")
						.append(bpmFormField.getFieldName())
						.append("(" + bpmFormField.getFieldDesc() + ")")
						.append("\"},");
			}

			if (!list.isEmpty()) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append("]},");

		}
		if (!list.isEmpty()) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 比较两个列表是否相等。在比较两个列表的元素时，比较的方式为(o==null ? e==null : o.equals(e)).
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	private boolean isListEqual(List list1, List list2) {
		if (list1.size() != list2.size()) {
			return false;
		}
		if (list1.containsAll(list2)) {
			return true;
		}
		return false;
	}

	/**
	 * 重置自定义表。删除已生成的自定义物理表、删除标记为删除的字段。<br/>
	 * 1、已经绑定表单的自定义表不能重置
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("reset")
	@Action(description = "重置自定义表", detail = "重置自定义表【SysAuditLinkService.getFormTableLink(Long.valueOf(tableId))}】")
	public void reset(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Long tableId = RequestUtil.getLong(request, "tableId");
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		boolean rtn = bpmFormDefService.isTableHasFormDef(tableId);
		if (rtn) {
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.bpmFormTable.reset.boundForm"));
		} else {
			service.reset(tableId);
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.bpmFormTable.reset.success"));
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 创建视图页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("createView")
	public ModelAndView createView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<? extends ISysDataSource> sysDataSourceList = sysDataSourceService
				.getAll();
		return getAutoView().addObject("dsList", sysDataSourceList);
	}

	/**
	 * 创建视图
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("create")
	public void create(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String dsalias = RequestUtil.getString(request, "dsalias");
		String sql = RequestUtil.getString(request, "sqlView");
		String viewName = RequestUtil.getString(request, "viewName");
		ResultMessage message = null;
		try {
			IDbView dbView = TableMetaFactory.getDbView(dsalias);
			dbView.createOrRep(viewName, sql);
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.bpmFormTable.createview.success"));
		} catch (Exception e) {
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.bpmFormTable.createview.fail") + ":"
							+ e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}

	/**
	 * 验证创建视图查询语句 是否正确
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("validSql")
	@ResponseBody
	public boolean validSql(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String sql = RequestUtil.getString(request, "sql");
		String dsalias = RequestUtil.getString(request, "dsalias");
		try {
			JdbcHelper<Map<String, Object>, ?> jdbcHelper = JdbcHelperUtil
					.getJdbcHelper(dsalias);
			jdbcHelper.execute(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 根据表ID和字段名称取得对应字段的数据（没有隐藏字段）。
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getFieldByTidFnaNh")
	public void getFieldByTidFnaNh(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		try {
			String fieldName = RequestUtil.getString(request, "fieldName", "");
			long tableId = RequestUtil.getLong(request, "tableId", 0);
			String subTableName = RequestUtil.getString(request,
					"subTableName", "");
			FormField field = null;
			if (StringUtil.isNotEmpty(fieldName) && tableId > 0) {
				field = bpmFormFieldService.getFieldByTidFnaNh(tableId,
						fieldName, subTableName);
				if (BeanUtils.isNotEmpty(field)) {
					JSONObject obj = new JSONObject().fromObject(field);
					writer.print(obj.toString().replaceAll(":null", ":\"\""));
				} else {
					writer.print("");
				}
			} else {
				writer.print("");
			}

		} catch (Exception e) {
			logger.warn(e.getMessage());
			writer.print("");
		} finally {
			writer.close();
		}
	}
	
	@RequestMapping("selector")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return getAutoView();
	}
    /**
	 * 获取结构化数据
	 */
	@RequestMapping("getOpinonData")
	public void getOpinonData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fieldName = RequestUtil.getString(request, "fieldName", "");
		String tableName = RequestUtil.getString(request, "tableName", "htjbb");
		fieldName = fieldName.replace(TableModel.CUSTOMER_COLUMN_PREFIX, "");
		tableName = tableName.replace(TableModel.CUSTOMER_TABLE_PREFIX, "");
		String optionsData = bpmFormFieldService.getOpinonData(fieldName, tableName);
		response.getWriter().write(optionsData);
	}
	
	
	/**
	 * 获取当前应用所有dubbo服务消费者列表
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@RequestMapping("getAllDubboService")
	public List<Map<String,String>> getAllDubboService(HttpServletRequest request,
        HttpServletResponse response){
	    List<Map<String,String>> result=dubboInterfaceService.getReferenceServiceNameList(CommonService.class, true);
	    return result;
	}
	
	
}
