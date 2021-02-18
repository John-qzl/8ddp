package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.MonOrgRoleDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.model.MonOrgRole;
import com.cssrc.ibms.core.user.model.SysOrg;
/**
 * 
 * <p>Title:MonOrgRoleService</p>
 * @author Yangbo 
 * @date 2016-8-11下午02:27:20
 */
@Service
public class MonOrgRoleService extends BaseService<MonOrgRole> {

	@Resource
	private MonOrgRoleDao dao;

	@Resource
	private SysOrgDao sysOrgDao;

	protected IEntityDao<MonOrgRole, Long> getEntityDao() {
		return this.dao;
	}

	public void saveAuth(Long groupId, String roleIds, String orgIds) {
		this.dao.delByGroupId(groupId);
		List<Long> orgList = getOrgs(orgIds);
		String[] aryRole = roleIds.split(",");
		for (String roleId : aryRole) {
			Long role = new Long(roleId);
			for (Long orgId : orgList) {
				MonOrgRole monOrgRole = new MonOrgRole();
				monOrgRole.setId(Long.valueOf(UniqueIdUtil.genId()));
				monOrgRole.setRoleid(role);
				monOrgRole.setOrgid(orgId);
				monOrgRole.setGroupid(groupId);
				this.dao.add(monOrgRole);
			}
		}
	}

	public List<Long> getOrgs(String orgIds) {
		String[] aryIds = orgIds.split(",");
		List orgIdList = new ArrayList();
		for (String orgId : aryIds) {
			orgIdList.add(new Long(orgId));
		}
		List rtnList = new ArrayList();
		String preOrg = "";
		List<SysOrg> orgList = this.sysOrgDao.getByOrgIds(orgIdList);
		for (SysOrg org : orgList) {
			if (preOrg.equals("")) {
				rtnList.add(org.getOrgId());
				preOrg = org.getPath();
			} else {
				String curPath = org.getPath();
				if (curPath.indexOf(preOrg) == -1) {
					rtnList.add(org.getOrgId());
					preOrg = org.getPath();
				}
			}
		}

		return rtnList;
	}
}
