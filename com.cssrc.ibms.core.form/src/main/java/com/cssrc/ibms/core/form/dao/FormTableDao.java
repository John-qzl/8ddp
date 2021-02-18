
package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 对象功能:自定义表 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class FormTableDao extends BaseDao<FormTable>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormTable.class;
	}

	/**
	 * 表名是否已存在。
	 * <pre>
	 * 表名区分大小写，使用多数据库。
	 * </pre>
	 * @param tableName
	 * @return
	 */
	public boolean isTableNameExisted(String tableName)
	{
		return (Integer) this.getOne("isTableNameExisted_"+this.getDbType(), tableName) > 0;
	}
	
	/**
	 * 判断表是否已经存在。
	 * <pre>
	 * 表名区分大小写，使用多数据库。
	 * </pre>
	 * @param tableId
	 * @param tableName
	 * @return
	 */
	public boolean isTableNameExistedForUpd(Long tableId, String tableName)
	{
		Map params =new HashMap();
		params.put("tableId", tableId);
		params.put("tableName", tableName);
		return (Integer) this.getOne("isTableNameExistedForUpd_"+this.getDbType(), params) > 0;
	}
	
	
	
	/**
	 * 判断表名是否存在。
	 * @param tableName
	 * @param dsAlias
	 * @return
	 */
	public boolean isTableNameExternalExisted(String tableName,String dsAlias)
	{
		FormTable FormTable=new FormTable();
		FormTable.setTableName(tableName);
		FormTable.setDsAlias(dsAlias);
		return (Integer) this.getOne("isTableNameExternalExisted", FormTable) > 0;
	}
	


	
	/**
	 * 通过mainTableId获得所有字表
	 * 
	 * @param mainTableId
	 * @return
	 */
	public List<FormTable> getSubTableByMainTableId(Long mainTableId)
	{
		return this.getBySqlKey("getSubTableByMainTableId", mainTableId);
	}
	/**
	 * 获取mainTable被其他表引用的所有外键列。
	 * 
	 * @param mainTableId
	 * @return
	 */
	public List<FormTable> getRelTableByMainTableId(Long mainTableId)
	{
		return this.getBySqlKey("getRelTableByMainTableId", mainTableId);
	}
	
	/**
	 * 按数据源别名获取所有主表
	 * @param dsName
	 * @return
	 */
	public List<FormTable> getMainTableByDsName(String dsName){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("dsName",dsName);
		return this.getBySqlKey("getMainTableByDsName",params);
	}
	
	/**
	 * 获取可以关联的主表。
	 * 1.没有关联表单。
	 * 2.可以是发布或未发布的。
	 * @return
	 */
	public List<FormTable> getAssignableMainTable()
	{
		return this.getBySqlKey("getAssignableMainTable");
	}
	
	/**
	 * 按过滤器查询记录列表
	 * @param queryFilter
	 * @return
	 */
	@Override
	public List<FormTable> getAll(QueryFilter queryFilter){
		String sqlKey="getAll_"+getDbType();
		List<FormTable> list =getBySqlKey(sqlKey, queryFilter);
		return list;
	}
	
	
	
	/**
	 * 获取未关联的主表。
	 * @return
	 */
	public List<FormTable> getAllUnpublishedMainTable(){
		return this.getBySqlKey("getAllUnpublishedMainTable");
	}

	
	/**
	 * 获得所有未分配的子表
	 * @param tableName
	 * @return
	 */
	public List<FormTable> getAllUnassignedSubTable( )
	{
		return this.getBySqlKey("getAllUnassignedSubTable", null);
	}
	
	
	
	/**
	 * 获取默认已发布的主表列表。
	 * @param queryFilter
	 * @return
	 */
	public List<FormTable> getAllMainTable(QueryFilter queryFilter) {
		String statementName = getIbatisMapperNamespace() + ".getAllMainTable";
		
		List<FormTable> list= getList(statementName,queryFilter);
		//设置回请求对象
		queryFilter.setForWeb();
		return list;
	}
	
	/**
	 * 根据数据源查询子表
	 * @param dsName
	 * @return
	 */
	public List<FormTable> getByDsSubTable(String dsName){
		 List<FormTable> list= this.getBySqlKey("getByDsSubTable", dsName);
		 return list;
	}
	
	/**
	 * 更新关联字段
	 * @param FormTable
	 */
	public void updateRelations(FormTable FormTable){
		this.update("updateRelations", FormTable);
	}
	
	/**
	 * 更新主表字段。
	 * @param FormTable
	 */
	public void updateMain(FormTable FormTable){
		this.update("updateMain", FormTable);
	}
	
	/**
	 * 清空子表的主表字段。
	 * @param mainTableId
	 */
	public void updateMainEmpty(Long mainTableId){
		this.update("updateMainEmpty", mainTableId);
	}
	
	/**
	 * 修改表为发布。
	 * @param FormTable
	 */
	public void updPublished(FormTable FormTable){
		this.update("updPublished", FormTable);
	}
	
		
	
	/**
	 * 根据数据源和表名获取 FormTable 对象。
	 * @param dsName
	 * @param tableName
	 * @return
	 */
	public FormTable getByDsTablename(String dsName,String tableName){
		Map<String,String> params=new HashMap<String, String>();
		params.put("dsAlias", dsName);
		params.put("tableName", tableName);
		return (FormTable) this.getOne("getByDsTablename", params);
	}
	
	/**
	 * 根据表名获得 FormTable 对象
	 * @param tableName
	 * @return
	 */
	public FormTable getByTableName(String tableName){
		return getUnique("getByTableName_" + this.getDbType(), tableName.toLowerCase());
	}

	public List<FormTable> getMainTables(String tableName) {
		Map<String,String> params=new HashMap<String, String>();
		params.put("tableName", tableName);
		return this.getBySqlKey("getMainTables",params);
	}
	
	public FormTable getByAliasTableName(String dsAlias,String tableName){
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("dsAlias", dsAlias);
		params.put("tableName", tableName);
		return this.getUnique("getByAliasTableName",params);
	}
	
	/**
	 * 根据流程定义ID返回表名称。
	 * 如果是外部表则返回 数据源别名 +“_" + 表名
	 * @param defId
	 * @return
	 */
	public List<FormTable> getTableNameByDefId(Long defId){
		return  this.getBySqlKey("getTableNameByDefId",defId);
	}
	
	/**
	 * 根据流程定义获取流程表
	 * @param defId
	 * @return
	 * @throws Exception 
	 */
	public FormTable getByDefId(Long defId) throws Exception{
		List<FormTable> list=this.getBySqlKey("getByDefId", defId);
		if(BeanUtils.isEmpty(list)){
		    throw new Exception("流程没有定义的表单!");
		}else if(list.size()>1){
		    throw new Exception("流程定义只能对应一个表!");
		}else{
		    return list.get(0);
		}
	}
	
	   /**
     * 根据流程定义获取流程表
     * @param defId
     * @return
     * @throws Exception 
     */
    public FormTable getByDefIdAndNodeId(Long defId,String nodeId) throws Exception{
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("nodeId", nodeId);
        params.put("defId", defId);
        List<FormTable> list=this.getBySqlKey("getByDefIdAndNodeId", params);
        if(BeanUtils.isEmpty(list)){
            throw new Exception("流程节点没有定义的表单!");
        }else if(list.size()>1){
            throw new Exception("流程节点定义只能对应一个表!");
        }else{
            return list.get(0);
        }
    }
    
	/**
	 * 获得所有的表
	 * @return
	 */
	public List<FormTable> getAllTable(){
		return this.getBySqlKey("getAllTable");
	}

	/**
	 * 只获取name,id 等几个字段
	 * @param tabid
	 * @return
	 */
	public Map<String, String> getTableMapById(Long tabid) {
		return (Map<String,String>)this.getOne("getByTabId",tabid);
	}
	
    /**
     * 只获取name,id 等几个字段
     * @param tabid
     * @return
     */
    public Map<String, String> getTableMapByName(String name) {
        return (Map<String,String>)this.getOne("getByTabName",name);
    }
	
	
	/**
	 * 根据数组ID获取表，只读取部分重要数据
	 * @param ids
	 * @return
	 */
	public List<FormTable> getByTabIds(String[] ids) {
		return this.getBySqlKey("getByTabIds", ids);
	}


	/**
	 * 获取所有表,只读取id,name,desc等重要数据
	 * @param params
	 * @return List<Map<String, String>>
	 */
	public List<?> getTabs(Map<String, String> params) {
		return this.getBySqlKey("getTabs",params);
	}
	
    /**
     * 根据数组names获取表，只读取部分重要数据
     * @param names
     * @return
     */
    public List<FormTable> getByTabNames(String[] names)
    {
        return this.getBySqlKey("getByTabNames", names);

    }

    /** 
    * @Title: getParentTableByTableId 
    * @Description: TODO(获取表的父亲表) 
    * @param @param tableId
    * @param @return     
    * @return List<FormTable>    返回类型 
    * @throws 
    */
    public List<FormTable> getParentTableByTableId(Long tableId)
    {
        return this.getBySqlKey("getParentTableByTableId", tableId);

    }	
	
	
}