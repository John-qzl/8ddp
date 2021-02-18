package com.cssrc.ibms.api.form.intf;

import java.io.InputStream;

import com.cssrc.ibms.api.form.model.IFormQuery;
import com.cssrc.ibms.api.form.model.IQueryResult;

public interface IFormQueryService {

	/**
	 * 根据别名获取对话框对象。
	 * 
	 * @param alias
	 * @return
	 */
	public abstract IFormQuery getByAlias(String alias);

	/**
	 * 检查别名是否唯一
	 * 
	 * @param alias
	 * @return
	 */
	public abstract boolean isExistAlias(String alias);

	/**
	 * 检查别名是否唯一。
	 * 
	 * @param alias
	 * @return
	 */
	public abstract boolean isExistAliasForUpd(Long id, String alias);

	public abstract String exportXml(Long[] tableIds) throws Exception;

	public abstract void importXml(InputStream inputStream) throws Exception;

	public abstract IQueryResult getData(String alias, String queryData,
			Integer page, Integer pageSize) throws Exception;
	
	public abstract IFormQuery getById(Long id);

}