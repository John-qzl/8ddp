package com.cssrc.ibms.dp.template.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cssrc.ibms.core.form.dao.DataTemplateDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.resources.product.service.ProductCategoryBatchService;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dp.form.service.FormValidateService;
import com.cssrc.ibms.dp.template.constant.TemplateConstant;
import com.cssrc.ibms.dp.template.model.Template;
import com.cssrc.ibms.dp.template.service.TemplateCheckConditionResultService;
import com.cssrc.ibms.dp.template.service.TemplateCheckConditionService;
import com.cssrc.ibms.dp.template.service.TemplateFolderService;
import com.cssrc.ibms.dp.template.service.TemplateService;
import com.cssrc.ibms.dp.template.service.TemplateSignResultService;
import com.cssrc.ibms.dp.template.service.TemplateSignService;

import net.sf.json.JSONArray;

/**
 * @description 模板管理控制器类
 * @author xie chen
 * @date 2019年12月3日 下午2:22:12
 * @version V1.0
 */
@Controller
@RequestMapping("/template/manage/")
public class TemplateController extends BaseController {

	@Resource
	private TemplateService service;
	@Resource
	private TemplateFolderService templateFolderService;
	@Resource
	private TemplateSignService templateSignService;
	@Resource
	private TemplateSignResultService templateSignResultService;
	@Resource
	private TemplateCheckConditionService templateCheckConditionService;
	@Resource
	private TemplateCheckConditionResultService templateCheckConditionResultService;
	@Resource
	private FormValidateService formValidateService;
	@Resource
	private ProductCategoryBatchService productCategoryBatchService;
	@Resource
	private DataTemplateDao dataTemplateDao;

	/**
	 * @Desc （型号）通用模板管理结构树视图
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("common")
	public ModelAndView common(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 型号id，名称
		String moduleId = RequestUtil.getString(request, "moduleId");
		String moduleName = RequestUtil.getString(request, "moduleName");
		ModelAndView mv = getAutoView();
		mv.addObject("moduleId", moduleId);
		mv.addObject("moduleName", moduleName);
		return mv;
	}

	/**
	 * @Desc 获取（型号）通用模板结构树数据JSON
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getTreeData")
	@ResponseBody
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<String> tree = new ArrayList<String>();
		String moduleId = RequestUtil.getString(request, "moduleId");
		String moduleName = RequestUtil.getString(request, "moduleName");
		if(moduleName.equals("1")) {  //模板复制传过来是1代表全部型号
			moduleName="全部型号";
		}
		// 添加根节点--型号节点
		String rootId = "1";
		String rootNode = "{id:" + rootId + ", parentId:-1" + ", name:\"" + moduleName
				+ "\" , type:\"root\", tempUrl:\"\"" + ", target:\"formFrame\",open:true}";
		tree.add(rootNode);

		// 添加表单模板
		String formUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000000720028&__pk__=";
		List<Map<String, Object>> templates = new ArrayList<>();
		templates = service.getTemplatesByModuleId(moduleId);
		for (Map<String, Object> form : templates) {
			String id = CommonTools.Obj2String(form.get("ID"));
			String name = CommonTools.Obj2String(form.get("F_NAME"));
			String parentId = CommonTools.Obj2String(form.get("F_TEMP_FILE_ID"));
			String status = CommonTools.Obj2String(form.get("F_STATUS"));
			if (StringUtil.isEmpty(parentId)) {
				parentId = rootId;
			}
			String node = "{id:" + id + ", parentId:" + parentId + ", name:\"" + name + "\" , type:\"form\", status:\""
					+ status + "\" , tempUrl:" + (formUrl + id + "\"") + ", target :\"formFrame\",open:true}";
			tree.add(node);
		}
		// 添加文件夹
		List<Map<String, Object>> folders = templateFolderService.getTemplateFolderByModuleId(moduleId);
		String folderUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021480075&__pk__=";
		for (int i = 0; i < folders.size(); i++) {
			Map<String, Object> folder = folders.get(i);
			String id = CommonTools.Obj2String(folder.get("ID"));
			String parentId = CommonTools.Obj2String(folder.get("F_TEMP_FILE_ID"));
			if (StringUtil.isEmpty(parentId)) {
				parentId = rootId;
			}
			String name = CommonTools.Obj2String(folder.get("F_NAME"));
			String node = "{id:" + id + ", parentId:" + parentId + ", name:\"" + name + "\" , type:\"folder\", tempUrl:"
					+ (folderUrl + id + "\"") + ", target :\"formFrame\",open:true}";
			tree.add(node);
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONArray.fromObject(tree).toString());

	}

	/**
	 * @Desc 新增模板-概况页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("templateInfor")
	public ModelAndView templateInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String moduleId = RequestUtil.getString(request, "moduleId");
		String folderId = RequestUtil.getString(request, "folderId");
		ModelAndView mv = getAutoView();
		mv.addObject("moduleId", moduleId);
		mv.addObject("folderId", folderId);
		return mv;
	}

	/**
	 * @Desc 通用模板编号唯一性校验
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("templateCodeCheck")
	@ResponseBody
	public Map<String, Object> templateCodeCheck(HttpServletRequest request, HttpServletResponse response) {
		String templateCode = RequestUtil.getString(request, "id");
		String templateId = RequestUtil.getString(request, "MID");
		return service.templateCodeCheck(templateCode, templateId);
	}

	/**
	 * @Desc 通用模板名是否包含制表符
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("templateNameCheck")
	@ResponseBody
	public Map<String, Object> templateNameCheck(HttpServletRequest request, HttpServletResponse response) {
		String templateName = RequestUtil.getString(request, "templateName");
		Map<String, Object> message = new HashMap<>();
		// 检验 是否包含空格之类的特殊字符
		if (templateName.contains("\t")) {
			message.put("success", "false");
			message.put("msg", "检查表名称中包含制表符");
		} else {
			message.put("success", "true");
			message.put("msg", "检查表名称可以使用");
		}
		return message;
	}
	/**
	 * @Desc 通用模板名是否包含制表符
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("templateTypeCheck")
	@ResponseBody
	public Map<String, Object> templateTypeCheck(HttpServletRequest request, HttpServletResponse response) {
		String templateType = RequestUtil.getString(request, "templateType");
		List<String> list=new ArrayList<>();
		for(int i=1;i<7;i++) {
			list.add(String.valueOf(i));
		}
		Map<String, Object> message = new HashMap<>();
		// 检验 是否包含空格之类的特殊字符
		if (list.contains(templateType)) {
			message.put("success", "true");
			message.put("msg", "检查表种类可以使用");
		} else {
			message.put("success", "false");
			message.put("msg", "不存在此检查表种类");
		}
		return message;
	}
	/**
	 * @Desc 保存通用模板基本信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("templateBaseSave")
	public ModelAndView templateBaseSave(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");// 模板编号
		String templateId = request.getParameter("MID");// 模板主键
		String fcName = request.getParameter("fcName");
		String name = request.getParameter("name");
		String sign[] = request.getParameterValues("sign");
		String status[] = request.getParameterValues("status");
		String attention = request.getParameter("attention");
		String moduleId = RequestUtil.getString(request, "moduleId");// 型号id
		String folderId = RequestUtil.getString(request, "folderId");// 文件夹id
		String type = RequestUtil.getString(request, "type");
		Map<String, Object> message;
		ModelAndView mv = new ModelAndView();
		if (StringUtil.isEmpty(templateId)) {
			// 新增
			message = service.addTemplateBaseInfor(id, name, sign, status, attention, moduleId, folderId, type,
					TemplateConstant.TEMP_UNCOMPLETE);
		} else {
			message = service.updateTemplateBaseInfor(templateId, id, name, sign, status, attention, type);
			Template tabletemp = service.getById(Long.parseLong(templateId));
			String html = tabletemp.getContents();
			if (html != null) {
				int index1 = html.indexOf("<table");
				int index2 = html.lastIndexOf("</table>");
				if (index1 != -1 && index2 != -1) {
					try {
						html = html.substring(index1, index2 + 8);
					} catch (Exception e) {
						html = "";
					}
				}
			}
			mv.addObject("html", html);
		}

		mv.addObject("id", id);// 检查表编号
		mv.addObject("name", name);
		mv.addObject("fcName", fcName);
		mv.addObject("attention", attention);
		mv.addObject("moduleId", moduleId);// 型号id
		mv.addObject("folderId", folderId);// 文件夹id
		mv.addObject("type", type);
		mv.addObject("MID", message.get("ID"));// 模板编号
		mv.addObject("message", message);

		if (message.get("success").equals("true")) {
			mv.addObject("sign", arrayToString(sign));
			mv.addObject("status", arrayToString(status));
			mv.setViewName("/template/templateDesigner.jsp");
		} else {
			mv.addObject("sign", sign);
			mv.addObject("status", status);
			mv.setViewName("/template/manageTemplateInfor.jsp");
		}
		return mv;
	}

	/**
	 * @Desc 字符串数组转字符，以"/"分隔
	 * @param strs
	 * @return
	 */
	private String arrayToString(String[] strs) {
		boolean first = true;
		StringBuffer sb = new StringBuffer("");
		for (String str : strs) {
			if (StringUtil.isNotEmpty(str)) {
				if (first) {
					sb.append(str);
					first = false;
				} else {
					sb.append("/" + str);
				}
			}
		}
		return sb.toString();
	}

	@RequestMapping("delCommonTemplate")
	@ResponseBody
	public Map<String, Object> delCommonTemplate(HttpServletRequest request, HttpServletResponse response) {
		String templateId = RequestUtil.getString(request, "id");
		Map<String, Object> msg = service.delCommonTemplate(templateId);
		return msg;
	}

	@RequestMapping("validateTemplateHtml")
	public ModelAndView validateTemplateHtml(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		String html = RequestUtil.getString(request, "content");
		String type = RequestUtil.getString(request, "type");
		String MID = RequestUtil.getString(request, "MID");// 模板id
		Map<String, Object> msg = formValidateService.validate(html, type, MID);
		mv.addObject("id", RequestUtil.getString(request, "id"));// 检查表编号
		mv.addObject("name", RequestUtil.getString(request, "name"));
		mv.addObject("attention", RequestUtil.getString(request, "attention"));
		mv.addObject("moduleId", RequestUtil.getString(request, "moduleId"));// 型号id
		mv.addObject("folderId", RequestUtil.getString(request, "folderId"));// 文件夹id
		mv.addObject("fcName", RequestUtil.getString(request, "fcName"));
		mv.addObject("type", type);
		mv.addObject("MID", MID);// 模板编号
		mv.addObject("html", msg.get("html"));
		mv.addObject("msg", msg);

		String sign = RequestUtil.getString(request, "sign");
		String status = RequestUtil.getString(request, "status");

		// 校验未通过，直接跳至设计器页，并给出提示信息
		if (msg.get("success").toString().equals("false")) {
			mv.setViewName("/template/templateDesigner.jsp");
			mv.addObject("sign", sign);
			mv.addObject("status", status);
			return mv;
		}

		// 跳至预览页
		mv.setViewName("/template/templatePreview.jsp");
		String _status[] = status.split("/");
		String _signs[] = sign.split("/");
		mv.addObject("signs", _signs);
		mv.addObject("status", _status);

		return mv;
	}

	@RequestMapping("commonSave")
	@ResponseBody
	public Map<String, Object> commonSave(HttpServletRequest request, HttpServletResponse response) {
		String html = RequestUtil.getString(request, "htmlres");
		String templateId = RequestUtil.getString(request, "MID");
		String type = RequestUtil.getString(request, "type");
		int index1 = html.indexOf("<table");
		int index2 = html.lastIndexOf("</table>");
		html = html.substring(index1, index2 + 8);
		System.out.println(html);
		return service.commonSave(html, templateId, type);
	}
	
	/**
     * 跳转到编辑模板页
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("commonEdit")
    public ModelAndView commonEdit(HttpServletRequest request,
                                         HttpServletResponse response) {
        String templateId = RequestUtil.getString(request, "id");
        String fcName = RequestUtil.getString(request, "fcName");

        Map<String, Object> tabletemp = service.getTemplateMapById(templateId);
        List<Map<String, Object>> signs = templateSignService.getSignListMapByTemplateId(templateId);
        List<Map<String, Object>> checkCondition = templateCheckConditionService.getCheckConditionListMapByTemplateId(templateId);

        ModelAndView mv = new ModelAndView("/template/templateInforEdit.jsp");
        mv.addObject("id", templateId);
        mv.addObject("tabletemp", tabletemp);
        mv.addObject("MID", tabletemp.get("ID"));
        mv.addObject("sign", signs);
        mv.addObject("status", checkCondition);
        mv.addObject("fcName", fcName);
        return mv;
    }
    
	/**
	 * @Desc 通用模板选择器页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("commonTemplateCopyView")
	public ModelAndView commonTemplateCopyView(HttpServletRequest request, HttpServletResponse response) {
		String categoryId = request.getParameter("categoryId");
		return new ModelAndView("/template/category/commonTemplateCopy.jsp").addObject("categoryId", categoryId);
	}
	
	/**
	 * @Desc 批量复制通用模板
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("commonMultiCopy")
	@ResponseBody
	public Map<String, Object> commonMultiCopy(HttpServletRequest request, HttpServletResponse response) {
		String templateIds = RequestUtil.getString(request, "templateIds");
		String categoryId = RequestUtil.getString(request, "categoryId");
		return service.commonMultiCopy(categoryId, templateIds.split(","));
	}
	
	/**
	 * @Desc 获取产品类别下所有模板
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getCategoryTemplate")
    @ResponseBody
    public void getCategoryTemplate(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        List<String> tree = new ArrayList<String>();
        String productCategoryId = request.getParameter("productCategoryId");
        Map<String, Object> productBatch = productCategoryBatchService.getById(productCategoryId);
    	String categoryName = CommonTools.Obj2String(productBatch.get("F_CPMC"));
    	
        String rootUrl = "\"\"";
        
        // 添加根节点
        String rootId = "1";
        String rootParentId = "-1";
        String rootNode = "{id:" + rootId + ", parentId:" + rootParentId
                + ", name:\"" + categoryName + "\" , type:\"root\", tempUrl:"
                + rootUrl + ", target:\"formFrame\",open:true}";
        tree.add(rootNode);

        // 添加表单模板
        List<Map<String, Object>> forms = new ArrayList<>();
        forms = service.getTemplatesByCategoryId(productCategoryId);
        for (Map<String, Object> form : forms) {
            String id = CommonTools.Obj2String(form.get("ID"));
            String name = CommonTools.Obj2String(form.get("F_NAME"));
            String parentId = CommonTools.Obj2String(form.get("F_TEMP_FILE_ID"));
            String status = CommonTools.Obj2String(form.get("F_STATUS"));
            if (StringUtil.isEmpty(parentId)) {
                parentId = rootId;
            }
            String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
                    + name + "\" , type:\"form\", status:\"" + status
                    + "\", target :\"formFrame\",open:true}";
            tree.add(node);
        }
        // 添加文件夹
        List<Map<String, Object>> folders = templateFolderService.getTemplateFolderByCategoryId(productCategoryId);
        
        for (int i = 0; i < folders.size(); i++) {
            Map<String, Object> folder = folders.get(i);
            String id = CommonTools.Obj2String(folder.get("ID"));
            String parentId = CommonTools.Obj2String(folder
                    .get("F_TEMP_FILE_ID"));
            if (StringUtil.isEmpty(parentId)) {
                parentId = rootId;
            }
            String name = CommonTools.Obj2String(folder.get("F_NAME"));
            String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
                    + name + "\" , type:\"folder\","
                    + " target :\"formFrame\",open:true}";
            tree.add(node);
        }
        // 利用Json插件将Array转换成Json格式
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSONArray.fromObject(tree).toString());
    }
	
	/**
	 * @Desc 产品类别模板选择器页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("categoryTemplateSelectView")
	public ModelAndView categoryTemplateSelectView(HttpServletRequest request, HttpServletResponse response) {
		String productCategoryId = request.getParameter("productCategoryId");
		return new ModelAndView("/product/batch/categoryTemplateSelector.jsp").addObject("productCategoryId", productCategoryId);
	}

	/**
	 * @Desc 靶场实验任务模板选择器页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("missionTemplateSelectView")
	public ModelAndView missionTemplateSelectView(HttpServletRequest request, HttpServletResponse response) {
		String xhId = request.getParameter("xhId");
		return new ModelAndView("/mission/missionTemplateSelector.jsp").addObject("xhId", xhId);
	}

	/**
	 * @Desc 武器所检任务模板选择器页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("weaponCheckTemplateSelectView")
	public ModelAndView weaponCheckTemplateSelectView(HttpServletRequest request, HttpServletResponse response) {
		String xhId = request.getParameter("xhId");
		return new ModelAndView("/weaponCheck/weaponCheckTemplateSelector.jsp").addObject("xhId", xhId);
	}


	/**
	 * @Desc 获取产品类别下所有模板
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getMissionTemplate")
	@ResponseBody
	public void getMissionTemplate(HttpServletRequest request,
									HttpServletResponse response) throws IOException {
		List<String> tree = new ArrayList<String>();
		String xhId = request.getParameter("xhId");
		String rootFloderName="可选模板";
		String rootUrl = "\"\"";

		// 添加根节点
		String rootId = "1";
		String rootParentId = "-1";
		String rootNode = "{id:" + rootId + ", parentId:" + rootParentId
				+ ", name:\"" + rootFloderName + "\" , type:\"root\", tempUrl:"
				+ rootUrl + ", target:\"formFrame\",open:true}";
		tree.add(rootNode);

		// 添加表单模板
		List<Map<String, Object>> forms = new ArrayList<>();
		forms = service.getTemplatesByCategoryId(xhId);
		for (Map<String, Object> form : forms) {
			String id = CommonTools.Obj2String(form.get("ID"));
			String name = CommonTools.Obj2String(form.get("F_NAME"));
			String parentId = CommonTools.Obj2String(form.get("F_TEMP_FILE_ID"));
			String status = CommonTools.Obj2String(form.get("F_STATUS"));
			if (StringUtil.isEmpty(parentId)) {
				parentId = rootId;
			}
			String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
					+ name + "\" , type:\"form\", status:\"" + status
					+ "\", target :\"formFrame\",open:true}";
			tree.add(node);
		}
		// 添加文件夹
		List<Map<String, Object>> folders = templateFolderService.getTemplateFolderByCategoryId(xhId);

		for (int i = 0; i < folders.size(); i++) {
			Map<String, Object> folder = folders.get(i);
			String id = CommonTools.Obj2String(folder.get("ID"));
			String parentId = CommonTools.Obj2String(folder
					.get("F_TEMP_FILE_ID"));
			if (StringUtil.isEmpty(parentId)) {
				parentId = rootId;
			}
			String name = CommonTools.Obj2String(folder.get("F_NAME"));
			String node = "{id:" + id + ", parentId:" + parentId + ", name:\""
					+ name + "\" , type:\"folder\","
					+ " target :\"formFrame\",open:true}";
			tree.add(node);
		}
		// 利用Json插件将Array转换成Json格式
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(JSONArray.fromObject(tree).toString());
	}
	
	@RequestMapping("categoryTempFormPreview")
    public ModelAndView categoryTempFormPreview(HttpServletRequest request,
                                   HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("/template/category/categoryTempFormPreview.jsp");
        String templateId = request.getParameter("templateId");
        // 获取模板信息map
        Map<String, Object> templateMap = service.getTemplateMapById(templateId);
        // 获取模板签署定义及空实例
        List<Map<String, Object>> signresult = templateSignResultService.getSignDefAndResultByTemplateId(templateId);
        // 获取模板检查条件及空实例
        List<Map<String, Object>> condires = templateCheckConditionResultService.getConditionDefAndResultByTemplateId(templateId);
        //formService.getCheckItemAndMapById(slid)
        List<Map<String, Object>> checkItemandmap = new ArrayList<>();
        String content = templateMap.get("F_CONTENTS").toString();
        int index1 = content.indexOf("<table");
        int index2 = content.lastIndexOf("</table>");
        content = content.substring(index1, index2 + 8);
        content = content.replaceAll(
                "<input class=\"dpInputBtn\" type=\"button\" disabled=\"true\"",
                "<input class=\"dpInputBtn\" type=\"button\"");
        //可能存在未能将按钮的disabled属性正确替换的情况（因为构建表单模板时的一些顺序因素），这里需要重新替换
        content = content.replaceAll("disabled=\"true\""," ");

        mv.addObject("content", content);
        mv.addObject("signresult", signresult);
        mv.addObject("condires", condires);
        mv.addObject("checkitem", checkItemandmap);
        mv.addObject("slid", templateId);
         return mv;
    }

}
