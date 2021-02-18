package com.cssrc.ibms.layout.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysLayoutService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.layout.dao.SysLayoutDao;
import com.cssrc.ibms.layout.model.SysLayout;
import com.cssrc.ibms.layout.util.Constant;

@Service
public class SysLayoutService extends BaseService<SysLayout> implements ISysLayoutService {

	@Resource
	private SysLayoutDao dao;
	
	@Override
	protected IEntityDao<SysLayout, Long> getEntityDao() {
		return dao;
	}

	/**
	 * Description: 保存布局信息
	 * Author: WJJ
	 * Date: 2018年5月8日 下午3:21:23
	 * @param layoutName
	 * @param appId
	 */
	public void save(String layoutName, String appType, String appId) {
		
		Long appType_n = Long.valueOf(appType);
		Long appId_n = null;
		
		if(appType_n!=Constant.APPTYPE_ALL) {
			if(StringUtil.isNotEmpty(appId)) {
				appId_n = Long.valueOf(appId);
			}
		}
		SysLayout sysLayout = dao.getUniqueSysLayout(appType_n, appId_n);
		boolean isNew = false;
		if(sysLayout==null) {
			sysLayout = new SysLayout();
			isNew = true;
		}
		
		sysLayout.setLayoutName(layoutName);
		sysLayout.setAppType(appType_n);
		sysLayout.setAppId(appId_n);
		if(isNew) {
			// 新增
			sysLayout.setId(UniqueIdUtil.genId());
			this.add(sysLayout);
		}else {
			// 更新
			this.update(sysLayout);
		}
	}
	
	/**
	 * Description: 获取当前用户布局
	 * Author: WJJ
	 * Date: 2018年5月8日 下午2:59:52
	 * @param userId
	 * @param orgId
	 * @return
	 */
	public String getLayoutType(Long userId, ISysOrg curSysOrg) {
		
		SysLayout sysLayout = this.getSysLayout(userId, curSysOrg);
		if (sysLayout!=null && StringUtil.isNotEmpty(sysLayout.getLayoutName())) {
			return sysLayout.getLayoutName();
		} 
		
		return "main";
	}
	
	/**
	 * 获取当前用户布局
	 * @param userId
	 * @param curSysOrg
	 * @return
	 */
	public SysLayout getSysLayout(Long userId, ISysOrg curSysOrg) {
		Long appType_n = Constant.APPTYPE_ALL;
		Long appId_n = null;
		//个人
		if (dao.isExist(Constant.APPTYPE_PER, userId)) { 
			appType_n = Constant.APPTYPE_PER;
			appId_n = userId;
		} else if (curSysOrg != null) {
			Long orgId = curSysOrg.getOrgId();
			if (dao.isExist(Constant.APPTYPE_ORG, orgId)) { 				
				appType_n = Constant.APPTYPE_ORG;
				appId_n = orgId;
			} 
		}
		
		SysLayout sysLayout = dao.getUniqueSysLayout(appType_n, appId_n);
		return sysLayout;
	}
}
