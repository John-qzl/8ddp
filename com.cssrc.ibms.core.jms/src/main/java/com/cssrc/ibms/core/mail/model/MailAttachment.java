package com.cssrc.ibms.core.mail.model;

import java.io.Serializable;

public class MailAttachment implements Serializable {
	private static final long serialVersionUID = 608655613427099779L;
	protected String fileName;
	protected String filePath;
	protected byte[] fileBlob;

	public MailAttachment() {
	}

	public MailAttachment(String fileName, String filePath) {
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public MailAttachment(String fileName, byte[] fileBlob) {
		this.fileName = fileName;
		this.fileBlob = fileBlob;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getFileBlob() {
		return this.fileBlob;
	}

	public void setFileBlob(byte[] fileBlob) {
		this.fileBlob = fileBlob;
	}
}
