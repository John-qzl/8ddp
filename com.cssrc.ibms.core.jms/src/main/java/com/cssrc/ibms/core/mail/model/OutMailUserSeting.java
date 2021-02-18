package com.cssrc.ibms.core.mail.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.jms.model.IOutMailUserSeting;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class OutMailUserSeting extends BaseModel implements IOutMailUserSeting
{
	protected Long id;
	protected Long userId;
	protected String userName;
	protected String mailAddress;
	protected String mailPass;
	protected String smtpHost;
	protected String smtpPort;
	protected String popHost;
	protected String popPort;
	protected String imapHost;
	protected String imapPort;
	protected Integer isDefault;
	protected String mailType;
	protected Long parentId;
	protected String isParent;
	protected Integer isLeaf;
	protected String open = "true";
	protected Integer types;
	protected int SSL = 0;

	protected int isDeleteRemote = 0;

	protected int validate = 1;

	protected int isHandleAttach = 1;
	public static final int ENABLE = 1;
	public static final int DISABLE = 0;

	public String getMailType()
	{
		return this.mailType;
	}
	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getUserId()
	{
		return this.userId;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserName()
	{
		return this.userName;
	}

	public void setMailAddress(String mailAddress)
	{
		this.mailAddress = mailAddress;
	}

	public String getMailAddress()
	{
		return this.mailAddress;
	}

	public void setMailPass(String mailPass)
	{
		this.mailPass = mailPass;
	}

	public String getMailPass()
	{
		return this.mailPass;
	}

	public void setSmtpHost(String smtpHost)
	{
		this.smtpHost = smtpHost;
	}

	public String getSmtpHost()
	{
		return this.smtpHost;
	}

	public void setSmtpPort(String smtpPort)
	{
		this.smtpPort = smtpPort;
	}

	public String getSmtpPort()
	{
		return this.smtpPort;
	}

	public void setPopHost(String popHost)
	{
		this.popHost = popHost;
	}

	public String getPopHost()
	{
		return this.popHost;
	}

	public void setPopPort(String popPort)
	{
		this.popPort = popPort;
	}

	public String getPopPort()
	{
		return this.popPort;
	}

	public void setImapHost(String imapHost)
	{
		this.imapHost = imapHost;
	}

	public String getImapHost()
	{
		return this.imapHost;
	}

	public void setImapPort(String imapPort)
	{
		this.imapPort = imapPort;
	}

	public String getImapPort()
	{
		return this.imapPort;
	}

	public void setIsDefault(Integer isDefault)
	{
		this.isDefault = isDefault;
	}

	public Integer getIsDefault()
	{
		return this.isDefault;
	}

	public Integer getTypes()
	{
		return this.types;
	}
	public void setTypes(Integer types) {
		this.types = types;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof OutMailUserSeting))
		{
			return false;
		}
		OutMailUserSeting rhs = (OutMailUserSeting)object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.userId, rhs.userId)
		.append(this.userName, rhs.userName)
		.append(this.mailAddress, rhs.mailAddress)
		.append(this.mailPass, rhs.mailPass)
		.append(this.smtpHost, rhs.smtpHost)
		.append(this.smtpPort, rhs.smtpPort)
		.append(this.popHost, rhs.popHost)
		.append(this.popPort, rhs.popPort)
		.append(this.imapHost, rhs.imapHost)
		.append(this.imapPort, rhs.imapPort)
		.append(this.isDefault, rhs.isDefault)
		.isEquals();
	}

	public Long getParentId() {
		return this.parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getIsParent() {
		return this.isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
	public Integer getIsLeaf() {
		return this.isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getOpen() {
		return this.open;
	}
	public void setOpen(String open) {
		this.open = open;
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.userId)
		.append(this.userName)
		.append(this.mailAddress)
		.append(this.mailPass)
		.append(this.smtpHost)
		.append(this.smtpPort)
		.append(this.popHost)
		.append(this.popPort)
		.append(this.imapHost)
		.append(this.imapPort)
		.append(this.isDefault)
		.append(this.SSL)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("userId", this.userId)
		.append("userName", this.userName)
		.append("mailAddress", this.mailAddress)
		.append("mailPass", this.mailPass)
		.append("smtpHost", this.smtpHost)
		.append("smtpPort", this.smtpPort)
		.append("popHost", this.popHost)
		.append("popPort", this.popPort)
		.append("imapHost", this.imapHost)
		.append("imapPort", this.imapPort)
		.append("isDefault", this.isDefault)
		.append(this.SSL)
		.toString();
	}
	public int getSSL() {
		return this.SSL;
	}
	public void setSSL(int SSL) {
		this.SSL = SSL;
	}
	public int getIsDeleteRemote() {
		return this.isDeleteRemote;
	}
	public void setIsDeleteRemote(int isDeleteRemote) {
		this.isDeleteRemote = isDeleteRemote;
	}
	public int getValidate() {
		return this.validate;
	}
	public void setValidate(int validate) {
		this.validate = validate;
	}
	public int getIsHandleAttach() {
		return this.isHandleAttach;
	}
	public void setIsHandleAttach(int isHandleAttach) {
		this.isHandleAttach = isHandleAttach;
	}
}
