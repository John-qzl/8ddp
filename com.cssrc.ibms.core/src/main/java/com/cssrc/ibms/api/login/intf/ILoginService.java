package com.cssrc.ibms.api.login.intf;

import javax.servlet.http.HttpServletRequest;

import com.cssrc.ibms.api.login.model.ILoginLog;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

public interface ILoginService {

	boolean checkLicesne(String licensePath, String clientType,
			String IPAddress, StringBuffer errorInfo);

	ILoginLog checkLoginUser(String username, String password,
			ISysUser sysUser, HttpServletRequest request);

}
