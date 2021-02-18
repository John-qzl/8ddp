package com.cssrc.ibms.core.resources.mission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.mission.dao.RangeMissionDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class RangeMissionService {

	@Resource
	private RangeMissionDao dao;
	/**
	 * 根据型号id获取产品类别
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getRangeMissionByModuleId(String moduleId) {

		//获取任务节点
		return dao.getRangeMissionByModuleId(moduleId);


	}

}
