package com.cssrc.ibms.core.log.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.log.model.SysLog;

@Repository
public class SysLogDao extends BaseDao<SysLog>{
	@Override
	public Class getEntityClass() {
		return SysLog.class;
	}
	
	/**
	 * 获取用户范围类的日志
	 *@author YangBo @date 2016年11月18日下午2:30:59
	 *@param userIds
	 *@return
	 */
	public List<SysLog> getInUserIds(QueryFilter filter){
		return this.getBySqlKey("getInUserIds", filter);
	}
	/**
	 * 获取不在该用户区间的日志
	 *@author YangBo @date 2016年11月18日下午2:31:08
	 *@param userIds
	 *@return
	 */
	public List<SysLog> getNotInUserIds(QueryFilter filter){
		return this.getBySqlKey("getNotInUserIds", filter);
	}
}
