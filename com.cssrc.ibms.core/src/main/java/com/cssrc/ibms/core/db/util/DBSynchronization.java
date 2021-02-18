package com.cssrc.ibms.core.db.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.core.constant.system.SysConfConstant;


public abstract class DBSynchronization {
	protected ISysDataSource sysDataSource;


	/** 
	* @Title: executeSql 
	* @Description: TODO(执行系统初始化脚本) 
	* @param @param sqlFiles
	* @param @param conn
	* @param @throws Exception     
	* @return void    返回类型 
	* @throws 
	*/
	public abstract void executeSql(List<String> sqlFiles,Connection conn)throws Exception;

	/** 
	* @Title: formatDb 
	* @Description: TODO(格式化当前数据库，统一表空间、用户名等) 
	* @param @param sqlFiles
	* @param @param conn
	* @param @throws Exception     
	* @return void    返回类型 
	* @throws 
	*/
	public abstract void formatDb(Connection conn)throws Exception;

	public void syncDB() throws Exception{
		Connection conn = this.getConn();
		try{
		    this.formatDb(conn);
			List<String> sqlFiles=getsqlFile();
			this.executeSql(sqlFiles,conn);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			// 5.关闭所有连接
			if(conn!=null){
				conn.close();
				conn = null;
			}
		}
		
	}

	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getConn() {
		try {
			// 1.创建数据库连接
			String className = sysDataSource.getDriverName();
			String userName = sysDataSource.getUserName();
			String password = sysDataSource.getPassword();
			String conName = sysDataSource.getUrl();
			if (sysDataSource.getDbType().equals(ISysDataSource.DBTYPE_MYSQL)) {
				//allowMultiQueries 可以执行多条脚本命令
				conName = conName + "&allowMultiQueries=true";
			}
			Class.forName(className);
			// 2.初始化conn
			Connection conn = DriverManager.getConnection(conName, userName, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取脚本文件全路径
	 * @return 返回文件列表
	 */
	public List<String> getsqlFile() {
		String dbType = sysDataSource.getDbType();
		// 3.创建存储过程
		String prePath = SysConfConstant.CONF_ROOT+ File.separator
				+ "updateSql" + File.separator + dbType + File.separator;
		// 先执行“结构”存储过程定义
		List<String> synStructList = new ArrayList<String>();
		// 再执行“数据”存储过程定义
		List<String> syndataList = new ArrayList<String>();
		// 其他脚本
		List<String> otherSqlList = new ArrayList<String>();

		//synStructList.add(prePath +dbType + "_synStruct.sql");// 添加数据库结构执行脚本
		//syndataList.add(prePath +dbType + "_synData.sql");// 添加数据执行脚本
		// 判断是结构还是数据sql文件，分别放到不同的list中
		File dir = new File(prePath);
		File[] files = dir.listFiles();
		for (File file : files) {
			String name = file.getName();
			if (name.contains(dbType + "_synStruct_")) {
				synStructList.add(prePath +name);
			} else if (name.contains(dbType + "_synData_")) {
				syndataList.add(prePath +name);
			} else if (name.contains(dbType + "_resetRoleFunction")||name.contains(dbType + "_getChildFunIdByCode")) {
				otherSqlList.add(prePath +name);
			}
		}
		// 合并到同一个list
		List<String> synList = new ArrayList<String>();
		synList.addAll(synStructList);
		synList.addAll(syndataList);
		synList.addAll(otherSqlList);
		return synList;
	}
	
	public ISysDataSource getSysDataSource() {
		return sysDataSource;
	}

	public void setSysDataSource(ISysDataSource sysDataSource) {
		this.sysDataSource = sysDataSource;
	}

	
}
