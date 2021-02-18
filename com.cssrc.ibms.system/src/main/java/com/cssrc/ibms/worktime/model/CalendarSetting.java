 package com.cssrc.ibms.worktime.model;
 
 import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.worktime.ICalendarSetting;
import com.cssrc.ibms.api.system.model.worktime.IWorkTime;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
 
 public class CalendarSetting extends BaseModel implements ICalendarSetting
 {
   protected Long id;
   protected Long calendarId;
   protected Short years;
   protected Short months;
   protected Short days;
   protected Short type;
   protected Long workTimeId;
   protected String calDay = "";
 
   protected boolean isLegal = false;
 
   protected String wtName = "";
   protected List<IWorkTime> workTimeList;
 
   public void setId(Long id)
   {
     this.id = id;
   }
 
   public Long getId()
   {
     return this.id;
   }
 
   public void setCalendarId(Long calendarId)
   {
     this.calendarId = calendarId;
   }
 
   public Long getCalendarId()
   {
     return this.calendarId;
   }
 
   public void setYears(Short years)
   {
     this.years = years;
   }
 
   public Short getYears()
   {
     return this.years;
   }
 
   public void setMonths(Short months)
   {
     this.months = months;
   }
 
   public Short getMonths()
   {
     return this.months;
   }
 
   public void setDays(Short days)
   {
     this.days = days;
   }
 
   public Short getDays()
   {
     return this.days;
   }
 
   public void setType(Short type)
   {
     this.type = type;
   }
 
   public Short getType()
   {
     return this.type;
   }
 
   public void setWorkTimeId(Long workTimeId)
   {
     this.workTimeId = workTimeId;
   }
 
   public Long getWorkTimeId()
   {
     return this.workTimeId;
   }
 
   public void setCalDay(String calDay)
   {
     this.calDay = calDay;
   }
 
   public String getCalDay() {
     return this.years + "-" + (this.months.shortValue() < 10 ? "0" + this.months : this.months) + "-" + (this.days.shortValue() < 10 ? "0" + this.days : this.days);
   }
 
   public String getWtName()
   {
     return this.wtName;
   }
   public void setWtName(String wtName) {
     this.wtName = wtName;
   }
 
   public boolean getIsLegal()
   {
     return this.isLegal;
   }
   public void setIsLegal(boolean isLegal) {
     this.isLegal = isLegal;
   }
 
   public List<IWorkTime> getWorkTimeList() {
     return this.workTimeList;
   }
   public void setWorkTimeList(List<IWorkTime> workTimeList) {
     this.workTimeList = workTimeList;
   }
 
   public boolean equals(Object object)
   {
     if (!(object instanceof CalendarSetting))
     {
       return false;
     }
     CalendarSetting rhs = (CalendarSetting)object;
     return new EqualsBuilder()
       .append(this.calendarId, rhs.calendarId)
       .append(this.years, rhs.years)
       .append(this.months, rhs.months)
       .append(this.days, rhs.days)
       .append(this.type, rhs.type)
       .append(this.workTimeId, rhs.workTimeId)
       .isEquals();
   }
 
   public int hashCode()
   {
     return new HashCodeBuilder(-82280557, -700257973)
       .append(this.id)
       .toHashCode();
   }
 
   public String toString()
   {
     return new ToStringBuilder(this)
       .append("id", this.id)
       .append("calendarId", this.calendarId)
       .append("years", this.years)
       .append("months", this.months)
       .append("days", this.days)
       .append("type", this.type)
       .append("workTimeId", this.workTimeId)
       .toString();
   }
 }