package com.cssrc.ibms.core.resources.product.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.product.dao.ModuleDispatchDao;

/**
 * @description 型号发次业务解析类
 * @author xie chen
 * @date 2019年11月29日 下午8:21:43
 * @version V1.0
 */
@Service
public class ModuleDispatchService {
	
	@Resource
	private ModuleDispatchDao dao;
	
	/**
	 * @Desc 根据型号批次id获取型号发次
	 * @param moduleBatchId
	 * @return
	 */
	public List<Map<String, Object>> getDispatchesByModuleBatchId(String moduleBatchId){
		return dao.getDispatchesByModuleBatchId(moduleBatchId);
	}
	
}
