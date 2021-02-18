package com.cssrc.ibms.index.model;

import java.io.Serializable;
import java.util.Date;

import com.cssrc.ibms.api.system.model.ITaskListTab;

/**
 * 转存首页任务列表信息
 * @author YangBo
 *
 */
public class TaskListTab implements ITaskListTab,Serializable{
	//任务类型：待办、已办、办结等
	protected String tabName;
	//数据的提醒数量
	protected String dataNum;
	
	//对应时间
	protected Date time;
	
	//状态：未完成和完成
	protected String status;
	
	//创建人
	protected String creator;
	
	//跳转路径
	protected String url;
	

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getDataNum() {
		return dataNum;
	}

	public void setDataNum(String dataNum) {
		this.dataNum = dataNum;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
