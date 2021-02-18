package com.cssrc.ibms.icons.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.icons.service.IconsService;

@Controller
@RequestMapping("/oa/webPortal/icons/")
public class IconsController extends BaseController {
	
	@Resource 
	private IconsService iconsService;
	
	@RequestMapping("iconfont")
	public ModelAndView iconfont(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconfont.css");
		List<Map<String, Object>> iconList = iconsService.getIcons(iconPath, "iconfont");
		return getAutoView().addObject("iconList", iconList);
	}
	
	@RequestMapping("icomoon")
	public ModelAndView icomoon(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconfont.css");
		List<Map<String, Object>> iconList = iconsService.getIcons(iconPath, "icomoon");
		return getAutoView().addObject("iconList", iconList);
	}
	
	@RequestMapping("imgIcons")
	public ModelAndView imgIcons(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconImg.css");
		List<Map<String, Object>> iconImgList = iconsService.getIconImgClassNameAndContent(iconPath);
		return getAutoView().addObject("iconImgList",iconImgList);
	}
}
