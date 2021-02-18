package com.cssrc.ibms.dp.sync.model;

import java.util.HashSet;
import java.util.Set;

public class SyncUserXML {
	
	private Long userId;
	private String userName;
	private String passWord;
	private String displayName;
	private String projectId;
	private String projectName;
	private String ttidandname;					//现在只传岗位ID
	private Set rw = new HashSet(0);
	private String commanderId;  //所负责节点id
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCommanderId() {
		return commanderId;
	}

	public void setCommanderId(String commanderId) {
		this.commanderId = commanderId;
	}

	public Set getRw() {
		return rw;
	}
	public void setRw(Set rw) {
		this.rw = rw;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectIDStr) {
		this.projectId = projectIDStr;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public void setUserInfoBean(SyncUser user){
		this.userId = user.getId();
		this.userName = user.getUserName().toString();
		this.passWord = user.getPassWord().toString();
		this.displayName = user.getDisplayName().toString();
		this.email = user.getEmail().toString();
	}
	public String getTtidandname() {
		return ttidandname;
	}
	public void setTtidandname(String ttidandname) {
		this.ttidandname = ttidandname;
	}
	
}
