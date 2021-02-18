package com.cssrc.ibms.core.form.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.service.FormDefService;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;


/**
 * 对象功能:FORM_DEF 控制器类  
 * 开发人员:zhulongchao  
 */
@Controller
@RequestMapping("/oa/form/formDef/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormDefFormController extends BaseFormController
{
	@Resource
	private FormDefService formDefService;
	

	/**
	 * 添加或更新自定义表单
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description = "添加或更新自定义表单",execOrder=ActionExecOrder.AFTER,
			detail="<#if isSuccess>" +
					"<#if isAdd>添加" +
					"<#else>更新" +
					"</#if>自定义表单：" +
					"【${SysAuditLinkService.getFormDefLink(defId)}】成功" +
					"<#else>" +
					"添加或更新自定义表单失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// 表定义
		String data = request.getParameter("data");
		
		JSONObject formDefJson=JSONObject.fromObject(data);
		
		String publishTime= formDefJson.getString("publishTime");
		if(StringUtil.isEmpty(publishTime)){
			formDefJson.put("publishTime", TimeUtil.getCurrentTime());
		}
		
		FormDef bpmFormDef = (FormDef) JSONObject.toBean(formDefJson, FormDef.class);
		
		String html=bpmFormDef.getHtml();
		html=html.replace("？", "");
		//logger.info(html);
		String template=FormUtil.getFreeMarkerTemplate(html,bpmFormDef.getTableId());	
		//logger.info(template);
		bpmFormDef.setTemplate(template);

		boolean isSuccess=true;
		boolean isadd=false;
		long defId=0;
		try{
			if (bpmFormDef.getFormDefId() == 0){
				isadd=true;
				formDefService.addForm(bpmFormDef);
				String msg = "表单新增成功";
				writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
			}
			else{
				formDefService.updateForm(bpmFormDef);
				String msg = "表单修改成功";
				writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
			}
			defId=bpmFormDef.getFormDefId();
		}
		catch (Exception e) {
			e.printStackTrace();
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,"保存失败"+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
			isSuccess=false;
		}
		LogThreadLocalHolder.putParamerter("isAdd", isadd);
		LogThreadLocalHolder.putParamerter("isSuccess", isSuccess);
		LogThreadLocalHolder.putParamerter("defId", String.valueOf(defId));
	}
	
}
