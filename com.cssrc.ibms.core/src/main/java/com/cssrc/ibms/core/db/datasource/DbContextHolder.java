package com.cssrc.ibms.core.db.datasource;

import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 
 * <p>Title:DbContextHolder</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:33:32
 */
public class DbContextHolder {
	private static final ThreadLocal<String> contextHolderAlias = new ThreadLocal();
	private static final ThreadLocal<String> contextHolderDbType = new ThreadLocal();

	public static void setDataSource(String dbAlias, String dbType) {
		contextHolderAlias.set(dbAlias);
		contextHolderDbType.set(dbType);
	}

	public static void setDefaultDataSource() {
		String dbType = AppConfigUtil.get("jdbc.dbType");
		contextHolderAlias.set("dataSource_Default");
		contextHolderDbType.set(dbType);
	}

	public static String getDataSource() {
		String str = (String) contextHolderAlias.get();
		return str;
	}

	public static String getDbType() {
		String str = (String) contextHolderDbType.get();
		return str;
	}

	public static void clearDataSource() {
		contextHolderAlias.remove();
		contextHolderDbType.remove();
	}

	public static void setDataSource(String alias) {
		if (StringUtil.isEmpty(alias))
			return;
		ISysDataSourceDefService sourceService = (ISysDataSourceDefService) AppUtil
				.getBean(ISysDataSourceDefService.class);
		ISysDataSourceDef sysDataSource = sourceService.getByAlias(alias);
		if (sysDataSource == null)
			return;
		setDataSource(alias, sysDataSource.getDbType());
	}
}
