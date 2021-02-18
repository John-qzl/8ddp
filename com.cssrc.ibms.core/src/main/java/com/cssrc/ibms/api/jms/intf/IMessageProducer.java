package com.cssrc.ibms.api.jms.intf;

import java.io.Serializable;

public interface IMessageProducer {
    public static String massage_queue="jms.massage.queue";
    public static String syncdata_topic="jms.massage.topic.syncreldata";
    public static String massage_queue_syncmdm="jms.massage.queue.syncmdm";

	public void send(Object model);
	
	public void send(Object model, String queueName);
	
	/**
	 * 发布同步数据订阅消息
	 * @param model
	 */
	public void sendTopic(final Serializable data);
	
	/**
	 * 发布同步数据订阅消息
	 * @param model
	 */
	public void sendTopic(final Serializable data, String topicName);
	
	
	/**
	 * 发送主数据 用户 组织 角色 岗位 数据同步消息
	 * @param model
	 */
	public void sendMdm(Object model);
}
