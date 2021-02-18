package com.cssrc.ibms.system.controller;

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
import com.cssrc.ibms.system.model.SysParam;
import com.cssrc.ibms.system.service.DemensionService;
import com.cssrc.ibms.system.service.DictionaryService;
import com.cssrc.ibms.system.service.GlobalTypeService;
import com.cssrc.ibms.system.service.SysParamService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * <p>Title:SysParamController</p>
 * @author Yangbo 
 * @date 2016-8-3下午02:47:06
 */
@Controller
@RequestMapping( { "/oa/system/sysParam/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class SysParamController extends BaseController {

	@Resource
	private SysParamService sysParamService;

	@Resource
	private DemensionService demensionService;

	@Resource
	private DictionaryService dictionaryService;

	@Resource
	private GlobalTypeService clobalTypeService;

	@RequestMapping( { "list" })
	@Action(description = "查看组织或人员参数属性分页列表", execOrder = ActionExecOrder.AFTER, detail = "查看组织或人员参数属性分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysParamService.getAll(new QueryFilter(request,
				"sysParamItem"));
		ModelAndView mv = getAutoView().addObject("sysParamList", list)
				.addObject("dataTypeMap", SysParam.DATA_TYPE_MAP);
		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除组织或人员参数属性", execOrder = ActionExecOrder.BEFORE, detail = "删除组织或人员参数属性<#list StringUtils.split(paramId,\",\") as item><#assign entity=sysParamService.getById(Long.valueOf(item))/>【${entity.paramName}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "paramId");
			this.sysParamService.delByIds(lAryId);
			message = new ResultMessage(1, "删除组织或人员参数属性成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑组织或人员参数属性", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>编辑</#if>组织或人员参数属性", exectype = SysAuditExecType.UPDATE_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long paramId = Long.valueOf(RequestUtil.getLong(request, "paramId"));
		String returnUrl = RequestUtil.getPrePage(request);
		List demList = this.demensionService.getAll();
		List dicList = this.dictionaryService.getAll();
		List globalList = this.clobalTypeService.getAll();
		SysParam sysParam = null;
		boolean isadd = true;
		if (paramId.longValue() != 0L) {
			sysParam = (SysParam) this.sysParamService.getById(paramId);
			isadd = false;
		} else {
			sysParam = new SysParam();
		}
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		List categoryList = this.sysParamService
				.getDistinctCategory(null, null);
		return getAutoView().addObject("sysParam", sysParam).addObject(
				"returnUrl", returnUrl).addObject("dataTypeMap",
				SysParam.DATA_TYPE_MAP).addObject("demList", demList)
				.addObject("dicList", dicList).addObject("categoryList",
						categoryList)
				.addObject("isAdd", Boolean.valueOf(isadd));
	}

	@RequestMapping( { "get" })
	@Action(description = "查看组织或人员参数属性明细", execOrder = ActionExecOrder.AFTER, detail = "查看组织或人员参数属性明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "paramId");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		SysParam sysParam = (SysParam) this.sysParamService.getById(Long
				.valueOf(id));
		String demension = ((Demension) this.demensionService.getById(sysParam
				.getBelongDem())).getDemName();
		return getAutoView().addObject("sysParam", sysParam).addObject(
				"dataTypeMap", SysParam.DATA_TYPE_MAP).addObject("canReturn",
				Long.valueOf(canReturn)).addObject("demension", demension);
	}
}
