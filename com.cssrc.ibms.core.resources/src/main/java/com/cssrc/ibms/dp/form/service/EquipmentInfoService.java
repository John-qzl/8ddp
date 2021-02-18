package com.cssrc.ibms.dp.form.service;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.form.dao.EquipmentInfoDao;
import com.cssrc.ibms.dp.form.model.Equipment;
@Service
public class EquipmentInfoService {
	@Resource
	EquipmentInfoDao equipmentInfoDao;
	
	public Equipment findEquipmentByNum(String equipmentNum) {
		return equipmentInfoDao.findEquipmentByNum(equipmentNum);
	}
	
	public int updataStatus(String equipmentNum,String status) {
		return equipmentInfoDao.updataStatus(equipmentNum, status);
	}
	
	public void insertEquipment(Equipment equipment) {
		equipmentInfoDao.insertEquipment(equipment);
	}
}
