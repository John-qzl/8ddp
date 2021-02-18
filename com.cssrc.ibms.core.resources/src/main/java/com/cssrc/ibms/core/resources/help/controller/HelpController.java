package com.cssrc.ibms.core.resources.help.controller;

import com.cssrc.ibms.core.resources.help.service.HelpService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/help/help")
public class HelpController extends BaseController {
	/**
	 * 系统帮助管理界面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/manage")
	@Action(description = "系统帮助")
	public ModelAndView manage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return 	getAutoView();
	}
    


}

