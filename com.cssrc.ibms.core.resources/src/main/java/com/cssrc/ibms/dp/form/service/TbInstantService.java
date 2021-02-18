package com.cssrc.ibms.dp.form.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.form.dao.TbInstantDao;
import com.cssrc.ibms.dp.form.model.TbInstant;

@Service
public class TbInstantService {
	@Resource
	TbInstantDao tbInstantDao;
	public TbInstant getById(Long key){
		return tbInstantDao.getById(key);
	}
}
