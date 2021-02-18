package com.cssrc.ibms.core.resources.product.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.product.dao.ModulePeriodDao;

/**
 * @description 型号阶段业务解析类
 * @author xie chen
 * @date 2019年11月29日 下午7:19:28
 * @version V1.0
 */
@Service
public class ModulePeriodService {
	
	@Resource
	private ModulePeriodDao dao;
	
	/**
	 * @Desc 根据型号id获取型号阶段
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getPeriodsByModuleId(String moduleId){
		return dao.getPeriodsByModuleId(moduleId);
	}
	
	/**
	 * @Desc 根据型号阶段id获取型号阶段及阶段所属型号信息
	 * @param modulePeriodId
	 * @return
	 */
	public Map<String, Object> getPeriodAndModule(String modulePeriodId) {
		return dao.getPeriodAndModule(modulePeriodId);
	}
	
}
