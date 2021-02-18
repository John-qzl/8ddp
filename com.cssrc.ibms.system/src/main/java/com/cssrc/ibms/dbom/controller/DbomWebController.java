package com.cssrc.ibms.dbom.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dbom.model.DBom;
import com.cssrc.ibms.dbom.service.DBomNodeService;
import com.cssrc.ibms.dbom.service.DbomMetaDataService;

@Controller
@RequestMapping("/oa/system/dbomNode/")
public class DbomWebController extends BaseController
{
    @Resource
    DBomNodeService dbomNodeService;
    @Resource
    DbomMetaDataService dbomMetaDataService;
    
    /**
     * 前端dbom 引用页面
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @Action(description = "DBom前台-引用界面")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String code = RequestUtil.getString(request, "__dbomCode__");
        ModelAndView modelView = new ModelAndView("oa/dbom/dbomNodeList.jsp");
        DBom dbom = dbomMetaDataService.getDbomByCode(code);
        modelView.addObject("dbomCode", code);
        modelView.addObject("dbomName", dbom.getName());
        return modelView;
    }
    
    /**
     * 根据 dbom meta 元数据 生成 业务数据 tree
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getTree")
    @ResponseBody
    @Action(description = "DBom前台-树节点数据加载")
    public List<Map<String,Object>> getTree(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String treeCode = RequestUtil.getString(request, "treeCode");
        String curcode = RequestUtil.getString(request, "curcode");
        String curId = RequestUtil.getString(request, "curId");
        String customFilter = RequestUtil.getString(request, "customFilter");

        if(StringUtil.isEmpty(curcode)&&StringUtil.isEmpty(curId)){
            //获取dbom tree root data and fisrt node data
            List<Map<String,Object>> list = dbomNodeService.getDbomRootNodeData(treeCode,customFilter);
            return list;
        }else{
            //获取当前节点子节点 data
            List<Map<String,Object>> list = dbomNodeService.getDbomSubNodeData(curcode, curId,customFilter);
            return list;

        }
        
    }
    
    /**
     * node 点击 目标url
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getTab")
    @ResponseBody
    @Action(description = "DBom前台-动态加载Tab")
    public JSONObject getTab(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String code = RequestUtil.getString(request, "code");
        return dbomNodeService.getTab(code);
    }
    
    /**
     * 根据当前节点获取父节点所有相关过滤条件
     * 
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("getURLParams")
    @ResponseBody
    @Action(description = "DBom前台-数据过滤，获取URL过滤参数")
    public Map<String,String> getURLParams(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        String curCode = RequestUtil.getString(request, "code");
        String curId = RequestUtil.getString(request, "value");
        String urlIndex = RequestUtil.getString(request, "urlIndex");
        return dbomNodeService.getURLParams(curCode, curId,urlIndex);
    }
}
