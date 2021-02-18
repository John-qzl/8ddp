package com.cssrc.ibms.core.flow.controller;

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
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
/**
 * 对象功能:流程实例扩展控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/processRun/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class ProcessRunFormController extends BaseFormController
{
	@Resource
	private ProcessRunService processRunService;
	
	/**
	 * 添加或更新流程实例扩展。
	 * @param request
	 * @param response
	 * @param processRun 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新流程实例扩展",execOrder=ActionExecOrder.AFTER,
			detail="<#if StringUtil.isNotEmpty(isAdd)>" +
					"<#if isAdd==0>添加" +
					"<#else>更新" +
					"</#if>" +
					"流程实例扩展  :【${SysAuditLinkService.getProcessRunLink(Long.valueOf(runId))}】" +
				"<#else>" +
					"添加或更新流程实例扩展：【${subject}】失败" +
				"</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response, ProcessRun processRun,BindingResult bindResult) throws Exception
	{
		
		ResultMessage resultMessage=validForm("processRun", processRun, bindResult, request);
		String isAdd="0";
		//add your custom validation rule here such as below code:
		//bindResult.rejectValue("name","errors.exist.student",new Object[]{"jason"},"重复姓名");
		if(resultMessage.getResult()==ResultMessage.Fail)
		{
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg=null;
		if(processRun.getRunId()==null){
			processRun.setRunId(UniqueIdUtil.genId());
			processRunService.add(processRun);
			resultMsg=getText("record.added",getText("controller.processRun.save"));
		}else{
			processRunService.update(processRun);
			resultMsg=getText("record.updated",getText("controller.processRun.save"));
			isAdd="1";
		}
		LogThreadLocalHolder.putParamerter("isAdd", isAdd);
		LogThreadLocalHolder.putParamerter("runId", processRun.getRunId().toString());
		LogThreadLocalHolder.putParamerter("subject", processRun.getSubject());
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param runId
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected ProcessRun getFormObject(@RequestParam("runId") Long runId,Model model) throws Exception {
		logger.debug("enter ProcessRun getFormObject here....");
		ProcessRun processRun=null;
		if(runId!=null){
			processRun=processRunService.getById(runId);
		}else{
			processRun= new ProcessRun();
		}
		return processRun;
    }

}
