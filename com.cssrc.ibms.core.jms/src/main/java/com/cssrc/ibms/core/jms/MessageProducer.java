package com.cssrc.ibms.core.jms;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;

/**
 * 用于发送jms消息。 <br/>
 * 发送邮件，短信，内部消息。
 * 
 * @author zhulongchao
 *
 */
public class MessageProducer implements IMessageProducer, BeanPostProcessor
{
    private static final Log logger = LogFactory.getLog(MessageProducer.class);
    
    @Resource
    ISysParameterService sysParameterService;
    
    private JmsTemplate jmsQueueTemplate;
    
    private JmsTemplate jmsTopicTemplate;
    
    public MessageProducer()
    {
        
    }
    
    public void send(Object model)
    {
        if (jmsQueueTemplate == null)
        {
            logger.warn(" jmsQueueTemplate si null");
            return;
        }
        String massageQueue = AppConfigUtil.get("pluginproperties", massage_queue);
        logger.debug("procduce the message:" + massageQueue);
        // 产生邮件\短信\消息发送的消息，加到消息队列中去
        jmsQueueTemplate.convertAndSend(massageQueue, model);
    }
    
    public void send(Object model, String queueName)
    {
        if (jmsQueueTemplate == null)
        {
            logger.warn(" jmsQueueTemplate si null");
            return;
        }
        logger.debug("procduce the message:" + queueName);
        // 产生邮件\短信\消息发送的消息，加到消息队列中去
        
        jmsQueueTemplate.convertAndSend(queueName, model);
    }
    
    public void sendMdm(Object model)
    {
        if (jmsQueueTemplate == null)
        {
            logger.warn(" jmsQueueTemplate si null");
            return;
        }
        String massageQueue = AppConfigUtil.get("pluginproperties", massage_queue);
        if (this.isSyncMdm(model))
        {
            // 增加系统参数 是否开启用户数据同步功能
            jmsQueueTemplate.convertAndSend(massageQueue, model);
        }
    }
    
    /**
     * 发送同步数据订阅消息
     * 
     * @param topicName
     * @param msg
     */
    public void sendTopic(final Serializable data)
    {
        if (jmsTopicTemplate == null)
        {
            logger.warn(" jmsTopicTemplate si null");
            return;
        }
        String syncdataTopic = AppConfigUtil.get("pluginproperties", syncdata_topic);
        logger.debug("procduce the " + syncdataTopic + " topic message");
        
        jmsTopicTemplate.send(syncdataTopic, new MessageCreator()
        {
            @Override
            public Message createMessage(Session session)
                throws JMSException
            {
                return session.createObjectMessage(data);
            }
            
        });
    }
    
    /**
     * 发送同步数据订阅消息
     * 
     * @param topicName
     * @param msg
     */
    public void sendTopic(final Serializable data, String topicName)
    {
        
        if (jmsTopicTemplate == null)
        {
            logger.warn(" jmsTopicTemplate si null");
            return;
        }
        logger.debug("procduce the " + topicName + " topic message");
        
        jmsTopicTemplate.send(topicName, new MessageCreator()
        {
            @Override
            public Message createMessage(Session session)
                throws JMSException
            {
                return session.createObjectMessage(data);
            }
            
        });
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException
    {
        boolean isJmsTemplate = BeanUtils.isInherit(bean.getClass(), JmsTemplate.class);
        if ("jmsQueueTemplate".equals(beanName) && isJmsTemplate)
        {
            this.jmsQueueTemplate = (JmsTemplate)bean;
        }
        if ("jmsTopicTemplate".equals(beanName) && isJmsTemplate)
        {
            this.jmsTopicTemplate = (JmsTemplate)bean;
        }
        return bean;
    }
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException
    {
        return bean;
    }
    
    
    
    public boolean isSyncMdm(Object model)
    {
        if (model instanceof ISysUser)
        {
            // 增加系统参数 是否开启用户数据同步功能
            return sysParameterService.getIntByAlias("mdm.user.sync", 0) == 1 ? true : false;
        }
        else if (model instanceof ISysRole)
        {
            // 增加系统参数 是否开启角色数据同步功能
            return sysParameterService.getIntByAlias("mdm.role.sync", 0) == 1 ? true : false;
            
        }
        else if (model instanceof IPosition)
        {
            // 增加系统参数 是否开启岗位数据同步功能
            return sysParameterService.getIntByAlias("mdm.pos.sync", 0) == 1 ? true : false;
            
        }
        else if (model instanceof ISysOrg)
        {
            // 增加系统参数 是否开启用户数据同步功能
            return sysParameterService.getIntByAlias("mdm.org.sync", 0) == 1 ? true : false;
            
        }
        else if (model instanceof IUserPosition)
        {
            // 增加系统参数 是否开启用户数据同步功能
            return sysParameterService.getIntByAlias("mdm.userpos.sync", 0) == 1 ? true : false;
            
        }
        return false;
    }
    
}
