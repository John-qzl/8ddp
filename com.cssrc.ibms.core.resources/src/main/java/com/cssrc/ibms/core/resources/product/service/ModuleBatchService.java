package com.cssrc.ibms.core.resources.product.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.product.dao.ModuleBatchDao;

/**
 * @description 型号批次业务解析类
 * @author xie chen
 * @date 2019年11月29日 下午8:02:18
 * @version V1.0
 */
@Service
public class ModuleBatchService {
	
	@Resource
	private ModuleBatchDao dao;
	
	/**
	 * @Desc 根据型号阶段id获取型号批次
	 * @param modulePeriodId
	 * @return
	 */
	public List<Map<String, Object>> getBatchesByModulePeriodId(String modulePeriodId){
		return dao.getBatchesByModulePeriodId(modulePeriodId);
	}
	
	/**
	 * @Desc 根据型号批次id获取型号批次、阶段、型号信息
	 * @param moduleBatchId
	 * @return
	 */
	public Map<String, Object> getBatchPeriodModule(String moduleBatchId) {
		return dao.getBatchPeriodModule(moduleBatchId);
	}
	
}
