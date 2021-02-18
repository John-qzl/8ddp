package org.displaytag.localization;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;

/**
 * Displaytag的设置的语言环境
 * 
 * @author zhulongchao
 * 
 */
public class DisplaytagLocaleResolver implements LocaleResolver {

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		return UserContextUtil.getLocale();
	}

}
