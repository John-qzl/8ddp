package com.cssrc.ibms.system.controller;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.Dictionary;
import com.cssrc.ibms.system.service.DictionaryService;

import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/oa/system/dictionary/"})
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class DictionaryFormController extends BaseFormController
{

	@Resource
	private DictionaryService dictionaryService;

	@RequestMapping({"save"})
	@Action(description="添加或更新数据字典", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>新数据字典【${SysAuditLinkService.getDictionaryLink(Long.valueOf(dicId))}】")
	public void save(HttpServletRequest request, HttpServletResponse response, Dictionary dictionary, BindingResult bindResult)
	throws Exception
	{
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = validForm("dictionary", dictionary, bindResult, request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}

		Long dicId = dictionary.getDicId();
		Long typeId = dictionary.getTypeId();
		Long parentId = dictionary.getParentId();
		String itemKey = dictionary.getItemKey();

		if (dicId.longValue() == 0L) {
			if (StringUtil.isNotEmpty(itemKey)) {
				boolean rtn = this.dictionaryService.isItemKeyExists(typeId.longValue(), itemKey);
				if (rtn) {
					resultMessage = new ResultMessage(0, "字典关键字已存在");
					writeResultMessage(out, resultMessage);
					return;
				}
			}
			try {
				dicId = Long.valueOf(UniqueIdUtil.genId());
				dictionary.setDicId(dicId);
				dictionary.setSn(Long.valueOf(0L));

				if (parentId.equals(typeId)) {
					dictionary.setParentId(typeId);
					dictionary.setNodePath("." + parentId + "." + dicId + ".");
				}
				else {
					Dictionary parentDic = (Dictionary)this.dictionaryService.getById(parentId);
					dictionary.setParentId(parentId);
					dictionary.setNodePath(parentDic.getNodePath() + dicId + ".");
				}
				this.dictionaryService.add(dictionary);
				resultMessage = new ResultMessage(1, "添加字典成功!");
				writeResultMessage(response.getWriter(), resultMessage);
				LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(true));
				LogThreadLocalHolder.putParamerter("dicId", dictionary.getDicId().toString());
			}
			catch (Exception ex) {
				String str = MessageUtil.getMessage();
				ex.printStackTrace();
				this.logger.error(ex.getMessage());
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(0, "添加字典失败:" + str);
					response.getWriter().print(resultMessage); return;
				}
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}

		}
		else
		{
			if (StringUtil.isNotEmpty(itemKey)) {
				boolean rtn = this.dictionaryService.isItemKeyExistsForUpdate(dicId.longValue(), typeId.longValue(), itemKey);
				if (rtn) {
					resultMessage = new ResultMessage(0, "字典关键字已存在");
					writeResultMessage(out, resultMessage);
					return;
				}
			}
			try {
				this.dictionaryService.update(dictionary);
				resultMessage = new ResultMessage(1, "编辑字典成功!");
				writeResultMessage(response.getWriter(), resultMessage);
				LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(false));
				LogThreadLocalHolder.putParamerter("dicId", dictionary.getDicId().toString());
			}
			catch (Exception e) {
				String str = MessageUtil.getMessage();
				e.printStackTrace();
				this.logger.error(e.getMessage());
				if (StringUtil.isNotEmpty(str)) {
					resultMessage = new ResultMessage(0, "编辑字典失败:" + str);
					response.getWriter().print(resultMessage);
				} else {
					String message = ExceptionUtil.getExceptionMessage(e);
					resultMessage = new ResultMessage(0, message);
					response.getWriter().print(resultMessage);
				}
			}
		}
	}

	@ModelAttribute
	protected Dictionary getFormObject(@RequestParam("dicId") Long dicId, Model model)
	throws Exception
	{
		Dictionary dictionary = null;

		if (dicId.longValue() != 0L)
			dictionary = (Dictionary)this.dictionaryService.getById(dicId);
		else {
			dictionary = new Dictionary();
		}
		return dictionary;
	}
}

