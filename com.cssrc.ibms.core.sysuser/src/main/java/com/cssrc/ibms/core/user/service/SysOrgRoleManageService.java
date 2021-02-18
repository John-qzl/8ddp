package com.cssrc.ibms.core.user.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgRoleManageService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysOrgRoleManageDao;
import com.cssrc.ibms.core.user.model.SysOrgRoleManage;
/**
 * 
 * <p>Title:SysOrgRoleManageService</p>
 * @author Yangbo 
 * @date 2016-8-5下午03:16:01
 */
@Service
public class SysOrgRoleManageService extends BaseService<SysOrgRoleManage> implements ISysOrgRoleManageService{

	@Resource
	private SysOrgRoleManageDao dao;

	protected IEntityDao<SysOrgRoleManage, Long> getEntityDao() {
		return this.dao;
	}

	public boolean addOrgRole(Long orgId, String roleIds, Integer grade) {
		boolean rtn = false;
		String[] aryRoles = roleIds.split(",");
		for (String sRoleId : aryRoles) {
			long roleId = Long.parseLong(sRoleId);
			if (this.dao.isOrgRoleExists(orgId, Long.valueOf(roleId))) {
				continue;
			}
			long id = UniqueIdUtil.genId();
			SysOrgRoleManage orgRole = new SysOrgRoleManage();
			orgRole.setId(Long.valueOf(id));
			orgRole.setOrgid(orgId);
			orgRole.setRoleid(Long.valueOf(roleId));
			orgRole.setCanDel(grade);
			this.dao.add(orgRole);
			rtn = true;
		}
		return rtn;
	}

	public void delByOrgId(Long orgId) {
		this.dao.delByOrgId(orgId);
	}
}
