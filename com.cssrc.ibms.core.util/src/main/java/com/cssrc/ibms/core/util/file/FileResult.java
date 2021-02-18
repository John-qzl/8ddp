package com.cssrc.ibms.core.util.file;

public class FileResult {
	public static String FILES="resultFile";
	String fileName;
	String storePath;
	String extName;   
	Integer storeType;
	byte[] file;

	public FileResult(String fileName, String storePath, String extName,
			byte[] bytes,Integer storeType) {
		this.fileName = fileName;
		this.storePath = storePath;
		this.extName = extName;
		this.storeType=storeType;
		// 是否需要file文件流
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

    public Integer getStoreType()
    {
        return storeType;
    }

    public void setStoreType(Integer storeType)
    {
        this.storeType = storeType;
    }
	

}
