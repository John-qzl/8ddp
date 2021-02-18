package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * 表格实例映射bean
 * @author lyc
 *
 */
public class TableInstance{
	private Set signs = new HashSet(0);
	private Set rows = new HashSet(0);
	private Set conditions = new HashSet(0);
	private String name;
	private String file;
	private String remark;
	private String tableinstanceId;
	private String taskPic;
	private String path;
	private String rwid;
	private String rwname;
	private String version;
	private String postid;
	private String postname;
	private String postinstanceid;
	private String responsibility;
	private String isOK;
	private String isFinish;
	
	public Set getSigns() {
		return signs;
	}
	public void setSigns(Set signs) {
		this.signs = signs;
	}
	public Set getRows() {
		return rows;
	}
	public void setRows(Set rows) {
		this.rows = rows;
	}
	public Set getConditions() {
		return conditions;
	}
	public void setConditions(Set conditions) {
		this.conditions = conditions;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTableinstanceId() {
		return tableinstanceId;
	}
	public void setTableinstanceId(String tableinstanceId) {
		this.tableinstanceId = tableinstanceId;
	}
	public String getTaskPic() {
		return taskPic;
	}
	public void setTaskPic(String taskPic) {
		this.taskPic = taskPic;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPostid() {
		return postid;
	}
	public void setPostid(String postid) {
		this.postid = postid;
	}
	public String getPostname() {
		return postname;
	}
	public void setPostname(String postname) {
		this.postname = postname;
	}
	public String getPostinstanceid() {
		return postinstanceid;
	}
	public void setPostinstanceid(String postinstanceid) {
		this.postinstanceid = postinstanceid;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getIsOK() {
		return isOK;
	}
	public void setIsOK(String isOK) {
		this.isOK = isOK;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	
}
