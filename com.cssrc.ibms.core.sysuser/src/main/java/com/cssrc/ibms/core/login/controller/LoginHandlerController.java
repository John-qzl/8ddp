package com.cssrc.ibms.core.login.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 
 * @author Yangbo 2016-7-25
 * 退出或时间超过缓存设定的则重回login.jsp
 */
@Controller
public class LoginHandlerController {
	private String defaultLogin = "/login.jsp";

	@RequestMapping( { "/loginRedirect.do" })
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//移动端的login判断
/*		String loginAction = CookieUtil.getValueByName("loginAction", request);
		String redirectUrl = "";
		Map actionPageMap = (Map) AppUtil.getBean("actionPageMap");
		if ((StringUtil.isNotEmpty(loginAction))
				&& (actionPageMap.containsKey(loginAction))) {
			redirectUrl = (String) actionPageMap.get(loginAction);
			response.sendRedirect(request.getContextPath() + redirectUrl);
			return;
		}*/
		response.sendRedirect(request.getContextPath() + this.defaultLogin);
	}
}
