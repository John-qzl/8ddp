package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.api.sysuser.model.ISysRole;

public interface ISysRoleService{

	/**
	 * 角色别名校验
	 * @param alias
	 * @return
	 */
	public abstract boolean isExistRoleAlias(String alias);

	/**
	 * 该角色别名是否存在了
	 * @param alias
	 * @param roleId
	 * @return
	 */
	public abstract boolean isExistRoleAliasForUpd(String alias, Long roleId);

	/**
	 * 添加过滤的角色数组
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysRole> getRoleList(QueryFilter queryFilter);

	/**
	 * 用户id获取角色信息
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysRole> getByUserId(Long userId);

	/**
	 * 用户id获得授权的角色字符串
	 * @param userId
	 * @return
	 */
	public abstract String getRoleIdsByUserId(Long userId);

	/**
	 * 用户id获得授权的角色Long数组
	 * @param userId
	 * @return
	 */
	public abstract List<Long> getRoleIdsByUserIdLong(Long userId);

	/**
	 * 根据条件查询role列表
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysRole> getRoleTree(QueryFilter queryFilter);

	/**
	 * 获得组织和用户授权角色的别名数组的总和
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public abstract List<String> getRolesByUserIdAndOrgId(Long userId,
			Long orgId);

	/**
	 * 获取组织的授权角色别名
	 * @param orgId
	 * @return
	 */
	public abstract List<String> getOrgRoles(Long orgId);

	/**
	 * 符合角色名的列表
	 * @param roleName
	 * @return
	 */
	public abstract List<?extends ISysRole> getByRoleName(String roleName);

	/**
	 * 符合角色别名的角色数组
	 * @param roleAlias
	 * @return
	 */
	public abstract ISysRole getByRoleAlias(String roleAlias);

	/**
	 * 授权id查找授权的角色
	 * @param authId
	 * @return
	 */
	public abstract List<?extends ISysRole> getByAuthId(Long authId);

	/**
	 * 用过用户(管理员id)获得角色
	 * @param userId
	 * @return
	 */
	public abstract List<?extends ISysRole> getByUser(long userId);

	/**
	 * 当前用户管理的分级组织授权的角色
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends ISysRole> getUserAssignRole(QueryFilter queryFilter);

	/**
	 * 获取角色的分类
	 * @return
	 */
	public abstract List<String> getDistinctCategory();

	public abstract ISysRole getById(Long id);
	
	/**
	 * 将所有角色放到redis中
	 */
	public void setAllSysRoleToRedis();

}