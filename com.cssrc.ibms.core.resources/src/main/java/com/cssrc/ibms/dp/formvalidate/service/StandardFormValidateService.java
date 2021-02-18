package com.cssrc.ibms.dp.formvalidate.service;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.formvalidate.inf.IFormValidate;

@Service
public class StandardFormValidateService extends IFormValidate{
	/**
	 * 需要标记表头和页脚
	 * 指定行a：为表头
	 * 指定行b：为页脚
	 * ab之间的m行：表单主体
	 * a以上为页眉
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> specialValidate(Map<String, Object> msg) {

		List<Element> tables=(List<Element>) msg.get("tables");
		//获取所有的行
		List<Element> trs = tables.get(0).selectNodes(".//tr");
		//判断是否存在某行为表头
		int titleTr=getSignIndex(trs,TableReduction.title);
		if(titleTr==-1){
			String info="标准模板，需要标记表头，请使用上部的表头标记按钮进行标记或选择其他模板。";
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}
		//判断是否存在某行为页脚
		int tailTr=getSignIndex(trs,TableReduction.tail);
		if(tailTr==-1){
			String info="标准模板，需要标记页脚，请使用上部的页脚标记按钮进行标记或选择其他模板。";
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
