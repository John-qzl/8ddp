package com.cssrc.ibms.core.jms.factory;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;
import javax.jms.ConnectionFactory;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import org.springframework.jms.connection.CachingConnectionFactory;

/**
 * MQ的clientID按照约定格式进行自动生成
 * 
 * @author liubo
 * @date 2017/07/11
 */
public class MessageConnectionFactory extends CachingConnectionFactory {

	public MessageConnectionFactory() {
		super();
		setReconnectOnException(true);
	}

	public MessageConnectionFactory(ConnectionFactory targetConnectionFactory) {
		super(targetConnectionFactory);
		setReconnectOnException(true);
	}

	@Override
	public void setClientId(String clientId) {
		try {
			//获取当前系统IP地址
			InetAddress inet = InetAddress.getLocalHost();
			String ip = inet.getHostAddress();
			//获取正在使用的tomcat端口号
			String port = getTomcatPort();
			
			//拼接clientID，格式为IP-端口-系统名
			clientId = ip + "-" + port + "-" + clientId;
			super.setClientId(clientId);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 通过MBeanServer获取当前tomcat的端口号
	 * @return
	 * @throws MalformedObjectNameException
	 */
	public String getTomcatPort() throws MalformedObjectNameException {
		MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName(
				"*:type=Connector,*"), Query.match(Query.attr("protocol"),
				Query.value("HTTP/1.1")));
		String port = objectNames.iterator().next().getKeyProperty("port");

		return port;
	}
}
