package com.cssrc.ibms.system.service;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.IRoleResourcesService;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.RoleResourcesDao;
import com.cssrc.ibms.system.model.RoleResources;

@Service
public class RoleResourcesService extends BaseService<RoleResources> implements IRoleResourcesService{

	@Resource
	private RoleResourcesDao roleResourcesDao;

	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	IResourcesService resourcesService;

	protected IEntityDao<RoleResources, Long> getEntityDao() {
		return this.roleResourcesDao;
	}
	/**
	 * 给某角色赋值功能点
	 * @param roleId
	 * @param resIds
	 * @throws Exception
	 */
	public void update(Long roleId, Long[] resIds)
			throws Exception {
		this.roleResourcesDao.delByRole(roleId);
		//roleId的值要注意
		if ((roleId!=null)&& (resIds != null) && (resIds.length > 0)) {
			Long[] arrayOfLong;
			int j = (arrayOfLong = resIds).length;
			for (int i = 0; i < j; i++) {
				long resId = arrayOfLong[i].longValue();
				RoleResources rores = new RoleResources();
				rores.setRoleResId(Long.valueOf(UniqueIdUtil.genId()));
				rores.setResId(Long.valueOf(resId));
				rores.setRoleId(roleId);
				add(rores);
			}
		}
		//更新用户redis功能菜单
		this.resourcesService.setUserMenuToRedis(roleId);
		
	}
	/**
	 * 批量角色授权功能点(具有某个岗位和某个个体可拥有多个角色)
	 * @param roleIds
	 * @param resIds
	 */
	public void saveBatch(Long[] roleIds, Long[] resIds) {
		this.roleResourcesDao.delByRoleAndRes(roleIds, resIds);

		if ((roleIds != null)&& (roleIds.length > 0) && (resIds != null)&& (resIds.length > 0)) {
			Long[] arrayOfLong1;
			int j = (arrayOfLong1 = resIds).length;
			for (int i = 0; i < j; i++) {
				long resId = arrayOfLong1[i].longValue();
				Long[] arrayOfLong2;
				int m = (arrayOfLong2 = roleIds).length;
				for (int k = 0; k < m; k++) {
					long roleId = arrayOfLong2[k].longValue();
					RoleResources rores = new RoleResources();
					rores.setRoleResId(Long.valueOf(UniqueIdUtil.genId()));
					rores.setResId(Long.valueOf(resId));
					rores.setRoleId(Long.valueOf(roleId));
					add(rores);
				}
			}
		}
		for(Long roleId:roleIds){
		    //更新用户redis功能菜单
            this.resourcesService.setUserMenuToRedis(roleId);
		}
	}
	public void delByResId(Long resId) {
		this.roleResourcesDao.delByResId(resId.longValue());
	}

	@Override
	public void copyRoleResources(Long oldRoleId,Long newRoleId) {
		List<RoleResources> roleResourcesList = this.roleResourcesDao
				.getByRole(Long.valueOf(oldRoleId));
		for (RoleResources rores : roleResourcesList) {
			RoleResources roleres = (RoleResources) rores.clone();
			roleres.setRoleResId(Long.valueOf(UniqueIdUtil.genId()));
			roleres.setRoleId(newRoleId);
			this.add(roleres);
		}
	}
}
