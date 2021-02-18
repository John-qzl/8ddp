package com.cssrc.ibms.dp.sync.bean;

public class Mmc {

	private Long id;
	private String mmcId;
	private String mmcName;
	private String gwId;					   		//岗位ID，也就是试验队ID
	private String displaypathName;			//展示路径
	private String rwId;								//任务ID
	private String type;								//文件类型
	private String mmcobjId;						//mmc的dataobjectId
	private String packageId;					//packageid
	private String taskId;
	private String acceptancePlanId;     //策划Id
	private String batchId;              //批次id
	private String categoryId;           //类别ID

	private String modelId;
	private String taskName;

	private String modelName;


	private String acceptancePlanName;

	private String batchName;

	private String categoryName;
	private String fieldType;


	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getAcceptancePlanName() {
		return acceptancePlanName;
	}

	public void setAcceptancePlanName(String acceptancePlanName) {
		this.acceptancePlanName = acceptancePlanName;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getAcceptancePlanId() {
		return acceptancePlanId;
	}

	public void setAcceptancePlanId(String acceptancePlanId) {
		this.acceptancePlanId = acceptancePlanId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMmcId() {
		return mmcId;
	}
	public void setMmcId(String mmcId) {
		this.mmcId = mmcId;
	}
	public String getMmcName() {
		return mmcName;
	}
	public void setMmcName(String mmcName) {
		this.mmcName = mmcName;
	}
	public String getGwId() {
		return gwId;
	}
	public void setGwId(String gwId) {
		this.gwId = gwId;
	}
	public String getDisplaypathName() {
		return displaypathName;
	}
	public void setDisplaypathName(String displaypathName) {
		this.displaypathName = displaypathName;
	}
	public String getRwId() {
		return rwId;
	}
	public void setRwId(String rwId) {
		this.rwId = rwId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMmcobjId() {
		return mmcobjId;
	}
	public void setMmcobjId(String mmcobjId) {
		this.mmcobjId = mmcobjId;
	}


}