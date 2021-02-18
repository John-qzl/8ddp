package com.cssrc.ibms.system.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.system.model.Demension;
import com.cssrc.ibms.system.service.DemensionService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
 * 
 * <p>
 * Title:DemensionFormController
 * </p>
 * 
 * @author Yangbo
 * @date 2016-8-1下午04:54:28
 */
@Controller
@RequestMapping( { "/oa/system/demension/" })
@Action(ownermodel = SysAuditModelType.DIMENSION_MANAGEMENT)
public class DemensionFormController extends BaseFormController {

	@Resource
	private DemensionService demensionService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新维度管理信息", execOrder = ActionExecOrder.AFTER, detail = "<#if isExit>【维度[${demName}]名称已经存在】<#else><#if isAdd>添加<#else>更新</#if>维度管理信息 ${SysAuditLinkService.getDemensionLink(Long.valueOf(demId))}</#if>", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { Demension.class }, pkName = "demId")
	public void save(HttpServletRequest request, HttpServletResponse response,
			Demension demension, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("demension", demension,
				bindResult, request);

		Long currentUserId = UserContextUtil.getCurrentUserId();
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isadd = true;
		boolean isExit = false;
		String demId = null;
		if (demension.getDemId() == null) {
			Map param = new HashMap();
			param.put("demName", demension.getDemName());
			boolean isTrue = this.demensionService.getNotExists(param);
			if (isTrue) {
				demension.setDemId(Long.valueOf(UniqueIdUtil.genId()));
				demension.setDemension_delFlag((short)0);
				demension.setDemension_creatorId(currentUserId);
				demension.setDemension_createTime(new Date());
				demension.setDemension_updateId(currentUserId);
				demension.setDemension_updateTime(new Date());
				this.demensionService.add(demension);
				result = 1;
				resultMsg = "维度保存成功";
				demId = demension.getDemId().toString();
				writeResultMessage(response.getWriter(), resultMsg, 1);
			} else {
				isExit = true;
				resultMsg = "该维度已经存在！";
				demId = demensionService.getByName(demension.getDemName()).getDemId().toString();
				writeResultMessage(response.getWriter(), resultMsg, 0);
			}
		} else {
			demension.setDemension_updateId(currentUserId);
			demension.setDemension_updateTime(new Date());
			this.demensionService.update(demension);
			result = 1;
			resultMsg = "维度更新成功";
			demId = demension.getDemId().toString();
			writeResultMessage(response.getWriter(), resultMsg, 1);
			isadd = false;
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("isExit", Boolean.valueOf(isExit));
			LogThreadLocalHolder.putParamerter("demId", demId);
			LogThreadLocalHolder.putParamerter("demName", demension.getDemName());
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@ModelAttribute
	protected Demension getFormObject(@RequestParam("demId") Long demId,
			Model model) throws Exception {
		this.logger.debug("enter Demension getFormObject here....");
		Demension demension = null;
		if (demId != null)
			demension = (Demension) this.demensionService.getById(demId);
		else {
			demension = new Demension();
		}
		return demension;
	}
}
