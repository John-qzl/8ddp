package com.cssrc.ibms.system.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cssrc.ibms.api.system.model.BaseSysFile;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.util.CustomDateSerializer;
import com.google.gson.annotations.Expose;
@XmlRootElement(name = "dataObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class SysFile extends BaseSysFile implements ISysFile{
	
	private static final long serialVersionUID = 1L;
	
	protected Long fileId;
	@Expose
	protected String ext; 
	@Expose
	private String filename;
	@Expose
	protected String filepath;
	@Expose
	protected String fileType;
	@Expose
	protected Date createtime;
	@Expose
	protected Long totalBytes;
	@Expose
	protected Long creatorId;
	@Expose
	protected String creator;
	@Expose
	protected String note;
	@Expose
	protected Short delFlag;
	
	protected String tableId;

	protected String dataId;
	
	protected Short shared;
    
	protected Long folderid;
	
	protected Short fileatt;
	
	protected String folderPath ;
	
	/**文件密级*/
	protected String security;
	/**是否加密存储*/
	protected Long isEncrypt = Long.valueOf(0L);;
	/**文件描述*/
	protected String describe;
	
	protected Long protypeId; //globaltype的typeid外键
	
	protected byte[] fileBlob;
	
	protected Long filing= Long.valueOf(0L);//默认未归档
	
	protected Long parentId;
	
	protected Long isnew= Long.valueOf(1L);//默认添加为最新
	
	protected String version="0.1";
	
	protected Long storeWay=Long.valueOf(0L);//默认存本地服务器
	
	protected String attachFieldName; //业务表附件字段名
	
	protected String dimension; //维度属性
	
	public SysFile() {
		
	}
	public String getAttachFieldName() {
		return attachFieldName;
	}

	public void setAttachFieldName(String attachFieldName) {
		this.attachFieldName = attachFieldName;
	}

	public Long getStoreWay() {
		return storeWay;
	}

	public void setStoreWay(Long storeWay) {
		this.storeWay = storeWay;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
 
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	@JsonSerialize(using = CustomDateSerializer.class) 
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Long getTotalBytes() {
		return totalBytes;
	}

	public void setTotalBytes(Long totalBytes) {
		this.totalBytes = totalBytes;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Short getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Short delFlag) {
		this.delFlag = delFlag;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	
	public Short getShared() {
		return shared;
	}

	public void setShared(Short shared) {
		this.shared = shared;
	}

	public Long getFolderid() {
		return folderid;
	}

	public void setFolderid(Long folderid) {
		this.folderid = folderid;
	}

	public Short getFileatt() {
		return fileatt;
	}
	
	public void setFileatt(Short fileatt) {
		this.fileatt = fileatt;
	}
	
	public String getFolderPath() {
		return folderPath;
	}
	
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Long getProtypeId() {
		return protypeId;
	}

	public void setProtypeId(Long protypeId) {
		this.protypeId = protypeId;
	}

	public byte[] getFileBlob()
	{
		return this.fileBlob;
	}

	public void setFileBlob(byte[] fileBlob) {
		this.fileBlob = fileBlob;
	}

	public Long getFiling() {
		return filing;
	}

	public void setFiling(Long filing) {
		this.filing = filing;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getIsnew() {
		return isnew;
	}

	public void setIsnew(Long isnew) {
		this.isnew = isnew;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public Long getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(Long isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

}
