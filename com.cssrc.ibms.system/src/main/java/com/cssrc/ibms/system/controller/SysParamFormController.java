package com.cssrc.ibms.system.controller;

import java.util.Date;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.system.model.SysParam;
import com.cssrc.ibms.system.service.SysParamService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 用户或组织自定义属性
 * EFFECT 参数类型 1.个人2.组织
 * <p>Title:SysParamFormController</p>
 * @author Yangbo 
 * @date 2016-8-20下午04:30:53
 */
@Controller
@RequestMapping( { "/oa/system/sysParam/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class SysParamFormController extends BaseFormController {

	@Resource
	private SysParamService sysParamService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新系统参数属性", detail = "<#if isAdd>添加<#else>更新</#if>系统参数：${SysAuditLinkService.getSysParamLink(Long.valueOf(id))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysParam.class }, pkName = "id")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysParam sysParam, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("sysParam", sysParam,
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		//设置操作结果，默认为操作失败
		Short result = 0;
		String resultMsg = null;
		boolean isadd = true;
		String id = null;
		if (sysParam.getParamId() == null) {
			if (this.sysParamService.getByParamKey(sysParam.getParamKey()) != null) {
				resultMsg = "参数Key:"+ sysParam.getParamKey() + ",已被使用";
				id = sysParamService.getByParamKey(sysParam.getParamKey()).getParamId().toString();
				writeResultMessage(response.getWriter(), resultMsg, 0);
			}else{
				sysParam.setParamId(Long.valueOf(UniqueIdUtil.genId()));
				sysParam = setSourceKey(request, sysParam);
				sysParam.setSysParam_creatorId(UserContextUtil.getCurrentUserId());
				sysParam.setSysParam_createTime(new Date());
				sysParam.setSysParam_updateId(UserContextUtil.getCurrentUserId());
				sysParam.setSysParam_updateTime(new Date());
				this.sysParamService.add(sysParam);
				result = 1;
				resultMsg = "添加系统参数属性成功";
				id = sysParam.getParamId().toString();
			}
		} else {
			sysParam = setSourceKey(request, sysParam);
			sysParam.setSysParam_updateId(UserContextUtil.getCurrentUserId());
			sysParam.setSysParam_updateTime(new Date());
			this.sysParamService.update(sysParam);
			resultMsg = "更新系统参数属性成功";
			result = 1;
			isadd = false;
			id = sysParam.getParamId().toString();
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id", id);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	private SysParam setSourceKey(HttpServletRequest request, SysParam sysParam) {
		String[] keys = request.getParameterValues("key");
		String[] values = request.getParameterValues("value");
		if (sysParam.getSourceType().equals("dict"))
			return sysParam;
		if (sysParam.getSourceType().equals("input"))
			sysParam.setSourceKey("");
		else {
			sysParam.setSourceKey(getJson(keys, values).toString());
		}
		return sysParam;
	}

	private JSONObject getJson(String[] keys, String[] values) {
		JSONObject json = new JSONObject();
		for (int i = 0; i < keys.length; i++) {
			json.put(keys[i], values[i]);
		}
		return json;
	}

	@ModelAttribute
	protected SysParam getFormObject(@RequestParam("paramId") Long paramId,
			Model model) throws Exception {
		this.logger.debug("enter SysParam getFormObject here....");
		SysParam sysParam = null;
		if (paramId != null)
			sysParam = (SysParam) this.sysParamService.getById(paramId);
		else {
			sysParam = new SysParam();
		}
		return sysParam;
	}
}
