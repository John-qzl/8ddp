package com.cssrc.ibms.core.db.mybatis.model;

import org.springframework.beans.factory.FactoryBean;

import com.cssrc.ibms.core.db.constant.SqlTypeConst;
import com.cssrc.ibms.core.db.mybatis.dialect.DB2Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.DmDialect;
import com.cssrc.ibms.core.db.mybatis.dialect.H2Dialect;
import com.cssrc.ibms.core.db.mybatis.dialect.MySQLDialect;
import com.cssrc.ibms.core.db.mybatis.dialect.OracleDialect;
import com.cssrc.ibms.core.db.mybatis.dialect.SQLServer2005Dialect;

/**
 * 方言FactoryBean，通过统一的接口取得不同数据库的分页Sql语句。
 * 
 * <pre>
 * 在app-beans.xml中的配置。
 * 
 * &lt;bean id="dialect" class="com.ibms.core.customertable.DialectFactoryBean">
 * 		&lt;property name="dbType" value="${jdbc.dbType}"/>
 * &lt;/bean>
 * </pre>
 * 
 * @author zhulongchao
 * 
 */
public class DialectFactoryBean implements FactoryBean<Dialect> {

	private Dialect dialect;

	/**
	 * 设置数据库类型
	 * 
	 * @param dbType
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	private String dbType = SqlTypeConst.MYSQL;

	@Override
	public Dialect getObject() throws Exception {
		dialect = getDialect(this.dbType);
		return dialect;
	}

	
	/**
	 * 获得数据源
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	public static Dialect getDialect(String dbType) throws Exception {
		Dialect dialect = null;
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
	
	
	/**
	 * 根据驱动类获得数据源
	 * @param dbType
	 * @return
	 * @throws Exception
	 */
	public static Dialect getDialectByDriverClassName(String driverClassName) throws Exception {
		Dialect dialect = null;
		if (driverClassName.contains(SqlTypeConst.ORACLE)) {//oracle.jdbc.OracleDriver
			dialect = new OracleDialect();
		} else if (driverClassName.contains("sqlserver")) {//com.microsoft.sqlserver.jdbc.SQLServerDriver
			dialect = new SQLServer2005Dialect();
		} else if (driverClassName.contains(SqlTypeConst.DB2)) {//com.ibm.db2.jcc.DB2Driver
			dialect = new DB2Dialect();
		} else if (driverClassName.contains(SqlTypeConst.MYSQL)) {//com.mysql.jdbc.Driver
			dialect = new MySQLDialect();
		} else if (driverClassName.contains(SqlTypeConst.H2)) {//org.h2.Driver
			dialect = new H2Dialect();
		} else if (driverClassName.contains(SqlTypeConst.DM)) {//dm.jdbc.driver.DmDriver
			dialect = new DmDialect();
		} else {
			throw new Exception("没有设置合适的数据库类型");
		}
		return dialect;
	}

	@Override
	public Class<?> getObjectType() {
		return Dialect.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
