package com.cssrc.ibms.dp.formvalidate.service;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.formvalidate.inf.IFormValidate;
/**
 * 页眉版模板
 * 即：需要标记表头 
 * @author scc
 *
 */
@Service
public class FrontFormValidateService extends IFormValidate{
	/**
	 * 需要标记表头
	 * 判断表头是否符合唯一性（单个td 行、列均不能合并）
	 * 指定行：为表头  
	 * 以下的n行：表单模板主体
	 * 以上的m行：表单的页眉
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> specialValidate(Map<String, Object> msg) {
		List<Element> tables=(List<Element>) msg.get("tables");
		//获取所有的行
		List<Element> trs = tables.get(0).selectNodes(".//tr");
		//判断是否存在某行为标题
		//获取标题标记的行号
		int titleTr=getSignIndex(trs,TableReduction.title);
		if(titleTr==-1){
			String info="极简+页眉类的模板，需要标记表头，请使用上部的表头标记按钮进行标记或选择其他模板。";
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}
		//获取表头行
		Element tr = trs.get(titleTr);
		//校验表头
		validateTitleTr(msg,tr);
		return msg;
	}

}
