package com.cssrc.ibms.core.db.mybatis.dialect;

import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 
 * <p>Title:DialectUtil</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:40:34
 */
public class DialectUtil {
	public static Dialect getDialect(String dbType) throws Exception {
		Dialect dialect;
		if (dbType.equals("oracle")) {
			dialect = new OracleDialect();
		} else {
			if (dbType.equals("mssql")) {
				dialect = new SQLServer2005Dialect();
			} else {
				if (dbType.equals("db2")) {
					dialect = new DB2Dialect();
				} else {
					if (dbType.equals("mysql")) {
						dialect = new MySQLDialect();
					} else {
						if (dbType.equals("h2")) {
							dialect = new H2Dialect();
						} else {
							if (dbType.equals("dm"))
								dialect = new DmDialect();
							else
								throw new Exception("没有设置合适的数据库类型");
						}
					}
				}
			}
		}
		return dialect;
	}

	public static Dialect getCurrentDialect() throws Exception {
		return getDialect(DbContextHolder.getDbType());
	}

	public static Dialect getDialect(ISysDataSource sysDataSource)
			throws Exception {
		return getDialect(sysDataSource.getDbType());
	}

	public static Dialect getDialectByDataSourceAlias(String alias)
			throws Exception {
		ISysDataSourceService sysDataSourceService = (ISysDataSourceService) AppUtil
				.getBean(ISysDataSourceService.class);
		ISysDataSource sysDataSource = sysDataSourceService.getByAlias(alias);
		if (sysDataSource == null) {
			return getDialect(AppConfigUtil.get("jdbc.dbType"));
		}
		return getDialect(sysDataSource);
	}
}
