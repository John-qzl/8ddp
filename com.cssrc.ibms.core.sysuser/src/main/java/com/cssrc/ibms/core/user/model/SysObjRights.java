package com.cssrc.ibms.core.user.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.ISysObjRights;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 
 * @author Yangbo 2016-7-20
 *
 */
public class SysObjRights extends BaseModel implements ISysObjRights
{

	protected Long id;
	protected String objType;
	protected Long objectId;
	protected Long ownerId;
	protected String owner;
	protected String rightType;

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getId()
	{
		return this.id;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getObjType()
	{
		return this.objType;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getObjectId()
	{
		return this.objectId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Long getOwnerId()
	{
		return this.ownerId;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner()
	{
		return this.owner;
	}
	public void setRightType(String rightType) {
		this.rightType = rightType;
	}

	public String getRightType()
	{
		return this.rightType;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof SysObjRights))
		{
			return false;
		}
		SysObjRights rhs = (SysObjRights)object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.objType, rhs.objType)
		.append(this.objectId, rhs.objectId)
		.append(this.ownerId, rhs.ownerId)
		.append(this.owner, rhs.owner)
		.append(this.rightType, rhs.rightType)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id)
		.append(this.objType)
		.append(this.objectId)
		.append(this.ownerId)
		.append(this.owner)
		.append(this.rightType)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("objType", this.objType)
		.append("objectId", this.objectId)
		.append("ownerId", this.ownerId)
		.append("owner", this.owner)
		.append("rightType", this.rightType)
		.toString();
	}
}

