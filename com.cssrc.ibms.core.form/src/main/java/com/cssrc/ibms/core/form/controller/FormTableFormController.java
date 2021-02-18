package com.cssrc.ibms.core.form.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:自定义表 控制器类  
 * 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/form/formTable/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormTableFormController extends BaseFormController {
	@Resource
	private FormTableService formTableService;

	/**
	 * 添加自定义表。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("saveTable")
	@Action(description="添加自定义表",
			execOrder=ActionExecOrder.AFTER,
			detail="<#if StringUtil.isNotEmpty(isAdd)>" +
						"<#if isAdd==1>添加" +
						"<#else>更新" +
						"</#if>" +
						"自定义表  :【${SysAuditLinkService.getFormTableLink(Long.valueOf(id))}】" +
					"<#else>" +
						"添加或更新自定义表：【表名:${table.tableName}, 表注释:${table.tableDesc}】失败" +
					"</#if>",
			exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName={FormTable.class},pkName="table_tableId")
	public void saveTable(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String tableJson=request.getParameter("table");
		String fieldsJson=request.getParameter("fields");
		int generator = RequestUtil.getInt(request, "generator");
		int isadd= 0;
		try {
			List<FormField> fieldList=getByFormFieldJson(fieldsJson);
			JSONObject tableJsonObj = JSONObject.fromObject(tableJson);
			
			tableJsonObj.remove("createtime");
			tableJsonObj.remove("publishTime");

			FormTable table = (FormTable) JSONObject.toBean(tableJsonObj,FormTable.class);
			
			table.setFieldList(fieldList);
			String msg = "";
			
			//系统日志参数
			try {
				LogThreadLocalHolder.putParamerter("table", table);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
			//如果是新建表，则判断是否已经有同表名的表存在，如存在，则提示错误。
			if (table.getTableId() == 0) {
				if (formTableService.isTableNameExisted(table.getTableName())) {
					msg = getText("controller.FormTable.tableName");
					writeResultMessage(response.getWriter(), msg,ResultMessage.Fail);
					return;
				}
				//添加表数据定义
				int rtn = formTableService.addFormTable(table);
				if (rtn == -1) {
					Object [] args ={TableModel.CUSTOMER_COLUMN_CURRENTUSERID};
					msg = getText("controller.FormTable.reservedField",args);
					writeResultMessage(response.getWriter(), msg,ResultMessage.Fail);
					return;
				}
				isadd=1;
				msg = getText("record.added", getText("controller.FormTable.table"));
			} else {
				boolean isExist = formTableService.isTableNameExistedForUpd(table.getTableId(), table.getTableName());
				if (isExist) {
					//"输入的表名在系统中已经存在!"
					msg = getText("controller.FormTable.already");
					writeResultMessage(response.getWriter(), msg,ResultMessage.Fail);
					return;
				}
				//TODO 更新表的设计。
				int rtn = formTableService.upd(table,generator);
				if (rtn == -1) {
					Object [] args ={TableModel.CUSTOMER_COLUMN_CURRENTUSERID};
					msg = getText("controller.FormTable.reservedField",args);
					writeResultMessage(response.getWriter(), msg,ResultMessage.Fail);
					return;
				} else if (rtn == -2) {
					//自定义数据表中已经有数据，字段不能设置为非空，请检查添加的字段!
					msg = getText("controller.FormTable.hashData");
					writeResultMessage(response.getWriter(), msg,ResultMessage.Fail);
					return;
				} else if (rtn == 0) {
					msg = getText("record.updated", getText("controller.FormTable.table"));
				}
			}
			// 是否需要为已生成的主表 生成新的子表
			if (generator == 1) {
				formTableService.generateTable(table.getTableId(),UserContextUtil.getCurrentUser().getFullname());
			}
			LogThreadLocalHolder.putParamerter("isAdd", String.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id", table.getTableId().toString());
			writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
		} catch (Exception ex) {
			ex.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.update.fail")+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	
	/**
	 * 根据字段的JSON返回字段列表。
	 * @param fieldsJson
	 * @return
	 */
	private List<FormField> getByFormFieldJson(String fieldsJson){
		JSONArray aryJson = JSONArray.fromObject(fieldsJson);
		List<FormField> list = new ArrayList<FormField>();
		for(Object obj : aryJson){
			JSONObject fieldJObject = (JSONObject)obj;
			String options = "";
			String ctlProperty="";
			if (fieldJObject.containsKey("options")) {
				options = fieldJObject.getString("options");
				fieldJObject.remove("options");
			}
			
			if (fieldJObject.containsKey("ctlProperty")) {
				ctlProperty = fieldJObject.getString("ctlProperty");
				fieldJObject.remove("ctlProperty");
				
			}
			////解决表单设计编辑的情况下，由于RelFormDialogStripCData转换为json串出现多个属性后，无法保存的问题。
			if(fieldJObject.containsKey("relFormDialogStripCData")){
				fieldJObject.remove("relFormDialogStripCData");	
			}
			
			FormField FormField = (FormField)JSONObject.toBean(fieldJObject,FormField.class);
			FormField.setOptions(options);
			FormField.setCtlProperty(ctlProperty);
			FormField.setFieldName(StringUtil.trim(FormField.getFieldName(), " "));
			
			list.add(FormField);
		}
		return list;
	}

	@RequestMapping("saveExtTable")
	public void saveExtTable(HttpServletRequest request,HttpServletResponse response) throws Exception {

		String tableJson = request.getParameter("table");
		String fieldsJson = request.getParameter("fields");

		FormTable table = (FormTable) JSONObject.toBean(JSONObject.fromObject(tableJson), FormTable.class);
		//表明转成小写
		table.setTableName(table.getTableName().toLowerCase());
		List<FormField> list= getByFormFieldJson(fieldsJson);
		table.setFieldList(list);
	
		String msg = "";
		try {
			if (table.getTableId() == 0) {
				String tableName = table.getTableName();
				String dsAlias = table.getDsAlias();
				if (formTableService.isTableNameExternalExisted(tableName,dsAlias)) {
					//"表名已存在"
					msg = "表名已存在";
					writeResultMessage(response.getWriter(), msg,ResultMessage.Fail);
					return;
				}
				formTableService.addExt(table);
				msg = "保存成功";
				writeResultMessage(response.getWriter(), msg,ResultMessage.Success);
			}
			else {
				formTableService.updExtTable(table);
				writeResultMessage(response.getWriter(), msg,ResultMessage.Success);
			}
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.FormTable.saveExtTable.fail") + ":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	/**
	 * 通过流程定义标题自动生成流程KEY
	 * 
	 * @param request
	 * @return flowkey
	 * @throws Exception
	 */
	@RequestMapping("getFieldKey")
	public void getFieldKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subject = RequestUtil.getString(request, "subject");
		if (StringUtil.isEmpty(subject))
			return;
		String msg = "";
		String pingyin = PinyinUtil.getPinYinHeadCharFilter(subject);
		msg = pingyin;
		writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
	}

	/**
	 * 通过流程定义标题自动生成流程KEY
	 * 
	 * @param request
	 * @return flowkey
	 * @throws Exception
	 */
	@RequestMapping("getTableKey")
	public void getTableKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableName = RequestUtil.getString(request, "subject");
		Long tableId = RequestUtil.getLong(request, "tableId");
		String msg = "";
		String pingyin = PinyinUtil.getPinYinHeadCharFilter(tableName);
		msg = pingyin;
		try {
			if (tableId == 0) {
				if (formTableService.isTableNameExisted(pingyin)) {
					msg = "表名已存在";
					writeResultMessage(response.getWriter(), msg,
							ResultMessage.Fail);
					return;
				}
			} else {
				boolean isExist = formTableService.isTableNameExistedForUpd(
						tableId, pingyin);
				if (isExist) {
					msg = "表名已存在";
					writeResultMessage(response.getWriter(), msg,
							ResultMessage.Fail);
					return;
				}
			}
			writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
		} catch (Exception ex) {
			writeResultMessage(response.getWriter(), ex.getMessage(),
					ResultMessage.Fail);
		}
	}
}
