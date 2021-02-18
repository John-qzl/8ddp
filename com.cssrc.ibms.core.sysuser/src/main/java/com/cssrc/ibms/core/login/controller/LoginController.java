package com.cssrc.ibms.core.login.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.login.model.ILoginLog;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.login.service.LoginLogService;
import com.cssrc.ibms.core.login.service.LoginService;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.CookieUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping("/login.do")
@Action(ownermodel = SysAuditModelType.LOGIN_MANAGEMENT)
public class LoginController extends BaseController{
	@Resource
	private ISysUserService sysUserService;
	@Resource(name = "authenticationManager")
	private AuthenticationManager authenticationManager = null;
	@Resource
	private LoginService loginService;
	@Resource
	private LoginLogService loginLogService;
	@Resource
	private ISysParameterService sysParameterService;
	
	
	private String key = "ibmsPrivateKey";
	public static final String TRY_MAX_COUNT = "tryMaxCount";
	public static final int maxTryCount = 5;
	private String succeedUrl = "/oa/console/main.do";
	private String doorUrl = "/oa/login/door/main.do";

	@RequestMapping
	@Action(description = "用户登录", execOrder = ActionExecOrder.AFTER, detail = "登录系统",exectype = SysAuditExecType.LOGIN_TYPE)
	public void login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "username") String username,
			@RequestParam(value = "password") String password)
			throws IOException {
		//获取系统参数
		List<? extends ISysParameter> isShowDoor = sysParameterService.getByParamName("IS_SHOW_DOOR");
		String isDoor = isShowDoor.get(0).getParamvalue();
		//设置操作结果，默认为操作失败
		Short result = 0;
		String succeedUrl = this.succeedUrl;
		String doorUrl = this.doorUrl;
		SecurityContextHolder.clearContext();
		ResultMessage resultMessage = null;
		
		//License校验
		String ipAddress = request.getRemoteAddr();
		StringBuffer errorInfo = new StringBuffer();
		String licenseFilePath = AppUtil.getAppAbsolutePath() + "WEB-INF"
				+ File.separator;
		boolean success = loginService.checkLicesne(licenseFilePath, "5",
				ipAddress, errorInfo);
		//记录登陆状况
		ILoginLog loginLog=null;
		//失败则不能启动系统
		if (success == false) {
			resultMessage = new ResultMessage(ResultMessage.Fail, "License校验失败");
			writeResultMessage(response.getWriter(), resultMessage);
		} else {
			// 从表中获取该用户信息
			ISysUser sysUser = this.sysUserService.getByUsername(username);
			try {
				loginLog = loginService.checkLoginUser(username, password, sysUser,request);
				if(loginLog.getStatus()!=ILoginLog.SUCCESS.shortValue()){
					request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", loginLog.getDesc());
					throw new AccessDeniedException(loginLog.getDesc());
				}
				result = 1;
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
				authRequest.setDetails(new WebAuthenticationDetails(request));
				SecurityContext securityContext = SecurityContextHolder.getContext();
				
				//调用app-security.xml方法中authenticationManager user-service-ref="sysUserDao"方法获取user
				Authentication auth = authenticationManager.authenticate(authRequest);
				securityContext.setAuthentication(auth);
				String rememberMe = request.getParameter("_spring_security_remember_me");
				if ((rememberMe != null) && (rememberMe.equals("1"))) {
					long tokenValiditySeconds = 1209600L;
					long tokenExpiryTime = System.currentTimeMillis()+ tokenValiditySeconds * 1000L;

					String signatureValue = DigestUtils.md5Hex(username + ":"+ tokenExpiryTime + ":" + sysUser.getPassword()+ ":" + this.key);
					String tokenValue = username + ":" + tokenExpiryTime + ":"+ signatureValue;
					String tokenValueBase64 = new String(Base64.encodeBase64(tokenValue.getBytes()));
					CookieUtil.delCookie("username", response);

					CookieUtil.delCookie("SPRING_SECURITY_REMEMBER_ME_COOKIE", response);
					CookieUtil.addCookie(tokenExpiryTime, tokenValueBase64,request,response);
					CookieUtil.addCookie("username", URLEncoder.encode(sysUser.getUsername(),"utf-8"), 
					   (int)tokenValiditySeconds, response);
					}else{//清除缓存iCache中存放的org,position,company信息
						 UserContextUtil.removeCurrentOrg();
						 UserContextUtil.cleanCurUser();//清除 CurrentContext中的静态static变量curUser值
						 
						 CookieUtil.delCookie("username", response);
						 CookieUtil.delCookie("SPRING_SECURITY_REMEMBER_ME_COOKIE",response);
					}
				RequestUtil.getHttpServletRequest().getSession().setAttribute("userInfo", sysUser);
				
			} catch (AccessDeniedException localAccessDeniedException) 
			{
				localAccessDeniedException.printStackTrace();
			} finally {
				this.loginLogService.save(loginLog);
				//by liubo 根据参数判断是门户系统还是普通系统
				if(StringUtil.isNotEmpty(isDoor) && isDoor.equals("1")){
					response.sendRedirect(request.getContextPath() + doorUrl);
				}else{
					response.sendRedirect(request.getContextPath() + succeedUrl);
				}
			}
		}
		try {
			LogThreadLocalHolder.putParamerter("username", username);
			LogThreadLocalHolder.putParamerter("resultMsg", loginLog.getDesc());
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	
	protected Cookie makeValidCookie(long expiryTime, String tokenValueBase64,
			HttpServletRequest request) {
		Cookie cookie = new Cookie("SPRING_SECURITY_REMEMBER_ME_COOKIE",
				tokenValueBase64);
		cookie.setMaxAge(157680000);
		cookie.setPath(org.springframework.util.StringUtils.hasLength(request
				.getContextPath()) ? request.getContextPath() : "/");
		return cookie;
	}

}
