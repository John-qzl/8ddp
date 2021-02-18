package com.cssrc.ibms.core.msg.dao;


import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.msg.model.MessageLog;

/**
 * <pre>
 * 对象功能:消息日志 Dao类 
 * 开发人员:zhulongchao
 * 创建时间:2014-11-29 16:24:35
 * </pre>
 */
@Repository
public class MessageLogDao extends BaseDao<MessageLog> {
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		return MessageLog.class;
	}

}
