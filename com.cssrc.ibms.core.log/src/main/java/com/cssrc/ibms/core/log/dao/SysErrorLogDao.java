package com.cssrc.ibms.core.log.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.log.model.SysErrorLog;

import org.springframework.stereotype.Repository;

/**
 * 操作错误日志Dao层
 *
 * @author Yangbo 2016-7-26
 *
 */
@Repository
public class SysErrorLogDao extends BaseDao<SysErrorLog> {
	public Class<?> getEntityClass() {
		return SysErrorLog.class;
	}
}
