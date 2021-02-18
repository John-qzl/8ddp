
package com.cssrc.ibms.core.login.dao;

import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.login.model.LoginLog;
/**
 * @author Yangbo
 * 2016.7.14
 * LoginLogDao层
 */
@Repository
public class LoginLogDao extends BaseDao<LoginLog> {
	public Class<?> getEntityClass() {
		return LoginLog.class;
	}
}
