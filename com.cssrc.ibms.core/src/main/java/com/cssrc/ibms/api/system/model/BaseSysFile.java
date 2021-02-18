package com.cssrc.ibms.api.system.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class BaseSysFile extends BaseModel implements ISysFile
{

    private static final long serialVersionUID = 706011945236415687L;
    protected Long fileId;
    protected Long creatorId;
    protected String creator;
    protected String filepath;
    protected String ext; 
    private String filename;
    protected byte[] fileBlob;
    protected Long totalBytes;
    protected Long parentId;
    protected Long storeWay=Long.valueOf(0L);//默认存本地服务器
    protected String dataId;
    protected String attachFieldName; //业务表附件字段名
    protected Long isEncrypt = Long.valueOf(0L);//是否加密存储
    protected String dimension; //维度属性
    protected String tableId;
    public Long getFileId()
    {
        return fileId;
    }
    public void setFileId(Long fileId)
    {
        this.fileId = fileId;
    }
    public Long getCreatorId()
    {
        return creatorId;
    }
    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }
    public String getCreator()
    {
        return creator;
    }
    public void setCreator(String creator)
    {
        this.creator = creator;
    }
    public String getFilepath()
    {
        return filepath;
    }
    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
    }
    public String getExt()
    {
        return ext;
    }
    public void setExt(String ext)
    {
        this.ext = ext;
    }
    public String getFilename()
    {
        return filename;
    }
    public void setFilename(String filename)
    {
        this.filename = filename;
    }
    public byte[] getFileBlob()
    {
        return fileBlob;
    }
    public void setFileBlob(byte[] fileBlob)
    {
        this.fileBlob = fileBlob;
    }
    public Long getTotalBytes()
    {
        return totalBytes;
    }
    public void setTotalBytes(Long totalBytes)
    {
        this.totalBytes = totalBytes;
    }
    public Long getParentId()
    {
        return parentId;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }
    public Long getStoreWay()
    {
        return storeWay;
    }
    public void setStoreWay(Long storeWay)
    {
        this.storeWay = storeWay;
    }
    public String getDataId()
    {
        return dataId;
    }
    public void setDataId(String dataId)
    {
        this.dataId = dataId;
    }
    public String getAttachFieldName()
    {
        return attachFieldName;
    }
    public void setAttachFieldName(String attachFieldName)
    {
        this.attachFieldName = attachFieldName;
    }
	public Long getIsEncrypt() 
	{
		return isEncrypt;
	}
	public void setIsEncrypt(Long isEncrypt) 
	{
		this.isEncrypt = isEncrypt;
	}
	public String getDimension() 
	{
		return dimension;
	}
	public void setDimension(String dimension) 
	{
		this.dimension = dimension;
	}
	public String getTableId() 
	{
		return tableId;
	}
	public void setTableId(String tableId) 
	{
		this.tableId = tableId;
	}
}
