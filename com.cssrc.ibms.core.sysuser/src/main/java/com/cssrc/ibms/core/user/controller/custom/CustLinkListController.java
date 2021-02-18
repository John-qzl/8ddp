package com.cssrc.ibms.core.user.controller.custom;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.user.model.custom.CustLinkLists;
import com.cssrc.ibms.core.user.service.custom.CustLinkListService;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping({"/oa/system/CustLinkList"})
public class CustLinkListController extends BaseController{
	@Resource
	private CustLinkListService service;

	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/custom"))
			viewName = viewName.replace("/oa/system/", "/oa/system/custom/");
		mv.setViewName(viewName);
		return mv;
	}
	/**
     * 配置窗口
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("dialog")
    public ModelAndView listConfsDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {    	
    	CustLinkLists cLists = service.getCustLinkLists();
    	if(cLists==null || cLists.getConfs().size()==0){
    		cLists = service.getCustLinkLists(ISysUser.IMPLEMENT_USER);
		}
    	String json = JSONObject.fromObject(cLists).toString();
        return this.getAutoView().addObject("custLinkLists",json);
    }
	
	/**
     * 添加/保存
     * @param request
     * @param response
     * @throws Exception
     */
	@RequestMapping("save")
	public void saveListConfs(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String custLinkListsStr = RequestUtil.getString(request, "custLinkLists");
		JSONObject obj = JSONObject.fromObject(custLinkListsStr);
		CustLinkLists custLinkLists = CustLinkLists.toBean(obj);
		ResultMessage result = null;
		try{
			service.save(custLinkLists);
			result = new ResultMessage(ResultMessage.Success,"保存成功!");
		}catch(Exception e){
			result = new ResultMessage(ResultMessage.Fail,"保存失败!");
		}
		writeResultMessage(response.getWriter(),result);
	}
}
