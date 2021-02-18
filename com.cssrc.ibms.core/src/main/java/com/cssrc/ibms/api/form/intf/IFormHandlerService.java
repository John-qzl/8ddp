package com.cssrc.ibms.api.form.intf;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormModel;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IPkValue;

public interface IFormHandlerService {
    public final static String _bpmFormDef_="bpmFormDef";//表单设计对象
    public final static String _businessKey_="businessKey";//业务数据主键Id
    public final static String _userId_="userId";//当前用户Id
    public final static String _processRun_="processRun";//流程运行对象
    public final static String _contextPath_="contextPath";//web context path
    public final static String _isCopyFlow_="isCopyFlow";   //是否复制流程
    public final static String _displayId_="displayId";//datatemplate 模板 Id
    public final static String _highLight_="highLight";//datatemplate 模板 Id

    
	/**
	 * @param value
	 * @param businessKey
	 * @param formDefId
	 */
	public void updateProcessData(String value,String businessKey,Long formKey);
	/**
	 * 删除业务数据。
	 * 
	 * @param dsAlias
	 * @param tableName
	 * @param businessKey
	 */
	void delByDsAliasAndTableName(String dsAlias, String tableName,
			String businessKey) throws Exception;

	/**
	 * 
	 * @param businessKey
	 * @param bpmFormTable
	 * @param json
	 * @return
	 * @throws Exception
	 */
	IFormData getFormData(String businessKey, IFormTable bpmFormTable,
			String json) throws Exception;


	/** 
	* @Title: getFormDetail 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param param
	* @param @return
	* @param @throws Exception     
	* @return String    返回类型 
	* @throws 
	*/
	String getFormDetail(Map<String,Object> param) throws Exception;
    
    /**
     * @param formDefId
     * @param businessKey
     * @param currUserId
     * @param processRun
     * @param ctxPath
     * @param b
     * @return
     * @throws Exception
     */
    public String getFormDetail(Long formDefId, String businessKey, Long currUserId, IProcessRun processRun,
        String ctxPath, boolean isCopyForm) throws Exception;
    
	/**
	 * @param businessKey
	 * @param string
	 */
	void delByIdTableName(String businessKey, String string);

	/**
	 * @param tableName
	 * @param businessKey
	 * @return
	 */
	boolean isExistsData(String tableName, String businessKey);

	/**
	 * 根据主键获取子表的数据。
	 * 
	 * @param tableName
	 * @param pk
	 * @return
	 */
	List getByFk(String tableName, Long fk);

	/**
	 * 删除业务数据。
	 * 
	 * @param id
	 * @param tableName
	 * @throws Exception
	 */
	void delById(String businessKey, String tableName) throws Exception;

	/**
	 * 保存表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	void handFormData(Map<String, Object> params, IFormData bpmFormData,
			IProcessRun processRun) throws Exception;

	/**
	 * 处理动态表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	void handFormData(Map<String, Object> params, IFormData bpmFormData,
			IProcessRun processRun, String nodeId) throws Exception;

	/**
	 * 
	 * @param bpmFormTable
	 * @param pk
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getByKey(IFormTable bpmFormTable, String pk,
			String[] fields) throws Exception;

	/**
	 * 根据主键查询列表数据。
	 * 
	 * @param tableId
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	IFormData getByKey(Map<String, Object> parmas, long tableId,
			String pkValue, boolean isHandleData) throws Exception;

	/**
	 * 获取新的主键
	 * 
	 * @param pkname
	 * @param pkvalue
	 * @param isAdd
	 * @return
	 */
	IPkValue getNewPkValue(String pkname, String pkvalue, boolean isAdd);

	/**
	 * 保存表单数据
	 * 
	 * @param bpmFormData
	 * @throws Exception
	 */
	void handFormData(Map<String, Object> params, IFormData bpmFormData)
			throws Exception;

	/**
	 * 取得起始表单。
	 * @param businessKey
	 * @param actDefId
	 * @param ctxPath
	 * @param userId TODO
	 * @param formKey
	 * 
	 * @throws Exception
	 */
	IFormModel getStartForm(INodeSet nodeSet, String businessKey,
			String actDefId, String ctxPath, Long userId) throws Exception;

	/**
	 * @param formDefId
	 * @param userId
	 * @param businessKey
	 * @param string
	 * @param actDefId
	 * @param nodeId
	 * @param ctxPath
	 * @param string2
	 * @param b
	 * @return
	 * @throws Exception 
	 */
	String obtainHtml(Long formId, Long userId, String pkValue,
			String instanceId, String actDefId, String nodeId, String ctxPath,
			String parentActDefId, boolean isCopyFlow) throws Exception;

	/**
	 * 将json 转成form
	 * @param json
	 * @param pkValue
	 * @param bpmFormTable
	 * @return
	 * @throws Exception 
	 */
	IFormData getFormFromJson(String json, IPkValue pkValue,
			IFormTable bpmFormTable) throws Exception;

	/**
	 * @param bpmFormData
	 * @param processRun
	 * @throws Exception 
	 */
	void handFormData(IFormData bpmFormData, IProcessRun processRun) throws Exception;

	/**
	 * @param bpmFormData
	 * @param processRun
	 * @param nodeId
	 * @throws Exception 
	 */
	void handFormData(IFormData bpmFormData, IProcessRun processRun,
			String nodeId) throws Exception;
	/**
	 * 根据主键获取关联表的数据。
	 * @param tableName
	 * @param fkFieldName 外键字段
	 * @param fkFieldValue外键值
	 * @param orderFieldName 排序字段
	 *   
	 * @return
	 */
	List getRelDataByFk(String relTableName, String fkFieldName,
			Object fkFieldValue, String orderFieldName);
	
	
	
	/**
	 * 根据业务表id查找记录
	 *@author YangBo @date 2016年11月24日上午11:19:15
	 *@param jdbcTemplate
	 *@param pk
	 *@param bpmFormTable
	 *@param isHandleData
	 *@return
	 */
	 public Map<String, Object> getByKey(JdbcTemplate jdbcTemplate,  String  pkField,String pkValue,IFormTable bpmFormTable ,boolean isHandleData);
	 
	 /**
	 * 根据主键和节点id查找列表数据
	 * @author liubo
	 * @param pkValue
	 * @param nodeId
	 * @return
	 */
	public IFormData getBpmFormData(String pkValue, String nodeId);
	
	/**
	 * 根据主键查找记录
	 * @author liubo
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public Map<String, Object> getByKey(String tableName, String pk);

    /**
     * rpc 远程获取 formdata
     * @param params
     * @param tableId
     * @param pkValue
     * @param instanceId
     * @param actDefId
     * @param nodeId
     * @param isCopyFlow
     * @return
     * @throws Exception 
     */
    IFormData getBpmFormData(Map<String, Object> params, Long tableId, String pkValue, String instanceId,
        String actDefId, String nodeId, boolean isCopyFlow) throws Exception;

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
    void saveFormData(IFormTable bpmFormTable, Long tableId, int isNeed, String pkData, String formData, String formKey) throws Exception;
   
}