package com.cssrc.ibms.api.core.intf;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
/**
 * 上下文方法接口，实现从CurrentContext.java这里写
 * @author Yangbo
 * 2016-7-19
 *
 */
public abstract interface IContext {
    public abstract Locale getLocale();

    public abstract void setLocale(Locale paramLocale);

	public abstract String getCurrentUserSkin(HttpServletRequest request);
}
