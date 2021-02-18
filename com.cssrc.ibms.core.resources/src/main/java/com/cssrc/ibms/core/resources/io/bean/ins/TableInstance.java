package com.cssrc.ibms.core.resources.io.bean.ins;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;

/**
 * @author wenjie
 * 检查表实例对象
 */
@XmlRootElement(name = "instance")
@XmlAccessorType(XmlAccessType.FIELD)
public class TableInstance {
	public TableInstance() {}
	public TableInstance(Map<String,Object> map) {
		if(map!=null) {
			this.id = CommonTools.Obj2String(map.get("ID"));
			this.name = CommonTools.Obj2String(map.get("F_NAME"));
			this.number = CommonTools.Obj2String(map.get("F_NUMBER"));
			this.status = CommonTools.Obj2String(map.get("F_STATUS"));
			this.uploadTime = CommonTools.Obj2String(map.get("F_UPLOAD_TIME"));
			this.uploadTime = DateUtil.getDateStr(uploadTime, "yyyy-MM-dd");	
			this.tableTempID = CommonTools.Obj2String(map.get("F_TABLE_TEMP_ID"));
			this.taskId = CommonTools.Obj2String(map.get("F_TASK_ID"));
			this.content = CommonTools.Obj2String(map.get("F_CONTENT"));
			this.version = CommonTools.Obj2String(map.get("F_VERSION"));
			this.startTime = CommonTools.Obj2String(map.get("F_STARTTIME"));
			this.startTime = DateUtil.getDateStr(startTime, "yyyy-MM-dd");	
			this.endTime = CommonTools.Obj2String(map.get("F_ENDTIME"));
			this.endTime = DateUtil.getDateStr(endTime, "yyyy-MM-dd");
			this.productId = CommonTools.Obj2String( map.get("F_PRODUCTID"));
			this.planId = CommonTools.Obj2String( map.get("F_PLANID"));
			this.type=CommonTools.Obj2String(map.get("F_BDZL"));
			this.userId=CommonTools.Obj2String(map.get("F_HCRID"));
			this.userName=CommonTools.Obj2String(map.get("F_HCR"));
		}
			
	}
	@XmlAttribute
	private String id;
	
	@XmlAttribute
	private String name;	//模板名称
	
	@XmlAttribute
	private String number;	//编号
	
	@XmlAttribute
	private String status;	//状态
	
	@XmlAttribute
	private String uploadTime;	//上传时间
	
	@XmlAttribute
	private String tableTempID;	//所属模板Id
	
	@XmlAttribute
	private String taskId;//版本
		
	@XmlAttribute
	private String content;	//检查表内容
	
	@XmlAttribute
	private String version;//所属模板Id
	
	@XmlAttribute
	private String startTime;//开始时间
	
	@XmlAttribute
	private String endTime;//结束时间

	@XmlAttribute
	private String productId;//所属产品
	@XmlAttribute
	private String planId;//所属策划
	@XmlAttribute
	private String type;//实例种类是否是pad回传
	@XmlElement(name = "hcr", required = false)  //回传人
	private String userName;
	@XmlElement(name = "hcrID", required = false)
	private String userId;
	/**
	 * 检查结果
	 */
	@XmlElement(name ="checkResult")
	private List<CheckResult> checkResultList;
	
	/**
	 * 检查条件结果
	 */
	@XmlElement(name ="conditionResult")
	private List<ConditionResult> conditionResultList;
	
	/**
	 * 签署结果
	 */
	@XmlElement(name ="signResult")
	private List<SignResult> signResultList;
	
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getTableTempID() {
		return tableTempID;
	}
	public void setTableTempID(String tableTempID) {
		this.tableTempID = tableTempID;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getUpdateSql() {
		StringBuffer sql = new StringBuffer();
		sql.append(" update W_TB_INSTANT set ");
		sql.append(" F_NAME").append("='").append(name).append("',");
		sql.append(" F_NUMBER").append("='").append(number).append("',");
		sql.append(" F_STATUS").append("='").append(status).append("',");
		//sql.append(" F_UPLOAD_TIME").append("=").append(uploadTime).append("',");
		sql.append(" F_TABLE_TEMP_ID").append("='").append(tableTempID).append("',");
		sql.append(" F_TASK_ID").append("='").append(taskId).append("',");
		sql.append(" F_CONTENT").append("='").append(content).append("',");
		sql.append(" F_VERSION").append("='").append(version).append("'");
		//sql.append(" F_STARTTIME").append("='").append(startTime).append("',");
		//sql.append(" F_ENDTIME").append("='").append(endTime).append("' ");
		sql.append(" where id='").append(id).append("'");
		return  sql.toString();
	}
	public List<CheckResult> getCheckResultList() {
		return checkResultList;
	}
	public void setCheckResultList(List<CheckResult> checkResultList) {
		this.checkResultList = checkResultList;
	}
	public List<ConditionResult> getConditionResultList() {
		return conditionResultList;
	}
	public void setConditionResultList(List<ConditionResult> conditionResultList) {
		this.conditionResultList = conditionResultList;
	}
	public List<SignResult> getSignResultList() {
		return signResultList;
	}
	public void setSignResultList(List<SignResult> signResultList) {
		this.signResultList = signResultList;
	}
}
