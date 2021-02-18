package com.cssrc.ibms.core.msg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.jms.intf.IMessageReceiverService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.msg.dao.MessageReadDao;
import com.cssrc.ibms.core.msg.dao.MessageReceiverDao;
import com.cssrc.ibms.core.msg.dao.MessageReplyDao;
import com.cssrc.ibms.core.msg.dao.MessageSendDao;
import com.cssrc.ibms.core.msg.model.MessageReceiver;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:消息接收者 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class MessageReceiverService extends BaseService<MessageReceiver> implements IMessageReceiverService
{
	@Resource
	private MessageReceiverDao dao;
	@Resource
	private MessageReceiverDao redao;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private MessageSendDao messageSendDao;
	@Resource
	private MessageReplyDao messageReplyDao;
	@Resource
	private MessageReadDao messageReadDao;
	@Resource
	private MessageReadService readService;
	
	public MessageReceiverService()
	{
	}
	
	@Override
	protected IEntityDao<MessageReceiver, Long> getEntityDao() 
	{
		return dao;
	}
	
	
	public List<MessageReceiver> getByMessageId(Long messageId){
		List<MessageReceiver> messageReceivers=null;
		messageReceivers=dao.getMessageReceiverList(messageId);
		return messageReceivers;
	}
	
	
	
	/**
	 * 查询某个用户的接收消息
	 * @param queryFilter
	 * @return
	 */
	public List<Map> getMessageReadReply(Long messageId)
	{
		String path="";
		List<Map> list= new ArrayList<Map>();
		List<Map> listByUser= new ArrayList<Map>();
		List<Map> listByOrg= new ArrayList<Map>();
		List<MessageReceiver> reList=redao.getMessageReceiverList(messageId);
		if(reList==null&&reList.size()==0) return null;
		for(MessageReceiver reModel: reList)
		{
			if(reModel.getReceiveType().equals(MessageReceiver.TYPE_USER))//收信的对象是用户
			{
				listByUser=dao.getReadReplyByUser(messageId);
				list.addAll(listByUser);
			}
			else//收信的对象是组织
			{
				ISysOrg sysOrg=sysOrgService.getById(reModel.getReceiverId());
				if(sysOrg==null) continue;
					path=sysOrg.getPath();
					if(StringUtil.isEmpty(path)) continue;						
						listByOrg=dao.getReadReplyByPath(messageId,path);
						list.addAll(listByOrg);
			}
			
		}	
		return list;
	}
	
	/**
	 * 根据消息者Id,删除消息者信息。
	 * 当要删除的消息者是相应消息的最后一个末删除的消息者，将级联删除所有与相应消息相关的消息数据，
	 * 包括消息发送、消息回复、消息已读。
	 * @param ids 消息者ID
	 */
	public void delByIdsCascade(Long[] ids){
		if(BeanUtils.isEmpty(ids))
			return;
		for (Long id : ids){
			MessageReceiver receiver = getById(id);
			Long messageId=receiver.getMessageId();
			int count = dao.getCountByMsgId(messageId);
			if(count==1){
				messageReplyDao.delReplyByMsgId(messageId);
				messageReadDao.delReadByMsgId(messageId);
				delById(id);
				messageSendDao.delById(messageId);
			}else{
				delById(id);
			}
		}
	}
	
	/**
	 * 标记为已读
	 * @param ids
	 * @throws Exception 
	 */
	public void updateReadStatus(Long[] ids) throws Exception{
		if(ids.length==0)return;
		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
		for(Long id : ids){
			if(id>0){
				MessageReceiver receiver = dao.getById(id);
				if(BeanUtils.isEmpty(receiver)) continue;
				readService.addMessageRead(receiver.getMessageId(), sysUser);
			}
		}
	}
}
