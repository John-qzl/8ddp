package com.cssrc.ibms.record.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.rec.intf.IRecRoleFunService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
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
import com.cssrc.ibms.record.model.RecRoleSon;
import com.cssrc.ibms.record.model.RecType;
import com.cssrc.ibms.record.service.RecRoleService;
import com.cssrc.ibms.record.service.RecRoleSonFunService;
import com.cssrc.ibms.record.service.RecRoleSonService;
import com.cssrc.ibms.record.service.RecTypeService;

@Controller
@RequestMapping("/oa/system/recRoleSon/")
@Action(ownermodel=SysAuditModelType.ROLETYPE_PROJECT_MANAGEMENT)
public class RecRoleSonController extends BaseController {
	@Resource
	private RecTypeService recTypeService;
	@Resource
	private RecRoleSonService recRoleSonService;
	@Resource
	private RecRoleSonFunService recRoleSonFunService;
	@Resource
	private RecRoleService recRoleService;
	@Resource
	private IRecRoleFunService recRoleFunService;
	@Resource
	private ISysUserService sysUserService;
	
	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/record/"))
			viewName = viewName.replace("/oa/system/", "/oa/system/record/role/son/");
		mv.setViewName(viewName);
		return mv;
	}
	@RequestMapping({"list"})
	@Action(description="查看记录角色列表", detail="查看记录角色列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long typeId; 
		Long dataId = RequestUtil.getLong(request, "dataId");
		Long dataTemplateId = RequestUtil.getLong(request, "dataTemplateId");
		String typeAlias = RequestUtil.getString(request, "typeAlias");
		//不是第一次访问，从session中获取
		if(dataId.equals(0L)){
			dataId = Long.valueOf(request.getSession().getAttribute("dataId").toString());
			dataTemplateId = Long.valueOf(request.getSession().getAttribute("dataTemplateId").toString());
			typeAlias = request.getSession().getAttribute("typeAlias").toString();
		}else{
	   //第一次访问，放入session中
			request.getSession().setAttribute("dataId", dataId);
			request.getSession().setAttribute("dataTemplateId", dataTemplateId);
			request.getSession().setAttribute("typeAlias", typeAlias);
			request.getSession().setAttribute("isFrist", false);
		}
		QueryFilter qf = new QueryFilter(request, "recRoleSonItem");
		qf.getFilters().put("dataId", dataId);
		List<RecRoleSon> list = this.recRoleSonService.getAll(qf);	
		if(BeanUtils.isNotEmpty(list)){
			typeId = list.get(0).getTypeId();
		}else{
			//List为空需要进行同步
			recRoleSonService.initRecRoleSon(dataId,dataTemplateId,typeAlias);
			list = this.recRoleSonService.getAll(qf);	
			typeId = list.get(0).getTypeId();;
		}
		//权限控制
		boolean flag = recRoleSonService.isExistRoleSon(dataId);
		Long funId = RequestUtil.getLong(request, "funId");
		Long userId = UserContextUtil.getCurrentUserId();
		Map<String,Boolean> ButtonPermission = null;
		if(flag){
			String sonAlias = recRoleSonService.getAllRoleSonAliasByType(userId, dataId);
			ButtonPermission = recRoleSonFunService.getButtonPemission(sonAlias, funId, dataId);
			
		}else{
			String alias = recRoleService.getAllRoleAliasByType(userId, typeAlias);
			ButtonPermission = recRoleFunService.getButtonPemission(alias, funId);
		}
		return getAutoView().addObject("recRoleSonList", list).addObject("typeId", typeId).addObject("ButtonPermission", ButtonPermission);
	}
	@RequestMapping({"roleSync"})
	@Action(description="角色信息同步", detail="角色信息同步", exectype = SysAuditExecType.SELECT_TYPE)
	public void roleSync(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long dataId = Long.valueOf(request.getSession().getAttribute("dataId").toString());
			Long dataTemplateId = Long.valueOf(request.getSession().getAttribute("dataTemplateId").toString());
			String typeAlias = request.getSession().getAttribute("typeAlias").toString();
			recRoleSonService.initRecRoleSon(dataId,dataTemplateId,typeAlias);				
		} catch (Exception e) {
			message = new ResultMessage(0, "角色信息同步失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	/**
	 * 角色编辑页面
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "edit" })
	@Action(description = "添加或编辑记录角色表", execOrder = ActionExecOrder.AFTER, detail = "打开添加或编辑记录角色表页面", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long dataId = Long.valueOf(request.getSession().getAttribute("dataId").toString());
		Long dataTemplateId = Long.valueOf(request.getSession().getAttribute("dataTemplateId").toString());
		String typeAlias = request.getSession().getAttribute("typeAlias").toString();		
		Long roleSonId = RequestUtil.getLong(request, "roleSonId");
		RecRoleSon recRoleSon = null;
		
		if (roleSonId.longValue() != 0L) {
			recRoleSon = (RecRoleSon) this.recRoleSonService.getById(roleSonId);
		} else {
			RecType rt = this.recRoleSonService.getRecType(typeAlias);
			recRoleSon = new RecRoleSon();
			recRoleSon.setDataId(dataId);
			recRoleSon.setDataTemplateId(dataTemplateId);
			recRoleSon.setTypeName(rt.getTypeName());
			recRoleSon.setTypeId(rt.getTypeId());
		}
		return getAutoView().addObject("recRoleSon", recRoleSon);
	}
	
	/**
	 * 删除角色
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping( { "del" })
	@Action(description = "删除记录角色表", execOrder = ActionExecOrder.BEFORE, detail = "删除记录角色表<#list roleId?split(\",\") as item><#assign entity=recRoleSonService.getById(Long.valueOf(item))/>【${entity.roleName}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "roleSonId");
			delByIds(lAryId);				
		} catch (Exception e) {
			message = new ResultMessage(0, "删除记录角色失败:" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	// 有关角色的所有信息删除
	private void delByIds(Long[] lAryId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (Long id : lAryId) {
			this.recRoleSonService.delById(id);
			//删除此角色分配的功能点
			recRoleSonFunService.delByRoleSonId(id);
		}
	}
	
	/**
	 * 角色新增编辑记录提交
	 */
	@RequestMapping( { "save" })
	@Action(description = "添加或更新记录角色表", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>记录角色表：<#assign ent=recRoleSonService.getById(id)/>【${ent.roleSonName}】", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { RecRoleSon.class }, pkName = "roleSonId")
	public void save(HttpServletRequest request, HttpServletResponse response,
			RecRoleSon recRoleSon, BindingResult bindResult) throws Exception {
		PrintWriter out = response.getWriter();
		//设置操作结果，默认为操作失败
		Short resultStatus = 0;
		ResultMessage resultMessage = validForm("recRoleSon", recRoleSon, bindResult,
				request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		boolean isExist = false;
		boolean isadd = true;
		if (recRoleSon.getRoleSonId() == null) {
			String alias = recRoleSon.getAlias();
			Long dataId = recRoleSon.getDataId();
			isExist = this.recRoleSonService.isExistRoleAlias(alias,dataId);
			if (!isExist) {
				recRoleSon.setRoleSonId(Long.valueOf(UniqueIdUtil.genId()));
				this.recRoleSonService.add(recRoleSon);
				resultStatus = 1;
				String result = "{result:1,operate:'add'}";
				out.print(result);
			} else {
				resultMsg = "角色别名：[" +alias+ "]已存在";
				writeResultMessage(response.getWriter(), resultMsg, 0);
			}
		} else {
			String alias = recRoleSon.getAlias();
			Long roleSonId = recRoleSon.getRoleSonId();
			Long dataId = recRoleSon.getDataId();
			isExist = this.recRoleSonService.isExistRoleAliasForUpd(alias, roleSonId,dataId);
			if (isExist) {
				resultMsg = "角色别名：[" + alias + "]已存在";
				writeResultMessage(response.getWriter(), resultMsg, 0); 
			} else {
				this.recRoleSonService.update(recRoleSon);
				resultStatus = 1;
				String result = "{result:1,operate:'edit'}";
				out.print(result);				
			}
			isadd = false;
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("isExist", Boolean.valueOf(isExist));
			LogThreadLocalHolder.setResult(resultStatus);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	@RequestMapping({"addUser"})
	@Action(description="添加用户",detail="向<#assign ent=recRoleSonService.getById(Long.valueOf(roleSonId))/>【${ent.roleName}】中添加用户 <#list userIds?split(\",\") as item><#assign entity=sysUserService.getById(Long.valueOf(item))/>${entity.fullname}【${entity.username}】</#list>", exectype = SysAuditExecType.ADD_TYPE)
	public void addUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "userIds");
		Long roleSonId = RequestUtil.getLong(request, "roleSonId");
		this.recRoleSonService.addUser(lAryId,roleSonId);
		String preUrl = RequestUtil.getPrePage(request);
		response.sendRedirect(preUrl);
	}
	@RequestMapping({"delUser"})
	@Action(description="删除用户", execOrder = ActionExecOrder.BEFORE,detail="从<#assign ent=recRoleSonService.getById(Long.valueOf(roleSonId))/>【${ent.roleName}】中删除用户 <#list __pk__?split(\",\") as item><#assign entity=sysUserService.getById(Long.valueOf(item))/>${entity.fullname}【${entity.username}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void delUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "__pk__");
		Long roleSonId = RequestUtil.getLong(request, "roleSonId");
		this.recRoleSonService.delUser(lAryId,roleSonId);
		String preUrl = RequestUtil.getPrePage(request);
		response.sendRedirect(preUrl);	
	}
}
