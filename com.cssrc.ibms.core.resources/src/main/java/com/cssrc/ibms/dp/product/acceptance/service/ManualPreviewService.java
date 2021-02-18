package com.cssrc.ibms.dp.product.acceptance.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.product.acceptance.dao.ManualPreviewDao;

@Service
public class ManualPreviewService {
	@Resource 
	ManualPreviewDao manualPreviewDao;
	
	public String getFileId(String id) {
		return manualPreviewDao.getFileId(id);
	}
}
