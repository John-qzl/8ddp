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
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColNew;
import com.cssrc.ibms.index.service.InsColNewService;
import com.cssrc.ibms.index.service.InsColumnService;
import com.cssrc.ibms.index.service.InsNewsService;

/**
 * 栏目新闻关联信息form操作Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insColNew/" })
public class InsColNewFormController  extends BaseController{


	@Resource
	private InsColNewService insColNewService;

	@Resource
	private InsColumnService insColumnService;

	@Resource
	private InsNewsService insNewsService;
	
	/**
	 * 获取一条新闻栏目纪录
	 * @param request
	 * @return
	 */
	@ModelAttribute("insColNew")
	public InsColNew processForm(HttpServletRequest request)
	{
		String id = request.getParameter("id");
		InsColNew insColNew = null;
		if (StringUtils.isNotEmpty(id))
			insColNew = (InsColNew)this.insColNewService.getById(Long.valueOf(id));
		else {
			insColNew = new InsColNew();
		}

		return insColNew;
	}
	
	/**
	 * 保存提交新闻栏目纪录
	 * @param request
	 * @param insColNew
	 * @param result
	 * @return
	 */
	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insColNew") 
							InsColNew insColNew, BindingResult result)
	{
		if (result.hasFieldErrors()) {
			return new JsonResult(false, "保存错误");
		}
		String msg = null;
		Date currdate = new Date();
		String userId = UserContextUtil.getCurrentUserId().toString(); 
		insColNew.setUpdateBy(userId);
		insColNew.setUpdateTime(currdate);
		
		if (insColNew.getId() ==null) {
			String orgId = UserContextUtil.getCurrentOrgId().toString();
			
			insColNew.setId(UniqueIdUtil.genId());
			insColNew.setCreateBy(userId);
			insColNew.setCreateTime(currdate);
			insColNew.setOrgId(orgId);
			this.insColNewService.add(insColNew);
			msg = getText("成功创建!", new Object[] { insColNew.getId() }, "成功创建!");
		} else {
			this.insColNewService.update(insColNew);
			msg = getText("成功更新!", new Object[] { insColNew.getId() }, "成功更新!");
		}
		return new JsonResult(true, msg);
	}
	
	/**
	 * 发布，更新实时新闻
	 * @param request
	 * @param insColNew
	 * @param result
	 * @return
	 */
	@RequestMapping(value={"saveTime"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult saveTime(HttpServletRequest request, @ModelAttribute("insColNew")  
								InsColNew insColNew, BindingResult result) 
	{ 	
		Date currdate = new Date();
		String userId = UserContextUtil.getCurrentUserId().toString(); 
		insColNew.setUpdateBy(userId);
		insColNew.setUpdateTime(currdate);
		
		this.insColNewService.update(insColNew);
		return new JsonResult(true, "成功更新！");
	}

}
	