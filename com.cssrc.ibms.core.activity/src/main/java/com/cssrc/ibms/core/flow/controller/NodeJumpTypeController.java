package com.cssrc.ibms.core.flow.controller;

import java.util.HashMap;
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
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * NodeSqlController
 * 
 * @author liubo
 * @date 2017年2月16日
 */
@Controller
@RequestMapping({"/oa/flow/nodeJumpType/"})
public class NodeJumpTypeController extends BaseController
{
    @Resource
    private NodeSetService bpmNodeSetService;
    
    @RequestMapping({"save"})
    @Action(description = "更新nodeJumpType")
    public void save(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        try{
            String setId=request.getParameter("setId");
            String jumpSetting=request.getParameter("jumpSetting");
            bpmNodeSetService.updateNodeJumpSetting(setId,jumpSetting);
            this.writeResultMessage(response.getWriter(), "保存成功", ResultMessage.Success);
        }catch(Exception e){
            this.writeResultMessage(response.getWriter(),e.getMessage(), ResultMessage.Fail);
        }
       
    }
    
    @RequestMapping({"edit"})
    @Action(description = "编辑nodeJumpType")
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv=getAutoView();

        String nodeId=request.getParameter("nodeId");
        String actdefId=request.getParameter("actdefId");
        NodeSet nodeSet= bpmNodeSetService.getByDefIdNodeId(Long.valueOf(actdefId), nodeId);

        String returnUrl = RequestUtil.getPrePage(request);
        mv.addObject("nodeId", nodeId);
        mv.addObject("actdefId", actdefId);
        mv.addObject("setId", nodeSet.getSetId());
        return mv.addObject("returnUrl", returnUrl);
    }
    
    
    @RequestMapping({"getData"})
    @Action(description = "获取节点数据")
    @ResponseBody
    public Object getData(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Map<String,Object> result=new HashMap<String,Object>();
        String nodeId=request.getParameter("nodeId");
        String actdefId=request.getParameter("actdefId");
        Object jumpTypeNodeList=bpmNodeSetService.getJumpTypeNodeList(actdefId, nodeId);
        result.put("Rows", jumpTypeNodeList);
        return result;
    }
    
}
