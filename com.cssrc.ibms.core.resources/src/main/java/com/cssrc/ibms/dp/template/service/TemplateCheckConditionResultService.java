package com.cssrc.ibms.dp.template.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.template.dao.TemplateCheckConditionResultDao;

/**
 * @description 模板检查条件实例结果业务解析类
 * @author xie chen
 * @date 2019年12月16日 上午11:00:55
 * @version V1.0
 */
@Service
public class TemplateCheckConditionResultService {
   
	@Resource
	TemplateCheckConditionResultDao dao;
	
	/**
	 * @Desc 获取模板检查条件及空实例数据（预览）
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getConditionDefAndResultByTemplateId(String templateId) {
		return dao.getConditionDefAndResultByTemplateId(templateId);
	}
	
}
