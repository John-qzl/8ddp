package com.cssrc.ibms.core.jms;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.core.jms.intf.IJmsHandler;
import com.cssrc.ibms.core.util.bean.BeanUtils;

/**
 * 从消息队列中读取对象，并且进行消息发送。
 * 
 * @author zxh
 * 
 */
public class MessageConsumer {

	/**
	 * 处理消息
	 */
	private Map<String, IJmsHandler> handlers = new HashMap<String, IJmsHandler>();
	
	protected Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
	public void setHandlers(Map<String, IJmsHandler> handlers) {
		this.handlers = handlers;
	}

	/**
	 * 发送消息
	 * 
	 * @param model
	 *            发送的对象
	 * @throws Exception
	 */
	public void sendMessage(Object model) throws Exception {
		if (BeanUtils.isNotEmpty(handlers) && BeanUtils.isNotEmpty(model)) {
		    /* 
		     * 所有的消息具体处理类都配置在 app-jm.xml中
		     * 通过消息model作为key,动态获取具体的消息处理类，处理类必须实现IJmsHandler接口
		     * */
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

