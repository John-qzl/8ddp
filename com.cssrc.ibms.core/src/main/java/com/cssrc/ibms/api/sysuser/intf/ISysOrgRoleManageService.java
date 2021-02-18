package com.cssrc.ibms.api.sysuser.intf;

import com.cssrc.ibms.api.sysuser.model.ISysOrgRoleManage;

/**
 * ISysOrgRoleManageService
 * @author liubo
 * @date 2017年8月31日下午6:10:54
 */
public interface ISysOrgRoleManageService{
	/**
	 * 根据ID查找 组织角色
	 * @param id
	 * @return
	 */
	public abstract ISysOrgRoleManage getById(Long id);
}