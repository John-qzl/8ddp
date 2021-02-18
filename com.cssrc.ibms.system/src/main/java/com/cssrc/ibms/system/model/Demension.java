package com.cssrc.ibms.system.model;

import java.util.Date;

import com.cssrc.ibms.api.system.model.IDemension;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 维度类(new)
 * <p>Title:Demension</p>
 * @author Yangbo 
 * @date 2016-8-1下午04:04:38
 */
public class Demension extends BaseModel implements IDemension{
	public static Long ADMINSTRATION = Long.valueOf(1L);

	public static Demension positionDem = new Demension();
	@SysFieldDescription(detail="维度编号")
	private Long demId;
	@SysFieldDescription(detail="维度名称")
	private String demName;
	@SysFieldDescription(detail="维度描述")
	private String demDesc;
	@SysFieldDescription(detail="维度组织path")
	private String demOrgPath;
	@SysFieldDescription(detail="创建人ID")
	protected Long demension_creatorId;// 创建人ID
	@SysFieldDescription(detail="创建时间")
	protected Date demension_createTime;// 创建时间
	@SysFieldDescription(detail="更改人ID")
	protected Long demension_updateId;// 更改人ID
	@SysFieldDescription(detail="更改时间")
	protected Date demension_updateTime;// 更改时间
	@SysFieldDescription(detail="是否删除",maps="{\"1\":\"已删除\",\"0\":\"未删除\"}")
	protected Short demension_delFlag;// 是否删除

	static {
		positionDem.setDemId(Long.valueOf(0L));
		positionDem.setDemName("岗位维度");
	}

	public Long getDemension_creatorId() {
		return demension_creatorId;
	}

	public void setDemension_creatorId(Long demension_creatorId) {
		this.demension_creatorId = demension_creatorId;
	}

	public Date getDemension_createTime() {
		return demension_createTime;
	}

	public void setDemension_createTime(Date demension_createTime) {
		this.demension_createTime = demension_createTime;
	}

	public Long getDemension_updateId() {
		return demension_updateId;
	}

	public void setDemension_updateId(Long demension_updateId) {
		this.demension_updateId = demension_updateId;
	}

	public Date getDemension_updateTime() {
		return demension_updateTime;
	}

	public void setDemension_updateTime(Date demension_updateTime) {
		this.demension_updateTime = demension_updateTime;
	}

	public Short getDemension_delFlag() {
		return demension_delFlag;
	}

	public void setDemension_delFlag(Short demension_delFlag) {
		this.demension_delFlag = demension_delFlag;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public Long getDemId() {
		return this.demId;
	}

	public void setDemName(String demName) {
		this.demName = demName;
	}

	public String getDemName() {
		return this.demName;
	}

	public void setDemDesc(String demDesc) {
		this.demDesc = demDesc;
	}

	public String getDemDesc() {
		return this.demDesc;
	}

	public String getDemOrgPath() {
		return this.demOrgPath;
	}

	public void setDemOrgPath(String demOrgPath) {
		this.demOrgPath = demOrgPath;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Demension)) {
			return false;
		}
		Demension rhs = (Demension) object;
		return new EqualsBuilder().append(this.demId, rhs.demId).append(
				this.demName, rhs.demName).append(this.demDesc, rhs.demDesc)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.demId)
				.append(this.demName).append(this.demDesc).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("demId", this.demId).append(
				"demName", this.demName).append("demDesc", this.demDesc)
				.toString();
	}
}
