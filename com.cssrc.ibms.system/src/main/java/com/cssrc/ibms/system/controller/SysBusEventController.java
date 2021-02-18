package com.cssrc.ibms.system.controller;

import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysBusEvent;
import com.cssrc.ibms.system.service.SysBusEventService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/sysBusEvent/" })
public class SysBusEventController extends BaseController {

	@Resource
	private SysBusEventService sysBusEventService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新 业务保存逻辑")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysBusEvent sysBusEvent) throws Exception {
		String resultMsg = null;
		try {
			if ((sysBusEvent.getId() == null)
					|| (sysBusEvent.getId().longValue() == 0L)) {
				this.sysBusEventService.save(sysBusEvent);
				resultMsg = "添加业务保存逻辑";
			} else {
				this.sysBusEventService.save(sysBusEvent);
				resultMsg = "更新业务保存逻辑";
			}
			resultMsg = resultMsg + "成功!";
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			resultMsg = resultMsg + "失败,";
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看 业务保存逻辑分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysBusEventService.getAll(new QueryFilter(request,
				"sysBusEventItem"));
		ModelAndView mv = getAutoView().addObject("sysBusEventList", list);
		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除 业务保存逻辑")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysBusEventService.delByIds(lAryId);
			message = new ResultMessage(1, "删除 业务保存逻辑成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑 业务保存逻辑")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		String formKey = RequestUtil.getString(request, "formKey");
		String returnUrl = RequestUtil.getPrePage(request);
		SysBusEvent sysBusEvent = this.sysBusEventService.getByFormKey(formKey);
		if (sysBusEvent == null) {
			sysBusEvent = new SysBusEvent();
			sysBusEvent.setFormkey(formKey.toString());
		}
		return getAutoView().addObject("sysBusEvent", sysBusEvent).addObject(
				"returnUrl", returnUrl).addObject("tablePre",
				ITableModel.CUSTOMER_TABLE_PREFIX).addObject("fieldPre",
				ITableModel.CUSTOMER_COLUMN_PREFIX);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看 业务保存逻辑明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysBusEvent sysBusEvent = (SysBusEvent) this.sysBusEventService.getById(id);
		return getAutoView().addObject("sysBusEvent", sysBusEvent);
	}
}