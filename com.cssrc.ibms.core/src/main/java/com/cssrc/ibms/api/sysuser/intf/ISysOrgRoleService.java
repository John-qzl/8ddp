package com.cssrc.ibms.api.sysuser.intf;

import com.cssrc.ibms.api.sysuser.model.ISysOrgRole;

/**
 * ISysOrgRoleService
 * @author liubo
 * @date 2017年8月31日下午6:11:15
 */
public interface ISysOrgRoleService{
	/**
	 * 根据ID查找 组织角色
	 * @param id
	 * @return
	 */
	public abstract ISysOrgRole getById(Long id);
}