package com.cssrc.ibms.index.controller;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.SysIndexMyLayout;
import com.cssrc.ibms.index.service.SysIndexColumnService;
import com.cssrc.ibms.index.service.SysIndexLayoutService;
import com.cssrc.ibms.index.service.SysIndexMyLayoutService;
@Controller
@RequestMapping( { "/oa/system/sysIndexMyLayout/" })
public class SysIndexMyLayoutController extends BaseController {

	@Resource
	private SysIndexMyLayoutService sysIndexMyLayoutService;

	@Resource
	private SysIndexLayoutService sysIndexLayoutService;

	@Resource
	private SysIndexColumnService sysIndexColumnService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新我的布局")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysIndexMyLayout sysIndexMyLayout) throws Exception {
		String resultMsg = null;
		try {
			if ((sysIndexMyLayout.getId() == null)
					|| (sysIndexMyLayout.getId().longValue() == 0L)) {
				sysIndexMyLayout.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.sysIndexMyLayoutService.add(sysIndexMyLayout);
				resultMsg = getText("添加", new Object[] { "我的布局" });
			} else {
				this.sysIndexMyLayoutService.update(sysIndexMyLayout);
				resultMsg = getText("更新", new Object[] { "我的布局" });
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看我的布局分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysIndexMyLayoutService.getAll(new QueryFilter(
				request, "sysIndexMyLayoutItem"));
		ModelAndView mv = getAutoView().addObject("sysIndexMyLayoutList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除我的布局")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysIndexMyLayoutService.delByIds(lAryId);
			message = new ResultMessage(1, "删除我的布局成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑我的布局")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		SysIndexMyLayout sysIndexMyLayout = (SysIndexMyLayout) this.sysIndexMyLayoutService
				.getById(id);

		return getAutoView().addObject("sysIndexMyLayout", sysIndexMyLayout)
				.addObject("returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看我的布局明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysIndexMyLayout sysIndexMyLayout = (SysIndexMyLayout) this.sysIndexMyLayoutService
				.getById(id);
		return getAutoView().addObject("sysIndexMyLayout", sysIndexMyLayout);
	}

	@RequestMapping( { "design" })
	@Action(description = "设计我的首页布局")
	public ModelAndView design(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();

		List layoutList = this.sysIndexLayoutService.getAll();
		QueryFilter filter = new QueryFilter(request, "sysIndexMyLayout");
		Map params = RequestUtil.getParameterValueMap(request);

		List columnList = this.sysIndexColumnService.getHashRightColumnList(
				filter, params, Boolean.valueOf(true));

		Map columnMap = this.sysIndexColumnService.getColumnMap(columnList);

		SysIndexMyLayout sysIndexMyLayout = this.sysIndexMyLayoutService
				.getLayoutList(userId, columnList);

		return getAutoView().addObject("layoutList", layoutList).addObject(
				"columnMap", columnMap).addObject("sysIndexMyLayout",
				sysIndexMyLayout);
	}

	@RequestMapping( { "saveLayout" })
	@Action(description = "保存首页布局")
	public void saveLayout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String html = RequestUtil.getString(request, "html");
		String designHtml = RequestUtil.getString(request, "designHtml");
		ResultMessage resultObj = null;
		try {
			this.sysIndexMyLayoutService.save(html, designHtml);
			resultObj = new ResultMessage(1, "保存成功");
		} catch (Exception e) {
			resultObj = new ResultMessage(0, e.getMessage());
		}
		response.getWriter().print(resultObj);
	}

	@RequestMapping( { "deleteLayout" })
	@Action(description = "保存首页布局")
	public void deleteLayout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		ResultMessage resultObj = null;
		try {
			this.sysIndexMyLayoutService.delById(id);
			resultObj = new ResultMessage(1, "删除成功");
		} catch (Exception e) {
			resultObj = new ResultMessage(0, e.getMessage());
		}
		response.getWriter().print(resultObj);
	}
}
