package com.cssrc.ibms.api.core.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import org.springframework.context.support.AbstractMessageSource;
import com.cssrc.ibms.api.core.intf.IContext;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class ContextUtil {
	public static final String CurrentOrg = "CurrentOrg_";
	public static final String CurrentCompany = "CurrentCompany_";
	public static final String CurrentPos = "CurrentPos_";
	private static IContext context_;

	public static String getPositionKey(Long userId)
	{
		String posKey = "CurrentPos_" + userId;
		return posKey;
	}

	public static String getOrgKey(Long userId) {
		String orgKey = "CurrentOrg_" + userId;
		return orgKey;
	}

	public static String getCompanyKey(Long userId) {
		String orgKey = "CurrentCompany_" + userId;
		return orgKey;
	}

	public static synchronized IContext getContext()
	{
		if (context_ == null) {
			context_ = (IContext)AppUtil.getBean(IContext.class);
		}
		return context_;
	}

	public static String getCurrentUserSkin(HttpServletRequest request)
	{
		IContext context = getContext();
		return context.getCurrentUserSkin(request);
	}

	public static Locale getLocale()
	{
		IContext context = getContext();
		return context.getLocale();
	}

	public static void setLocale(Locale locale)
	{
		IContext context = getContext();
		context.setLocale(locale);
	}

	 /**
	  * 通过资源的key获得对于key语言
	  * @param code 资源的key
	  * @return
	  */
	 public static String getMessages(String code){
		 return getMessages(code, null);
	 }
	 /**
	  * 通过资源的key获得对于key语言
	  * @param code 资源的key
	  * @param args
	  * @param locale
	  * @return
	  */
	 public static String getMessages(String code,Object[] args,Locale locale){
		 AbstractMessageSource messages = (AbstractMessageSource) AppUtil.getBean("messageSource");
		 if(locale == null)
			 locale = getLocale();
		 return messages.getMessage(code,args,locale);
	 }

	 /**
	  * 	通过资源的key获得对于key语言
	  * @param code 资源的key
	  * @param args
	  * @return
	  */
	 public static String getMessages(String code,Object[] args){
		 return getMessages(code, args, getLocale());
	 }

}
