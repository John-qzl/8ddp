package com.cssrc.ibms.core.jms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.jms.ConnectionFactory;
import javax.jms.Session;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.broker.jmx.DestinationViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.pool.PooledSession;
import org.apache.activemq.web.MessageQuery;
import org.apache.activemq.web.QueueBrowseQuery;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.SessionPool;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * <pre>
 * 对象功能:jms监控 
 * 开发人员:zhulongchao 
 * </pre>
 */
public class QueuesService
{
    protected ConnectionFactory simpleJmsConnectionFactory;
    
    public QueuesService()
    {
        try
        {
            simpleJmsConnectionFactory = AppUtil.getBean("simpleJmsConnectionFactory", ConnectionFactory.class);
        }
        catch (Exception e)
        {
        }
    }
    
    private RemoteJMXBrokerFacade brokerFacade;
    
    private RemoteJMXBrokerFacade getBrokerFacade()
    {
        if (brokerFacade == null)
        {
            brokerFacade = new RemoteJMXBrokerFacade();
            String jmxHostIp = AppConfigUtil.get("jms.ip");
            String connectorPort = AppConfigUtil.get("jms.connectorPort.port");
            System.clearProperty("webconsole.jmx.url");
            System.setProperty("webconsole.jmx.url",
                "service:jmx:rmi:///jndi/rmi://" + jmxHostIp + ":" + connectorPort + "/jmxrmi");
            // 测试服务的用户密码
            /*
             * System.setProperty("webconsole.jmx.user","admin");
             * System.setProperty("webconsole.jmx.password","activemq");
             */
            // 创建配置
            SystemPropertiesConfiguration configuration = new SystemPropertiesConfiguration();
            // 创建链接
            brokerFacade.setConfiguration(configuration);
        }
        return brokerFacade;
    }
    
    /**
     * 根据queueName获取queue相关的信息
     * @param name
     * @return
     * @throws Exception
     */
    private QueueViewMBean getQueue(String name)
        throws Exception
    {
        QueueViewMBean qvb = (QueueViewMBean)getDestinationByName(getQueues(), name);
        return qvb;
    }
    
    /**
     * 从队列集合中获取与name匹配的队列
     * @param collection
     * @param name
     * @return
     */
    private DestinationViewMBean getDestinationByName(Collection<Object> collection, String name)
    {
        Iterator<Object> iter = collection.iterator();
        while (iter.hasNext())
        {
            Object obj = iter.next();
            if (obj instanceof DestinationViewMBean)
            {
                DestinationViewMBean destinationViewMBean = (DestinationViewMBean)obj;
                if (name.equals(destinationViewMBean.getName()))
                {
                    return destinationViewMBean;
                }
            }
            else
            {
                
            }
        }
        return null;
    }
    
    /**
     * 获取连接session
     * @return
     * @throws Exception
     */
    private SessionPool getSessionPool()
        throws Exception
    {
        if (simpleJmsConnectionFactory == null)
        {
            return null;
        }
        SessionPool sp = new SessionPool();
        sp.setConnectionFactory(simpleJmsConnectionFactory);
        sp.setConnection(sp.getConnection());
        Session session = sp.borrowSession();
        ActiveMQSession am = null;
        if (session instanceof ActiveMQSession)
        {
            am = (ActiveMQSession)(session);
        }
        if (session instanceof PooledSession)
        {
            PooledSession pooledSession = (PooledSession)session;
            am = pooledSession.getInternalSession();
        }
        sp.returnSession(am);
        return sp;
    }
    
    public Collection<Object> getQueues()
        throws Exception
    {
        try
        {
            Collection<Object> result = new ArrayList<Object>();
            RemoteJMXBrokerFacade brokerFacade = getBrokerFacade();
            Collection<QueueViewMBean> qs = brokerFacade.getQueues();
            Collection<TopicViewMBean> topics = brokerFacade.getTopics();
            result.addAll(qs);
            result.addAll(topics);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 清空队列
     * @param JMSDestination
     * @throws Exception
     */
    public void purgeDestination(String JMSDestination)
        throws Exception
    {
        try
        {
            ActiveMQDestination destination =
                ActiveMQDestination.createDestination(JMSDestination, ActiveMQDestination.QUEUE_TYPE);
            getBrokerFacade().purgeQueue(destination);
        }
        catch (Exception e)
        {
        }
        
    }
    
    /**
     * 删除消息
     * @param JMSDestination
     * @param messageId
     * @throws Exception
     */
    public void removeMessage(String JMSDestination, String messageId)
        throws Exception
    {
        QueueViewMBean queueView = getQueue(JMSDestination);
        queueView.removeMessage(messageId);
    }
    
    /**
     * 查看队列中的未消费消息
     * @param JMSDestination
     * @return
     * @throws Exception
     */
    public QueueBrowseQuery getQueueBrowseQuery(String JMSDestination)
        throws Exception
    {
        try
        {
            QueueBrowseQuery qbq = new QueueBrowseQuery(getBrokerFacade(), getSessionPool());
            qbq.setJMSDestination(JMSDestination);
            return qbq;
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
    
    /**
     * 查看消息详情
     * @param id
     * @param JMSDestination
     * @return
     * @throws Exception
     */
    public MessageQuery getMessageQuery(String id, String JMSDestination)
        throws Exception
    {
        try
        {
            MessageQuery mq = new MessageQuery(getBrokerFacade(), getSessionPool());
            mq.setJMSDestination(JMSDestination);
            mq.setId(id);
            return mq;
            
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
    
    /**
     * 删除队列
     * @param JMSDestination
     * @throws Exception
     */
    public void removeDestinationQueue(String JMSDestination)
        throws Exception
    {
        try
        {
            getBrokerFacade().getBrokerAdmin().removeQueue(JMSDestination);
        }
        catch (Exception e)
        {
        }
        
    }
}
