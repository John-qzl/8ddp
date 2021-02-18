package com.cssrc.ibms.core.form.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.form.model.FormQuery;
import com.cssrc.ibms.core.form.service.FormQueryService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:通用表单查询 控制器类 
 */
@Controller
@RequestMapping("/oa/form/formQuery/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormQueryFormController extends BaseController
{
	@Resource
	private FormQueryService bpmFormQueryService;
	
	/**
	 * 添加或更新通用表单查询。
	 * @param request
	 * @param response
	 * @param bpmFormQuery 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新通用表单查询",
			detail="<#if StringUtil.isNotEmpty(isAdd)>" +
			"<#if isAdd==0>添加<#else>更新</#if>" +
			"通用表单查询【${SysAuditLinkService.getFormQueryLink(Long.valueOf(queryId))}】成功" +
			"<#else>添加或更新通用表单查询【${name}】失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response,FormQuery bpmFormQuery) throws Exception
	{
		String  isAdd="0";
		String resultMsg=null;		
		if(StringUtil.isEmpty(bpmFormQuery.getConditionfield())){
			resultMsg=getText("record.add.fail",getText("controller.bpmFormQuery.notSetConditionfield"));//"未设置查询的字段"
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}
		if(StringUtil.isEmpty(bpmFormQuery.getResultfield())){
			resultMsg=getText("record.add.fail",getText("controller.bpmFormQuery.notSetResultfield"));//"未设置返回的字段"
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
			return;
		}
		
		if(bpmFormQuery.getId()==0){
			bpmFormQuery.setId(UniqueIdUtil.genId());
			String alias=bpmFormQuery.getAlias();
			boolean isExist=bpmFormQueryService.isExistAlias(alias);
			if(isExist){
				resultMsg=getText("record.add.fail",getText("controller.bpmFormQuery.alias"));
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
				return;
			}
			bpmFormQueryService.add(bpmFormQuery);
			resultMsg=getText("record.added",getText("controller.bpmFormQuery"));
			
		}else{
			String alias=bpmFormQuery.getAlias();
			Long id=bpmFormQuery.getId();
			boolean isExist=bpmFormQueryService.isExistAliasForUpd(id, alias);
			if(isExist){
				resultMsg=getText("record.add.fail",getText("controller.bpmFormQuery.alias"));
				writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Fail);
				return;
			}
			isAdd="1";
			bpmFormQueryService.update(bpmFormQuery);
			resultMsg=getText("record.updated",getText("controller.bpmFormQuery"));
		}
		LogThreadLocalHolder.putParamerter("isAdd", isAdd);
		LogThreadLocalHolder.putParamerter("queryId", bpmFormQuery.getId().toString());
		LogThreadLocalHolder.putParamerter("name", bpmFormQuery.getName());
		writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
	}
	
	/**
	 * 取得 FormQuery 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute
	protected FormQuery getFormObject(@RequestParam("id") Long id, Model model) throws Exception
	{
		FormQuery bpmFormQuery = null;
		if (id > 0){
			bpmFormQuery = bpmFormQueryService.getById(id);
		} 
		else{
			bpmFormQuery = new FormQuery();
		}
		return bpmFormQuery;
	}
}
