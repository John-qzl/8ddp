package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.dao.FormFieldDao;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.util.bean.BeanUtils;


/**
 * 对象功能:自定义表字段 Service类  
 * 开发人员:zhulongchao  
 */
@Service
public class FormFieldService extends BaseService<FormField> implements IFormFieldService
{
	@Resource
	private FormFieldDao dao;

	public FormFieldService()
	{
	}

	@Override
	protected IEntityDao<FormField, Long> getEntityDao()
	{
		return dao;
	}

	/**
	 * 通过tableId查找
	 * 
	 * @param tableId 自定义表Id
	 * @return
	 */
	public List<FormField> getByTableId(Long tableId)
	{
		return dao.getByTableId(tableId);
	}
	
	/**
	 * 通过tableId查找所有（包括已删除的）
	 * 
	 * @param tableId 自定义表Id
	 * @return
	 */
	public List<FormField> getAllByTableId(Long tableId)
	{
		return dao.getAllByTableId(tableId);
	}
	
	/**
	 * 根据表获取所有的字段。**************构造外键显示列***********************
	 * @param tableId
	 * @return
	 */
	public List<FormField> getFieldsByTableId(Long tableId){
		 List<FormField> formFieldList = dao.getFieldsByTableId(tableId);
		 return formFieldList;
	}
	
	/**
	 * 获取所有流程默认自带的变量
	 * @return
	 */
	private List<FormField> getCommonFields(){
		List<FormField> list = new ArrayList<FormField>();
		
		FormField startUser=new FormField();
		startUser.setFieldName("startUser");
		startUser.setFieldDesc(getText("service.bpmFormField.getCommonFields.startUser"));
		startUser.setFieldType(ColumnModel.COLUMNTYPE_NUMBER);
		list.add(startUser);
		
		FormField businessKey=new FormField();
		businessKey.setFieldName("businessKey");
		businessKey.setFieldDesc(getText("service.bpmFormField.getCommonFields.formKey"));
		businessKey.setFieldType(ColumnModel.COLUMNTYPE_VARCHAR);
		list.add(businessKey);
		
		FormField flowRunId=new FormField();
		flowRunId.setFieldName("flowRunId");
		flowRunId.setFieldDesc(getText("service.bpmFormField.getCommonFields.flowRunId"));
		flowRunId.setFieldType(ColumnModel.COLUMNTYPE_INT);
		list.add(flowRunId);
		
		return list;
	}
	
	/**
	 * 根据流程定义ID获取流程变量。
	 * @param defId		流程定义ID
	 * @return
	 */
	public List<FormField> getFlowVarByFlowDefId(Long defId)
	{
		List<FormField> list = getFormVarByFlowDefId(defId);
		list.addAll(getCommonFields());
		parseDateFormat(list);
		return list;
	}
	
	/**
	 * 根据流程定义ID获取流程变量。
	 * @param defId				流程定义ID
	 * @param excludeHidden		排除隐藏变量
	 * @return
	 */
	public List<FormField> getFlowVarByFlowDefId(Long defId,boolean excludeHidden)
	{
		List<FormField> flowVars=new ArrayList<FormField>();
		//获取表单流程变量。
		List<FormField> list = getFormVarByFlowDefId(defId);
		for(FormField field:list){
			//排除重复，并且
			if(excludeHidden && field.isExecutorSelectorHidden()){
				continue;
			}
			flowVars.add(field);
		}
		flowVars.addAll(getCommonFields());
		parseDateFormat(flowVars);
		return flowVars;
	}
	
	public List<FormField> getByTableIdContainHidden(Long tableId){
		return dao.getByTableIdContainHidden(tableId);
	}
	
	/**
	 * 处理日期类型
	 * @param list
	 */
	private void parseDateFormat(List<FormField> list) {
		if(BeanUtils.isEmpty(list)) return;
		for (FormField bpmFormField : list) {
			if(BeanUtils.isEmpty(bpmFormField.getCtlProperty()))
				continue;	
			if(  bpmFormField.getFieldType().equals(ColumnModel.COLUMNTYPE_DATE)||  bpmFormField.getControlType().shortValue() ==IFieldPool.DATEPICKER )
					this.setDateFormat(bpmFormField);
			
		}
	}
	private void setDateFormat(FormField bpmFormField){
		if(BeanUtils.isNotEmpty(bpmFormField.getCtlProperty())){
			JSONObject json = 	JSONObject.fromObject(bpmFormField.getCtlProperty());
			String format = (String) json.get("format");
			bpmFormField.setDatefmt(format);
		}
	}
	
	/**
	 * 根据流程定义ID获取流程表单变量。
	 * @param defId		流程定义ID
	 * @return
	 */
	public List<FormField> getFormVarByFlowDefId(Long defId){
		return dao.getFlowVarByFlowDefId(defId);
	}
	/**
	 * 通过tableId以及relTableId获取关系列。
	 * @param tableId
	 * @return
	 */
	public List<FormField> getRelFieldsByTableId(Long tableId,Long relTableId) {
		
		return dao.getRelFieldsByTableId(tableId,relTableId);
	}
	
	/**
	 * 根据表ID和字段名称取得对应字段的数据（没有隐藏字段）。
	 * @param defId		流程定义ID
	 * @return
	 */
	public FormField getFieldByTidFnaNh(Long tableId,String fieldName,String subTableName){
		return dao.getFieldByTidFnaNh(tableId,fieldName,subTableName);
	}
	

	@Override
	public JSONArray getFiledJSON(IFormTable table, List<? extends IFormField> fields,
			Boolean isMain, Boolean ifFilter) {
		JSONArray jsonArray = new  JSONArray();	
		JSONArray jArray = (JSONArray)JSONArray.fromObject(fields);
		if(!isMain && !ifFilter){ //不是主表并且不是进行过滤
			FormField field = new FormField();
			field.setFieldId(table.getTableId());
			field.setType((short) 1);
			field.setFieldName(table.getTableName());
			field.setFieldDesc(table.getTableDesc());
			jsonArray.add(field);	
		}	
		jsonArray.addAll(jArray);	
		return jsonArray;
	}

	@Override
	public List<FormField> getByTableName(String replace, int i) {
		return null;
	}

	public void getFileds(List<IFormField> userVarList,
			List<IFormField> orgVarList,
			List<IFormField> roleVarList,
			List<IFormField> posVarList,
			List<IFormField> otherList,
			Long defId) {
		//显示表单变量。
		List<FormField> fieldList= this.getFlowVarByFlowDefId(defId);
		for(FormField field:fieldList){
			Short controlType=field.getControlType();
			if(controlType==null) continue;
			if( controlType==IFieldPool.SELECTOR_USER_SINGLE || controlType==IFieldPool.SELECTOR_USER_MULTI ){
				if(field.getIsHidden()!=1 )
					userVarList.add(field);
			}
			else if(controlType==IFieldPool.SELECTOR_ORG_SINGLE || controlType==IFieldPool.SELECTOR_ORG_MULTI ){
				if(field.getIsHidden()!=1 )
					orgVarList.add(field);
			}
			else if(controlType==IFieldPool.SELECTOR_ROLE_SINGLE || controlType==IFieldPool.SELECTOR_ROLE_MULTI ){
				if(field.getIsHidden()!=1 )
					roleVarList.add(field);
			}
			else if(controlType==IFieldPool.SELECTOR_POSITION_SINGLE || controlType==IFieldPool.SELECTOR_POSITION_MULTI ){
				if(field.getIsHidden()!=1 )
					posVarList.add(field);
			}
			else{//普通变量
				if(field.getIsFlowVar()==1) {
					otherList.add(field);
				}
			}
		}
		
	}
	
	/**
	 * 根据 tableid 和 fieldname 获得 FormField
	 * @param tableId
	 * @param fieldName
	 * @return
	 */
	@Override
	public FormField getFieldByTidFna(Long tableId, String fieldName) {
		
		return this.dao.getFieldByTidFna(tableId, fieldName);
	}

	

    /**
     * 获取需要同步表 字段信息。
     * @param tableId
     * @param tableName
     * @return
     */
    @Override
    public List<FormField> getRelFiledByTableIdAndName(String tableId, String tableName)
    {
        return this.dao.getRelFiledByTableIdAndName(tableId,tableName);
    }


	/**
	 * 获取结构化数据
	 * @param fieldName :字段名（不带F_）
	 * @param tableName	：表名（不带W_）
	 * @return
	 * @return:String
	 */
	public String getOpinonData(String fieldName,String tableName) {
		String optionsJson = "";
		String fieldName_up = fieldName.toUpperCase();
		String fieldName_low = fieldName.toLowerCase();
		List<Map> options =  this.dao.getOpinonData(fieldName_up,fieldName_low, tableName);
		if(BeanUtils.isNotEmpty(options)&&options.size()>0){
			
			optionsJson = (String)options.get(0).get("options");
			if(optionsJson==null){
				optionsJson = (String)options.get(0).get("OPTIONS");
			}
		}
		return optionsJson;
	}
	
    /**
     * 根据选择器类型获取 所有相关自定义字段
     * @return
     */
    @Override
    public List<? extends IFormField> getFiledBySelector(Integer[] filedType)
    {
        return this.dao.getFiledBySelector(filedType);

    }
}
