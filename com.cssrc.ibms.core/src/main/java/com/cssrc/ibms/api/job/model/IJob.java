package com.cssrc.ibms.api.job.model;

public interface IJob {

	public static final String NODE_KEY = "zwjb";

	Long getJobid();

	String getJobcode();

	String getJobname();

	Short getGrade();

}