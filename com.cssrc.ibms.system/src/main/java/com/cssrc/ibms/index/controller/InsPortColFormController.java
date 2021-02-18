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
import com.cssrc.ibms.index.model.InsPortCol;
import com.cssrc.ibms.index.service.InsPortColService;

@Controller
@RequestMapping({ "/oa/portal/insPortCol/" })
public class InsPortColFormController   extends BaseController{

	@Resource
	private InsPortColService insPortColService;

	@ModelAttribute("insPortCol")
	public InsPortCol processForm(HttpServletRequest request)
	{
		String confId = request.getParameter("confId");
		InsPortCol insPortCol = null;
		if (StringUtils.isNotEmpty(confId))
			insPortCol = (InsPortCol)this.insPortColService.getById(Long.valueOf(confId));
		else {
			insPortCol = new InsPortCol();
		}

		return insPortCol;
	}

	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insPortCol")  InsPortCol insPortCol, BindingResult result)
	{
		if (result.hasFieldErrors()) {
			/*return new JsonResult(false, getErrorMsg(result));*/
			return new JsonResult(false, "false");
		}
		String msg = null;
		Date currdate = new Date();
		String userId = UserContextUtil.getCurrentUserId().toString(); 
		if (insPortCol.getConfId() == null) {
			insPortCol.setConfId(UniqueIdUtil.genId());
			
			String orgId = UserContextUtil.getCurrentOrgId().toString();
			insPortCol.setOrgId(orgId);
			insPortCol.setCreateBy(userId);
			insPortCol.setCreateTime(currdate);
			insPortCol.setUpdateTime(currdate);
			insPortCol.setUpdateBy(userId);
			this.insPortColService.add(insPortCol);
			msg = getText("成功创建!", new Object[] { insPortCol.getConfId() }, "成功创建!");
		} else {
			insPortCol.setUpdateTime(currdate);
			insPortCol.setUpdateBy(userId);
			this.insPortColService.update(insPortCol);
			msg = getText("成功更新!", new Object[] { insPortCol.getConfId() }, "成功更新!");
		}

		return new JsonResult(true, msg);
	}


}
