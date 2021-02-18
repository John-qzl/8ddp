package com.cssrc.ibms.system.controller;

import com.cssrc.ibms.api.core.intf.IBaseService;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.service.RoleResourcesService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 角色资源分配
 * <p>Title:RoleResourcesController</p>
 * @author Yangbo 
 * @date 2016-8-23下午09:28:01
 */
@Controller
@RequestMapping( { "/oa/system/roleResources/" })
@Action(ownermodel = SysAuditModelType.ROLE_MANAGEMENT)
public class RoleResourcesController extends BaseController {

	@Resource
	private RoleResourcesService roleResourcesService;
	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	private IBaseService<IResources> resourcesService;

	@RequestMapping( { "edit" })
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		long roleId = RequestUtil.getLong(request, "roleId");
		ISysRole role = (ISysRole) this.sysRoleService.getById(Long
				.valueOf(roleId));
		String returnUrl = RequestUtil.getString(request, "returnUrl",
				RequestUtil.getPrePage(request));
		mv.addObject("roleId", Long.valueOf(roleId));
		mv.addObject("role", role);
		mv.addObject("returnUrl", returnUrl);
		return mv;
	}

	@RequestMapping( { "upd" })
	@Action(description = "分配角色资源",exectype=SysAuditExecType.UPDATE_TYPE,
	detail="给角色  ${SysAuditLinkService.getSysRoleLink(sysRoleService.getById(Long.valueOf(roleId)))}分配了如下资源：【${resources}】")
	public void upd(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId", 0L));

		Long[] resIds = (Long[]) null;
		if ((request.getParameter("resIds") != null)
				&& (!request.getParameter("resIds").equals(""))) {
			resIds = RequestUtil.getLongAryByStr(request, "resIds");
		}
		ResultMessage resultMessage = new ResultMessage(1, "角色资源分配成功");
		writeResultMessage(response.getWriter(), resultMessage);
		try {
			this.roleResourcesService.update(roleId, resIds);
			
			try {
				StringBuffer sb=new StringBuffer();
				for(long id:resIds){
					IResources res=resourcesService.getById(id);
					sb.append(res.getResName()+" ");
				}
				LogThreadLocalHolder.putParamerter("resources", sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "角色资源分配失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	@RequestMapping( { "batchGrant" })
	public ModelAndView batchGrant(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		String returnUrl = RequestUtil.getString(request, "returnUrl",
				RequestUtil.getPrePage(request));

		mv.addObject("returnUrl",returnUrl);
		return mv;
	}

	@RequestMapping( { "saveBatch" })
	public void saveBatch(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		Long[] resIds = (Long[]) null;
		Long[] roleIds = (Long[]) null;
		if ((request.getParameter("resIds") != null)
				&& (!request.getParameter("resIds").equals(""))) {
			resIds = RequestUtil.getLongAryByStr(request, "resIds");
		}

		if ((request.getParameter("roleIds") != null)
				&& (!request.getParameter("roleIds").equals(""))) {
			roleIds = RequestUtil.getLongAryByStr(request, "roleIds");
		}
		ResultMessage resultMessage = new ResultMessage(1, "角色资源分配成功");
		try {
			this.roleResourcesService.saveBatch(roleIds, resIds);
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "角色资源分配失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
}
