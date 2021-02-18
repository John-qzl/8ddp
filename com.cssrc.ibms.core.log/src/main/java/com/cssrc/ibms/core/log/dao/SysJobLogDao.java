package com.cssrc.ibms.core.log.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.log.model.SysJobLog;
/**
 * 后台日志数据访问层
 * <p>Title:SysJobLogDao</p>
 * @author Yangbo 
 * @date 2016-8-4下午05:21:24
 */
@Repository
public class SysJobLogDao extends BaseDao<SysJobLog> {
	public Class getEntityClass() {
		return SysJobLog.class;
	}
}
