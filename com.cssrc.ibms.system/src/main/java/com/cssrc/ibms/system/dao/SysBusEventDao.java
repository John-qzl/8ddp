package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysBusEvent;

import org.springframework.stereotype.Repository;

@Repository
public class SysBusEventDao extends BaseDao<SysBusEvent> {
	
	public Class<?> getEntityClass() {
		return SysBusEvent.class;
	}

	public SysBusEvent getByFormKey(String formKey) {
		return (SysBusEvent) getUnique("getByFormKey", formKey);
	}
}