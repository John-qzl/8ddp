package com.cssrc.ibms.system.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.Demension;
import com.cssrc.ibms.system.service.DemensionService;

/**
 * 系统管理-维度管理功能
 * @author Yangbo 2016-8-1
 */
@Controller
@RequestMapping("/oa/system/demension/")
@Action(ownermodel = SysAuditModelType.DIMENSION_MANAGEMENT)
public class DemensionController extends BaseController {
	@Resource
	private DemensionService demensionService;

	@RequestMapping( { "list" })
	@Action(description = "查看维度管理信息分页列表", execOrder = ActionExecOrder.AFTER, detail = "查看维度管理信息分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Demension> list = this.demensionService.getDemenByQuery(new QueryFilter(
				request, "demensionItem"));
		ModelAndView mv = getAutoView().addObject("demensionList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除维度管理信息", execOrder = ActionExecOrder.BEFORE, detail = "删除系统维度信息 <#list demId?split(\",\") as item><#assign entity=demensionService.getById(Long.valueOf(item))/> ${entity.demName}【${entity.demDesc}】 </#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "demId");
			this.demensionService.delByIds(lAryId);
			message = new ResultMessage(1, "删除维度成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除维度失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "添加或编辑维度管理信息", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加维度管理信息<#else>编辑维度管理信息<#assign entity=demensionService.getById(Long.valueOf(demId))/>${entity.demName}【${entity.demDesc}】</#if>", exectype = SysAuditExecType.UPDATE_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long demId = Long.valueOf(RequestUtil.getLong(request, "demId"));
		Demension demension = null;
		boolean isadd = true;
		if (demId.longValue() != 0L) {
			demension = (Demension) this.demensionService.getById(demId);
			isadd = false;
		} else {
			demension = new Demension();
		}
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		return getAutoView().addObject("demension", demension).addObject(
				"demId", demId);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看维度管理信息明细", execOrder = ActionExecOrder.AFTER, detail = "查看维度管理信息明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "demId");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		Demension demension = (Demension) this.demensionService.getById(Long
				.valueOf(id));
		return getAutoView().addObject("demension", demension).addObject(
				"canReturn", Long.valueOf(canReturn));
	}

}
