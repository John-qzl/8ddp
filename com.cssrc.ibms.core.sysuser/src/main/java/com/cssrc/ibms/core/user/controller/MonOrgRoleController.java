package com.cssrc.ibms.core.user.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.MorpherRegistry;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.user.model.MonOrgRole;
import com.cssrc.ibms.core.user.service.MonOrgRoleService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
/**
 * 
 * <p>Title:MonOrgRoleController</p>
 * @author Yangbo 
 * @date 2016-8-11下午02:31:23
 */
@Controller
@RequestMapping( { "/oa/bpm/monOrgRole/" })
public class MonOrgRoleController extends BaseController {

	@Resource
	private MonOrgRoleService monOrgRoleService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新监控组权限分配")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		MonOrgRole monOrgRole = getFormObject(request);
		try {
			if ((monOrgRole.getId() == null)
					|| (monOrgRole.getId().longValue() == 0L)) {
				monOrgRole.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.monOrgRoleService.add(monOrgRole);
				resultMsg = "添加监控组权限分配成功";
			} else {
				this.monOrgRoleService.update(monOrgRole);
				resultMsg = "更新监控组权限分配成功";
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	protected MonOrgRole getFormObject(HttpServletRequest request)
			throws Exception {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));

		String json = RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);

		MonOrgRole monOrgRole = (MonOrgRole) JSONObject.toBean(obj,
				MonOrgRole.class);

		return monOrgRole;
	}

	@RequestMapping( { "list" })
	@Action(description = "查看监控组权限分配分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.monOrgRoleService.getAll(new QueryFilter(request,
				"monOrgRoleItem"));
		ModelAndView mv = getAutoView().addObject("monOrgRoleList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除监控组权限分配")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.monOrgRoleService.delByIds(lAryId);
			message = new ResultMessage(1, "删除监控组权限分配成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑监控组权限分配")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		MonOrgRole monOrgRole = (MonOrgRole) this.monOrgRoleService.getById(id);

		return getAutoView().addObject("monOrgRole", monOrgRole).addObject(
				"returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看监控组权限分配明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		MonOrgRole monOrgRole = (MonOrgRole) this.monOrgRoleService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("monOrgRole", monOrgRole);
	}
}
