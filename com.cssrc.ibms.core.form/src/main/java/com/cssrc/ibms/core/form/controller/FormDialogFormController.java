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
import com.cssrc.ibms.core.form.model.FormDialog;
import com.cssrc.ibms.core.form.service.FormDialogService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:通用表单对话框 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/form/formDialog/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormDialogFormController extends BaseFormController
{
	@Resource
	private FormDialogService bpmFormDialogService;
	
	/**
	 * 添加或更新通用表单对话框。
	 * @param request
	 * @param response
	 * @param bpmFormDialog 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新通用表单对话框",
	detail="<#if StringUtil.isNotEmpty(isAdd)>" +
			"<#if isAdd==0>添加<#else>更新</#if>" +
			"通用表对话框【${SysAuditLinkService.getBpmFormDialogLink(Long.valueOf(dialogId))}】成功" +
			"<#else>添加或更新通用表对话框【${name}】失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response, FormDialog bpmFormDialog,BindingResult bindResult) throws Exception
	{
		ResultMessage resultMessage=validForm("bpmFormDialog", bpmFormDialog, bindResult, request);
		String  isAdd="0"; 
		if(resultMessage.getResult()==ResultMessage.Fail){
			writeResultMessage(response.getWriter(),resultMessage);
			return;
		}
		String resultMsg="";
		
		if(StringUtil.isEmpty(bpmFormDialog.getDisplayfield())){
			resultMsg=getText("record.add.fail", getText("controller.bpmFormDialog.notSetDisplayField"));
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}
		if(StringUtil.isEmpty(bpmFormDialog.getResultfield())){
			resultMsg=getText("record.add.fail", getText("controller.bpmFormDialog.notSetResultfield"));
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}	
		try{
			if(bpmFormDialog.getId()==0){
				bpmFormDialog.setId(UniqueIdUtil.genId());
				String alias=bpmFormDialog.getAlias();
				boolean isExist=bpmFormDialogService.isExistAlias(alias);
				if(isExist){
					resultMsg=getText("record.add.fail",getText("controller.bpmFormDialog.alias"));
					writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
					return;
				}
				bpmFormDialogService.add(bpmFormDialog);
				resultMsg=getText("record.added",getText("controller.bpmFormDialog.dialog"));
				
			}else{
				String alias=bpmFormDialog.getAlias();
				Long id=bpmFormDialog.getId();
				boolean isExist=bpmFormDialogService.isExistAliasForUpd(id, alias);
				if(isExist){
					resultMsg=getText("record.add.fail",getText("controller.bpmFormDialog.alias"));
					writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
					return;
				}
				bpmFormDialogService.update(bpmFormDialog);
				resultMsg=getText("record.updated",getText("controller.bpmFormDialog.dialog"));
				isAdd="1";
			}
		}catch(Exception e){
			//e.printStackTrace();
			resultMsg=getText("record.add.fail",e.getCause());
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}
		LogThreadLocalHolder.putParamerter("isAdd", isAdd);
		LogThreadLocalHolder.putParamerter("dialogId", bpmFormDialog.getId().toString());
		LogThreadLocalHolder.putParamerter("name", bpmFormDialog.getName());
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 在实体对象进行封装前，从对应源获取原实体
	 * @param ID
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute
    protected FormDialog getFormObject(@RequestParam("id") Long id,Model model) throws Exception {
		logger.debug("enter BpmFormDialog getFormObject here....");
		FormDialog bpmFormDialog=null;
		if(id>0){
			bpmFormDialog=bpmFormDialogService.getById(id);
		}else{
			bpmFormDialog= new FormDialog();
		}
		return bpmFormDialog;
    }

}
