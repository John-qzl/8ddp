package com.cssrc.ibms.system.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;

/**
 * Description: 将SysFileService无依赖的方法抽取到util中，并进行修改
 * <p>SysFileUtil.java</p>
 * @author dengwenjie 
 * @date 2017年9月7日
 */
public class SysFileUtil {
	
	public static String getStorePath() {
		Calendar cal = Calendar.getInstance();
		String storePath= ""+cal.get(Calendar.YEAR)+File.separator+(cal.get(Calendar.MONTH)+1)+File.separator+cal.get(Calendar.DAY_OF_MONTH);
		return storePath;
	}
	/**
	 * @param map
	 * @param nodePath
	 * @return
	 */
	public static String getFloderName(Map<String,String> map,String nodePath){
		String floderName = "";
		if(nodePath.contains(".")){
			String[] nodeArr = nodePath.split("\\.");
			for(String node : nodeArr){
				String name = map.get(node);
				if(BeanUtils.isEmpty(name)){
					continue;
				}
				floderName+= name + File.separator;
			}
		}
		return floderName;
	}
	/**
	 * 获取response头类型。
	 * 
	 * @param extName
	 * @return
	 */
	public  static String getContextType(String extName, boolean isRead) {
		String contentType = "application/octet-stream";
		if ("jpg".equalsIgnoreCase(extName) || "jpeg".equalsIgnoreCase(extName)) {
			contentType = "image/jpeg";
		} else if ("png".equalsIgnoreCase(extName)) {
			contentType = "image/png";
		} else if ("gif".equalsIgnoreCase(extName)) {
			contentType = "image/gif";
		} else if ("doc".equalsIgnoreCase(extName)
				|| "docx".equalsIgnoreCase(extName)) {
			contentType = "application/msword";
		} else if ("xls".equalsIgnoreCase(extName)
				|| "xlsx".equalsIgnoreCase(extName)) {
			contentType = "application/vnd.ms-excel";
		} else if ("ppt".equalsIgnoreCase(extName)
				|| "pptx".equalsIgnoreCase(extName)) {
			contentType = "application/ms-powerpoint";
		} else if ("rtf".equalsIgnoreCase(extName)) {
			contentType = "application/rtf";
		} else if ("htm".equalsIgnoreCase(extName)
				|| "html".equalsIgnoreCase(extName)) {
			contentType = "text/html";
		} else if ("swf".equalsIgnoreCase(extName)) {
			contentType = "application/x-shockwave-flash";
		} else if ("bmp".equalsIgnoreCase(extName)) {
			contentType = "image/bmp";
		} else if ("mp4".equalsIgnoreCase(extName)) {
			contentType = "video/mp4";
		} else if ("wmv".equalsIgnoreCase(extName)) {
			contentType = "video/x-ms-wmv";
		} else if ("wm".equalsIgnoreCase(extName)) {
			contentType = "video/x-ms-wm";
		} else if ("rv".equalsIgnoreCase(extName)) {
			contentType = "video/vnd.rn-realvideo";
		} else if ("mp3".equalsIgnoreCase(extName)) {
			contentType = "audio/mp3";
		} else if ("wma".equalsIgnoreCase(extName)) {
			contentType = "audio/x-ms-wma";
		} else if ("wav".equalsIgnoreCase(extName)) {
			contentType = "audio/wav";
		}
		if ("pdf".equalsIgnoreCase(extName) && isRead)// txt不下载文件，读取文件内容
		{
			contentType = "application/pdf";
		}
		if (("sql".equalsIgnoreCase(extName) || "txt".equalsIgnoreCase(extName))
				&& isRead)// pdf不下载文件，读取文件内容
		{
			contentType = "text/plain";
		}
		return contentType;
	}
	/**
	 * 创建文件目录
	 * 
	 * @param tempPath
	 * @param fileName
	 *            文件名称
	 * @return 文件的完整目录
	 */
	public static String createFilePath(String tempPath, String fileName) {
		File one = new File(tempPath);
		if (!one.exists()) {
			one.mkdirs();
		}
		return one.getPath() + File.separator + fileName;
	}
	/**
	 * 压缩文件
	 * @param zipOutStream
	 * @param filePath
	 * @param fileName
	 */
	public static void compressFile(ZipOutputStream zipOutStream, String filePath,
			String fileName,Boolean isNoGroup) {
		
		try {
			String zipEntryName = fileName; // 乱码处理
			zipOutStream.putNextEntry(new ZipEntry(zipEntryName));
			File file = new File(filePath);
			if(isNoGroup){//分布式文件压缩
				byte[] content = FastDFSFileOperator.getFileByte(filePath);
				zipOutStream.write(content);
			}else {
				if (file.exists()) {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
					byte[] buffer = new byte[8192];
					int len = 0;
					while ((len = bis.read(buffer)) != -1) {
						zipOutStream.write(buffer, 0, len);
					}
					bis.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
