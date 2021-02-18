package com.cssrc.ibms.system.model;

import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.encrypt.EncryptUtil;

/**
 * 对象功能:系统数据源管理 Model对象 开发人员:zhulongchao
 */
public class SysDataSource extends BaseModel implements ISysDataSource
{
    
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
    
    /***/
    private static final long serialVersionUID = -3760293574264972800L;
    
    // 主键
    private Long id;
    
    // 数据源名称
    private String name;
    
    // 别名
    private String alias;
    
    // 驱动名称
    private String driverName;
    
    // 数据库URL
    private String url;
    
    // 用户名
    private String userName;
    
    // 密码
    private String password;
    
    private String encPassword;
    
    // 数据库类型
    private String dbType = "";
    
    // 表空间
    //private String tablespace = "";
    private String settingJson;
	
	private Boolean initOnStart;
	
	private Boolean enabled;
	
	private String classPath;
	
	private String initMethod;
	
	private String closeMethod;
	
	private Long runId = Long.valueOf(0L);
    
    public String getDbType()
    {
        return dbType;
    }
    
    public void setDbType(String dbType)
    {
        this.dbType = dbType;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    /**
     * 返回 主键
     * 
     * @return
     */
    public Long getId()
    {
        return id;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * 返回 数据源名称
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }
    
    public void setAlias(String alias)
    {
        this.alias = alias;
    }
    
    /**
     * 返回 别名
     * 
     * @return
     */
    public String getAlias()
    {
        return alias;
    }
    
    public void setDriverName(String driverName)
    {
        this.driverName = driverName;
    }
    
    /**
     * 返回 驱动名称
     * 
     * @return
     */
    public String getDriverName()
    {
        return driverName;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    /**
     * 返回 数据库URL
     * 
     * @return
     */
    public String getUrl()
    {
        return url;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /**
     * 返回 用户名
     * 
     * @return
     */
    public String getUserName()
    {
        return userName;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * 返回 密码
     * 
     * @return
     */
    public String getPassword()
    {
        return password;
    }
    
    public String getEncPassword()
    {
        return encPassword;
    }
    
    public void setEncPassword(String pwd)
        throws Exception
    {
        pwd = EncryptUtil.decrypt(pwd);
        this.encPassword = pwd;
    }
    
/*    public String getTablespace()
    {
        return tablespace;
    }
    
    public void setTablespace(String tablespace)
    {
        this.tablespace = tablespace;
    }*/
    public String getSettingJson() {
		return settingJson;
	}
	public void setSettingJson(String settingJson) {
		this.settingJson = settingJson;
	}
	public Boolean getInitOnStart() {
		return initOnStart;
	}
	public void setInitOnStart(Boolean initOnStart) {
		this.initOnStart = initOnStart;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	public String getInitMethod() {
		return initMethod;
	}
	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}
	public String getCloseMethod() {
		return closeMethod;
	}
	public void setCloseMethod(String closeMethod) {
		this.closeMethod = closeMethod;
	}
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
    /**
     * 获取表空间
     * @return
     */
    public String getTableSpace()
    {
        if (this.dbType.equals(DBTYPE_MYSQL))
        {
            String appName = "";
            String[] pro = this.url.split(":");
            appName = pro[pro.length - 1].split("/")[1];
            if (appName.indexOf("?") > 0)
            {
                appName = appName.substring(0, appName.indexOf("?"));
            }
            return appName;
        }
        else if (this.dbType.equals(DBTYPE_ORACLE))
        {
            return this.userName;
        }
        else
        {
            //暂时不支持其他数据库
            return null;
        }
    }
    
    /**
     * 获取系统名称
     * @return
     */
    public String getAppName()
    {
        return getTableSpace();
    }
}