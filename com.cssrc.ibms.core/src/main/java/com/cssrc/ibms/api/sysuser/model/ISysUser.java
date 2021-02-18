package com.cssrc.ibms.api.sysuser.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

public abstract interface ISysUser {
	
	public static final String SEARCH_BY_ROL = "1";
	public static final String SEARCH_BY_ORG = "2";
	public static final String SEARCH_BY_POS = "3";
	public static final String SEARCH_BY_ONL = "4";

	public static ThreadLocal<Collection<GrantedAuthority>> roleList = new ThreadLocal();

	public static final Short UN_LOCKED = Short.valueOf((short) 0);

	public static final Short LOCKED = Short.valueOf((short) 1);

	public static final Long SYSTEM_USER = new Long(-1L); //系统管理员
	public static final Long CHECK_USER = new Long(-3L);  //安全审计员 
	public static final Long RIGHT_USER = new Long(-2L);  //权限管理员
	
	public static final Long IMPLEMENT_USER = new Long(-4L);  //系统实施员

	public static final Long SUPER_USER = new Long(1L);

	public static final Short DYNPWD_STATUS_BIND = Short.valueOf((short) 1);//激活
	public static final Short DYNPWD_STATUS_UNBIND = Short.valueOf((short) 0);//禁用
	public static final Short DYNPWD_STATUS_OUT = Short.valueOf((short) -1);//离职

	public static final String SECURITY_HEXIN= "12";//核心
	public static final String SECURITY_ZHONGYAO= "9";//重要
	public static final String SECURITY_YIBAN = "6";//一般
	public static final String SECURITY_FEIMI= "3";//非密
	public static final String HEXIN= "核心";
	public static final String ZHONGYAO= "重要";
	public static final String YIBAN = "一般";
	public static final String FEIMI= "非密";
	//人员密级map，value为密级对应的中文名
	public static final Map<String, String> SECURITY_USER_MAP=new HashMap<String, String>();
	//系统人员表
	public static final String USER_SECURITY_TABLE= "cwm_sys_user";
	//系统人员表的密级字段
	public static final String USER_SECURITY_FIELD= "SECURITY";
	
	public static final Short UNDEL = Short.valueOf((short) 0);
	public static final Short DELED = Short.valueOf((short) 1);

	public static final Short NOTNORMAL = Short.valueOf((short) 0);
	public static final Short NORMAL = Short.valueOf((short) 1);
	
	public static final String SKILL_TITLE_1= "zlgcs";
	public static final String SKILL_TITLE_2= "gcs";
	public static final String SKILL_TITLE_3 = "gjgcsf";
	public static final String SKILL_TITLE_4= "gjgcsz";
	public static final String SKILL_TITLE_5= "yjy";
	public static final String SKILL_TITLE_6= "zlyjy";
	
	
	public abstract Long getUserId();

	public abstract String getFullname();

	public abstract String getUsername();

	public abstract String getMobile();

	public abstract String getEmail();

	public abstract Short getDelFlag();

	public abstract Short getStatus();

	public abstract String getPassword();

	public abstract String getRoles();

	public abstract Long[] getOrgId();

	public abstract String getOrgName();

	public abstract Collection<GrantedAuthority> getAuthorities();

	public abstract Short getSex();

	public abstract String getPhone();

	public abstract String getAddress();

	public abstract Date getBirthDay();

	public abstract void setPassword(String string);

	public abstract Object getJob();
	
	public abstract String getSecurity();
	
	public abstract Date getUser_updateTime();

	public abstract Date getLastFailureTime();
	
	public abstract String getLoginFailures();
	
	public abstract void setLastFailureTime(Date lastFailureTime);
	
	public abstract void setLoginFailures(String loginFailures);
	
	public abstract Short getLockState();
	
	public abstract Date getLockTime();
	
	public abstract Date getPasswordSetTime();
	
	public abstract void setLockTime(Date lockTime);
	
	public abstract void setLockState(Short lockState);

	public abstract void setFullname(String string);

	public abstract void setUserId(Long systemuserid);
	
	public abstract String getSkilltitle();
	
	public abstract String getMajor();
}
