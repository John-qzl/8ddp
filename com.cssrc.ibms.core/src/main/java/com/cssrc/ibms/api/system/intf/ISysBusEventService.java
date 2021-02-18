package com.cssrc.ibms.api.system.intf;

import com.cssrc.ibms.api.system.model.ISysBusEvent;

public interface ISysBusEventService{


	public abstract ISysBusEvent getSysBusEvent(String json);


	public abstract ISysBusEvent getByFormKey(String formKey);


	public abstract void importSysBusEvent(ISysBusEvent sysBusEvent,
			Long formKey);
	
    public  Class<?extends ISysBusEvent> getBusClass();

}