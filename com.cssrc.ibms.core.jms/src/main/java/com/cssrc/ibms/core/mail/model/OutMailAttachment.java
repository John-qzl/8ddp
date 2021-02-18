package com.cssrc.ibms.core.mail.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class OutMailAttachment extends BaseModel {
	protected Long fileId;
	protected String fileName;
	protected String filePath;
	protected Long mailId;

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	public Long getFileId() {
		return this.fileId;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public Long getMailId() {
		return this.mailId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof OutMailAttachment)) {
			return false;
		}
		OutMailAttachment rhs = (OutMailAttachment) object;
		return new EqualsBuilder().append(this.fileId, rhs.fileId)
				.append(this.fileName, rhs.fileName)
				.append(this.filePath, rhs.filePath)
				.append(this.mailId, rhs.mailId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.fileId)
				.append(this.fileName).append(this.filePath)
				.append(this.mailId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("fileId", this.fileId)
				.append("fileName", this.fileName)
				.append("filePath", this.filePath)
				.append("mailId", this.mailId).toString();
	}
}
