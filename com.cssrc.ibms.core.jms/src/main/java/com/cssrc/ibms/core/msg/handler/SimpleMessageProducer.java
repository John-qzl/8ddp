package com.cssrc.ibms.core.msg.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.form.model.SyncConstant;
import com.cssrc.ibms.api.jms.intf.ISimpleMessageProducer;
import com.cssrc.ibms.api.jms.model.ISyncModel;

public class SimpleMessageProducer implements ISimpleMessageProducer{
	@Resource
	private JmsTemplate remoteJmsTemplate;
	private Map<String, String> queues = new HashMap<String, String>();
	
	
	public void setQueues(Map<String, String> queues) {
		this.queues = queues;
	}
 
	
	public void send(String destQueue,ISyncModel model){
		final String json = converSyncModelToText(model);
		remoteJmsTemplate.send(queues.get(destQueue), new MessageCreator(){
			@Override
			public Message createMessage(Session s) throws JMSException { 			 
				TextMessage tm = s.createTextMessage(json);    		
				return tm;
			}
			
		});
	}
	
	private String converSyncModelToText(ISyncModel syncModel){
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String, Object> mainfields = (Map<String, Object>)syncModel.getMain().get("fields");
		map.put(SyncConstant.MainTable, ITableModel.CUSTOMER_TABLE_PREFIX+syncModel.getMainTableName());
		map.put(SyncConstant.c_id_from, 
				mainfields.get(SyncConstant.id_from));
		map.put(SyncConstant.c_place_from, 
				mainfields.get(SyncConstant.place_from));
		map.put(SyncConstant.c_id_to.toUpperCase(), 
				mainfields.get(SyncConstant.id_to));
		JSONObject jsonObj = JSONObject.fromObject(map);
		return jsonObj.toString();
	}
	
}
