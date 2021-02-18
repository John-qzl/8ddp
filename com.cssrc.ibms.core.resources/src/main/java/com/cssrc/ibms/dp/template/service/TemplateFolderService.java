package com.cssrc.ibms.dp.template.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.template.dao.TemplateFolderDao;

/**
 * @description 模板文件夹管理业务解析类
 * @author xie chen
 * @date 2019年12月3日 下午4:21:29
 * @version V1.0
 */
@Service
public class TemplateFolderService {
   
	@Resource
	TemplateFolderDao dao;
	
	/**
	 * @Desc 获取型号下的所有文件夹
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getTemplateFolderByModuleId(String moduleId) {
        return dao.getTemplateFolderByModuleId(moduleId);
    }
	
	/**
	 * @Desc 获取产品类别下所有文件夹
	 * @param productCategoryId
	 * @return
	 */
	public List<Map<String, Object>> getTemplateFolderByCategoryId(String productCategoryId) {
		return dao.getTemplateFolderByCategoryId(productCategoryId);
	}

}
