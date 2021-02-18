package com.cssrc.ibms.system.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.system.model.SysTypeKey;
import com.cssrc.ibms.system.service.SysTypeKeyService;

/**
 * 分类标识管理
 * <p>Title:SysTypeKeyController</p>
 * @author Yangbo 
 * @date 2016-8-29下午05:23:18
 */
@Controller
@RequestMapping("/oa/system/sysTypeKey/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SysTypeKeyController extends BaseController {

	@Resource
	SysTypeKeyService sysTypeKeyService;

	@RequestMapping("list")
	public ModelAndView list(HttpServletRequest request) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, "sysTypeKeyItem", false);
		List<SysTypeKey> list = this.sysTypeKeyService.getAll(queryFilter);
		ModelAndView mv = getAutoView().addObject("sysTypeKeyList", list);
		return mv;
	}

	@RequestMapping({"getPingyinByName"})
	@Action(description="根据汉字获取对应的拼音")
	public void getPingyinByName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String typeName = RequestUtil.getString(request, "typeName");
		String str = PinyinUtil.getPinYinHeadCharFilter(typeName);
		out.print(str);
	}

	@RequestMapping({"del"})
	@Action(description="删除系统分类键定义", execOrder=ActionExecOrder.BEFORE, detail="删除系统数据源<#list StringUtils.split(typeKeyId,\",\") as item><#assign entity=sysTypeKeyService.getById(Long.valueOf(item))/>【${entity.typeName}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "typeKeyId");
			this.sysTypeKeyService.delByIds(lAryId);
			message = new ResultMessage(1, "删除系统分类键定义成功!");
		}
		catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"edit"})
	@Action(description="编辑系统分类键定义", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加系统分类键定义<#else>编辑系统分类键定义:<#assign entity=sysTypeKeyService.getById(Long.valueOf(typeKeyId))/>【${entity.name}】</#if>")
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		Long typeKeyId = Long.valueOf(RequestUtil.getLong(request, "typeKeyId"));
		String returnUrl = RequestUtil.getPrePage(request);
		SysTypeKey sysTypeKey = null;
		boolean isadd = true;
		if (typeKeyId.longValue() != 0L) {
			sysTypeKey = (SysTypeKey)this.sysTypeKeyService.getById(typeKeyId);
			isadd = false;
		} else {
			sysTypeKey = new SysTypeKey();
		}
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		return getAutoView().addObject("sysTypeKey", sysTypeKey).addObject("returnUrl", returnUrl);
	}

	@RequestMapping({"get"})
	@Action(description="查看系统分类键定义明细", detail="查看系统分类键定义明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		long id = RequestUtil.getLong(request, "typeKeyId");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		SysTypeKey sysTypeKey = (SysTypeKey)this.sysTypeKeyService.getById(Long.valueOf(id));
		return getAutoView().addObject("sysTypeKey", sysTypeKey).addObject("canReturn", Long.valueOf(canReturn));
	}
	@RequestMapping({"saveSequence"})
	@Action(description="排序操作", detail="排序操作")
	public void saveSequence(HttpServletRequest request, HttpServletResponse response) throws Exception { ResultMessage message;
	try { Long[] ids = RequestUtil.getLongAryByStr(request, "ids");
	this.sysTypeKeyService.saveSequence(ids);
	message = new ResultMessage(1, "分类排序成功!");
	}
	catch (Exception ex)
	{
		message = new ResultMessage(0, "分类排序失败!");
	}
	writeResultMessage(response.getWriter(), message);
	}
}
