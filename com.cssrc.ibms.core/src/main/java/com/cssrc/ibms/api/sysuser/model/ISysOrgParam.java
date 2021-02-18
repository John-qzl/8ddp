package com.cssrc.ibms.api.sysuser.model;

public interface ISysOrgParam {

	public abstract void setOrgId(Long orgId);

	public abstract Long getOrgId();

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

	public abstract String getParamValue();

}