package com.cssrc.ibms.index.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.cssrc.ibms.index.model.InsColumn;
import com.cssrc.ibms.index.model.InsPortCol;
import com.cssrc.ibms.index.model.InsPortal;
import com.cssrc.ibms.index.service.InsColumnService;
import com.cssrc.ibms.index.service.InsPortColService;
import com.cssrc.ibms.index.service.InsPortalService;


@Controller
@RequestMapping({ "/oa/portal/insPortal/" })
public class InsPortalFormController extends BaseController {


	@Resource
	private InsPortalService insPortalService;

	@Resource
	private InsPortColService insPortColService;

	@Resource
	private InsColumnService insColumnService;

	@ModelAttribute("insPortal")
	public InsPortal processForm(HttpServletRequest request)
	{
		String portId = request.getParameter("portId");
		InsPortal insPortal = null;
		if (StringUtils.isNotEmpty(portId))
			insPortal = (InsPortal)this.insPortalService.getById(Long.valueOf(portId));
		else {
			insPortal = new InsPortal();
		}
		return insPortal;
	}
	
	@RequestMapping(value={"savePortal"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult savePortal(HttpServletRequest request, @ModelAttribute("insPortal")  InsPortal insPortal, BindingResult result)
	{
		InsPortal insPortal1 = insPortal;
		String userId = UserContextUtil.getCurrentUserId().toString();
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Date currdate = new Date();
		String key = insPortal.getKey();
		String pageInfo = request.getParameter("data");
		JSONObject page = JSONObject.fromObject(pageInfo);
		JSONArray layoutInfo = page.getJSONArray("layout");
		if ((key.equals("GLOBAL-ORG")) || (key.equals("ORG")))
		{
			if (key.equals("GLOBAL-ORG")) {
				insPortal = new InsPortal();
				insPortal.setPortId(UniqueIdUtil.genId());
				insPortal.setKey("ORG");
				insPortal.setName("公司");
				insPortal.setCreateBy(userId);
				insPortal.setCreateTime(currdate);
				insPortal.setUpdateTime(currdate);
				insPortal.setUpdateBy(userId);
				insPortal.setIsDefault("NO");
				insPortal.setOrgId(orgId);
				insPortal.setColNums(insPortal1.getColNums());
				insPortal.setColWidths(insPortal1.getColWidths());
				insPortal.setLayoutInfo(layoutInfo.toString());
				this.insPortalService.add(insPortal);
			}

		}
		else if (key.equals("GLOBAL-PERSONAL")) {
			insPortal = new InsPortal();
			insPortal.setPortId(UniqueIdUtil.genId());
			insPortal.setKey("PERSONAL");
			insPortal.setName("个人");
			insPortal.setIsDefault("NO");
			insPortal.setUserId(userId);
			insPortal.setCreateBy(userId);
			insPortal.setCreateTime(currdate);
			insPortal.setUpdateTime(currdate);
			insPortal.setUpdateBy(userId);
			insPortal.setColNums(insPortal1.getColNums());
			insPortal.setColWidths(insPortal1.getColWidths());
			insPortal.setLayoutInfo(layoutInfo.toString());
			this.insPortalService.add(insPortal);
		}else {
			insPortal1.setLayoutInfo(layoutInfo.toString());
			this.insPortalService.update(insPortal1);
		}

		String msg = null;
		String pans = request.getParameter("data");
		JSONArray array = JSONArray.fromObject(pans);
		for (int i = 0; i < array.size(); i++) {
			JSONObject a = array.getJSONObject(i);
			Long colId=Long.valueOf(a.getString("id"));
			InsColumn insColumn = (InsColumn)this.insColumnService.getById(colId);
			InsPortCol insPortCol = this.insPortColService.getByPortCol(insPortal.getPortId(), Long.valueOf(insColumn.getColId()));
			if (insPortCol != null) {
				InsPortCol insPC = insPortCol;
				//insPC.setSn(a.getInt("sn"));
				//insPC.setColNum(a.getInt("column"));
				//insPC.setHeight(insColumn.getHeight());
				insPC.setUpdateBy(userId);
				insPC.setUpdateTime(currdate);
				this.insPortColService.update(insPC);
			} else {
				InsPortCol insPC = new InsPortCol(insPortal);
				insPC.setColId(Long.valueOf(insColumn.getColId()));
				//insPC.setSn(a.getInt("sn"));
				//insPC.setColNum(a.getInt("column"));
				//insPC.setHeight(insColumn.getHeight());
				insPC.setHeightUnit("px");
				insPC.setConfId(UniqueIdUtil.genId());
				this.insPortColService.add(insPC);

			}
		}

		return new JsonResult(true, msg);
	}
	
	/**
	 * 保存布局样式
	 * @param request
	 * @param insPortal
	 * @param result
	 * @return
	 */
	@RequestMapping(value={"saveGlobal"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult saveGlobal(HttpServletRequest request, @ModelAttribute("insPortal")  InsPortal insPortal, BindingResult result)
	{
		String msg = null;
		
		//更新布局信息
		String pans = request.getParameter("data");
		String name = request.getParameter("name");
		String priority = request.getParameter("priority");
		String desc = request.getParameter("desc");
		JSONObject page = JSONObject.fromObject(pans);
		JSONArray layoutInfo = page.getJSONArray("layout");
		
		insPortal.setName(name);
		insPortal.setDesc(desc);
		insPortal.setLayoutInfo(layoutInfo.toString());
		insPortalService.update(insPortal);
				
		String userId = UserContextUtil.getCurrentUserId().toString();
		Date currdate = new Date();
		
		for (int i = 0; i < layoutInfo.size(); i++) {
			JSONObject a = layoutInfo.getJSONObject(i);
			InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(a.getString("id")));
			InsPortCol insPortCol = this.insPortColService.getByPortCol(insPortal.getPortId(), Long.valueOf(insColumn.getColId()));
			if (insPortCol != null) {
				InsPortCol insPC = insPortCol;
				//insPC.setSn(a.getInt("sn"));
				//insPC.setColNum(a.getInt("column"));
				//insPC.setHeight(insColumn.getHeight());
				insPC.setUpdateBy(userId);
				insPC.setUpdateTime(currdate);
				this.insPortColService.update(insPC);
			} else {
				InsPortCol insPC = new InsPortCol(insPortal);
				insPC.setColId(Long.valueOf(insColumn.getColId()));
				//insPC.setSn(a.getInt("sn"));
				//insPC.setColNum(a.getInt("column"));
				//insPC.setHeight(insColumn.getHeight());
				insPC.setHeightUnit("px");
				insPC.setConfId(UniqueIdUtil.genId());
				this.insPortColService.add(insPC);
			}
		}

		/*msg = getMessage("insPortal.updated", new Object[] { insPortal.getName() }, "门户成功更新!");*/
		return new JsonResult(true, msg);
	}
	
	/**
	 * 保存添加的栏目和栏目宽、列信息
	 * @param request
	 * @param insPortal
	 * @param result
	 * @return
	 */
	@RequestMapping(value={"save"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	@ResponseBody
	public JsonResult save(HttpServletRequest request, @ModelAttribute("insPortal")  InsPortal insPortal, BindingResult result)
	{
		if (result.hasFieldErrors()) {
			/*return new JsonResult(false, getErrorMsg(result));*/
			return new JsonResult(false,"失败！");
		}
		String msg = null;

		String column = request.getParameter("insPortCols");
		List<Map<String,Object>> layoutInfo = new ArrayList<Map<String,Object>>();
		JSONArray jarray = new JSONArray();
		if (StringUtils.isNotEmpty(column)) {
			String[] columns = column.split(",");
			int startIndex = columns.length;
			for (int i = 0; i < columns.length; i++) {
				
				JSONObject sublayout = new JSONObject();
				sublayout.put("id", columns[i]);
				sublayout.put("col", 1);
				if((i+1)%2==0){
					sublayout.put("col", 6);
				}
				sublayout.put("row", 1);
				sublayout.put("size_x", 5);
				sublayout.put("size_y", 6);
				layoutInfo.add(sublayout);
				
				InsPortCol portCol = this.insPortColService.getByPortCol(insPortal.getPortId(), Long.valueOf(columns[i]));
				if (portCol != null) {
					continue;
				}
				InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(columns[i]));
				InsPortCol portcol = new InsPortCol(insPortal);
				portcol.setColId(Long.valueOf(columns[i]));
				portcol.setPortId(insPortal.getPortId());
				portcol.setConfId(UniqueIdUtil.genId());
				portcol.setHeight(insColumn.getHeight());
				portcol.setHeightUnit("px");
				portcol.setSn((startIndex + i));
				portcol.setColNum(Integer.valueOf(0));
				this.insPortColService.add(portcol);
			}

			ArrayList<String> arrOldCol = new ArrayList<String>();
			ArrayList<String> arrDelCol = new ArrayList<String>();
			List<InsPortCol> inspc = this.insPortColService.getGlobalPortal(insPortal.getPortId());
			for (InsPortCol pc : inspc) {
				InsColumn insColumn = insColumnService.getById(Long.valueOf(pc.getColId()));
				if (insColumn != null) {
					arrOldCol.add(insColumn.getColId().toString());
				}
			}
			arrDelCol = arrContrast(arrOldCol, columns);
			for (String delColId : arrDelCol)
			{
				this.insPortColService.delByPortCol(insPortal.getPortId(), Long.valueOf(delColId));
			}
		}

		insPortal.setLayoutInfo(layoutInfo.toString());
		this.insPortalService.update(insPortal);
		msg = getText("门户成功更新!", new Object[] { insPortal.getName() }, "门户成功更新!");
		return new JsonResult(true, msg);
	}
	
	/**
	 * 返回arr1中arr2没有的元素
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private static ArrayList<String> arrContrast(ArrayList<String> arr1, String[] arr2)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (String str : arr1) {
			if (!list.contains(str)) {
				list.add(str);
			}
		}
		for (String str : arr2) {
			if (list.contains(str)) {
				list.remove(str);
			}
		}
		return list;
	}
}
