package com.cssrc.ibms.api.system.intf;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;

public interface ISysDataSourceService{

	/**
	 * 根据id集合删除数据源。
	 */
	public abstract void delByIds(Long[] ids);

	/**
	 * 测试数据源是否可以连接
	 * 
	 * @param ids
	 * @return
	 */
	public abstract List<Map<String, Object>> testConnectById(Long[] ids);

	/**
	 * 根据别名获取数据源
	 * @param alias
	 * @return
	 */
	public abstract ISysDataSource getByAlias(String alias);

	/**
	 * 别名是否已存在
	 * @param alias
	 * @return
	 */
	public abstract boolean isAliasExisted(String alias);

	/**
	 * 通过数据源别名获取数据源管理类
	 * @param alias
	 * @return
	 */
	public abstract DriverManagerDataSource getDriverMangerDataSourceByAlias(
			String dsAlias);

	/**
	 * @return
	 */
	public abstract List<? extends ISysDataSource> getAll();
	
	
	/**
	 * 获取系统名称
	 * @return
	 */
	public String getAppName();
	
    /**
     * 获取表空间
     * @return
     */
	public String getTableSpace();

    /**
     * 获取数据库类型
     * @return
     */
    public abstract String getJdbcType();
    
	/**
	 * 获取数据源信息
	 * @param sysDataSourceDef
	 * @return
	 */
	public ISysDataSource getSysDataSourceByDef(ISysDataSourceDef sysDataSourceDef) ;


}