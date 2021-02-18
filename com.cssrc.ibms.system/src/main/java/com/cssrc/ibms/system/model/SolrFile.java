package com.cssrc.ibms.system.model;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;

import org.com.cssrc.ibms.solrclient.intf.ISolrFile;

/**
 *@author vector
 *@date 2017年8月8日 下午12:16:33
 *@Description 获取文件数据，用于solr检索
 */
public class SolrFile extends SysFile implements ISolrFile{
	private static final long serialVersionUID = 6700400339087207198L;
	//字符流
	private byte[] bytes;
	//本项目 主域名及端口
	private String domain;
	//本项目 content root web根的上下文环境
	private String root;
	private int securityRank;
	public SolrFile(SysFile file){
		this.fileId=file.fileId;
		this.ext=file.ext;
		this.setFilename(file.getFilename());
		this.securityRank=Integer.valueOf(file.security);
		//setDomainAndPort();
		
	}
	@SuppressWarnings("unused")
	private void setDomainAndPort(){
		try {
			//获取当前系统IP地址
			InetAddress inet = InetAddress.getLocalHost();
			String ip = inet.getHostAddress();
			//获取正在使用的tomcat端口号
			String port = getTomcatPort();
			domain="http://"+ip+":"+port;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 通过MBeanServer获取当前tomcat的端口号
	 * @return
	 * @throws MalformedObjectNameException
	 */
	private String getTomcatPort() throws MalformedObjectNameException {
		MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName(
				"*:type=Connector,*"), Query.match(Query.attr("protocol"),
				Query.value("HTTP/1.1")));
		String port = objectNames.iterator().next().getKeyProperty("port");

		return port;
	}
	//通过配置文件获取域名端口和本系统的content root 
	//使用时在solrserver中的index.html中再添加
	public String getDownload() {
		return "/oa/system/sysFile/download.do?fileId="+fileId;
	}
	@Override
	public String getPreview() {
		String url ="/oa/system/sysFile/preview.do?fileId="+fileId;
		if("doc,docx".contains(ext)){
			url += "&url=oa/system/sysFileOfficePreview.jsp";
		}else if("pdf".contains(ext)){
			url += "&url=oa/system/sysFilePDFPreview.jsp";
		}
		return url;
	}

	@Override
	public byte[] getFileBytes() {
		
		return bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public int getSecurityRank() {
		return securityRank;
	}
	
}
