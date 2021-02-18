package com.cssrc.ibms.api.form.intf;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;

public interface IDataTemplateService {
	
	public String getDisplay(IDataTemplate dataTemplate,Long curUserId,Map<String,Object> params) throws Exception;
	
	public String generateTemplate(IDataTemplate bpmDataTemplate,String html)throws Exception;
	
	public IFormTable getFieldListByTableId(Long tableId) ;

	public abstract IDataTemplate getByFormKey(Long formKey);

	/**
	 * 获得显示的字段的头
	 * 
	 * @param name
	 * @param desc
	 * @param permission
	 * @return
	 */
	public abstract String getShowField(String name, String desc,
			String action, Map<String, String> sort,
			Map<String, Object> permission);

	/**
	 * 获得显示的字段的头
	 * 
	 * @param name
	 * @param desc
	 * @param permission
	 * @return
	 */
	public abstract String getShowField(String name, String html,
			Map<String, Object> permission);

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
	public abstract String getShowFieldList(String name,
			Map<String, Object> data, Map<String, Object> formatData,
			Map<String, Object> permission);

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
	public abstract String getManage(String name, String desc, String pkField,
			Map<String, Object> data, Map<String, String> actionUrl,
			Map<String, Object> permission);

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
	public abstract String getToolBar(String name, String desc,
			Map<String, String> actionUrl, Map<String, Object> permission);

	/**
	 * 预览数据//TODO
	 * 
	 * @param id
	 * @param params
	 * @param queryParams
	 * @param curUserId TODO
	 * @return
	 * @throws Exception
	 */
	public abstract String getDisplay(Long id, Map<String, Object> params, Map<String, Object> queryParams, Long curUserId) throws Exception;

	/**
	 * 获取当前用户的权限Map
	 * 
	 * @param userId
	 * @param curOrgId
	 * @return
	 */
	public abstract Map<String, Object> getRightMap(Long userId, Long curOrgId);

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
	public abstract String addConditionSQL(Map<String, Object> params,
			String querySQL);

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
	public abstract String getFilterSQL(JSONObject filterJson,
			Map<String, String> tableMap,
			Map<String, Map<String, String>> relationMap);

	/**
	 * 修正后的表名
	 * 
	 * @param tableName
	 * @param source
	 * @return
	 */
	public abstract String fixTableName(String tableName, String source);


	/** 
	* @Title: getForm 
	* @Description: TODO(获取表单数据) 
	* @param @param id datatemplate 模板Id
	* @param @param pk 业务数据主键ID
	* @param @param curUserId 当前用户ID
	* @param @param contextPath web context path
	* @param @return
	* @param @throws Exception     
	* @return String    返回类型 
	* @throws 
	*/
	public abstract String getForm(Long id, String pk, Long curUserId, String contextPath) throws Exception;
	
	/** 
	* @Title: getForm 
	* @Description: TODO(获取表单数据) 
	* @param @param map 参数集合
	* @param @return
	* @param @throws Exception     
	* @return String    返回类型 
	* @throws 
	*/
	public abstract Map<String,Object> getForm(Map<String, Object> map) throws Exception;
	/**
	 * 删除数据 ，包括批量删除数据
	 * 
	 * @param id
	 * @param _pk_
	 *            用逗号撇开。
	 * @throws Exception
	 */
	public abstract void deleteData(Long bpmDataTemplateId, String _pk_)
			throws Exception;

	/**
	 * 导出execl
	 * 
	 * @param id
	 * @param exportIds
	 * @param exportType
	 * @param curUserId TODO
	 * @param filterKey
	 * @return
	 * @throws Exception
	 */
	public abstract HSSFWorkbook export(Long id, String[] exportIds,
			int exportType, Map<String, Object> params, Long curUserId) throws Exception;

	// TODO =================导入===================================
	/**
	 * 导入文件
	 * 
	 * @param inputStream
	 * @param id
	 * @throws Exception
	 */
	public abstract StringBuffer importFile(InputStream inputStream, Long id)
			throws Exception;

	/**
	 * 根据formKey获取业务表单数量。
	 * 
	 * @param formKey
	 * @return
	 */
	public abstract Integer getCountByFormKey(Long formKey);

	/**
	 * 编辑业务数据模板数据.
	 */
	public abstract Map<String, Object> editData(Long displayId, String pk,
			Long userId, String ctxPath) throws Exception;

	public abstract IDataTemplate getById(Long displayId);

	public abstract void update(IDataTemplate dataTemplate);

	/**
	 * 通过自定义表获取数据源
	 * 
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	public abstract JdbcHelper getJdbcHelper(IFormTable formTable)
			throws Exception;
	
	
	/**
	 * 获取格式化后数据
	 * @return
	 */
	public Map<String, Object> getFormatDataMap(Long tableId, int tag);
	
    
    /**
     * 获取表单里配置的过滤条件
     * @param id 业务数据模板datatemplate id 
     * @param filterKey 业务表单中配置的 filter key 
     * @return
     */
    public String getFilterSql(Long id, String filterKey);

    /**
     * 通过业务数据主键，业务数据模板获取流程实例ID，可能取到多条，只去第一条
     * @param pk 业务数据主键
     * @param displayId 业务数据模板ID
     * @return
     */
    public abstract String getActInstId(String pk, String displayId);

    /**
     * rpc 远程 批量删除
     * @param _pk_
     */
    public abstract void delFileOfData(String _pk_);

    /**
     * 通过 template 别名获取 data template
     * @param string
     * @return
     */
    public abstract IDataTemplate getByAlias(String template);
    
    
    /** 
    * @Title: getFormatDataMap 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param fieldList
    * @param @return   
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String, Object> getFormatDataMap(List<?extends IFormField> fieldList);

    /** 
    * @Title: getPermission 
    * @Description: TODO(获取字段的权限) 
    * @param @param rightTypeShow
    * @param @param displayField
    * @param @param rightMap    
    * @return void    返回类型 
    * @throws 
    */
    public abstract Map<String, Boolean> getPermission(int rightTypeShow, String displayField, Map<String, Object> rightMap);

    /** 
    * @Title: hasRight 
    * @Description: TODO(判断是否有权限。) 
    * @param @param permission
    * @param @param rightMap
    * @param @return   
    * @return boolean    返回类型 
    * @throws 
    */
    public boolean hasRight(com.alibaba.fastjson.JSONObject permission, Map<String, Object> rightMap);

    /** 
    * @Title: getFormHeadHtml 
    * @Description: TODO(引用头样式) 
    * @param @param displayId
    * @param @param contextPath
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    public abstract String getFormHeadHtml(Long displayId, String contextPath);

    public String getDisplayBySubjects(String allSubjects);

	public String getDisplayIdByFormAliases(String allFormAliases);
    
}