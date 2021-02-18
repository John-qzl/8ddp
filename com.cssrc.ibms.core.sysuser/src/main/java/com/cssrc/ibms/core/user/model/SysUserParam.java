package com.cssrc.ibms.core.user.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.api.sysuser.model.ISysUserParam;
/**
 * 用户自定义参数
 * <p>Title:SysUserParam</p>
 * @author Yangbo 
 * @date 2016-8-19上午11:16:13
 */
public class SysUserParam extends SysBaseParam implements ISysUserParam{
	protected Long userId;
	protected Long userParam_creatorId;// 创建人ID
	
	protected Date userParam_createTime;// 创建时间
	
	protected Long userParam_updateId;// 更改人ID
	
	protected Date userParam_updateTime;// 更改时间

	protected Short userParam_delFlag;// 是否删除
	
	public SysUserParam() {
	}

	public SysUserParam(ISysParam param) {
		this.paramId = param.getParamId();
		this.paramName = param.getParamName();
		this.dataType = param.getDataType();
		this.sourceKey = param.getSourceKey();
		this.sourceType = param.getSourceType();
		this.description = param.getDescription();
		this.status_ = param.getStatus_();
	}

	public Long getUserParam_creatorId() {
		return userParam_creatorId;
	}

	public void setUserParam_creatorId(Long userParam_creatorId) {
		this.userParam_creatorId = userParam_creatorId;
	}

	public Date getUserParam_createTime() {
		return userParam_createTime;
	}

	public void setUserParam_createTime(Date userParam_createTime) {
		this.userParam_createTime = userParam_createTime;
	}

	public Long getUserParam_updateId() {
		return userParam_updateId;
	}

	public void setUserParam_updateId(Long userParam_updateId) {
		this.userParam_updateId = userParam_updateId;
	}

	public Date getUserParam_updateTime() {
		return userParam_updateTime;
	}

	public void setUserParam_updateTime(Date userParam_updateTime) {
		this.userParam_updateTime = userParam_updateTime;
	}

	public Short getUserParam_delFlag() {
		return userParam_delFlag;
	}

	public void setUserParam_delFlag(Short userParam_delFlag) {
		this.userParam_delFlag = userParam_delFlag;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysUserParam)) {
			return false;
		}
		SysUserParam rhs = (SysUserParam) object;
		return new EqualsBuilder().append(this.valueId, rhs.valueId).append(
				this.userId, rhs.userId).append(this.paramId, rhs.paramId)
				.append(this.paramValue, rhs.paramValue).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.valueId)
				.append(this.userId).append(this.paramId).append(
						this.paramValue).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("valueId", this.valueId)
				.append("userId", this.userId).append("paramId", this.paramId)
				.append("paramValue", this.paramValue).toString();
	}
}
