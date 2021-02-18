package com.cssrc.ibms.api.system.intf;

import java.util.List;

import com.cssrc.ibms.api.jms.model.IMessageModel;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IMessageSendService {

	/**
	 * 查询某个用户的接收消息
	 * 
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends IMessageSend> getReceiverByUser(QueryFilter queryFilter);

	/**
	 * 获取个人未读信息
	 * @param userId
	 * @return
	 */
	public abstract List<?extends IMessageSend> getNotReadMsg(Long receiverId);

	public abstract void addMessageSend(IMessageSend messageSend);

	public abstract IMessageSend getMessageSend(IMessageModel model);
	/**
	 * @author Yangbo 2016-7-22
	 * 获取未读信息
	 * @param userId
	 * @param pb
	 * @return
	 */
	public abstract List<?extends IMessageSend> getNotReadMsgByUserId(long longValue, PagingBean pb);

	public abstract IMessageSend getById(Long valueOf);

	public abstract Integer getCountReceiverByUser(Long currentUserId);

	public abstract Integer getCountNotReadMsg(Long currentUserId);

}