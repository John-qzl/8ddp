package com.cssrc.ibms.core.job.scheduler;
/**
 * 
 * <p>Title:PlanObject</p>
 * @author Yangbo 
 * @date 2016-8-4下午05:35:07
 */
public class PlanObject {
	private int type = 0;

	private String timeInterval = "";

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTimeInterval() {
		return this.timeInterval;
	}

	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
}
