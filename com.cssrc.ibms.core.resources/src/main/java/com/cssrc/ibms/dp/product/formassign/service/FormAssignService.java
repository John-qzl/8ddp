package com.cssrc.ibms.dp.product.formassign.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.product.formassign.dao.FormAssignDao;

/**
 * @description 表单下发业务解析类
 * @author xie chen
 * @date 2019年12月17日 下午3:21:55
 * @version V1.0
 */
@Service
public class FormAssignService {
	
	@Resource
	private FormAssignDao dao;
	
	/**
	 * @Desc 获取批次下的所有表单下发
	 * @param productBatchId
	 * @return
	 */
	public List<Map<String, Object>> getFormAssignByProductBatchId(String productBatchId){
		return dao.getFormAssignByProductBatchId(productBatchId);
	}
	
}
