package com.cssrc.ibms.dp.template.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.template.dao.TemplateSignResultDao;

/**
 * @description 模板签署实例结果业务解析类
 * @author xie chen
 * @date 2019年12月16日 上午10:32:55
 * @version V1.0
 */
@Service
public class TemplateSignResultService {
   
	@Resource
	TemplateSignResultDao dao;
	
	/**
	 * @Desc 获取模板签署项及空实例数据（预览）
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getSignDefAndResultByTemplateId(String templateId) {
		return dao.getSignDefAndResultByTemplateId(templateId);
	}
	
}
