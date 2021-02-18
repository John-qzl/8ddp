package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysOrgTactic;
import com.cssrc.ibms.core.user.service.SysOrgTacticService;
import com.cssrc.ibms.core.user.service.SysOrgTypeService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 组织策略Controller
 * <p>Title:SysOrgTacticController</p>
 * @author Yangbo 
 * @date 2016-8-4下午08:54:38
 */
@Controller
@RequestMapping( { "/oa/system/sysOrgTactic/" })
public class SysOrgTacticController extends BaseController {

	@Resource
	private SysOrgTacticService sysOrgTacticService;

	@Resource
	private SysOrgTypeService sysOrgTypeService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新组织策略")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysOrgTactic sysOrgTactic) throws Exception {
		String resultMsg = null;
		try {
			SysOrgTactic orgTactic = (SysOrgTactic) this.sysOrgTacticService
					.getById(SysOrgTactic.DEFAULT_ID);
			if (BeanUtils.isEmpty(orgTactic))
				this.sysOrgTacticService.add(sysOrgTactic);
			else {
				this.sysOrgTacticService.update(sysOrgTactic);
			}
			resultMsg = getText("保存组织策略成功");
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看组织策略分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysOrgTacticService.getAll(new QueryFilter(request,
				"sysOrgTacticItem"));
		ModelAndView mv = getAutoView().addObject("sysOrgTacticList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除组织策略")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysOrgTacticService.delByIds(lAryId);
			message = new ResultMessage(1, "删除组织策略成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑组织策略")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		List orgTypelist = this.sysOrgTypeService.getByDemId(1L);
		SysOrgTactic sysOrgTactic = (SysOrgTactic) this.sysOrgTacticService
				.getById(SysOrgTactic.DEFAULT_ID);
		if (BeanUtils.isEmpty(sysOrgTactic)) {
			sysOrgTactic = new SysOrgTactic();
			sysOrgTactic.setId(SysOrgTactic.DEFAULT_ID);
			sysOrgTactic.setOrgTactic(Short
					.valueOf((short) SysOrgTactic.ORG_TACTIC_WITHOUT));
		}

		return getAutoView().addObject("sysOrgTactic", sysOrgTactic).addObject(
				"orgTypelist", orgTypelist);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看组织策略明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysOrgTactic sysOrgTactic = (SysOrgTactic) this.sysOrgTacticService
				.getById(id);
		return getAutoView().addObject("sysOrgTactic", sysOrgTactic);
	}

	@RequestMapping( { "selector" })
	@Action(description = "查看组织策略明细")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String orgName = RequestUtil.getString(request, "orgName");
		List sysOrgList = this.sysOrgTacticService
				.getSysOrgListByOrgName(orgName);
		return getAutoView().addObject("sysOrgList", sysOrgList).addObject(
				"isSingle", Boolean.valueOf(true));
	}
}
