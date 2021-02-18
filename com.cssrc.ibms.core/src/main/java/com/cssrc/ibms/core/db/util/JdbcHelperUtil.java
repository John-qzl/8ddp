package com.cssrc.ibms.core.db.util;

import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.constant.SqlTypeConst;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.db.mybatis.dialect.DB2Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.DmDialect;
import com.cssrc.ibms.core.db.mybatis.dialect.H2Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.MySQLDialect;
import com.cssrc.ibms.core.db.mybatis.dialect.OracleDialect;
import com.cssrc.ibms.core.db.mybatis.dialect.SQLServer2005Dialect;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.util.appconf.AppUtil;


public class JdbcHelperUtil {
	/**
	 * 根据数据源获取JdbcHelper。
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static JdbcHelper getJdbcHelper(String dsName) throws Exception {
		ISysDataSource dataSource=null;
		if(dsName==null){
			dataSource=(ISysDataSource)AppUtil.getBean("sysdatasource");
		}else if(!dsName.equals(BpmConst.LOCAL_DATASOURCE)){ 
			ISysDataSourceDefService sysDataSourceDefService = (ISysDataSourceDefService) AppUtil
					.getBean(ISysDataSourceDefService.class);
			dataSource = sysDataSourceDefService.getSysDataSource(dsName);
		}else{
			dataSource=getLocalSysDataSource(dsName);
		}
		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.init(dsName, dataSource.getDriverName(), dataSource
				.getUrl(), dataSource.getUserName(), dataSource
				.getPassword());
		DbContextHolder.setDataSource(dsName, dataSource.getDbType());
		jdbcHelper.setCurrentDb(dsName);
		Dialect dialect = getDialect(dataSource.getDbType());
		jdbcHelper.setDialect(dialect);
		return jdbcHelper;
	}
	
	public static ISysDataSource  getLocalSysDataSource(String dsName){
		return (ISysDataSource)AppUtil.getBean("sysdatasource");
	}
	
	/**
	 * 获取方言。
	 * 
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	private static Dialect getDialect(String dbType) throws Exception {
		Dialect dialect = new OracleDialect();
		if (dbType.equals(SqlTypeConst.ORACLE)) {
			dialect = new OracleDialect();
		} else if (dbType.equals(SqlTypeConst.SQLSERVER)) {
			dialect = new SQLServer2005Dialect();
		} else if (dbType.equals(SqlTypeConst.DB2)) {
			dialect = new DB2Dialect();
		} else if (dbType.equals(SqlTypeConst.MYSQL)) {
			dialect = new MySQLDialect();
		} else if (dbType.equals(SqlTypeConst.H2)) {
			dialect = new H2Dialect();
		} else if (dbType.equals(SqlTypeConst.DM)) {
			dialect = new DmDialect();
		} else {
			throw new Exception("没有设置合适的数据库类型");
		}
		return dialect;

	}
	
	
	
}
