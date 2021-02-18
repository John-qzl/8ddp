package com.cssrc.ibms.dp.template.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.template.dao.TemplateSignDao;

/**
 * @description 模板签署业务解析类
 * @author xie chen
 * @date 2019年12月9日 下午4:06:34
 * @version V1.0
 */
@Service
public class TemplateSignService {
   
	@Resource
	TemplateSignDao dao;
	
	/**
	 * @Desc 根据模板id获取模板签署集合
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getSignListMapByTemplateId(String templateId) {
		return dao.getSignListMapByTemplateId(templateId);
	}

}
