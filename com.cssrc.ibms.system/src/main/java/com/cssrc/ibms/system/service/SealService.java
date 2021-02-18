package com.cssrc.ibms.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.IUserRoleService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.IUserRole;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.SealDao;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.Seal;

/**
 * 对象功能:电子印章 Service类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-14 上午08:15:04 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-14 上午08:15:04
 * @see
 */
@Service
public class SealService extends BaseService<Seal> {
	@Resource
	private SealDao sealDao;
	@Resource
	private SysFileDao sysFileDao;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private IUserRoleService userRoleService;

	protected IEntityDao<Seal, Long> getEntityDao() {
		return this.sealDao;
	}

	/**
	 * 根据印章ids，删除印章信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:27:40 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:27:40
	 * @param userId
	 * @param sealName
	 * @return
	 * @see
	 */
	public void delByIds(Long[] ids) {
		
		if (BeanUtils.isEmpty(ids))
			return;
		for (Long p : ids) {
			//获取印章
			Seal seal = (Seal)getById(p);
			//删除印章
			delById(p);
			//删除附件
			if (BeanUtils.isNotIncZeroEmpty(seal.getAttachmentId())) 
				this.sysFileDao.delById(seal.getAttachmentId());
			//删除图片
			if (BeanUtils.isNotIncZeroEmpty(seal.getShowImageId()))
				this.sysFileDao.delById(seal.getShowImageId());
		}
	}

	/**
	 * 检查用户ID和印章名称，获取印章信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午11:34:23 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:44:18
	 * @param id
	 * @param code
	 * @return
	 * @see
	 */
	public List<Seal> getSealByUserId(Long userId, String sealName) {
		
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("userId", userId);
		if (StringUtil.isNotEmpty(sealName)) {
			map.put("sealName", "%" + sealName + "%");
		}
		//获取当前用户所属角色信息
		List<?extends IUserRole> roles = this.userRoleService.getByUserId(userId);
		if (BeanUtils.isNotEmpty(roles)) {
			String roleIds = "";
			for (IUserRole userRole : roles) {
				roleIds = roleIds + userRole.getRoleId() + ",";
			}
			roleIds = roleIds.substring(0, roleIds.length() - 1);
			map.put("roleIds", roleIds);
		}
		//获取当前用户所属组织信息
		List<?extends ISysOrg> orgs = this.sysOrgService.getOrgsByUserId(userId);
		if (BeanUtils.isNotEmpty(orgs)) {
			String orgIds = "";
			for (ISysOrg sysOrg : orgs) {
				orgIds = orgIds + sysOrg.getOrgId() + ",";
			}
			orgIds = orgIds.substring(0, orgIds.length() - 1);
			map.put("orgIds", orgIds);
		}
		return this.sealDao.getSealByUserId(map);
	}
	
}
