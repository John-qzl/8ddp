package com.cssrc.ibms.core.resources.util.listener;

import org.springframework.context.ApplicationEvent;

public abstract class BaseEvent<T> extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private String message;
	
	
	
	public BaseEvent(Object source,String message) {
		super(source);
		this.message=message;
	}

	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}
	

}
