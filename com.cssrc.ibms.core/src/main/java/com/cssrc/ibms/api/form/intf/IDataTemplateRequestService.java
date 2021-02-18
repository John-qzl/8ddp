package com.cssrc.ibms.api.form.intf;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.util.appconf.AppUtil;

public abstract class IDataTemplateRequestService {

	/**
	 * 自定义参数接口方法
	 * @param request
	 * @param mv
	 */
	public abstract ModelAndView putEditData(HttpServletRequest request, ModelAndView mv);
	
	
	
	
	
	
	
	
	
	/**
	 * 调用方法
	 * @param request
	 * @param mv
	 * @return
	 */
	public static ModelAndView getCustomEditData(HttpServletRequest request, ModelAndView mv){
		IDataTemplateRequestService service=null;
		try{
			service=(IDataTemplateRequestService)AppUtil.getBean(IDataTemplateRequestService.class);
		}catch(Throwable e){
		}
		if(service!=null){
			return service.putEditData(request, mv);
		}else{
			return mv;
		}
		
	}
	
}
