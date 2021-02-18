package com.cssrc.ibms.api.system.intf;

import java.util.List;
import com.cssrc.ibms.api.system.model.IAccountStrategy;
import com.cssrc.ibms.api.sysuser.model.ISysUser;

/**
 * IAccountStrategyService
 * @author liubo
 * @date 2017年3月30日
 */
public interface IAccountStrategyService {
	
	public abstract List<?extends IAccountStrategy> getAll();
	
	public abstract IAccountStrategy getById(String id);
	
	public abstract String meetMinPasswordLen(String password);
	
	public abstract String meetPasswordComplexity(String userName, String password);
	
	public abstract boolean meetMaxPasswordAge(ISysUser sysUser);
	
	public abstract boolean meetMinPasswordAge(ISysUser sysUser);
	
	public abstract boolean meetAccountLock(ISysUser sysUser);
}
