package com.cssrc.ibms.core.form.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.form.model.FormDefHi;
import com.cssrc.ibms.core.form.service.FormDefHiService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 * 自定义表单历史记录Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/form/formDefHi/" })
public class FormDefHiController extends BaseController {

	@Resource
	private FormDefHiService formDefHiService;

	@RequestMapping({ "list" })
	@Action(description = "查看分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long formDefId = Long
				.valueOf(RequestUtil.getLong(request, "formDefId"));
		QueryFilter filter = new QueryFilter(request, "formDefHiItem");
		if (formDefId.longValue() == 0L) {
			formDefId = Long.valueOf(-1L);
		}
		filter.addFilterForIB("formDefId", formDefId);
		List<FormDefHi> list = this.formDefHiService.getAll(filter);
		ModelAndView mv = getAutoView().addObject("formDefHiList", list)
				.addObject("formDefId", formDefId);

		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "hisId");
			this.formDefHiService.delByIds(lAryId);
			message = new ResultMessage(1, "删除成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "get" })
	@Action(description = "查看明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long hisId = Long.valueOf(RequestUtil.getLong(request, "hisId"));
		FormDefHi formDefHi = (FormDefHi) this.formDefHiService.getById(hisId);
		return getAutoView().addObject("formDefHi", formDefHi);
	}
	
	/**
	 * 恢复历史记录到formDef
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "getByAjax" })
	@ResponseBody
	public FormDefHi getByAjax(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long hisId = Long.valueOf(RequestUtil.getLong(request, "hisId"));
		FormDefHi formDefHi = (FormDefHi) this.formDefHiService.getById(hisId);
		return formDefHi;
	}
}
