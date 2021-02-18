package com.cssrc.ibms.core.user.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.user.model.UserCustom;
@Repository
public class UserCustomDao extends BaseDao<UserCustom> {
	public Class getEntityClass() {
		return UserCustom.class;
	}
}
