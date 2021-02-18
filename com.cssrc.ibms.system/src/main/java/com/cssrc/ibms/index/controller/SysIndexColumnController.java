package com.cssrc.ibms.index.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTacticService;
import com.cssrc.ibms.api.sysuser.model.ISysObjRights;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysOrgTactic;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.SysIndexColumn;
import com.cssrc.ibms.index.service.SysIndexColumnService;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.service.GlobalTypeService;
@Controller
@RequestMapping({"/oa/system/sysIndexColumn/"})
public class SysIndexColumnController extends BaseController {

	@Resource
	private SysIndexColumnService sysIndexColumnService;

	@Resource
	private ISysOrgService sysOrgService;

	@Resource
	private GlobalTypeService globalTypeService;
	
	@Resource    
	private ISysOrgTacticService sysOrgTacticService;


	@RequestMapping( { "save" })
	@Action(description = "添加或更新首页栏目")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysIndexColumn sysIndexColumn) throws Exception {
		String resultMsg = null;
		try {
			String alias = sysIndexColumn.getAlias();

			Boolean isExist = this.sysIndexColumnService.isExistAlias(
					sysIndexColumn.getAlias(), sysIndexColumn.getId());
			if (isExist.booleanValue()) {
				resultMsg = "栏目别名：[" + alias + "]已存在";
				writeResultMessage(response.getWriter(), resultMsg, 0);
				return;
			}
			if (!UserContextUtil.isSuperAdmin()) {
					Long orgId =  UserContextUtil.getCurrentOrgId();
					if (BeanUtils.isNotEmpty(orgId))
						sysIndexColumn.setOrgId(orgId);
			}
			if ((sysIndexColumn.getId() == null)
					|| (sysIndexColumn.getId().longValue() == 0L)) {
				sysIndexColumn.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.sysIndexColumnService.add(sysIndexColumn);
				resultMsg = getText("添加首页栏目成功");
			} else {
				this.sysIndexColumnService.update(sysIndexColumn);
				resultMsg = getText("更新首页栏目成功");
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "saveOrg" })
	@Action(description = "添加或更新首页栏目")
	public void saveOrg(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		Long orgId = RequestUtil.getLong(request, "orgId", null);
		try {
			SysIndexColumn sysIndexColumn = (SysIndexColumn) this.sysIndexColumnService
					.getById(id);
			sysIndexColumn.setOrgId(orgId);
			this.sysIndexColumnService.update(sysIndexColumn);
			resultMsg = getText("保存组织成功");
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看首页栏目分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request, "sysIndexColumnItem");
		boolean isSuperAdmin = UserContextUtil.isSuperAdmin();
		List<SysIndexColumn> hashRightList = this.sysIndexColumnService.getHashRightColumnList(
				filter, null, Boolean.valueOf(false));
		for (SysIndexColumn sysIndexColumn : hashRightList) {
			//isNotIncZeroEmpty
			if (BeanUtils.isNotIncZeroEmpty(sysIndexColumn.getOrgId())) {
				ISysOrg sysOrg = (ISysOrg) this.sysOrgService
						.getById(sysIndexColumn.getOrgId());
				sysIndexColumn.setOrgName(sysOrg.getOrgName());
			}
		}
		ISysOrgTactic sysOrgTactic = this.sysOrgTacticService.getOrgTactic();
		return getAutoView().addObject("objType",ISysObjRights.RIGHT_TYPE_INDEX_COLUMN)
				.addObject("sysIndexColumnList", hashRightList)
				.addObject("isSuperAdmin", Boolean.valueOf(isSuperAdmin))
				.addObject("orgTactic", sysOrgTactic.getOrgTactic());
	}

	@RequestMapping( { "del" })
	@Action(description = "删除首页栏目")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysIndexColumnService.delByIds(lAryId);
			message = new ResultMessage(1, "删除首页栏目成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑首页栏目")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		SysIndexColumn sysIndexColumn = (SysIndexColumn) this.sysIndexColumnService
				.getById(id);
		//BeanUtils.isNotIncZeroEmpty(sysIndexColumn.getOrgId())
		if ((BeanUtils.isNotEmpty(sysIndexColumn))
				&& (BeanUtils.isNotEmpty(sysIndexColumn.getOrgId()))) {
			ISysOrg sysOrg =this.sysOrgService.getById(sysIndexColumn
					.getOrgId());
			sysIndexColumn.setOrgName(sysOrg.getOrgName());
		}

		List<GlobalType> catalogList = this.globalTypeService.getByCatKey(
				GlobalType.CAT_INDEX_COLUMN, false);

		return getAutoView().addObject("sysIndexColumn", sysIndexColumn)
				.addObject("catalogList", catalogList).addObject("returnUrl",
						returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看首页栏目明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysIndexColumn sysIndexColumn = (SysIndexColumn) this.sysIndexColumnService
				.getById(id);
		return getAutoView().addObject("sysIndexColumn", sysIndexColumn);
	}

	@RequestMapping( { "getTemp" })
	@Action(description = "预览模版")
	public ModelAndView getTemp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		Map<String, Object> params = RequestUtil.getParameterValueMap(request);
		String html = "";
		try {
			html = this.sysIndexColumnService.getHtmlById(id, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getAutoView().addObject("html", html);
	}

	@RequestMapping( { "getData" })
	@Action(description = "查看首页栏目明细")
	@ResponseBody
	public String getData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String alias = RequestUtil.getString(request, "alias");
		Map<String, Object> params = getParameterValueMap(request);
		String data = "";
		try {
			data = this.sysIndexColumnService.getHtmlByColumnAlias(alias, params);
		} catch (Exception localException) {
		}
		return data;
	}

	private Map<String, Object> getParameterValueMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("__ctx", request.getContextPath());
		String params = RequestUtil.getString(request, "params");
		if (BeanUtils.isEmpty(params))
			return map;
		JSONObject json = JSONObject.fromObject(params);
		Iterator it = json.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = json.get(key);
			map.put(key, value);
		}
		return map;
	}

	@RequestMapping( { "init" })
	@Action(description = "初始化模板", detail = "初始化模板")
	public void init(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			this.sysIndexColumnService.initIndexColumn();
			message = new ResultMessage(1, "初始化模板成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "初始化表模板失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
}
