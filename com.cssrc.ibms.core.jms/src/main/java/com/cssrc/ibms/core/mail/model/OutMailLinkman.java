package com.cssrc.ibms.core.mail.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class OutMailLinkman extends BaseModel
{
	protected Long linkId;
	protected Long userId;
	protected Date sendTime;
	protected String linkName;
	protected String linkAddress;
	protected Long mailId;
	protected int sendTimes = 0;

	public Long getMailId()
	{
		return this.mailId;
	}

	public void setMailId(Long mailid) {
		this.mailId = this.mailId;
	}

	public int getSendTimes()
	{
		return this.sendTimes;
	}

	public void setSendTimes(int sendTimes) {
		this.sendTimes = sendTimes;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Long getLinkId()
	{
		return this.linkId;
	}

	public Long getUserId()
	{
		return this.userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public void setSendTime(Date sendTime)
	{
		this.sendTime = sendTime;
	}

	public Date getSendTime()
	{
		return this.sendTime;
	}

	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}

	public String getLinkName()
	{
		return this.linkName;
	}

	public void setLinkAddress(String linkAddress)
	{
		this.linkAddress = linkAddress;
	}

	public String getLinkAddress()
	{
		return this.linkAddress;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof OutMailLinkman))
		{
			return false;
		}
		OutMailLinkman rhs = (OutMailLinkman)object;
		return new EqualsBuilder()
		.append(this.linkId, rhs.linkId)
		.append(this.sendTime, rhs.sendTime)
		.append(this.sendTimes, rhs.sendTimes)
		.append(this.linkName, rhs.linkName)
		.append(this.linkAddress, rhs.linkAddress)
		.append(this.mailId, rhs.mailId)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.linkId)
		.append(this.sendTime)
		.append(this.sendTimes)
		.append(this.linkName)
		.append(this.linkAddress)
		.append(this.mailId)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("linkId", this.linkId)
		.append("sendTime", this.sendTime)
		.append("sendTimes", this.sendTimes)
		.append("linkName", this.linkName)
		.append("linkAddress", this.linkAddress)
		.append("mailId", this.mailId)
		.toString();
	}
}


