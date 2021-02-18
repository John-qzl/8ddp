package com.cssrc.ibms.core.jms;

import javax.annotation.Resource;

import com.cssrc.ibms.api.jms.intf.IMessageHandler;
import com.cssrc.ibms.api.jms.model.MessageModel;
import com.cssrc.ibms.core.jms.intf.IJmsHandler;


public class MessageHandler implements IJmsHandler {
	
	@Resource
	MessageHandlerContainer messageHandlerContainer;

	@Override
	public void handMessage(Object model) {
		MessageModel msgModel=(MessageModel)model;
		String type=msgModel.getInformType();
		IMessageHandler handler= messageHandlerContainer.getHandler(type);
		handler.handMessage(msgModel);

	}

}