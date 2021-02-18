package com.cssrc.ibms.core.msg.handler;

import com.cssrc.ibms.api.form.intf.ISyncFormJmsWebService;
import com.cssrc.ibms.core.jms.intf.IJmsHandler;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.CommonTools;


public class SimpleMessageHandler implements IJmsHandler{

	@Override
	public void handMessage(Object model) {   
			String jsonData = CommonTools.Obj2String(model);
			ISyncFormJmsWebService syncFormWebService = AppUtil.getBean(ISyncFormJmsWebService.class);
			syncFormWebService.updateIDTO(jsonData);
		 
		
	}

}
