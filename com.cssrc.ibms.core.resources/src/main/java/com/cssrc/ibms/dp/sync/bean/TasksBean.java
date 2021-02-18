package com.cssrc.ibms.dp.sync.bean;

import java.util.HashSet;
import java.util.Set;

public class TasksBean {
	private Set signs = new HashSet(0);
	private Set rows = new HashSet(0);
	private Set conditions = new HashSet(0);

	private String rwid;
	private String pclbid;				//批次类别ID
	private String rwname;
	private String postid;
	private String postinstanceid;		//岗位实例ID
	private String nodeLeaderId;         			//所属节点负责人id
	private String postname;				//岗位实例名称
	private String version;
	private String name;
	private String file;
	private String remark;
	private String tableinstanceId;
	private String path;				//所属数据包名称
	private String pathId;				//所属数据包ID，即所所属试验节点ID
	private String responsibility;
	private String fxtid;
	private String fxtname;
	private String isfinished;
	private String isOK;
	private String order;
	private String taskPic;
	private String cplb;		//产品类别
	private String chbh;		//策划编号
	private String chId;		//策划ID
	private String xhId;		//型号ID
	private String xhmc;		//型号名称
	private String tempId;		//模板ID
	private String tempType;		//模板类型

	public String getTempType() {
		return tempType;
	}

	public void setTempType(String tempType) {
		this.tempType = tempType;
	}

	//检查开始和完成时间
	private String starttime;
	private String endtime;

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getXhId() {
		return xhId;
	}

	public void setXhId(String xhId) {
		this.xhId = xhId;
	}

	public String getXhmc() {
		return xhmc;
	}

	public void setXhmc(String xhmc) {
		this.xhmc = xhmc;
	}

	public String getChbh() {
		return chbh;
	}

	public void setChbh(String chbh) {
		this.chbh = chbh;
	}

	public String getChId() {
		return chId;
	}

	public void setChId(String chId) {
		this.chId = chId;
	}

	public String getCplb() {
		return cplb;
	}

	public void setCplb(String cplb) {
		this.cplb = cplb;
	}

	public String getNodeLeaderId() {
		return nodeLeaderId;
	}

	public void setNodeLeaderId(String nodeLeaderId) {
		this.nodeLeaderId = nodeLeaderId;
	}

	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getRwname() {
		return rwname;
	}
	public void setRwname(String rwname) {
		this.rwname = rwname;
	}
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid) {
		this.rwid = rwid;
	}

	public String getPclbid() {
		return pclbid;
	}

	public void setPclbid(String pclbid) {
		this.pclbid = pclbid;
	}

	public String getFxtid() {
		return fxtid;
	}
	public void setFxtid(String fxtid) {
		this.fxtid = fxtid;
	}
	public String getFxtname() {
		return fxtname;
	}
	public void setFxtname(String fxtname) {
		this.fxtname = fxtname;
	}
	public String getIsfinished() {
		return isfinished;
	}
	public void setIsfinished(String isfinished) {
		this.isfinished = isfinished;
	}
	public String getTaskPic() {
		return taskPic;
	}
	public void setTaskPic(String taskPic) {
		this.taskPic = taskPic;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getIsOK() {
		return isOK;
	}
	public void setIsOK(String isOK) {
		this.isOK = isOK;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getPostid() {
		return postid;
	}
	public void setPostid(String postid) {
		this.postid = postid;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getPathId() {
		return pathId;
	}

	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

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

}
