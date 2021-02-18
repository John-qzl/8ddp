package com.cssrc.ibms.dp.sync.model;
/**
 * 
 * @author lyc
 *
 */
public class SyncUser {
	
	private Long userId;
	private String userName;
	private String passWord;
	private String allname;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return userId;
	}
	public void setId(Long id) {
		this.userId = id;
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
		return allname;
	}
	public void setDisplayName(String displayName) {
		this.allname = displayName;
	}
	
}
