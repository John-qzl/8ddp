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
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.service.FormTemplateService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseFormController;

/**
 * 对象功能:表单模板 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/form/formTemplate/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormTemplateFormController extends BaseFormController
{
	@Resource
	private FormTemplateService formTemplateService;
	
	/**
	 * 添加或更新表单模板。
	 * @param request
	 * @param response
	 * @param bpmFormTemplate 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新表单模板",
			detail="<#if isAdd>添加<#else>更新</#if>" +
					"表单模板${SysAuditLinkService.getFormTemplateLink(Long.valueOf(ruleId))}",
			exectype = SysAuditExecType.UPDATE_TYPE)
    @DataNote(beanName = { FormTemplate.class }, pkName = "templateId")
	public void save(HttpServletRequest request, HttpServletResponse response, FormTemplate bpmFormTemplate,BindingResult bindResult) throws Exception
	{
		
		ResultMessage resultMessage=validForm("bpmFormTemplate", bpmFormTemplate, bindResult, request);
		//add your custom validation rule here such as below code:
		//bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
		boolean isAdd=true;
		if(resultMessage.getResult()==ResultMessage.Fail)
		{
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg=null;
		if(bpmFormTemplate.getTemplateId()==null){
			bpmFormTemplate.setTemplateId(UniqueIdUtil.genId());
			bpmFormTemplate.setCanEdit(1);
			String alias=bpmFormTemplate.getAlias();
			boolean isExist=formTemplateService.isExistAlias(alias);
			if(isExist){
				resultMsg=getText("record.add.fail",getText("controller.bpmFormTemplate.copyTemplate.already"));
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
				return;
			}else{
			    formTemplateService.add(bpmFormTemplate);
			    resultMsg=getText("record.added", getText("controller.bpmFormTemplate"));
			}
		}else{
			isAdd=false;
			formTemplateService.update(bpmFormTemplate);
			resultMsg=getText("record.updated",getText("controller.bpmFormTemplate"));
		}
		LogThreadLocalHolder.putParamerter("isAdd", isAdd);
		LogThreadLocalHolder.putParamerter("ruleId", bpmFormTemplate.getTemplateId().toString());
		LogThreadLocalHolder.putParamerter("name", bpmFormTemplate.getTemplateName());
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param templateId
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected FormTemplate getFormObject(@RequestParam("templateId") Long templateId,Model model) throws Exception {
		logger.debug("enter FormTemplate getFormObject here....");
		FormTemplate bpmFormTemplate=null;
		if(templateId!=null){
			bpmFormTemplate=formTemplateService.getById(templateId);
		}else{
			bpmFormTemplate= new FormTemplate();
		}
		return bpmFormTemplate;
    }

}
