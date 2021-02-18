package com.cssrc.ibms.core.form.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.form.model.FormRule;
import com.cssrc.ibms.core.form.service.FormRuleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;


/**
 * 对象功能:表单验证规则 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/form/formRule/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormRuleFormController extends BaseFormController
{
	@Resource
	private FormRuleService formRuleService;
	
	/**
	 * 添加或更新表单验证规则。
	 * @param request
	 * @param response
	 * @param bpmFormRule 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新表单验证规则",
			detail="<#if StringUtil.isNotEmpty(isAdd)>" +
					"<#if isAdd==0>添加<#else>更新</#if>" +
					"表单验证规则【${SysAuditLinkService.getFormRuleLink(Long.valueOf(ruleId))}】成功" +
					"<#else>添加或更新表单验证规则【${name}】失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response, FormRule bpmFormRule,BindingResult bindResult) throws Exception
	{
		ResultMessage resultMessage=validForm("bpmFormRule", bpmFormRule, bindResult, request);
		String isAdd="0";
		if(resultMessage.getResult()==ResultMessage.Fail){
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg=null;
		if(bpmFormRule.getId()==null){
			bpmFormRule.setId(UniqueIdUtil.genId());
			formRuleService.add(bpmFormRule);
			resultMsg=getText("record.added",getText("controller.bpmFormRule"));
		}else{
			isAdd="1";
			formRuleService.update(bpmFormRule);
			resultMsg=getText("record.updated",getText("controller.bpmFormRule"));
		}
		//重新生成js验证文件。
		formRuleService.generateJS();
		LogThreadLocalHolder.putParamerter("isAdd", isAdd);
		LogThreadLocalHolder.putParamerter("ruleId", bpmFormRule.getId().toString());
		LogThreadLocalHolder.putParamerter("name", bpmFormRule.getName());
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param id
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected FormRule getFormObject(@RequestParam("id") Long id,Model model) throws Exception {
		logger.debug("enter FormRule getFormObject here....");
		FormRule bpmFormRule=null;
		if(id!=null){
			bpmFormRule=formRuleService.getById(id);
		}else{
			bpmFormRule= new FormRule();
		}
		return bpmFormRule;
    }

}
