package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dp.form.model.Equipment;
@Repository
public class EquipmentInfoDao  extends BaseDao<Equipment>{

	@Override
	public Class getEntityClass() {
		// TODO Auto-generated method stub
		return Equipment.class;
	}
	//查找设备编码对应的设备信息
	public Equipment findEquipmentByNum(String equipmentNum){
		Map<String,Object> param=new HashMap<>();
		param.put("equipmentNum", equipmentNum);
		return this.getUnique("findEquipmentByNum", equipmentNum);
	}
	
	public int updataStatus(String equipmentNum,String status) {
		Map<String,Object> param=new HashMap<>();
		param.put("equipmentNum", equipmentNum);
		param.put("status", status);
		return this.update("updataStatus", param);
	}
	
	public void insertEquipment(Equipment equipment) {
		this.insert("insertEquipment", equipment);
	}
}
