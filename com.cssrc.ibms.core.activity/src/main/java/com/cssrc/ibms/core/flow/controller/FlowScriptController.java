package com.cssrc.ibms.core.flow.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.flow.service.impl.ScriptImpl;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonDateValueProcessor;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;

@Controller
@RequestMapping({"/oa/flow/flowscript/"})
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)

public class FlowScriptController {
	@Resource
	private ScriptImpl scriptImpl;
	@Resource
	private IFormTableService formTableService;
	
	@RequestMapping({"setFormDataValue"})
	@ResponseBody
	@Action(description = "设置静态值", execOrder = ActionExecOrder.BEFORE, detail = "")
	public String setFormDataValue(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String dataIds = RequestUtil.getString(request,"dataId");
		String fieldForData = RequestUtil.getString(request,"fieldForData","id",false);
		String tableName = RequestUtil.getString(request, "tableName");
		String fieldName = RequestUtil.getString(request, "fieldName");
		String value = RequestUtil.getString(request, "value");
		//date
		String fieldType = RequestUtil.getString(request, "fieldType");
		String dateFormat = RequestUtil.getString(request, "dateFormat");
		if("date".equals(fieldType) ){
			scriptImpl.setFormDataValue(dataIds, tableName , fieldName, value, dateFormat, fieldForData); 
		}else{
			IFormTable bpmFormTable=formTableService.getByTableName(tableName.toLowerCase().replace("w_", ""), 1);
			List<?extends IFormField> fieldList=bpmFormTable.getFieldList();
			String[] dataIdArray = dataIds.split(",");
			for (int i = 0; i < dataIdArray.length; i++) {
				if(!dataIdArray[i].equals("")){
					String[] fieldNameArray = fieldName.split(",");
					String[] valueArray = value.split(",");
					Object[] valueA = new Object[valueArray.length];
					//对日期转换的特殊处理
					for(int k=0;k<valueArray.length;k++){
						
						String fieldName1 = fieldNameArray[k%fieldNameArray.length];
						Object value1 = valueArray[k];
						for(IFormField formField:fieldList){
							String formFieldName = formField.getFieldName();
							String formFieldType = formField.getFieldType();
							
							if(formFieldName.toUpperCase().equals(fieldName1.toUpperCase().replace("F_", ""))){
								if(formFieldType.equals(formField.DATATYPE_DATE)){
									//日期转换不正确，改为用DateUtil.getDate() by李青
									//java.util.Date date = DateUtil.parseDate((String) value1);
									java.util.Date date =DateUtil.getDate((String)value1, "yyyy-MM-dd");
									valueA[k] = date;
								}else{
									valueA[k]= valueArray[k];
								}
							}
						}
					}
					scriptImpl.setFormDataValue(dataIdArray[i], tableName, fieldNameArray, valueA, fieldForData); 
				}
			}
		}
		
		return "{success:true}";
	}
	
	@RequestMapping({"setRefFormDataValue"})
	@ResponseBody
	@Action(description = "改变关联表的相关值", execOrder = ActionExecOrder.BEFORE, detail = "")
	public String setRefFormDataValue(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String curDataIds = RequestUtil.getString(request,"curDataId");
		String curTableName = RequestUtil.getString(request, "curTableName");
		String refFieldForData = RequestUtil.getString(request, "refFieldForData");
		String refTableName = RequestUtil.getString(request, "refTableName");
		String refFieldName = RequestUtil.getString(request, "refFieldName");
		String refFieldValue = RequestUtil.getString(request, "refFieldValue");
		//date
		String refFieldType = RequestUtil.getString(request, "refFieldType");
		String refDateFormat = RequestUtil.getString(request, "refDateFormat");
		if("date".equals(refFieldType) ){
			scriptImpl.setRefFormDataValue(curTableName, curDataIds, refFieldForData, refTableName , refFieldName, refFieldValue, refDateFormat); 
		}else{
			IFormTable bpmFormTable=formTableService.getByTableName(refTableName.toLowerCase().replace("w_", ""), 1);
			List<?extends IFormField> fieldList=bpmFormTable.getFieldList();
			String[] dataIdArray = curDataIds.split(",");
			for (int i = 0; i < dataIdArray.length; i++) {
				if(!dataIdArray[i].equals("")){
					String[] fieldNameArray = refFieldName.split(",");
					String[] valueArray = refFieldValue.split(",");
					Object[] valueA = new Object[valueArray.length];
					//对日期转换的特殊处理
					for(int k=0;k<valueArray.length;k++){
						
						String fieldName1 = fieldNameArray[k%fieldNameArray.length];
						Object value1 = valueArray[k];
						for(IFormField formField:fieldList){
							String formFieldName = formField.getFieldName();
							String formFieldType = formField.getFieldType();
							
							if(formFieldName.toUpperCase().equals(fieldName1.toUpperCase().replace("F_", ""))){
								if(formFieldType.equals(formField.DATATYPE_DATE)){
									java.util.Date date = DateUtil.parseDate((String) value1);
									valueA[k] = date;
								}else{
									valueA[k]= valueArray[k];
								}
							}
						}
					}
					scriptImpl.setRefFormDataValue(curTableName, dataIdArray[i], refFieldForData, refTableName, fieldNameArray, valueA); 
				}
			}
		}
		
		return "{success:true}";
	}
	
	@RequestMapping({"getRefDataValue"})
	@ResponseBody   
	@Action(description = "获取从表数据", execOrder = ActionExecOrder.BEFORE, detail = "")
	public String getRefDataValue(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableName = RequestUtil.getString(request, "tableName");
		String fieldName = RequestUtil.getString(request, "fieldName");
		String fieldValue = RequestUtil.getString(request, "fieldValue");
		String orderFieldName = RequestUtil.getString(request, "orderFieldName");
		List<Map<String,Object>> list = scriptImpl.getRefDataValue(tableName, fieldName, fieldValue, orderFieldName);
		
		JSONObject obj = new JSONObject();
		obj.put("success", "true");
		JsonConfig jsonConfig = new JsonConfig();
		//bean->JSON 日期转换
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class,new JsonDateValueProcessor()); 
		jsonConfig.registerJsonValueProcessor(java.sql.Timestamp.class,new JsonDateValueProcessor()); 
		 
		JSONArray ja = JSONArray.fromObject(list,jsonConfig);
		obj.put("result",ja);
		System.out.println(obj.toString());
		return obj.toString();
	}
}
