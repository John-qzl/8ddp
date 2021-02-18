package com.cssrc.ibms.core.resources.datapackage.model;

import java.util.Map;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * 工作规划
 * @author vector
 *
 */
public class WorkPlan {
	private String id;//主键
	private String name;//名称
	private String type;//类型
	private String status;//执行状态
	private String planStartTime;//计划开始时间
	private String planEndTime;//计划结束时间
	private String realStartTime;//实际开始时间
	private String realEndTime;//实际结束时间
	private String desp;//描述
	private String workStart;//任务分发开关
	private String workEndStatus;//任务结束审批状态
	private String index;//序号
	private String allPlanStatus;//总计划状态
	private String taskUID;//任务UID
	private String parentPlanId;//父试验任务
	private String dataPackageId;//所属数据包
	private String fcId;//所属发次
	private String gw;//所属岗位
	public WorkPlan(Map<String, Object> res) {
		this.id=CommonTools.Obj2String(res.get("ID"));
		this.name=CommonTools.Obj2String(res.get("F_MC"));
		this.type=CommonTools.Obj2String(res.get("F_LX"));
		this.status=CommonTools.Obj2String(res.get("F_ZXZT"));
		this.planStartTime=CommonTools.Obj2String(res.get("F_JHKSSJ"));
		this.planEndTime=CommonTools.Obj2String(res.get("F_JHJSSJ"));
		this.realStartTime=CommonTools.Obj2String(res.get("F_SJKSSJ"));
		this.realEndTime=CommonTools.Obj2String(res.get("F_SJJSSJ"));
		this.desp=CommonTools.Obj2String(res.get("F_MS"));
		this.workStart=CommonTools.Obj2String(res.get("F_RWFFKG"));
		this.workEndStatus=CommonTools.Obj2String(res.get("F_RWJSSPZT"));
		this.index=CommonTools.Obj2String(res.get("F_XH"));
		this.allPlanStatus=CommonTools.Obj2String(res.get("F_ZJHZT"));
		this.taskUID=CommonTools.Obj2String(res.get("F_RWUID"));
		this.parentPlanId=CommonTools.Obj2String(res.get("F_SSFSYRW"));
		this.dataPackageId=CommonTools.Obj2String(res.get("F_SSSJBJD"));
		this.fcId=CommonTools.Obj2String(res.get("F_SSFC"));
		this.gw=CommonTools.Obj2String(res.get("F_SSGW"));
	}
	public WorkPlan() {
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}
	public String getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}
	public String getRealStartTime() {
		return realStartTime;
	}
	public void setRealStartTime(String realStartTime) {
		this.realStartTime = realStartTime;
	}
	public String getRealEndTime() {
		return realEndTime;
	}
	public void setRealEndTime(String realEndTime) {
		this.realEndTime = realEndTime;
	}
	public String getDesp() {
		return desp;
	}
	public void setDesp(String desp) {
		this.desp = desp;
	}
	public String getWorkStart() {
		return workStart;
	}
	public void setWorkStart(String workStart) {
		this.workStart = workStart;
	}
	public String getWorkEndStatus() {
		return workEndStatus;
	}
	public void setWorkEndStatus(String workEndStatus) {
		this.workEndStatus = workEndStatus;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getAllPlanStatus() {
		return allPlanStatus;
	}
	public void setAllPlanStatus(String allPlanStatus) {
		this.allPlanStatus = allPlanStatus;
	}
	
	public String getTaskUID() {
		return taskUID;
	}
	public void setTaskUID(String taskUID) {
		this.taskUID = taskUID;
	}
	public String getParentPlanId() {
		return parentPlanId;
	}
	public void setParentPlanId(String parentPlanId) {
		this.parentPlanId = parentPlanId;
	}
	public String getDataPackageId() {
		return dataPackageId;
	}
	public void setDataPackageId(String dataPackageId) {
		this.dataPackageId = dataPackageId;
	}
	public String getFcId() {
		return fcId;
	}
	public void setFcId(String fcId) {
		this.fcId = fcId;
	}
	public String getGw() {
		return gw;
	}
	public void setGw(String gw) {
		this.gw = gw;
	}
	
}
