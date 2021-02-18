package com.cssrc.ibms.core.web.servlet;

public class UploadConstant {

	// 上传根目录
	public static final String	UPLOAD_DIR						= "/upload";

	// 上传文件[临时保存]
	public static final String	UPLOAD_TEMP_DIR				= UploadConstant.UPLOAD_DIR + "/temp";
	// 上传文件[保存的文件]
	public static final String	UPLOAD_SAVE_DIR				= UploadConstant.UPLOAD_DIR + "/save";
	// 上传文件[删除的文件]
	public static final String	UPLOAD_DELETE_DIR				= UploadConstant.UPLOAD_DIR + "/delete";
	
	//文件上传，解析编码
	public static final String  UPLOAD_Header_Encoding = "UTF-8";

	// 设置内存阀值，超过后写入临时文件:5MB
	public static final int		UPLOAD_SIZE_THRESHOLD		= 1024 * 1024 * 2;
	// 单次最大上传量request [ 放开限制所以10GB ]
	public static final long	MAX_ONCE_UPLOAD_SIZE			= 1024L * 1024 * 1024 * 10;
	// 单个文件最大上传量[单位MB]
	public static final long	MAX_EACH_FILE_UPLOAD_SIZE	= 1024L * 1024 * 50;

	public static String getFileSize(String fileBytes){
		String fileSize = "0 bytes";

		try{

			long bytes = Long.parseLong(fileBytes);			
			
			if(bytes >= Long.parseLong("1073741824")){
				// 
				fileSize = (bytes / 1024 / 1024 / 1024) + " GB";
			}else if(bytes >= Long.parseLong("1048576")){
				// 
				fileSize = (bytes / 1024 / 1024) + " MB";
			}else if(bytes >= Long.parseLong("1024")){
				// 
				fileSize = (bytes / 1024) + " KB";
			}else if(bytes >= Long.parseLong("1")){
				// 
				fileSize = bytes + " bytes";
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return fileSize;
	}
}
