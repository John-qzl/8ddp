package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;
import com.cssrc.ibms.api.sysuser.model.IUserRole;

public interface IUserRoleService{

	public abstract void add(Long roleId, Long[] userIds) throws Exception;

	public abstract IUserRole getUserRoleModel(Long userId, Long roleId);

	public abstract void delUserRoleByIds(String[] lAryId, Long userId);

	public abstract void saveUserRole(Long userId, Long[] roleIds)
			throws Exception;

	public abstract List<Long> getUserIdsByRoleId(Long roleId);

	public abstract List<?extends IUserRole> getUserByRoleIds(String roleIds);

	public abstract List<?extends IUserRole> getUserRoleByRoleId(Long roleId);

	public abstract void delByRoleId(Long roleId);

	public abstract void delByUserRoleId(Long[] aryUserRoleId);

	public abstract List<?extends IUserRole> getByUserId(Long userId);

	/**
	 * 删除有关用户的所有角色信息(完全删除)
	 * 
	 * @param userId
	 * @return
	 */
	public abstract void delByUserId(Long userId);

	public abstract void delByUserIdAndRoleId(Long userId, Long roleId);

}