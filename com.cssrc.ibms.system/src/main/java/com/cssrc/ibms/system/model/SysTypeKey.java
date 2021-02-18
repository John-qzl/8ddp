package com.cssrc.ibms.system.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysTypeKey;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 分类标识管理
 * <p>
 * Title:SysTypeKey
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-29下午02:26:51
 */
public class SysTypeKey extends BaseModel implements ISysTypeKey{

	
	protected Long typeKeyId = Long.valueOf(0L); // typeid
	protected String typeName;
	protected String typeKey;
	protected Integer sn = Integer.valueOf(0);
	protected Integer flag = Integer.valueOf(0);
	protected Integer type = Integer.valueOf(0);

	protected Long typekey_creatorId;// 创建人ID
	
	protected Date typekey_createTime;// 创建时间
	
	protected Long typekey_updateId;// 更改人ID
	
	protected Date typekey_updateTime;// 更改时间

	public Long getTypekey_creatorId() {
		return typekey_creatorId;
	}

	public void setTypekey_creatorId(Long typekey_creatorId) {
		this.typekey_creatorId = typekey_creatorId;
	}

	public Date getTypekey_createTime() {
		return typekey_createTime;
	}

	public void setTypekey_createTime(Date typekey_createTime) {
		this.typekey_createTime = typekey_createTime;
	}

	public Long getTypekey_updateId() {
		return typekey_updateId;
	}

	public void setTypekey_updateId(Long typekey_updateId) {
		this.typekey_updateId = typekey_updateId;
	}

	public Date getTypekey_updateTime() {
		return typekey_updateTime;
	}

	public void setTypekey_updateTime(Date typekey_updateTime) {
		this.typekey_updateTime = typekey_updateTime;
	}

	public Long getTypeKeyId() {
		return this.typeKeyId;
	}

	public void setTypeKeyId(Long typeKeyId) {
		this.typeKeyId = typeKeyId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeKey() {
		return typeKey;
	}

	public void setTypeKey(String typeKey) {
		this.typeKey = typeKey;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysTypeKey)) {
			return false;
		}
		SysTypeKey rhs = (SysTypeKey) object;
		return new EqualsBuilder().append(this.typeKeyId, rhs.typeKeyId)
				.append(this.typeName, rhs.typeName).append(this.typeKey,
						rhs.typeKey).append(this.sn, rhs.sn).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973)
				.append(this.typeKeyId).append(this.typeName).append(
						this.typeKey).append(this.sn).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("typekeyId", this.typeKeyId)
				.append("typeName", this.typeName).append("typeKey",
						this.typeKey).append("sn", this.sn).toString();
	}
}