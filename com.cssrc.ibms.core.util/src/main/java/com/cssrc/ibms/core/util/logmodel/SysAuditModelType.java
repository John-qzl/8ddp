package com.cssrc.ibms.core.util.logmodel;

/**
 * 对象功能:系统日志归属模块 Model对象 开发人员:zhulongchao 创建时间:2014-11-24
 */
public class SysAuditModelType {
	public static final String NULL="末指定";
	//系统管理
	public static final String SYSTEM_SETTING="系统管理";
	public static final String USER_MANAGEMENT="系统管理-用户管理"; 
	public static final String ORG_MANAGEMENT="系统管理-组织管理"; 
	public static final String RESOURCES_MANAGEMENT="系统管理-功能点管理"; 
	public static final String JOB_MANAGEMENT="系统管理-职务管理"; 
	public static final String NEWS_ANNOUNCE_MANAGEMENT="系统管理-门户管理-新闻公告管理"; 
	public static final String NEWS_COMMENT_MANAGEMENT="系统管理-门户管理-新闻评论管理"; 
	public static final String DIMENSION_MANAGEMENT="系统管理-维度管理"; 
	public static final String PARAMETER_MANAGEMENT="系统管理-参数管理"; 
	public static final String ACCOUNTSTRATEGY_MANAGEMENT="系统管理-账户策略"; 
	public static final String ROLE_MANAGEMENT="系统管理-角色管理";
	public static final String LOG_SYS_MANAGEMENT="系统管理-日志管理-系统日志"; 
	public static final String LOG_SWITCH_MANAGEMENT="系统管理-日志管理-日志开关"; 
	public static final String LOG_ERROR_MANAGEMENT="系统管理-日志管理-错误日志"; 
	public static final String RECTYPE_PROJECT_MANAGEMENT="系统管理-项目权限管理-项目功能点类别"; 
	public static final String ROLETYPE_PROJECT_MANAGEMENT="系统管理-项目权限管理-项目角色类别"; 
	//辅助开发
	public static final String MESSAGEMONITOR_ASSIST="辅助开发-系统监控-JMS消息监控"; 
	public static final String DATASOURCE_ASSIST="辅助开发-系统监控-数据源信息监控"; 
	//流程管理
	public static final String FLOWUSER_MANAGEMENT="流程管理-流程人员分配";
	public static final String FLOWLOG_MANAGEMENT="流程管理-流程操作日志";
	//日历管理
	public static final String WORKTIME_CALENDARASSIGN="系统管理-日历管理-工作日历分配";
	public static final String WORKTIME_CALENDSETTING="系统管理-日历管理-工作日历设置";
	public static final String WORKTIME_OVERTIME="系统管理-日历管理-加班请假管理";
	public static final String WORKTIME_SYSCALENDAR="系统管理-日历管理-系统日历";
	public static final String WORKTIME_VACATION="系统管理-日历管理-法定假期设置";
	public static final String WORKTIME_WORKTIME="系统管理-日历管理-班次设置管理";
	public static final String WORKTIME_WORKTIMESETTING="系统管理-日历管理-日历时间设置管理";
	
	public static final String DATA_SEARCH="数据检索";
	public static final String PROCESS_AUXILIARY="流程辅助";
	public static final String DATA_DOWNLOAD="数据下载";
	public static final String DATA_COMPARE="数据对比";
	public static final String DATA_UPLOAD="数据上传"; 
	public static final String LOGIN_MANAGEMENT="登录管理";
	public static final String DATA_BROWSE="数据浏览";
	public static final String META_MANAGEMENT="元数据管理";
	public static final String DATA_STATISTICS="数据统计";
	public static final String GANTT_STATISTICS="甘特图统计";
	public static final String MEDIA_UPLOAD="多媒体上传";
	public static final String MEDIA_CHECK="多媒体审批"; 
	public static final String FORM_MANAGEMENT="表单管理"; 
	public static final String BUSINESS_MANAGEMENT="业务管理"; 
	public static final String FLOW_MANAGEMENT="流程管理";
	public static final String REPORT_MANAGEMENT="报表管理";
	public static final String FILE_MANAGEMENT="文件管理";
	
//	private final String type;
//	private SysAuditModelType=String type){
//		this.type=type;
//	}
//	
//	@Override
//	public String toString=){
//		return type;
//	}
}