package com.cssrc.ibms.api.sysuser.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.support.AbstractMessageSource;

import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.ICurrentSystem;
import com.cssrc.ibms.api.sysuser.model.IOnlineUser;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.intf.IUserContextService;

/**
 * 取得当前用户登录时的上下文环境，一般用于获取当前登录的用户
 *
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] zhulongchao <br/>
 *         [创建时间] 2015-3-17 下午04:41:36 <br/>
 *         [修改时间] 2015-3-17 下午04:41:36
 * @see
 */
public class UserContextUtil {
	public static final String CurrentOrg = "CurrentOrg_";
	public static final String CurrentCompany = "CurrentCompany_";
	public static final String CurrentPos = "CurrentPos_";
	private static IUserContextService context_;
	private static Map<String, ? extends IOnlineUser> onlineUsers = new HashMap<String, IOnlineUser>();
	private static HashSet<Long> onlineUserIds = new HashSet<Long>();

	public static String getPositionKey(Long userId) {
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

	public static synchronized IUserContextService getContext() {
		if (context_ == null) {
			context_ = (IUserContextService) AppUtil
					.getBean(IUserContextService.class);
		}
		return context_;
	}

	public static ISysUser getCurrentUser() {
		IUserContextService context = getContext();
		return context.getCurrentUser();
	}
    
    public static ISysUser createUser()
    {
        IUserContextService context = getContext();
        return context.createUser();
    }
	
	//获取当前用户密级
	public static String getCurrentUserSecurity() {
		IUserContextService context = getContext();
		return context.getCurrentUserSecurity();
	}

	public static Locale getLocale() {
		IUserContextService context = getContext();
		return context.getLocale();
	}

	public static void setLocale(Locale locale) {
		IUserContextService context = getContext();
		context.setLocale(locale);
	}

	public static Long getCurrentUserId() {
		IUserContextService context = getContext();
		return context.getCurrentUserId();
	}

	public static void setCurrentUsername(String username) {
		IUserContextService context = getContext();
		context.setCurrentUserUsername(username);
	}

	public static void setCurrentUser(ISysUser sysUser) {
		IUserContextService context = getContext();
		context.setCurrentUser(sysUser);
	}

	public static void setCurrentPos(Long posId) {
		IUserContextService context = getContext();
		context.setCurrentPos(posId);
	}

	public static ISysOrg getCurrentOrg() {
		IUserContextService context = getContext();
		return context.getCurrentOrg();
	}

	public static ISysOrg getCurrentCompany() {
		IUserContextService context = getContext();
		return context.getCurrentCompany();
	}

	public static Long getCurrentCompanyId() {
		IUserContextService context = getContext();
		return context.getCurrentCompanyId();
	}

	public static IPosition getCurrentPos() {
		IUserContextService context = getContext();
		return context.getCurrentPos();
	}

	public static Long getCurrentPosId() {
		IUserContextService context = getContext();
		return context.getCurrentPosId();
	}

	public static Long getCurrentOrgId() {
		IUserContextService context = getContext();
		return context.getCurrentOrgId();
	}

	public static String getCurrentUserSkin(HttpServletRequest request) {
		IUserContextService context = getContext();
		return context.getCurrentUserSkin(request);
	}

	public static void cleanCurUser() {
		IUserContextService context = getContext();
		context.cleanCurUser();
	}

	public static void removeCurrentOrg() {
		IUserContextService context = getContext();
		context.removeCurrentOrg();
	}

	public static void clearAll() {
		IUserContextService context = getContext();
		context.clearAll();
	}

	public static void removeCurrentUser() {
		IUserContextService context = getContext();
		context.removeCurrentUser();
	}

	public static boolean isSuperAdmin() {
		IUserContextService context = getContext();
		return context.isSuperAdmin();
	}

	public static boolean isSuperAdmin(ISysUser user) {
		IUserContextService context = getContext();
		return context.isSuperAdmin(user);
	}

	/**
	 * 通过资源的key获得对于key语言
	 * 
	 * @param code
	 *            资源的key
	 * @return
	 */
	public static String getMessages(String code) {
		return getMessages(code, null);
	}

	/**
	 * 通过资源的key获得对于key语言
	 * 
	 * @param code
	 *            资源的key
	 * @param args
	 * @param locale
	 * @return
	 */
	public static String getMessages(String code, Object[] args, Locale locale) {
		AbstractMessageSource messages = (AbstractMessageSource) AppUtil
				.getBean("messageSource");
		if (locale == null)
			locale = getLocale();
		return messages.getMessage(code, args, locale);
	}

	/**
	 * 通过资源的key获得对于key语言
	 * 
	 * @param code
	 *            资源的key
	 * @param args
	 * @return
	 */
	public static String getMessages(String code, Object[] args) {
		return getMessages(code, args, getLocale());
	}

	public static ISysUser getCurrentUser(Long userId, String fullName) {
		ISysUser currentUser = UserContextUtil.getCurrentUser();
		return currentUser;
	}

	/**
	 * 取得用户连接。
	 * 
	 * @param userId
	 * @param fullName
	 * @return
	 */
	public static String getUserLink(String userId, String fullName) {
		String url = AppConfigUtil.get("serverUrl");
		url += "/oa/system/sysUser/get.do?userId=" + userId + "&hasClose=true";
		return "<a href='" + url + "'>" + fullName + "</a>";
	}

	public static Map<Long, ? extends IOnlineUser> getOnLineUsers() {
		return context_.getOnLineUsers();
	}

	/**
	 * 获取当前项目系统基本参数
	 * 
	 * @return
	 */
	public static ICurrentSystem getCurrentSystem() {
		ISysParameterService sysParameterService = (ISysParameterService) AppUtil
				.getBean(ISysParameterService.class);
		return sysParameterService.getCurrentSystem();
	}

	/**
	 * 获取当前用户的json数据
	 * 
	 * @return
	 */
	public static String getCurUserInfo() {
		ISysUser currentUser = getCurrentUser();
		if(currentUser!=null){
		      StringBuffer sb = new StringBuffer();
		        sb.append("{success:true,user:{userId:'")
		                .append(currentUser.getUserId()).append("',fullname:'")
		                .append(currentUser.getFullname()).append("',username:'")
		                .append(currentUser.getUsername()).toString();
		        sb.append("}");
		        return sb.toString().replaceAll("\"", "'");
		}else{
		    return "";
		}

	}

	public static void removeOnlineUser(String sessionId) {
		IOnlineUser user = onlineUsers.get(sessionId);
		if (user != null) {
			onlineUserIds.remove(user.getUserId());
		}
		onlineUsers.remove(sessionId);
	}

	public static Map<String, ? extends IOnlineUser> getOnlineUsers() {
		return onlineUsers;
	}

	public static HashSet<Long> getOnlineUserIds() {
		return onlineUserIds;
	}
}
