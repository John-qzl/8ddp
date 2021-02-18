package com.cssrc.ibms.core.user.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 *岗位
 * <p>Title:Position</p>
 * @author Yangbo 
 * @date 2016-8-4上午08:36:33
 */@XmlRootElement(name = "position")
 @XmlAccessorType(XmlAccessType.FIELD)
public class Position extends BaseModel implements IPosition{
	private static final long serialVersionUID = -7944651413880020519L;
	@SysFieldDescription(detail="岗位ID")
	protected Long posId;
	@SysFieldDescription(detail="岗位名称")
	protected String posName;
	@SysFieldDescription(detail="组织描述")
	protected String posDesc;
	@SysFieldDescription(detail="岗位代码")
	protected String posCode;
	@SysFieldDescription(detail="组织ID")
	protected Long orgId;
	@SysFieldDescription(detail="职务ID")
	protected Long jobId;
	@SysFieldDescription(detail="是否删除",maps="{\"1\":\"已删除\",\"0\":\"未删除\"}")
	protected Long isDelete = Long.valueOf(0L);
	@SysFieldDescription(detail="是否主岗位",maps="{\"1\":\"是\",\"0\":\"否\"}")
	protected Short isPrimary;
	@SysFieldDescription(detail="组织代码")
	protected String orgCode;
	@SysFieldDescription(detail="职务代码")
	protected String jobCode;
	@SysFieldDescription(detail="组织名称")
	protected String orgName;
	@SysFieldDescription(detail="用户名")
	protected String userNames;
	@SysFieldDescription(detail="职务名称")
	protected String jobName;
	@SysFieldDescription(detail="分级职务名称")
	protected String jobGradeName;
	@SysFieldDescription(detail="公司名")
	protected String company;
	@SysFieldDescription(detail="公司ID")
	protected Long companyId;
	@SysFieldDescription(detail="创建人ID")
	protected Long pos_creatorId;// 创建人ID
	@SysFieldDescription(detail="创建时间")
	protected Date pos_createTime;// 创建时间
	@SysFieldDescription(detail="更改人ID")
	protected Long pos_updateId;// 更改人ID
	@SysFieldDescription(detail="更改时间")
	protected Date pos_updateTime;// 更改时间
	
	public Long getPos_creatorId() {
		return pos_creatorId;
	}

	public void setPos_creatorId(Long pos_creatorId) {
		this.pos_creatorId = pos_creatorId;
	}

	public Date getPos_createTime() {
		return pos_createTime;
	}

	public void setPos_createTime(Date pos_createTime) {
		this.pos_createTime = pos_createTime;
	}

	public Long getPos_updateId() {
		return pos_updateId;
	}

	public void setPos_updateId(Long pos_updateId) {
		this.pos_updateId = pos_updateId;
	}

	public Date getPos_updateTime() {
		return pos_updateTime;
	}

	public void setPos_updateTime(Date pos_updateTime) {
		this.pos_updateTime = pos_updateTime;
	}

	public String getUserNames() {
		return this.userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Short getIsPrimary() {
		return this.isPrimary;
	}

	public void setIsPrimary(Short isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Long getPosId() {
		return this.posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public String getPosName() {
		return this.posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getPosDesc() {
		return this.posDesc;
	}

	public void setPosDesc(String posDesc) {
		this.posDesc = posDesc;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Long isDelete) {
		this.isDelete = isDelete;
	}

	public String getPosCode() {
		return this.posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getJobCode() {
		return this.jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobGradeName() {
		return this.jobGradeName;
	}

	public void setJobGradeName(String jobGradeName) {
		this.jobGradeName = jobGradeName;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Position)) {
			return false;
		}
		Position rhs = (Position) object;
		return new EqualsBuilder().append(this.posId, rhs.posId).append(
				this.posName, rhs.posName).append(this.posDesc, rhs.posDesc)
				.append(this.orgId, rhs.orgId).append(this.jobId, rhs.jobId)
				.append(this.isDelete, rhs.isDelete).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.posId)
				.append(this.posName).append(this.posDesc).append(this.orgId)
				.append(this.jobId).append(this.isDelete).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("posId", this.posId).append(
				"posName", this.posName).append("posDesc", this.posDesc)
				.append("orgId", this.orgId).append("jobId", this.jobId)
				.append("isDelete", this.isDelete).toString();
	}
}
