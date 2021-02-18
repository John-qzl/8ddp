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
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.service.InsColTypeService;

@Controller
@RequestMapping({ "/oa/portal/insColType/" })
public class InsColTypeFormController   extends BaseController{

	@Resource
	private InsColTypeService insColTypeService;

	@ModelAttribute("insColType")
	public InsColType processForm(HttpServletRequest request)
	{
		String typeId = request.getParameter("typeId");
		InsColType insColType = null;
		if (StringUtils.isNotEmpty(typeId))
			insColType = (InsColType)this.insColTypeService.getById(Long.valueOf(typeId));
		else {
			insColType = new InsColType();
		}

		return insColType;
	}

	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insColType")  InsColType insColType, BindingResult result)
	{
		if (result.hasFieldErrors()) {
			/*return new JsonResult(false, getErrorMsg(result));*/
			return new JsonResult(false, "false");
		}
		String msg = null;
		String templateName = request.getParameter("tempId_name");
		insColType.setTempName(templateName);
		Date currdate = new Date();
		String userId = UserContextUtil.getCurrentUserId().toString(); 
		
		if (insColType.getTypeId() ==null) {
			insColType.setTypeId(UniqueIdUtil.genId());
			
			String orgId = UserContextUtil.getCurrentOrgId().toString(); 
			insColType.setCreateBy(userId);
			insColType.setUpdateBy(userId);
			insColType.setOrgId(orgId);
			insColType.setUpdateTime(currdate);
			insColType.setUpdateBy(userId);
			insColType.setCreateTime(currdate);
			this.insColTypeService.add(insColType);
			msg = getText("栏目分类成功创建!", new Object[] { insColType.getTypeId() }, "栏目分类成功创建!");
		} else {
			insColType.setUpdateTime(currdate);
			insColType.setUpdateBy(userId);
			this.insColTypeService.update(insColType);
			msg = getText("栏目分类成功更新!", new Object[] { insColType.getTypeId() }, "栏目分类成功更新!");
		}

		return new JsonResult(true, msg);
	}


}
