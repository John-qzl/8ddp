package com.cssrc.ibms.api.sysuser.model;

public interface ISysOrgType {

	public abstract String getIcon();

	public abstract void setIcon(String icon);

	public abstract void setId(Long id);

	public abstract Long getId();

	public abstract void setDemId(Long demId);

	public abstract Long getDemId();

	public abstract void setName(String name);

	public abstract String getName();

	public abstract void setLevels(Long levels);

	public abstract Long getLevels();

	public abstract void setMemo(String memo);

	public abstract String getMemo();

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

}