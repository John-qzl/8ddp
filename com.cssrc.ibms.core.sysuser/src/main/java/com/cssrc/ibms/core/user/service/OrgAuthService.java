package com.cssrc.ibms.core.user.service;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.IOrgAuthService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.OrgAuthDao;
import com.cssrc.ibms.core.user.model.AuthRole;
import com.cssrc.ibms.core.user.model.OrgAuth;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 组织授权业务层
 * 
 * <p>Title:OrgAuthService</p>
 * @author Yangbo 
 * @date 2016-7-30下午03:01:19
 */
@Service
public class OrgAuthService extends BaseService<OrgAuth> implements IOrgAuthService{

	@Resource
	private OrgAuthDao dao;

	@Resource
	private AuthRoleService authRoleService;

	protected IEntityDao<OrgAuth, Long> getEntityDao() {
		return this.dao;
	}

	public void add(OrgAuth orgAuth, Long[] roleIds) {
		checkOrgAuthIsExist(orgAuth.getUserId(), orgAuth.getOrgId());
		add(orgAuth);
		saveAuthRoles(roleIds, orgAuth.getId());
	}

	private void checkOrgAuthIsExist(Long userId, Long orgId) {
		if (this.dao.checkOrgAuthIsExist(userId, orgId))
			throw new RuntimeException("当前组织的分级管理员已经存在，请勿重复添加！");
	}
	
	private void saveAuthRoles(Long[] roleIds, Long authId) {
		this.authRoleService.delByAuthId(authId);
		if (BeanUtils.isEmpty(roleIds))
			return;
		Long[] arrayOfLong;
		int j = (arrayOfLong = roleIds).length;
		for (int i = 0; i < j; i++) {
			long roleId = arrayOfLong[i].longValue();
			this.authRoleService.add(new AuthRole(UniqueIdUtil.genId(), authId,
					Long.valueOf(roleId)));
		}
	}

	public void update(OrgAuth orgAuth, Long[] roleIds) {
		update(orgAuth);
		saveAuthRoles(roleIds, orgAuth.getId());
	}

	public void delById(Long id) {
		this.authRoleService.delByAuthId(id);
		super.delById(id);
	}
	/**
	 * 分级用户(管理员)授权信息
	 * @param userId
	 * @return
	 */
	public List<OrgAuth> getByUserId(long userId) {
		List<OrgAuth> groupAuthList = this.dao.getByUserId(userId);
		List authList = new ArrayList();

		for (OrgAuth auth : groupAuthList) {
			boolean isChild = false;
			for (OrgAuth groupAuth : groupAuthList) {
				if ((auth.getId() != groupAuth.getId().longValue())
						&& (auth.getDimId().equals(groupAuth.getDimId()))
						&& (auth.getOrgPath()
								.startsWith(groupAuth.getOrgPath())))
					isChild = true;
			}
			if (isChild)
				continue;
			authList.add(auth);
		}

		return authList;
	}

	public OrgAuth getUserIdDimId(Long dimId, Long userId) {
		return this.dao.getUserIdDimId(dimId, userId);
	}

	public boolean checkIsExist(Long userId, Long orgId) {
		return this.dao.checkOrgAuthIsExist(userId, orgId);
	}

	public OrgAuth getByUserIdAndOrgId(long userId, long orgId) {
		return this.dao.getByUserIdAndOrgId(userId, orgId);
	}

	public void delByUserId(Long userId) {
		this.dao.delByUserId(userId);
	}

	public void delByOrgId(Long orgId) {
		this.dao.delByOrgId(orgId);
	}
}
