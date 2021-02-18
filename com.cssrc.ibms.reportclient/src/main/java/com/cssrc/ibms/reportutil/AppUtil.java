package com.cssrc.ibms.reportutil;


/**
 * 2016-7-19
 * 
 * @author Yangbo
 *
 */
public class AppUtil {
	/**
	 * (D:\apache-tomcat-666\webapps\ibms)\WEB-INF\
	 *
	 * @author YangBo @date 2016年10月20日下午8:07:51
	 * @return
	 */
	public static String getRootPath() {
		
		return AppUtil.class.getClassLoader().getResource("").getPath();
	}
}
