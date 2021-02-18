package com.cssrc.ibms.record.controller;

import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.record.model.RecRole;
import com.cssrc.ibms.record.service.RecRoleFunService;
import com.cssrc.ibms.record.service.RecRoleService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description:
 * <p>RecRoleFunController.java</p>
 * @author dengwenjie 
 * @date 2017年3月20日
 */
@Controller
@RequestMapping( { "/oa/system/recRoleFun/" })
@Action(ownermodel=SysAuditModelType.ROLETYPE_PROJECT_MANAGEMENT)
public class RecRoleFunController extends BaseController {

	@Resource
	private RecRoleFunService recRoleFunService;
	@Resource
	private RecRoleService recRoleService;

	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/record/"))
			viewName = viewName.replace("/oa/system/", "/oa/system/record/role/");
		mv.setViewName(viewName);
		return mv;
	}
	@RequestMapping( { "edit" })
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		Long roleId = RequestUtil.getLong(request, "roleId");
		Long typeId = RequestUtil.getLong(request, "typeId");
		RecRole role = (RecRole) this.recRoleService.getById(Long
				.valueOf(roleId));
		String returnUrl = RequestUtil.getString(request, "returnUrl",
				RequestUtil.getPrePage(request));
		mv.addObject("roleId", Long.valueOf(roleId));
		mv.addObject("typeId", Long.valueOf(typeId));
		mv.addObject("role", role);
		mv.addObject("returnUrl", returnUrl);
		return mv;
	}

	@RequestMapping( { "upd" })
	@Action(description = "分配角色功能点")
	public void upd(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId", 0L));
		String dataJson = RequestUtil.getString(request, "dataJson");
	
		ResultMessage resultMessage = new ResultMessage(1, "角色功能点分配成功");
		writeResultMessage(response.getWriter(), resultMessage);
		try {
			this.recRoleFunService.update(roleId,dataJson);
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

	/**
	 * 角色批量授权功能点
	 * @return
	 */
	@RequestMapping( { "batchGrant" })
	public ModelAndView batchGrant(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		Long typeId = RequestUtil.getLong(request, "typeId");
		String returnUrl = RequestUtil.getString(request, "returnUrl",
				RequestUtil.getPrePage(request));

		mv.addObject("returnUrl",returnUrl).addObject("typeId",typeId);
		return mv;
	}

	@RequestMapping( { "saveBatch" })
	public void saveBatch(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Long[] funIds = (Long[]) null;
		Long[] roleIds = (Long[]) null;
		if ((request.getParameter("funIds") != null)
				&& (!request.getParameter("funIds").equals(""))) {
			funIds = RequestUtil.getLongAryByStr(request, "funIds");
		}

		if ((request.getParameter("roleIds") != null)
				&& (!request.getParameter("roleIds").equals(""))) {
			roleIds = RequestUtil.getLongAryByStr(request, "roleIds");
		}
		ResultMessage resultMessage = new ResultMessage(1, "角色功能点分配成功");
		try {
			this.recRoleFunService.saveBatch(roleIds, funIds);
			writeResultMessage(response.getWriter(), resultMessage);
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
