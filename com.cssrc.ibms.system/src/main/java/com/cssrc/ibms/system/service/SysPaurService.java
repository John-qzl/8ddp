package com.cssrc.ibms.system.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysPaurService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.SysPaurDao;
import com.cssrc.ibms.system.model.SysPaur;
/**
 * 
 * @author Yangbo
 * 2016-7-19
 *
 */
@Service
public class SysPaurService extends BaseService<SysPaur> implements ISysPaurService{

	@Resource
	private SysPaurDao dao;

	protected IEntityDao<SysPaur, Long> getEntityDao() {
		return this.dao;
	}

	public SysPaur getByUserAndAlias(Long userId, String aliasName) {
		if ((userId == null) || (aliasName == null))
			return null;
		return this.dao.getByUserAndAlias(userId, aliasName);
	}
	/**
	 * 根据用户id获取设置的对应主题皮肤
	 * @param userId
	 * @return
	 */
	public String getCurrentUserSkin(Long userId) {
		String skinStyle = "default";
		SysPaur skinSysPaur = this.dao.getByUserAndAlias(userId, "skin");
		if (skinSysPaur == null) {
			skinSysPaur = this.dao.getByUserAndAlias(Long.valueOf(0L), "skin");
		}
		if ((skinSysPaur != null) && (skinSysPaur.getPaurvalue() != null)) {
			skinStyle = skinSysPaur.getPaurvalue();
		}
		return skinStyle;
	}
	
	/**
	 * 更改系统主题，一旦更改，整个平台更改
	 *@author YangBo @date 2016年11月17日下午2:44:40
	 *@param paramvalue
	 */
	public void updSysTheme(String paramvalue){
			this.dao.updSysTheme(paramvalue);
	}
}
