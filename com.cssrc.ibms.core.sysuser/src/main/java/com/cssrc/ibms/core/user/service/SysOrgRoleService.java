package com.cssrc.ibms.core.user.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgRoleService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysOrgRoleDao;
import com.cssrc.ibms.core.user.dao.SysOrgRoleManageDao;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysOrgRole;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 
 * <p>Title:SysOrgRoleService</p>
 * @author Yangbo 
 * @date 2016-8-5下午03:16:10
 */
@Service
public class SysOrgRoleService extends BaseService<SysOrgRole> implements ISysOrgRoleService{

	@Resource
	private SysOrgRoleDao dao;

	@Resource
	private SysOrgDao sysOrgDao;

	@Resource
	private SysOrgRoleManageDao sysOrgRoleManageDao;

	protected IEntityDao<SysOrgRole, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 通过组织路径获取父路径
	 * @param orgId
	 * @return
	 */
	private String getPathByOrgId(Long orgId) {
		SysOrg sysOrg = this.sysOrgDao.getById(orgId);
		if (sysOrg == null)
			return "";
		String path = sysOrg.getPath();
		if (StringUtil.isEmpty(path))
			return "";

		path = path.replace("." + orgId + ".", "");

		int pos = path.indexOf(".");
		//超过四层有出错的可能
		path = path.substring(pos + 1);
		return path;
	}
	/**
	 * 获得该组织授权的角色
	 * @param orgId
	 * @return
	 */
	public List<SysOrgRole> getAssignRoleByOrgId(Long orgId) {
		List<SysOrgRole> list = this.sysOrgRoleManageDao
				.getAssignRoleByOrgId(orgId);
		if (BeanUtils.isNotEmpty(list)) {
			return list;
		}
		String path = getPathByOrgId(orgId);
		String[] aryPath = path.split("[.]");
		if (aryPath.length == 0)
			return list;
		String tmpOrgId;
		for (int i = aryPath.length - 1; i >= 0; i--) {
			tmpOrgId = aryPath[i];
			if(StringUtil.isNotEmpty(tmpOrgId)){
				Long lTmpOrgId = Long.valueOf(Long.parseLong(tmpOrgId));
				//可以分配给下级组织的角色
				list = this.sysOrgRoleManageDao.getAssignRoleByOrgId(lTmpOrgId);
	
				if (BeanUtils.isNotEmpty(list)) {
					break;
				}
			}
		}
		if (BeanUtils.isNotEmpty(list)) {
			return list;
		}
		//授权给组织的角色
		list = getRolesByOrgId(orgId);

		for (SysOrgRole sysOrgRole : list)
			sysOrgRole.setFromType(Integer.valueOf(1));
		return list;
	}
	/**
	 * 角色授权组织的角色获取
	 * @param orgId
	 * @return
	 */
	public List<SysOrgRole> getRolesByOrgId(Long orgId) {
		List<SysOrgRole> list = this.dao.getRolesByOrgId(orgId);
		if (BeanUtils.isNotEmpty(list))
			return list;

		list = new ArrayList();
		String path = getPathByOrgId(orgId);
		if (StringUtil.isEmpty(path))
			return list;
		String[] aryPath = path.split("[.]");
		
		for (int i = aryPath.length - 1; i >= 0; i--) {
			String tmpOrgId = aryPath[i];
			if(StringUtil.isEmpty(tmpOrgId)){
				continue;
			}
			Long lTmpOrgId = Long.valueOf(Long.parseLong(tmpOrgId));
			//父组织的授权角色
			list = this.dao.getRolesByOrgId(lTmpOrgId);

			if (BeanUtils.isNotEmpty(list)) {
				break;
			}
		}
		return list;
	}
	/**
	 * 给某组织授权角色
	 * @param roles
	 * @param orgId
	 * @param grade
	 * @return
	 */
	public String addOrgRole(Long[] roles, Long orgId, Integer grade) {
		String str = "";
		for (Long role : roles)
			//是否已授权该角色
			if (this.dao.getCountByOrgidRoleid(orgId, role)) {
				str = ",但添加的角色中包含已经授权的角色。";
			} else {
				SysOrgRole sysOrgRole = new SysOrgRole();
				sysOrgRole.setId(Long.valueOf(UniqueIdUtil.genId()));
				sysOrgRole.setOrgid(orgId);
				sysOrgRole.setRoleid(role);
				sysOrgRole.setCanDel(grade);
				this.dao.add(sysOrgRole);
			}
		return str;
	}
	/**
	 * 通过orgroleid来删除组织授权信息
	 * @param ids
	 * @return
	 */
	public Long delByOrgRoleIds(Long[] ids) {
		Set set = new HashSet();
		if (BeanUtils.isEmpty(ids))
			return Long.valueOf(0L);
		Long orgId = Long.valueOf(0L);
		for (Long id : ids) {
			SysOrgRole sysOrgRole = (SysOrgRole) this.dao.getById(id);
			orgId = sysOrgRole.getOrgid();

			this.dao.delById(id);
		}
		return orgId;
	}
	/**
	 * 删除该角色的组织关系
	 * @param roleId
	 */
	public void delByRoleId(Long roleId) {
		this.dao.delByRoleId(roleId);
	}
	/**
	 * 删除orgid的组织授权关系
	 * @param orgId
	 */
	public void delByOrgId(Long orgId) {
		this.dao.delByOrgId(orgId);
	}
	/**
	 * 级联删除该组织下所有子组织的角色授权信息(注意传进的path需要加%)
	 * @param path
	 */
	public void delByOrgPath(String path) {
		this.dao.delByOrgPath(path);
	}
	/**
	 * 删除orgid的组织角色为roleid的对应授权关系(指定解除授权)
	 * @param orgId
	 * @param roleId
	 */
	public void delByOrgIdAndRoleId(Long orgId, Long roleId) {
		this.dao.delByOrgIdAndRoleId(orgId, roleId);
	}
}
