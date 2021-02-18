package com.cssrc.ibms.api.system.intf;

import java.util.List;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;

/**
 * ISysDataSourceDefService
 * @author liubo
 * @date 2017年4月14日
 */
public interface ISysDataSourceDefService{

	/**
	 * 测试数据源是否可以连接
	 * 
	 * @param sysDataSourceDef
	 * @return
	 */
	public abstract boolean checkConnection(ISysDataSourceDef sysDataSourceDef);

	/**
	 * 通过数据源别名获取数据源信息
	 * @param alias
	 * @return
	 */	
	public abstract ISysDataSource getSysDataSource(String alias);
	
	/**
	 * 根据别名获取数据源
	 * @param alias
	 * @return
	 */
	public abstract ISysDataSourceDef getByAlias(String alias);

	/**
	 * 获取所有数据源
	 * @return
	 */
	public List<? extends ISysDataSourceDef> getAllAndDefault();

}