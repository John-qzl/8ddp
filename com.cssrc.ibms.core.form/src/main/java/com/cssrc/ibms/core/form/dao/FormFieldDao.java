
package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormField;
/**
 * 对象功能:自定义表字段 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class FormFieldDao extends BaseDao<FormField>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormField.class;
	}
	
	/**
	 * 通过tableId查找
	 * @param tableId
	 * @return
	 */
	public List<FormField> getByTableId(Long tableId) {
		List<FormField> formFieldList = getBySqlKey("getByTableId", tableId);
		
		return formFieldList;
	}
	/**根据TableId查询所有表列，包含隐藏列**************构造外键显示列***********************
	 * **/
	public List<FormField> getByTableIdContainHidden(Long tableId) {
		List<FormField> formFieldList = getBySqlKey("getByTableIdContainHidden", tableId);
		
		 return formFieldList;
	}
	
	
	
	/**
	 * 通过tableId查找所有（包括已删除的）************构造外键显示列***********************
	 * @param tableId
	 * @return
	 */
	public List<FormField> getAllByTableId(Long tableId) {
		List<FormField> formFieldList =  getBySqlKey("getAllByTableId", tableId) ;
		
		 return formFieldList;
	}
	/**
	 * 通过tableId以及relTableId 获取关系列。
	 * @param tableId
	 * @return
	 */
	public List<FormField> getRelFieldsByTableId(Long tableId,Long relTableId) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("relTableId", relTableId);
		List<FormField> fieldList = this.getBySqlKey("getRelFieldsByTableId", map) ;
		return fieldList;
	}

	/**
	 * 将该表所有字段标识为删除
	 * @param tableId
	 * @return
	 */
	public int markDeletedByTableId(Long tableId) {
		return this.update("markDeletedByTableId", tableId);
		
	}
	
	/**
	 * 根据流程defId获取自定义表中的流程变量列表。
	 * @param defId
	 * @return
	 */
	public List<FormField> getFlowVarByFlowDefId(Long defId){
		 List<FormField> formFieldList =  this.getBySqlKey("getFlowVarByFlowDefId", defId);
		 return formFieldList;
	}
	
	/**
	 * 根据表id删除表的定义。
	 * @param tableId
	 */
	public void delByTableId(Long tableId){
		 this.delBySqlKey("delByTableId", tableId);
	}
	
	/**
	 * 根据 tableid 和 fieldname 获得 FormField
	 * @param tableId
	 * @param fieldName
	 * @return
	 */
	public FormField getFieldByTidFna(Long tableId,String fieldName) {
		
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("fieldName", fieldName);
		return this.getUnique("getFieldByTidFna", map);
	}
	
	/**
	 * 根据表获取所有的字段。**************构造外键显示列***********************
	 * @param tableId
	 * @return
	 */
	public List<FormField> getFieldsByTableId(Long tableId){
		 List<FormField> formFieldList = this.getBySqlKey("getFieldsByTableId", tableId);
		 
		 return formFieldList;
	}
	
	/**
	 * 根据 tableid 和 fieldname 和subTableName 获得 不是隐藏字段的FormField
	 * @param tableId
	 * @param fieldName
	 * @param subTableName
	 * @return
	 */
	public FormField getFieldByTidFnaNh(Long tableId,String fieldName,String subTableName) {
		Map map = new HashMap();
		map.put("tableId", tableId);
		map.put("fieldName", fieldName);
		map.put("subTableName", subTableName);
		return this.getUnique("getFieldByTidFnaNh", map);
	}

	public List<Map<String, String>> getFiledsByTabId(Long tabId) {
		return (List<Map<String, String>>)this.getListBySqlKey("getByTabId", tabId);
	}
	
	/**
	 * 根据数组ID获取表，只读取部分重要数据
	 * @param ids
	 * @return
	 */
	public List<FormField> getFiledsByIds(String[] ids) {
		return this.getBySqlKey("getByIds", ids);
	}

	/**
	 * 查找两个表的关系字段
	 * @param mtabId 主表ID
	 * @param reltabId 关系表ID
	 * @return
	 */
	public List<FormField> getRelFiledByTableId(Long mtabId, Long reltabId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("mtabId", mtabId);
		map.put("reltabId", reltabId);
		return this.getBySqlKey("getRelFiledByTableId", map);
	}
	
    /**
     * 根据 表ID 字段名获取字段信息，只读取部分重要数据
     * @param tableId
     * @param filedName
     * @return
     */
    public List<? extends IFormField> getFileds(Long tableId, String[] filedName)
    {
        Map<String,Object> param=new HashMap<>();
        param.put("tableId", tableId);
        param.put("filedName", filedName);
        return this.getBySqlKey("getFileds", param);
    }
    /**
	 * 获取结构化数据
	 * @param fieldName :字段名（不带F_）
	 * @param tableName	：表名（不带W_）
	 * @return
	 * @return:String
	 */
	public List<Map> getOpinonData(String fieldName_up,String fieldName_low,String tableName) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("fieldName_up", fieldName_up);
		map.put("fieldName_low", fieldName_low);
		map.put("tableName", tableName);
		return this.getListBySqlKey("getOpinonData", map);
		
	}
    
    /**
     * 获取需要同步表 字段信息。
     * @param tableId
     * @param tableName
     * @return
     */
    public List<FormField> getRelFiledByTableIdAndName(String relTableId, String relTableName)
    {
        Map<String,Object> param=new HashMap<>();
        param.put("relTableId", relTableId);
        param.put("relTableName", relTableName);
        return this.getBySqlKey("getRelFiledByTableIdAndName", param);
    }
    
    /**
     * 根据选择器类型获取 所有相关自定义字段
     * @return
     */
    public List<? extends IFormField> getFiledBySelector(Integer[] filedType)
    {
        return this.getBySqlKey("getFiledBySelector", filedType);
    }

}