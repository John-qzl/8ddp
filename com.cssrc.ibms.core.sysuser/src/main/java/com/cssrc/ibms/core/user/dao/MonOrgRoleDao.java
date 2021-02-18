package com.cssrc.ibms.core.user.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.MonOrgRole;
/**
 * 
 * <p>Title:MonOrgRoleDao</p>
 * @author Yangbo 
 * @date 2016-8-11下午02:24:04
 */
@Repository
public class MonOrgRoleDao extends BaseDao<MonOrgRole> {
	public Class<?> getEntityClass() {
		return MonOrgRole.class;
	}

	public void delByGroupId(Long groupId) {
		delBySqlKey("delByGroupId", groupId);
	}
}
