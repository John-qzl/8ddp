package com.cssrc.ibms.api.form.intf;

import java.io.InputStream;
import java.util.List;

import com.cssrc.ibms.api.form.model.IQuerySql;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IQuerySqlService {

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

	public abstract List<? extends IQuerySql> getAll(QueryFilter queryFilter);

	public abstract IQuerySql getSysQuerySql(String json);

	public abstract void importXml(InputStream inputStream) throws Exception;

	public abstract String exportXml(Long[] tableIds) throws Exception;

	/**
	 * 根据别名获取查询对象。
	 * 
	 * @param alias
	 *            查询别名。
	 * @return
	 */
	public abstract IQuerySql getByAlias(String substring);

}