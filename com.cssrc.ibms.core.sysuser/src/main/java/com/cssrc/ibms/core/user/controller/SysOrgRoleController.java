package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysOrgRole;
import com.cssrc.ibms.core.user.service.SysOrgRoleService;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 组织角色授权
 * <p>Title:SysOrgRoleController</p>
 * @author Yangbo 
 * @date 2016-8-5下午03:19:56
 */
@Controller
@RequestMapping( { "/oa/system/sysOrgRole/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class SysOrgRoleController extends BaseController {

	@Resource
	private SysOrgRoleService sysOrgRoleService;

	@Resource
	private SysOrgService sysOrgService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新组织角色授权信息", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>修改</#if>组织角色授权信息：<#assign entity=sysOrgRoleService.getById(Long.valueOf(id))/>组织名：${entity.orgName}，角色名：${entity.role.roleName}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysOrgRole.class }, pkName = "id")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		SysOrgRole sysOrgRole = getFormObject(request);
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isadd = true;
		try {
			if ((sysOrgRole.getId() == null)
					|| (sysOrgRole.getId().longValue() == 0L)) {
				sysOrgRole.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.sysOrgRoleService.add(sysOrgRole);
				resultMsg = "添加组织角色授权信息成功";
			} else {
				this.sysOrgRoleService.update(sysOrgRole);
				isadd = false;
				resultMsg = "更新组织角色授权信息成功";
			}
			result = 1;
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "组织角色授权失败:"
						+ str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 对form提交的Json对象进行转化
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected SysOrgRole getFormObject(HttpServletRequest request)
			throws Exception {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));

		String json = RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);

		SysOrgRole sysOrgRole = (SysOrgRole) JSONObject.toBean(obj,
				SysOrgRole.class);

		return sysOrgRole;
	}
	/**
	 * 组织授权列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "list" })
	@Action(description = "查看组织角色授权信息分页列表",detail="看组织角色授权信息分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysOrgRoleService.getAll(new QueryFilter(request,
				"sysOrgRoleItem"));
		ModelAndView mv = getAutoView().addObject("sysOrgRoleList", list);

		return mv;
	}
	/**
	 * 分配角色列表(弹出dilog选择)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "roleList" })
	@Action(description = "取得分配的角色列表",detail="取得分配的角色列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView roleList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		mv.addObject("scope", "global");
		mv = getRoleListMv(request, mv);
		return mv;
	}
	/**
	 * 分级组织角色分配列表(弹出dilog选择)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "roleGradeList" })
	@Action(description = "取得分配的角色列表",detail="取得分配的角色列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView roleGradeList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.addObject("scope", "grade");
		mv.setViewName("/oa/system/sysOrgRoleRoleList.jsp");
		mv = getRoleListMv(request, mv);
		return mv;
	}

	private ModelAndView getRoleListMv(HttpServletRequest request,
			ModelAndView mv) {
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		SysOrg sysOrg = (SysOrg) this.sysOrgService.getById(orgId);
		if (sysOrg == null) {
			mv.addObject("sysOrg", sysOrg);
		} else {
			List orgRoles = this.sysOrgRoleService.getRolesByOrgId(orgId);
			mv.addObject("orgRoles", orgRoles).addObject("orgId", orgId)
					.addObject("sysOrg", sysOrg);
		}
		return mv;
	}
	/**
	 * 组织分配角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "addOrgRole" })
	@Action(description = "新增组织角色授权信息", execOrder = ActionExecOrder.AFTER ,detail="${sysAuditLinkService.addSysOrgRoleChange(roleIds,orgId)}", exectype = SysAuditExecType.ADD_TYPE)
	public void addOrgRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		addOrgRole(request, response, 0);
	}
	/**
	 * 分级组织分配角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "addGradeOrgRole" })
	@Action(description = "新增组织角色授权信息", execOrder = ActionExecOrder.AFTER ,detail="${sysAuditLinkService.addSysOrgRoleChange(roleIds,orgId)}", exectype = SysAuditExecType.ADD_TYPE)
	public void addGradeOrgRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		addOrgRole(request, response, 1);
	}
	/**
	 * 给组织授权角色
	 * @param request
	 * @param response
	 * @param grade
	 * @throws Exception
	 */
	private void addOrgRole(HttpServletRequest request,
			HttpServletResponse response, int grade) throws Exception {
		Long[] roleIds = RequestUtil.getLongAryByStr(request, "roleIds");
		Long orgId = Long.valueOf(RequestUtil.getLong(request, "orgId"));
		ResultMessage resultMessage = null;
		try {
			String result = this.sysOrgRoleService.addOrgRole(roleIds, orgId,
					Integer.valueOf(grade));
			resultMessage = new ResultMessage(1, "给组织授权角色成功" + result);
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "给组织授权角色失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	/**
	 * 删除组织授权信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "del" })
	@Action(description = "删除组织角色授权信息", execOrder = ActionExecOrder.BEFORE ,detail="${sysAuditLinkService.getSysOrgRoleChange(id)}", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		delete(request, response);
	}
	/**
	 * 分级组织删除授权信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "delGrade" })
	@Action(description = "删除组织角色授权信息", execOrder = ActionExecOrder.BEFORE ,detail="${sysAuditLinkService.getSysOrgRoleChange(id)}", exectype = SysAuditExecType.DELETE_TYPE)
	public void delGrade(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		delete(request, response);
	}
	/**
	 * 删除组织授权信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			//此处返回orgid代表删除成功
			Long orgId = this.sysOrgRoleService.delByOrgRoleIds(lAryId);

			message = new ResultMessage(1, "删除组织角色授权信息成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑组织角色授权信息",detail="编辑组织角色授权信息", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		SysOrgRole sysOrgRole = (SysOrgRole) this.sysOrgRoleService.getById(id);

		return getAutoView().addObject("sysOrgRole", sysOrgRole).addObject(
				"returnUrl", returnUrl);
	}
	/**
	 * 组织角色授权明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "get" })
	@Action(description = "查看组织角色授权信息明细",detail="查看组织角色授权信息明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysOrgRole sysOrgRole = (SysOrgRole) this.sysOrgRoleService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("sysOrgRole", sysOrgRole);
	}
}