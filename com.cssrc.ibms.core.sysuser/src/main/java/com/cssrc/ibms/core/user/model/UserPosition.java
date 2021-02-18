package com.cssrc.ibms.core.user.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 用户组织岗位表
 * <p>Title:UserPosition</p>
 * @author Yangbo 
 * @date 2016-8-2上午10:44:34
 */
@XmlRootElement(name = "userPosition")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserPosition extends BaseModel implements IUserPosition
{	
	@SysFieldDescription(detail="用户组织岗位表ID")
	protected Long userPosId;
	@SysFieldDescription(detail="组织ID")
	protected Long orgId;
	@SysFieldDescription(detail="用户ID")
	protected Long userId;
	@SysFieldDescription(detail="岗位ID")
	protected Long posId;
	@SysFieldDescription(detail="是否主岗位")
	protected Short isPrimary = Short.valueOf((short) 0);//主岗位
	@SysFieldDescription(detail="是否负责人")
	protected Short isCharge = CHARRGE_NO;//负责人
	@SysFieldDescription(detail="是否删除")
	protected Short isDelete = DELETE_NO;
	@SysFieldDescription(detail="岗位名称")
	protected String posName;
	@SysFieldDescription(detail="中文名")
	protected String fullname;// 中文名
	@SysFieldDescription(detail="用户名")
	protected String username;//用户帐户 等同于 account
	@SysFieldDescription(detail="职位ID")
	protected Long jobId;
	@SysFieldDescription(detail="职位名称")
	protected String jobName;
	@SysFieldDescription(detail="公司")
	protected String company;
	@SysFieldDescription(detail="公司ID")
	protected Long companyId;
	@SysFieldDescription(detail="用户组织岗位表状态")
	protected Short status;
	@SysFieldDescription(detail="组织名称")
	protected String orgName;
	@SysFieldDescription(detail="chargeName")
	protected String chargeName = "";
	@SysFieldDescription(detail="用户组织岗位表创建人ID")
	protected Long position_creatorId;// 创建人ID
	@SysFieldDescription(detail="用户组织岗位表创建时间")
	protected Date position_createTime;// 创建时间
	@SysFieldDescription(detail="用户组织岗位表更改人ID")
	protected Long position_updateId;// 更改人ID
	@SysFieldDescription(detail="用户组织岗位表更改时间")
	protected Date position_updateTime;// 更改时间

	public String getFullname()
	{
		return this.fullname;
	}

	public Long getPosition_creatorId() {
		return position_creatorId;
	}

	public void setPosition_creatorId(Long position_creatorId) {
		this.position_creatorId = position_creatorId;
	}

	public Date getPosition_createTime() {
		return position_createTime;
	}

	public void setPosition_createTime(Date position_createTime) {
		this.position_createTime = position_createTime;
	}

	public Long getPosition_updateId() {
		return position_updateId;
	}

	public void setPosition_updateId(Long position_updateId) {
		this.position_updateId = position_updateId;
	}

	public Date getPosition_updateTime() {
		return position_updateTime;
	}

	public void setPosition_updateTime(Date position_updateTime) {
		this.position_updateTime = position_updateTime;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return this.jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getPosName() {
		return this.posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public Long getUserPosId() {
		return userPosId;
	}

	public void setUserPosId(Long userPosId) {
		this.userPosId = userPosId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getPosId() {
		return this.posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Short getIsPrimary() {
		return this.isPrimary;
	}

	public void setIsPrimary(Short isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Short getIsCharge() {
		return this.isCharge;
	}

	public void setIsCharge(Short isCharge) {
		this.isCharge = isCharge;
	}

	public Short getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Short isDelete) {
		this.isDelete = isDelete;
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
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getChargeName() {
		return chargeName;
	}

	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	public boolean equals(Object object)
	{
		if (!(object instanceof UserPosition))
		{
			return false;
		}
		UserPosition rhs = (UserPosition)object;
		return new EqualsBuilder()
		.append(this.userPosId, rhs.userPosId)
		.append(this.orgId, rhs.orgId)
		.append(this.posId, rhs.posId)
		.append(this.userId, rhs.userId)
		.append(this.isPrimary, rhs.isPrimary)
		.append(this.isCharge, rhs.isCharge)
		.append(this.isDelete, rhs.isDelete)
		.isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.userPosId)
		.append(this.orgId)
		.append(this.posId)
		.append(this.userId)
		.append(this.isPrimary)
		.append(this.isCharge)
		.append(this.isDelete)
		.toHashCode();
	}

	public String toString()
	{
		return new ToStringBuilder(this)
		.append("userPosId", this.userPosId)
		.append("orgId", this.orgId)
		.append("posId", this.posId)
		.append("userId", this.userId)
		.append("isPrimary", this.isPrimary)
		.append("isCharge", this.isCharge)
		.append("isDelete", this.isDelete)
		.toString();
	}
}

