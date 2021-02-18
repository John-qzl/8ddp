package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.model.SysOrgType;
import com.cssrc.ibms.core.user.service.SysOrgTypeService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * <p>Title:SysOrgTypeController</p>
 * @author Yangbo 
 * @date 2016-8-2上午09:10:36
 */
@Controller
@RequestMapping( { "/oa/system/sysOrgType/" })
@Action(ownermodel = SysAuditModelType.ORG_MANAGEMENT)
public class SysOrgTypeController extends BaseController {

	@Resource
	private SysOrgTypeService sysOrgTypeService;

	@RequestMapping( { "save" })
	@Action(description="添加或更新组织结构类型", execOrder = ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>组织结构类型 ${SysAuditLinkService.getSysOrgTypeLink(Long.valueOf(id))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysOrgType.class }, pkName = "id")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isAdd = true;
		SysOrgType sysOrgType = getFormObject(request);
		try {
			if ((sysOrgType.getId() == null)
					|| (sysOrgType.getId().longValue() == 0L)) {
				sysOrgType.setId(Long.valueOf(UniqueIdUtil.genId()));
				sysOrgType.setOrgType_creatorId(UserContextUtil.getCurrentUserId());
				sysOrgType.setOrgType_createTime(new Date());
				sysOrgType.setOrgType_updateId(UserContextUtil.getCurrentUserId());
				sysOrgType.setOrgType_updateTime(new Date());
				this.sysOrgTypeService.add(sysOrgType);
				resultMsg = "组织结构类型保存成功";
			} else {
				sysOrgType.setOrgType_updateId(UserContextUtil.getCurrentUserId());
				sysOrgType.setOrgType_updateTime(new Date());
				this.sysOrgTypeService.update(sysOrgType);
				isAdd = false;
				resultMsg = "组织结构类型更新成功";
			}
			result = 1;
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0,
						"添加或更新组织结构类型失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isAdd));
			LogThreadLocalHolder.putParamerter("id", sysOrgType.getId().toString());
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	protected SysOrgType getFormObject(HttpServletRequest request)
			throws Exception {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));

		String json = RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);

		SysOrgType sysOrgType = (SysOrgType) JSONObject.toBean(obj,
				SysOrgType.class);

		return sysOrgType;
	}

	@RequestMapping( { "list" })
	@Action(description = "查看组织结构类型分页列表",detail="查看组织结构类型分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long demId = Long.valueOf(RequestUtil.getLong(request, "demId"));
		List list = this.sysOrgTypeService.getByDemId(demId.longValue());
		ModelAndView mv = getAutoView().addObject("sysOrgTypeList", list)
				.addObject("demId", demId);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description="删除组织结构类型", execOrder=ActionExecOrder.BEFORE, detail="删除组织结构类型<#list id?split(\",\") as item><#assign entity=sysOrgTypeService.getById(Long.valueOf(item))/>${entity.name}【${entity.id}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysOrgTypeService.delByIds(lAryId);
			message = new ResultMessage(1, "删除组织结构类型成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑组织结构类型",detail="编辑组织结构类型", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		Long demId = Long.valueOf(RequestUtil.getLong(request, "demId"));

		String returnUrl = RequestUtil.getPrePage(request);
		SysOrgType sysOrgType = (SysOrgType) this.sysOrgTypeService.getById(id);
		ModelAndView mv = getAutoView().addObject("sysOrgType", sysOrgType)
				.addObject("returnUrl", returnUrl);
		if (id.longValue() == 0L) {
			Integer temp = this.sysOrgTypeService.getMaxLevel(demId);
			if (temp == null)
				temp = Integer.valueOf(0);
			Integer currentLevel = Integer.valueOf(temp.intValue() + 1);
			mv.addObject("levels", currentLevel).addObject("demId", demId);
		}
		return mv;
	}

	@RequestMapping( { "get" })
	@Action(description = "查看组织结构类型明细",detail="查看组织结构类型明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysOrgType sysOrgType = (SysOrgType) this.sysOrgTypeService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("sysOrgType", sysOrgType);
	}

	@RequestMapping( { "multSave" })
	@Action(description = "更新组织结构类型排序", execOrder=ActionExecOrder.AFTER, detail="更新组织结构类型排序", exectype = SysAuditExecType.UPDATE_TYPE)
	public void multSave(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		String[] ids = RequestUtil.getStringAryByStr(request, "ids");
		try {
			for (Integer i = Integer.valueOf(1); i.intValue() < ids.length; i = Integer
					.valueOf(i.intValue() + 1)) {
				SysOrgType sysOrgType = (SysOrgType) this.sysOrgTypeService
						.getById(Long
								.valueOf(Long.parseLong(ids[i.intValue()])));

				sysOrgType
						.setLevels(Long.valueOf(Long.parseLong(i.toString())));
				this.sysOrgTypeService.update(sysOrgType);
			}
			result = 1;
			resultMsg = "组织结构类型保存成功";
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0,
						"组织结构类型保存失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
