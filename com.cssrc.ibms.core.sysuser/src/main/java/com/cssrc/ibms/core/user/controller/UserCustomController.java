package com.cssrc.ibms.core.user.controller;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.user.model.UserCustom;
import com.cssrc.ibms.core.user.service.UserCustomService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
@Controller
@RequestMapping({"/oa/system/userCustom/"})
public class UserCustomController  extends BaseController{
	@Resource
	private UserCustomService userCustomService;
	@Resource
    private IDataTemplateService dataTemplateService;
	/**
	 * 列表查询-获取设置信息
     * queryField格式：[{name:col1,desc:"字段1",checked:true/false},...];
     */
    @RequestMapping("getQuerySetInfo")
    public void getQuerySetInfo(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	   	
    	String queryField = "";
	    Long displayId = RequestUtil.getLong(request, "displayId");
	    queryField = (new UserCustom()).getQueryFieldInfo(displayId,UserContextUtil.getCurrentUserId());
	    if(BeanUtils.isEmpty(queryField)){
	    	JSONArray rtn = userCustomService.getQueryField(displayId,null);
	    	queryField = rtn.toString();
	    }
	    queryField = userCustomService.dealQueryField(displayId,queryField);
	    if(StringUtil.isNotEmpty(queryField)){
	        response.getWriter().write(queryField);
	    }
	    
    }
	/**
	 * 列表查询-弹框
	 */
    @RequestMapping("querySetDialog")
    public ModelAndView querySetDialog(HttpServletRequest request, HttpServletResponse response) 
    throws Exception{
    	 String querySetInfo = RequestUtil.getString(request, "querySetInfo");
    	 querySetInfo = URLDecoder.decode(querySetInfo,"UTF-8");
    	 ModelAndView mv = getAutoView();
         return mv.addObject("querySetInfo",querySetInfo);
    }
	/**
	 * 列表查询-用户自定义设置信息保存
	 */
	@RequestMapping({"saveQuerySetInfo"})
	@Action(description="")
	public void saveQuerySetInfo(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long displayId = RequestUtil.getLong(request, "displayId");
		String querySetInfo = RequestUtil.getString(request, "querySetInfo");
		String resultMsg = null;
		try {
			resultMsg = "用户自定义查询信息设置成功！";
			JSONArray rtn = userCustomService.getQueryField(displayId,querySetInfo);
			(new UserCustom()).setQueryFieldInfo(displayId,UserContextUtil.getCurrentUserId(), rtn);
			userCustomService.save(UserContextUtil.getCurrentUserId());
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			resultMsg = "用户自定义查询信息设置失败！";
			e.printStackTrace();
			writeResultMessage(response.getWriter(), resultMsg, 0);
		}
	}
	
	/**
	 * 列表查询-获取设置信息
     * displayField格式：[{fieldName:col1,fieldDesc:"字段1",isDisplay:true/false},...];
     */
    @RequestMapping("getDisplaySetInfo")
    public void getDisplaySetInfo(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
    	String displayField = "";
	    Long displayId = RequestUtil.getLong(request, "displayId");
	    displayField = (new UserCustom()).getDisplayFieldInfo(displayId,UserContextUtil.getCurrentUserId());
	    if(BeanUtils.isEmpty(displayField)){
	    	JSONArray rtn = userCustomService.getDisplayField(displayId,null);
	    	displayField = rtn.toString();
	    }
	    displayField = userCustomService.dealDisplayField(displayId,displayField);
	    if(displayField!=null){
	        response.getWriter().write(displayField);
	    }
    }
	/**
	 * 列表显示列-用户自定义设置信息保存
	 */
	@RequestMapping({"saveDisplaySetInfo"})
	@Action(description="")
	public void saveDisplaySetInfo(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long displayId = RequestUtil.getLong(request, "displayId");
		String displaySetInfo = RequestUtil.getString(request, "displaySetInfo");
		String resultMsg = null;
		try {
			resultMsg = "用户自定义显示列信息设置成功！";
			JSONArray rtn = userCustomService.getDisplayField(displayId,displaySetInfo);
			(new UserCustom()).setDisplayFieldInfo(displayId,UserContextUtil.getCurrentUserId(), rtn);
			userCustomService.save(UserContextUtil.getCurrentUserId());
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			resultMsg = "用户自定义显示列信息设置失败！";
			e.printStackTrace();
			writeResultMessage(response.getWriter(), resultMsg, 0);
		}
	}
	/**
	 * 列值过滤-弹框
	 */
    @RequestMapping("lvfField")
    public void lineVlueFilterField(HttpServletRequest request, HttpServletResponse response) 
    throws Exception{
    	Long dsiplayId = RequestUtil.getLong(request, "displayId");
    	String fieldName = RequestUtil.getString(request, "fieldName");
    	JSONObject lvfObj = userCustomService.getLvfField(dsiplayId,fieldName);
    	response.getWriter().write(lvfObj.toString());
    }
    /**
     * 获取业务数据模板中的导出字段信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("getExportField")
    public void getExportField(HttpServletRequest request, HttpServletResponse response)throws Exception{
    	Long dsiplayId = RequestUtil.getLong(request, "displayId");
    	JSONArray exportField = userCustomService.getExportField(dsiplayId);
    	response.getWriter().write(exportField.toString());   	
    }
}
