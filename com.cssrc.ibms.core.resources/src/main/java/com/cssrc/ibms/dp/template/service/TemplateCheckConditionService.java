package com.cssrc.ibms.dp.template.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.template.dao.TemplateCheckConditionDao;

/**
 * @description 模板检查条件业务解析类
 * @author xie chen
 * @date 2019年12月9日 下午4:15:23
 * @version V1.0
 */
@Service
public class TemplateCheckConditionService {
   
	@Resource
	TemplateCheckConditionDao dao;
	
	public List<Map<String, Object>> getCheckConditionListMapByTemplateId(String templateId) {
		return dao.getCheckConditionListMapByTemplateId(templateId);
	}

}
