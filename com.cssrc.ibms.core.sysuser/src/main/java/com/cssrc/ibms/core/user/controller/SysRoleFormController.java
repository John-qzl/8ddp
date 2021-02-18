package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.user.model.SysRole;
import com.cssrc.ibms.core.user.service.SysRoleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 角色管理表单
 * <p>Title:SysRoleFormController</p>
 * @author Yangbo 
 * @date 2016-8-24下午09:04:02
 */
@Controller
@RequestMapping( { "/oa/system/sysRole/" })
@Action(ownermodel = SysAuditModelType.ROLE_MANAGEMENT)
public class SysRoleFormController extends BaseFormController {

	@Resource
	private SysRoleService sysRoleService;
	@Resource
    private IMessageProducer messageProducer;
	/**
	 * 角色新增编辑表单提交
	 * @param request
	 * @param response
	 * @param sysRole
	 * @param bindResult
	 * @throws Exception
	 */
	@RequestMapping( { "save" })
	@Action(description = "添加或更新系统角色表",exectype=SysAuditExecType.UPDATE_TYPE, detail = "<#if isExist>角色${roleName}已存在!<#else><#if isAdd>添加<#else>更新</#if>系统角色表 ${SysAuditLinkService.getSysRoleLink(sysRoleService.getById(Long.valueOf(roleId)))}</#if>")
	@DataNote(beanName = { SysRole.class }, pkName = "roleId")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysRole sysRole, BindingResult bindResult) throws Exception {
		String systemAlias = "";
		ResultMessage resultMessage = validForm("sysRole", sysRole, bindResult,
				request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		boolean isExist = false;
		boolean isadd = true;
		if (sysRole.getRoleId() == null) {
			String tmpAlias = sysRole.getAlias();
			String alias = systemAlias + tmpAlias;

			isExist = this.sysRoleService.isExistRoleAlias(alias);
			if (!isExist) {
				sysRole.setRoleId(Long.valueOf(UniqueIdUtil.genId()));
				sysRole.setAlias(alias);
				this.sysRoleService.add(sysRole);
				resultMsg = "添加系统角色成功";
				writeResultMessage(response.getWriter(), resultMsg, 1);
			} else {
				resultMsg = "角色别名：[" + tmpAlias + "]已存在";
				writeResultMessage(response.getWriter(), resultMsg, 0);
			}
		} else {
			String tmpAlias = sysRole.getAlias();
			String alias = systemAlias + tmpAlias;
			Long roleId = sysRole.getRoleId();
			isExist = this.sysRoleService.isExistRoleAliasForUpd(alias, roleId);
			if (isExist) {
				resultMsg = "角色别名：[" + tmpAlias + "]已存在";
				writeResultMessage(response.getWriter(), resultMsg, 0);
			} else {
				sysRole.setAlias(alias);
				this.sysRoleService.update(sysRole);
				resultMsg = "更新系统角色成功";
				writeResultMessage(response.getWriter(), resultMsg, 1);
			}
			isadd = false;
		}
		//发送 同步消息
        messageProducer.sendMdm(sysRole);

        try {
        	LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
    		LogThreadLocalHolder.putParamerter("isExist", Boolean.valueOf(isExist));
    		if (!isExist){
    	          LogThreadLocalHolder.putParamerter("roleId", sysRole.getRoleId()
                      .toString());
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@ModelAttribute
	protected SysRole getFormObject(@RequestParam("roleId") Long roleId,
			Model model) throws Exception {
		this.logger.debug("enter SysRole getFormObject here....");
		SysRole sysRole = null;
		if (roleId != null)
			sysRole = (SysRole) this.sysRoleService.getById(roleId);
		else {
			sysRole = new SysRole();
		}
		return sysRole;
	}
}
