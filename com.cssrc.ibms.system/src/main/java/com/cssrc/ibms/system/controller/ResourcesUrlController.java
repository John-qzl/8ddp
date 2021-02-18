package com.cssrc.ibms.system.controller;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.Resources;
import com.cssrc.ibms.system.model.ResourcesUrl;
import com.cssrc.ibms.system.service.ResourcesService;
import com.cssrc.ibms.system.service.ResourcesUrlService;
/**
 * 
 * <p>Title:ResourcesUrlController</p>
 * @author Yangbo 
 * @date 2016-8-22下午09:57:32
 */
@Controller
@RequestMapping( { "/oa/system/resourcesUrl/" })
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class ResourcesUrlController extends BaseController {

	@Resource
	private ResourcesUrlService resourcesUrlService;

	@Resource
	private ResourcesService resourcesService;

	@RequestMapping( { "edit" })
	@Action(description = "编辑资源URL", execOrder = ActionExecOrder.AFTER, detail = "编辑<#assign entity=resourcesService.getById(Long.valueOf(resId))/>【<a href='${ctx}/ibms/oa/system/resourcesUrl/edit.do?resId=${resId}'>${entity.resName}</a>】的URL", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long resId = Long.valueOf(RequestUtil.getLong(request, "resId", 0L));
		String returnUrl = RequestUtil.getString(request, "returnUrl",
				RequestUtil.getPrePage(request));
		List resourcesUrlList = this.resourcesUrlService.getByResId(resId
				.longValue());
		Resources resources = (Resources) this.resourcesService.getById(resId);
		return getAutoView().addObject("resourcesUrlList", resourcesUrlList)
				.addObject("returnUrl", returnUrl).addObject("resources",
						resources);
	}

	@RequestMapping( { "upd" })
	@Action(description = "添加或更新资源URL", execOrder = ActionExecOrder.AFTER, detail = "添加或更新<#assign entity=resourcesService.getById(Long.valueOf(resId))/>【<a href='${ctx}/ibms/oa/system/resourcesUrl/edit.do?resId=${resId}'>${entity.resName}</a>】的URL<#list nameList as item>【${item}】</#list>", exectype = SysAuditExecType.UPDATE_TYPE)
	public void upd(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter out = response.getWriter();
		long resId = RequestUtil.getLong(request, "resId", 0L);
		String[] name = request.getParameterValues("name");
		String[] url = request.getParameterValues("url");
		String nameString = new String();

		if (resId != 0L) {
			List resourcesUrlList = new ArrayList();
			if (name != null) {
				for (int i = 0; i < name.length; i++) {
					ResourcesUrl resUrl = new ResourcesUrl();
					resUrl.setResUrlId(Long.valueOf(UniqueIdUtil.genId()));
					resUrl.setResId(Long.valueOf(resId));
					String nameTemp = name[i];
					if (nameTemp != null)
						nameTemp = nameTemp.trim();
					resUrl.setName(nameTemp);
					String urlTemp = url[i];
					if (urlTemp != null)
						urlTemp = urlTemp.trim();
					resUrl.setUrl(urlTemp);
					resourcesUrlList.add(resUrl);
					try {
						List nameList;
						if (LogThreadLocalHolder.getParamerter("nameList") == null) {
							nameList = new ArrayList();
							LogThreadLocalHolder.putParamerter("nameList",
									nameList);
						} else {
							nameList = (List) LogThreadLocalHolder
									.getParamerter("nameList");
						}
						nameList.add(name[i]);
					} catch (Exception e) {
						e.printStackTrace();
						this.logger.error(e.getMessage());
					}
				}
			}
			LogThreadLocalHolder.putParamerter("name", nameString);
			this.resourcesUrlService.update(resId, resourcesUrlList);
			String defaultUrl = RequestUtil.getSecureString(request,
					"defaultUrl", "");
			if ((defaultUrl != null) && (!defaultUrl.equals(""))) {
				Resources res = (Resources) this.resourcesService.getById(Long
						.valueOf(resId));
				res.setDefaultUrl(defaultUrl.trim());
				this.resourcesService.update(res);
			}
		}

		ResultMessage message = new ResultMessage(1, "编辑资源URL成功");
		out.print(message.toString());
	}
}
