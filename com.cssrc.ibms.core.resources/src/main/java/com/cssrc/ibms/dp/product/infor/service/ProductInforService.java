package com.cssrc.ibms.dp.product.infor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.dp.product.infor.dao.ProductInforDao;

/**
 * @description 产品信息业务解析类
 * @author xie chen
 * @date 2019年11月21日 下午7:57:31
 * @version V1.0
 */
@Service
public class ProductInforService {
	
	@Resource
	private ProductInforDao dao;
	@Resource
	private ProductCategoryBatchDao productCategoryBatchDao;
	
	/**
	 * @Desc 根据批次id获取产品
	 * @param batchId
	 * @return
	 */
	public List<Map<String, Object>> getByProductBatchId(String batchId){
		return dao.getByProductBatchId(batchId);
	}
	
	/**
	 * @Desc 批次策划下批量添加产品
	 * @param acceptancePlanId
	 * @param productBatchId
	 * @param productKeys
	 */
	public void autoBatchAddProduct(String acceptancePlanId, String productBatchId, String[] productKeys) {
		Map<String, Object> productBatch = productCategoryBatchDao.getById(productBatchId);
		Map<String, Object> productMap = new HashMap<>();
		productMap.put("acceptancePlanId", acceptancePlanId);
		productMap.put("productBatchId", productBatchId);
		productMap.put("productCategoryId", productBatch.get("F_SSCPLB"));
		productMap.put("productBatchKey", productBatch.get("F_PCH"));
		productMap.put("modelId", productBatch.get("F_SSXH"));
		for (String productKey : productKeys) {
			productMap.put("productKey", productKey);
			dao.addProduct(productMap);
		}
	}
	
	/**
	 * @Desc 获取产品批次策划下所有产品数据
	 * @param acceptancePlanId
	 * @return
	 */
	public List<Map<String, Object>> getByAcceptancePlanId(String acceptancePlanId){
		return dao.getByAcceptancePlanId(acceptancePlanId);
	}

}
