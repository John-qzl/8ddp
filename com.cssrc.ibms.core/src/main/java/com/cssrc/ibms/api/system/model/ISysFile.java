package com.cssrc.ibms.api.system.model;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public interface ISysFile {
	/**
	 * 文件存在[value=0]
	 */
	public static Short FILE_NOT_DEL = 0;
	public static Short FILE_DEL = 1;
	
	public static final Short SHARED_TRUE = 1;
	public static final Short SHARED_FALSE =0;
	
	public static final Short FILEATT_TRUE = 1;
	public static final Short FILEATT_FALSE =0;
	/*明细多Tab展示模板*/
	public static String MLUTITAB_VIEW="dataTemplateMultiTabTemplate";
	public static String FILEVIEW="dataTemplateFileView";
	public static String FILELIST="dataTemplateFileList";
	public static String PROCESSVIEW="dataTemplateProcessView";
	public static final Long ISNEW_VERSION=Long.valueOf(1);
	public static final Long ISOLD_VERSION=Long.valueOf(0);
	
	/**
	 * 文件密级
	 */
	//文件密级map，value为密级对应的中文名
	public static final Map<String, String> SECURITY_CHINESE_MAP=new LinkedHashMap<String, String>();
	//判断是否显示密级属性和是否按照密级权限显示的系统参数名称
	public static final String IS_DISPLAY_SECURITY= "IS_DISPLAY_SECURITY";
	//文件密级属性键值对的配置，需要与业务表定义中的“密级管理”控制保持一致，格式为“3:公开-6:秘密-9:机密-12:绝密”
	public static String FILE_SECURITY_MAP= "FILE_SECURITY_MAP";
	//系统附件表
	public static final String FILE_SECURITY_TABLE= "cwm_sys_file";
	//系统文件表的密级字段
	public static final String FILE_SECURITY_FIELD= "SECURITY_";
	//系统文件表的是否归档字段
	public static final String FILE_FILING_FIELD= "FILING";
	//系统文件表的文件存储方式字段
	public static final String FILE_STOREWAY_FIELD= "STOREWAY";
	//文件归档状态
	public static final Map<Long, String> FILING_STATUS=new HashMap<Long, String>(){{
		put(1l,"已归档");
		put(0l,"未归档");
	}};
	//文件存储类型
	public static final Map<Long, String> STORE_WAY=new HashMap<Long, String>(){{
		put(1l,"分布式存储");
		put(0l,"本地存储");
	}};
	
	/**
	 * 匿名用户
	 */
	public static String FILE_UPLOAD_UNKNOWN = "unknown";
	public static String FILE_EX_SMALL = "_small";
	public static String FILE_TEMP_FODER = "temp";
	public static final Short DEFAULT_ORIGINAL_PHOTO_ID = Short.valueOf((short) 3);
	Long getFileId();
	Long getCreatorId();
	Date getCreatetime();
	void setCreatetime(Date createtime);
	void setCreatorId(Long userId);
	void setCreator(String username);
	String getFilepath();
	String getExt();
	String getFilename();
	byte[] getFileBlob();
	Long getTotalBytes();
	void setFileId(Long fileId);
	Long getParentId();
	public Long getStoreWay();
	public void setStoreWay(Long storeWay);
	String getDataId();
	String getAttachFieldName();
	void setDataId(String dataId);
	void setAttachFieldName(String attachFieldName);
	Long getIsEncrypt();
	String getDimension();
	String getTableId();
}