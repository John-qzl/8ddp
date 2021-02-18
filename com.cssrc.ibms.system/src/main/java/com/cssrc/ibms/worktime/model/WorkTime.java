package com.cssrc.ibms.worktime.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.worktime.IWorkTime;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.date.TimeUtil;

public class WorkTime extends BaseModel implements IWorkTime, Cloneable {
	protected Long id;
	protected Long settingId;
	protected String startTime;
	protected String endTime;
	protected String memo = "";

	protected String calDay = "";
	protected Date startDateTime;
	protected Date endDateTime;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}

	public Long getSettingId() {
		return this.settingId;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return this.endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getCalDay() {
		return this.calDay;
	}

	public void setCalDay(String calDay) {
		this.calDay = calDay;
		String start = calDay + " " + this.startTime + ":00";
		String end = calDay + " " + this.endTime + ":00";

		this.startDateTime = TimeUtil.getDateTimeByTimeString(start);
		this.endDateTime = TimeUtil.getDateTimeByTimeString(end);
		if (this.startDateTime.compareTo(this.endDateTime) > 0)
			this.endDateTime = TimeUtil.getNextDays(this.endDateTime, 1);
	}

	public boolean equals(Object object) {
		if (!(object instanceof WorkTime)) {
			return false;
		}
		WorkTime rhs = (WorkTime) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.settingId, rhs.settingId)
				.append(this.startTime, rhs.startTime)
				.append(this.endTime, rhs.endTime).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.settingId).append(this.startTime)
				.append(this.endTime).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("settingId", this.settingId)
				.append("startTime", this.startTime)
				.append("endTime", this.endTime).toString();
	}

	public Object clone() {
		WorkTime obj = null;
		try {
			obj = (WorkTime) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
