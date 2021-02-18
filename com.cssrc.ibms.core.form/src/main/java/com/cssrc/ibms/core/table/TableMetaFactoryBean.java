package com.cssrc.ibms.core.table;


import org.springframework.beans.factory.FactoryBean;

import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.core.db.constant.SqlTypeConst;
import com.cssrc.ibms.core.table.impl.Db2TableMeta;
import com.cssrc.ibms.core.table.impl.DmTableMeta;
import com.cssrc.ibms.core.table.impl.H2TableMeta;
import com.cssrc.ibms.core.table.impl.MySqlTableMeta;
import com.cssrc.ibms.core.table.impl.OracleTableMeta;
import com.cssrc.ibms.core.table.impl.SqlServerTableMeta;

/**
 * TableOperator factorybean，用户创建ITableOperator对象。
 * <pre>
 * 配置文件：app-beans.xml
 * &lt;bean id="tableOperator" class="com.ibms.core.customertable.TableOperatorFactoryBean">
 *			&lt;property name="dbType" value="${jdbc.dbType}"/>
 *			&lt;property name="jdbcTemplate" ref="jdbcTemplate"/>
 * &lt;/bean>
 * </pre>
 * @author zhulongchao
 *
 */
public class TableMetaFactoryBean implements FactoryBean<BaseTableMeta> {
	

	
	private BaseTableMeta tableMeta;
	
	private String dbType=SqlTypeConst.MYSQL;

	private ISysDataSource sysDataSource;

	@Override
	public BaseTableMeta getObject() throws Exception {
		dbType=sysDataSource.getDbType();
		if(dbType.equals(SqlTypeConst.ORACLE)){
			tableMeta = new OracleTableMeta();
		}
		else if(dbType.equals(SqlTypeConst.SQLSERVER)){
			tableMeta = new SqlServerTableMeta();
		}
		else if(dbType.equals(SqlTypeConst.MYSQL)){
			tableMeta = new MySqlTableMeta();
		}else if(dbType.equals(SqlTypeConst.DB2)){
			tableMeta = new Db2TableMeta();
		}else if(dbType.equals(SqlTypeConst.H2)){
			tableMeta = new H2TableMeta();
		}else if(dbType.equals(SqlTypeConst.DM)){
			tableMeta = new DmTableMeta();
		}else{
			throw new Exception("没有设置合适的数据库类型");
		}
	//	sysDataSource.setPassword(sysDataSource.getEncPassword());
		tableMeta.setDataSource(sysDataSource);
		return tableMeta;
	}
	
	
	/**
	 * 设置数据库类型
	 * @param dbType
	 */
	public void setDbType(String dbType)
	{
		 this.dbType=dbType;
	}
	
	
	public void setSysDataSource(ISysDataSource sysDataSource) {
		this.sysDataSource = sysDataSource;
	}

	
	 

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return BaseTableMeta.class;
	}

	@Override
	public boolean isSingleton() {
		
		return true;
	}

}
