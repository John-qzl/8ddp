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
import com.cssrc.ibms.core.user.model.custom.ListConfs;
import com.cssrc.ibms.core.user.service.UserCustomService;
import com.cssrc.ibms.core.user.service.custom.ListConfsService;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping({"/oa/system/listConfs/"})
public class ListConfsController  extends BaseController{
	@Resource
	private ListConfsService service;
	@Resource
	private UserCustomService userCustomService;
	
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
    	ListConfs confs = service.getListConfs();
    	if(confs==null || confs.getConfs().size()==0){
    		confs = service.getListConfs(ISysUser.IMPLEMENT_USER);
		}
    	JSONArray arr = service.getDatatemplate();
    	String json = JSONObject.fromObject(confs).toString();
        return this.getAutoView().addObject("listConfs",json)
        		.addObject("dataTemplates",arr);
    }
    /**
     * @param request
     * @param response
     */
    @RequestMapping("save")
    public void saveListConfs(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	String listConfsStr = RequestUtil.getString(request, "listConfs");
    	JSONObject obj = JSONObject.fromObject(listConfsStr);
    	ListConfs conf = ListConfs.toBean(obj);
    	ResultMessage result= null;
    	try{
    		service.save(conf);
    		result = new ResultMessage(ResultMessage.Success,"保存成功！");
    	}catch(Exception e){
    		result = new ResultMessage(ResultMessage.Fail,"保存失败！");
    	}
    	writeResultMessage(response.getWriter(), result);
    };
    @RequestMapping("del")
    public void del(HttpServletRequest request, HttpServletResponse response){
    	service.del();
    }
}
