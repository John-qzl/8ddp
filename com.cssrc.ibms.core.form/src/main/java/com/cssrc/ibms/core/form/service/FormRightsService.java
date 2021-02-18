package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRightsService;
import com.cssrc.ibms.api.form.model.IFormRights;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.dao.FormDefDao;
import com.cssrc.ibms.core.form.dao.FormRightsDao;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormRights;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.util.FieldOperateRightUtil;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.string.StringUtil;



/**
 * <pre>
 * 对象功能:表单权限 Service类。 存储表单字段，子表，意见等权限。  
 * 开发人员:zhulongchao  
 * </pre>
 */
@Service
public class FormRightsService implements IFormRightsService{
	@Resource
	private FormRightsDao formRightsDao;
	@Resource
	private FormTableService formTableService;
	@Resource
	private ISysUserService sysUserService; 
	@Resource
	private FormDefDao formDefDao;
	@Resource
	private IPositionService positionService; 
	@Resource
	private ISysOrgService sysOrgService; 
	@Resource
	private ISysRoleService sysRoleService;
	

	/**
	 * 获取默认的权限数据。
	 * 
	 * @param title
	 * @param memo
	 * @param type
	 * 	//type 权限类型(1,字段FieldRights ,2,子表TableRights,,3,意见OpinionRights,
	//4.子表是否显示TableShowRights,5.rel表TableRelRights ,6. rel表是否显示TableRelShowRights)
	 * @return
	 */
	public JSONObject getPermissionJson(String title, String memo,int type) {
		String defJson = "{type:'everyone',id:'', fullname:''}";
		JSONObject json = new JSONObject();
		json.element("title", title);
		json.element("memo", memo);
		switch(type){
		    case FormRights.TableShowRights:
		    case FormRights.TableRelShowRights:
		        json.element("show", "w");
		        break;
            case FormRights.TableRights:
            case FormRights.TableRelRights:
            case FormRights.AttachFileRights:
		    case FormRights.FieldRights:
		    case FormRights.OpinionRights:
		    case FormRights.tableGroupRights:
		        json.element("read", defJson);
	            json.element("write", defJson);
	            if(type ==FormRights.FieldRights && type ==FormRights.OpinionRights&& type ==FormRights.tableGroupRights){
                    json.element("required", "{type:'none',id:'', fullname:''}");
                }
	            break;
		} 
		return json;
	}

	/**
	 * 保存表单权限。
	 * 
	 * @param formDefId
	 *            表单KEY
	 * @param permission
	 *            权限JSON数据。
	 * @throws Exception
	 */
	public void save(FormDef bpmFormDef, JSONObject permission) throws Exception {
		Map<String, FormField[]> selectorFieldMap = formTableService.getExecutorSelectorField(bpmFormDef.getTableId());
		save(null,null,bpmFormDef.getFormKey(),permission,selectorFieldMap,"");
	}
	
	/**
	 * 保存表单权限。
	 * 
	 * @param formDefId
	 *            表单KEY
	 * @param permission
	 *            权限JSON数据。
	 * @throws Exception
	 */
	public void save(long formKey, JSONObject permission) throws Exception {
		save(null,null,formKey,permission, "");
	}

	
	/**
	 * 保存任务节点权限。
	 * 
	 * @param actDefId
	 *            流程定义id
	 * @param formDefId
	 *            表单定义id
	 * @param permission
	 *            权限JSON数据。
	 * @throws Exception
	 */
	public void save(String actDefId, long formKey, JSONObject permission, String parentActDefId)throws Exception {
		save(actDefId,null,formKey,permission,parentActDefId);
	}
	
	/**
	 * 保存任务节点权限。
	 * 
	 * @param actDefId
	 *            流程定义id
	 * @param nodeId
	 *            流程节点id
	 * @param formDefId
	 *            表单定义id
	 * @param permission
	 *            权限JSON数据。
	 * @throws Exception
	 */
	private void save(String actDefId, String nodeId,long formKey,JSONObject permission,Map<String, FormField[]> selectorFieldMap) throws Exception {
		JSONArray fieldPermissions = permission.getJSONArray("field");//主表字段权限
		JSONArray attachPermissions = permission.getJSONArray("fileAttach");//文件附件权限 by YangBo
		JSONArray tablePermissions = permission.getJSONArray("subtable");//子表权限json串
		JSONArray tableRelPermissions = permission.getJSONArray("reltable");//rel表权限json串
		JSONArray opinionPermissions = permission.getJSONArray("opinion");
		JSONArray subTableShows = permission.getJSONArray("subTableShows");//子表是否显示权限json串
		JSONArray relTableShows = permission.getJSONArray("relTableShows");//rel表是否显示权限json串
	    JSONArray tableGroupPermission = permission.getJSONArray("tableGroup");//rel表是否显示权限json串

		List<FormRights> list = new ArrayList<FormRights>();		
		
		// 表单字段权限。
		if(StringUtil.isNotEmpty(nodeId)){
			formRightsDao.delByActDefIdAndNodeId(actDefId, nodeId);	
		}else if(StringUtil.isNotEmpty(actDefId)){
			formRightsDao.delByActDefId(actDefId, false);
		}else{
			formRightsDao.delByFormKey(formKey,false);
		}
		//主表字段权限
		if (BeanUtils.isNotEmpty(fieldPermissions)) {
			for (Object obj : fieldPermissions) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.FieldRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				list.add(bpmFormRights);
				
				if(selectorFieldMap.containsKey(name)){
					String idName =selectorFieldMap.get(name)[1].getFieldName(); 
					JSONObject idJson = JSONObject.fromObject(json);
					idJson.element("title", idName);
					String idJsonStr = idJson.toString();
					FormRights hiddenIdRights =  (FormRights) bpmFormRights.clone();
					hiddenIdRights.setId(UniqueIdUtil.genId());
					hiddenIdRights.setName(idName);
					hiddenIdRights.setPermission(idJsonStr);
					list.add(hiddenIdRights);
				}
			}
		}
	    //表分组权限
        if (BeanUtils.isNotEmpty(tableGroupPermission)) {
            for (Object obj : tableGroupPermission) {
                String json = obj.toString();
                JSONObject jsonObj = (JSONObject) obj;
                String name = (String) jsonObj.get("title");
                FormRights bpmFormRights = new FormRights(
                        UniqueIdUtil.genId(), formKey, name, json,
                        FormRights.tableGroupRights);
                bpmFormRights.setActDefId(actDefId);
                bpmFormRights.setNodeId(nodeId);
                list.add(bpmFormRights);
                
                if(selectorFieldMap.containsKey(name)){
                    String idName =selectorFieldMap.get(name)[1].getFieldName(); 
                    JSONObject idJson = JSONObject.fromObject(json);
                    idJson.element("title", idName);
                    String idJsonStr = idJson.toString();
                    FormRights hiddenIdRights =  (FormRights) bpmFormRights.clone();
                    hiddenIdRights.setId(UniqueIdUtil.genId());
                    hiddenIdRights.setName(idName);
                    hiddenIdRights.setPermission(idJsonStr);
                    list.add(hiddenIdRights);
                }
            }
        }
		//文件夹附件权限 by YangBo
		if(BeanUtils.isNotEmpty(attachPermissions)){
			for (Object obj : attachPermissions) {
				String json = obj.toString();
				if(json.contains("文件夹权限")){
					continue;
				}
				if(json.contains("附件权限")){
					continue;
				}
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights attachRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.AttachFileRights);
				attachRights.setActDefId(actDefId);
				attachRights.setNodeId(nodeId);
				list.add(attachRights);
			}
		}
		
		
		// 子表权限。
		if (BeanUtils.isNotEmpty(tablePermissions)) {
			for (Object obj : tablePermissions) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.TableRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				list.add(bpmFormRights);
			}
		}
		// rel表权限。
		if (BeanUtils.isNotEmpty(tableRelPermissions)) {
					for (Object obj : tableRelPermissions) {
						String json = obj.toString();
						JSONObject jsonObj = (JSONObject) obj;
						String name = (String) jsonObj.get("title");
						FormRights bpmFormRights = new FormRights(
								UniqueIdUtil.genId(), formKey, name, json,
								FormRights.TableRelRights);
						bpmFormRights.setActDefId(actDefId);
						bpmFormRights.setNodeId(nodeId);
						list.add(bpmFormRights);
					}
				}	
		// 表单意见权限。
		if (BeanUtils.isNotEmpty(opinionPermissions)) {
			for (Object obj : opinionPermissions) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.OpinionRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				list.add(bpmFormRights);
			}
		}		

		//子表的显示与否
		if(BeanUtils.isNotEmpty(subTableShows)){
			for (Object obj : subTableShows) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.TableShowRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				list.add(bpmFormRights);				
			}
		}
		//rel表的显示与否
		if(BeanUtils.isNotEmpty(relTableShows)){
			for (Object obj : relTableShows) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.TableRelShowRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				list.add(bpmFormRights);				
			}
		}	
		for (FormRights right : list) {
			formRightsDao.add(right);
		}
	}
	
	/**
	 * 保存任务节点权限。
	 * 
	 * @param actDefId
	 *            流程定义id
	 * @param nodeId
	 *            流程节点id
	 * @param formDefId
	 *            表单定义id
	 * @param permission
	 *            权限JSON数据。
	 * @throws Exception
	 */
	private void save(String actDefId, String nodeId,long formKey,JSONObject permission,Map<String, FormField[]> selectorFieldMap, String parentActDefId) throws Exception {
		JSONArray fieldPermissions = permission.getJSONArray("field");//主表字段权限
		JSONArray tablePermissions = permission.getJSONArray("subtable");//子表权限json串
		JSONArray tableRelPermissions = permission.getJSONArray("reltable");//rel表权限json串
		JSONArray opinionPermissions = permission.getJSONArray("opinion");
		JSONArray subTableShows = permission.getJSONArray("subTableShows");//子表是否显示权限json串
		JSONArray relTableShows = permission.getJSONArray("relTableShows");//rel表是否显示权限json串
		List<FormRights> list = new ArrayList<FormRights>();		
		
		// 表单字段权限。
		if(StringUtil.isNotEmpty(nodeId)){
			formRightsDao.delByActDefIdAndNodeId(actDefId, nodeId, parentActDefId);	
		}else if(StringUtil.isNotEmpty(actDefId)){
			formRightsDao.delByActDefId(actDefId, false, parentActDefId);
		}else{
			formRightsDao.delByFormKey(formKey,false);
		}
		//主表字段权限
		if (BeanUtils.isNotEmpty(fieldPermissions)) {
			for (Object obj : fieldPermissions) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.FieldRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				bpmFormRights.setParentActDefId(parentActDefId);
				list.add(bpmFormRights);
				
				if(selectorFieldMap.containsKey(name)){
					String idName =selectorFieldMap.get(name)[1].getFieldName(); 
					JSONObject idJson = JSONObject.fromObject(json);
					idJson.element("title", idName);
					String idJsonStr = idJson.toString();
					FormRights hiddenIdRights =  (FormRights) bpmFormRights.clone();
					hiddenIdRights.setId(UniqueIdUtil.genId());
					hiddenIdRights.setName(idName);
					hiddenIdRights.setPermission(idJsonStr);
					bpmFormRights.setParentActDefId(parentActDefId);
					list.add(hiddenIdRights);
				}
			}
		}
		// 子表权限。
		if (BeanUtils.isNotEmpty(tablePermissions)) {
			for (Object obj : tablePermissions) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.TableRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				bpmFormRights.setParentActDefId(parentActDefId);
				list.add(bpmFormRights);
			}
		}
		// rel表权限。
		if (BeanUtils.isNotEmpty(tableRelPermissions)) {
				for (Object obj : tableRelPermissions) {
						String json = obj.toString();
						JSONObject jsonObj = (JSONObject) obj;
						String name = (String) jsonObj.get("title");
						FormRights bpmFormRights = new FormRights(
								UniqueIdUtil.genId(), formKey, name, json,
								FormRights.TableRelRights);
						bpmFormRights.setActDefId(actDefId);
						bpmFormRights.setNodeId(nodeId);
						bpmFormRights.setParentActDefId(parentActDefId);
						list.add(bpmFormRights);
					}
				}	
		// 表单意见权限。
		if (BeanUtils.isNotEmpty(opinionPermissions)) {
			for (Object obj : opinionPermissions) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.OpinionRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				bpmFormRights.setParentActDefId(parentActDefId);
				list.add(bpmFormRights);
			}
		}		

		//子表的显示与否
		if(BeanUtils.isNotEmpty(subTableShows)){
			for (Object obj : subTableShows) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.TableShowRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				bpmFormRights.setParentActDefId(parentActDefId);
				list.add(bpmFormRights);				
			}
		}
		//rel表的显示与否
		if(BeanUtils.isNotEmpty(relTableShows)){
			for (Object obj : relTableShows) {
				String json = obj.toString();
				JSONObject jsonObj = (JSONObject) obj;
				String name = (String) jsonObj.get("title");
				FormRights bpmFormRights = new FormRights(
						UniqueIdUtil.genId(), formKey, name, json,
						FormRights.TableRelShowRights);
				bpmFormRights.setActDefId(actDefId);
				bpmFormRights.setNodeId(nodeId);
				bpmFormRights.setParentActDefId(parentActDefId);
				list.add(bpmFormRights);				
			}
		}
		for (FormRights right : list) {
			formRightsDao.add(right);
		}
	}
	
	
	
	/**
	 * 保存任务节点权限。
	 * 
	 * @param actDefId
	 *            流程定义id
	 * @param nodeId
	 *            流程节点id
	 * @param formDefId
	 *            表单定义id
	 * @param permission
	 *            权限JSON数据。
	 * @throws Exception
	 */
	public void save(String actDefId, String nodeId, long formKey,JSONObject permission,String parentActDefId) throws Exception {
		FormDef bpmFormDef = formDefDao.getDefaultVersionByFormKey(formKey);
		Map<String, FormField[]> selectorFieldMap = formTableService.getExecutorSelectorField(bpmFormDef.getTableId());
		//是否有流程节点进行分辨
		if (StringUtil.isEmpty(parentActDefId))
		      save(actDefId, nodeId, bpmFormDef.getFormKey().longValue(), permission, selectorFieldMap);
		    else
		      save(actDefId, nodeId, bpmFormDef.getFormKey().longValue(), permission, selectorFieldMap, parentActDefId);
	}


	
	/**
	 * 根据流程定义id，任务节点id和表单id获取权限数据。
	 * 
	 * @param actDefId
	 *            流程定义ID
	 * @param nodeId
	 *            任务节点
	 * @param formKey
	 *            表单定义ID
	 * @return
	 */
	public Map<String, List<JSONObject>> getPermission(Long formKey, String actDefId, String nodeId) {
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		List<FormRights> rightList = null;
		if(StringUtil.isNotEmpty(nodeId)){
			rightList = formRightsDao.getByActDefIdAndNodeId(formKey,actDefId, nodeId);
		}
		if(StringUtil.isNotEmpty(actDefId) && BeanUtils.isEmpty(rightList)) {
			rightList = formRightsDao.getByActDefId(formKey,actDefId,false);
		}
		if(BeanUtils.isEmpty(rightList)){
			rightList = formRightsDao.getByFormKey(formKey, false);
		}
			
		FormDef bpmFormDef = formDefDao.getDefaultVersionByFormKey(formKey);
		//表单权限的封装
		if (BeanUtils.isEmpty(rightList)) {			
			map = getPermissionByFormKey(bpmFormDef);
		}else{
			map = getPermissionByFormKey(rightList,bpmFormDef);
		}
		return map;
		
		/*FormDef bpmFormDef = bpmFormDefDao.getDefaultVersionByFormKey(formKey);
		rightList = adjustOrder(rightList,bpmFormDef);
		List<JSONObject> fieldJsonList = new ArrayList<JSONObject>();
		List<JSONObject> tableJsonList = new ArrayList<JSONObject>();
		List<JSONObject> opinionJsonList = new ArrayList<JSONObject>();
		for (FormRights rights : rightList) {
			switch (rights.getType()) {
			case FormRights.FieldRights:
				fieldJsonList.add(JSONObject.fromObject(rights.getPermission()));
				break;
			case FormRights.TableRights:
				tableJsonList.add(JSONObject.fromObject(rights.getPermission()));
				break;
			case FormRights.OpinionRights:
				opinionJsonList.add(JSONObject.fromObject(rights.getPermission()));
				break;
			}
		}
		map.put("field", fieldJsonList);
		map.put("table", tableJsonList);
		map.put("opinion", opinionJsonList);
		return map;*/

	}

	
	/**
	 * 根据表ID和表单id获取表表单的权限。
	 * 
	 * @param tableId
	 *            数据表id
	 * @param formKey
	 *            表单定义key
	 * @return
	 */
	public Map<String, List<JSONObject>> getPermissionByTableId(Long tableId) {
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		FormTable bpmFormTable = formTableService.getTableById(tableId);	
		
		// 主表字段权限
		List<FormField> fieldList = bpmFormTable.getFieldList();
		List<JSONObject> fieldJsonList = new ArrayList<JSONObject>();
		for (FormField field : fieldList) {
			JSONObject permission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
			permission.put("tableId", tableId);
			permission.put("tableName", bpmFormTable.getTableName());
			permission.put("mainTableId", "");
			permission.put("mainTableName","");
			fieldJsonList.add(permission);
		}
		map.put("field", fieldJsonList);
		
		//文件附件权限  by YangBo
		JSONArray  attachJSONArry = bpmFormTable.getAttachJSONArry();
/*		List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
		for (FileAttachRights attachField : attachFieldList) {
			JSONObject permission = getPermissionJson(attachField.getRightName(),attachField.getRightMemo(),FormRights.AttachFileRights);
			permission.put("tableId", tableId);
			permission.put("tableName", bpmFormTable.getTableName());
			permission.put("mainTableId", "");
			permission.put("mainTableName","");
			attachJsonList.add(permission);
		}
		map.put("fileAttach", attachJsonList);*/
		List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
		for (Object  obj : attachJSONArry) {
			JSONObject json = JSONObject.fromObject(obj);
			String rightName = (String) json.get("rightName");
			String rightMemo = (String) json.get("rightMemo");
			//给附件权限拼接JSon
			JSONObject permission = getPermissionJson(rightName,rightMemo,FormRights.AttachFileRights);
			permission.put("tableId", tableId);
			permission.put("tableName", bpmFormTable.getTableName());
			permission.put("mainTableId","");
			permission.put("mainTableName","");
			attachJsonList.add(permission);
		}
		map.put("fileAttach", attachJsonList);
		
		
		
		// 子表权限。
		List<JSONObject> tableJsonList = new ArrayList<JSONObject>();
		List<FormTable> tableList = bpmFormTable.getSubTableList();
		for (FormTable table : tableList) {
			// 子表整个表的权限
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableRights);
			//每个子表中的每个字段
			List<FormField> subFieldList = table.getFieldList();
			List<JSONObject> subFieldJsonList = new ArrayList<JSONObject>();
			for (FormField field : subFieldList) {
				JSONObject subPermission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
				subPermission.put("tableId", table.getTableId());
				subPermission.put("tableName", table.getTableName());
				subPermission.put("mainTableId", tableId);
				subPermission.put("mainTableName",bpmFormTable.getTableName());
				subFieldJsonList.add(subPermission);
			}
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", tableId);
			permission.put("mainTableName",bpmFormTable.getTableName());
			permission.put("subField", subFieldJsonList);
			tableJsonList.add(permission);
		}

		map.put("table", tableJsonList);
		// rel关系表权限。
		map = getTableRelPermissionList(map, tableId,bpmFormTable);

		//子表显示与否
		List<JSONObject> tableShowJsonList = new ArrayList<JSONObject>();
		for (FormTable table : tableList) {
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableShowRights);
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", tableId);
			permission.put("mainTableName",bpmFormTable.getTableName());
			tableShowJsonList.add(permission);
		}
		
		map.put("tableShow", tableShowJsonList);
		//rel表显示与否权限
		map = this.getTableRelShowJsonList(map, tableId, bpmFormTable);		
		
		/*List<FormField> fieldList = bpmFormTable.getFieldList();
		List<JSONObject> fieldJsonList = new ArrayList<JSONObject>();
		for (FormField field : fieldList) {
			JSONObject permission = getPermissionJson(field.getFieldName(),
					field.getFieldDesc(),FormRights.FieldRights);
			fieldJsonList.add(permission);
		}
		List<JSONObject> tableJsonList = new ArrayList<JSONObject>();
		List<FormTable> tableList = bpmFormTable.getSubTableList();
		for (FormTable table : tableList) {
			JSONObject permission = getPermissionJson(table.getTableName(),
					table.getTableDesc(),FormRights.TableRights);
			tableJsonList.add(permission);
		}
		map.put("field", fieldJsonList);
		map.put("table", tableJsonList);*/
		return map;
	}
	
	/**
	 * 调整字段顺序
	 * @param rightList
	 * @param bpmFormDef
	 * @return
	 */
	private List<FormRights> adjustOrder(List<FormRights> rightList, FormDef bpmFormDef) {
		if(BeanUtils.isEmpty(bpmFormDef)){
			return rightList;
		}
		List<FormRights> bpmFormRightsList = new ArrayList<FormRights>();
		List<FormRights> formRightsList = new ArrayList<FormRights>();
		List<FormRights> otherRightsList = new ArrayList<FormRights>();
		for (FormRights bpmFormRights : rightList) {
			if(bpmFormRights.getType() == FormRights.FieldRights){
				formRightsList.add(bpmFormRights);		
			}else{
				otherRightsList.add(bpmFormRights);
			}
		}
		FormTable bpmFormTable=formTableService.getTableById(bpmFormDef.getTableId());
		if(BeanUtils.isEmpty(bpmFormTable)){
			return rightList;
		}
		List<FormField> fieldList= bpmFormTable.getFieldList();
		for (FormField bpmFormField : fieldList) {
			for (FormRights bpmFormRights : formRightsList) {
				if(bpmFormRights.getName().equalsIgnoreCase(bpmFormField.getFieldName())){
					bpmFormRights.setSn(bpmFormField.getSn());
					bpmFormRightsList.add(bpmFormRights);
				}
			}
		}
		
		Collections.sort(bpmFormRightsList, new Comparator<FormRights>() {   
			@Override
			public int compare(FormRights o1, FormRights o2) {
			    int a = o1.getSn();   
	            int b = o2.getSn();   
	            return (a < b ? -1 : (a == b ? 0 : 1));  
			}   
	       }); 
		
		//处理其它
		for (FormRights bpmFormRights : otherRightsList) {
			bpmFormRightsList.add(bpmFormRights);
		}
		
		return bpmFormRightsList;
	}
	
	/**
	 * 获取通过表单设定定义表单的权限数据。
	 * 
	 * <pre>
	 * 通过设计定义的表单，
	 * 如果没有还没有设置过权限，那么权限信息可以通过解析表单获取。
	 * </pre>
	 * 
	 * @param formKey
	 * @return
	 */
	
	private Map<String, List<JSONObject>> getPermissionByFormKey(FormDef bpmFormDef) {
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		// 获取模版。
		String html = bpmFormDef.getHtml();
		//获取表单主表id
		Long tableId = bpmFormDef.getTableId();
		// 读取表(sub,rel，文件附件等表关系)。
		FormTable bpmFormTable = formTableService.getTableById(tableId);
		// 主表字段权限
		List<FormField> fieldList = bpmFormTable.getFieldList();
		List<JSONObject> fieldJsonList = new ArrayList<JSONObject>();
		for (FormField field : fieldList) {
			//给字段加默认权限
			JSONObject permission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
			permission.put("tableId", tableId);
			permission.put("tableName", bpmFormTable.getTableName());
			permission.put("mainTableId","");
			permission.put("mainTableName","");
			fieldJsonList.add(permission);
		}
		map.put("field", fieldJsonList);
	      
        //表字段分组权限
        List<JSONObject> tableGroupJsonList = new ArrayList<JSONObject>();
        String team=bpmFormTable.getTeam();
        JSONArray teams=null;
        if(StringUtil.isNotEmpty(team)){
            team=JSON.parseObject(team).get("team").toString();
            teams=JSONArray.fromObject(team);
            for (Object _team : teams) {
                String tkey=((JSONObject)_team).getString("teamNameKey");
                String tname=((JSONObject)_team).getString("teamName");

                JSONObject permission = getPermissionJson(tkey,tname,FormRights.tableGroupRights);
                permission.put("tableId", bpmFormDef.getTableId());
                permission.put("tableName", bpmFormDef.getTableName());
                permission.put("mainTableId","");
                permission.put("mainTableName","");
                tableGroupJsonList.add(permission);
            }
            map.put("tableGroupShows", tableGroupJsonList);
        }
		
		/*		for (FileAttachRights attach : attachList) {
		//给附件权限拼接JSon
		JSONObject permission = getPermissionJson(attach.getRightName(),attach.getRightMemo(),FormRights.AttachFileRights);
		permission.put("tableId", tableId);
		permission.put("tableName", bpmFormTable.getTableName());
		permission.put("mainTableId","");
		permission.put("mainTableName","");
		attachJsonList.add(permission);
	}
	map.put("fileAttach", attachJsonList);*/
		//添加文件附件权限 by YangBo
		List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
		JSONArray attachJSONArry = bpmFormTable.getAttachJSONArry();
		for (Object  obj : attachJSONArry) {
			JSONObject json = JSONObject.fromObject(obj);
			String rightName = (String) json.get("rightName");
			String rightMemo = (String) json.get("rightMemo");
			//给附件权限拼接JSon
			JSONObject permission = getPermissionJson(rightName,rightMemo,FormRights.AttachFileRights);
			permission.put("tableId", tableId);
			permission.put("tableName", bpmFormTable.getTableName());
			permission.put("mainTableId","");
			permission.put("mainTableName","");
			attachJsonList.add(permission);
		}
		map.put("fileAttach", attachJsonList);
		
		

		// 子表权限。
		List<JSONObject> tableJsonList = new ArrayList<JSONObject>();
		List<FormTable> tableList = bpmFormTable.getSubTableList();

		for (FormTable table : tableList) {
			// 子表整个表的权限
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableRights);
			//每个子表中的每个字段
			List<FormField> subFieldList = table.getFieldList();
			List<JSONObject> subFieldJsonList = new ArrayList<JSONObject>();
			for (FormField field : subFieldList) {
				JSONObject subPermission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
				subPermission.put("tableId", table.getTableId());
				subPermission.put("tableName", table.getTableName());
				subPermission.put("mainTableId", tableId);
				subPermission.put("mainTableName", bpmFormTable.getTableName());
				subFieldJsonList.add(subPermission);
			}
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", tableId);
			permission.put("mainTableName", bpmFormTable.getTableName());
			permission.put("subField", subFieldJsonList);
			tableJsonList.add(permission);
		}

		map.put("table", tableJsonList);
		
		// rel关系表权限。
		map =  this.getTableRelPermissionList(map, tableId,bpmFormTable);

		// 意见权限。
		List<JSONObject> opinionJsonList = new ArrayList<JSONObject>();
		Map<String, String> opinionMap = FormUtil.parseOpinion(html);
		Set<Entry<String, String>> set = opinionMap.entrySet();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Entry<String, String> tmp = it.next();
			JSONObject permission = getPermissionJson(tmp.getKey(),tmp.getValue(),FormRights.OpinionRights);
			permission.put("tableId", tableId);
			permission.put("tableName", bpmFormTable.getTableName());
			permission.put("mainTableId","");
			permission.put("mainTableName","");
			opinionJsonList.add(permission);
		}
		map.put("opinion", opinionJsonList);
		
		//子表显示与否权限
		List<JSONObject> tableShowJsonList = new ArrayList<JSONObject>();
		for (FormTable table : tableList) {
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableShowRights);
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", tableId);
			permission.put("mainTableName",bpmFormTable.getTableName());
			tableShowJsonList.add(permission);
		}
		map.put("tableShow", tableShowJsonList);		
		
		//rel表显示与否权限
		map = this.getTableRelShowJsonList(map, tableId, bpmFormTable);		

		return map;
	}
	/**
	 * rel表显示与否权限
	 * */
	private Map<String, List<JSONObject>> getTableRelShowJsonList(Map<String, List<JSONObject>> map,
			Long tableId, FormTable bpmFormTable) {
		List<JSONObject> tableRelShowJsonList = new ArrayList<JSONObject>();
		List<FormTable> tableRelList = bpmFormTable.getRelTableList();
		for (FormTable table : tableRelList) {
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableRelShowRights);
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", tableId);
			permission.put("mainTableName",bpmFormTable.getTableName());
			tableRelShowJsonList.add(permission);
		}

		map.put("tableRelShow", tableRelShowJsonList);
		return map;
	}
	/**
	 * 获取rel关联表权限。
	 */
	private Map<String, List<JSONObject>> getTableRelPermissionList(
			Map<String, List<JSONObject>> map, Long tableId,
			FormTable bpmFormTable) {
		List<JSONObject> tableRelJsonList = new ArrayList<JSONObject>();
		List<FormTable> tableRelList = bpmFormTable.getRelTableList();

		for (FormTable table : tableRelList) {
			// rel关系表整个表的权限
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableRelRights);
			//每个rel关系表中的每个字段
			List<FormField> relFieldList = table.getFieldList();
			List<JSONObject> relFieldJsonList = new ArrayList<JSONObject>();
			for (FormField field : relFieldList) {
				JSONObject relPermission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
				relPermission.put("tableId", table.getTableId());
				relPermission.put("tableName", table.getTableName());
				relPermission.put("mainTableId", tableId);
				relPermission.put("mainTableName", bpmFormTable.getTableName());
				relFieldJsonList.add(relPermission);
			}
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId", tableId);
			permission.put("mainTableName", bpmFormTable.getTableName());
			permission.put("relField", relFieldJsonList);
			tableRelJsonList.add(permission);
		}

		map.put("reltable", tableRelJsonList);
		return map;
	}
	
	  public Map<String, Map<String, String>> getByFormKeyAndUserId(Long formKey, Long userId, String actDefId, String nodeId, String parentActDefId)
	  {
	    return getByFormKeyAndUserId(formKey, userId, actDefId, nodeId, parentActDefId, Integer.valueOf(1));
	  }

	/**
	 * 根据用户获得权限。
	 * 
	 * <pre>
	 * field：{"NAME": "w", "SEX": "r"}
	 * table：{"TABLE1": "r", "TABLE2": "w"}
	 * opinion：{"领导意见": "w", "部门意见": "r"}
	 * </pre>
	 * 
	 * @param formDefId
	 * @param userId
	 * @return
	 * @throws DocumentException
	 */
	public Map<String, Map<String, String>> getByFormKeyAndUserId(Long formKey,
			Long userId, String actDefId, String nodeId) {
		List<FormRights> rightList = null;
		// 如果流程定义id和任务节点id不为空那么获取节点的权限。
		if (StringUtil.isNotEmpty(actDefId) && StringUtil.isNotEmpty(nodeId)) {
			rightList = formRightsDao.getByActDefIdAndNodeId(formKey,actDefId, nodeId);
		}
		if (BeanUtils.isEmpty(rightList) && StringUtil.isNotEmpty(actDefId) ) {
			rightList = formRightsDao.getByActDefId(formKey,actDefId,false);
		}
		if (BeanUtils.isEmpty(rightList)) {
			rightList = formRightsDao.getByFormKey(formKey,false);
		}
		ISysUser user = sysUserService.getById(userId);
		List<?extends ISysRole> roles = sysRoleService.getByUserId(userId);
		List<?extends IPosition> positions = positionService.getByUserId(userId);
		List<?extends ISysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
		//获取可以管理的组织列表。
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);

		Map<String, Map<String, String>> permissions = new HashMap<String, Map<String, String>>();

		Map<String, String> fieldPermission = new HashMap<String, String>();
		Map<String, String> tablePermission = new HashMap<String, String>();
		Map<String, String> tableRelPermission = new HashMap<String, String>();
		Map<String, String> opinionPermission = new HashMap<String, String>();
		Map<String, String> attachPermission = new HashMap<String, String>();
        Map<String, String> tableGroupPermission = new HashMap<String, String>();

		for (FormRights rights : rightList) {
			if(FormRights.TableShowRights!=rights.getType() && FormRights.TableRelShowRights!=rights.getType()){    //子表和rel表显示权限不用进行读写权限判断
				JSONObject permission = JSONObject.fromObject(rights
						.getPermission());
				String name = rights.getName().toLowerCase();
				// 取得权限
				String right = getRight(permission, roles, positions, orgs,
						ownOrgs, userId); 
                Object hightLight = permission.get("hightLight"); 
                
				//只读提交
				if(right.equals("r")){
					boolean rpost=false;
					if( permission.containsKey("rpost")){
						rpost = permission.getBoolean("rpost");
					}
					if(rpost){
						right="rp";
					}
				}
				//用于添加字段控件对应的操作行为权限
				String operateRight = FieldOperateRightUtil.addOperateRight(right, permission, rights.getType());
				String tablename = permission.getString("tableName");
				if(!operateRight.equals("none")) 
					fieldPermission.put(name+"_"+tablename+"_"+FieldOperateRightUtil.OPERATE_RIGHT, operateRight);
				switch (rights.getType()) {
				case FormRights.FieldRights:
					fieldPermission.put(name, right);
					break;
				case FormRights.TableRights:
					//tablePermission.put(name, right=="b"?"w":right);
					tablePermission.put(name, right);
					break;
				case FormRights.TableRelRights:
					//tableRelPermission.put(name, right=="b"?"w":right);
					tableRelPermission.put(name, right);
					break;
				case FormRights.OpinionRights:
					opinionPermission.put(name, right);
					break;
				case FormRights.AttachFileRights:
					attachPermission.put(name, right);
					break;	
				case FormRights.tableGroupRights:
				    JSONObject _right=new JSONObject();
				    _right.put("right", right);
				    _right.put("hightLight", hightLight!=null?hightLight.toString():"");
				    tableGroupPermission.put(name, _right.toString());
                    break;  
                }
			}			
		}
		permissions.put("field", fieldPermission);
		permissions.put("table", tablePermission);
		permissions.put("reltable", tableRelPermission);
		permissions.put("opinion", opinionPermission);
		permissions.put("attach", attachPermission);
        permissions.put("tableGroup", tableGroupPermission);

		return permissions;
	}
	
	 public Map<String, Map<String, String>> getByFormKeyAndUserId(Long formKey, Long userId, String actDefId, String nodeId, String parentActDefId, Integer platform)
	  {
	    List<FormRights> rightList = null;
	    if ((StringUtil.isNotEmpty(actDefId)) && (StringUtil.isNotEmpty(nodeId))) {
	      if (StringUtil.isNotEmpty(parentActDefId))
	        rightList = this.formRightsDao.getFormRights(formKey, actDefId, false, nodeId, false, parentActDefId, false, platform);
	      else {
	        rightList = this.formRightsDao.getFormRights(formKey, actDefId, false, nodeId, false, null, true, platform);
	      }
	    }
	    if ((StringUtil.isNotEmpty(actDefId)) && (BeanUtils.isEmpty(rightList))) {
	      if (StringUtil.isNotEmpty(parentActDefId))
	        rightList = this.formRightsDao.getFormRights(formKey, actDefId, false, null, true, parentActDefId, false, platform);
	      else {
	        rightList = this.formRightsDao.getFormRights(formKey, actDefId, false, null, true, null, true, platform);
	      }
	    }
	    if (BeanUtils.isEmpty(rightList)) {
	      rightList = this.formRightsDao.getFormRights(formKey, null, true, null, false, null, false, platform);
	    }
	    //ISysUser user = sysUserService.getById(userId);
	    List<?extends ISysRole> roles = sysRoleService.getByUserId(userId);
		List<?extends IPosition> positions = positionService.getByUserId(userId);
		List<?extends ISysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);

	    Map<String, Map<String, String>> permissions = new HashMap<String, Map<String, String>>();

	    Map<String,String> fieldPermission = new HashMap<String,String>();
	    Map<String,String> tablePermission = new HashMap<String,String>();
	    Map<String,String> tableRelPermission = new HashMap<String,String>();
	    Map<String,String> opinionPermission = new HashMap<String,String>();
	    Map<String,String> attachPermission = new HashMap<String,String>();

	    for (FormRights rights : rightList) {
	      if (FormRights.TableShowRights != rights.getType() && FormRights.TableRelShowRights != rights.getType()) {
	        JSONObject permission = JSONObject.fromObject(rights
	          .getPermission());
	        String name = rights.getName().toLowerCase();

	        String right = getRight(permission, roles, positions, orgs, 
	          ownOrgs, userId);

	        if (right.equals("r")) {
	          boolean rpost = false;
	          if (permission.containsKey("rpost")) {
	            rpost = permission.getBoolean("rpost");
	          }
	          if (rpost) {
	            right = "rp";
	          }
	        }
	        //用于添加字段控件对应的操作行为权限
	        String operateRight = FieldOperateRightUtil.addOperateRight(right, permission, rights.getType());
			String tablename = permission.getString("tableName");
			if(!operateRight.equals("none")) 
				fieldPermission.put(name+"_"+tablename+"_"+FieldOperateRightUtil.OPERATE_RIGHT, operateRight);
	        switch (rights.getType()) {
			case FormRights.FieldRights:
				fieldPermission.put(name, right);
				break;
			case FormRights.TableRights:
				tablePermission.put(name, right);
				break;
			case FormRights.TableRelRights:
				tableRelPermission.put(name, right);
				break;
			case FormRights.OpinionRights:
				opinionPermission.put(name, right);
			case FormRights.AttachFileRights:
				attachPermission.put(name, right);

			}	
	        
	      }
	    }

	    permissions.put("field", fieldPermission);
	    permissions.put("table", tablePermission);
	    permissions.put("reltable", tableRelPermission);
	    permissions.put("opinion", opinionPermission);
	    permissions.put("attach", attachPermission);

	    return permissions;
	  }
	 /** 
	* @Title: getByFormKey 
	* @Description: TODO(获取表单权限) 
	* @param @param bpmFormDef 表单设计对象
	* @param @param userId 当前用户ID
	* @param @return     
	* @return Map<String,Map<String,String>>    返回类型 
	* @throws 
	*/
	public Map<String, Map<String, String>> getByFormKey(FormDef bpmFormDef, Long userId) {
	     Map<String, Object> param=new HashMap<String,Object>();
	     param.put(IFormHandlerService._bpmFormDef_, bpmFormDef);
	     param.put(IFormHandlerService._userId_, userId);
	     return getByFormKey(param);
	 }

	/** 
	* @Title: getByFormKey 
	* @Description: TODO(获取表单权限) 
	* @param @param param
	* @param @return     
	* @return Map<String,Map<String,String>>    返回类型 
	* @throws
	*/
	public Map<String, Map<String, String>> getByFormKey(Map<String, Object> param) {
	    FormDef bpmFormDef=MapUtil.get(param, IFormHandlerService._bpmFormDef_, FormDef.class);
	    Long userId=MapUtil.get(param, IFormHandlerService._userId_, Long.class);
	    //全局控制是否高亮，优先级最高
	    Boolean globalHighLight=MapUtil.get(param, IFormHandlerService._highLight_, Boolean.class);
		Map<String, Map<String, String>> permissions = new HashMap<String, Map<String, String>>();
		Map<String, String> fieldPermission = new HashMap<String, String>();
		Map<String, String> tablePermission = new HashMap<String, String>();
		Map<String, String> tableRelPermission = new HashMap<String, String>();
		Map<String, String> opinionPermission = new HashMap<String, String>();
		Map<String, String> attachPermission = new HashMap<String, String>();
	    Map<String, String> tableGroupPermission = new HashMap<String, String>();
		List<FormRights> rightList = formRightsDao.getByFormKey(bpmFormDef.getFormKey(),false);
		String right = "r";
		//ISysUser user = sysUserService.getById(userId);
		List<?extends ISysRole> roles = sysRoleService.getByUserId(userId);
		List<?extends IPosition> positions = positionService.getByUserId(userId);
		List<?extends ISysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
		//获取可以管理的组织列表。
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);
		for (FormRights rights : rightList) {
			JSONObject permission = JSONObject.fromObject(rights
			          .getPermission());
			String r = getRight(permission, roles, positions, orgs,
					ownOrgs, UserContextUtil.getCurrentUserId());
            Object hightLight = globalHighLight==null?permission.get("hightLight"):globalHighLight; 
			if(BeanUtils.isEmpty(r)){
				right = r;
			}else{
				right = "r";
			}
			String name = rights.getName().toLowerCase();
			//用于添加字段控件对应的操作行为权限
			String operateRight = FieldOperateRightUtil.addOperateRight(right, permission, rights.getType());
			String tablename = permission.getString("tableName");
			if(!operateRight.equals("none")) 
				fieldPermission.put(name+"_"+tablename+"_"+FieldOperateRightUtil.OPERATE_RIGHT, operateRight);
			switch (rights.getType()) {
				case FormRights.FieldRights:  
					fieldPermission.put(rights.getName().toLowerCase(), right); 
					break;
				case FormRights.TableRights: 
					tablePermission.put(rights.getName().toLowerCase(), right);
					break;
				case FormRights.TableRelRights:
					tableRelPermission.put(rights.getName().toLowerCase(), right);
					break;
				case FormRights.OpinionRights:
				    opinionPermission.put(rights.getName().toLowerCase(), right);
					break;
				case FormRights.AttachFileRights:
					attachPermission.put(rights.getName().toLowerCase(), right);
					break;
				case FormRights.tableGroupRights:
                    JSONObject _right=new JSONObject();
                    _right.put("right", right);
                    _right.put("hightLight", hightLight!=null?hightLight.toString():"");
                    tableGroupPermission.put(rights.getName().toLowerCase(), _right.toString());
                    break;
			}
		}

		/*String html = bpmFormDef.getHtml();
		Long tableId = bpmFormDef.getTableId();
		// 读取表。
		FormTable bpmFormTable = formTableService.getTableById(tableId);
		 
		// 主表字段权限
		List<FormField> fieldList = bpmFormTable.getFieldList();
		for (FormField field : fieldList) {
			for (FormRights bpmFormRights : rightList) {
				if(!BeanUtils.isEmpty(bpmFormRights.getName()) 
						&&!BeanUtils.isEmpty(bpmFormRights.getPermission()) 
						&& bpmFormRights.getName().equalsIgnoreCase(field.getFieldName())){
					fieldPermission.put(field.getFieldName().toLowerCase(), right);
				}
			}
			
		}*/
		permissions.put("field", fieldPermission);

		// 子表权限。
		/*List<FormTable> tableList = bpmFormTable.getSubTableList();

		for (FormTable table : tableList) {
			// 子表字段权限
			List<FormField> subFieldList = table.getFieldList();
			
			for (FormField field : subFieldList) {
				tablePermission.put(field.getFieldName().toLowerCase(), right);
				 
				
			}
		}*/
		
		permissions.put("table", tablePermission);
		// rel表权限。
				/*List<FormTable> tableList = bpmFormTable.getRelTableList();

				for (FormTable table : tableList) {
					// rel表字段权限
					List<FormField> relFieldList = table.getFieldList();
					
					for (FormField field : relFieldList) {
						tableRelPermission.put(field.getFieldName().toLowerCase(), right);
						 
						
					}
				}*/
		permissions.put("reltable", tableRelPermission);
		// 意见权限。
		/*Map<String, String> opinionMap = FormUtil.parseOpinion(html);
		Set<Entry<String, String>> set = opinionMap.entrySet();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
			Entry<String, String> tmp = it.next();
			opinionPermission.put(tmp.getKey().toLowerCase(), right);
		}*/
		permissions.put("opinion", opinionPermission);
		permissions.put("attach", attachPermission);
        permissions.put("tableGroup", tableGroupPermission);

		return permissions;
	}

	/**
	 * 获取权限。
	 * 
	 * @param jsonObject
	 *            数据格式为：{
	 *            'title':'orderId','memo':'订单ID','read':{'type':'everyone','id':'',
	 *            'fullname':''},'write':{'type':'everyone','id':'',
	 *            'fullname':''}}
	 * @param roles
	 *            用户所属的角色
	 * @param positions
	 *            用户所属的岗位
	 * @param orgs
	 *            用户所在的组织
	 * @param ownOrgs
	 *            组织负责人
	 * @param userId
	 *            用户ID
	 * @return
	 */
	private String getRight(JSONObject jsonObject, List<?extends ISysRole> roles,
			List<?extends IPosition> positions, List<? extends ISysOrg> orgs,
			List<? extends ISysOrg> ownOrgs, Long userId) {
		String right = "";
		if(hasRight(jsonObject, "required", roles, positions, orgs, ownOrgs,userId)){
			right = "b";
		}else if (hasRight(jsonObject, "write", roles, positions, orgs, ownOrgs,userId)) {
			right = "w";
		} else if (hasRight(jsonObject, "read", roles, positions, orgs,ownOrgs, userId)) {
			right = "r";
		}
		return right;
	}

	/**
	 * 判断是否有权限。
	 * 
	 * @param permission
	 *            权限JSONOjbect
	 * @param mode
	 *            读或写 (write,read)
	 * @param roles
	 *            角色列表
	 * @param positions
	 *            岗位
	 * @param orgs
	 *            组织
	 * @param ownOrgs
	 *            组织负责人
	 * @param userId
	 *            用户ID
	 * @return
	 */
	private boolean hasRight(JSONObject permission, String mode,
			List<?extends ISysRole> roles, List<?extends IPosition> positions, List<?extends ISysOrg> orgs,
			List<?extends ISysOrg> ownOrgs, Long userId) {
		boolean hasRight = false;
		JSONObject node = permission.getJSONObject(mode);
		if(JSONUtils.isNull(node)) return false;
		if(BeanUtils.isEmpty(node.get("type")))return false;
		String type = node.getString("type");
		String id=node.get("id")==null?null:node.getString("id");
		if ("none".equals(type)) {
			return false;
		}
		if ("everyone".equals(type)) {
			return true;
		}
		if ("hiddenfield".equals(type)) {
			return false;
		}
		// 指定用户
		if ("user".equals(type)) {
			hasRight = contain(id, userId.toString());
			return hasRight;
		}
		// 指定角色
		else if ("role".equals(type)) {
			for (ISysRole role : roles) {
				if (contain(id, role.getRoleId().toString())) {
					return true;
				}
			}
		}
		// 指定组织
		else if ("org".equals(type)) {
			for (ISysOrg org : orgs) {
				if (contain(id, org.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 组织负责人
		else if ("orgMgr".equals(type)) {
			for (ISysOrg org : ownOrgs) {
				if (contain(id, org.getOrgId().toString())) {
					return true;
				}
			}
		}
		// 岗位
		else if ("pos".equals(type)) {
			for (IPosition position : positions) {
				if (contain(id, position.getPosId().toString())) {
					return true;
				}
			}
		}
		return false;
	}


	public void updateRights() {
		List<FormRights> bpmFormRightsList = formRightsDao.getAll();
		for (FormRights bpmFormRights : bpmFormRightsList) {
			String permission = bpmFormRights.getPermission();
			JSONObject jsonObject = JSONObject.fromObject(permission);
			String title = jsonObject.get("title").toString();
			String memo =jsonObject.get("memo").toString();
			String read =jsonObject.get("read").toString(); 
			String write =jsonObject.get("write").toString(); 
			JSONObject json =getJson(title, memo, read, write);
			bpmFormRights.setPermission(json.toString());
			formRightsDao.update(bpmFormRights);
		}
	}
	
	private static JSONObject getJson(String title, String memo,String read,String write) {
		JSONObject json = new JSONObject();
		json.element("title", title);
		json.element("memo", memo);
		json.element("read", read);
		json.element("write", write);
		json.element("required", "{type:'none',id:'', fullname:''}");
		return json;
	}
	
	/**
	 * 判定逗号分隔的字符串是否包括指定的字符。
	 * 
	 * @param ids
	 * @param curId
	 * @return
	 */
	private boolean contain(String ids, String curId) {
		if (StringUtil.isEmpty(ids))
			return false;
		String[] aryId = ids.split(",");
		for (String id : aryId) {
			if (id.equalsIgnoreCase(curId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据formkey删除表单权限
	 * @param cascade 是同时否删除表单人流程的流程节点表单权限设置
	 * @param formKey
	 */
	public void deleteByFormKey(Long formKey,boolean cascade){
		formRightsDao.delByFormKey(formKey,cascade);
	}
	
	

	/**
	 * 根据表id删除表单的权限。
	 * 
	 * @param tableId
	 */
	public void deleteByTableId(Long tableId) {
		formRightsDao.deleteByTableId(tableId);
	}
	
	
	/**
	 * 删除表单权限设置。
	 * @param formKey
	 * @param cascade 级联删除标志。如果为True，同时删除所有与表单相关联的流程、节点权限设置
	 */
	public void delFormRights(Long formKey,boolean cascade){
		formRightsDao.delByFormKey(formKey,cascade);
	}
	
	/**
	 * 删除表单流程权限设置
	 * @param actDefId
	 * @param cascade 级联删除标志。如果为True，同时删除所有流程的节点权限设置
	 */
	public void delFormFlowRights(String actDefId,boolean cascade){
		formRightsDao.delByActDefId(actDefId,cascade);
	}
	
	/**
	 * 删除表单节点权限设置
	 * @param actDefId
	 * @param nodeId
	 */
	public void delFormFlowNodeRights(String actDefId,String nodeId){
		formRightsDao.delByActDefIdAndNodeId(actDefId, nodeId);
	}
	
	/**
	 * 根据流程定义id，任务节点id和表单id获取权限数据。
	 * @param actDefId		流程定义ID
	 * @param nodeId		任务节点
	 * @param formKey		表单定义ID
	 * @return
	 */
	public  Map<String,List<JSONObject>> getPermissionByFormNode(String actDefId,String nodeId,Long formKey){
		Map<String,List<JSONObject>> map=new HashMap<String, List<JSONObject>>();
		List<FormRights> rightList= formRightsDao.getByFlowFormNodeId(actDefId, nodeId);   //具体节点的权限
		if(rightList.size()==0){
			if(StringUtil.isNotEmpty(actDefId)) {
				rightList = formRightsDao.getByActDefId(formKey,actDefId,false);  //流程的全局菜单
			}
			if(BeanUtils.isEmpty(rightList)){
				rightList = formRightsDao.getByFormKey(formKey, false);      //表单管理的表单权限
			}			
			if(BeanUtils.isEmpty(rightList)){
				FormDef bpmFormDef =formDefDao.getDefaultVersionByFormKey(formKey);     //默认的权限
				map= getPermissionByFormKey(bpmFormDef);
				return map;
			}
		}
		
		FormDef bpmFormDef = formDefDao.getDefaultVersionByFormKey(formKey);
		map = getPermissionByFormKey(rightList,bpmFormDef);
		return map;
		/*//用于过虑类型为删除和隐藏字段的表单对应信息（字段数据不包含删除的字段和隐藏的字段）
		FormDef bpmFormDef = bpmFormDefDao.getDefaultVersionByFormKey(formKey);
		rightList = adjustOrder(rightList,bpmFormDef);
		
		List<JSONObject> fieldJsonList=new ArrayList<JSONObject>();
		List<JSONObject> tableJsonList=new ArrayList<JSONObject>();
		List<JSONObject> opinionJsonList=new ArrayList<JSONObject>();
		for(FormRights rights:rightList){
			switch(rights.getType()){
				case FormRights.FieldRights:
					fieldJsonList.add(JSONObject.fromObject(rights.getPermission()));
					break;
				case FormRights.TableRights:
					tableJsonList.add(JSONObject.fromObject(rights.getPermission()));
					break;
				case FormRights.OpinionRights:
					opinionJsonList.add(JSONObject.fromObject(rights.getPermission()));
					break;
			}
		}
		map.put("field", fieldJsonList);
		map.put("table", tableJsonList);
		map.put("opinion", opinionJsonList);
		return map;*/
	
	}
	
	/**
	 * 根据formkey删除表单权限
	 * @param formKey
	 */
	public void deleteByFormKey(Long formKey){
		formRightsDao.delByFormDefId(formKey);
	}
	
	
	/**
	 * 获取通过表单设定定义表单的权限数据。
	 * 
	 * <pre>
	 * 通过设计定义的表单，
	 * 如果有设置过权限，那么权限信息可以通过设置过权限与解析表单匹配获取。
	 * </pre>
	 * @param rightList
	 * @param formKey
	 * @return
	 */
	private Map<String, List<JSONObject>> getPermissionByFormKey(List<FormRights> rightList,FormDef bpmFormDef) {		
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		if(BeanUtils.isEmpty(bpmFormDef)){
			return map;
		}		
		List<FormRights> formRightsList = new ArrayList<FormRights>();
		List<FormRights> tableRightsList = new ArrayList<FormRights>();
		List<FormRights> tableRelRightsList = new ArrayList<FormRights>();
		List<FormRights> otherRightsList = new ArrayList<FormRights>();
		List<FormRights> tableShowRightsList = new ArrayList<FormRights>();  //子表显示与否
		List<FormRights> tableRelShowRightsList = new ArrayList<FormRights>();  //rel表显示与否
		List<FormRights> attachRightsList = new ArrayList<FormRights>();  //文件附件 by YangBo
	    List<FormRights> tableGroupRightsList = new ArrayList<FormRights>();  //表字段分组权限

		for (FormRights bpmFormRights : rightList) {
			if(bpmFormRights.getType() == FormRights.FieldRights){
				formRightsList.add(bpmFormRights);		
			}else if(bpmFormRights.getType() == FormRights.TableRights){
				tableRightsList.add(bpmFormRights);	
			}else if(bpmFormRights.getType() == FormRights.TableRelRights){
				tableRelRightsList.add(bpmFormRights);	
			}else if(bpmFormRights.getType() == FormRights.TableShowRights){
				tableShowRightsList.add(bpmFormRights);	
			}else if(bpmFormRights.getType() == FormRights.TableRelShowRights){
				tableRelShowRightsList.add(bpmFormRights);	
			}else if(bpmFormRights.getType() == FormRights.AttachFileRights){//文件附件权限
				attachRightsList.add(bpmFormRights);	
			}else if(bpmFormRights.getType() == FormRights.tableGroupRights){
			    tableGroupRightsList.add(bpmFormRights);    
			}else{
				otherRightsList.add(bpmFormRights);
			}
		}
		FormTable bpmFormTable=formTableService.getTableById(bpmFormDef.getTableId());
		if(BeanUtils.isEmpty(bpmFormTable)){
			return map;
		}
		
	    //表字段分组权限
        List<JSONObject> tableGroupJsonList = new ArrayList<JSONObject>();
        String team=bpmFormTable.getTeam();
        JSONArray teams=null;
        if(StringUtil.isNotEmpty(team)){
            team=JSON.parseObject(team).get("team").toString();
            teams=JSONArray.fromObject(team);
            for (Object _team : teams) {
                boolean mark = true;
                try{
                    String tname=((JSONObject)_team).getString("teamName");
                    String tnameKey=((JSONObject)_team).getString("teamNameKey");
                    for (FormRights bpmFormRights : tableGroupRightsList) {
                        String rname=bpmFormRights.getName();
                        if(StringUtil.isNotEmpty(rname) && rname.equalsIgnoreCase(tnameKey)){
                            JSONObject permission = JSONObject.fromObject(bpmFormRights.getPermission());
                            if(!permission.containsKey("tableId")){
                                permission.put("tableId", bpmFormTable.getTableId());
                                permission.put("tableName", bpmFormTable.getTableName());
                                permission.put("mainTableId","");
                                permission.put("mainTableName","");
                            }
                            tableGroupJsonList.add(permission);
                            mark = false;
                            break;
                        }
                    }
                    if(mark){              
                        //没有设置过匹配权限权限时,直接到初始化中获取(兼容以前版本)
                        JSONObject permission = getPermissionJson(tnameKey,tname,FormRights.tableGroupRights);
                        permission.put("tableId", bpmFormDef.getTableId());
                        permission.put("tableName", bpmFormDef.getTableName());
                        permission.put("mainTableId","");
                        permission.put("mainTableName","");
                        tableGroupJsonList.add(permission);
                    }
                }catch(Exception e){
                    
                }

                
            }
            map.put("tableGroupShows", tableGroupJsonList);
        }
        
		//主表字段权限
		List<JSONObject> fieldJsonList = new ArrayList<JSONObject>();
		List<FormField> fieldList= bpmFormTable.getFieldList();
		for (FormField bpmFormField : fieldList) {
			boolean mark = true;
			for (FormRights bpmFormRights : formRightsList) {
				if(!BeanUtils.isEmpty(bpmFormRights.getName()) && bpmFormRights.getName().equalsIgnoreCase(bpmFormField.getFieldName())){
				//	bpmFormRights.setSn(bpmFormField.getSn());
					JSONObject permission = JSONObject.fromObject(bpmFormRights.getPermission());
					if(!permission.containsKey("tableId")){
						permission.put("tableId", bpmFormTable.getTableId());
						permission.put("tableName", bpmFormTable.getTableName());
						permission.put("mainTableId","");
						permission.put("mainTableName","");
					}
					fieldJsonList.add(permission);
					mark = false;
					break;
				}
			}
			if(mark){              //没有设置过匹配权限权限时,直接到初始化中获取(兼容以前版本)
				JSONObject permission = getPermissionJson(bpmFormField.getFieldName(),bpmFormField.getFieldDesc(),FormRights.FieldRights);
				permission.put("tableId", bpmFormDef.getTableId());
				permission.put("tableName", bpmFormDef.getTableName());
				permission.put("mainTableId","");
				permission.put("mainTableName","");
				fieldJsonList.add(permission);
			}
		}
		map.put("field", fieldJsonList);
		
		
		//文件附件权限  by YangBo
		List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
		JSONArray attachJSONArry = bpmFormTable.getAttachJSONArry();
		
		for (Object  obj : attachJSONArry) {
			boolean mark = true;
			JSONObject json = JSONObject.fromObject(obj);
			String rightName = (String) json.get("rightName");
			String rightMemo = (String) json.get("rightMemo");

			for (FormRights bpmFormRights : attachRightsList) {
				if(!BeanUtils.isEmpty(bpmFormRights.getName()) && bpmFormRights.getName().equalsIgnoreCase(rightName)){
					JSONObject permission = JSONObject.fromObject(bpmFormRights.getPermission());
					if(!permission.containsKey("tableId")){
						permission.put("tableId", bpmFormTable.getTableId());
						permission.put("tableName", bpmFormTable.getTableName());
						permission.put("mainTableId","");
						permission.put("mainTableName","");
					}
					attachJsonList.add(permission);
					mark = false;
					break;
				}
			}

			if(mark){              //没有设置过匹配权限权限时,直接到初始化中获取(兼容以前版本)
				JSONObject permission = getPermissionJson(rightName,rightMemo,FormRights.AttachFileRights);
				permission.put("tableId", bpmFormDef.getTableId());
				permission.put("tableName", bpmFormDef.getTableName());
				permission.put("mainTableId","");
				permission.put("mainTableName","");
				attachJsonList.add(permission);
			}
		}
		map.put("fileAttach", attachJsonList);

		//子表字段权限
		List<JSONObject> tableJsonList = new ArrayList<JSONObject>();
		List<FormTable> tableList = bpmFormTable.getSubTableList();
		for (FormTable table : tableList) {
			// 子表整个表的权限
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableRights);
			//每个子表中的每个字段
			List<FormField> subFieldList = table.getFieldList();
			List<JSONObject> subFieldJsonList = new ArrayList<JSONObject>();
			for (FormField field : subFieldList) {
				boolean mark = true;
				for (FormRights bpmFormRights : tableRightsList) {
					if(!BeanUtils.isEmpty(bpmFormRights.getName()) && bpmFormRights.getName().equalsIgnoreCase(field.getFieldName())){
					//	bpmFormRights.setSn(field.getSn());
						JSONObject subPermission = JSONObject.fromObject(bpmFormRights.getPermission());
						if(subPermission.containsKey("tableId") && subPermission.getString("tableId").equals(table.getTableId().toString())){
							if(!subPermission.containsKey("mainTableId")){
								subPermission.put("tableId", table.getTableId());
								subPermission.put("tableName", table.getTableName());
								subPermission.put("mainTableId",bpmFormTable.getTableId());
								subPermission.put("mainTableName",bpmFormTable.getTableName());
							}
							subFieldJsonList.add(subPermission);
							mark = false;
							break; 
						}						
					}
				}
				if(mark){              //没有设置过匹配权限时直接到初始化中获取(兼容以前版本)
					JSONObject subPermission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
					//子表整个表的权限是重新生成的，所以不用判断
					subPermission.put("tableId", table.getTableId());
					subPermission.put("tableName", table.getTableName());
					subPermission.put("mainTableId",bpmFormTable.getTableId());
					subPermission.put("mainTableName",bpmFormTable.getTableId());
					subFieldJsonList.add(subPermission);
				}
			}
			//子表整个表的权限是重新生成的，所以不用判断
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId",bpmFormTable.getTableId());
			permission.put("mainTableName",bpmFormTable.getTableName());
			permission.put("subField", subFieldJsonList);
			tableJsonList.add(permission);
		}
		map.put("table", tableJsonList);
		

		//rel表字段权限
		List<JSONObject> tableRelJsonList = new ArrayList<JSONObject>();
		List<FormTable> reltableList = bpmFormTable.getRelTableList();
		for (FormTable table : reltableList) {
			// rel表整个表的权限
			JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableRelRights);
			//每个rel表中的每个字段
			List<FormField> relFieldList = table.getFieldList();
			List<JSONObject> relFieldJsonList = new ArrayList<JSONObject>();
			for (FormField field : relFieldList) {
				boolean mark = true;
				for (FormRights bpmFormRights : tableRelRightsList) {
					if(!BeanUtils.isEmpty(bpmFormRights.getName()) && bpmFormRights.getName().equalsIgnoreCase(field.getFieldName())){
					//	bpmFormRights.setSn(field.getSn());
						JSONObject relPermission = JSONObject.fromObject(bpmFormRights.getPermission());
						if(relPermission.containsKey("tableId") && relPermission.getString("tableId").equals(table.getTableId().toString())){
							if(!relPermission.containsKey("mainTableId")){
								relPermission.put("tableId", table.getTableId());
								relPermission.put("tableName", table.getTableName());
								relPermission.put("mainTableId",bpmFormTable.getTableId());
								relPermission.put("mainTableName",bpmFormTable.getTableName());
							}
							relFieldJsonList.add(relPermission);
							mark = false;
							break; 
						}						
					}
				}
				if(mark){              //没有设置过匹配权限时直接到初始化中获取(兼容以前版本)
					JSONObject relPermission = getPermissionJson(field.getFieldName(),field.getFieldDesc(),FormRights.FieldRights);
					//rel表整个表的权限是重新生成的，所以不用判断
					relPermission.put("tableId", table.getTableId());
					relPermission.put("tableName", table.getTableName());
					relPermission.put("mainTableId",bpmFormTable.getTableId());
					relPermission.put("mainTableName",bpmFormTable.getTableId());
					relFieldJsonList.add(relPermission);
				}
			}
			//rel表整个表的权限是重新生成的，所以不用判断
			permission.put("tableId", table.getTableId());
			permission.put("tableName", table.getTableName());
			permission.put("mainTableId",bpmFormTable.getTableId());
			permission.put("mainTableName",bpmFormTable.getTableName());
			permission.put("relField", relFieldJsonList);
			tableRelJsonList.add(permission);
		}
		map.put("reltable", tableRelJsonList);	
				
		//处理其它  意见权限。
		String html = bpmFormDef.getHtml();   // 获取模版。
		List<JSONObject> opinionJsonList = new ArrayList<JSONObject>();
		Map<String, String> opinionMap = FormUtil.parseOpinion(html);
		Set<Entry<String, String>> set = opinionMap.entrySet();
		for (Iterator<Entry<String, String>> it = set.iterator(); it.hasNext();) {
			boolean mark = true;
			Entry<String, String> tmp = it.next();
			for (FormRights bpmFormRights : otherRightsList) {
				JSONObject opinionPermission = JSONObject.fromObject(bpmFormRights.getPermission());
				if(!BeanUtils.isEmpty(bpmFormRights.getName())&& bpmFormRights.getName().equalsIgnoreCase(tmp.getKey())){
					if(!opinionPermission.containsKey("tableId")){
						opinionPermission.put("tableId", bpmFormTable.getTableId());
						opinionPermission.put("tableName", bpmFormTable.getTableName());
						opinionPermission.put("mainTableId","");
						opinionPermission.put("mainTableName","");
					}
					opinionJsonList.add(opinionPermission);
					mark = false;
					break;
				}	
			}
			if(mark){              //没有设置过匹配权限时,  直接到初始化中获取(兼容以前版本)
				JSONObject permission = getPermissionJson(tmp.getKey(),tmp.getValue(),FormRights.OpinionRights);
				permission.put("tableId", bpmFormTable.getTableId());
				permission.put("tableName", bpmFormTable.getTableName());
				permission.put("mainTableId","");
				permission.put("mainTableName","");
				opinionJsonList.add(permission);
			}
		}
		map.put("opinion", opinionJsonList);
		
		//子表显示与否
		List<JSONObject> tableShowJsonList = new ArrayList<JSONObject>();
		for (FormTable table : tableList) {
			boolean mark = true;
			for (FormRights bpmFormRights : tableShowRightsList) {
				if(!BeanUtils.isEmpty(bpmFormRights.getName()) && bpmFormRights.getName().equalsIgnoreCase(table.getTableName())){
					JSONObject permission = JSONObject.fromObject(bpmFormRights.getPermission());
					if(!permission.containsKey("tableId")){
						permission.put("tableId", table.getTableId());
						permission.put("tableName", table.getTableName());
						permission.put("mainTableId",bpmFormTable.getTableId());
						permission.put("mainTableName",bpmFormTable.getTableName());
					}
					tableShowJsonList.add(permission);
					mark = false;
					break;
				}
			}
			if(mark){              //没有设置过匹配权限权限时,   直接到初始化中获取(兼容以前版本)
				JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableShowRights);
				permission.put("tableId", table.getTableId());
				permission.put("tableName", table.getTableName());
				permission.put("mainTableId",bpmFormTable.getTableId());
				permission.put("mainTableName",bpmFormTable.getTableName());
				tableShowJsonList.add(permission);
			}
		}
		map.put("tableShow", tableShowJsonList);
		
		
		//rel表显示与否
		List<JSONObject> tableRelShowJsonList = new ArrayList<JSONObject>();
				for (FormTable table : reltableList) {
					boolean mark = true;
					for (FormRights bpmFormRights : tableRelShowRightsList) {
						if(!BeanUtils.isEmpty(bpmFormRights.getName()) && bpmFormRights.getName().equalsIgnoreCase(table.getTableName())){
							JSONObject permission = JSONObject.fromObject(bpmFormRights.getPermission());
							if(!permission.containsKey("tableId")){
								permission.put("tableId", table.getTableId());
								permission.put("tableName", table.getTableName());
								permission.put("mainTableId",bpmFormTable.getTableId());
								permission.put("mainTableName",bpmFormTable.getTableName());
							}
							tableRelShowJsonList.add(permission);
							mark = false;
							break;
						}
					}
					if(mark){              //没有设置过匹配权限权限时,   直接到初始化中获取(兼容以前版本)
						JSONObject permission = getPermissionJson(table.getTableName(),table.getTableDesc(),FormRights.TableShowRights);
						permission.put("tableId", table.getTableId());
						permission.put("tableName", table.getTableName());
						permission.put("mainTableId",bpmFormTable.getTableId());
						permission.put("mainTableName",bpmFormTable.getTableName());
						tableRelShowJsonList.add(permission);
					}
				}
				map.put("tableRelShow", tableRelShowJsonList);
		return map;
	}
	
	/**
	 * 获取通过表单设定定义表单的权限数据。
	 * 
	 * <pre>
	 * 通过设计定义的表单，
	 * 如果有设置过权限，那么权限信息可以通过设置过权限与解析表单匹配获取。
	 * </pre>
	 * @param rightList
	 * @param formKey
	 * @return
	 */
	public Map<String, List<JSONObject>> getRelTablePermission(Long formKey,Long userId,String actDefId,String nodeId){
		//具体权限取得
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		List<FormRights> rightList = null;
		// 如果流程定义id和任务节点id不为空那么获取节点的权限。
		if (StringUtil.isNotEmpty(actDefId) && StringUtil.isNotEmpty(nodeId)) {
			rightList = formRightsDao.getByActDefIdAndNodeId(formKey,actDefId, nodeId);
		}
		if (BeanUtils.isEmpty(rightList) && StringUtil.isNotEmpty(actDefId) ) {
			rightList = formRightsDao.getByActDefId(formKey,actDefId,false);
		}
		if (BeanUtils.isEmpty(rightList)) {
			rightList = formRightsDao.getByFormKey(formKey,false);
		}
		
		ISysUser user = sysUserService.getById(userId);
		
		List<?extends ISysRole> roles = null;
		List<?extends IPosition> positions =null;
		List<?extends ISysOrg> orgs = null;
		if(!BeanUtils.isEmpty(user)){
			roles = sysRoleService.getByUserId(userId);
			positions = positionService.getByUserId(userId);
			orgs = sysOrgService.getOrgsByUserId(userId);
		}
		//获取可以管理的组织列表。
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);

		List<JSONObject> relJsonList = new ArrayList<JSONObject>();
		List<JSONObject> oldRelJsonList = new ArrayList<JSONObject>();
		List<JSONObject> relTableShowList = new ArrayList<JSONObject>();
		Map<String, List<JSONObject>> tableMap = new HashMap<String, List<JSONObject>>();
		for (FormRights rights : rightList) {
			if(rights.getType()==FormRights.TableRelRights){   //rel表字段
				JSONObject permission = JSONObject.fromObject(rights.getPermission());
				String name = rights.getName().toLowerCase();
				// 取得权限
				String right = getRight(permission, roles, positions, orgs,ownOrgs, userId);
				//只读提交
				if(right.equals("r")){
					boolean rpost=false;
					if( permission.containsKey("rpost")){
						rpost = permission.getBoolean("rpost");
					}
					if(rpost){
						right="rp";
					}
				}
				permission.put("power",right);
				if(permission.containsKey("tableId")){    //旧版本的没有tableId 新版本有的
					relJsonList.add(permission);
				}else{ 
					//旧版本
					oldRelJsonList.add(permission);
				}	
			}else if(rights.getType()==FormRights.TableRelShowRights){    //整个子表显示与否
				JSONObject permission = JSONObject.fromObject(rights.getPermission());
				relTableShowList.add(permission);
			}	
		}
		map.put("relFileJsonList", relJsonList);
		map.put("oldRelFileJsonList",oldRelJsonList);
		map.put("relTableShowList", relTableShowList);
		
		return map;
	}
	/**
	 * 获取通过表单设定定义表单的权限数据。
	 * 
	 * <pre>
	 * 通过设计定义的表单，
	 * 如果有设置过权限，那么权限信息可以通过设置过权限与解析表单匹配获取。
	 * </pre>
	 * @param rightList
	 * @param formKey
	 * @return
	 */
	public Map<String, List<JSONObject>> getSubTablePermission(Long formKey,Long userId,String actDefId,String nodeId){
		Map<String, List<JSONObject>> map = new HashMap<String, List<JSONObject>>();
		List<FormRights> rightList = null;
		// 如果流程定义id和任务节点id不为空那么获取节点的权限。
		if (StringUtil.isNotEmpty(actDefId) && StringUtil.isNotEmpty(nodeId)) {
			rightList = formRightsDao.getByActDefIdAndNodeId(formKey,actDefId, nodeId);
		}
		if (BeanUtils.isEmpty(rightList) && StringUtil.isNotEmpty(actDefId) ) {
			rightList = formRightsDao.getByActDefId(formKey,actDefId,false);
		}
		if (BeanUtils.isEmpty(rightList)) {
			rightList = formRightsDao.getByFormKey(formKey,false);
		}
		
		ISysUser user = sysUserService.getById(userId);
		
		List<?extends ISysRole> roles = null;
		List<?extends IPosition> positions =null;
		List<?extends ISysOrg> orgs = null;
		if(!BeanUtils.isEmpty(user)){
			roles = sysRoleService.getByUserId(userId);
			positions = positionService.getByUserId(userId);
			orgs = sysOrgService.getOrgsByUserId(userId);
		}
		//获取可以管理的组织列表。
		List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);

		List<JSONObject> subJsonList = new ArrayList<JSONObject>();
		List<JSONObject> oldSubJsonList = new ArrayList<JSONObject>();
		List<JSONObject> subTableShowList = new ArrayList<JSONObject>();
		Map<String, List<JSONObject>> tableMap = new HashMap<String, List<JSONObject>>();
		for (FormRights rights : rightList) {
			if(rights.getType()==FormRights.TableRights){   //子表字段
				JSONObject permission = JSONObject.fromObject(rights.getPermission());
				String name = rights.getName().toLowerCase();
				// 取得权限
				String right = getRight(permission, roles, positions, orgs,ownOrgs, userId);
				//只读提交
				if(right.equals("r")){
					boolean rpost=false;
					if( permission.containsKey("rpost")){
						rpost = permission.getBoolean("rpost");
					}
					if(rpost){
						right="rp";
					}
				}
				permission.put("power",right);
				if(permission.containsKey("tableId")){    //旧版本的没有tableId 新版本有的
					subJsonList.add(permission);
				}else{ 
					//旧版本
					oldSubJsonList.add(permission);
				}	
			}else if(rights.getType()==FormRights.TableShowRights){    //整个子表显示与否
				JSONObject permission = JSONObject.fromObject(rights.getPermission());
				subTableShowList.add(permission);
			}	
		}
		map.put("subFileJsonList", subJsonList);
		map.put("oldSubFileJsonList", oldSubJsonList);
		map.put("subTableShowList", subTableShowList);
		return map;
	}
	
	
	/**
	 * 根据formkey\actDefId\nodeId删除表单权限
	 * @param formKey
	 * @param actDefId
	 * @param nodeId
	 */
	public void deleteRight(Long formKey,String actDefId,String nodeId){
		if(StringUtil.isNotEmpty(nodeId)){
			formRightsDao.delByActDefIdAndNodeId(actDefId, nodeId);	
		}else if(StringUtil.isNotEmpty(actDefId)){
			formRightsDao.delByActDefId(actDefId, false);
		}else{
			formRightsDao.delByFormKey(formKey,false);
		}
	}	
	
	/**
	 * 获取默认只读的的权限数据。
	 * 
	 * @param title
	 * @param memo
     * @param type
	 * 	//type 权限类型(1,字段FieldRights ,2,子表TableRights,,3,意见OpinionRights,
	//4.子表是否显示TableShowRights,5.rel表TableRelRights ,6. rel表是否显示TableRelShowRights)
	 * @return
	 */
	public JSONObject getReadPermissionJson(String title, String memo,int type) {
	//	String defJson = "{type:'everyone',id:'', fullname:''}";
		String defJson = "{type:'none',id:'', fullname:''}";
		JSONObject json = new JSONObject();
		json.element("title", title);
		json.element("memo", memo);
        if(type!=FormRights.TableShowRights && type!=FormRights.TableRelShowRights){
        	json.element("read", "{type:'everyone',id:'', fullname:''}");
    		json.element("write", defJson);
    		if(type !=FormRights.TableRights && type !=FormRights.TableRelRights){
    			json.element("required",defJson );
    		}
		}else{
			json.element("show", "r");     //子表显示可写：w  子表显示只读：r  为显示  其它为隐藏（y）   可以增加默认
		}
		return json;
	}
	/**
	 * 根据流程定义ID删除流程表单权限设置
	 * @param actDefId 流程定义ID
	 * @param cascade 是同时否删除的流程节点表单权限设置
	 */
	@Override
	public void delByActDefId(String actDefId, boolean cascade) {
		this.formRightsDao.delByActDefId(actDefId,cascade);
	}

	@Override
	public IFormRights getById(Long primaryKey) {
		return this.formRightsDao.getById(primaryKey);
	}

	@Override
	public void update(IFormRights bpmFormRights) {
		this.formRightsDao.update((FormRights)bpmFormRights);
	}

	@Override
	public void add(IFormRights bpmFormRights) {
		this.formRightsDao.add((FormRights)bpmFormRights);
		
	}
	/**
	 * 根据actDefId 获取表单权限。
	 * @param actDefId
	 * @param cascade
	 * @return
	 */
	@Override
	public List<FormRights> getFormRightsByActDefId(String actDefId) {
		return  this.formRightsDao.getBySqlKey("getFormRightsByActDefId", actDefId);
	}

	
	/**
	 * 根据流程定义id，节点id删除表单权限。
	 * @param actDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 */
	@Override
	public void delByFlowFormNodeId(String actDefId, String nodeId) {
		
		this.formRightsDao.delByFlowFormNodeId(actDefId, nodeId);
	}

	
}
