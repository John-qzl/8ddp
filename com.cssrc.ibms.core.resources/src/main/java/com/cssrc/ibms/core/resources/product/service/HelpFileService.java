package com.cssrc.ibms.core.resources.product.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.product.dao.HelpFileDao;

@Service
public class HelpFileService {
	@Resource
	HelpFileDao helpFileDao;
	
	
	public List<Map<String,Object>> getAllFile(){
		return helpFileDao.getAllHelpFile();
		
	}
}
