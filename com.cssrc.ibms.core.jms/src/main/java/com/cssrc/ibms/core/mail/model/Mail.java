package com.cssrc.ibms.core.mail.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Mail implements Serializable {
	private static final long serialVersionUID = 4311266253309771066L;
	protected String messageId;
	protected String senderName;
	protected String senderAddress;
	protected String subject;
	protected String content;
	protected Date sendDate;
	protected String receiverName;
	protected String receiverAddresses;
	protected String copyToName;
	protected String copyToAddresses;
	protected String bccName;
	protected String bcCAddresses;
	protected List<MailAttachment> attachments = new ArrayList();

	public String getSenderAddress() {
		return this.senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBcCAddresses() {
		return this.bcCAddresses;
	}

	public void setBcCAddresses(String bcCAddresses) {
		this.bcCAddresses = bcCAddresses;
	}

	public List<MailAttachment> getMailAttachments() {
		return this.attachments;
	}

	public void setMailAttachments(List<MailAttachment> attachments) {
		this.attachments = attachments;
	}

	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getSenderName() {
		return this.senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getCopyToName() {
		return this.copyToName;
	}

	public void setCopyToName(String copyToName) {
		this.copyToName = copyToName;
	}

	public String getBccName() {
		return this.bccName;
	}

	public void setBccName(String bccName) {
		this.bccName = bccName;
	}

	public String getCopyToAddresses() {
		return this.copyToAddresses;
	}

	public void setCopyToAddresses(String copyToAddresses) {
		this.copyToAddresses = copyToAddresses;
	}

	public String getReceiverAddresses() {
		return this.receiverAddresses;
	}

	public void setReceiverAddresses(String receiverAddresses) {
		this.receiverAddresses = receiverAddresses;
	}

	public String getUID() {
		return this.messageId;
	}

	public void setUID(String uID) {
		this.messageId = uID;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result
				+ (this.messageId == null ? 0 : this.messageId.hashCode());
		result = 31
				* result
				+ (this.bcCAddresses == null ? 0 : this.bcCAddresses.hashCode());
		result = 31 * result
				+ (this.bccName == null ? 0 : this.bccName.hashCode());
		result = 31 * result
				+ (this.content == null ? 0 : this.content.hashCode());
		result = 31
				* result
				+ (this.copyToAddresses == null ? 0 : this.copyToAddresses
						.hashCode());
		result = 31 * result
				+ (this.copyToName == null ? 0 : this.copyToName.hashCode());
		result = 31 * result
				+ (this.attachments == null ? 0 : this.attachments.hashCode());
		result = 31
				* result
				+ (this.receiverAddresses == null ? 0 : this.receiverAddresses
						.hashCode());
		result = 31
				* result
				+ (this.receiverName == null ? 0 : this.receiverName.hashCode());
		result = 31 * result
				+ (this.sendDate == null ? 0 : this.sendDate.hashCode());
		result = 31
				* result
				+ (this.senderAddress == null ? 0 : this.senderAddress
						.hashCode());
		result = 31 * result
				+ (this.senderName == null ? 0 : this.senderName.hashCode());
		result = 31 * result
				+ (this.subject == null ? 0 : this.subject.hashCode());
		return result;
	}

	public String toString() {
		return "Mail [UID=" + this.messageId + ", senderName="
				+ this.senderName + ", senderAddress=" + this.senderAddress
				+ ", subject=" + this.subject + ", content=" + this.content
				+ ", sendDate=" + this.sendDate + ", receiverName="
				+ this.receiverName + ", receiverAddresses="
				+ this.receiverAddresses + ", copyToName=" + this.copyToName
				+ ", copyToAddresses=" + this.copyToAddresses + ", bccName="
				+ this.bccName + ", bcCAddresses=" + this.bcCAddresses
				+ ", attachments=" + this.attachments + "]";
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Mail))
			return false;
		Mail other = (Mail) obj;
		if (this.messageId == null) {
			if (other.messageId != null)
				return false;
		} else if (!this.messageId.equals(other.messageId))
			return false;
		if (this.bcCAddresses == null) {
			if (other.bcCAddresses != null)
				return false;
		} else if (!this.bcCAddresses.equals(other.bcCAddresses))
			return false;
		if (this.bccName == null) {
			if (other.bccName != null)
				return false;
		} else if (!this.bccName.equals(other.bccName))
			return false;
		if (this.content == null) {
			if (other.content != null)
				return false;
		} else if (!this.content.equals(other.content))
			return false;
		if (this.copyToAddresses == null) {
			if (other.copyToAddresses != null)
				return false;
		} else if (!this.copyToAddresses.equals(other.copyToAddresses))
			return false;
		if (this.copyToName == null) {
			if (other.copyToName != null)
				return false;
		} else if (!this.copyToName.equals(other.copyToName))
			return false;
		if (this.attachments == null) {
			if (other.attachments != null)
				return false;
		} else if (!this.attachments.equals(other.attachments))
			return false;
		if (this.receiverAddresses == null) {
			if (other.receiverAddresses != null)
				return false;
		} else if (!this.receiverAddresses.equals(other.receiverAddresses))
			return false;
		if (this.receiverName == null) {
			if (other.receiverName != null)
				return false;
		} else if (!this.receiverName.equals(other.receiverName))
			return false;
		if (this.sendDate == null) {
			if (other.sendDate != null)
				return false;
		} else if (!this.sendDate.equals(other.sendDate))
			return false;
		if (this.senderAddress == null) {
			if (other.senderAddress != null)
				return false;
		} else if (!this.senderAddress.equals(other.senderAddress))
			return false;
		if (this.senderName == null) {
			if (other.senderName != null)
				return false;
		} else if (!this.senderName.equals(other.senderName))
			return false;
		if (this.subject == null) {
			if (other.subject != null)
				return false;
		} else if (!this.subject.equals(other.subject))
			return false;
		return true;
	}
}
