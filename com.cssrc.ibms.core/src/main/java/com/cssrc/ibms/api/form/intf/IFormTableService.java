package com.cssrc.ibms.api.form.intf;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cssrc.ibms.api.form.model.BaseFormTableXml;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.migration.model.IOutTable;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IFormTableService{

	/**
	 * 数据迁移，外部表转换成IBMS表
	 * @param outTable
	 * @param log
	 */
	public boolean saveFormTable(IOutTable outTable,StringBuffer log);

	/**
	 * 获取选择器关联的字段
	 * 
	 * @param tableId
	 * @return
	 */
	public abstract Map<String, ? extends IFormField[]> getExecutorSelectorField(
			Long tableId);

	/**
	 * 根据表名判断是否该表在系统中已经定义。
	 * 
	 * @param tableName
	 * @return
	 */
	public abstract boolean isTableNameExisted(String tableName);

	/**
	 * 判断表是否已存在，在更新时使用。
	 * 
	 * @param tableId
	 * @param tableName
	 * @return
	 */
	public abstract boolean isTableNameExistedForUpd(Long tableId,
			String tableName);

	/**
	 * 判断表名是否存在。
	 * 
	 * @param tableName
	 * @param dsAlias
	 * @return
	 */
	public abstract boolean isTableNameExternalExisted(String tableName,
			String dsAlias);

	/**
	 * 通过mainTableId获得所有字表
	 * 
	 * @param mainTableId
	 * @return
	 */
	public abstract List<? extends IFormTable> getSubTableByMainTableId(Long mainTableId);

	/**
	 * 获取mainTable被其他表引用的所有外键列。
	 * 
	 * @param mainTableId
	 * @return
	 */
	public abstract List<? extends IFormTable> getRelTableByMainTableId(Long mainTableId);

	/**
	 * 获得所有未分配的子表
	 * 
	 * @param tableName
	 * @return
	 */
	public abstract List<? extends IFormTable> getAllUnassignedSubTable();

	/**
	 * 根据数据源名称取得子表。
	 * 
	 * @param dsName
	 * @return
	 */
	public abstract List<? extends IFormTable> getByDsSubTable(String dsName);

	/**
	 * 将表定义进行发布。
	 * 
	 * @param tableId
	 *            表定义ID
	 * @param operator
	 *            发布人
	 * @throws Exception
	 */
	public abstract void generateTable(Long tableId, String operator)
			throws Exception;

	/**
	 * 创建业务数据历史表
	 * 
	 * @Methodname: generateHistoryTable
	 * @Discription:
	 * @param tableId
	 * @throws Exception
	 * 
	 */
	public abstract void ensureHistoryTableExixts(Long tableId)
			throws Exception;

	/**
	 * 关联子表。
	 * 
	 * @param mainTableId
	 * @param subTableId
	 * @throws Exception
	 */
	public abstract void linkSubtable(Long mainTableId, Long subTableId,
			String fkField) throws Exception;

	/**
	 * 取消关联
	 * 
	 * @param subTableId
	 */
	public abstract void unlinkSubTable(Long subTableId);

	/**
	 * 获取可以关联的主表。 1.没有关联表单。 2.可以是发布或未发布的。
	 * 
	 * @return
	 */
	public abstract List<? extends IFormTable> getAssignableMainTable();

	/**
	 * 获取未发布的主表。
	 * 
	 * @return
	 */
	public abstract List<? extends IFormTable> getAllUnpublishedMainTable();

	/**
	 * 查找默认主表列表
	 * 
	 * @param queryFilter
	 * @return
	 */
	public abstract List<? extends IFormTable> getAllMainTable(QueryFilter queryFilter);

	public abstract List<? extends IFormTable> getMainTables(String tableName);

	public abstract IFormTable getByTableName(String tableName);

	/**
	 * 获取同名表
	 * **/
	public abstract IFormTable getByTableName(String tabName, int need);

	/**
	 * 根据数据源和表名获取表对象。
	 * 
	 * @param dsName
	 * @param tableName
	 * @return
	 */
	public abstract IFormTable getByDsTablename(String dsName, String tableName);

	/**
	 * 根据表的Id删除外部表定义。
	 * 
	 * @param tableId
	 */
	public abstract void delExtTableById(Long tableId);

	/**
	 * 根据表ID删除表定义。
	 * 
	 * <pre>
	 * 1.如果该表是主表，那么先删除其所有的关联的子表。
	 * 2.根据表的ID删除表定义。
	 * </pre>
	 * 
	 * @param tableId
	 */
	public abstract void delByTableId(Long tableId);

	/**
	 * 根据表Id删除表所有字段
	 * @param tableId
	 */
	public abstract void delFieldsByTableId(Long tableId);

	/**
	 * 根据表id获取表的表和表字段的信息。
	 * 
	 * <pre>
	 * 	如果输入的是主表id。
	 *  那么将返回主表的信息和字段信息，同时返回子表和字段的信息。
	 *  字段数据不包含删除的字段和隐藏的字段。
	 *  need 是否包含隐藏或者删除字段   0为正常字段   1为正常字段加上隐藏字段    2 为正常字段加上逻辑删除字段
	 * </pre>
	 * 
	 * @param tableId
	 * @return
	 */
	public abstract IFormTable getTableById(Long tableId, int need);

	//获取表的列信息
	public abstract List<? extends IFormField> getFormFieldList(Long tableId, int need);

	/**
	 * 根据流程定义获取流程表的所有数据。
	 * 
	 * <pre>
	 * 1.包括表的元数据。
	 * 2.表的列元数据列表。
	 * 3.子表数据。
	 * 4.子表列的元数据。
	 * </pre>
	 * 
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	public abstract IFormTable getByDefId(Long defId) throws Exception;
	
    /**
     * 根据流程定义获取流程表的所有数据。
     * 
     * <pre>
     * 1.包括表的元数据。
     * 2.表的列元数据列表。
     * 3.子表数据。
     * 4.子表列的元数据。
     * </pre>
     * 
     * @param defId
     * @return
     * @throws Exception
     */
    public abstract IFormTable getByDefIdAndNodeId(Long defId,String nodeId) throws Exception;
	/**
	 * 
	 * @param tableId
	 * @return
	 */
	public abstract IFormTable getTableById(Long tableId);

	/**
	 * 根据表id获取(主表,rel关系表,sub子表)信息
	 * 
	 * @param tableId
	 *            表ID
	 * @param need
	 *            0为正常字段 1为正常字段加上隐藏字段 2 为正常字段加上逻辑删除字段
	 * @return
	 */
	public abstract IFormTable getByTableId(Long tableId, int need);

	/**
	 * 根据表的ID取得表的列名是否可以编辑。
	 * 
	 * @param tableId
	 * @return
	 */
	public abstract boolean getCanEditColumnNameTypeByTableId(Long tableId);

	/**
	 * 生成所有发布表
	 * 
	 * @param queryFilter
	 * @throws Exception
	 */
	public abstract void genAllTable() throws Exception;

	/**
	 * 生成表
	 * 
	 * @param tableId
	 * @throws Exception
	 */
	public abstract void genTable(Long tableId) throws Exception;

	/**
	 * 保存复制的表和字段
	 * 
	 * @param json
	 * @throws Exception
	 */
	public abstract void saveCopy(String json) throws Exception;

	/**
	 * @param json
	 * @param addSync
	 * @throws Exception
	 */
	public abstract void saveCopy(String json, boolean addSync)
			throws Exception;

	/**
	 * TODO 导入自定义表XML
	 * 
	 * @param fileStr
	 * @throws Exception
	 */
	public abstract void importXml(InputStream inputStream) throws Exception;

	/**
	 * 设置导入的流水号
	 * 
	 * @param identityList
	 * @param identitySet
	 */
	public abstract void setSerialNumber(List<?extends ISerialNumber> serialList,
        Set<ISerialNumber> serialSet);

	/**
	 * 导入的流水号
	 * 
	 * @param identitySet
	 * @throws Exception
	 */
	public abstract void importSerialNumber(Set<ISerialNumber> serialSet)
			throws Exception;


	/**
	 * 查询包含隐藏的表和字段
	 * 
	 * @param tableId
	 * @return
	 */
	public abstract IFormTable getTableByIdContainHidden(Long tableId);


	/**
	 * TODO 导出自定义表XML
	 * 
	 * @param Long
	 *            [] tableIds 导出的tableId
	 * @param map
	 * @return
	 */
	public abstract String exportXml(Long[] tableIds, Map<String, Boolean> map)
			throws Exception;

	/**
	 * 重置自定义表
	 * 
	 * @param tableId
	 * @throws Exception
	 */
	public abstract void reset(Long tableId);

	/**
	 * @param dsAlias
	 * @param tableName
	 * @return
	 */
	public abstract IFormTable getByAliasTableName(String dsAlias,
			String tableName);

	/**
	 * 获取可授权的主表
	 * 
	 * @param tableId
	 *            子表的Id
	 * @return
	 */
	public abstract List<? extends IFormTable> getMainTableSubTableId(Long tableId);

	/**
	 * 获取默认的导出的Map
	 * 
	 * @param map
	 * @return
	 */
	public abstract Map<String, Boolean> getDefaultExportMap(
			Map<String, Boolean> map);
	
	



	/**
	 * 根据表ID得到表数据
	 * @param params
	 * @return
	 */
	List<Map<String, String>> getTabs(Map<String, String> params);

	/**
	 * 只获取name,id 等几个字段
	 * @param tabid
	 * @return
	 */
	Map<String, String> getTableMapById(Long tabId);
	
	 /**
     * 只获取name,id 等几个字段
     * @param tabid
     * @return
     */
    Map<String, String> getTableMapByName(String name);
	

	/**
	 * 根据表IDs得到表数据
	 * @param ids 数组 表ID
	 * @return
	 */
	List<? extends IFormTable> getByTabIds(String[] ids);
	
    /**
     * 根据表names得到表数据
     * @param names
     * @return
     */
    List<? extends IFormTable> getByTabNames(String[] names);

	/**
	 * 查找表的字段
	 * @param tabId
	 * @return
	 */
	public List<Map<String, String>> getFiledsByTabId(Long tabId);
	
	/**
	 * 根据数组ID获取表，只读取部分重要数据
	 * @param ids
	 * @return
	 */
	List<? extends IFormField> getFiledsByIds(String[] ids);
	
    /**
     * 根据 表ID 字段名获取字段信息，只读取部分重要数据
     * @param tableId
     * @param filedName
     * @return
     */
    List<? extends IFormField> getFileds(Long tableId,String[] filedName);	

	/**
	 * 查找两个表的关系字段
	 * @param mtabId 主表ID
	 * @param reltabId 关系表ID
	 * @return
	 */
	public List<? extends IFormField> getRelFiledByTableId(Long mtabId, Long reltabId);

	/** 
	 * 导入自定义表
	 * @param bpmFormTableXmlList
	 * @throws Exception
	 */
	public abstract void importFormTableXml(
			List<BaseFormTableXml> bpmFormTableXmlList)throws Exception;

	public abstract IFormTable getById(Long tableId);

	/**
	 * 导出表的信息
	 * 
	 * @param formTable
	 * @param map
	 * @return
	 */
	public abstract BaseFormTableXml exportTable(Long tableId,
			Map<String, Boolean> map);
	/**
	 * 根据流程定义ID返回表名称。
	 * 如果是外部表则返回 数据源别名 +“_" + 表名
	 * @param defId
	 * @return
	 */
	public abstract List<? extends IFormTable> getTableNameByDefId(Long defId);

	public abstract List<? extends IFormTable> getAll(QueryFilter filter);

	/**
	 * 根据前端所配置 获得表信息列表
	 * @param request
	 * @return
	 */
	public abstract List<?extends IFormTable> getFormtableList(String[] tableIds,
			String[] classNames, String[] classVars, String[] packageNames,
			String system, String[] formDefIds);

	public abstract String getFormHtml(IFormDef bpmFormDef, IFormTable table,
			boolean isEdit) throws Exception;

}