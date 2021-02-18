package com.cssrc.ibms.core.msg.service;


import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.IMessageLogService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.msg.dao.MessageLogDao;
import com.cssrc.ibms.core.msg.model.MessageLog;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <pre>
 * 对象功能:消息日志 Service类 
 * 开发人员:zhulongchao
 * 创建时间:2014-11-29 16:24:35
 * </pre>
 */
@Service
public class MessageLogService extends BaseService<MessageLog> implements IMessageLogService{
	@Resource
	private MessageLogDao dao;

	public MessageLogService() {
	}

	@Override
	protected IEntityDao<MessageLog, Long> getEntityDao() {
		return dao;
	}

	/**
	 * 新增消息日志
	 * @param subject 主题
	 * @param receiver 接受者 ，多个人 ","分隔
	 * @param messageType 消息类型
	 * @param state 状态
	 */
	public void addMessageLog(String subject, String receiver,
			Integer messageType, Integer state) {
		MessageLog messageLog = new MessageLog();
		messageLog.setId(UniqueIdUtil.genId());
		messageLog.setSubject(StringUtils.substring(subject, 0, 50));
		messageLog.setReceiver(receiver);
		messageLog.setMessageType(messageType);
		messageLog.setState(state);
		messageLog.setSendTime(new Date());
		dao.add(messageLog);
	}
}
