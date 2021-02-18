package com.cssrc.ibms.core.user.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.ISysOrgType;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 组织类型类
 * <p>Title:SysOrgType</p>
 * @author Yangbo 
 * @date 2016-8-2上午08:50:58
 */
public class SysOrgType extends BaseModel implements ISysOrgType{
	@SysFieldDescription(detail="ID")
	protected Long id;
	@SysFieldDescription(detail="维度ID")
	protected Long demId;
	@SysFieldDescription(detail="名称")
	protected String name;
	@SysFieldDescription(detail="级别")
	protected Long levels;
	@SysFieldDescription(detail="维度名")
	protected String memo;
	@SysFieldDescription(detail="显示图标")
	protected String icon;
	@SysFieldDescription(detail="创建人ID")
	protected Long orgType_creatorId;// 创建人ID
	@SysFieldDescription(detail="创建时间")
	protected Date orgType_createTime;// 创建时间
	@SysFieldDescription(detail="更改人ID")
	protected Long orgType_updateId;// 更改人ID
	@SysFieldDescription(detail="更改时间")
	protected Date orgType_updateTime;// 更改时间
	@SysFieldDescription(detail="是否删除",maps="{\"1\":\"已删除\",\"0\":\"未删除\"}")
	protected Short orgType_delFlag = Short.valueOf((short) 0);// 是否删除
	
	public Long getOrgType_creatorId() {
		return orgType_creatorId;
	}

	public void setOrgType_creatorId(Long orgType_creatorId) {
		this.orgType_creatorId = orgType_creatorId;
	}

	public Date getOrgType_createTime() {
		return orgType_createTime;
	}

	public void setOrgType_createTime(Date orgType_createTime) {
		this.orgType_createTime = orgType_createTime;
	}

	public Long getOrgType_updateId() {
		return orgType_updateId;
	}

	public void setOrgType_updateId(Long orgType_updateId) {
		this.orgType_updateId = orgType_updateId;
	}

	public Date getOrgType_updateTime() {
		return orgType_updateTime;
	}

	public void setOrgType_updateTime(Date orgType_updateTime) {
		this.orgType_updateTime = orgType_updateTime;
	}

	public Short getOrgType_delFlag() {
		return orgType_delFlag;
	}

	public void setOrgType_delFlag(Short orgType_delFlag) {
		this.orgType_delFlag = orgType_delFlag;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setDemId(Long demId) {
		this.demId = demId;
	}

	public Long getDemId() {
		return this.demId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setLevels(Long levels) {
		this.levels = levels;
	}

	public Long getLevels() {
		return this.levels;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysOrgType)) {
			return false;
		}
		SysOrgType rhs = (SysOrgType) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.demId,
				rhs.demId).append(this.name, rhs.name).append(this.levels,
				rhs.levels).append(this.memo, rhs.memo).append(this.icon,
				rhs.icon).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.demId).append(this.name).append(this.levels)
				.append(this.memo).append(this.icon).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("demId",
				this.demId).append("name", this.name).append("levels",
				this.levels).append("memo", this.memo)
				.append("icon", this.icon).toString();
	}
}
