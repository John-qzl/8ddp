package com.cssrc.ibms.core.form.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import com.cssrc.ibms.core.form.model.CustomRecord;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.form.service.FormDefService;
import com.cssrc.ibms.core.form.service.FormHandlerService;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.form.util.ParseReult;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

import net.sf.json.JSONArray;


/**
 * 对象功能:自定义表单数据处理 
 * 开发人员:zhulongchao  
 */
/**
 * @author liubo
 *
 */
@Controller
@RequestMapping("/oa/form/formHandler/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
@DataNote(beanName=FormTable.class)
public class FormHandlerController extends BaseController {
	@Resource
	private FormHandlerService formHandlerService;

	@Resource
	private FormDefService formDefService;
	
	@Resource
	private FormTableService formTableService;
	
	@Resource
	private DataTemplateDao dao;
	
	@Resource
	private DataTemplateService dataTemplateService;
	
	@Resource
	private JdbcTemplate jdbcTemplate;

	/**
	 * 表单预览
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "表单预览")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String pkValue = request.getParameter("pkValue");
		String returnUrl = RequestUtil.getPrePage(request);
		String ctxPath=request.getContextPath();
		Map<String, Object> params = new HashMap();
		params.put("ctx", ctxPath);
		
		FormDef bpmFormDef = null;
		if (formDefId != 0) {
			bpmFormDef = formDefService.getById(formDefId);	
			String html = formHandlerService.obtainHtml(bpmFormDef, UserContextUtil.getCurrentUserId(), pkValue, "", "#formPrev", "",ctxPath,"",false);
			bpmFormDef.setHtml(html);
		} else {
			String html = request.getParameter("html");
			Long tableId = RequestUtil.getLong(request, "tableId");
			String title = RequestUtil.getString(request, "title");
			String headHtml = RequestUtil.getString(request, "headHtml");
			bpmFormDef = new FormDef();
			bpmFormDef.setSubject(RequestUtil.getString(request, "name"));
			bpmFormDef.setTabTitle(title);
			bpmFormDef.setFormDesc(RequestUtil.getString(request, "comment"));
			bpmFormDef.setHeadHtml(headHtml);
			bpmFormDef.setHtml(html);
			if (tableId > 0) {
				// 读取表。
				FormTable bpmFormTable = formTableService.getTableById(tableId);
				ParseReult result = new ParseReult();
				result.setFormTable(bpmFormTable);
				String template = FormUtil.getFreeMarkerTemplate(html, tableId);
				result.setTemplate(template);
				html = formHandlerService.obtainHtml(title, result, null, true);
				html = html+formHandlerService.getSubPermission(bpmFormTable, false);
				//关系表权限 
				html = html+formHandlerService.getRelPermission(bpmFormTable, false);
				//logger.info(html);
				bpmFormDef.setHtml(html);
			}
		}		
		return getAutoView().addObject("bpmFormDef", bpmFormDef)
				.addObject("returnUrl", returnUrl)
				.addObject("formDefId",formDefId)
				.addObject("headHtml",StringUtil.elReplace(bpmFormDef.getHeadHtml(), params));
	}
	
	/**
	 * 业务表单。
	 * <pre>
	 * 1.输入表单key。
	 * 2.输入主键。
	 * </pre>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("bizForm")
	@Action(description = "显示业务表单。" )
	public ModelAndView bizForm(HttpServletRequest request) throws Exception {
		Long formKey = RequestUtil.getLong(request, "formKey");
		String id = request.getParameter("id");
		boolean hasPk=StringUtil.isNotEmpty(id);
		String returnUrl = RequestUtil.getPrePage(request);
		String ctxPath = request.getContextPath();
		FormDef bpmFormDef = null;
		String tableName="";
		String pkField="";
		if (formKey != 0) {
			bpmFormDef = formDefService.getDefaultVersionByFormKey(formKey);
			FormTable bpmFormTable= formTableService.getById(bpmFormDef.getTableId());
			tableName=bpmFormTable.getTableDesc();
			String html = formHandlerService.obtainHtml(bpmFormDef, UserContextUtil.getCurrentUserId(), id, "", "#formPrev", "", ctxPath, "", false);
			pkField=bpmFormTable.getPkField();
			bpmFormDef.setHtml(html);
		} 
		return getAutoView()
				.addObject("bpmFormDef", bpmFormDef)
				.addObject("id", id)
				.addObject("pkField", pkField)
				.addObject("tableName", tableName)
				.addObject("returnUrl", returnUrl)
				.addObject("hasPk",hasPk);
	}
	
	/**
	 * 删除数据
	 * <pre>
	 * 1.输入表单key。
	 * 2.输入主键。
	 * </pre>
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除数据。" )
	public void del(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String returnUrl=RequestUtil.getPrePage(request);
		Long formKey = RequestUtil.getLong(request, "formKey");
		String id = request.getParameter("id");
		FormDef bpmFormDef = null;
		try{
			if (formKey != 0) {
				bpmFormDef = formDefService.getDefaultVersionByFormKey(formKey);
				FormTable bpmFormTable= formTableService.getById(bpmFormDef.getTableId());			
				formHandlerService.delById(id,bpmFormTable);
				addMessage(new ResultMessage(ResultMessage.Success, "删除业务数据成功"), request);
			}else{
				addMessage(new ResultMessage(ResultMessage.Fail, "删除业务数据失败,没有取得表名"), request);
			}			
		}
		catch(Exception ex){
			ex.printStackTrace();
			addMessage(new ResultMessage(ResultMessage.Fail, "删除业务数据失败"), request);
		}
		response.sendRedirect(returnUrl);
	}
	

	/**
	 * 保存业务数据。
	 * @param request
	 * @param response
	 * @return
	 * {"main":{"fields":{}},"sub":[{"tableName":"CTCsbzjjhsb",
	 * "fields":[{"xh":"1","yqmc":"12","xinh":"12","bh":"1","yjdrq":"2015-10-16","jdzq":"1","jdyxqz":"2015-10-17","jhjdrq":"2015-10-20","jddw":"","sjjdrq":"","jdjg":"","bz":"","id":""}]}],"opinion":[]}
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description = "添加或更新",ownermodel=SysAuditModelType.BUSINESS_MANAGEMENT,execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>业务数据${SysAuditLinkService.getFormTableDesc(tableId,id)}",exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName={FormTable.class},pkName="tableId")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String data = request.getParameter("formData");
		Long tableId= RequestUtil.getLong(request, "tableId");//表单主表id
		String alias = RequestUtil.getString(request, "alias");//表单id
		
	    String __bakData__ =request.getParameter("__bakData__");
        String __remark__ = request.getParameter("__remark__");

		//判断是否有RPC远程过程调用服务
		String rpcrefname = RequestUtil.getString(request, IFieldPool.rpcrefname);
		Map<String,Object> params=new HashMap<String,Object>();
		String id = "";
		Boolean isAdd=false;
		if(StringUtil.isNotEmpty(rpcrefname)){
			//采用IOC方式，根据RPC远程过程调用服务调用数据
			CommonService commonService = (CommonService)  AppUtil.getContext().getBean(rpcrefname);
			FormTable bpmFormTable = (FormTable)commonService.getByTableId(tableId, 1);//1为正常字段加上隐藏字段 
			 id = request.getParameter(bpmFormTable.getPkField());
			 //考虑到bpmFormTable对象属性值多，序列化后不好传输，故不传输,在服务端重新通过tableId获取。
			 id = commonService.saveFormData(null,tableId,1, id, data, alias);
		}else{
			//当不是RPC远程过程调用服务  或者  远程过程调用超时失败，从本地调用
			if(StringUtil.isEmpty(id)){
				//获取该表的相关信息，如main表，sub表，rel表。
				FormTable bpmFormTable=formTableService.getByTableId(tableId, 1);//1为正常字段加上隐藏字段 
				id = request.getParameter(bpmFormTable.getPkField());
				if(StringUtil.isEmpty(id)){
					isAdd=true;
				}
				try{
					//FormData bpmFormData = new FormData(bpmFormTable);
					params.put("__bakData__", __bakData__);
					params.put("__remark__", __remark__);
					id = formHandlerService.saveFormData(bpmFormTable,tableId,1, id, data, alias,params);
					writeResultMessage(response.getWriter(), "保存表单数据成功!"+"&&&*&*"+id, ResultMessage.Success);
				}catch(Exception ex){
					String str = MessageUtil.getMessage();
					if (StringUtil.isNotEmpty(str)) {
						ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,"保存表单数据失败:" + str);
						response.getWriter().print(resultMessage);
					} else {
						String message = ExceptionUtil.getExceptionMessage(ex);
						ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
						response.getWriter().print(resultMessage);
					}
					ex.printStackTrace();
				}
			}else{
				writeResultMessage(response.getWriter(), "保存表单数据成功!", ResultMessage.Success);
			}
		 }
		try {
			LogThreadLocalHolder.putParamerter("isAdd", isAdd);
			LogThreadLocalHolder.putParamerter("tableId", tableId);
			LogThreadLocalHolder.putParamerter("id", id);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	
	/**
	 * 校验有唯一性约束条件的数据
	 * @param request
	 * @param response
	 * @author liubo
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("checkUnique")
	@Action(description="校验表单唯一约束性条件")
	public void checkUnique(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		//获取唯一性约束的列名
		String name = RequestUtil.getString(request,"name");
		String lablename = RequestUtil.getString(request,"lablename");
		String datafmt = RequestUtil.getString(request,"datefmt");
		String[] names = name.split(":");
		String column = names[2];
		column = TableModel.CUSTOMER_COLUMN_PREFIX + column.toUpperCase();
		
		Long displayId = RequestUtil.getLong(request, "displayId");
		//当前所在记录的ID
		Long pkId = RequestUtil.getLong(request, "pkId");
		//具有唯一性约束条件列的值
		String columnValue = request.getParameter("columnValue");
		// 获取DataTemplate
		DataTemplate bpmDataTemplate = dao.getById(displayId);
		// 获取FormTable
		FormTable bpmFormTable = formTableService.getByTableId(
				bpmDataTemplate.getTableId(), FormTable.NEED_HIDE);
		
		String pkField = bpmFormTable.getPkField();
		String tableName = bpmFormTable.getTableName();
		String table = tableName.toLowerCase();
		table = TableModel.CUSTOMER_TABLE_PREFIX.toLowerCase() + table;
		Map<String, String> tableMap = new HashMap<String, String>();
		//将修正后的表名放入map
		if (StringUtils.isEmpty(tableName) || StringUtils.isEmpty(DataTemplate.SOURCE_CUSTOM_TABLE))
			tableMap.put(table,tableName);
		if (DataTemplate.SOURCE_CUSTOM_TABLE.equals(DataTemplate.SOURCE_CUSTOM_TABLE)){
			tableName = TableModel.CUSTOMER_TABLE_PREFIX + tableName.toUpperCase();
			tableMap.put(table,tableName);
		}

		// 查询语句
		StringBuffer checkUniqueSql = new StringBuffer(" SELECT ");
		checkUniqueSql.append(dataTemplateService.getFromTableSQL(pkField, bpmDataTemplate,
				tableName, tableMap, new HashMap()));
		String sql = checkUniqueSql.toString();
		List<?> dataList = jdbcTemplate.queryForList(sql);
		
		ResultMessage resultObj = null;
		//没有违反唯一性约束条件为true
		resultObj = new ResultMessage(1,"校验通过");
		if(!StringUtils.isEmpty(tableName)){
			//循环比较所有数据
			for(int i = 0;i < dataList.size();i++){
				Map map = (Map) dataList.get(i);
				Object oneId = map.get(pkField);
				String oneColumn = CommonTools.Obj2String(map.get(column));
				Long id = Long.valueOf(oneId.toString());
				//除了数据所在记录还存在值相同的情况就返回false
				try{
					if(BeanUtils.isNotEmpty(datafmt)){
						Date date = DateUtil.getDate(oneColumn);
						oneColumn = DateUtil.getDateString(date,datafmt);
					}
					//唯一性校验时去掉首位的空格
					if(!oneColumn.isEmpty()){
						oneColumn = oneColumn.trim();
					}
					if(!columnValue.isEmpty()){
						columnValue = columnValue.trim();
					}
					if(!columnValue.isEmpty()&&id.longValue() != pkId.longValue() && oneColumn.equals(columnValue)){
						resultObj = new ResultMessage(0,"【"+lablename+": "+columnValue+"】  已经存在，请修改！");
						break;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		response.getWriter().print(resultObj);
	}
	
	/**
	 * 打开要打印区域的选择列表页面
	 * @param request
	 * @param response
	 * @param page    请求页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("modulesDialog")
	@Action(description="打开要打印区域的选择列表")
	public ModelAndView modulesDialog(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		ModelAndView mv=this.getAutoView();
		return mv;
	}
	@RequestMapping("updateCustomRecord")
	public void updateCustomRecord(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		try {
			String ibmsRecords = RequestUtil.getString(request, "ibmsRecords");
			List<CustomRecord>  list = CustomRecord.initList(ibmsRecords);
			formHandlerService.updateCustomRecord(list);
			writeResultMessage(response.getWriter(), "数据更新成功!", ResultMessage.Success);
		}catch(Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), e.getMessage(), ResultMessage.Fail);
		}
	}
	@RequestMapping("addCustomRecord")
	public void addCustomRecord(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String ibmsRecords = RequestUtil.getString(request, "ibmsRecords");
			List<CustomRecord>  list = CustomRecord.initList(ibmsRecords);
			formHandlerService.addCustomRecord(list);
			writeResultMessage(response.getWriter(), "数据保存成功!", ResultMessage.Success);
		}catch(Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), e.getMessage(), ResultMessage.Fail);
		}
	}
}
