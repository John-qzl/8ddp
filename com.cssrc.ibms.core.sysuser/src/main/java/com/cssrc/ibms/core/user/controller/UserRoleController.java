package com.cssrc.ibms.core.user.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.UserRole;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.user.service.UserRoleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 * 角色用户分配
 * <p>Title:UserRoleController</p>
 * @author Yangbo 
 * @date 2016-8-24下午08:23:55
 */
@Controller
@RequestMapping( { "/oa/system/userRole/" })
@Action(ownermodel = SysAuditModelType.ROLE_MANAGEMENT)
public class UserRoleController extends BaseController {

	@Resource
	private UserRoleService userRoleService;
	@Resource
	private SysRoleService  sysRoleService;
	
	@RequestMapping( { "del" })
	@Action(description = "删除用户与角色映射",exectype=SysAuditExecType.DELETE_TYPE,
		execOrder=ActionExecOrder.BEFORE,
		detail="<#list StringUtil.split(userRoleId,\",\") as item>"+
		"<#if item_index==0>从角色<#assign entity=userRoleService.getById(Long.valueOf(item))/>"+
		"【${sysRoleService.getById(entity.roleId).getRoleName()}】 中删除了以下用户</#if>"+
		"<#assign entity=userRoleService.getById(Long.valueOf(item))/>【${entity.fullname}】</#list>"
		)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "userRoleId");
			this.userRoleService.delByUserRoleId(lAryId);
			message = new ResultMessage(1, "删除用户成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除用户失败：" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "查看用户角色映射表",detail="查看角色【${sysRoleService.getById(roleId).getRoleName()}】中的人员")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv = getAutoView();
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId"));
		String roleName = sysRoleService.getById(roleId).getRoleName();
		QueryFilter queryFilter = new QueryFilter(request, "userRoleItem", true);
		queryFilter.addFilterForIB("roleId", roleId);
		List userRoleList = this.userRoleService.getAll(queryFilter);
		mv.addObject("userRoleList", userRoleList).addObject("roleId", roleId)
				.addObject("roleName", roleName);
		return mv;
	}

	@RequestMapping( { "add" })
	@Action(description = "添加用户",exectype=SysAuditExecType.ADD_TYPE,execOrder=ActionExecOrder.BEFORE,
			detail="在角色【${sysRoleService.getById(roleId).getRoleName()}】中,加入以下人员"+
					"<#list StringUtil.split(userIds,\",\") as item>"+
					"<#assign entity=sysUserService.getById(Long.valueOf(item))/>【${entity.fullname}】</#list>"
					)
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId", 0L));
		Long[] userIds = RequestUtil.getLongAryByStr(request, "userIds");
		this.userRoleService.add(roleId, userIds);
		
		response.sendRedirect(RequestUtil.getPrePage(request));
	}

	@RequestMapping( { "getUserListByRoleId" })
	@ResponseBody
	@Action(description = "根据角色ID取得用户List")
	public List<UserRole> getUserListByRoleId(HttpServletRequest request)
			throws Exception {
		String roleId = RequestUtil.getString(request, "roleId");
		return this.userRoleService.getUserByRoleIds(roleId);
	}
}
