package com.cssrc.ibms.core.msg.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.msg.dao.MessageReplyDao;
import com.cssrc.ibms.core.msg.model.MessageReply;

/**
 * 对象功能:消息回复 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class MessageReplyService extends BaseService<MessageReply>
{
	@Resource
	private MessageReplyDao dao;
	
	public MessageReplyService()
	{
	}
	
	@Override
	protected IEntityDao<MessageReply, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 保存 MessageReply
	 * @param messageReply
	 * @param sysUser
	 */
	public void saveReply(MessageReply messageReply, ISysUser sysUser) throws Exception{
		messageReply.setId(UniqueIdUtil.genId());
		messageReply.setReplyId(sysUser.getUserId());
		messageReply.setReply(sysUser.getFullname());
		Date now = new Date();
		messageReply.setReplyTime(now);
		add(messageReply);
	}
	
	/**
	 * 获得已回复此消息的人员
	 * @param messageId
	 * @return
	 */
	public List<MessageReply> getReplyByMsgId(Long messageId)
	{
		return dao.getReplyByMsgId(messageId);
	}
	
}
