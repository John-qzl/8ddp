package com.cssrc.ibms.core.mail.model;

import java.io.Serializable;

public class MailSeting implements Serializable {
	protected static final long serialVersionUID = -1563590072023033989L;
	protected String sendHost;
	protected String sendPort;
	protected String receiveHost;
	protected String receivePort;
	protected String protocal;
	protected Boolean SSL = Boolean.valueOf(false);

	protected Boolean validate = Boolean.valueOf(true);
	protected String mailAddress;
	protected String password;
	protected String nickName;
	protected Boolean isHandleAttach = Boolean.valueOf(true);

	protected Boolean isDeleteRemote = Boolean.valueOf(false);
	public static final String SMTP_PROTOCAL = "smtp";
	public static final String POP3_PROTOCAL = "pop3";
	public static final String IMAP_PROTOCAL = "imap";

	public Boolean getSSL() {
		return this.SSL;
	}

	public void setSSL(Boolean SSL) {
		this.SSL = SSL;
	}

	public Boolean getValidate() {
		return this.validate;
	}

	public void setValidate(Boolean validate) {
		this.validate = validate;
	}

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProtocal() {
		return this.protocal;
	}

	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}

	public String getReceiveHost() {
		return this.receiveHost;
	}

	public void setReceiveHost(String receiveHost) {
		this.receiveHost = receiveHost;
	}

	public String getReceivePort() {
		return this.receivePort;
	}

	public void setReceivePort(String receivePort) {
		this.receivePort = receivePort;
	}

	public Boolean getIsHandleAttach() {
		return this.isHandleAttach;
	}

	public void setIsHandleAttach(Boolean isHandleAttach) {
		this.isHandleAttach = isHandleAttach;
	}

	public String getSendHost() {
		return this.sendHost;
	}

	public void setSendHost(String sendHost) {
		this.sendHost = sendHost;
	}

	public String getSendPort() {
		return this.sendPort;
	}

	public void setSendPort(String sendPort) {
		this.sendPort = sendPort;
	}

	public Boolean getIsDeleteRemote() {
		return this.isDeleteRemote;
	}

	public void setIsDeleteRemote(Boolean isDeleteRemote) {
		this.isDeleteRemote = isDeleteRemote;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + (this.SSL == null ? 0 : this.SSL.hashCode());
		result = 31
				* result
				+ (this.isDeleteRemote == null ? 0 : this.isDeleteRemote
						.hashCode());
		result = 31
				* result
				+ (this.isHandleAttach == null ? 0 : this.isHandleAttach
						.hashCode());
		result = 31 * result
				+ (this.mailAddress == null ? 0 : this.mailAddress.hashCode());
		result = 31 * result
				+ (this.nickName == null ? 0 : this.nickName.hashCode());
		result = 31 * result
				+ (this.password == null ? 0 : this.password.hashCode());
		result = 31 * result
				+ (this.protocal == null ? 0 : this.protocal.hashCode());
		result = 31 * result
				+ (this.receiveHost == null ? 0 : this.receiveHost.hashCode());
		result = 31 * result
				+ (this.receivePort == null ? 0 : this.receivePort.hashCode());
		result = 31 * result
				+ (this.sendHost == null ? 0 : this.sendHost.hashCode());
		result = 31 * result
				+ (this.sendPort == null ? 0 : this.sendPort.hashCode());
		result = 31 * result
				+ (this.validate == null ? 0 : this.validate.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof MailSeting))
			return false;
		MailSeting other = (MailSeting) obj;
		if (this.SSL == null) {
			if (other.SSL != null)
				return false;
		} else if (!this.SSL.equals(other.SSL))
			return false;
		if (this.isDeleteRemote == null) {
			if (other.isDeleteRemote != null)
				return false;
		} else if (!this.isDeleteRemote.equals(other.isDeleteRemote))
			return false;
		if (this.isHandleAttach == null) {
			if (other.isHandleAttach != null)
				return false;
		} else if (!this.isHandleAttach.equals(other.isHandleAttach))
			return false;
		if (this.mailAddress == null) {
			if (other.mailAddress != null)
				return false;
		} else if (!this.mailAddress.equals(other.mailAddress))
			return false;
		if (this.nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!this.nickName.equals(other.nickName))
			return false;
		if (this.password == null) {
			if (other.password != null)
				return false;
		} else if (!this.password.equals(other.password))
			return false;
		if (this.protocal == null) {
			if (other.protocal != null)
				return false;
		} else if (!this.protocal.equals(other.protocal))
			return false;
		if (this.receiveHost == null) {
			if (other.receiveHost != null)
				return false;
		} else if (!this.receiveHost.equals(other.receiveHost))
			return false;
		if (this.receivePort == null) {
			if (other.receivePort != null)
				return false;
		} else if (!this.receivePort.equals(other.receivePort))
			return false;
		if (this.sendHost == null) {
			if (other.sendHost != null)
				return false;
		} else if (!this.sendHost.equals(other.sendHost))
			return false;
		if (this.sendPort == null) {
			if (other.sendPort != null)
				return false;
		} else if (!this.sendPort.equals(other.sendPort))
			return false;
		if (this.validate == null) {
			if (other.validate != null)
				return false;
		} else if (!this.validate.equals(other.validate))
			return false;
		return true;
	}

	public String toString() {
		return "MailSeting [sendHost=" + this.sendHost + ", sendPort="
				+ this.sendPort + ", receiveHost=" + this.receiveHost
				+ ", receivePort=" + this.receivePort + ", protocal="
				+ this.protocal + ", SSL=" + this.SSL + ", validate="
				+ this.validate + ", mailAddress=" + this.mailAddress
				+ ", password=" + this.password + ", nickName=" + this.nickName
				+ ", isHandleAttach=" + this.isHandleAttach
				+ ", isDeleteRemote=" + this.isDeleteRemote + "]";
	}
}
