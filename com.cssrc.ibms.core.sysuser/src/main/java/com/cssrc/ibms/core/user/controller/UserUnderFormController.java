package com.cssrc.ibms.core.user.controller;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.user.model.UserUnder;
import com.cssrc.ibms.core.user.service.UserUnderService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 
 * <p>Title:UserUnderFormController</p>
 * @author Yangbo 
 * @date 2016-8-20下午04:03:54
 */
@Controller
@RequestMapping( { "/oa/system/userUnder/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class UserUnderFormController extends BaseFormController {

	@Resource
	private UserUnderService userUnderService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新下属管理", detail = "<#if StringUtil.isNotEmpty(isAdd)>为【${SysAuditLinkService.getSysUserLink(Long.valueOf(userid))}】<#if isAdd>添加<#else>更新</#if>下属【${SysAuditLinkService.getSysUserLink(Long.valueOf(underuserid))}】<#else>添加或更新下属管理失败</#if>")
	public void save(HttpServletRequest request, HttpServletResponse response,
			UserUnder userUnder, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("userUnder", userUnder,
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		String resultMsg = null;
		String isadd = "0";
		if (userUnder.getId() == null) {
			userUnder.setId(Long.valueOf(UniqueIdUtil.genId()));
			this.userUnderService.add(userUnder);
			resultMsg = "添加下属管理成功";
		} else {
			this.userUnderService.update(userUnder);
			resultMsg = "更新下属管理成功";
			isadd = "1";
		}
		LogThreadLocalHolder.putParamerter("isAdd", isadd);
		LogThreadLocalHolder.putParamerter("userid", userUnder.getUserid()
				.toString());
		LogThreadLocalHolder.putParamerter("underuserid", userUnder
				.getUnderuserid().toString());
		writeResultMessage(response.getWriter(), resultMsg, 1);
	}

	@ModelAttribute
	protected UserUnder getFormObject(@RequestParam("id") Long id, Model model)
			throws Exception {
		this.logger.debug("enter UserUnder getFormObject here....");
		UserUnder userUnder = null;
		if (id != null)
			userUnder = (UserUnder) this.userUnderService.getById(id);
		else {
			userUnder = new UserUnder();
		}
		return userUnder;
	}
}
