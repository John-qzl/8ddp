package com.cssrc.ibms.api.sysuser.model;

public interface IUserUnder {

	public abstract void setId(Long id);

	public abstract Long getId();

	public abstract void setUserid(Long userid);

	public abstract Long getUserid();

	public abstract void setUnderuserid(Long underuserid);

	public abstract Long getUnderuserid();

	public abstract void setUnderusername(String underusername);

	public abstract String getUnderusername();

	public abstract String getLeaderName();

	public abstract void setLeaderName(String leaderName);

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

}