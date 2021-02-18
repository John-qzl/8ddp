package com.cssrc.ibms.core.msg.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.msg.model.MessageReply;
import com.cssrc.ibms.core.util.bean.BeanUtils;

@Repository
public class MessageReplyDao extends BaseDao<MessageReply>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return MessageReply.class;
	}
	
	/**
	 * 获得已回复此消息的人员
	 * @param messageId
	 * @return
	 */
	public List<MessageReply> getReplyByMsgId(Long messageId)
	{
		return this.getBySqlKey("getReplyByMsgId", messageId);
	}
	
	/**
	 * 根据消息ID删除回复
	 * @param messageId
	 */
	public int delReplyByMsgId(Long messageId){
		return delBySqlKey("delByMessageId", messageId);
	}
	
	/**
	 * 根据消息ID删除回复
	 * @param messageId
	 */
	public int delReplyByMsgIds(Long[] messageIds){
		int delCount = 0;
		if(BeanUtils.isEmpty(messageIds))
			return 0;
		for(Long messageId:messageIds){
			int i=delReplyByMsgId(messageId);
			delCount+=i;
		}
		return delCount;
	}
}