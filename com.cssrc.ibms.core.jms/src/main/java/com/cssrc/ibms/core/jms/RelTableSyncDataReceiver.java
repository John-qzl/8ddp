package com.cssrc.ibms.core.jms;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssrc.ibms.api.form.intf.IFormDataSyncService;

public class RelTableSyncDataReceiver implements MessageListener
{
    @Resource
    IFormDataSyncService formDataSyncService;
    
    private static final Log logger = LogFactory.getLog(MessageProducer.class);

    @Override
    public void onMessage(Message msg)
    {
        try
        {
            logger.info("recive topic："+msg.getJMSMessageID());
            ActiveMQObjectMessage mqmsg=(ActiveMQObjectMessage)msg;
            Object object=mqmsg.getObject();
            if(object instanceof HashMap){
                Map<String,Object> data=(HashMap<String,Object>)object;
                formDataSyncService.handelData(data);
            }
           
        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
        
    }
    
}
