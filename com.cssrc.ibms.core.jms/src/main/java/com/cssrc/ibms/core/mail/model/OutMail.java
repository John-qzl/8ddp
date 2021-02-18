package com.cssrc.ibms.core.mail.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class OutMail extends BaseModel {
	public static Short Mail_IsRead = Short.valueOf((short) 1);

	public static Short Mail_IsNotRead = Short.valueOf((short) 0);

	public static Integer Mail_IsReplay = Integer.valueOf(1);

	public static Integer Mail_IsNotReplay = Integer.valueOf(0);

	public static Short Mail_InBox = Short.valueOf((short) 1);

	public static Short Mail_OutBox = Short.valueOf((short) 2);

	public static Short Mail_DraftBox = Short.valueOf((short) 3);

	public static Short Mail_DumpBox = Short.valueOf((short) 4);
	protected Long mailId;
	protected Long setId;
	protected String title;
	protected String content;
	protected String senderAddresses;
	protected String senderName;
	protected String receiverAddresses;
	protected String receiverNames;
	protected String ccAddresses;
	protected String ccNames;
	protected String bcCAddresses;
	protected String bcCAnames;
	protected Date mailDate;
	protected String fileIds;
	protected Short isRead = Mail_IsNotRead;

	protected Integer isReply = Mail_IsNotReplay;
	protected String emailId;
	protected Integer types;//1:收件箱;2:发件箱;3:草稿箱;4:垃圾箱
	protected Long userId;

	public Integer getTypes() {
		return this.types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	public void setMailId(Long mailId) {
		this.mailId = mailId;
	}

	public Long getMailId() {
		return this.mailId;
	}

	public Long getSetId() {
		return this.setId;
	}

	public void setSetId(Long setId) {
		this.setId = setId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return this.content;
	}

	public Short getIsRead() {
		return this.isRead;
	}

	public void setIsRead(Short isRead) {
		this.isRead = isRead;
	}

	public Integer getIsReply() {
		return this.isReply;
	}

	public void setIsReply(Integer isReply) {
		this.isReply = isReply;
	}

	public void setSenderAddresses(String senderAddresses) {
		this.senderAddresses = senderAddresses;
	}

	public String getSenderAddresses() {
		return this.senderAddresses;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderName() {
		return this.senderName;
	}

	public void setReceiverAddresses(String receiverAddresses) {
		this.receiverAddresses = receiverAddresses;
	}

	public String getReceiverAddresses() {
		return this.receiverAddresses;
	}

	public void setReceiverNames(String receiverNames) {
		this.receiverNames = receiverNames;
	}

	public String getReceiverNames() {
		return this.receiverNames;
	}

	public void setCcAddresses(String ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String getCcAddresses() {
		return this.ccAddresses;
	}

	public void setCcNames(String ccNames) {
		this.ccNames = ccNames;
	}

	public String getCcNames() {
		return this.ccNames;
	}

	public void setBcCAddresses(String bcCAddresses) {
		this.bcCAddresses = bcCAddresses;
	}

	public String getBcCAddresses() {
		return this.bcCAddresses;
	}

	public void setBcCAnames(String bcCAnames) {
		this.bcCAnames = bcCAnames;
	}

	public String getBcCAnames() {
		return this.bcCAnames;
	}

	public void setMailDate(Date mailDate) {
		this.mailDate = mailDate;
	}

	public Date getMailDate() {
		return this.mailDate;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}

	public String getFileIds() {
		return this.fileIds;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof OutMail)) {
			return false;
		}
		OutMail rhs = (OutMail) object;
		return new EqualsBuilder().append(this.mailId, rhs.mailId)
				.append(this.title, rhs.title)
				.append(this.content, rhs.content)
				.append(this.senderAddresses, rhs.senderAddresses)
				.append(this.senderName, rhs.senderName)
				.append(this.receiverAddresses, rhs.receiverAddresses)
				.append(this.receiverNames, rhs.receiverNames)
				.append(this.ccAddresses, rhs.ccAddresses)
				.append(this.ccNames, rhs.ccNames)
				.append(this.bcCAddresses, rhs.bcCAddresses)
				.append(this.bcCAnames, rhs.bcCAnames)
				.append(this.mailDate, rhs.mailDate)
				.append(this.fileIds, rhs.fileIds)
				.append(this.isRead, rhs.isRead)
				.append(this.isReply, rhs.isReply)
				.append(this.emailId, rhs.emailId)
				.append(this.types, rhs.types).append(this.userId, rhs.userId)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.mailId)
				.append(this.title).append(this.content)
				.append(this.senderAddresses).append(this.senderName)
				.append(this.receiverAddresses).append(this.receiverNames)
				.append(this.ccAddresses).append(this.ccNames)
				.append(this.bcCAddresses).append(this.bcCAnames)
				.append(this.mailDate).append(this.fileIds).append(this.isRead)
				.append(this.isReply).append(this.emailId).append(this.types)
				.append(this.userId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("mailId", this.mailId)
				.append("title", this.title).append("content", this.content)
				.append("senderAddresses", this.senderAddresses)
				.append("senderName", this.senderName)
				.append("receiverAddresses", this.receiverAddresses)
				.append("receiverNames", this.receiverNames)
				.append("ccAddresses", this.ccAddresses)
				.append("ccNames", this.ccNames)
				.append("bcCAddresses", this.bcCAddresses)
				.append("bcCAnames", this.bcCAnames)
				.append("mailDate", this.mailDate)
				.append("fileIds", this.fileIds).append("isRead", this.isRead)
				.append("isReply", this.isReply)
				.append("emailId", this.emailId).append("types", this.types)
				.append("userId", this.userId).toString();
	}
}
