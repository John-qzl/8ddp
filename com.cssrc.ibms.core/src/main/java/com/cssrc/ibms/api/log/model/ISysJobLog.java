package com.cssrc.ibms.api.log.model;

import java.util.Date;

public abstract interface ISysJobLog
{
  public abstract void setLogId(Long paramLong);

  public abstract Long getLogId();

  public abstract void setJobName(String paramString);

  public abstract String getJobName();

  public abstract void setTrigName(String paramString);

  public abstract String getTrigName();

  public abstract void setStartTime(Date paramDate);

  public abstract Date getStartTime();

  public abstract void setEndTime(Date paramDate);

  public abstract Date getEndTime();

  public abstract void setContent(String paramString);

  public abstract String getContent();

  public abstract void setState(int paramInt);

  public abstract int getState();

  public abstract void setRunTime(Long paramLong);

  public abstract Long getRunTime();
}
