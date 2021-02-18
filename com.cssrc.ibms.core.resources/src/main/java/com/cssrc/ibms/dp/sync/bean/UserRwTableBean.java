package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class UserRwTableBean {
	
	private Set user = new HashSet(0);
	private String userid;
	private String username;
	private String rwid;
	private String rwname;
	private String tableid;
	private String tablename;
	private String location;
	
	public Set getUser() {
		return user;
	}
	public void setUser(Set user) {
		this.user = user;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid) {
		this.rwid = rwid;
	}
	public String getRwname() {
		return rwname;
	}
	public void setRwname(String rwname) {
		this.rwname = rwname;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
}
