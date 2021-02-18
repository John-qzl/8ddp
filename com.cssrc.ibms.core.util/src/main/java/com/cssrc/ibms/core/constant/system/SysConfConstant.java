package com.cssrc.ibms.core.constant.system;

import java.util.HashMap;
import java.util.Map;
public class SysConfConstant {
	public static String WELCOME_MESSAGE = "";
	// 系统参数

	// WEB界面的标题
	public static String SYSTEM_TITLE = "";
	// WEB界面的标题LOGO
	public static String SYSTEM_TITLE_LOGO = "";
	//公司名称
	public static String COMPANY_NAME="";
	// 文件上传目录
	public static String UploadFileFolder = "";
	//RTX消息地址
	public static String RTX_NOTIFY_LINK = "";
	//RTX开关
	public static int  RTX_NOTIFY_ON_OFF = 0;
	//RTX接收人账户
	public static int  RTX_RECEIVE_TYPE = 0;
	//系统参数
	public static Map<String,Object>  SYS_PARAM_MAP = new HashMap<String,Object>();

	//配置文件根目录
	public static String CONF_ROOT=""; 
	public static String MVC_VIEW=""; 
	public static String FTL_ROOT=""; 
	
	//页面ui类型判断 0表示新界面，1表示新界面
	public static int SHOW_TYPE = 1;
	
}