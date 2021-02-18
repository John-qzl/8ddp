package com.cssrc.ibms.system.dao;
import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.cache.impl.DynamicDataSourceCache;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysDataSource;
/**
 * 对象功能:系统数据源管理 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class SysDataSourceDao extends BaseDao<SysDataSource>
{
	@Resource
	private DynamicDataSourceCache dynamicDataSourceCache;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return SysDataSource.class;
	}
	
	/**
	 * 别名是否已存在
	 * @param alias
	 * @return
	 */
	public boolean isAliasExisted(String alias) {
		return (Integer)getOne("isAliasExisted", alias) > 0;
	}
	
	/**
	 * 更新的别名是否已存在
	 * @param sysDataSource
	 * @return
	 */
	public boolean isAliasExistedByUpdate(SysDataSource sysDataSource) {
		return (Integer)getOne("isAliasExistedByUpdate", sysDataSource) > 0;
	}
	
	/**
	 * 根据别名获取数据源
	 * @param alias 数据源别名
	 * @return
	 */
	public SysDataSource getByAlias(String alias){
		return this.getUnique("getByAlias", alias);
	}
	
	/**
	 * 通过数据源别名获取数据源管理类
	 * @param alias
	 * @return
	 */
	public DriverManagerDataSource getDriverMangerDataSourceByAlias(String dsAlias){
		DriverManagerDataSource driverManagerDataSource =dynamicDataSourceCache.getDataSource(dsAlias);
		if(driverManagerDataSource==null){
			SysDataSource sysDataSource=getByAlias(dsAlias);
			if(sysDataSource!=null){
				driverManagerDataSource=dynamicDataSourceCache.putDataSource(dsAlias,sysDataSource.getDriverName(), sysDataSource.getUrl(),
						sysDataSource.getUserName(), sysDataSource.getPassword());
			}
		}
		return driverManagerDataSource;
	}
	
}