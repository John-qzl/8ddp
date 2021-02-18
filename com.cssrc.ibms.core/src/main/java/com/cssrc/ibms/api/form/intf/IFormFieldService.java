package com.cssrc.ibms.api.form.intf;

import java.util.List;

import net.sf.json.JSONArray;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IFormFieldService {

	/**
	 * 通过tableId查找
	 * 
	 * @param tableId
	 *            自定义表Id
	 * @return
	 */
	public abstract List<? extends IFormField> getByTableId(Long tableId);

	/**
	 * 通过tableId查找所有（包括已删除的）
	 * 
	 * @param tableId
	 *            自定义表Id
	 * @return
	 */
	public abstract List<? extends IFormField> getAllByTableId(Long tableId);
	
	/**
	 * 根据表获取所有的字段。**************构造外键显示列***********************
	 * @param tableId
	 * @return
	 */
	public List<? extends IFormField> getFieldsByTableId(Long tableId);
	

	/**
	 * 根据流程定义ID获取流程变量。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @return
	 */
	public abstract List<? extends IFormField> getFlowVarByFlowDefId(Long defId);

	/**
	 * 根据流程定义ID获取流程变量。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @param excludeHidden
	 *            排除隐藏变量
	 * @return
	 */
	public abstract List<? extends IFormField> getFlowVarByFlowDefId(
			Long defId, boolean excludeHidden);

	public abstract List<? extends IFormField> getByTableIdContainHidden(
			Long tableId);

	/**
	 * 根据流程定义ID获取流程表单变量。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @return
	 */
	public abstract List<? extends IFormField> getFormVarByFlowDefId(Long defId);

	/**
	 * 通过tableId以及relTableId获取关系列。
	 * 
	 * @param tableId
	 * @return
	 */
	public abstract List<? extends IFormField> getRelFieldsByTableId(
			Long tableId, Long relTableId);

	/**
	 * 根据表ID和字段名称取得对应字段的数据（没有隐藏字段）。
	 * 
	 * @param defId
	 *            流程定义ID
	 * @return
	 */
	public abstract IFormField getFieldByTidFnaNh(Long tableId,
			String fieldName, String subTableName);

	public abstract JSONArray getFiledJSON(IFormTable table,
			List<? extends IFormField> fields, Boolean isMain, Boolean ifFilter);

	/**
	 * @param replace
	 * @param i
	 * @return
	 */
	public abstract List<? extends IFormField> getByTableName(String replace,
			int i);

	public abstract void getFileds(List<IFormField> userVarList,
			List<IFormField> orgVarList,
			List<IFormField> roleVarList,
			List<IFormField> posVarList,
			List<IFormField> otherList,
			Long defId);

	public abstract List<? extends IFormField> getAll(QueryFilter filter);
	
	/**
	 * 根据 tableid 和 fieldname 获得 FormField
	 * @param tableId
	 * @param fieldName
	 * @return
	 */
	public abstract IFormField getFieldByTidFna(Long tableId,String fieldName);

    /**
     * 获取需要同步表 字段信息。
     * @param tableId
     * @param tableName
     * @return
     */
    public abstract List<?extends IFormField> getRelFiledByTableIdAndName(String tableId, String tableName);

    /**
     * 根据选择器类型获取 所有相关自定义字段
     * @return
     */
    public abstract List<? extends IFormField> getFiledBySelector(Integer[] filedType);
}