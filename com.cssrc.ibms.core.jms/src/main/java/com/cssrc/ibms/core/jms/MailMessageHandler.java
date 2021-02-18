package com.cssrc.ibms.core.jms;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.jms.model.IMessageModel;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.mail.model.Mail;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.singlelogin.IbmsSinglePasswordEncoder;

public class MailMessageHandler implements IMessageHandler
{
    
    private final Log logger = LogFactory.getLog(MailMessageHandler.class);
    
    @Resource
    private MessageEngine messageEngine;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Override
    public String getTitle()
    {
        return "邮件";
    }
    
    // by yangbo
    @Override
    public void handMessage(IMessageModel model)
    {
        Mail mail = new Mail();
        String subject = getSubject(model);
        String content = getContent(model);
        mail.setSubject(subject);
        mail.setContent(content);
        String[] toAddress = model.getTo();
        String[] bcc = model.getBcc();
        String[] cc = model.getCc();
        
        /* 外部邮箱 */
        if ((model.getTo() != null) && (model.getTo().length > 0))
        {
            mail.setReceiverAddresses(StringUtils.join(toAddress, ","));
            if (BeanUtils.isNotEmpty(bcc))
                mail.setBcCAddresses(StringUtils.join(bcc, ","));
            if (BeanUtils.isNotEmpty(cc))
                mail.setCopyToAddresses(StringUtils.join(cc, ","));
        }
        else
        {
            String eamilStr = "";
            if (model.getReceiveUser() != null)
            {
                Long rid = model.getReceiveUser()[0];
                ISysUser user = sysUserService.getById(rid);
                eamilStr = user.getEmail();
                
            }
            if ((StringUtil.isEmpty(eamilStr)) || (!StringUtil.isEmail(eamilStr)))
                return;
            mail.setReceiverAddresses(eamilStr);
        }
        this.messageEngine.sendMail(mail);
        this.logger.debug("MailModel");
    }
    
    /**
     * 获取邮件的模版内容
     */
    private String getTemplate(IMessageModel model)
    {
        String mailTemplate = "";
        try
        {
            mailTemplate = model.getTemplateMap().get(ISysTemplate.TEMPLATE_TYPE_HTML);
            mailTemplate = StringUtil.jsonUnescape(mailTemplate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return mailTemplate;
    }
    
    private String getSubject(IMessageModel model)
    {
        if (model.getTemplateMap() == null)
            return model.getSubject();
        String title = model.getTemplateMap().get(ISysTemplate.TEMPLATE_TITLE);
        Long rid = model.getReceiveUser()[0];
        ISysUser user = sysUserService.getById(rid);
        ISysUser sendUser = sysUserService.getById(model.getSendUser());
        
        title = MsgUtil
            .replaceTitleTag(title, user.getFullname(), sendUser.getFullname(), model.getSubject(), model.getOpinion());
        title = MsgUtil.replaceVars(title, model.getVars());
        return title;
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
        String url = "";
        if (BeanUtils.isNotEmpty(model.getExtId()))
        {
            IbmsSinglePasswordEncoder singlePasswordEncoder=AppUtil.getBean(IbmsSinglePasswordEncoder.class);
            String param="taskId="+model.getExtId()+"&userId="+model.getReceiveUser()[0];
            param=singlePasswordEncoder.getSignParam(param);
            url = PropertyUtil.getUrl(model.getExtId().toString(), model.getIsTask());
            url+="?"+param;
        }
        Long rid = model.getReceiveUser()[0];
        ISysUser user = sysUserService.getById(rid);
        content = MsgUtil.replaceTemplateTag(this.getTemplate(model),
            user.getFullname(),
            sendUserName,
            model.getSubject(),
            url,
            model.getOpinion(),
            false);
        content = MsgUtil.replaceVars(content, model.getVars());
        return content;
    }
    
    /**
     * 默认不勾选邮件
     */
    @Override
    public boolean getIsDefaultChecked()
    {
        return false;
    }
    
}
