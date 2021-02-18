package com.cssrc.ibms.core.resources.project.service;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.io.bean.Project;
import com.cssrc.ibms.core.resources.project.dao.ProjectDao;

@Service
public class ProjectService {
	@Resource
	private ProjectDao pDao;
	/** 根据型号id查询发次所有信息  */
	public List<Map<String, Object>> queryProjectNodeById(String id){
		return pDao.queryProjectNodeById(id);
	}
	public Project getById(Long id) {
		Map<String, Object> map = pDao.getById(id);
		if(map==null) {
			return null;
		}else {
			return new Project(map);
		}
	}
}
