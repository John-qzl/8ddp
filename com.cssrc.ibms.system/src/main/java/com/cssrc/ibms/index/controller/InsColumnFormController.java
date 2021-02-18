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
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.model.InsColumn;
import com.cssrc.ibms.index.model.InsPortCol;
import com.cssrc.ibms.index.model.InsPortal;
import com.cssrc.ibms.index.service.InsColTypeService;
import com.cssrc.ibms.index.service.InsColumnService;
import com.cssrc.ibms.index.service.InsPortColService;
import com.cssrc.ibms.index.service.InsPortalService;

@Controller
@RequestMapping({ "/oa/portal/insColumn/" })
public class InsColumnFormController  extends BaseController{

	@Resource
	private InsColumnService insColumnService;

	@Resource
	private InsColTypeService insColTypeService;

	@Resource
	private InsPortalService insPortalService;
	
	@Resource
	private InsPortColService insPortColService;

	@ModelAttribute("insColumn")
	public InsColumn processForm(HttpServletRequest request)
	{
		String colId = request.getParameter("colId");
		InsColumn insColumn = null;
		if (StringUtils.isNotEmpty(colId))
			insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(colId));
		else {
			insColumn = new InsColumn();
		}

		return insColumn;
	}

	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insColumn")  InsColumn insColumn, BindingResult result)
	{
		String colTypeId = request.getParameter("colType");
		if (result.hasFieldErrors()) {
			/*return new JsonResult(false, getErrorMsg(result));*/
			return new JsonResult(false, "false");
		}
		String userId = UserContextUtil.getCurrentUserId().toString();
		Date currdate = new Date();
		String msg = null;
		if (insColumn.getColId() == null) {
			InsColType insColType = (InsColType)this.insColTypeService.getById(Long.valueOf(colTypeId));
			String orgId = UserContextUtil.getCurrentOrgId().toString();
			insColumn.setTypeId(insColType.getTypeId());
			insColumn.setColId(UniqueIdUtil.genId());
			insColumn.setColType(insColType.getName());
			insColumn.setCreateBy(userId);
			insColumn.setCreateTime(currdate);
			insColumn.setUpdateBy(userId);
			insColumn.setOrgId(orgId);
			insColumn.setUpdateTime(currdate);
			this.insColumnService.add(insColumn);
			msg = getText("栏目成功创建!", new Object[] { insColumn.getName() }, "栏目成功创建!");
		} else {
			InsColType insColType = (InsColType)this.insColTypeService.getById(Long.valueOf(colTypeId));
			insColumn.setColType(insColType.getName());
			insColumn.setUpdateBy(userId);
			insColumn.setUpdateTime(currdate);
			this.insPortColService.updateHeight(insColumn.getHeight(), Long.valueOf(insColumn.getColId()));
			this.insColumnService.update(insColumn);
			msg = getText("栏目成功更新!", new Object[] { insColumn.getName() }, "栏目成功更新!");
		}

		return new JsonResult(true, msg);
	}

	@RequestMapping(value={"saveByPort"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult saveByPort(HttpServletRequest request, @ModelAttribute("insColumn")  InsColumn insColumn, BindingResult result)
	{
		String colTypeId = request.getParameter("colType");
		Long portId = RequestUtil.getLong(request,"portId",0L);
		if (result.hasFieldErrors()) {
			/*return new JsonResult(false, getErrorMsg(result));*/
			return new JsonResult(false, "false");
		}
		String msg = null;
		String userId = UserContextUtil.getCurrentUserId().toString();
		Date currdate = new Date();
		
		InsPortal insPortal = (InsPortal)this.insPortalService.getById(portId);
		if (insColumn.getColId() ==null) {
			InsColType insColType = (InsColType)this.insColTypeService.getById(Long.valueOf(colTypeId));
			String orgId = UserContextUtil.getCurrentOrgId().toString();
			insColumn.setTypeId(insColType.getTypeId());
			insColumn.setColId(UniqueIdUtil.genId());
			insColumn.setColType(insColType.getName());
			insColumn.setCreateBy(userId);
			insColumn.setCreateTime(currdate);
			insColumn.setUpdateBy(userId);
			insColumn.setOrgId(orgId);
			this.insColumnService.add(insColumn);
			msg = getText("栏目成功创建!", new Object[] { insColumn.getName() }, "栏目成功创建!");
		}

		InsPortCol portcol = new InsPortCol(insPortal);
		portcol.setConfId(UniqueIdUtil.genId());
		portcol.setPortId(portId);
		portcol.setColId(Long.valueOf(insColumn.getColId()));
		portcol.setHeight(insColumn.getHeight());
		portcol.setHeightUnit("px");
		portcol.setSn(Integer.valueOf(99));
		portcol.setColNum(Integer.valueOf(0));
		insPortColService.add(portcol);
		this.insPortalService.update(insPortal);

		return new JsonResult(true, msg);
	}


}
