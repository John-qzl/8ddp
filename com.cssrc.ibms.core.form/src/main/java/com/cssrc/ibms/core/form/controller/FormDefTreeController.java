package com.cssrc.ibms.core.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormDefTree;
import com.cssrc.ibms.core.form.service.FormDefTreeService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONObjectUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping( { "/oa/form/formDefTree/" })
public class FormDefTreeController extends BaseController {

	@Resource
	private FormDefTreeService formDefTreeService;

	@Resource
	private IFormTableService formTableService;

	@Resource
	private IFormFieldService formFieldService;

	@Resource
	private IFormDefService formDefService;

	@Resource
	private IDataTemplateService dataTemplateService;

	@RequestMapping("save")
	@Action(description = "添加或更新form_def_tree")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String json = FileOperator.inputStream2String(request.getInputStream());
		FormDefTree formDefTree = (FormDefTree) JSONObjectUtil.toBean(json,
				FormDefTree.class);
		String resultMsg = null;
		try {
			if ((formDefTree.getId() == null)
					|| (formDefTree.getId().longValue() == 0L)) {
				resultMsg = getText("添加成功", new Object[] { "form_def_tree" });
			} else {
				this.formDefTreeService.save(formDefTree);
				resultMsg = getText("更新成功", new Object[] { "form_def_tree" });
			}
			this.formDefTreeService.save(formDefTree);
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			if ((e instanceof DuplicateKeyException))
				resultMsg = "别名已被使用：" + formDefTree.getFormDefId();
			else {
				resultMsg = resultMsg + "," + e.getMessage();
			}
			writeResultMessage(response.getWriter(), resultMsg, 0);
		}
	}

	@RequestMapping("list")
	@Action(description = "查看form_def_tree分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.formDefTreeService.getAll(new QueryFilter(request,
				"formDefTreeItem"));
		ModelAndView mv = getAutoView().addObject("formDefTreeList", list);
		return mv;
	}

	@RequestMapping("del")
	@Action(description = "删除form_def_tree")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.formDefTreeService.delByIds(lAryId);
			message = new ResultMessage(1, "删除form_def_tree成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"show_{formKey}"})
	@Action(description = "删除form_def_tree")
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("formKey") String formKey) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/oa/form/formDefTreeShow.jsp");
		DataTemplate dataTemplate = (DataTemplate) dataTemplateService.getByFormKey(Long.valueOf(formKey)); 
		mv.addObject("formKey", formKey);
		mv.addObject("__displayId__", dataTemplate.getId());
		return mv;
	}

	@RequestMapping("getObject")
	@Action(description = "按各种参数查询对象")
	@ResponseBody
	public Object getObject(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String action = RequestUtil.getString(request, "action");
		Long formKey = RequestUtil.getLong(request, "formKey");

		if ("table".equals(action)) {
			IFormDef formDef = this.formDefService.getDefaultPublishedByFormKey(formKey);
			IFormTable bpmFormTable = this.formTableService.getByTableId(formDef
					.getTableId(), 0);
			Map rtnMap = new HashMap();
			rtnMap.put("formName", formDef.getSubject());
			rtnMap.put("bpmFormTable", bpmFormTable);
			return rtnMap;
		}

		if ("formDefTree".equals(action)) {
			return this.formDefTreeService.getByFormKey(formKey);
		}

		if ("json".equals(action)) {
			String id = RequestUtil.getString(request, "id");
			Map params = new HashMap();
			params.put("id", id);
			return this.formDefTreeService.treeListJson(formKey, params);
		}
		return null;
	}

	@RequestMapping("delData_{alias}")
	@Action(description = "删除一条数据")
	public void delData(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("alias") String alias)
			throws Exception {
		String[] pks = RequestUtil.getStringAryByStr(request, "pk");
		for (String pk : pks)
			this.dataTemplateService.deleteData(Long.valueOf(alias), pk);
	}
}