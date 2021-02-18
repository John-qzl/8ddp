package com.cssrc.ibms.core.user.controller;

import com.cssrc.ibms.api.sysuser.intf.ICurUserService;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.user.model.SysObjRights;
import com.cssrc.ibms.core.user.service.SysObjRightsService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.string.StringUtil;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * 
 * 
 * <p>Title:SysObjRightsController</p>
 * @author YangBo 
 * @date 2016-7-26上午09:37:56
 */
@Controller
@RequestMapping({"/oa/system/sysObjRights/"})
public class SysObjRightsController extends BaseController
{

	@Resource
	private SysObjRightsService sysObjRightsService;

	@RequestMapping({"save"})
	@Action(description="添加或更新对象权限表")
	public void save(HttpServletRequest request, HttpServletResponse response)
	throws Exception
 {
		try {
			String objType = RequestUtil.getString(request, "objType");
			String objectId = RequestUtil.getString(request, "objectId");

			if ((StringUtil.isNotEmpty(objectId))
					&& (StringUtil.isNotEmpty(objType))) {
				this.sysObjRightsService.deleteByObjTypeAndObjectId(objType,
						objectId);
			}

			String sysObjRightsStr = RequestUtil.getString(request,
					"sysObjRights");

			JSONArray jsonArray = JSONArray.fromObject(sysObjRightsStr);
			for (Object obj : jsonArray) {
				SysObjRights sysObjRights = (SysObjRights) JSONObject.toBean(
						(JSONObject) obj, SysObjRights.class);
				this.sysObjRightsService.save(sysObjRights);
			}

			writeResultMessage(response.getWriter(), "权限更改成功", 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(),
					"权限更改失败," + e.getMessage(), 0);
		}
	}

	@RequestMapping({"list"})
	@Action(description="查看对象权限表分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List list = this.sysObjRightsService.getAll(new QueryFilter(request, "sysObjRightsItem"));
		ModelAndView mv = getAutoView().addObject("sysObjRightsList", list);
		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除对象权限表")
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysObjRightsService.delByIds(lAryId);
			message = new ResultMessage(1, "删除对象权限表成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl); } 
	@RequestMapping({"getRightType"})
	@Action(description="获取这个类型的授权类型列表")
	@ResponseBody
	public JSONArray getRightType(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		String beanId = RequestUtil.getString(request, "beanId");
	if (StringUtil.isEmpty(beanId)) {
		return new JSONArray();
	}
	List<ICurUserService> list = (List)AppUtil.getBean(beanId);
	JSONArray ja = new JSONArray();
	for (ICurUserService ics : list) {
		JSONObject jo = new JSONObject();
		jo.put("key", ics.getKey());
		jo.put("value", ics.getTitle());
		ja.add(jo);
	}
	return ja; } 
	@RequestMapping({"getObject"})
	@Action(description="按各种参数查询权限")
	@ResponseBody
	public Object getObject(HttpServletRequest request, HttpServletResponse response) throws Exception { String objType = RequestUtil.getString(request, "objType");
	String objectId = RequestUtil.getString(request, "objectId");
	if ((StringUtil.isNotEmpty(objectId)) && (StringUtil.isNotEmpty(objType))) {
		List list = this.sysObjRightsService.getByObjTypeAndObjectId(objType, objectId);
		return this.sysObjRightsService.getByObjTypeAndObjectId(objType, objectId);
	}
	return null;
	}
}

