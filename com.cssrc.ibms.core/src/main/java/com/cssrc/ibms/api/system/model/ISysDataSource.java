package com.cssrc.ibms.api.system.model;

public interface ISysDataSource {

	/**
	 * 数据库类型=oracle
	 */
	public final static String DBTYPE_ORACLE = "oracle";
	/**
	 * 数据库类型=sql2005
	 */
	public final static String DBTYPE_SQL2005 = "sql2005";
	/**
	 * 数据库类型=mysql
	 */
	public final static String DBTYPE_MYSQL = "mysql";
	/**
	 * 数据库类型=db2
	 */
	public final static String DBTYPE_DB2 = "db2";
	/**
	 * 数据库类型=h2
	 */
	public final static String DBTYPE_H2 = "h2";
	/**
	 * 数据库类型=达梦
	 */
	public final static String DBTYPE_DM = "dm";

	/**
	 * 代表本地数据源，即代表连接本库
	 */
	public final static String DS_LOCAL = "LOCAL";
	
	public abstract String getDbType();

	public abstract void setDbType(String dbType);

	public abstract void setId(Long id);

//	String getDbType();
//
//	String getAlias();
//
//	String getUrl();
//
//	String getDriverName();
//
//	String getUserName();
//
//	String getPassword();
//
//	String getTablespace();
	
	/**
	 * 返回 主键
	 * @return
	 */
	public abstract Long getId();

	public abstract void setName(String name);

	/**
	 * 返回 数据源名称
	 * @return
	 */
	public abstract String getName();

	public abstract void setAlias(String alias);

	/**
	 * 返回 别名
	 * @return
	 */
	public abstract String getAlias();

	public abstract void setDriverName(String driverName);

	/**
	 * 返回 驱动名称
	 * @return
	 */
	public abstract String getDriverName();

	public abstract void setUrl(String url);

	/**
	 * 返回 数据库URL
	 * @return
	 */
	public abstract String getUrl();

	public abstract void setUserName(String userName);

	/**
	 * 返回 用户名
	 * @return
	 */
	public abstract String getUserName();

	public abstract void setPassword(String password);

	/**
	 * 返回 密码
	 * @return
	 */
	public abstract String getPassword();

	public abstract String getEncPassword();

	public abstract void setEncPassword(String pwd) throws Exception;

	//public abstract String getTablespace();

	//public abstract void setTablespace(String tablespace);

}