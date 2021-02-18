package com.cssrc.ibms.core.user.listener;


import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.core.user.event.UserEvent;

public class UserEventListener implements ApplicationListener<UserEvent>
{
    protected Logger logger = LoggerFactory.getLogger(UserEventListener.class);
    @Resource
    private IMessageProducer messageProducer;
    public  String queueName;
    
    public void onApplicationEvent(UserEvent event)
    {
        logger.info(event.getActionMsg()+" user "+event.getUserId());
        logger.info(event.getActionMsg()+" user "+" send mq");
        //set mq 异步 操作，避免同步用户数据长时间等待
        messageProducer.sendMdm(event.getUser());

    }

    public String getQueueName()
    {
        return queueName;
    }

    public void setQueueName(String queueName)
    {
        this.queueName = queueName;
    }
    
    
    
}
