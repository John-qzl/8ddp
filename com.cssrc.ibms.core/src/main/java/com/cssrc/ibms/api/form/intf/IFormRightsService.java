package com.cssrc.ibms.api.form.intf;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;

import com.cssrc.ibms.api.form.model.IFormRights;

public interface IFormRightsService{

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
	public abstract JSONObject getPermissionJson(String title, String memo,
			int type);

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
	public abstract Map<String, List<JSONObject>> getPermission(Long formKey,
			String actDefId, String nodeId);

	/**
	 * 根据表ID和表单id获取表表单的权限。
	 * 
	 * @param tableId
	 *            数据表id
	 * @param formKey
	 *            表单定义key
	 * @return
	 */
	public abstract Map<String, List<JSONObject>> getPermissionByTableId(
			Long tableId);

	public abstract Map<String, Map<String, String>> getByFormKeyAndUserId(
			Long formKey, Long userId, String actDefId, String nodeId,
			String parentActDefId);

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
	public abstract Map<String, Map<String, String>> getByFormKeyAndUserId(
			Long formKey, Long userId, String actDefId, String nodeId);

	public abstract Map<String, Map<String, String>> getByFormKeyAndUserId(
			Long formKey, Long userId, String actDefId, String nodeId,
			String parentActDefId, Integer platform);


	public abstract void updateRights();

	/**
	 * 根据formkey删除表单权限
	 * @param cascade 是同时否删除表单人流程的流程节点表单权限设置
	 * @param formKey
	 */
	public abstract void deleteByFormKey(Long formKey, boolean cascade);

	/**
	 * 根据表id删除表单的权限。
	 * 
	 * @param tableId
	 */
	public abstract void deleteByTableId(Long tableId);

	/**
	 * 删除表单权限设置。
	 * @param formKey
	 * @param cascade 级联删除标志。如果为True，同时删除所有与表单相关联的流程、节点权限设置
	 */
	public abstract void delFormRights(Long formKey, boolean cascade);

	/**
	 * 删除表单流程权限设置
	 * @param actDefId
	 * @param cascade 级联删除标志。如果为True，同时删除所有流程的节点权限设置
	 */
	public abstract void delFormFlowRights(String actDefId, boolean cascade);

	/**
	 * 删除表单节点权限设置
	 * @param actDefId
	 * @param nodeId
	 */
	public abstract void delFormFlowNodeRights(String actDefId, String nodeId);

	/**
	 * 根据流程定义id，任务节点id和表单id获取权限数据。
	 * @param actDefId		流程定义ID
	 * @param nodeId		任务节点
	 * @param formKey		表单定义ID
	 * @return
	 */
	public abstract Map<String, List<JSONObject>> getPermissionByFormNode(
			String actDefId, String nodeId, Long formKey);

	/**
	 * 根据formkey删除表单权限
	 * @param formKey
	 */
	public abstract void deleteByFormKey(Long formKey);

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
	public abstract Map<String, List<JSONObject>> getRelTablePermission(
			Long formKey, Long userId, String actDefId, String nodeId);

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
	public abstract Map<String, List<JSONObject>> getSubTablePermission(
			Long formKey, Long userId, String actDefId, String nodeId);

	/**
	 * 根据formkey\actDefId\nodeId删除表单权限
	 * @param formKey
	 * @param actDefId
	 * @param nodeId
	 */
	public abstract void deleteRight(Long formKey, String actDefId,
			String nodeId);

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
	public abstract JSONObject getReadPermissionJson(String title, String memo,
			int type);

	/**
	 * 根据流程定义ID删除流程表单权限设置
	 * @param actDefId 流程定义ID
	 * @param cascade 是同时否删除的流程节点表单权限设置
	 */
	public abstract void delByActDefId(String actDefId,boolean cascade);

	public abstract IFormRights getById(Long id);

	public abstract void update(IFormRights bpmFormRights);

	public abstract void add(IFormRights bpmFormRights);
	/**
	 * 根据actDefId 获取表单权限。
	 * @param actDefId
	 * @param cascade
	 * @return
	 */
	public abstract List<? extends IFormRights> getFormRightsByActDefId(
			String actDefId);
	
	/**
	 * 根据流程定义id，节点id删除表单权限。
	 * @param actDefId		流程定义ID
	 * @param nodeId		流程节点ID
	 */
	public abstract void delByFlowFormNodeId(String actDefId, String nodeId);

}