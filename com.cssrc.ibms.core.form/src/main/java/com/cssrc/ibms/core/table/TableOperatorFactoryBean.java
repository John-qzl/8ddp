package com.cssrc.ibms.core.table;


import org.springframework.beans.factory.FactoryBean;
import org.springframework.jdbc.core.JdbcTemplate;
import com.cssrc.ibms.core.db.constant.SqlTypeConst;
import com.cssrc.ibms.core.db.mybatis.dialect.Dialect;
import com.cssrc.ibms.core.form.intf.ITableOperator;
import com.cssrc.ibms.core.table.impl.Db2TableOperator;
import com.cssrc.ibms.core.table.impl.DmTableOperator;
import com.cssrc.ibms.core.table.impl.H2TableOperator;
import com.cssrc.ibms.core.table.impl.MysqlTableOperator;
import com.cssrc.ibms.core.table.impl.OracleTableOperator;
import com.cssrc.ibms.core.table.impl.SqlserverTableOperator;


/**
 * TableOperator factorybean，用户创建ITableOperator对象。
 * <pre>
 * 配置文件：app-beans.xml
 * &lt;bean id="tableOperator" class="com.ibms.core.table.TableOperatorFactoryBean">
 *			&lt;property name="dbType" value="${jdbc.dbType}"/>
 *			&lt;property name="jdbcTemplate" ref="jdbcTemplate"/>
 * &lt;/bean>
 * </pre>
 * @author zhulongchao
 *
 */
public class TableOperatorFactoryBean implements FactoryBean<ITableOperator> {
	
	
	
	private ITableOperator tableOperator;
	
	private String dbType=SqlTypeConst. MYSQL;
	
	private JdbcTemplate jdbcTemplate;
	
	private Dialect dialect;

	@Override
	public ITableOperator getObject() throws Exception {
		if(dbType.equals(SqlTypeConst.ORACLE)){
			tableOperator = new OracleTableOperator();
		}
		else if(dbType.equals(SqlTypeConst.SQLSERVER)){
			tableOperator = new SqlserverTableOperator();
		}
		else if(dbType.equals(SqlTypeConst.DB2)){
			tableOperator = new Db2TableOperator();
		}
		else if(dbType.equals(SqlTypeConst.MYSQL)){
			tableOperator = new MysqlTableOperator();
		}
		else if(dbType.equals(SqlTypeConst.H2)){
			tableOperator = new H2TableOperator();
		}
		else if(dbType.equals(SqlTypeConst.DM)){
			tableOperator = new DmTableOperator();
		}else{
			throw new Exception("没有设置合适的数据库类型");
		}
		
		tableOperator.setDbType(dbType);
		tableOperator.setJdbcTemplate(jdbcTemplate);
		tableOperator.setDialect(dialect);
		return tableOperator;
	}
	
	
	/**
	 * 设置数据库类型
	 * @param dbType
	 */
	public void setDbType(String dbType)
	{
		 this.dbType=dbType;
	}
	
	/**
	 * 设置jdbcTemplate
	 * @param jdbcTemplate
	 */
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate=jdbcTemplate;
	}
	
	
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
	 

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return ITableOperator.class;
	}

	@Override
	public boolean isSingleton() {
		
		return true;
	}

}
