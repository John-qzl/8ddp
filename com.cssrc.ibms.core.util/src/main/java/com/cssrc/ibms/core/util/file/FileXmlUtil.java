package com.cssrc.ibms.core.util.file;

import java.io.File;
import java.util.List;
import java.util.Map;





/**
 * 瀵煎叆xml鍒犻櫎鏂囦欢
 * @author zhulongchao
 *
 */
public class FileXmlUtil {
	
	/**
	 * 鍐橷ml鏂囦欢
	 * @param fileName
	 * @param list
	 * @param path
	 * @return
	 */
	public static  String writeXmlFile(String fileName,String context,String path){
		String filePath = createFilePath(path , fileName);
		FileOperator.writeFile(filePath,context);
		return filePath;
	}

	/**
	 * 鍒涘缓鏂囦欢鐩綍
	 * @param tempPath
	 * @param fileName 鏂囦欢鍚嶇О
	 * @return 鏂囦欢鐨勫畬鏁寸洰褰?	 */
	public static String createFilePath(String tempPath, String fileName){
		File file = new File(tempPath);
		//鏂囦欢澶逛笉瀛樺湪鍒涘缓
		if(!file.exists())
			file.mkdirs();
		return file.getPath() + File.separator+ fileName;
	}
	/**
	 * 创建文件夹 如果存在就删除重新创建
	 * @param tempPath
	 * @param fileName 文件名
	 * @return 文件路径 */
	public static String createNewFilePath(String tempPath, String fileName){
		File file = new File(tempPath);
		if(!file.exists())
			file.mkdirs();
		else {
			file.delete();
			file.mkdirs();
		}
		return file.getPath() + File.separator+ fileName;
	}
	

}
