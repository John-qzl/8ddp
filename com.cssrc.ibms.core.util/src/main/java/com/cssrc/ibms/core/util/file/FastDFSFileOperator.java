package com.cssrc.ibms.core.util.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.encrypt.Base64;
import com.cssrc.ibms.core.util.http.HttpClientUtil;
import com.cssrc.ibms.core.util.redis.RedisClient;

/**
 * FastDFS分布式文件系统文件上传下载工具类
 * @author liubo
 * @date 2016/11/21
 */
public class FastDFSFileOperator {
    private static Logger  logger=Logger.getLogger(FastDFSFileOperator.class);

    public static final String confbean="pluginproperties";

    private  static  String interview_server="fastdfs.interview_server";
	//进行分布式文件存储的存储服务器客户端
    public static StorageClient1 storageClient1;
    
    public static TrackerServer trackerServer;
    
    public static StorageServer storageServer;
    
    //初始化FastDFS Client，连接FastDFS服务器的方法
	private static void getStoreClient() {
        int fastdfsuse = Integer.parseInt(AppConfigUtil.get(confbean, "plugin.fastdfs.use"));
        if(fastdfsuse==0) {
            logger.error(" fastdfs is not use......");
            storageClient1 = null;
            return;
        }
		try {
		    ClientGlobal.initByProperties(AppUtil.getBean("pluginproperties", Properties.class));
		    TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
		    trackerServer = trackerClient.getConnection();
		    if (trackerServer == null) {
		        throw new IllegalStateException("getConnection return null");
		    }
		    //建立连接，返回可用的storageServer
		    storageServer = trackerClient.getStoreStorage(trackerServer);
		    if (storageServer == null) {
		        throw new IllegalStateException("getStoreStorage return null");
		    }
		    //通过storageServer和trackerServer来获进行文件操作的存储服务器客户端
		    storageClient1 = new StorageClient1(trackerServer,storageServer);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

    /**
     * 上传文件
     * @param file 文件对象
     * @param extName 文件后缀名
     * @return
     * @throws MyException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @SuppressWarnings("unused")
	public static String uploadFile(MultipartFile file,String extName) throws FileNotFoundException, IOException, MyException {
    	try {
    		byte[] buff = file.getBytes();
    		//文件属性，当前设置为空
            Map<String,String> metaList = null;
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String,String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name,value);
                }
            }
            
            //连接分布式文件服务器
            getStoreClient();
            //文件上传成功后返回路径
        	String filePath = storageClient1.upload_file1(buff,extName,null);
        	return filePath;
        } catch (Exception e) {
            throw new IllegalStateException("FastDFS连接存在问题，请检查");
        }
    }
    /**
     * 上传文件
     * @param file 文件对象
     * @param extName 文件后缀名
     * @return
     * @throws MyException 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public static String uploadFile(byte[] buff,String extName){
        try {
            //连接分布式文件服务器
            getStoreClient();
            //文件上传成功后返回路径
            String filePath = storageClient1.upload_file1(buff,extName,null);
            return filePath;
        } catch (Exception e) {
            throw new IllegalStateException("FastDFS连接存在问题，请检查");
        }
    }

    /**
     * 删除文件
     * @param fileId 文件ID
     */
    public static void deleteFile(String fileId) {
    	//连接分布式存储服务器
    	getStoreClient();
    	
        try {
            storageClient1.delete_file1(fileId);
        } catch (Exception e) {
        	throw new IllegalStateException("FastDFS连接存在问题，请检查");
        }
    }

    
    /**
     * 下载文件
     * @param filePath 文件ID（上传文件成功后返回的ID）
     * @param fileName 文件名称
     * @return
     */
    public static void download(HttpServletRequest request,
			HttpServletResponse response,String filePath, String fileName) throws IOException, MyException{
    	
    	OutputStream outp = response.getOutputStream();
    	
    	byte[] content = getFileByte(filePath);
    	
    	response.setContentType("APPLICATION/OCTET-STREAM");
		String filedisplay = fileName;
		String agent = (String) request.getHeader("USER-AGENT");
		// firefox
		if (agent != null && agent.indexOf("MSIE") == -1) {
			String enableFileName = "=?UTF-8?B?" + (new String(Base64.getBase64(filedisplay))) + "?=";
			response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
		} else {
			filedisplay = URLEncoder.encode(filedisplay, "utf-8");
			response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
		}
		try {
			outp = response.getOutputStream();
			outp.write(content);
			outp.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outp != null) {
				outp.close();
				outp = null;
				response.flushBuffer();
			}
		}
    	
    }
    
    /**
     * 
     * 获取客户端访问地址
     * @throws IOException 
     */
    public static String getInterviewServer()
        throws IOException
    {
        try {
            Properties properties =AppUtil.getBean("pluginproperties", Properties.class);
            String interviewserver=properties.get(interview_server).toString();
            String url = "http://" + interviewserver + "/";
            return url;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    /**
     * 获取FastDFS上的文件的字节流
     * 
     * @param fileId 文件名 （卷名+文件名）
     * @throws FileNotFoundException 
     */
    public static byte[] getFileByte(String filePath) throws IOException{
    	String interview_server = getInterviewServer();
    	String url = interview_server + filePath;
    	byte[] content = null;
		try {
			content = HttpClientUtil.httpGetByte(url);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally{
		    
		}
		return content;
    }
    
    

    
    /**
     * 判断服务器上文件是否存在
     * 
     * @param filePath 文件名
     * @throws MyException 
     * @throws IOException 
     */
    public static Boolean isExist(String filePath) throws IOException, MyException{
    	byte[] content = getFileByte(filePath);;
    	if(content == null){
    		return false;
    	}else{
    		return true;
    	}
    }
    
}
