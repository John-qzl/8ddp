package com.cssrc.ibms.index.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsPortCol;
import com.cssrc.ibms.index.service.InsPortColService;

@Controller
@RequestMapping({ "/oa/portal/insPortCol/" })
public class InsPortColController extends BaseController{

	@Resource
	private InsPortColService insPortColService;
	
	@RequestMapping({"del"})
	@ResponseBody
	public JsonResult del(HttpServletRequest request, HttpServletResponse response) throws Exception { String uId = request.getParameter("ids");
	if (StringUtils.isNotEmpty(uId)) {
		String[] ids = uId.split(",");
		for (String id : ids) {
			this.insPortColService.delById(Long.valueOf(id));
		}
	}
	return new JsonResult(true, "成功删除！");
	}

	@RequestMapping({"get"})
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String pkId = request.getParameter("pkId");
		InsPortCol insPortCol = null;
		if (StringUtils.isNotEmpty(pkId))
			insPortCol = (InsPortCol)this.insPortColService.getById(Long.valueOf(pkId));
		else {
			insPortCol = new InsPortCol();
		}
		return getAutoView().addObject("insPortCol", insPortCol);
	}

	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String pkId = request.getParameter("pkId");

		String forCopy = request.getParameter("forCopy");
		InsPortCol insPortCol = null;
		if (StringUtils.isNotEmpty(pkId)) {
			insPortCol = (InsPortCol)this.insPortColService.getById(Long.valueOf(pkId));
			if ("true".equals(forCopy))
				insPortCol.setConfId(null);
		}
		else {
			insPortCol = new InsPortCol();
		}
		return getAutoView().addObject("insPortCol", insPortCol);
	}


}
