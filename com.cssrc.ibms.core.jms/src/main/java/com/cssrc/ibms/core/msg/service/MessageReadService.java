package com.cssrc.ibms.core.msg.service;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.msg.dao.MessageReadDao;
import com.cssrc.ibms.core.msg.model.MessageRead;

/**
 * 对象功能:接收状态 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class MessageReadService extends BaseService<MessageRead>
{
	@Resource
	private MessageReadDao dao;
	
	public MessageReadService()
	{
	}
	
	@Override
	protected IEntityDao<MessageRead, Long> getEntityDao() 
	{
		return dao;
	}	
	
	/**
	 * 添加数据MessageRead
	 * @param messageId
	 * @param sysUser
	 * @throws Exception
	 */
	public void addMessageRead(Long messageId, ISysUser sysUser) throws Exception{
		
		MessageRead msgRead = dao.getReadByUser(messageId, sysUser.getUserId());
		if(msgRead==null){
			Date now = new Date();
			MessageRead messageRead = new MessageRead();
			messageRead.setId(UniqueIdUtil.genId());
			messageRead.setMessageId(messageId);
			messageRead.setReceiverId(sysUser.getUserId());
			messageRead.setReceiver(sysUser.getFullname());
			messageRead.setReceiveTime(now);
			add(messageRead);
		}
	}
	
	/**
	 * 获得已读此消息的人员
	 * @param messageId
	 * @return
	 */
	public List<MessageRead> getReadByMsgId(Long messageId)
	{
		return dao.getReadByMsgId(messageId);
	}
	
}
