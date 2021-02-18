
package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormRights;
/**
 * 对象功能:字段权限 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class FormRightsDao extends BaseDao<FormRights>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormRights.class;
	}
	
	
	/**
	 * 根据actDefId 获取表单权限。
	 * @param actDefId
	 * @param cascade
	 * @return
	 */
	public List<FormRights> getFormRightsByActDefId(String actDefId) {
		return this.getBySqlKey("getFormRightsByActDefId", actDefId);
	}
	
	/**
	 * 根据表单key 获取表单权限。
	 * @param formKey
	 * @param cascade
	 * @return
	 */
	public List<FormRights> getByFormKey(Long formKey,boolean cascade) {
		String statment = null;
		if(cascade){
			statment = "getByFormKey";
		}else{
			statment = "getByFormKeyExcActDefId";
		}
		return this.getBySqlKey(statment, formKey);
	}
	
	
	/**
	 * 获取表单权限。
	 * @param formKey
	 * @param actDefId
	 * @return cascade 
	 * 
	 */
	public List<FormRights> getByActDefId(Long formKey,String actDefId,boolean cascade) {
		String statment = null;
		if(cascade){
			statment = "getByActDefId";
		}else{
			statment = "getByActDefIdExcNodeId";
		}
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("formKey", formKey);
		params.put("actDefId", actDefId);
		return this.getBySqlKey(statment,params);
	}
	
	
	/**
	 * 根据流程定义id，节点id获取表单的权限。
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public List<FormRights> getByActDefIdAndNodeId(Long formKey,String actDefId,String nodeId) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("formKey", formKey);
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		return this.getBySqlKey("getByActDefIdAndNodeId", params);
	}
	
	/**
	 * 根据流程定义id，节点id删除表单权限。
	 * @param actDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void delByActDefIdAndNodeId(String actDefId,String nodeId){
		Map params=new HashMap();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		this.delBySqlKey("delByActDefIdAndNodeId", params);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void delByActDefIdAndNodeId(String actDefId, String nodeId, String parentActDefId)
	  {
	    Map params = new HashMap();
	    params.put("actDefId", actDefId);
	    params.put("nodeId", nodeId);
	    params.put("parentActDefId", parentActDefId);
	    delBySqlKey("delByActDefIdAndNodeIdAndParentActDefId", params);
	  }
	
	/**
	 * 根据表id删除所有的表单权限数据。
	 * @param tableId
	 */
	public void deleteByTableId(Long tableId){
		String statment="deleteByTableId";
		this.delBySqlKey(statment, tableId);
	}
	
	/**
	 * 根据表单键删除权限。
	 * @param formKey
	 */
	public void delByFormKey(Long formKey){
		String statment="delByFormKey";
		this.delBySqlKey(statment, formKey);
	}
	
	/**
	 * 根据表单键删除权限。
	 * @param formKey
	 * @param cascade 是同时否删除表单的流程的流程节点表单权限设置
	 */
	public void delByFormKey(Long formKey,boolean cascade){
		String statment = null;
		if(cascade){
			statment="delByFormKey";	
		}else{
			statment="delByFormKeyExcActDefId";
		}
		this.delBySqlKey(statment, formKey);
	}
	
	
	/**
	 * 根据流程定义ID删除流程表单权限设置
	 * @param actDefId 流程定义ID
	 * @param cascade 是同时否删除的流程节点表单权限设置
	 */
	public void delByActDefId(String actDefId,boolean cascade){
		String statment=null;
		if(cascade){
			statment="delByActDefId";
		}else{
			statment="delByActDefIdExcNode";
		}
		this.delBySqlKey(statment, actDefId);
	}
	
	public void delByActDefId(String actDefId, boolean cascade, String parentActDefId)
	  {
	    Map<String,Object> params = new HashMap<String,Object>();
	    params.put("actDefId", actDefId);
	    params.put("parentActDefId", parentActDefId);
	    String statment = null;
	    if (cascade)
	      statment = "delByActDefId";
	    else {
	      statment = "delByActDefIdAndParentActDefIdExcNode";
	    }
	    delBySqlKey(statment, params);
	  }

	/**
	 * 根据表单key获得表单权限设置
	 * @param formKey 表单key
	 * 
	 */
	public List<FormRights> getByFormKeyActDefIdIsNotNull(Long formKey) {
		return this.getBySqlKey("getByFormKeyActDefIdIsNotNull", formKey);
	}
	
	/**
	 * 根据流程定义id，节点id获取表单的权限。
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<FormRights> getByFlowFormNodeId(String actDefId,String nodeId) {
		Map params=new HashMap();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		
		return this.getBySqlKey("getByFlowFormNodeId", params);
	}
	
	
	/**
	 * 根据表单id获取表单权限。
	 * @param formDefId
	 * @return
	 */
	public List<FormRights> getByFormDefId(Long formDefId) {
		return this.getBySqlKey("getByFormDefId", formDefId);
	}
	
	/**
	 * 根据formKey删除权限。
	 * @param formDefId
	 */
	public void delByFormDefId(Long formDefId) {
		this.delBySqlKey("delByFormDefId", formDefId);
	}
	
	/**
	 * 根据流程定义id，节点id删除表单权限。
	 * @param actDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void delByFlowFormNodeId(String actDefId,String nodeId){
		Map params=new HashMap();
		params.put("actDefId", actDefId);
		params.put("nodeId", nodeId);
		this.delBySqlKey("delByFlowFormNodeId", params);
	}

	public List<FormRights> getFormRights(Long formKey, String actDefId,
			boolean actDefIdIsNull, String nodeId, boolean nodeIdIsNull,
			String parentActDefId, boolean parentActDefIdIsNull,
			Integer platform) {
		Map params = new HashMap();
		params.put("formKey", formKey);

		if (actDefIdIsNull)
			params.put("actDefIdIsNull", Boolean.valueOf(actDefIdIsNull));
		else {
			params.put("actDefId", actDefId);
		}

		if (nodeIdIsNull)
			params.put("nodeIdIsNull", Boolean.valueOf(nodeIdIsNull));
		else {
			params.put("nodeId", nodeId);
		}

		if (parentActDefIdIsNull)
			params.put("parentActDefIdIsNull", Boolean
					.valueOf(parentActDefIdIsNull));
		else {
			params.put("parentActDefId", parentActDefId);
		}
		params.put("platform", platform);

		return getBySqlKey("getFormRights", params);
	}
	
}