package com.cssrc.ibms.system.controller;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.cache.intf.ICache;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.SysPaur;
import com.cssrc.ibms.system.service.SysPaurService;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/sysPaur/" })
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
public class SysPaurController extends BaseController {

	@Resource
	private SysPaurService sysPaurService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新SYS_PAUR", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>SYS_PAUR【${SysAuditLinkService.getSysPaurLink(Long.valueOf(paurid))}】")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		SysPaur sysPaur = getFormObject(request);
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(sysPaur
					.getPaurid() == null));
			if ((sysPaur.getPaurid() == null)
					|| (sysPaur.getPaurid().longValue() == 0L)) {
				sysPaur.setPaurid(Long.valueOf(UniqueIdUtil.genId()));
				this.sysPaurService.add(sysPaur);
				resultMsg = getText("SYS_PAUR");
			} else {
				this.sysPaurService.update(sysPaur);
				resultMsg = getText("SYS_PAUR");
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
			LogThreadLocalHolder.putParamerter("paurid", sysPaur.getPaurid()
					.toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	protected SysPaur getFormObject(HttpServletRequest request)
			throws Exception {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));

		String json = RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);

		SysPaur sysPaur = (SysPaur) JSONObject.toBean(obj, SysPaur.class);

		return sysPaur;
	}

	@RequestMapping( { "list" })
	@Action(description = "查看SYS_PAUR分页列表", detail = "查看SYS_PAUR分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, "");
		queryFilter.getFilters().put("userid", Integer.valueOf(0));
		List list = this.sysPaurService.getAll(queryFilter);
		ModelAndView mv = getAutoView().addObject("sysPaurList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除SYS_PAUR", execOrder = ActionExecOrder.BEFORE, detail = "删除SYS_PAUR<#list StringUtils.split(id,\",\") as item><#assign entity=sysPaurService.getById(Long.valueOf(item))/>【${entity.paurname}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "paurid");
			this.sysPaurService.delByIds(lAryId);
			message = new ResultMessage(1, "删除SYS_PAUR成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "添加或编辑SYS_PAUR", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加SYS_PAUR<#else>编辑SYS_PAUR:<#assign entity=sysPaurService.getById(Long.valueOf(paurid))/>【${entity.paurname}】</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long paurid = Long.valueOf(RequestUtil.getLong(request, "paurid"));
		String returnUrl = RequestUtil.getPrePage(request);
		SysPaur sysPaur = (SysPaur) this.sysPaurService.getById(paurid);
		LogThreadLocalHolder.putParamerter("isAdd", Boolean
				.valueOf(sysPaur == null));

		return getAutoView().addObject("sysPaur", sysPaur).addObject(
				"returnUrl", returnUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看SYS_PAUR明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long paurid = RequestUtil.getLong(request, "paurid");
		SysPaur sysPaur = (SysPaur) this.sysPaurService.getById(Long
				.valueOf(paurid));
		return getAutoView().addObject("sysPaur", sysPaur);
	}

	@RequestMapping( { "changeSkin" })
	@Action(description = "添加或更新SYS_PAUR", detail = "<#if isAdd> 添加<#else>更新</#if>SYS_PAUR:【${SysAuditLinkService.getSysPaurLink(Long.valueOf(paurid))}】")
	public void changeSkin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String resultMsg = null;
		String styleName = RequestUtil.getString(request, "styleName");

		Long userId = UserContextUtil.getCurrentUserId();
		try {
			if (StringUtil.isNotEmpty(styleName)) {
				boolean isadd = true;
				SysPaur sysPaur = this.sysPaurService.getByUserAndAlias(userId,
						"skin");
				if (sysPaur != null) {
					sysPaur.setPaurvalue(styleName);
					this.sysPaurService.update(sysPaur);
					resultMsg = getText("SYS_PAUR");
					isadd = false;
				} else {
					sysPaur = new SysPaur();
					sysPaur.setPaurid(Long.valueOf(UniqueIdUtil.genId()));
					sysPaur.setAliasname("skin");
					sysPaur.setPaurname("皮肤");
					sysPaur.setUserid(userId);
					sysPaur.setPaurvalue(styleName);
					this.sysPaurService.add(sysPaur);
					resultMsg = getText("SYS_PAUR");
				}
				ICache iCache = (ICache) AppUtil.getBean(ICache.class);
				iCache.delByKey("skinStyle_" + userId);
				HttpSession session = request.getSession();
				session.setAttribute("skinStyle", styleName);
				LogThreadLocalHolder.putParamerter("isAdd", Boolean
						.valueOf(isadd));
				LogThreadLocalHolder.putParamerter("paurid", sysPaur
						.getPaurid().toString());
			}

			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}
}
