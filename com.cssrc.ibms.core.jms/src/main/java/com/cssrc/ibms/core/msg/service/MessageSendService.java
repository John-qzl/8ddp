package com.cssrc.ibms.core.msg.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.jms.model.IMessageModel;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.msg.dao.MessageReceiverDao;
import com.cssrc.ibms.core.msg.dao.MessageSendDao;
import com.cssrc.ibms.core.msg.model.MessageReceiver;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 发送消息 Service类.
 *
 * <p>detailed comment</p>
 * @author [创建人]  zhulongchao <br/> 
 * 		   [创建时间] 2015-3-18 下午04:43:19 <br/>  
 * 		   [修改时间] 2015-3-18 下午04:43:19
 * @see
 */
@Service
public class MessageSendService extends BaseService<MessageSend> implements IMessageSendService
{
    @Resource
    private MessageSendDao dao;
    
    @Resource
    private MessageReceiverDao messageReceiverDao;
    
    @Resource
    private ISysUserService sysUserService;
    
    public MessageSendService()
    {
    }
    
    @Override
    protected IEntityDao<MessageSend, Long> getEntityDao()
    {
        return dao;
    }
    
    /**
     * 查询某个用户的接收消息
     * 
     * @param queryFilter
     * @return
     */
    public List<MessageSend> getReceiverByUser(QueryFilter queryFilter)
    {
        return dao.getReceiverByUser(queryFilter);
    }
    
    /**
     * 获取个人未读信息
     * @param userId
     * @return
     */
    public List<MessageSend> getNotReadMsg(Long receiverId)
    {
        
        return dao.getNotReadMsg(receiverId);
    }
    
    /**
     * 插入MessageSend数据，插入MessageReceiver数据
     * 
     * @param messageSend
     * @param curUser
     * @param receiverOrgName
     * @throws Exception
     */
    public void addMessageSend(MessageSend messageSend, ISysUser curUser, String receiverId, String receiverName,
        String receiverOrgId, String receiverOrgName)
        throws Exception
    {
        
        // 插入MessageSend
        if (receiverOrgName.length() > 0 && receiverName.length() > 0)
        {
            messageSend.setReceiverName(receiverName + "," + receiverOrgName);
        }
        
        Long messageId = null;
        if (messageSend.getId() == null)
        {
            messageId = UniqueIdUtil.genId();
            messageSend.setId(messageId);
            messageSend.setUserId(curUser.getUserId());
            messageSend.setUserName(curUser.getFullname());
            Date now = new Date();
            messageSend.setSendTime(now);
            add(messageSend);
        }
        else
        {
            messageId = messageSend.getId();
            update(messageSend);
        }
        
        String[] idArr = receiverId.split(",");
        String[] nameArr = receiverName.split(",");
        String[] orgIdArr = receiverOrgId.split(",");
        String[] orgNameArr = receiverOrgName.split(",");
        
        // 插入MessageReceiver
        MessageReceiver messageReceiver = null;
        if (receiverId.length() > 0)
        {
            for (int i = 0; i < idArr.length; i++)
            {
                messageReceiver = new MessageReceiver();
                messageReceiver.setId(UniqueIdUtil.genId());
                messageReceiver.setMessageId(messageId);
                if (StringUtil.isNotEmpty(idArr[i]))
                {
                    messageReceiver.setReceiverId(Long.parseLong(idArr[i]));
                    if (nameArr.length > i)
                        messageReceiver.setReceiver(nameArr[i]);
                    messageReceiver.setReceiveType(new Short("0"));
                }
                messageReceiverDao.add(messageReceiver);
            }
        }
        
        if (receiverOrgId.length() > 0)
        {
            for (int i = 0; i < orgIdArr.length; i++)
            {
                messageReceiver = new MessageReceiver();
                messageReceiver.setId(UniqueIdUtil.genId());
                messageReceiver.setMessageId(messageId);
                if (StringUtil.isNotEmpty(orgIdArr[i]))
                {
                    messageReceiver.setReceiverId(Long.parseLong(orgIdArr[i]));
                    if (orgNameArr.length > i)
                        messageReceiver.setReceiver(orgNameArr[i]);
                    messageReceiver.setReceiveType(MessageReceiver.TYPE_ORG);
                }
                messageReceiverDao.add(messageReceiver);
            }
        }
    }
    
    /**
     * 添加只有一个接受者的站内消息
     * @param messageSend
     * @throws Exception
     */
    public void addMessageSend(MessageSend messageSend)
        throws Exception
    {
        dao.add(messageSend);
        MessageReceiver receiver = new MessageReceiver();
        receiver.setCreateBy(messageSend.getCreateBy());
        receiver.setCreatetime(new Date());
        receiver.setId(UniqueIdUtil.genId());
        receiver.setMessageId(messageSend.getId());
        receiver.setReceiverId(messageSend.getRid());
        receiver.setReceiveType(MessageReceiver.TYPE_USER);
        receiver.setReceiver(messageSend.getReceiverName());
        messageReceiverDao.add(receiver);
    }
    
    @Override
    public void addMessageSend(IMessageSend messageSend)
    {
        this.add((MessageSend)messageSend);
        MessageReceiver receiver = new MessageReceiver();
        receiver.setCreateBy(messageSend.getCreateBy());
        receiver.setCreatetime(new Date());
        receiver.setId(UniqueIdUtil.genId());
        receiver.setMessageId(messageSend.getId());
        receiver.setReceiverId(messageSend.getRid());
        receiver.setReceiveType(MessageReceiver.TYPE_USER);
        receiver.setReceiver(messageSend.getReceiverName());
        messageReceiverDao.add(receiver);
    }
    
    @Override
    public IMessageSend getMessageSend(IMessageModel model)
    {
        MessageSend messageSend = new MessageSend();
        if (model.getReceiveUser() == null || model.getSendUser() == null)
        {
            return null;
        }
        messageSend.setId(UniqueIdUtil.genId());
        messageSend.setUserName(this.getSendUserName(model));// 发送人姓名
        messageSend.setUserId(model.getSendUser());// 发送人ID
        messageSend.setSendTime(model.getSendDate());
        messageSend.setMessageType(MessageSend.MESSAGETYPE_FLOWTASK);
        messageSend.setContent(this.getContent(model));
        messageSend.setSubject(this.getSubject(model));
        messageSend.setCreateBy(model.getSendUser());// 创建人ID
        messageSend.setRid(model.getReceiveUser()[0]);// 接收人ID
        Long rid = model.getReceiveUser()[0];
        ISysUser user = sysUserService.getById(rid);
        messageSend.setReceiverName(user.getFullname());// 接收人姓名
        messageSend.setCreatetime(model.getSendDate() == null ? new Date() : model.getSendDate());
        return messageSend;
    }
    
    /**
     * 获取短信的模版内容
     */
    private String getTemplate(IMessageModel model)
    {
        String smsTemplate = "";
        try
        {
            smsTemplate = model.getTemplateMap().get(ISysTemplate.TEMPLATE_TYPE_HTML);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return smsTemplate;
    }
    
    private String getSendUserName(IMessageModel model)
    {
        String sendUserName = "";
        if (model.getSendUser() == null)
        {
            sendUserName = "系统消息";
        }
        else
        {
            ISysUser user = sysUserService.getById(model.getSendUser());
            sendUserName = user.getFullname();
        }
        return sendUserName;
    }
    
    /**
     * 内容要用模版进行替换，
     * 如果有模版
     */
    private String getContent(IMessageModel model)
    {
        String content = "";
        if (model.getTemplateMap() == null)
            return model.getContent();
        
        Long id = model.getExtId();// 流程运行ID或任务ID（由isTask决定）
        boolean isTask = model.getIsTask(); // 是否是任务（taskId 是true,runId 是false）
        // 链接地址
        String url = "";
        if (BeanUtils.isNotEmpty(id))
        {
            url = PropertyUtil.getUrl(id.toString(), isTask);
        }
        if (StringUtil.isNotEmpty(url))
        {
            url = url.replace("http://", "");
            url = url.substring(url.indexOf("/"), url.length());
        }
        
        // 发送内容
        Long rid = model.getReceiveUser()[0];
        ISysUser user = sysUserService.getById(rid);
        content = MsgUtil.replaceTemplateTag(this.getTemplate(model),
            user.getFullname(),
            this.getSendUserName(model),
            model.getSubject(),
            url,
            model.getOpinion(),
            false);
        
        return content;
    }
    
    private String getSubject(IMessageModel model)
    {
        if (model.getTemplateMap() == null)
            return model.getSubject();
        // 发送标题
        Long rid = model.getReceiveUser()[0];
        ISysUser user = sysUserService.getById(rid);
        String title = MsgUtil.replaceTitleTag(model.getTemplateMap().get(ISysTemplate.TEMPLATE_TITLE),
            user.getFullname(),
            this.getSendUserName(model),
            model.getSubject(),
            model.getOpinion());
        
        return title;
    }
    
    @Override
    public List getNotReadMsgByUserId(long longValue, PagingBean pb)
    {
        return this.dao.getNotReadMsgByUserId(longValue, pb);
    }
    
    /**
     * 获取内部消息数量
     *@author YangBo @date 2016年12月2日上午10:21:38
     *@param userId
     *@return
     */
    public Integer getCountReceiverByUser(Long userId)
    {
        return this.dao.getCountNotReadMsg(userId);
    }
    
    /**
     * 未读消息个数
     *@author YangBo @date 2016年12月2日上午10:23:33
     *@param userId
     *@return
     */
    public Integer getCountNotReadMsg(Long userId)
    {
        return this.dao.getCountNotReadMsg(userId);
    }
    
}
