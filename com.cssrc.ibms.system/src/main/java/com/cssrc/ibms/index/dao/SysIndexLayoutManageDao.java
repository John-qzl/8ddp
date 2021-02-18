package com.cssrc.ibms.index.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.index.model.SysIndexLayoutManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class SysIndexLayoutManageDao extends BaseDao<SysIndexLayoutManage> {
	public Class<?> getEntityClass() {
		return SysIndexLayoutManage.class;
	}

	public List<SysIndexLayoutManage> getManageLayout(String orgIds, Short isDef) {
		Map params = new HashMap();
		params.put("orgIds", orgIds);
		params.put("isDef", isDef);
		if (BeanUtils.isEmpty(orgIds)) {
			params.put("isNullOrg", Boolean.valueOf(true));
		}
		return getBySqlKey("getManageLayout", params);
	}

	public List<SysIndexLayoutManage> getByUserIdFilter(QueryFilter filter) {
		return getBySqlKey("getByUserIdFilter", filter);
	}

	public void updateIsDef(Long orgId) {
		Map params = new HashMap();
		params.put("orgId", orgId);
		update("updateIsDef", params);
	}
}
