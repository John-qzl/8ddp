package com.cssrc.ibms.index.controller;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.SysIndexLayout;
import com.cssrc.ibms.index.service.SysIndexLayoutService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/sysIndexLayout/" })
public class SysIndexLayoutController extends BaseController {

	@Resource
	private SysIndexLayoutService sysIndexLayoutService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新首页布局")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysIndexLayout sysIndexLayout) throws Exception {
		String resultMsg = null;
		try {
			if ((sysIndexLayout.getId() == null)
					|| (sysIndexLayout.getId().longValue() == 0L)) {
				sysIndexLayout.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.sysIndexLayoutService.add(sysIndexLayout);
				resultMsg = getText("添加", new Object[] { "首页布局" });
			} else {
				this.sysIndexLayoutService.update(sysIndexLayout);
				resultMsg = getText("更新", new Object[] { "首页布局" });
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看首页布局分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysIndexLayoutService.getAll(new QueryFilter(request,
				"sysIndexLayoutItem"));
		ModelAndView mv = getAutoView().addObject("sysIndexLayoutList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除首页布局")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysIndexLayoutService.delByIds(lAryId);
			message = new ResultMessage(1, "删除首页布局成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑首页布局")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		SysIndexLayout sysIndexLayout = (SysIndexLayout) this.sysIndexLayoutService
				.getById(id);

		return getAutoView().addObject("sysIndexLayout", sysIndexLayout)
				.addObject("returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看首页布局明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysIndexLayout sysIndexLayout = (SysIndexLayout) this.sysIndexLayoutService
				.getById(id);
		return getAutoView().addObject("sysIndexLayout", sysIndexLayout);
	}
}
