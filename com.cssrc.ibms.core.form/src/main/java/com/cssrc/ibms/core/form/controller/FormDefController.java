package com.cssrc.ibms.core.form.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.model.TeamModel;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.form.service.FormDefService;
import com.cssrc.ibms.core.form.service.FormFieldService;
import com.cssrc.ibms.core.form.service.FormHandlerService;
import com.cssrc.ibms.core.form.service.FormRightsService;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.form.service.FormTemplateService;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.form.util.ParseReult;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;

import freemarker.template.TemplateException;

/**
 * 对象功能:自定义表单 控制器类  
 * 开发人员:zhulongchao  
 */
@Controller
@RequestMapping("/oa/form/formDef/")
@Action(ownermodel=SysAuditModelType.FORM_MANAGEMENT)
public class FormDefController extends BaseController {

	@Resource
	private FormDefService service;
	@Resource
	private FormTableService formTableService;
	@Resource
	private FormFieldService formFieldService;
	@Resource
	private FormTemplateService formTemplateService;
	@Resource
	private FormRightsService formRightsService;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	private IBpmService bpmService;
	@Resource
	private FormHandlerService formHandlerService;
	@Resource
	private IGlobalTypeService globalTypeService;
	/*@Resource
	private SysLanguageService sysLanguageService;*/
	@Resource
	private DataTemplateService dataTemplateService;
	@Resource
	INodeSetService nodeSetService;

	@RequestMapping("manage")
	@Action(description = "自定义表单管理页面")
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();

		return mv;
	}

	/**
	 * 取得自定义表单分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 *            请求页数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看自定义表单分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", defaultValue = "1") int page)
			throws Exception {
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		QueryFilter filter = new QueryFilter(request, "bpmFormDefItem");
		List<FormDef> list = service.getAll(filter);
		Map<Long, Integer> publishedCounts = new HashMap<Long, Integer>();
		Map<Long, Integer> dataTemplateCounts = new HashMap<Long, Integer>();
		Map<Long, FormDef> defaultVersions = new HashMap<Long, FormDef>();
		for (int i = 0; i < list.size(); i++) {
			FormDef formDef = list.get(i);
			Long formKey=formDef.getFormKey();
			Long formId=formDef.getFormDefId();
			
			Integer publishedCount = service.getCountByFormKey(formKey);
			Integer dataFormCount=dataTemplateService.getCountByFormKey(formKey);
			
			dataTemplateCounts.put(formId, dataFormCount);
			publishedCounts.put(formId, publishedCount);
			FormDef defaultVersion = service.getDefaultVersionByFormKey(formKey);
			if (defaultVersion != null) {
				defaultVersions.put(formId, defaultVersion);
			}
		}
		ModelAndView mv = this.getAutoView().addObject("bpmFormDefList", list)
				.addObject("publishedCounts", publishedCounts)
				.addObject("defaultVersions", defaultVersions)
				.addObject("dataTemplateCounts", dataTemplateCounts)
				.addObject("categoryId", categoryId);
		return mv;
	}

	@RequestMapping("newVersion")
	@Action(description = "新建表单版本",
			detail="新建表单版本【${SysAuditLinkService.getFormDefLink(Long.valueof(formDefId))}】"
	)
	public void newVersion(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String preUrl = RequestUtil.addReturnUrl(request.getHeader("Referer"),request);
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		ResultMessage msg;
		try {
			service.newVersion(formDefId);
			msg = new ResultMessage(ResultMessage.Success, getText("controller.formDef.newVersion.success"));
		} catch (Exception ex) {
			msg = new ResultMessage(ResultMessage.Fail, getText("controller.formDef.newVersion.success"));
		}
		addMessage(msg, request);
		response.sendRedirect(preUrl);
	}
	
	@ResponseBody
	@RequestMapping("getTableInfo")
	@Action(description = "获取对应自定义表信息")
	public List<FormTable> getTableInfo(HttpServletRequest request,
			HttpServletResponse response){
		Long formDefId=RequestUtil.getLong(request, "formDefId", 0L);
		if(formDefId==0L)return null;
		List<FormTable> bpmFormTableList=new ArrayList<FormTable>();
		try {
			FormDef bpmFormDef=service.getById(formDefId);
			Long tableId=bpmFormDef.getTableId();
			FormTable bpmFormTable=formTableService.getById(tableId);
			bpmFormTableList.add(bpmFormTable);
			List<FormTable>subTableList=formTableService.getSubTableByMainTableId(tableId);
			if(BeanUtils.isNotEmpty(subTableList)){
				bpmFormTableList.addAll(subTableList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bpmFormTableList;
		
	}

	/**
	 * 收集信息，为添加表单做准备
	 * 
	 * @param request
	 * @param response
	 * @param page
	 *            请求页数
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("gatherInfo")
	@Action(description = "收集信息")
	public ModelAndView gatherInfo(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "page", defaultValue = "1") int page)
			throws Exception {
		long categoryId = RequestUtil.getLong(request, "categoryId");
		String subject = RequestUtil.getString(request, "subject");
		String formAlias = RequestUtil.getString(request, "formAlias");
		String formDesc = RequestUtil.getString(request, "formDesc");
		int designType = RequestUtil.getInt(request, "designType",0);
		
		return this.getAutoView()
				.addObject("categoryId", categoryId)
				.addObject("subject",subject)
				.addObject("formAlias", formAlias)
				.addObject("formDesc",formDesc)
				.addObject("designType",designType);
	}
	/**
	 * 通过表单设计的表单标题自动生成表单别名
	 * 
	 * @param request
	 * @return formAlias
	 * @throws Exception
	 */
	@RequestMapping("getTableAlias")
	public void getTableAlias(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subject = RequestUtil.getString(request, "subject");
		String msg = "";
		String pingyin = PinyinUtil.getPinYinHeadCharFilter(subject);
		msg = pingyin;
		List<FormDef> formDefList = service.getAll();
		for(FormDef formDef:formDefList){
			if(pingyin!=null && pingyin.equals(formDef.getFormAlias())){
				msg = "表单别名已存在";
				writeResultMessage(response.getWriter(), msg,
						ResultMessage.Fail);
				return;
			}
		}
		writeResultMessage(response.getWriter(), msg, ResultMessage.Success);
	}
	/**
	 * 选择模板
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selectTemplate")
	@Action(description = "选择模板")
	public ModelAndView selectTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String subject = RequestUtil.getString(request, "subject");
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		String formAlias = RequestUtil.getString(request, "formAlias");
		String formDesc = RequestUtil.getString(request, "formDesc");
		//表单的主表id
		Long tableId = RequestUtil.getLong(request, "tableId");
		int isSimple = RequestUtil.getInt(request, "isSimple", 0);
		//templatesId???没看懂这个参数的含义
		String templatesId = RequestUtil.getString(request, "templatesId");
		//节点类型(主表，子表sub，关系表rel,父亲表)
		String nodeType = RequestUtil.getString(request, "nodeType");
		//字段节点类型的判断(子表字段sub，关系表字段rel)
		String fieldNodeType = RequestUtil.getString(request, "fieldNodeType");
        //获取模型视图
		ModelAndView mv = this.getAutoView();
		//表单的主表信息
		FormTable table = formTableService.getById(tableId);
		//如果是关系rel表或者是rel表中的字段，则提供关系rel表模板。
		if(nodeType.equals("rel")||fieldNodeType.equals("rel")){
			 List<FormTable> relTables = new ArrayList<FormTable>();
			relTables.add(table);
			List<FormTemplate> relTableTemplates = formTemplateService
					.getAllRelTableTemplate();
			mv.addObject("relTables", relTables).addObject("relTableTemplates",
					relTableTemplates);
        } else if ("parent".equals(nodeType)){
            // 如果是父亲表
            List<FormTemplate> mainTableTemplates = formTemplateService.getAllMainTableTemplate();
            mv.addObject("mainTable", table).addObject("mainTableTemplates", mainTableTemplates);
        } else if (table.getIsMain() == 1) {
		    //如果是主表表单或者是主表中的字段，则提供主表模板和sub表模板。（注：rel表也是主表）
			List<FormTable> subTables = formTableService
					.getSubTableByMainTableId(tableId);
			List<FormTemplate> mainTableTemplates = formTemplateService
					.getAllMainTableTemplate();
			List<FormTemplate> subTableTemplates = formTemplateService
					.getAllSubTableTemplate();
			mv.addObject("mainTable", table).addObject("subTables", subTables)
					.addObject("mainTableTemplates", mainTableTemplates)
					.addObject("subTableTemplates", subTableTemplates);
		} else {
		//否则，提供sub表模板。
			List<FormTable> subTables = new ArrayList<FormTable>();
			subTables.add(table);
			List<FormTemplate> subTableTemplates = formTemplateService
					.getAllSubTableTemplate();
			mv.addObject("subTables", subTables).addObject("subTableTemplates",
					subTableTemplates);
		}
		
		mv.addObject("subject", subject).addObject("categoryId", categoryId)
				.addObject("tableId", tableId).addObject("formDesc", formDesc)
				.addObject("isSimple", isSimple).addObject("formAlias", formAlias)
				.addObject("templatesId", templatesId).addObject("nodeType",nodeType);

		return mv;
	}

	/**
	 * 加载编辑器设计模式的模板列表
	 */
	@RequestMapping("chooseDesignTemplate")
	@Action(description = "选择编辑器设计模板")
	public ModelAndView chooseDesignTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = this.getAutoView();
		String subject = RequestUtil.getString(request, "subject");
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		String formDesc = RequestUtil.getString(request, "formDesc");
		int isSimple = RequestUtil.getInt(request, "isSimple", 0);

		String templatePath = FormUtil.getDesignTemplatePath();
		String xml = FileOperator.readFile(templatePath + "designtemps.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		String reStr = "[";
		for (Element element : list) {
			
			String alias = element.attributeValue("alias");
			String name=getText("controller."+alias+".name");
			String templateDesc = getText("controller."+alias+".templateDesc");
			//String name = element.attributeValue("name");
			//String templateDesc = element.attributeValue("templateDesc");
			if (!reStr.equals("["))
				reStr += ",";
			reStr += "{name:'" + name + "',alias:'" + alias
					+ "',templateDesc:'" + templateDesc + "'}";
		}
		reStr += "]";
		mv.addObject("subject", subject).addObject("categoryId", categoryId)
				.addObject("formDesc", formDesc).addObject("temps", reStr)
				.addObject("isSimple", isSimple);
		return mv;
	}

	/**
	 * 编辑器设计表单
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("designEdit")
	@Action(description = "编辑器设计表单")
	public ModelAndView designEdit(HttpServletRequest request) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId", 0);
		int isBack = RequestUtil.getInt(request, "isBack", 0);

		boolean isPublish = false;

		ModelAndView mv = getAutoView();
		FormDef bpmFormDef = null;
		if (formDefId == 0) {
			bpmFormDef = new FormDef();
			bpmFormDef
					.setCategoryId(RequestUtil.getLong(request, "categoryId"));
			bpmFormDef.setFormDesc(RequestUtil.getString(request, "formDesc"));
			bpmFormDef.setSubject(RequestUtil.getString(request, "subject"));
			bpmFormDef.setDesignType(FormDef.DesignType_CustomDesign);
			String tempalias = RequestUtil.getString(request, "tempalias");
			String templatePath = FormUtil.getDesignTemplatePath();
			String html = FileOperator
					.readFile(templatePath + tempalias + ".html");
			String reult=this.filedInternation(html);
			bpmFormDef.setHtml(reult);

			mv.addObject("canEditColumnNameAndType", true);
		} else {
			boolean canEditColumnNameAndType = true;
			bpmFormDef = service.getById(formDefId);
			Long tableId = bpmFormDef.getTableId();

			if (tableId > 0) {
				canEditColumnNameAndType = formTableService
						.getCanEditColumnNameTypeByTableId(bpmFormDef
								.getTableId());
				FormTable bpmFormTable = formTableService
						.getById(tableId);
				mv.addObject("bpmFormTable", bpmFormTable);
				isPublish = true;
			}

			mv.addObject("canEditColumnNameAndType", canEditColumnNameAndType);
		}
		// 回退到编辑页面。
		if (isBack > 0) {
			Long categoryId = RequestUtil.getLong(request, "categoryId");
			String subject = RequestUtil.getString(request, "subject");
			String formDesc = RequestUtil.getString(request, "formDesc");
			String title = RequestUtil.getString(request, "tabTitle");
			String html = request.getParameter("html");

			bpmFormDef.setCategoryId(categoryId);
			bpmFormDef.setFormDesc(formDesc);
			bpmFormDef.setSubject(subject);
			bpmFormDef.setDesignType(FormDef.DesignType_CustomDesign);
			bpmFormDef.setHtml(html);
			bpmFormDef.setTabTitle(title);
		}
		String languages = "";//sysLanguageService.getAllLanguages();
		String locale = ContextUtil.getLocale().toString();
		mv.addObject("bpmFormDef", bpmFormDef)
		  .addObject("isPublish", isPublish)
		  .addObject("languages", languages)
		  .addObject("locale", locale);
		return mv;
	}


	/**
	 * 流程表单授权
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("rightsDialog")
	@Action(description = "流程表单授权")
	public ModelAndView rightsDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		boolean isNodeRights = false;

		ModelAndView mv = this.getAutoView();
		// 是否针对流程节点授权。
		if (StringUtil.isNotEmpty(nodeId) ) {
			Map<String, String> nodeMap = bpmService.getExecuteNodesMap(
					actDefId, true);
			mv.addObject("nodeMap", nodeMap);
			mv.addObject("nodeId", nodeId);
		//	mv.addObject("actDefId", actDefId);
			isNodeRights = true;
		}
		mv.addObject("formKey", formKey);
		mv.addObject("actDefId", actDefId);
		mv.addObject("isNodeRights", isNodeRights);
		return mv;
	}

	/**
	 * 根据表Id和模版Id获取所有的控件定义。
	 * 
	 * @param request
	 * @param response
	 * @param templateId
	 *            表单模板Id
	 * @param tableId
	 *            自定义表Id
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
/*	@ResponseBody
	@RequestMapping("getControls")
	@Action(description = "获取表单控件")
	public Map<String, String> getMacroTemplate(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "templateAlias") Long templateAlias,
			@RequestParam(value = "tableId") Long tableId)
			throws TemplateException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		FormTemplate template = bpmFormTemplateService.getById(templateAlias);
		if (template != null) {
			template = bpmFormTemplateService.getByTemplateAlias(template
					.getMacroTemplateAlias());
			String macro = template.getHtml();
			FormTable table = bpmFormTableService.getById(tableId);
			List<FormField> fields = bpmFormFieldService
					.getByTableId(tableId);
			for (FormField field : fields) {
				String fieldname = field.getFieldName();
				// 字段命名规则
				// 表类型(m:主表，s:子表) +":" + 表名 +“：” + 字段名称
				field.setFieldName((table.getIsMain() == 1 ? "m:" : "s:")
						+ table.getTableName() + ":" + field.getFieldName());
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("field", field);
				map.put(fieldname,
						freemarkEngine.parseByStringTemplate(data, macro
								+ "<@input field=field/>"));
			}
		}
		return map;
	}*/

	/**
	 * 删除自定义表单。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("delByFormKey")
	@Action(description = "删除自定义表单",
			execOrder=ActionExecOrder.BEFORE,
			detail="自定义表单:" +
					"<#list StringUtils.split(formKey,\",\") as item>" +
					"<#assign entity=bpmFormDefService.getById(Long.valueOf(item))/>" +
					"【${entity.subject}】" +
					"<#if bpmFormDefService.getFlowUsed(Long.valueOf(item))<=0 >删除成功" +
					"<#else>不能被删除" +
					"</#if>"+
					"</#list>" 
	)
	public void delByFormKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] aryFormKey = RequestUtil.getLongAryByStr(request, "formKey");
		ResultMessage msg = null;
		List<String> subject=new ArrayList<String>();
		List<String> actDef=new ArrayList<String>();
		boolean error = false ;
		int rtn = 0;
		for(Long formKey:aryFormKey){
			rtn = service.getFlowUsed(formKey);
			if (rtn > 0) {
				error = true ;
				List<Object> list = service.getFormRelatedDef(formKey);
				if(BeanUtils.isEmpty(list) || list.size()!=2) continue ;
				if(BeanUtils.isEmpty(subject)){
					//多个表单被关联，只提示被关联的表单名列表
					actDef = (List<String>) list.get(1);
				}
				subject.add((String)list.get(0));
			} else {
				try {
					service.delByFormKey(formKey);
				} catch (Exception e) {
					msg = new ResultMessage(ResultMessage.Fail, "删除失败");
				}
			}
		}
		if (error) {
			if(subject.size()>1){
				//多个表单被关联，只提示被关联的表单名列表
				String result = "" ;
				for(String str:subject){
					result += "<br>"+str;
				}
				msg = new ResultMessage(ResultMessage.Fail, "删除关联表单"+result);
			}else if(subject.size()==1){
				String result = "" ;
				for(int i=0;i<actDef.size();i++){
					result += "<br>"+(i+1)+"、"+actDef.get(i);
				}
				msg = new ResultMessage(ResultMessage.Fail, getText("controller.bpmFormDef.delByFormKey.onlyYelation",subject.get(0))+result);
			}
		} else if(BeanUtils.isEmpty(msg)){
			msg = new ResultMessage(ResultMessage.Success,  "删除成功");
		}
		writeResultMessage(response.getWriter(), msg);
	}
	/**
	 * 删除自定义表单。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("delByFormDefId")
	@Action(description = "删除某一版本的自定义表单")
	public void delByFormDefId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		ResultMessage msg = new ResultMessage(ResultMessage.Success,  "删除成功");
		List<String> subject=new ArrayList<String>();
		List<String> actDef=new ArrayList<String>();
		boolean error = false ;
		
		try {
				service.delByFormDefId(formDefId);
			} catch (Exception e) {
				msg = new ResultMessage(ResultMessage.Fail, "删除失败");
			}
		writeResultMessage(response.getWriter(), msg);
	}
	/**
	 * 编辑自定义表单。
	 * 
	 * @param request
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "编辑自定义表单")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv = getAutoView();
		//获取表单定义ID
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String returnUrl = RequestUtil.getPrePage(request);
		FormDef bpmFormDef = null;
		if (formDefId != 0) {
			//修改表单
			bpmFormDef = service.getById(formDefId);
		} else {
			//新建表单
			bpmFormDef = new FormDef();
			// 对应tableId
			bpmFormDef.setTableId(RequestUtil.getLong(request, "tableId"));
			//表单分类
			bpmFormDef.setCategoryId(RequestUtil.getLong(request, "categoryId"));
			//// 描述
			bpmFormDef.setFormDesc(RequestUtil.getString(request, "formDesc"));
			//// 表单别名
			bpmFormDef.setFormAlias(RequestUtil.getString(request, "formAlias"));
			//// 表单标题
			bpmFormDef.setSubject(RequestUtil.getString(request, "subject"));
			//节点类型(主表，子表sub，关系表rel)
			String nodeType = RequestUtil.getString(request, "nodeType");
			//表单模板ids 如 [10000005600000, 10000005600005]
			Long[] templateTableId = RequestUtil.getLongAryByStr(request,"templateTableId");
			//表单模板别名s 如[fourColumn, blockTemplate3]
			String[] templateAlias = RequestUtil.getStringAryByStr(request,"templateAlias");
			//如 10000005600000,fourColumn;10000005600005,blockTemplate3
			String templatesId = getTemplateId(templateTableId, templateAlias);
			//根据选择的主表模板id，sub表模板id，rel模板id 生成html
			String reult = this.genTemplate(templateTableId, templateAlias,nodeType);
			bpmFormDef.setHtml(reult);
			bpmFormDef.setTemplatesId(templatesId);
		}
		String locale = ContextUtil.getLocale().toString();
		mv.addObject("bpmFormDef", bpmFormDef)
		  .addObject("returnUrl", returnUrl)
		  .addObject("locale", locale);

		return mv;
	}

	/**
	 * 获得模板与表的对应关系
	 * 
	 * @param templateTablesId
	 * @param templatesId
	 * @return
	 */
	@SuppressWarnings("unused")
	private String getTemplateId(Long[] templateTablesId, Long[] templatesId) {
		StringBuffer sb = new StringBuffer();
		if (BeanUtils.isEmpty(templateTablesId)
				|| BeanUtils.isEmpty(templatesId))
			return sb.toString();
		for (int i = 0; i < templateTablesId.length; i++) {
			for (int j = 0; j < templatesId.length; j++) {
				if (i == j) {
					sb.append(templateTablesId[i]).append(",")
							.append(templatesId[j]);
					break;
				}
			}
			sb.append(";");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 取得自定义表单明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看自定义表单明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "formDefId");
		long canReturn = RequestUtil.getLong(request, "canReturn",0);
		String preUrl = RequestUtil.getPrePage(request);
		FormDef bpmFormDef = service.getById(id);
		return getAutoView().addObject("bpmFormDef", bpmFormDef)
				.addObject("returnUrl", preUrl)
				.addObject("canReturn",canReturn)
				.addObject("locale",ContextUtil.getLocale().toString());
	}

	/**
	 * 取得表fields, 如果是主表，同时取所有子表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getAllFieldsByTableId")
	public String getAllFieldsByTableId(HttpServletRequest request,HttpServletResponse response)throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId",0);
		Long formDefId = RequestUtil.getLong(request, "formDefId",0);
		if(tableId.longValue()==0){
			if(formDefId.longValue()>0){
				FormDef bpmFormDef = service.getById(formDefId);
				tableId = bpmFormDef.getTableId();
			}
		}
		if(tableId.longValue()==0){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		FormTable mainTable = formTableService.getById(tableId);
		List<FormField> mainTableFields = formFieldService.getByTableId(tableId);
		//移除已设置为隐藏的字段数据信息 Liubo
		for(FormField formField:mainTableFields){
			if(!formField.getOptions().isEmpty()){
				String options = formField.getOptions();
				JSONArray optionsObject = (JSONArray) JSONArray.fromObject(options);
				for(int i=0;i<optionsObject.size();i++){
					Map optionObject = (Map) optionsObject.get(i);
					Object statusObject = optionObject.get("status");
					if(statusObject!=null){
						String status = statusObject.toString();
						if(status.equals("0")){
							optionsObject.remove(i);
							formField.setOptions(optionsObject.toString());
						}
					}
					
				}
			}
		}
		sb.append("{mainname:\"");
		// 表没有填写描述时，取表名
		sb.append(StringUtil.isEmpty(mainTable.getTableDesc()) ? mainTable.getTableName() : mainTable.getTableDesc());
		sb.append("\",mainid:");
		sb.append(tableId);
		sb.append(",tablename:\"");
		sb.append(mainTable.getTableName());
		sb.append("\",mainfields:");
		JSONArray jArray = (JSONArray) JSONArray.fromObject(mainTableFields);
		String s = jArray.toString();
		sb.append(s);
		sb.append(",subtables:[");

		List<FormTable> subTables = formTableService.getSubTableByMainTableId(tableId);
		for (int i = 0; i < subTables.size(); i++) {
			FormTable subTable = subTables.get(i);
			Long subTableId = subTable.getTableId();
			List<FormField> subFields = formFieldService.getByTableId(subTableId);
			if (i > 0)
				sb.append(",");
			sb.append("{name:\"");
			sb.append("[sub表]"+(StringUtil.isEmpty(subTable.getTableDesc()) ? subTable.getTableName() : subTable.getTableDesc()));
			sb.append("\",id:");
			sb.append(subTableId);
			sb.append(",tablename:\"");
			sb.append(subTable.getTableName());
			sb.append("\",subfields:");
			JSONArray subArray = (JSONArray) JSONArray.fromObject(subFields);
			sb.append(subArray.toString());
			sb.append("}");
		}
		sb.append("]");
		//父亲表
	    sb.append(",parenttables:[");
	    List<FormTable> parentTables = formTableService.getParentTableByTableId(tableId);
        for (int i = 0; i < parentTables.size(); i++) {
            FormTable parentTable = parentTables.get(i);
            Long parentTableId = parentTable.getTableId();
            List<FormField> paerntFields = formFieldService.getByTableId(parentTableId);
            if (i > 0)
                sb.append(",");
            sb.append("{name:\"");
            sb.append("[parent表]"+(StringUtil.isEmpty(parentTable.getTableDesc()) ? parentTable.getTableName() : parentTable.getTableDesc()));
            sb.append("\",id:");
            sb.append(parentTableId);
            sb.append(",tablename:\"");
            sb.append(parentTable.getTableName());
            sb.append("\",parentfields:");
            JSONArray relArray = (JSONArray) JSONArray.fromObject(paerntFields);
            sb.append(relArray.toString());
            sb.append("}");
        }
        sb.append("]");
		//关系表
		sb.append(",reltables:[");
		//获取mainTable被其他表引用的所有外键列。
		List<FormTable> relTables = formTableService.getRelTableByMainTableId(tableId);
		for (int i = 0; i < relTables.size(); i++) {
			FormTable relTable = relTables.get(i);
			Long relTableId = relTable.getTableId();
			List<FormField> relFields = formFieldService.getByTableId(relTableId);
			if (i > 0)
				sb.append(",");
			sb.append("{name:\"");
			sb.append("[rel表]"+(StringUtil.isEmpty(relTable.getTableDesc()) ? relTable.getTableName() : relTable.getTableDesc()));
			sb.append("\",id:");
			sb.append(relTableId);
			sb.append(",tablename:\"");
			sb.append(relTable.getTableName());
			sb.append("\",relfields:");
			JSONArray relArray = (JSONArray) JSONArray.fromObject(relFields);
			sb.append(relArray.toString());
			sb.append("}");
		}
		sb.append("]}");
		//logger.error(sb.toString());
		return sb.toString();
	}

	/**
	 * 根据表和表单定义ID获取表单权限信息。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getPermissionByTableFormKey")
	public Map<String, List<JSONObject>> getPermissionByTableFormKey(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long tableId = RequestUtil.getLong(request, "tableId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		// Map<String, List<JSONObject>> permission =
		// bpmFormRightsService.getPermissionByTableFormKey(tableId, formKey);
		Map<String, List<JSONObject>> permission = null;
		if (formKey > 0) {
			permission = formRightsService.getPermission(formKey, null, null);
		} else {
			// 还没有生成表单，没有定义表单权限。
			// 则根据表定义获取表单的权限。
			// 这里还缺少意见表单权限。
			//加不加附件权限？？？？(目前加上)--by Yang Bo TOODO
			permission = formRightsService.getPermissionByTableId(tableId);
		}
		return permission;
	}
	
	
	/**
	 * 根据表和表单定义ID获取表单权限信息。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getPermissionByActDefId")
	public Map<String, List<JSONObject>> getPermissionByActDefId(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long formKey = RequestUtil.getLong(request, "formKey");
		String actDefId = RequestUtil.getString(request, "actDefId");
		Map<String, List<JSONObject>> permission = formRightsService.getPermission(formKey, actDefId, null);
		return permission;
	}

	
	/**
	 * 根据流程定义，任务节点，表单定义id获取权限信息。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getPermissionByFormNode")
	public Map<String, List<JSONObject>> getPermissionByFormNode(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		Map<String, List<JSONObject>> permission = formRightsService.getPermissionByFormNode(actDefId, nodeId, formKey);
		return permission;
	}

	/**
	 * 保存表单权限。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("savePermission")
	@Action(description="保存表单权限",
			detail="保存表单【${SysAuditLinkService.getFormDefLink(formKey)}】的表单权限"
	)
	public void savePermission(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String permission = request.getParameter("permission");
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		String parentActDefId = RequestUtil.getString(request, "parentActDefId", "");
		JSONObject jsonObject = JSONObject.fromObject(permission);
		ResultMessage resultMessage = null;
		try {
			if (StringUtil.isNotEmpty(nodeId)) {
				// 设置节点权限。
				formRightsService.save(actDefId, nodeId, formKey, jsonObject,parentActDefId);
			}else if(StringUtil.isNotEmpty(actDefId)){
				// 设置流程全局权限。
				formRightsService.save(actDefId, null, formKey, jsonObject,parentActDefId);
			}else {
				// 根据表单key保存权限。
				formRightsService.save(formKey, jsonObject);
			}

			resultMessage = new ResultMessage(ResultMessage.Success,
					getText("controller.save.success"));
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,
						getText("controller.save.fail")+":" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}

	/**
	 * 根据模板产生html。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("genByTemplate")
	public void genByTemplate(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] templateTableId = RequestUtil.getLongAryByStr(request,
				"templateTableId");
		String[] templateAlias = RequestUtil.getStringAryByStr(request, "templateAlias");
		String nodeType = RequestUtil.getString(request, "nodeType");
		PrintWriter out = response.getWriter();
		String html = genTemplate(templateTableId, templateAlias,nodeType);
		out.println(html);
	}

	/**
	 * 根据模板别名返回 编辑器设计模板
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getHtmlByAlias")
	public void getHtmlByAlias(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tempalias = RequestUtil.getString(request, "tempalias");
		PrintWriter out = response.getWriter();
		String templatePath = FormUtil.getDesignTemplatePath();
		String html = FileOperator.readFile(templatePath + tempalias + ".html");
		String reult=this.filedInternation(html);
		out.println(reult);
	}

	/**
	 * 根据表和指定的html生成表单。
	 * 
	 * @param tableIds
	 * @param tableTemplateIds
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	/*private String genTemplate(Long[] tableIds, Long[] tableTemplateIds)
			throws TemplateException, IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tableIds.length; i++) {
			// 表
			Map<String, Object> fieldsMap = new HashMap<String, Object>();
			FormTable table = formTableService.getById(tableIds[i]);
			List<FormField> fields = formFieldService
					.getByTableId(tableIds[i]);
			fieldsMap.put("table", table);
			fieldsMap.put("fields", fields);
			for (FormField field : fields) {
				field.setFieldName((table.getIsMain() == 1 ? "m:" : "s:")
						+ table.getTableName() + ":" + field.getFieldName());
			}
			// 模板
			FormTemplate tableTemplate = formTemplateService
					.getById(tableTemplateIds[i]);
			FormTemplate macroTemplate = formTemplateService
					.getByTemplateAlias(tableTemplate.getMacroTemplateAlias());
			String macroHtml = "";
			if (macroTemplate != null) {
				macroHtml = macroTemplate.getHtml();
			}
			String result = freemarkEngine.parseByStringTemplate(fieldsMap,
					macroHtml + tableTemplate.getHtml());
			if (table.getIsMain() == 1) {
				sb.append(result);
			} else {
				sb.append("<div type=\"subtable\" right=\"w\" tableName=\"");
				sb.append(table.getTableName());
				sb.append("\">\n");
				sb.append(result);
				sb.append("</div>\n");
			}
		}
		return sb.toString();
	}*/

	/**
	 * 发布表单
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("publish")
	@Action(description = "发布表单",detail="发布表单【${SysAuditLinkService.getFormDefLink(Long.valueOf(formDefId))}】")
	public void publish(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String preUrl = RequestUtil.addReturnUrl(request.getHeader("Referer"),request);
		ResultMessage resultObj = null;
		try {
			service.publish(formDefId, ((ISysUser)UserContextUtil.getCurrentUser())
					.getFullname());
			resultObj = new ResultMessage(ResultMessage.Success, "发布成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultObj = new ResultMessage(ResultMessage.Fail, e.getCause()
					.toString());
		}
		addMessage(resultObj, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 查看版本
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("versions")
	@Action(description = "查看版本")
	public ModelAndView versions(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView result = getAutoView();
        String returnUrl=RequestUtil.getPrePage(request);
		String formKey = request.getParameter("formKey");

		// 版本信息
		List<FormDef> versions = service.getByFormKey(Long
				.parseLong(formKey));
		result.addObject("versions", versions).addObject("formName",
		versions.get(0).getSubject()).addObject("returnUrl", returnUrl);
		return result;
	}

	/**
	 * 设置默认版本
	 * 
	 * @param request
	 * @param response
	 * @param formDefId
	 * @param formDefId
	 * @throws Exception
	 */
	@RequestMapping("setDefaultVersion")
	@Action(description = "设置默认版本" , detail="设置表单【${SysAuditLinkService.getFormDefLink(formDefId)}： ${formDefId}】为默认版本")
	public void setDefaultVersion(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam("formDefId") Long formDefId,
			@RequestParam("formKey") Long formKey) throws Exception {
		ResultMessage resultObj = new ResultMessage(ResultMessage.Success,
				getText("controller.bpmFormDef.setDefaultVersion"));		
		String preUrl = RequestUtil.addReturnUrl(request.getHeader("Referer"),request);
		service.setDefaultVersion(formDefId, formKey);
		addMessage(resultObj, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 选择器
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("selector")
	@Action(description = "选择器")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, "bpmFormDefItem");
		List<FormDef> list = service.getPublished(queryFilter);
		ModelAndView mv = this.getAutoView().addObject("bpmFormDefList", list);
		return mv;
	}

	/**
	 * 根据表单定义id获取是否可以删除。
	 * 
	 * @param formDefId
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getFlowUsed")
	public void getFlowUsed(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		long formDefId = RequestUtil.getLong(request, "formDefId");
		int rtn = service.getFlowUsed(formDefId);
		out.println(rtn);
	}

	/**
	 * 设计表。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "designTable", method = RequestMethod.POST)
	public ModelAndView designTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String content = request.getParameter("content");
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		Long categoryId = RequestUtil.getLong(request, "categoryId");

		String subject = RequestUtil.getString(request, "subject");
		String formDesc = RequestUtil.getString(request, "formDesc");
		String tabTitle = RequestUtil.getString(request, "tabTitle");

		ParseReult result = FormUtil.parseHtmlNoTable(content, "", "");

		String tableName = RequestUtil.getString(request, "tableName");
		String tableComment = RequestUtil.getString(request, "tableComment");

		boolean canEditTableName = true;

		if (formDefId > 0) {
			FormDef bpmFormDef = service.getById(formDefId);
			Long tableId = bpmFormDef.getTableId();
			if (tableId > 0) {
				canEditTableName = formTableService
						.getCanEditColumnNameTypeByTableId(tableId);
			}
		}

		ModelAndView mv = this.getAutoView();
		mv.addObject("result", result).addObject("content", content)
				.addObject("formDefId", formDefId)
				.addObject("subject", subject)
				.addObject("categoryId", categoryId)
				.addObject("formDesc", formDesc)
				.addObject("tabTitle", tabTitle)
				.addObject("tableName", tableName)
				.addObject("tableComment", tableComment)
				.addObject("canEditTableName", canEditTableName);
		return mv;
	}

	/**
	 * 对表单的html进行验证，验证html是否合法。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "validDesign", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> validDesign(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		Long formDefId = RequestUtil.getLong(request, "formDefId");
		String html = request.getParameter("html");
		String tableName = RequestUtil.getString(request, "tableName");
		String tableComment = RequestUtil.getString(request, "tableComment");
		Boolean incHide = RequestUtil.getBoolean(request, "incHide", false);
		// boolean isTableExist=false;
		Long tableId = 0L;
		if (formDefId > 0) {
			FormDef bpmFormDef = service.getById(formDefId);
			tableId = bpmFormDef.getTableId();
		}
		// 输入了主表名。
		// 验证主表名称。
		if (StringUtil.isNotEmpty(tableName)) {
			if (tableId > 0) {
				FormTable bpmFormTable = formTableService
						.getById(tableId);
				// 输入的表名和原来的表名不一致的情况。
				if (!tableName.equalsIgnoreCase(bpmFormTable.getTableName())) {
					boolean isTableExist = formTableService
							.isTableNameExisted(tableName);
					if (isTableExist) {
						map.put("valid", false);
						map.put("errorMsg", getText("controller.bpmFormDef.validDesign",tableName));
						return map;
					}
				}
			} else {
				boolean isTableExist = formTableService
						.isTableNameExisted(tableName);
				if (isTableExist) {
					map.put("valid", false);
					map.put("errorMsg", getText("controller.bpmFormDef.validDesign",tableName));
					return map;
				}
			}
		}

		ParseReult result = FormUtil.parseHtmlNoTable(html, tableName,
				tableComment);

		FormTable bpmFormTable = result.getFormTable();
		// 验证子表。
		String strValid = validSubTable(bpmFormTable, tableId);

		if (StringUtil.isNotEmpty(strValid)) {
			map.put("valid", false);
			map.put("errorMsg", strValid);
			return map;
		}
		// 验证表单是否有错。
		boolean rtn = result.hasErrors();
		if (rtn) {
			map.put("valid", false);
			map.put("errorMsg", result.getError());
		} else {
			map.put("valid", true);
			map.put("table", result.getFormTable());
			if(incHide){//需要添加隐藏的控件ID
				addHiddenFiled(bpmFormTable);
				List<FormTable> subList = bpmFormTable.getSubTableList();
				for(FormTable sub:subList){
					addHiddenFiled(sub);
				}
				map.put("table", bpmFormTable);
			}
		}
		return map;

	}
	/**
	 * 根据传入的bpmFormTable，复制并添加控件对应的隐藏ID字段信息到传入的bpmFormTable中
	 * @param bpmFormTable
	 */
	private void addHiddenFiled(FormTable bpmFormTable){
		List<FormField> list = bpmFormTable.getFieldList();
		FormField hiddenField = null ;
		int listSize = list.size() ;
		for(int i=0;i<listSize;i++){
			FormField field = list.get(i);
			if(FormTableService.isExecutorSelector(field.getControlType())){
				hiddenField = (FormField) field.clone();
				hiddenField.setIsHidden(FormField.HIDDEN);
				hiddenField.setFieldName(field.getFieldName()+FormField.FIELD_HIDDEN);
				hiddenField.setFieldDesc(field.getFieldDesc()+FormField.FIELD_HIDDEN);
				bpmFormTable.addField(hiddenField);
			}
		}
	}

	/**
	 * 验证子表系统中是否存在。
	 * 
	 * <pre>
	 * 	对表单的子表循环。
	 * 		1.表单还没有生成子表的情。
	 * 			验证子表表名系统中是否存在。
	 * 		2.已经生成子表。
	 * 			判断当前子表是否在原子表的列表中，如果不存在，则表示该子表为新添加的表，需要验证子表系统中是否已经存在。
	 * 
	 * </pre>
	 * 
	 * @param bpmFormTable
	 * @param tableId
	 * @return
	 */
	private String validSubTable(FormTable bpmFormTable, Long tableId) {

		List<FormTable> subTableList = bpmFormTable.getSubTableList();
		if (BeanUtils.isEmpty(subTableList))
			return "";
		String str = "";
		for (FormTable subTable : subTableList) {
			String tableName = subTable.getTableName().toLowerCase();
			// 还没有生成表的情况。
			if (tableId == 0) {
				boolean isTableExist = formTableService
						.isTableNameExisted(tableName);
				if (isTableExist) {
					str += getText("controller.bpmFormDef.validSubTable",tableName)+"<br/>";
				}
			}
			// 表已经生成的情况。
			else {
				FormTable orginTable = formTableService
						.getTableById(tableId);
				List<FormTable> orginSubTableList = orginTable
						.getSubTableList();
				Map<String, FormTable> mapSubTable = new HashMap<String, FormTable>();
				for (FormTable table : orginSubTableList) {
					mapSubTable.put(table.getTableName().toLowerCase(), table);
				}
				// 原子表中不存在该表，表示该表为新添加的表。
				if (!mapSubTable.containsKey(tableName)) {
					boolean isTableExist = formTableService
							.isTableNameExisted(tableName);
					if (isTableExist) {
						str += getText("controller.bpmFormDef.validSubTable",tableName)+"<br/>";
					}
				}
			}
		}
		return str;
	}

	/**
	 * 设计时对表单进行预览。
	 * 
	 * <pre>
	 * 	步骤：
	 * 	1.获取设计的html。
	 *  2.对设计的html解析。
	 *  3.生成相应的freemaker模版。
	 *  4.解析html模版输入实际的html。
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("preview")
	@Action(description = "编辑器设计表单预览")
	public ModelAndView preview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId", 0);

		String html = "";
		String name = "";
		String comment = "";
		String title = "";
		Long tableId = 0L;
		// 在列表页面点击预览
		Map<String, Map<String, String>> permission = null;
		if (formDefId > 0) {
			FormDef bpmFormDef = service.getById(formDefId);
			html = bpmFormDef.getHtml();
			name = bpmFormDef.getSubject();
			comment = bpmFormDef.getFormDesc();
			title = bpmFormDef.getTabTitle();
			permission = formRightsService.getByFormKeyAndUserId(bpmFormDef.getFormKey(), UserContextUtil.getCurrentUserId(),"", "");
			
			tableId = bpmFormDef.getTableId();
			if(tableId.longValue()>0){
				FormTable mainTable = formTableService.getById(tableId);
				if(mainTable!=null){
					name = mainTable.getTableName();
				}
			}
		}
		// 在编辑页面预览
		else {
			html = request.getParameter("html");
			name = RequestUtil.getString(request, "name");
			title = RequestUtil.getString(request, "title");
			comment = RequestUtil.getString(request, "comment");
		}
		ParseReult result = FormUtil.parseHtmlNoTable(html, name, comment);
		FormTable bpmFormTable=result.getFormTable();
		bpmFormTable.setTableId(tableId);
		String outHtml = formHandlerService.obtainHtml(title, result, permission,true);
		ModelAndView mv = this.getAutoView();
		mv.addObject("html", outHtml);
		return mv;
	}

	/**
	 * 保存表单。
	 * 
	 * <pre>
	 * 	1.新建表单的情况，不发布。
	 * 		1.添加表单定义。
	 * 		2.添加表定义。
	 * 	2.新建表单发，发布。
	 * 		1.添加表单定义。
	 * 		2.添加表定义。
	 *  3.编辑表单，未发布的情况。
	 *  	1.编辑表单定义。
	 *  	2.删除表定义重新添加。
	 *  4.编辑表单，已经发布。
	 *  	1.编辑表单定义
	 *  	2.是否有多个版本，或者已经有数据。
	 *  		1.是只对表进行编辑。
	 *  		2.否则删除表重建。
	 * 
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "saveForm", method = RequestMethod.POST)
	@Action(description="保存表单",detail="保存表单【${SysAuditLinkService.getFormDefLink(Long.valueOf(formDefId))}】")
	public void saveForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId");
		Long categoryId = RequestUtil.getLong(request, "categoryId");
		String subject = RequestUtil.getString(request, "subject");
		String formDesc = RequestUtil.getString(request, "formDesc");
		String headHtml = RequestUtil.getString(request, "headHtml");
		String tableName = RequestUtil.getString(request, "tableName");
		String tableComment = RequestUtil.getString(request, "tableComment");
		String title = RequestUtil.getString(request, "tabTitle");
		String html = request.getParameter("html");
		int publish = RequestUtil.getInt(request, "publish", 0);
		String json = request.getParameter("json");
		
		html = html.replace("？", "");
		html = html.replace("<p>﻿</p>", "");
		
        if(StringUtil.isNotEmpty(json)){
        	//根据json更新html
        	html=FormUtil.changeHtml(html,json,tableName);
        }
		ParseReult result = FormUtil.parseHtmlNoTable(html, tableName, tableComment);

		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
		String userName = sysUser.getFullname();
		FormDef bpmFormDef = null;
		if (formDefId == 0) {
			bpmFormDef = new FormDef();
			bpmFormDef.setCategoryId(categoryId);
		} else {
			bpmFormDef = service.getById(formDefId);
		}

		ResultMessage resultMessage = null;
		String message = null;
		Long tableId = bpmFormDef.getTableId();
		boolean isTableExist = false;
		// 输入了主表名。
		// 验证主表名称。
		if (StringUtil.isNotEmpty(tableName)) {
			if (tableId > 0) {
				FormTable bpmFormTable = formTableService
						.getById(tableId);
				// 输入的表名和原来的表名不一致的情况。
				if (!tableName.equalsIgnoreCase(bpmFormTable.getTableName())) {
					isTableExist = formTableService
							.isTableNameExisted(tableName);
				}
			} else {
				isTableExist = formTableService
						.isTableNameExisted(tableName);
			}
		}
		if (isTableExist) {
			resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.bpmFormDef.saveForm",tableName));
			response.getWriter().print(resultMessage);
			return;
		}

		bpmFormDef.setSubject(subject);
		bpmFormDef.setFormDesc(formDesc);
		bpmFormDef.setHeadHtml(headHtml);
		bpmFormDef.setTabTitle(title);
		bpmFormDef.setHtml(html);
		bpmFormDef.setTemplate(result.getTemplate());

		bpmFormDef.setPublishedBy(userName);

		message = (publish == 1) ?  getText("controller.formDef.saveForm.publish") :getText("controller.formDef.saveForm.success");

		resultMessage = new ResultMessage(ResultMessage.Success, message);
		try {
			// 是否发布
			boolean isPublish = (publish == 1);
			// 通过解析后得到的表对象。
			FormTable bpmFormTable = result.getFormTable();
			service.saveForm(bpmFormDef, bpmFormTable, isPublish);
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.formDef.saveForm.fail")+":"
						+ str);
			} else {
				message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		response.getWriter().print(resultMessage);
	}


	/**
	 * 复制表单（克隆表单）
	 * 
	 * <pre>
	 * 1、获取要复制的表单对象；
	 * 2、重新生成表单ID（formDefId）、表单key（formKey）；
	 * 3、打开复制表单的设置页面，让用户设置重新设置『表单标题』、『表单分类』、『表单描述』；
	 * 4、保存以后，复制的表单和原表单使用同样的表，表可以添加字段和修改字段（修改的字段不能修改字段名和类型）。
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("copy")
	@Action(description = "复制表单")
	public ModelAndView copy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = RequestUtil.getLong(request, "formDefId", 0);
		FormDef bpmFormDef = null;

		if (formDefId > 0) {
			bpmFormDef = service.getById(formDefId);
		}
		Long cateId = bpmFormDef.getCategoryId();
		if (cateId != null && cateId != 0) {
			IGlobalType globalType = globalTypeService.getById(bpmFormDef
					.getCategoryId());
			bpmFormDef.setCategoryName(globalType.getTypeName());
		}
		ModelAndView mv = this.getAutoView();
		mv.addObject("bpmFormDef", bpmFormDef);
		return mv;
	}

	/**
	 * 保存克隆
	 * 
	 * @param request
	 * @param response
	 * @param po
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@RequestMapping("saveCopy")
	@Action(description = "保存克隆",detail="保存表单【${SysAuditLinkService.getFormDefLink(Long.valueOf(formDefId))}】的克隆")
	public void saveCopy(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		long formDefId = RequestUtil.getLong(request, "formDefId");
		String formName = RequestUtil.getString(request, "formName");
		Long typeId = RequestUtil.getLong(request, "typeId");
		String formDesc = RequestUtil.getString(request, "formDesc");

		FormDef bpmFormDef = service.getById(formDefId);
		long oldFormKey = bpmFormDef.getFormKey();
		if (bpmFormDef != null) {
			if (!StringUtil.isEmpty(formName)) {
				bpmFormDef.setSubject(formName);
			}
			if (typeId > 0) {
				bpmFormDef.setCategoryId(typeId);
			}
			if (!StringUtil.isEmpty(formDesc)) {
				bpmFormDef.setFormDesc(formDesc);
			}
			long id = UniqueIdUtil.genId();
			bpmFormDef.setFormDefId(id);
			bpmFormDef.setFormKey(id);
			bpmFormDef.setIsPublished((short) 0);
			bpmFormDef.setIsDefault((short) 1);
			bpmFormDef.setVersionNo(1);
			try {
				service.copyForm(bpmFormDef, oldFormKey);
				writeResultMessage(writer, getText("controller.bpmFormDef.saveCopy.success"), ResultMessage.Success);
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					ResultMessage resultMessage = new ResultMessage(
							ResultMessage.Fail, getText("controller.bpmFormDef.saveCopy.fail")+":" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					ResultMessage resultMessage = new ResultMessage(
							ResultMessage.Fail, message);
					response.getWriter().print(resultMessage);
				}
			}
		} else {
			writeResultMessage(writer, getText("controller.bpmFormDef.saveCopy.getFormFail"), ResultMessage.Fail);
			return;
		}
	}

	/**
	 * 导入表单源文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importForm")
	@Action(description = "导入表单源文件")
	public void importForm(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("importInput");
		String str = FileOperator.inputStream2String(fileLoad.getInputStream());
		response.getWriter().print(str);
	}

	/**
	 * 导出表单源文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportForm")
	@Action(description = "导出表单")
	public void exportForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String title = RequestUtil.getString(request, "title");
		String html = request.getParameter("html");
		if (StringUtil.isNotEmpty(title)) {
			html += "#content-title#" + title;
		}
		String subject = request.getParameter("subject") + ".html";
		response.setContentType("APPLICATION/OCTET-STREAM");
		String filedisplay = StringUtil.encodingString(subject, "GBK",
				"ISO-8859-1");
		response.addHeader("Content-Disposition", "attachment;filename="
				+ filedisplay);
		response.getWriter().write(html);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	 * 初始化表单权限设置
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("initRights")
	@Action(description="初始化表单权限设置",detail="初始化表单【${SysAuditLinkService.getFormDefLink(formkey)}】权限设置")
	public ResultMessage initRights(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResultMessage resultMessage = null;
		try {
			Long formKey = RequestUtil.getLong(request, "formKey", 0);
			String actDefId = RequestUtil.getString(request, "actDefId");
			String nodeId = RequestUtil.getString(request, "nodeId");
			formRightsService.deleteRight(formKey,actDefId,nodeId);
			resultMessage = new ResultMessage(ResultMessage.Success,getText("controller.save.success"));
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.save.fail")+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
		return resultMessage;
	}

	/**
	 * 判断是否有子表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("isSubTable")
	public String isSubTable(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		StringBuffer sb = new StringBuffer();
		Long formKey = RequestUtil.getLong(request, "formKey", 0);
		FormDef bpmFormDef = service.getById(formKey);
		if (BeanUtils.isNotEmpty(bpmFormDef)
				&& BeanUtils.isNotEmpty(bpmFormDef.getTableId())) {
			List<FormTable> list = formTableService
					.getSubTableByMainTableId(bpmFormDef.getTableId());
			if (BeanUtils.isNotEmpty(list)) {
				sb.append("{success:true,tableId:")
						.append(bpmFormDef.getTableId()).append("}");
			} else {
				sb.append("{success:false,msg:'"+getText("controller.bpmFormDef.isSubTable.notSet")+"'}");
			}
		} else {
			sb.append("{success:false,msg:'"+getText("controller.bpmFormDef.isSubTable.notForm")+"'}");
		}

		return sb.toString();
	}

	/**
	 * 流程表单子表授权
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("subRightsDialog")
	@Action(description = "流程表单子表授权")
	public ModelAndView subRightsDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long formKey = RequestUtil.getLong(request, "formKey");
		Long tableId = RequestUtil.getLong(request, "tableId");

		ModelAndView mv = this.getAutoView();
		mv.addObject("actDefId", actDefId);
		mv.addObject("nodeId", nodeId);
		mv.addObject("formKey", formKey);
		mv.addObject("tableId", tableId);
		return mv;
	}

	/**
	 * 导出xml窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("export")
	@Action(description = " 导出xml窗口")
	public ModelAndView export(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String formDefIds = RequestUtil.getString(request, "formDefIds");

		ModelAndView mv = this.getAutoView();
		mv.addObject("formDefIds", formDefIds);
		return mv;
	}

	/**
	 * 导出自定义表单XML
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("exportXml")
	@Action(description = "导出自定义表单XML",
			execOrder=ActionExecOrder.AFTER,
			detail="导出自定义表单:" +
					"<#list StringUtils.split(formDefIds,\",\") as item>" +
					"<#assign entity=bpmFormDefService.getById(Long.valueOf(item))/>" +
					"【${entity.subject}】" +
					"</#list>")
	public void exportXml(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long[] formDefIds = RequestUtil.getLongAryByStr(request, "formDefIds");

		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("bpmFormDef", true);
		map.put("bpmFormTable", RequestUtil.getBoolean(request, "bpmFormTable"));
		map.put("bpmFormDefOther",
				RequestUtil.getBoolean(request, "bpmFormDefOther"));
		map.put("bpmFormRights",
				RequestUtil.getBoolean(request, "bpmFormRights"));
		map.put("bpmTableTemplate",
				RequestUtil.getBoolean(request, "bpmTableTemplate"));
		map.put("sysBusEvent", 
				Boolean.valueOf(RequestUtil.getBoolean(request, "sysBusEvent")));
		map.put("formDefTree", 
				Boolean.valueOf(RequestUtil.getBoolean(request, "formDefTree")));

		if (BeanUtils.isEmpty(formDefIds))
			return;
		try {	
			String fileName=DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
			if(formDefIds.length==1){
				FormDef formDef = service.getById(formDefIds[0]);
				fileName = formDef.getSubject()+"_"+fileName;
			}
			else fileName= "多条表单记录_"+fileName;
			
			String strXml = service.exportXml(formDefIds, map);
			//logger.info(strXml);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.encodingString(fileName, "GBK", "ISO-8859-1")+ ".xml");
			response.getWriter().write(strXml);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	/**
	 * 导入自定义表单的XML。
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importXml")
	@Action(description = "导入自定义表单")
	public void importXml(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			service.importXml(fileLoad.getInputStream());
			message = new ResultMessage(ResultMessage.Success,
					MsgUtil.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(ResultMessage.Fail,
					 getText("controller.importXml.fail"));
		}
		writeResultMessage(response.getWriter(), message);
	}
	
	@RequestMapping("setCategory")
	@Action(description = "设置分类" , 
			detail="设置表单" +
					"<#list StringUtils.split(formKeys,\",\") as item>" +
					" 【${SysAuditLinkService.getFormDefLink(item)}】 "+
					"</#list>" +
					"的分类为" +
					"${SysAuditLinkService.getGlobalTypeLink(Long.valueOf(categoryId))}")
	public void setCategory(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		Long categoryId = RequestUtil.getLong(request, "categoryId", 0);
		String formKeys = RequestUtil.getString(request, "formKeys");
		String[] aryFormKey = formKeys.split(",");

		if (categoryId == 0L) {
			// writer
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,"没有选择分类"));
			return;
		}
		
		if (StringUtil.isEmpty(formKeys)) {
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,"没有选择表单"));
			return;
		}
		
		List<Long> list = new ArrayList<Long>();
		
		for (String formKey : aryFormKey) {
			list.add(Long.parseLong(formKey));
		}
		try {
			service.updCategory(categoryId, list);
			writeResultMessage(writer, new ResultMessage(ResultMessage.Success,"设置分类成功"));
		} catch (Exception ex) {
			String msg = ExceptionUtil.getExceptionMessage(ex);
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,
					msg));
		}
		
	}
	/**
	 * 获得模板与表的对应关系
	 * 
	 * @param templateTablesId
	 * @param templateAlias
	 * @return
	 */
	private String getTemplateId(Long[] templateTablesId, String[] templateAlias) {
		StringBuffer sb = new StringBuffer();
		if (BeanUtils.isEmpty(templateTablesId)
				|| BeanUtils.isEmpty(templateAlias))
			return sb.toString();
		for (int i = 0; i < templateTablesId.length; i++) {
			for (int j = 0; j < templateAlias.length; j++) {
				if (i == j) {
					sb.append(templateTablesId[i]).append(",")
							.append(templateAlias[j]);
					break;
				}
			}
			sb.append(";");
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	
    /**
     * 移除关联关系的现实字段，关联关系显示字段已经在控件中存在了
     * 
     * @param table
     * @param fields
     */
    private void removeRelField(FormTable table, List<FormField> fields)
    {
        List<String> relation = new ArrayList<String>();
        for (FormField f : fields)
        {
            String rel = f.getRelFormDialogStripCData();
            if (StringUtil.isNotEmpty(rel))
            {
                relation.add(rel);
            }
        }
        for (Iterator<FormField> it = fields.iterator(); it.hasNext();)
        {
            FormField field = it.next();
            if(field.getControlType()==IFieldPool.RELATION_COLUMN_CONTROL){
                continue;
            }
            for (String rel : relation)
            {
                JSONObject _rel = JSONObject.fromObject(rel);
                JSONArray relfs = _rel.getJSONArray("fields");
                for (Object relf : relfs)
                {
                    JSONObject _f = (JSONObject)relf;
                    try{
                    	boolean isShow=_f.getBoolean(IFieldPool.FK_LISTSHOW);
                        if (isShow&&_f.getString(IFieldPool.FK_TABLEFIELD).equals(field.getFieldName()))
                        {
                            it.remove();
                            break;
                        }
                    }catch(Exception e){
                    	e.printStackTrace();
                    	break;
                    }
                    
                }
                break;
            }
        }
    }
	/**
	 * 根据表和指定的html生成表单。
	 * 
	 * @param tableIds
	 * @param templateAlias
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	private String genTemplate(Long[] tableIds, String[] templateAlias,String nodeType)
			throws TemplateException, IOException {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < tableIds.length; i++) {
			// 表
			Map<String, Object> fieldsMap = new HashMap<String, Object>();
			FormTable table = formTableService.getById(tableIds[i]);
			List<FormField> fields = formFieldService.getByTableId(tableIds[i]);
			//删除主键外键字段。
			removeField(table,fields);
			//删除关联表外键字段。
			removeRelField(table,fields);
			//移除流程状态字段
			removeProcessStateField(table,fields);
			fieldsMap.put("table", table);
			fieldsMap.put("fields", fields);
			// 设置主表和子表分组
			this.setTeamFields(fieldsMap, table, fields,nodeType);
			// 设置 字段名称   【关系表字段前缀（r），主表字段前缀（m），子表字段前缀（s）】:【表名】:【字段名】
			this.setFormFieldName(table, fields,nodeType);
			// 根据模版别名取得模版
			FormTemplate tableTemplate = formTemplateService.getByTemplateAlias(templateAlias[i]);
			// 根据宏模板别名取得模版
			FormTemplate macroTemplate = formTemplateService.getByTemplateAlias(tableTemplate.getMacroTemplateAlias());
			//logger.info(macroTemplate.getHtml());
			String macroHtml = "";
			if (macroTemplate != null) {
				macroHtml = macroTemplate.getHtml();
			}
			//a标签国际化
			/*FormLanguage formLanguage = FormLanguage.getFormLanguage();
			fieldsMap.put("formLanguage", formLanguage);*/
			// 根据"参数"和"freemarker字符串模版"解析出内容
			String result = freemarkEngine.parseByStringTemplate(fieldsMap,
					macroHtml + tableTemplate.getHtml());
			//System.out.print(macroHtml);
			//System.out.print(tableTemplate.getHtml());
			if(StringUtils.isNotEmpty(nodeType) && nodeType.equals("rel")) {
				sb.append("<p><br/></p>");
				sb.append("<div type=\"reltable\" tableName=\"");
				sb.append(table.getTableName());
				sb.append("\">\n");
				sb.append(result);
				sb.append("</div>\n");
			}else if("parent".equals(nodeType)){
                sb.append("<p><br/></p>");
                sb.append("<div type=\"parenttable\" tableName=\"");
                sb.append(table.getTableName());
                sb.append("\">\n");
                sb.append(result);
                sb.append("</div>\n");
			}else if (table.getIsMain() == 1) {
				sb.append(result);
			}else {
				sb.append("<p><br/></p>");
				sb.append("<div type=\"subtable\" tableName=\"");
				sb.append(table.getTableName());
				sb.append("\">\n");
				sb.append(result);
				sb.append("</div>\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * 删除外部表的主键外键字段。
	 * @param table
	 * @param fields
	 */
	private void removeField(FormTable table ,List<FormField> fields){
		//判断是否为外部表
		if(!table.isExtTable()) return ;
		String pk=table.getPkField();
		String fk=table.getRelation();
		for(Iterator<FormField> it=fields.iterator();it.hasNext();){
			FormField field=it.next();
			//字段可能同时为主键和外键，因此只需删除一次即可
			if(field.getFieldName().equalsIgnoreCase(fk)){
				it.remove();
			}else if(field.getFieldName().equalsIgnoreCase(pk)){
				it.remove();
			}
		}
	}
	/**
	 * 移除流程状态字段
	 * @param table
	 * @param fields
	 */
	private void removeProcessStateField(FormTable table ,List<FormField> fields){
		//判断是否为外部表
		List<FormField> remList = new ArrayList();
		for(Iterator<FormField> it=fields.iterator();it.hasNext();){
			FormField field=it.next();
			if(field.getControlType().equals(IFieldPool.FLOW_STATE)){
				remList.add(field);
			}
		}
		fields.removeAll(remList);
	}
	/**
	 * 设置分组字段
	 * 
	 * @param fieldsMap
	 * @param table
	 * @param fields
	 * @param fieldList
	 * @return
	 */
	private List<TeamModel> setTeamFields(Map<String, Object> fieldsMap,
			FormTable table, List<FormField> fields,String nodeType) {
		//判断表是否有分组
		if (StringUtil.isEmpty(table.getTeam()))
			return null;
		List<TeamModel> list = new ArrayList<TeamModel>();
		JSONObject json = JSONObject.fromObject(table.getTeam());
		fieldsMap.put("isShow", json.get("isShow"));
		fieldsMap.put("showPosition", json.get("showPosition"));

		JSONArray teamJson = JSONArray.fromObject(json.get("team"));
		for (Object obj : teamJson) {
		    try{
		        TeamModel teamModel = new TeamModel();
	            JSONObject jsonObj = (JSONObject) obj;
	            String teamName = jsonObj.get("teamName").toString();
	            String teamNameKey = jsonObj.get("teamNameKey").toString();
	            teamModel.setTeamName(teamName);
	            teamModel.setTeamNameKey(teamNameKey);
	            // 获取字段
	            JSONArray jArray = (JSONArray) jsonObj.get("teamField");
	            List<FormField> teamFields = new ArrayList<FormField>();
	            for (Object object : jArray) {
	                JSONObject fieldObj = (JSONObject) object;
	                String fieldName = (String) fieldObj.get("fieldName");
	                FormField bpmFormField = this
	                        .getTeamField(fields, fieldName);
	                if (BeanUtils.isNotEmpty(bpmFormField)) {
	                    fields.remove(bpmFormField);
	                    teamFields.add(bpmFormField);
	                }
	            }
	            this.setFormFieldName(table, teamFields,nodeType);
	            teamModel.setTeamFields(teamFields);
	            list.add(teamModel); 
		    }catch(Exception e){
		        
		    }
			
		}
		fieldsMap.put("teamFields", list);
		return list;
	}
	
	/**
	 * 设置字段名字【关系表字段前缀（r），主表字段前缀（m），子表字段前缀（s）】:【表名】:【字段名】
	 * 
	 * @param table
	 * @param fields
	 */
	private void setFormFieldName(FormTable table,
			List<FormField> fields,String nodeType) {
		for (FormField field : fields) {
		    field.setFormTable(table);
			field.setFieldName(setFormFieldName(table.getIsMain(),nodeType)
					+ table.getTableName()
					+ ":"
					+ field.getFieldName());
		}

	}
	//取得字段前缀 字段前缀：关系表字段前缀（r:），主表字段前缀（m:），子表字段前缀（s:）
	private String setFormFieldName(Short isMain,String nodeType) {
		String extStr = "";
		if(StringUtils.isNotEmpty(nodeType) && nodeType.equals("rel")){
			extStr = "r:";
		} else if("parent".equals(nodeType)){
		    extStr = "p:";
		}else{
			extStr = (isMain.shortValue() == FormTable.IS_MAIN
					.shortValue() )? "m:" : "s:";
		}
		return extStr;
	}
	/**
	 * 获取分组字段
	 * 
	 * @param fields
	 * @param fieldName
	 * @return
	 */
	private FormField getTeamField(List<FormField> fields,
			String fieldName) {
		for (FormField bpmFormField : fields) {
			if (bpmFormField.getFieldName().equals(fieldName))
				return bpmFormField;
		}
		return null;
	}

	/**
	 * 根据表Id和模版Id获取所有的控件定义。
	 * 
	 * @param request
	 * @param response
	 * @param templateAlias
	 *            表单模板别名
	 * @param tableId
	 *            自定义表Id
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("getControls")
	@Action(description = "获取表单控件")
	public Map<String, String> getControls(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "templateAlias") String templateAlias,
			//@RequestParam(value = "fieldNodeType") String fieldNodeType,
			@RequestParam(value = "tableId") Long tableId)
			throws TemplateException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		String fieldNodeType = RequestUtil.getString(request, "fieldNodeType");
		FormTemplate template = formTemplateService
				.getByTemplateAlias(templateAlias);
		if (template != null) {
			template = formTemplateService.getByTemplateAlias(template
					.getMacroTemplateAlias());
			String macro = template.getHtml();
			FormTable table = formTableService.getById(tableId);
			List<FormField> fields = formFieldService
					.getByTableId(tableId);
			for (FormField field : fields) {
				String fieldname = field.getFieldName();
				// 字段命名规则
				// 表类型(m:主表，s:子表) +":" + 表名 +“：” + 字段名称，用tabType表示
				String tabType = "";
				//如果fieldNodeType不为空
				if(StringUtil.isNotEmpty(fieldNodeType)){
					if(fieldNodeType.equals("rel")){
						tabType = "r:";
					}else if(table.getIsMain() == 1){
						tabType = "m:";
					}else{
						tabType = "s:";
					}
				}else{
					if(table.getIsMain() == 1){
						tabType = "m:";
					}else{
						tabType = "s:";
					}
				}
				//对于枚举类型，若是存在status，并且status为0时就隐藏该选项 Liubo
				String options = field.getOptions();
				if(!options.isEmpty()){
					JSONArray optionsObject = (JSONArray) JSONArray.fromObject(options);
					for(int i=0;i<optionsObject.size();i++){
						Map optionObject = (Map) optionsObject.get(i);
						Object statusObject = optionObject.get("status");
						if(statusObject!=null){
							String status = statusObject.toString();
							if(status.equals("0")){
								optionsObject.remove(i);
								field.setOptions(optionsObject.toString());
							}
						}
						
					}
				}
				field.setFieldName(tabType + table.getTableName() + ":" + field.getFieldName());
				field.setFormTable(table);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("field", field);
				map.put(fieldname,
						freemarkEngine.parseByStringTemplate(data, macro
								+ "<@input field=field/>"));
			}
		}
		return map;
	}
	
	@RequestMapping("icon")
	public ModelAndView icon(HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("/extend/icon/icon.jsp");
		return mv;
	}
	
	private String filedInternation(String html) {
		org.jsoup.nodes.Document doc=  Jsoup.parseBodyFragment(html);
		Elements el=new Elements();
		el=doc.select("td.formHead");
		el.html(getText("controller.bpmFormDef.filedInternation.mainTable"));
		el=doc.select("td.formTitle");
		for (org.jsoup.nodes.Element element :el) {
			String filed = element.attr("key");
			element.html(getText("controller.bpmFormDef.filedInternation."+filed));
		}
		return doc.html();
	}
	/**
	 * 意见回填字段窗口
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"fieldDialog"})
	public ModelAndView fieldDialog(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long formKey = RequestUtil.getLong(request, "formKey");
		FormDef bpmFormDef = this.service.getDefaultPublishedByFormKey(formKey);
		Long tableId = bpmFormDef.getTableId();
		List<FormField> fieldList = this.formFieldService.getByTableIdContainHidden(tableId);
		return getAutoView().addObject("bpmFormFieldList", fieldList);
	}
	
    /** 
    * @Title: getFlowNode 
    * @Description: TODO(获取流程节点) 
    * @param @param request
    * @param @param response
    * @param @return
    * @param @throws Exception    设定文件 
    * @return Object    返回类型 
    * @throws 
    */
    @RequestMapping({"getFlowNode"})
    @ResponseBody
    public Object getFlowNode(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long formDefId = RequestUtil.getLong(request, "formDefId");
        FormDef bpmFormDef = this.service.getById(formDefId);
        DataTemplate dataTemplate=this.dataTemplateService.getByFormKey(bpmFormDef.getFormKey());
        Long defId=dataTemplate.getDefId();
        if(defId!=null){
            List<?extends INodeSet> nodeSetList = nodeSetService.getByDefId(defId);
            return nodeSetList;
        }else{
            return null;
        }
    }
    
    /**
     * 选择模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("opinionTemplate")
    @ResponseBody
    public Object opinionTemplate(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<FormTemplate> templates = formTemplateService.getTemplateType(IFormTemplate.GROUPOPINION);
        return templates;
    }   
    
    /**
     * 选择模板
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getTablegroup")
    @ResponseBody
    public Object getTablegroup(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long formDefId = RequestUtil.getLong(request, "formDefId");
        FormDef bpmFormDef = this.service.getById(formDefId);
        FormTable formTable = this.formTableService.getById(bpmFormDef.getTableId());
        if(StringUtil.isNotEmpty(formTable.getTeam())){
            com.alibaba.fastjson.JSONObject team=JSON.parseObject(formTable.getTeam());
            return JSON.parseArray(team.getString("team"));
        }else{
            return null;
        }
    }   
    /**
	 * 表单设计批量导入
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("batImport")
	public void batImport(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile file = request.getFile("xmlFile");
		StringBuffer log = new StringBuffer();
		try {
			service.saveFromXML(file,log);
			FileUtil.downLoad(request, response,log.toString(),"生成日志.txt");
		} catch (Exception e) {
			log.append("数据已进行回滚!!!\r\n"+e.getMessage());
			FileUtil.downLoad(request, response,log.toString(),"生成日志.txt");
		}
	}
}