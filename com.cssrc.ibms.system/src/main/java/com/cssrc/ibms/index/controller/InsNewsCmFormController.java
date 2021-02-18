package com.cssrc.ibms.index.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsNewsCm;
import com.cssrc.ibms.index.service.InsNewsCmService;
import com.cssrc.ibms.index.service.InsNewsService;


/**
 * 新闻评论内容管理Form操作Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insNewsCm/" })
@Action(ownermodel = SysAuditModelType.NEWS_COMMENT_MANAGEMENT)
public class InsNewsCmFormController  extends BaseController{

	@Resource
	private InsNewsCmService insNewsCmService;

	@Resource
	private InsNewsService insNewsService;
	
	/**
	 * 获取新闻评论对象
	 * @param request
	 * @return
	 */
	@ModelAttribute("insNewsCm")
	@Action(description = "获取新闻评论对象",detail="获取新闻评论对象", exectype = SysAuditExecType.SELECT_TYPE)
	public InsNewsCm processForm(HttpServletRequest request)
	{
		String commId = request.getParameter("commId");
		InsNewsCm insNewsCm = null;
		if (StringUtils.isNotEmpty(commId))
			insNewsCm = (InsNewsCm)this.insNewsCmService.getById(Long.valueOf(commId));
		else {
			insNewsCm = new InsNewsCm();
		}

		return insNewsCm;
	}
	
	/**
	 * 发表一个新闻评论
	 * @param request
	 * @param insNewsCm
	 * @param result
	 * @return
	 */
	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	@Action(description="发表一个新闻评论", execOrder = ActionExecOrder.AFTER, detail="发表一个新闻评论  ${SysAuditLinkService.getInsNewsCmLink(Long.valueOf(cmId))}", exectype = SysAuditExecType.ADD_TYPE)
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insNewsCm")  InsNewsCm insNewsCm, BindingResult result)
	{
		/*String code = (String)request.getSession().getAttribute("KAPTCHA_SESSION_KEY");
		
		String validCode = request.getParameter("validCode");
		if ((code == null) || (!code.equals(validCode))) {
			return new JsonResult(false, "验证码不正确！");
		}*/

		String isReply = request.getParameter("reply");
		Long newId = RequestUtil.getLong(request,"newId");
		Long cmId = RequestUtil.getLong(request,"cmId");
		String msg = null;
		
		Date currdate = new Date();
		String userId = UserContextUtil.getCurrentUserId().toString(); 
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		insNewsCm.setCreateBy(userId);
		insNewsCm.setCreateTime(currdate);
		insNewsCm.setUpdateTime(currdate);
		insNewsCm.setUpdateBy(userId);
		insNewsCm.setOrgId(orgId);
		
		if ("no".equals(isReply)) {
			insNewsCm.setCommId(UniqueIdUtil.genId());
			insNewsCm.setAgreeNums(Integer.valueOf(0));
			insNewsCm.setNewId(newId);
			insNewsCm.setIsReply(isReply);
			insNewsCm.setRefuseNums(Integer.valueOf(0));
			this.insNewsCmService.add(insNewsCm);
			msg = getText("评论成功回复!", new Object[] { insNewsCm.getCommId() }, "评论成功回复!");
		} else if ("yes".equals(isReply)) {
			insNewsCm.setCommId(UniqueIdUtil.genId());
			insNewsCm.setAgreeNums(Integer.valueOf(0));
			insNewsCm.setNewId(newId);
			insNewsCm.setIsReply(isReply);
			insNewsCm.setRepId(cmId);
			insNewsCm.setRefuseNums(Integer.valueOf(0));
			this.insNewsCmService.add(insNewsCm);
			msg = getText("评论成功回复!", new Object[] { insNewsCm.getCommId() }, "评论成功回复!");
		}

		return new JsonResult(true, msg);
	}

}
