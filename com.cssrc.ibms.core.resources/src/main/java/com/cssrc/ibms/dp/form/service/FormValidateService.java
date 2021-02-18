package com.cssrc.ibms.dp.form.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.dp.formvalidate.service.ComplexFormValidateService;
import com.cssrc.ibms.dp.formvalidate.service.FrontFormValidateService;
import com.cssrc.ibms.dp.formvalidate.service.SimpleFormValidateService;
import com.cssrc.ibms.dp.formvalidate.service.StandardFormValidateService;
import com.cssrc.ibms.dp.formvalidate.service.TailFormValidateService;

@Service
public class FormValidateService {
	@Resource
	private SimpleFormValidateService simpleFormValidateService;
	@Resource
	private FrontFormValidateService frontFormValidateService;
	@Resource
	private TailFormValidateService tailFormValidateService;
	@Resource
	private StandardFormValidateService standardFormValidateService;
	@Resource
	private ComplexFormValidateService complexFormValidateService;
	public Map<String, Object> validate(String html,String type,String formtempId){
		Map<String, Object> msg=new HashMap<String,Object>();
		if(type.equals("1")){//极简版
			return simpleFormValidateService.validate(html, formtempId);
		}else if(type.equals("2")){//页眉版
			return frontFormValidateService.validate(html, formtempId);
		}else if(type.equals("3")){//页脚版
			return tailFormValidateService.validate(html, formtempId);
		}else if(type.equals("4")){//标准版
			return standardFormValidateService.validate(html, formtempId);
		}else if(type.equals("5")){//复杂版
			return complexFormValidateService .validate(html, formtempId);
		}else {
			msg.put("success", "false");
			msg.put("info", "模板类别错误：类别为"+type);
		}
		return msg;
	}
}
