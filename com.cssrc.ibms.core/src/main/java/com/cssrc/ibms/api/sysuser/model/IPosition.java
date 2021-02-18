package com.cssrc.ibms.api.sysuser.model;

public abstract interface IPosition
{
  public abstract Long getPosId();

  public abstract void setPosId(Long paramLong);

  public abstract String getPosName();

  public abstract void setPosName(String paramString);

  public abstract String getPosDesc();

  public abstract void setPosDesc(String paramString);

  public abstract String getPosCode();

  public abstract void setPosCode(String paramString);

  public abstract Long getOrgId();

  public abstract void setOrgId(Long paramLong);

  public abstract Long getJobId();

  public abstract void setJobId(Long paramLong);
}

