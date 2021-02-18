package com.cssrc.ibms.layout.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.layout.model.SysLayout;

@Repository
public class SysLayoutDao extends BaseDao<SysLayout>{

	@Override
	public Class<SysLayout> getEntityClass() {
		return SysLayout.class;
	}
	
	/**
	 * Description: 判断是否自定义系统布局
	 * Author: WJJ
	 * Date: 2018年5月8日 下午1:58:27
	 * @param appId
	 * @return
	 */
	public boolean isExist(Long appType, Long appId) {
		Map map = new HashMap();
		map.put("appType", appType);
		map.put("appId", appId);
		int count = ((Integer)this.getOne("isExist", map)).intValue();
		return count > 0;
	}
	
	public boolean isExistAppType(Long appType) {
		int count = ((Integer)this.getOne("isExistAppType", appType)).intValue();
		return count > 0;
	}
	
	public SysLayout getUniqueSysLayout(Long appType, Long appId) {
		Map map = new HashMap();
		map.put("appType", appType);
		map.put("appId", appId);

		SysLayout sysLayout = this.getUnique("getLayoutByAppId", map);
		return sysLayout;
	}
}
