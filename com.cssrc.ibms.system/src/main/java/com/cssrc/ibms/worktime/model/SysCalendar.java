package com.cssrc.ibms.worktime.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.worktime.ISysCalendar;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class SysCalendar extends BaseModel implements ISysCalendar {
	protected Long id = Long.valueOf(0L);
	protected String name;
	protected String memo;
	protected Short isDefault = Short.valueOf((short) 0);

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setIsDefault(Short isDefault) {
		this.isDefault = isDefault;
	}

	public Short getIsDefault() {
		return this.isDefault;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysCalendar)) {
			return false;
		}
		SysCalendar rhs = (SysCalendar) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.name, rhs.name).append(this.memo, rhs.memo)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.memo).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("name", this.name).append("memo", this.memo).toString();
	}
}
