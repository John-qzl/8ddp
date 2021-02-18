package com.cssrc.ibms.core.flow.controller.pendingtask;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.flow.service.AlreadyTaskTemplateService;
import com.cssrc.ibms.core.flow.service.PendingTaskTemplateService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

@Controller
@RequestMapping("/oa/flow/userTask/")
public class UserTaskController extends BaseController
{
    private static String category = "userTask";
    
    private static Logger logger = Logger.getLogger(PendingSettingController.class);
    
    @Resource
    AlreadyTaskTemplateService alreadyTaskTemplateService;
    
    @Resource
    PendingTaskTemplateService pendingTaskTemplateService;
    
    /**
     * 查看已办事宜流程列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("alreadyMatters")
    public ModelAndView alreadyMatters(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = this.getView(category, "alreadyMatters");
        return mv;
    }
    
    /**
     * 查看已办事宜流程列表
     * 
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping("alreadyMattersList")
    @Action(description = "查看已办事宜流程列表")
    public ModelAndView alreadyMattersList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = this.getView(category, "alreadyMattersList");
        // 取得当前页URL,如有参数则带参数
        boolean ajax = RequestUtil.getBoolean(request, "ajax");
        Map<String, Object> params = RequestUtil.getQueryMap(request);
        Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
        for (String key : queryParams.keySet())
        {
            params.put(key, queryParams.get(key));
        }
        String html = alreadyTaskTemplateService.pareTemplateMatters(params);
        if (StringUtil.isEmpty(html))
        {
            mv = new ModelAndView("redirect:/oa/flow/processRun/alreadyMattersList.do?defId=" + params.get("defId"));
            return mv;
        }
        if (ajax)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", true);
            map.put("html", html);
            response.getWriter().print(JSONObject.toJSON(map));
            return null;
        }
        else
        {
            return mv.addObject("html", html);
        }
    }
    
    /**
     * 查看代办任务列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("pendingMatters")
    public ModelAndView pendingMatters(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = this.getView(category, "pendingMatters");
        return mv;
    }
    /**
     * 导出所有待办任务详情
     * 获取查询参数，设置不分页，得到模板解析后的html，再对html进行处理，获取其table中的数据
     */
	@RequestMapping("downloadAllPendingMatters")
    public void downloadAllPendingMatters(HttpServletRequest request, HttpServletResponse response){
    	this.downloadAllMatters("pending",request,response);	
    }
	 /**
     * 导出所有已办任务详情
     * 获取查询参数，设置不分页，得到模板解析后的html，再对html进行处理，获取其table中的数据
     */
	@RequestMapping("downloadAllAlreadyMatters")
    public void downloadAllAlreadyMatters(HttpServletRequest request, HttpServletResponse response){
    	this.downloadAllMatters("already",request,response);	
    }
	@SuppressWarnings("unchecked")
    private void downloadAllMatters(String type,HttpServletRequest request, HttpServletResponse response){
    	 Map<String, Object> params = RequestUtil.getQueryMap(request);
         Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
         for (String key : queryParams.keySet())
         {
             params.put(key, queryParams.get(key));
         }
         params.put("download_No_Page", "false");
         String html ="";
         String fileName="";
         if("pending".equals(type)){
        	 html = pendingTaskTemplateService.pareTemplateMatters(params);
        	 fileName="全部待办任务详情";
         }else if("already".equals(type)){//"already"
        	 html=alreadyTaskTemplateService.pareTemplateMatters(params);
        	 fileName="全部已办任务详情";
         }else{
        	 return ;
         }
         html=html.replaceAll("&", "");
//         html=html.replaceAll("&nbsp;", "");
         try {
			Document data=DocumentHelper.parseText(html);
			//header
			List<Element> thead = data.getRootElement().selectNodes(".//thead");
			String[] headers = null;
			if(thead!=null&&thead.size()>0){
				Element header=thead.get(0);
				List<Element> th=header.selectNodes(".//th");
				headers=new String[th.size()-1];
				for(int i=1;i<th.size();i++){
					headers[i-1]=th.get(i).getStringValue().trim();
				}
			}
			//tbody
			List<Element> tbody= data.getRootElement().selectNodes(".//tbody");
			List<Object[]> list = null;
			if(tbody!=null&&tbody.size()>0){
				Element content=tbody.get(0);
				list=new ArrayList<Object[]>();
				List<Element> trs=content.selectNodes(".//tr");
				for(Element tr:trs){
					List<Element> tds=tr.selectNodes(".//td");
					Object[] objs=new Object[tds.size()-1];
					for(int i=1;i<tds.size();i++){
						objs[i-1]=tds.get(i).getStringValue().trim();
					}
					list.add(objs);
				}
			}
			ExcelUtil.exportToExcel(fileName, headers, list, response);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }
    /**
     * 查看代办任务列表
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("pendingMattersList")
    public ModelAndView pendingMattersList(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = this.getView(category, "pendingMattersList");
        // 取得当前页URL,如有参数则带参数
        boolean ajax = RequestUtil.getBoolean(request, "ajax");
        Map<String, Object> params = RequestUtil.getQueryMap(request);
        Object defId=params.get("defId");
        Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
        for (String key : queryParams.keySet())
        {
            params.put(key, queryParams.get(key));
        }
        String html = pendingTaskTemplateService.pareTemplateMatters(params);
        if (StringUtil.isEmpty(html))
        {
            mv = new ModelAndView("redirect:/oa/flow/task/pendingMattersList.do?defId=" + defId);
            return mv;
        }
        if (ajax)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success", true);
            map.put("html", html);
            response.getWriter().print(JSONObject.toJSON(map));
            return null;
        }
        else
        {
            return mv.addObject("html", html);
        }
        
    }
    
    /**
     * @Title: pendingMattersView
     * @Description: TODO(待办任务详细页面)
     * @param @param request
     * @param @param response
     * @param @return 设定文件
     * @return ModelAndView 返回类型
     * @throws
     */
    @RequestMapping("mattersView")
    @ResponseBody
    public Map<String, Object> mattersView(HttpServletRequest request, HttpServletResponse response)
    {
        Map<String, Object> result=new HashMap<String,Object>();

        try
        {
            //ModelAndView mv = this.getView(category, "userMattersView");
            // 取得当前页URL,如有参数则带参数
            Map<String, Object> params = RequestUtil.getQueryMap(request);
            String parmaId = params.get("paramid").toString();
            String runIdKey = parmaId.split(":")[0];
            String runIdValue = parmaId.split(":")[1];

            params.clear();
            params.put(runIdKey, runIdValue);
            String html = "";
            if ("runId".equals(runIdKey))
            {
                html = alreadyTaskTemplateService.pareTemplateMattersView(params);
            }
            else if ("taskId".equals(runIdKey))
            {
                html = pendingTaskTemplateService.pareTemplateMattersView(params);
            }
            if(StringUtil.isEmpty(html)) {
                result.put("result", false);
                return result;
            }else {
                result.put("result", true);
                result.put("data", html);
                return result;
            }
        }
        catch (Exception e)
        {
            result.put("result", false);
            return result;
        }
        
    }
    
    /**
     * @Title: pendingMattersView
     * @Description: TODO(待办任务详细页面)
     * @param @param request
     * @param @param response
     * @param @return 设定文件
     * @return ModelAndView 返回类型
     * @throws
     */
    @RequestMapping("pendingMattersView")
    public ModelAndView pendingMattersView(HttpServletRequest request, HttpServletResponse response)
    {
        
        ModelAndView mv = this.getView(category, "userMattersView");
        // 取得当前页URL,如有参数则带参数
        Map<String, Object> params = RequestUtil.getQueryMap(request);
        Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
        for (String key : queryParams.keySet())
        {
            params.put(key, queryParams.get(key));
        }
        // 获取用户
        // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
        String html = pendingTaskTemplateService.pareTemplateMattersView(params);
        return mv.addObject("html", html);
    }
    
    /**
     * @Title: alreadyMattersView
     * @Description: TODO(已办任务详细页面)
     * @param @param request
     * @param @param response
     * @param @return 设定文件
     * @return ModelAndView 返回类型
     * @throws
     */
    @RequestMapping("alreadyMattersView")
    public ModelAndView alreadyMattersView(HttpServletRequest request, HttpServletResponse response)
    {
        
        ModelAndView mv = this.getView(category, "userMattersView");
        // 取得当前页URL,如有参数则带参数
        Map<String, Object> params = RequestUtil.getQueryMap(request);
        Map<String, Object> queryParams = RequestUtil.getQueryParams(request);
        for (String key : queryParams.keySet())
        {
            params.put(key, queryParams.get(key));
        }
        // 获取用户
        // 当不是rpc远程接口 或者 远程调用超时失败，从本地调用
        String html = alreadyTaskTemplateService.pareTemplateMattersView(params);
        return mv.addObject("html", html);
    }
}
