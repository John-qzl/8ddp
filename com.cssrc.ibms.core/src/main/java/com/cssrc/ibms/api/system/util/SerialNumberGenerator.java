package com.cssrc.ibms.api.system.util;

import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.core.db.datakey.IKeyGenerator;
import com.cssrc.ibms.core.util.appconf.AppUtil;


public class SerialNumberGenerator implements IKeyGenerator {
	
	private String alias="";

	@Override
	public Object nextId() throws Exception {
		ISerialNumberService serailService=(ISerialNumberService)AppUtil.getBean(ISerialNumberService.class);
		return serailService.nextId(alias);
		 
	}

	/**
	 * 设置别名。
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	 

}