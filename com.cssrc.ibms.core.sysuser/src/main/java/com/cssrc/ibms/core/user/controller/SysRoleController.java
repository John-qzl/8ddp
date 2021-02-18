package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.AuthRoleService;
import com.cssrc.ibms.core.user.service.SysOrgRoleService;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.user.service.UserRoleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * 角色管理
 * <p>Title:SysRoleController</p>
 * @author Yangbo 
 * @date 2016-8-23上午10:48:12
 */
@Controller
@RequestMapping( { "/oa/system/sysRole/" })
@Action(ownermodel = SysAuditModelType.ROLE_MANAGEMENT)
public class SysRoleController extends BaseController {

	@Resource
	private SysRoleService sysRoleService;

	@Resource
	private UserRoleService userRoleService;

	@Resource
	private AuthRoleService authRoleService;
	
	@Resource
	private SysOrgRoleService sysOrgRoleService;
	/**
	 * 角色弹出对话框
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "selector" })
	@Action(description = "角色对话框显示", execOrder = ActionExecOrder.AFTER, detail = "角色对话框显示", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		//获取scope
		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		//是否分级
		boolean isGrade = RequestUtil.getBoolean(request, "isGrade");
		QueryFilter queryFilter = new QueryFilter(request, "sysRoleItem");
		String roleName = RequestUtil.getString(request, "Q_roleName_S");
		if (StringUtil.isNotEmpty(roleName)) {
			queryFilter.addFilterForIB("roleName", "%" + roleName + "%");
		}
		List list = null;
		if (isGrade) {
			//获取当前用户分级的授权角色
			list = this.sysRoleService.getUserAssignRole(queryFilter);
			queryFilter.setForWeb();
		}else if(typeVal.equals("self")){
			Long userId = UserContextUtil.getCurrentUserId();
			list = this.sysRoleService.getByUserId(userId);
		}else{//包括self
			list = this.sysRoleService.getAll(queryFilter);
		}
		ModelAndView mv = getAutoView()
				.addObject("sysRoleList", list)
				.addObject("isSingle", isSingle)
				.addObject("isGrade", Boolean.valueOf(isGrade))
				.addObject("type", type)
		        .addObject("typeVal", typeVal);
		return mv;
	}

	@RequestMapping( { "list" })
	@Action(description = "查看系统角色表分页列表", execOrder = ActionExecOrder.AFTER, detail = "查看系统角色表分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysRoleService.getRoleList(new QueryFilter(request,
				"sysRoleItem"));
		ModelAndView mv = getAutoView().addObject("sysRoleList", list);
		SysUser current_user = (SysUser) UserContextUtil.getCurrentUser();
		boolean isAdmin=false;
		if (SysUser.getAdminUsername().contains(current_user.getUsername())) {
			isAdmin = true;
		}
		List categoryList = this.sysRoleService.getDistinctCategory();
		mv.addObject("isAdmin", Boolean.valueOf(isAdmin)).addObject(
				"categoryList", categoryList);
		return mv;
	}
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "del" })
	@Action(description = "删除系统角色表", execOrder = ActionExecOrder.BEFORE, detail = "删除系统角色表<#list roleId?split(\",\") as item><#assign entity=sysRoleService.getById(Long.valueOf(item))/>${entity.roleName}【${entity.alias}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		SysUser current_user = (SysUser) UserContextUtil.getCurrentUser();
		//查询当前用户的角色，防止被强硬删除
		List<SysRole> userroles = this.sysRoleService.getByUserId(current_user
				.getUserId());
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "roleId");
			for (SysRole userrole : userroles) {
				//匹配删除的角色(没有存在即删除)
				int index = Arrays.binarySearch(lAryId, userrole.getRoleId());
				if (index < 0) {
					delByIds(lAryId);
					message = new ResultMessage(1, "删除系统角色成功");
				} else {
					message = new ResultMessage(0, "不能删除自身所在角色，删除系统角色失败");
				}
			}
		} catch (Exception e) {
			message = new ResultMessage(0, "删除系统角色失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	//有关角色的所有信息删除
	private void delByIds(Long[] lAryId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (Long id : lAryId) {
			this.sysRoleService.delById(id);
			this.userRoleService.delByRoleId(id);
			this.sysOrgRoleService.delByRoleId(id);
			//TODO 是否考虑分级管理的角色授权(这里有需要解开注释)--yangbo
			/*this.authRoleService.delByRoleId(id);*/
		}
	}
	/**
	 * 角色复制页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "copy" })
	public ModelAndView copy(HttpServletRequest request) throws Exception {
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId"));

		SysRole sysRole = (SysRole) this.sysRoleService.getById(roleId);
		return getAutoView().addObject("sysRole", sysRole);
	}
	/**
	 * 角色编辑页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "edit" })
	//@Action(description = "添加或编辑系统角色表", execOrder = ActionExecOrder.AFTER, detail = "添加或编辑系统角色表", exectype = SysAuditExecType.UPDATE_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId"));
		String returnUrl = RequestUtil.getPrePage(request);
		SysRole sysRole = null;
		if (roleId.longValue() != 0L) {
			sysRole = (SysRole) this.sysRoleService.getById(roleId);
		} else {
			sysRole = new SysRole();
		}
		List categoryList = this.sysRoleService.getDistinctCategory();
		return getAutoView().addObject("sysRole", sysRole).addObject("returnUrl", returnUrl)
				.addObject("categoryList", categoryList);
	}
	/**
	 * 角色明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "get" })
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		return getView(request, response, mv, 0);
	}
	/**
	 * 执行人信息处角色信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getByRoleId" })
	public ModelAndView getByRoleId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/oa/system/sysRoleGet.jsp");
		return getView(request, response, mv, 1);
	}

	@Action(description = "查看系统角色表明细", execOrder = ActionExecOrder.AFTER, detail = "查看系统角色表明细<#assign entity=sysRoleService.getById(Long.valueOf(roleId))/>${entity.roleName}【${entity.alias}】", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView getView(HttpServletRequest request,
			HttpServletResponse response, ModelAndView mv, int isOtherLink)
			throws Exception {
		long id = RequestUtil.getLong(request, "roleId");
		SysRole sysRole = (SysRole) this.sysRoleService.getById(Long
				.valueOf(id));
		return mv.addObject("sysRole", sysRole).addObject("isOtherLink",
				Integer.valueOf(isOtherLink));
	}
	/**
	 * 角色管理--复制角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "copyRole" })
	@Action(description = "复制角色", execOrder = ActionExecOrder.AFTER, detail = "复制角色<#assign entity=sysRoleService.getById(Long.valueOf(roleId))/>${entity.roleName}【${entity.alias}】", exectype = SysAuditExecType.ADD_TYPE)
	public void copyRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		long roleId = RequestUtil.getLong(request, "roleId");

		String roleName = RequestUtil.getString(request, "newRoleName");
		String alias = RequestUtil.getString(request, "newAlias");
		long newRoleId = UniqueIdUtil.genId();

		SysRole sysRole = (SysRole) this.sysRoleService.getById(Long
				.valueOf(roleId));
		//校验角色别名
		boolean rtn = this.sysRoleService.isExistRoleAlias(alias);
		if (rtn) {
			writeResultMessage(writer, "输入的别名已存在", 0);
			return;
		}

		SysRole newRole = (SysRole) sysRole.clone();
		newRole.setRoleId(Long.valueOf(newRoleId));
		newRole.setAlias(alias);
		newRole.setRoleName(roleName);

		Short result = 0;
		try {
			this.sysRoleService.copyRole(newRole, roleId);
			result = 1;
			writeResultMessage(writer, "复制角色成功!", 1);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "复制角色失败:"
						+ str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	/**
	 * 查找所有角色数据(用户管理)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getTreeData" })
	@ResponseBody
	@Action(description = "获取角色树形数据", execOrder = ActionExecOrder.AFTER, detail = "获取角色树形数据", exectype = SysAuditExecType.SELECT_TYPE)
	public List<SysRole> getTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<SysRole> rolList = this.sysRoleService.getAll();
		return getRoleTree(rolList);
	}
	/**
	 * 获取分级用户的角色数据(组织分级用户添加或编辑)
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getGradeTreeData" })
	@ResponseBody
	@Action(description = "获取角色树形数据", execOrder = ActionExecOrder.AFTER, detail = "获取分级角色树形数据", exectype = SysAuditExecType.SELECT_TYPE)
	public List<SysRole> getGradeTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long userId = UserContextUtil.getCurrentUserId().longValue();
		List<SysRole> rolList = this.sysRoleService.getByUser(userId);
		return getRoleTree(rolList);
	}
	
	private List<SysRole> getRoleTree(List<SysRole> rolList) {
		SysRole rol = null;
		List<SysRole> treeList = new ArrayList();

		rol = new SysRole();
		rol.setRoleId(new Long(0L));
		rol.setRoleName("全局角色");
		rol.setpId(1L);
		treeList.add(rol);

		for (SysRole sysRole : rolList) {
			treeList.add(sysRole);
		}
		return treeList;
	}
	/**
	 * 全部角色列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getAll" })
	@ResponseBody
	public List<SysRole> getAll(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<SysRole> list = this.sysRoleService.getAll(new QueryFilter(request, false));
		return list;
	}
	/**
	 * 角色管理禁用启用按钮
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "runEnable" })
	@Action(description = "禁用或启用角色", detail = "<#if enabled=='1'>启用<#else>禁用</#if>系统角色 ${SysAuditLinkService.getSysRoleLink(sysRoleService.getById(Long.valueOf(roleId)))}", exectype = SysAuditExecType.UPDATE_TYPE)
	public void runEnableRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long roleId = RequestUtil.getLong(request, "roleId");
		long enabled ;
		SysRole sysRole = (SysRole) this.sysRoleService.getById(Long
				.valueOf(roleId));
		if (sysRole.getStatus().equals(Short.valueOf((short) 1))) {
			sysRole.setStatus(Short.valueOf((short) 0));
			enabled=0;
		} else {
			sysRole.setStatus(Short.valueOf((short) 1));
			enabled=1;
		}
		this.sysRoleService.update(sysRole);
		try {
			LogThreadLocalHolder.putParamerter("enabled", enabled);
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect(RequestUtil.getPrePage(request));
	}
	/**
	 *角色选择对话框
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "dialog" })
	@Action(description = "选择角色", execOrder = ActionExecOrder.AFTER, detail = "选择角色", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String isSingle = RequestUtil.getString(request, "isSingle", "false");
		String isGrade = RequestUtil.getString(request, "isGrade", "false");
		return getAutoView().addObject("isSingle", isSingle).addObject(
				"isGrade", isGrade);
	}
	/**
	 * 批量授权资源处角色树
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getRoleTree" })
	@ResponseBody
	@Action(description = "获取角色树形数据", execOrder = ActionExecOrder.AFTER, detail = "获取角色树形数据", exectype = SysAuditExecType.SELECT_TYPE)
	public List<SysRole> getRoleTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, null);
		List<SysRole> sysRoleList = this.sysRoleService.getRoleTree(queryFilter);
		return sysRoleList;
	}
	
	/**
	 * 通过前台传过来的角色输入框的值对角色进行模糊查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"getFuzzySysRole"})
	public void getFuzzySysRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		
		String roleJson = "";
		
		String roleFuzzyName = RequestUtil.getString(request, "fuzzyName");
		String fieldName = RequestUtil.getString(request, "fieldName");
		
		String type = RequestUtil.getString(request, "type");
		String typeVal = RequestUtil.getString(request, "typeVal");
		String relvalue = RequestUtil.getString(request, "relvalue");
		
		if(fieldName==""){
			//若没有指定字段名称，默认为roleName
			fieldName="roleName";
		}
		
		try {
			JSONArray roleArr = this.sysRoleService.getFuzzySysRoleList(roleFuzzyName, fieldName, relvalue, type, typeVal);
			roleJson = roleArr.toString();
			
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, roleJson);
			resultMessage.addData("type", "role");
			out.print(resultMessage);
		} catch (Exception e) {
			// TODO: handle exception
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, e.getMessage());
			out.print(resultMessage);
		}
	}
}
