package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.ITaskThreadClearService;
import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.system.intf.ISysPaurService;
import com.cssrc.ibms.api.sysuser.intf.IUserContextService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.cache.intf.ICache;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.db.datasource.DbContextHolder;
import com.cssrc.ibms.core.login.model.OnlineUser;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.listener.UserSessionListener;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestContext;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.tag.AnchorTag;
/**
 * 上下文一些获取方法均可写在这。 但要注意从ICurrentContext这里继承
 * 
 * @author Yangbo 2016-7-19
 * 
 */
@Service
public class UserContextService implements IUserContextService {
	private static ThreadLocal<String> curUserAccount = new ThreadLocal();
	private static ThreadLocal<ISysUser> curUser = new ThreadLocal();
	private static ThreadLocal<Locale> curLocale = new ThreadLocal();
	
	public ISysUser getCurrentUser() {
		//1。系统通过AOP切面类，AopFilter.java中的ContextUtil.clearAll();将curUser，curLocale等所有static值都清空了。
		//2。AopFilter.java类每执行一个action，就执行一遍。
		//3。iCache存放的org,position,company信息。在执行AopFilter.java的时候没有清空。
		//4。iCache存放的org,position,company信息：：  通过PositionService.java的类getDefaultPosByUserId(Long userId)获取，规则如下：
		    //(1)"有主组织获取主组织，没有主组织获取查询后的第一个组织。"(2)"有主岗位获取主岗位，没有主岗位获取查询后的第一个岗位。"
		//5。通过加redis实现内存缓存，可以参考iCache的实现
		//6。一个用户可以属于多个部门、属于多个岗位，但每个用户只有一个主部门和一个主岗位。
		if (curUser.get() != null) {
			//代码不会走进来，因为每次AopFilter.java将curUser清空了，都需要重新获取。
			ISysUser user = (ISysUser) curUser.get();
			return user;
		}
		ISysUser sysUser = null;
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if (securityContext != null) {
			Authentication auth = securityContext.getAuthentication();
			if (auth != null) {
				Object principal = auth.getPrincipal();
				if ((principal instanceof ISysUser)) {
					sysUser = (ISysUser) principal;
					
				}
			}
		}
		return sysUser;
	}

	public Long getCurrentUserId() {
		ISysUser curUser = getCurrentUser();
		if (curUser != null)
			return curUser.getUserId();
		return Long.valueOf(0L);
	}
	
	//获取当前用户密级
	public String getCurrentUserSecurity() {
		ISysUser curUser = getCurrentUser();
		if (curUser != null)
			return curUser.getSecurity();
		return null;
	}

	/**
	 * 获取当前用户皮肤
	 * 
	 * 
	 */
	public String getCurrentUserSkin(HttpServletRequest request) {
		String skinStyle = "default";

		HttpSession session = request.getSession();
		String skin = (String) session.getAttribute("skinStyle");
		if (StringUtil.isNotEmpty(skin))
			return skin;

		ISysPaurService sysPaurService = (ISysPaurService) AppUtil
				.getBean("sysPaurService");
		Long userId = getCurrentUserId();
		skinStyle = sysPaurService.getCurrentUserSkin(userId);
		session.setAttribute("skinStyle", skinStyle);
		return skinStyle;
	}

	/**
	 * 设置_当前岗位
	 */
	public void setCurrentPos(Long posId) {
		ISysUser user = getCurrentUser();
		PositionService positionService = (PositionService) AppUtil
				.getBean(PositionService.class);
		SysOrgService orgService = (SysOrgService) AppUtil
				.getBean(SysOrgService.class);
		Position position = (Position) positionService.getById(posId);
		SysOrg sysOrg = (SysOrg) orgService.getById(position.getOrgId());
		ICache iCache = (ICache) AppUtil.getBean(ICache.class);

		Long userId = user.getUserId();

		String posKey = ContextUtil.getPositionKey(userId);
		String orgKey = ContextUtil.getOrgKey(userId);
		iCache.add(posKey, position);
		iCache.add(orgKey, sysOrg);
	}

    public boolean isSuperAdmin()
    {
        SysUser user = (SysUser)getCurrentUser();
        if (user == null)
        {
            // 如果用户为空 表示不是超级管理员
            return false;
        }
        else
        {
            Collection<GrantedAuthority> auths = user.getAuthorities();
            return (auths != null) && (auths.size() > 0) && (auths.contains(SystemConst.ROLE_GRANT_SUPER));
        }
    }
	/**
	 * 获取_当前岗位
	 */
	public IPosition getCurrentPos() {
		Long userId = getCurrentUserId();
		ICache iCache = (ICache) AppUtil.getBean(ICache.class);
		String positionKey = ContextUtil.getPositionKey(userId);
		Position position = (Position) iCache.getByKey(positionKey);
		if (position == null) {
			PositionService positionService = (PositionService) AppUtil.getBean(PositionService.class);
			position = positionService.getDefaultPosByUserId(userId);
			if (position != null) {
				iCache.add(positionKey, position);
			}
		}
		return position;
	}
	//获取当前登录用户的组织 
	public ISysOrg getCurrentOrg() {
		ICache iCache = (ICache) AppUtil.getBean(ICache.class);
		SysOrgService sysOrgService = (SysOrgService) AppUtil.getBean(SysOrgService.class);
		//获取当前用户id
		Long userId = getCurrentUserId();
		//获取当前用户的组织keyname
		String orgKey = ContextUtil.getOrgKey(userId);
		//从iCache中获取取当前用户的组织sysOrg
		SysOrg sysOrg = (SysOrg) iCache.getByKey(orgKey);
		if (sysOrg == null) {
			//获取当前用户的岗位
			IPosition position = getCurrentPos();
			if (position != null) {
				//通过岗位获取组织id
				Long orgId = position.getOrgId();
				sysOrg = (SysOrg) sysOrgService.getById(orgId);
			}
		}
		//如果缓存中没有，则从UserPosition表中查找。可能有bug，一个账户有多个组织，多个岗位 by honghuajun
		if(sysOrg == null){
			UserPositionDao userPositionDao = (UserPositionDao) AppUtil.getBean(UserPositionDao.class);
			List<UserPosition> userPositionList = userPositionDao.getOrgListByUserId(userId);
			for(UserPosition userPosition : userPositionList){
				Long orgId = userPosition.getOrgId();
				sysOrg = (SysOrg) sysOrgService.getById(orgId);
				iCache.add(orgKey, sysOrg);
			}
		}
		if (sysOrg != null) {
			iCache.add(orgKey, sysOrg);
		}
		return sysOrg;
	}

	public ISysOrg getCurrentCompany() {
		ICache iCache = (ICache) AppUtil.getBean(ICache.class);
		String orgKey = "CurrentCompany_" + getCurrentUserId();
		ISysOrg sysCompany = (ISysOrg) iCache.getByKey(orgKey);

		if (sysCompany == null) {
			ISysOrg org = getCurrentOrg();
			if (org == null)
				return null;

			SysOrgTacticService orgTacticService = (SysOrgTacticService) AppUtil
					.getBean(SysOrgTacticService.class);
			List<SysOrg> sysOrgList = orgTacticService
					.getSysOrgListByOrgTactic();

			if (BeanUtils.isEmpty(sysOrgList)) {
				return null;
			}
			List orgIdList = new ArrayList();

			for (SysOrg orgTmp : sysOrgList) {
				orgIdList.add(orgTmp.getOrgId());
			}

			SysOrgService orgService = (SysOrgService) AppUtil.getBean(SysOrgService.class);

			while (!orgIdList.contains(org.getOrgId())) {
				Long parentId = org.getOrgSupId();
				if (parentId.equals(SysOrg.BEGIN_ORGID))
					break;
				org = (ISysOrg) orgService.getById(parentId);
			}

			if (orgIdList.contains(org.getOrgId())) {
				sysCompany = org;
			}

			if (sysCompany != null) {
				iCache.add(orgKey, sysCompany);
			}
		}
		return sysCompany;
	}

	public Long getCurrentPosId() {
		IPosition pos = getCurrentPos();
		if (pos != null)
			return pos.getPosId();
		return Long.valueOf(0L);
	}

	public Long getCurrentCompanyId() {
		if (isSuperAdmin())
			return Long.valueOf(0L);
		ISysOrg org = getCurrentCompany();
		if (org != null) {
			return org.getOrgId();
		}

		return Long.valueOf(0L);
	}
	
	public boolean isSuperAdmin(ISysUser user)
	{
		SysUser sysUser = (SysUser)user;
		Collection auths = sysUser.getAuthorities();
		return (auths != null) && (auths.size() > 0) && (auths.contains(SystemConst.ROLE_GRANT_SUPER));
	}
	
	public Long getCurrentOrgId()
	{
		ISysOrg sysOrg = getCurrentOrg();
		if (sysOrg == null) return Long.valueOf(0L);
		return sysOrg.getOrgId();
	}

	
	
	
	public Locale getLocale() {
		if (curLocale.get() != null) {
			return (Locale) curLocale.get();
		}
		setLocale(new Locale("zh", "CN"));
		return (Locale) curLocale.get();
	}

	public void setLocale(Locale locale) {
		curLocale.set(locale);
	}

	public void removeCurrentUser() {
		curUserAccount.remove();
	}

	public void removeCurrentOrg() {
		Long userId = UserContextUtil.getCurrentUserId();
		ICache iCache = (ICache) AppUtil.getBean(ICache.class);
		String positionKey = ContextUtil.getPositionKey(userId);
		String orgKey = ContextUtil.getOrgKey(userId);
		iCache.delByKey(positionKey);
		iCache.delByKey(orgKey);
		iCache.delByKey("CurrentCompany_" + userId);
	}

	public void clearAll() {
		curUser.remove();
		curLocale.remove();
		RequestContext.clearHttpReqResponse();
		
		ITaskThreadClearService taskThreadClearService=AppUtil.getBean(ITaskThreadClearService.class);
		taskThreadClearService.clearTaskAll();
		taskThreadClearService.clearUserAssignAll();
		taskThreadClearService.clearActivitiDefCache();

		MessageUtil.clean();

		SysUser.removeRoleList();

		AnchorTag.cleanFuncRights();

		DbContextHolder.clearDataSource();
		DbContextHolder.setDefaultDataSource();
	}

	public void setCurrentUserUsername(String account) {
		SysUserService sysUserService = (SysUserService) AppUtil
				.getBean(SysUserService.class);
		ISysUser sysUser = sysUserService.getByUsername(account);
		curUser.set(sysUser);
	}

	public void setCurrentUser(ISysUser sysUser) {
		curUser.set(sysUser);
	}

	public void cleanCurUser() {
		curUser.remove();
	}

	@Override
	public Map<Long, OnlineUser> getOnLineUsers() {
		return UserSessionListener.getOnLineUsers();
	}

    @Override
    public ISysUser createUser()
    {
        return new SysUser();
    }
}
