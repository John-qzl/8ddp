package com.cssrc.ibms.record.controller;

import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.record.model.RecRoleSon;
import com.cssrc.ibms.record.service.RecRoleSonFunService;
import com.cssrc.ibms.record.service.RecRoleSonService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * Description:
 * <p>RecRoleSonSonFunController.java</p>
 * @author dengwenjie 
 * @date 2017年4月7日
 */
@Controller
@RequestMapping( { "/oa/system/recRoleSonFun/" })
@Action(ownermodel=SysAuditModelType.ROLETYPE_PROJECT_MANAGEMENT)
public class RecRoleSonFunController extends BaseController {

	@Resource
	private RecRoleSonFunService recRoleSonFunService;
	@Resource
	private RecRoleSonService recRoleSonService;

	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/record/"))
			viewName = viewName.replace("/oa/system/", "/oa/system/record/role/son/");
		mv.setViewName(viewName);
		return mv;
	}
	@RequestMapping( { "edit" })
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		Long roleSonId = RequestUtil.getLong(request, "roleSonId");
		Long typeId = RequestUtil.getLong(request, "typeId");
		RecRoleSon roleSon = (RecRoleSon) this.recRoleSonService.getById(Long
				.valueOf(roleSonId));
		String returnUrl = RequestUtil.getString(request, "returnUrl",
				RequestUtil.getPrePage(request));
		mv.addObject("roleSonId", Long.valueOf(roleSonId));
		mv.addObject("typeId", Long.valueOf(typeId));
		mv.addObject("roleSon", roleSon);
		mv.addObject("returnUrl", returnUrl);
		return mv;
	}

	@RequestMapping( { "upd" })
	@Action(description = "分配角色功能点", detail = "分配角色功能点", exectype = SysAuditExecType.SELECT_TYPE)
	public void upd(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Long roleSonId = Long.valueOf(RequestUtil.getLong(request, "roleSonId", 0L));
		String dataJson = RequestUtil.getString(request, "dataJson");
	
		ResultMessage resultMessage = new ResultMessage(1, "角色功能点分配成功");
		writeResultMessage(response.getWriter(), resultMessage);
		try {
			this.recRoleSonFunService.update(roleSonId,dataJson);
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "角色功能点分配失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
}
