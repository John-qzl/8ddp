package com.cssrc.ibms.core.user.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.SysOrgParam;
/**
 * 
 * <p>Title:SysOrgParamDao</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:50:18
 */
@Repository
public class SysOrgParamDao extends BaseDao<SysOrgParam> {
	public Class getEntityClass() {
		return SysOrgParam.class;
	}

	public int delByOrgId(long orgId) {
		return delBySqlKey("delByOrgId", Long.valueOf(orgId));
	}

	public List<SysOrgParam> getByOrgId(Long orgId) {
		return getBySqlKey("getByOrgId", orgId);
	}

	public SysOrgParam getByParamKeyAndOrgId(String paramKey, Long orgId) {
		Map params = new HashMap();
		params.put("paramKey", paramKey);
		params.put("orgId", orgId);
		return (SysOrgParam) getUnique("getByParamKeyAndOrgId", params);
	}
}
