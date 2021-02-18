package com.cssrc.ibms.api.rpc.intf;

import com.cssrc.ibms.core.util.appconf.AppConfigUtil;

/** 
 * 应用基本信息
* @ClassName: AppInfo 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author zxg 
* @date 2017年5月5日 下午1:37:45 
*  
*/
public class AppInfo
{
    //应用host ip
    private String apphost="";
    //应用host 端口
    private String appport="";
    //应用host contextpath
    private String appcontext="";
    //应用连接的数据库
    private String dbhost="";
    //应用 所属单位
    private String apporg="";
    //应用 系统名称
    private String appname="";
    
    public AppInfo(){
        this.apphost=AppConfigUtil.get("dubboproperties","app.url");
        this.appport=AppConfigUtil.get("dubboproperties","app.port");
        this.appcontext=AppConfigUtil.get("dubboproperties","app.context");
        this.dbhost=AppConfigUtil.get("jdbc.url");
        this.apporg=AppConfigUtil.get("dubboproperties","app.org");
        this.appname=AppConfigUtil.get("dubboproperties","app.name");
    }
    public String getApphost()
    {
        return apphost;
    }
    public void setApphost(String apphost)
    {
        this.apphost = apphost;
    }
    public String getAppport()
    {
        return appport;
    }
    public void setAppport(String appport)
    {
        this.appport = appport;
    }
    public String getAppcontext()
    {
        return appcontext;
    }
    public void setAppcontext(String appcontext)
    {
        this.appcontext = appcontext;
    }
    public String getDbhost()
    {
        return dbhost;
    }
    public void setDbhost(String dbhost)
    {
        this.dbhost = dbhost;
    }
    public String getApporg()
    {
        return apporg;
    }
    public void setApporg(String apporg)
    {
        this.apporg = apporg;
    }
    public String getAppname()
    {
        return appname;
    }
    public void setAppname(String appname)
    {
        this.appname = appname;
    }

    public String getAppurl()
    {
        return "http://"+apphost+":"+appport+"/"+appcontext;
    }
}
