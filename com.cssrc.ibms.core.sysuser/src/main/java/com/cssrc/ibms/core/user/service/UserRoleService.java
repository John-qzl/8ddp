package com.cssrc.ibms.core.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.api.sysuser.intf.IUserRoleService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.UserRoleDao;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class UserRoleService extends BaseService<UserRole> implements IUserRoleService{

	@Resource
	private UserRoleDao userRoleDao;
	@Resource
	private IResourcesService resourcesService;

	protected IEntityDao<UserRole, Long> getEntityDao() {
		return this.userRoleDao;
	}

	public void add(Long roleId, Long[] userIds) throws Exception {
		if ((roleId == null) || (roleId.longValue() == 0L) || (userIds == null)
				|| (userIds.length == 0))
			return;
		for (Long userId : userIds) {
			UserRole userRole = this.userRoleDao.getUserRoleModel(userId,
					roleId);
			if (userRole != null) {
				continue;
			}
			long userRoleId = UniqueIdUtil.genId();
			UserRole urro = new UserRole();
			urro.setUserRoleId(Long.valueOf(userRoleId));
			urro.setRoleId(roleId);
			urro.setUserId(userId);
			this.userRoleDao.add(urro);
		}
		//更新用户redis功能菜单
		this.resourcesService.setUserMenuToRedis(roleId);
	}

	public UserRole getUserRoleModel(Long userId, Long roleId) {
		return this.userRoleDao.getUserRoleModel(userId, roleId);
	}

	public void delUserRoleByIds(String[] lAryId, Long userId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (String roleId : lAryId) {
			if (StringUtil.isEmpty(roleId))
				continue;
			this.userRoleDao.delUserRoleByIds(userId,
					Long.valueOf(Long.parseLong(roleId)));
			//更新用户redis功能菜单
			this.resourcesService.updSysRightByUserId(userId);
		}
	}

	public void saveUserRole(Long userId, Long[] roleIds) throws Exception {
		this.userRoleDao.delByUserId(userId);

		if (BeanUtils.isEmpty(roleIds)) {
			return;
		}
		for (int i = 0; i < roleIds.length; i++) {
			Long roleId = roleIds[i];
			UserRole userRole = new UserRole();
			userRole.setRoleId(roleId);
			userRole.setUserId(userId);
			userRole.setUserRoleId(Long.valueOf(UniqueIdUtil.genId()));
			this.userRoleDao.add(userRole);
			//更新用户redis功能菜单
			this.resourcesService.setUserMenuToRedis(roleId);
		}
	}

	public List<Long> getUserIdsByRoleId(Long roleId) {
		return this.userRoleDao.getUserIdsByRoleId(roleId);
	}

	public List<UserRole> getUserByRoleIds(String roleIds) {
		return this.userRoleDao.getUserByRoleIds(roleIds);
	}

	public List<UserRole> getUserRoleByRoleId(Long roleId) {
		return this.userRoleDao.getUserRoleByRoleId(roleId);
	}

	public void delByRoleId(Long roleId) {
		List<Long> userIds = userRoleDao.getUserIdsByRoleId(roleId);
		this.userRoleDao.delByRoleId(roleId);
		//更新用户redis功能菜单
		for(Long userId:userIds){
			this.resourcesService.updSysRightByUserId(userId);
		}
	}

	public void delByUserRoleId(Long[] aryUserRoleId) {
		for (int i = 0; i < aryUserRoleId.length; i++) {
			Long userRoleId = aryUserRoleId[i];
			UserRole userRole = (UserRole) this.userRoleDao.getById(userRoleId);
			Long userId = userRole.getUserId();
			this.userRoleDao.delById(userRoleId);
			//更新用户redis功能菜单
			this.resourcesService.updSysRightByUserId(userId);
		}
	}

	public List<UserRole> getByUserId(Long userId) {
		return this.userRoleDao.getByUserId(userId);
	}

	/**
	 * 删除有关用户的所有角色信息(完全删除)
	 * 
	 * @param userId
	 * @return
	 */
	public void delByUserId(Long userId) {
		this.userRoleDao.delByUserId(userId);
		//更新用户redis功能菜单
		this.resourcesService.updSysRightByUserId(userId);
	}

	public void delByUserIdAndRoleId(Long userId, Long roleId) {
		this.userRoleDao.delUserRoleByIds(userId, roleId);
		//更新用户redis功能菜单
		this.resourcesService.updSysRightByUserId(userId);
	}
}
