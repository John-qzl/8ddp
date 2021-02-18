package com.cssrc.ibms.core.user.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.cache.intf.ICache;
import com.cssrc.ibms.core.login.model.OnlineUser;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;

/**
 * 用户在线状态监听.
 *
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] zhulongchao <br/>
 *         [创建时间] 2015-3-11 上午09:18:25 <br/>
 *         [修改时间] 2015-3-11 上午09:18:25
 * @see
 */
public class UserSessionListener implements HttpSessionListener {
	protected Logger logger = LoggerFactory
			.getLogger(UserSessionListener.class);
	private static String ONLINE_USERS = "onLineUsers_";

	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		String sessionId = event.getSession().getId();
		UserContextUtil.removeOnlineUser(sessionId);
	}

	public static Map<Long, OnlineUser> getOnLineUsers() {
		ICache icache = (ICache) AppUtil.getBean(ICache.class);
		Map usersMap = (Map) icache.getByKey(ONLINE_USERS);
		if (BeanUtils.isEmpty(usersMap)) {
			usersMap = new HashMap();
		}
		return usersMap;
	}

	public static void addOnlineUser(OnlineUser onlineUser) {
		ICache icache = (ICache) AppUtil.getBean(ICache.class);
		Map usersMap = (Map) icache.getByKey(ONLINE_USERS);
		if (BeanUtils.isEmpty(usersMap)) {
			usersMap = new HashMap();
		}
		usersMap.put(onlineUser.getUserId(), onlineUser);
		icache.add(ONLINE_USERS, usersMap);
	}

	public static void removeUser(Long userId) {
		ICache icache = (ICache) AppUtil.getBean(ICache.class);
		Map usersMap = (Map) icache.getByKey(ONLINE_USERS);
		if (BeanUtils.isNotEmpty(usersMap)) {
			usersMap.remove(userId);
		} else {
			usersMap = new HashMap();
		}
		icache.add(ONLINE_USERS, usersMap);
	}

	public void attributeAdded(HttpSessionBindingEvent event) {
		if (!"SPRING_SECURITY_LAST_USERNAME".equals(event.getName()))
			return;
		SysUser user = (SysUser) UserContextUtil.getCurrentUser();
		if (user == null) {
			return;
		}

		OnlineUser onlineUser = new OnlineUser();

		onlineUser.setUserId(user.getUserId());
		onlineUser.setUsername(user.getUsername());
		SysOrg org = (SysOrg) UserContextUtil.getCurrentOrg();
		if (org != null) {
			onlineUser.setOrgId(org.getOrgId());
			onlineUser.setOrgName(org.getOrgName());
		}

		addOnlineUser(onlineUser);
	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		if ("SPRING_SECURITY_LAST_USERNAME".equals(event.getName())) {
			SysUser user = ((SysUserService) AppUtil
					.getBean(SysUserService.class))
					.getByUsername((String) event.getValue());
			if (user != null)
				removeUser(user.getUserId());
		}
	}

	public void attributeReplaced(HttpSessionBindingEvent event) {
		this.logger.info(event.getName());
	}

}
