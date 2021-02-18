package com.cssrc.ibms.core.flow.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * NodeSqlController
 * 
 * @author liubo
 * @date 2017年2月16日
 */
@Controller
@RequestMapping({"/oa/flow/userLable/"})
public class NodeUserLableController extends BaseController
{
    @Resource
    private NodeSetService bpmNodeSetService;
    
    @RequestMapping({"save"})
    @Action(description = "添加或更新userLable")
    public void save(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        try{
            String setId=request.getParameter("setId");
            String tip=request.getParameter("tip");
            String userlabel=request.getParameter("userlabel");
            JSONObject json=new JSONObject();
            json.put("tip", tip);
            json.put("userlabel", userlabel);
            bpmNodeSetService.updateUserLabel(setId,json.toJSONString());
            this.writeResultMessage(response.getWriter(), "保存成功", ResultMessage.Success);
        }catch(Exception e){
            this.writeResultMessage(response.getWriter(),e.getMessage(), ResultMessage.Fail);

        }
       
    }
    
    @RequestMapping({"edit"})
    @Action(description = "编辑userLable")
    public ModelAndView edit(HttpServletRequest request)
        throws Exception
    {
        ModelAndView mv=getAutoView();
        String nodeId=request.getParameter("nodeId");
        String actdefId=request.getParameter("actdefId");
        String returnUrl = RequestUtil.getPrePage(request);
        
        NodeSet nodeSet =bpmNodeSetService.getByActDefIdNodeId(actdefId, nodeId);
        String userlabel=nodeSet.getUserLabel();
        if(StringUtil.isNotEmpty(userlabel)){
            mv.addObject("userlabel", JSONObject.parse(userlabel));
        }
        mv.addObject("setId", nodeSet.getSetId());
        return mv.addObject("returnUrl", returnUrl);
    }
    
}
