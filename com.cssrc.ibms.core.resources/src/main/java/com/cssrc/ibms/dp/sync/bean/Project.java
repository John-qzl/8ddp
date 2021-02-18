package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * 策划表
 * @author lyc
 *
 */
public class Project {
	
	private long projectId;
	private String projectName;
	private String sscppc;
	private String sscpmc;
	private String ssxh;
	private Set<TableInstance> instanSet = new HashSet(0);

	public String getSsxh() {
		return ssxh;
	}

	public void setSsxh(String ssxh) {
		this.ssxh = ssxh;
	}

	public String getSscpmc() {
		return sscpmc;
	}

	public void setSscpmc(String sscpmc) {
		this.sscpmc = sscpmc;
	}

	public String getSscppc() {
		return sscppc;
	}

	public void setSscppc(String sscppc) {
		this.sscppc = sscppc;
	}

	public Set<TableInstance> getInstanSet() {
		return instanSet;
	}
	public void setInstanSet(Set<TableInstance> instanSet) {
		this.instanSet = instanSet;
	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
