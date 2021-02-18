package com.cssrc.ibms.api.activity.util;

import java.lang.reflect.Method;

import com.cssrc.ibms.core.util.appconf.AppUtil;

public class RuleUtil {
	/***
	 * 验证规则
	 * @author Yangbo 2016-7-22
	 * @param handler
	 * @param parameterTypes
	 * @return
	 */
	public static int isHandlerValidNoCmd(String handler, Class<?>[] parameterTypes)
	{
		if (handler.indexOf(".") == -1)
			return -1;
		String[] aryHandler = handler.split("[.]");
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		Object serviceBean = null;
		try {
			serviceBean = AppUtil.getBean(beanId);
		} catch (Exception ex) {
			return -2;
		}
		if (serviceBean == null)
			return -2;
		try
		{
			Method invokeMethod = serviceBean.getClass().getMethod(method, parameterTypes);
			if (invokeMethod != null) {
				return 0;
			}
			return -3;
		}
		catch (NoSuchMethodException e) {
			return -3; } catch (Exception e) {
			}
			return -4;
	}
}
