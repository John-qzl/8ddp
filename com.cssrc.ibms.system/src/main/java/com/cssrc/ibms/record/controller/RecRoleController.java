package com.cssrc.ibms.record.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.record.model.RecRole;
import com.cssrc.ibms.record.model.RecType;
import com.cssrc.ibms.record.service.RecRoleService;
import com.cssrc.ibms.record.service.RecTypeService;

@Controller
@RequestMapping("/oa/system/recRole/")
@Action(ownermodel=SysAuditModelType.ROLETYPE_PROJECT_MANAGEMENT)
public class RecRoleController extends BaseController {
	@Resource
	private RecTypeService recTypeService;

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
	@RequestMapping({"get"})
	@Action(description="查看表单角色明细", detail="查看表单角色明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long id = RequestUtil.getLong(request, "roleId");
		RecRole rr = (RecRole)this.recRoleService.getById(Long.valueOf(id));
		return getAutoView().addObject("recRole", rr);
	}
	@RequestMapping({"list"})
	@Action(description="查看表单角色列表", detail="查看表单角色列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long typeId = RequestUtil.getLong(request, "typeId");
		QueryFilter qf = new QueryFilter(request, "recRoleItem");
		if(!qf.equals(0L)){
			qf.getFilters().put("typeId", typeId);
		}
		List<RecRole> list = this.recRoleService.getAll(qf);
		return getAutoView().addObject("recRoleList", list).addObject("typeId", typeId);
	}
	@RequestMapping("manage")
	@Action(description="查看表单角色类别", detail="查看表单角色类别", exectype = SysAuditExecType.SELECT_TYPE )
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return getAutoView();
	}
	/**
	 * 角色编辑页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "edit" })
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
		Long roleId = Long.valueOf(RequestUtil.getLong(request, "roleId"));
		String returnUrl = RequestUtil.getPrePage(request);
		RecRole recRole = null;
		if (roleId.longValue() != 0L) {
			recRole = (RecRole) this.recRoleService.getById(roleId);
		} else {
			recRole = new RecRole();
		}
		if(BeanUtils.isEmpty(recRole.getFilter())){
			recRole.setFilter(this.getDefaultFilterJson().toString());
		}
		return getAutoView().addObject("recRole", recRole).addObject("returnUrl", returnUrl)
					.addObject("typeId", typeId);
	}
	
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "del" })
	@Action(description = "删除表单角色表", execOrder = ActionExecOrder.BEFORE, detail = "删除表单角色：<#list roleId?split(\",\") as item><#assign entity=recRoleService.getById(Long.valueOf(item))/>${entity.roleName}【${entity.alias}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "roleId");
			delByIds(lAryId);				
		} catch (Exception e) {
			message = new ResultMessage(0, "删除表单角色失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	//有关角色的所有信息删除
		private void delByIds(Long[] lAryId) {
			if (BeanUtils.isEmpty(lAryId))
				return;
			for (Long id : lAryId) {
				this.recRoleService.delById(id);
				//this.userRoleService.delByRoleId(id);
			}
		}
	
	/**
	 * 角色新增编辑表单提交
	 * @param request
	 * @param response
	 * @param recRole
	 * @param bindResult
	 * @throws Exception
	 */
	@RequestMapping( { "save" })
	@Action(description = "添加或更新日志开关", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>修改</#if>表单角色：${SysAuditLinkService.getRecRoleLink(Long.valueOf(id))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { RecRole.class }, pkName = "roleId")
	public void save(HttpServletRequest request, HttpServletResponse response,
			RecRole recRole, BindingResult bindResult) throws Exception {
		Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
		PrintWriter out = response.getWriter();
		String systemAlias = "";
		ResultMessage resultMessage = validForm("recRole", recRole, bindResult,
				request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		//设置操作结果，默认为操作失败
		Short resultStatus = 0;
		String resultMsg = null;
		boolean isExist = false;
		boolean isadd = true;
		String id = null;
		if (recRole.getRoleId() == null) {
			String tmpAlias = recRole.getAlias();
			String alias = systemAlias + tmpAlias;

			isExist = this.recRoleService.isExistRoleAlias(alias);
			if (!isExist) {
				recRole.setRoleId(Long.valueOf(UniqueIdUtil.genId()));
				recRole.setAlias(alias);
				this.recRoleService.add(recRole);
				resultStatus = 1;
				id = recRole.getRoleId().toString();
				String result = "{result:1,typeId:" + typeId + ",operate:'add'}";
				out.print(result);
			} else {
				resultMsg = "角色别名：[" + tmpAlias + "]已存在";
				id = recRoleService.getRoleByAlias(recRole.getAlias()).getRoleId().toString();
				writeResultMessage(response.getWriter(), resultMsg, 0);
			}
		} else {
			String tmpAlias = recRole.getAlias();
			String alias = systemAlias + tmpAlias;
			Long roleId = recRole.getRoleId();
			isExist = this.recRoleService.isExistRoleAliasForUpd(alias, roleId);
			if (isExist) {
				resultMsg = "角色别名：[" + tmpAlias + "]已存在";
				id = recRoleService.getRoleByAlias(recRole.getAlias()).getRoleId().toString();
				writeResultMessage(response.getWriter(), resultMsg, 0);
			} else {
				recRole.setAlias(alias);
				this.recRoleService.update(recRole);
				resultStatus = 1;
				id = recRole.getRoleId().toString();
				String result = "{result:1,typeId:" + typeId + ",operate:'edit'}";
				out.print(result);
				
			}
			isadd = false;
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id",id);
			LogThreadLocalHolder.setResult(resultStatus);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@RequestMapping( { "getRoleTreeData" })
	@ResponseBody
	@Action(description = "获取角色树形数据", execOrder = ActionExecOrder.AFTER, detail = "获取分级角色树形数据", exectype = SysAuditExecType.SELECT_TYPE)
	public List<RecRole> getRoleTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
		List<RecRole> rolList = this.recRoleService.getByTypeId(typeId);
		return getRoleTree(rolList);
	}
	
	private List<RecRole> getRoleTree(List<RecRole> rolList) {
		RecRole rol = null;
		List<RecRole> treeList = new ArrayList();

		for (RecRole recRole : rolList) {
			treeList.add(recRole);
		}
		return treeList;
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
		RecRole role = (RecRole) this.recRoleService.getById(roleId);
		return getAutoView().addObject("recRole", role);
	}
	/**
	 * 角色管理--复制角色
	 * @param request
	 * @param response
	 * @throws Exception
	 *//*
	@RequestMapping( { "copyRole" })
	@Action(description = "复制角色", execOrder = ActionExecOrder.AFTER, detail = "复制角色<#assign entity=recRoleService.getById(Long.valueOf(roleId))/>【${entity.roleName}】<#if isSuccess>成功<#else>失败</#if>", exectype = "管理日志")
	public void copyRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		long roleId = RequestUtil.getLong(request, "roleId");

		String roleName = RequestUtil.getString(request, "newRoleName");
		String alias = RequestUtil.getString(request, "newAlias");
		long newRoleId = UniqueIdUtil.genId();

		RecRole recRole = (RecRole) this.recRoleService.getById(Long
				.valueOf(roleId));
		//校验角色别名
		boolean rtn = this.recRoleService.isExistRoleAlias(alias);
		if (rtn) {
			writeResultMessage(writer, "输入的别名已存在", 0);
			return;
		}

		RecRole newRole = (RecRole) recRole.clone();
		newRole.setRoleId(Long.valueOf(newRoleId));
		newRole.setAlias(alias);
		newRole.setRoleName(roleName);

		boolean issuccess = true;
		try {
			this.recRoleService.copyRole(newRole, roleId);
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
			issuccess = false;
		}
		LogThreadLocalHolder.putParamerter("isSuccess", Boolean
				.valueOf(issuccess));
	}*/

	
	@RequestMapping( { "getAlowSetInfo" })
	public void getAlowSetInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<RecType> rtList = recTypeService.getAll();
		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
		Long userId = sysUser.getUserId();
		JSONObject json = new JSONObject();
		for(RecType rt : rtList){
			String typeAlias = rt.getAlias();
			boolean flag = this.recRoleService.canRecordSet(userId, typeAlias);
			json.put(typeAlias, flag);
		}
		PrintWriter out = response.getWriter();
		out.print(json.toString());
	}
	/**
	 * 默认的过滤条件
	 * 
	 * @return
	 */
	private JSONArray getDefaultFilterJson() {
		return JSONArray
				.fromObject("[{\"s\":4,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]");
	}
}
