package com.cssrc.ibms.core.form.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.service.FormTemplateService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 对象功能:表单模板 控制器类 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/form/formTemplate/")
@Action(ownermodel = SysAuditModelType.FORM_MANAGEMENT)
public class FormTemplateController extends BaseController {
	@Resource
	private FormTemplateService formTemplateService;

	/**
	 * 取得表单模板分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看表单模板分页列表", detail = "查看表单模板分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getUrl(request);
		returnUrl = returnUrl.replace("&", "@");
		List<FormTemplate> list = formTemplateService.getAll(new QueryFilter(
				request, "bpmFormTemplateItem"));
		ModelAndView mv = this.getAutoView()
				.addObject("bpmFormTemplateList", list)
				.addObject("returnUrl", returnUrl);

		return mv;
	}

	// 此方法找不到对应的jsp路径啊
	@RequestMapping("selector")
	@Action(description = "查看表单模板分页列表", detail = "查看表单模板分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<FormTemplate> list = formTemplateService.getAll(new QueryFilter(
				request, "bpmFormTemplateItem"));
		ModelAndView mv = this.getAutoView().addObject("bpmFormTemplateList",
				list);

		return mv;
	}

	/**
	 * 删除表单模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除表单模板", execOrder = ActionExecOrder.BEFORE, detail = "删除表单模板："
			+ "<#list StringUtil.split(templateId,\",\") as item>"
			+ "<#assign entity=formTemplateService.getById(Long.valueOf(item))/>"
			+ "【${entity.templateName}】" + "</#list>",
			exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "templateId");
			formTemplateService.delByIds(lAryId);
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.del.success"));
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.del.fail") + ":" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description = "编辑表单模板",detail = "编辑表单模板【${formTemplateService.getById(Long.valueOf(templateId)).getTemplateName()}】",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long templateId = RequestUtil.getLong(request, "templateId");
		String returnUrl = request.getParameter("returnUrl");
		if (returnUrl != null) {
			returnUrl = returnUrl.replace("@", "&");
		}
		// String preUrl=RequestUtil.getPrePage(request);
		String preUrl = request.getHeader("Referer");
		FormTemplate bpmFormTemplate = null;
		if (templateId != 0) {
			bpmFormTemplate = formTemplateService.getById(templateId);
		} else {
			bpmFormTemplate = new FormTemplate();
		}
		List<FormTemplate> macroTemplates = formTemplateService
				.getAllMacroTemplate();
		return getAutoView().addObject("bpmFormTemplate", bpmFormTemplate)
				.addObject("macroTemplates", macroTemplates)
				.addObject("returnUrl1", returnUrl).addObject("preUrl", preUrl);
	}

	/**
	 * 取得表单模板明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看表单模板明细",detail = "查看表单模板【${formTemplateService.getById(Long.valueOf(templateId)).getTemplateName()}】的明细",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		long id = RequestUtil.getLong(request, "templateId");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0);
		FormTemplate bpmFormTemplate = formTemplateService.getById(id);
		return getAutoView().addObject("bpmFormTemplate", bpmFormTemplate)
				.addObject("canReturn", canReturn)
				.addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得初始化模板信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("init")
	@Action(description = "初始化模板", detail = "初始化表单模板",exectype = SysAuditExecType.INIT_TYPE)
	public void init(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			formTemplateService.initAllTemplate();
			message = new ResultMessage(ResultMessage.Success, "模板初始化成功");
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail, "模板初始化失败" + ":"
					+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 将用户自定义模板备份
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("backUp")
	@Action(description = "备份模板", detail = "备份表单模板【${formTemplateService.getById(Long.valueOf(templateId)).getTemplateName()}】",exectype = SysAuditExecType.BACKUP_TYPE)
	public void backUp(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		long id = RequestUtil.getLong(request, "templateId");
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			formTemplateService.backUpTemplate(id);
			message = new ResultMessage(ResultMessage.Success,
					getText("controller.bpmFormTemplate.backUp.success"));
		} catch (Exception ex) {
			message = new ResultMessage(ResultMessage.Fail,
					getText("controller.bpmFormTemplate.backUp.fail") + ":"
							+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 复制模板信息
	 * 
	 * @param request
	 * @param response
	 * @param template
	 * @throws Exception
	 */
	@RequestMapping("copyTemplate")
	@Action(description = "复制模板", detail = "<#if !isExist>"
			+ "复制表单模板【${formTemplateService.getById(Long.valueOf(templateId)).getTemplateName()}】,"
			+ "新表单模板名称为【${newTemplateName}】" + "</#if>",exectype = SysAuditExecType.COPY_TYPE)
	public void copyTemplate(HttpServletRequest request,
			HttpServletResponse response, FormTemplate template)
			throws Exception {
		long id = RequestUtil.getLong(request, "templateId");
		FormTemplate bpmFormTemplate = formTemplateService.getById(id);
		String name = RequestUtil.getString(request, "newTemplateName");
		String newAlias = RequestUtil.getString(request, "newAlias");
		boolean isExist = formTemplateService.isExistAlias(newAlias);
		LogThreadLocalHolder.putParamerter("isExist", isExist);
		if (isExist) {
			writeResultMessage(response.getWriter(), new ResultMessage(
					ResultMessage.Fail,
					getText("controller.bpmFormTemplate.copyTemplate.already")));
		} else {
			long newId = UniqueIdUtil.genId();
			template.setTemplateId(newId);
			template.setTemplateName(name);
			template.setAlias(newAlias);
			template.setCanEdit(1);
			template.setHtml(bpmFormTemplate.getHtml());
			template.setMacroTemplateAlias(bpmFormTemplate
					.getMacroTemplateAlias());
			template.setTemplateDesc(bpmFormTemplate.getTemplateDesc());
			template.setTemplateType(bpmFormTemplate.getTemplateType());
			formTemplateService.add(template);
			writeResultMessage(response.getWriter(), new ResultMessage(
					ResultMessage.Success,
					getText("controller.bpmFormTemplate.copyTemplate.success")));
		}
	}
	
	/**
	 * 模板复制View
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "copy" })
	public ModelAndView copy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		return mv;
	}
	
	/**
	 * 批量导入表单模板
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("batImport")
	@Action(description = "批量导入表单模板", detail = "批量导入表单模板",exectype = SysAuditExecType.IMPORT_TYPE)
	public void batImport(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception
	{
		  MultipartFile file = request.getFile("xmlFile");
		  ResultMessage msg = new ResultMessage();
		  formTemplateService.readXml(file,msg);
		  response.getWriter().write(msg.toString());
	}
	
	/**
	 * 导出自定义表单模板XML
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description = "导出自定义表单模板XML",execOrder=ActionExecOrder.AFTER)
	public void exportXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] formTemplateIds = RequestUtil.getLongAryByStr(request, "formTemplateIds");
		if (BeanUtils.isEmpty(formTemplateIds))
			return;
		try{
			String fileName = DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
			if(formTemplateIds.length==1){
				FormTemplate formTemplate = formTemplateService.getById(formTemplateIds[0]);
				fileName = formTemplate.getTemplateName() + "_" + fileName +".xml";
			}else{
				fileName = "多条模板记录_"+fileName +".xml";
			}
			String strXml = this.formTemplateService.exportXml(formTemplateIds);
			FileUtil.downLoad(request, response, strXml, fileName);
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	/**
	 * 导入自定义表单模板XML
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "importXml" })
	@Action(description = "导入自定义表单模板")
	public void importXml(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			this.formTemplateService.importXml(fileLoad.getInputStream());
			message = new ResultMessage(1, MsgUtil.getMessage());
		} catch (Exception e){
			e.printStackTrace();
			message = new ResultMessage(0, "导入文件异常，请检查文件格式！");
		}
		writeResultMessage(response.getWriter(), message);
	}

}
