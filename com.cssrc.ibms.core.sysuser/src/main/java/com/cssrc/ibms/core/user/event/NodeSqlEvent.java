package com.cssrc.ibms.core.user.event;

import org.springframework.context.ApplicationEvent;
/**
 * 
 * <p>Title:NodeSqlEvent</p>
 * @author Yangbo 
 * @date 2016-8-18下午03:51:49
 */
public class NodeSqlEvent extends ApplicationEvent {
	public NodeSqlEvent(NodeSqlContext source) {
		super(source);
	}
}
