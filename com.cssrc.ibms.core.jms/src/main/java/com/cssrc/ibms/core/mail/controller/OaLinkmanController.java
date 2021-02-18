package com.cssrc.ibms.core.mail.controller;
import java.sql.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.mail.model.OaLinkman;
import com.cssrc.ibms.core.mail.model.OutMailLinkman;
import com.cssrc.ibms.core.mail.service.OaLinkmanService;
import com.cssrc.ibms.core.mail.service.OutMailLinkmanService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping({ "/oa/oa/oaLinkman/" })
public class OaLinkmanController extends BaseController {

	@Resource
	private OaLinkmanService oaLinkmanService;
	@Resource
	private ISysUserService sysUserservice;

	@Resource
	private OutMailLinkmanService outMailLinkmanService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更联系人表")
	public void save(HttpServletRequest request, HttpServletResponse response,
			OaLinkman oaLinkman) throws Exception {
		String resultMsg = null;
		Long userId = UserContextUtil.getCurrentUserId();
		try {
			if ((oaLinkman.getId() == null)
					|| (oaLinkman.getId().longValue() == 0L)) {
				this.oaLinkmanService.save(oaLinkman);
				resultMsg = getText("添加", new Object[] { "添加联系人成功" });
			} else {
				this.oaLinkmanService.save(oaLinkman);
				resultMsg = getText("更新", new Object[] { "更新联系人成功" });
			}

			OutMailLinkman man = this.outMailLinkmanService.findLinkMan(
					oaLinkman.getEmail(), userId.longValue());
			if (man != null) {
				man.setLinkName(oaLinkman.getName());
				this.outMailLinkmanService.update(man);
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(),
					resultMsg + "," + e.getMessage(), 0);
		}
	}

	@RequestMapping({ "list" })
	@Action(description = "查看联系人分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = UserContextUtil.getCurrentUserId();
		QueryFilter queryFilter = new QueryFilter(request, "oaLinkmanItem");
		List list = this.oaLinkmanService.getByUserId(queryFilter, id);
		ModelAndView mv = getAutoView().addObject("oaLinkmanList", list);
		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除联系人")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.oaLinkmanService.delByIds(lAryId);
			message = new ResultMessage(1, "删除联系人成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑联系人")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		OaLinkman oaLinkman = new OaLinkman();
		oaLinkman = (OaLinkman) this.oaLinkmanService.getById(id);
		return getAutoView().addObject("oaLinkman", oaLinkman).addObject(
				"returnUrl", returnUrl);
	}

	@RequestMapping({ "get" })
	@Action(description = "查看联系人明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		OaLinkman oaLinkman = (OaLinkman) this.oaLinkmanService.getById(id);
		return getAutoView().addObject("oaLinkman", oaLinkman);
	}

	@RequestMapping({ "selector" })
	@Action(description = "联系人选择框")
	public ModelAndView selector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = UserContextUtil.getCurrentUserId();
		QueryFilter queryFilter = new QueryFilter(request, "oaLinkmanItem");
		List list = this.oaLinkmanService.getSelectorList(queryFilter, id);
		ModelAndView mv = getAutoView().addObject("oaLinkmanList", list);
		return mv;
	}

	@RequestMapping({ "addNew" })
	@Action(description = "添加新联系人")
	public ModelAndView addNew(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String email = RequestUtil.getString(request, "emailName");
		Long userId = UserContextUtil.getCurrentUserId();
		OaLinkman oaLinkman = new OaLinkman();
		if (!this.oaLinkmanService.isOaLinkExist(userId, email)) {
			oaLinkman.setEmail(email);
			oaLinkman.setUserid(userId);
			Date date = new Date(System.currentTimeMillis());
			oaLinkman.setCreatetime(date);
		} else {
			oaLinkman = this.oaLinkmanService.getByUserEmail(userId,
					email).get(0);
		}
		ModelAndView mv = getAutoView();
		return mv.addObject("oaLinkman", oaLinkman);
	}
}
