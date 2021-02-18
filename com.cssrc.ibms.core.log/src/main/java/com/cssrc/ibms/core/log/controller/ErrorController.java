package com.cssrc.ibms.core.log.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.log.model.SysErrorLog;
import com.cssrc.ibms.core.log.service.SysErrorLogService;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/error.do" })
public class ErrorController {

	@Resource
	private SysErrorLogService sysErrorLogService;

	@RequestMapping( { "*" })
	public ModelAndView error(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Exception exception = (Exception) request
				.getAttribute("javax.servlet.error.exception");

		String errorurl = getErrorUrl(request);

		String ip = RequestUtil.getIpAddr(request);

		ISysUser sysUser = (ISysUser) UserContextUtil.getCurrentUser();

		String account = sysUser.getUsername();
		
		String name = sysUser.getFullname();
		
		String error = ExceptionUtil.getExceptionMessage(exception);

		Long id = Long.valueOf(UniqueIdUtil.genId());
		SysErrorLog sysErrorLog = new SysErrorLog();
		sysErrorLog.setId(id);
		sysErrorLog.setHashcode(String.valueOf(error.hashCode()));
		sysErrorLog.setName(name);
		sysErrorLog.setAccount(account);
		sysErrorLog.setIp(ip);
		sysErrorLog.setError(error);
		sysErrorLog.setErrorurl(StringUtils.substring(errorurl, 0, 1000));
		sysErrorLog.setErrordate(new Date());
		this.sysErrorLogService.add(sysErrorLog);

		return new ModelAndView("error.jsp").addObject("errorCode", id);
	}

	private String getErrorUrl(HttpServletRequest request) {
		String url = request.getAttribute("javax.servlet.error.request_uri")
				.toString();
		StringBuffer urlThisPage = new StringBuffer();
		urlThisPage.append(url);
		Enumeration e = request.getParameterNames();
		String para = "";
		String values = "";
		urlThisPage.append("?");
		while (e.hasMoreElements()) {
			para = (String) e.nextElement();
			values = request.getParameter(para);
			urlThisPage.append(para);
			urlThisPage.append("=");
			urlThisPage.append(values);
			urlThisPage.append("&");
		}
		return urlThisPage.substring(0, urlThisPage.length() - 1);
	}
}
