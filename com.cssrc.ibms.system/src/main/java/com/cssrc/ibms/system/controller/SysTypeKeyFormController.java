package com.cssrc.ibms.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.system.model.SysTypeKey;
import com.cssrc.ibms.system.service.SysTypeKeyService;
/**
 * 分类标识管理FORM提交
 * <p>Title:SysTypeKeyController</p>
 * @author YangBo
 * @date 2016-8-29下午05:23:18
 */
@Controller
@RequestMapping({ "/oa/system/sysTypeKey/" })
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SysTypeKeyFormController extends BaseFormController {

	@Resource
	private SysTypeKeyService sysTypeKeyService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新系统分类键定义", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>系统分类键定义【${SysAuditLinkService.getSysTypeKeyLink(Long.valueOf(typeKeyId))}】")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysTypeKey sysTypeKey, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("sysTypeKey", sysTypeKey,bindResult, request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		LogThreadLocalHolder.putParamerter("isAdd",Boolean.valueOf(sysTypeKey.getTypeKeyId() == null));
		String typeKey = sysTypeKey.getTypeKey();
		ResultMessage resultMsg = null;
		if (sysTypeKey.getTypeKeyId().longValue() == 0L) {
			if (this.sysTypeKeyService.isExistKey(typeKey)) {
				String message = "键值已经存在!";
				resultMsg = new ResultMessage(0, message);
			} else {
				try {
					sysTypeKey.setTypeKeyId(Long.valueOf(UniqueIdUtil.genId()));
					sysTypeKey.setFlag(Integer.valueOf(0));
					sysTypeKey.setSn(Integer.valueOf(0));
					this.sysTypeKeyService.add(sysTypeKey);
					String message = "添加系统分类键定义成功";
					resultMsg = new ResultMessage(1, message);
				} catch (Exception ex) {
					String str = MessageUtil.getMessage();
					if (StringUtil.isNotEmpty(str)) {
						resultMsg = new ResultMessage(0, "添加系统分类key失败:" + str);
						response.getWriter().print(resultMsg);
					}
					String message = ExceptionUtil.getExceptionMessage(ex);
					resultMsg = new ResultMessage(0, message);
					response.getWriter().print(resultMsg);
				}
			}

		} else if (this.sysTypeKeyService.isKeyExistForUpdate(typeKey,sysTypeKey.getTypeKeyId())) {
			String message = "键值已经存在!";
			resultMsg = new ResultMessage(0, message);
		} else {
			try {
				this.sysTypeKeyService.update(sysTypeKey);
				String message = "更新系统分类键定义成功";
				resultMsg = new ResultMessage(1, message);
			} catch (Exception ex) {
				String str = MessageUtil.getMessage();
				if (StringUtil.isNotEmpty(str)) {
					resultMsg = new ResultMessage(0, "修改系统分类key失败:" + str);
					response.getWriter().print(resultMsg);
				} else {
					String message = ExceptionUtil.getExceptionMessage(ex);
					resultMsg = new ResultMessage(0, message);
					response.getWriter().print(resultMsg);
				}
			}
		}

		writeResultMessage(response.getWriter(), resultMsg);
		LogThreadLocalHolder.putParamerter("typeKeyId", sysTypeKey.getTypeKeyId().toString());
	}

	@ModelAttribute
	protected SysTypeKey getFormObject(@RequestParam("typeKeyId") Long typeKeyId,Model model) throws Exception {
		this.logger.debug("enter SysTypeKey getFormObject here....");
		SysTypeKey sysTypeKey = null;
		if (typeKeyId.longValue() > 0L)
			sysTypeKey = (SysTypeKey) this.sysTypeKeyService.getById(typeKeyId);
		else {
			sysTypeKey = new SysTypeKey();
		}
		return sysTypeKey;
	}
}
