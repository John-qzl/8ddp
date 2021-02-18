package com.cssrc.ibms.core.resources.io.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.system.model.SysFile;
/**
 * 
 * @author wenjie
 *
 */
@XmlRootElement(name = "dpFile")
@XmlAccessorType(XmlAccessType.NONE)
public class DpFile{

	private static final long serialVersionUID = 1L;
	
	public DpFile() {}
	
	public DpFile(SysFile file) {
		this.id = file.getFileId();
		this.tableId = file.getTableId();
		this.dataId = file.getDataId();
		this.ext = file.getExt();
		this.filename = file.getFilename();
		this.creatorId = file.getCreatorId();
		this.creator = file.getCreator();
		this.note = file.getNote();
		this.filePath = file.getFilepath();
		this.timeStr = DateUtil.getCurrentDate("HHmmss");
		this.isEncrypt = file.getIsEncrypt();
	}
	public SysFile toSysFile() {
		SysFile file = new SysFile();
		file.setFileId(id);
		file.setExt(ext);
		file.setFilename(filename);
		file.setCreatorId(creatorId);
		file.setCreator(creator);
		file.setNote(note);
		file.setTableId(tableId);
		file.setDataId(dataId);
		file.setIsEncrypt(isEncrypt);
		return file;
	}
	@XmlAttribute
	private Long id;
	
	@XmlAttribute
	private String tableId;
	
	@XmlAttribute
	private String dataId;
	
	@XmlAttribute
	private String ext;
	
	@XmlAttribute
	private String filename;
	
	public Long getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(Long isEncrypt) {
		this.isEncrypt = isEncrypt;
	}
	@XmlAttribute
	private Long isEncrypt;
	
	@XmlAttribute
	private String fileType;
	
	@XmlAttribute
	private Long totalBytes;
	
	@XmlAttribute
	private Long creatorId;
	
	@XmlAttribute
	private String creator;
	
	@XmlAttribute
	private String note;
	
	private String filePath;

	@XmlAttribute
	private String timeStr;
	
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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTimeStr() {
		return timeStr;
	}

	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public String getFullFileName() {
		return this.getFilename()+this.getTimeStr() + "." + this.getExt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
}
