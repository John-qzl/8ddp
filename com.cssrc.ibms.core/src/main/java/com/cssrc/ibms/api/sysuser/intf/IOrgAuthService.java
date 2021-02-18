package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.api.sysuser.model.IOrgAuth;

public interface IOrgAuthService {

	public abstract void delById(Long id);

	/**
	 * 分级用户(管理员)授权信息
	 * @param userId
	 * @return
	 */
	public abstract List<?extends IOrgAuth> getByUserId(long userId);

	public abstract IOrgAuth getUserIdDimId(Long dimId, Long userId);

	public abstract boolean checkIsExist(Long userId, Long orgId);

	public abstract IOrgAuth getByUserIdAndOrgId(long userId, long orgId);

	public abstract void delByUserId(Long userId);

	public abstract void delByOrgId(Long orgId);

}