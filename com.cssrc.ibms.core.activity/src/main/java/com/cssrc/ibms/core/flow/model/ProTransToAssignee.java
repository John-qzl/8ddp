package com.cssrc.ibms.core.flow.model;

public class ProTransToAssignee {
	public static Integer STATUS_CHECKING = 1;

	public static Integer STATUS_CHECKED = 2;
	Long userId;
	String userName;
	Integer status;
	String parentTaskId;

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getParentTaskId() {
		return this.parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
}
