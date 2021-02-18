package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.UserUnder;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.user.service.UserUnderService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
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
 * 用户上下级Controller层
 * <p>Title:UserUnderController</p>
 * @author Yangbo 
 * @date 2016-8-20下午03:59:53
 */
@Controller
@RequestMapping( { "/oa/system/userUnder/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class UserUnderController extends BaseController {

	@Resource
	private UserUnderService userUnderService;
	@Resource
	private SysUserService sysUserService;
	
	@RequestMapping( { "list" })
	@Action(description = "查看下属管理分页列表", detail = "查看下属管理分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean showMyleader = false;

		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		if (userId.longValue() == 0L) {
			userId = UserContextUtil.getCurrentUser().getUserId();
			showMyleader = true;
		}
		QueryFilter queryFilter = new QueryFilter(request, "userUnderItem");
		queryFilter.addFilterForIB("userid", userId);
		List list = this.userUnderService.getAll(queryFilter);
		ModelAndView mv = getAutoView().addObject("userUnderList", list);
		mv.addObject("userId", userId).addObject("showMyleader",
				Boolean.valueOf(showMyleader));
		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除下属管理", execOrder = ActionExecOrder.BEFORE, detail = "删除下属管理：<#list StringUtils.split(id,\",\") as item><#assign entity=userUnderService.getById(Long.valueOf(item))/>(为【${SysAuditLinkService.getSysUserLink(Long.valueOf(entity.userid))}】删除下属【${SysAuditLinkService.getSysUserLink(Long.valueOf(entity.underuserid))}】),</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.userUnderService.delByIds(lAryId);
			message = new ResultMessage(1, "删除下属管理成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑下属管理")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String returnUrl = RequestUtil.getPrePage(request);
		UserUnder userUnder = null;
		if (id.longValue() != 0L)
			userUnder = (UserUnder) this.userUnderService.getById(id);
		else {
			userUnder = new UserUnder();
		}
		return getAutoView().addObject("userUnder", userUnder).addObject(
				"returnUrl", returnUrl);
	}

	@RequestMapping( { "myLeaders" })
	@Action(description = "编辑下属管理", detail = "编辑下属管理")
	public ModelAndView getMyLeaders(HttpServletRequest request)
			throws Exception {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		List list = this.sysUserService.getMyLeaders(userId);
		ModelAndView mv = getAutoView().addObject("sysUserList", list);

		return mv;
	}

	@RequestMapping( { "get" })
	@Action(description = "查看下属管理明细", detail = "查看下属管理明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		UserUnder userUnder = (UserUnder) this.userUnderService.getById(Long
				.valueOf(id));
		return getAutoView().addObject("userUnder", userUnder);
	}

	@RequestMapping( { "addUnderUser" })
	@Action(description = "添加下属", detail = "<#assign entity=sysUserService.getById(Long.valueOf(userId))/>为【${SysAuditLinkService.getSysUserLink(Long.valueOf(userId))}】添加<#list userIds?split(\",\") as item>【${SysAuditLinkService.getSysUserLink(Long.valueOf(item))}】</#list>等下属")
	public void addUnderUser(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userIds = RequestUtil.getSecureString(request, "userIds");
		String userNames = RequestUtil.getSecureString(request, "userNames");

		Long userId = Long.valueOf(RequestUtil.getLong(request, "userId"));
		if (userId.longValue() == 0L) {
			userId = UserContextUtil.getCurrentUser().getUserId();
		}
		LogThreadLocalHolder.putParamerter("userId", userId);
		this.userUnderService.addMyUnderUser(userId, userIds, userNames);
	}
}
