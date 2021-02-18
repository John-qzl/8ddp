package com.cssrc.ibms.dbom.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 对象功能:DBom节点管理 控制器类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-14 上午08:05:04 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-14 上午08:05:04 <br/> 
 * @see
 */
@Controller
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class DBomNodeJsController extends BaseController{
	
	/**
	 * ext js 特殊controller
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/js/system/DBomView")
	public ModelAndView extJs(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv=new ModelAndView("oa/dbom/SeaJSShow_Panel.jsp");
		String requestURI=request.getRequestURI();
		requestURI=requestURI.replace(".do", "");
		//处理RequestURI
		String contextPath=request.getContextPath();
		requestURI=requestURI.replace(".do", "");
		int cxtIndex=requestURI.indexOf(contextPath);
		if(cxtIndex!=-1)
		{
			requestURI=requestURI.substring(cxtIndex+contextPath.length());
		}
		mv.addObject("requestURI", requestURI);
		return mv;
	}
	
}
