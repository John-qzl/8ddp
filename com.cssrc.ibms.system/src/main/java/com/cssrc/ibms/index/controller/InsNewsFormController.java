package com.cssrc.ibms.index.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsNews;
import com.cssrc.ibms.index.service.InsNewsService;

/**
 * 新闻公告功能Form操作Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insNews/" })
@Action(ownermodel = SysAuditModelType.NEWS_ANNOUNCE_MANAGEMENT)
public class InsNewsFormController extends BaseController{

	@Resource
	private InsNewsService insNewsService;

	@ModelAttribute("insNews")
	public InsNews processForm(HttpServletRequest request)
	{
		String newId = request.getParameter("newId");
		InsNews insNews = null;
		if (StringUtils.isNotEmpty(newId))
			insNews = (InsNews)this.insNewsService.getById(Long.valueOf(newId));
		else {
			insNews = new InsNews();
		}

		return insNews;
	}
	
	/**
	 * 发布新闻纪录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({"published"})
	@ResponseBody
	@Action(description="发布新闻纪录", execOrder = ActionExecOrder.AFTER, detail="发布新闻纪录  ${SysAuditLinkService.getInsNewsLink(Long.valueOf(newId))}", exectype = SysAuditExecType.ADD_TYPE)
	public JsonResult published(HttpServletRequest request, HttpServletResponse response) { 
		String msg = null;
		String isLongValid = null;
		String issuedColIds = null;
		Date startTime = null;
		Date endTime = null;
		String newIds = request.getParameter("newId");
		String[] pkIds = newIds.split(",");
		for (int i = 0; i < pkIds.length; i++) {
			InsNews insNews = (InsNews)this.insNewsService.getById(Long.valueOf(pkIds[i]));
			if ("Draft".equals(insNews.getStatus())) {
				isLongValid = request.getParameter("isLongValid");
				issuedColIds = request.getParameter("issuedColIds");
				String sStartTime = request.getParameter("startTime");
				String sEndTime = request.getParameter("endTime");
				if (StringUtils.isNotEmpty(sStartTime)) {
					startTime = DateUtil.parseDate(sStartTime);
				}
				if (StringUtils.isNotEmpty(sEndTime)) {
					endTime = DateUtil.parseDate(sEndTime);
				}
				this.insNewsService.doPublish(insNews, issuedColIds, startTime, endTime, isLongValid);
			}
		}

		return new JsonResult(true, msg);
	}
	
	/**
	 * 保存一条新闻公告纪录
	 * @param request
	 * @param insNews
	 * @param result
	 * @return
	 */
	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	@Action(description="添加或更新新闻公告纪录", execOrder = ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>新闻公告纪录${SysAuditLinkService.getInsNewsLink(Long.valueOf(newId))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { InsNews.class }, pkName = "newId")
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insNews")  InsNews insNews, BindingResult result)
	{
		boolean isAdd = true;
		if (result.hasFieldErrors()) {
			return new JsonResult(false, "保存错误！");
		}
		String msg = null;
		String isImg = request.getParameter("isImg");
		
		Date currdate = new Date();
		String userId = UserContextUtil.getCurrentUserId().toString(); 
		
		if (StringUtils.isEmpty(isImg)) {
			insNews.setIsImg("NO");
		}
		if (insNews.getNewId() == null) {
			String orgId = UserContextUtil.getCurrentOrgId().toString();
			
			insNews.setNewId(UniqueIdUtil.genId());
			insNews.setReadTimes(Integer.valueOf(0));
			insNews.setCreateBy(userId);
			insNews.setCreateTime(currdate);
			insNews.setUpdateBy(userId);
			insNews.setUpdateTime(currdate);
			insNews.setOrgId(orgId);
			this.insNewsService.add(insNews);
			msg = getText("新闻公告成功创建!", new Object[] { insNews.getSubject() }, "新闻公告成功创建!");
		} else {
			insNews.setUpdateBy(userId);
			insNews.setUpdateTime(currdate);
			this.insNewsService.update(insNews);
			isAdd = false;
			msg = getText("新闻公告成功更新!", new Object[] { insNews.getSubject() }, "新闻公告成功更新!");
		}
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isAdd));
			LogThreadLocalHolder.putParamerter("newId", insNews.getNewId());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return new JsonResult(true, msg);
	}


}
