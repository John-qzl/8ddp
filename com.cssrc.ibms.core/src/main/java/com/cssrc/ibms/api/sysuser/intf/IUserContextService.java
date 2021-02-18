package com.cssrc.ibms.api.sysuser.intf;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.IContext;
import com.cssrc.ibms.api.sysuser.model.IOnlineUser;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

import java.util.Locale;
import java.util.Map;
/**
 * 上下文方法接口，实现从CurrentContext.java这里写
 * @author Yangbo
 * 2016-7-19
 *
 */
@Service
public abstract interface IUserContextService extends IContext{
	public abstract ISysUser getCurrentUser();
	
	//获取当前用户密级
	public abstract String getCurrentUserSecurity();

	public abstract Long getCurrentUserId();
	/**
	 * 获取当前用户选择的皮肤
	 * @param paramHttpServletRequest
	 * @return
	 */
	public abstract String getCurrentUserSkin(
			HttpServletRequest paramHttpServletRequest);
	
	public abstract void setCurrentPos(Long paramLong);
	
	public abstract Long getCurrentCompanyId();
	
	public abstract ISysOrg getCurrentOrg();
	
	public abstract Long getCurrentOrgId();
	
    public abstract Long getCurrentPosId();
    
    public abstract boolean isSuperAdmin();
    
    public abstract IPosition getCurrentPos();
    
    public abstract boolean isSuperAdmin(ISysUser paramISysUser);
    
    public abstract Locale getLocale();

    public abstract void setLocale(Locale paramLocale);
    
    public abstract void removeCurrentUser();
    
    public abstract void removeCurrentOrg();

    public abstract void clearAll();
    
    public abstract void setCurrentUserUsername(String paramString);
    
    public abstract void setCurrentUser(ISysUser paramISysUser);

    public abstract ISysOrg getCurrentCompany();
    
    public abstract void cleanCurUser();
    
    public abstract Map<Long, ?extends IOnlineUser> getOnLineUsers();

    public abstract ISysUser createUser();
}
