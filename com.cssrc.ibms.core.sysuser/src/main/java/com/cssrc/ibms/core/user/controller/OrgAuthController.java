package com.cssrc.ibms.core.user.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.OrgAuth;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.service.OrgAuthService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 * 组织授权(维度，用户，组织多个层级授权)
 * 
 * <p>Title:OrgAuthController</p>
 * @author YangBo 
 * @date 2016-7-30下午03:00:31
 */
@Controller
@RequestMapping({"/oa/system/orgAuth/"})
public class OrgAuthController extends BaseController
{

	@Resource
	private OrgAuthService orgAuthService;

	@Resource
	private SysOrgService sysOrgService;

	@Resource
	private SysRoleService sysRoleService;

	@RequestMapping({"save"})
	@Action(description="添加或更新组织管理员")
	public void save(HttpServletRequest request, HttpServletResponse response, OrgAuth orgAuth)
	throws Exception
	{
		Long[] roleIds = RequestUtil.getLongAryByStr(request, "roleIds");
		String resultMsg = null;
		try {
			if ((orgAuth.getId() == null) || (orgAuth.getId().longValue() == 0L)) {
				orgAuth.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.orgAuthService.add(orgAuth, roleIds);
				resultMsg = "添加组织管理员成功！";
			} else {
				this.orgAuthService.update(orgAuth, roleIds);
				resultMsg = "修改组织管理员成功！";
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
		}
	}

	@RequestMapping({"list"})
	@Action(description="查看组织管理员分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId", 0L));
		QueryFilter filter = new QueryFilter(request, "orgAuthItem");
		if (orgId.longValue() > 0L) filter.addFilterForIB("orgId", orgId);
		List<OrgAuth> list = this.orgAuthService.getAll(filter);
		ModelAndView mv = getAutoView().addObject("orgAuthList", list);
		mv.addObject("orgId", orgId);
		return mv;
	}
	@RequestMapping({"gradeList"})
	@Action(description="查看组织管理员分页列表")
	public ModelAndView gradeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId", 0L));
		Long topOrgId = Long.valueOf(RequestUtil.getLong(request, "orgSupId", 0L));
		QueryFilter filter = new QueryFilter(request, "orgAuthItem");
		filter.addFilterForIB("orgId", orgId);

		List<OrgAuth> list = this.orgAuthService.getAll(filter);

		long userId = UserContextUtil.getCurrentUserId().longValue();
		OrgAuth userAuth = null;
		if (topOrgId.longValue() != 0L) {
			userAuth = this.orgAuthService.getByUserIdAndOrgId(userId, topOrgId.longValue());
		}

		ModelAndView mv = new ModelAndView("/oa/system/orgAuthList.jsp")
		.addObject("orgAuthList", list)
		.addObject("orgId", orgId)
		.addObject("grade", Boolean.valueOf(true))
		.addObject("userAuth", userAuth)
		.addObject("topOrgId", topOrgId)
		.addObject("action", "grade");
		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除组织管理员")
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.orgAuthService.delByIds(lAryId);
			message = new ResultMessage(1, "删除组织管理员成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"edit"})
	@Action(description="编辑组织管理员")
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		Long userId = UserContextUtil.getCurrentUserId();
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId", 0L));
		Long topOrgId = Long.valueOf(RequestUtil.getLong(request, "topOrgId", 0L));
		boolean isGrade = RequestUtil.getBoolean(request, "isGrade");
		OrgAuth userAuth = null;

		if (topOrgId.longValue() != 0L) {
			userAuth = this.orgAuthService.getByUserIdAndOrgId(userId.longValue(), topOrgId.longValue());
		}
		ModelAndView mv = getAutoView();

		if (id.longValue() != 0L) {
			OrgAuth orgAuth = (OrgAuth)this.orgAuthService.getById(id);
			List<SysRole> roleList = this.sysRoleService.getByAuthId(id);
			mv.addObject("orgAuth", orgAuth)
			.addObject("roleList", roleList);
		} else {
			mv.addObject("sysOrg", this.sysOrgService.getById(orgId));
		}

		return mv.addObject("returnUrl", RequestUtil.getPrePage(request))
		.addObject("orgId", orgId).addObject("isGrade", Boolean.valueOf(isGrade))
		.addObject("userAuth", userAuth);
	}

	@RequestMapping({"get"})
	@Action(description="查看组织管理员明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		OrgAuth orgAuth = (OrgAuth)this.orgAuthService.getById(id);
		return getAutoView().addObject("orgAuth", orgAuth);
	}
}

