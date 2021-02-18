package com.cssrc.ibms.core.user.controller.custom;

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

import com.cssrc.ibms.api.custom.model.IAdvancedQuery;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.core.user.model.custom.AdvancedQuery;
import com.cssrc.ibms.core.user.service.UserCustomService;
import com.cssrc.ibms.core.user.service.custom.AdvancedQueryService;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping({"/oa/system/advancedQuery/"})
public class AdvancedQueryController  extends BaseController{
	@Resource
	private AdvancedQueryService service;
	@Resource
	private UserCustomService userCustomService;
	@Resource
    private IDataTemplateService dataTemplateService;
	
	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/custom"))
			viewName = viewName.replace("/oa/system/", "/oa/system/custom/");
		mv.setViewName(viewName);
		return mv;
	}
	@ResponseBody
	@RequestMapping("getAqInfo")
	public JSONArray getAqInfo(HttpServletRequest request){
		String displayId = RequestUtil.getString(request, "displayId");
		List<IAdvancedQuery> list = service.getAdvancedQuery(displayId);		
		return JSONArray.fromObject(list);
	}
	/**
     * 高级查询窗口
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("dialog")
    public ModelAndView advancedQueryDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
    	String displayId = RequestUtil.getString(request, "displayId");
    	String queryKey = RequestUtil.getString(request, "queryKey");
    	
    	IDataTemplate template = dataTemplateService.getById(Long.valueOf(displayId));  	
    	Long tableId = template.getTableId();   	
        IFormTable bpmFormTable = dataTemplateService.getFieldListByTableId(tableId);
        String source =
                (bpmFormTable.getIsExternal() == IFormTable.NOT_EXTERNAL) ? IDataTemplate.SOURCE_CUSTOM_TABLE
                    : IDataTemplate.SOURCE_OTHER_TABLE;
        List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
        AdvancedQuery aq = service.getAdvancedQuery(displayId, queryKey);
        return this.getAutoView()
            .addObject("commonVars", commonVars)
            .addObject("displayId", displayId)
            .addObject("source", source)
            .addObject("bpmFormTable", bpmFormTable)
        	.addObject("condition", JSONArray.fromObject(aq.getCondition()).toString())
        	.addObject("advancedQuery", aq);
    }
    /**
     * 保存高级查询条件到数据库中
     * @param request
     * @param response
     */
	@ResponseBody
    @RequestMapping("save")
    public JsonResult saveAdvancedQuery(HttpServletRequest request, HttpServletResponse response){
    	String queryStr = RequestUtil.getString(request, "advancedQuery");
    	JSONObject obj = JSONObject.fromObject(queryStr);
    	AdvancedQuery aq = AdvancedQuery.toBean(obj);
    	JsonResult result;
    	try{
    		service.save(aq); 
    		result = new JsonResult(true,"",obj);
    	}catch(Exception e){
    		result = new JsonResult(false);
    	}
    	return result;
    };
    @RequestMapping("del")
    public void del(HttpServletRequest request, HttpServletResponse response){
    	String displayId = RequestUtil.getString(request, "displayId");
    	String[] queryKey = RequestUtil.getStringAryByStr(request, "queryKeys");
    	service.del(displayId, queryKey);
    }
}
