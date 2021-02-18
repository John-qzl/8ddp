package com.cssrc.ibms.dp.form.model;

public class Equipment {
	/*主键*/
	protected Long ID;
	
	protected String EquipmentNum;
	
	protected String EquipmentName;
	
	protected String EquipmentStatus;
	
	protected String EquipmentID;
	


	public String getEquipmentID() {
		return EquipmentID;
	}

	public void setEquipmentID(String equipmentID) {
		EquipmentID = equipmentID;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getEquipmentNum() {
		return EquipmentNum;
	}

	public void setEquipmentNum(String equipmentNum) {
		EquipmentNum = equipmentNum;
	}

	public String getEquipmentName() {
		return EquipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		EquipmentName = equipmentName;
	}

	public String getEquipmentStatus() {
		return EquipmentStatus;
	}

	public void setEquipmentStatus(String equipmentStatus) {
		EquipmentStatus = equipmentStatus;
	}
	
}
