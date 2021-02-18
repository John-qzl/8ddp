package com.cssrc.ibms.index.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.cssrc.ibms.index.model.SysIndexLayout;
import com.cssrc.ibms.index.model.SysIndexLayoutManage;
import com.cssrc.ibms.index.service.SysIndexColumnService;
import com.cssrc.ibms.index.service.SysIndexLayoutManageService;
import com.cssrc.ibms.index.service.SysIndexLayoutService;
/**
 * 
 * @author Yangbo 2016-7-22
 *
 */
@Controller
@RequestMapping( { "/oa/system/sysIndexLayoutManage/" })
public class SysIndexLayoutManageController extends BaseController {

	@Resource
	private SysIndexLayoutManageService sysIndexLayoutManageService;

	@Resource
	private SysIndexLayoutService sysIndexLayoutService;

	@Resource
	private SysIndexColumnService sysIndexColumnService;
	
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private ISysOrgTacticService sysOrgTacticService;
	

	@RequestMapping( { "save" })
	@Action(description = "添加或更新布局管理")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysIndexLayoutManage sysIndexLayoutManage) throws Exception {
		String resultMsg = null;
		try {
			if ((sysIndexLayoutManage.getId() == null)
					|| (sysIndexLayoutManage.getId().longValue() == 0L)) {
				sysIndexLayoutManage.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.sysIndexLayoutManageService.add(sysIndexLayoutManage);
				resultMsg = getText("添加", new Object[] { "布局管理" });
			} else {
				this.sysIndexLayoutManageService.update(sysIndexLayoutManage);
				resultMsg = getText("更新", new Object[] { "布局管理" });
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看布局管理分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request,
				"sysIndexLayoutManageItem");
     	boolean isSuperAdmin = UserContextUtil.isSuperAdmin();
		if (!isSuperAdmin) {
			List orgIds = sysOrgService.getOrgsByUserId(UserContextUtil.getCurrentUserId());
			filter.addFilterForIB("orgIds", StringUtils.join(orgIds, ","));
		}
		List<SysIndexLayoutManage> list = this.sysIndexLayoutManageService.getAll(filter);
		//BeanUtils.isNotIncZeroEmpty(sysIndexLayoutManage.getOrgId())
		for (SysIndexLayoutManage sysIndexLayoutManage : list) {
			if (BeanUtils.isNotIncZeroEmpty(sysIndexLayoutManage.getOrgId())) {
				ISysOrg Org = (ISysOrg) this.sysOrgService
				.getById(sysIndexLayoutManage.getOrgId());
				sysIndexLayoutManage.setOrgName(Org.getOrgName());
			}
		}
		ISysOrgTactic sysOrgTactic = this.sysOrgTacticService.getOrgTactic();
		return getAutoView().addObject("objType",ISysObjRights.RIGHT_TYPE_INDEX_MANAGE)
				.addObject("isSuperAdmin", Boolean.valueOf(isSuperAdmin))
				.addObject("sysIndexLayoutManageList", list)
				.addObject("orgTactic",sysOrgTactic.getOrgTactic());
	}

	@RequestMapping( { "del" })
	@Action(description = "删除布局管理")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysIndexLayoutManageService.delByIds(lAryId);
			message = new ResultMessage(1, "删除布局管理成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑布局管理")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String returnUrl = RequestUtil.getPrePage(request);
		SysIndexLayoutManage sysIndexLayoutManage = (SysIndexLayoutManage) this.sysIndexLayoutManageService
				.getById(id);

		return getAutoView().addObject("sysIndexLayoutManage",
				sysIndexLayoutManage).addObject("returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看布局管理明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysIndexLayoutManage sysIndexLayoutManage = (SysIndexLayoutManage) this.sysIndexLayoutManageService
				.getById(id);
		return getAutoView().addObject("sysIndexLayoutManage",
				sysIndexLayoutManage);
	}

	@RequestMapping( { "design" })
	@Action(description = "设计我的首页布局")
	public ModelAndView design(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));

		List<SysIndexLayout> layoutList = this.sysIndexLayoutService.getAll();
		QueryFilter filter = new QueryFilter(request);
		filter.addFilterForIB("id", null);
		filter.setPagingBean(null);
		Map params = RequestUtil.getParameterValueMap(request);

		List columnList = this.sysIndexColumnService.getHashRightColumnList(
				filter, params, Boolean.valueOf(true));

		Map columnMap = this.sysIndexColumnService.getColumnMap(columnList);

		SysIndexLayoutManage sysIndexLayoutManage = this.sysIndexLayoutManageService
				.getLayoutList(id, columnList);
		//BeanUtils.isNotIncZeroEmpty(sysIndexLayoutManage.getOrgId())
		if ((BeanUtils.isNotEmpty(sysIndexLayoutManage))
				&& (BeanUtils.isNotEmpty(sysIndexLayoutManage.getOrgId()))) {
			ISysOrg Org = (ISysOrg) this.sysOrgService
					.getById(sysIndexLayoutManage.getOrgId());
			sysIndexLayoutManage.setOrgName(Org.getOrgName());
		}
		return getAutoView().addObject("layoutList", layoutList).addObject(
				"columnMap", columnMap).addObject("sysIndexLayoutManage",
				sysIndexLayoutManage);
	}

	@RequestMapping( { "saveLayout" })
	@Action(description = "保存首页布局")
	public void saveLayout(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = RequestUtil.getLong(request, "id", null);
		String name = RequestUtil.getString(request, "name");
		String memo = RequestUtil.getString(request, "memo");
		Short isDef = RequestUtil.getShort(request, "isDef");
		String html = RequestUtil.getString(request, "html");
		String designHtml = RequestUtil.getString(request, "designHtml");

		ResultMessage resultObj = null;
		try {
			int type = 0;
			SysIndexLayoutManage sysIndexLayoutManage = new SysIndexLayoutManage();
			if (BeanUtils.isEmpty(id)) {
				id = Long.valueOf(UniqueIdUtil.genId());
				sysIndexLayoutManage.setId(id);
			} else {
				type = 1;
				sysIndexLayoutManage = (SysIndexLayoutManage) this.sysIndexLayoutManageService
						.getById(id);
			}
			if (!UserContextUtil.isSuperAdmin()) {
				Long companyId = UserContextUtil.getCurrentOrgId();
				sysIndexLayoutManage.setOrgId(companyId);
			}
			sysIndexLayoutManage.setName(name);
			sysIndexLayoutManage.setMemo(memo);
			sysIndexLayoutManage.setIsDef(isDef);
			sysIndexLayoutManage.setDesignHtml(designHtml);
			sysIndexLayoutManage.setTemplateHtml(html);
			this.sysIndexLayoutManageService.save(sysIndexLayoutManage, type);

			resultObj = new ResultMessage(1, id.toString());
		} catch (Exception e) {
			resultObj = new ResultMessage(0, e.getMessage());
		}
		response.getWriter().print(resultObj);
	}

	@RequestMapping( { "dialog" })
	@Action(description = "选择首页布局模版")
	public ModelAndView dialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter filter = new QueryFilter(request,
				"sysIndexLayoutManageItem");
		Map params = RequestUtil.getParameterValueMap(request);

		List columnList = this.sysIndexColumnService.getHashRightColumnList(
				filter, params, Boolean.valueOf(true));
		List<SysIndexLayoutManage> list = this.sysIndexLayoutManageService
				.getList(filter);
		for (SysIndexLayoutManage sysIndexLayoutManage : list) {
			sysIndexLayoutManage.setDesignHtml(this.sysIndexColumnService
					.parserDesignHtml(sysIndexLayoutManage.getDesignHtml(),
							columnList));
		}
		return getAutoView().addObject("sysIndexLayoutManageList", list);
	}

	@RequestMapping( { "saveOrg" })
	@Action(description = "保存组织")
	public void saveOrg(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		Long orgId = RequestUtil.getLong(request, "orgId", null);
		try {
			SysIndexLayoutManage sysIndexLayoutManage = (SysIndexLayoutManage) this.sysIndexLayoutManageService
					.getById(id);
			sysIndexLayoutManage.setOrgId(orgId);
			this.sysIndexLayoutManageService.update(sysIndexLayoutManage);
			resultMsg = getText("保存组织成功");
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}
}
