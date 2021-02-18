package com.cssrc.ibms.api.form.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public abstract interface IFormDialogService {
	/**
	 * 检查模板别名是否唯一
	 * @param alias
	 * @return
	 */
	public abstract boolean isExistAlias(String alias);

	/**
	 * 检查模板别名是否唯一。
	 * @param alias
	 * @return
	 */
	public abstract boolean isExistAliasForUpd(Long id, String alias);


	/**
	 * 根据别名获取对话框对象。
	 * @param alias
	 * @return
	 */
	public abstract IFormDialog getByAlias(String alias);

	/**
	 * 返回树型结构的数据。
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public abstract List getTreeData(String alias) throws Exception;

	/**
	 * 返回树型结构的数据。
	 * 
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public abstract List getTreeData(String alias,Map<String,Object> params,boolean isRoot) throws Exception ;


	/**
	 * 根据别名获取对应对话框的数据。
	 * @param alias		对话框别名。
	 * @param params	参数集合。
	 * @return
	 * @throws Exception 
	 */
	public abstract IFormDialog getData(String alias,Map<String, Object> params) throws Exception;

	/**
	 * 获取外键显示值数据
	 * @param alias		对话框别名。
	 * @param params	参数集合。
	 * @return
	 * @throws Exception 
	 */
	public abstract Map<String, Object> getFKColumnShowData(String alias,Map<String, Object> params) throws Exception;


	
	public abstract IFormDialog getById(Long valueOf);
	
    /**
     * 根据dialog 别名获取 对话框数据源的表信息
     * @param alias
     */
    IFormTable getFormDilaogTableInfo(String alias);

    /**
     * 获取所有 自定义对话框
     * @param queryFilter
     * @return
     */
    public abstract List<? extends IFormDialog> getAll(QueryFilter queryFilter);

}
