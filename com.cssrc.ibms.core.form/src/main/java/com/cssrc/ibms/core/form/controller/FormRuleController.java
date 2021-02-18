package com.cssrc.ibms.core.form.controller;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.FormRule;
import com.cssrc.ibms.core.form.service.FormRuleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;


/**
 * 对象功能:表单验证规则 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/form/formRule/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormRuleController extends BaseController
{
	@Resource
	private FormRuleService formRuleService;
	
	/**
	 * 取得表单验证规则分页列表
	 * @param request
	 * @param response
	 * @param page    请求页面
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看表单验证规则分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<FormRule> list=formRuleService.getAll(new QueryFilter(request,"bpmFormRuleItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmFormRuleList",list);
		
		return mv;
	}
	
	/**
	 * 删除表单验证规则
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除表单验证规则",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除表单验证规则" +
					"<#list StringUtils.split(id,\",\") as item>" +
						"<#assign entity=bpmFormRuleService.getById(Long.valueOf(item))/>" +
						"【${entity.name}】"+
					"</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request); 
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");  
			formRuleService.delByIds(lAryId);
			formRuleService.generateJS();
			message=new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		}
		catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
    
	/**
	 * 编辑表单验证规则
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑表单验证规则")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		FormRule bpmFormRule=null;
		if(id!=0){
			 bpmFormRule= formRuleService.getById(id);
		}else{
			bpmFormRule=new FormRule();
		}
		return getAutoView().addObject("bpmFormRule",bpmFormRule).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得表单验证规则明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看表单验证规则明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		long canReturn=RequestUtil.getLong(request, "canReturn",0);
		FormRule bpmFormRule = formRuleService.getById(id);		
		return getAutoView().addObject("bpmFormRule", bpmFormRule).addObject("canReturn",canReturn);
	}
	
	/**
	 * 返回所有验证规则
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getAllRules")
	public List<FormRule> getAllRules(HttpServletRequest request) {
		List<FormRule> validRuleList = formRuleService.getAll();
		return validRuleList;
	}
	
	@RequestMapping("export")
	@Action(description="导出脚本",execOrder=ActionExecOrder.AFTER,
			detail="<#list StringUtils.split(id,\",\") as item>" +
					"<#assign entity=bpmFormRuleService.getById(Long.valueOf(item))/>" +
					"【${entity.name}】"+
					"</#list>")
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
		if(BeanUtils.isNotEmpty(lAryId)){
			Calendar now=Calendar.getInstance();				
			String localTime=now.get(Calendar.YEAR)+"-"+now.get(Calendar.MONTH)+"-"+now.get(Calendar.DATE);
			String strXml=formRuleService.exportXml(lAryId);
			response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename=FormRule"+localTime+".xml");
	        response.getWriter().write(strXml);
	        response.getWriter().flush();
	        response.getWriter().close();			
		}		
	}
	
	/**
	 * 导入表单验证规则的XML。
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description = "导入自定义表")
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {	
		MultipartFile fileLoad = request.getFile("xmlFile");
		formRuleService.importXml(fileLoad.getInputStream());
		ResultMessage message=null;		
		message=new ResultMessage(ResultMessage.Success,"导入成功");
		writeResultMessage(response.getWriter(), message);
	}
}
