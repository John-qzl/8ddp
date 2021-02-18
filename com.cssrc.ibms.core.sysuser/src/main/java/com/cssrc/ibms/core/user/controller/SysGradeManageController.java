package com.cssrc.ibms.core.user.controller;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.service.OrgAuthService;
import com.cssrc.ibms.core.user.service.SysOrgRoleManageService;
import com.cssrc.ibms.core.user.service.SysOrgRoleService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 分级组织管理Controller层
 * <p>Title:SysGradeManageController</p>
 * @author Yangbo 
 * @date 2016-8-18上午08:58:21
 */
@Controller
@RequestMapping( { "/oa/system/grade/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class SysGradeManageController extends BaseController {

	@Resource
	private SysOrgService sysOrgService;

	@Resource
	private SysOrgRoleService sysOrgRoleService;

	@Resource
	private SysOrgRoleManageService sysOrgRoleManageService;

	@Resource
	private OrgAuthService orgAuthService;

	@RequestMapping( { "manage" })
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		List orgAuthList = this.orgAuthService.getByUserId(userId);

		return getAutoView().addObject("orgAuthList", orgAuthList);
	}

	@RequestMapping( { "getOrgJsonByAuthOrgId" })
	public void getOrgJsonByUserId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		String str = this.sysOrgService.getOrgJsonByAuthOrgId(orgId);
		response.getWriter().print(str);
	}

	@RequestMapping( { "addOrgRole" })
	@Action(description = "新增组织可以授权的角色范围", execOrder = ActionExecOrder.AFTER ,detail="${sysAuditLinkService.addSysGradeOrgRoleChange(roleIds,orgId)}", exectype = SysAuditExecType.ADD_TYPE)
	public void addOrgRole(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		addOrgRole(request, response, 0);
	}

	@RequestMapping( { "addGradeOrgRole" })
	@Action(description = "新增组织可以授权的角色范围", execOrder = ActionExecOrder.AFTER ,detail="${sysAuditLinkService.addSysGradeOrgRoleChange(roleIds,orgId)}", exectype = SysAuditExecType.ADD_TYPE)
	public void addGradeOrgRole(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		addOrgRole(request, response, 1);
	}

	private void addOrgRole(HttpServletRequest request,
			HttpServletResponse response, int grade) throws IOException {
		String roleIds = RequestUtil.getString(request, "roleIds");
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));

		if (StringUtil.isEmpty(roleIds)) {
			writeResultMessage(response.getWriter(), "没有选择角色!", 0);
			return;
		}
		try {
			boolean rtn = this.sysOrgRoleManageService.addOrgRole(orgId,
					roleIds, Integer.valueOf(grade));
			if (rtn) {
				String msg = "添加角色成功!";
				writeResultMessage(response.getWriter(), msg, 1);
			} else {
				String msg = "选择的角色在系统中已存在!";
				writeResultMessage(response.getWriter(), msg, 0);
			}
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "添加角色失败:"
						+ str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	/**
	 * 总体的可分配角色
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "assignRoleList" })
	@Action(description = "取得总体的可分配角色列表",detail="取得总体的可分配角色列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView assignRoleList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		mv.addObject("scope", "global");
		mv = getAssignRoleListMv(request, mv);
		return mv;
	}
	
	@RequestMapping( { "assignRoleGradeList" })
	public ModelAndView assignRoleGradeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/oa/system/gradeAssignRoleList.jsp");
		mv.addObject("scope", "grade");
		mv = getAssignRoleListMv(request, mv);
		return mv;
	}

	private ModelAndView getAssignRoleListMv(HttpServletRequest request,
			ModelAndView mv) {
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		if (orgId.longValue() > 0L) {
			List list = this.sysOrgRoleService.getAssignRoleByOrgId(orgId);
			mv.addObject("orgRoles", list).addObject("orgId", orgId);
		}
		return mv;
	}

	@RequestMapping( { "roleSelector" })
	public ModelAndView roleSelector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));

		List list = this.sysOrgRoleService.getAssignRoleByOrgId(orgId);
		mv.addObject("orgRoles", list).addObject("orgId", orgId);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除组织可以授权的角色范围", execOrder = ActionExecOrder.BEFORE ,detail="${sysAuditLinkService.getSysGradeOrgRoleChange(id)}", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		delete(request, response);
	}

	@RequestMapping( { "delGrade" })
	@Action(description = "删除组织可以授权的角色范围", execOrder = ActionExecOrder.BEFORE ,detail="${sysAuditLinkService.getSysGradeOrgRoleChange(id)}", exectype = SysAuditExecType.DELETE_TYPE)
	public void delGrade(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		delete(request, response);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysOrgRoleManageService.delByIds(lAryId);
			message = new ResultMessage(1, "删除组织可以授权的角色范围(用于分级授权)成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
}
