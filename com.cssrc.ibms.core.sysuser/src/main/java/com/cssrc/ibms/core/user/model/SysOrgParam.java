package com.cssrc.ibms.core.user.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.api.sysuser.model.ISysOrgParam;
/**
 * 组织参数
 * <p>Title:SysOrgParam</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:24:23
 */
public class SysOrgParam extends SysBaseParam implements ISysOrgParam{
	protected Long orgId;
	
	protected Long orgParam_creatorId;// 创建人ID
	
	protected Date orgParam_createTime;// 创建时间
	
	protected Long orgParam_updateId;// 更改人ID
	
	protected Date orgParam_updateTime;// 更改时间

	protected Short orgParam_delFlag;// 是否删除
	
	public SysOrgParam() {
	}

	public SysOrgParam(ISysParam param) {
		this.paramId = param.getParamId();
		this.paramName = param.getParamName();
		this.dataType = param.getDataType();
		this.sourceKey = param.getSourceKey();
		this.sourceType = param.getSourceType();
		this.description = param.getDescription();
		this.status_ = param.getStatus_();
	}

	public Long getOrgParam_creatorId() {
		return orgParam_creatorId;
	}

	public void setOrgParam_creatorId(Long orgParam_creatorId) {
		this.orgParam_creatorId = orgParam_creatorId;
	}

	public Date getOrgParam_createTime() {
		return orgParam_createTime;
	}

	public void setOrgParam_createTime(Date orgParam_createTime) {
		this.orgParam_createTime = orgParam_createTime;
	}

	public Long getOrgParam_updateId() {
		return orgParam_updateId;
	}

	public void setOrgParam_updateId(Long orgParam_updateId) {
		this.orgParam_updateId = orgParam_updateId;
	}

	public Date getOrgParam_updateTime() {
		return orgParam_updateTime;
	}

	public void setOrgParam_updateTime(Date orgParam_updateTime) {
		this.orgParam_updateTime = orgParam_updateTime;
	}

	public Short getOrgParam_delFlag() {
		return orgParam_delFlag;
	}

	public void setOrgParam_delFlag(Short orgParam_delFlag) {
		this.orgParam_delFlag = orgParam_delFlag;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysOrgParam)) {
			return false;
		}
		SysOrgParam rhs = (SysOrgParam) object;
		return new EqualsBuilder().append(this.valueId, rhs.valueId).append(
				this.orgId, rhs.orgId).append(this.paramId, rhs.paramId)
				.append(this.paramValue, rhs.paramValue).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.valueId)
				.append(this.orgId).append(this.paramId)
				.append(this.paramValue).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("valueId", this.valueId)
				.append("orgId", this.orgId).append("paramId", this.paramId)
				.append("paramValue", this.paramValue).toString();
	}
}
