package com.cssrc.ibms.skin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.skin.util.Constant;
import com.cssrc.ibms.skin.util.LessUtil;

@Controller
@RequestMapping("/oa/webPortal/sysSkin/")
public class SysSkinController extends BaseController {
	
	@RequestMapping("list")
	@Action(description="皮肤管理")
	@ResponseBody
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//Long userId = UserContextUtil.getCurrentUserId();
		//ISysOrg curSysOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
		//当前布局
		//String layout = sysLayoutService.getLayoutType(userId,curSysOrg);
		JSONObject jsonObject = LessUtil.resolveLessToMap(Constant.SKIN_LESS_APP);
		return getAutoView().addObject("skinJson", jsonObject);
	}
}
