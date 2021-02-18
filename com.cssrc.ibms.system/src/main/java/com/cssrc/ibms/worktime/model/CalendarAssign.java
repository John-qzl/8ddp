 package com.cssrc.ibms.worktime.model;
 
 import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.worktime.ICalendarAssign;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
 
 public class CalendarAssign extends BaseModel implements ICalendarAssign
 {
   public static int User = 1;
 
   public static int Organization = 2;
   protected Long id;
   protected Long canlendarId;
   protected Short assignType;
   protected Long assignId;
   protected String calendarName;
   protected String assignUserName;
 
   public void setId(Long id)
   {
     this.id = id;
   }
 
   public Long getId()
   {
     return this.id;
   }
 
   public void setCanlendarId(Long canlendarId)
   {
     this.canlendarId = canlendarId;
   }
 
   public Long getCanlendarId()
   {
     return this.canlendarId;
   }
 
   public void setAssignType(Short assignType)
   {
     this.assignType = assignType;
   }
 
   public Short getAssignType()
   {
     return this.assignType;
   }
 
   public void setAssignId(Long assignId)
   {
     this.assignId = assignId;
   }
 
   public Long getAssignId()
   {
     return this.assignId;
   }
 
   public String getCalendarName() {
     return this.calendarName;
   }
   public void setCalendarName(String calendarName) {
     this.calendarName = calendarName;
   }
 
   public String getAssignUserName() {
     return this.assignUserName;
   }
   public void setAssignUserName(String assignUserName) {
     this.assignUserName = assignUserName;
   }
 
   public boolean equals(Object object)
   {
     if (!(object instanceof CalendarAssign))
     {
       return false;
     }
     CalendarAssign rhs = (CalendarAssign)object;
     return new EqualsBuilder()
       .append(this.id, rhs.id)
       .append(this.canlendarId, rhs.canlendarId)
       .append(this.assignType, rhs.assignType)
       .append(this.assignId, rhs.assignId)
       .isEquals();
   }
 
   public int hashCode()
   {
     return new HashCodeBuilder(-82280557, -700257973)
       .append(this.id)
       .append(this.canlendarId)
       .append(this.assignType)
       .append(this.assignId)
       .toHashCode();
   }
 
   public String toString()
   {
     return new ToStringBuilder(this)
       .append("id", this.id)
       .append("canlendarId", this.canlendarId)
       .append("assignType", this.assignType)
       .append("assignId", this.assignId)
       .toString();
   }
 }
