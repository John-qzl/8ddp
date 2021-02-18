package com.cssrc.ibms.layout.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.layout.model.SysLayout;
import com.cssrc.ibms.layout.service.SysLayoutService;

@Controller
@RequestMapping("/oa/webPortal/sysLayout/")
public class SysLayoutController extends BaseController {

	@Resource
	private SysLayoutService sysLayoutService;
	
	@RequestMapping("list")
	@Action(description="查看布局列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();
		ISysOrg curSysOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
		
		//当前布局
		String layout = "main";
		String appType = "";
		String appId = "";
		SysLayout sysLayout = sysLayoutService.getSysLayout(userId, curSysOrg);
		if (sysLayout!=null) {
			if(StringUtil.isNotEmpty(sysLayout.getLayoutName())) {
				layout = sysLayout.getLayoutName();
			}
			if(sysLayout.getAppType()!=null) {
				appType = sysLayout.getAppType().toString();
			}
			if(sysLayout.getAppId()!=null) {
				appId = sysLayout.getAppId().toString();
			}
		} 
		
		return getAutoView().addObject("userId", userId)
				.addObject("layout", layout)
				.addObject("appType", appType)
				.addObject("appId", appId);
	}
	
	@RequestMapping("save")
	public void save(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		String layoutName = RequestUtil.getString(request, "selectedModel");
		String appType = RequestUtil.getString(request, "appType");
		String appId = RequestUtil.getString(request, "appId");
		
		try {
			sysLayoutService.save(layoutName, appType, appId);
			writeResultMessage(response.getWriter(), "保存成功!", ResultMessage.Success);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), e.getMessage(), ResultMessage.Fail);
			e.printStackTrace();
		}
	}
}
