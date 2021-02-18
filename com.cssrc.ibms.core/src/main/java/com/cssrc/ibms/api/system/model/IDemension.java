package com.cssrc.ibms.api.system.model;

public interface IDemension {
	public static Long ADMINSTRATION = Long.valueOf(1L);

	Long getDemId();

	String getDemName();

	String getDemDesc();


}