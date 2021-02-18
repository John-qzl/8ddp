package com.cssrc.ibms.core.msg.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.core.jms.intf.IJmsHandler;
import com.cssrc.ibms.core.util.bean.BeanUtils;


public class BlobMessageConsumer {
	private Map<String, IJmsHandler> handlers = new HashMap<String, IJmsHandler>();

	protected Logger logger = LoggerFactory.getLogger(BlobMessageConsumer.class);
	public void setHandlers(Map<String, IJmsHandler> handlers) {
		this.handlers = handlers;
	}
	public void processMessage(Object model) throws Exception {
		if (BeanUtils.isNotEmpty(handlers) && BeanUtils.isNotEmpty(model)) {
			IJmsHandler jmsHandler = handlers.get(model.getClass().getName());
			if(jmsHandler!=null){
				jmsHandler.handMessage(model);
			}
			else{
				logger.info(model.toString());
			}
		} else {
			throw new Exception("Object:[" + model + "] is not  entity Object ");
		}
	}
}
