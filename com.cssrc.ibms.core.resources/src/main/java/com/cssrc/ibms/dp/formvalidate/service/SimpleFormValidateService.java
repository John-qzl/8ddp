package com.cssrc.ibms.dp.formvalidate.service;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.formvalidate.inf.IFormValidate;
/**
 * 极简版表单模板校验
 * @author scc
 * 
 */
@Service
public class SimpleFormValidateService extends IFormValidate{
	/**
	 * 判断表头是否符合唯一性（单个td 行、列均不能合并）
	 * 第一行：为表头 
	 * 剩下的n行：表单模板主体
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> specialValidate(Map<String, Object> msg) {
		List<Element> tables=(List<Element>) msg.get("tables");
		//获取所有的行
		List<Element> trs = tables.get(0).selectNodes(".//tr");
		//首行为表头
		Element tr = trs.get(0);
		//校验表头
		validateTitleTr(msg,tr);
		return msg;
	}

	
	


}
