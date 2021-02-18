package com.cssrc.ibms.api.jms.intf;

import com.cssrc.ibms.api.jms.model.ISyncModel;


public interface ISimpleMessageProducer {

	void send(String destQueue,ISyncModel model);

}
