package com.cssrc.ibms.statistics.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.statistics.model.Address;
import com.cssrc.ibms.statistics.model.Tool;
import com.cssrc.ibms.statistics.service.AddressService;
import com.cssrc.ibms.statistics.service.RedirectService;
import com.cssrc.ibms.statistics.service.ToolService;

@Controller
@RequestMapping("/oa/redirect/")
public class RedirectController  extends BaseController{
	@Resource
	private ToolService toolService;
	@Resource
	private AddressService addressService;
	@Resource
	private RedirectService redirectService;

	@RequestMapping("trans")
    public void trans(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Map<String, Object> map = RequestUtil.getQueryMap(request);
		String toolAlias = (String)map.get(Tool.NAME);
		String addressAlias = (String)map.get(Address.NAME);
		Tool tool = toolService.getByAlias(toolAlias);
		Address addres = addressService.getByAliasToolId(addressAlias, tool.getToolId());
		response.sendRedirect(addres.getUrl());
	}
	/**
	 * 出错则转发到错误界面
	 */
	@RequestMapping("view")
	public ModelAndView view(HttpServletRequest request, HttpServletResponse response)throws Exception{
		Map<String, Object> map = RequestUtil.getQueryMap(request);
		String msg = redirectService.urlCheck(map);
		ModelAndView defaultMv = new ModelAndView("/oa/statistics/redirectView.jsp");
		if(msg.equals("")){
			String toolAlias = (String)map.get(Tool.NAME);
			String addressAlias = (String)map.get(Address.NAME);
			Tool tool = toolService.getByAlias(toolAlias);
			Address addres = addressService.getByAliasToolId(addressAlias, tool.getToolId());
			if(BeanUtils.isEmpty(addres.getViewDef())){
				return defaultMv.addAllObjects(map);
			}else{
				return new ModelAndView(addres.getViewDef())
						.addAllObjects(map);
			}
		}else{
			response.getWriter().write(msg);
			return new ModelAndView("/oa/statistics/error.jsp")
				.addObject("errorInfo",msg);
		}		
	}
}
