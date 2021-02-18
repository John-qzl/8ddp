package com.cssrc.ibms.core.jms;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.jms.model.IMessageModel;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.msg.model.MessageSend;
import com.cssrc.ibms.core.util.msg.MsgUtil;

public class RtxMessageHandler implements IMessageHandler
{
    
    private final Log logger = LogFactory.getLog(RtxMessageHandler.class);
    
    @Resource
    private MessageEngine messageEngine;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Override
    public String getTitle()
    {
        return "RTX消息";
    }
    
    @Override
    public void handMessage(IMessageModel model)
    {
        MessageSend messageSend = new MessageSend();
        
        if (model.getReceiveUser() == null || model.getSendUser() == null)
            return;
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
        messageSend.setReceiverName(getReceiveUserName(user));// 接收人姓名
        messageSend.setCreatetime(model.getSendDate() == null ? new Date() : model.getSendDate());
        messageEngine.sendRtxMessage(messageSend);
        logger.debug("sendRtxMessage");
    }
    
    /**
     * 取得接收人的账户
     */
    private String getReceiveUserName(ISysUser receiveUser)
    {
        // 判断配置的接收人账户类型
        if (SysConfConstant.RTX_RECEIVE_TYPE == 0)
        {
            // 取得接收人的账号
            return receiveUser.getUsername();
        }
        else
        {
            // 其他情况取得接收人的中文名称
            return receiveUser.getFullname();
        }
        
    }
    
    /**
     * 获取短信的模版内容
     */
    private String getTemplate(IMessageModel model)
    {
        String smsTemplate = "";
        try
        {
            smsTemplate = model.getTemplateMap().get(ISysTemplate.TEMPLATE_TYPE_PLAIN);
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
        
        // 发送内容
        Long rid = model.getReceiveUser()[0];
        ISysUser user = sysUserService.getById(rid);
        content = MsgUtil.replaceTemplateTag(this.getTemplate(
            model), user.getFullname(), this.getSendUserName(model), model.getSubject(), "", model.getOpinion(), false);
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
    public boolean getIsDefaultChecked()
    {
        return true;
    }
    
}
