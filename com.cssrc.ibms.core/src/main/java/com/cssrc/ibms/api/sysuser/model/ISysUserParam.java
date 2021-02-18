package com.cssrc.ibms.api.sysuser.model;

public interface ISysUserParam {

	public abstract void setUserId(Long userId);

	public abstract Long getUserId();

	public abstract boolean equals(Object object);

	public abstract int hashCode();

	public abstract String toString();

	public abstract String getDataType();

	public abstract Object getParamValue();

	public abstract Object getParamIntValue();

	public abstract Object getParamDateValue();

}