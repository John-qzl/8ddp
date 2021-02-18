package com.cssrc.ibms.core.util.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.support.ResourcePropertySource;

import com.cssrc.ibms.core.util.file.FastDFSFileOperator;

/**
 * 分布式文件存储FastDFS，客户端连接服务器的初始化
 * @author liubo
 * @date 2016/12/14
 *
 */
public class FastDFS implements ServletContextListener{
	
	Logger log = Logger.getLogger(FastDFS.class);
	
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("FastDFS contextDestroyed!");
	}

	public void contextInitialized(ServletContextEvent sce) {
		try {
			this.log.debug("---------[contextInitialized]开始连接分布式文件系统。");
			//获取配置文件fdfs_client.conf
			ResourcePropertySource propertySource = new ResourcePropertySource(
					"classpath:sys.properties");
			String CONF_ROOT=(String) propertySource.getProperty("conf.root");
			String path=CONF_ROOT+File.separator+"conf"+File.separator+"fdfs_client.conf";
			
			//读取配置文件中的信息
		    ClientGlobal.init(path);
		    TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
		    
		    TrackerServer trackerServer = trackerClient.getConnection();
		    if (trackerServer == null) {
		        throw new IllegalStateException("getConnection return null");
		    }else{
		    	FastDFSFileOperator.trackerServer = trackerServer;
		    }
	
		    //建立连接，返回可用的storageServer
		    StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
		    if (storageServer == null) {
		        throw new IllegalStateException("getStoreStorage return null");
		    }else{
		    	FastDFSFileOperator.storageServer = storageServer;
		    }
		    //通过storageServer和trackerServer来获进行文件操作的存储服务器客户端
		    StorageClient1 storageClient = new StorageClient1(trackerServer,storageServer);
		    
		    this.log.debug("---------[contextInitialized]分布式文件系统连接成功。");
		    //将返回的StorageClient1存入FastDFS工具类中的静态常量里;
		    FastDFSFileOperator.storageClient1 = storageClient;
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
		

	}
	
	
}