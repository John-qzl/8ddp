package com.cssrc.ibms.core.table.impl;


import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.constant.SqlTypeConst;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.form.model.FormTableIndex;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.IDbView;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.colmap.SqlServerColumnMap;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.table.AbstractTableOperator;
/**
 * 元数据读取工厂。
 * 
 * @author zhulongchao
 * 
 */
public class TableMetaFactory {
	
	/**
	 * 根据数据源名称获取表元素据读取对象。 这里使用了简单工厂。
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	public static BaseTableMeta getMetaData(String dsName) throws Exception {
		ISysDataSource sysDataSource=null;
		if(dsName==null){
			sysDataSource=(ISysDataSource)AppUtil.getBean("sysdatasource");
		}
		else if(!dsName.equals(BpmConst.LOCAL_DATASOURCE)){ 
			ISysDataSourceDefService sysDataSourceDefService = (ISysDataSourceDefService) AppUtil
					.getBean(ISysDataSourceDefService.class);
			sysDataSource = sysDataSourceDefService.getSysDataSource(dsName);
		}else{
			sysDataSource=getLocalSysDataSource(dsName);
			}
		String dbType =SqlTypeConst.getDbType(sysDataSource.getUrl());
		BaseTableMeta meta = null;
		if (dbType.equals(SqlTypeConst.ORACLE)) {
			meta = new OracleTableMeta();
		} else if (dbType.equals(SqlTypeConst.MYSQL)) {
			meta = new MySqlTableMeta();
		}else if (dbType.equals(SqlTypeConst.SQLSERVER)){
			meta =new SqlServerTableMeta();
		}else if (dbType.equals(SqlTypeConst.DB2)){
			meta =new Db2TableMeta();
		}else if (dbType.equals(SqlTypeConst.H2)){
			meta =new H2TableMeta();
		}else if (dbType.equals(SqlTypeConst.DM)){
			meta =new DmTableMeta();
		}else {
			throw new Exception("未知的数据库类型");
		}
		meta.setDataSource(sysDataSource);
		return meta;
	}

	/**
	 * 根据数据源获取
	 * 
	 * @param dsName
	 * @return
	 * @throws Exception
	 */
	public static IDbView getDbView(String dsName) throws Exception {
		ISysDataSource sysDataSource = null;
		if(dsName.equals(BpmConst.LOCAL_DATASOURCE)){
			sysDataSource=getLocalSysDataSource(dsName);
		}else{
			ISysDataSourceService sysDataSourceService = (ISysDataSourceService) AppUtil
					.getBean(ISysDataSourceService.class);
			sysDataSource = sysDataSourceService.getByAlias(dsName);
		}
		
		String dbType =SqlTypeConst.getDbType(sysDataSource.getUrl());
		IDbView meta = null;
		if (dbType.equals(SqlTypeConst.ORACLE)) {
			meta = new OracleDbView();
		} else if (dbType.equals(SqlTypeConst.SQLSERVER)) {
			meta = new SqlserverDbView();
		} else if (dbType.equals(SqlTypeConst.MYSQL)){
			meta = new MysqlDbView();
		}else if (dbType.equals(SqlTypeConst.DB2)){
			meta = new Db2DbView();
		}else if (dbType.equals(SqlTypeConst.H2)){
			meta = new H2DbView();
		}else if (dbType.equals(SqlTypeConst.DM)){
			meta = new DmDbView();
		}else {
			throw new Exception("未知的数据库类型");
		}
		meta.setDataSource(sysDataSource);
		return meta;
	}
	public static ISysDataSource  getLocalSysDataSource(String dsName){
		return (ISysDataSource)AppUtil.getBean("sysdatasource");
	}

}
