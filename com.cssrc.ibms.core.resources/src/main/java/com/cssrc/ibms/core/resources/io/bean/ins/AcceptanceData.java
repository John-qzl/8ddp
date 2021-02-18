package com.cssrc.ibms.core.resources.io.bean.ins;

public class AcceptanceData {
	String AcceptanceProject;
	String productId;
	String unit;
	String requiredValue;
	String realValue;
	String productName;
	String ins_Id;
	int row;
	String requirements;
	String isCheck;

	

	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getRequirements() {
		return requirements;
	}
	public void setRequirements(String requirements) {
		this.requirements = requirements;
	}
	public String getIns_Id() {
		return ins_Id;
	}
	public void setIns_Id(String ins_Id) {
		this.ins_Id = ins_Id;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getAcceptanceProject() {
		return AcceptanceProject;
	}
	public void setAcceptanceProject(String acceptanceProject) {
		AcceptanceProject = acceptanceProject;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRequiredValue() {
		return requiredValue;
	}
	public void setRequiredValue(String requiredValue) {
		this.requiredValue = requiredValue;
	}
	public String getRealValue() {
		return realValue;
	}
	public void setRealValue(String realValue) {
		this.realValue = realValue;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
