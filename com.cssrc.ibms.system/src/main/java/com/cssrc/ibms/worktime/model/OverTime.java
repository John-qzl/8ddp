 package com.cssrc.ibms.worktime.model;
 
 import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.worktime.IOverTime;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
 
 public class OverTime extends BaseModel implements IOverTime
 {
   protected Long id;
   protected Long userId;
   protected Date startTime;
   protected Date endTime;
   protected String userName;
   protected String sTime;
   protected String eTime;
   protected Short workType;
   protected String subject;
   protected String memo;
 
   public void setId(Long id)
   {
     this.id = id;
   }
 
   public Long getId()
   {
     return this.id;
   }
 
   public void setUserId(Long userId)
   {
     this.userId = userId;
   }
 
   public Long getUserId()
   {
     return this.userId;
   }
 
   public void setStartTime(Date startTime)
   {
     this.startTime = startTime;
   }
 
   public Date getStartTime()
   {
     return this.startTime;
   }
 
   public void setEndTime(Date endTime)
   {
     this.endTime = endTime;
   }
 
   public Date getEndTime()
   {
     return this.endTime;
   }
 
   public Short getWorkType()
   {
     return this.workType;
   }
 
   public String getSubject()
   {
     return this.subject;
   }
   public void setSubject(String subject) {
     this.subject = subject;
   }
 
   public String getMemo()
   {
     return this.memo;
   }
   public void setMemo(String memo) {
     this.memo = memo;
   }
   public void setWorkType(Short workType) {
     this.workType = workType;
   }
   public String getUserName() {
     return this.userName;
   }
   public void setUserName(String userName) {
     this.userName = userName;
   }
 
   public String getsTime() {
     return this.sTime;
   }
   public void setsTime(String sTime) {
     this.sTime = sTime;
   }
   public String geteTime() {
     return this.eTime;
   }
   public void seteTime(String eTime) {
     this.eTime = eTime;
   }
 
   public boolean equals(Object object)
   {
     if (!(object instanceof OverTime))
     {
       return false;
     }
     OverTime rhs = (OverTime)object;
     return new EqualsBuilder()
       .append(this.id, rhs.id)
       .append(this.userId, rhs.userId)
       .append(this.startTime, rhs.startTime)
       .append(this.endTime, rhs.endTime)
       .isEquals();
   }
 
   public int hashCode()
   {
     return new HashCodeBuilder(-82280557, -700257973)
       .append(this.id)
       .append(this.userId)
       .append(this.startTime)
       .append(this.endTime)
       .toHashCode();
   }
 
   public String toString()
   {
     return new ToStringBuilder(this)
       .append("id", this.id)
       .append("userId", this.userId)
       .append("startTime", this.startTime)
       .append("endTime", this.endTime)
       .toString();
   }
 }
